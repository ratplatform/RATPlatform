var AddRootDomain = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"16639a6e-4e25-4f1f-95a9-1b23ee9006b1",
		RootVertexUUID:"b5876c07-5714-4f22-ab46-00f072d503cb",
		commandName:"AddRootDomain",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		commandsNodeUUID : {
			VertexInstructionParameterNameField:"commandsNodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"ee71da81-afda-4d96-a06e-98c8a0989373",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		queriesNodeUUID : {
			VertexInstructionParameterNameField:"queriesNodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"917302db-9289-447d-be1b-e58df5b9f1f9",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		}
	}
};

var LoadCommands = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"a805196f-b633-4de5-be42-cade7c2d7707",
		RootVertexUUID:"42fc1097-b340-41a1-8ab2-e4d718ad48b9",
		commandName:"LoadCommands",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		folder : {
			VertexInstructionParameterNameField:"folder",
			ReturnType:"string",
			VertexUUIDField:"8848a932-7598-45fa-aee6-752727ba7463",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		}
	}
};

var LoadQueries = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"0a52285a-e02c-4855-a71d-ee6eda837006",
		RootVertexUUID:"309c67c5-fd6e-4124-8cb6-8a455de32233",
		commandName:"LoadQueries",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		folder : {
			VertexInstructionParameterNameField:"folder",
			ReturnType:"string",
			VertexUUIDField:"7c0e307c-1a1d-4540-8fcb-0b6e2bcec294",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		}
	}
};

var AddRootDomainAdminUser = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"f0adba0f-c04a-4649-a773-bf34f359389f",
		RootVertexUUID:"acecb733-743f-4651-b11a-d1d8cc80ac6c",
		commandName:"AddRootDomainAdminUser",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		isUserOfNodeUUID : {
			VertexInstructionParameterNameField:"isUserOfNodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"9056678d-21a7-407e-a568-95e1d52dcd3a",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			ReturnType:"string",
			VertexUUIDField:"d37358e6-68c7-461e-87c2-ea10b6b8648d",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			ReturnType:"string",
			VertexUUIDField:"dd497e16-73f3-4bba-82cf-6006208d77d9",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userEmail : {
			VertexInstructionParameterNameField:"userEmail",
			ReturnType:"string",
			VertexUUIDField:"ee7cc7a5-4149-4f9b-a756-1c5294e8d5da",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"62226c88-ecec-4bcb-b02b-64f095b20f29",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		}
	}
};

var AddNewUser = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"50a59cf1-8206-4bfd-b69a-c90d5dad8254",
		RootVertexUUID:"c2695baa-ae7d-4055-9a58-6e7ded5ea3e5",
		commandName:"AddNewUser",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		isPutByNode2UUID : {
			VertexInstructionParameterNameField:"isPutByNode2UUID",
			ReturnType:"uuid",
			VertexUUIDField:"d6342ffe-956e-4851-b663-b04145410e0e",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isUserOfNodeUUID : {
			VertexInstructionParameterNameField:"isUserOfNodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"16a98abb-0cee-4c54-a226-711da6d3d188",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			ReturnType:"string",
			VertexUUIDField:"6559e05e-0aa6-475f-988e-81d9eea4fd20",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			ReturnType:"string",
			VertexUUIDField:"5b8c4094-720a-409d-92dc-582392a97f30",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userEmail : {
			VertexInstructionParameterNameField:"userEmail",
			ReturnType:"string",
			VertexUUIDField:"fe07666d-cb97-4455-a342-2d4e8d809452",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"8663aad0-b680-496f-96e4-24b08a0b438f",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		}
	}
};

var AddNewDomain = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"89417949-fa89-4e09-83d1-26918cf2550c",
		RootVertexUUID:"d1cb9a9c-b118-4a9b-bf60-30c36af7b953",
		commandName:"AddNewDomain",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		nodeUUID : {
			VertexInstructionParameterNameField:"nodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"54617e9b-9486-45ea-92c1-e112861760ce",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		domainName : {
			VertexInstructionParameterNameField:"domainName",
			ReturnType:"string",
			VertexUUIDField:"804e326c-bd40-4d8e-b7db-1b46965f3f75",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			ReturnType:"string",
			VertexUUIDField:"3efae6b4-c51f-4edb-a3a5-99d3f4590916",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		}
	}
};

var AddComment = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"692ce95a-cca5-42ba-8da9-477bae5ef209",
		RootVertexUUID:"71d5dcc5-7a38-472d-9a77-291e0b0e4f48",
		commandName:"AddComment",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		endPageY : {
			VertexInstructionParameterNameField:"endPageY",
			ReturnType:"integer",
			VertexUUIDField:"cdd2fdb4-5309-435a-8360-5805422abdd4",
			InstructionOrderField:"3",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		startPageY : {
			VertexInstructionParameterNameField:"startPageY",
			ReturnType:"integer",
			VertexUUIDField:"2980a01e-60a5-44c9-b891-d62e96009939",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		endPageX : {
			VertexInstructionParameterNameField:"endPageX",
			ReturnType:"integer",
			VertexUUIDField:"a4a571a0-0461-418c-8a62-dd097821da1a",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userNodeUUID : {
			VertexInstructionParameterNameField:"userNodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"05bf350b-d450-4877-8e3d-ffc7b30bdbed",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		ownerNodeUUID : {
			VertexInstructionParameterNameField:"ownerNodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"ece40cf9-ca02-4c6c-a58d-ac63bcc7c4e7",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		url : {
			VertexInstructionParameterNameField:"url",
			ReturnType:"url",
			VertexUUIDField:"9b21604c-802b-4b13-b092-4df840cd0882",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		VertexContentField : {
			VertexInstructionParameterNameField:"VertexContentField",
			ReturnType:"string",
			VertexUUIDField:"e87fbdb5-be09-4563-8075-2ff3bdee62bc",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			ReturnType:"string",
			VertexUUIDField:"e175745d-8e6b-45b8-b120-4a8fc0d8e076",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		startPageX : {
			VertexInstructionParameterNameField:"startPageX",
			ReturnType:"integer",
			VertexUUIDField:"3450c078-b752-4837-9a6e-77eb88c40a67",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		}
	}
};

var BindFromUserToDomain = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"67513558-6b5e-4b75-8e0b-72d8ce8495b3",
		RootVertexUUID:"f5d1f288-8e6b-405c-b718-16c6aa9fdb3f",
		commandName:"BindFromUserToDomain",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		domainNodeUUID : {
			VertexInstructionParameterNameField:"domainNodeUUID",
			ReturnType:"uuid",
			VertexUUIDField:"fa34926a-0ac3-40d1-a8cd-4cd446bbbce9",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userUUID : {
			VertexInstructionParameterNameField:"userUUID",
			ReturnType:"uuid",
			VertexUUIDField:"5d491aae-4b38-45a1-8f35-64ca3f085c0f",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		}
	}
};


/*Public functions*/
AddRootDomainSet = function(currentDomainUUID, param0, param1){
	AddRootDomain.settings.commandsNodeUUID.VertexInstructionParameterValueField = param0;
	AddRootDomain.settings.queriesNodeUUID.VertexInstructionParameterValueField = param1;
	AddRootDomain.header.DomainUUID = currentDomainUUID;
};

LoadCommandsSet = function(currentDomainUUID, param0){
	LoadCommands.settings.folder.VertexInstructionParameterValueField = param0;
	LoadCommands.header.DomainUUID = currentDomainUUID;
};

LoadQueriesSet = function(currentDomainUUID, param0){
	LoadQueries.settings.folder.VertexInstructionParameterValueField = param0;
	LoadQueries.header.DomainUUID = currentDomainUUID;
};

AddRootDomainAdminUserSet = function(currentDomainUUID, param0, param1, param2, param3, param4){
	AddRootDomainAdminUser.settings.isUserOfNodeUUID.VertexInstructionParameterValueField = param0;
	AddRootDomainAdminUser.settings.userPwd.VertexInstructionParameterValueField = param1;
	AddRootDomainAdminUser.settings.userName.VertexInstructionParameterValueField = param2;
	AddRootDomainAdminUser.settings.userEmail.VertexInstructionParameterValueField = param3;
	AddRootDomainAdminUser.settings.isPutByNodeUUID.VertexInstructionParameterValueField = param4;
	AddRootDomainAdminUser.header.DomainUUID = currentDomainUUID;
};

AddNewUserSet = function(currentDomainUUID, param0, param1, param2, param3, param4, param5){
	AddNewUser.settings.isPutByNode2UUID.VertexInstructionParameterValueField = param0;
	AddNewUser.settings.isUserOfNodeUUID.VertexInstructionParameterValueField = param1;
	AddNewUser.settings.userPwd.VertexInstructionParameterValueField = param2;
	AddNewUser.settings.userName.VertexInstructionParameterValueField = param3;
	AddNewUser.settings.userEmail.VertexInstructionParameterValueField = param4;
	AddNewUser.settings.isPutByNodeUUID.VertexInstructionParameterValueField = param5;
	AddNewUser.header.DomainUUID = currentDomainUUID;
};

AddNewDomainSet = function(currentDomainUUID, param0, param1, param2){
	AddNewDomain.settings.nodeUUID.VertexInstructionParameterValueField = param0;
	AddNewDomain.settings.domainName.VertexInstructionParameterValueField = param1;
	AddNewDomain.settings.VertexLabelField.VertexInstructionParameterValueField = param2;
	AddNewDomain.header.DomainUUID = currentDomainUUID;
};

AddCommentSet = function(currentDomainUUID, param0, param1, param2, param3, param4, param5, param6, param7, param8){
	AddComment.settings.startPageY.VertexInstructionParameterValueField = param0;
	AddComment.settings.endPageY.VertexInstructionParameterValueField = param1;
	AddComment.settings.endPageX.VertexInstructionParameterValueField = param2;
	AddComment.settings.userNodeUUID.VertexInstructionParameterValueField = param3;
	AddComment.settings.ownerNodeUUID.VertexInstructionParameterValueField = param4;
	AddComment.settings.url.VertexInstructionParameterValueField = param5;
	AddComment.settings.startPageX.VertexInstructionParameterValueField = param6;
	AddComment.settings.VertexLabelField.VertexInstructionParameterValueField = param7;
	AddComment.settings.VertexContentField.VertexInstructionParameterValueField = param8;
	AddComment.header.DomainUUID = currentDomainUUID;
};

BindFromUserToDomainSet = function(currentDomainUUID, param0, param1){
	BindFromUserToDomain.settings.domainNodeUUID.VertexInstructionParameterValueField = param0;
	BindFromUserToDomain.settings.userUUID.VertexInstructionParameterValueField = param1;
	BindFromUserToDomain.header.DomainUUID = currentDomainUUID;
};


