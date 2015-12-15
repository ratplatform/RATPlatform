var title = document.getElementById("title");
var textArea = document.getElementById("edit-box");
var saveComment = document.getElementById("saveComment");
var closeComment = document.getElementById("saveComment");

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

	var letters = /^[0-9a-zA-Z_\s\-\.]+$/;
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

	self.port.emit("comment", textArea.value, title.value);
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

