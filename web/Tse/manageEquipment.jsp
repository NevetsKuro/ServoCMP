<br/><btn class ="btn btn-primary btn-group-sm" onclick="showUpdateEquipment('Add Equipment')">Add New Equipment</btn>
<form class="form-inline" method="GET" action="#" role="form" id="equipment">
    <div class="row">
        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
            <label>Select Customer <span class="text-danger">*</span></label>
            <select id="equipmentCustomer"  style="width: 100%; padding: 5px" class="form-control select2-select" onchange="getMake()" required="required">
                <option value="">Select Customer </option>
            </select>
        </div>
        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
            <label>Select Make <span class="text-danger">*</span></label>
                <select id="equipmentMake"  style="width: 100%; padding: 5px" class="form-control select2-select" onchange="ManageEquipments()" required="required">
                <option value="">Select Make </option>
            </select>
        </div>
        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
            <label>Select Equipment *</label>
            <select id="equipmentEquipment" style="width: 100%; padding: 5px;" class="form-control select2-select">
                <option value="">Select Equipment *</option>
            </select>
        </div>
    </div>
    <div class="centre-btn" style="margin-top: 50px">
        <input type="button" value="Get Details" class="btn btn-primary" onclick="UpdateEquipment('Update Equipment Details')"/>
        <input style="margin-left: 20px" type="button" value="Get Summary" class="btn btn-primary" onclick="getEquipmentSummary()"/>
    </div>
</form>
<div id="equipmentTableDiv" class="container-fluid">
    <table id="manageEquipmentTable" class="table table-bordered table-condensed table-responsive table-striped">
        <thead>
        <th>Customer</th>
        <th>Make</th>
        <th>Equipment</th>
        <th>Updated By</th>
        <th>Updated Date</th>
        </thead>
        <tbody></tbody>
    </table>
</div>
<div id="UpdateEquipment" class="modal fade" role="dialog" data-backdrop='static'>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form id="updateEquipmentForm" class="form-inline" role="form" action="#" method="POST">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-Equipment" class="modal-title"></h4>
                </div>
                <div class="modal-body container-fluid">
                    <div class="row">
                        <input type="hidden" id="EquipmentId" name="equipmentId"/>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Select Customer *</label>
                            <select id="equipmentCustomerModal" name="equipmentCustomer" class="form-control" onchange="getMake()" style="width: 100%">
                                <option value="">Select Customer</option>
                            </select>
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Select Make *</label>
                            <select id="equipmentMakeModal" name="equipmentMake" class="form-control" onchange="getEquipments(this.value)" style="width: 100%">
                                <option value="">Select Make </option>
                            </select>
                        </div>
                        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Equipment Name</label>
                            <input id="equipmentEquipmentModal" name="equipmentName" placeholder="Enter Equipment Name" class="form-control" type="text" style="width: 100%"/>
                        </div>
                    </div>
                    <br/>
                    <br/>
                </div>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" id="equipmentSubmit" onclick="submitEquipment(this)" class="btn btn-default btn-primary"></button>
                </div>
            </form>
        </div>
    </div>
</div>