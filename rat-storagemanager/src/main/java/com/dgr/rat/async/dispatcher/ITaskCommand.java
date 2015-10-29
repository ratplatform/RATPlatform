/**
 * @author Daniele Grignani (dgr)
 * @date Jun 4, 2015
 */

package com.dgr.rat.async.dispatcher;

public interface ITaskCommand<V> {
	public V execute();
}
