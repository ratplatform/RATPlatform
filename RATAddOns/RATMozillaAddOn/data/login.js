var ws = require('./ws');
var ss = require("sdk/simple-storage");

//var sessionID;
//var userUUID;
//var responseText;
//var status = 0;

/*
function getSessionID() {
	return sessionID;
}
function getUserUUID() {
	return userUUID;
}

function getResponseText(){
	return responseText;
}

function getStatus(){
	return status;
}
*/
function getStartDomains(response){
	try {
		var status = response.status;
		ss.storage.lastStatus = status;
		
		if(status == 200){
			ss.storage.sessionID = response.json.sessionID;
			ss.storage.userUUID = response.json.userUUID;
			//console.log("userUUID: " + userUUID);
			//console.log("sessionID: " + sessionID);
			ss.storage.lastResponseText = response.text;
			//console.log("responseText: " + responseText);
			//openSideBar(response.text);
		}
		else{
			console.log("status Error: " + ss.storage.lastStatus);
		}
	}
	catch (e) {
		console.log("Error: " + e);
	}
}

function login(url, email, password){
	var json = {email:email, password:password};
	if (!ss.storage.sessionID){
		ws.post(url + "/v0.1/login", JSON.stringify(json), getStartDomains);
	}
	else{
		console.log("sessionID: " + ss.storage.sessionID);
		console.log("userUUID: " + ss.storage.userUUID);
		console.log("Already logged in!");
	}
}

//exports.getResponseText = getResponseText;
exports.login = login;
//exports.getSessionID = getSessionID;
//exports.getUserUUID = getUserUUID;
//exports.getStatus = getStatus;
