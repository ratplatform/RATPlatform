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

public class AddPageAnnotation extends AbstractCommand{

	public AddPageAnnotation(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.AbstractCommand#addNodesToGraph()
	 */
	@Override
	public void addNodesToGraph() throws Exception {
		CommentNode rootNode = this.buildRootNode(CommentNode.class, VertexType.Comment.toString());
		rootNode.addCreateCommandRootVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
//		rootNode.addPropertyVertexInstruction("startPageX", RATConstants.VertexContentUndefined, ReturnType.integer);
//		rootNode.addPropertyVertexInstruction("startPageY", RATConstants.VertexContentUndefined, ReturnType.integer);
//		rootNode.addPropertyVertexInstruction("endPageX", RATConstants.VertexContentUndefined, ReturnType.integer);
//		rootNode.addPropertyVertexInstruction("endPageY", RATConstants.VertexContentUndefined, ReturnType.integer);
		
		rootNode.addInstruction("SetVertexProperty", RATConstants.VertexLabelField, RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addInstruction("SetVertexProperty", RATConstants.VertexContentField, RATConstants.VertexContentUndefined, ReturnType.string);
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class, "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso il nodo dell'utente
		isPutByNode.addBindInstruction("userNodeUUID", RATConstants.VertexContentUndefined);
		
		SystemKeyNode belongsTo = this.buildNode(SystemKeyNode.class, "belongs-to");
		belongsTo.addCreateVertexInstruction("nodeName", "belongs-to", ReturnType.string);
		// COMMENT: bind verso il nodo dell'owner (dominio)
		belongsTo.addInstruction("BindSubComment", "domainUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		SystemKeyNode isPublic = this.buildNode(SystemKeyNode.class, "is-public");
		isPublic.addCreateVertexInstruction("nodeName", "is-public", ReturnType.string);
		
		SystemKeyNode isUniversal = this.buildNode(SystemKeyNode.class, "is-universal");
		isUniversal.addCreateVertexInstruction("nodeName", "is-universal", ReturnType.string);
		
		SystemKeyNode webDoc = this.buildNode(SystemKeyNode.class, "webDocument");
		webDoc.addInstruction("CreateWebDocument", "url", RATConstants.VertexContentUndefined, ReturnType.url);
		//webDoc.addInstruction("SetVertexProperty", RATConstants.VertexLabelField, RATConstants.VertexContentUndefined, ReturnType.url);
		
		rootNode.addChild(isPutByNode);
		rootNode.addChild(belongsTo);
		rootNode.addChild(isPublic);
		rootNode.addChild(isUniversal);
		rootNode.addChild(webDoc);
		
//		this.setQueryPivot(isPutByNode, "GetAllUserComments", "StartQueryPipe", true);
//		this.setQueryPivot(isPutByNode, "GetAllUserComments", "SetQueryPipe", false);
//		this.setQueryPivot(rootNode, "GetAllUserComments", "GetAllUserComments", false);
//		
//		this.setQueryPivot(belongsTo, "GetAllPageComments", "StartQueryPipe", true);
//		this.setQueryPivot(belongsTo, "GetAllPageComments", "SetQueryPipe", false);
//		this.setQueryPivot(rootNode, "GetAllDomainComments", "GetWebDoc", false, "url");
		// TODO: nel parametro finale aggiungere anche il tipo, altrimenti è sempre string!
//		this.setQueryPivot(isPutByNode, "GetAllDomainComments", "GetAllDomainComments", false, "userUUID");
//		
//		this.setQueryPivot(belongsTo, "GetCommentComments", "StartQueryPipe", true);
//		this.setQueryPivot(belongsTo, "GetCommentComments", "SetQueryPipe", false);
//		this.setQueryPivot(rootNode, "GetCommentComments", "GetCommentComments", false);
	}
}
