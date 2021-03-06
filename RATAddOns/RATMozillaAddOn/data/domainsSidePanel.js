var sidebar = require("sdk/ui/sidebar");
var ws = require('./ws');
var data = require('sdk/self').data;
//var commands = require('./ratCommands');
//var queries = require('./queries');
var simplePrefs = require("sdk/simple-prefs");
//var login = require('./login');
var ss = require("sdk/simple-storage");
var Request = require("sdk/request").Request;
//var alchemy = require("./alchemy.min");

var domainsSidePanel;
var workerArray = [];

function openSidePanel(json){
	console.log("openSideBar: " + json);
	var url = simplePrefs.prefs.ratURL;
	var sessionID = ss.storage.sessionID;

	domainsSidePanel = sidebar.Sidebar({
		id: 'domains-sidepanel',
		title: 'Domains',
		url: data.url("domainSideBar.html"),
		contentScriptFile: data.url('./userDomains.js'),
		onHide: hideSidebar,
		onDetach: detachWorker,
		onReady: function (worker) {
			workerArray.push(worker);
			worker.port.emit("message", 'data', json);
			worker.port.emit("message", 'url', url);
			worker.port.emit("message", 'sessionid', sessionID);

			worker.port.on("message", function(text, domainName, currentDomainUUID) {
				console.log("index.js onDomainNameReceived new domainName: " + domainName);
				console.log("index.js onDomainNameReceived current currentDomainUUID: " + currentDomainUUID);
				

				switch (text){
					case 'newdomain':
						//console.log("newdomain");
						//commands.addNewCommand(currentDomainUUID, currentDomainUUID, domainName, domainName);
						//console.log(JSON.stringify(commands.AddNewCommand));
						//console.log(sessionID);
						//var wsUrl = url + "/v0.1/executeconfigurationcommand?sessionid=" + sessionID;
						//console.log(wsUrl);
						//ws.post(url, commands.AddNewCommand, addNewDomainCallBak);
					break;

					case 'getuserdomains':
						//console.log("getuserdomains: " + currentDomainUUID);
						//queries.getAllDomains(currentDomainUUID, currentDomainUUID);
						//console.log(JSON.stringify(queries.GetAllDomains));
						//var url = simplePrefs.prefs.ratURL + "/v0.1/query?sessionid=" + sessionID;
						//console.log(url);
						//ws.post(url, JSON.stringify(queries.GetAllDomains), getUserDomainsCallBak);
					break;
				}
			});
		}
	});

	domainsSidePanel.show();
}
/*
function getUserDomainsCallBak(response){
	console.log("response: " + response);
	try {
		var status = 0;
		status = response.status;
		
		if(status == 200){
			console.log("response.text: " + response.text);
			//console.log("sessionID: " + sessionID);
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
function addNewDomainCallBak(response){
	console.log("response: " + response);
	try {
		var status = 0;
		status = response.status;
		
		if(status == 200){
			console.log("response.text: " + response.text);
			//console.log("sessionID: " + sessionID);
		}
		else{
			console.log("status Error: " + status);
		}
	}
	catch (e) {
		console.log("Error: " + e);
	}
}

function detachWorker(worker) {
	console.log("detachWorker worker: " + worker);
	var index = workerArray.indexOf(worker);
	if(index != -1) {
		workerArray.splice(index, 1);
	}
}

function hideSidebar(){
	domainsSidePanel.dispose();
}

function addNewDomain(response){
	try {
		var status = 0;
		status = response.status;
		
		if(status == 200){
			responseText = response.text;
			console.log("responseText: " + responseText);
		}
		else{
			console.log("status Error: " + status);
		}
	}
	catch (e) {
		console.log("Error: " + e);
	}
}

exports.openSidePanel = openSidePanel;
//exports.addNewDomainCallBak = addNewDomainCallBak;
