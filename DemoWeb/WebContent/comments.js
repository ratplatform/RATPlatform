var wnds = [];
var anEndpointDestination;

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

function addNewCommentCallBack(data, textStatus, jqXHR) {
	removeAllComments();
	//console.log("addNewCommentCallBack: " + data);


	getAllComments(currentURL);

	setTimeout(openURLs, 1000);
}

function openURLs(){
	var wsUrl = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
	var getAllUserURLs = getUserURLsFunc("null", loginResult.userUUID);
	callWs(wsUrl, 'POST', getAllUserURLs, getAllUserURLsCallBack, errorCallBack);
}

function getAllComments(url){
	var wsURL = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
	var json  = getAllUserDomainCommentsFunc(currentDomainUUID, loginResult.userUUID, currentDomainUUID, url);
	console.log("getAllComments json: " + json);

	callWs(wsURL, 'POST', json, allUserCommentsCallBack, errorCallBack);
}

function allUserCommentsCallBack(data, textStatus, jqXHR) {
	console.log("allUserCommentsCallBack: " + data);
	if(data.indexOf("nodes") > -1){
		var json = JSON.parse(data);
		//currentJsonForGraph = json;
		renderGraph('comments', json);

		var nodes = [];
		var i = 0;
		var jsonNodes = json.nodes;
		//for(var node in jsonNodes){
			//nodes[i] = ;
			//i++;
		//}
		addNode(jsonNodes);
	}
}

function addNode(array){
	wnds = [];
	var arrayLength = array.length;
	var prevWnd;
	for (var i = 0; i < arrayLength; i++) {
		var node = array[i];
		if(typeof node == 'undefined'){
			console.log("pageScripts.js domainsSidePanel.html getDomainAndUserComments node[0] typeof is undefined ");
			continue;
		}
		var type = node.VertexTypeField;
		if(type != 'Comment'){
			continue;
		}
		var label = node.VertexLabelField;
		var content = node.VertexContentField;
		var subComments = node.subComments;
		if(!subComments){
			subComments = 0;
		}

		var uuid = node.VertexUUIDField;
		if(!uuid){
			console.log("#ERROR addNode uuid: " + uuid);
			continue;
		}

		var coordinates = node.jsonCoordinates;
		
		//sendSelectionNode(coordinates);

		var id;
		id = addWindow(prevWnd, i, label, content, uuid, subComments);
	       	var currentWnd = document.getElementById(id);
		if(prevWnd){
			var elements = { currentWnd : currentWnd, prevWnd : prevWnd };
			wnds.push(elements);
		}
		prevWnd = currentWnd;
		
	}
}

function addSubNodes(array, rootCommentUUID){
	var elements = [];
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

	for (var i = 0; i < arrayLength; i++) {
		var node = array[i];
		if(typeof node == 'undefined'){
			console.log("addSubNodes node[0] typeof is undefined ");
			continue;
		}
		var type = node.VertexTypeField;
		if(type != 'Comment'){
			console.log("addSubNodes node " + i  + " is not a comment");
			continue;
		}

		var label = node.VertexLabelField;
		var content = node.VertexContentField;
		var subComments = node.subComments;
		if(!subComments){
			subComments = 0;
		}

		var uuid = node.VertexUUIDField;
		if(!uuid){
			console.log("#ERROR addNode uuid: " + uuid);
			continue;
		}

		var coordinates = node.jsonCoordinates;
		var element = {label: label, content: content, uuid: uuid, subComments:subComments};
		if(uuid == rootCommentUUID){
			elements.unshift(element);
		}
		else{
			elements.push(element);
		}
	}
	
	var parentWnd; 
	var parentWndID;
	arrayLength = elements.length;
	for (var i = 0; i < arrayLength; i++) {
		var element = elements[i];
		var wndID = addWindow(parentWnd, i, element.label, element.content, element.uuid, element.subComments);
		var currentWnd = document.getElementById(wndID);

		if(i == 0){
			$('#sub_' + wndID).removeClass('hasSubComments');
			$('#sub_' + wndID).unbind("click", subCommentsClick);
			parentWnd = currentWnd;
			parentWndID = wndID;
		}
		else{
			wnds.push({ currentWnd : currentWnd, parentWnd : parentWnd });
			setTimeout(timeOut, 15);
		}
	}
}

function timeOut(){
	var element = wnds.pop();
	if(element){
		var prevWndID = element.parentWnd.getAttribute('id');
		var currentWndID = element.currentWnd.getAttribute('id');

		if(anEndpointDestination){
			jsPlumb.addEndpoint(element.currentWnd, { uuid: currentWndID }, anEndpointDestination);
			jsPlumb.addEndpoint(element.parentWnd, { uuid: prevWndID }, anEndpointDestination);
			jsPlumb.connect({uuids:[prevWndID, currentWndID]});
		}
	}
}

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
	var parent = eventObject.currentTarget.parentNode;
	var hidden = document.getElementById('uuid_' + parent.id);
	var title = document.getElementById('title_' + parent.id);
	var titleText = title.textContent;
	var uuid = hidden.value;

	getSubComments(uuid, titleText);
}


