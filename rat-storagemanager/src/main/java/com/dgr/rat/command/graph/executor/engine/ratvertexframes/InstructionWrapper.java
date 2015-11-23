/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.QueryPivotNode;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.rat.json.utils.ReturnType;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;

public class InstructionWrapper {
	private class Param{
		public String paramName = null;
		public String paramValue = null;
		public ReturnType returnType = ReturnType.unknown;
		public int order = 0;
	}
	
	private int _num = 0;
	private IInstructionNodeFrame _instructionNode = null;
	private Map<String, Param>_parameters = new LinkedHashMap<String, Param>();
	private String _instructionName = null;
	private UUID _instructionUUID = null;
	private int _maxNumParameter = 1;
	private VertexType _vertexType2Bind = null;
//	private Map<String, LinkedHashMap<String, QueryPivotNode>>_queryPivotNodeParams = new LinkedHashMap<String, LinkedHashMap<String, QueryPivotNode>>();
//	private Map<String, LinkedList<QueryPivotNode>>_queryPivotNodes = new LinkedHashMap<String, LinkedList<QueryPivotNode>>();
	
	private String _lastParamName = null;
	
	public InstructionWrapper(String instructionName, int num) {
		_instructionName = instructionName;
		_num = num;
	}
	
	public void setMaxNumParameter(int maxNum){
		_maxNumParameter = maxNum;
	}
	
	public Integer getMaxNumParameter(){
		return _maxNumParameter;
	}
	
	public Integer getOrder(){
		return _num;
	}
	
	public String getInstructionName(){
		return _instructionName;
	}
	
//	public void addQueryPivot(String key, String query, boolean isStartPivot) throws Exception{
//		LinkedList<QueryPivotNode>list = null;
//		if(_queryPivotNodes.containsKey(key)){
//			list = _queryPivotNodes.get(key);
//		}
//		else{
//			list = new LinkedList<QueryPivotNode>();
//			_queryPivotNodes.put(key, list);
//		}
//		
//		QueryPivotNode node = new QueryPivotNode(isStartPivot);
//		node.set_nodeUUID(UUID.randomUUID());
//		node.setQueryName(query);
//		node.set_orderField(list.size());
//		node.set_nodeContent(query);
//		node.set_correlationKey(key);
//		
//		list.add(node);
//		
//		this.addQueryParamPivot(key, query);
//	}
	
//	private void addQueryParamPivot(String key, String query) throws Exception{
//		// COMMENT: prendo l'ultimo paramName inserito
//		if(_lastParamName == null){
//			throw new Exception();
//		}
//		
//		LinkedHashMap<String, QueryPivotNode>map = null;
//		if(_queryPivotNodeParams.containsKey(key)){
//			map = _queryPivotNodeParams.get(key);
//		}
//		else{
//			map = new LinkedHashMap<String, QueryPivotNode>();
//			_queryPivotNodeParams.put(key, map);
//		}
//		
//		QueryPivotNode node = new QueryPivotNode(false);
//		node.set_nodeUUID(UUID.randomUUID());
//		node.setQueryName(query);
//		node.set_orderField(map.size());
//		node.set_nodeContent(query);
//		node.set_correlationKey(key);
//		
//		map.put(_lastParamName, node);
//	}
//	
//	private boolean containsParam(String paramName){
//		boolean result = false;
//		Iterator<String>it = _queryPivotNodeParams.keySet().iterator();
//		while(it.hasNext()){
//			String key = it.next();
//			LinkedHashMap<String, QueryPivotNode> map = _queryPivotNodeParams.get(key);
//			Iterator<String>mapIt = map.keySet().iterator();
//			while(mapIt.hasNext()){
//				String name = mapIt.next();
//				if(paramName.equals(name)){
//					result = true;
//					break;
//				}
//			}
//			
//			if(result){
//				break;
//			}
//		}
//		
//		return result;
//	}
//	
//	private void buildParamQueryPivots(FramedGraph<Graph> framedGraph, Vertex instructionParameter, UUID commandUUID) throws Exception{
//		Iterator<String>it = _queryPivotNodeParams.keySet().iterator();
//		while(it.hasNext()){
//			String key = it.next();
//			LinkedHashMap<String, QueryPivotNode> map = _queryPivotNodeParams.get(key);
//			Iterator<String>mapIt = map.keySet().iterator();
//			while(mapIt.hasNext()){
//				String param = mapIt.next();
//				QueryPivotNode node = map.get(param);
//				node.buildNodes(framedGraph, null, commandUUID, RATConstants.QueryPivotEdgeLabel);
//				IRATNodeQueryPivotFrame childNode = node.getNode();
//				instructionParameter.addEdge(RATConstants.QueryPivotEdgeLabel, childNode.asVertex());
//			}
//		}
//	}
//	
//	private void buildQueryPivots(FramedGraph<Graph> framedGraph, Vertex parent, UUID commandUUID) throws Exception{
//		Iterator<String>it = _queryPivotNodes.keySet().iterator();
//		while(it.hasNext()){
//			String key = it.next();
//			LinkedList<QueryPivotNode>list = _queryPivotNodes.get(key);
//			for(QueryPivotNode query : list){
//				query.buildNodes(framedGraph, null, commandUUID, RATConstants.QueryPivotEdgeLabel);
//				IRATNodeQueryPivotFrame childNode = query.getNode();
//				parent.addEdge(RATConstants.QueryPivotEdgeLabel, childNode.asVertex());
//			}
//		}
//	}
	
	public void addParam(final String paramName, final String paramValue, final ReturnType returnType) throws Exception{
		if(_parameters.containsKey(paramName)){
			throw new Exception();
		}
		
		_lastParamName = paramName;
		
		Param param = new Param();
		param.paramName = paramName;
		param.paramValue = paramValue;
		param.returnType = returnType;
		param.order = _parameters.size();
		_parameters.put(paramName, param);
	}
	
	private IInstructionParameterNodeFrame getParam(FramedGraph<Graph> framedGraph, final Param param){
		IInstructionParameterNodeFrame instructionParameter = framedGraph.addVertex(null, IInstructionParameterNodeFrame.class);
		instructionParameter.setVertexContentField(VertexType.InstructionParameter.toString());
		instructionParameter.setVertexLabelField(VertexType.InstructionParameter.toString());
		instructionParameter.setVertexRoleValueRootField(false);
		instructionParameter.setVertexTypeField(VertexType.InstructionParameter);
		instructionParameter.setVertexUserCommandsInstructionsParameterNameField(param.paramName);
		instructionParameter.setVertexUserCommandsInstructionsParameterReturnTypeField(param.returnType);
		instructionParameter.setVertexUserCommandsInstructionsParameterValueField(param.paramValue);
		
		instructionParameter.setVertexUUIDField(UUID.randomUUID().toString());
		instructionParameter.setInstructionOrderField(param.order);
		
		return instructionParameter;
	}
	
	public IInstructionNodeFrame getInstruction(){
		return _instructionNode;
	}
	
	public IInstructionNodeFrame buildInstructionGraph(FramedGraph<Graph> framedGraph) throws Exception{
		Graph graph = framedGraph.getBaseGraph();
		Vertex vertex = graph.addVertex(null);
        _instructionNode = framedGraph.frame(vertex, IInstructionNodeFrame.class);
        _instructionNode.setInstructionOrderField(_num);
        _instructionNode.setVertexContentField(_vertexType2Bind != null ? _vertexType2Bind.toString() : _instructionName);
        _instructionNode.setVertexLabelField(_instructionName);
        _instructionNode.setVertexRoleValueRootField(false);
        _instructionNode.setVertexTypeField(VertexType.Instruction);
        _instructionNode.setVertexUUIDField(_instructionUUID.toString());
        _instructionNode.setMaxNumParameters(_maxNumParameter);
        
//        this.buildQueryPivots(framedGraph, vertex, _instructionUUID);
        
        Iterator<String> it = _parameters.keySet().iterator();
        while(it.hasNext()){
        	String paramName = it.next();
        	Param param = _parameters.get(paramName);
        	IInstructionParameterNodeFrame instructionParameter = this.getParam(framedGraph, param);
        	
        	IInstructionParameterEdgeFrame instructionParameterInfo = _instructionNode.addUserCommandsInstructionsParameter(instructionParameter);
        	instructionParameterInfo.setWeight(param.order);
        	
//        	if(this.containsParam(paramName)){
//        		this.buildParamQueryPivots(framedGraph, instructionParameter.asVertex(), _instructionUUID);
//        	}
        }
        
        return _instructionNode;
	}

	/**
	 * @return the _instructionUUID
	 */
	public UUID get_instructionUUID() {
		return _instructionUUID;
	}

	/**
	 * @param _instructionUUID the _instructionUUID to set
	 */
	public void set_instructionUUID(UUID instructionUUID) {
		this._instructionUUID = instructionUUID;
	}

	/**
	 * @return the _vertexType2Bind
	 */
	public VertexType get_vertexType2Bind() {
		return _vertexType2Bind;
	}

	/**
	 * @param _vertexType2Bind the _vertexType2Bind to set
	 */
	public void set_vertexType2Bind(VertexType vertexType2Bind) {
		this._vertexType2Bind = vertexType2Bind;
	}
}
