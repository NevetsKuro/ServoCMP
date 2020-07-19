<%-- 
    Document   : error
    Created on : 17 Oct, 2016, 11:41:18 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div align='center'>
            <h3>Although You have valid Central Login Credentials. There was Problem Connecting to Database.</h3>
            <div class="text-center">
                <img src="resources/images/dbError.jpg"/>
            </div>
            <span>Possible Reason: </span><br/>
            <span style="color: red">${errorMsg}</span>
            <h4><a href="${pageContext.request.contextPath}">Click Here to Retry......</a></h4>
            <span>OR</span>
            <h6><a href="logout">Click Here to Logout.........</a></h6>
        </div>
    </body>
</html>
