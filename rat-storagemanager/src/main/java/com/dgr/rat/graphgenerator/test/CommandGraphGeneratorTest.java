/**
 * @author Daniele Grignani (dgr)
 * @date Sep 15, 2015
 */

package com.dgr.rat.graphgenerator.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionEdgeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionParameterEdgeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionParameterNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.graphgenerator.JSONObjectBuilder;
import com.dgr.rat.graphgenerator.GraphGeneratorHelpers;
import com.dgr.rat.graphgenerator.MakeSigmaJSON;
import com.dgr.rat.graphgenerator.commands.AbstractCommand;
import com.dgr.rat.graphgenerator.commands.AddComment;
import com.dgr.rat.graphgenerator.commands.AddNewDomain;
import com.dgr.rat.graphgenerator.commands.AddNewUser;
import com.dgr.rat.graphgenerator.commands.AddRootPlatformNode;
import com.dgr.rat.graphgenerator.commands.AddRootDomainAdminUser;
import com.dgr.rat.graphgenerator.commands.AddSubComment;
import com.dgr.rat.graphgenerator.commands.BindFromDomainToUser;
import com.dgr.rat.graphgenerator.commands.BindFromUserToDomain;
import com.dgr.rat.graphgenerator.commands.ICommandCreator;
import com.dgr.rat.graphgenerator.commands.LoadCommands;
import com.dgr.rat.graphgenerator.commands.LoadQueries;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.AppProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

public class CommandGraphGeneratorTest {
	// TODO: da aprire dal file di properties
	public static final String LoadCommandsUUID = "42fc1097-b340-41a1-8ab2-e4d718ad48b9";
	public static final String LoadQueriesUUID = "309c67c5-fd6e-4124-8cb6-8a455de32233";
	public static final String RootDomainUUID = "b5876c07-5714-4f22-ab46-00f072d503cb";
	
	List<String>_ratJSONs = new ArrayList<String>();
	Map<String, String>_clientJsonCommands = new HashMap<String, String>();
	
	private ICommandCreator addRootDomain(String commandVersion) throws Exception{
		String commandName = "AddRootDomain";
		
		AbstractCommand command = new AddRootPlatformNode(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.addNodesToGraph();
		command.buildGraph();
		
		return command;
	}
	
	private ICommandCreator loadQueries(String commandVersion) throws Exception{
		String commandName = "LoadQueries";
		
		AbstractCommand command = new LoadQueries(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.addNodesToGraph();
		command.buildGraph();

		return command;
	}
	
	private ICommandCreator loadCommands(String commandVersion) throws Exception{
		String commandName = JSONType.LoadCommands.toString();
		
		AbstractCommand command = new LoadCommands(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.addNodesToGraph();
		command.buildGraph();

		return command;
	}
	
	private ICommandCreator addRootDomainAdminUser(String commandVersion) throws Exception{
		String commandName = "AddRootDomainAdminUser";
		
		AbstractCommand command = new AddRootDomainAdminUser(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.addNodesToGraph();
		command.buildGraph();
		
		return command;
	}
	
	private ICommandCreator addNewDomain(String commandVersion) throws Exception{
		String commandName = "AddNewDomain";
		
		AbstractCommand command = new AddNewDomain(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.addNodesToGraph();
		command.buildGraph();
		
		return command;
	}
	
	private ICommandCreator addNewUser(String commandVersion) throws Exception{
		String commandName = "AddNewUser";
		
		AbstractCommand command = new AddNewUser(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.addNodesToGraph();
		command.buildGraph();

		return command;
	}
	
	private ICommandCreator addSubComment(String commandVersion) throws Exception{
		String commandName = "AddSubComment";
		
		AbstractCommand command = new AddSubComment(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.set_edgeName("AddSubComment");
		command.addNodesToGraph();
		command.buildGraph();

		return command;
	}
	
//	private AbstractCommand addGetCommandParams(String commandVersion) throws Exception{
//		String commandName = "GetCommandParams";
//		
//		AbstractCommand command = new GetCommandParams(commandName, commandVersion);
//		command.set_commandType(JSONType.SystemCommands);
//		command.addNodesToGraph();
//		command.buildGraph();
//
//		return command;
//	}
	
	private ICommandCreator addBindGraphFromDomainToUser(String commandVersion) throws Exception{
		String commandName = "BindFromDomainToUser";
		
		AbstractCommand command = new BindFromDomainToUser(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.set_edgeName("BindDomainUser");
		command.addNodesToGraph();
		command.buildGraph();

		return command;
	}
	
	private ICommandCreator addBindGraphFromUserToDomain(String commandVersion) throws Exception{
		String commandName = "BindFromUserToDomain";
		
		AbstractCommand command = new BindFromUserToDomain(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.set_edgeName("BindDomainUser");
		command.addNodesToGraph();
		command.buildGraph();

		return command;
	}
	
	// TODO: da sistemare in quanto è uno userCommand
	private ICommandCreator addAddComment(String commandVersion) throws Exception{
		String commandName = "AddComment";
		
		AbstractCommand command = new AddComment(commandName, commandVersion);
		command.set_commandType(JSONType.SystemCommands);
		command.addNodesToGraph();
		command.buildGraph();

		return command;
	}
	
//	// TODO: da sistemare in quanto è uno userCommand (forse....)
//	private AbstractCommand addGetAllDomains(String commandVersion) throws Exception{
//		String commandName = "GetAllDomains";
//		
//		AbstractCommand command = new GetAllDomains(commandName, commandVersion);
//		command.set_commandType(JSONType.SystemCommands);
//		command.addNodesToGraph();
//		command.buildGraph();
//
//		return command;
//	}
	
	private void addCommands() throws Exception{
		GraphGeneratorHelpers.storeUUID(RATConstants.Commands, UUID.fromString(LoadCommandsUUID));
		GraphGeneratorHelpers.storeUUID(RATConstants.Queries, UUID.fromString(LoadQueriesUUID));
		GraphGeneratorHelpers.storeUUID(VertexType.RootDomain.toString(), UUID.fromString(RootDomainUUID));
		
		RATHelpers.initProperties(GraphGeneratorHelpers.StorageManagerPropertyFile);
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String applicationName = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationName);
		String applicationVersion = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationVersionField);
		
		ICommandCreator command = null;
		
		// LoadCommands
		command = this.loadCommands("0.1");
//		String commandName = JSONType.LoadCommands.toString();
//		
//		AbstractCommand command = new LoadCommands(commandName, commandVersion);
//		command.set_commandType(JSONType.SystemCommands);
//		command.addNodesToGraph();
//		command.buildGraph();
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");
		
		// LoadQueries
		command = this.loadQueries("0.1");
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");
		
		// AddRootDomain
		command = this.addRootDomain("0.1");
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");
	
		// AddRootDomainAdminUser
		command = this.addRootDomainAdminUser("0.1");
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");

		// AddNewDomain
		command = this.addNewDomain("0.1");
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");
		
		// AddNewUser
		command = this.addNewUser("0.1");
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");
		
		// BindDomaUser
		command = this.addBindGraphFromDomainToUser("0.1");
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");
		command = this.addBindGraphFromUserToDomain("0.1");
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");
		
		// AddComment
		command = this.addAddComment("0.1");
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");
		
		// AddSubComment
		command = this.addSubComment("0.1");
		this.writeAll(command, placeHolder, applicationName, applicationVersion, "commands");
		
//		/*Queries*/
//		// GetCommandParams
//		command = this.addGetCommandParams("0.1");
//		this.writeAll(command, placeHolder, applicationName, applicationVersion);
//		
//		// GetAllDomains
//		command = this.addGetAllDomains("0.1");
//		this.writeAll(command, placeHolder, applicationName, applicationVersion);
	}
	
	@Test
	public void test() {
		try {
			this.addCommands();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeAll(ICommandCreator command, String placeHolder, String applicationName, String applicationVersion, String destinationFolder) throws Exception{
		JsonHeader header = new JsonHeader();
		header.setApplicationName(applicationName);
		header.setApplicationVersion(applicationVersion);
		header.setCommandVersion(command.get_commandVersion());
		header.setDomainName(placeHolder);
		header.setMessageType(MessageType.Template);
		header.setCommandType(command.get_commandType());
		header.setCommandName(command.get_commandName());
		header.setCommandGraphUUID(command.get_commandUUID());
		header.setRootVertexUUID(command.get_rootNodeUUID());
		
		String path = GraphGeneratorHelpers.CommandsFolder + GraphGeneratorHelpers.PathSeparator + command.get_commandName() + ".conf";
		String remoteRequestJson = JSONObjectBuilder.buildRemoteCommand(header, command.get_rootNode());
		
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(remoteRequestJson));
		GraphGeneratorHelpers.writeGraphToJson(remoteRequestJson, path);
		
		String commandTemplate = JSONObjectBuilder.buildCommandTemplate(header, command.getGraph());
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(commandTemplate));
		path = GraphGeneratorHelpers.CommandTemplatesFolder + GraphGeneratorHelpers.PathSeparator + command.get_commandName() + "Template.conf";
		GraphGeneratorHelpers.writeGraphToJson(commandTemplate, path);

		//Alchemy command template JSON
		String alchemyJSON = MakeSigmaJSON.fromRatJsonToAlchemy(commandTemplate);
		GraphGeneratorHelpers.writeAlchemyJson(command.get_commandName() + "Template", command.get_commandVersion(), alchemyJSON, destinationFolder);

		this.saveForTest(commandTemplate, remoteRequestJson);
//		JSONObjectBuilder.buildQuery(header, command.get_rootNode().getNode().asVertex());
	}
	
	private void saveForTest(String ratJson, String clientJsonCommand) throws JsonParseException, JsonMappingException, IOException{
		_ratJSONs.add(ratJson);
		
		String commandName = RATJsonUtils.getRATJsonHeaderProperty(ratJson, RATConstants.CommandName);
		_clientJsonCommands.put(commandName, clientJsonCommand);
	}
	
	@After
	public void after(){
		try {
			this.verifyUUID();
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
	
	private void verifyParams(IInstructionNodeFrame instruction) throws Exception{
		long num = instruction.getNumberOfInstructionParameters();
		for(int i = 0; i < num; i++){
			IInstructionParameterNodeFrame instructionParameter = instruction.getInstructionParameter(i);
			int order = instructionParameter.getInstructionOrderField();
			Assert.assertEquals(order, i);
			
			System.out.println(instructionParameter.getVertexContentField());
			System.out.println(instructionParameter.getVertexLabelField());
			System.out.println(instructionParameter.getVertexUUIDField());
			System.out.println(instructionParameter.getVertexUserCommandsInstructionsParameterNameField());
			System.out.println(instructionParameter.getVertexUserCommandsInstructionsParameterValueField());
		}
		
		Iterator<IInstructionParameterEdgeFrame> it = instruction.getInstructionParameterInfo().iterator();
		while(it.hasNext()){
			IInstructionParameterEdgeFrame instructionParameterInfo = it.next();
			IInstructionNodeFrame i = instructionParameterInfo.getInstruction();
			Assert.assertEquals(i, instruction);
			
			IInstructionParameterNodeFrame instructionParameter = instruction.getInstructionParameter(instructionParameterInfo.getWeight());
			IInstructionParameterNodeFrame instructionParam = instructionParameterInfo.getInstructionParameter();
			Assert.assertEquals(instructionParameter, instructionParam);
		}
	}
	
	private void verifyInstructions(IRATNodeFrame node) throws Exception{
		long num = node.getNumberOfInstructions();
		for(int i = 0; i < num; i++){
			IInstructionNodeFrame instruction = node.getInstruction(i);
			int order = instruction.getInstructionOrderField();
			Assert.assertEquals(order, i);
			this.verifyParams(instruction);
			
			System.out.println(instruction.getVertexContentField());
			System.out.println(instruction.getVertexLabelField());
			System.out.println(instruction.getVertexUUIDField());
		}
		
		Iterator<IInstructionEdgeFrame> it = node.getInstructionInfo().iterator();
		while(it.hasNext()){
			IInstructionEdgeFrame instructionInfo = it.next();
			IRATNodeFrame n = instructionInfo.getRatNode();
			Assert.assertEquals(n, node);
			IInstructionNodeFrame instruction = node.getInstruction(instructionInfo.getWeight());
			IInstructionNodeFrame ins = instructionInfo.getInstruction();
			Assert.assertEquals(instruction, ins);
		}
		
//		GremlinPipeline<Vertex, Edge> pipe = new GremlinPipeline<Vertex, Edge> (node.asVertex());
//		long num = pipe.outE().has("weight").count();
//		System.out.println(num);
//		for(int i = 0; i < num; i++){
//			pipe = new GremlinPipeline<Vertex, Edge> (node.asVertex());
//			Edge e = (Edge) pipe.outE().has("weight", i).next();
//			System.out.println(e.toString());
//		}
	}

	private void verifyUUID() throws Exception{
		Map<String, Vertex>vertexUUIDs = new HashMap<String, Vertex>();
		Map<String, Vertex>rootUUIDs = new HashMap<String, Vertex>();
		Map<String, String>commandUUIDs = new HashMap<String, String>();
		
		boolean exists = false;
		
		for(String ratJSON : _ratJSONs){
			String commandUUID = RATJsonUtils.getRATJsonHeaderProperty(ratJSON, RATConstants.CommandGraphUUID);
			String commandName = RATJsonUtils.getRATJsonHeaderProperty(ratJSON, RATConstants.CommandName);
			String rootVertexUUID = RATJsonUtils.getRATJsonHeaderProperty(ratJSON, RATConstants.RootVertexUUID);
			Graph settingsGraph = GraphGeneratorHelpers.getRATJsonSettingsGraph(ratJSON);
			FramedGraph<Graph> framedGraph =  new FramedGraphFactory(new JavaHandlerModule()).create(settingsGraph);
			exists = commandUUIDs.containsKey(commandUUID);
			Assert.assertFalse(exists);

			exists = rootUUIDs.containsKey(commandUUID);
			Assert.assertFalse(exists);
			
			exists = vertexUUIDs.containsKey(commandUUID);
			Assert.assertFalse(exists);
			
			commandUUIDs.put(commandUUID, ratJSON);
			
			// Verifico che internamente non ci siano UUID ripetuti, se non quello del rootVertex
			Iterator<Vertex> it = settingsGraph.getVertices().iterator();
			while(it.hasNext()){
				Vertex vertex = it.next();
				
				/*VertexType*/ String type = vertex.getProperty(RATConstants.VertexTypeField);
				if(!VertexType.Instruction.toString().equals(type) && !VertexType.InstructionParameter.toString().equals(type)){
					IRATNodeFrame node = framedGraph.frame(vertex, IRATNodeFrame.class);
					this.verifyInstructions(node);
				}
				
				String vertexUUID = vertex.getProperty(RATConstants.VertexUUIDField);
				Boolean isRoot = vertex.getProperty(RATConstants.VertexIsRootField);
				
				if(isRoot != null && isRoot){
					Assert.assertTrue(rootVertexUUID.equals(vertexUUID));
					
					exists = rootUUIDs.containsKey(vertexUUID);
					if(exists){
						System.out.println("Command: " + commandName);
						Vertex v = rootUUIDs.get(vertexUUID);
						for(String key : v.getPropertyKeys()){
							System.out.println("key: " + key + "; value: "+ v.getProperty(key).toString());
						}
						
					}
					Assert.assertFalse(exists);
					
					exists = vertexUUIDs.containsKey(vertexUUID);
					if(exists){
						System.out.println("Command: " + commandName);
						Vertex v = vertexUUIDs.get(vertexUUID);
						for(String key : v.getPropertyKeys()){
							System.out.println("key: " + key + "; value: "+ v.getProperty(key).toString());
						}
						
					}
					Assert.assertFalse(exists);
					
					exists = commandUUIDs.containsKey(vertexUUID);
					if(exists){
						String j = commandUUIDs.get(vertexUUID);
						System.out.println(j);
					}
					Assert.assertFalse(exists);
					
					rootUUIDs.put(vertexUUID, vertex);
				}
				else{
					exists = vertexUUIDs.containsKey(vertexUUID);
					if(exists){
						System.out.println("Command: " + commandName);
						Vertex v = vertexUUIDs.get(vertexUUID);
						System.out.println("UUID: " + vertexUUID);
						for(String key : v.getPropertyKeys()){
							System.out.println("key: " + key + "; value: "+ v.getProperty(key).toString());
						}
						
					}
					Assert.assertFalse(exists);
					
					exists = rootUUIDs.containsKey(vertexUUID);
					if(exists){
						System.out.println("Command: " + commandName);
						Vertex v = rootUUIDs.get(vertexUUID);
						for(String key : v.getPropertyKeys()){
							System.out.println("key: " + key + "; value: "+ v.getProperty(key).toString());
						}
						
					}
					Assert.assertFalse(exists);
					
					exists = commandUUIDs.containsKey(vertexUUID);
					if(exists){
						String j = commandUUIDs.get(vertexUUID);
						System.out.println(j);
					}
					Assert.assertFalse(exists);
					
					vertexUUIDs.put(vertexUUID, vertex);
				}
			}
		}
		
		Assert.assertEquals(rootUUIDs.size(), _ratJSONs.size());
		Assert.assertEquals(commandUUIDs.size(), _ratJSONs.size());
	}
}
