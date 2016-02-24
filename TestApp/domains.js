function onAddNewDomainClick() {
	if (!currentDomainUUID){
		alert("Please select a domain");
		return;
	}

	//console.log("onAddNewDomainClick currentDomainUUID: " + currentDomainUUID);
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

	var wsUrl = ratURL + "/runcommand?sessionid=" + loginResult.sessionID;
	//console.log("url: " + wsUrl);
	AddNewDomainSet(currentDomainUUID, domain, domain, currentDomainUUID);
	//console.log("onAddNewDomainClick AddNewDomain: " + JSON.stringify(AddNewDomain));
	callWs(wsUrl, 'POST', JSON.stringify(AddNewDomain), addNewDomainCallBack, errorCallBack);
}

function addNewDomainCallBack(data, textStatus, jqXHR) {
	var json = JSON.parse(data);
	//console.log("addNewDomainCallBack data: " + data);
	var newUUID = json.settings.VertexUUIDField;
	console.log("addNewDomainCallBack newUUID: " + newUUID);

	GetAllDomainsSet(newUUID, newUUID);
	var wsUrl = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
	//console.log("url: " + wsUrl);
	callWs(wsUrl, "POST", JSON.stringify(GetAllDomains), getUserDomainsCallBack, errorCallBack);
}

function getUserDomainsCallBack(data, textStatus, jqXHR) {
	console.log("getUserDomainsCallBack: " + data);
	if(data.indexOf("nodes") > -1){
		var json = JSON.parse(data);
		if(json.header.commandName == "GetAllDomainComments"){
			// TODO: da capire perché succede
			console.log("#ERROR getUserDomains: GetAllDomainComments");
			return;
		}

		alchemyConfig.dataSource = json;
		var alchemy = new Alchemy(alchemyConfig);

		var jsonNodes = json.nodes;
		for(var i in jsonNodes){
			var vertexTypeField = jsonNodes[i].VertexTypeField;
			console.log("populateDomainTree vertexTypeField: " + vertexTypeField);

			if(vertexTypeField == 'Domain'){
				var domainUUID = jsonNodes[i].VertexUUIDField;
				var domainName = jsonNodes[i].domainName;
				//console.log("populateDomainTree vertexUUIDField: " + domainUUID);
				//console.log("populateDomainTree domainName: " + domainName);
				append(currentDomainUUID, domainName, domainUUID);
			}
		}
	}
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

