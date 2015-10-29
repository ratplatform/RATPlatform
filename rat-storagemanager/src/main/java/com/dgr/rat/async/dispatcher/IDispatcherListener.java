/**
 * @author Daniele Grignani (dgr)
 * @date Jun 3, 2015
 */

package com.dgr.rat.async.dispatcher;


public interface IDispatcherListener<V> {
	void onReceive(V message);
}
