/**
 * @author Daniele Grignani (dgr)
 * @date Aug 27, 2015
 */

package com.dgr.rat.tests.old;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.MakeSigmaJSON;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.main.SystemCommandsInitializer;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.storage.provider.TinkerGraphStorage;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;

public class InitCommandsTest {

	@Test
	public void test() {
		try {
			RATHelpers.initProperties(RATConstants.PropertyFile);
			
			String path = RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "start-systemcommands.xml";
			FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(path);
			SystemCommandsInitializer systemCommandsInitializer = (SystemCommandsInitializer)context.getBean("InitSystemCommands");
			
			systemCommandsInitializer.initStorage();
			systemCommandsInitializer.loadCommandTemplates();
			
//			// 2) Eseguo tutti i comandi SystemCommands
			systemCommandsInitializer.runSystemCommands();
//			// 3) Salvo i template degli UserCommands
//			systemCommandsInitializer.readUserCommandsCommandTemplates();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Before
	public void init(){
		try {
//			RATHelpers.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "unittest.properties");

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void verify(){
		try {
			this.verifyNumOfEdges();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	
	private String getCommandsPath(String folderName){
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(RATConstants.ConfigurationFolder);
		pathBuffer.append(FileSystems.getDefault().getSeparator());
		String systemCommandsFolder = AppProperties.getInstance().getStringProperty(folderName);
		pathBuffer.append(systemCommandsFolder);
		pathBuffer.append(FileSystems.getDefault().getSeparator());
		
		return pathBuffer.toString();
	}
	
	public void verifyNumOfEdges() throws Exception{
		String commandTemplatesPath = this.getCommandsPath(RATConstants.CommandTemplatesFolder);
		
		File[] templates = FileUtils.listingFiles(false, commandTemplatesPath, ".conf");
		// Il numero dei vertivi di questo comando deve essere pari al numero di file
		int numVertices = templates.length;
		
		TinkerGraphStorage storage = (TinkerGraphStorage) StorageBridge.getInstance().getStorage();
		// ATTENZIONE: il graph ottenuto in questo modo vale solo per il TinkerGraphStorage!
		Graph graph = storage.getGraph();
		Iterator<Edge> it = graph.getEdges().iterator();
		ArrayList<Edge>edgesFromDB = new ArrayList<Edge>();
		while (it.hasNext()){
			edgesFromDB.add(it.next());
		}
		System.out.println("edges: " + numVertices);
		System.out.println("edgesFromDB: " + edgesFromDB.size());
		Assert.assertEquals(numVertices, edgesFromDB.size());
		
		if(numVertices != edgesFromDB.size()){
			throw new Exception();
		}
	}
}
