<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Laboratory Equipments"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h3 class="centre-btn" style="text-decoration: underline">Laboratory Equipments for: ${labDetails.labName}</h3>
            <btn class ="btn btn-primary btn-group-sm" onclick="AddEquipments('Add Equipments')" data-toggle="tooltip" data-placement="bottom" title="Add Laboratory Equipments">Add Lab Equipment</btn>
            <btn style="float: right" class ="btn btn-primary btn-group-sm right" onclick="removeLabEquipments('Remove Equipments')" data-toggle="tooltip" data-placement="bottom" title="Inactive Equipments">Removed Equipments</btn>
            <br/>
            <br/>
            <table id="labEquipTable" class="table table-bordered tbCenter" style="border-bottom: 1px solid darkgray;">
                <thead>
                    <tr style="font-size:small" >
                        <th style="width: 10%">Equipment Name</th>
                        <th style="width: 10%">Make Name</th>
                        <th>Working</th>
                        <th style="width: 10%">Method Name</th>
                        <th>Remarks</th>
                        <th>Updated By</th>
                        <th style="width: 15%">Updated Date</th>
                        <th class="text-center">Update Equipment</th>
                    </tr>
                </thead>
                <tbody> 
                    <c:forEach items="${labEquip}" var="data">
                        <tr style="font-size:small" >
                            <td>${data.labEquipName}</td>
                            <td>${data.makeName}</td>
                            <td class="NevsCenter">
                                <c:if test="${data.operationalStatus eq 'YES'}">
                                    <div><span class="hide">1</span><span class="dot-green"></span></div>
                                </c:if>
                                <c:if test="${data.operationalStatus eq 'NO'}">
                                    <div><span class="hide">2</span><span class="dot-red"></span></div>
                                </c:if>
                            
                            </td>
                            <td>${data.methodName}</td>
                            <td>${data.remarks}</td>
                            <td><a href="#" onclick="openHelloIOCian(${data.updatedBy})">${data.updatedBy}</a></td>
                            <td>${data.updatedDatetime}</td>
                            <td class="text-center">
                                <form method="post" style="margin-bottom: 0px" action="RemoveLabEquipment" id="inactiveLabEquipment">
                                    <input name="labEquipId" type="hidden" value="${data.labEquipId}"/>
                                    <input name="labEquipName" type="hidden" value="${data.labEquipName}"/>
                                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                                    <a href="#" onclick="AddEquipments('Update LabEquipment', '${data.labEquipId}', '${data.labEquipName}','${data.operationalStatus}', this)" data-toggle="tooltip" data-placement="left" title="Edit ${data.labEquipName} Equipment" style=" text-decoration: none;">
                                        <span class="glyphicon glyphicon-pencil"></span>
                                    </a>
                                    |
                                    <a href="#" onclick="removeLabEquipments(${data.labEquipId}, '${data.labEquipName}','${data.operationalStatus}', this)" data-toggle="tooltip" data-placement="left" title="Remove ${data.labEquipName} Equipment" style=" text-decoration: none;">
                                        <span class="glyphicon glyphicon-trash"></span>
                                    </a>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div id="UpdatelabEquipment" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST" id="updateLabEquipForm">
                        <input type="hidden" id="labLocCode" name="labLocCode" value="${labDetails.labCode}"/>
                        <input type="hidden" id="labEquipId" name="labEquipId" value=""/>
                        <input type="hidden" id="oldEquipName" name="oldEquipName"/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-LabEquip" class="modal-title"></h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="floating-label-form-group floating-label-form-group-with-value col-xs-offset-3 col-xs-6 removedEquipment">
                                <label>Lab Equipments Name <span class="text-danger">*</span></label>
                                <select id="labEquipNameModal" name="labEquipNameModal" style="width: 100%; box-shadow: none" class="form-control">
                                    <option value="">Select Lab Equipment</option>
                                    
                                </select>
                            </div>
                            <div class="hideActive">
                                <div class="row vertical-center-row hideActive">
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                        <label>Equipment Name <span class="text-danger">*</span></label>
                                        <input type="text" id="labEquipName" name="labEquipName" class="form-control" placeholder="Enter Equipment Name" style="width: 100%" maxlength="45"/>
                                    </div>
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Make Name <span class="text-danger">*</span></label>
                                        <input type="text" id="labMakeName" name="labMakeName" class="form-control" placeholder="Enter Make Name" style="width: 100%" maxlength="45"/>
                                    </div>
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Method Name <span class="text-danger">*</span></label>
                                        <input type="text" id="labMethodName" name="labMethodName" class="form-control" placeholder="Enter Method Name" style="width: 100%" maxlength="45"/>
                                    </div>
                                </div>
                                <div class="row vertical-center-row">
                                    <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Select Operational Status <span class="text-danger">*</span></label>
                                        <select id="operationalStatus" name="operationalStatus" class="form-control" style="width: 100%">
                                            <option selected value="YES">YES</option>
                                            <option value="NO">NO</option>
                                        </select>
                                    </div>
                                    <div class="form-group col-xs-9 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Remarks </label>
                                        <input id="labEquipRemarks" class="form-control" placeholder="Enter Remarks " style="width: 100%;height: 28px;" name="remarks" maxlength="245"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                            <button type="button" id="labEquipSubmit" onclick="submitLabEquipment(this)" class="btn btn-default btn-success"></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>