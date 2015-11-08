/**
 * @author: Daniele Grignani
 * @date: Nov 8, 2015
 */

package com.rat.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgr.rat.webservices.RATWebServicesContextListener;

public class Monitor extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public Monitor() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String keepAliveSent = "?";
		String keepAliveReceived = "?";
		String keepAliveStatus = "?";
		
		Object obj = getServletContext().getAttribute(RATWebServicesContextListener.KeepAliveSent);
		if(obj != null){
			keepAliveSent = obj.toString();
		}
		
		obj = getServletContext().getAttribute(RATWebServicesContextListener.KeepAliveReceived);
		if(obj != null){
			keepAliveReceived = obj.toString();
		}
		
		obj = getServletContext().getAttribute(RATWebServicesContextListener.KeepAliveStatus);
		if(obj != null){
			keepAliveStatus = obj.toString();
		}
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html;charset=UTF-8");
		response.setIntHeader("Refresh", 5);
		
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Monitor</title>");           
        out.println("</head>");
        out.println("<body>");
        
        out.println("<p>KeepAlive sent: " + keepAliveSent + "</p>");
        out.println("<p>KeepAlive received: " + keepAliveReceived + "</p>");
        out.println("<p>KeepAlive status: " + keepAliveStatus + "</p>");
        
        out.println("</body>");
        out.println("</html>");
	}
}
