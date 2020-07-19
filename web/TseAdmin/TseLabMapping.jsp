<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Tse Access"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">Manage Tse Access</h4>
            <div class="row">
                <div class="col-xs-offset-6 col-xs-6">
                    <div class="col-md-4">
                        <label class="col-md-12">
                            <button type="button" class="button button-primary button-info button-small button-border-thick" onclick="changeTseLab();" title="Click to assign Tse to a Lab">Assign Tse To Lab</button>
                        </label>
                    </div>
                    <div class="col-md-4">
                        <label class="">
                            <button type="button" class="button button-primary button-info button-small button-border-thick access_empCode"> <i class="glyphicon glyphicon-refresh"></i> Refresh Tse-Lab List</button>
                        </label>
                    </div>
                </div>
                <!--<div class="col-xs-2">
                        <label>
                            <div id="viewLabAccess" class="access_view"></div>
                        </label>
                    </div>-->
            </div>
            <br/>
            <div class="container-fluid" style="width: 80%; margin: 0 auto">
                <table id="tseAccess" class="table-bordered table-condensed table table-hover table-responsive table-striped">
                    <thead>
                        <tr>
                            <th>Tse Employee Assigned</th>
                            <th>CSL Lab Code</th>
                            <th>CSL Lab Name</th>
                            <th>Remove</th>
                        </tr>
                    </thead>
                    <tbody id="modal_accessView">

                    </tbody>
                </table>
            </div>

            <div id="AssignTseLab" class="modal fade" role="dialog" data-backdrop='static'>
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form role="form" class="form-inline" action="#" method="POST" id="assignTseForm">
                            <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 id="modal-title-Customer" class="modal-title">Assign TSE to Lab:</h4>
                            </div>
                            <div class="modal-body container-fluid">
                                <div class="row vertical-center-row right">
                                    <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Employee Code <span class="text-danger">*</span></label>
                                        <select id="lab_newEmpCode" name="lab_newEmpCode" style="width: 100%; box-shadow: none" class="form-control selectCustom">
                                            <c:forEach items="${mUser}" var="data">
                                                <c:if test="${data.mstRole.roleId=='1'}">
                                                    <option value="${data.empCode}">${data.empName}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Labs <span class="text-danger">*</span></label>
                                        <select id="lab_labCode" name="lab_labCode" style="width: 100%; box-shadow: none" class="form-control selectCustom">
                                            <c:forEach items="${mLab}" var="data">
                                                <option value="${data.labCode}"> ${data.labName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <span style="float: left" class="text-info">IndianOil.in</span>
                                <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                                <button type="submit" id="TseLabSubmit" class="btn btn-default btn-success">Assign Tse</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
</body>
<jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>