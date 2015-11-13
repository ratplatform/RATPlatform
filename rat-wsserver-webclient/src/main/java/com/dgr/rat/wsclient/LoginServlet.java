package com.dgr.rat.wsclient;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.login.json.LoginResponse;

/**
 * Servlet implementation class Index
 */
@WebServlet(name="LoginServlet", urlPatterns={"/login"})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");
	    String wsURL = request.getParameter("wsurl");

	    LoginHelper loginHelper = new LoginHelper(wsURL);
	    LoginResponse wsResponse = loginHelper.login(email, password);
	    StatusCode status = StatusCode.fromString(wsResponse.getStatusCode().toString());
	    if(status.equals(StatusCode.Ok)){
	    	String path = this.getServletContext().getContextPath() + "/domains";
	        HttpSession session = request.getSession(false);
	        
	        session.setAttribute("wsResponse", wsResponse);
	        session.setAttribute("wsURL", wsURL);
	        session.setAttribute("sessionID", wsResponse.get_sessionID());
	        session.setAttribute("userUUID", wsResponse.get_userUIID());
	        
	    	response.sendRedirect(path);
	    }
	    else{
	    	request.setAttribute("error", status);
	    	request.getRequestDispatcher("error.jsp").forward(request, response);
	    }
	}

}
