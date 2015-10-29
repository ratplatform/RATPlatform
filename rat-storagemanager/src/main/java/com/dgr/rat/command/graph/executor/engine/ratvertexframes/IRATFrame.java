/**
 * @author Daniele Grignani (dgr)
 * @date Oct 17, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;

public interface IRATFrame extends VertexFrame{
    @Property(RATConstants.VertexLabelField)
    public void setVertexLabelField(final String text);
    
    @Property(RATConstants.VertexLabelField)
    public String getVertexLabelField();
    
    @Property(RATConstants.VertexCommandOwnerField)
    public void setVertexCommandOwnerField(final String commandOwner);
    
    @Property(RATConstants.VertexCommandOwnerField)
    public String getVertexCommandOwnerField();
    
    // COMMENT: UUID uguale per tutti gli elementi del comando: vertices e UUID
    @Property(RATConstants.CommandGraphUUID)
    public void setCommandGraphUUID(final String uuid);
    @Property(RATConstants.CommandGraphUUID)
    public String geCommandGraphUUID();
    
    @Property(RATConstants.VertexTypeField)
    public void setVertexTypeField(final VertexType type);
    
    @Property(RATConstants.VertexTypeField)
    public VertexType getVertexTypeField();
    
    // COMMENT: UUID che identifica univocamente questo vertice
    @Property(RATConstants.VertexUUIDField)
    public void setVertexUUIDField(final String uuid);
    @Property(RATConstants.VertexUUIDField)
    public String getVertexUUIDField();
}
