<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Make"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">MAKE MASTERS</h4>
            <div style="width: 70%; margin-left: auto; margin-right: auto">
                <btn class ="btn btn-primary btn-group-sm" onclick="AddMake('Add Make')" data-toggle="tooltip" data-placement="bottom" title="Add Make to Master Records">Add Make</btn>
                <btn style="float: right" class ="btn btn-primary btn-group-sm right" onclick="removeMake('Remove Make')" data-toggle="tooltip" data-placement="bottom" title="Inactive Make(s)">Removed Make(s)</btn>
                <br/><br/>
                <table id="MakeTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <th>Make Name</th>
                            <th style="width: 10%">Updated By</th>
                            <th style="width: 20%">Updated Date</th>
                            <th style="width: 10%">Update Make</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${Makes}" var="make">
                            <tr>
                                <td class="NevsLeft">${make.makeName}</td>
                                <td><a href="#" onclick="openHelloIOCian(${make.updatedBy})">${make.updatedBy}</a></td>
                                <td>${make.updateddateTime}</td>
                                <td class="text-center">
                                    <form method="post" style="margin-bottom: 0px" action="RemoveMake" id="inactiveMake">
                                        <input name="makeId" type="hidden" value="${make.makeId}"/>
                                        <input name="makeName" type="hidden" value="${make.makeName}"/>
                                        <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
                                        <a href="#" onclick="AddMake('Update Make', ${make.makeId}, '${make.makeName}', this)" data-toggle="tooltip" data-placement="right" title="Edit ${make.makeName} Make" style=" text-decoration: none;">
                                            <span class="glyphicon glyphicon-pencil"></span>
                                        </a>
                                        |
                                        <a href="#" onclick="removeMake(${make.makeId}, '${make.makeName}', this)" data-toggle="tooltip" data-placement="right" title="Remove ${make.makeName} Make" style=" text-decoration: none;">
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
        <div id="UpdateMake" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST" id="updateMakeForm">
                        <input type="hidden" id="makeId" name="makeId"/>
                        <input type="hidden" id="oldMakeName" name="oldMakeName"/>
                        <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-Make" class="modal-title"></h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="row vertical-center-row">
                                <div class="form-group col-xs-offset-3 col-xs-6 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <label>Make Name *</label>
                                    <select id="makeNameModal" name="ActMakeId" style="width: 100%; box-shadow: none" class="form-control upperCase">
                                        <option value="">Select Make *</option>
                                        <c:forEach items="${Makes}" var="make">
                                            <option  value="${make.makeId}">${make.makeName}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="hideActive">
                                        <select multiple="multiple" id="makeNameInput" name="makeNameInput" style="width: 100%; box-shadow: none" class="form-control" placeholder="">
                                            <option></option>
                                            <c:forEach items="${Makes}" var="make">
                                                <option  value="${make.makeName}">${make.makeName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="hideMakeEdit">
                                        <input type="text" id="makeEdit" name="makeNameEdit" class="form-control" placeholder="Enter Make Name" maxlength="50" style="width: 100%"/>
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
                            <button type="button" id="makeSubmit" onclick="submitMake(this)" class="btn btn-default btn-success"></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>