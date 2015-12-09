/**
 * @author: Daniele Grignani
 * @date: Dec 6, 2015
 */

package com.dgr.rat.messages;

import javax.servlet.ServletContext;
import javax.ws.rs.container.AsyncResponse;

/**
 * @author dgr
 *
 */
public interface IMessageSender {
	public void sendMessage(final AsyncResponse asyncResponse, final ServletContext servletContext, final String sessionID, final String data);
}
