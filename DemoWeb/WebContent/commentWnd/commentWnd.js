var ownerUUID;

function showCommentWnd(title, text, url, uuid){
	if(!url || url.length < 1){
		alert('Please select url');
		return;
	}

	if(!uuid || uuid.length < 1){
		alert('Owner UUID ERROR!');
		return;
	}

	ownerUUID = uuid;

	$('#commenttitle').val(title);
	$('#commentedit-box').val(text);
	$('#commenturl').val(url);

	$("#commentWnd").fadeIn();
	$("#commentWnd").css({"visibility":"visible","display":"block"});  
}

function hideCommentWnd(){
   $("#commentWnd").fadeOut();
   $("#commentWnd").css({"visibility":"hidden","display":"none"});
}

function onSaveComment(){
	var titleText = $('#commenttitle').val();
	if (!titleText){
		alert("Please add a title");
		return;
	}

	titleText = titleText.trim();
	$('#commenttitle').val(titleText);
	var n = titleText.length;
	if (n == 0){
		alert("Please insert a valid title");
		return;
	}

	var letters = /^[0-9a-zA-Z_\s\-\.\:]+$/;
	if(!titleText.match(letters)) {
		alert("Please insert a valid title; permitted characters are alphanumeric characters, dot, underscore and minus sign");
		return;
	}

	var expression = /[-a-zA-Z0-9@:%_\+.~#?&//=]{2,256}\.[a-z]{2,4}\b(\/[-a-zA-Z0-9@:%_\+.~#?&//=]*)?/gi;
	var regex = new RegExp(expression);
	var url = $('#commenturl').val();
	url = url.trim();
	$('#commenturl').val(url);
	if(!url.match(regex)) {
		alert("Please insert a valid URL");
		return;
	}

	var text = $('#commentedit-box').val();
	text = text.trim();
	$('#commentedit-box').val(text);
	n = text.length;
	if (n == 0){
		alert("Please insert a valid text");
		return;
	}

	var commentsContainer = document.getElementById("commentsContainer");
	commentsContainer.innerHTML = "";

	addNewComment(titleText, text, url);
	setUrlCookie(url);

	onCloseComment();
}

function onCloseComment(){
	$('#commenturl').val('');
	$('#commenttitle').val('');
	$('#commentedit-box').val('');

	hideCommentWnd();
}

function addNewComment(titleText, text, url){
	var wsUrl = ratURL + "/runcommand?sessionid=" + loginResult.sessionID;
//addCommentFunc = function(currentDomainUUID, userNodeUUID, jsonCoordinates, ownerNodeUUID, url, vertexContentField, vertexLabelField){
	var json = addCommentFunc(currentDomainUUID, loginResult.userUUID, "{JSON selectionData}", ownerUUID, url, text, titleText);
	callWs(wsUrl, 'POST', json, addNewCommentCallBack, errorCallBack);
/*
	AddCommentSet(currentDomainUUID, "{JSON selectionData}", titleText, text, url, loginResult.userUUID, ownerUUID);

	console.log("AddComment: " + JSON.stringify(AddComment));
	callWs(wsUrl, 'POST', JSON.stringify(AddComment), addNewCommentCallBack, errorCallBack);
*/
}

function onComboWndUrlChange() {
    var optionSelected = $("option:selected", this);
    var valueSelected = this.value;
    $('#commenturl').val(valueSelected);
}




