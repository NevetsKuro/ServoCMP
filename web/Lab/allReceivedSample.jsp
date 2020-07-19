<%-- 
    Document   : receivedSample
    Created on : 5 Jan, 2017, 7:29:18 PM
    Author     : Manish Jangir
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Sample(s) Received for Testing"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="text-center">
            <h4 class="text-center text-info text-primary">Sample(s) Received for Testing</h4>
            <label class="text-info"><u>PRIORITY</u>&nbsp; &rarr;&nbsp; </label>
            <span class="badge priorityLevel3">NORMAL</span>
            <span class="badge priorityLevel2">MEDIUM</span>
            <span class="badge priorityLevel1">HIGH</span>
        </div>
        <div class="container-fluid body-content">
            <table id="testSample" class="table table-bordered tbCenter-head" cellspacing="0" width="100%">
                <thead style="font-size: x-small">
                <th>SAMPLE ID</th>
                <th>CUSTOMER NAME</th>
                <th>DEPARTMENT</th>
                <th>APPLICATION</th>
                <th>TANK NO</th>
                <th>EQUIPMENT</th>
                <th>PRODUCT</th>
                <th>QTY DRAWN(Ltr.)</th>
                <th>DRAWN ON</th>
                <th>RECEIVED ON</th>
                </thead>
                <tbody>
                <c:forEach items="${receivedSampleDetails}" var="data" varStatus="count">
                    <tr style="font-size:small" class="priorityLevel${data.samplepriorityId} cursorOnHover">
                        <td class="text-center">
                            <a href="${pageContext.request.contextPath}/Lab/GetTestSample?smplid=${data.sampleId}&labCode=${data.mstLab.labCode}&status=${data.statusId}&csrftoken=${sessionScope.csrfToken}"> ${data.sampleId}</a>
                        </td>
                        <td class="NevsLeft">${data.mstDept.mstCustomer.customerName}</td>
                        <td class="NevsLeft">${data.mstDept.departmentName}</td>
                        <td class="NevsLeft">${data.mstApp.appName}</td>
                        <td class="text-center">${data.tankNo}</td>
                        <td class="NevsLeft">${data.mstEquip.equipmentName}</td>
                        <td class="NevsLeft">${data.mstProd.proName}</td>
                        <td class="text-center">${data.qtyDrawn}</td>
                        <td class="text-center">${data.stringsamplecreatedDate}</td>
                        <td class="text-center">${data.stringLabrecDate}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
