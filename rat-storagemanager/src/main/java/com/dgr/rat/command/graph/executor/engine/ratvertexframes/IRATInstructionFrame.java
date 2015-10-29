/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;

public interface IRATInstructionFrame extends VertexFrame{
    @Property(RATConstants.VertexLabelField)
    public void setVertexLabelField(final String text);
    
    @Property(RATConstants.VertexLabelField)
    public String getVertexLabelField();

    @Property(RATConstants.VertexUUIDField)
    public void setVertexUUIDField(final String uuid);
    
    @Property(RATConstants.VertexUUIDField)
    public String getVertexUUIDField();
    
    @Property(RATConstants.VertexTypeField)
    public void setVertexTypeField(final VertexType type);
    
    @Property(RATConstants.VertexTypeField)
    public VertexType getVertexTypeField();
    
    @Property(RATConstants.VertexIsRootField)
    public void setVertexRoleValueRootField(final Boolean isRoot);
    
    @Property(RATConstants.VertexIsRootField)
    public Boolean getVertexRoleValueRootField();
    
    @Property(RATConstants.VertexContentField)
    public void setVertexContentField(final String content);
    
    @Property(RATConstants.VertexContentField)
    public String getVertexContentField();
    
    @Property(RATConstants.InstructionOrderField)
    public void setInstructionOrderField(final Integer order);
    
    @Property(RATConstants.InstructionOrderField)
    public Integer getInstructionOrderField();
}
