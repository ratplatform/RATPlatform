/**
 * @author Daniele Grignani (dgr)
 * @date Sep 16, 2015
 */

package com.dgr.rat.graphgenerator;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.tools.ant.util.DateUtils;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.utils.RATUtils;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;

public class GraphGeneratorHelpers {
	public static final String PathSeparator = FileSystems.getDefault().getSeparator();
	public static final String IndexFolder = "conf" + PathSeparator + "UUIDIndex";
	public static final String UnitTestPropertyFile = "conf" + PathSeparator + "unittest.properties";
	public static final String CommandsFolder = "conf" + PathSeparator + "Commands";
	public static final String QueriesFolder = "conf" + PathSeparator + "Queries";
	public static final String StorageManagerPropertyFile = "conf" + PathSeparator + RATConstants.PropertyFileName;
	public static final String QueryTemplatesFolder = "conf" + PathSeparator + "QueryTemplates";
	public static final String CommandTemplatesFolder = "conf" + PathSeparator + "CommandTemplates";
	
	public static void writeText(String json, String path) throws IOException{
        BufferedWriter writer = null;
        
        try {
            File file = new File(path);

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(json);
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
            try {
                writer.close();
            } 
            catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }
	
	public static void writeAlchemyJson(String commandName, String commandVersion, String alchemyJSON, String destinationFolder) throws Exception{
		RATUtils.initProperties(UnitTestPropertyFile);
		String sep = FileSystems.getDefault().getSeparator();
		
		String appPath = AppProperties.getInstance().getStringProperty("sigma.path");
		String dataFolder = AppProperties.getInstance().getStringProperty("sigma.data.folder");
		String pageTitlePlaceholder = AppProperties.getInstance().getStringProperty("page.title.placeholder");
		
		String resultPlaceholder = AppProperties.getInstance().getStringProperty("json.result.placeholder");
		String pageTemplate = AppProperties.getInstance().getStringProperty("page.template");

//		Path currentRelativePath = Paths.get("");
//		String s = currentRelativePath.toAbsolutePath().toString();
//		System.out.println("Current relative path is: " + s);
		
		String text = FileUtils.fileRead(".." + sep + appPath + sep + pageTemplate);
		
//		String jsonPath = ".." + sep + appPath + sep + destinationFolder + sep + commandName + ".json";
		String jsonPath = ".." + sep + appPath + sep + destinationFolder + sep + commandName + ".json";
		String html = text.replace(resultPlaceholder, commandName + ".json");
		String date = DateUtils.getDateForHeader();
		html = html.replace("@datePlaceholder@", date);
		html = html.replace(pageTitlePlaceholder, commandName);
		FileUtils.write(".." + sep + appPath + sep + destinationFolder + sep + commandName + ".html", html, false);
	    
		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(alchemyJSON), jsonPath);
	}
	
	public static void writeJavaScript(String name, String javaScript) throws Exception{
		RATUtils.initProperties(UnitTestPropertyFile);
		String sep = FileSystems.getDefault().getSeparator();
		String destinationFolder = AppProperties.getInstance().getStringProperty("javascript.destination.folder");
		FileUtils.write(".." + sep + destinationFolder + sep + name + ".js", javaScript, false);
	}
	
	public static void storeUUID(String query, UUID uuid){
		GraphGeneratorUUIDIndex index = new GraphGeneratorUUIDIndex();
		try {
			index.initSearch(IndexFolder);
			String result = index.search(query);
			index.closeSearcher();
			
			if(result == null){
				String strUUID = uuid.toString();
				index.initWriter(IndexFolder);
				index.addText(query, strUUID);
				index.closeWriter();
			}
		} 
		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	// TODO: da rivedere result = UUID.randomUUID().toString(); in exception, non mi piace
	public static String getUUID(String query){
		GraphGeneratorUUIDIndex index = new GraphGeneratorUUIDIndex();
		String result = null;
		try {
			index.initSearch(IndexFolder);
			result = index.search(query);
			index.closeSearcher();
			
			if(result == null){
				UUID uuid = UUID.randomUUID();
				GraphGeneratorHelpers.storeUUID(query, uuid);
				result = uuid.toString();
			}
		} catch (IOException | ParseException e) {
			result = UUID.randomUUID().toString();
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Graph getRATJsonSettingsGraph(String ratJson) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(ratJson, RATJsonObject.class);
		String output = mapper.writeValueAsString(jsonHeader.getSettings());
		JsonNode actualObj = mapper.readTree(output);
		Graph graph = new TinkerGraph();
		InputStream inputStream = new ByteArrayInputStream(actualObj.toString().getBytes());
		GraphSONReader.inputGraph(graph, inputStream);
		
		return graph;
	}
}
