var deletedNodeUUID = null;

function onDeleteDomainClick() {
	if (!currentDomainUUID){
		alert("Please select a domain");
		return;
	}

	var wsUrl = ratURL + "/runcommand?sessionid=" + loginResult.sessionID;
	//console.log("url: " + wsUrl);
	var node = $("#userDomainsTree").tree('getNodeById', currentDomainUUID);
	var parentNode = node.parent;
	if(parentNode == null){
		return;
	}

	var parentNodeUUID = parentNode.id;
	console.log("parentNodeName: " + parentNode.name);
	console.log("parentNodeName: " + node.name);
	deletedNodeUUID = currentDomainUUID;
	var json = deleteDomainFunc(currentDomainUUID, currentDomainUUID, parentNodeUUID);
	console.log("deleteDomainFunc json: " + json);
	callWsSimple(wsUrl, 'POST', json, deleteDomainCallBack, errorCallBack);
}

function deleteDomainCallBack(data, textStatus, jqXHR) {
	var json = JSON.parse(data);
	console.log("deleteDomainCallBack data: " + data);
	if(jqXHR.status == 200){
		removeAllComments();
		resetSubCommentStack();
		clearAllOptionsFromCombo("comboUrl");
		document.getElementById("currentDomain").innerHTML = "";

		if(deletedNodeUUID != null){
			var node = $("#userDomainsTree").tree('getNodeById', deletedNodeUUID);
			$("#userDomainsTree").tree('removeNode', node);
			deletedNodeUUID = null;
		}

		var wsUrl = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
		var getAllUserDomains = getAllUserDomainsFunc("null", loginResult.userUUID);
		callWs(wsUrl, 'POST', getAllUserDomains, getAllUserDomainsCallBack, errorCallBack);
	}
	else{
		console.log("deleteDomainCallBack status Error: " + jqXHR.status);
	}
}

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
	$("#domainName").val('');
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
	var json = addNewDomainFunc(currentDomainUUID, currentDomainUUID, domain, domain);
	console.log("onAddNewDomainClick json: " + json);
	callWs(wsUrl, 'POST', json, addNewDomainCallBack, errorCallBack);
}

function addNewDomainCallBack(data, textStatus, jqXHR) {
	var json = JSON.parse(data);
	//console.log("addNewDomainCallBack data: " + data);
	var newUUID = json.settings.VertexUUIDField;
	//console.log("addNewDomainCallBack newUUID: " + newUUID);

	var wsUrl = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
	//console.log("url: " + wsUrl);
	var json = getDomainDomainsFunc(newUUID, newUUID);
	callWs(wsUrl, "POST", json, getUserDomainsCallBack, errorCallBack);
}

function getUserDomainsCallBack(data, textStatus, jqXHR) {
	//console.log("getUserDomainsCallBack: " + data);
	if(data.indexOf("nodes") > -1){
		var json = JSON.parse(data);
		if(json.header.commandName == "GetAllDomainComments"){
			// TODO: da capire perché succede
			console.log("#ERROR getUserDomains: GetAllDomainComments");
			return;
		}

		renderGraph('domains', json);

		var jsonNodes = json.nodes;
		for(var i in jsonNodes){
			var vertexTypeField = jsonNodes[i].VertexTypeField;
			//console.log("populateDomainTree vertexTypeField: " + vertexTypeField);

			if(vertexTypeField == 'Domain'){
				var domainUUID = jsonNodes[i].VertexUUIDField;
				var domainName = jsonNodes[i].VertexLabelField;
				//console.log("populateDomainTree vertexUUIDField: " + domainUUID);
				//console.log("populateDomainTree domainName: " + domainName);
				append(currentDomainUUID, domainName, domainUUID);
			}
		}
	}

	removeAllComments();

	if(currentURL){
		getAllComments(currentURL);
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

