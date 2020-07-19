<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>403 Error - ServoCMP</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div style="text-align: center; margin-top: 20%; color: red">
            <h2 style="text-align: center">Unauthorized Access.</h2>
            <div>
                <h3>The Page or Resource You are trying to access is forbidden</h3>
                <h3>Contact WRO IS Department for Assistance.</h3>
            </div>
            <h6><a href="${pageContext.request.contextPath}">Click Here to Continue......</a></h6>
            <span>OR</span>
            <h6><a href="logout">Click Here to Logout.........</a></h6>
        </div>
    </body>
</html>