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



        <h4 class="text-center text-info" style="text-decoration: underline">Samples Send to Lab for Testing</h4>

        <form role="form" action="${pageContext.request.contextPath}/Tse/GetCreatedSamples?status=0" id="iom" class="form-inline" method="post">
            <input type="hidden" id="sampleType" name="sampleType" value="${sampleType}" />
            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
            <div class="row centre-btn">
                <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                    <label>Select LAB <span class="text-danger">*</span></label>
                    <select id="idlabName"  name="labName" style="width: 100%; padding: 5px" class="form-control select2-select" required="required">
                        <option value="">Select LAB *</option>
                        <c:forEach items="${labmaster}" var="labMaster">
                            <c:choose>
                                <c:when test="${labMaster.labCode eq labLocCode}">
                                    <option selected="" id="${labMaster.labAuthority}" value="${labMaster.labCode}">${labMaster.labName}</option>
                                    <c:set var="selectedLabAuthority" value="${labMaster.labAuthority}" />
                                </c:when>
                                <c:otherwise>
                                    <option id="${labMaster.labAuthority}" value="${labMaster.labCode}">${labMaster.labName}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row centre-btn">
                <div class="centre-btn" style="margin-top: 20px">

                    <input style="margin-left: 20px" type="submit" value="Get Summary" class="btn btn-primary" />
                </div>
            </div>
        </form>

        <c:if test="${InProcessTseSampleDetails.size() > -1}">


            <div class="container-fluid">



                <div class="text-center container-fluid">
                    <a id='selectionProcess' style="float: left; cursor: pointer;">How to Select?</a>
                    <label class="text-info"><u>PRIORITY</u>&nbsp; &rarr;&nbsp; </label>
                    <span class="badge priorityLevel3">NORMAL</span>
                    <span class="badge priorityLevel2">MEDIUM</span>
                    <span class="badge priorityLevel1">HIGH</span>
                </div>

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
                    <form id="iomForm" action="${pageContext.request.contextPath}/Tse/GetSampleSummaryData" method="post">
                        <input type="hidden" id="jsonSampleIds" name="sampleIds" value="">
                        <input type="hidden" id="idLabLocCode" name="nameLabLocCode" value="${labLocCode}">
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <input type="button" class="btn btn-primary" value="View IOM" id="viewIOM">
                        <input type="button" class="btn btn-primary" value="View Sample" id="viewSample"/>
                        <a href="${pageContext.request.contextPath}/Tse/GetUpdateSample?smplid=${data.sampleId}&status=${data.statusId}&csrftoken=${sessionScope.csrfToken}" title="Click to edit Sample details sent to Lab"></a>
                    </form>
                </div>
            </div>
        </c:if>  

        <c:if test="${InProcessTseSampleDetails.size() < 1}">
            <div class="text-center">
                <span class="text-center"></span>
            </div>
        </c:if>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
