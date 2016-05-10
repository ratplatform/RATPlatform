package org.rat.platform.rat_graph_generator;

import org.rat.platform.command.nodes.Node;
import org.rat.platform.command.nodes.RootNode;
import org.rat.platform.command.nodes.SystemKey;
import org.rat.platform.graph.CommandGraph;
import org.rat.platform.graph.visitor.InstructionParamsDriller;
import org.rat.platform.query.nodes.RootQueryPivot;

import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.AppProperties;

public class Commands {
	private String _ratStorageManagerPath = null;
	private BuildQueryJavaScript _buildQueryJavaScript = null;
	private AlchemyManager _alchemyManager = null;
	
	public Commands(AlchemyManager alchemyManager, String ratStorageManagerPath, BuildQueryJavaScript buildQueryJavaScript) {
		_alchemyManager = alchemyManager;
		_ratStorageManagerPath = ratStorageManagerPath;
		_buildQueryJavaScript = buildQueryJavaScript;
	}
	
	// TODO: modifico anche il contenuto di RATConstants.VertexContentField, ma lo posso fare via JavaScript usando un solo parametro per entrambi
	// quando i JavaScript non verranno più creati automaticamente
	public CommandGraph editDomain(String commandName, String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(VertexType.Domain.toString(), VertexType.Domain.toString(), VertexType.Domain);
    	rootNode.addInstruction("ChangeProperty", "domainNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	rootNode.addInstruction("ChangeProperty", RATConstants.VertexLabelField, RATConstants.VertexContentUndefined, ReturnType.string);
    	rootNode.addInstruction("ChangeProperty", RATConstants.VertexContentField, RATConstants.VertexContentUndefined, ReturnType.string);
    	
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
	}
	
	public CommandGraph editCommentTitle(String commandName, String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(VertexType.Comment.toString(), VertexType.Comment.toString(), VertexType.Comment);
    	rootNode.addInstruction("ChangeProperty", "commentNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	rootNode.addInstruction("ChangeProperty", RATConstants.VertexLabelField, RATConstants.VertexContentUndefined, ReturnType.string);
    	
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
	}
	
	public CommandGraph editCommentText(String commandName, String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(VertexType.Comment.toString(), VertexType.Comment.toString(), VertexType.Comment);
    	rootNode.addInstruction("ChangeProperty", "commentNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	rootNode.addInstruction("ChangeProperty", RATConstants.VertexContentField, RATConstants.VertexContentUndefined, ReturnType.string);
    	
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
	}
	
    public CommandGraph bindFromUserToDomain(String commandName, String commandVersion) throws Exception{
    	RootNode isUserOfNode = new RootNode("is-user-of", "is-user-of", VertexType.SystemKey);
		isUserOfNode.addCreateCommandRootVertexInstruction("nodeName", "is-user-of", ReturnType.string);
		// COMMENT: bind verso il DomainNode
		isUserOfNode.addInstruction("BindDomainUser", "domainNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		SystemKey isPutByNode = new SystemKey("is-put-by", "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso lo user
		isPutByNode.addInstruction("BindDomainUser", "userUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		isUserOfNode.addChild(isPutByNode);
		
		CommandGraph graph = new CommandGraph(isUserOfNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph deleteDomain(String commandName, String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(VertexType.Domain.toString(), VertexType.Domain.toString(), VertexType.Domain);
    	// COMMENT: il primo è sempre l'UUID del nodo da cancellare 
    	rootNode.addInstruction("DeleteNode", "nodeToDeleteUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	// COMMENT: il secondo è sempre l'UUID del nodo parentn del nodo da cancellare
    	rootNode.addInstruction("DeleteNode", "parentNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	// COMMENT: il terzo definisce sempre lo stato del nodo (true/false)
    	rootNode.addInstruction("DeleteNode", RATConstants.IsDeleted, "true", ReturnType.bool);
    	
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph addComment(String commandName, String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(VertexType.Comment.toString(), VertexType.Comment.toString(), VertexType.Comment);
		rootNode.addCreateCommandRootVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
		rootNode.addPropertyVertexInstruction("jsonCoordinates", RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addInstruction("SetVertexProperty", RATConstants.VertexLabelField, RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addInstruction("SetVertexProperty", RATConstants.VertexContentField, RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addCommandNodeProperty(RATConstants.SubNodes, 0);
		
		SystemKey isPutByNode = new SystemKey("is-put-by", "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso il nodo dell'utente
		isPutByNode.addBindInstruction("userNodeUUID", RATConstants.VertexContentUndefined);
		
		SystemKey belongsTo = new SystemKey("belongs-to", "belongs-to");
		belongsTo.addCreateVertexInstruction("nodeName", "belongs-to", ReturnType.string);
		// COMMENT: bind verso il nodo dell'owner (dominio)
		belongsTo.addInstruction("BindSubNode", "ownerNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		SystemKey isPublic = new SystemKey("is-public", "is-public");
		isPublic.addCreateVertexInstruction("nodeName", "is-public", ReturnType.string);
		
		SystemKey isUniversal = new SystemKey("is-universal", "is-universal");
		isUniversal.addCreateVertexInstruction("nodeName", "is-universal", ReturnType.string);
		
		Node webDoc = new Node(VertexType.URI, "webDocument", "webDocument");
		webDoc.addInstruction("CreateWebDocument", "url", RATConstants.VertexContentUndefined, ReturnType.url);
		
		SystemKey isLinkedTo = new SystemKey("is-linked-to", "is-linked-to");
		isLinkedTo.addCreateVertexInstruction("nodeName", "is-linked-to", ReturnType.string);
		isLinkedTo.addBindInstruction("userNodeUUID", RATConstants.VertexContentUndefined);
		webDoc.addChild(isLinkedTo);
		
		rootNode.addChild(isPutByNode);
		rootNode.addChild(belongsTo);
		rootNode.addChild(isPublic);
		rootNode.addChild(isUniversal);
		rootNode.addChild(webDoc);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph addNewDomain(String commandName, String commandVersion) throws Exception{
		RootNode rootNode = new RootNode(VertexType.Domain.toString(), VertexType.Domain.toString(), VertexType.Domain);
		rootNode.addCreateCommandRootVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
		rootNode.addInstruction("InitDomain");
		rootNode.addPropertyVertexInstruction(RATConstants.VertexLabelField, RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addPropertyVertexInstruction(RATConstants.VertexContentField, RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addCommandNodeProperty(RATConstants.SubNodes, 0);
		
		SystemKey isDomain = new SystemKey("is-domain",  "is-domain");
		isDomain.addCreateVertexInstruction("nodeName", "is-domain", ReturnType.string);
		
		SystemKey isPutByNode = new SystemKey("is-put-by",  "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		
		SystemKey isPutByNode2 = new SystemKey("is-put-by",  "is-put-by");
		isPutByNode2.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: Bind to RootDomain RAT
		isPutByNode2.addBindInstruction("nodeUUID", RATConstants.VertexContentUndefined);
		
		rootNode.addChild(isDomain);
		rootNode.addChild(isPutByNode2);
		isDomain.addChild(isPutByNode);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph addRootDomainAdminUser(String commandName, String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(VertexType.RootAdminUser.toString(), VertexType.RootAdminUser.toString(), VertexType.RootAdminUser);
    	rootNode.addCreateCommandRootVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
		rootNode.addPropertyVertexInstruction("userEmail", RATConstants.VertexContentUndefined, ReturnType.string, true);
		rootNode.addPropertyVertexInstruction("userName", RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addPropertyVertexInstruction("userPwd", RATConstants.VertexContentUndefined, ReturnType.string);
		
    	SystemKey isUserOf = new SystemKey("is-user-of", "is-user-of");
    	isUserOf.addCreateVertexInstruction("nodeName", isUserOf.getContent(), ReturnType.string);
    	isUserOf.addBindInstruction("isUserOfNodeUUID", RATConstants.VertexContentUndefined);
    	
    	SystemKey isPutBy = new SystemKey("is-put-by", "is-put-by");
    	isPutBy.addCreateVertexInstruction("nodeName", isPutBy.getContent(), ReturnType.string);
    	isPutBy.addBindInstruction("isPutByNodeUUID", RATConstants.VertexContentUndefined);
    	
		SystemKey isUser = new SystemKey("is-user",  "is-user");
		isUser.addCreateVertexInstruction("nodeName", "is-user", ReturnType.string);
		
		rootNode.addChild(isUser);
		rootNode.addChild(isUserOf);
		isUserOf.addChild(isPutBy);
    	
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph loadCommands(String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(RATConstants.Commands, RATConstants.Commands, VertexType.SystemKey);
    	rootNode.addCreateCommandRootVertexInstruction("name", RATConstants.Commands, ReturnType.string);
    	rootNode.addInstruction("LoadCommandsAction", "folder", RATConstants.VertexContentUndefined, ReturnType.string);
    	
		CommandGraph graph = new CommandGraph(rootNode, "LoadCommands");
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph loadQueries(String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(RATConstants.Queries, RATConstants.Queries, VertexType.SystemKey);
    	rootNode.addCreateCommandRootVertexInstruction("name", RATConstants.Queries, ReturnType.string);
    	rootNode.addInstruction("LoadCommandsAction", "folder", RATConstants.VertexContentUndefined, ReturnType.string);
    	
		CommandGraph graph = new CommandGraph(rootNode, "LoadQueries");
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph addRootDomain(String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(VertexType.RootDomain.toString(), VertexType.RootDomain.toString(), VertexType.RootDomain);
    	String platformName = AppProperties.getInstance().getStringProperty("root.domain.name");
    	rootNode.addCreateRootPlatformVertexInstruction("rootDomainName", platformName, ReturnType.string);
    	rootNode.addBindInstruction("commandsNodeUUID", RATConstants.VertexContentUndefined);
    	rootNode.addBindInstruction("queriesNodeUUID", RATConstants.VertexContentUndefined);
    	
    	SystemKey isSystem = new SystemKey("is-system", "is-system");
    	isSystem.addCreateVertexInstruction("nodeName", isSystem.getContent(), ReturnType.string);
    	
    	SystemKey isPutBy = new SystemKey("is-put-by", "is-put-by");
    	isPutBy.addCreateVertexInstruction("nodeName", isPutBy.getContent(), ReturnType.string);

    	rootNode.addChild(isSystem);
    	isSystem.addChild(isPutBy);
    	isPutBy.addChild(rootNode);
    	
		CommandGraph graph = new CommandGraph(rootNode, "AddRootDomain");
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph addNewUser(String commandName, String commandVersion) throws Exception{
    	RootNode rootNode = new RootNode(VertexType.User.toString(), VertexType.User.toString(), VertexType.User);
    	rootNode.addCreateCommandRootVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
		rootNode.addPropertyVertexInstruction("userEmail", RATConstants.VertexContentUndefined, ReturnType.string, true);
		rootNode.addPropertyVertexInstruction("userName", RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addPropertyVertexInstruction("userPwd", RATConstants.VertexContentUndefined, ReturnType.string);
		
    	SystemKey isUserOf = new SystemKey("is-user-of", "is-user-of");
    	isUserOf.addCreateVertexInstruction("nodeName", isUserOf.getContent(), ReturnType.string);
    	isUserOf.addBindInstruction("isUserOfNodeUUID", RATConstants.VertexContentUndefined);
    	
    	SystemKey isPutBy = new SystemKey("is-put-by", "is-put-by");
    	isPutBy.addCreateVertexInstruction("nodeName", isPutBy.getContent(), ReturnType.string);
    	isPutBy.addBindInstruction("isPutByNode2UUID", RATConstants.VertexContentUndefined);
    	
		isUserOf.addChild(isPutBy);
    	
    	SystemKey isUser = new SystemKey("is-user", "is-user");
    	isUser.addCreateVertexInstruction("nodeName", isUser.getContent(), ReturnType.string);
		
    	SystemKey isPutBy2 = new SystemKey("is-put-by", "is-put-by");
    	isPutBy2.addCreateVertexInstruction("nodeName", isPutBy2.getContent(), ReturnType.string);
    	isPutBy2.addBindInstruction("isPutByNodeUUID", RATConstants.VertexContentUndefined);
		
		isUser.addChild(isPutBy2);
		
		rootNode.addChild(isUserOf);
		rootNode.addChild(isUser);
		
		CommandGraph addNewUser = new CommandGraph(rootNode, commandName);
		addNewUser.set_commandVersion(commandVersion);
		addNewUser.run();
		
		return addNewUser;
    }
    
	public void writeCommands(CommandGraph command, String placeHolder, String applicationName, String applicationVersion) throws Exception{
		JsonHeader header = new JsonHeader();
		header.setApplicationName(applicationName);
		header.setApplicationVersion(applicationVersion);
		header.setCommandVersion(command.get_commandVersion());
		header.setDomainName(placeHolder);
		header.setDomainUUID("null");
		header.setMessageType(MessageType.Template);
		header.setCommandType(command.get_commandType());
		header.setCommandName(command.get_commandName());
		header.setCommandGraphUUID(command.get_commandUUID());
		header.setRootVertexUUID(command.get_rootNodeUUID());
		
		String commandTemplate = GraphGeneratorHelpers.buildCommandTemplate(header, command.getGraph());
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(commandTemplate));
		
		_alchemyManager.addCommandTemplate(command.get_commandName(), command.get_commandVersion(), commandTemplate);
		
		InstructionParamsDriller paramsDriller = new InstructionParamsDriller();
		command.accept(paramsDriller);
		String json = paramsDriller.getValues();
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
		String remoteRequest = GraphGeneratorHelpers.buildRemoteCommand(header, json);
		String ratStorageManagerCommandsFolder = AppProperties.getInstance().getStringProperty("commands.folder");
		String path = _ratStorageManagerPath + Constants.PathSeparator + Constants.ConfFolder + Constants.PathSeparator + 
				ratStorageManagerCommandsFolder + Constants.PathSeparator + command.get_commandName() + ".conf";
		//System.out.println(path);
		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(remoteRequest), path);
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(remoteRequest));
		
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(commandTemplate));
		String ratStorageManagerCommandTemplatesFolder = AppProperties.getInstance().getStringProperty("command.templates.folder");
		path = _ratStorageManagerPath + Constants.PathSeparator + Constants.ConfFolder + Constants.PathSeparator + 
				ratStorageManagerCommandTemplatesFolder + Constants.PathSeparator +
				command.get_commandName() + ".conf";
		//System.out.println(path);
		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(commandTemplate), path);

		_buildQueryJavaScript.setHeader(header);
		RATJsonObject ratJsonObject = RATJsonUtils.getRATJsonObject(remoteRequest);
		_buildQueryJavaScript.make(command.get_commandName(), ratJsonObject);
		String javaScript = _buildQueryJavaScript.getJavaScript();
		GraphGeneratorHelpers.writeJavaScript("ratCommands", javaScript);
	}

}
