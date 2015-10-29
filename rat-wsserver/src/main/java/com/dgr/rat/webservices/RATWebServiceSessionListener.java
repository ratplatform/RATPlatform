/*
 * @author Daniele Grignani
 * Apr 2, 2015
*/

package com.dgr.rat.webservices;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class RATWebServiceSessionListener implements HttpSessionListener{

	public RATWebServiceSessionListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		System.out.println("RATWebServiceSessionListener.sessionCreated ID: " + session.getId());
		System.out.println(arg0.getSession().getMaxInactiveInterval());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		System.out.println("RATWebServiceSessionListener.sessionDestroyed ID: " + session.getId());
	}
}
