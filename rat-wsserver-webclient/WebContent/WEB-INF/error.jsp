<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error</title>
</head>
<body>
<%
    Object param = request.getAttribute("error");
    request.removeAttribute("error");
    
    if (param == null){
%>
    <p>No error given to this page.</p>
<%
    }
    else{
%>
    <p>The value of error is <%= param.toString() %>.</p>
<%
    }
%>
</body>
</html>