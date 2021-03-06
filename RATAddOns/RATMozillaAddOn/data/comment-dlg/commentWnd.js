var self = require("sdk/self");
var domainsSidePanel = require('../domains/domainsSidePanel');

var currentDomainName;
var currentDomainUUID;

function openWindow(domainName, domainUUID, data) {
	//console.log("commentWnd.js openWindow domainName: " + domainName);
	//console.log("commentWnd.js openWindow currentDomainUUID: " + domainUUID);
	panel.show();
	if(data){
		if(!data.title){
			data.title = "";
		}
		if(!data.text){
			data.text = "";
		}

/*
			var commandData = {
				sessionID : currentSessionID,
				currentDomainUUID: currentDomainUUID, 
				currentDomainName: currentDomainName,
				ownerUUID: currentDomainUUID,
				userUUID: currentUserUUID, 
				startpagex: selectionData.startPageX, 
				endpagex: selectionData.endPageX,
				startpagey: selectionData.startPageY, 
				endpagey: selectionData.endPageY,
				currentURL: currentURL, 
				text: selectionData.text, 
				title: selectionData.title
			};
*/
/*
		console.log("commentWnd.js: openWindow.data.currentSessionID: " + data.currentSessionID);
		console.log("commentWnd.js: openWindow.data.currentDomainUUID: " + data.currentDomainUUID);
		console.log("commentWnd.js: openWindow.data.currentDomainName: " + data.currentDomainName);
		console.log("commentWnd.js: openWindow.data.currentDomainUUID: " + data.currentDomainUUID);
		console.log("commentWnd.js: openWindow.data.currentUserUUID: " + data.currentUserUUID);
		console.log("commentWnd.js: openWindow.data.startpagex: " + data.startpagex);
		console.log("commentWnd.js: openWindow.data.endpagex: " + data.endpagex);
		console.log("commentWnd.js: openWindow.data.startpagey: " + data.startpagey);
		console.log("commentWnd.js: openWindow.data.endpagey: " + data.endpagey);
		console.log("commentWnd.js: openWindow.data.currentURL: " + data.currentURL);
		console.log("commentWnd.js: openWindow.data.title: " + data.title);
		console.log("commentWnd.js: openWindow.data.text: " + data.text);
*/
		// Verso comment.js -> self.port.on("set-text"
		panel.port.emit('set-text', data.title, data.text, data);
	}
}

var panel = require("sdk/panel").Panel({
	contentURL: self.data.url("./comment-dlg/commentWnd.html"),
	contentScriptFile: self.data.url("./comment-dlg/comment.js"),
	height: 400,
	width: 400
});

panel.port.on("hide", function () {
	panel.hide();
});


panel.port.on("comment", function (commandData) {
/*
	console.log("commentWnd.js: comment.commandData.currentSessionID: " + commandData.currentSessionID);
	console.log("commentWnd.js: comment.commandData.currentDomainUUID: " + commandData.currentDomainUUID);
	console.log("commentWnd.js: comment.commandData.currentDomainName: " + commandData.currentDomainName);
	console.log("commentWnd.js: comment.commandData.currentDomainUUID: " + commandData.currentDomainUUID);
	console.log("commentWnd.js: comment.commandData.currentUserUUID: " + commandData.currentUserUUID);
	console.log("commentWnd.js: comment.commandData.startpagex: " + commandData.startpagex);
	console.log("commentWnd.js: comment.commandData.endpagex: " + commandData.endpagex);
	console.log("commentWnd.js: comment.commandData.startpagey: " + commandData.startpagey);
	console.log("commentWnd.js: comment.commandData.endpagey: " + commandData.endpagey);
	console.log("commentWnd.js: comment.commandData.currentURL: " + commandData.currentURL);
	console.log("commentWnd.js: comment.commandData.title: " + commandData.title);
	console.log("commentWnd.js: comment.commandData.text: " + commandData.text);
*/
	// Verso domainSidePanel.js -> setComment
	domainsSidePanel.setComment(commandData);
});


exports.openWindow = openWindow;
