var Request = require("sdk/request").Request;

function post(url, jsonCommand, callback){
	var post = Request({
		url: url,
		content: jsonCommand,
		contentType: "text/plain",
		overrideMimeType: "text/plain; charset=latin1",
		onComplete: callback
	});

	post.post();
}

exports.post = post;

