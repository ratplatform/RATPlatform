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
	console.log("comments-sidepanel: " + json);
	var url = simplePrefs.prefs.ratURL;
	var sessionID = ss.storage.sessionID;

	commentsSidePanel = sidebar.Sidebar({
		id: 'comments-sidepanel',
		title: 'Comments',
		url: data.url("commentsSidePanel.html"),
		contentScriptFile: data.url('./comments.js'),
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

					break;

					case 'getuserdomains':

					break;
				}
			});
		}
	});

	commentsSidePanel.show();
}

function detachWorker(worker) {
	console.log("detachWorker worker: " + worker);
	var index = workerArray.indexOf(worker);
	if(index != -1) {
		workerArray.splice(index, 1);
	}
}

function hideSidebar(){
	commentsSidePanel.dispose();
}


exports.openSidePanel = openSidePanel;
//exports.addNewDomainCallBak = addNewDomainCallBak;
