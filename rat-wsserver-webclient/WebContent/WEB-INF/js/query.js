var runQuery = function(url, json, callbackSuccess, callbackError){
	$.ajax({
		crossDomain: true,
		type: 'POST',
		dataType: 'text',
		contentType: 'text/plain; charset=utf-8',
		data: JSON.stringify(json),
		url: url,
		timeout: 3000,
		success: callbackSuccess,
		error: callbackError
	});
};

var queryCallbackSuccess = function(data, textStatus, jqXHR){
	console.log("data: " + data);
	console.log("textStatus: " + textStatus);
	console.log("jqXHR: " + jqXHR);
};

var queryCallbackError = function(jqXHR, textStatus, errorThrown){
	console.log("jqXHR: " + jqXHR);
	console.log("textStatus: " + textStatus);
	console.log("errorThrown: " + errorThrown);
};

