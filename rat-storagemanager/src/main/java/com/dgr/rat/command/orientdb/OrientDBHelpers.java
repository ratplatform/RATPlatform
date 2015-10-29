/**
 * @author Daniele Grignani (dgr)
 * @date Jun 7, 2015
 */

package com.dgr.rat.command.orientdb;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientDBHelpers {
	public OrientDBHelpers() {
		// TODO Auto-generated constructor stub
	}
	
	public static Vertex createVertex(OrientGraph graph, String propertyName, String propertyValue) throws Exception {
		Vertex vertex = graph.addVertex(null);
		vertex.setProperty(propertyName, propertyValue);
		
		return vertex;
	}
	
	public void setNodeKeyAndOwnership(OrientGraph graph, Vertex node, Vertex nodeOwner, String nodeKey, Vertex keyOwner) throws Exception {
//		List<Vertex> targets;
//		if (nodeOwner != null) {
//			targets = new ArrayList<Vertex>();
//			targets.add(node);
//			targets.add(nodeOwner);
//			
//			this.createChild(graph, targets, SemanticPrefix + IS_PUT_BY);
//		}
//		
//		Vertex child = this.createChild(graph, node, nodeKey);
//		
//		targets = new ArrayList<Vertex>();
//		targets.add(child);
//		targets.add(keyOwner);
//		this.createChild(graph, targets, SemanticPrefix + IS_PUT_BY);
	}
}
