/**
 * @author Daniele Grignani (dgr)
 * @date Aug 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.commands;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.dgr.rat.command.graph.executor.engine.ICommandGraphData;
import com.dgr.rat.command.graph.executor.engine.ICommandGraphVisitableFactory;
import com.dgr.rat.command.graph.executor.engine.ICommandNodeVisitable;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.commons.constants.RATConstants;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;
import com.tinkerpop.gremlin.java.GremlinPipeline;

// Rappresenta il grafo degli elementi JSON dentro "settings"
public class CommandTemplateGraph extends TinkerGraph implements ICommandGraphData{
	private static final long serialVersionUID = -7211111627086377576L;

	public CommandTemplateGraph() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T extends ICommandNodeVisitable> ICommandNodeVisitable getRootNode(Class<T> cls) throws Exception{
		Vertex vertex = this.getVertex(RATConstants.VertexIsRootField, String.valueOf(true));
		ICommandNodeVisitable root = this.getRoot(vertex, cls);
		
		return root;
	}

	// TODO: da gestire l'errore se root node != 1
	private <T extends ICommandNodeVisitable> ICommandNodeVisitable getRoot(Vertex vertex, Class<T> cls) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> argTypes[] = { Vertex.class };
        Constructor<?> ct = cls.getConstructor(argTypes);
        Object arglist[] = { vertex };
        Object object = ct.newInstance(arglist);
        ICommandNodeVisitable result = (ICommandNodeVisitable) object;
		
		return result;
	}
	
	protected Vertex getVertex(String label, Object value) {
		Iterable<Vertex> it = this.getVertices(label, value);
		System.out.println("label " + label + "; value" + value.toString());
		return it.iterator().next();
//		GremlinPipeline<Vertex, Vertex> queryPipe = new GremlinPipeline<Vertex, Vertex>(this.getVertices());
//		return (Vertex) queryPipe.has(RATConstants.VertexIsRootField, true).next();
	}

	// TODO: da gestire l'errore se root node != 1
	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.ICommandGraphData#getRootNode(com.dgr.rat.command.graph.ICommandGraphVisitableFactory)
	 */
	@Override
	public ICommandNodeVisitable getRootNode(ICommandGraphVisitableFactory factory) throws Exception {
		Vertex vertex = this.getVertex(RATConstants.VertexIsRootField, true);
		FramedGraph<CommandTemplateGraph> framedGraph =  new FramedGraphFactory(new JavaHandlerModule()).create(this);
		IRATNodeFrame node = framedGraph.frame(vertex, IRATNodeFrame.class);
		ICommandNodeVisitable result = new RATNodeProxy(node);
		
		return result;
	}
}
