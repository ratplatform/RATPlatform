/**
 * @author Daniele Grignani (dgr)
 * @date Sep 18, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import java.lang.reflect.Constructor;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.InstructionWrapper;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.CommandNode;
import com.dgr.rat.graphgenerator.node.wrappers.Properties;
import com.dgr.rat.graphgenerator.node.wrappers.QueryPivotNode;
import com.dgr.rat.graphgenerator.node.wrappers.RootAdminUser;
import com.dgr.rat.graphgenerator.node.wrappers.SystemKeyNode;
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
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class,  "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		isPutByNode.addBindInstruction("isPutByNodeUUID", RATConstants.VertexContentUndefined);
		
		SystemKeyNode isUserNode = this.buildNode(SystemKeyNode.class,  "is-user");
		isUserNode.addCreateVertexInstruction("nodeName", "is-user", ReturnType.string);
		
		Properties properties = this.buildNode(Properties.class);
		InstructionWrapper instructionWrapper = properties.addPropertyVertexInstruction("userEmail", RATConstants.VertexContentUndefined, ReturnType.string);
		properties.addPropertyVertexInstruction("userName", RATConstants.VertexContentUndefined, ReturnType.string);
		properties.addPropertyVertexInstruction("userPwd", RATConstants.VertexContentUndefined, ReturnType.string);
		
		rootNode.addChild(isUserNode);
		rootNode.addChild(properties);
		rootNode.addChild(isUserOfNode);
		isUserOfNode.addChild(isPutByNode);
		
		this.setQueryPivot(isUserOfNode, "GetAllAdminUsers", "StartQueryPipe", true);
		this.setQueryPivot(isUserOfNode, "GetAllAdminUsers", "SetQueryPipe", false);
		this.setQueryPivot(rootNode, "GetAllAdminUsers", "GetAllAdminUsers", false);
		
		this.setQueryPivot(isUserOfNode, "GetAdminUsersByEmail", "StartQueryPipe", true);
		this.setQueryPivot(isUserOfNode, "GetAdminUsersByEmail", "SetQueryPipe", false);
		this.setQueryPivot(rootNode, "GetAdminUsersByEmail", "SetQueryPipe", false);
		this.setQueryPivot(properties, "GetAdminUsersByEmail", "SetQueryPipe", false);
		this.setQueryPivot(instructionWrapper, "GetAdminUsersByEmail", "GetAdminUsersByEmail", false, "userEmail");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends CommandNode> T buildNode(Class<T> cls) throws Exception {
		T node = null;
		try{
			Class<?> argTypes[] = {AbstractCommand.class};
	        Constructor<?> ct = cls.getConstructor(argTypes);
	        Object arglist[] = { this };
	        Object object = ct.newInstance(arglist);
	        node = (T) object;
		}
        catch(Exception e){
        	e.printStackTrace();
        	throw new Exception(e);
        }
		node.set_nodeUUID(this.createNodeUUID(node));
        return node;
	}
}
