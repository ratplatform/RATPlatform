package com.dgr.rat.commons.mqmessages;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class RATJSONMessage /*implements IResponse*/{
	private static final long serialVersionUID = 1L;
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
	
	public int countNodes() throws Exception{
		Vertex rootVertex = this.getRootVertex();
		if(rootVertex == null){
			throw new Exception();
		}
		
		GremlinPipeline<Vertex, Vertex> p = new GremlinPipeline<Vertex, Vertex>(rootVertex);
		int num = (int) p.outE().inV().count();
		
		return num;
	}
	
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
}
