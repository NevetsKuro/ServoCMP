<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Test Specification"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">TEST SPECIFICATION MASTERS</h4>
            
            <div class="row" style="margin-top: 5px;">
                <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">Filter:</div>
                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                    <label style="padding-left: 7px;">Select Product <span class="text-danger">*</span></label>
                    <select id="prodFilter" name="productId" style="width: 100%; box-shadow: none" class="form-control">
                        <option value="">Select Product</option>
                        <c:forEach items="${mPro}" var="pro">
                            <option value="${pro.proId}">(${pro.proId}) ${pro.proName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-xs-1" style="padding-top: 10px">
                    <button id="filterSearch" class="btn btn-default btn-success" data-toggle="tooltip" data-placement="bottom" title="Search Records by Product Name">Search By</button>
                </div>
                <div class="col-xs-1" style="padding-top: 10px">
                    <button class ="btn btn-primary btn-group-sm" onclick="AddTestSpec('Add Test Specification')" data-toggle="tooltip" data-placement="bottom" title="Add Test Specifications to Master Records">Add Test Specification</button>
                </div>
            </div>
            <br/><br/>
            <table id="TestSpecTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                <thead>
                    <tr>
                        <th>Product Name</th>
                        <th>Test Name</th>
                        <th>Value Check</th>
                        <th>MIN</th>
                        <th>MAX</th>
                        <th>EQUAL</th>
                        <th>DEVIATION</th>
                        <th>MAX (DELIMITER)</th>
                        <th>OTHER</th>
                        <th style="width: 10%">Updated By</th>
                        <th style="width: 15%">Updated Date</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
            <div id="UpdateTestSpec" class="modal fade" role="dialog" data-backdrop='static'>
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form role="form" class="form-inline" action="#" method="POST" id="updateTestSpecForm">
                            <input type="hidden" id="testId" name="testId"/>
                            <input type="hidden" id="proId" name="proId"/>
                            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                                <h4 id="modal-title-Test-Spec" class="modal-title"></h4>
                            </div>
                            <div class="modal-body container-fluid">
                                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <label>Product Name <span class="text-danger">*</span></label>
                                    <select id="proName" name="proName" style="width: 100%; box-shadow: none" class="form-control" required onchange="getTest(this.value)">
                                        <option value="">Select Product</option>
                                        <c:forEach items="${mPro}" var="mPro">
                                            <option value="${mPro.proId}" ${mPro.proId == param.proName ? 'selected':''}>${mPro.proName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Test Name <span class="text-danger">*</span></label>
                                    <select id="testName" name="testName" style="width: 100%; box-shadow: none" class="form-control">
                                        <option value="">Select Test Name</option>
                                        <c:forEach items="${mTest}" var="mTest">
                                            <option value="${mTest.testId}">${mTest.testName}</option>
                                        </c:forEach>
                                    </select>
                                    <input id="editTestName" name="editTestName" data-testid="" type="text" readonly class="form-control"/>
                                </div>
                                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Check For <span class="text-danger">*</span></label>
                                    <select id="valChk" name="valChk" style="width: 100%; box-shadow: none" class="form-control" onchange="showRelField(this.value)" required>
                                        <option value="">Select Check For</option>
                                        <c:forEach items="${valChk}" var="valChk">
                                            <option value="${valChk.testId}">${valChk.spec}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="floating-label-form-group floating-label-form-group-with-value col-xs-offset-4 col-xs-4" id="minVal" style="display: none; border-left: none">
                                    <label>Enter Minimum Value *</label>
                                    <input class="form-control" placeholder="Enter Minimum Value *" type="text" id="minValId" name="minVal" onkeypress="javascript:return isNumber(event)" maxlength="8" required/>
                                </div>
                                <div class="floating-label-form-group floating-label-form-group-with-value col-xs-offset-4 col-xs-4" id="maxVal" style="display: none; border-left: none">
                                    <label>Enter Maximum Value <span class="text-danger">*</span></label>
                                    <input class="form-control" placeholder="Enter Maximum Value *"  type="text" id="maxValId" name="maxVal" onkeypress="javascript:return isNumber(event)" maxlength="8" required/>
                                </div>
                                <div id="minMaxVal" style="display: none">
                                    <div class="floating-label-form-group floating-label-form-group-with-value col-xs-offset-2 col-xs-4">
                                        <label>Enter Minimum Value <span class="text-danger">*</span></label>
                                        <input class="form-control" placeholder="Enter Minimum Value *" type="text" id="minMaxMinValId" name="minMinVal" onkeypress="javascript:return isNumber(event)" maxlength="8" required/>
                                    </div>
                                    <div class="floating-label-form-group floating-label-form-group-with-value col-xs-4">
                                        <label>Enter Maximum Value <span class="text-danger">*</span></label>
                                        <input class="form-control" placeholder="Enter Maximum Value *" type="text" id="minMaxMaxValId" name="minMaxVal" onkeypress="javascript:return isNumber(event)" maxlength="8" required/>
                                    </div>
                                </div>
                                <div id="eqVal" style="display: none; border-left: none">
                                    <div class="floating-label-form-group floating-label-form-group-with-value col-xs-offset-2 col-xs-4">
                                        <label>Enter Equal to Value</label>
                                        <input class="form-control" placeholder="Enter Equal to *" type="text" id="eqValId" name="eqVal" onkeypress="javascript:return isNumber(event)" maxlength="8" required/>
                                    </div>
                                    <div class="floating-label-form-group floating-label-form-group-with-value col-xs-4">
                                        <label>Enter Deviation Value</label>
                                        <input class="form-control" placeholder="Enter Deviation *" type="text" id="devValId" name="devVal" onkeypress="javascript:return isNumber(event)" maxlength="8" required/>
                                    </div>
                                </div>
                                <div id="othVal" style="display: none">
                                    <div class="form-group floating-label-form-group col-xs-5 floating-label-form-group-with-value">
                                        <label>Other Possible Values *</label>
                                        <input id="othVal1" type="text" class="form-control" placeholder="No Other Values Defined Kindly Add Other Values" name="othVal" style="width:100%" disabled required/>
                                    </div>
                                    <div class="col-xs-4" style="margin-bottom: auto; margin-top: 25px">
                                        <span id="otherValuesPage" class="glyphicon glyphicon-plus"></span>
                                    </div>
                                </div>

                                <div class="floating-label-form-group floating-label-form-group-with-value col-xs-offset-4 col-xs-4" id="maxDelVal" style="display: none; border-left: none">
                                    <label>Enter Maximum Value with Delimiter <span class="text-danger">*</span></label>
                                    <input class="form-control" placeholder="Enter Maximum Value with Delimiter *" type="text" id="maxDevValId" name="maxDelVal" onkeypress="javascript:return isNumber(event)" required/>
                                </div>
                            </div>
                            <br/>
                            <br/>
                            <div class="modal-footer">
                                <span style="float: left" class="text-info">IndianOil.in</span>
                                <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                                <button type="submit" id="testSpecSubmit" class="btn btn-default btn-success"></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
<%--
Body of table
<c:forEach items="${testSpecs}" var="testSpec">
                        <tr>
                            <td class="NevsLeft">${testSpec.proName}</td>
                            <td class="NevsLeft">
                                ${testSpec.testName}
                                <a href="#" onclick="AddTestSpec('Update Test', '${testSpec.proName}', '${testSpec.testName}', ${testSpec.testId}, ${testSpec.proId}, this, ${testSpec.mstTestParam.checkId})" data-toggle="tooltip" data-placement="right" title="Edit ${testSpec.testName} Test" style=" text-decoration: none; float: right">
                                    <span class="glyphicon glyphicon-pencil"></span>
                                </a>
                            </td>
                            <td>${testSpec.spec}</td>
                            <td>${testSpec.mstTestParam.minValue}</td>
                            <td>
                                <c:if test="${not fn:contains(testSpec.mstTestParam.maxValue, '/')}">
                                    ${testSpec.mstTestParam.maxValue}
                                </c:if>
                            </td>
                            <td>${testSpec.mstTestParam.typValue}</td>
                            <td>${testSpec.mstTestParam.devValue}</td>
                            <td>
                                <c:if test="${fn:contains(testSpec.mstTestParam.maxValue, '/')}">
                                    ${testSpec.mstTestParam.maxValue}
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${testSpec.mstTestParam.otherVal eq 'Yes'}">
                                    <span data-toggle="tooltip" data-placement="bottom" title="${otherVal}">${testSpec.mstTestParam.otherVal}</span>
                                </c:if>
                                <c:if test="${testSpec.mstTestParam.otherVal ne 'Yes'}">
                                    ${testSpec.mstTestParam.otherVal}
                                </c:if>
                            <td><a href="#" onclick="openHelloIOCian(${testSpec.updatedBy})">${testSpec.updatedBy}</a></td>
                            <td>${testSpec.updatedDate}</td>
                        </tr>
                    </c:forEach>
--%>