/**
 * @author Daniele Grignani (dgr)
 * @date Sep 6, 2015
 */

package com.dgr.rat.json.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

public class Edges {
	private Object _edges = null;
	private Object _nodes = null;
	public Edges() {
		// TODO Auto-generated constructor stub
	}
	
	@JsonRawValue
    @JsonProperty("edges")
    protected void setEdges(Object edges){
		_edges = edges;
    }
	
	public Object getEdges(){
		return _edges;
	}
	
	@JsonRawValue
    @JsonProperty("vertices")
    protected void setNodes(Object nodes){
		_nodes = nodes;
    }
	
	public Object getNodes(){
		return _nodes;
	}
}
