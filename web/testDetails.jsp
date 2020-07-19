<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:include page="/UItemplate/header.jsp">
    <jsp:param name="title" value="Quantity Required for Testing."/>
</jsp:include>
<body>
    <div class="container-fluid">
        <h4 class="text-center text-info" style="text-decoration: underline">Quantity Required for Testing</h4>
        <div style="width: 50%; margin: 0 auto">
            <table class="table table-fixed table-condensed table-responsive table-striped table-bordered">
                <thead>
                    <tr>
                        <th class="col-xs-2">#</th>
                        <th class="col-xs-8">Test Name</th>
                        <th class="col-xs-2">Sample Quantity Required (in ml)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${testMaster}" var="data">
                        <tr>
                            <td class="col-xs-2"><input type="checkbox" class="checkbox chkclass"/></td>
                            <td class="col-xs-8">${data.testName}</td>
                            <td class="col-xs-2 testQty text-right">${data.sampleqty}</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr style="font-weight: bolder" class="text-right">
                        <td></td>
                        <td>Total Quantity Required for Selected Tests</td>
                        <td id="totalQuantity" class="text-info">0 (ml)</td>
                    </tr>
                </tfoot>
            </table>
        </div>

    </div>
</body>
<%@include file="UItemplate/footer.jsp" %>