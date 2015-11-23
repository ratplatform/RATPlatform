var GetAllAdminUsers = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"e2b8d5f3-43d4-40ab-8e77-3ffca7257650",
		RootVertexUUID:"866bcc23-53b1-4992-9406-cf0f59dfd4ac",
		commandName:"GetAllAdminUsers",
		domainName:"@domainPlaceholder@",
		MessageType:"Request"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexUUIDField:"3b426079-204e-420f-8ac5-ecd6409900e7",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		}
	}
};

var GetAdminUsersByEmail = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"49103a7d-5ac7-4827-8af9-5d9737befa65",
		RootVertexUUID:"480be276-5c7e-407c-b013-0a6702a097ca",
		commandName:"GetAdminUsersByEmail",
		domainName:"@domainPlaceholder@",
		MessageType:"Request"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexUUIDField:"2712e34e-2bd3-4161-9024-d58399f47515",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		},
		userEmail : {
			VertexInstructionParameterNameField:"userEmail",
			VertexUUIDField:"8ddbdf7f-c598-41a3-804e-dd9e2c491b8d",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		}
	}
};

var GetAllDomainComments = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"91eb42ac-2834-45ec-a1b1-93b85dabd824",
		RootVertexUUID:"b9b763c7-6511-4ae6-a458-fd72e796e283",
		commandName:"GetAllDomainComments",
		domainName:"@domainPlaceholder@",
		MessageType:"Request"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexUUIDField:"5c494815-d894-40ed-a741-dc229e19cf4e",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
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
		CommandGraphUUID:"3b91b726-9c69-4f76-9b61-d6366251201c",
		RootVertexUUID:"ac885cb8-7a84-4264-956d-4d158c06fe5a",
		commandName:"GetAllUserComments",
		domainName:"@domainPlaceholder@",
		MessageType:"Request"
	},
	settings : {
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexUUIDField:"5eb52706-0f41-43e7-9fdd-849fcab9be26",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		}
	}
};


/*Public functions*/
GetAllAdminUsersSet = function(currentDomainUUID, param0){
	GetAllAdminUsers.settings.rootNodeUUID.VertexInstructionParameterValueField = param0;
	GetAllAdminUsers.header.DomainUUID = currentDomainUUID;
};

GetAdminUsersByEmailSet = function(currentDomainUUID, param0, param1){
	GetAdminUsersByEmail.settings.rootNodeUUID.VertexInstructionParameterValueField = param0;
	GetAdminUsersByEmail.settings.userEmail.VertexInstructionParameterValueField = param1;
	GetAdminUsersByEmail.header.DomainUUID = currentDomainUUID;
};

GetAllDomainCommentsSet = function(currentDomainUUID, param0){
	GetAllDomainComments.settings.rootNodeUUID.VertexInstructionParameterValueField = param0;
	GetAllDomainComments.header.DomainUUID = currentDomainUUID;
};

GetAllUserCommentsSet = function(currentDomainUUID, param0){
	GetAllUserComments.settings.rootNodeUUID.VertexInstructionParameterValueField = param0;
	GetAllUserComments.header.DomainUUID = currentDomainUUID;
};


