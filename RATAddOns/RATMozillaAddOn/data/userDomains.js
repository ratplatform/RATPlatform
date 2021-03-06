var data = [];
var url;
var sessionID;
addon.port.on("message", function(msgType, text) {
	switch (msgType){
	case 'data':
	var obj = JSON.parse(text);
	var inc = 0;
        for(var domain in obj.userDomains){
		var domainUUID = obj.userDomains[domain];
		data[inc] = {label: domain, id: domainUUID,};
		inc++;
        }
    
	console.log("userDomains.js data: " + JSON.stringify(data));
	break;

	case 'url':
	url = text;
	console.log("userDomains.js url: " + url);
	break;

	case 'sessionid':
	sessionID = text;
	console.log("userDomains.js sessionID: " + sessionID);
	break;
	}
});

function sendResponse(text, domainName, currentDomainUUID){
	addon.port.emit("message", text, domainName, currentDomainUUID);
}


