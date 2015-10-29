/**
 * @author Daniele Grignani (dgr)
 * @date Oct 13, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.CommentNode;
import com.dgr.rat.graphgenerator.node.wrappers.SystemKeyNode;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;
// TRASH
public class GetAllDomains extends AbstractCommand{

	public GetAllDomains(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.AbstractCommand#addNodesToGraph()
	 */
	@Override
	public void addNodesToGraph() throws Exception {
		CommentNode rootNode = this.buildRootNode(CommentNode.class, VertexType.Comment.toString());
		rootNode.addCreateVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class, "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso il nodo dell'utente
		isPutByNode.addBindInstruction("userNodeUUID", RATConstants.VertexContentUndefined);
		
		SystemKeyNode belongsTo = this.buildNode(SystemKeyNode.class, "belongs-to");
		belongsTo.addCreateVertexInstruction("nodeName", "belongs-to", ReturnType.string);
		// COMMENT: bind verso il nodo dell'owner (dominio o altro commentnode)
		belongsTo.addBindInstruction("ownerNodeUUID", RATConstants.VertexContentUndefined);
		
		SystemKeyNode isPublic = this.buildNode(SystemKeyNode.class, "is-public");
		isPublic.addCreateVertexInstruction("nodeName", "is-public", ReturnType.string);
		
		SystemKeyNode isUniversal = this.buildNode(SystemKeyNode.class, "is-universal");
		isUniversal.addCreateVertexInstruction("nodeName", "is-universal", ReturnType.string);
		
//		InputFromUser content = this.buildNode(InputFromUser.class);
//		content.addCreateVertexInstruction("inputFromUser", RATConstants.VertexContentUndefined, ReturnType.string);
		SystemKeyNode content = this.buildNode(SystemKeyNode.class, "inputFromUser");
		content.addCreateVertexInstruction("inputFromUser", RATConstants.VertexContentUndefined, ReturnType.string);
		
		SystemKeyNode start = this.buildNode(SystemKeyNode.class, "start");
		start.addCreateVertexInstruction("start", RATConstants.VertexContentUndefined, ReturnType.integer);
		
		SystemKeyNode end = this.buildNode(SystemKeyNode.class, "end");
		end.addCreateVertexInstruction("end", RATConstants.VertexContentUndefined, ReturnType.integer);
		
		SystemKeyNode urlDoc = this.buildNode(SystemKeyNode.class, "urlDocument");
		urlDoc.addCreateVertexInstruction("urlDocument", RATConstants.VertexContentUndefined, ReturnType.url);
		
		rootNode.addChild(isPutByNode);
		rootNode.addChild(belongsTo);
		rootNode.addChild(isPublic);
		rootNode.addChild(isUniversal);
		rootNode.addChild(content);
		
		content.addChild(start);
		content.addChild(end);
		content.addChild(urlDoc);
	}
}
