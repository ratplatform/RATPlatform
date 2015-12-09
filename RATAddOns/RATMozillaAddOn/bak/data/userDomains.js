var data = [];

addon.port.on("onReceived", function(text) {
	var obj = JSON.parse(text);
        for(var domain in obj.userDomains){
		var domainUUID = obj.userDomains[domain];
		data[data.length] = {label: domain, id: domainUUID,};
        }
    
	console.log("userDomains.js: " + data);
});

function sendResponse(domainName){
	addon.port.emit("onDomainNameReceived", domainName);
}

/*
addon.port.emit("ping");
  //addon.port.emit("ping", {
    //console.log("addon.port.emit ping");
 // });

var data = [];

addon.port.on("pong", function(text) {
  	//console.log("sidebar script got the reply B: " + text);
	var obj = JSON.parse(text);
	//console.log("sidebar script got the reply B: " + obj.userDomains);
        for(var domain in obj.userDomains){
		var domainUUID = obj.userDomains[domain];
		//console.log(domain + ": " + domainUUID);
		//console.log(data.length);
		data[data.length] = {label: domain, id: domainUUID,};
		//console.log(data);
        }
    
	console.log("userDomains.js: " + data);
});
*/


//var data;
/*
	var data = [
	    {
		label: 'node1', id: 1,
		children: [
		    { label: 'child1aaaaaaaaaaa', id: 5 },
		    { label: 'child2', id: 67 }
		]
	    },
	    {
		label: 'node2', id: 2,
		children: [
		    { label: 'child3', id: 8 }
		]
	    }
	];
*/

