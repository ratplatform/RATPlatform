var GetAllDomainUsers = {
	header : {
		commandType:"Query",
		DomainUUID:"null",
		applicationVersion:"2.0",
		application:"RATPlatform",
		time:new Date().toUTCString(),
		commandVersion:"0.1",
		CommandGraphUUID:"43497ee2-9abe-4120-866e-ab0f1d822fb5",
		RootVertexUUID:"e9acc4f4-929f-4684-afd0-2fc5cc04be78",
		commandName:"GetAllDomainUsers",
		domainName:"@domainPlaceholder@",
		MessageType:"Request"
	},
	settings : {
		VertexTypeField : {
			VertexInstructionParameterNameField:"VertexTypeField",
			VertexUUIDField:"10ebd9ac-0caa-4cd2-bc22-25ccd5a62021",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexUUIDField:"230c2dee-d9ed-405f-b0b8-044402c53ece",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
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
		CommandGraphUUID:"07e8fe20-0e0d-4820-baf5-2a51e86d23e7",
		RootVertexUUID:"7776cb5f-b2d8-4dc9-9663-d3353535f0a7",
		commandName:"GetAllUserDomains",
		domainName:"@domainPlaceholder@",
		MessageType:"Request"
	},
	settings : {
		VertexTypeField : {
			VertexInstructionParameterNameField:"VertexTypeField",
			VertexUUIDField:"88b8641a-5773-4128-9623-af8cc8ecb4e0",
			InstructionOrderField:"2",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"string"
		},
		rootNodeUUID : {
			VertexInstructionParameterNameField:"rootNodeUUID",
			VertexUUIDField:"efc5eaac-95a2-4dbd-99f5-90abfac0f706",
			InstructionOrderField:"0",
			VertexInstructionParameterValueField:"VertexContentUndefined",
			ReturnType:"uuid"
		}
	}
};


/*Public functions*/
GetAllDomainUsersSet = function(currentDomainUUID, param0, param1){
	GetAllDomainUsers.settings.VertexTypeField.VertexInstructionParameterValueField = param0;
	GetAllDomainUsers.settings.rootNodeUUID.VertexInstructionParameterValueField = param1;
	GetAllDomainUsers.header.DomainUUID = currentDomainUUID;
};

GetAllUserDomainsSet = function(currentDomainUUID, param0, param1){
	GetAllUserDomains.settings.VertexTypeField.VertexInstructionParameterValueField = param0;
	GetAllUserDomains.settings.rootNodeUUID.VertexInstructionParameterValueField = param1;
	GetAllUserDomains.header.DomainUUID = currentDomainUUID;
};


