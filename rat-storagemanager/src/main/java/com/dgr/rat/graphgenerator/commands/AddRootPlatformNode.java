/**
 * @author Daniele Grignani (dgr)
 * @date Sep 16, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.RATPlatformDomainNode;
import com.dgr.rat.graphgenerator.node.wrappers.SystemKeyNode;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class AddRootPlatformNode extends AbstractCommand{
	
	public AddRootPlatformNode(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}
	
	public void addNodesToGraph() throws Exception{
		RATPlatformDomainNode rootDomainNode = this.buildRootNode(RATPlatformDomainNode.class, VertexType.RootDomain.toString());
		rootDomainNode.addCreateRootPlatformVertexInstruction("rootDomainName", "RAT", ReturnType.string);
//		this.setQueryPivot(rootDomainNode, rootDomainNode.getType(), VertexType.RootDomain, "StartQueryPipe", "SetQueryPipe","GetRootDomain");
		
		SystemKeyNode isSystemNode = this.buildNode(SystemKeyNode.class,  "is-system");
		isSystemNode.addCreateVertexInstruction("nodeName", "is-system", ReturnType.string);
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class,  "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		
		rootDomainNode.addBindInstruction("commandsNodeUUID", RATConstants.VertexContentUndefined);
		rootDomainNode.addBindInstruction("queriesNodeUUID", RATConstants.VertexContentUndefined);
		
		// COMMENT: prima eseguo il bind, poi modifico l'UUID del nodo RAT, altrimenti il bind fallisce
//		rootDomainNode.addInstruction("SetVertexProperty", RATConstants.VertexUUIDField, RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		rootDomainNode.addChild(isSystemNode);
		isSystemNode.addChild(isPutByNode);
		isPutByNode.addChild(rootDomainNode);
		
		this.setQueryPivot(isSystemNode, "GetRootDomain", "GetRootDomain", true, "nodeType");
//		this.setQueryPivot(isSystemNode, "GetRootDomain", "SetQueryPipe", true);
//		this.setQueryPivot(rootDomainNode, "GetRootDomain", "GetRootDomain", false);
//		this.setQueryPivot(rootDomainNode, "GetRootDomain", "GetUserByEmail", false, "domainType");
	}
}
