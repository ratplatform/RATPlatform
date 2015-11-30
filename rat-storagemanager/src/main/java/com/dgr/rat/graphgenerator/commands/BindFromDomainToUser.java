/**
 * @author Daniele Grignani (dgr)
 * @date Oct 13, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.InstructionWrapper;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.SystemKeyNode;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class BindFromDomainToUser extends AbstractCommand{

	public BindFromDomainToUser(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.AbstractCommand#addNodesToGraph()
	 */
	@Override
	public void addNodesToGraph() throws Exception {
		SystemKeyNode isUserOfNode = this.buildNode(SystemKeyNode.class, "is-user-of");
		isUserOfNode.addCreateCommandRootVertexInstruction("nodeName", "is-user-of", ReturnType.string);
		// COMMENT: bind verso il DomainNode
		/*isUserOfNode.addInstruction("BindDomainUser", "domainNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);*/
//		this.setQueryPivot(isUserOfNode, VertexType.Domain, VertexType.User, "StartQueryPipe", "GetAllUserDomainsQueryPipe", "GetAllDomainUsers");

		SystemKeyNode isPutByNode = this.buildRootNode(SystemKeyNode.class, "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		InstructionWrapper instructionWrapper = isPutByNode.addInstruction("BindDomainUser", "userUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		isPutByNode.addInstruction("BindDomainUser", RATConstants.VertexTypeField, RATConstants.VertexContentUndefined, ReturnType.string);
		isPutByNode.addInstruction("BindDomainUser", "domainNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		isPutByNode.addChild(isUserOfNode);
		
//		this.setQueryPivot(isUserOfNode, "GetAllDomainUsers", "StartQueryPipe", true);
//		this.setQueryPivot(isUserOfNode, "GetAllDomainUsers", "SetQueryPipe", false);
//		this.setQueryPivot(isPutByNode, "GetAllDomainUsers", "SetQueryPipe", false);
//		this.setQueryPivot(instructionWrapper, "GetAllDomainUsers", "GetUserByEmail", false, RATConstants.VertexTypeField);
		
//		this.setQueryPivot(isPutByNode, "GetAllUserDomains", "StartQueryPipe", true);
//		this.setQueryPivot(isPutByNode, "GetAllUserDomains", "SetQueryPipe", false);
//		this.setQueryPivot(isUserOfNode, "GetAllUserDomains", "SetQueryPipe", false);
//		this.setQueryPivot(instructionWrapper, "GetAllUserDomains", "GetUserByEmail", false, RATConstants.VertexTypeField);
	}

}
