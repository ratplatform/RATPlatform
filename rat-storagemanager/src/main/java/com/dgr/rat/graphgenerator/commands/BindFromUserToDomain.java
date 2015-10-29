/**
 * @author Daniele Grignani (dgr)
 * @date Oct 13, 2015
 */

package com.dgr.rat.graphgenerator.commands;

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
		isUserOfNode.addInstruction("BindDomainUser", "domainUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class, "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso lo user
		isPutByNode.addInstruction("BindDomainUser", "userUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		this.setQueryPivot(isPutByNode, VertexType.User, VertexType.Domain, "StartQueryPipe", "SetQueryPipe", "GetAllUserDomains");
		
		isUserOfNode.addChild(isPutByNode);
	}

}
