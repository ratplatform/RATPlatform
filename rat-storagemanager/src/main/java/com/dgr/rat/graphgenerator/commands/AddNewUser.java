/**
 * @author Daniele Grignani (dgr)
 * @date Sep 20, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.SystemKeyNode;
import com.dgr.rat.graphgenerator.node.wrappers.User;
import com.dgr.rat.graphgenerator.node.wrappers.UserName;
import com.dgr.rat.graphgenerator.node.wrappers.UserPwd;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class AddNewUser extends AbstractCommand{

	public AddNewUser(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.AbstractCommand#addNodesToGraph()
	 */
	@Override
	public void addNodesToGraph() throws Exception {
		User rootNode = this.buildRootNode(User.class, VertexType.User.toString());
		rootNode.addCreateCommandRootVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
		
		UserName userNameNode = this.buildNode(UserName.class);
		userNameNode.addCreateVertexInstruction("userName", RATConstants.VertexContentUndefined, ReturnType.string);
		
		UserPwd userPwd = this.buildNode(UserPwd.class);
		userPwd.addCreateVertexInstruction("userPwd", RATConstants.VertexContentUndefined, ReturnType.string);
		
		SystemKeyNode isUserOfNode = this.buildNode(SystemKeyNode.class, "is-user-of");
		isUserOfNode.addCreateVertexInstruction("nodeName", "is-user-of", ReturnType.string);
		// COMMENT: bind verso il RATNode
		isUserOfNode.addBindInstruction("isUserOfNodeUUID", RATConstants.VertexContentUndefined);
		this.setQueryPivot(isUserOfNode, rootNode.getType(), VertexType.RootDomain, "StartQueryPipe", "SetQueryPipe", "GetAllUsers");
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class, "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso il RATNode
		isPutByNode.addBindInstruction("isPutByNodeUUID", RATConstants.VertexContentUndefined);
//		this.setQueryPivot(isPutByNode, VertexType.RootDomain);
		
		SystemKeyNode isPutByNode2 = this.buildNode(SystemKeyNode.class, "is-put-by");
		isPutByNode2.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso il RATNode
		isPutByNode2.addBindInstruction("isPutByNode2UUID", RATConstants.VertexContentUndefined);
//		this.setQueryPivot(isPutByNode2, VertexType.RootDomain);
		
//		NewDomainNode domain = this.buildNode(NewDomainNode.class, VertexType.Domain.toString());
//		domain.addBindInstruction(RATConstants.VertexContentUndefined);
		
//		SystemKeyNode isPutByNode2 = this.buildNode(SystemKeyNode.class, "is-put-by");
//		isPutByNode2.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		
		SystemKeyNode isUserNode = this.buildNode(SystemKeyNode.class, "is-user");
		isUserNode.addCreateVertexInstruction("nodeName", "is-user", ReturnType.string);
		
//		RATDomainNode rootDomain = this.buildNode(RATDomainNode.class, VertexType.RootDomain.toString());
//		rootDomain.addBindInstruction(RATConstants.VertexContentUndefined);
		
		rootNode.addChild(userNameNode);
		rootNode.addChild(userPwd);
		rootNode.addChild(isUserOfNode);
		rootNode.addChild(isUserNode);
		
		isUserNode.addChild(isPutByNode);
		
		isUserOfNode.addChild(isPutByNode2);
//		isUserOfNode.addChild(domain);
//		
//		domain.addChild(isPutByNode2);
		
//		isPutByNode2.addChild(isUserNode);
//		isPutByNode2.addChild(rootDomain);
	}
}
