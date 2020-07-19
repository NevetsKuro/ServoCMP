<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Lab Access"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">Manage Lab Access</h4>
            <div class="row">
                <div class="form-group col-xs-offset-4 col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="border-left: none;">
                    <label>Select Lab Employee Code <span class="text-danger">*</span></label>
                    <select id="empCode" name="empCode" style="width: 100%" onchange="window.location.href = '${pageContext.request.contextPath}/LabAdmin/LabAccess?empCode=' + this.value+'&csrftoken=${sessionScope.csrfToken}'">
                        <option value=""></option>
                        <c:forEach items="${mUser}" var="data">
                            <option value="${data.empCode}" ${data.empCode == param.empCode ? 'selected':''}> ${data.empCode} (${data.empName})</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <br/>
            <div class="container-fluid" style="width: 80%; margin: 0 auto">
                <btn class ="btn btn-primary btn-group-sm" onclick="AddLab('Add Lab')" data-toggle="tooltip" data-placement="bottom" title="Add Lab to Master Records">Add Lab</btn>
                <br/><br/>
                <table id="labAccess" class="table-bordered table-condensed table table-hover table-responsive table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Lab Code</th>
                            <th>Lab Name</th>
                            <th style="width: 12px">Edit</th>
                            <th>Lab Address</th>
                            <th style="width: 60px">Lab Emp Code</th>
                            <th>Lab Emp Name</th>
                            <th>Updated By</th>
                            <th>Updated Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${mLab}" var="data">
                            <tr>
                                <td></td>
                                <td>${data.labCode}</td>
                                <td class="NevsLeft">${data.labName}</td>
                                <td>
                                    <a href="#" onclick="AddLab('Update Lab', '${data.labCode}', '${data.labName}', this, ${data.mstUser.empCode})" data-toggle="tooltip" data-placement="bottom" title="Edit ${data.labName} Lab" style=" text-decoration: none;">
                                        <span class="glyphicon glyphicon-pencil"></span>
                                    </a>
                                </td>
                                <td class="NevsLeft">
                                    <span>${data.labAdd1}</span>
                                    <span>${data.labAdd2}</span>
                                    <span>${data.labAdd3}</span>
                                </td>
                                <td><a href="#" onclick="openHelloIOCian(${data.mstUser.empCode})">${data.mstUser.empCode}</a></td>
                                <td class="NevsLeft">${data.mstUser.empName}</td>
                                <td><a href="#" onclick="openHelloIOCian(${data.mstUser.updatedBy})">${data.mstUser.updatedBy}</a></td>
                                <td>${data.mstUser.updatedDate}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="centre-btn">
                    <button class="btn btn-primary" onclick="changeLab();" title="Click to change Tse of Selected Customer">Change Lab Allocation</button>
                    <button id="labSelect" class="btn btn-primary" onclick="selectLab(this);" value="1" title="Click to Select all Visible Tse" style="margin-left: 20px">&nbsp;Select All&nbsp;</button>
                </div>
            </div>
            <div id="UpdateLab" class="modal fade" role="dialog" data-backdrop='static'>
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form role="form" class="form-inline" action="#" method="POST" id="updateTseForm">
                            <input type="hidden" name="labNames" id="labNames"/>
                            <input type="hidden" name="labCodes" id="labCodes"/>
                            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 id="modal-title-Lab" class="modal-title"></h4>
                            </div>
                            <div class="modal-body container-fluid">
                                <div class="row vertical-center-row right">
                                    <div class="form-group col-xs-6" style="border-left: none">
                                        Change Lab person for Below Lab(s): 
                                        <ol id="selectedLab"></ol>
                                    </div>
                                    <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Employee Code <span class="text-danger">*</span></label>
                                        <select id="newEmpCode" name="newEmpCode" style="width: 100%; box-shadow: none" class="form-control">
                                            <c:forEach items="${mUser}" var="data">
                                                <option value="${data.empCode}" ${data.empCode == param.empCode ? 'selected':''}> ${data.empCode} (${data.empName})</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <span style="float: left" class="text-info">IndianOil.in</span>
                                <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                                <button type="submit" id="LabSubmit" class="btn btn-default btn-success">Change Lab</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div id="UpdateLabDetails" class="modal fade" role="dialog" data-backdrop='static'>
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <form role="form" class="form-inline" action="#" method="POST" id="updateLabForm">
                            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 id="modal-title-LabDetails" class="modal-title"></h4>
                            </div>
                            <div class="modal-body container-fluid">
                                <div class="row vertical-center-row right">
                                    <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Lab Location Code <span class="text-danger">*</span></label>
                                        <input data-field="Lab Location Code" type="text" name="labLocCode" id="labLocCode" class="form-control isNumber" placeholder="Enter Location Code of Lab" style="width: 100%" maxlength="10" required/>
                                    </div>
                                    <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Lab Name <span class="text-danger">*</span></label>
                                        <input type="text" name="labName" id="labName" class="form-control upperCase" placeholder="Enter Name of Lab" style="width: 100%" maxlength="50" required/>
                                    </div>
                                </div>
                                <div class="row vertical-center-row right">
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Lab Address <span class="text-danger">*</span></label>
                                        <textarea name="labAdd" id="labAdd" class="form-control" placeholder="Enter Address of Lab" style="width: 100%" maxlength="45"></textarea>
                                    </div>
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Lab Address 2 <span class="text-danger">*</span></label>
                                        <textarea name="labAdd2" id="labAdd2" class="form-control" placeholder="Enter Address 2 of Lab" style="width: 100%" maxlength="45"></textarea>
                                    </div>
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Lab Address 3 <span class="text-danger">*</span></label>
                                        <textarea name="labAdd3" id="labAdd3" class="form-control" placeholder="Enter Address 3 of Lab" style="width: 100%" maxlength="45"></textarea>
                                    </div>
                                </div>
                                <div class="row vertical-center-row">
                                    <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value" style="padding: 3px;">
                                        <label>Lab Type <span class="text-danger">*</span></label>
                                        <select id="labType" name="labType" style="width: 100%; box-shadow: none;" class="form-control selectCustom" required>
                                            <option value="CSL">CSL Lab</option>
                                            <option value="RND">RND Lab</option>
                                        </select>
                                    </div>
                                    <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Employee Code <span class="text-danger">*</span></label>
                                        <select id="labEmpCode" name="labEmpCode" style="width: 100%; box-shadow: none" class="form-control">
                                            <c:forEach items="${mUser}" var="data">
                                                <option value="${data.empCode}" ${data.empCode == param.empCode ? 'selected':''}> ${data.empCode} (${data.empName})</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-xs-1">
                                        <a href="${pageContext.request.contextPath}/LabAdmin/AllActiveUser?csrftoken=${sessionScope.csrfToken}"><div style="padding-top:25px; font-size:21px; "><i class="glyphicon glyphicon-question-sign" title="Click here to add non-existing Employees"></i></div></a>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <span style="float: left" class="text-info">IndianOil.in</span>
                                <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                                <button type="submit" id="LabDetailsSubmit" class="btn btn-default btn-success">Change Lab</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>