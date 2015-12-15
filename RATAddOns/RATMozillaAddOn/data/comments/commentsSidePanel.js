var sidebar = require("sdk/ui/sidebar");
var data = require('sdk/self').data;
var simplePrefs = require("sdk/simple-prefs");
var commentWnd = require('../comment-dlg/commentWnd');
var tabs = require("sdk/tabs");

var commentsSidePanel;
var workerArray = [];
var currentDomainName;
var currentDomainUUID;
var commentsSidePanelWorker;
var currentUserUUID;
var currentSessionID;
var currentURL = tabs.activeTab.url;
console.log("commentsSidePanel.js currentURL: " + currentURL);

function openSidePanel(sessionID, userUUID, domainUUID, domainName){
	currentSessionID = sessionID;
	currentUserUUID = userUUID;
	currentDomainUUID = domainUUID;
	currentDomainName = domainName;

	var ratURL = simplePrefs.prefs.ratURL;
	//var sessionID = sessionID;

	console.log("openSidePanel currentDomainUUID: " + currentDomainUUID);
	console.log("openSidePanel currentDomainName: " + currentDomainName);
	console.log("openSidePanel ratURL: " + ratURL);
	console.log("openSidePanel currentSessionID: " + currentSessionID);
	currentURL = currentURL.indexOf("about:") > -1 ? null : currentURL;

	commentsSidePanel = sidebar.Sidebar({
		id: 'comments-sidepanel',
		title: 'Comments',
		url: data.url("./comments/commentsSidePanel.html"),
		contentScriptFile: data.url('./comments/comments.js'),
		onHide: hideSidebar,
		onDetach: detachWorker,
		onReady: function (worker) {
			workerArray.push(worker);
			commentsSidePanelWorker = worker;
			worker.port.emit("message", 'raturl', ratURL);

			worker.port.emit("message", 'sessionid', currentSessionID);
			worker.port.emit("message", 'useruuid', currentUserUUID);
			worker.port.emit("message", 'currentdomainuuid', currentDomainUUID);
			worker.port.emit("message", 'currentdomainname', currentDomainName);
			worker.port.emit("message", 'currenturl', currentURL);

			worker.port.on("message", function(text, domainName, domainUUID) {
				console.log("commentsSidePanel.js worker text: " + text);
				console.log("commentsSidePanel.js worker domainName: " + domainName);
				console.log("commentsSidePanel.js worker currentDomainUUID: " + domainUUID);

				switch (text){
					case 'opencomment-dlg':
					commentWnd.openWindow(domainName, domainUUID);
					break;

					case 'getuserdomains':
					break;
				}
			});
		}

	});
	commentsSidePanel.show();
	console.log("commentsSidePanel.js commentsSidePanel: " + commentsSidePanel);
}




tabs.on('open', function onOpen(tab) {
	commentsSidePanelWorker.port.emit("message", "currenturl", null);
});

tabs.on('ready', function(tab) {
	var url = tab.url.indexOf("about:") > -1 ? null : tab.url;
  	console.log(tab.url);
	commentsSidePanelWorker.port.emit("message", "currenturl", url);
});

tabs.on('activate', function (tab) {
	var url = tab.url.indexOf("about:") > -1 ? null : tab.url;
  	console.log(tab.url);
	commentsSidePanelWorker.port.emit("message", "currenturl", url);
});


function detachWorker(worker) {
	console.log("detachWorker worker: " + worker);
	var index = workerArray.indexOf(worker);
	if(index != -1) {
		workerArray.splice(index, 1);
	}
}

function hideSidebar(){
	console.log("commentsSidePanel.js hideSidebar");
	commentsSidePanel.dispose();
}

function setComment(title, comment){
	console.log("commentsSidePanel.js setComment title: " + title);
	console.log("commentsSidePanel.js setComment comment: " + comment);

	commentsSidePanelWorker.port.emit("message", "title", title);
	commentsSidePanelWorker.port.emit("message", "comment", comment);
}

exports.openSidePanel = openSidePanel;
exports.setComment = setComment;
