var {Cc, Ci, Cu} = require("chrome");
var { Hotkey } = require("sdk/hotkeys");
var self = require("sdk/self");
var data = require('sdk/self').data;
var windows = require('sdk/windows');
var tabs = require('sdk/tabs');
var panel = require("sdk/panel");
var cm = require("sdk/context-menu");
var { ToggleButton } = require('sdk/ui/button/toggle');
var xulApp = require('sdk/system/xul-app');
var ui = require("sdk/ui");
var ss = require("sdk/simple-storage");
var storage = ss.storage;
var mediator = Cc['@mozilla.org/appshell/window-mediator;1']
	.getService(Ci.nsIWindowMediator);
var AddonManager = Cu.import("resource://gre/modules/AddonManager.jsm").AddonManager;
var PrivateBrowsingUtils = Cu.import("resource://gre/modules/PrivateBrowsingUtils.jsm").PrivateBrowsingUtils;

// dgr
var sidebar = require("sdk/ui/sidebar");
var simplePrefs = require("sdk/simple-prefs");
var domainsSidePanel = require('./domainsSidePanel');
var commentsSidePanel = require('./commentsSidePanel');
var login = require('./login');
var ss = require("sdk/simple-storage");
// end dgr

var visibleKey,entireKey;
var popupPanel,toolbarButton;

function getTimeStamp() {
	var y, m, d, h, M, s; 
	var time = new Date();
	y = time.getFullYear();
	m = time.getMonth()+1;
	d = time.getDate();
	h = time.getHours();
	M = time.getMinutes();
	s = time.getSeconds();
	if (m < 10) m = '0' + m;
	if (d < 10) d = '0' + d;
	if (h < 10) h = '0' + h;
	if (M < 10) M = '0' + M;
	if (s < 10) s = '0' + s;
	return 	y + '-' + m + '-' + d + ' ' + h + '-' + M + '-' + s;
}

function createMenuPopup(doc) {
	var menupopup = doc.createElement('menupopup');
	var visibleItem = doc.createElement('menuitem');
	var entireItem = doc.createElement('menuitem');
	menupopup.setAttribute('class', 'rat-menupopup');
	visibleItem.setAttribute('data-option', 'visible');
	entireItem.setAttribute('data-option', 'entire');
	visibleItem.setAttribute('class', 'menuitem-iconic');
	entireItem.setAttribute('class', 'menuitem-iconic');
	visibleItem.setAttribute('label', 'Capture Visible Part');
	entireItem.setAttribute('label', 'Capture Full page');
	visibleItem.setAttribute('image', data.url('img/visible.png'));
	entireItem.setAttribute('image', data.url('img/entire.png'));
	visibleItem.setAttribute('key', 'visibleKey');
	
	var separator = doc.createElement('menuseparator');
	
	var options = doc.createElement('menuitem');
	options.setAttribute('data-option', 'options');
	options.setAttribute('label', 'Options');
	
	menupopup.appendChild(visibleItem);
	menupopup.appendChild(entireItem);
	menupopup.appendChild(separator);
	menupopup.appendChild(options);
	return menupopup;
}

function addToolbarButton(doc) {
	var addonBar = doc.getElementById("nav-bar");
	if (!addonBar) return;
	var toolbarbutton = doc.createElement("toolbarbutton"); 	
	
	var id = toolbarbutton.id = 'rat-toolbarbutton';
	toolbarbutton.setAttribute('type', 'menu');
	toolbarbutton.setAttribute('class', 'toolbarbutton-1 chromeclass-toolbar-additional');
	toolbarbutton.setAttribute('image', data.url('img/icon-16.png'));
	toolbarbutton.setAttribute('orient', 'horizontal');
	toolbarbutton.setAttribute('label', 'RAT Addon');
	
	doc.defaultView.addEventListener("aftercustomization", function() {
		var as = doc.getElementById(id);
		storage.customize = {
			parent: as ? as.parentNode.id : null,
			next: as ? as.nextSibling.id : null
		};
	}, false);
}

var isShort = false;

function removeShortcuts(){
	if(!isShort) return;
	isShort = false;
	visibleKey.destroy();
	entireKey.destroy();
}

function removeUI(win) {
	var doc = win.document;
	
	var addonBar = doc.getElementById("nav-bar");
	var button = doc.getElementById("rat-toolbarbutton");
	if (button) addonBar.removeChild(button);
	
	var popup = doc.getElementById('contentAreaContextMenu');
	var menu = doc.getElementById("rat-contextmenu");
	if (menu) popup.removeChild(menu);
}

function removeAll() {
	var enumerator = mediator.getEnumerator(null);
	while(enumerator.hasMoreElements()) {
		removeUI(enumerator.getNext());
	}
}


function toolbarChange(state){
    if(state.checked){
        popupPanel.show({
            position:toolbarButton
        });
    }
}

function popupHide(){
    toolbarButton.state('window',{checked:false});
}

function addToolbarButtons(){
	toolbarButton = ToggleButton({
        id:'rat-toolbarbutton',
        label:'RAT Addon',
        icon:{
            "16":'./img/icon-16.png',
            "32":'./img/icon-32.png',
            "64":'./img/icon-64.png'
        },
        onChange:toolbarChange
    });

    popupPanel = panel.Panel({
        width:300,
        height:100,
        contentURL:data.url('./popup.html'),
        onHide:popupHide
    });

    popupPanel.port.on('popupmessage',function(text){
        switch (text){
            case 'login':
                //capture('visible');
		console.log("login");
		var url = simplePrefs.prefs.ratURL;
		var userEmail = simplePrefs.prefs.userEmail;
		var userPwd = simplePrefs.prefs.userPassword;
		login.login(url, userEmail, userPwd);
                break;
            case 'logout':
                //capture('entire');
		console.log("logout"); 
                break;
            case 'sidePanelDomains':
		//console.log("login.getResponseText : " + login.getResponseText);
		//console.log("login.getUserUUID : " + login.getUserUUID);
		// TODO: controllare login.getStatus
		domainsSidePanel.openSidePanel(ss.storage.lastResponseText);
                break;
            case 'commentsSidePanel':
		console.log("commentsSidePanel");
		//console.log("login.getUserUUID : " + login.getUserUUID);
		// TODO: controllare login.getStatus
		commentsSidePanel.openSidePanel(ss.storage.lastResponseText);
                break;

        }
        popupPanel.hide();
    });
}

function init(capture) {
    	removeAll();
	addToolbarButtons();
}

exports.init = init;

