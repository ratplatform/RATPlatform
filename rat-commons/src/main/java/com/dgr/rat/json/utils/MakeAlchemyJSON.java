/**
 * @author Daniele Grignani (dgr)
 * @date Aug 26, 2015
 */

package com.dgr.rat.json.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MakeAlchemyJSON {

	public MakeAlchemyJSON() {
	}
	
	// TODO: da capire differenza con fromRatJsonToAlchemy
	public static String fromRatJsonToAlchemy2(String ratJson) throws Exception{
		int pos = ratJson.indexOf("\"edges\"");
		String edges = ratJson.substring(pos);
//		System.out.println(edges);
		edges = MakeAlchemyJSON.makeAlchemyJSONEdges(edges);
		String vertices = ratJson.substring(0, pos);
		vertices = vertices.replace("\"vertices\"", "\"nodes\"");
		vertices = MakeAlchemyJSON.makeAlchemyJSONVertices(vertices);
//		System.out.println(vertices);
		String result = vertices + edges;
//		System.out.println(json);
		return result;
	}
	
	private static String makeAlchemyJSONEdges(String inJson){
		String edgesString = inJson.replaceAll("\"_id\"[\\s:]*\"[0-9]*\"[\\s,]*", "");
		edgesString = edgesString.replaceAll("\"_type\"[\\s:]*\"edge\"[\\s,]*", "");
		edgesString = edgesString.replace("\"_outV\"", "\"source\"");
		edgesString = edgesString.replace("\"_inV\"", "\"target\"");
		edgesString = edgesString.replace("\"_label\"", "\"caption\"");
		
		return edgesString;
	}
	
	private static String makeAlchemyJSONVertices(String inVertices){
		String outVertices = inVertices.replace("\"_id\"", "\"id\"");
		
		return outVertices;
	}
	
	private static String makeAlchemyJSON(String header, String vertices, String edges){
		String json = "{" + "\"header\":" + header + ",\"nodes\":" + vertices + ",\"edges\":" + edges +"}";
		
		return json;
	}
	
	public static String fromRatJsonToAlchemy(String ratJson) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		RATJson jsonHeader = (RATJson) mapper.readValue(ratJson, RATJson.class);
		
		String settings = mapper.writeValueAsString(jsonHeader.getSettings());
		Edges edgesObj = (Edges) mapper.readValue(settings, Edges.class);
		String edgesString = mapper.writeValueAsString(edgesObj.getEdges());
		edgesString = MakeAlchemyJSON.makeAlchemyJSONEdges(edgesString);
//		System.out.println(edgesString);
		
		String nodesString = mapper.writeValueAsString(edgesObj.getNodes());
		nodesString = MakeAlchemyJSON.makeAlchemyJSONVertices(nodesString);
//		System.out.println(nodesString);

		String header = mapper.writeValueAsString(jsonHeader.getHeader());
		String json = MakeAlchemyJSON.makeAlchemyJSON(header, nodesString, edgesString);

		return json;
	}
}
