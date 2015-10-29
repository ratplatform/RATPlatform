package com.dgr.rat.wsclient;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dgr.rat.login.json.LoginResponse;

/**
 * Servlet implementation class Index
 */
//@WebServlet("/Index")
@WebServlet(name="IndexServel", urlPatterns={"/index"})
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
        super();
        System.out.println("IndexServlet");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet");
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost");
		
	    String userName = request.getParameter("username");
	    String password = request.getParameter("password");
	    String wsURL = request.getParameter("wsurl");
	    
	    LoginHelper loginHelper = new LoginHelper(wsURL);
	    LoginResponse wsResponse = loginHelper.login(userName, password);
	    String status = wsResponse.getStatusResponse();
	    if(status.equals("200")){
	    	String path = this.getServletContext().getContextPath() + "/domain";
	        HttpSession session = request.getSession(false);
	        session.setAttribute("wsResponse", wsResponse);
	        session.setAttribute("wsURL", wsURL);
	        
	    	response.sendRedirect(path);
	    }
	    else{
	    	request.setAttribute("error", status);
	    	request.getRequestDispatcher("error.jsp").forward(request, response);
	    }
	}

}
