/**
 * @author Daniele Grignani (dgr)
 * @date Oct 18, 2015
 */

package com.dgr.rat.command.graph.executor.engine.queries;

import com.dgr.rat.command.graph.executor.engine.ICommandGraphVisitableFactory;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.commands.CommandTemplateGraph;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IQueryFrame;
import com.dgr.rat.commons.constants.RATConstants;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

public class QueryTemplateGraph extends CommandTemplateGraph{
	private static final long serialVersionUID = 1L;

	public QueryTemplateGraph() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ICommandNodeVisitable getRootNode(ICommandGraphVisitableFactory factory) throws Exception {
		Vertex vertex = this.getVertex(RATConstants.VertexIsRootField, true);
		FramedGraph<QueryTemplateGraph> framedGraph = new FramedGraphFactory(new JavaHandlerModule()).create(this);
		IQueryFrame node = framedGraph.frame(vertex, IQueryFrame.class);
		ICommandNodeVisitable result = new QueryNodeWrapper(node);
		
		return result;
	}
}
