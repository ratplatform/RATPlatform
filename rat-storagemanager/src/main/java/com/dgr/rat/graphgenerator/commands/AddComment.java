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

// TODO: da rinominare AddDocumentAnnotation
public class AddComment extends AbstractCommand{

	public AddComment(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.AbstractCommand#addNodesToGraph()
	 */
	@Override
	public void addNodesToGraph() throws Exception {
		CommentNode rootNode = this.buildRootNode(CommentNode.class, VertexType.Comment.toString());
		rootNode.addCreateCommandRootVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
		// TODO: rimuovere startComment ed endComment che non servono
//		rootNode.addPropertyVertexInstruction("startComment", RATConstants.VertexContentUndefined, ReturnType.integer);
//		rootNode.addPropertyVertexInstruction("endComment", RATConstants.VertexContentUndefined, ReturnType.integer);
		//rootNode.addPropertyVertexInstruction("startPageX", RATConstants.VertexContentUndefined, ReturnType.integer);
		//rootNode.addPropertyVertexInstruction("endPageX", RATConstants.VertexContentUndefined, ReturnType.integer);
		//rootNode.addPropertyVertexInstruction("startPageY", RATConstants.VertexContentUndefined, ReturnType.integer);
		//rootNode.addPropertyVertexInstruction("endPageY", RATConstants.VertexContentUndefined, ReturnType.integer);
		rootNode.addPropertyVertexInstruction("jsonCoordinates", RATConstants.VertexContentUndefined, ReturnType.string);
		
		rootNode.addInstruction("SetVertexProperty", RATConstants.VertexLabelField, RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addInstruction("SetVertexProperty", RATConstants.VertexContentField, RATConstants.VertexContentUndefined, ReturnType.string);
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class, "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: bind verso il nodo dell'utente
		isPutByNode.addBindInstruction("userNodeUUID", RATConstants.VertexContentUndefined);
		
		SystemKeyNode belongsTo = this.buildNode(SystemKeyNode.class, "belongs-to");
		belongsTo.addCreateVertexInstruction("nodeName", "belongs-to", ReturnType.string);
		// COMMENT: bind verso il nodo dell'owner (dominio)
		//belongsTo.addBindInstruction("domainUUID", RATConstants.VertexContentUndefined);
		//this.setQueryPivot(belongsTo, rootNode.getType(), VertexType.Domain, "StartQueryPipe", "SetQueryPipe", "GetAllDomainComments");
		belongsTo.addInstruction("BindSubComment", "ownerNodeUUID", RATConstants.VertexContentUndefined, ReturnType.uuid);
		
		SystemKeyNode isPublic = this.buildNode(SystemKeyNode.class, "is-public");
		isPublic.addCreateVertexInstruction("nodeName", "is-public", ReturnType.string);
		
		SystemKeyNode isUniversal = this.buildNode(SystemKeyNode.class, "is-universal");
		isUniversal.addCreateVertexInstruction("nodeName", "is-universal", ReturnType.string);
		
		//SystemKeyNode content = this.buildNode(SystemKeyNode.class, "inputFromUser");
		//content.addCreateVertexInstruction("inputFromUser", "inputFromUser", ReturnType.string);
		//content.addInstruction("SetVertexProperty", RATConstants.VertexContentField, RATConstants.VertexContentUndefined, ReturnType.string);
		
		// TODO: per il momento non uso SetVertexProperty per le istruzioni di seguito,  
		// se lo facessi, per ora non potrei più distinguere i parametri tra di loro (vedi AddRootDomainTest.setAddCommentsValues).
		// Da risolvere
		//SystemKeyNode start = this.buildNode(SystemKeyNode.class, "start");
		//start.addCreateVertexInstruction("start", RATConstants.VertexContentUndefined, ReturnType.integer);
		
		//SystemKeyNode end = this.buildNode(SystemKeyNode.class, "end");
		//end.addCreateVertexInstruction("end", RATConstants.VertexContentUndefined, ReturnType.integer);
		
		SystemKeyNode webDoc = this.buildNode(SystemKeyNode.class, "webDocument");
		webDoc.addInstruction("CreateWebDocument", "url", RATConstants.VertexContentUndefined, ReturnType.url);
		//webDoc.addBindInstruction("userNodeUUID", RATConstants.VertexContentUndefined);
		
		SystemKeyNode isLinkedTo = this.buildNode(SystemKeyNode.class, "is-linked-to");
		isLinkedTo.addCreateVertexInstruction("nodeName", "is-linked-to", ReturnType.string);
		isLinkedTo.addBindInstruction("userNodeUUID", RATConstants.VertexContentUndefined);
		webDoc.addChild(isLinkedTo);
		
		rootNode.addChild(isPutByNode);
		rootNode.addChild(belongsTo);
		rootNode.addChild(isPublic);
		rootNode.addChild(isUniversal);
		rootNode.addChild(webDoc);
		
		//content.addChild(start);
		//content.addChild(end);
		//content.addChild(urlDoc);
		
		this.setQueryPivot(webDoc, "GetUserURLs", "StartQueryPipe", true);
		this.setQueryPivot(webDoc, "GetUserURLs", "SetQueryPipe", false);
		this.setQueryPivot(webDoc, "GetUserURLs", "GetAllNodesByType", false);
		
		this.setQueryPivot(isPutByNode, "GetAllUserComments", "StartQueryPipe", true);
		this.setQueryPivot(isPutByNode, "GetAllUserComments", "SetQueryPipe", false);
		this.setQueryPivot(rootNode, "GetAllUserComments", "GetAllUserComments", false);
		
		this.setQueryPivot(belongsTo, "GetAllDomainComments", "StartQueryPipe", true);
		this.setQueryPivot(belongsTo, "GetAllDomainComments", "SetQueryPipe", false);
		this.setQueryPivot(rootNode, "GetAllDomainComments", "GetWebDoc", false, "url");
		// TODO: nel parametro finale aggiungere anche il tipo, altrimenti è sempre string!
		this.setQueryPivot(isPutByNode, "GetAllDomainComments", "GetAllDomainComments", false, "userUUID");
		
		this.setQueryPivot(belongsTo, "GetCommentComments", "StartQueryPipe", true);
		this.setQueryPivot(belongsTo, "GetCommentComments", "SetQueryPipe", false);
		this.setQueryPivot(rootNode, "GetCommentComments", "GetCommentComments", false);
	}
}
