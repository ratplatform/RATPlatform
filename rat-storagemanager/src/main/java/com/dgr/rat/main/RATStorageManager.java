/*
 * @author Daniele Grignani
 * Mar 29, 2015
*/

package com.dgr.rat.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.utils.RATUtils;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.messages.RATMessagingService;
import com.dgr.utils.AppProperties;

public class RATStorageManager {
	private RATMessagingService _messagingServer = null;
	
	public RATStorageManager() throws Exception {
		this.init();
	}
	
	private void init() throws Exception{
		RATUtils.initProperties(RATConstants.PropertyFile);
		// TODO questo deve essere letto solo se imposto il DB OrientDB
		RATUtils.initProperties(RATConstants.OrientDBPropertyFile);
		
		String path = RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "spring-consumer.xml";
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(path);
		_messagingServer = (RATMessagingService)context.getBean("RATMessagingService");

		SystemCommandsInitializer systemCommandsInitializer = new SystemCommandsInitializer();
		String storageType = AppProperties.getInstance().getStringProperty(RATConstants.StorageType);
		systemCommandsInitializer.set_storageType(storageType);
		// COMMENT: Inizializzo il database (se non esiste il DB allora lo creo)
		systemCommandsInitializer.initStorage();
		systemCommandsInitializer.addCommandTemplates();
		
		System.out.println("Ready!");
	}
	
	public void shutdownServer() throws Exception{
		System.out.println("RATStorageManager: shutdown storage manager");
		
		_messagingServer.close();
	}

	public static void main(String args[])throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		
		System.out.println("Enter 'stop' to quit.");
		
		try {
			RATStorageManager main = new RATStorageManager();
			
			do {
				str = br.readLine();
				//System.out.println(str);
				// TODO: naturalmente shutdownServer non può essere eseguito fino a che DumpGraph non smette di "girare"
				if(str.equals("dump")){
					DumpGraph.getInstance().execute();
				}
			} while(!str.equals("stop"));
			
			main.shutdownServer();
			System.exit(0);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

}
