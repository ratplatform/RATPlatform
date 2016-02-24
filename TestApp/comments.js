function removeAllComments(){
	var children = $('.comments').children();
	//console.log("children: " + children.length); 
	for (var i = 0; i < children.length; i++) {
		var div = children[i];
		//console.log("div: " + div); 
		var id = $(div).attr('id');
		//console.log("div id: " + id); 
		$('#' + id).remove(); 
	}

	var commentsBox = document.getElementById("commentsContainer");
	commentsBox.innerHTML = "";
}

function addNewCommentCallBack(data, textStatus, jqXHR) {
	removeAllComments();

	console.log("addNewCommentCallBack: " + req);

	var json = JSON.parse(req);
	var newUUID = json.settings.VertexUUIDField;
	//console.log("newDomain Response received newUUID: " + newUUID);
	GetAllDomainCommentsSet(currentDomainUUID, currentDomainUUID, userUUID, currentURL);
	//getAllDomains(newUUID, newUUID);
	console.log("addCommentResponse: " + JSON.stringify(GetAllDomainComments));
	var wsUrl = ratUrl + "/v0.1/query?sessionid=" + sessionID;
	console.log("url: " + wsUrl);
	var request = new Request();
	request.sendRequest(wsUrl, "POST", JSON.stringify(GetAllDomainComments), getDomainAndUserComments, errorGetUserDomains);

	//console.log("comments.js addCommentResponse Response received: " + req.responseText.indexOf("nodes"));
}
