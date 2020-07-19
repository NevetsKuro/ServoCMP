<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage User"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">USER MASTERS</h4>
            <div>
                <button class ="btn btn-primary btn-group-sm" onclick="AddUser('Add User')" data-toggle="tooltip" data-placement="bottom" title="Add User and Assign Roles">Add User</button>
                <button class ="btn btn-primary btn-group-sm" onclick="changeRole('Change Role')" data-toggle="tooltip" data-placement="bottom" title="Change Existing User Roles">Change User Roles</button>
                <button style="float: right" class ="btn btn-primary btn-group-sm right" onclick="removeUser('Remove User')" data-toggle="tooltip" data-placement="bottom" title="Inactive Users">Inactive Users</button>
                <button id="sync_empCode" class="btn btn-primary btn-group-sm right sync_empCode" data-toggle="tooltip" data-placement="bottom"  title="For updating/syncing/ of employee's controlling officer"> Sync List</button>
                <br/><br/>
                <table id="UserTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <th>Emp Code</th>
                            <th>Emp Name</th>
                            <th>Emp Email Id</th>
<!--                            <th style="width: 10%">Updated By</th>-->
                            <th style="width: 10%">Controlling Officer</th>
                            <th style="width: 20%">Updated Date</th>
                            <th>Access</th>
                            <th style="width: 10%">Remove User</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${User}" var="usr">
                            <tr>
                                <td>
                                    <a href="#" onclick="openHelloIOCian('${usr.empCode}')">${usr.empCode}</a>
                                </td>
                                <td class="NevsLeft">${usr.empName}</td>
                                <td class="NevsLeft">${usr.empEmail}</td>
                                <td><a href="#" onclick="openHelloIOCian(${usr.ctrlEmpCode})">${usr.ctrlEmpCode}</a></td>
                                <td>${usr.updatedDate}</td>
                                <td>${usr.mstRole.roleName}</td>
                                <td class="text-center">
                                    <c:if test="${usr.mstRole.roleId eq 1 || usr.mstRole.roleId eq 3}">
                                        <form method="post" style="margin-bottom: 0px" action="${pageContext.request.contextPath}/TseAdmin/RemoveUser" id="inactiveUser">
                                            <input name="empCode" type="hidden" value="${usr.empCode}"/>
                                            <input name="empName" type="hidden" value="${usr.empName}"/>
                                            <input name="oldEmpRole" id="oldEmpRole" type="hidden"/>
                                            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                                            <a href="#" onclick="removeUser(${usr.empCode}, '${usr.empName}', this, ${usr.mstRole.roleId})" data-toggle="tooltip" data-placement="left" title="Remove ${usr.empName} as ServoCMP User" style=" text-decoration: none;">
                                                <span class="glyphicon glyphicon-trash"></span>
                                            </a>
                                        </form>
                                    </c:if>
                                    <c:if test="${usr.mstRole.roleId eq 2 || usr.mstRole.roleId eq 4}">
                                        <a href="#" data-toggle="tooltip" data-placement="left" title="Contact Lab Admin to remove ${usr.empName}" style=" text-decoration: none; cursor: not-allowed" disabled>
                                            <span class="glyphicon glyphicon-trash"></span>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div id="changeRoles" class="modal fade" role="dialog" data-backdrop="static">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="${pageContext.request.contextPath}/TseAdmin/ChangeRole" method="POST" id="changeRolesForm">
                        <input type="hidden" id="changeRoleName" name="changeRoleName"/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-changeRole" class="modal-title"></h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="row vertical-center-row">
                                <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value" style="padding-bottom: 6px">
                                    <label>Select Employee <span class="text-danger">*</span></label>
                                    <select id="changeEmpCode" name="changeEmpCode" style="width: 100%; box-shadow: none" class="form-control" onchange="getRole(this);" required>
                                        <option value="">Select Employee *</option>
                                        <c:forEach items="${User}" var="usr">
                                            <option data-roleName="${usr.mstRole.roleName}" value="${usr.empCode}">(${usr.empCode}) ${usr.empName}</option>
                                            
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Assigned Role <span class="text-danger">*</span></label>
                                    <select id="changeRole" name="changeRole" style="width: 100%; box-shadow: none" class="form-control" onchange="EDButton(this)" required>
                                        <option value="">Select Access Type</option>
                                        <c:forEach items="${Role}" var="role">
                                            <c:if test="${role.roleId != 5}">
                                                <option value="${role.roleId}">${role.roleName}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-default btn-primary" data-dismiss="modal">Cancel</button>
                            <input id="changeRoleSubmit" type="submit" value="Change Role" class="btn btn-default btn-success"/>
                            <!--<button type="button" id="changeRoleSubmit" onclick="submitChangeRole(this)" class="btn btn-default btn-primary"></button>-->
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div id="inactiveEmployee" class="modal fade" role="dialog" data-backdrop="static">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form class="form-inline" action="${pageContext.request.contextPath}/TseAdmin/ActivateUser" method="POST" id="inactiveEmployee">
                        <input type="hidden" id="inActiveRoleId" name="inActiveRoleId"/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-inactiveEmployee" class="modal-title"></h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="row vertical-center-row">
                                <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value col-xs-offset-3">
                                    <label>Select Employee <span class="text-danger">*</span></label>
                                    <select id="inactiveEmpCode" name="inactiveEmpCode" style="width: 100%; box-shadow: none" class="form-control" onchange="">
                                        <option value="">Select Employee </option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-default btn-primary" data-dismiss="modal">Cancel</button>
                            <input id="inactiveEmployeeSubmit" type="submit" value="Activate" class="btn btn-default btn-success"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div id="UpdateUser" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST" id="updateUserForm">
                        <input type="hidden" id="roleId" name="roleId"/>
                        <input type="hidden" id="roleName" name="roleName"/>
                        <input type="hidden" id="roleSO" name="roleSO"/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close closeIt" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-User" class="modal-title"></h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="row vertical-center-row">
                                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Access Type <span class="text-danger">*</span></label>
                                    <select id="fAreaCode" name="fAreaCode" style="width: 100%; box-shadow: none" class="form-control" onchange="getEmployee(this)">
                                        <option value="">Select Access Type</option>
                                        <c:forEach items="${Role}" var="role">
                                            <c:if test="${role.roleId != 5}">
                                                <option data-roleId="${role.roleId}" data-locCode="${role.locCode}" value="${role.funcAreaCode}">${role.roleName}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Employee Code <span class="text-danger">*</span></label>
                                    <select id="empCode" name="empCode" style="width:100%" class="form-control" onchange="popEmployeeData(this);">
                                        <option>Select Access Type </option>
                                    </select>
                                </div>
                                <div class="form-group col-xs-1" style="margin-top: 18px">
                                    <button id="searchEmployee" type="button" class="btn btn-default btn-success"  title="For Searching Other TSE Officers"><i class="fa fa-search"></i></button>
                                </div>
                            </div>
                            <div class="row vertical-center-row">
                                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Employee Name <span class="text-danger">*</span></label>
                                    <input id="empName" name="empName" placeholder="Enter Employee Name" style="width: 100%" class="form-control" type="text" readonly/>
                                </div>
                                <div class="form-group col-xs-8 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Employee Email <span class="text-danger">*</span></label>
                                    <input id="empEmail" name="empEmail" placeholder="Enter Employee Email" style="width: 100%" class="form-control" type="text" readonly/>
                                </div>
                            </div>
                            <div class="row vertical-center-row">
                                <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <label>Ctrl Emp Code <span class="text-danger">*</span></label>
                                    <input id="ctrlEmpCode" name="ctrlEmpCode" placeholder="Employee Code" style="width: 100%" class="form-control" type="text" readonly/>
                                </div>
                                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Ctrl Employee Name <span class="text-danger">*</span></label>
                                    <input id="ctrlEmpName" name="ctrlEmpName" placeholder="Enter Controlling Employee Name" style="width: 100%" class="form-control" type="text" readonly/>
                                </div>
                                <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Ctrl Employee Email <span class="text-danger">*</span></label>
                                    <input id="ctrlEmpEmail" name="ctrlEmpEmail" placeholder="Enter Controlling Employee Email" style="width: 100%" class="form-control" type="text" readonly/>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-default btn-primary closeIt" data-dismiss="modal">Cancel</button>
                            <button type="button" id="userSubmit" onclick="submitUser(this)" class="btn btn-default btn-success">Submit</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>