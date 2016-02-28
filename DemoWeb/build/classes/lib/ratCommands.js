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
			VertexUUIDField:"ca5c3523-e499-4ebd-abf6-ca0e9dccd2ea",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		},
		queriesNodeUUID : {
			VertexInstructionParameterNameField:"queriesNodeUUID",
			VertexUUIDField:"e9fc072c-d195-41ec-abca-2627046d62ac",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
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
			VertexUUIDField:"6d529531-6962-4b26-ae8a-ff369383c9cc",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
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
			VertexUUIDField:"81a6a31d-a5eb-4e66-bed1-82f16a2ba340",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
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
			VertexUUIDField:"dfb791eb-4124-4234-8469-0a5f1ff0d454",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			VertexUUIDField:"2787b45f-db96-4808-bbe0-58f94d7f7ea5",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			VertexUUIDField:"957125f9-2d07-45e4-bff3-adbb8981909b",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		isUserOfNodeUUID : {
			VertexInstructionParameterNameField:"isUserOfNodeUUID",
			VertexUUIDField:"4f4b574d-ca35-44a8-8c59-f8f3bcadfeee",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			VertexUUIDField:"3079af35-ac36-4c78-b8c9-a4fe786dd857",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
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
			VertexUUIDField:"dbf8cc41-2d4e-4501-892f-ee49ec570ef6",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			VertexUUIDField:"e005fce0-df63-4e29-a94d-b6c3ee7a9ad7",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			VertexUUIDField:"0641d8ce-d159-49c8-afee-690706777247",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			VertexUUIDField:"464f3f16-7934-440a-ae93-ec13fc35375c",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		},
		isUserOfNodeUUID : {
			VertexInstructionParameterNameField:"isUserOfNodeUUID",
			VertexUUIDField:"27416684-d68a-4560-9422-412c26202740",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		},
		isPutByNode2UUID : {
			VertexInstructionParameterNameField:"isPutByNode2UUID",
			VertexUUIDField:"8cc65958-c01d-4489-9ffd-88ba6d2ea201",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
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
			VertexUUIDField:"814710d5-aabd-4922-9d06-7f4a1f4c8337",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexUUIDField:"04a7222a-eff3-454f-9773-ddce36fe1697",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		nodeUUID : {
			VertexInstructionParameterNameField:"nodeUUID",
			VertexUUIDField:"cd49f338-7e15-405d-9ff9-769aba57fa77",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
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
			VertexUUIDField:"b331d5bc-1505-4030-bd6c-d74de489f82b",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexUUIDField:"b8e7fffa-39c1-438f-b3f6-06a61d4bbf68",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		VertexContentField : {
			VertexInstructionParameterNameField:"VertexContentField",
			VertexUUIDField:"eb917ff9-18ae-41d1-89ea-2a5a1df3b848",
			InstructionOrderField:"1",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		url : {
			VertexInstructionParameterNameField:"url",
			VertexUUIDField:"a1882343-aea9-4d56-aaec-e0268d7b0931",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"url"
		},
		userNodeUUID : {
			VertexInstructionParameterNameField:"userNodeUUID",
			VertexUUIDField:"36a0c87b-619b-4082-8435-6dd02f2d271d",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		},
		ownerNodeUUID : {
			VertexInstructionParameterNameField:"ownerNodeUUID",
			VertexUUIDField:"e84a0b4b-54ad-437b-9e99-763ebea004de",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
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
			VertexUUIDField:"b9b32100-8a26-4df8-bc78-3d871565003a",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		},
		userUUID : {
			VertexInstructionParameterNameField:"userUUID",
			VertexUUIDField:"97e2c92b-56a0-4b5b-a8c3-b76f563f43ea",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
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


