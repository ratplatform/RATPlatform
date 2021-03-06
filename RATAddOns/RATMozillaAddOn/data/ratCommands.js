/*
{
  "settings" : {
    "nodeUUID" : {
      "VertexInstructionParameterNameField" : "nodeUUID",
      "VertexUUIDField" : "d354f6ab-b4dd-4ebb-98d3-638d0a57f0c6",
      "ReturnType" : "uuid",
      "InstructionOrderField" : 0,
      "VertexInstructionParameterValueField" : "VertexContentUndefined"
    },
    "domainName" : {
      "VertexInstructionParameterNameField" : "domainName",
      "VertexUUIDField" : "ed705af2-bbbd-4ebb-ac3c-a0e3727b05d8",
      "ReturnType" : "string",
      "InstructionOrderField" : 0,
      "VertexInstructionParameterValueField" : "VertexContentUndefined"
    },
    "VertexLabelField" : {
      "VertexInstructionParameterNameField" : "VertexLabelField",
      "VertexUUIDField" : "b6cf1336-247a-42a6-8ee8-f01765ff87ae",
      "ReturnType" : "string",
      "InstructionOrderField" : 0,
      "VertexInstructionParameterValueField" : "VertexContentUndefined"
    }
  },
  "header" : {
    "commandType" : "SystemCommands",
    "DomainUUID" : "null",
    "applicationVersion" : "2.0",
    "application" : "RATPlatform",
    "time" : "2015-11-30 23:57:12.929",
    "commandVersion" : "0.1",
    "CommandGraphUUID" : "89417949-fa89-4e09-83d1-26918cf2550c",
    "RootVertexUUID" : "d1cb9a9c-b118-4a9b-bf60-30c36af7b953",
    "commandName" : "AddNewDomain",
    "domainName" : "@domainPlaceholder@",
    "MessageType" : "Template"
  }
}
*/
var AddNewCommand = {
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
			VertexInstructionParameterNameField : "nodeUUID",
			VertexUUIDField : "d354f6ab-b4dd-4ebb-98d3-638d0a57f0c6",
			ReturnType : "uuid",
			InstructionOrderField : 0,
			VertexInstructionParameterValueField : "VertexContentUndefined"
		},
		domainName : {
			VertexInstructionParameterNameField : "domainName",
			VertexUUIDField : "ed705af2-bbbd-4ebb-ac3c-a0e3727b05d8",
			ReturnType : "string",
			InstructionOrderField : 0,
			VertexInstructionParameterValueField : "VertexContentUndefined"
		},
		VertexLabelField : {
			VertexInstructionParameterNameField : "VertexLabelField",
			VertexUUIDField : "b6cf1336-247a-42a6-8ee8-f01765ff87ae",
			ReturnType : "string",
			InstructionOrderField : 0,
			VertexInstructionParameterValueField : "VertexContentUndefined"
		}
	}
};

/*Public functions*/
addNewCommand = function(currentDomainUUID, param0, param1, param2){
	AddNewCommand.settings.nodeUUID.VertexInstructionParameterValueField = param0;
	AddNewCommand.settings.domainName.VertexInstructionParameterValueField = param1;
	AddNewCommand.settings.VertexLabelField.VertexInstructionParameterValueField = param2;
	AddNewCommand.header.DomainUUID = currentDomainUUID;
};

//exports.addNewCommand = addNewCommand;
//exports.AddNewCommand = AddNewCommand;
