var url;
var sessionID;
var currentDomainUUID;

addon.port.on("message", function(msgType, text) {
	switch (msgType){
		case 'data':
			var data = [];
			var obj = JSON.parse(text);
			var inc = 0;
			if(obj == null){
				return;
			}

			for(var domain in obj.userDomains){
				var domainUUID = obj.userDomains[domain];
				data[inc] = {label: domain, id: domainUUID,};
				inc++;
			}
			//console.log("pageScripts.js domainsSidePanel.html data: " + JSON.stringify(data));

			$("#userDomainsTree").tree({
				data: data,
				autoOpen: 1
			});
		break;

		case 'url':
			url = text;
			//console.log("pageScripts.js domainsSidePanel.html url: " + url);
		break;

		case 'sessionid':
			sessionID = text;
			//console.log("pageScripts.js domainsSidePanel.html sessionID: " + sessionID);
		break;
	}
});

function sendResponse(text, domainName, currentDomainUUID){
	addon.port.emit("message", text, domainName, currentDomainUUID);
}

function sendRequest(url, method, jsonText, successCallback, errorCallBack) {
	$.ajax({
		url: url,
		method: method,
		data: jsonText,
		dataType: 'text',
		contentType: "text/plain",
		success: successCallback,
		error: errorCallBack
	}); 
}

function getUserDomains(req) {
	//console.log("pageScripts.js domainsSidePanel.html getUserDomains: " + req);
	//console.log("pageScripts.js domainsSidePanel.html getUserDomains Response received: " + req.responseText.indexOf("nodes"));
	if(req.indexOf("nodes") > -1){
		var config = {dataSource : JSON.parse(req)};
		var alchemy = new Alchemy(config);
		//console.log("pageScripts.js domainsSidePanel.html getUserDomains alchemy.allNodes().length: " + alchemy.allNodes().length);
		//console.log("pageScripts.js domainsSidePanel.html getUserDomains currentNodeUUID: " + currentDomainUUID);
		var arrayLength = alchemy.allNodes().length;
		for (var i = 0; i < arrayLength; i++) {
		    var node = alchemy.allNodes()[i];//alchemy.getNodes(i);
			if(typeof node == 'undefined'){
				console.log("pageScripts.js domainsSidePanel.html getUserDomains node[0] typeof is undefined ");
				continue;
			}

			//console.log("pageScripts.js domainsSidePanel.html getUserDomains node[0] typeof: " + typeof node);
			//console.log("pageScripts.js domainsSidePanel.html getUserDomains node.length: " + node.length);
			var domainUUID = node.getProperties("VertexUUIDField");
			var domainName = node.getProperties("domainName");
			//console.log("pageScripts.js domainsSidePanel.html getUserDomains alchemy node domainName: " + domainName);
			//console.log("pageScripts.js domainsSidePanel.html getUserDomains alchemy node domainUUID: " + domainUUID);

			append(currentDomainUUID, domainName, domainUUID);
		}
	}
}

function errorGetUserDomains(jqXHR, textStatus, errorThrown){
	console.log("pageScripts.js domainsSidePanel.html errorGetUserDomains jqXHR: " + jqXHR);
	console.log("pageScripts.js domainsSidePanel.html errorGetUserDomains textStatus: " + textStatus);
}

function append(parentNodeID, childNodeName, childNodeID){
	var parentNode = $("#userDomainsTree").tree('getNodeById', parentNodeID);
	var child = $("#userDomainsTree").tree('getNodeById', childNodeID);
	//console.log("pageScripts.js currentDomainUUID append node child: " + child);
	if(!child){
		$("#userDomainsTree").tree('appendNode',{
			label: childNodeName,
			id: childNodeID
		}, parentNode);
	}
}

function addNewDomain(req) {
	//console.log("newDomain Response received: " + req);
	var json = JSON.parse(req);
	var newUUID = json.settings.VertexUUIDField;
	//console.log("newDomain Response received newUUID: " + newUUID);
	GetAllDomainsSet(newUUID, newUUID);
	//getAllDomains(newUUID, newUUID);
	//console.log("GetAllDomains: " + JSON.stringify(GetAllDomains));
	var wsUrl = url + "/v0.1/query?sessionid=" + sessionID;
	//console.log("url: " + wsUrl);
	sendRequest(wsUrl, "POST", JSON.stringify(GetAllDomains), getUserDomains, errorGetUserDomains);
}

$(document).ready(function() {
	console.log("domainsSidePanel.html: ready");
	$("#addNewDomain").prop('disabled', true);

	$("#userDomainsTree").bind('tree.click', function(event) {
		if(!sessionID){
			alert("Please login");
			return;
		}

		var node = event.node;
		//console.log(node.id);

		document.getElementById("currentDomain").innerHTML = "Domain selected: " + node.name;
		//document.getElementById("currentDomainUUID").value = node.id;
		//console.log("pageScripts.js currentDomainUUID: " + document.getElementById("currentDomainUUID").value);
		currentDomainUUID = node.id;
		console.log("domainSidePanel.html currentDomainUUID: " + currentDomainUUID);
		sendResponse("domainselected", node.name, currentDomainUUID);

		if(currentDomainUUID || currentDomainUUID.length > 0){
			$("#addNewDomain").prop('disabled', false);
			GetAllDomainsSet(node.id, node.id);
			//console.log("domainSidePanel.html GetAllDomains: " + JSON.stringify(GetAllDomains));

			var wsUrl = url + "/v0.1/query?sessionid=" + sessionID;
			//console.log("pageScripts.js url: " + wsUrl);
			sendRequest(wsUrl, "POST", JSON.stringify(GetAllDomains), getUserDomains, errorGetUserDomains);
		}
	});

	$("#addNewDomain").click(function() {
		//alert("click");
		//var currentDomainUUID = document.getElementById("currentDomainUUID").value;
		if (!currentDomainUUID){
			alert("Please select a domain");
			return;
		}

		console.log("domainSideBar.html currentDomainUUID: " + currentDomainUUID);
		var domain = $("#domainName").val();
		if (!domain){
			alert("Please add a domain name");
			return;
		}

		domain = domain.trim();
		$("#domainName").val(domain);
		var n = domain.length;
		if (n == 0){
			alert("Please insert a valid domain name");
			return;
		}

		if(!isValidText(domain)){
			alert("Please insert a valid domain name; permitted characters are alphanumeric characters, dot, underscore and minus sign");
			return;
		}

		//alert(domain);
		var wsUrl = url + "/v0.1/executeconfigurationcommand?sessionid=" + sessionID;
		console.log("url: " + wsUrl);
		AddNewDomainSet(currentDomainUUID, currentDomainUUID, domain, domain);
		//console.log("AddNewCommand: " + JSON.stringify(AddNewDomain));
		sendRequest(wsUrl, "POST", JSON.stringify(AddNewDomain), addNewDomain, errorGetUserDomains);
	});
});


