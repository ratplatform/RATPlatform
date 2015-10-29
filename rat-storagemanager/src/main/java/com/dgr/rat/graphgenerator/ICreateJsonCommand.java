/**
 * @author Daniele Grignani (dgr)
 * @date Sep 6, 2015
 */

package com.dgr.rat.graphgenerator;

import com.dgr.rat.graphgenerator.commands.ICommandCreator;

public interface ICreateJsonCommand {

	public abstract String makeRemoteRequest(ICommandCreator command) throws Exception;

}