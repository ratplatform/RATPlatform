/**
 * @author Daniele Grignani (dgr)
 * @date Oct 19, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import java.util.List;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;

public interface IQueryFrame extends IRATVertexFrame{
    @Property(RATConstants.TowardNode)
    public void setTowardNode(final VertexType type);
    @Property(RATConstants.TowardNode)
    public VertexType getTowardNode();
    
    @Property(RATConstants.FromNode)
    public void setFromNode(final VertexType type);
    @Property(RATConstants.FromNode)
    public VertexType getFromNode();
    
    @JavaHandler
    public Iterable<IRATQueryEdgeFrame> getAdjacentVertices();
    
    abstract class Impl implements JavaHandlerContext<Vertex>, IQueryFrame{
    	
    	private static final PipeFunction<Vertex, Boolean> filterFunction = new PipesFunction<Vertex, Boolean>(){
    		@Override
    		public Boolean compute(Vertex vertex){
    			boolean result = false;
    			String content = vertex.getProperty(RATConstants.VertexTypeField);
    			
    			result = content.equalsIgnoreCase(VertexType.InstructionParameter.toString()) ? true : false;
    			return !result;
    		}
    	};
    	
    	public Iterable<IRATQueryEdgeFrame> getAdjacentVertices() {
    		GremlinPipeline<Vertex, Edge>pipe = new GremlinPipeline<Vertex, Edge>(this.asVertex());
    		List<Edge> list = pipe.outE().inV().filter(Impl.filterFunction).inE().toList();
    		
    		return this.frameEdges(list, IRATQueryEdgeFrame.class);
		}
    }
}
