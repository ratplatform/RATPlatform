function callWs(url, method, jsonText, successCallback, errorCallBack){
	$.ajax({
		url: url,
		method: method,
		data: jsonText,
		dataType: 'text',
		contentType: "text/plain",
		success: successCallback,
		error: errorCallBack
	}); 
}

function errorCallBack(jqXHR, textStatus, errorThrown){
	console.log("errorCallBack jqXHR: " + jqXHR);
	console.log("errorCallBack textStatus: " + textStatus);
}
