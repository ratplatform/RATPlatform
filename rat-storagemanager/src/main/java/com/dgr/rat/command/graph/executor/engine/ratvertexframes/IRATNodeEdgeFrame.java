/**
 * @author Daniele Grignani (dgr)
 * @date Sep 24, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import com.dgr.rat.commons.constants.RATConstants;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.Property;

public interface IRATNodeEdgeFrame extends EdgeFrame{
    @Property("weight")
    public Integer getWeight();
    @Property("weight")
    public void setWeight(Integer weight);
    
    // COMMENT: UUID univoco dell'edge
    @Property(RATConstants.EdgeUUIDField)
    public String getEdgeUUID();
    @Property(RATConstants.EdgeUUIDField)
    public void setEdgeUUID(String uuid);

    @Property(RATConstants.CommandGraphUUID)
    public void setCommandGraphUUID(final String uuid);
    @Property(RATConstants.CommandGraphUUID)
    public String geCommandGraphUUID();

    @OutVertex
    IRATNodeFrame getOutRatNode();

    @InVertex
    IRATNodeFrame getInRatNode();
}
