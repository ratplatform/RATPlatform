/**
 * @author Daniele Grignani (dgr)
 * @date Sep 23, 2015
 */

package com.dgr.rat.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.json.utils.MakeSigmaJSON;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.main.SystemCommandsInitializer;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.storage.provider.TinkerGraphStorage;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.tinkerpop.blueprints.Graph;

public class TestHelpers {
	
	public static final <T> Object invokeMethod(final String name, final T obj, final Class<?>[] types, final Object... args) throws Exception {
		Method method = obj.getClass().getDeclaredMethod(name, types);
		method.setAccessible(true);
		return method.invoke(obj, args);
	}
	
	public static final <T> Object getPrivateDataMember(final Class<T> cls, final T obj, final String dataMemberName) throws Exception {
		Field field = cls.getDeclaredField(dataMemberName);
		field.setAccessible(true);
		return field.get(obj);
	}
	
	public static SystemCommandsInitializer getSystemCommandsInitializer() {
		SystemCommandsInitializer systemCommandsInitializer = null;
		try {
			RATHelpers.initProperties(RATConstants.PropertyFile);
			RATHelpers.initProperties(RATConstants.OrientDBPropertyFile);
			
			String path = RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "spring-consumer.xml";
			FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(path);

			path = RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "start-systemcommands.xml";
			context = new FileSystemXmlApplicationContext(path);
			systemCommandsInitializer = (SystemCommandsInitializer)context.getBean("InitSystemCommands");
			systemCommandsInitializer.initStorage();
		} 
		catch (Exception e) {
			systemCommandsInitializer = null;
			e.printStackTrace();
		}
		
		return systemCommandsInitializer;
	}
	
	
	
	public static String writeGraphToHTML(String resultFilename, String destinationFolder) throws Exception{
		String appPath = AppProperties.getInstance().getStringProperty("sigma.path");
		//String dataFolder = AppProperties.getInstance().getStringProperty("sigma.data.folder");
		String pageTitlePlaceholder = AppProperties.getInstance().getStringProperty("page.title.placeholder");
		String sep = FileSystems.getDefault().getSeparator();
				
		
		String resultPlaceholder = AppProperties.getInstance().getStringProperty("json.result.placeholder");
		String pageTemplate = AppProperties.getInstance().getStringProperty("page.template");

		String text = FileUtils.fileRead(".." + sep + appPath + sep + pageTemplate);
		
		String path = ".." + sep + appPath + sep + destinationFolder + sep + resultFilename + ".json";
		String html = text.replace(resultPlaceholder, resultFilename + ".json");
		html = html.replace(pageTitlePlaceholder, resultFilename);
		
		FileUtils.write(".." + sep + appPath + sep + destinationFolder + sep + resultFilename + ".html", html, false);
		
		return path;
//		TestHelpers.dumpJson(path);
	}
	
	public static void dumpJson(String path) throws Exception{
		TinkerGraphStorage storage = (TinkerGraphStorage) StorageBridge.getInstance().getStorage();
		// ATTENZIONE: il graph ottenuto in questo modo vale solo per il TinkerGraphStorage!
		Graph graph = storage.getGraph();
		
		String result = MakeSigmaJSON.fromRatJsonToAlchemy(graph);
//		System.out.println(result);
		
		try {
			TestHelpers.writeGraphToJson(result, path);
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
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
            }
        }
    }
}
