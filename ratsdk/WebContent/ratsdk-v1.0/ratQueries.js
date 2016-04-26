var GetUserByEmail = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"71cdb5ab-f0f6-446f-8831-b64c5cb52796",
		RootVertexUUID:"92ae2be7-ebe0-48d9-be0f-1c620426e5d4",
		commandName:"GetUserByEmail",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		paramValue : {
			VertexInstructionParameterNameField:"paramValue",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"GetSingleNode",
			InstructionOrderField:1
		}
	}
};

var GetRootUserByEmail = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"44befd32-fe71-40b7-98c2-620b92197ee7",
		RootVertexUUID:"0d22c5ff-865b-4f33-8c26-bd12aac404ef",
		commandName:"GetRootUserByEmail",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		paramValue : {
			VertexInstructionParameterNameField:"paramValue",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"GetSingleNode",
			InstructionOrderField:1
		}
	}
};

var GetDomainByName = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"cfc9390c-bb5d-48b1-a22c-cfd161ef8377",
		RootVertexUUID:"37ae152e-7658-432e-aea9-df6027fa5769",
		commandName:"GetDomainByName",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		paramValue : {
			VertexInstructionParameterNameField:"paramValue",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"GetSingleNode",
			InstructionOrderField:1
		}
	}
};

var GetAllUserDomainComments = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"6d7328be-dd92-4bba-86f6-2fc51d9afb50",
		RootVertexUUID:"74da6fda-fcb4-4312-8374-da23d4e3fcfc",
		commandName:"GetAllUserDomainComments",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"StartStep",
			InstructionOrderField:0
		},
		domainUUIDValue : {
			VertexInstructionParameterNameField:"domainUUIDValue",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"HasStep",
			InstructionOrderField:1
		},
		urlValue : {
			VertexInstructionParameterNameField:"urlValue",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"HasStep",
			InstructionOrderField:1
		}
	}
};

var GetAllDomainUsers = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"a02a52cc-4b71-4b69-be2e-17fd35dd94df",
		RootVertexUUID:"e901ecb4-055f-4d8c-8e2a-e6f408d73225",
		commandName:"GetAllDomainUsers",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"StartStep",
			InstructionOrderField:0
		}
	}
};

var GetAllUserDomains = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"acafe167-203b-4765-a316-19bbde25d5a2",
		RootVertexUUID:"647f29ec-f9e5-4e83-a1b1-bb6f81fb98d1",
		commandName:"GetAllUserDomains",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"StartStep",
			InstructionOrderField:0
		}
	}
};

var GetAllUserComments = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"7b76705f-15db-4c34-b423-35a79b8b59e5",
		RootVertexUUID:"358ee500-fe6f-4ac6-98c7-5a82de608fc7",
		commandName:"GetAllUserComments",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"StartStep",
			InstructionOrderField:0
		}
	}
};

var GetUserURLs = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"6f80acf8-2d88-4e3a-ab6a-7c102919274f",
		RootVertexUUID:"68829922-e798-4f4b-8345-518b67be8f16",
		commandName:"GetUserURLs",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"StartStep",
			InstructionOrderField:0
		}
	}
};

var GetCommentComments = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"a0816501-5fca-4bbf-b794-6775d2677d69",
		RootVertexUUID:"1df30284-c674-45a0-ac78-1768e63d3c48",
		commandName:"GetCommentComments",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"StartStep",
			InstructionOrderField:0
		}
	}
};

var GetDomainDomains = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"c4a773ff-f286-4906-8346-85a27323b47e",
		RootVertexUUID:"8270bbca-3980-40cd-ae5e-780690792adc",
		commandName:"GetDomainDomains",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"StartStep",
			InstructionOrderField:0
		}
	}
};

var GetNodeByType = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"bc330da7-0af8-4874-b15e-c76791f16f64",
		RootVertexUUID:"583592d9-a7d8-4892-8dff-a7f42397389b",
		commandName:"GetNodeByType",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		paramValue : {
			VertexInstructionParameterNameField:"paramValue",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"GetSingleNode",
			InstructionOrderField:1
		}
	}
};

var GetUserDomainByName = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"3966690a-4c57-47f0-b1ab-3e07e39cfe7b",
		RootVertexUUID:"49475bd0-6086-43d0-af73-83bf033d2060",
		commandName:"GetUserDomainByName",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"StartStep",
			InstructionOrderField:0
		},
		paramValue : {
			VertexInstructionParameterNameField:"paramValue",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"string",
			VertexInstructionOwnerNameField:"HasStep",
			InstructionOrderField:1
		}
	}
};

var GetGraph = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"eaed5712-b6a4-4ab8-92dd-239cd077e09f",
		RootVertexUUID:"639d631b-6eef-4f58-b800-3bdb6dd286e5",
		commandName:"GetGraph",
		domainName:"@domainPlaceholder@",
		MessageType:"Template"
	},
	settings : {
		paramValue : {
			VertexInstructionParameterNameField:"paramValue",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			VertexInstructionParameterReturnTypeField:"uuid",
			VertexInstructionOwnerNameField:"GetGraph",
			InstructionOrderField:1
		}
	}
};


/*Public functions*/
getUserByEmailFunc = function(currentDomainUUID, paramValue){
	GetUserByEmail.settings.paramValue.VertexInstructionParameterValueField = paramValue;
	GetUserByEmail.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetUserByEmail);
};

getRootUserByEmailFunc = function(currentDomainUUID, paramValue){
	GetRootUserByEmail.settings.paramValue.VertexInstructionParameterValueField = paramValue;
	GetRootUserByEmail.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetRootUserByEmail);
};

getDomainByNameFunc = function(currentDomainUUID, paramValue){
	GetDomainByName.settings.paramValue.VertexInstructionParameterValueField = paramValue;
	GetDomainByName.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetDomainByName);
};

getAllUserDomainCommentsFunc = function(currentDomainUUID, rootNodeUUID, domainUUIDValue, urlValue){
	GetAllUserDomainComments.settings.rootNodeUUID.VertexInstructionParameterValueField = rootNodeUUID;
	GetAllUserDomainComments.settings.domainUUIDValue.VertexInstructionParameterValueField = domainUUIDValue;
	GetAllUserDomainComments.settings.urlValue.VertexInstructionParameterValueField = urlValue;
	GetAllUserDomainComments.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetAllUserDomainComments);
};

getAllDomainUsersFunc = function(currentDomainUUID, rootNodeUUID){
	GetAllDomainUsers.settings.rootNodeUUID.VertexInstructionParameterValueField = rootNodeUUID;
	GetAllDomainUsers.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetAllDomainUsers);
};

getAllUserDomainsFunc = function(currentDomainUUID, rootNodeUUID){
	GetAllUserDomains.settings.rootNodeUUID.VertexInstructionParameterValueField = rootNodeUUID;
	GetAllUserDomains.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetAllUserDomains);
};

getAllUserCommentsFunc = function(currentDomainUUID, rootNodeUUID){
	GetAllUserComments.settings.rootNodeUUID.VertexInstructionParameterValueField = rootNodeUUID;
	GetAllUserComments.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetAllUserComments);
};

getUserURLsFunc = function(currentDomainUUID, rootNodeUUID){
	GetUserURLs.settings.rootNodeUUID.VertexInstructionParameterValueField = rootNodeUUID;
	GetUserURLs.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetUserURLs);
};

getCommentCommentsFunc = function(currentDomainUUID, rootNodeUUID){
	GetCommentComments.settings.rootNodeUUID.VertexInstructionParameterValueField = rootNodeUUID;
	GetCommentComments.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetCommentComments);
};

getDomainDomainsFunc = function(currentDomainUUID, rootNodeUUID){
	GetDomainDomains.settings.rootNodeUUID.VertexInstructionParameterValueField = rootNodeUUID;
	GetDomainDomains.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetDomainDomains);
};

getNodeByTypeFunc = function(currentDomainUUID, paramValue){
	GetNodeByType.settings.paramValue.VertexInstructionParameterValueField = paramValue;
	GetNodeByType.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetNodeByType);
};

getUserDomainByNameFunc = function(currentDomainUUID, rootNodeUUID, paramValue){
	GetUserDomainByName.settings.rootNodeUUID.VertexInstructionParameterValueField = rootNodeUUID;
	GetUserDomainByName.settings.paramValue.VertexInstructionParameterValueField = paramValue;
	GetUserDomainByName.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetUserDomainByName);
};

getGraphFunc = function(currentDomainUUID, paramValue){
	GetGraph.settings.paramValue.VertexInstructionParameterValueField = paramValue;
	GetGraph.header.DomainUUID = currentDomainUUID;

	return JSON.stringify(GetGraph);
};


