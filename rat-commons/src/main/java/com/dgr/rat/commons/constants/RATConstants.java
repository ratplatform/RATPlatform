/**
 * @author Daniele Grignani (dgr)
 * @date Jun 27, 2015
 */

package com.dgr.rat.commons.constants;

import java.nio.file.FileSystems;

// TODO: molte sono inutili: da rimuovere le costanti inutilizzate
public class RATConstants {
	public static final String Commands = "Commands";
	public static final String Queries = "Queries";
	
	// Vertex Properties
	public static final String VertexContentField = "VertexContentField";
	public static final String VertexUUIDField = "VertexUUIDField";
	public static final String VertexLabelField = "VertexLabelField"; // Contiene il nome del vertice, es. "is-type-of"
	public static final String GraphCommandOwner = "GraphCommandOwner";
	public static final String IsDeleted = "isDeleted";
	public static final String GraphUUID = "GraphUUID";
	public static final String SubNodes = "subNodes";
	
	// Files & folders
	public static final String ConfigurationFolder = "conf";
	public static final String PropertyFileName = "application.properties";
	public static final String PropertyFile = ConfigurationFolder + FileSystems.getDefault().getSeparator() + PropertyFileName;
	public static final String OrientDBPropertyFile = ConfigurationFolder + FileSystems.getDefault().getSeparator() + "orientdb.properties";
	
	// KeepAlive
	public static final String CorrelationID = "CorrelationID";
	
	// JSON Header
	public static final String CommandVersion = "commandVersion";
	public static final String ApplicationVersion = "applicationVersion";
	public static final String Application = "application";
	public static final String DomainName = "domainName";
	public static final String DomainUUID = "DomainUUID";
	public static final String CommandName = "commandName";
	public static final String CommandType = "commandType";
	public static final String Settings = "settings";
	public static final String Header = "header";
	public static final String CommandGraphUUID = "CommandGraphUUID";
	public static final String RootVertexUUID = "RootVertexUUID";
	public static final String MessageType = "MessageType";
	public static final String StatusCode = "StatusCode";
	public static final String Time = "time";
	public static final String DateFormat = "yyyy-MM-dd HH:mm:ss.SSS"; 
	
	// RATStorageManager/conf/application.properties
	public static final String RootPlatformDomainName = "root.domain.name";
	public static final String DomainPlaceholder = "domain.placeholder";
	public static final String RootPlatformDomainPlaceholder = "root.domain.placeholder";
	public static final String RootDomainUUIDPlaceholder = "root.domain.uuid.placeholder";
//	public static final String SystemCommandsFolder = "systemcommands.folder";
	public static final String CommandTemplatesFolder = "command.templates.folder";
	public static final String QueryTemplatesFolder = "query.templates.folder";
	public static final String CommandsFolder = "commands.folder";
	public static final String QueriesFolder = "queries.folder";
	public static final String ApplicationVersionField = "application.version";
	public static final String ApplicationName = "application.name";
	public static final String StorageType = "storage.type";
	public static final String DBDefaultAdminName = "db.default.admin.name";
	public static final String DBDefaultAdminPwd = "db.default.admin.pwd";
	public static final String DBDefaultAdminEmail = "db.default.admin.email";
	// TODO: temporanei: leggere commento in SystemCommandsInitializer.loadCommandTemplates
	public static final String CommandsTemplateUUID = "commands.template.uuid";
	public static final String QueriesTemplateUUID = "queries.template.uuid";
	public static final String RootPlatformDomainUUID = "root.domain.uuid";
			
	// VertexValue
//	public static final String VertexUUIDField = "VertexUUIDField";
//	public static final String VertexLabelField = "VertexLabelField"; // Contiene il nome del vertice, es. "is-type-of"
//	public static final String GraphCommandOwner = "GraphCommandOwner";
	public static final String VertexContentUndefined = "VertexContentUndefined";
	
	//Instructions
	public static final String VertexInstructionParameterNameField = "VertexInstructionParameterNameField";
	public static final String VertexInstructionParameterValueField = "VertexInstructionParameterValueField";
	public static final String VertexInstructionParameterReturnTypeField = "VertexInstructionParameterReturnTypeField";
	public static final String InstructionOrderField = "InstructionOrderField";
	public static final String MaxNumParameters = "MaxNumParameters";
	public static final String VertexInstructionOwnerNameField = "VertexInstructionOwnerNameField";
	
	// Vertex di tipo Parameters
//	public static final String VertexContentField = "VertexContentField"; // Contiene il contenuto del vertice, che può essere di qualunque tipo, ad es. il valore di un vertex di tipo parametro
	//public static final String VertexContentTypeField = "VertexContentTypeField"; // Tipo contenuto da VertexContentField: VertexContentTypeIntValue, VertexContentTypeStringValue, etc.
	//public static final String VertexContentTypeStringValue = "string";
	//public static final String VertexContentTypeIntValue = "integer";
	
	// Vertex
	//public static final String ObjectID = "ObjectID";
	
	// Edges
	public static final String EdgeInstructionParameter = "EdgeInstructionsParameter";
	public static final String EdgeInstruction = "EdgeInstruction";
	public static final String EdgeUUIDField = "EdgeUUIDField";
	
	// Vertex + Edge
//	public static final String CommandGraphUUID = "CommandGraphUUID";
	
	public static final String RATNodeEdge = "RATNodeEdge";
	
	// VertexRole
//	public static final String VertexRoleField = "VertexRoleField";
	public static final String VertexIsRootField = "VertexIsRootField"; // Può essere "true" o "false", oppure non esserci (in quel caso è false)
//	public static final String VertexRoleValueLeaf = "VertexRoleValueLeaf";
//	public static final String VertexUserName = "VertexUserName";
//	public static final String VertexUserPassword = "VertexUserPassword";
//	public static final String VertexIncrementalID = "VertexIncrementalID";
	
	// VertexType
	// Il primo è il field che deve assumere uno dei valori sottostanti
	public static final String VertexTypeField = "VertexTypeField";
	//public static final String VertexTypeValueSystemKeys = "SystemKey";
	//public static final String VertexTypeValueFunction = "Function";
	//public static final String VertexTypeValueParameter = "Parameter";
	//public static final String VertexTypeValueAttribute = "Attribute";
	//public static final String VertexTypeValueUser = "User";
	//public static final String VertexTypeValuePwd = "Password";
	//public static final String VertexTypeValueDomain = "Domain";
	public static final String VertexTypeValueRootDomain = "RootDomain";
	
	// QueryPivot Vertices
	public static final String TowardNode = "TowardNode";
	public static final String FromNode = "FromNode";
	public static final String QueryName = "QueryName";
//	public static final String StartPipeInstruction = "StartPipeInstruction";
//	public static final String EndPipeInstruction = "EndPipeInstruction";
//	public static final String InternalPipeInstruction = "InternalPipeInstruction";
	public static final String IsRootQueryPivot = "IsRootQueryPivot";
	public static final String QueryPivotEdgeLabel = "QueryPivotEdge";
	public static final String CorrelationKey = "CorrelationKey";
//	public static final String QueryPivotPathLabel = "QueryPivotPath";
	
	//VertexActions
	// Il primo è il field che deve assumere uno dei valori sottostanti
	//public static final String VertexActionField = "VertexActionField";
	//public static final String VertexActionValueCreate = "CreateVertex";
	//public static final String VertexActionValueBind = "Bind";
	//public static final String VertexActionValueBindToRootDomain = "BindToRootDomain";
	
	//public static final String VertexActionValueExecuteStored = "ExecuteStored";
	//public static final String VertexActionValueSetAttribute = "SetAttribute";
	
}
