<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="com.dgr.rat.login.json.LoginResponse" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Domains</title>
</head>
<body>
<%
String[] domains = null;

HttpSession httpSession = request.getSession(false);
Object attribute = httpSession.getAttribute("wsResponse");
//session.removeAttribute("wsResponse");
if(attribute != null){
	LoginResponse loginResponse = (LoginResponse)attribute;
	domains = loginResponse.getUserDomains();
}
%>

<form action="domain" method="post">
<select name="domainSelect" id="domainsID">
<%
	if(domains != null){
		for(String domain : domains){
%>
			<option value="<%= domain %>"><%= domain %></option>
<%
		}
	}
%>
</select>

<input type="submit" value="domainButton" />

</form>

</body>
</html>