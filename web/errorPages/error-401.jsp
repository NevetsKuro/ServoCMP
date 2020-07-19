<html>
    <head>
        <title>401 Error - ServoCMP</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div style="text-align: center; margin-top: 15%; color: red">
            <h2 style="text-align: center">Unauthorized Access.</h2>
            <div>
                <h3>You are not Authorized to Access this page.</h3>
                <h3>Kindly Login and Try again.</h3>
                <h3>OR</h3>
                <h3>Contact WRO IS Department for Assistance.</h3>
                <span>Possible Reason: </span><br/>
                <span style="color: red">${errorMsg}</span>
                <h6><a href="${pageContext.request.contextPath}">Click Here to Continue......</a></h6>
                <span>OR</span>
                <h6><a href="logout">Click Here to Logout.........</a></h6>
            </div>
        </div>
    </body>
</html>
