package org.rat.platform.rat_graph_generator;

import java.util.Map;
import org.rat.platform.graph.CommandGraph;
import org.rat.platform.graph.visitor.InstructionParamsDriller;
import org.rat.platform.query.nodes.QueryPivot;
import org.rat.platform.query.nodes.RootQueryPivot;
import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.AppProperties;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;

public class Queries {
	private String _ratStorageManagerPath = null;
	private BuildQueryJavaScript _buildQueryJavaScript = null;
	private AlchemyManager _alchemyManager = null;
	private Map<String, Graph>_commands = null;
	
	public Queries(Map<String, Graph>commands, AlchemyManager alchemyManager, String ratStorageManagerPath, BuildQueryJavaScript buildQueryJavaScript) {
		_alchemyManager = alchemyManager;
		_ratStorageManagerPath = ratStorageManagerPath;
		_buildQueryJavaScript = buildQueryJavaScript;
		_commands = commands;
	}
	
//    "AddComment" "AddNewUser"
    public CommandGraph getUserURLs(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
		rootNode.addInstruction("StartStep", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		QueryPivot isLinkedTo = new QueryPivot("EdgeInStep");
		isLinkedTo.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isLinkedTo2 = new QueryPivot("HasStep");
		isLinkedTo2.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isLinkedTo2.addInstruction("HasStep", "paramValue", "is-linked-to", ReturnType.string);
		
		QueryPivot isLinkedTo3 = new QueryPivot("EdgeInStep");
		isLinkedTo3.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isLinkedTo4 = new QueryPivot("HasStep");
		isLinkedTo4.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		isLinkedTo4.addInstruction("HasStep", "paramValue", VertexType.URI.toString(), ReturnType.string);
		
		QueryPivot isLinkedTo5 = new QueryPivot("DistinctStep");
		isLinkedTo5.addInstruction("DistinctStep");
		
		QueryPivot URL = new QueryPivot(VertexType.URI.toString());
		URL.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.URI.toString(), ReturnType.string);
		
		rootNode.addChild(isLinkedTo);
		isLinkedTo.addChild(isLinkedTo2);
		isLinkedTo2.addChild(isLinkedTo3);
		isLinkedTo3.addChild(isLinkedTo4);
		isLinkedTo4.addChild(isLinkedTo5);
		isLinkedTo5.addChild(URL);
		//isLinkedTo4.addChild(URL);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph getAllDomainUsers(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.Domain.toString(), VertexType.QueryPivot.toString());
    	rootNode.addInstruction("StartStep", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);

		QueryPivot edgeOutStep = new QueryPivot("EdgeOutStep");
		edgeOutStep.addInstruction("EdgeOutStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
		
		QueryPivot isUserOf = new QueryPivot("is-user-of");
		isUserOf.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isUserOf.addInstruction("HasStep", "paramValue", "is-user-of", ReturnType.string);
		
		QueryPivot edgeOutStep2 = new QueryPivot("EdgeOutStep");
		edgeOutStep2.addInstruction("EdgeOutStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
		
		QueryPivot isPutBy = new QueryPivot("is-put-by");
		isPutBy.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isPutBy.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
		
		QueryPivot edgeOutStep3 = new QueryPivot("EdgeOutStep");
		edgeOutStep3.addInstruction("EdgeOutStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
		
		QueryPivot endStep = new QueryPivot(VertexType.User.toString());
		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.User.toString(), ReturnType.string);
		
		rootNode.addChild(edgeOutStep);
		edgeOutStep.addChild(isUserOf);
		isUserOf.addChild(edgeOutStep2);
		edgeOutStep2.addChild(isPutBy);
		isPutBy.addChild(edgeOutStep3);
		edgeOutStep3.addChild(endStep);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph getAllUserComments(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
    	rootNode.addInstruction("StartStep", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	
		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isPutBy = new QueryPivot("is-put-by");
		isPutBy.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isPutBy.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
		
		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot hasStep = new QueryPivot("HasStep");
		hasStep.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		hasStep.addInstruction("HasStep", "paramValue", VertexType.Comment.toString(), ReturnType.string);
		
		QueryPivot edgeOutStep2 = new QueryPivot("EdgeOutStep");
		edgeOutStep2.addInstruction("EdgeOutStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot belongsTo = new QueryPivot("belongs-to");
		belongsTo.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		belongsTo.addInstruction("HasStep", "paramValue", "belongs-to", ReturnType.string);
		
		QueryPivot edgeOutStep3 = new QueryPivot("EdgeOutStep");
		edgeOutStep3.addInstruction("EdgeOutStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot hasStep2 = new QueryPivot("HasStep");
		hasStep2.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		hasStep2.addInstruction("HasStep", "paramValue", VertexType.Domain.toString(), ReturnType.string);
		
		QueryPivot backStep = new QueryPivot("BackStep");
		//backStep.addInstruction("BackStep", "backLabel", VertexType.Comment.toString(), ReturnType.string);
		// TODO: da convertire in una label (GremlinPIpeline.back(integer) è deprecato)
		backStep.addInstruction("BackStep", "backSteps", "5", ReturnType.integer);
		
		QueryPivot endStep = new QueryPivot(VertexType.User.toString());
		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Comment.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(isPutBy);
		isPutBy.addChild(edgeInStep2);
		edgeInStep2.addChild(hasStep);
		hasStep.addChild(edgeOutStep2);
		edgeOutStep2.addChild(belongsTo);
		belongsTo.addChild(edgeOutStep3);
		edgeOutStep3.addChild(hasStep2);
		hasStep2.addChild(backStep);
		backStep.addChild(endStep);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph getAllUserDomains(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
    	rootNode.addInstruction("StartStep", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	
		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
		
		QueryPivot isPutBy = new QueryPivot("is-put-by");
		isPutBy.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isPutBy.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
		
		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
		
		QueryPivot hasStep = new QueryPivot("is-user-of");
		hasStep.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		hasStep.addInstruction("HasStep", "paramValue", "is-user-of", ReturnType.string);
		//hasStep.addInstruction("DebugDumpValue");
		
		QueryPivot edgeInStep3 = new QueryPivot("EdgeInStep");
		edgeInStep3.addInstruction("EdgeInStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
		
		QueryPivot isDomain = new QueryPivot("isDomain");
		isDomain.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		isDomain.addInstruction("HasStep", "paramValue", VertexType.Domain.toString(), ReturnType.string);
		
		QueryPivot edgeOutStep = new QueryPivot("EdgeOutStep");
		edgeOutStep.addInstruction("EdgeOutStep", "edgeLabel", "AddNewDomain", ReturnType.string);
		
		QueryPivot isPutBy2 = new QueryPivot("is-put-by");
		isPutBy2.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isPutBy2.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
		
		QueryPivot edgeOutStep2 = new QueryPivot("EdgeOutStep");
		edgeOutStep2.addInstruction("EdgeOutStep", "edgeLabel", "AddNewDomain", ReturnType.string);
		
		QueryPivot isRootDomain = new QueryPivot("isRootDomain");
		isRootDomain.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		isRootDomain.addInstruction("HasStep", "paramValue", VertexType.RootDomain.toString(), ReturnType.string);
		
		QueryPivot backStep = new QueryPivot("BackStep");
		//backStep.addInstruction("BackStep", "backLabel", VertexType.Comment.toString(), ReturnType.string);
		// TODO: da convertire in una label (GremlinPIpeline.back(integer) è deprecato)
		backStep.addInstruction("BackStep", "backSteps", "5", ReturnType.integer);
		
		QueryPivot endStep = new QueryPivot(VertexType.User.toString());
		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(isPutBy);
		isPutBy.addChild(edgeInStep2);
		edgeInStep2.addChild(hasStep);
		hasStep.addChild(edgeInStep3);
		edgeInStep3.addChild(isDomain);
		isDomain.addChild(edgeOutStep);
		edgeOutStep.addChild(isPutBy2);
		isPutBy2.addChild(edgeOutStep2);
		edgeOutStep2.addChild(isRootDomain);
		isRootDomain.addChild(backStep);
		backStep.addChild(endStep);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
//    public CommandGraph getAllDomainUsers(String commandName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.Domain.toString(), VertexType.QueryPivot.toString());
//    	
//		rootNode.addInstruction("GetSingleNode", "paramName", RATConstants.VertexUUIDField, ReturnType.string);
//		rootNode.addInstruction("GetSingleNode", "paramValue", RATConstants.VertexContentUndefined, ReturnType.uuid);
//		rootNode.addInstruction("GetSingleNode", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
//		
//		QueryPivot queryPivot = new QueryPivot("is-user-of");
//		queryPivot.addInstruction("GetNodeByContent", RATConstants.VertexContentField, "is-user-of", ReturnType.string);
//		queryPivot.addInstruction("GetNodeByContent", RATConstants.VertexTypeField, VertexType.SystemKey.toString(), ReturnType.string);
//		rootNode.addChild(queryPivot);
//		
//		QueryPivot getNodeByContent = new QueryPivot("is-put-by");
//		getNodeByContent.addInstruction("GetNodeByContent", RATConstants.VertexContentField, "is-put-by", ReturnType.string);
//		getNodeByContent.addInstruction("GetNodeByContent", RATConstants.VertexTypeField, VertexType.SystemKey.toString(), ReturnType.string);
//		queryPivot.addChild(getNodeByContent);
//		
//		QueryPivot getNode = new QueryPivot(VertexType.User.toString());
//		getNode.addInstruction("GetNode", RATConstants.VertexTypeField, VertexType.User.toString(), ReturnType.string);
//		getNodeByContent.addChild(getNode);
//		
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
//    public CommandGraph getUserURLs(String commandName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
//		rootNode.addInstruction("StartQueryPipe", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.string);
//		rootNode.addProperty("edgeDirection", Direction.IN);
//		rootNode.addProperty("edgeLabel", "AddComment");
////    	rootNode.addInstruction("AddNewUser/User", "StartQueryPipe", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.string);
//		
//		QueryPivot isLinkedTo = new QueryPivot("is-linked-to");
//		isLinkedTo.addInstruction("HasPipe", RATConstants.VertexContentField, "is-linked-to", ReturnType.string);
//		//isLinkedTo.addInstruction("HasPipe", "param1", "RATConstants.VertexContentField:is-linked-to", ReturnType.string);
//		isLinkedTo.addProperty("edgeDirection", Direction.IN);
//		isLinkedTo.addProperty("edgeLabel", "AddComment");
//		
//		QueryPivot isLinkedTo2 = new QueryPivot("is-linked-to");
////		isLinkedTo.addInstruction("HasNotPipe", RATConstants.VertexContentField, "is-linked-to", ReturnType.string);
////		isLinkedTo.addProperty("edgeDirection", Direction.IN);
////		isLinkedTo.addProperty("edgeLabel", "AddComment");
//		isLinkedTo2.addInstruction("HasPipe", RATConstants.VertexContentField, "is-linked-to", ReturnType.string);
//		//isLinkedTo.addInstruction("HasPipe", "param1", "RATConstants.VertexContentField:is-linked-to", ReturnType.string);
//		isLinkedTo2.addProperty("edgeDirection", Direction.IN);
//		isLinkedTo2.addProperty("edgeLabel", "AddComment");
//		
//		QueryPivot URL = new QueryPivot(VertexType.URI.toString());
//		URL.addInstruction("GerResultOfPipe", RATConstants.VertexTypeField, VertexType.URI.toString(), ReturnType.string);
//		URL.addProperty("edgeDirection", Direction.IN);
//		URL.addProperty("edgeLabel", "AddComment");
////		URL.addInstruction("AddComment/Comment/webDocument", "GerResultOfPipe", RATConstants.VertexTypeField, VertexType.URI.toString(), ReturnType.string);
//		
//		rootNode.addChild(isLinkedTo);
//		isLinkedTo.addChild(URL);
////		isLinkedTo2.addChild(URL);
////		URL.addChild(isLinkedTo);
////		isLinkedTo.addChild(rootNode);
//		
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
    
    public CommandGraph getCommentComments(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
		rootNode.addInstruction("GetSingleNode", "paramName", RATConstants.VertexUUIDField, ReturnType.string);
		rootNode.addInstruction("GetSingleNode", "paramValue", RATConstants.VertexContentUndefined, ReturnType.uuid);
		rootNode.addInstruction("GetSingleNode", RATConstants.VertexTypeField, VertexType.User.toString(), ReturnType.string);
		
		QueryPivot isLinkedTo = new QueryPivot("is-linked-to");
		isLinkedTo.addInstruction("GetNodeByContent", RATConstants.VertexContentField, "is-put-by", ReturnType.string);
		isLinkedTo.addInstruction("GetNodeByContent", RATConstants.VertexTypeField, VertexType.SystemKey.toString(), ReturnType.string);
		
		QueryPivot URL = new QueryPivot(VertexType.URI.toString());
		URL.addInstruction("GetNode", RATConstants.VertexTypeField, VertexType.URI.toString(), ReturnType.string);
		
		rootNode.addChild(isLinkedTo);
		isLinkedTo.addChild(URL);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
//    public CommandGraph getAllUserDomains(String commandName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
//    	rootNode.addInstruction("StartQueryPipe", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.string);
//		rootNode.addCommandNodeProperty("edgeDirection", Direction.IN);
//		rootNode.addCommandNodeProperty("edgeLabel", "AddComment");
//		
//		QueryPivot getNodeByContent = new QueryPivot("is-put-by");
//		getNodeByContent.addInstruction("GetNodeByContent", RATConstants.VertexContentField, "is-put-by", ReturnType.string);
//		getNodeByContent.addInstruction("GetNodeByContent", RATConstants.VertexTypeField, VertexType.SystemKey.toString(), ReturnType.string);
//		rootNode.addChild(getNodeByContent);
//		
//		QueryPivot getNodeByContent2 = new QueryPivot("is-user-of");
//		getNodeByContent2.addInstruction("GetNodeByContent", RATConstants.VertexContentField, "is-user-of", ReturnType.string);
//		getNodeByContent2.addInstruction("GetNodeByContent", RATConstants.VertexTypeField, VertexType.SystemKey.toString(), ReturnType.string);
//		getNodeByContent.addChild(getNodeByContent2);
//		
//		QueryPivot getNode = new QueryPivot(VertexType.Domain.toString());
//		getNode.addInstruction("GetNode", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
//		getNodeByContent2.addChild(getNode);
//		
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
    
//    public CommandGraph getAllUserComments(String commandName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
//		rootNode.addInstruction("GetSingleNode", "paramName", RATConstants.VertexUUIDField, ReturnType.string);
//		rootNode.addInstruction("GetSingleNode", "paramValue", RATConstants.VertexContentUndefined, ReturnType.uuid);
//		rootNode.addInstruction("GetSingleNode", RATConstants.VertexTypeField, VertexType.User.toString(), ReturnType.string);
//		
//		QueryPivot isPutBy = new QueryPivot("is-put-by");
//		isPutBy.addInstruction("GetNodeByContent", RATConstants.VertexContentField, "is-put-by", ReturnType.string);
//		isPutBy.addInstruction("GetNodeByContent", RATConstants.VertexTypeField, VertexType.SystemKey.toString(), ReturnType.string);
//		
//		QueryPivot comment = new QueryPivot(VertexType.Comment.toString());
//		comment.addInstruction("GetNode", RATConstants.VertexTypeField, VertexType.Comment.toString(), ReturnType.string);
//		
//		rootNode.addChild(isPutBy);
//		isPutBy.addChild(comment);
//		
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
    
//    public CommandGraph getAllUserDomains(String commandName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
//    	
//		rootNode.addInstruction("GetSingleNode", "paramName", RATConstants.VertexUUIDField, ReturnType.string);
//		rootNode.addInstruction("GetSingleNode", "paramValue", RATConstants.VertexContentUndefined, ReturnType.uuid);
//		rootNode.addInstruction("GetSingleNode", RATConstants.VertexTypeField, VertexType.User.toString(), ReturnType.string);
//		
//		QueryPivot getNodeByContent = new QueryPivot("is-put-by");
//		getNodeByContent.addInstruction("GetNodeByContent", RATConstants.VertexContentField, "is-put-by", ReturnType.string);
//		getNodeByContent.addInstruction("GetNodeByContent", RATConstants.VertexTypeField, VertexType.SystemKey.toString(), ReturnType.string);
//		rootNode.addChild(getNodeByContent);
//		
//		QueryPivot getNodeByContent2 = new QueryPivot("is-user-of");
//		getNodeByContent2.addInstruction("GetNodeByContent", RATConstants.VertexContentField, "is-user-of", ReturnType.string);
//		getNodeByContent2.addInstruction("GetNodeByContent", RATConstants.VertexTypeField, VertexType.SystemKey.toString(), ReturnType.string);
//		getNodeByContent.addChild(getNodeByContent2);
//		
//		QueryPivot getNode = new QueryPivot(VertexType.Domain.toString());
//		getNode.addInstruction("GetNode", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
//		getNodeByContent2.addChild(getNode);
//		
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
    
//    public static CommandGraph getAllDomains(String commandName, String instructionName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(commandName, VertexType.QueryPivot.toString());
//		rootNode.addInstruction(instructionName, "paramName", "domainName", ReturnType.string);
//		rootNode.addInstruction(instructionName, "paramValue", RATConstants.VertexContentUndefined, ReturnType.string);
//		
//		rootNode.addInstruction(instructionName, RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
//		
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
    
//    public CommandGraph getNodeSingleNode(String commandName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(commandName, VertexType.QueryPivot.toString());
//		rootNode.addInstruction("GetNodeSingleNode", "paramName", RATConstants.VertexContentUndefined, ReturnType.string);
//		rootNode.addInstruction("GetNodeSingleNode", "paramValue", RATConstants.VertexContentUndefined, ReturnType.string);
//		//rootNode.addInstruction("GetSingleNode", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
//		
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
    
    public CommandGraph getNodesByType(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(commandName, VertexType.QueryPivot.toString());
		rootNode.addInstruction("GetSingleNode", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		rootNode.addInstruction("GetSingleNode", "paramValue", RATConstants.VertexContentUndefined, ReturnType.string);
		//rootNode.addInstruction("GetSingleNode", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    
    public CommandGraph getDomainByName(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(commandName, VertexType.QueryPivot.toString());
		rootNode.addInstruction("GetSingleNode", "paramName", "domainName", ReturnType.string);
		rootNode.addInstruction("GetSingleNode", "paramValue", RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addInstruction("GetSingleNode", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph getUserByEmail(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(commandName, VertexType.QueryPivot.toString());
		rootNode.addInstruction("GetSingleNode", "paramName", "userEmail", ReturnType.string);
		rootNode.addInstruction("GetSingleNode", "paramValue", RATConstants.VertexContentUndefined, ReturnType.string);
		
		rootNode.addInstruction("GetSingleNode", RATConstants.VertexTypeField, VertexType.User.toString(), ReturnType.string);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
//    public static CommandGraph getAllNodesOfType(String commandName, String instructionName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(commandName, VertexType.QueryPivot.toString());
//		
//		rootNode.addInstruction(instructionName, RATConstants.VertexTypeField, RATConstants.VertexContentUndefined, ReturnType.string);
//		
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
    
	public void writeQuery(CommandGraph command, String placeHolder, String applicationName, String applicationVersion) throws Exception{
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

		_alchemyManager.addCommandTemplate(command.get_commandName(), command.get_commandVersion(), commandTemplate);
		
		InstructionParamsDriller paramsDriller = new InstructionParamsDriller();
		command.accept(paramsDriller);
		String json = paramsDriller.getValues();
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
		String remoteRequest = GraphGeneratorHelpers.buildRemoteCommand(header, json);
		String ratStorageManagerCommandsFolder = AppProperties.getInstance().getStringProperty("queries.folder");
		String path = _ratStorageManagerPath + Constants.PathSeparator + Constants.ConfFolder + Constants.PathSeparator + 
				ratStorageManagerCommandsFolder + Constants.PathSeparator + command.get_commandName() + ".conf";
		//System.out.println(path);
		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(remoteRequest), path);
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(remoteRequest));
		
		//System.out.println(RATJsonUtils.jsonPrettyPrinter(commandTemplate));
		String ratStorageManagerCommandTemplatesFolder = AppProperties.getInstance().getStringProperty("query.templates.folder");
		path = _ratStorageManagerPath + Constants.PathSeparator + Constants.ConfFolder + Constants.PathSeparator + 
				ratStorageManagerCommandTemplatesFolder + Constants.PathSeparator +
				command.get_commandName() + ".conf";
		//System.out.println(path);
		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(commandTemplate), path);

		_buildQueryJavaScript.setHeader(header);
		RATJsonObject ratJsonObject = RATJsonUtils.getRATJsonObject(remoteRequest);
		_buildQueryJavaScript.make(command.get_commandName(), ratJsonObject);
		String javaScript = _buildQueryJavaScript.getJavaScript();
		GraphGeneratorHelpers.writeJavaScript("ratQueries", javaScript);
	}
}
