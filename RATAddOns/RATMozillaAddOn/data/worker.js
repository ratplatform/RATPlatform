/*
onconnect = function(e) {
	var port = e.ports[0];

	port.onmessage = function(e) {
	  var workerResult = 'Result: ' + (e.data[0] + " " + e.data[1]);
	  port.postMessage(workerResult);
	}

}
*/
/*
self.addEventListener('connect', function(e) { // addEventListener() is needed
  var port = e.ports[0];
  port.onmessage = function(e) {
    var workerResult = 'Result: ' + (e.data[0] + " " + e.data[1]);
    port.postMessage(workerResult);
  }
  port.start();  // not necessary since onmessage event handler is being used
});
*/

onconnect = function(e) {
	console.log("worker.js: ready");
	var port = e.ports[0];
/*
	var panel = require("sdk/panel").Panel({
		contentURL: self.data.url("./comment-dlg/commentWnd.html"),
		contentScriptFile: self.data.url("./comment-dlg/comment.js"),
		height: 400,
		width: 400
	});
	panel.show();
	console.log("worker.js: panel " + panel);
*/
	port.onmessage = function(e) {
	  var workerResult = 'Result: ' + (e.data[0] + " " + e.data[1]);
	  port.postMessage(workerResult);
	}

}
