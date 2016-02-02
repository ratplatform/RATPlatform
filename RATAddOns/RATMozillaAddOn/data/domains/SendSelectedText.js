var status;
var pageX;
var pageY;

(function() {
	self.port.on("message", function(msgType, data) {
		console.log("message: " + msgType + "; " + data); 
		var bodyRect = document.body.getBoundingClientRect();
    		offset   = elemRect.top - bodyRect.top;

		console.log('Element is ' + offset + ' vertical pixels from <body>');

/*
		var coordinates = {
			startpagex: node.getProperties("startPageX"),
			endpagex: node.getProperties("endPageX"),
			startpagey: node.getProperties("startPageY"),
			endpagey: node.getProperties("endPageY")
		}
*/
/*		
		console.log(data.startpagex);
		console.log(data.startpagey);
		console.log("Object " + document.elementFromPoint(data.startpagex, data.startpagey));
		console.log("Object " + document.elementFromPoint(data.endpagex, data.endpagey));
*/

/*
		var div = document.createElement('div');
		div.id = 'block';
		div.className = 'block';
		div.style.position = "absolute";
		div.style.left = data.startpagex+'px';
		div.style.top = data.startpagey+'px';
		div.innerHTML = "*";
		document.getElementsByTagName('body')[0].appendChild(div);

		div = document.createElement('div');
		div.id = 'block';
		div.className = 'block';
		div.style.position = "absolute";
		div.style.left = data.endpagex+'px';
		div.style.top = data.endpagey+'px';
		div.innerHTML = "*";
		document.getElementsByTagName('body')[0].appendChild(div);
*/
/*
		var div = document.createElement('div');
		div.id = 'block';
		div.className = 'block';
		div.style.position = "absolute";
		div.style.left = data.startpagex;
		div.style.top = data.startpagey;
		div.innerHTML = "*";
		document.getElementsByTagName('body')[0].appendChild(div);

		div = document.createElement('div');
		div.id = 'block';
		div.className = 'block';
		div.style.position = "absolute";
		div.style.left = data.endpagex;
		div.style.top = data.endpagey;
		div.innerHTML = "*";
		document.getElementsByTagName('body')[0].appendChild(div);
*/
	});

	document.onmousedown = mouseDown;
	document.onmouseup = mouseUp;

	function mouseDown(event) {
		status = 'mouseDown';
		handleMouseMove(event);
/*
		event = event || window.event;
		var elementId = (event.target || event.srcElement).id;
		console.log("elementId: " + elementId);
*/
	}

	function mouseUp(event) {
		status = 'mouseUp';
		handleMouseMove(event);
/*
		var text = getSelectionText();
		var innerHTML = event.target.innerHTML
		var index = innerHTML.indexOf(text);
		if ( index >= 0 )
		{ 
		innerHTML = innerHTML.substring(0,index) + "<span style='background-color:yellow;'>" + innerHTML.substring(index,index+text.length) + "</span>" + innerHTML.substring(index + text.length);
		event.target.innerHTML = innerHTML 
		}
*/
/*
		event = event || window.event;
		var elementId = (event.target || event.srcElement).id;
		console.log("elementId: " + elementId);
*/
/*
		var text = window.getSelection();
		var rect = text.getRangeAt(0);
		rect = rect.getBoundingClientRect();
		console.log("s: " + text);

                console.log("rect.left: " + rect.left);
                console.log("rect.top: " + rect.top);
                console.log("rect.right: " + rect.right);
                console.log("rect.bottom: " + rect.bottom);
                var w = rect.right - rect.left;
                var h = rect.bottom - rect.top;
                console.log("w: " + w);
                console.log("h: " + h);

		var commandData = {
			buttonCode: event.which,
			startPageX: Math.round(rect.left), 
			startPageY: Math.round(rect.top), 
			endPageX: Math.round(rect.right), 
			endPageY: Math.round(rect.bottom), 
			title: "Untitled",
			text: text.toString(),
			status: status
		};
		self.port.emit("message", commandData);
*/
	}

	function handleMouseMove(event) {
		//console.log("handleMouseMove: " + event.which); 

		var dot, eventDoc, doc, body;//, pageX, pageY;

		event = event || window.event;

		if (event.pageX == null && event.clientX != null) {
			eventDoc = (event.target && event.target.ownerDocument) || document;
			doc = eventDoc.documentElement;
			body = eventDoc.body;

			event.pageX = event.clientX +
				(doc && doc.scrollLeft || body && body.scrollLeft || 0) -
				(doc && doc.clientLeft || body && body.clientLeft || 0);
			event.pageY = event.clientY +
				(doc && doc.scrollTop  || body && body.scrollTop  || 0) -
				(doc && doc.clientTop  || body && body.clientTop  || 0 );
		}

		if(status == 'mouseUp'){
			console.log("pageX: " + pageX); 
			console.log("pageY: " + pageY);
			console.log("event.pageX: " + event.pageX); 
			console.log("event.pageY: " + event.pageY);

			var mouseDownEvent = {
				pageX: pageX,
				pageY: pageY
			};

			var mouseUpEvent = {
				pageX: event.pageX,
				pageY: event.pageY
			};
		
			var text = getSelectionText();

/*
			var commandData = {
				buttonCode: event.which,
				startPageX: pageX, 
				startPageY: pageY, 
				endPageX: event.pageX, 
				endPageY: event.pageY, 
				title: "Untitled",
				text: text,
				status: status
			};
*/
			var coordinates = {
				startPageX: pageX, 
				startPageY: pageY, 
				endPageX: event.pageX, 
				endPageY: event.pageY, 
			}
			var commandData = {
				coordinates: JSON.stringify(coordinates),
				buttonCode: event.which,
				title: "Untitled",
				text: text,
				status: status
			};
			// Verso domainSidePanel.js -> onTabWorkerMessage
			self.port.emit("message", commandData);
		}
		else if(status == 'mouseDown'){
			//console.log("status: " + status); 
			pageX = event.pageX;
			pageY = event.pageY;
			//console.log("pageX: " + pageX); 
		}
	}

	function getSelectionText() {
	    var text = "";
	    if (window.getSelection) {
		text = window.getSelection().toString();
	    } else if (document.selection && document.selection.type != "Control") {
		text = document.selection.createRange().text;
	    }
	    return text;
	}
})();
