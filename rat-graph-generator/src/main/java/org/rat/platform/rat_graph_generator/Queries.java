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
	
	public CommandGraph countSubDomains(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.Domain.toString(), VertexType.QueryPivot.toString());
		rootNode.addInstruction("StartStep", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "AddNewDomain", ReturnType.string);
		
		QueryPivot isPutBy = new QueryPivot("is-put-by");
		isPutBy.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isPutBy.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
		
		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "AddNewDomain", ReturnType.string);
		
		QueryPivot isDomain = new QueryPivot("isDomain");
		isDomain.addInstruction("HasStep", "type", RATConstants.VertexTypeField, ReturnType.string);
		isDomain.addInstruction("HasStep", "typeValue", VertexType.Domain.toString(), ReturnType.string);
		
		QueryPivot isNotDeleted = new QueryPivot("isNotDeleted");
		isNotDeleted.addInstruction("HasNotStep", "paramName", RATConstants.IsDeleted, ReturnType.string);
		isNotDeleted.addInstruction("HasNotStep", "paramValue", "true", ReturnType.bool);
		
		QueryPivot countStep = new QueryPivot(VertexType.Domain.toString());
		countStep.addInstruction("CountStep", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(isPutBy);
		isPutBy.addChild(edgeInStep2);
		edgeInStep2.addChild(isDomain);
		//isDomain.addChild(countStep);
		isDomain.addChild(isNotDeleted);
		isNotDeleted.addChild(countStep);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
	}
	
	public CommandGraph countComments(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.Domain.toString(), VertexType.QueryPivot.toString());
		rootNode.addInstruction("StartStep", "ownerNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot belongsTo = new QueryPivot("belongs-to");
		belongsTo.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		belongsTo.addInstruction("HasStep", "paramValue", "belongs-to", ReturnType.string);
		
		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isComment = new QueryPivot("isComment");
		isComment.addInstruction("HasStep", "type", RATConstants.VertexTypeField, ReturnType.string);
		isComment.addInstruction("HasStep", "typeValue", VertexType.Comment.toString(), ReturnType.string);
		
		QueryPivot isNotDeleted = new QueryPivot("isNotDeleted");
		isNotDeleted.addInstruction("HasNotStep", "paramName", RATConstants.IsDeleted, ReturnType.string);
		isNotDeleted.addInstruction("HasNotStep", "paramValue", "true", ReturnType.bool);
		
		QueryPivot countStep = new QueryPivot(VertexType.Comment.toString());
		countStep.addInstruction("CountStep", RATConstants.VertexTypeField, VertexType.Comment.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(belongsTo);
		belongsTo.addChild(edgeInStep2);
		edgeInStep2.addChild(isComment);
//		isComment.addChild(countStep);
		isComment.addChild(isNotDeleted);
		isNotDeleted.addChild(countStep);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
	}
	
	public CommandGraph countAllUserComments(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
		rootNode.addInstruction("StartStep", "userNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isPutBy = new QueryPivot("is-put-by");
		isPutBy.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isPutBy.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
		
		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isComment = new QueryPivot("isComment");
		isComment.addInstruction("HasStep", "type", RATConstants.VertexTypeField, ReturnType.string);
		isComment.addInstruction("HasStep", "typeValue", VertexType.Comment.toString(), ReturnType.string);
		
		QueryPivot isNotDeleted = new QueryPivot("isNotDeleted");
		isNotDeleted.addInstruction("HasNotStep", "paramName", RATConstants.IsDeleted, ReturnType.string);
		isNotDeleted.addInstruction("HasNotStep", "paramValue", "true", ReturnType.bool);
		
		QueryPivot countStep = new QueryPivot(VertexType.Comment.toString());
		countStep.addInstruction("CountStep", RATConstants.VertexTypeField, VertexType.Comment.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(isPutBy);
		isPutBy.addChild(edgeInStep2);
		edgeInStep2.addChild(isComment);
//		isComment.addChild(countStep);
		isComment.addChild(isNotDeleted);
		isNotDeleted.addChild(countStep);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
	}
	
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
    
    // TODO: attenzione, non fa il controllo degli utenti cancellati; da modificare!
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
    
    public CommandGraph getAllUserDomainComments(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
    	rootNode.addInstruction("StartStep", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	
		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isPutBy = new QueryPivot("is-put-by");
		isPutBy.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isPutBy.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
		
		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isComment = new QueryPivot("isComment");
		isComment.addInstruction("HasStep", "paramNameA", RATConstants.VertexTypeField, ReturnType.string);
		isComment.addInstruction("HasStep", "paramValueA", VertexType.Comment.toString(), ReturnType.string);
		
		QueryPivot isNotDeleted = new QueryPivot("isNotDeleted");
		isNotDeleted.addInstruction("HasNotStep", "paramName", RATConstants.IsDeleted, ReturnType.string);
		isNotDeleted.addInstruction("HasNotStep", "paramValue", "true", ReturnType.bool);
		
		QueryPivot edgeOutStep2 = new QueryPivot("EdgeOutStep");
		edgeOutStep2.addInstruction("EdgeOutStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isURI = new QueryPivot("isURI");
		isURI.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		isURI.addInstruction("HasStep", "paramValue", VertexType.URI.toString(), ReturnType.string);
		
		QueryPivot domainHasName = new QueryPivot("domainHasName");
		domainHasName.addInstruction("HasStep", "url", RATConstants.VertexContentField, ReturnType.string);
		domainHasName.addInstruction("HasStep", "urlValue", RATConstants.VertexContentUndefined.toString(), ReturnType.string);
		
		QueryPivot backStep = new QueryPivot("BackStep");
		//backStep.addInstruction("BackStep", "backLabel", VertexType.Comment.toString(), ReturnType.string);
		// TODO: da convertire in una label (GremlinPIpeline.back(integer) è deprecato)
		backStep.addInstruction("BackStep", "backSteps", "3", ReturnType.integer);
		
		QueryPivot edgeOutStep3 = new QueryPivot("EdgeOutStep");
		edgeOutStep3.addInstruction("EdgeOutStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot belongsTo = new QueryPivot("belongs-to");
		belongsTo.addInstruction("HasStep", "paramNameC", RATConstants.VertexContentField, ReturnType.string);
		belongsTo.addInstruction("HasStep", "paramValueC", "belongs-to", ReturnType.string);
		
		QueryPivot edgeOutStep4 = new QueryPivot("EdgeOutStep");
		edgeOutStep4.addInstruction("EdgeOutStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot hasStep2 = new QueryPivot("HasStep");
		hasStep2.addInstruction("HasStep", "type", RATConstants.VertexTypeField, ReturnType.string);
		hasStep2.addInstruction("HasStep", "typeValue", VertexType.Domain.toString(), ReturnType.string);
		
		QueryPivot hasStep3 = new QueryPivot("HasStep");
		hasStep3.addInstruction("HasStep", "domainUUID", RATConstants.VertexUUIDField, ReturnType.string);
		hasStep3.addInstruction("HasStep", "domainUUIDValue", RATConstants.VertexContentUndefined.toString(), ReturnType.uuid);
//		hasStep3.addInstruction("HasStep", "paramNameF", RATConstants.VertexUUIDField, ReturnType.string);
//		hasStep3.addInstruction("HasStep", "paramValueF", RATConstants.VertexContentUndefined.toString(), ReturnType.uuid);
		
		QueryPivot backStep2 = new QueryPivot("BackStep");
		//backStep2.addInstruction("BackStep", "backLabel", VertexType.Comment.toString(), ReturnType.string);
		// TODO: da convertire in una label (GremlinPIpeline.back(integer) è deprecato)
		backStep2.addInstruction("BackStep", "backSteps", "5", ReturnType.integer);
		
		QueryPivot endStep = new QueryPivot(VertexType.User.toString());
		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Comment.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(isPutBy);
		isPutBy.addChild(edgeInStep2);
		edgeInStep2.addChild(isComment);
//		isComment.addChild(edgeOutStep2);
		isComment.addChild(isNotDeleted);
		isNotDeleted.addChild(edgeOutStep2);
		edgeOutStep2.addChild(isURI);
		isURI.addChild(domainHasName);
		domainHasName.addChild(backStep);
		backStep.addChild(edgeOutStep3);
		edgeOutStep3.addChild(belongsTo);
		belongsTo.addChild(edgeOutStep4);
		edgeOutStep4.addChild(hasStep2);
		hasStep2.addChild(hasStep3);
		hasStep3.addChild(backStep2);
		backStep2.addChild(endStep);
		
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
		
		QueryPivot isComment = new QueryPivot("isComment");
		isComment.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		isComment.addInstruction("HasStep", "paramValue", VertexType.Comment.toString(), ReturnType.string);
		
		QueryPivot isNotDeleted = new QueryPivot("isNotDeleted");
		isNotDeleted.addInstruction("HasNotStep", "paramName", RATConstants.IsDeleted, ReturnType.string);
		isNotDeleted.addInstruction("HasNotStep", "paramValue", "true", ReturnType.bool);
		
		QueryPivot edgeOutStep2 = new QueryPivot("EdgeOutStep");
		edgeOutStep2.addInstruction("EdgeOutStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot belongsTo = new QueryPivot("belongs-to");
		belongsTo.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		belongsTo.addInstruction("HasStep", "paramValue", "belongs-to", ReturnType.string);
		
		QueryPivot edgeOutStep3 = new QueryPivot("EdgeOutStep");
		edgeOutStep3.addInstruction("EdgeOutStep", "edgeLabel", "AddComment", ReturnType.string);
		
		QueryPivot isDomain = new QueryPivot("isDomain");
		isDomain.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		isDomain.addInstruction("HasStep", "paramValue", VertexType.Domain.toString(), ReturnType.string);
		
		QueryPivot backStep = new QueryPivot("BackStep");
		//backStep.addInstruction("BackStep", "backLabel", VertexType.Comment.toString(), ReturnType.string);
		// TODO: da convertire in una label (GremlinPIpeline.back(integer) è deprecato)
		backStep.addInstruction("BackStep", "backSteps", "5", ReturnType.integer);
		
		QueryPivot endStep = new QueryPivot(VertexType.User.toString());
		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Comment.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(isPutBy);
		isPutBy.addChild(edgeInStep2);
		edgeInStep2.addChild(isComment);
//		isComment.addChild(edgeOutStep2);
		isComment.addChild(isNotDeleted);
		isNotDeleted.addChild(edgeOutStep2);
		edgeOutStep2.addChild(belongsTo);
		belongsTo.addChild(edgeOutStep3);
		edgeOutStep3.addChild(isDomain);
		isDomain.addChild(backStep);
		backStep.addChild(endStep);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
    public CommandGraph getUserDomainByName(String commandName, String commandVersion) throws Exception{
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
		
		QueryPivot isDomainName = new QueryPivot("isDomainName");
		isDomainName.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isDomainName.addInstruction("HasStep", "paramValue", RATConstants.VertexContentUndefined, ReturnType.string);
		
		QueryPivot endStep = new QueryPivot(VertexType.User.toString());
		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(isPutBy);
		isPutBy.addChild(edgeInStep2);
		edgeInStep2.addChild(hasStep);
		hasStep.addChild(edgeInStep3);
		edgeInStep3.addChild(isDomain);
		isDomain.addChild(isDomainName);
		isDomainName.addChild(endStep);
		
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
		
		QueryPivot edgeInStep3 = new QueryPivot("EdgeInStep");
		edgeInStep3.addInstruction("EdgeInStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
		
		QueryPivot isDomain = new QueryPivot("isDomain");
		isDomain.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		isDomain.addInstruction("HasStep", "paramValue", VertexType.Domain.toString(), ReturnType.string);
		
		QueryPivot isNotDeleted = new QueryPivot("isNotDeleted");
		isNotDeleted.addInstruction("HasNotStep", "paramName", RATConstants.IsDeleted, ReturnType.string);
		isNotDeleted.addInstruction("HasNotStep", "paramValue", "true", ReturnType.bool);
		
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
		
		QueryPivot endStep = new QueryPivot(VertexType.Domain.toString());
		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(isPutBy);
		isPutBy.addChild(edgeInStep2);
		edgeInStep2.addChild(hasStep);
		hasStep.addChild(edgeInStep3);
		edgeInStep3.addChild(isDomain);
		//isDomain.addChild(edgeOutStep);
		isDomain.addChild(isNotDeleted);
		isNotDeleted.addChild(edgeOutStep);
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
    
//    public CommandGraph getAllUserDomains(String commandName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
//    	rootNode.addInstruction("StartStep", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
//    	
//		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
//		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
//		
//		QueryPivot isPutBy = new QueryPivot("is-put-by");
//		isPutBy.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
//		isPutBy.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
//		
//		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
//		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
//		
//		QueryPivot hasStep = new QueryPivot("is-user-of");
//		hasStep.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
//		hasStep.addInstruction("HasStep", "paramValue", "is-user-of", ReturnType.string);
//		//hasStep.addInstruction("DebugDumpValue");
//		
//		QueryPivot edgeInStep3 = new QueryPivot("EdgeInStep");
//		edgeInStep3.addInstruction("EdgeInStep", "edgeLabel", "BindFromUserToDomain", ReturnType.string);
//		
//		QueryPivot isDomain = new QueryPivot("isDomain");
//		isDomain.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
//		isDomain.addInstruction("HasStep", "paramValue", VertexType.Domain.toString(), ReturnType.string);
//		
//		QueryPivot edgeOutStep = new QueryPivot("EdgeOutStep");
//		edgeOutStep.addInstruction("EdgeOutStep", "edgeLabel", "AddNewDomain", ReturnType.string);
//		
//		QueryPivot isPutBy2 = new QueryPivot("is-put-by");
//		isPutBy2.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
//		isPutBy2.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
//		
//		QueryPivot edgeOutStep2 = new QueryPivot("EdgeOutStep");
//		edgeOutStep2.addInstruction("EdgeOutStep", "edgeLabel", "AddNewDomain", ReturnType.string);
//		
//		QueryPivot isRootDomain = new QueryPivot("isRootDomain");
//		isRootDomain.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
//		isRootDomain.addInstruction("HasStep", "paramValue", VertexType.RootDomain.toString(), ReturnType.string);
//		
//		QueryPivot backStep = new QueryPivot("BackStep");
//		//backStep.addInstruction("BackStep", "backLabel", VertexType.Comment.toString(), ReturnType.string);
//		// TODO: da convertire in una label (GremlinPIpeline.back(integer) è deprecato)
//		backStep.addInstruction("BackStep", "backSteps", "5", ReturnType.integer);
//		
//		QueryPivot endStep = new QueryPivot(VertexType.Domain.toString());
//		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
//		
//		rootNode.addChild(edgeInStep);
//		edgeInStep.addChild(isPutBy);
//		isPutBy.addChild(edgeInStep2);
//		edgeInStep2.addChild(hasStep);
//		hasStep.addChild(edgeInStep3);
//		edgeInStep3.addChild(isDomain);
//		isDomain.addChild(edgeOutStep);
//		edgeOutStep.addChild(isPutBy2);
//		isPutBy2.addChild(edgeOutStep2);
//		edgeOutStep2.addChild(isRootDomain);
//		isRootDomain.addChild(backStep);
//		backStep.addChild(endStep);
//		
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
    
    public CommandGraph getCommentComments(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.Comment.toString(), VertexType.QueryPivot.toString());
    	rootNode.addInstruction("StartStep", "commentNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	
		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);
    	
		QueryPivot hasStep = new QueryPivot("belongs-to");
		hasStep.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		hasStep.addInstruction("HasStep", "paramValue", "belongs-to", ReturnType.string);
		
		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "AddComment", ReturnType.string);

		QueryPivot isComment = new QueryPivot("isComment");
		isComment.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		isComment.addInstruction("HasStep", "paramValue", VertexType.Comment.toString(), ReturnType.string);
		
		QueryPivot isNotDeleted = new QueryPivot("isNotDeleted");
		isNotDeleted.addInstruction("HasNotStep", "paramName", RATConstants.IsDeleted, ReturnType.string);
		isNotDeleted.addInstruction("HasNotStep", "paramValue", "true", ReturnType.bool);
		
		QueryPivot endStep = new QueryPivot(VertexType.User.toString());
		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Comment.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(hasStep);
		hasStep.addChild(edgeInStep2);
		edgeInStep2.addChild(isComment);
		//isComment.addChild(endStep);
		isComment.addChild(isNotDeleted);
		isNotDeleted.addChild(endStep);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
//    public CommandGraph getDomainDomains(String commandName, String commandVersion) throws Exception{
//    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
//    	rootNode.addInstruction("StartStep", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
//    	
//		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
//		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "AddNewDomain", ReturnType.string);
//    	
//		QueryPivot isPutBy = new QueryPivot("is-put-by");
//		isPutBy.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
//		isPutBy.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
//		
//		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
//		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "AddNewDomain", ReturnType.string);
//		
//		QueryPivot isDomain = new QueryPivot("isDomain");
//		isDomain.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
//		isDomain.addInstruction("HasStep", "paramValue", VertexType.Domain.toString(), ReturnType.string);
//		
//		QueryPivot endStep = new QueryPivot(VertexType.Domain.toString());
//		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
//		
//		rootNode.addChild(edgeInStep);
//		edgeInStep.addChild(isPutBy);
//		isPutBy.addChild(edgeInStep2);
//		edgeInStep2.addChild(isDomain);
//		isDomain.addChild(endStep);
//
//		CommandGraph graph = new CommandGraph(rootNode, commandName);
//		graph.set_commandVersion(commandVersion);
//		graph.run();
//		
//		return graph;
//    }
    
    public CommandGraph getDomainDomains(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(VertexType.User.toString(), VertexType.QueryPivot.toString());
    	rootNode.addInstruction("StartStep", "rootNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
    	
		QueryPivot edgeInStep = new QueryPivot("EdgeInStep");
		edgeInStep.addInstruction("EdgeInStep", "edgeLabel", "AddNewDomain", ReturnType.string);
    	
		QueryPivot isPutBy = new QueryPivot("is-put-by");
		isPutBy.addInstruction("HasStep", "paramName", RATConstants.VertexContentField, ReturnType.string);
		isPutBy.addInstruction("HasStep", "paramValue", "is-put-by", ReturnType.string);
		
		QueryPivot edgeInStep2 = new QueryPivot("EdgeInStep");
		edgeInStep2.addInstruction("EdgeInStep", "edgeLabel", "AddNewDomain", ReturnType.string);
		
		QueryPivot isDomain = new QueryPivot("isDomain");
		isDomain.addInstruction("HasStep", "paramName", RATConstants.VertexTypeField, ReturnType.string);
		isDomain.addInstruction("HasStep", "paramValue", VertexType.Domain.toString(), ReturnType.string);
		
		QueryPivot isNotDeleted = new QueryPivot("isNotDeleted");
		isNotDeleted.addInstruction("HasNotStep", "paramName", RATConstants.IsDeleted, ReturnType.string);
		isNotDeleted.addInstruction("HasNotStep", "paramValue", "true", ReturnType.bool);
		
		QueryPivot endStep = new QueryPivot(VertexType.Domain.toString());
		endStep.addInstruction("EndStep", RATConstants.VertexTypeField, VertexType.Domain.toString(), ReturnType.string);
		
		rootNode.addChild(edgeInStep);
		edgeInStep.addChild(isPutBy);
		isPutBy.addChild(edgeInStep2);
		edgeInStep2.addChild(isDomain);
//		isDomain.addChild(endStep);
		isDomain.addChild(isNotDeleted);
		isNotDeleted.addChild(endStep);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
    }
    
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
    
    public CommandGraph getRootUserByEmail(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(commandName, VertexType.QueryPivot.toString());
		rootNode.addInstruction("GetSingleNode", "paramName", "userEmail", ReturnType.string);
		rootNode.addInstruction("GetSingleNode", "paramValue", RATConstants.VertexContentUndefined, ReturnType.string);
		
		rootNode.addInstruction("GetSingleNode", RATConstants.VertexTypeField, VertexType.RootAdminUser.toString(), ReturnType.string);
		
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
    
	public CommandGraph getGraph(String commandName, String commandVersion) throws Exception{
    	RootQueryPivot rootNode = new RootQueryPivot(commandName, VertexType.QueryPivot.toString());
		rootNode.addInstruction("GetGraph", "paramName", RATConstants.GraphUUID, ReturnType.string);
		rootNode.addInstruction("GetGraph", "paramValue", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		//rootNode.addInstruction("GetGraph", RATConstants.VertexTypeField, VertexType.User.toString(), ReturnType.string);
		
		CommandGraph graph = new CommandGraph(rootNode, commandName);
		graph.set_commandVersion(commandVersion);
		graph.run();
		
		return graph;
	}
    
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
