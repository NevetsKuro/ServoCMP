<%-- 
    Document   : toMaintenance
    Created on : Jan 5, 2017, 2:14:02 PM
    Author     : Manish Jangir
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Send Sample under Maintenance."/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid" style="font-family: sans-serif">
            <h4 class="text-center text-info text-primary" style="border-bottom: 1px solid">
                <span style="float: left; font-size: 12px">Last Sample Date: ${ms.stringpresampleDate} &nbsp;&nbsp;&nbsp;</span>
                <span style="float: left; font-size: 12px">
                    <a title="Click here to get details of postponed history." onclick="callGetSamplePostponedDetail('${ms.tankId}', '${ms.stringpresampleDate}')" style="cursor: pointer">
                        Postponed History
                    </a>
                </span>
                <span class="text-center">Postpone Sample</span>
                <span style="float: right; font-size: 12px">Sample Due Date: ${ms.stringnxtsampleDate}</span>
            </h4>
            <div id="Sample">
                <form id="postponedSample" class="form-inline" action="${pageContext.request.contextPath}/Tse/SampleMaintenance" method="POST" role="form">
                    <br/>
                    <input type="hidden" value="${ms}" name="ms"/>
                    <input type="hidden" value="${ms.stringnxtsampleDate}" id="nextSampleDate"/>
                    <input type="hidden" value="${ms.postponeendDate}" id="postponeendDate"/>
                    <input type="hidden" value="${sampleType}" id="sampleType" name="sampleType"/>
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    <div class="row">
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Industry</label>
                            <input value="${ms.mstInd.indName}" name="indName" placeholder="Industry" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>Customer</label>
                            <input value="${ms.mstDept.mstCustomer.customerName}" name="customerName" placeholder="Customer" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Department</label>
                            <input value="${ms.mstDept.departmentName}" name="departmentName" placeholder="Department" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Application</label>
                            <input value="${ms.mstApp.appName}" name="appName" placeholder="Application" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>Describe Application</label>
                            <input value="${cs.descAppl}" name="descAppName" placeholder="Describe Application" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Equipment</label>
                            <input value="${ms.mstEquip.equipmentName}" name="equipmentName" placeholder="Equipment" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Tank no</label>
                            <input value="${ms.tankNo}" name="tankNo" placeholder="Tank no" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Product</label>
                            <input value="${ms.mstProd.proName}" name="proName" placeholder="Product" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Capacity in Liters</label>
                            <input value="${ms.mstProd.proCapacity}" name="proCapacity" placeholder="Capacity in lt." disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-8 floating-label-form-group floating-label-form-group-with-value">
                            <label>Parameters needed to be Tested for <span class="text-danger">*</span></label>
                            <select name="testIds" style="width: 100%;" class="form-control exist-test-dropdown" id="exist-test-dropdown" multiple="multiple" disabled>
                                <c:forEach items="${preTestList}" var="data" varStatus="count">
                                    <option selected value="${data.testId}" disabled>${data.testName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div style="border-left: 1px solid darkgrey; border-bottom: 1px solid darkgrey" id="postponetillDate" class="col-xs-4 form-group floating-label-form-group floating-label-form-group-with-value">
                            <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-date-format="dd-mm-yyyy" data-date-start-date="${ms.sdStartDate}" data-date-end-date="${ms.postponetillDate}" data-date-container="#dateContainer">
                                <label>Postponed till Date <span class="text-danger">*</span></label>
                                <input name="postponetillDate" placeholder="Postpone till date" required style="width: 100%" type="text" class="form-control col-xs-2 datepicker"/>
                                <div class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                <span class="input-group-addon" style="border:0; background: none">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-12 floating-label-form-group  floating-label-form-group-with-value">
                            <label>Reason for Postponing the Sample<strong>(Max:80)</strong></label>
                            <textarea name="postponeReason" style="width: 100%" placeholder="Reason for Postponing the Sample" maxlength="80" class="form-control" rows="2"></textarea>
                            <div class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger" style="margin-top: 0px"></div>
                        </div>
                    </div>

                </form>
            </div>
            <div class="row form-inline">
                <div class="centre-btn">
<%--                    <a class="btn-primary form-control" href="${pageContext.request.contextPath}/Tse/GetAllPendingSamplesAtTSE&sampleType=${sampleType=='0'?'CMP':'OTS'}&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="top" title="Summary of samples which have been created and need to send to LAB"  style="margin-right: 20px; text-decoration: none">GO BACK</a>--%>
                    <a class="btn-primary form-control cursorOnHover" onclick="window.close()" data-toggle="tooltip"  data-placement="top" title="Click here to close the window"  style="margin-right: 20px; text-decoration: none">CLOSE</a>
                    <button onclick="review('postponedSample')" type="submit" class="btn btn-success" data-toggle="tooltip" data-placement="top" title="Postpone this Sample.">REVIEW | SUBMIT</button>
                </div>    
            </div>
        </div>
    </body>
</html>