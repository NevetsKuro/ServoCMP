<%@page import="java.util.ArrayList"%>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Test Result Sent by Lab"/>
    </jsp:include>
    <body onload="validate(); loadImages();">
        <script src="${pageContext.request.contextPath}/resources/js/highchart.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/exporting.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/rgbcolor.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/canvg.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jspdf.debug.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jspdf.plugin.autotable.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/export.js" type="text/javascript"></script>
        <div class="container-fluid">
            <div id="Sample">
                <form method="post" action="#" enctype="multipart/form-data" class="sendToCustomer">
                    <c:forEach items="${gpsd}" var="sample">
                        <input type="hidden" value="${sample.sampleId}" name="sampleID"/>
                        <input type="hidden" value="${sample.getTankId()}" name="custsampleinfoId"/>
                        <input type="hidden" value="${sample.mstDept.mstCustomer.customerId}" name="customerId"/>
                    </c:forEach>
                    <input type="hidden" value="${SendTo}" name="fm1Value"/>
                    <input type="hidden" value="${SendCC}" name="fm2Value"/>
                    <input type="hidden" value="${SendSubject}" name="fm3Value"/>
                    <input type="hidden" value="${SendBody}" name="fm4Value"/>
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    <h4 class="text-center text-info" style="text-decoration: underline">Sample Test Results</h4>

                    <table id="custTableId" class="custTable">
                        <tr>
                            <td class="col-md-3"><label>Customer Name</label></td>
                            <td class="col-md-3">${gpsd[0].mstDept.mstCustomer.customerName}</td>
                            <td class="col-md-3"><label>Sample id</label></td>
                            <td class="col-md-3"><label style="color: #3c763d;">${gpsd[0].sampleId}</label></td>
                        </tr>
                        <tr>
                            <td class="col-md-3"><label>Oil tested</label></td>
                            <td class="col-md-3"><label style="color: #3c763d;">${gpsd[0].mstProd.proName}</label></td>
                            <td class="col-md-3"><label>Date of Sample</label></td>
                            <td class="col-md-3">${gpsd[0].stringsampledrawnDate}</td>
                        </tr>
                    </table>
                    <br/>
                    <c:forEach items="${gpsd}" var="sample">

                        <table id="iocTableId" class="iocTable">
                            <tr>
                                <c:choose>
                                    <c:when test="${sample.mstLab.labType eq 'CSL'}">
                                        <td style="text-align: center;" colspan="4"><label>CSL Lab Details</label></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td style="text-align: center;" colspan="4"><label>RND Lab Details</label></td>
                                    </c:otherwise>
                                </c:choose>   
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td class="col-md-3"><label>IOC State Office</label></td>
                                <td class="col-md-3">${sUser.sCURR_COMP}</td>
                                <td class="col-md-3"><label>Sample Tested at</label></td>
                                <td class="col-md-3">${sample.mstLab.labName}</td>
                            </tr>
                            <tr>
                                <td class="col-md-3"><label>IOC Field Officer Name</label></td>
                                <td class="col-md-3">${sUser.sEMP_NAME} </td>
                                <td class="col-md-3"><label>Email ID</label></td>
                                <td class="col-md-3">${sUser.sEMAIL_ID}</td>
                            </tr>
                            <tr>
                                <td class="col-md-3"><label>Designation</label></td>
                                <td class="col-md-3">${sUser.sDESIGN_SHORT_DESC}</td>
                                <td class="col-md-3"><label>Date of Oil Sampling</label></td>
                                <td class="col-md-3">${sample.stringtestresultenteredDate}</td>
                            </tr>
                        </table>
                    </c:forEach>
                    <br/>
                    <table id="deptTableId" class="deptTable">
                        <tr>
                            <td class="col-md-3"><label>User department</label></td>
                            <td class="col-md-3">${gpsd[0].mstDept.departmentName}</td>
                            <td class="col-md-3"><label>Sump Capacity (Liters)</label></td>
                            <td class="col-md-3">${gpsd[0].mstProd.proCapacity}</td>
                        </tr>
                        <tr>
                            <td class="col-md-3"><label>Equipment</label></td>
                            <td class="col-md-3">${gpsd[0].mstEquip.equipmentName}</td>
                            <td class="col-md-3"><label>Running Hrs</label></td>
                            <td class="col-md-3">${gpsd[0].runningHrs}</td>
                        </tr>
                        <tr>
                            <td class="col-md-3"><label>OEM / Make</label></td>
                            <td class="col-md-3">${gpsd[0].mstEquip.mstmake.makeName}</td>
                            <td class="col-md-3"><label>Date of Last Oil Change</label></td>
                            <td class="col-md-3">${gpsd[0].lastoilChanged}</td>
                        </tr>
                        <tr>
                            <td class="col-md-3"><label>Top-up Quantity</label></td>
                            <td class="col-md-3">${gpsd[0].topupQty}</td>
                            <td class="col-md-3"></td>
                            <td class="col-md-3"></td>
                        </tr>
                    </table>

                    <div id="marketResult">
                        <h4 class="col-md-6">Result from Market</h4>
                        <table class="testTable tbCenter" cellspacing="0">
                            <thead style="font-size: small">
                            <th>TEST NAME</th>
                            <th>TEST METHOD</th>
                            <th>SPEC</th>
                            <th>${arrRsltDts[0]}</th>
                            <th>${arrRsltDts[1]}</th>
                            <th>${arrRsltDts[2]}</th>
                            <th>Units</th>
                            </thead>
                            <tbody>
                                <c:forEach items="${finalTestReportResult}" var="data" varStatus="count">
                                    <tr style="font-size:small;">
                                        <td class="text-center" style="vertical-align: middle">
                                            <div class="checkbox">
                                                <label style="color: black" class="removeRow">
                                                    <input style="float: left; color: black;" type="checkbox" title="" onclick="removeRow(this)" checked/>
                                                    <span class="cr" style="border-color: dodgerblue;">
                                                        <i class="cr-icon glyphicon glyphicon-ok" style="color: dodgerblue;"></i>
                                                    </span>
                                                    ${data.testName}
                                                </label>
                                            </div>
                                        </td>
                                        <td class="text-center">${data.testMethod}</td>
                                        <td class="text-center">${data.spec}</td>
                                        <td class="text-center" style="width:10%">
                                            <c:if test="${not empty data.prevToprevVal}"> 
                                                <c:if test="${data.mstTestParam.checkId eq 0}">
                                                    <input style="float: right" type="text" class="form-control validate"  value="${data.prevToprevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 1}">
                                                    <input style="float: right" type="text" class="form-control validate" onclick="javascript:return validationMin(this, ${data.mstTestParam.minValue})" value="${data.prevToprevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 2}">
                                                    <input style="float: right" type="text" class="form-control validate" onclick="javascript:return validationMax(this, ${data.mstTestParam.maxValue})" value="${data.prevToprevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 3}">
                                                    <input style="float: right" type="text" class="form-control validate" onclick="javascript:return validationMinMax(this, ${data.mstTestParam.minValue}, ${data.mstTestParam.maxValue})" value="${data.prevToprevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 4}">
                                                    <input style="float: right" type="text" class="form-control validate" onclick="javascript:return validationTypVal(this, ${data.mstTestParam.typValue}, ${data.mstTestParam.devValue})" value="${data.prevToprevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 5}">
                                                    <input style="float: right" type="text" class="form-control validate" value="${data.prevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 6}">
                                                    <input style="float: right" type="text" class="form-control validate" onclick="javascript:return validationSpclMax(this, '${data.mstTestParam.maxValue}')"  value="${data.prevToprevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq null && data.prevVal ne null}">
                                                    <input style="float: right" type="text" class="form-control validate alert-info" value="${data.prevToprevVal}" readonly>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${empty data.prevToprevVal}">
                                                -
                                            </c:if>
                                        </td>
                                        <td class="text-center" style="width:10%">
                                            <c:if test="${not empty data.prevVal}"> 
                                                <c:if test="${data.mstTestParam.checkId eq 0}">
                                                    <input style="float: right" type="text" class="form-control validate"  value="${data.prevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 1}">
                                                    <input style="float: right" type="text" class="form-control validate" onclick="javascript:return validationMin(this, ${data.mstTestParam.minValue})" value="${data.prevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 2}">
                                                    <input style="float: right" type="text" class="form-control validate" onclick="javascript:return validationMax(this, ${data.mstTestParam.maxValue})" value="${data.prevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 3}">
                                                    <input style="float: right" type="text" class="form-control validate" onclick="javascript:return validationMinMax(this, ${data.mstTestParam.minValue}, ${data.mstTestParam.maxValue})" value="${data.prevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 4}">
                                                    <input style="float: right" type="text" class="form-control validate" onclick="javascript:return validationTypVal(this, ${data.mstTestParam.typValue}, ${data.mstTestParam.devValue})" value="${data.prevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 5}">
                                                    <input style="float: right" type="text" class="form-control validate" value="${data.prevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 6}">
                                                    <input style="float: right" type="text" class="form-control validate"  onclick="javascript:return validationSpclMax(this, '${data.mstTestParam.maxValue}')"  value="${data.prevVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq null && data.prevVal ne null}">
                                                    <input style="float: right" type="text" class="form-control validate alert-info" value="${data.prevVal}" readonly>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${empty data.prevVal}">
                                                -
                                            </c:if>
                                        </td>
                                        <td class="text-center" style="width:20%">
                                            <c:if test="${not empty data.curVal}"> 
                                                <c:if test="${data.mstTestParam.checkId eq 0}">
                                                    <input style="float: right" type="text" class="form-control validate" aria-describedby="basic-addon2"  value="${data.curVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 1}">
                                                    <input style="float: right" type="text" class="form-control validate" aria-describedby="basic-addon2" onclick="javascript:return validationMin(this, ${data.mstTestParam.minValue})" value="${data.curVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 2}">
                                                    <input style="float: right" type="text" class="form-control validate" aria-describedby="basic-addon2" onclick="javascript:return validationMax(this, ${data.mstTestParam.maxValue})" value="${data.curVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 3}">
                                                    <input style="float: right" type="text" class="form-control validate" aria-describedby="basic-addon2" onclick="javascript:return validationMinMax(this, ${data.mstTestParam.minValue}, ${data.mstTestParam.maxValue})" value="${data.curVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 4}">
                                                    <input style="float: right" type="text" class="form-control validate" aria-describedby="basic-addon2" onclick="javascript:return validationTypVal(this, ${data.mstTestParam.typValue}, ${data.mstTestParam.devValue})" value="${data.curVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 5}">
                                                    <input style="float: right" type="text" class="form-control validate" aria-describedby="basic-addon2" value="${data.curVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq 6}">
                                                    <input style="float: right" type="text" class="form-control validate" aria-describedby="basic-addon2" onclick="javascript:return validationSpclMax(this, '${data.mstTestParam.maxValue}')" value="${data.curVal}" readonly>
                                                </c:if>
                                                <c:if test="${data.mstTestParam.checkId eq null && data.curVal ne null}">
                                                    <input style="float: right" type="text" class="form-control validate alert-info" aria-describedby="basic-addon2" value="${data.curVal}" readonly>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${empty data.curVal}">
                                                -
                                            </c:if>
                                        </td>
                                        <td>
                                            <span class="input-group-addon" id="basic-addon2">${data.unit}&nbsp;</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="collapse">
                        <h4>Result from Market</h4>
                        <table id="Market-Test" class="table table-striped table-bordered" cellspacing="0">
                            <thead style="font-size: x-small">
                            <th>TEST NAME</th>
                            <th>TEST METHOD</th>
                            <th>Unit</th>
                            <th>${arrRsltDts[0]} </th>
                            <th>${arrRsltDts[1]}</th>
                            <th>${arrRsltDts[2]}</th>
                            </thead>
                            <tbody>
                                <c:forEach items="${finalTestReportResult}" var="data" varStatus="count">
                                    <tr style="font-size:small">
                                        <td class="text-center">${data.testName}</td>
                                        <td class="text-center">${data.testMethod}</td>
                                        <td>${data.unit}</td>
                                        <td class="text-center" style="width:10%">
                                            <c:if test="${data.mstTestParam.checkId eq 0}">
                                                ${data.prevToprevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 1}">
                                                ${data.prevToprevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 2}">
                                                ${data.prevToprevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 3}">
                                                ${data.prevToprevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 4}">
                                                ${data.prevToprevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 5}">
                                                ${data.prevToprevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 6}">
                                                ${data.prevToprevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq null && data.prevToprevVal ne null}">
                                                ${data.prevToprevVal}
                                            </c:if>
                                        </td>
                                        <td class="text-center" style="width:10%">
                                            <c:if test="${data.mstTestParam.checkId eq 0}">
                                                ${data.prevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 1}">
                                                ${data.prevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 2}">
                                                ${data.prevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 3}">
                                                ${data.prevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 4}">
                                                ${data.prevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 5}">
                                                ${data.prevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 6}">
                                                ${data.prevVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq null && data.prevVal ne null}">
                                                ${data.prevVal}
                                            </c:if>
                                        </td>
                                        <td class="text-center" style="width:20%">
                                            <c:if test="${data.mstTestParam.checkId eq 0}">
                                                ${data.curVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 1}">
                                                ${data.curVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 2}">
                                                ${data.curVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 3}">
                                                ${data.curVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 4}">
                                                ${data.curVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 5}">
                                                ${data.curVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq 6}">
                                                ${data.curVal}
                                            </c:if>
                                            <c:if test="${data.mstTestParam.checkId eq null && data.curVal ne null}">
                                                ${data.curVal}
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <br/>
                    <br/>
                    <div class="col-md-12 graphDiv">

                    </div>
                    <div class="container">
                        <div class="form-group col-xs-6 floating-label-form-group" style="border-right: 1px solid darkgray">
                            <label>Final Remarks(Max: 90 chars)</label>
                            <input name="finalRemarks" id="remarks" value="" style="width: 100%" placeholder="Enter your Remarks(Max: 90 chars) " class="form-control" maxlength="90" required/>
                        </div>
                        <c:forEach items="${gpsd}" var="sample">

                            <c:if test="${sample.fileuploadStatus eq 'YES'}">
                                <div class="checkbox chk1 form-group col-xs-3" style="margin-top: -4px">
                                    <label>
                                        <input type="checkbox" value="" checked>
                                        <span class="cr" style="border-color: dodgerblue;">
                                            <i class="cr-icon glyphicon glyphicon-ok" style="color: dodgerblue;"></i>
                                        </span>
                                        Attachment (by 
                                        <c:if test="${sample.mstLab.labType eq 'CSL'}">
                                            CSL
                                        </c:if>
                                        <c:if test="${sample.mstLab.labType eq 'RND'}">
                                            RND
                                        </c:if>
                                        LAB): <a href="${pageContext.request.contextPath}/DownloadUploadedFile?smplid=${sample.sampleId}&labCode=${sample.mstLab.labCode}&roleDoc=LAB&csrftoken=${sessionScope.csrfToken}" target="_blank" title="Click to download Attachment"><span class="glyphicon glyphicon-download-alt"></span> ${sample.sampleId}</a>
                                    </label>
                                </div>
                            </c:if>
                            <c:if test="${sample.fileuploadStatus eq 'NO'}">
                                <div class="form-group col-md-6 text-center" style="border-bottom: 1px solid darkgray; padding: 15px;">
                                    <label>No Files Attached by 
                                        <c:if test="${sample.mstLab.labType eq 'CSL'}">
                                            CSL
                                        </c:if>
                                        <c:if test="${sample.mstLab.labType eq 'RND'}">
                                            RND
                                        </c:if>
                                        Lab</label>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                    <br/>
                </form>
                <iframe id="pdf" class="reviewPDF">
                    <p>Your Browser Does not Support Iframe</p>
                </iframe>
            </div>
            <div class="form-group centre-btn form-inline">
                <!--<a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?status=3&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="top" title="Summary of samples for which test results have already been entered and Sent back to Tse" class="btn-primary form-control" style="margin-right: 20px; text-decoration: none">GO BACK</a>-->
                <a onclick="window.close()" data-toggle="tooltip" data-placement="top" title="Summary of samples for which test results have already been entered and Sent back to Tse" class="btn-primary form-control cursorOnHover" style="margin-right: 20px; text-decoration: none">CLOSE</a>
                <input data-toggle="tooltip" data-placement="top" title="Click to Download PDF" id="viewPDF" type="button" class="btn btn-primary input-group" style="margin-right: 20px" value="Download PDF">
                <input data-toggle="tooltip" data-placement="top" title="Click to Edit Email" id="openEMail" type="button" class="btn btn-primary input-group" style="margin-right: 20px" value="Edit Mail">
                <input data-toggle="tooltip" data-placement="top" title="Send Sample Report to Customer (click 'Download PDF' button before sending  mail)" id="reviewSendToCustomerBtn" type="submit" class="btn btn-success input-group" value="REVIEW | SUBMIT">
            </div>
        </div>
        <script>
            var pdf = document.getElementById("pdf");
            $(document).on('click', '#openEMail', function () {
                $('#emailModal').modal('show');
            });
            var base = "${SendTo}";
            var regex = new RegExp("^" + base, "i");
            $(document).on("input", '#fm1', function (ev) {
                var query = $(this).val();
                if (!regex.test(query)) {
                    //ev.preventDefault();
                    $(this).val(base);
                }
            });
            var base1 = "${SendCC}";
            var regex1 = new RegExp("^" + base1, "i");
            $(document).on("input", '#fm2', function (ev) {
                var query = $(this).val();
                if (!regex1.test(query)) {
                    //ev.preventDefault();
                    $(this).val(base1);
                }
            });
            var base2 = "The Sample Id: ${gpsd[0].sampleId} has been Tested. ";
            var regex2 = new RegExp("^" + base2, "i");
            $(document).on("input", '#fm4', function (ev) {
                var query = $(this).val();
                if (!regex2.test(query)) {
                    //ev.preventDefault();
                    $(this).val(base2);
                }
            });
            function validateEmail(email) {
                var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(String(email).toLowerCase());
            }
            $(document).on('change', 'input[name="fm1"]', function () {
                var stats = false;
                $.each($(this).val().split(','), function (i, v) {
                    if (validateEmail(v.trim())) {
                        stats = true;
                    } else {
                        $.alert({
                            title: 'Invalid Input',
                            content: 'Please enter a valid email',
                            type: 'red',
                            typeAnimated: true
                        });
                        return false;
                    }
                });
                if (stats) {
                    $('input[name="fm1Value"]').val($(this).val());
                }
            });
            $(document).on('change', 'input[name="fm2"]', function () {
                var stats = false;
                $.each($(this).val().split(','), function (i, v) {
                    if (validateEmail(v.trim())) {
                        stats = true;
                    } else {
                        $.alert({
                            title: 'Invalid Input',
                            content: 'Please enter valid emails',
                            type: 'red',
                            typeAnimated: true
                        });
                        return false;
                    }
                });
                if (stats) {
                    $('input[name="fm2Value"]').val($(this).val());
                }
            });
            //            $(document).on('change', 'input[name="fm3"]', function () {
            //                $('input[name="fm3Value"]').val($(this).val());
            //            });
            $(document).on('change', 'textarea[name="fm4"]', function () {
                $('input[name="fm4Value"]').val($(this).val());
            });
            $("#reviewSendToCustomerBtn").click(function (event) {
                if (checkRemarks()) {
                    event.preventDefault();
                    var funcStr = window.location.hash.replace(/#/g, '') || 'html';
                    var doc = createPDF[funcStr]("${gpsd[0].sampleId}", $('#remarks').val(), false);
                    $("#pdf").attr('src', doc.output('datauristring'));
                    $("#reviewModal").modal('show');
                    $("#reviewModal .modal-lg-review .modal-content .modal-body-review").html("");
                    $("#pdf").show();
                    $("#pdf").appendTo("#reviewModal .modal-lg-review .modal-content .modal-body-review");
                } else {
                    return false;
                }
            });
            $("#viewPDF").click(function (event) {
                if (checkRemarks()) {
                    var funcStr = window.location.hash.replace(/#/g, '') || 'html';
                    var doc = createPDF[funcStr]("${gpsd[0].sampleId}", $('#remarks').val());
                    doc.save('${gpsd[0].sampleId}.pdf');
                }
            });
            function getSampleCount(sampleId) {
                var i = 0;
                $.ajax({
                    async: false,
                    url: '/ServoCMP/Tse/redirectController?url=getSampleCount&sampleId=' + sampleId,
                    type: 'GET',
                    dataType: 'JSON',
                    success: function (data) {
                        i = data;
                    },
                    error: function (error) {
                        console.log(error.responseText);
                        i = -1;
                    }
                });
                return i;
            }
            $("#submitBtn").click(function (event) {
                event.preventDefault();
                var remks = $('#remarks').val();
                if (remks === '' || remks === null) {
                    $.alert({
                        title: '',
                        content: 'Kindly Enter Your Remarks',
                        type: 'red',
                        typeAnimated: true
                    });
                    return false;
                }
                var j = getSampleCount("${gpsd[0].sampleId}");
                if (j == 2) {
                    $.alert({
                        title: 'Sample Pending(1 out of 2)',
                        content: 'Kindly Wait for the other lab to send their sample',
                        type: 'red',
                        typeAnimated: true
                    });
                    return false;
                } else if (j == -1) {
                    $.alert({
                        title: 'Error',
                        content: 'Something went Wrong',
                        type: 'red',
                        typeAnimated: true
                    });
                    return false;
                }

                $.confirm({
                    title: 'Confirm',
                    content: 'Are you sure? You want to Submit',
                    type: 'red',
                    typeAnimated: true,
                    buttons: {
                        Yes: {
                            btnClass: 'btn-red',
                            action: function () {
                                var funcStr = window.location.hash.replace(/#/g, '') || 'html';
                                var doc1 = createPDF[funcStr]("${gpsd[0].sampleId}", $('#remarks').val());
                                var test = "${gpsd[1].sampleId}";
                                var doc2 = "";
                                if (test) {
                                    doc2 = createPDF[funcStr]("${gpsd[1].sampleId}", $('#remarks').val());
                                }
                                var data = new FormData($('.sendToCustomer')[0]);
                                data.append("doc1", doc1.output('blob'));
                                if (test) {
                                    data.append("doc2", doc2.output('blob'));
                                }
                                data.append("labCode1", "${gpsd[0].mstLab.labCode}");
                                data.append("labCode2", "${gpsd[1].mstLab.labCode}");
                                event.preventDefault();
                                $.ajax({
                                    type: "POST",
                                    url: "${pageContext.request.contextPath}/Tse/SendToCustomer",
                                    processData: false,
                                    data: data,
                                    contentType: false,
                                    cache: false,
                                    success: function (data) {
                                        showResponseDialog(data);
                                        $('#responseDialog').modal('show');
                                        $("#submitBtn").prop("disabled", true);
                                    },
                                    error: function (e) {
                                        showResponseDialog();
                                        $('#responseDialog').modal('show');
                                    }
                                });
                            }
                        },
                        No: {
                            btnClass: 'btn-red',
                            action: function () {
                                $.alert({
                                    title: '',
                                    content: 'No Changes were made to the Sample',
                                    type: 'red',
                                    typeAnimated: true
                                });
                            }
                        }
                    }
                });
                $('#responseDialog').on('hidden.bs.modal', function () {
                    //                  window.location.href = "${pageContext.request.contextPath}/Tse/GetCreatedSamples?status=3";
                    window.close();
                });
            });
            console.log(${graphData});
            var graphData = ${graphData};
            var plotOptions = {
                line: {
                    dataLabels: {
                        rotation: '-40',
                        align: 'centre',
                        enabled: true,
                        format: '({y}, {x:%d-%m-%Y})',
                        crop: false,
                        overflow: true
                    },
                    enableMouseTracking: true
                }
            };
            var credits = {
                enabled: false
            };
            var chart = {
                borderWidth: 1,
                borderColor: 'black',
                plotBorderWidth: 1,
                borderRadius: 0,
                showAxes: true,
                type: 'line'
            };
            var legends = {
                backgroundColor: '#FCFFC5',
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'bottom',
                borderRadius: 5
            };
            var xAxis = {
                crosshair: true,
                tickPositioner: function () {
                    var positions = [],
                            tick = Math.floor(this.dataMin),
                            increment = Math.ceil((this.dataMax - this.dataMin) / 3);
                    if (this.dataMax !== null && this.dataMin !== null) {
                        for (tick; tick - increment < this.dataMax; tick += increment) {
                            positions.push(tick);
                        }
                        if (increment === 0)
                            positions.push(tick);
                    }
                    return positions;
                },
                labels: {
                    format: '{value:%d-%m-%Y}',
                    style: {"color": "#000000", fontWeight: 'bold'}
                },
                lineWidth: 3,
                lineColor: "#000000",
                tickWidth: 2,
                tickColor: "#000000",
                allowDecimals: true,
                type: 'datetime',
                title: {
                    text: 'Date of Sample',
                    useHTML: false,
                    style: {"color": "#000000", "backgroundColor": "#000000", "fontSize": "14px", fontWeight: 'bold'}
                }
            };



            $(document).ready(function () {
                var alert = "${alerts}";
                if (alert) {
                    $.alert({
                        title: 'Result Status',
                        content: alert,
                        type: 'red',
                        typeAnimated: true
                    });
                    $('#reviewSendToCustomerBtn').attr('disabled','true');
                    $('#reviewSendToCustomerBtn').attr('title',alert);
                }
                

                (function (H) {
                    H.Chart.prototype.createCanvas = function (divId) {
                        var svg = this.getSVG(),
                                width = parseInt(svg.match(/width="([0-9]+)"/)[1]),
                                height = parseInt(svg.match(/height="([0-9]+)"/)[1]),
                                canvas = document.createElement('canvas');
                        canvas.setAttribute('width', width);
                        canvas.setAttribute('height', height);
                        if (canvas.getContext && canvas.getContext('2d')) {
                            canvg(canvas, svg);
                            return canvas.toDataURL("image/jpeg");
                        } else {
                            alert("Your browser doesn't support this feature, please use a modern browser");
                            return false;
                        }
                    };
                }(Highcharts));
                $.each(graphData, function (i, data) {
                    var testData = [];
                    for (var j = 0; j < data.testData.length; j++) {
                        var temp = Date.UTC(data.testData[j].date.split("-")[2], parseInt(data.testData[j].date.split("-")[1]) - 1, data.testData[j].date.split("-")[0]);
                        testData.push([temp, parseFloat(data.testData[j].val)]);
                    }
                    var testName = data.testName;
                    var testMinVal = data.minVal;
                    var testMaxVal = data.maxVal;
                    var testMinRangeVal = data.minRangeVal;
                    var testMaxRangeVal = data.maxRangeVal;
                    var htmlDiv = "<div class='row'><div id='" + testName.replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '-') + "Graph' class='charts'></div></div>";
                    $('.graphDiv').append(htmlDiv);
                    testSeries = [{
                            name: testName,
                            data: testData
                        }];
                    var IdName = "#" + testName.replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '-') + "Graph";
                    $(IdName).highcharts({
                        chart: chart,
                        xAxis: xAxis,
                        legend: legends,
                        plotOptions: plotOptions,
                        series: testSeries,
                        credits: credits,
                        navigation: {
                            buttonOptions: {
                                enabled: false
                            }
                        },
                        title: {
                            text: 'Sample Date vs test Id ' + testName, //Test Name
                            useHTML: false,
                            style: {"color": "#000000", "backgroundColor": "#00FF00", "fontSize": "18px", fontWeight: 'bold'},
                            border: '2px solid black'
                        },
                        subtitle: {
                            text: 'Recent Data of test Id ' + testName, //Test Name
                            style: {"color": "#000000", "backgroundColor": "#000000", "fontSize": "12px", fontWeight: 'bold'}
                        },
                        yAxis: {
                            crosshair: true,
                            title: {
                                text: 'test Id ' + testName, //Test Name
                                useHTML: false,
                                style: {"color": "#000000", "backgroundColor": "#000000", "fontSize": "14px", fontWeight: 'bold'},
                                border: '1px solid black'
                            },
                            labels: {
                                style: {"color": "#0000FF", fontWeight: 'bold'}
                            },
                            lineWidth: 3,
                            lineColor: "#000000",
                            tickWidth: 2,
                            tickColor: "#000000",
                            min: testMinRangeVal,
                            max: testMaxRangeVal,
                            plotLines: [{
                                    value: testMinVal,
                                    color: 'green',
                                    dashStyle: 'shortdash',
                                    width: 2,
                                    label: {
                                        text: 'Min Value (' + testMinVal + ')',
                                        style: {"color": "#008000", fontWeight: 'bold'}
                                    }
                                }, {
                                    value: testMaxVal,
                                    color: 'red',
                                    dashStyle: 'shortdash',
                                    width: 2,
                                    label: {
                                        text: 'Max Value (' + testMaxVal + ')',
                                        style: {"color": "#FF0000", fontWeight: 'bold'}
                                    }
                                }]
                        }
                    });
                });
                //                $('#kv40Graph').highcharts({
                //                    chart: chart,
                //                    xAxis: xAxis,
                //                    legend: legends,
                //                    plotOptions: plotOptions,
                //                    series: kv40Series,
                //                    credits: credits,
                //                    navigation: {
                //                        buttonOptions: {
                //                            enabled: false
                //                        }
                //                    },
                //                    title: {
                //                        text: 'Sample Date vs KV@40',
                //                        useHTML: false,
                //                        style: {"color": "#000000", "backgroundColor": "#00FF00", "fontSize": "18px", fontWeight: 'bold'},
                //                        border: '2px solid black'
                //                    },
                //                    subtitle: {
                //                        text: 'Recent Data of KV@40',
                //                        style: {"color": "#000000", "backgroundColor": "#000000", "fontSize": "12px", fontWeight: 'bold'}
                //                    },
                //                    yAxis: {
                //                        crosshair: true,
                //                        title: {
                //                            text: 'KV@40',
                //                            useHTML: false,
                //                            style: {"color": "#000000", "backgroundColor": "#000000", "fontSize": "14px", fontWeight: 'bold'},
                //                            border: '1px solid black'
                //                        },
                //                        labels: {
                //                            style: {"color": "#0000FF", fontWeight: 'bold'}
                //                        },
                //                        lineWidth: 3,
                //                        lineColor: "#000000",
                //                        tickWidth: 2,
                //                        tickColor: "#000000",
                //                        min: kv40minRangeVal,
                //                        max: kv40maxRangeVal,
                //                        plotLines: [{
                //                                value: kv40minVal,
                //                                color: 'green',
                //                                dashStyle: 'shortdash',
                //                                width: 2,
                //                                label: {
                //                                    text: 'Min Value (' + kv40minVal + ')',
                //                                    style: {"color": "#008000", fontWeight: 'bold'}
                //                                }
                //                            }, {
                //                                value: kv40maxVal,
                //                                color: 'red',
                //                                dashStyle: 'shortdash',
                //                                width: 2,
                //                                label: {
                //                                    text: 'Max Value (' + kv40maxVal + ')',
                //                                    style: {"color": "#FF0000", fontWeight: 'bold'}
                //                                }
                //                            }]
                //                    }
                //                });
                ////
                //                $('#viscocityIndexGraph').highcharts({
                //                    chart: chart,
                //                    xAxis: xAxis,
                //                    legend: legends,
                //                    series: viscocityIndexSeries,
                //                    plotOptions: plotOptions,
                //                    credits: credits,
                //                    navigation: {
                //                        buttonOptions: {
                //                            enabled: false
                //                        }
                //                    },
                //                    title: {
                //                        text: 'Sample Date vs Viscocity Index',
                //                        useHTML: false,
                //                        style: {"color": "#000000", "backgroundColor": "#00FF00", "fontSize": "18px", fontWeight: 'bold'},
                //                        border: '2px solid black'
                //                    },
                //                    subtitle: {
                //                        text: 'Recent Data of Viscosity Index',
                //                        style: {"color": "#000000", "backgroundColor": "#000000", "fontSize": "12px", fontWeight: 'bold'}
                //                    },
                //                    yAxis: {
                //                        crosshair: true,
                //                        title: {
                //                            text: 'VI',
                //                            useHTML: false,
                //                            style: {"color": "#000000", "backgroundColor": "#000000", "fontSize": "14px", fontWeight: 'bold'},
                //                            border: '1px solid black'
                //                        },
                //                        lineWidth: 3,
                //                        lineColor: "#000000",
                //                        tickWidth: 2,
                //                        tickColor: "#000000",
                //                        min: viscocityIndexminRangeVal,
                //                        max: viscocityIndexmaxRangeVal,
                //                        labels: {
                //                            style: {"color": "#0000FF", fontWeight: 'bold'}
                //                        },
                //                        plotLines: [{
                //                                value: viscocityIndexminVal,
                //                                color: 'green',
                //                                dashStyle: 'shortdash',
                //                                width: 2,
                //                                label: {
                //                                    text: 'Min Value (' + viscocityIndexminVal + ')',
                //                                    style: {"color": "#008000", fontWeight: 'bold'}
                //                                }
                //                            }]
                //                    }
                //                });
                //
                //                $('#tanGraph').highcharts({
                //                    chart: chart,
                //                    xAxis: xAxis,
                //                    legend: legends,
                //                    series: tanSeries,
                //                    plotOptions: plotOptions,
                //                    credits: credits,
                //                    navigation: {
                //                        buttonOptions: {
                //                            enabled: false
                //                        }
                //                    },
                //                    title: {
                //                        text: 'Sample Date vs TAN',
                //                        useHTML: false,
                //                        style: {"color": "#000000", "backgroundColor": "#00FF00", "fontSize": "18px", fontWeight: 'bold'},
                //                        border: '2px solid black'
                //                    },
                //                    subtitle: {
                //                        text: 'Recent Data of TAN',
                //                        style: {"color": "#000000", "backgroundColor": "#000000", "fontSize": "12px", fontWeight: 'bold'}
                //                    },
                //
                //                    yAxis: {
                //                        crosshair: true,
                //                        title: {
                //                            text: 'TAN (mgKOH/gm)',
                //                            useHTML: false,
                //                            style: {"color": "#000000", "backgroundColor": "#000000", "fontSize": "14px", fontWeight: 'bold'},
                //                            border: '1px solid black'
                //                        },
                //                        lineWidth: 3,
                //                        lineColor: "#000000",
                //                        tickWidth: 2,
                //                        tickColor: "#000000",
                //                        min: TanMinRangeVal,
                //                        max: TanMaxRangeVal,
                //                        labels: {
                //                            style: {"color": "#0000FF", fontWeight: 'bold'}
                //                        },
                //                        plotLines: [{
                //                                value: TanMaxVal,
                //                                color: 'red',
                //                                dashStyle: 'shortdash',
                //                                width: 2,
                //                                label: {
                //                                    text: 'Max Value (' + TanMaxVal + ')',
                //                                    style: {"color": "#FF0000", fontWeight: 'bold'}
                //                                }
                //                            }]
                //                    }
                //                });

                //                $('#fm1').select2({
                //                    multiple: true,
                //                    placeholder: "To...",
                //                    tags: true,
                //                    allowClear: true,
                //                    dropdownParent: $("#emailModal")
                //                });
                //                $('#fm2').select2({
                //                    multiple: true,
                //                    placeholder: "Cc.....",
                //                    tags: true,
                //                    allowClear: true,
                //                    dropdownParent: $("#emailModal")
                //                });

            });
        </script>
        <div id="emailModal" class="modal fade" role="dialog" data-backdrop='static' aria-labelledby="modal-title">
            <div class="modal-dialog" style="overflow-y: initial !important;width: 50%" role="document">
                <div class="modal-content">
                    <div class="modal-header" style="background: #232f3e;">
                        <button type="button" class="close removeBackdrop" data-dismiss="modal" style="color: white;opacity: 1;">&times;</button>
                        <h4 id="modal-title" class="modal-title" style="color: whitesmoke;">Email</h4>
                    </div>
                    <div class="modal-body" style="height:300px; overflow-y: auto;background: #D9F3FF;">
                        <fieldset>
                            <div class="row" style="margin-bottom: 10px">
                                <div class="col-xs-10" style="display: -webkit-inline-box;">
                                    <span 
                                        style="margin-left: 37px;font-size: 19px;margin-top: pad;position: relative;top: 6px;font-family: monospace; "
                                        >To:</span>&nbsp;&nbsp;&nbsp;<span></span><span style="padding: 6px"></span>
                                    <input class="form-control" id="fm1" name="fm1"  placeholder="To..." value="${SendTo}"
                                           style="border-radius: 20px;background: #D9F3FF;border: 0px;"
                                           >
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 10px">
                                <div class="col-xs-10" style="display: -webkit-inline-box;">
                                    <span 
                                        style="margin-left: 38px;font-size: 19px;margin-top: pad;position: relative;top: 6px;font-family: monospace; "
                                        >Cc:</span>&nbsp;&nbsp;&nbsp;<span style="padding: 6px"></span>
                                    <input class="form-control" id="fm2" name="fm2" placeholder="Cc.." value="${SendCC}" 
                                           style="border-radius: 20px;background: #D9F3FF;border: 0px;"
                                           />
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 25px">
                                <div class="col-xs-10">
                                    <span
                                        style="margin-right: 18px;font-size: 16px;margin-top: pad;position: relative;top: 6px;font-family: monospace;"
                                        >Subject:</span>&nbsp;&nbsp;&nbsp;<span 
                                        style="font-size: 14px;position: relative;top: 5px;font-family: sans-serif;font-style: italic;"
                                        >${SendSubject}</span>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 6px">
                                <div class="col-xs-10" style="display: -webkit-inline-box;">
                                    <span 
                                        style="margin-left: 20px;font-size: 19px;margin-top: pad;position: relative;top: 6px;font-family: monospace; "
                                        >Body:</span>&nbsp;&nbsp;&nbsp;<small></small><span style="padding: 6px"></span>
                                    <textarea row="3" name="fm4" id="fm4" class="form-control" placeholder="Body.." 
                                              style="border-radius: 20px;background: #D9F3FF;border: 0px;"
                                              >The Sample Id: ${gpsd[0].sampleId} has been Tested.</textarea>
                                </div>
                            </div>
                        </fieldset>
                    </div>
                    <div class="modal-footer" style="background: #D9F3FF;">
                        <span style="float: left" class="text-info">IndianOil.in</span>
                        <button type="button" class="btn btn-default removeBackdrop" data-dismiss="modal">Save</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
