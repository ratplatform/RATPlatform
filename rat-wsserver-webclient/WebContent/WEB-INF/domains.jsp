<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%@ page import="com.dgr.rat.login.json.LoginResponse" %>
    <%@ page import="java.util.Map" %>
    <%@ page import="java.util.Iterator" %>

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
session.removeAttribute("wsResponse");

String user = null;
String sessionID = null;
Map<String, String> userDomains = null;

if(attribute != null){
	LoginResponse loginResponse = (LoginResponse)attribute;
	
	user = loginResponse.getUserName();
	sessionID = loginResponse.get_sessionID();
	userDomains = loginResponse.getUserDomains();
}
%>

<form name="submitForm" method="POST">
            <center>
            <table border="1" width="30%" cellpadding="3">
                <thead>
                    <tr>
                        <th colspan="2">Login Result</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>User</td>
                        <td><%=user%></td>
                    </tr>
                    <tr>
                        <td>SessionID</td>
                        <td><%=sessionID%></td>
                    </tr>
                    <tr>
						<%
							if(userDomains != null){
								Iterator<String>it = userDomains.keySet().iterator();
								while(it.hasNext()){
									String domain = it.next();
									String domainUUID = userDomains.get(domain);
						%>
									<td>Domain</td>
									<td><a href="http://localhost:8080/RATWebClient/domain?domainName=<%=domain%>?domainUUID=<%=domainUUID%>"><%=domain%></a></td> 
						<%
								}
							}
						%>
                    </tr>

                </tbody>
            </table>
            </center>
</form>

</body>
</html>