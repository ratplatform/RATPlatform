/**
 * @author Daniele Grignani (dgr)
 * @date Oct 25, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result.queries;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;
import com.dgr.rat.command.graph.executor.engine.result.IResultDriller;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.instructions.InstructionResultInfo;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Graph;

public class QueryResultDriller implements IResultDriller{
	private Map<String, Object> _commandResponsePropertiesMap = new HashMap<String, Object>();
	public QueryResultDriller() {
		// TODO Auto-generated constructor stub
	}
	
	public Map<String, Object> getResult(){
		return _commandResponsePropertiesMap;
	}
	
	public Object getProperty(String key){
		return _commandResponsePropertiesMap.get(key);
	}
	
	public Set<String>getPropertyKeys(){
		return _commandResponsePropertiesMap.keySet();
	}
	
	public Iterator<String>getKeyIterator(){
		return this.getPropertyKeys().iterator();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.result.IResultDriller#drill(com.dgr.rat.command.graph.executor.engine.result.CommandResult)
	 */
	@Override
	public void drill(CommandResponse commandResult) throws Exception {
		InstructionResultInfo info = commandResult.getInstructionResultInfo();
		if(info == null){
			throw new Exception();
			// TODO log ?
		}
		IInstructionResult result = commandResult.getInstructionResult();
		if(result == null){
			throw new Exception();
			// TODO log ?
		}
				
		QueryResult queryResult = result.getContent(QueryResult.class);
		Graph graph = queryResult.getGraph();
		String json = RATHelpers.fromGraphToJson(graph);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		
		// TODO: non sono riuscito a pensare di meglio: da rivedere: rootUUID mi serve nelle risposte delle query 
		// qual'è il nodo dal quale è partita la query (es user e tutti i domini dello user: rootUUID corrisponde a quella del nodo user)
		commandResult.setRootUUID(queryResult.getRootUUID());
//		System.out.println(RATJsonUtils.jsonPrettyPrinter(json));
		_commandResponsePropertiesMap.put(RATConstants.Settings, jsonNode);
	}
}
