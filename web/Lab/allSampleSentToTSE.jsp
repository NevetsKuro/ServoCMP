<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Sample(s) Sent To TSE"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="text-center">
            <h4 class="text-center text-info text-primary">Sample(s) sent to Tse</h4>
            <label class="text-info"><u>PRIORITY</u>&nbsp; &rarr;&nbsp; </label>
            <span class="badge priorityLevel3">NORMAL</span>
            <span class="badge priorityLevel2">MEDIUM</span>
            <span class="badge priorityLevel1">HIGH</span>
            <span class="spacespace"></span>
            <label class="text-info"><u> One-Time Sample Type</u>&nbsp; &rarr;&nbsp; </label>
            <span class="defFont" style='color:mediumvioletred'><i class='glyphicon glyphicon-file'></i></span>
            <span class="spacespace"></span>
            <label class="text-info"><u> CMP Sample Type</u>&nbsp; &rarr;&nbsp; </label>
            <span class="defFont" style='color:royalblue'><i class='glyphicon glyphicon-book'></i></span>
        </div>
        <div class="row">
            <div class="col-md-3" style="padding-top: 5px;padding-right: 10px;">
                <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">Filter:</div>
                <div class="form-group col-lg-12 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                    <label style="padding-left: 7px;">Select Customer<span class="text-danger">*</span></label>
                    <select id="custFilter" name="custId" style="width: 100%; box-shadow: none" class="form-control selectFilter">
                        <option value="">Select Customer</option>
                        <c:forEach items="${mCus}" var="cus">
                            <option value="${cus.customerId}">${cus.customerName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-md-3" style="padding-top: 5px;padding-right: 10px;">
                <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">&nbsp;</div>
                <div class="form-group col-lg-12 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                    <label style="padding-left: 7px;">Select Department<span class="text-danger">*</span></label>
                    <select id="deptFilter" name="deptId" style="width: 100%; box-shadow: none" class="form-control selectFilter">
                        <option value="">Select Department</option>
                        <c:forEach items="${mDept}" var="dep">
                            <option value="${dep.departmentId}">${dep.departmentName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-md-3" style="padding-top: 5px;padding-right: 10px;">
                <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">&nbsp;</div>
                <div class="form-group col-lg-12 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                    <label style="padding-left: 7px;">Select Application<span class="text-danger">*</span></label>
                    <select id="appFilter" name="appId" style="width: 100%; box-shadow: none" class="form-control selectFilter ">
                        <option value="">Select Application</option>
                        <c:forEach items="${mApp}" var="app">
                            <option value="${app.appId}">${app.appName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-md-3" style="padding-top: 5px;padding-right: 10px;">
                <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">&nbsp;</div>
                <div class="form-group col-lg-12 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                    <label style="padding-left: 7px;">Select Product<span class="text-danger">*</span></label>
                    <select id="prodFilter" name="productId" style="width: 100%; box-shadow: none" class="form-control ">
                        <option value="">Select Product</option>
                        <c:forEach items="${mPro}" var="pro">
                            <option value="${pro.proId}">${pro.proName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-1" style="margin-top: 30px">
                <div class="col-xs-1" style="padding-top: 10px">
                    <button id="filterSearchfrLab" class="btn btn-default btn-success" data-status="3" data-toggle="tooltip" data-placement="bottom" title="Search Records by Product Name">Search By</button>
                </div>
            </div>
        </div>
        <br>
        <br>
        <div class="container-fluid body-content">
            <table id="sampleToTse" class="table table-bordered table-hover tbCenter-head cursorOnHoverTable" cellspacing="0" width="100%">
                <thead style="font-size: small">
                <th style="width: 10px">SAMPLING<br/>TYPE</th>
                <th style="width: 80px;"> SAMPLE ID</th>
                <th style="width: 201px;">CUSTOMER NAME</th>
                <th style="width: 75px;">DEPARTMENT</th>
                <th style="width: 75px;">APPLICATION</th>
                <th style="width:50px">TANK NO</th>
                <th>EQUIPMENT</th>
                <th>PRODUCT</th>
                <th style="width:85px">QTY DRAWN(Ltr.)</th>
                <th style="width:77px">DRAWN DATE</th>
                </thead>
                <tbody style="font-size: small">

                </tbody>
            </table>
        </div>
        <script>
            $(document).ready(function() {
                $(document).on('click','#sampleToTse > tbody > tr',function () {
//                    var id = $('#sentToCustomer > tbody > tr:nth-child(1)').find('td:nth-child(1)').html().trim();
                    var id = $(this).find('td:nth-child(2)').html().trim();
                    var labCode = $(this).data('labCode');
                    window.open('${pageContext.request.contextPath}/Lab/getSampleTestResult?smplid='+id+'&labCode='+labCode+'&status=3&csrftoken=${sessionScope.csrfToken}');
                });
                
            });
        </script>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
    </html>
<%--<c:forEach items="${receivedSampleDetails}" var="data" varStatus="count">
                        <tr style="font-size:small" class="priorityLevel${data.samplepriorityId}">
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/Lab/getSampleTestResult?smplid=${data.sampleId}&status=${data.statusId}"> ${data.sampleId}</a>
                            </td>
                            <td class="text-center">${data.mstDept.mstCustomer.customerName}</td>
                            <td class="text-center">${data.mstDept.departmentName}</td>
                            <td class="text-center">${data.mstApp.appName}</td>
                            <td class="text-center">${data.tankNo}</td>
                            <td class="text-center">${data.mstEquip.equipmentName}</td>
                            <td class="text-center">${data.mstProd.proName}</td>
                            <td class="text-center">${data.qtyDrawn}</td>
                            <td class="text-center">${data.stringsamplecreatedDate}</td>
                        </tr>
                    </c:forEach>--%>