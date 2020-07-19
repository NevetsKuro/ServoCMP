<%-- 
    Document   : acceptSmpl
    Created on : 3 Nov, 2016, 10:34:54 AM
    Author     : Manish Jangir
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Update Test Results"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <div id="Sample">
                <form class="form-inline" action="${pageContext.request.contextPath}/Lab/SendTestResultToTSE?Flag=AddResult" method="POST" enctype="multipart/form-data" id="sendtestResulttoTSE">
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    
                    <div class="text-center" style="margin-top: -10px;">
                        <h3>Enter Test Result(s)</h3>
                        <hr style=" margin: 0;">
                    </div>
                    <br>
                    <div>
                        <table class="table table-bordered table-striped table-responsive table-hover table-condensed">
                            <thead class="info">
                            <th class="success">Sample Id</th>
                            <th class="success">Customer</th>
                            <th class="success">Product</th>
                            <th class="success">Laboratory</th>
                            <th class="success">Taken Date</th>
                            </thead>
                            <tbody>
                                <tr class="info text-center">
                                    <td style="font-weight: bold">${gtsm.sampleId}</td>
                                    <td>${gtsm.mstDept.mstCustomer.customerName}</td>
                                    <td style="font-weight: bold">${gtsm.mstProd.proName}</td>
                                    <td>${gtsm.mstLab.labName}</td>
                                    <td>${gtsm.stringsamplecreatedDate}</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="text-center">
                            <label class="text-info"><u>Test Value Alert</u>&nbsp; &rarr;&nbsp; </label>
                            <span class="badge priorityLevel3">Within Range</span>
                            <span class="badge priorityLevel1">Beyond Range</span>
                            <span class="badge alert-warning">Incorrect Format</span>
                            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
                            <label class="text-info"><span class='text-danger' style="font-size: 24px;position: relative;top: 9px;">*</span>&nbsp; &rarr;&nbsp; </label>
                            <span class="badge alert-warning">No Specification Found</span>
                        </div>
                        <table class="table table-bordered" style="border-bottom: 1px solid darkgray">
                            <thead>
                            <th>Parameters to be Tested</th>
                            <th>Method</th>
                            <th>Specification</th>
                            <th style="width: 20%">Value</th>
                            <th>Remarks</th>
                            </thead>
                            <tbody> 
                                <c:forEach items="${mproTest}" var="data" varStatus="count">
                                    <tr>
                                        <td>
                                            <input name="evaluatetestIds" type="hidden" value="${data.testId}"/>${data.testName}
                                        </td>
                                        <td class=" col-xs-2">
                                            <input name="evaluatetestMethods" type="hidden" value="${data.testMethod}"/>${data.testMethod}
                                        </td>
                                        <td class=" col-xs-3">
                                            <input name="evaluatetestMethods" type="hidden" value="${data.spec}"/>${data.spec}
                                        </td>
                                        <td class="col-xs-2">
                                            <div class="input-group unity-input" style="width: 100%">
                                                <c:if test="${data.mstTestParam.checkId eq 0}">
                                                    <input name="evaluatetestValues" style="float: right" type="text" class="form-control" placeholder="Test value" aria-describedby="basic-addon2" maxlength="18">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 1}">
                                                    <input name="evaluatetestValues" style="float: right" type="text" class="form-control" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onchange="javascript:return validationMin(this, ${data.mstTestParam.minValue})" maxlength="18">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 2}">
                                                    <input name="evaluatetestValues" style="float: right" type="text" class="form-control" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onchange="javascript:return validationMax(this, ${data.mstTestParam.maxValue})" maxlength="18">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 3}">
                                                    <input name="evaluatetestValues" style="float: right" type="text" class="form-control" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onchange="javascript:return validationMinMax(this, ${data.mstTestParam.minValue}, ${data.mstTestParam.maxValue})" maxlength="18">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 4}">
                                                    <input name="evaluatetestValues" style="float: right" type="text" class="form-control" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onchange="javascript:return validationTypVal(this, ${data.mstTestParam.typValue}, ${data.mstTestParam.devValue})" maxlength="18">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 5}">
                                                    <select style="overflow-y: auto;" id="labDropdown" data-loadd="0" name="evaluatetestValues" class="Custom-form-control idDropdown" aria-describedby="basic-addon2" data-id="${data.testId}" data-minimum-results-for-search="Infinity"> <!-- onclick="javascript:return loadothData()     data-loaded="0""-->
                                                        <option value="" >SELECT</option>
                                                    </select>
                                                    <div class="errmsg-contentSelectlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 6}">
                                                    <input name="evaluatetestValues" style="float: right" type="text" class="form-control" placeholder="Test value" aria-describedby="basic-addon2" pattern="^[-]{0,1}[0-9]*[.]{0,1}[0-9]+\/[-]{0,1}[0-9]*[.]{0,1}[0-9]+$" onchange="javascript:return validationSpclMax(this, '${data.mstTestParam.maxValue}')" maxlength="18">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId == null}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onclick="" onchange="" maxlength="18">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <span class="input-group-addon" id="basic-addon2">${data.unit}</span>
                                            </div>
                                        </td>
                                        <td class="text-right col-xs-2">
                                            <input name="evaluatetestRemarks" maxlength="80" style="width: 100%" placeholder="Conclusion of the Test" class="form-control"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div class="container">
                            <div class="form-group col-xs-6 floating-label-form-group" style="border-right: 1px solid darkgray">
                                <label>Final Remarks(Max: 90 chars)</label>
                                <input name="finalTestRemarks" id="remarks" value="" style="width: 100%" placeholder="Enter your Remarks(Max: 90 chars) " class="form-control" maxlength="90" required/>
                                <div style="border: none; margin-top: 0; margin-left: -15px" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                            </div>

                            <div class="form-group col-md-6 text-center" style="border-bottom: 1px solid darkgray; padding: 8px;">
                                <label title="Click to choose this file to send as mail attachment">
                                    <input type="file" name="testReport" id="testReportFile" accept="application/pdf" value="1" />
                                </label>
                            </div>
                        </div>
                    </div>
                    <div>
                        <input name="ressmplid" type="hidden" value="${gtsm.sampleId}"/>
                        <input name="ressmplprodid" type="hidden" value="${gtsm.mstProd.proId}"/>
                        <input type="hidden" name="labCode" value="${gtsm.mstLab.labCode}" />
                    </div>
                </form>
            </div>
            <div class="row form-inline">
                <br/>
                <div class="centre-btn">
                    <button id="rejectSampleAtTest" data-smpid="${gtsm.sampleId}" data-labcode="${gtsm.mstLab.labCode}" class="btn btn-danger" data-toggle="tooltip" data-placement="top" title="For rejecting sample if the test cannot be processed" style="margin-right: 20px; text-decoration: none">REJECT</button>
                    <a href="${pageContext.request.contextPath}/Lab/GetReceivedSample?status=2&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" class="btn-primary form-control cursorOnHover" data-placement="top" title="Summary of samples sent to lab and yet to acknowledge upon received physically" style="margin-right: 20px; text-decoration: none">GO BACK</a>
                    <button onclick="review('sendtestResulttoTSE')" type="submit" class="btn btn-success" data-toggle="tooltip" data-placement="top" title="Update Sample test results and send to TSE">REVIEW | SUBMIT</button>
                </div>    
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>