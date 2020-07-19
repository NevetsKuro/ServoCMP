<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Industry"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">INDUSTRY MASTERS</h4>
            <div style="width: 70%; margin-left: auto; margin-right: auto">
                <btn class ="btn btn-primary btn-group-sm" onclick="AddIndustry('Add Industry')" data-toggle="tooltip" data-placement="bottom" title="Add Customer to Master Records">Add Industry</btn>
                <btn style="float: right" class ="btn btn-primary btn-group-sm right" onclick="removeIndustry('Remove Industry')" data-toggle="tooltip" data-placement="bottom" title="Inactive Industries">Removed Industries</btn>
                <br/><br/>
                <table id="IndustryTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <th>Industry Name</th>
                            <th style="width: 10%">Updated By</th>
                            <th style="width: 20%">Updated Date</th>
                            <th style="width: 10%">Update Industry</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${Industries}" var="industry">
                            <tr>
                                <td class="NevsLeft">${industry.indName}</td>
                                <td><a href="#" onclick="openHelloIOCian(${industry.updatedBy})">${industry.updatedBy}</a></td>
                                <td>${industry.updatedDate}</td>
                                <td class="text-center">
                                    <form method="post" style="margin-bottom: 0px" action="RemoveIndustry" id="inactiveIndustry">
                                        <input name="indId" type="hidden" value="${industry.indId}"/>
                                        <input name="indName" type="hidden" value="${industry.indName}"/>
                                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                                        <a href="#" onclick="AddIndustry('Update Industry', ${industry.indId}, '${industry.indName}', this)" data-toggle="tooltip" data-placement="right" title="Edit ${industry.indName} Industry" style=" text-decoration: none;">
                                            <span class="glyphicon glyphicon-pencil"></span>
                                        </a>
                                        |
                                        <a href="#" onclick="removeIndustry(${industry.indId}, '${industry.indName}', this)" data-toggle="tooltip" data-placement="right" title="Remove ${industry.indName} Industry" style=" text-decoration: none;">
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
        <div id="UpdateIndustry" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST" id="updateIndustryForm">
                        <input type="hidden" id="indId" name="indId"/>
                        <input type="hidden" id="oldIndName" name="oldIndName"/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-Industry" class="modal-title"></h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="row vertical-center-row">
                                <div class="form-group col-xs-offset-3 col-xs-6 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <label>Industry Name <span class="text-danger">*</span></label>
                                    <select id="industryNameModal" name="industryId" style="width: 100%; box-shadow: none" class="form-control upperCase">
                                        <option value="">Select Industry <span class="text-danger">*</span></option>
                                        <c:forEach items="${Industries}" var="industry">
                                            <option  value="${industry.indId}">${industry.indName}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="hideActive">
                                        <select multiple="multiple" id="industryNameInput" name="industryNameInput" style="width: 100%; box-shadow: none" class="form-control upperCase">
                                            <option value="">Select..</option>
                                            <c:forEach items="${Industries}" var="industry">
                                                <option  value="${industry.indName}">${industry.indName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="hideIndustryEdit">
                                        <input type="text" id="industryEdit" name="industryNameEdit" class="form-control" placeholder="Enter Industry Name" maxlength="40" style="width: 100%"/>
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
                            <button type="button" id="industrySubmit" onclick="submitIndustry(this)" class="btn btn-default btn-success"></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
