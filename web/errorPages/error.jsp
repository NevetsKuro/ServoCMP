<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../UItemplate/header.jsp" %>
<body>
    <div class="text-center">
        <h2>Something is not Right here.....</h2>
        <h4 class="text-danger">${errorMsg}</h4>
        <h6><a href="${pageContext.request.contextPath}">Click Here to Continue......</a></h6>
        <span>OR</span>
        <h6><a href="logout">Click Here to Logout.........</a></h6>
    </div>
</body>
<%@include file="../UItemplate/footer.jsp" %>