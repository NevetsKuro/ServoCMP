<%-- 
    Document   : footer
    Created on : 9 Oct, 2016, 4:49:20 PM
    Author     : itsma
--%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="year" value="${now}" pattern="yyyy" />
<footer>
    <hr style="border-bottom: 1px solid gray"/>
    <p class="container-fluid">Copyright &copy; ${year} indianoil.co.in. All Rights Reserved.</p>
</footer>
