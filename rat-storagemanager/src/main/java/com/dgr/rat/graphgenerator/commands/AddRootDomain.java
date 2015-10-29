/**
 * @author Daniele Grignani (dgr)
 * @date Sep 16, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.RATDomainNode;
import com.dgr.rat.graphgenerator.node.wrappers.SystemKeyNode;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class AddRootDomain extends AbstractCommand{
	
	public AddRootDomain(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}
	
	public void addNodesToGraph() throws Exception{
		RATDomainNode rootDomainNode = this.buildRootNode(RATDomainNode.class, VertexType.RootDomain.toString());
		rootDomainNode.addCreateCommandRootVertexInstruction("rootDomainName", "RAT", ReturnType.string);

		SystemKeyNode isSystemNode = this.buildNode(SystemKeyNode.class,  "is-system");
		isSystemNode.addCreateVertexInstruction("nodeName", "is-system", ReturnType.string);
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class,  "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		
		rootDomainNode.addBindInstruction("commandsNodeUUID", RATConstants.VertexContentUndefined);
		rootDomainNode.addBindInstruction("queriesNodeUUID", RATConstants.VertexContentUndefined);
		
		rootDomainNode.addChild(isSystemNode);
		isSystemNode.addChild(isPutByNode);
		isPutByNode.addChild(rootDomainNode);
	}
}
