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
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.graphgenerator.GraphGeneratorHelpers;
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
import com.rat.init.SystemInitializerTestHelpers;
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
			systemCommandsInitializer.addCommandTemplates();
			
			String json = this.setAddRootDomainValues("AddRootDomain.conf");
//			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			Response response = this.executeRemoteCommand(json);
			String rootDomainUUID = this.getNewRootNodeUUID(response);

			String userAdminName = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminName);
			String userAdminPwd = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminPwd);
			String userAdminEmail = AppProperties.getInstance().getStringProperty(RATConstants.DBDefaultAdminEmail);
			
			//json = this.setAddRootDomainAdminUserValues("AddRootDomainAdminUser.conf", rootDomainUUID);
			//createAddRootDomainAdminUser(String fileName, String rootDomainUUID, String userAdminName, String userAdminPwd, String email) 
			json = SystemInitializerTestHelpers.createAddRootDomainAdminUser("AddRootDomainAdminUser.conf", rootDomainUUID, userAdminName, userAdminPwd, userAdminEmail);
			response = this.executeRemoteCommand(json);
			
			json = SystemInitializerTestHelpers.createNewUser("AddNewUser.conf", rootDomainUUID, "dgr1", "dgr1", "dgr1@gmail.com");
			response = this.executeRemoteCommand(json);
			String dgr1UUID = this.getNewRootNodeUUID(response);
//			
			json = SystemInitializerTestHelpers.createNewUser("AddNewUser.conf", rootDomainUUID, "dgr2", "dgr2", "dgr2@gmail.com");
			response = this.executeRemoteCommand(json);
			String dgr2UUID = this.getNewRootNodeUUID(response);
			
//			json = SystemInitializerTestHelpers.createNewUser("AddNewUser.conf", rootDomainUUID, "dgr3", "dgr3", "dgr2@gmail.com");
//			response = this.executeRemoteCommand(json);
//			String dgr3UUID = this.getNewRootNodeUUID(response);
////			
			json = SystemInitializerTestHelpers.createNewDomain("AddNewDomain.conf", rootDomainUUID, "DGR1 Domain 1");
			response = this.executeRemoteCommand(json);
			String DGRDomain1UUID = this.getNewRootNodeUUID(response);
			
			json = SystemInitializerTestHelpers.createNewDomain("AddNewDomain.conf", rootDomainUUID, "DGR1 Domain 2");
			response = this.executeRemoteCommand(json);
			String DGRDomain2UUID = this.getNewRootNodeUUID(response);
//			String commandGraphUUID = response.getCommandGraphUUID();
			
			json = SystemInitializerTestHelpers.createNewDomain("AddNewDomain.conf", DGRDomain2UUID, "DGR1 Domain 2.1");
			response = this.executeRemoteCommand(json);
			String DGRDomain21UUID = this.getNewRootNodeUUID(response);
////			
			json = SystemInitializerTestHelpers.bindUserToDomain("BindFromUserToDomain.conf", DGRDomain1UUID, dgr1UUID);
			response = this.executeRemoteCommand(json);
			
			json = SystemInitializerTestHelpers.bindUserToDomain("BindFromUserToDomain.conf", DGRDomain2UUID, dgr1UUID);
			response = this.executeRemoteCommand(json);
			
			json = SystemInitializerTestHelpers.bindUserToDomain("BindFromUserToDomain.conf", DGRDomain2UUID, dgr2UUID);
			response = this.executeRemoteCommand(json);
			
			json = SystemInitializerTestHelpers.bindUserToDomain("BindFromUserToDomain.conf", DGRDomain1UUID, dgr2UUID);
			response = this.executeRemoteCommand(json);
			
			//addUserComment(String fileName, String domainUUID, String userNodeUUID, int startComment, int endComment, String url, String vertexContentField, String vertexLabelFie
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", DGRDomain1UUID, dgr1UUID, 25, 345, 5, 18, "http://www.repubblica.it/", "il mio commento a la repubblica", "larepubblica.it 1");
			response = this.executeRemoteCommand(json);
			
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", DGRDomain1UUID, dgr1UUID, 78, 90, 5, 18, "http://www.repubblica.it/", "il mio secondo commento a la repubblica", "larepubblica.it 2");
			response = this.executeRemoteCommand(json);
			
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", DGRDomain1UUID, dgr1UUID, 258, 300, 5, 18, "http://ilfattoquotidiano.it", "il mio commento a ilfattoquotidiano", "ilfattoquotidiano.it 1");
			response = this.executeRemoteCommand(json);
			
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", DGRDomain1UUID, dgr2UUID, 25, 345, 5, 18, "http://www.repubblica.it/", "dgr2UUID il mio commento 1 a la repubblica", "Commento 1");
			response = this.executeRemoteCommand(json);
			String comment1UUID = this.getNewRootNodeUUID(response);
			
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", DGRDomain1UUID, dgr2UUID, 25, 345, 5, 18, "http://www.repubblica.it/", "dgr2UUID il mio commento 1 a la repubblica", "Commento 2");
			response = this.executeRemoteCommand(json);
			String comment2UUID = this.getNewRootNodeUUID(response);
			
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", comment1UUID, dgr2UUID, 25, 345, 5, 18, "http://www.repubblica.it/", "dgr2UUID il mio sub commento 1 al comment 1 a la repubblica", "Commento 1.1");
			response = this.executeRemoteCommand(json);
			String subComment1UUID = this.getNewRootNodeUUID(response);
			
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", comment1UUID, dgr2UUID, 25, 345, 5, 18, "http://www.repubblica.it/", "dgr2UUID il mio sub commento 1 al comment 1 a la repubblica", "Commento 1.2");
			response = this.executeRemoteCommand(json);
			String subComment5UUID = this.getNewRootNodeUUID(response);
			
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", subComment1UUID, dgr2UUID, 25, 345, 5, 18, "http://www.repubblica.it/", "dgr2UUID il mio sub commento 1 sub commento 1 al comment 1 a la repubblica", "Commento 1.1.1");
			response = this.executeRemoteCommand(json);
			String subComment2UUID = this.getNewRootNodeUUID(response);
			
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", subComment1UUID, dgr2UUID, 25, 345, 5, 18, "http://www.repubblica.it/", "dgr2UUID il mio sub commento 1 sub commento 1 al comment 1 a la repubblica", "Commento 1.1.2");
			response = this.executeRemoteCommand(json);
			String subComment3UUID = this.getNewRootNodeUUID(response);
			
			json = SystemInitializerTestHelpers.addUserComment("AddComment.conf", comment2UUID, dgr2UUID, 25, 345, 5, 18, "http://www.repubblica.it/", "dgr2UUID il mio sub commento 1 al comment 1 a la repubblica", "Commento 2.1");
			response = this.executeRemoteCommand(json);
			String subComment4UUID = this.getNewRootNodeUUID(response);
			
//			IStorage storage = StorageBridge.getInstance().getStorage();
//			Vertex vertex = storage.getRootDomain();
//			
//			try {
//				JSONObject jsonObject = GraphSONUtility.jsonFromElement(vertex, vertex.getPropertyKeys(), GraphSONMode.COMPACT);
//				System.out.println(jsonObject.toString());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + RATConstants.PropertyFileName);
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "unittest.properties");
			
			String dir = "";//".." + FileSystems.getDefault().getSeparator();
			
			String commandJSON = SystemInitializerTestHelpers.createGetUserByEmail("GetAdminUserByEmail.conf", rootDomainUUID, "userEmail", "admin@admin.com");
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			String alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			String resultFilename = dir + "GetAdminUserByEmail.conf" + "QueryResult";
			String path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			commandJSON = SystemInitializerTestHelpers.createGetAllNodesByType("GetAllUsers.conf", rootDomainUUID);
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllUsers.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			commandJSON = SystemInitializerTestHelpers.createGetUserByEmail("GetUserByEmail.conf", rootDomainUUID, "userEmail", "dgr1@gmail.com");
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetUserByEmail.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			commandJSON = SystemInitializerTestHelpers.createGetAllNodesByType("GetAllDomains.conf", rootDomainUUID);
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllDomains.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			commandJSON = SystemInitializerTestHelpers.createGetUserByEmail("GetDomainByName.conf", rootDomainUUID, "domainName", "DGR1 Domain 2");
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetDomainByName.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			commandJSON = SystemInitializerTestHelpers.createGetUsersAndDomains("GetAllUserDomains.conf", dgr1UUID, VertexType.Domain);
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllUserDomains.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			commandJSON = SystemInitializerTestHelpers.createGetUsersAndDomains("GetAllDomainUsers.conf", DGRDomain2UUID, VertexType.User);
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllDomainUsers.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			commandJSON = SystemInitializerTestHelpers.createGetUserDomainByName("GetUserDomainByName.conf", dgr1UUID, "DGR1 Domain 2", VertexType.User);
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetUserDomainByName.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			//DGRDomain1UUID, dgr1UUID
			commandJSON = SystemInitializerTestHelpers.createGetAllDomainComments("GetAllDomainComments.conf", DGRDomain1UUID, dgr1UUID, "http://www.repubblica.it/");
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllDomainComments-dgr1UUID.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			commandJSON = SystemInitializerTestHelpers.createGetAllDomainComments("GetAllDomainComments.conf", DGRDomain1UUID, dgr2UUID, "http://www.repubblica.it/");
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllDomainComments-dgr2UUID.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			commandJSON = SystemInitializerTestHelpers.createGetAllUserComments("GetAllUserComments.conf", dgr2UUID);
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetAllUserComments-dgr2UUID.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
			// COMMENT: prendo i subcomment
			commandJSON = SystemInitializerTestHelpers.createGetAllUserComments("GetCommentComments.conf", comment1UUID);
			response = this.executeRemoteCommand(commandJSON);
			json = JSONObjectBuilder.serializeCommandResponse(response);
			System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
			alchemyJson = MakeSigmaJSON.fromRatJsonToAlchemy(json);
			resultFilename = dir + "GetCommentComments-dgr2UUID.conf" + "QueryResult";
			path = TestHelpers.writeGraphToHTML(resultFilename, "queryResults");
			TestHelpers.writeGraphToJson(alchemyJson, path);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String query(String fileName, String paramName, String ownerNodeUUID) throws Exception{
		String json = this.readRemoteQueryJSONFile(fileName);
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
		int changed = remoteCommandsContainer.setValue(paramName, ownerNodeUUID, ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATJsonUtils.getRATJson(jsonHeader);
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

		Graph commandGraph = GraphGeneratorHelpers.getRATJsonSettingsGraph(ratJson);
		String rootVertexUUID = RATJsonUtils.getRATJsonHeaderProperty(ratJson, RATConstants.RootVertexUUID);
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
				
				String edgeLabel = commandVertex.getProperty(RATConstants.GraphCommandOwner);
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
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		
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
		String newJson = RATJsonUtils.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return newJson;
	}
	
//	private String setBindGraphValues(String fileName, String domainUUID, String userUUID) throws Exception{
//		String json = this.readRemoteCommandJSONFile(fileName);
//		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
//		
//		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
//		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
//		
//		int changed = remoteCommandsContainer.setValue("domainUUID", domainUUID, ReturnType.uuid);
//		System.out.println("Changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("userUUID", userUUID, ReturnType.uuid);
//		System.out.println("Changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		jsonHeader.setSettings(remoteCommandsContainer.serialize());
//		String newJson = RATJsonUtils.getRATJson(jsonHeader);
////		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
//		
//		return newJson;
//	}

//	private String setAddNewDomainValues(String fileName, String rootDomainUUID, String domainName) throws Exception{
//		String json = this.readRemoteCommandJSONFile(fileName);
//		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
//		
//		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
//		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
//		
//		int changed = remoteCommandsContainer.setValue("nodeUUID", rootDomainUUID, ReturnType.uuid);
//		System.out.println("Changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("VertexLabelField", domainName, ReturnType.string);
//		System.out.println("Changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("VertexContentField", domainName, ReturnType.string);
//		System.out.println("Changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		jsonHeader.setSettings(remoteCommandsContainer.serialize());
//		String newJson = RATJsonUtils.getRATJson(jsonHeader);
////		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
//		
//		return newJson;
//	}
	
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
		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
		
		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
		int changed = remoteCommandsContainer.setValue("commandsNodeUUID", "42fc1097-b340-41a1-8ab2-e4d718ad48b9", ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		changed = remoteCommandsContainer.setValue("queriesNodeUUID", "309c67c5-fd6e-4124-8cb6-8a455de32233", ReturnType.uuid);
		System.out.println("Changed in " + fileName + ": " + changed);
		Assert.assertEquals(1, changed);
		
		jsonHeader.setSettings(remoteCommandsContainer.serialize());
		String newJson = RATJsonUtils.getRATJson(jsonHeader);
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(newJson));
		
		return newJson;
	}
	
//	private String setAddNewUserValues(String fileName, String rootDomainUUID, String userName, String pwd) throws Exception{
//		String json = this.readRemoteCommandJSONFile(fileName);
//		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
//		
//		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
//		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
//		
//		int changed = remoteCommandsContainer.setValue("isPutByNode2UUID", rootDomainUUID, ReturnType.uuid);
//		System.out.println("isPutByNode2UUID changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("isUserOfNodeUUID", rootDomainUUID, ReturnType.uuid);
//		System.out.println("isUserOfNodeUUID changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("isPutByNodeUUID", rootDomainUUID, ReturnType.uuid);
//		System.out.println("isPutByNodeUUID changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("userName", userName, ReturnType.string);
//		System.out.println("userName changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("userPwd", pwd, ReturnType.string);
//		System.out.println("userPwd changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		jsonHeader.setSettings(remoteCommandsContainer.serialize());
//		String newJson = RATJsonUtils.getRATJson(jsonHeader);
//		
//		return newJson;
//	}
	
//	private String setAddRootDomainAdminUserValues(String fileName, String newRootDomainUUID) throws Exception{
//		String json = this.readRemoteCommandJSONFile(fileName);
//		RATJsonObject jsonHeader = RATJsonUtils.getRATJsonObject(json);
//		
//		RemoteCommandsContainer remoteCommandsContainer = new RemoteCommandsContainer();
//		remoteCommandsContainer.deserialize(RATJsonUtils.getSettings(jsonHeader));
//		
//		int changed = remoteCommandsContainer.setValue("isUserOfNodeUUID", newRootDomainUUID, ReturnType.uuid);
//		System.out.println("isUserOfNodeUUID changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("isPutByNodeUUID", newRootDomainUUID, ReturnType.uuid);
//		System.out.println("isPutByNodeUUID changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("userName", "admin", ReturnType.string);
//		System.out.println("userName changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		changed = remoteCommandsContainer.setValue("userPwd", "admin", ReturnType.string);
//		System.out.println("userPwd changed in " + fileName + ": " + changed);
//		Assert.assertEquals(1, changed);
//		
//		jsonHeader.setSettings(remoteCommandsContainer.serialize());
//		String newJson = RATJsonUtils.getRATJson(jsonHeader);
//		
//		return newJson;
//	}
	
	private void executeFromBean(SystemCommandsInitializer systemCommandsInitializer)throws Exception{
		String[] executionOrder = (String[]) TestHelpers.getPrivateDataMember(SystemCommandsInitializer.class, systemCommandsInitializer, "_executionOrder");
		@SuppressWarnings("unchecked")
		Map<String, String> componentMap = (Map<String, String>) TestHelpers.getPrivateDataMember(SystemCommandsInitializer.class, systemCommandsInitializer, "_componentMap");
		this.readRemoteCommands(componentMap, executionOrder);
	}
	
	private Response executeRemoteCommand(String json)throws Exception{
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String rootDomain = AppProperties.getInstance().getStringProperty(RATConstants.RootPlatformDomainName);
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
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + RATConstants.PropertyFileName);
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
