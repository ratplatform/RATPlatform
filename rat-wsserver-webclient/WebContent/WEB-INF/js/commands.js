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
			VertexUUIDField:"61a100b4-2203-4f60-9797-ea6bf776d90a",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		queriesNodeUUID : {
			VertexInstructionParameterNameField:"queriesNodeUUID",
			VertexUUIDField:"77cf03ce-861a-4fa7-bce6-117ad719a576",
			ReturnType:"uuid",
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
			VertexUUIDField:"5ad1298d-dd11-48cf-91df-83751705c1a8",
			ReturnType:"string",
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
			VertexUUIDField:"77352fa8-7b8f-441f-a8e4-270e662203e8",
			ReturnType:"string",
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
			VertexUUIDField:"5f849c60-62ef-4924-8374-c15c04d76710",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			VertexUUIDField:"01be7cc6-42a2-442c-b7bd-268ff7685dfd",
			ReturnType:"string",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			VertexUUIDField:"5de5814e-081a-44ca-8ff2-ea8c2cc18074",
			ReturnType:"string",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userEmail : {
			VertexInstructionParameterNameField:"userEmail",
			VertexUUIDField:"1bdb7a42-c3e9-43a1-9de5-2e2c20392681",
			ReturnType:"string",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			VertexUUIDField:"27338dc1-9ab0-47db-b14c-1b8cb5f44184",
			ReturnType:"uuid",
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
			VertexUUIDField:"ebd53f01-7126-4f17-bf0f-d0ce2363dcae",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isUserOfNodeUUID : {
			VertexInstructionParameterNameField:"isUserOfNodeUUID",
			VertexUUIDField:"e1b9b2e7-e0f4-48a2-8ee5-404d6b0ea163",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			VertexUUIDField:"dac64700-b4ce-4380-b96a-3e0bcabd3005",
			ReturnType:"string",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			VertexUUIDField:"6dbe2ae2-85fe-4af6-9cad-f3b157113d16",
			ReturnType:"string",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userEmail : {
			VertexInstructionParameterNameField:"userEmail",
			VertexUUIDField:"8fa2b868-4d89-4686-8e62-026ede528b06",
			ReturnType:"string",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			VertexUUIDField:"ccb4e4f8-8933-4e39-828e-3c3a814c4d1c",
			ReturnType:"uuid",
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
			VertexUUIDField:"3d951b4c-5629-4378-9f0f-2334b1294d99",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		domainName : {
			VertexInstructionParameterNameField:"domainName",
			VertexUUIDField:"3af73426-640b-4f05-9ae8-a692e41661db",
			ReturnType:"string",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexUUIDField:"59a36477-f870-448b-9c94-edbcedf53cf7",
			ReturnType:"string",
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
		domainUUID : {
			VertexInstructionParameterNameField:"domainUUID",
			VertexUUIDField:"f82bce13-7e6c-469b-b65d-042650bdf25c",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userNodeUUID : {
			VertexInstructionParameterNameField:"userNodeUUID",
			VertexUUIDField:"c6713ab7-6dc9-41e9-a965-c0dc86b8b54d",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		startComment : {
			VertexInstructionParameterNameField:"startComment",
			VertexUUIDField:"60ce177c-f8d9-4a92-8ef1-c229d0682ba3",
			ReturnType:"integer",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		url : {
			VertexInstructionParameterNameField:"url",
			VertexUUIDField:"aae78dcd-2637-46df-bd7c-557017f1d209",
			ReturnType:"url",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		endComment : {
			VertexInstructionParameterNameField:"endComment",
			VertexUUIDField:"f856f52b-1834-4a80-812d-4c757792dc5b",
			ReturnType:"integer",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		VertexContentField : {
			VertexInstructionParameterNameField:"VertexContentField",
			VertexUUIDField:"f7844c10-7517-4098-af90-68d5d4f9c6dc",
			ReturnType:"string",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexUUIDField:"37a8f7d3-6c5b-4f5a-9bef-2e7be2f8fb6f",
			ReturnType:"string",
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
			VertexUUIDField:"dd1576e1-709e-419a-855c-bd2525ed1afb",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userUUID : {
			VertexInstructionParameterNameField:"userUUID",
			VertexUUIDField:"ab4f55ed-a15b-4d3a-927c-b4bf39d6116a",
			ReturnType:"uuid",
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

AddCommentSet = function(currentDomainUUID, param0, param1, param2, param3, param4, param5, param6){
	AddComment.settings.domainUUID.VertexInstructionParameterValueField = param0;
	AddComment.settings.userNodeUUID.VertexInstructionParameterValueField = param1;
	AddComment.settings.startComment.VertexInstructionParameterValueField = param2;
	AddComment.settings.endComment.VertexInstructionParameterValueField = param3;
	AddComment.settings.url.VertexInstructionParameterValueField = param4;
	AddComment.settings.VertexLabelField.VertexInstructionParameterValueField = param5;
	AddComment.settings.VertexContentField.VertexInstructionParameterValueField = param6;
	AddComment.header.DomainUUID = currentDomainUUID;
};

BindFromUserToDomainSet = function(currentDomainUUID, param0, param1){
	BindFromUserToDomain.settings.domainNodeUUID.VertexInstructionParameterValueField = param0;
	BindFromUserToDomain.settings.userUUID.VertexInstructionParameterValueField = param1;
	BindFromUserToDomain.header.DomainUUID = currentDomainUUID;
};


