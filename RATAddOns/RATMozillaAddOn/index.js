var self = require('sdk/self');
var buttons = require('sdk/ui/button/action');
var tabs = require("sdk/tabs");

// dgr
var ss = require("sdk/simple-storage");
//const {XMLHttpRequest} = require("sdk/net/xhr");
var data = require("sdk/self").data;
var pageMod = require("sdk/page-mod");
var actionButtonMenu = require('./data/actionButtonMenu');
var ss = require("sdk/simple-storage");
//var windows = require("sdk/windows").browserWindows;
// end dgr


console.log("Start plugin");

delete ss.storage.sessionID;
delete ss.storage.userUUID;
delete ss.storage.lastResponseText;
delete ss.storage.lastStatus;
delete ss.storage.currentDomainUUID;
delete ss.storage.currentDomainName;

/*
var currentURL;
require("sdk/tabs").on("ready", logURL);
 
function logURL(tab) {
  //console.log(tab.url);
	currentURL = tab.url;
}
*/

actionButtonMenu.init();
