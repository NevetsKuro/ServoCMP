<%-- 
    Document   : editSampleDetail
    Created on : 29 Dec, 2016, 2:55:41 PM
    Author     : Manish Jangir
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Sample Received by Lab"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <div id="takeSample">
                <h4 class="text-center text-info text-primary" style="border-bottom: 1px solid">
                    <span style="float: left; font-size: 12px">Last Sample Date: ${cs.stringpresampleDate}</span>
                    <span class="text-center">Sample Received by LAB</span>
                    <span style="float: right; font-size: 12px">Sample Due Date: ${cs.stringnxtsampleDate}</span>
                </h4>
                <form class="form-inline" action="#" method="POST" role="form" id="editsendsampletoLAB">
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    <input type="hidden" value="${cs}" name="cs"/>
                    <label>Sample Received Date: </label> <span style="text-decoration: underline">${cs.stringLabrecDate}</span>
                    <span style="float: right; "><label>Expected Test Result Date: </label> <span style="text-decoration: underline">${cs.stringExptestresultDate}</span></span>
                    <div class="row">
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Industry</label>
                            <input value="${cs.mstInd.indName}" name="indName" placeholder="Industry" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>Customer</label>
                            <input value="${cs.mstDept.mstCustomer.customerName}" name="customerName" placeholder="Customer" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Department</label>
                            <input value="${cs.mstDept.departmentName}" name="departmentName" placeholder="Department" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Application</label>
                            <input value="${cs.mstApp.appName}" name="appName" placeholder="Application" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>Describe Application</label>
                            <input value="${cs.descAppl}" name="descAppName" placeholder="Describe Application" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Equipment</label>
                            <input value="${cs.mstEquip.equipmentName}" name="equipmentName" placeholder="Equipment" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Tank no</label>
                            <input value="${cs.tankNo}" name="tankNo" placeholder="Tank no" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Product</label>
                            <input value="${cs.mstProd.proName}" name="proName" placeholder="Product" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Capacity in Liters</label>
                            <input value="${cs.mstProd.proCapacity}" name="proCapacity" placeholder="Capacity in lt." disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-2 floating-label-form-group has-error floating-label-form-group-with-value">
                            <label>Sample Drawn in ml <span class="">*</span></label>
                            <input value="${cs.qtyDrawn}" name="qtyDrawn" placeholder="Qunatity Drawn in ml *" required style="width: 100%" class="form-control col-xs-2" type="text" disabled>
                        </div>
                        <div style="border-left: 1px solid darkgrey; border-bottom: 1px solid darkgrey" id="editsampleDate" class="col-xs-3">
                            <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer">
                                <label>Sample Drawn Date *</label>
                                <input value="${cs.stringsampledrawnDate}" name="qtydrawnDate" placeholder="Sample Drawn date *" required style="width: 100%" type="text" class="form-control col-xs-2 datepicker" disabled/>
                                <span class="input-group-addon" style="border:0; background: none">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>TOP UP Quantity between ${cs.stringpresampleDate} and ${cs.stringsamplecreatedDate} *</label>
                            <input value="${cs.topupQty}" name="topupQty" placeholder="TOP UP Quantity between ${cs.stringpresampleDate} and ${cs.stringsamplecreatedDate}" style="width: 100%" class="form-control col-xs-2" type="text" disabled>
                        </div>
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>Running Hrs(Current Oil) <span class="has-error">*</span></label>
                            <input value="${cs.runningHrs}" name="runningHrs" placeholder="Running Hrs(Current Oil) *" required="required" style="width: 100%" class="form-control col-xs-2" type="text" disabled>
                        </div>    
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>Lab Name</label>
                            <select id="labCodeId" name="labCode" style="width: 100%" class="form-control" disabled>
                                <option value="">Select Lab to send Sample *</option>
                                <c:forEach items="${labmaster}" var="labMaster">
                                    <c:choose>
                                        <c:when test="${labMaster.labCode eq cs.mstLab.labCode}">
                                            <option selected="" id="${labMaster.labAuthority}" value="${labMaster.labCode}">${labMaster.labName}</option>
                                            <c:set var="selectedLabAuthority" value="${labMaster.labAuthority}" />
                                        </c:when>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Lab In Charge *</label>
                            <input id="labAuthorityId" name="labAuthority" placeholder="Head of Laboratory" style="width: 100%" class="form-control" type="text" value="${selectedLabAuthority}" readonly="readonly">
                        </div>
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>Sample Priority *</label>
                            <select style="width: 100%" class="form-control" name="samplepriorityId" disabled>
                                <option>Select Priority of Sample</option>
                                <c:forEach items="${prioritymaster}" var="prioMaster">
                                    <c:choose>
                                        <c:when test="${prioMaster.priorityId eq cs.samplepriorityId}">
                                            <option selected="" name="prioritymaster" value="${prioMaster.priorityId}">${prioMaster.priorityName}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option name="prioritymaster" value="${prioMaster.priorityId}">${prioMaster.priorityName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
                            <label>Parameters needed to be Tested for *</label>
                            <select style="width: 100%; border: none" class="form-control exist-test-dropdown" id="exist-test-dropdown" name="testIds" multiple="multiple" disabled>
                                <c:forEach items="${preTestList}" var="data" varStatus="count">
                                    <option selected value="${data.testId}">${data.testName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>Comments on Sample</label>
                            <textarea style="width: 100%" placeholder="Comments on Sample" class="form-control" rows="2" disabled>${cs.sampledrawnRemarks}</textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Comments on Priority</label>
                            <textarea name="samplepriorityRemarks" style="width: 100%" placeholder="Comments on Priority of Sample" class="form-control" rows="2" disabled>${cs.samplepriorityRemarks}</textarea>
                        </div>
                        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Lab Received Remarks</label>
                            <textarea name="labrecRemarks" style="width: 100%" placeholder="Lab Receive Remarks" class="form-control" rows="2" disabled>${cs.labrecRemarks}</textarea>
                        </div>
                    </div>
                </form>
                <div class="row form-inline">
                    <br/>
                    <div class="centre-btn">
                        <!--<a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?status=2&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" class="btn-primary form-control" data-placement="top" title="Summary details for which samples need to be created or postponed" style="margin-right: 20px; text-decoration: none">GO BACK</a>-->
                        <a onclick="window.close()" data-toggle="tooltip" class="btn-primary form-control cursorOnHover" data-placement="top" title="Summary details for which samples need to be created or postponed" style="margin-right: 20px; text-decoration: none">CLOSE</a>
                    </div>    
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
