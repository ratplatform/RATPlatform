package org.rat.platform.rat_graph_generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.rat.platform.graph.CommandGraph;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.utils.RATUtils;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.tinkerpop.blueprints.Graph;

public class App {
	private static String _ratStorageManagerPath = null;
	private static BuildQueryJavaScript _buildQueryJavaScript = null;
	private static AlchemyManager _alchemyManager = null;
	
	private static Map<String, Graph>_commands = new HashMap<String, Graph>();
	
    public static void main( String[] args ){
    	try {
    		App.init();
    		
			String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
			String applicationName = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationName);
			String applicationVersion = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationVersionField);
			
			_buildQueryJavaScript = new BuildQueryJavaScript();
			App.createCommands(placeHolder, applicationName, applicationVersion);
			
			_alchemyManager.createCommands();
			
			_buildQueryJavaScript = new BuildQueryJavaScript();
			App.createQueries(placeHolder, applicationName, applicationVersion);
			
			_alchemyManager.createQueries();
		} 
    	catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println("Bye!");
    }
    
    public static void createQueries(String placeHolder, String applicationName, String applicationVersion){
    	try {
			CommandGraph graph = null;
			Queries queries = new Queries(_commands, _alchemyManager, _ratStorageManagerPath, _buildQueryJavaScript);
			
			graph = queries.getUserByEmail("GetUserByEmail", "0.1");
			graph.set_commandType(JSONType.Query);
			queries.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
//			graph = App.getAllNodesOfType("GetAllNodesOfType", "GetNodeByNodeField", "0.1");
//			graph.set_commandType(JSONType.SystemCommands);
//			App.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
			graph = queries.getDomainByName("GetDomainByName", "0.1");
			graph.set_commandType(JSONType.Query);
			queries.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
//			graph = App.getAllDomains("GetAllDomains", "GetNodeByNodeField", "0.1");
//			graph.set_commandType(JSONType.SystemCommands);
//			App.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
			graph = queries.getAllDomainUsers("GetAllDomainUsers", "0.1");
			graph.set_commandType(JSONType.Query);
			queries.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
			graph = queries.getAllUserDomains("GetAllUserDomains", "0.1");
			graph.set_commandType(JSONType.Query);
			queries.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
			graph = queries.getAllUserComments("GetAllUserComments", "0.1");
			graph.set_commandType(JSONType.Query);
			queries.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
			graph = queries.getUserURLs("GetUserURLs", "0.1");
			graph.set_commandType(JSONType.Query);
			queries.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
			graph = queries.getCommentComments("GetCommentComments", "0.1");
			graph.set_commandType(JSONType.Query);
			queries.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
			graph = queries.getNodesByType("GetNodeByType", "0.1");
			graph.set_commandType(JSONType.Query);
			queries.writeQuery(graph, placeHolder, applicationName, applicationVersion);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void createCommands(String placeHolder, String applicationName, String applicationVersion){
    	try {
    		Commands command = new Commands(_alchemyManager, _ratStorageManagerPath, _buildQueryJavaScript);
			CommandGraph graph = null;
			
			graph = command.addRootDomain("0.1");
			graph.set_commandType(JSONType.SystemCommands);
			command.writeCommands(graph, placeHolder, applicationName, applicationVersion);
			
			graph = command.loadCommands("0.1");
			graph.set_commandType(JSONType.SystemCommands);
			command.writeCommands(graph, placeHolder, applicationName, applicationVersion);
			
			graph = command.loadQueries("0.1");
			graph.set_commandType(JSONType.SystemCommands);
			command.writeCommands(graph, placeHolder, applicationName, applicationVersion);
			
			graph = command.addRootDomainAdminUser("AddRootDomainAdminUser", "0.1");
			graph.set_commandType(JSONType.SystemCommands);
			command.writeCommands(graph, placeHolder, applicationName, applicationVersion);
			
			graph = command.addNewDomain("AddNewDomain", "0.1");
			graph.set_commandType(JSONType.SystemCommands);
			command.writeCommands(graph, placeHolder, applicationName, applicationVersion);
			
			graph = command.addNewUser("AddNewUser", "0.1");
			graph.set_commandType(JSONType.SystemCommands);
			command.writeCommands(graph, placeHolder, applicationName, applicationVersion);
			_commands.put("AddNewUser", graph.getGraph());
			
			graph = command.addComment("AddComment", "0.1");
			graph.set_commandType(JSONType.SystemCommands);
			command.writeCommands(graph, placeHolder, applicationName, applicationVersion);
			_commands.put("AddComment", graph.getGraph());
			
			graph = command.bindFromUserToDomain("BindFromUserToDomain", "0.1");
			graph.set_commandType(JSONType.SystemCommands);
			command.writeCommands(graph, placeHolder, applicationName, applicationVersion);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void init() throws Exception{
    	RATUtils.initProperties(Constants.ConfFolder + Constants.PathSeparator + Constants.RATGraphGeneratorPropertyFile);
    	_ratStorageManagerPath = AppProperties.getInstance().getStringProperty("ratstoragemanager.path");
    	RATUtils.initProperties(_ratStorageManagerPath + Constants.PathSeparator + Constants.ConfFolder + Constants.PathSeparator + RATConstants.PropertyFileName);
    	
    	String alchemyFolder = AppProperties.getInstance().getStringProperty("alchemy.folder");
    	String alchemyCommandsFolder = AppProperties.getInstance().getStringProperty("alchemy.commands.folder");
    	String alchemyQueriesFolder = AppProperties.getInstance().getStringProperty("alchemy.queries.folder");
    	
    	String _alchemyFolder = Constants.ConfFolder + Constants.PathSeparator + alchemyFolder;
    	String _alchemyCommandsFolder = _alchemyFolder + Constants.PathSeparator + alchemyCommandsFolder;
    	String _alchemyQueriesFolder = _alchemyFolder + Constants.PathSeparator + alchemyQueriesFolder;
    	
		if(!FileUtils.fileExists(Constants.ConfFolder + Constants.PathSeparator + Constants.IndexFolder)){
			FileUtils.createDir(Constants.ConfFolder + Constants.PathSeparator + Constants.IndexFolder);
		}
		
		if(!FileUtils.fileExists(_alchemyCommandsFolder)){
			FileUtils.createDir(_alchemyCommandsFolder);
		}
		
		if(!FileUtils.fileExists(_alchemyQueriesFolder)){
			FileUtils.createDir(_alchemyQueriesFolder);
		}
		
		String loadCommandsUUID = AppProperties.getInstance().getStringProperty("commands.template.uuid");
		String loadQueriesUUID = AppProperties.getInstance().getStringProperty("queries.template.uuid");
		String rootDomainUUID = AppProperties.getInstance().getStringProperty("root.domain.uuid");
		
		GraphGeneratorHelpers.store(RATConstants.Commands, loadCommandsUUID);
		GraphGeneratorHelpers.store(RATConstants.Queries, loadQueriesUUID);
		GraphGeneratorHelpers.store(VertexType.RootDomain.toString(), rootDomainUUID);
		
		_alchemyManager = new AlchemyManager(_alchemyFolder, _alchemyCommandsFolder, _alchemyQueriesFolder);
    }
}