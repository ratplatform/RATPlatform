var AddRootDomain = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"c38b887c-a61c-4716-a497-6b9a2d4de025",
		RootVertexUUID:"496c2947-3a9b-4f18-83c9-903bb4496480",
		commandName:"AddRootDomain",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		commandsNodeUUID : {
			VertexInstructionParameterNameField:"commandsNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"Bind",
			InstructionOrderField:0
		},
		queriesNodeUUID : {
			VertexInstructionParameterNameField:"queriesNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"Bind",
			InstructionOrderField:1
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
		CommandGraphUUID:"679a302a-8024-41ff-a9da-65233b5a4f4c",
		RootVertexUUID:"bb168097-fbc0-47b4-87d4-4afb81d17a44",
		commandName:"LoadCommands",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		folder : {
			VertexInstructionParameterNameField:"folder",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"LoadCommandsAction",
			InstructionOrderField:0
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
		CommandGraphUUID:"b011abab-c792-4e2a-a1c1-221c5c36f570",
		RootVertexUUID:"22f73e1d-cfb4-4c77-b6bd-0b380e008594",
		commandName:"LoadQueries",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		folder : {
			VertexInstructionParameterNameField:"folder",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"LoadCommandsAction",
			InstructionOrderField:0
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
		CommandGraphUUID:"1844d589-6714-4f00-9ed0-1d37137ea014",
		RootVertexUUID:"87b2f6a9-85bc-46ce-95bf-cb279a9cd2fc",
		commandName:"AddRootDomainAdminUser",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		isUserOfNodeUUID : {
			VertexInstructionParameterNameField:"isUserOfNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"Bind",
			InstructionOrderField:0
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"AddProperties",
			InstructionOrderField:2
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"AddProperties",
			InstructionOrderField:1
		},
		userEmail : {
			VertexInstructionParameterNameField:"userEmail",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"AddProperties",
			InstructionOrderField:0
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"Bind",
			InstructionOrderField:0
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
		CommandGraphUUID:"11133d39-8864-4cc3-9170-087a5a33617a",
		RootVertexUUID:"816adf95-40bf-475b-b160-63224afb5f43",
		commandName:"AddNewDomain",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		nodeUUID : {
			VertexInstructionParameterNameField:"nodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"Bind",
			InstructionOrderField:0
		},
		VertexContentField : {
			VertexInstructionParameterNameField:"VertexContentField",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"AddProperties",
			InstructionOrderField:1
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"AddProperties",
			InstructionOrderField:0
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
		CommandGraphUUID:"96569a3f-fadb-468d-8e46-8a0c24a5ca20",
		RootVertexUUID:"3538074e-9b99-477c-8059-896045ce4aa5",
		commandName:"AddNewUser",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		isPutByNode2UUID : {
			VertexInstructionParameterNameField:"isPutByNode2UUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"Bind",
			InstructionOrderField:0
		},
		isUserOfNodeUUID : {
			VertexInstructionParameterNameField:"isUserOfNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"Bind",
			InstructionOrderField:0
		},
		userPwd : {
			VertexInstructionParameterNameField:"userPwd",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"AddProperties",
			InstructionOrderField:2
		},
		userName : {
			VertexInstructionParameterNameField:"userName",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"AddProperties",
			InstructionOrderField:1
		},
		userEmail : {
			VertexInstructionParameterNameField:"userEmail",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"AddProperties",
			InstructionOrderField:0
		},
		isPutByNodeUUID : {
			VertexInstructionParameterNameField:"isPutByNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"Bind",
			InstructionOrderField:0
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
		CommandGraphUUID:"dd896e61-4232-4545-98c8-b2c99a7afe71",
		RootVertexUUID:"68891c4f-dce7-4c70-930e-ae6e7daf51d4",
		commandName:"AddComment",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		userNodeUUID : {
			VertexInstructionParameterNameField:"userNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"Bind",
			InstructionOrderField:0
		},
		jsonCoordinates : {
			VertexInstructionParameterNameField:"jsonCoordinates",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"AddProperties",
			InstructionOrderField:0
		},
		ownerNodeUUID : {
			VertexInstructionParameterNameField:"ownerNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"BindSubNode",
			InstructionOrderField:0
		},
		url : {
			VertexInstructionParameterNameField:"url",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"url",
			VertexInstructionOwnerNameField:"CreateWebDocument",
			InstructionOrderField:0
		},
		VertexContentField : {
			VertexInstructionParameterNameField:"VertexContentField",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"SetVertexProperty",
			InstructionOrderField:1
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"SetVertexProperty",
			InstructionOrderField:0
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
		CommandGraphUUID:"4d2bbf3f-68cd-4710-a69a-180da3bff449",
		RootVertexUUID:"d92fab56-e098-440e-90ae-e05b7d7bcd32",
		commandName:"BindFromUserToDomain",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		domainNodeUUID : {
			VertexInstructionParameterNameField:"domainNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"BindDomainUser",
			InstructionOrderField:0
		},
		userUUID : {
			VertexInstructionParameterNameField:"userUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"BindDomainUser",
			InstructionOrderField:0
		}
	}
};

var DeleteDomain = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"10dc32cd-5b21-49dc-8ccc-fee6f439c8f7",
		RootVertexUUID:"83a968be-a9e6-4d05-bb3f-a788c714510c",
		commandName:"DeleteDomain",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		parentNodeUUID : {
			VertexInstructionParameterNameField:"parentNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"DeleteNode",
			InstructionOrderField:1
		},
		nodeToDeleteUUID : {
			VertexInstructionParameterNameField:"nodeToDeleteUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"DeleteNode",
			InstructionOrderField:0
		}
	}
};

var EditDomain = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"14d9ebfa-2ecc-48f1-9e74-cfd4ff8e4d3e",
		RootVertexUUID:"c938208b-17f7-49ed-b92b-d5852be92ed1",
		commandName:"EditDomain",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		domainNodeUUID : {
			VertexInstructionParameterNameField:"domainNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"ChangeProperty",
			InstructionOrderField:0
		},
		VertexContentField : {
			VertexInstructionParameterNameField:"VertexContentField",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"ChangeProperty",
			InstructionOrderField:2
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"ChangeProperty",
			InstructionOrderField:1
		}
	}
};

var EditCommentTitle = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"03bd15a5-319a-4f96-a789-57ff5a985843",
		RootVertexUUID:"6a8a6c40-a8e1-4fde-bedc-db8eeafb332f",
		commandName:"EditCommentTitle",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		commentNodeUUID : {
			VertexInstructionParameterNameField:"commentNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"ChangeProperty",
			InstructionOrderField:0
		},
		VertexLabelField : {
			VertexInstructionParameterNameField:"VertexLabelField",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"ChangeProperty",
			InstructionOrderField:1
		}
	}
};

var EditCommentText = {
	header : {
		commandType:"SystemCommands",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"01b49cda-304c-4e08-9f62-ba822ed58959",
		RootVertexUUID:"9d4d3fd6-3485-4d55-bbca-cad99a251cd3",
		commandName:"EditCommentText",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		commentNodeUUID : {
			VertexInstructionParameterNameField:"commentNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"ChangeProperty",
			InstructionOrderField:0
		},
		VertexContentField : {
			VertexInstructionParameterNameField:"VertexContentField",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"ChangeProperty",
			InstructionOrderField:1
		}
	}
};


/*Public functions*/
addRootDomainFunc = function(currentDomainUUID, commandsNodeUUID, queriesNodeUUID){
	AddRootDomain.settings.commandsNodeUUID.VertexInstructionParameterValueField = commandsNodeUUID;
	AddRootDomain.settings.queriesNodeUUID.VertexInstructionParameterValueField = queriesNodeUUID;
	AddRootDomain.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(AddRootDomain);
};

loadCommandsFunc = function(currentDomainUUID, folder){
	LoadCommands.settings.folder.VertexInstructionParameterValueField = folder;
	LoadCommands.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(LoadCommands);
};

loadQueriesFunc = function(currentDomainUUID, folder){
	LoadQueries.settings.folder.VertexInstructionParameterValueField = folder;
	LoadQueries.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(LoadQueries);
};

addRootDomainAdminUserFunc = function(currentDomainUUID, isUserOfNodeUUID, userPwd, userName, userEmail, isPutByNodeUUID){
	AddRootDomainAdminUser.settings.isUserOfNodeUUID.VertexInstructionParameterValueField = isUserOfNodeUUID;
	AddRootDomainAdminUser.settings.userPwd.VertexInstructionParameterValueField = userPwd;
	AddRootDomainAdminUser.settings.userName.VertexInstructionParameterValueField = userName;
	AddRootDomainAdminUser.settings.userEmail.VertexInstructionParameterValueField = userEmail;
	AddRootDomainAdminUser.settings.isPutByNodeUUID.VertexInstructionParameterValueField = isPutByNodeUUID;
	AddRootDomainAdminUser.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(AddRootDomainAdminUser);
};

addNewDomainFunc = function(currentDomainUUID, nodeUUID, vertexContentField, vertexLabelField){
	AddNewDomain.settings.nodeUUID.VertexInstructionParameterValueField = nodeUUID;
	AddNewDomain.settings.VertexContentField.VertexInstructionParameterValueField = vertexContentField;
	AddNewDomain.settings.VertexLabelField.VertexInstructionParameterValueField = vertexLabelField;
	AddNewDomain.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(AddNewDomain);
};

addNewUserFunc = function(currentDomainUUID, isPutByNode2UUID, isUserOfNodeUUID, userPwd, userName, userEmail, isPutByNodeUUID){
	AddNewUser.settings.isPutByNode2UUID.VertexInstructionParameterValueField = isPutByNode2UUID;
	AddNewUser.settings.isUserOfNodeUUID.VertexInstructionParameterValueField = isUserOfNodeUUID;
	AddNewUser.settings.userPwd.VertexInstructionParameterValueField = userPwd;
	AddNewUser.settings.userName.VertexInstructionParameterValueField = userName;
	AddNewUser.settings.userEmail.VertexInstructionParameterValueField = userEmail;
	AddNewUser.settings.isPutByNodeUUID.VertexInstructionParameterValueField = isPutByNodeUUID;
	AddNewUser.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(AddNewUser);
};

addCommentFunc = function(currentDomainUUID, userNodeUUID, jsonCoordinates, ownerNodeUUID, url, vertexContentField, vertexLabelField){
	AddComment.settings.userNodeUUID.VertexInstructionParameterValueField = userNodeUUID;
	AddComment.settings.jsonCoordinates.VertexInstructionParameterValueField = jsonCoordinates;
	AddComment.settings.ownerNodeUUID.VertexInstructionParameterValueField = ownerNodeUUID;
	AddComment.settings.url.VertexInstructionParameterValueField = url;
	AddComment.settings.VertexContentField.VertexInstructionParameterValueField = vertexContentField;
	AddComment.settings.VertexLabelField.VertexInstructionParameterValueField = vertexLabelField;
	AddComment.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(AddComment);
};

bindFromUserToDomainFunc = function(currentDomainUUID, domainNodeUUID, userUUID){
	BindFromUserToDomain.settings.domainNodeUUID.VertexInstructionParameterValueField = domainNodeUUID;
	BindFromUserToDomain.settings.userUUID.VertexInstructionParameterValueField = userUUID;
	BindFromUserToDomain.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(BindFromUserToDomain);
};

deleteDomainFunc = function(currentDomainUUID, parentNodeUUID, nodeToDeleteUUID){
	DeleteDomain.settings.parentNodeUUID.VertexInstructionParameterValueField = parentNodeUUID;
	DeleteDomain.settings.nodeToDeleteUUID.VertexInstructionParameterValueField = nodeToDeleteUUID;
	DeleteDomain.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(DeleteDomain);
};

editDomainFunc = function(currentDomainUUID, domainNodeUUID, vertexContentField, vertexLabelField){
	EditDomain.settings.domainNodeUUID.VertexInstructionParameterValueField = domainNodeUUID;
	EditDomain.settings.VertexContentField.VertexInstructionParameterValueField = vertexContentField;
	EditDomain.settings.VertexLabelField.VertexInstructionParameterValueField = vertexLabelField;
	EditDomain.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(EditDomain);
};

editCommentTitleFunc = function(currentDomainUUID, commentNodeUUID, vertexLabelField){
	EditCommentTitle.settings.commentNodeUUID.VertexInstructionParameterValueField = commentNodeUUID;
	EditCommentTitle.settings.VertexLabelField.VertexInstructionParameterValueField = vertexLabelField;
	EditCommentTitle.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(EditCommentTitle);
};

editCommentTextFunc = function(currentDomainUUID, commentNodeUUID, vertexContentField){
	EditCommentText.settings.commentNodeUUID.VertexInstructionParameterValueField = commentNodeUUID;
	EditCommentText.settings.VertexContentField.VertexInstructionParameterValueField = vertexContentField;
	EditCommentText.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(EditCommentText);
};


