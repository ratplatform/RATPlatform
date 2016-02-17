package com.dgr.rat.commons.utils;

import java.nio.file.FileSystems;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;

public class RATUtils {
//	public static String readQueryJSONFile(String fileName) throws Exception{
//		String commandsPath = RATUtils.getCommandsPath(RATConstants.QueriesFolder);
//		StringBuffer pathBuffer = new StringBuffer();
//		pathBuffer.append(commandsPath);
//		pathBuffer.append(fileName);
//		
//		String templatePath = pathBuffer.toString();
//		String input = FileUtils.fileRead(templatePath);
//		
//		return input;
//	}
	
	public static String getCommandsPath(String folderName){
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(RATConstants.ConfigurationFolder);
		pathBuffer.append(FileSystems.getDefault().getSeparator());
		String systemCommandsFolder = AppProperties.getInstance().getStringProperty(folderName);
		pathBuffer.append(systemCommandsFolder);
		pathBuffer.append(FileSystems.getDefault().getSeparator());
		
		return pathBuffer.toString();
	}
	
	public static String readCommandJSONFile(String fileName, String folderName) throws Exception{
//		String commandsPath = RATUtils.getCommandsPath(RATConstants.CommandsFolder);
		String commandsPath = RATUtils.getCommandsPath(folderName);
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(commandsPath);
		pathBuffer.append(fileName);
		
		String templatePath = pathBuffer.toString();
		String input = FileUtils.fileRead(templatePath);
		
		return input;
	}
}
