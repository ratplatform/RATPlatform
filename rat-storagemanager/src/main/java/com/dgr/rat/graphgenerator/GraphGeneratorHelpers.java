/**
 * @author Daniele Grignani (dgr)
 * @date Sep 16, 2015
 */

package com.dgr.rat.graphgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.lucene.queryparser.classic.ParseException;

import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;

public class GraphGeneratorHelpers {
	public static final String IndexFolder = "conf/UUIDIndex";
	public static final String UnitTestPropertyFile = "conf/unittest.properties";
	public static final String PathSeparator = FileSystems.getDefault().getSeparator();
	public static final String CommandsFolder = "conf/Commands";
	public static final String QueriesFolder = "conf/Queries";
	public static final String StorageManagerPropertyFile = "conf/storage-manager.properties";
	public static final String QueryTemplatesFolder = "conf/QueryTemplates";
	public static final String CommandTemplatesFolder = "conf/CommandTemplates";
	
	public static void writeGraphToJson(String json, String path) throws IOException{
        BufferedWriter writer = null;
        
        try {
            File file = new File(path);

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(RATJsonUtils.jsonPrettyPrinter(json));
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
		RATHelpers.initProperties(UnitTestPropertyFile);
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
		html = html.replace(pageTitlePlaceholder, commandName);
		FileUtils.write(".." + sep + appPath + sep + destinationFolder + sep + commandName + ".html", html, false);
	    
		GraphGeneratorHelpers.writeGraphToJson(alchemyJSON, jsonPath);
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
}
