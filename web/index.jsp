<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/UItemplate/header.jsp">
    <jsp:param name="title" value="Welcome to SERVOCMP"/>
</jsp:include>
<body>
    <c:if test="${sUser.role_id ne 5}">
        <link href="${pageContext.request.contextPath}/resources/css/material-dashboard.min.css" rel="stylesheet" type="text/css"/>
    </c:if>
    <!--<script src="${pageContext.request.contextPath}/resources/js/highchart.js" type="text/javascript"></script>-->
    <!--<script src="https://code.highcharts.com/highcharts.src.js"></script>-->
    <script src="${pageContext.request.contextPath}/resources/js/highcharts.src.js"></script>
    <!--<script src="${pageContext.request.contextPath}/resources/js/data.js" type="text/javascript"></script>-->
    <script src="${pageContext.request.contextPath}/resources/js/drilldown.js" type="text/javascript"></script>
    <c:if test="${sUser.role_id eq 1}">
        <%@include file="/Tse/TseHome.jsp" %>
    </c:if>
    <c:if test="${sUser.role_id eq 2}">
        <%@include file="/Lab/LabHome.jsp" %>
    </c:if>
    <c:if test="${sUser.role_id eq 3}">
        <%@include file="/TseAdmin/TseAdminHome.jsp" %>
    </c:if>
    <c:if test="${sUser.role_id eq 4}">
        <%@include file="/LabAdmin/LabAdminHome.jsp" %>
    </c:if>
    <c:if test="${sUser.role_id eq 5}">
        <%@include file="/ISAdmin/universalLogged.jsp" %>
    </c:if>
</body>
<jsp:include page="/UItemplate/footer.jsp"></jsp:include>