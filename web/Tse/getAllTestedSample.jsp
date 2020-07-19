<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Sample Send by Lab."/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="text-center">
            <label class="text-info"><u>PRIORITY</u>&nbsp; &rarr;&nbsp; </label>
            <span class="badge priorityLevel3">NORMAL</span>
            <span class="badge priorityLevel2">MEDIUM</span>
            <span class="badge priorityLevel1">HIGH</span>
            <span class="spacespace"></span>
            <label class="text-info"><u>CSL LAB Type</u>&nbsp; &rarr;&nbsp; </label>
            <span class="defFont" style='color:mediumvioletred'><i class='fas fa-flask'></i> CSL</span>
            <span class="spacespace"></span>
            <label class="text-info"><u>RND LAB Type</u>&nbsp; &rarr;&nbsp; </label>
            <span class="defFont" style='color:royalblue'><i class='fas fa-flask'></i> RND</span>
            <span class="spacespace"></span>
            <label class="text-info"><u>BOTH LAB/RND Type</u>&nbsp; &rarr;&nbsp; </label>
            <span style='color:#3B1780;border-color:#3B1780;' class='defFont'><i class='fas fa-flask'></i> CSL/RND</span>
        </div>
        <div class="container-fluid">
            <input type="hidden" id="sampleType" name="sampleType" value="${sampleType}">
            <h4 class="text-center text-info" style="text-decoration: underline">${sampleType} Samples Sent by LAB</h4>
            <div class="row">
                <div class="col-md-3" style="padding-top: 5px;padding-right: 10px;">
                    <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">Filter:</div>
                    <div class="form-group col-lg-12 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                        <label style="padding-left: 7px;">Select Customer</label>
                        <select id="custFilter" name="custId" style="width: 100%; box-shadow: none" class="form-control selectFilter">
                            <option value="">All Customer</option>
                            <c:forEach items="${mCus}" var="cus">
                                <option value="${cus.customerId}">${cus.customerName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="col-md-3" style="padding-top: 5px;padding-right: 10px;">
                    <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">&nbsp;</div>
                    <div class="form-group col-lg-12 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                        <label style="padding-left: 7px;">Select Department</label>
                        <select id="deptFilter" name="deptId" style="width: 100%; box-shadow: none" class="form-control selectFilter">
                            <option value="">All Department</option>
                            <c:forEach items="${mDept}" var="dep">
                                <option value="${dep.departmentId}">${dep.departmentName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="col-md-3" style="padding-top: 5px;padding-right: 10px;">
                    <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">&nbsp;</div>
                    <div class="form-group col-lg-12 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                        <label style="padding-left: 7px;">Select Application</label>
                        <select id="appFilter" name="appId" style="width: 100%; box-shadow: none" class="form-control selectFilter ">
                            <option value="">All Application</option>
                            <c:forEach items="${mApp}" var="app">
                                <option value="${app.appId}">${app.appName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="col-md-3" style="padding-top: 5px;padding-right: 10px;">
                    <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">&nbsp;</div>
                    <div class="form-group col-lg-12 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                        <label style="padding-left: 7px;">Select Product</label>
                        <select id="prodFilter" name="productId" style="width: 100%; box-shadow: none" class="form-control">
                            <option value="">All Product</option>
                            <c:forEach items="${mPro}" var="pro">
                                <option value="${pro.proId}">${pro.proName}</option>
                            </c:forEach>
                        </select>
                    </div>

                </div>
                <div class="col-1" style="margin-top: 30px">
                    <div class="col-xs-1" style="padding-top: 10px">
                        <button id="filterSearchfrSC" class="btn btn-default btn-success" data-status="3" data-toggle="tooltip" data-placement="bottom" title="Search Records by Product Name">Search By</button>
                    </div>
                </div>
            </div>
            <br>
            <br>
            <table id="sentByLab" class="table table-bordered table-hover tbCenter" cellspacing="0" width="100%">
                <thead style="font-size: x-small">
                <th style="width: 85px">LAB TYPE</th>
                <th style="width: 80px;">SAMPLE ID</th>
                <th style="width: 201px;">CUSTOMER NAME</th>
                <th style="width: 75px;">DEPARTMENT</th>
                <th style="width: 75px;">APPLICATION</th>
                <th style="width:50px">TANK NO</th>
                <th>EQUIPMENT</th>
                <th>PRODUCT</th>
                <th style="width:50px">QTY. DRAWN (Ltr.)</th>
                <th style="width:60px">DRAWN DATE</th>
                <th style="width:60px">CREATED ON</th>
                </thead>
                <tbody class="samplebody cursorOnHover" style="font-size:small">

                </tbody>
            </table>
        </div>
        <script>
            $(document).ready(function () {
                $(document).on('click', '#sentByLab > tbody > tr', function () {
//                    var id = $('#sentToCustomer > tbody > tr:nth-child(1)').find('td:nth-child(1)').html().trim();
                    var id = $(this).find('td:nth-child(2)').html().trim();
                    var labCode = $(this).data('labCode');
                    var sampleType = $('#sampleType').val();
                    if (sampleType) {
                        window.open('${pageContext.request.contextPath}/Tse/GetTestResultsDetails?smplid=' + id + '&sampleType=' + sampleType + '&labCode=' + labCode + '&type=WRITE&csrftoken=${sessionScope.csrfToken}');
                    }
                });

            });
        </script>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
    </html>
<%--<c:forEach items="${InProcessTseSampleDetails}" var="data" varStatus="count">
<tr style="font-size:small" class="priorityLevel${data.samplepriorityId}">
    <td class="text-center">
        <a href="${pageContext.request.contextPath}/Tse/GetTestResultsDetails?smplid=${data.sampleId}&type=WRITE" title="Click to edit Sample details sent to Lab"> ${data.sampleId}</a>
    </td>
    <td class="text-left">${data.mstDept.mstCustomer.customerName}</td>
    <td class="text-left">${data.mstDept.departmentName}</td>
    <td class="text-left">${data.mstApp.appName}</td>
    <td class="text-center">${data.tankNo}</td>
    <td class="text-left">${data.mstEquip.equipmentName}</td>
    <td class="text-left">${data.mstProd.proName}</td>
    <td class="text-center">${data.qtyDrawn}</td>
    <td class="text-center">${data.stringsamplecreatedDate}</td>
</tr>
</c:forEach>--%>