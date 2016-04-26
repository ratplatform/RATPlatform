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
var currentURL = null;
var userUUID;
var currentCommentJsonForGraph;
var currentDomainJsonForGraph;

$(document).ready(function() {
	console.log("index.html: ready");

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

	$('#comboWndUrl').bind('change', onComboWndUrlChange);
	$('#comboUrl').bind('change', onComboUrlChange);

	$("#login").click(function(){
		showPopup();
	});

	$("#addComment").click(function(){
		showCommentWnd('', '', getComboValue("comboUrl"), currentDomainUUID);
	});

	$("#saveComment").bind('click', onSaveComment);
	$("#closeComment").bind('click', onCloseComment);

	$("#commentsContainer").contextmenu({
		delegate: ".window",
		menu: [{title:"Delete", cmd: "delete"}, {title:"Reply", cmd: "reply"}],
		select: selectCommentMenuItem
	});

	$("#dologin").click(function(){
		var url = $('#url').val();
		var login = $('#userID').val();
		var password = $('#password').val();

		ratLogin(url, login, password, errorCallBack)

		hidePopup();
	});

	$("#breadcrumb").bind("change", onChange);

	addCombo("comboGraph", "Domains", "domains");
	addCombo("comboGraph", "Comments", "comments");
	$('#comboGraph').bind('change', onComboGraphChange);

});

function populateComboURLs(json){
	var data = json.nodes;
	console.log("populateComboURLs json: " + json);

	clearAllOptionsFromCombo("comboUrl");

	for(var i in data){
		var vertexTypeField = data[i].VertexTypeField;
		//console.log("populateComboURLs vertexTypeField: " + vertexTypeField);

		if(vertexTypeField == 'URI'){
			var vertexUUIDField = data[i].VertexUUIDField;
			var url = data[i].VertexLabelField;
			addCombo("comboUrl", url);
		}
	}
}

function onComboGraphChange(event) {
	var json;
	if(event.target.value == "domains"){
		json = currentDomainJsonForGraph;
	}
	else{
		json = currentCommentJsonForGraph;
	}

	renderGraph(event.target.value, json);
}

function renderGraph(type, json){
	if(!json){
		return;
	}

	var value = $("#comboGraph option:selected").val();

	if(value == type){
		alchemyConfig.dataSource = json;
		var alchemy = new Alchemy(alchemyConfig);
	}

	if(type == "domains"){
		currentDomainJsonForGraph = json;
	}
	else{
		currentCommentJsonForGraph = json;
	}
}

function onChange(event){
	//alert(event.target.value);
	var domainName = document.getElementById("currentDomain").innerHTML;
	var selection = event.target.options[event.target.selectedIndex].text;
	var uuid = event.target.value;

	var breadcrumb = document.getElementById("breadcrumb");
	breadcrumb.innerHTML = "";

	removeAllComments();
	resetSubCommentStack()

	if(domainName == selection){
		getAllComments(currentURL)
	}
	else{
		getSubComments(uuid, selection);
	}
}

function onUserDomainsTreeClick(event){
	if(!loginResult.sessionID){
		alert("Please login");
		return;
	}
	
	resetSubCommentStack();

	var node = event.node;
	document.getElementById("currentDomain").innerHTML = node.name;
	currentDomainUUID = node.id;
	//console.log("currentDomainUUID: " + currentDomainUUID);
	currentURL = getComboValue("comboUrl");//getComboUrl();

	if(currentDomainUUID || currentDomainUUID.length > 0){
		$("#addNewDomain").prop('disabled', false);
		enableAddComment();

		//GetAllDomainsSet(currentDomainUUID, currentDomainUUID);
		var wsURL = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
		//var getAllDomainsQuery = JSON.stringify(GetAllDomains);
		var getAllDomainsQuery = getDomainDomainsFunc(currentDomainUUID, currentDomainUUID);
		//console.log("onUserDomainsTreeClick: " + getAllDomainsQuery);

		callWs(wsURL, 'POST', getAllDomainsQuery, getUserDomainsCallBack, errorCallBack);
	}
}

/*
function getComboUrl() {
	var value = $("#comboUrl option:selected").val();
	return value;
}
*/
function onComboUrlChange() {
	//var optionSelected = $("option:selected", this);
	//currentURL = this.value;
	//console.log("onComboUrlChange currentURL: " + currentURL);
	currentURL = getComboValue("comboUrl");

	removeAllComments();
	getAllComments(currentURL);
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
		//console.log("populateDomainTree vertexTypeField: " + vertexTypeField);

		if(vertexTypeField == 'Domain'){
			var vertexUUIDField = data[i].VertexUUIDField;
			var domainName = data[i].domainName;
			treeData[ii] = {label: domainName, id: vertexUUIDField};
			ii++;
			//console.log("populateDomainTree vertexUUIDField: " + vertexUUIDField);
			//console.log("populateDomainTree domainName: " + domainName);
		}
	}

	$("#userDomainsTree").tree({
		data: treeData,
		autoOpen: 1
	});
}



