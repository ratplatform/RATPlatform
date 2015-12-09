var self = require('sdk/self');
var buttons = require('sdk/ui/button/action');
var tabs = require("sdk/tabs");

// dgr
//var simplePrefs = require("sdk/simple-prefs");
//var Request = require("sdk/request").Request;
var ss = require("sdk/simple-storage");
//const {XMLHttpRequest} = require("sdk/net/xhr");
var data = require("sdk/self").data;
var pageMod = require("sdk/page-mod");
var actionButtonMenu = require('./data/actionButtonMenu');
var ss = require("sdk/simple-storage");
// end dgr

//var workers = require("sdk/content/worker");

console.log("Start plugin");

delete ss.storage.sessionID;
delete ss.storage.userUUID;
delete ss.storage.lastResponseText;
delete ss.storage.lastStatus;

//var sessionID;
//var userUUID;
/*
var button = buttons.ActionButton({
  id: "mozilla-link",
  label: "Visit Mozilla",
  icon: {
    "16": "./icon-16.png",
    "32": "./icon-32.png",
    "64": "./icon-64.png"
  },
  onClick: handleClick
});


function handleClick(state) {
	var url = simplePrefs.prefs.ratURL;
	var userEmail = simplePrefs.prefs.userEmail;
	var userPwd = simplePrefs.prefs.userPassword;

	connect(url, userEmail, userPwd);
}

function connect(url, email, password){
	var json = {email:email, password:password};
	if (!sessionID){
		post(url + "/login", JSON.stringify(json), getStartDomains);
	}
	else{
		console.log("sessionID: " + sessionID);
		console.log("userUUID: " + userUUID);
		console.log("Already logged in!");
	}
}

function getStartDomains(response){
	try {
		var status = 0;
		status = response.status;
		sessionID = response.json.sessionID;
		userUUID = response.json.userUUID;
		if(status == 200){
			openSideBar(response.text);
		}
		else{
			console.log("status Error: " + status);
		}
	}
	catch (e) {
		console.log("Error: " + e);
	}
}
*/
/*
function post(url, jsonCommand, callback){
	var post = Request({
		url: url,
		content: jsonCommand,
		contentType: "text/plain",
		overrideMimeType: "text/plain; charset=latin1",
		onComplete: callback
	});

	post.post();
}
*/
var workerArray = [];

function openSideBar(json){
	//console.log("openSideBar");
	var sidebar = require("sdk/ui/sidebar").Sidebar({
		id: 'my-sidebar',
		title: 'My sidebar',
		url: require("sdk/self").data.url("domainSideBar.html"),
		onDetach: detachWorker,
		onReady: function (worker) {
			workerArray.push(worker);
			worker.port.emit("onReceived", json);
			worker.port.on("onDomainNameReceived", function(domainName) {
				console.log("index.js onDomainNameReceived domainName: " + domainName);
			});
		}
	});

	sidebar.show();

	console.log("sidebar: " + sidebar);
}

function detachWorker(worker) {
	console.log("detachWorker worker: " + worker);
	var index = workerArray.indexOf(worker);
	if(index != -1) {
		workerArray.splice(index, 1);
	}
}

actionButtonMenu.init();
