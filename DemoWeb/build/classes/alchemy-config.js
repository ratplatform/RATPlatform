var alchemyConfig = {
	dataSource: null,
	forceLocked: true,
	directedEdges: true,
	backgroundColour: "#FFFFFF",
      graphHeight: function(){ return $("#alchemy").parent().height(); },
      graphWidth: function(){ return  $("#alchemy").parent().width(); },   
	linkDistance: function(){ return 50; },
	nodeTypes: {
		"VertexTypeField":[ 
			"SystemKey",
			"RootDomain",
			"InstructionParameter",
			"Instruction",
			"UserPwd",
			"QueryPivot"
		]
	},
	nodeStyle: {
		"all": {
			"borderColor": "#127DC1",
			"color": function(node){
				var properties = node.getProperties();
				if (properties.hasOwnProperty("VertexIsRootField") && 
				properties["VertexIsRootField"] == true) {			
					return "#46C7C7";
				}
				else{
					return "#4863A0";
				}
			},
			"radius": function(node) {
				var properties = node.getProperties();
				if (properties.hasOwnProperty("VertexIsRootField") && 
				properties["VertexIsRootField"] == true) {			
					return 15;
				}
				else{
					return 10;
				}
			}, 
		},
		"RootDomain": {
			"color"      : "#00CC00"
		},
		"SystemKey":{
			"color"      : "#FFCC99"
		},
		"InstructionParameter": {
			"color"      : "#FFCCCC"
		},
		"Instruction": {
			"color"      : "#E77471"
		},
		"QueryPivot": {
			"color"      : "#9999FF"
		}

	},
	edgeStyle: {
		"all": {
			"width": function(edge) {
				var properties = edge.getProperties();
				if (properties.hasOwnProperty("caption") && 
				properties["caption"] == "Instruction" ||
				properties["caption"] == "UserCommandsInstructionsParameter" ) {			
					return 2;
				}
				else{
					return 4;
				}
			},
			"color": function(edge) {
				var properties = edge.getProperties();
				if (properties.hasOwnProperty("caption") && 
				properties["caption"] == "Instruction" ||
				properties["caption"] == "UserCommandsInstructionsParameter" ) {			
					return "#888888";
				}
				else{
					return "#A80000";
				}
			}
		}
	},
	nodeCaptionsOnByDefault: true,
	nodeCaption: function(node){ 
		return node.VertexLabelField;
	},
	nodeMouseOver: function(node){
		var text = "";
		var properties = node.getProperties();
		for (var property in properties) {
			if (properties.hasOwnProperty(property)) {
			text += property + ": " + properties[property] + '<br />';
			}
		}

		tooltip(text);
	},
	nodeMouseOut:function(node){
		exit();
	}
};

