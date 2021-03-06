var title = document.getElementById("title");
var textArea = document.getElementById("edit-box");
var saveComment = document.getElementById("saveComment");
var closeComment = document.getElementById("closeComment");
var commandData;

saveComment.addEventListener("click", function(){
	var titleText = title.value;
	if (!titleText){
		alert("Please add a title");
		return;
	}

	titleText = titleText.trim();
	title.value = titleText;
	var n = titleText.length;
	if (n == 0){
		alert("Please insert a valid title");
		return;
	}

	var letters = /^[0-9a-zA-Z_\s\-\.\:]+$/;
	if(!titleText.match(letters)) {
		alert("Please insert a valid title; permitted characters are alphanumeric characters, dot, underscore and minus sign");
		return;
	}

	var text = textArea.value;
	text = text.trim();
	n = text.length;
	if (n == 0){
		alert("Please insert a valid text");
		return;
	}

	commandData.text = textArea.value;
	commandData.title = title.value;
/*
	console.log("comment.js: click.commandData.currentSessionID: " + commandData.currentSessionID);
	console.log("comment.js: click.commandData.currentDomainUUID: " + commandData.currentDomainUUID);
	console.log("comment.js: click.commandData.currentDomainName: " + commandData.currentDomainName);
	console.log("comment.js: click.commandData.currentDomainUUID: " + commandData.currentDomainUUID);
	console.log("comment.js: click.commandData.currentUserUUID: " + commandData.currentUserUUID);
	console.log("comment.js: click.commandData.startpagex: " + commandData.startpagex);
	console.log("comment.js: click.commandData.endpagex: " + commandData.endpagex);
	console.log("comment.js: click.commandData.startpagey: " + commandData.startpagey);
	console.log("comment.js: click.commandData.endpagey: " + commandData.endpagey);
	console.log("comment.js: click.commandData.currentURL: " + commandData.currentURL);
	console.log("comment.js: click.commandData.title: " + commandData.title);
	console.log("comment.js: click.commandData.text: " + commandData.text);
*/
	// Verso commentWnd.js -> panel.port.on("comment"
	self.port.emit("comment", commandData);

	textArea.value = '';
	title.value = '';
	self.port.emit("hide");
});

closeComment.addEventListener("click", function(){
	textArea.value = '';
	title.value = '';

	self.port.emit("hide");
});

self.port.on("show", function onShow() {
	textArea.focus();
});

self.port.on("set-text", function(title, text, data) {
/*
	console.log("comment.js: set-text.data.currentSessionID: " + data.currentSessionID);
	console.log("comment.js: set-text.data.currentDomainUUID: " + data.currentDomainUUID);
	console.log("comment.js: set-text.data.currentDomainName: " + data.currentDomainName);
	console.log("comment.js: set-text.data.currentDomainUUID: " + data.currentDomainUUID);
	console.log("comment.js: set-text.data.currentUserUUID: " + data.currentUserUUID);
	console.log("comment.js: set-text.data.startpagex: " + data.startpagex);
	console.log("comment.js: set-text.data.endpagex: " + data.endpagex);
	console.log("comment.js: set-text.data.startpagey: " + data.startpagey);
	console.log("comment.js: set-text.data.endpagey: " + data.endpagey);
	console.log("comment.js: set-text.data.currentURL: " + data.currentURL);
	console.log("comment.js: set-text.data.title: " + data.title);
	console.log("comment.js: set-text.data.text: " + data.text);
*/
	document.getElementById('title').value = title;
	document.getElementById('edit-box').value = text;
	commandData = data;
});


