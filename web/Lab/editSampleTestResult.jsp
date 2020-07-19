<%-- 
    Document   : editSampleTestResult
    Created on : 24 Jan, 2017, 2:34:18 PM
    Author     : Manish Jangir
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Sample Sent to TSE"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body onload="validate();">
        <div class="container-fluid">
            <div id="Sample">
                <form class="form-inline" action="${pageContext.request.contextPath}/Lab/SendTestResultToTSE?Flag=EditResult" method="POST" enctype="multipart/form-data" id="sendtestResulttoTSE">
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    <div class="text-center" style="margin-top: -10px;">
                        <h3>Sample Details</h3>
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
                            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
                            <label class="text-info"><span class='text-danger' style="font-size: 24px;position: relative;top: 9px;">*</span>&nbsp; &rarr;&nbsp; </label>
                            <span class="badge alert-warning">No Specification Found</span>
                        </div>
                        <table class="table table-bordered" style="border-bottom: 1px solid darkgray">
                            <thead>
                            <th>Parameters to be Tested</th>
                            <th>Method</th>
                            <th>Specification</th>
                            <th style="width: 15%">Value</th>
                            <th>Remarks</th>
                            </thead>
                            <tbody>
                                <c:forEach items="${mproTest}" var="data" varStatus="count">
                                    <tr>
                                        <td class="text-left col-xs-3">
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
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" >
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 1}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onclick="javascript:return validationMin(this, ${data.mstTestParam.minValue})" onchange="javascript:return validationMin(this, ${data.mstTestParam.minValue})">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 2}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onclick="javascript:return validationMax(this, ${data.mstTestParam.maxValue})" onchange="javascript:return validationMax(this, ${data.mstTestParam.maxValue})">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 3}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)"  onclick="javascript:return validationMinMax(this, ${data.mstTestParam.minValue}, ${data.mstTestParam.maxValue})" onchange="javascript:return validationMinMax(this, ${data.mstTestParam.minValue}, ${data.mstTestParam.maxValue})">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 4}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onclick="javascript:return validationTypVal(this, ${data.mstTestParam.typValue}, ${data.mstTestParam.devValue})" onchange="javascript:return validationTypVal(this, ${data.mstTestParam.typValue}, ${data.mstTestParam.devValue})">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 5}">
                                                    <select style="overflow-y: auto" id="labDropdown" name="evaluatetestValues"  onclick="javascript:return loadothData(${data.testId}, '${data.testVal}')" class="Custom-form-control" aria-describedby="basic-addon2" title="Double click to select value" >
                                                        <option value="${data.testVal}" selected>${data.testVal}</option>
                                                    </select>
                                                    <div class="errmsg-contentSelectlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 6}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onclick="javascript:return validationSpclMax(this, '${data.mstTestParam.maxValue}')" onchange="javascript:return validationSpclMax(this, '${data.mstTestParam.maxValue}')">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId == null}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onclick="" onchange="">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <span class="input-group-addon" id="basic-addon2">${data.unit}</span>
                                            </div>
                                        </td>
                                        <td class="text-right col-xs-2">
                                            <input maxlength="80" style="width: 100%" name="evaluatetestRemarks" placeholder="Conclusion of the Test" class="form-control" value="${data.testRemarks}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div class="container">
                            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="border-right: 1px solid darkgray">
                                <label>Final Remarks(Max: 90 chars)</label>
                                <input name="finalTestRemarks" id="remarks" value="${gtsm.labFinalTestRemarks}" style="width: 100%" placeholder="Enter your Remarks(Max: 90 chars) " class="form-control" maxlength="90" required/>
                            </div>
                            <c:if test="${gtsm.fileuploadStatus eq 'YES'}">
                                <div id="divUploadFile" class="form-inline col-md-4 text-center" style="border-bottom: 1px solid darkgray; border-right: 1px solid darkgray; padding: 10px;">
                                    <div class="input-group">
                                        Uploaded PDF File: <a href="${pageContext.request.contextPath}/DownloadUploadedFile?smplid=${gtsm.sampleId}&labCode=${gtsm.mstLab.labCode}&roleDoc=LAB" class="btn btn-info btn-l-lg" target="_blank" title="Click to download uploaded document"><span class="glyphicon glyphicon-download-alt"></span> ${gtsm.sampleId}</a>
                                        <a style="text-decoration: none; cursor: pointer;" target="_blank" title="Click to delete uploaded document" onclick="callUpdate('${gtsm.sampleId}','${gtsm.mstLab.labCode}')" >
                                            <span class="glyphicon glyphicon-remove-circle" style=" font-size:20px; "> </span> 
                                        </a>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${gtsm.fileuploadStatus eq 'NO'}">
                                <div class="form-inline col-md-4 text-center" style="border-bottom: 1px solid darkgray; border-right: 1px solid darkgray; padding: 15px;">
                                    <label>No Files Attached</label>
                                </div>
                            </c:if>
                            <div class="input-group col-md-4 text-center" style="border-bottom: 1px solid darkgray;">
                                Upload or Override PDF file (optional): <input id="testReportFile" name="testReport" class="filestyle" data-buttonBefore="true" type="file" accept="application/pdf"/>
                            </div>
                        </div>
                    </div>
                    <div>
                        <input name="ressmplid" type="hidden" value="${gtsm.sampleId}"/>
                        <input name="ressmplprodid" type="hidden" value="${gtsm.mstProd.proId}"/>
                        <input name="labCode" type="hidden" value="${gtsm.mstLab.labCode}"/>
                    </div>
                </form>
            </div>
            <div class="row form-inline">
                <br/>
                <div class="centre-btn">
                    <!--<a href="${pageContext.request.contextPath}/Lab/GetReceivedSample?status=3&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" class="btn-primary form-control" data-placement="top" title="Summary of samples sent to TSE but not forwarded to Customer by TSE. These Sample(s) are Editable." style="margin-right: 20px; text-decoration: none">GO BACK</a>-->
                    <a onclick="window.close()" data-toggle="tooltip" class="btn-primary form-control cursorOnHover" data-placement="top" title="Summary of samples sent to TSE but not forwarded to Customer by TSE. These Sample(s) are Editable." style="margin-right: 20px; text-decoration: none">CLOSE</a>
                    <btn onclick="review('sendtestResulttoTSE')" type="submit" class="btn btn-danger" data-toggle="tooltip" data-placement="top" title="Update Sample test results and send to TSE">REVIEW | SUBMIT</btn>
                </div>    
            </div>
        </div>
    <script>
        $(document).ready(function () {
            $('#responseDialog').on('hide.bs.modal', function (e) {
                window.location.reload();
            });
        });

    </script>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
