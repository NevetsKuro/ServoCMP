<%-- 
    Document   : myerror
    Created on : Dec 12, 2017, 12:56:36 PM
    Author     : wrtrg1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page errorPage="/errorPages/error.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        Null pointer is generated below:
        <%
            String s = null;
            s.length();
        %>
    </body>
</html>
