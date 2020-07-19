<%-- 
    Document   : acceptSmpl
    Created on : 3 Nov, 2016, 10:34:54 AM
    Author     : Manish Jangir
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Sample(s) available for Acknowledgement"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="text-center">
            <h4 class="text-center text-info text-primary">Sample(s) available for Acknowledgement</h4>
            <label class="text-info"><u>PRIORITY</u>&nbsp; &rarr;&nbsp; </label>
            <span class="badge priorityLevel3">NORMAL</span>
            <span class="badge priorityLevel2">MEDIUM</span>
            <span class="badge priorityLevel1">HIGH</span>
            <span class="spacespace"></span>
            <label class="text-info"><u>One Time Sampling Type</u>&nbsp; &rarr;&nbsp; </label>
            <span class="defFont" style='color:mediumvioletred'><i class='glyphicon glyphicon-file'></i></span>
            <span class="spacespace"></span>
            <label class="text-info"><u>CMP Sampling Type</u>&nbsp; &rarr;&nbsp; </label>
            <span class="defFont" style='color:royalblue'><i class='glyphicon glyphicon-book'></i></span>
        </div>
        <div class="container-fluid body-content">
            <table id="receiveSample" class="table table-bordered tbCenter-head" cellspacing="0" width="100%">
                <thead style="font-size: x-small">
                <th>SAMPLING TYPE</th>
                <th>SAMPLE ID</th>
                <th>CUSTOMER NAME</th>
                <th>DEPARTMENT</th>
                <th>APPLICATION</th>
                <th>TANK NO</th>
                <th>EQUIPMENT</th>
                <th>PRODUCT</th>
                <th>QTY DRAWN(Ltrs.)</th>
                <th>DRAWN ON</th>
                </thead>
                <tbody>
                    <c:forEach items="${acceptSampleDetails}" var="data" varStatus="count">
                        <tr style="font-size:small" class="priorityLevel${data.samplepriorityId}">
                            <td class="text-center">${data.isSingleSampling=='OTS'?"<span style='color:mediumvioletred' class='defFont'><i class='glyphicon glyphicon-file'></i></span>" : "<span style='color:royalblue;' class='defFont'><i class='glyphicon glyphicon-book'></i></span>"}</td>
                            <td class="text-center">
                                <a class="getReceiveSample" href="${pageContext.request.contextPath}/Lab/GetAcknowledgeSample?smplid=${data.sampleId}&labCode=${data.mstLab.labCode}&csrftoken=${sessionScope.csrfToken}" data-smpid="${data.sampleId}" data-labCode="${data.mstLab.labCode}">${data.sampleId}</a>
                            </td>
                            <td class="NevsLeft">${data.mstDept.mstCustomer.customerName}</td>
                            <td class="NevsLeft">${data.mstDept.departmentName}</td>
                            <td class="NevsLeft">${data.mstApp.appName}</td>
                            <td class="text-center">${data.tankNo}</td>
                            <td class="NevsLeft">${data.mstEquip.equipmentName}</td>
                            <td class="NevsLeft">${data.mstProd.proName}</td>
                            <td class="text-center">${data.qtyDrawn}</td>
                            <td class="text-center">${data.stringsamplecreatedDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
