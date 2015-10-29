/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;

public interface IRATNodeFrame extends IRATVertexFrame{
    @Incidence(label = "Command", direction=Direction.OUT)
    public IRATNodeEdgeFrame addAdjacentVertex(final IRATFrame node);
    
    @Incidence(label = "Command", direction = Direction.OUT)
    public Iterable<IRATNodeEdgeFrame> getAdjacentVertices();
    
    @JavaHandler
    // COMMENT: non considero assolutamente i nodi di tipo instructions
    // inoltre conto solo gli edge out
    public int getVertexDegree();
    
    abstract class Impl implements JavaHandlerContext<Vertex>, IRATNodeFrame{
    	@Override
    	@JavaHandler
    	public int getVertexDegree() {
    		GremlinPipeline<Vertex, Edge> pipe = new GremlinPipeline<Vertex, Edge> (this.asVertex());
    		long num = pipe.outE().inV().filter(filterFunction).count();
    		return (int) num;
    	}
    	
    	private static final PipeFunction<Vertex, Boolean> filterFunction = new PipesFunction<Vertex, Boolean>(){
    		@Override
    		public Boolean compute(Vertex vertex){
    			boolean result = false;
    			String content = vertex.getProperty(RATConstants.VertexTypeField);
    			
    			result = content.equalsIgnoreCase(VertexType.Instruction.toString()) ? true : false;
    			return !result;
    		}
    	};
    }
}


