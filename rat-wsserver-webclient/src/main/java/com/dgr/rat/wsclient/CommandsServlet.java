package com.dgr.rat.wsclient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dgr.rat.login.json.ChooseDomainResponse;
import com.dgr.rat.login.json.LoginResponse;

/**
 * Servlet implementation class CommandsServlet
 */

@WebServlet(name="CommandsServlet", urlPatterns={"/commands"})
public class CommandsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommandsServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String command = request.getParameter("command");
//		
//		if(command == null){
//			
//		}
//		else if(command.equals("")){
//			
//		}
		request.getRequestDispatcher("commands.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String roleSelected = request.getParameter("roleSelect");
		String commandSelected = request.getParameter("commandSelect");
		String newDomainName = request.getParameter("newDomainName");
		
		if(roleSelected == null || commandSelected == null || newDomainName == null){
			throw new ServletException();
		}
		
		HttpSession session = request.getSession(false);
       
		Object attribute = session.getAttribute("wsResponse");
		if(attribute == null){
			throw new ServletException();
		}
		ChooseDomainResponse wsResponse = (ChooseDomainResponse)attribute;
       
		attribute = session.getAttribute("wsURL");
		if(attribute == null){
			throw new ServletException();
		}
		String wsURL = (String)attribute;
		LoginHelper loginHelper = new LoginHelper(wsURL);
		try {
			loginHelper.createDomain(wsResponse, newDomainName, roleSelected);
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
