<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Order of Test(s)"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="text-center">Order of Appearance of Test(s)</h4>
            <div style="width: 80%; margin-left: auto; margin-right: auto">
                <table id="TestSequence" class="table table-bordered table-condensed table-responsive table-striped">
                    <thead>
                        <tr>
                            <th style="width: 10%" name="testOrder">Test Order</th>
                            <th>Test Name</th>
                            <th>Unit</th>
                            <th>Test Method</th>
                            <th>Qty of Sample</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${mstTest}" var="data">
                            <tr>
                                <td class="text-center">${data.dispSeqNo}</td>
                                <td>${data.testName}</td>
                                <td>${data.unit}</td>
                                <td>${data.testMethod}</td>
                                <td class="text-right">${data.sampleqty}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <br/>
                <input type="button" value="Save Sequence" onclick="saveSequence();" class="btn btn-primary centre-btn" style="margin-left: auto; margin-right: auto"/>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>