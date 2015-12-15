
var sessionID;
var currentDomainUUID;
var currentDomainName;
var ratUrl;
var userUUID;
var title;
var comment;
var currentURL;

addon.port.on("message", function(msgType, text) {
	console.log("commentsSidePanel.html msgType: " + msgType);
	console.log("commentsSidePanel.html text: " + text);

	switch (msgType){
		case 'currentdomainname':
			currentDomainName = text;
			$("#currentDomainName").html(text);
		break;

		case 'currentdomainuuid':
			currentDomainUUID = text;
			if(currentDomainUUID){
				$("#addComment").prop("disabled", false);
			}
			else{
				$("#addComment").prop("disabled", true);
			}
		break;

		case 'sessionid':
			sessionID = text;
		break;

		case 'raturl':
			ratUrl = text;
			//console.log("pageScripts.js domainsSidePanel.html ratUrl: " + ratUrl);
		break;

		case 'useruuid':
			userUUID = text;
		break;

		case 'title':
			title = text;
		break;

		case 'comment':
			comment = text;
			if(title){
				addNewComment(title, comment);
			}
			else{
				console.log("comments.js title is null");
				alert("title is null");
			}
		break;

		case 'currenturl':
			currentURL = text;
			if(currentDomainUUID && currentURL){
				$("#addComment").prop("disabled", false);
			}
			else{
				$("#addComment").prop("disabled", true);
			}
		break;
	}

});

function addNewComment(title, text){
	var wsUrl = ratUrl + "/v0.1/executeconfigurationcommand?sessionid=" + sessionID;
	AddCommentSet(currentDomainUUID, currentDomainUUID, userUUID, -1, -1, currentURL, text, title);
	console.log("AddComment: " + JSON.stringify(AddComment));
	sendRequest(wsUrl, "POST", JSON.stringify(AddComment), addCommentResponse, addCommentError);
}

function addCommentResponse(req) {
	console.log("comments.js addCommentResponse: " + req);
	console.log("comments.js addCommentResponse Response received: " + req.responseText.indexOf("nodes"));
}

function addCommentError(jqXHR, textStatus, errorThrown){
	console.log("comments.js errorGetUserDomains jqXHR: " + jqXHR);
	console.log("comments.js addCommentError textStatus: " + textStatus);
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

function sendResponse(text, domainName, domainUUID){
	addon.port.emit("message", text, domainName, domainUUID);
}

$(document).ready(function() {
	console.log("commentsSidePanel.html: ready");
	console.log("commentsSidePanel.html: currentDomainName: " + currentDomainName);

	$("#addComment").prop("disabled", true);


	$("#delComment").prop("disabled", true);
	$("#currentDomainName").html(currentDomainName);

	$("#addComment").click(function(){
		sendResponse("opencomment-dlg", currentDomainName, currentDomainUUID);
	}); 

	var urlFromWnd = window.top.getBrowser().selectedBrowser.contentWindow.location.href;
	console.log("comments.js ready urlFromWnd: " + urlFromWnd);
});

console.log("commentsSidePanel.html self: " + self);
self.port.on("getElements", function(msgType, text) {
	console.log("commentsSidePanel.html msgType: " + msgType);
	console.log("commentsSidePanel.html text: " + text);
});


