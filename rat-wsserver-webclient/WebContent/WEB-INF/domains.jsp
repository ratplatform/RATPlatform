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
<script src="js/jquery-1.8.2.min.js"></script>  
<script src="js/queries.js"></script>  
<script src="js/query.js"></script>  
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

String wsURL = null;
attribute = httpSession.getAttribute("wsurl");
if(attribute != null){
	wsURL = attribute.toString();
}

String userUUID = null;
attribute = httpSession.getAttribute("userUUID");
if(attribute != null){
	userUUID = attribute.toString();
}
%>

<form name="submitForm" method="POST">
<table border="1">
<tr>
<td>
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
									<td><a href="<%=wsURL%>/domain?domainName=<%=domain%>?domainUUID=<%=domainUUID%>"><%=domain%></a></td> 
						<%
								}
							}
						%>
                    </tr>

                </tbody>
            </table>
            </td>
            <td>
            <script>
            var queryCallbackSuccess = function(data, textStatus, jqXHR){
            	console.log("SUCCESS");
            	console.log("data: " + data);
            	console.log("textStatus: " + textStatus);
            	console.log("jqXHR: " + jqXHR);
            };

            var queryCallbackError = function(jqXHR, textStatus, errorThrown){
            	console.log("ERROR");
            	console.log("jqXHR: " + jqXHR);
            	console.log("textStatus: " + textStatus);
            	console.log("errorThrown: " + errorThrown);
            };
            
            var url = "<%=wsURL%>/v0.1/query?sessionid=<%=sessionID%>";
            console.log("userUUID: " + "<%=userUUID%>");
            console.log("url: " + url);
            
            GetAllUserDomains.settings.rootNodeUUID.setVertexInstructionParameterValueField("<%=userUUID%>");
            console.log("GetAllUserDomains: " + JSON.stringify(GetAllUserDomains));
            runQuery(url, GetAllUserDomains, queryCallbackSuccess, queryCallbackError);
            
			//document.getElementById("demo").innerHTML = JSON.stringify(GetAllUserDomains);
			</script>
            </td>
            </tr>
            </table>
</form>

</body>
</html>