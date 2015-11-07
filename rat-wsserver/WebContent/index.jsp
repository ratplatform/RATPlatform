<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ page import="com.dgr.rat.webservices.RATWebServicesContextListener" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>RATWSServer connection status</title>
    </head>
    <body>
		<%
		response.setIntHeader("Refresh", 5);
		
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
		%>
        <form method="post" action="index">
            <center>
            <table border="1" width="30%" cellpadding="3">
                <thead>
                    <tr>
                        <th colspan="3">RATWSServer connection status</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Sent: <%= keepAliveSent %></td>
                        <td>Received: <%= keepAliveReceived %></td>
                        <td>Status: <%= keepAliveStatus %></td>
                    </tr>
                </tbody>
            </table>
            </center>
        </form>
    </body>
</html>