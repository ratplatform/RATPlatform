package org.rat.platform.rat_graph_generator;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.util.DateUtils;
import com.dgr.rat.json.utils.MakeSigmaJSON;
import com.dgr.rat.json.utils.RATJsonUtils;
import com.dgr.utils.AppProperties;
import com.dgr.utils.FileUtils;

public class AlchemyManager {
	private class CommandTemplate{
		public String commandTemplate;
		public String commandName;
		public String commandVersion;
	}
	private String _alchemyFolder = null;
	private String _alchemyCommandsFolder = null;
	private String _alchemyQueriesFolder = null;
	private List<CommandTemplate>_commandTemplates = new ArrayList<CommandTemplate>();
	
	public AlchemyManager(String alchemyFolder, String alchemyCommandsFolder, String alchemyQueriesFolder) {
		_alchemyFolder = alchemyFolder;
		_alchemyCommandsFolder = alchemyCommandsFolder;
		_alchemyQueriesFolder = alchemyQueriesFolder;
	}
	
	public void addCommandTemplate(String commandName, String commandVersion, String commandTemplate){
		CommandTemplate template = new CommandTemplate();
		template.commandTemplate = commandTemplate;
		template.commandName = commandName;
		template.commandVersion = commandVersion;
		
		_commandTemplates.add(template);
	}
	
	public void dispose(){
		_commandTemplates.clear();
	}
	
	public void createCommands() throws Exception{
		String table = "<table>\n";
		for(CommandTemplate template : _commandTemplates){
			String alchemyJSON = MakeSigmaJSON.fromRatJsonToAlchemy(template.commandTemplate);
			this.writeAlchemyJson(template.commandName, template.commandVersion, alchemyJSON, _alchemyCommandsFolder);
			String date = DateUtils.getDateForHeader();
			table += "<tr><td><a target='_blank' href='" + template.commandName + ".html'>" + template.commandName + "</a></td><td>" + date + "</td></tr>";
		}
		table += "</table>\n";
		String html = FileUtils.fileRead(_alchemyFolder + Constants.PathSeparator + "index.html");
		html = html.replace("@table@", table);
		FileUtils.write(_alchemyCommandsFolder + Constants.PathSeparator + "index.html", html, false);
		
		this.dispose();
	}
	
	public void createQueries() throws Exception{
		String table = "<table>\n";
		for(CommandTemplate template : _commandTemplates){
			String alchemyJSON = MakeSigmaJSON.fromRatJsonToAlchemy(template.commandTemplate);
			this.writeAlchemyJson(template.commandName, template.commandVersion, alchemyJSON, _alchemyQueriesFolder);
			String date = DateUtils.getDateForHeader();
			table += "<tr><td><a target='_blank' href='" + template.commandName + ".html'>" + template.commandName + "</a></td><td>" + date + "</td></tr>";
		}
		table += "</table>\n";
		String html = FileUtils.fileRead(_alchemyFolder + Constants.PathSeparator + "index.html");
		html = html.replace("@table@", table);
		FileUtils.write(_alchemyQueriesFolder + Constants.PathSeparator + "index.html", html, false);
		
		this.dispose();
	}
	
	private void writeAlchemyJson(String commandName, String commandVersion, String alchemyJSON, String destination) throws Exception{
		String pageTitlePlaceholder = AppProperties.getInstance().getStringProperty("page.title.placeholder");
		
		String resultPlaceholder = AppProperties.getInstance().getStringProperty("json.result.placeholder");
		String pageTemplate = AppProperties.getInstance().getStringProperty("page.template");
		
		String text = FileUtils.fileRead(_alchemyFolder + Constants.PathSeparator + pageTemplate);
		
		String jsonPath = destination + Constants.PathSeparator + commandName + ".json";
		String html = text.replace(resultPlaceholder, commandName + ".json");
		String date = DateUtils.getDateForHeader();
		html = html.replace("@datePlaceholder@", date);
		html = html.replace(pageTitlePlaceholder, commandName);
		FileUtils.write(destination + Constants.PathSeparator + commandName + ".html", html, false);
	    
		GraphGeneratorHelpers.writeText(RATJsonUtils.jsonPrettyPrinter(alchemyJSON), jsonPath);
	}
}
