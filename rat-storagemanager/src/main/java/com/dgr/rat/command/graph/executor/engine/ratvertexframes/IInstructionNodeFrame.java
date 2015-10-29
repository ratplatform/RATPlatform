/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import java.util.Iterator;

import com.dgr.rat.commons.constants.RATConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public interface IInstructionNodeFrame extends IRATInstructionFrame{
    @Adjacency(label = RATConstants.EdgeInstructionParameter, direction = Direction.OUT)
    public Iterable<IInstructionParameterNodeFrame> getUserCommandsInstructionParameters();
    
    @Incidence(label = RATConstants.EdgeInstructionParameter, direction = Direction.OUT)
    public IInstructionParameterEdgeFrame addUserCommandsInstructionsParameter(final IInstructionParameterNodeFrame param);
    
    @Incidence(label = RATConstants.EdgeInstructionParameter, direction = Direction.OUT)
    public Iterable<IInstructionParameterEdgeFrame> getInstructionParameterInfo();
    
    @Property(RATConstants.MaxNumParameters)
    public void setMaxNumParameters(final Integer maxNum);
    
    @Property(RATConstants.MaxNumParameters)
    public Integer getMaxNumParameters();
    
    @JavaHandler
    public IInstructionParameterNodeFrame getInstructionParameter(int num);
    
    @JavaHandler
    public long getNumberOfInstructionParameters();
    
    abstract class Impl implements JavaHandlerContext<Vertex>, IInstructionNodeFrame{
    	@Override
    	@JavaHandler
    	public IInstructionParameterNodeFrame getInstructionParameter(int num) {
    		Iterator<IInstructionParameterEdgeFrame>it = this.getInstructionParameterInfo().iterator();
    		IInstructionParameterNodeFrame result = null;
    		while(it.hasNext()){
    			IInstructionParameterEdgeFrame instructionInfo = it.next();
    			if(instructionInfo.getWeight() == num){
    				result = instructionInfo.getInstructionParameter();
    				break;
    			}
    		}
    		return result;
    	}
    	
    	@Override
    	@JavaHandler
    	public long getNumberOfInstructionParameters(){
    		GremlinPipeline<Vertex, Edge> pipe = new GremlinPipeline<Vertex, Edge> (this.asVertex());
    		long num = pipe.outE().has("weight").count();
    		
    		return num;
    	}
    }
}
