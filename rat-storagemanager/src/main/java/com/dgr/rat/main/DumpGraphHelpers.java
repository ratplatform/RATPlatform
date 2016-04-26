package com.dgr.rat.main;

import java.io.IOException;
import java.nio.file.FileSystems;
import org.apache.tools.ant.util.DateUtils;

import com.dgr.rat.json.utils.MakeAlchemyJSON;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.tests.TestHelpers;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.tinkerpop.blueprints.Graph;

// TRASH
public class DumpGraphHelpers {
	public static String writeGraphToHTML(String resultFilename, String destinationFolder) throws Exception{
		String appPath = AppProperties.getInstance().getStringProperty("sigma.path");
		String pageTitlePlaceholder = AppProperties.getInstance().getStringProperty("page.title.placeholder");
		String sep = FileSystems.getDefault().getSeparator();
				
		
		String resultPlaceholder = AppProperties.getInstance().getStringProperty("json.result.placeholder");
		String pageTemplate = AppProperties.getInstance().getStringProperty("page.template");

		String text = FileUtils.fileRead(".." + sep + appPath + sep + pageTemplate);
		
		String path = ".." + sep + appPath + sep + destinationFolder + sep + resultFilename + ".json";
		String html = text.replace(resultPlaceholder, resultFilename + ".json");
		String date = DateUtils.getDateForHeader();
		html = html.replace("@datePlaceholder@", date);
		html = html.replace(pageTitlePlaceholder, resultFilename);
		
		FileUtils.write(".." + sep + appPath + sep + destinationFolder + sep + resultFilename + ".html", html, false);
		
		return path;
//		TestHelpers.dumpJson(path);
	}
	
	public static void dumpJson(String path) throws Exception{
//		TinkerGraphStorage storage = (TinkerGraphStorage) StorageBridge.getInstance().getStorage();
//		
//		// ATTENZIONE: il graph ottenuto in questo modo vale solo per il TinkerGraphStorage!
//		Graph graph = storage.getGraph();
		
		StorageBridge.getInstance().getStorage().openConnection();
		Graph graph = StorageBridge.getInstance().getStorage().getGraph();
		String json = RATJsonUtils.serializeGraph(graph);
		String result = MakeAlchemyJSON.fromRatJsonToAlchemy2(json);
//		System.out.println(result);
		
		try {
			TestHelpers.writeGraphToJson(result, path);
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		StorageBridge.getInstance().getStorage().shutDown();
	}
}
