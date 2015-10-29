/**
 * @author Daniele Grignani (dgr)
 * @date Aug 28, 2015
 */

package com.dgr.rat.tests.old;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;

import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.MakeSigmaJSON;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.main.RATStorageManager;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.storage.provider.TinkerGraphStorage;
import com.dgr.rat.tests.RATSessionManager;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Graph;

public class CreateNewDomainTest {
	@Test
	public void test() {
		try {
//			RATStorageManager main = new RATStorageManager();
			
			
			Thread thread = new Thread(){
				@Override
				public void run(){
					try {
						RATStorageManager.main(new String[]{""});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			thread.start();
			
			String commandFile = RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + AppProperties.getInstance().getStringProperty("systemcommands.folder");
			commandFile += FileSystems.getDefault().getSeparator() + "CreateNewDomain.conf";
			String json = FileUtils.fileRead(commandFile);
			json = json.replace( AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder), "Dominio di Test");
			json = RATJsonUtils.jsonPrettyPrinter(json);
			FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "spring-producer-unitTest.xml");
			RATSessionManager.init();
			System.out.println(RATSessionManager.getInstance().sendMessage(context, json));
			
			thread.join();
			//main.shutdownServer();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Before
	public void init(){
		try {
			
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "storage-manager.properties");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void verify(){
//		try {
//			this.verifyEdges();
//			this.verifyVertices();
//			
//		} 
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// Anche in caso di exception voglio che scriva lo stesso i risultati per vederli
		try {
			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "unittest.properties");
			
			String appPath = AppProperties.getInstance().getStringProperty("sigma.path");
			String dataFolder = AppProperties.getInstance().getStringProperty("sigma.data.folder");
			String resultFilename = this.getClass().getSimpleName() + "Result";
			String pageTitlePlaceholder = AppProperties.getInstance().getStringProperty("page.title.placeholder");
			
			String resultPlaceholder = AppProperties.getInstance().getStringProperty("json.result.placeholder");
			String pageTemplate = AppProperties.getInstance().getStringProperty("page.template");

			String text = FileUtils.fileRead(appPath + FileSystems.getDefault().getSeparator() + pageTemplate);
			
			String path = appPath + FileSystems.getDefault().getSeparator() + dataFolder + FileSystems.getDefault().getSeparator() + resultFilename + ".json";
			String html = text.replace(resultPlaceholder, path);
			html = html.replace(pageTitlePlaceholder, resultFilename);
			
			FileUtils.write(appPath + FileSystems.getDefault().getSeparator() + resultFilename + ".html", html, false);
			
			this.dumpJson(path);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dumpJson(String path) throws Exception{
		TinkerGraphStorage storage = (TinkerGraphStorage) StorageBridge.getInstance().getStorage();
		// ATTENZIONE: il graph ottenuto in questo modo vale solo per il TinkerGraphStorage!
		Graph graph = storage.getGraph();
		
		String result = MakeSigmaJSON.fromRatJsonToAlchemy(graph);
//		System.out.println(result);
		
		try {
			this.writeGraphToJson(result, path);
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void writeGraphToJson(String json, String path) throws IOException{
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
            }
        }
    }

}
