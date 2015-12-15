var self = require("sdk/self");
var commentsSidePanel = require('../comments/commentsSidePanel');

var currentDomainName;
var currentDomainUUID;

function openWindow(domainName, domainUUID) {
	console.log("commentWnd.js openWindow domainName: " + domainName);
	console.log("commentWnd.js openWindow currentDomainUUID: " + domainUUID);
	panel.show();
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


panel.port.on("comment", function (title, text) {
	console.log(title);
	console.log(text);

	commentsSidePanel.setComment(title, text);
});


exports.openWindow = openWindow;
