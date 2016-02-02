package com.dgr.rat.main;

import java.nio.file.FileSystems;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.tools.ant.util.DateUtils;

import com.dgr.rat.json.utils.MakeSigmaJSON;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.rat.storage.orientdb.StorageInternalError;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.rat.tests.TestHelpers;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;
import com.tinkerpop.blueprints.Graph;

public class DumpGraph {
	private class Task implements Runnable{
		@Override
		public void run() {
			System.out.println("DumpGraph");
			try {
				String graphLibraryFolder = AppProperties.getInstance().getStringProperty("javascript.graph.library.path");
				String outputFolder = AppProperties.getInstance().getStringProperty("javascript.dump.data.folder");
				String pageTitlePlaceholder = AppProperties.getInstance().getStringProperty("html.template.page.title.placeholder");
				String sep = FileSystems.getDefault().getSeparator();
				String resultPlaceholder = AppProperties.getInstance().getStringProperty("json.result.placeholder");
				String pageTemplate = AppProperties.getInstance().getStringProperty("html.template.page.name");
				
				String htmlText = FileUtils.fileRead("conf" + sep + graphLibraryFolder + sep + pageTemplate);
				String destinationFolder = outputFolder;
				String resultFilename = String.valueOf(System.currentTimeMillis()) + "-graph";
				//System.out.println(text);
				if(!FileUtils.fileExists(destinationFolder)){
					FileUtils.createDir(destinationFolder);
				}

				htmlText = htmlText.replace(resultPlaceholder, resultFilename + sep + resultFilename + ".json");
				String date = DateUtils.getDateForHeader();
				htmlText = htmlText.replace("@datePlaceholder@", date);
				htmlText = htmlText.replace(pageTitlePlaceholder, resultFilename + ".json");
				FileUtils.write(destinationFolder + sep + resultFilename + ".html", htmlText, false);
				FileUtils.createDir(destinationFolder + sep + resultFilename);
				
				StorageBridge.getInstance().getStorage().openConnection();
				Graph graph = StorageBridge.getInstance().getStorage().getGraph();
				String json = RATJsonUtils.serializeGraph(graph);
				String result = MakeSigmaJSON.fromRatJsonToAlchemy2(json);
				TestHelpers.writeGraphToJson(result, destinationFolder + sep + resultFilename + sep + resultFilename + ".json");
				StorageBridge.getInstance().getStorage().shutDown();
				
			} catch (StorageInternalError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("End DumpGraph");
		}
	}
	
	private static DumpGraph _instance = null;
	private final ExecutorService _executorService = Executors.newSingleThreadExecutor();
	
	public static DumpGraph getInstance(){
		if(_instance == null){
			_instance = new DumpGraph();
		}
		
		return _instance;
	}
	
	public void execute(){
		_executorService.submit(new Task());
	}
}
