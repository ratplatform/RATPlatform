/**
 * @author Daniele Grignani (dgr)
 * @date Sep 20, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import java.lang.reflect.Constructor;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.InstructionWrapper;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.CommandNode;
import com.dgr.rat.graphgenerator.node.wrappers.Properties;
import com.dgr.rat.graphgenerator.node.wrappers.SystemKeyNode;
import com.dgr.rat.graphgenerator.node.wrappers.User;
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
		
//		Properties properties = this.buildNode(Properties.class);
//		properties.addCreateVertexInstruction("userName", RATConstants.VertexContentUndefined, ReturnType.string);
//		properties.addCreateVertexInstruction("userPwd", RATConstants.VertexContentUndefined, ReturnType.string);
//		properties.addCreateVertexInstruction("userEmail", RATConstants.VertexContentUndefined, ReturnType.string);
//		Properties properties = this.buildNode(Properties.class);
		InstructionWrapper instructionWrapper = rootNode.addPropertyVertexInstruction("userEmail", RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addPropertyVertexInstruction("userName", RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addPropertyVertexInstruction("userPwd", RATConstants.VertexContentUndefined, ReturnType.string);
		
		SystemKeyNode isUserOfNode = this.buildNode(SystemKeyNode.class, "is-user-of");
		isUserOfNode.addCreateVertexInstruction("nodeName", "is-user-of", ReturnType.string);
		// COMMENT: bind verso il RATNode
		isUserOfNode.addBindInstruction("isUserOfNodeUUID", RATConstants.VertexContentUndefined);
//		this.setQueryPivot(isUserOfNode, rootNode.getType(), VertexType.RootDomain, "StartQueryPipe", "SetQueryPipe", "GetAllUsers");
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class, "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso il RATNode
		isPutByNode.addBindInstruction("isPutByNodeUUID", RATConstants.VertexContentUndefined);
		
		SystemKeyNode isPutByNode2 = this.buildNode(SystemKeyNode.class, "is-put-by");
		isPutByNode2.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso il RATNode
		isPutByNode2.addBindInstruction("isPutByNode2UUID", RATConstants.VertexContentUndefined);
		
		SystemKeyNode isUserNode = this.buildNode(SystemKeyNode.class, "is-user");
		isUserNode.addCreateVertexInstruction("nodeName", "is-user", ReturnType.string);
		
//		rootNode.addChild(properties);
		rootNode.addChild(isUserOfNode);
		rootNode.addChild(isUserNode);
		isUserNode.addChild(isPutByNode);
		isUserOfNode.addChild(isPutByNode2);
		
		// COMMENT: dammi tutti gli utenti agganciati al RootDomain
		this.setQueryPivot(isUserOfNode, "GetAllUsers", "StartQueryPipe", true);
		this.setQueryPivot(isUserOfNode, "GetAllUsers", "SetQueryPipe", false);
		this.setQueryPivot(rootNode, "GetAllUsers", "GetAllNodesByType", false);
		
		// COMMENT: dammi l'utente agganciati al RootDomain con quell'email
		this.setQueryPivot(isUserOfNode, "GetUserByEmail", "StartQueryPipe", true);
		this.setQueryPivot(isUserOfNode, "GetUserByEmail", "SetQueryPipe", false);
		this.setQueryPivot(rootNode, "GetUserByEmail", "SetQueryPipe", false);
//		this.setQueryPivot(properties, "GetUserByEmail", "SetQueryPipe", false);
		this.setQueryPivot(instructionWrapper, "GetUserByEmail", "GetUserByEmail", false, "userEmail");
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
