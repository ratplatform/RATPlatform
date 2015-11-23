/**
 * @author Daniele Grignani (dgr)
 * @date Sep 17, 2015
 */

package com.dgr.rat.command.graph.executor.engine.ratvertexframes;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.utils.VertexType;
import com.tinkerpop.frames.Property;

public interface IRATNodeQueryPivotFrame extends IRATFrame{
    @Property(RATConstants.QueryName)
    public void setQueryName(final String queryName);
    
    @Property(RATConstants.QueryName)
    public String getQueryName();
    
    @Property(RATConstants.TowardNode)
    public void setTowardNode(final VertexType type);
    
    @Property(RATConstants.TowardNode)
    public VertexType getTowardNode();

    @Property(RATConstants.FromNode)
    public void setFromNode(final VertexType type);
    
    @Property(RATConstants.FromNode)
    public VertexType getFromNode();
    
    @Property(RATConstants.InstructionOrderField)
    public void setInstructionOrderField(final Integer order);
    
    @Property(RATConstants.InstructionOrderField)
    public Integer getInstructionOrderField();
    
    @Property(RATConstants.CorrelationKey)
    public void setCorrelationKey(final String correlationKey);
    
    @Property(RATConstants.CorrelationKey)
    public String getCorrelationKey();
    
    @Property(RATConstants.IsRootQueryPivot)
    public void setIsRootNode(final Boolean isRoot);
    
    @Property(RATConstants.IsRootQueryPivot)
    public Boolean getIsRootNode();
    
//    @Property(RATConstants.StartPipeInstruction)
//    public void setStartPipeInstructionName(final String instructionName);
//    
//    @Property(RATConstants.StartPipeInstruction)
//    public String getStartPipeInstructionName();
//    
//    @Property(RATConstants.InternalPipeInstruction)
//    public void setInternalPipeInstruction(final String instructionName);
//    
//    @Property(RATConstants.InternalPipeInstruction)
//    public String getInternalPipeInstruction();
//    
//    @Property(RATConstants.EndPipeInstruction)
//    public void setEndPipeInstruction(final String instructionName);
//    
//    @Property(RATConstants.EndPipeInstruction)
//    public String getEndPipeInstruction();
}


