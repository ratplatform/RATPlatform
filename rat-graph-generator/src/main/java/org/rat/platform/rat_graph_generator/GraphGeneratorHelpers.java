/**
 * @author Daniele Grignani (dgr)
 * @date Sep 16, 2015
 */

package org.rat.platform.rat_graph_generator;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.tools.ant.util.DateUtils;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.mqmessages.JsonHeader;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;

public class GraphGeneratorHelpers {
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
	
	public static String buildRemoteCommand(final JsonHeader header, final String remoteRequestJson) throws Exception{
		Map<String, String> headerMap = header.getHeaderProperties();
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode headerObjectNode = mapper.valueToTree(headerMap);
		
		JsonNode remoteRequestJsonObj = mapper.readTree(remoteRequestJson);
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put(RATConstants.Header, headerObjectNode);
		map.put(RATConstants.Settings, remoteRequestJsonObj);
		
		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
		
		return result;
	}
	
//	public static void writeAlchemyJson(String commandName, String commandVersion, String alchemyJSON, String alchemyFolder, String destinationFolder) throws Exception{
//		String pageTitlePlaceholder = AppProperties.getInstance().getStringProperty("page.title.placeholder");
//		
//		String resultPlaceholder = AppProperties.getInstance().getStringProperty("json.result.placeholder");
//		String pageTemplate = AppProperties.getInstance().getStringProperty("page.template");
//		
//		String text = FileUtils.fileRead(alchemyFolder + Constants.PathSeparator + pageTemplate);
//		
//		String jsonPath = destinationFolder + Constants.PathSeparator + commandName + ".json";
//		String html = text.replace(resultPlaceholder, commandName + ".json");
//		String date = DateUtils.getDateForHeader();
//		html = html.replace("@datePlaceholder@", date);
//		html = html.replace(pageTitlePlaceholder, commandName);
//		FileUtils.write(destinationFolder + Constants.PathSeparator + commandName + ".html", html, false);
//	    
//		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(alchemyJSON), jsonPath);
//	}
	
	public static void writeJavaScript(String name, String javaScript) throws Exception{
		String destinationFolder = AppProperties.getInstance().getStringProperty("javascript.destination.folder");
		FileUtils.write(destinationFolder + Constants.PathSeparator + name + ".js", javaScript, false);
	}
	
	public static void store(String key, String value){
		GraphGeneratorUUIDIndex index = new GraphGeneratorUUIDIndex();
		try {
			index.initSearch(Constants.ConfFolder + Constants.PathSeparator + Constants.IndexFolder);
			String result = index.search(key);
			index.closeSearcher();
			
			if(result == null){
				//String strUUID = uuid.toString();
				index.initWriter(Constants.ConfFolder + Constants.PathSeparator + Constants.IndexFolder);
				//index.addText(key, strUUID);
				index.addText(key, value);
				index.closeWriter();
			}
		} 
		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	// TODO: da rivedere result = UUID.randomUUID().toString(); in exception, non mi piace
	public static String getUUID(String query){
		//System.out.println("UUID " + query);
		GraphGeneratorUUIDIndex index = new GraphGeneratorUUIDIndex();
		String result = null;
		try {
			index.initSearch(Constants.ConfFolder + Constants.PathSeparator + Constants.IndexFolder);
			result = index.search(query);
			index.closeSearcher();
			
			if(result == null){
				UUID uuid = UUID.randomUUID();
				GraphGeneratorHelpers.store(query, uuid.toString());
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
	
	public static String buildCommandTemplate(final JsonHeader header, Graph commandTemplateGraph) throws IOException{
	    OutputStream output = new OutputStream(){
	        private StringBuilder string = new StringBuilder();
	        
	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }
	        public String toString(){
	            return this.string.toString();
	        }
	    };
	    
		GraphSONWriter.outputGraph(commandTemplateGraph, output);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(output.toString());
		
		Map<String, String> headerMap = header.getHeaderProperties();
		ObjectNode headerObjectNode = mapper.valueToTree(headerMap);
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put(RATConstants.Header, headerObjectNode);
		map.put(RATConstants.Settings, actualObj);
		
		ObjectNode ratJsonObject = mapper.valueToTree(map);
		String result = ratJsonObject.toString();
	    
	    return result;
	}
	
	public static String fromGraphToJson(Graph graph) throws IOException{
	    OutputStream output = new OutputStream(){
	        private StringBuilder string = new StringBuilder();
	        
	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }
	        public String toString(){
	            return this.string.toString();
	        }
	    };
	    
		GraphSONWriter.outputGraph(graph, output);
		String json = output.toString();
		
		return json;
	}
}
