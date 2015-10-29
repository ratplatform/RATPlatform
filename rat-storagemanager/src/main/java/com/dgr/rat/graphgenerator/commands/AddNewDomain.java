/**
 * @author Daniele Grignani (dgr)
 * @date Sep 18, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.graphgenerator.node.wrappers.NewDomainNode;
import com.dgr.rat.graphgenerator.node.wrappers.SystemKeyNode;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.json.utils.VertexType;

public class AddNewDomain extends AbstractCommand{

	public AddNewDomain(String commandName, String commandVersion) {
		super(commandName, commandVersion);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.graphgenerator.commands.AbstractCommand#addNodesToGraph()
	 */
	@Override
	public void addNodesToGraph() throws Exception {
		NewDomainNode rootNode = this.buildRootNode(NewDomainNode.class, VertexType.Domain.toString());
		rootNode.addCreateCommandRootVertexInstruction("nodeName", rootNode.getType().toString(), ReturnType.string);
		// COMMENT: essenziale che questo comando venga posto subito dopo CreateVertex
		// Inoltre paramName e paramValue possono contenere qualuque cosa purché il secondo non sia VertexContentUndefined
		// altrimenti il comando viene inserito tra quelli di cui l'utente deve valorizzare i parametri
		rootNode.addInstruction("InitDomain", RATConstants.VertexContentField, "#", ReturnType.string);
		
		rootNode.addInstruction("SetVertexProperty", RATConstants.VertexLabelField, RATConstants.VertexContentUndefined, ReturnType.string);
		rootNode.addInstruction("SetVertexProperty", RATConstants.VertexContentField, RATConstants.VertexContentUndefined, ReturnType.string);
		
		SystemKeyNode isDomainNode = this.buildNode(SystemKeyNode.class,  "is-domain");
		isDomainNode.addCreateVertexInstruction("nodeName", "is-domain", ReturnType.string);
		
		SystemKeyNode isPutByNode = this.buildNode(SystemKeyNode.class,  "is-put-by");
		isPutByNode.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		
		SystemKeyNode isPutByNode2 = this.buildNode(SystemKeyNode.class,  "is-put-by");
		isPutByNode2.addCreateVertexInstruction("nodeName", "is-put-by", ReturnType.string);
		// COMMENT: Bind to RootDomain RAT
		isPutByNode2.addBindInstruction(RATConstants.VertexContentUndefined);
		this.setQueryPivot(isPutByNode2, rootNode.getType(), VertexType.RootDomain, "StartQueryPipe", "SetQueryPipe", "GetAllDomains");
		
//		// TODO: correggere in seconda battuta: ho il problema che se ho diversi nomi di parametri uguali,
//		// quando setto i parametri non so quale scegliere
//		SystemKeyNode placeHolderNode = this.buildNode(SystemKeyNode.class, VertexType.Placeholder.toString());
//		placeHolderNode.set_nodeContent(VertexType.RootDomain.toString());
		
		rootNode.addChild(isDomainNode);
		rootNode.addChild(isPutByNode2);
		isDomainNode.addChild(isPutByNode);
//		isPutByNode2.addChild(placeHolderNode);
	}
}
