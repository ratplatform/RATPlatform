/**
 * @author Daniele Grignani (dgr)
 * @date Sep 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.Property;

public interface IInstructionEdgeFrame extends EdgeFrame{
    @Property("weight")
    public Integer getWeight();
    @Property("weight")
    public void setWeight(Integer weight);

    @OutVertex
    IRATNodeFrame getRatNode();

    @InVertex
    IInstructionNodeFrame getInstruction();
}
