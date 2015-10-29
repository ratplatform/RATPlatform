/**
 * @author Daniele Grignani (dgr)
 * @date Oct 19, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.lang.reflect.Constructor;


public class InstructionBuilder implements IInstructionBuilder{
	private String _packageName = null;
	public InstructionBuilder(String packageName) {
		_packageName = packageName;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.IInstructionBuilder#buildInstruction(java.lang.String)
	 */
	@Override
	public IInstruction buildInstruction(String actionName) throws Exception {
		IInstruction command = null;
		try{
			int pos = _packageName.lastIndexOf(".");
			int size = _packageName.length();
			if(size == pos + 1){
				_packageName = _packageName.substring(0, pos);
			}

			String className = _packageName + "." + actionName;
			Class<?> cls = Class.forName(className);
			Class<?> argTypes[] = { };
	        Constructor<?> ct = cls.getConstructor(argTypes);
	        Object arglist[] = { };
	        Object object = ct.newInstance(arglist);
	        command = (IInstruction) object;
		}
        catch(Exception e){
        	e.printStackTrace();
        	throw new Exception(e);
        }
		
        return command;
	}

}
