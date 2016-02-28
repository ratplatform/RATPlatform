function selectCommentMenuItem(event, ui) {
	//console.log("select " + ui.cmd + " on " + ui.target.text());

	var parentID = ui.target.context.parentNode.id;

	var obj = document.getElementById("uuid_" + parentID);
	if(!obj){
		alert("Please click on yellow rectangle");
		return;
	}
	var uuid = obj.value;
	//console.log("selectCommentMenuItem id: " + "uuid_" + parentID);
	//console.log("selectCommentMenuItem uuid: " + uuid);

	//console.log("selectCommentMenuItem id: " + "title_" + parentID);
	obj = document.getElementById("title_" + parentID);
	var title = obj.innerHTML;

	obj = document.getElementById("text_" + parentID);
	var text = obj.innerHTML;
	console.log("selectCommentMenuItem text: " + text);
	switch(ui.cmd){
		case 'reply':
			if(uuid){
				showCommentWnd('Re: ' + title, '>>' + text + '<<\n', getComboUrl(), uuid);
			}
		break;

		case 'delete':
		alert("not yet implemented");
		break;

		default:
		alert("Comment menu selecion error!");
	}

}
