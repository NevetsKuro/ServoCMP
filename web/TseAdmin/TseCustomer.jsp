<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Customer"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">Manage "${param.empName}'s" (${param.empCode}) Customer(s)</h4>
            <div class="container-fluid" style="width: 80%; margin: 0 auto">
                <table id="tseCustomer" class="table-bordered table-condensed table table-hover table-responsive table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Industry Name</th>
                            <th>Customer Name</th>
                            <th>Tse Emp Code</th>
                            <th>Tse Emp Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${mCust}" var="data">
                            <tr>
                                <td></td>
                                <td class="NevsLeft">${data.mstIndustry.indName}</td>
                                <td class="NevsLeft">${data.customerName}</td>
                                <td><a href="#" onclick="openHelloIOCian(${data.mstUser.empCode})">${data.mstUser.empCode}</a></td>
                                <td class="NevsLeft"><a href="${pageContext.request.contextPath}/TseAdmin/TseCustomer?empCode=${data.mstUser.empCode}&empName=${data.mstUser.empName}&csrftoken=${sessionScope.csrfToken}">${data.mstUser.empName}</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
     <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>