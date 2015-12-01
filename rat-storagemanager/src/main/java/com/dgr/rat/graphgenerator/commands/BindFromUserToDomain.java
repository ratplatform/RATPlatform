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

public class BindFromUserToDomain extends AbstractCommand{

	public BindFromUserToDomain(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.AbstractCommand#addNodesToGraph()
	 */
	@Override
	public void addNodesToGraph() throws Exception {
		SystemKeyNode isUserOfNode = this.buildRootNode(SystemKeyNode.class, "is-user-of");
		isUserOfNode.addCreateCommandRootVertexInstruction("nodeName", "is-user-of", ReturnType.string);
		// COMMENT: bind verso il DomainNode
		InstructionWrapper instructionWrapper = isUserOfNode.addInstruction("BindDomainUser", "domainNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		//isUserOfNode.addInstruction("BindDomainUser", RATConstants.VertexTypeField, RATConstants.VertexContentUndefined, ReturnType.string);
		//isUserOfNode.addInstruction("BindDomainUser", "userUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class, "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso lo user
		/*InstructionWrapper user =*/ isPutByNode.addInstruction("BindDomainUser", "userUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
//		isPutByNode.addInstruction("BindDomainUser", RATConstants.VertexTypeField, VertexType.User.toString(), ReturnType.string);
//		this.setQueryPivot(isPutByNode, VertexType.User, VertexType.Domain, "StartQueryPipe", "SetQueryPipe", "GetAllUserDomains");
//		this.setQueryPivot(isPutByNode, VertexType.User, VertexType.Domain, "StartQueryPipe", "SetQueryPipe", "GetUserDomainByName");
		
		isUserOfNode.addChild(isPutByNode);
		
//		this.setQueryPivot(isUserOfNode, "GetAllDomainUsers", "StartQueryPipe", true);
//		this.setQueryPivot(isUserOfNode, "GetAllDomainUsers", "SetQueryPipe", false);
//		this.setQueryPivot(isPutByNode, "GetAllDomainUsers", "SetQueryPipe", false);
//		this.setQueryPivot(user, "GetAllDomainUsers", "GetUserByEmail", false, RATConstants.VertexTypeField);
		
		this.setQueryPivot(isPutByNode, "GetAllUserDomains", "StartQueryPipe", true);
		this.setQueryPivot(isPutByNode, "GetAllUserDomains", "SetQueryPipe", false);
		this.setQueryPivot(isUserOfNode, "GetAllUserDomains", "SetQueryPipe", false);
		this.setQueryPivot(isUserOfNode, "GetAllUserDomains", "GetUsersAndDomains", false, RATConstants.VertexTypeField);
		
		this.setQueryPivot(isPutByNode, "GetUserDomainByName", "StartQueryPipe", true);
		this.setQueryPivot(isPutByNode, "GetUserDomainByName", "SetQueryPipe", false);
		this.setQueryPivot(isUserOfNode, "GetUserDomainByName", "SetQueryPipe", false);
		this.setQueryPivot(isUserOfNode, "GetUserDomainByName", "GetUserDomainByName", false, RATConstants.VertexTypeField, "domainName");
		
		this.setQueryPivot(isUserOfNode, "GetAllDomainUsers", "StartQueryPipe", true);
		this.setQueryPivot(isUserOfNode, "GetAllDomainUsers", "SetQueryPipe", false);
		this.setQueryPivot(isPutByNode, "GetAllDomainUsers", "SetQueryPipe", false);
		this.setQueryPivot(isPutByNode, "GetAllDomainUsers", "GetUsersAndDomains", false, RATConstants.VertexTypeField);
//		this.setQueryPivot(instructionWrapper, "GetAllUserDomains", "GetUserByEmail", false, RATConstants.VertexTypeField);
	}
	
}
