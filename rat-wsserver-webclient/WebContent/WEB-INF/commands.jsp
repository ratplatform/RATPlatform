<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ page import="com.dgr.rat.login.json.ChooseDomainResponse" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Commands</title>
</head>
<body>
<%
String domain = null;
String sessionID = null;
String userName = null;
String[] roles = null;

HttpSession httpSession = request.getSession(false);
Object attribute = httpSession.getAttribute("wsResponse");
//session.removeAttribute("wsResponse");
if(attribute != null){
	ChooseDomainResponse wsResponse = (ChooseDomainResponse)attribute;
	domain = wsResponse.getDomainName();
	sessionID = wsResponse.get_sessionID();
	roles = wsResponse.getDomainRoles();
	userName = wsResponse.getUserName();
}
%>
<form action="commands" method="post">
	<table>
	
	<tr>
	<td>
	<p>Domain: <%= domain %></p>
	</td>
	</tr>
	
	<tr>
	<td>
	<p>sessionID: <%= sessionID %></p>
	</td>
	</tr>
	
	<tr>
	<td>
	<p>userName: <%= userName %></p>
	</td>
	</tr>
	
	<tr>
	<td>
	<select name="roleSelect" id="roleSelectID">
	<%
		if(roles != null){
			for(String role : roles){
	%>
				<option value="<%= role %>"><%= role %></option>
	<%
			}
		}
	%>
	</select>
	</td>
	</tr>

	<tr>
	<td>
	<select name="commandSelect" id="commandSelectID">
	<option value="createcollaborationdomain">createcollaborationdomain</option>
	<option value="createnewuser">createnewuser</option>
	<option value="createnewuser">comment</option>
	</select>
	</td>
	</tr>
	
	<tr>
	<td>
	<input type="text" name="newDomainName" value="TestDomain3" />
	</td>
	</tr>
	
	<tr>
	<td>
	<input type="submit" value="createDomainButton" />
	</td>
	</tr>
	
	</table>
</form>
</body>
</html>