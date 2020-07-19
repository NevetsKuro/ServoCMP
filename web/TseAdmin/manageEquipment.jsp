<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Equipment"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">EQUIPMENT MASTERS</h4>
            <div style="width: 70%; margin-left: auto; margin-right: auto">
<!--                <btn class ="btn btn-primary btn-group-sm" onclick="" data-toggle="tooltip" data-placement="bottom" title="Add Application to Master Records">Add Application</btn>-->
<!--                <btn style="float: right" class ="btn btn-primary btn-group-sm right" onclick="removeEquipment('Remove Equipment')" data-toggle="tooltip" data-placement="bottom" title="Inactive Application">Removed Equipment</btn>-->
                <br/><br/>
                <table id="ApplicationTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <th>Equipment Name</th>
                            <th>Make Name</th>
                            <th style="width: 10%">Updated By</th>
                            <th style="width: 20%">Updated Date</th>
                            <th style="width: 10%">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${Equipment}" var="equipment">
                            <tr>
                                <td class="NevsLeft">${equipment.equipmentName}</td>
                                <td class="NevsLeft">${equipment.mstmake.makeName}</td>
                                <td><a href="#" onclick="openHelloIOCian(${equipment.updatedBy})">${equipment.updatedBy}</a></td>
                                <td>${equipment.updatedDatetime}</td>
                                <td class="text-center">
                                    <!--<button id="activeEquip" class="activeEquip" class="btn btn-primary btn-group-sm" data-id="${equipment.equipmentId}">Active</button>-->
                                    <button id="updateEquip" class="updateEquip" class="btn btn-success btn-group-sm" data-id="${equipment.equipmentId}" data-makeId="${equipment.mstmake.makeId}">Update</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div id="UpdateEquipmentModal" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST" id="updateEquipmentForm">
                        <input type="hidden" id="modal_equipId" name="modal_equipId"/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-Application" class="modal-title">Update Equipment</h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="row vertical-center-row">
                                <div class="form-group col-xs-offset-3 col-xs-6 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <label>Equipment Name <span class="text-danger">*</span></label>
                                    <input type="text" id="modal_equipName" name="modal_equipName" class="form-control" placeholder="Enter Application Name" style="width: 100%"/>
                                </div>
                                <div class="form-group col-xs-offset-3 col-xs-6 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <label>Make Name <span class="text-danger">*</span></label>
                                    <select id="modal_makeName" name="modal_makeName" style="width: 100%; box-shadow: none" class="form-control select2">
                                        <option value="">Select Make</option>
                                        <c:forEach items="${Make}" var="make">
                                            <option value="${make.makeId}">${make.makeName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                            <button type="button" id="modal_EquipmentSubmit" class="btn btn-default btn-success">Update</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
