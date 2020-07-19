<%-- 
    Document   : editSampleTestResult
    Created on : 24 Jan, 2017, 2:34:18 PM
    Author     : Manish Jangir
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Sample Sent to Customer"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body onload="validate();">
        <div class="container-fluid">
            <div id="Sample">
                <form class="form-inline" action="#" enctype="multipart/form-data" id="sendtestResulttoTSE">
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    <div class="text-center" style="margin-top: -10px;">
                        <h3>Sample Sent to Customer</h3>
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
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onblur="disabledMsg()">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 1}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onclick="javascript:return validationMin(this, ${data.mstTestParam.minValue})" onchange="javascript:return validationMin(this, ${data.mstTestParam.minValue})" onblur="disabledMsg()">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 2}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onclick="javascript:return validationMax(this, ${data.mstTestParam.maxValue})" onchange="javascript:return validationMax(this, ${data.mstTestParam.maxValue})" onblur="disabledMsg()">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 3}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)"  onclick="javascript:return validationMinMax(this, ${data.mstTestParam.minValue}, ${data.mstTestParam.maxValue})" onchange="javascript:return validationMinMax(this, ${data.mstTestParam.minValue}, ${data.mstTestParam.maxValue})" onblur="disabledMsg()">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 4}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onkeypress="javascript:return isNumber(event)" onclick="javascript:return validationTypVal(this, ${data.mstTestParam.typValue}, ${data.mstTestParam.devValue})" onchange="javascript:return validationTypVal(this, ${data.mstTestParam.typValue}, ${data.mstTestParam.devValue})" onblur="disabledMsg()">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 5}">
                                                    <select style="overflow-y: auto" id="labDropdown" name="evaluatetestValues" onclick="javascript:return loadothData(${data.testId}, '${data.testVal}')" class="Custom-form-control" aria-describedby="basic-addon2" title="Double click to select value" onblur="disabledMsg()">
                                                        <option value="${data.testVal}" selected>${data.testVal}</option>
                                                    </select>
                                                    <div class="errmsg-contentSelectlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 6}">
                                                    <input name="evaluatetestValues" value="${data.testVal}" style="float: right" type="text" class="form-control validate" placeholder="Test value" aria-describedby="basic-addon2" onclick="javascript:return validationSpclMax(this, '${data.mstTestParam.maxValue}')" onchange="javascript:return validationSpclMax(this, '${data.mstTestParam.maxValue}')" onblur="disabledMsg()">
                                                    <div style="border: none" class="errmsg-contentlab errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                                </c:if>
                                                <span class="input-group-addon" id="basic-addon2">${data.unit}</span>
                                            </div>
                                        </td>
                                        <td class="text-right col-xs-2">
                                            <input disabled style="width: 100%" name="evaluatetestRemarks" placeholder="Conclusion of the Test" class="form-control" value="${data.testRemarks}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div class="container centre-btn">
                            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="border-right: 1px solid darkgray">
                                <label>Final Remarks(Max: 90 chars)</label>
                                <input disabled name="finalTestRemarks" id="remarks" value="${gtsm.labFinalTestRemarks}" style="width: 100%" placeholder="Enter your Remarks(Max: 90 chars) " class="form-control" maxlength="90" required/>
                            </div>
                            <c:if test="${gtsm.fileuploadStatus eq 'YES'}">
                                <div id="divUploadFile" class="form-inline col-md-4 text-center" style="border-bottom: 1px solid darkgray; padding: 10px;">
                                    <div class="input-group">
                                        Uploaded PDF File: <a href="${pageContext.request.contextPath}/DownloadUploadedFile?smplid=${gtsm.sampleId}&roleDoc=LAB&csrftoken=${sessionScope.csrfToken}" class="btn btn-info btn-l-lg" target="_blank" title="Click to download uploaded document"><span class="glyphicon glyphicon-download-alt"></span> ${gtsm.sampleId}</a>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${gtsm.fileuploadStatus eq 'NO'}">
                                <div class="form-inline col-md-4 text-center" style="border-bottom: 1px solid darkgray; padding: 15px;">
                                    <label>No Files Attached</label>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </form>
            </div>
            <div class="row form-inline">
                <br/>
                <div class="centre-btn">
                    <!--<a href="${pageContext.request.contextPath}/Lab/GetReceivedSample?status=4&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" class="btn-primary form-control" data-placement="top" title="Summary of samples sent to TSE but not forwarded to Customer by TSE. These Sample(s) are Editable." style="margin-right: 20px; text-decoration: none">GO BACK</a>-->
                    <a onclick="window.close()" data-toggle="tooltip" class="btn-primary form-control cursorOnHover" data-placement="top" title="Summary of samples sent to TSE but not forwarded to Customer by TSE. These Sample(s) are Editable." style="margin-right: 20px; text-decoration: none">GO BACK</a>
                </div>    
            </div>
        </div>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#responseDialog').on('hide.bs.modal', function (e) {
                window.location.reload();
            });
        });

    </script>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
    </body>
</html>
