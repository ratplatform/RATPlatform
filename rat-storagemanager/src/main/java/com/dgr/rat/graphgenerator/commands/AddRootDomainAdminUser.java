/**
 * @author Daniele Grignani (dgr)
 * @date Sep 18, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.RootAdminUser;
import com.dgr.rat.graphgenerator.node.wrappers.SystemKeyNode;
import com.dgr.rat.graphgenerator.node.wrappers.UserName;
import com.dgr.rat.graphgenerator.node.wrappers.UserPwd;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;


public class AddRootDomainAdminUser extends AbstractCommand{

	public AddRootDomainAdminUser(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.AbstractCommand#addNodesToGraph()
	 */
	@Override
	public void addNodesToGraph() throws Exception {
		RootAdminUser rootNode = this.buildRootNode(RootAdminUser.class, VertexType.RootAdminUser.toString());
		rootNode.addCreateCommandRootVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
		
		SystemKeyNode isUserOfNode = this.buildNode(SystemKeyNode.class,  "is-user-of");
		isUserOfNode.addCreateVertexInstruction("nodeName", "is-user-of", ReturnType.string);
		isUserOfNode.addBindInstruction("isUserOfNodeUUID", RATConstants.VertexContentUndefined);
		this.setQueryPivot(isUserOfNode, rootNode.getType(), VertexType.RootDomain, "StartQueryPipe", "SetQueryPipe","GetAllAdminUsers");
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class,  "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		isPutByNode.addBindInstruction("isPutByNodeUUID", RATConstants.VertexContentUndefined);
//		this.setQueryPivot(isPutByNode, VertexType.RootDomain);
		
		SystemKeyNode isUserNode = this.buildNode(SystemKeyNode.class,  "is-user");
		isUserNode.addCreateVertexInstruction("nodeName", "is-user", ReturnType.string);
		
		UserName userNameNode = this.buildNode(UserName.class);
		userNameNode.addCreateVertexInstruction("userName", RATConstants.VertexContentUndefined, ReturnType.string);
		
		UserPwd userPwd = this.buildNode(UserPwd.class);
		userPwd.addCreateVertexInstruction("userPwd", RATConstants.VertexContentUndefined, ReturnType.string);
		
//		RATDomainNode rootDomainNode = this.buildNode(RATDomainNode.class, VertexType.RootDomain.toString());
//		rootDomainNode.addCreateVertexInstruction("rootDomainUUID", RATConstants.VertexContentUndefined, ReturnType.string);
		
		rootNode.addChild(isUserNode);
		rootNode.addChild(userPwd);
		rootNode.addChild(userNameNode);
		rootNode.addChild(isUserOfNode);
		
		isUserOfNode.addChild(isPutByNode);
//		isUserOfNode.addChild(rootDomainNode);
//		
//		isPutByNode.addChild(rootDomainNode);
	}
}
