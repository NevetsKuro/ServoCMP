<%-- 
    Document   : viewSendToLABSummary
    Created on : 30 Oct, 2017, 11:45:58 AM
    Author     : 00507469
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Sample Sent to Lab for Testing. or View IOM."/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="text-center container-fluid">
            <a id='selectionProcess' style="float: left; cursor: pointer;">How to Select?</a>
            <label class="text-info"><u>PRIORITY</u>&nbsp; &rarr;&nbsp; </label>
            <span class="badge priorityLevel3">NORMAL</span>
            <span class="badge priorityLevel2">MEDIUM</span>
            <span class="badge priorityLevel1">HIGH</span>
        </div>
        <div class="container-fluid">
            <h4 class="text-center text-info" style="text-decoration: underline">Samples Sent to Lab Summary</h4>
            <table id="IomTable" class="table table-bordered" cellspacing="0" width="100%">
                <thead style="font-size: x-small">
                <th></th>
                <th>SAMPLE ID</th>
                <th>CUSTOMER NAME</th>
                <th>DEPARTMENT</th>
                <th>APPLICATION</th>
                <th>TANK NO</th>
                <th>EQUIPMENT</th>
                <th>PRODUCT</th>
                <th>QTY DRAWN(ml.)</th>
                <th>DRAWN DATE</th>
                <th>LAB NAME</th>
                </thead>
                <tbody>
                    <c:forEach items="${InProcessTseSampleDetails}" var="data" varStatus="count">
                        <tr style="font-size:small" class="priorityLevel${data.samplepriorityId}">
                            <td class="select-checkbox"></td>
                            <td class="text-center">${data.sampleId}</td>
                            <td class="text-center">${data.mstDept.mstCustomer.customerName}</td>
                            <td class="text-center">${data.mstDept.departmentName}</td>
                            <td class="text-center">${data.mstApp.appName}</td>
                            <td class="text-center">${data.tankNo}</td>
                            <td class="text-center">${data.mstEquip.equipmentName}</td>
                            <td class="text-center">${data.mstProd.proName}</td>
                            <td class="text-center">${data.qtyDrawn}</td>
                            <td class="text-center">${data.stringsamplecreatedDate}</td>
                            <td class="text-center">${data.mstLab.labName}</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot style="font-size: x-small">
                <th></th>
                <th>SAMPLE ID</th>
                <th>CUSTOMER NAME</th>
                <th>DEPARTMENT</th>
                <th>APPLICATION</th>
                <th>TANK NO</th>
                <th>EQUIPMENT</th>
                <th>PRODUCT</th>
                <th>QTY DRAWN(ml.)</th>
                <th>DRAWN DATE</th>
                <th>LAB NAME</th>
                </tfoot>
            </table>
            <div class="text-center">
                <form id="iomForm" action="${pageContext.request.contextPath}/Tse/GetIOMSummaryData" method="post">
                    <input type="hidden" id="jsonSampleIds" name="sampleIds" value="">
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    <input type="button" class="btn btn-primary" value="View IOM" id="viewIOM">
                    <input type="button" class="btn btn-primary" value="View Sample" id="viewSample"/>
                    <a href="${pageContext.request.contextPath}/Tse/GetUpdateSample?smplid=${data.sampleId}&status=${data.statusId}" title="Click to edit Sample details sent to Lab"></a>
                </form>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
