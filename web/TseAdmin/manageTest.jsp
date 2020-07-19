<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Test(s)"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">TEST MASTERS</h4>
            <div style="width: 80%; margin-left: auto; margin-right: auto">
                <btn class ="btn btn-primary btn-group-sm" onclick="AddTest('Add Test')" data-toggle="tooltip" data-placement="bottom" title="Add Test to Master Records">Add Test</btn>
                <btn style="float: right" class ="btn btn-primary btn-group-sm right" onclick="removeTest('Remove Test')" data-toggle="tooltip" data-placement="bottom" title="Inactive Test">Removed Tests</btn>
                <br/><br/>
                <table id="TestTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <th>Test Name</th>
                            <th>Test Unit</th>
                            <th>Test Method</th>
                            <th>Sample Qty</th>
                            <th>Display Pos</th>
                            <th style="width: 10%">Updated By</th>
                            <th style="width: 20%">Updated Date</th>
                            <th style="width: 10%">Update Test</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${Tests}" var="data">
                            <tr>
                                <td class="NevsLeft">${data.testName}</td>
                                <td>${data.unit}</td>
                                <td class="NevsLeft">${data.testMethod}</td>
                                <td>${data.sampleqty}</td>
                                <td>${data.dispSeqNo}</td>
                                <td><a href="#" onclick="openHelloIOCian(${data.updatedBy})">${data.updatedBy}</a></td>
                                <td>${data.updatedDate}</td>
                                <td class="text-center">
                                    <form method="post" style="margin-bottom: 0px" action="RemoveTest" id="inactiveTest">
                                        <input name="testId" type="hidden" value="${data.testId}"/>
                                        <input name="testName" type="hidden" value="${data.testName}"/>
                                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                                        <a href="#" onclick="AddTest('Update Test', ${data.testId}, '${data.testName}', this)" data-toggle="tooltip" data-placement="right" title="Edit ${data.testName} Test" style=" text-decoration: none;">
                                            <span class="glyphicon glyphicon-pencil cursorOnHover"></span>
                                        </a>
                                        &nbsp;&nbsp;|&nbsp;&nbsp;
                                        <a href="#" onclick="removeTest(${data.testId}, '${data.testName}', this)" data-toggle="tooltip" data-placement="right" title="Remove ${data.testName} Test" style=" text-decoration: none;">
                                            <span class="glyphicon glyphicon-trash cursorOnHover"></span>
                                        </a>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div id="UpdateTest" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST" id="updateTestForm">
                        <input type="hidden" id="testId" name="testId"/>
                        <input type="hidden" id="role" name="role" value="tseAdmin"/>
                        <input type="hidden" id="oldTestName" name="oldTestName"/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}"/>
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-Test" class="modal-title"></h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="floating-label-form-group floating-label-form-group-with-value col-xs-offset-3 col-xs-6 removedTest">
                                <label>Test Name <span class="text-danger">*</span></label>
                                <select id="testNameModal" name="testNameModal" style="width: 100%; box-shadow: none" class="form-control">
                                    
                                </select>
                            </div>
                            <div class="hideActive">
                                <div class="row vertical-center-row hideActive">
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                        <label>Test Name <span class="text-danger">*</span><strong>(Max:40)</strong></label>
                                        <input type="text" id="testName" name="testName" class="form-control upperCase" placeholder="Enter Test Name" maxlength="40" style="width: 100%"/>
                                    </div>
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Test Unit <span class="text-danger">*</span><strong>(Max:40)</strong></label>
                                        <input type="text" id="testUnit" name="testUnit" class="form-control upperCase" placeholder="Enter Unit" style="width: 100%" maxlength="40"/>
                                    </div>
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Method Name <span class="text-danger">*</span><strong>(Max:40)</strong></label>
                                        <input type="text" id="methodName" name="methodName" class="form-control upperCase" placeholder="Enter Method Name" style="width: 100%" maxlength="40"/>
                                    </div>
                                </div>
                                <div class="row vertical-center-row">
                                    <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Required Qty of Sample <span class="text-danger">*</span><strong>(Max:5)</strong></label>
                                        <input type="text" id="sampleQty" name="sampleQty" class="form-control" placeholder="Enter Quantity of sample" title="Quantity of Sample Required for testing this Sample" style="width: 100%" onkeypress="javascript: return isNumber(event)" maxlength="5" value="0"/>
                                    </div>
                                    <div class="form-group col-xs-9 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Display Position <span class="text-danger">*</span><strong>(Max:4)</strong></label>
                                        <input type="number" min="0" id="dispSeq" name="dispSeq" class="form-control" placeholder="Enter position of sample" title="Position of Test among other test" style="width: 100%" onkeypress="javascript: return isNumber(this)" maxlength="4" value="0"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                            <button type="button" id="testSubmit" class="btn btn-default btn-success"></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
