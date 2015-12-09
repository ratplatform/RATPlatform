
var RATPost = (function (url) {
	var obj = {};
	obj.url = url;
	obj.post = function (wsFuncName, json, onCompleteFunction) {
		alert();
		var request = Request({
			url: url + "/" + wsFuncName,
			content: json,
			contentType: "text/plain",
			overrideMimeType: "text/plain; charset=latin1",
			onComplete: onCompleteFunction
		});

		request.post();
	};

	return obj;
}(url));

