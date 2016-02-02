
function addWindow(prevWnd, inc, title, comment, uuid, subComments){
	var id = "dynamic_" + inc;
	var wnd = createWnd(id, title, comment, uuid, subComments);
	$(wnd).appendTo($('.comments'));

	if (subComments > 0){
		var div = $('#sub_' + id);
		div.addClass('hasSubComments');
		div.click(subCommentsClick);
	}

	if(prevWnd){
		var position = $(prevWnd).offset();
		//console.log("position: " + position); 
		var parentID = $(prevWnd).attr('id');
		var top = 10 * inc;
		$('#' + id).css('top', top);

		position = $('#' + id).offset();
		//console.log("position: " + position); 
	}

	jsPlumb.draggable($('#' + id));

	return id;
}

function removeAllComments(){
	var children = $('.comments').children();
	//console.log("children: " + children.length); 
	for (var i = 0; i < children.length; i++) {
		var div = children[i];
		//console.log("div: " + div); 
		var id = $(div).attr('id');
		//console.log("div id: " + id); 
		$('#' + id).remove(); 
	}

	var commentsBox = document.getElementById("commentsContainer");
	commentsBox.innerHTML = "";
}

function createWnd(id, title, comment, uuid, subComments){
	var wnd = '<div class="window" id="' + id + '">' + 
		'<input type="hidden" id="uuid_' + id + '" name="commentUUID" value="' + uuid + '">' + 
		'<div class="resizableH3" id="title_' + id + '">' + title + '</div>' + 
		'<div class="textContainer" id="text_' + id + '">' + 
		comment +
		'</div>' + 
		'<div class="subComments" id="sub_' + id + '">Sub comments: ' + subComments + '</div>' + 
		'</div>';
	return wnd;
}

function subCommentsClick(eventObject){
	//var id = eventObject.currentTarget.id;
	//$('#' + id).removeClass('hasSubComments');
	//$('#' + id).unbind("click", subCommentsClick);

	var parent = eventObject.currentTarget.parentNode;
	//console.log("subCommentsClick parent: " + parent.id);
	var hidden = document.getElementById('uuid_' + parent.id);
	var title = document.getElementById('title_' + parent.id);
	var titleText = title.textContent;
	//console.log("subCommentsClick hidden: " + hidden);
	var uuid = hidden.value;
	//console.log("subCommentsClick uuid: " + uuid);
	getSubComments(uuid, titleText);
}

var wnds = [];
var anEndpointDestination;/* = {
	endpoint: [ "Dot", { radius:3 } ],
	style:{fillStyle:'#456'},
	isSource: true,
	isTarget: true,
	maxConnections: 1,
	connector : "Straight",
	connectorStyle: { lineWidth:1, strokeStyle:'#456' },
	anchor:"AutoDefault"
};
*/
function timeOut(){
	var element = wnds.pop();
	if(element){
		var prevWndID = element.prevWnd.getAttribute('id');
		var currentWndID = element.currentWnd.getAttribute('id');

		if(anEndpointDestination){
		jsPlumb.addEndpoint(element.currentWnd, { uuid: currentWndID }, anEndpointDestination);
		jsPlumb.addEndpoint(element.prevWnd, { uuid: prevWndID }, anEndpointDestination);
		jsPlumb.connect({uuids:[prevWndID, currentWndID]});
		}
	}
}
function addSubNodes(array, rootCommentUUID){
	if(!rootCommentUUID){
		alert("Error: root comment not found!");
		return;
	}

	anEndpointDestination = {
		endpoint: [ "Dot", { radius:3 } ],
		style:{fillStyle:'#456'},
		isSource: true,
		isTarget: true,
		maxConnections: 1,
		connector : "Straight",
		connectorStyle: { lineWidth:1, strokeStyle:'#456' },
		anchor:"AutoDefault"
	};

	var arrayLength = array.length;
	var parentWnd;

	for (var i = 0; i < arrayLength; i++) {
		var node = array[i];
		if(typeof node == 'undefined'){
			console.log("pageScripts.js domainsSidePanel.html getDomainAndUserComments node[0] typeof is undefined ");
			continue;
		}
		var type = node.getProperties("VertexTypeField");
		if(type === 'Domain'){
			continue;
		}

		var label = node.getProperties("VertexLabelField");
		var content = node.getProperties("VertexContentField");
		var subComments = node.getProperties("comments");
		if(!subComments){
			subComments = 0;
		}

		var uuid = node.getProperties("VertexUUIDField");
		if(!uuid){
			console.log("#ERROR pageScripts.js domainsSidePanel.html uuid: " + uuid);
		}

		var id;
		id = addWindow(parentWnd, i, label, content, uuid, subComments);
	       	var currentWnd = document.getElementById(id);

		if(uuid == rootCommentUUID){
			$('#sub_' + id).removeClass('hasSubComments');
			$('#sub_' + id).unbind("click", subCommentsClick);
			parentWnd = currentWnd;
		}

		if(parentWnd){
			var elements = { currentWnd : currentWnd, prevWnd : parentWnd };
			wnds.push(elements);
		}

		setTimeout(timeOut, 5);
	}
}

function addNode(array){
	var arrayLength = array.length;
	var prevWnd;
	for (var i = 0; i < arrayLength; i++) {
		var node = array[i];
		if(typeof node == 'undefined'){
			console.log("pageScripts.js domainsSidePanel.html getDomainAndUserComments node[0] typeof is undefined ");
			continue;
		}
		var type = node.getProperties("VertexTypeField");
		if(type === 'Domain'){
			continue;
		}
		var label = node.getProperties("VertexLabelField");
		var content = node.getProperties("VertexContentField");
		var subComments = node.getProperties("comments");
		if(!subComments){
			subComments = 0;
		}

		var uuid = node.getProperties("VertexUUIDField");
		if(!uuid){
			console.log("#ERROR pageScripts.js domainsSidePanel.html uuid: " + uuid);
		}
		
/*
		var coordinates = {
			startpagex: node.getProperties("startPageX"),
			endpagex: node.getProperties("endPageX"),
			startpagey: node.getProperties("startPageY"),
			endpagey: node.getProperties("endPageY")
		}
		if(coordinates.startpagex > -1 && coordinates.endpagex > -1 && coordinates.startpagey > -1 && coordinates.endpagey > -1){
			sendSelectionNode(coordinates);
		}
*/
		var coordinates = node.getProperties("jsonCoordinates");
		sendSelectionNode(coordinates);

		var id;
		id = addWindow(prevWnd, i, label, content, uuid, subComments);
	       	var currentWnd = document.getElementById(id);
		if(prevWnd){
			var elements = { currentWnd : currentWnd, prevWnd : prevWnd };
			wnds.push(elements);
		}
		prevWnd = currentWnd;
		//setTimeout(timeOut, 5);
	}
}
