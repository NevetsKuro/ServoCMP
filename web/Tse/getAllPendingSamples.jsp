<%-- 
    Document   : Sample Pending to be Created for Testing and then Made Available to sent to LAB for Testing.
    Created on : 19 Oct, 2016, 3:25:05 PM
    Author     : Manish Jangir
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Pending Samples for Testing"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="text-center text-info" style="text-decoration: underline">${sampleType} Samples pending to be Created for Testing </h4>
            <table id="PendingSamples" class="table table-striped table-bordered tbCenter-head" cellspacing="0" width="100%" style="overflow: scroll;" >
                <thead style="font-size: x-small">
                <th>Create <br/> OR Postpone</th>
                <th>CUSTOMER</th>
                <th>DEPARTMENT</th>
                <th>APPLICATION</th>
                <th>TANK</th>
                <th>EQUIPMENT</th>
                <th>PRODUCT</th>
                <th>CAPACITY</th>
                <th>FREQ</th>
                <c:if test="${sampleType=='CMP'}">
                    <th width="6%">LAST SAMPLING DATE</th>
                    <th width="6%">NEXT SAMPLING DUE DATE</th>
                    <th width="6%">REVISED SAMPLING DUE DATE</th>
                    <th>DEFERRED NO</th>
                </c:if>
                </thead>
                <tbody>
                    <c:forEach items="${customerDetails}" var="data" varStatus="count">
                        <tr style="font-size:small">
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/Tse/GetSamplePendingAtTSE?tank=${data.sampleId}&do=create&sampleType=${sampleTypeNo}&csrftoken=${sessionScope.csrfToken}" title="Click here to Create Sample" target="_blank" style=" text-decoration: none;">
                                    <img height="25" width="25" src="${pageContext.request.contextPath}/resources/images/create.png"/>
                                </a>
                                <c:if test="${sampleType eq 'CMP'}">
                                    |
                                    <a href="${pageContext.request.contextPath}/Tse/GetSamplePendingAtTSE?tank=${data.sampleId}&do=maintenance&sampleType=${sampleTypeNo}&csrftoken=${sessionScope.csrfToken}" title="Click here to Postpone this Sample Pending Date." target="_blank" style=" text-decoration: none;">
                                        <img height="25" width="25" src="${pageContext.request.contextPath}/resources/images/postpone.png"/>
                                    </a>
                                </c:if>
                            </td>
                            <td class="text-left">${data.mstDept.mstCustomer.customerName}</td>
                            <td class="text-left">${data.mstDept.departmentName}</td>
                            <td class="text-left">${data.mstApp.appName}</td>
                            <td class="text-center">${data.tankNo}</td>
                            <td class="text-left">${data.mstEquip.equipmentName}</td>
                            <td class="text-left">${data.mstProd.proName}</td>
                            <td class="text-left">${data.mstProd.proCapacity}</td>
                            <td class="text-center">${data.sampleFreq}</td>
                            <c:if test="${sampleType=='CMP'}">
                                <td class="text-center">${data.stringpresampleDate}</td>
                                <td class="text-center">${data.stringoldnxtsampleDate}</td>
                                <td class="text-center">${data.stringnxtsampleDate}</td>
                                <td class="text-center">
                                    <a title="Details of No of times SamplePostponed." onclick="callGetSamplePostponedDetail('${data.sampleId}', '${data.stringpresampleDate}')"  style="cursor:pointer;text-decoration: none;">
                                        ${data.postponedCount}
                                    </a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>