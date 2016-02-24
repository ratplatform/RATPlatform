var ratURL = "http://localhost:8080/RATWSServer/rat/v0.1";
var userEmail = "dgr@gmail.com";
var userPassword = "dgr";

var loginResult = {
	sessionID: null,
	userUUID: null,
	email: null,
	userDomains: null,
	status: null
};

var currentDomainUUID;
var currentURL = '*';
var userUUID;

$(document).ready(function() {
	console.log("domainsSidePanel.html: ready");

	$('#url').val(ratURL);
	$('#userID').val(userEmail);
	$('#password').val(userPassword);

	$("#login").prop("disabled", false);
	$("#logout").prop('disabled', true);
	$("#addNewDomain").prop('disabled', true);
	$("#addComment").prop("disabled", true);
	$("#delComment").prop("disabled", true);

	$("#userDomainsTree").bind('tree.click', onUserDomainsTreeClick);
	$("#addNewDomain").bind('click', onAddNewDomainClick);

	$("#login").click(function(){
		showPopup();
	});

	$("#addComment").click(function(){
		showCommentWnd();
	});

	$("#saveComment").bind('click', onSaveComment);
	$("#closeComment").bind('click', onCloseComment);

	$("#dologin").click(function(){
		var url = $('#url').val();
		var login = $('#userID').val();
		var password = $('#password').val();

		ratLogin(url, login, password, loginResultCallBack, errorCallBack)

		hidePopup();
	});
});

function onUserDomainsTreeClick(event){
	if(!loginResult.sessionID){
		alert("Please login");
		return;
	}

	var node = event.node;
	document.getElementById("currentDomain").innerHTML = node.name;
	currentDomainUUID = node.id;
	console.log("currentDomainUUID: " + currentDomainUUID);
	if(currentDomainUUID || currentDomainUUID.length > 0){
		$("#addNewDomain").prop('disabled', false);
		enableAddComment();

		GetAllDomainsSet(currentDomainUUID, currentDomainUUID);
		var wsURL = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
		var getAllDomainsQuery = JSON.stringify(GetAllDomains);
		console.log("onUserDomainsTreeClick: " + getAllDomainsQuery);

		callWs(wsURL, 'POST', getAllDomainsQuery, getUserDomainsCallBack, errorCallBack);

		//removeAllComments();

/*
		if(currentURL){
			GetAllDomainCommentsSet(currentDomainUUID, currentDomainUUID, userUUID, currentURL);
			console.log("GetAllDomainComments: " + JSON.stringify(GetAllDomainComments));
			var request2 = new Request();
			request2.sendRequest(wsURL, "POST", JSON.stringify(GetAllDomainComments), getDomainAndUserComments, errorGetUserDomains);
		}
*/

	}
}

function enableAddComment(){
	if(currentDomainUUID && currentURL){
		$("#addComment").prop("disabled", false);
	}
	else{
		$("#addComment").prop("disabled", true);
	}
}

function populateDomainTree(userDomains){
	var data = userDomains.nodes;
	var treeData = [];
	var ii = 0;
	for(var i in data){
		var vertexTypeField = data[i].VertexTypeField;
		console.log("populateDomainTree vertexTypeField: " + vertexTypeField);

		if(vertexTypeField == 'Domain'){
			var vertexUUIDField = data[i].VertexUUIDField;
			var domainName = data[i].domainName;
			treeData[ii] = {label: domainName, id: vertexUUIDField};
			ii++;
			console.log("populateDomainTree vertexUUIDField: " + vertexUUIDField);
			console.log("populateDomainTree domainName: " + domainName);
		}
	}

	$("#userDomainsTree").tree({
		data: treeData,
		autoOpen: 1
	});
}



