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
			VertexUUIDField:"f1e5ecaa-6234-4a01-ba56-47563ba605d8",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		queriesNodeUUID : {
			VertexInstructionParameterNameField:"queriesNodeUUID",
			VertexUUIDField:"dc4e06f5-b5c0-4aca-8039-4b313cee89d9",
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
			VertexUUIDField:"99092efd-a481-42b2-97a6-d72836598999",
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
			VertexUUIDField:"8b090d85-d382-49a9-8be1-056d6d5d5c31",
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
		userEmail : {
			VertexInstructionParameterNameField:"userEmail",
			VertexUUIDField:"97dda050-87c4-4b64-b0da-361cd361a49f",
			ReturnType:"string",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			VertexUUIDField:"f6b12a46-ee8f-4115-92dc-cb3b0c1161a5",
			ReturnType:"string",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			VertexUUIDField:"f5cedc81-d184-4b89-97b5-a520f305eed4",
			ReturnType:"string",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isUserOfNodeUUID : {
			VertexInstructionParameterNameField:"isUserOfNodeUUID",
			VertexUUIDField:"22cf2dc5-413f-427f-9c1c-23479599dfab",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			VertexUUIDField:"e8c7e16e-f8bd-4080-a82a-24da4ddb0dd9",
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
		userEmail : {
			VertexInstructionParameterNameField:"userEmail",
			VertexUUIDField:"daf23f87-eb98-4328-b185-521a4f278d55",
			ReturnType:"string",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			VertexUUIDField:"b1863601-fcfb-4c10-9ebe-3f75e3919b90",
			ReturnType:"string",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			VertexUUIDField:"7b77af84-5dd8-4a03-b902-c924630444cc",
			ReturnType:"string",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			VertexUUIDField:"c6111937-bd5b-4f42-b95a-07b67b191faa",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isUserOfNodeUUID : {
			VertexInstructionParameterNameField:"isUserOfNodeUUID",
			VertexUUIDField:"f54d6b4e-bf08-45b7-8a4f-595497da8608",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		isPutByNode2UUID : {
			VertexInstructionParameterNameField:"isPutByNode2UUID",
			VertexUUIDField:"bbceb532-c325-4362-af27-d6437e757b3b",
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
		domainName : {
			VertexInstructionParameterNameField:"domainName",
			VertexUUIDField:"5280b584-c1d9-48f3-9afe-4069e00e3d3e",
			ReturnType:"string",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexUUIDField:"8e4a04da-ac86-4918-b5bd-31194352eeca",
			ReturnType:"string",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		nodeUUID : {
			VertexInstructionParameterNameField:"nodeUUID",
			VertexUUIDField:"7267fefd-c9f1-4836-9eb4-cd576cff5f53",
			ReturnType:"uuid",
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
		jsonCoordinates : {
			VertexInstructionParameterNameField:"jsonCoordinates",
			VertexUUIDField:"cfa273a7-365f-47b2-a61b-bee972664107",
			ReturnType:"string",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexUUIDField:"487a9c70-e8aa-4b67-a2f4-d206d3c4b142",
			ReturnType:"string",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		VertexContentField : {
			VertexInstructionParameterNameField:"VertexContentField",
			VertexUUIDField:"60e03ecb-ae63-40eb-a546-604fc0b4ebd0",
			ReturnType:"string",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		url : {
			VertexInstructionParameterNameField:"url",
			VertexUUIDField:"e65b04f8-25b7-4b64-9acd-d2e9fe906940",
			ReturnType:"url",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userNodeUUID : {
			VertexInstructionParameterNameField:"userNodeUUID",
			VertexUUIDField:"ba52737e-b13d-475b-92a6-6059616cca0d",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		ownerNodeUUID : {
			VertexInstructionParameterNameField:"ownerNodeUUID",
			VertexUUIDField:"a7ed4ca3-856a-45af-89e4-344d00b6f077",
			ReturnType:"uuid",
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
			VertexUUIDField:"d7892d31-148f-4b0b-9c91-8b3c1080dcdf",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		},
		userUUID : {
			VertexInstructionParameterNameField:"userUUID",
			VertexUUIDField:"a2cba07e-0534-4e26-8fa9-5ada6dc036c0",
			ReturnType:"uuid",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined"
		}
	}
};


/*Public functions*/
AddRootDomainSet = function(currentDomainUUID, commandsnodeuuid, queriesnodeuuid){
	AddRootDomain.settings.commandsNodeUUID.VertexInstructionParameterValueField = commandsnodeuuid;
	AddRootDomain.settings.queriesNodeUUID.VertexInstructionParameterValueField = queriesnodeuuid;
	AddRootDomain.header.DomainUUID = currentDomainUUID;
};

LoadCommandsSet = function(currentDomainUUID, folder){
	LoadCommands.settings.folder.VertexInstructionParameterValueField = folder;
	LoadCommands.header.DomainUUID = currentDomainUUID;
};

LoadQueriesSet = function(currentDomainUUID, folder){
	LoadQueries.settings.folder.VertexInstructionParameterValueField = folder;
	LoadQueries.header.DomainUUID = currentDomainUUID;
};

AddRootDomainAdminUserSet = function(currentDomainUUID, useremail, username, userpwd, isuserofnodeuuid, isputbynodeuuid){
	AddRootDomainAdminUser.settings.userEmail.VertexInstructionParameterValueField = useremail;
	AddRootDomainAdminUser.settings.userName.VertexInstructionParameterValueField = username;
	AddRootDomainAdminUser.settings.userPwd.VertexInstructionParameterValueField = userpwd;
	AddRootDomainAdminUser.settings.isUserOfNodeUUID.VertexInstructionParameterValueField = isuserofnodeuuid;
	AddRootDomainAdminUser.settings.isPutByNodeUUID.VertexInstructionParameterValueField = isputbynodeuuid;
	AddRootDomainAdminUser.header.DomainUUID = currentDomainUUID;
};

AddNewUserSet = function(currentDomainUUID, useremail, username, userpwd, isputbynodeuuid, isuserofnodeuuid, isputbynode2uuid){
	AddNewUser.settings.userEmail.VertexInstructionParameterValueField = useremail;
	AddNewUser.settings.userName.VertexInstructionParameterValueField = username;
	AddNewUser.settings.userPwd.VertexInstructionParameterValueField = userpwd;
	AddNewUser.settings.isPutByNodeUUID.VertexInstructionParameterValueField = isputbynodeuuid;
	AddNewUser.settings.isUserOfNodeUUID.VertexInstructionParameterValueField = isuserofnodeuuid;
	AddNewUser.settings.isPutByNode2UUID.VertexInstructionParameterValueField = isputbynode2uuid;
	AddNewUser.header.DomainUUID = currentDomainUUID;
};

AddNewDomainSet = function(currentDomainUUID, domainname, vertexlabelfield, nodeuuid){
	AddNewDomain.settings.domainName.VertexInstructionParameterValueField = domainname;
	AddNewDomain.settings.VertexLabelField.VertexInstructionParameterValueField = vertexlabelfield;
	AddNewDomain.settings.nodeUUID.VertexInstructionParameterValueField = nodeuuid;
	AddNewDomain.header.DomainUUID = currentDomainUUID;
};

AddCommentSet = function(currentDomainUUID, jsoncoordinates, vertexlabelfield, vertexcontentfield, url, usernodeuuid, ownernodeuuid){
	AddComment.settings.jsonCoordinates.VertexInstructionParameterValueField = jsoncoordinates;
	AddComment.settings.VertexLabelField.VertexInstructionParameterValueField = vertexlabelfield;
	AddComment.settings.VertexContentField.VertexInstructionParameterValueField = vertexcontentfield;
	AddComment.settings.url.VertexInstructionParameterValueField = url;
	AddComment.settings.userNodeUUID.VertexInstructionParameterValueField = usernodeuuid;
	AddComment.settings.ownerNodeUUID.VertexInstructionParameterValueField = ownernodeuuid;
	AddComment.header.DomainUUID = currentDomainUUID;
};

BindFromUserToDomainSet = function(currentDomainUUID, domainnodeuuid, useruuid){
	BindFromUserToDomain.settings.domainNodeUUID.VertexInstructionParameterValueField = domainnodeuuid;
	BindFromUserToDomain.settings.userUUID.VertexInstructionParameterValueField = useruuid;
	BindFromUserToDomain.header.DomainUUID = currentDomainUUID;
};


