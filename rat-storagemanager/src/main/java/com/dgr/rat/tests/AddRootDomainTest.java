/**
 * @author Daniele Grignani (dgr)
 * @date Sep 23, 2015
 */

package com.dgr.rat.tests;

import java.nio.file.FileSystems;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.dgr.rat.command.graph.executor.engine.RemoteCommandsContainer;
import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;
import com.dgr.rat.command.graph.executor.engine.result.IResultDriller;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.factory.CommandSink;
import com.dgr.rat.json.factory.Response;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.MakeSigmaJSON;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.rat.main.SystemCommandsInitializer;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.dgr.utils.Utils;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONMode;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONUtility;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.LoopPipe;

public class AddRootDomainTest {

	public AddRootDomainTest() {
		// TODO Auto-generated constructor stub
	}
	
	private String getNewRootNodeUUID(Response response) throws Exception{
		CommandResponse commandResponse = (CommandResponse) TestHelpers.getPrivateDataMember(Response.class, response, "_commandResult");
		String rootUUID = commandResponse.getProperty(RATConstants.VertexUUIDField).toString();
		
		return rootUUID;
	}
	
	@Test
	public void test() {
		SystemCommandsInitializer systemCommandsInitializer = TestHelpers.getSystemCommandsInitializer();
		Assert.assertNotNull(systemCommandsInitializer);

		try {
			systemCommandsInitializer.loadCommandTemplates();
			
			String json = this.setAddRootDomainValues("AddRootDomain.conf");
//			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			Response response = this.executeRemoteCommand(json);
			String rootDomainUUID = this.getNewRootNodeUUID(response);

			json = this.setAddRootDomainAdminUserValues("AddRootDomainAdminUser.conf", rootDomainUUID);
			response = this.executeRemoteCommand(json);
			
			// Domain
			json = this.setAddNewUserValues("AddNewUser.conf", rootDomainUUID, "dgr1", "dgr1");
			response = this.executeRemoteCommand(json);
			String dgr1UUID = this.getNewRootNodeUUID(response);
			
			json = this.setAddNewUserValues("AddNewUser.conf", rootDomainUUID, "dgr3", "dgr3");
			response = this.executeRemoteCommand(json);
			String dgr3UUID = this.getNewRootNodeUUID(response);
//			
			json = this.setAddNewDomainValues("AddNewDomain.conf", rootDomainUUID, "DGR Domain 1");
			response = this.executeRemoteCommand(json);
			String DGRDomain1UUID = this.getNewRootNodeUUID(response);
			String commandGraphUUID = response.getCommandGraphUUID();
//			
			json = this.setBindGraphValues("BindFromUserToDomain.conf", DGRDomain1UUID, dgr1UUID);
			response = this.executeRemoteCommand(json);
//			
			json = this.setBindGraphValues("BindFromUserToDomain.conf", DGRDomain1UUID, dgr3UUID);
			response = this.executeRemoteCommand(json);
//			
			json = this.setAddNewDomainValues("AddNewDomain.conf", rootDomainUUID, "DGR Domain 2");
			response = this.executeRemoteCommand(json);
			String DGRDomain2UUID = this.getNewRootNodeUUID(response);
//			
//			json = this.setBindGraphValues("BindFromUserToDomain.conf", DGRDomain2UUID, dgr3UUID);
//			response = this.executeRemoteCommand(json);
//			
//			json = this.setBindGraphValues("BindFromUserToDomain.conf", DGRDomain2UUID, dgr1UUID);
//			response = this.executeRemoteCommand(json);
//			
			json = this.setAddNewDomainValues("AddNewDomain.conf", rootDomainUUID, "DGR Domain 3");
			response = this.executeRemoteCommand(json);
			String DGRDomain3UUID = this.getNewRootNodeUUID(response);
			
			json = this.setBindGraphValues("BindFromUserToDomain.conf", DGRDomain3UUID, dgr1UUID);
			response = this.executeRemoteCommand(json);
			
			json = this.setAddCommentsValues("AddComment.conf", DGRDomain1UUID, dgr1UUID, 567, 899, "http://url.com/test", "Questo è il mio commento numero 1");
			response = this.executeRemoteCommand(json);
			String comment1UUID = this.getNewRootNodeUUID(response);
			
			json = this.setAddCommentsValues("AddComment.conf", DGRDomain1UUID, dgr1UUID, 567, 899, "http://url.com/test", "Questo è il mio commento numero 2");
			response = this.executeRemoteCommand(json);
			String comment2UUID = this.getNewRootNodeUUID(response);
			
			json = this.setAddCommentsValues("AddComment.conf", comment2UUID, dgr1UUID, 567, 899, "http://url.com/test", "Questo è il mio commento numero 4");
			response = this.executeRemoteCommand(json);
			String comment3UUID = this.getNewRootNodeUUID(response);
//			
////			 SubDomain
//			json = this.setAddNewDomainValues("AddNewDomain.conf", DGRDomain3UUID, "DGR SubDomain 1");
//			response = this.executeRemoteCommand(json);
//			String DGRSubDomain1UUID = this.getNewRootNodeUUID(response);
//			
//			json = this.setAddNewUserValues("AddNewUser.conf", rootDomainUUID, "dgr2", "dgr2");
//			response = this.executeRemoteCommand(json);
//			String dgr2UUID = this.getNewRootNodeUUID(response);
//			
//			json = this.setAddNewUserValues("AddNewUser.conf", rootDomainUUID, "dgr4", "dgr4");
//			response = this.executeRemoteCommand(json);
//			String dgr4UUID = this.getNewRootNodeUUID(response);
//			
//			json = this.setBindGraphValues("BindFromUserToDomain.conf", DGRSubDomain1UUID, dgr4UUID);
//			response = this.executeRemoteCommand(json);
//			
//			json = this.setBindGraphValues("BindFromUserToDomain.conf", DGRSubDomain1UUID, dgr2UUID);
//			response = this.executeRemoteCommand(json);
//			
//			json = this.setAddCommentsValues("AddComment.conf", DGRSubDomain1UUID, dgr2UUID, 567, 899, "http://url.com/test", "Questo è il mio commento numero 5");
//			response = this.executeRemoteCommand(json);
//			commentUUID = this.getNewRootNodeUUID(response);
//			
//			json = this.setAddCommentsValues("AddComment.conf", commentUUID, dgr2UUID, 0, 0, "", "Questo è il mio commento al commento numero 1");
//			response = this.executeRemoteCommand(json);
//			commentUUID = this.getNewRootNodeUUID(response);
			
			IStorage storage = StorageBridge.getInstance().getStorage();
			Vertex vertex = storage.getRootDomain();
			
			try {
				JSONObject jsonObject = GraphSONUtility.jsonFromElement(vertex, vertex.getPropertyKeys(), GraphSONMode.COMPACT);
				System.out.println(jsonObject.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "storage-manager.properties");
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "unittest.properties");
			
			String dir = "";//".." + FileSystems.getDefault().getSeparator();
			
			System.out.println("Domini sotto RAT");
			json = this.query("GetAllDomains.conf", "rootNodeUUID", rootDomainUUID);
//			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			response = this.executeRemoteCommand(json);
			json = JSONObjectBuilder.buildJSONRatCommandResponse(response);
			String alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			String resultFilename = dir + "GetAllDomains.conf" + "QueryResult";
			String path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			System.out.println("Utenti sotto RAT");
			json = this.query("GetAllUsers.conf", "rootNodeUUID", rootDomainUUID);
			response = this.executeRemoteCommand(json);
			json = JSONObjectBuilder.buildJSONRatCommandResponse(response);
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllUsers.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			System.out.println("Domini dell'utente dgr1");
			json = this.query("GetAllUserDomains.conf", "rootNodeUUID", dgr1UUID);
			response = this.executeRemoteCommand(json);
			json = JSONObjectBuilder.buildJSONRatCommandResponse(response);
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllUserDomains.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			System.out.println("Utenti del dominio DGR Domain 1");
			json = this.query("GetAllDomainUsers.conf", "rootNodeUUID", DGRDomain1UUID);
			response = this.executeRemoteCommand(json);
			json = JSONObjectBuilder.buildJSONRatCommandResponse(response);
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllDomainUsers.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			System.out.println("Commenti dell'utente dgr1");
			json = this.query("GetAllUserComments.conf", "rootNodeUUID", dgr1UUID);
			response = this.executeRemoteCommand(json);
			json = JSONObjectBuilder.buildJSONRatCommandResponse(response);
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllUserComments.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
//			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			
//			this.verifyQueries("AddNewDomainTemplate.conf", rootDomainUUID);
//			
//			System.out.println("Utenti sotto RAT");
//			this.verifyQueries("AddNewUserTemplate.conf", rootDomainUUID);
//			
//			System.out.println("Commenti dell'utente " + dgr1UUID);
//			this.verifyQueries("AddCommentTemplate.conf", dgr1UUID);
//			
//			System.out.println("Commenti dell'utente " + dgr2UUID);
//			this.verifyQueries("AddCommentTemplate.conf", dgr2UUID);
//			
//			System.out.println("Commenti associati al dominio " + DGRDomain1UUID);
//			this.verifyQueries("AddCommentTemplate.conf", DGRDomain1UUID);
//			
//			System.out.println("Commenti associati al dominio " + DGRDomain2UUID);
//			this.verifyQueries("AddCommentTemplate.conf", DGRDomain2UUID);
//			
//			System.out.println("Commenti associati al dominio " + DGRDomain3UUID);
//			this.verifyQueries("AddCommentTemplate.conf", DGRDomain3UUID);
//			
//			System.out.println("Utenti del dominio " + DGRDomain3UUID);
//			this.verifyQueries("BindGraphTemplate.conf", DGRDomain3UUID);
//			
//			System.out.println("Utenti del dominio " + DGRDomain2UUID);
//			this.verifyQueries("BindGraphTemplate.conf", DGRDomain2UUID);
//			
//			System.out.println("Utenti del dominio " + DGRDomain1UUID);
//			this.verifyQueries("BindGraphTemplate.conf", DGRDomain1UUID);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String query(String fileName, String paramName, String ownerNodeUUID) throws Exception{
		String json = this.readRemoteQueryJSONFile(fileName);
		RATJsonObject jsonHeader = RATHelpers.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATHelpers.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue(paramName, ownerNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATHelpers.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return newJson;
	}
	
	public static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> whileFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
		@Override
		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
			return true;
		}
	};
	
	public static final PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean> emitFunction = new PipeFunction<LoopPipe.LoopBundle<Vertex>,Boolean>(){
		@Override
		public Boolean compute(LoopPipe.LoopBundle<Vertex> argument) {
			Vertex vertex = argument.getObject();
			if(vertex == null){
//				System.out.println("vertex == null");
				return false;
			}
			Object property = vertex.getProperty(RATConstants.VertexTypeField);
			if(property == null){
//				System.out.println("property == null");
				return false;
			}
			
        	boolean result = property.toString().equalsIgnoreCase(VertexType.QueryPivot.toString());
            return result;
		}
	};
	
	public void verifyQueries2(String rootDomainUUID) throws Exception{
		if(!Utils.isUUID(rootDomainUUID)){
			// TODO exception
		}
		
		UUID rootUUID = UUID.fromString(rootDomainUUID);
		IStorage storage = StorageBridge.getInstance().getStorage();
		Vertex rootVertex = storage.getVertex(rootUUID);
		
		GremlinPipeline<Vertex, Vertex> queryPipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		queryPipe.in("AddNewDomain").has(RATConstants.VertexContentField, "is-put-by");
		queryPipe.in("AddNewDomain").has(RATConstants.VertexTypeField, VertexType.Domain.toString());
		System.out.println(queryPipe.toString());
		
//		GremlinPipeline<Vertex, Vertex> queryPipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
//		queryPipe.inV().has(RATConstants.VertexContentField, "is-put-by");
//		queryPipe.in("AddNewDomain").has(RATConstants.VertexTypeField, VertexType.Domain.toString());
		
		Object result = queryPipe.toList();
		System.out.println(result.toString());
	}
	
	private String getEdgeLabel(Vertex vertexFrom, Vertex vertexTo) throws Exception{
		GremlinPipeline<Vertex, Edge> pipe = new GremlinPipeline<Vertex, Edge>(vertexFrom);
		EdgeFilterFunction f = new EdgeFilterFunction(RATConstants.VertexUUIDField, vertexTo.getProperty(RATConstants.VertexUUIDField).toString());
//		Edge edge = pipe.inE().outV().filter(f).outE().next();
		Edge edge = pipe.outE().inV().filter(f).inE().next();
		
		String label = edge.getLabel();
		
		return label;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public void verifyQueries(String fileName, String rootDomainUUID) throws Exception{
		if(!Utils.isUUID(rootDomainUUID)){
			// TODO exception
		}
		
		String commandTemplatesPath = RATHelpers.getCommandsPath(RATConstants.CommandTemplatesFolder);
		String ratJson = FileUtils.fileRead(commandTemplatesPath + FileSystems.getDefault().getSeparator() + fileName);

		Graph commandGraph = RATHelpers.getRATJsonSettingsGraph(ratJson);
		String rootVertexUUID = RATHelpers.getRATJsonHeaderProperty(ratJson, RATConstants.RootVertexUUID);
		if(!Utils.isUUID(rootVertexUUID)){
			// TODO exception
		}
		
		Iterable<Vertex>iterable = commandGraph.getVertices(RATConstants.VertexUUIDField, rootVertexUUID);
		Vertex commandVertex = iterable.iterator().next();
		if(commandVertex == null){
			// TODO exception
		}

		UUID rootUUID = UUID.fromString(rootDomainUUID);
		IStorage storage = StorageBridge.getInstance().getStorage();
		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(commandVertex);
		List<List> lists = p.out().loop(1, AddRootDomainTest.whileFunction, AddRootDomainTest.emitFunction).path().toList();
		
//		System.out.println(lists.size());
		for(List list: lists){
			
			GremlinPipeline<Vertex, Vertex> queryPipe = null;
			ListIterator lIt = list.listIterator(list.size());
			
			while(lIt.hasPrevious()) {
				Object obj = lIt.previous();
				Vertex vertex = (Vertex) obj;
//				System.out.println("Current vertex: " + vertex);
				
				String edgeLabel = commandVertex.getProperty(RATConstants.VertexCommandOwnerField);
				VertexType vertexType = VertexType.fromString(vertex.getProperty(RATConstants.VertexTypeField).toString());
				switch(vertexType){
				case QueryPivot:
					Vertex rootVertex = storage.getVertex(rootUUID);
					queryPipe = new GremlinPipeline<Vertex, Vertex>(rootVertex);
					break;
					
				case SystemKey:
					String content = vertex.getProperty(RATConstants.VertexContentField);
					queryPipe.in(edgeLabel).has(RATConstants.VertexContentField, content);
					break;
					
				default:
					queryPipe.in(edgeLabel).has(RATConstants.VertexTypeField, vertexType.toString());
					break;
						
				}
				
//				System.out.println(queryPipe);
			}
			Object result = queryPipe.toList();
			System.out.println(result.toString());
		}
		
	}
	
	private String setAddCommentsValues(String fileName, String ownerNodeUUID, String userNodeUUID, int start, int end, String urlDocument, String inputFromUser) throws Exception{
		String json = this.readRemoteCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATHelpers.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATHelpers.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("ownerNodeUUID", ownerNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userNodeUUID", userNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("start", String.valueOf(start), ReturnType.integer);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("end", String.valueOf(end), ReturnType.integer);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("VertexContentField", inputFromUser, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("urlDocument", urlDocument, ReturnType.url);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATHelpers.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return newJson;
	}
	
	private String setBindGraphValues(String fileName, String domainUUID, String userUUID) throws Exception{
		String json = this.readRemoteCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATHelpers.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATHelpers.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("domainUUID", domainUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userUUID", userUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATHelpers.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return newJson;
	}

	private String setAddNewDomainValues(String fileName, String rootDomainUUID, String domainName) throws Exception{
		String json = this.readRemoteCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATHelpers.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATHelpers.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("nodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("VertexLabelField", domainName, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("VertexContentField", domainName, ReturnType.string);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATHelpers.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return newJson;
	}
	
	private String readRemoteCommandJSONFile(String fileName) throws Exception{
		String commandsPath = RATHelpers.getCommandsPath(RATConstants.CommandsFolder);
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(commandsPath);
		pathBuffer.append(fileName);
		
		String templatePath = pathBuffer.toString();
		String input = FileUtils.fileRead(templatePath);
		
		return input;
	}
	
	private String readRemoteQueryJSONFile(String fileName) throws Exception{
		String commandsPath = RATHelpers.getCommandsPath(RATConstants.QueriesFolder);
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(commandsPath);
		pathBuffer.append(fileName);
		
		String templatePath = pathBuffer.toString();
		String input = FileUtils.fileRead(templatePath);
		
		return input;
	}
	
	private String setAddRootDomainValues(String fileName) throws Exception{
		String json = this.readRemoteCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATHelpers.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATHelpers.getSettings(jsonHeader));
		int changed = remoteCommandsContainer.setValue("commandsNodeUUID", "42fc1097-b340-41a1-8ab2-e4d718ad48b9", ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("queriesNodeUUID", "309c67c5-fd6e-4124-8cb6-8a455de32233", ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATHelpers.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return newJson;
	}
	
	private String setAddNewUserValues(String fileName, String rootDomainUUID, String userName, String pwd) throws Exception{
		String json = this.readRemoteCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATHelpers.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATHelpers.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("ratNodeUUID", rootDomainUUID, ReturnType.uuid);
		System.out.println("ratNodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(3, changed);
		
		changed = remoteCommandsContainer.setValue("userName", userName, ReturnType.string);
		System.out.println("userName changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userPwd", pwd, ReturnType.string);
		System.out.println("userPwd changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATHelpers.getRATJson(jsonHeader);
		
		return newJson;
	}
	
	private String setAddRootDomainAdminUserValues(String fileName, String newRootDomainUUID) throws Exception{
		String json = this.readRemoteCommandJSONFile(fileName);
		RATJsonObject jsonHeader = RATHelpers.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATHelpers.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue("nodeUUID", newRootDomainUUID, ReturnType.uuid);
		System.out.println("nodeUUID changed in " + fileName + ": " + changed);
		Assert.assertEquals(2, changed);
		
		changed = remoteCommandsContainer.setValue("userName", "admin", ReturnType.string);
		System.out.println("userName changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("userPwd", "admin", ReturnType.string);
		System.out.println("userPwd changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATHelpers.getRATJson(jsonHeader);
		
		return newJson;
	}
	
	private void executeFromBean(SystemCommandsInitializer systemCommandsInitializer)throws Exception{
		String[] executionOrder = (String[]) TestHelpers.getPrivateDataMember(SystemCommandsInitializer.class, systemCommandsInitializer, "_executionOrder");
		@SuppressWarnings("unchecked")
		Map<String, String> componentMap = (Map<String, String>) TestHelpers.getPrivateDataMember(SystemCommandsInitializer.class, systemCommandsInitializer, "_componentMap");
		this.readRemoteCommands(componentMap, executionOrder);
	}
	
	private Response executeRemoteCommand(String json)throws Exception{
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String rootDomain = AppProperties.getInstance().getStringProperty(RATConstants.RootDomain);
		String input = json.replace(placeHolder, rootDomain);
		
		CommandSink commandSink = new CommandSink();
		Response response = commandSink.doCommand(input);
		
		StatusCode statusCode = StatusCode.fromString(response.getStatusCode());
		System.out.println("StatusCode " + statusCode.toString());
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(commandResponse.getResponse()));
		
		Assert.assertEquals(StatusCode.Ok, statusCode);

		return response;
	}

	private void readRemoteCommands(Map<String, String> componentMap, String[] executionOrder) throws Exception{
		String commandsPath = RATHelpers.getCommandsPath(RATConstants.CommandsFolder);
		
//		for(String key :executionOrder){
//			String fileName = componentMap.get(key);
//			this.executeRemoteCommand(commandsPath, fileName);
//		}
	}
	
	@After
	public void verify(){
		// Anche in caso di exception voglio che scriva lo stesso i risultati per vederli
		try {
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "storage-manager.properties");
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "unittest.properties");
			
			String resultFilename = this.getClass().getSimpleName() + "Result";
			String path = TestHelpers.writeGraphToHTML(resultFilename, "commandResults");
			TestHelpers.dumpJson(path);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
