package com.dgr.rat.wsclient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dgr.rat.login.json.ChooseDomainResponse;
import com.dgr.rat.login.json.LoginResponse;

/**
 * Servlet implementation class DomainServlet
 */
//@WebServlet("/DomainServlet")
@WebServlet(name="DomainServlet", urlPatterns={"/domain"})
public class DomainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DomainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("domain.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("domainSelect") != null){
		   String selectedDomain = request.getParameter("domainSelect");
		   //System.out.println(selectedDomain);
		   
	       HttpSession session = request.getSession(false);
	       Object attribute = session.getAttribute("wsResponse");
	       LoginResponse loginResponse = null;
	       String wsURL = null;
	       
	       if(attribute != null){
	    	   loginResponse = (LoginResponse)attribute;
	       }
	       
	       attribute = session.getAttribute("wsURL");
	       if(attribute != null){
	    	   wsURL = (String)attribute;
	       }
	       
	       if(wsURL != null && loginResponse != null){
	    	   LoginHelper loginHelper = new LoginHelper(wsURL);
	    	   try {
	    		   ChooseDomainResponse wsResponse = loginHelper.chooseDomain(loginResponse, selectedDomain);
	    		   String status = wsResponse.getStatusResponse();
	    		    if(status.equals("200")){
	    		    	String path = this.getServletContext().getContextPath() + "/commands";
	    		    	session.removeAttribute("wsResponse");
	    		        session.setAttribute("wsResponse", wsResponse);
	    		        session.setAttribute("wsURL", wsURL);
	    		        
	    		    	response.sendRedirect(path);
	    		    }
	    		    else{
	    		    	request.setAttribute("error", status);
	    		    	request.getRequestDispatcher("error.jsp").forward(request, response);
	    		    }
	    	   } 
	    	   catch (Exception e) {
	    		   // TODO Auto-generated catch block
	    		   e.printStackTrace();
	    		   throw new ServletException(e);
	    	   }
	       }
		}
	}

}
