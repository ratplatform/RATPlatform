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
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.messages.RATMessagingService;
import com.dgr.utils.AppProperties;

public class RATStorageManager {
	private RATMessagingService _messagingServer = null;
	
	public RATStorageManager() throws Exception {
		//RATStorageManager.checkDB("local:/home/dgr/dev/RATPlatform/RATStorageManager/data/orientdb/rat-orientdb", "admin", "admin");
		
		this.init();
	}
	
	private void init() throws Exception{
		RATHelpers.initProperties(RATConstants.PropertyFile);
		RATHelpers.initProperties(RATConstants.OrientDBPropertyFile);

//		RATHelpers.readProperties(RATConstants.OrientDBPropertyFile);
		
		String path = RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "spring-consumer.xml";
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(path);
		_messagingServer = (RATMessagingService)context.getBean("RATMessagingService");

		path = RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "start-systemcommands.xml";
		context = new FileSystemXmlApplicationContext(path);
		SystemCommandsInitializer systemCommandsInitializer = (SystemCommandsInitializer)context.getBean("InitSystemCommands");
		// 1) Inizializzo il database (se non esiste il DB allora lo creo)
		systemCommandsInitializer.initStorage();
		
		systemCommandsInitializer.loadCommandTemplates();
		// 2) Eseguo tutti i comandi SystemCommands
		systemCommandsInitializer.runSystemCommands();
//		// 3) Salvo i template degli UserCommands
		systemCommandsInitializer.readUserCommandsCommandTemplates();
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
				System.out.println(str);
			} while(!str.equals("stop"));
			
			main.shutdownServer();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

}
