var stack;

function resetSubCommentStack(){
	var selectBox = document.getElementById("breadcrumb");
	selectBox.innerHTML = "";
	stack = new Array();
}

function getSubComments(uuid, title){
	if(stack.length < 1){
		var domainName = document.getElementById("currentDomain").innerHTML;

		var stackData = {
			uuid: currentDomainUUID,
			label: domainName
		};
		stack.push(stackData);
	}

	var stackData = {
		uuid: uuid,
		label: title
	};
	stack.push(stackData);

	//GetCommentCommentsSet(currentDomainUUID, uuid);
	//console.log("getSubComments: " + JSON.stringify(GetCommentComments));
	var wsUrl = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
	//callWs(wsUrl, 'POST', JSON.stringify(GetCommentComments), addSubCommentsCallBak, errorCallBack);
	var json = getCommentCommentsFunc(currentDomainUUID, uuid);
	callWs(wsUrl, 'POST', json, addSubCommentsCallBak, errorCallBack);
}

function addSubCommentsCallBak(data, textStatus, jqXHR){
	console.log("addSubCommentsCallBak: " + data);

	if(data.indexOf("nodes") > -1){
		removeAllComments();

		var selectBox = document.getElementById("breadcrumb");
		selectBox.innerHTML = "";

		for(var i = 0; i < stack.length; i++){
			var stackData = stack[i];
			if(i == stack.length - 1){
				$('#breadcrumb').append('<option selected="true" value="' + stackData.uuid + '">' + stackData.label + '</option');
			}
			else{
				$('#breadcrumb').append('<option value="' + stackData.uuid + '">' + stackData.label + '</option');
			}
		}

		var json = JSON.parse(data);
		renderGraph('comments', json);

		var nodes = [];
		var i = 0;
		var jsonNodes = json.nodes;

		addSubNodes(jsonNodes, json.header.RootVertexUUID);
	}
}

