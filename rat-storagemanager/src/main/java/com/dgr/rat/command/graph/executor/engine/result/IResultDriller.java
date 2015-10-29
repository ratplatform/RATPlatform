/**
 * @author Daniele Grignani (dgr)
 * @date Oct 25, 2015
 */

package com.dgr.rat.command.graph.executor.engine.result;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public interface IResultDriller {
	public void drill(CommandResponse result) throws Exception;
	public Map<String, Object> getResult();
	public Object getProperty(String key);
	public Set<String>getPropertyKeys();
	public Iterator<String>getKeyIterator();
}
