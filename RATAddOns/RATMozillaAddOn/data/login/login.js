var ws = require('../ws');
//var ss = require("sdk/simple-storage");
var simplePrefs = require("sdk/simple-prefs");
var self = require("sdk/self");
var actionButtonMenu = require('../actionButtonMenu');

function getStartDomains(response){
	var loginResult = {
		sessionID: null,
		userUUID: null,
		responseText: null,
		status: null
	};

	try {
		//var status = response.status;
		//ss.storage.lastStatus = status;
		
		loginResult.status = response.status;

		if(loginResult.status == 200){
			loginResult.sessionID = response.json.sessionID;
			loginResult.userUUID = response.json.userUUID;
			loginResult.responseText = response.text;
		}
		else{
			console.log("status Error: " + loginResult.status);
		}
	}
	catch (e) {
		loginResult.status = 500;
		console.log("Error: " + e);
	}

	actionButtonMenu.loginResult(loginResult);
}

function login(url, email, password){
	var json = {email:email, password:password};
	ws.post(url + "/v0.1/login", JSON.stringify(json), getStartDomains);
}
/*
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
*/

var panel = require("sdk/panel").Panel({
	contentURL: self.data.url("./login/loginWnd.html"),
	contentScriptFile: self.data.url("./login/loginWnd.js"),
	height: 400,
	width: 400
});

panel.port.on("hide", function () {
	panel.hide();
});

panel.port.on("login", function (loginData) {
	login(loginData.url, loginData.userEmail, loginData.userPwd);
});

function openWindow() {
	var loginData = {
		url: simplePrefs.prefs.ratURL,
		userEmail: simplePrefs.prefs.userEmail,
		userPwd: simplePrefs.prefs.userPassword
	};

	panel.show();
	panel.port.emit('loginData', loginData);
}

exports.openWindow = openWindow;
