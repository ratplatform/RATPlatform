var callback = null;

function errorCallBack(jqXHR, textStatus, errorThrown){
	console.log("errorCallBack jqXHR: " + jqXHR);
	console.log("errorCallBack textStatus: " + textStatus);

	currentCommentJsonForGraph = null;
	currentDomainJsonForGraph = null;
}

function wsCallBack(data, textStatus, jqXHR){
	console.log("wsCallBack jqXHR: " + jqXHR);
	console.log("wsCallBack textStatus: " + textStatus);

	var json = JSON.parse(data);

	var jsonEdges = json.settings.edges;
	var edges = getEdges(jsonEdges);

	var jsonVertices = json.settings.vertices;
	var vertices = getVertices(jsonVertices);

	var jsonHeader = json.header;
	var header = JSON.stringify(jsonHeader);

	var alchemy = "{" + "\"header\":" + header + ",\"nodes\":" + vertices + ",\"edges\":" + edges +"}";

	callback(alchemy, textStatus, jqXHR);
}

function getVertices(json){
	var data = JSON.stringify(json);

	var regex = /\"_id\"/gi;
	data = data.replace(regex, "\"id\"");

	return data;
}

function getEdges(json){
	var data = JSON.stringify(json);

	var regex = /\"_id\"( )*:( )*\"[0-9]*\",/gi;
	data = data.replace(regex, "");

	regex = /\"_type\"( )*:( )*\"edge\"( )*,/gi;
	data = data.replace(regex, "");

	regex = /\"_outV\"/gi;
	data = data.replace(regex, "\"source\"");

	regex = /\"_inV\"/gi;
	data = data.replace(regex, "\"target\"");

	regex = /\"_label\"/gi;
	data = data.replace(regex, "\"caption\"");

	return data;
}

// Questa funzione viene chiamata quando devo trasformare il JSON di tipo GraphSON prodotto da RAT nel formato richesto da Alchemy
function callWs(url, method, jsonText, successCallback, errorCallBack){
	callback = successCallback;
	$.ajax({
		url: url,
		method: method,
		data: jsonText,
		dataType: 'text',
		contentType: "text/plain",
		success: wsCallBack, /*La callBak wsCallBack esegue la trasformazione da GraphSON ad Alchemy*/
		error: errorCallBack
	}); 
}

// Questa funzione viene chiamata quando NON devo trasformare il JSON prodotto da RAT, ma lo uso direttamente
function callWsSimple(url, method, jsonText, successCallback, errorCallBack){
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

function callWsLogout(url, successCallback, errorCallBack){
	$.ajax({
		url: url,
		method: "POST",
		success: successCallback,
		error: errorCallBack
	}); 
}
