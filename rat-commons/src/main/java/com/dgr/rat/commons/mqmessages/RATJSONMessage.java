package com.dgr.rat.commons.mqmessages;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.utils.VertexType;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class RATJSONMessage /*implements IResponse*/{
	private JsonHeader _header = null;
	private TinkerGraph _graph = null;
	
//	private Map<String, Object> _commandResponseMap = null;
	
	private RATJSONMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public static RATJSONMessage deserialize(String ratJson) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		RATJsonObject ratJsonObject = (RATJsonObject) mapper.readValue(ratJson, RATJsonObject.class);
		
		String output = mapper.writeValueAsString(ratJsonObject.getSettings());
		JsonNode actualObj = mapper.readTree(output);
		RATJSONMessage ratJSONMessage = new RATJSONMessage();
		TinkerGraph graph = new TinkerGraph();
		InputStream inputStream = new ByteArrayInputStream(actualObj.toString().getBytes());
		GraphSONReader.inputGraph(graph, inputStream);
		ratJSONMessage.setGraph(graph);
		
		TypeFactory typeFactory = mapper.getTypeFactory();
		MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, String.class);
		HashMap<String, String> map = mapper.readValue(ratJsonObject.getHeader(), mapType);
		JsonHeader header = new JsonHeader();
		header.setHeaderProperties(map);
		ratJSONMessage.setHeader(header);
		
		return ratJSONMessage;
	}
	
	public List<Vertex> getNode(VertexType vertexType, String property, Object value) throws Exception{
		Vertex rootVertex = this.getRootVertex();
		if(rootVertex == null){
			throw new Exception();
		}
		
		List<Vertex> result = this.traverse(rootVertex, vertexType, property, value);
		return result;
	}
	
	private List<Vertex> traverse(Vertex root, VertexType vertexType, String property, Object value) throws Exception{
		Stack<Vertex>stack = new Stack<Vertex>();
		List<Vertex> visited = new LinkedList<Vertex>();
		List<Vertex> result = new LinkedList<Vertex>();
		stack.push(root);

		while(!stack.isEmpty()){
			Vertex vertex = stack.pop();
			visited.add(vertex);
			VertexType visitedVertexType = VertexType.fromString(vertex.getProperty(RATConstants.VertexTypeField).toString());
			if(visitedVertexType.equals(vertexType)){
				Object visitedValue = vertex.getProperty(property);
				if(visitedValue != null && visitedValue.toString().equalsIgnoreCase(value.toString())){
					result.add(vertex);
				}
			}
			Iterator<Vertex>it = vertex.getVertices(Direction.BOTH).iterator();
			while(it.hasNext()){
				Vertex child = it.next();
				if(!visited.contains(child) ){
					stack.push(child);
				}
			}
		}
		
		return result;
	}
	
	// COMMENT: conta i nodi attaccati al nodo root
	public int countNodes() throws Exception{
		Vertex rootVertex = this.getRootVertex();
		if(rootVertex == null){
			throw new Exception();
		}
		
		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		int num = (int) p.outE().inV().count();
		
		return num;
	}
	
	// COMMENT: conta tutti i nodi del grafo, siano essi attaccati a root o no; conta anche root
	public int countAllNodes() throws Exception{
		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(_graph.getVertices());
		int num = (int) p.count();
		
		return num;
	}
	
	private Vertex getRootVertex() throws Exception{
		String uuid = _header.getRootVertexUUID();
		if(!Utils.isUUID(uuid)){
			throw new Exception();
		}
		
		Iterator<Vertex> it = _graph.getVertices(RATConstants.VertexUUIDField, uuid).iterator();
		if(!it.hasNext()){
			throw new Exception();
		}
		
		return it.next();
	}
	
	protected void setHeader(JsonHeader header){
		_header = header;
	}

	public JsonHeader getHeader() {
		return _header;
	}

	private void setGraph(TinkerGraph graph) {
		this._graph = graph;
	}
	
	public String getStatusCode() {
		return _header.getStatusCode();
	}
}
