<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Applications"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">APPLICATION MASTERS</h4>
            <div style="width: 70%; margin-left: auto; margin-right: auto">
                <btn class ="btn btn-primary btn-group-sm" onclick="AddApplication('Add Application')" data-toggle="tooltip" data-placement="bottom" title="Add Application to Master Records">Add Application</btn>
                <btn style="float: right" class ="btn btn-primary btn-group-sm right" onclick="removeApplication('Remove Application')" data-toggle="tooltip" data-placement="bottom" title="Inactive Application">Removed Applications</btn>
                <br/><br/>
                <table id="ApplicationTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <th>Application Name</th>
                            <th style="width: 10%">Updated By</th>
                            <th style="width: 20%">Updated Date</th>
                            <th style="width: 10%">Remove Application</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${Applications}" var="application">
                            <tr>
                                <td class="NevsLeft">${application.appName}</td>
                                <td><a href="#" onclick="openHelloIOCian(${application.updatedBy})">${application.updatedBy}</a></td>
                                <td>${application.updatedDate}</td>
                                <td class="text-center">
                                    <form method="post" style="margin-bottom: 0px" action="RemoveApplication" id="inactiveApplication">
                                        <input name="appId" type="hidden" value="${application.appId}"/>
                                        <input name="appName" type="hidden" value="${application.appName}"/>
                                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                                        <a href="#" onclick="AddApplication('Update Application', ${application.appId}, '${application.appName}', this)" data-toggle="tooltip" data-placement="right" title="Update ${application.appName} Application" style=" text-decoration: none;">
                                            <span class="glyphicon glyphicon-pencil"></span>
                                        </a>
                                        |
                                        <a href="#" onclick="removeApplication(${application.appId}, '${application.appName}', this)" data-toggle="tooltip" data-placement="right" title="Remove ${application.appName} Application" style=" text-decoration: none;">
                                            <span class="glyphicon glyphicon-trash"></span>
                                        </a>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div id="UpdateApplication" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST" id="updateApplicationForm">
                        <input type="hidden" id="appId" name="appId"/>
                        <input type="hidden" id="oldAppName" name="oldAppName"/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-Application" class="modal-title"></h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="row vertical-center-row">
                                <div class="form-group col-xs-offset-3 col-xs-6 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <label>Application Name <span class="text-danger">*</span></label>
                                    <select id="applicationNameModal" name="applicationId" style="width: 100%; box-shadow: none" class="form-control">
                                        <option value="">Select Industry</option>
                                        <c:forEach items="${Applications}" var="application">
                                            <option  value="${application.appId}">${application.appName}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="hideActive">
                                        <select multiple="multiple" id="applicationNameInput" name="applicationNameInput" style="width: 100%; box-shadow: none" class="form-control" placeholder="">
                                            <option></option>
                                            <c:forEach items="${Applications}" var="application">
                                                <option  value="${application.appName}">${application.appName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="hideApplicationEdit">
                                        <input type="text" id="applicationEdit" name="applicationNameEdit" class="form-control" placeholder="Enter Application Name" style="width: 100%"/>
                                    </div>
                                    <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                            <button type="button" id="applicationSubmit" onclick="submitApplication(this)" class="btn btn-default btn-success"></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
