var self = require('sdk/self');
var buttons = require('sdk/ui/button/action');
var tabs = require("sdk/tabs");

// dgr
var simplePrefs = require("sdk/simple-prefs");
var Request = require("sdk/request").Request;
var ss = require("sdk/simple-storage");
const {XMLHttpRequest} = require("sdk/net/xhr");
var data = require("sdk/self").data;
var pageMod = require("sdk/page-mod");
// end dgr

//var workers = require("sdk/content/worker");

var sessionID;
var userUUID;

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

/*
workers.port.on("myMessage", function(text) {
console.log("myMessage: " + text);
});
*/

function handleClick(state) {
	var url = simplePrefs.prefs.ratURL;
	var userEmail = simplePrefs.prefs.userEmail;
	var userPwd = simplePrefs.prefs.userPassword;

	//console.log(url);
	//console.log(userName);
	//console.log(userPwd);

	connect(url, userEmail, userPwd);

  //tabs.open("http://www.mozilla.org/");
}

function connect(url, email, password){
	var json = {email:email, password:password};
	//console.log( JSON.stringify(json));
	//console.log(url + "/login");
	
// TODO: togliere sessionID e userUUID da simple-storage e metterli in variabili globali
	//var sessionID = ss.storage.sessionID;
	//var userUUID = ss.storage.userUUID;
	
//"sessionID":"f7cf435a-116f-4faa-8ec4-90d91b62e12b","email":"dgr@gmail.com","userDomains":{},"userUUID":"ad6933c1-0d9f-47aa-b35e-42cb54269d7d"
	if (!sessionID){
		var quijote = Request({
			url: url + "/login",
			content: JSON.stringify(json),
			contentType: "text/plain",
			overrideMimeType: "text/plain; charset=latin1",
			onComplete: function (response) {
				try {
					var status = 0;
					//console.log("response.text" + response.text);
					//console.log(JSON.stringify(response.json));
					//console.log(response.status);
					status = response.status;
					//ss.storage.sessionID = response.json.sessionID;
					//ss.storage.userUUID = response.json.userUUID;
					sessionID = response.json.sessionID;
					userUUID = response.json.userUUID;
					//console.log("sessionID: " + sessionID);
					//console.log("userUUID: " + userUUID);
					//console.log("status: " + status);
					if(status == 200){
						openSideBar(response.text);
					}
				}
				catch (e) {
					console.log("Error: " + e);
				}
			}
		});

		quijote.post();
	}
	else{
		console.log("sessionID: " + sessionID);
		console.log("userUUID: " + userUUID);
		console.log("Already logged in!");
	}


/*
var request = new XMLHttpRequest();
request.open("POST", url + "/login");
request.setRequestHeader("Content-Type", "text/plain");
request.overrideMimeType("text/plain");
request.onload = function()
{
    console.log("Response received: " + request.responseText);
};
request.send(JSON.stringify(json));
*/
}

function post(wsName, jsonCommand, callback){
	var post = Request({
		url: url + "/" + wsName,
		content: JSON.stringify(jsonCommand),
		contentType: "text/plain",
		overrideMimeType: "text/plain; charset=latin1",
		onComplete: callback
	});

	post.post();
}

function openSideBar(json){
	//console.log("openSideBar");
	var sidebar = require("sdk/ui/sidebar").Sidebar({
		id: 'my-sidebar',
		title: 'My sidebar',
		url: require("sdk/self").data.url("domainSideBar.html"),
		onReady: function (worker) {
			worker.port.emit("onReceived", json);
			worker.port.on("onDomainNameReceived", function(domainName) {
				console.log("index.js onDomainNameReceived domainName: " + domainName);
			});
		}
	});

	sidebar.show();

	console.log("sidebar: " + sidebar);
}

/*
function openSideBar(json){
	//console.log("openSideBar");
	var sidebar = require("sdk/ui/sidebar").Sidebar({
		id: 'my-sidebar',
		title: 'My sidebar',
		url: require("sdk/self").data.url("domainSideBar.html"),
		//contentScriptFile: data.url("listen.js"),
		//contentScriptWhen: "ready",
		onAttach: function (worker) {
			//console.log("onAttach");
			worker.port.on("ping", function() {
				//console.log("add-on script got the message");
				worker.port.emit("pong", json);
			});
		}
	});

	sidebar.show();

	console.log("sidebar: " + sidebar);
}
*/

