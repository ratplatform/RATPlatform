/**
 * @author Daniele Grignani (dgr)
 * @date Oct 19, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import java.util.Iterator;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;

public interface IRATVertexFrame extends IRATFrame{
    @Property(RATConstants.VertexContentField)
    public void setVertexContentField(final String content);
    
    @Property(RATConstants.VertexContentField)
    public String getVertexContentField();
    
    @Property(RATConstants.VertexIsRootField)
    public void setIsRootVertexField(final Boolean isRoot);
    
    @Property(RATConstants.VertexIsRootField)
    public Boolean getIsRootVertexField();
    
    @Incidence(label = RATConstants.EdgeInstruction)
    public IInstructionEdgeFrame addUserCommandsInstruction(final IInstructionNodeFrame instruction);
    
    @Adjacency(label = RATConstants.EdgeInstruction, direction = Direction.OUT)
    public Iterable<IInstructionNodeFrame> getUserCommandsInstructions();
    
    @Incidence(label = RATConstants.EdgeInstruction, direction = Direction.OUT)
    public Iterable<IInstructionEdgeFrame> getInstructionInfo();
    
    @JavaHandler
    public IInstructionNodeFrame getInstruction(int num);
    
    @JavaHandler
    public long getNumberOfInstructions();
    
    abstract class Impl implements JavaHandlerContext<Vertex>, IRATVertexFrame{
    	@Override
    	@JavaHandler
    	public IInstructionNodeFrame getInstruction(int num) {
    		Iterator<IInstructionEdgeFrame>it = this.getInstructionInfo().iterator();
    		IInstructionNodeFrame result = null;
    		while(it.hasNext()){
    			IInstructionEdgeFrame instructionInfo = it.next();
    			if(instructionInfo.getWeight() == num){
    				result = instructionInfo.getInstruction();
    				break;
    			}
    		}
    		return result;
    	}
    	
    	private static final PipeFunction<Vertex, Boolean> filterFunction = new PipesFunction<Vertex, Boolean>(){
    		@Override
    		public Boolean compute(Vertex vertex){
    			boolean result = false;
    			String content = vertex.getProperty(RATConstants.VertexTypeField);
    			
    			result = content.equalsIgnoreCase(VertexType.Instruction.toString()) ? true : false;
    			return result;
    		}
    	};
    	
    	@Override
    	@JavaHandler
    	public long getNumberOfInstructions(){
    		GremlinPipeline<Vertex, Edge> pipe = new GremlinPipeline<Vertex, Edge> (this.asVertex());
    		long num = pipe.outE().inV().filter(filterFunction).count();
    		
    		return num;
    	}
    }
}
