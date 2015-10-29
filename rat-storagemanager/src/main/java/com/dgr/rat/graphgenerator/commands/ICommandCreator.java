/**
 * @author Daniele Grignani (dgr)
 * @date Oct 18, 2015
 */

package com.dgr.rat.graphgenerator.commands;

import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.commons.constants.JSONType;
import com.tinkerpop.blueprints.Graph;

/**
 * 
 */
public interface ICommandCreator {

	public abstract Graph getGraph();

	/**
	 * @return the _commandUUID
	 */
	public abstract UUID get_commandUUID();

	/**
	 * @return the _commandName
	 */
	public abstract String get_commandName();

	/**
	 * @return the _commandType
	 */
	public abstract JSONType get_commandType();

	/**
	 * @return the _rootNodeUUID
	 */
	public abstract UUID get_rootNodeUUID();

	/**
	 * @return the _rootNode
	 */
	public abstract IRATNodeFrame get_rootNode();

	/**
	 * @return the _commandVersion
	 */
	public abstract String get_commandVersion();

}