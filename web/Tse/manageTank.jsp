<br/><btn class ="btn btn-primary btn-group-sm" onclick="showUpdateTank('Add Tank')">Add New Tank</btn>
<form class="form-inline" method="GET" action="#" role="form" id="Tank">
    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
    <div class="row">
        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
            <label style="margin-bottom: 6px">Select Customer <span class="text-danger">*</span></label>
            <select id="tankCustomer" style="width: 100%; padding: 5px" class="form-control select2-select" onchange="getEquipments(this.value)" required="required">
                <option value="">Select Customer *</option>
            </select>
        </div>
        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
            <label style="margin-bottom: 6px">Select Equipment <span class="text-danger">*</span></label>
            <select id="tankEquipment" style="width: 100%; padding: 5px" class="form-control select2-select" onchange="getTank(this.value)" required="required">
                <option value="">Select Equipment *</option>
            </select>
        </div>
        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
            <label style="margin-bottom: 6px">Select Tank No <span class="text-danger">*</span></label>
            <select id="tankTankNo" name="tankNo" style="width: 100%; padding: 5px" class="form-control select2-select" onchange="" required="required">
                <option value="">Select Tank No *</option>
            </select>
        </div>
    </div>
    <div class="centre-btn" style="margin-top: 50px">
        <input type="button" value="Get Details" class="btn btn-primary" onclick="UpdateTank('Update Tank Details')"/>
        <input style="margin-left: 20px" type="button" value="Get Summary" class="btn btn-primary" onclick="getTankSummary()"/>
    </div>
</form>
<div id="tankTableDiv" class="container-fluid">
    <table id="manageTankTable" class="table table-bordered table-condensed table-responsive table-striped">
        <thead>
        <th>Customer</th>
        <th>Equipment</th>
        <th>Tank No (Description)</th>
        <th>Updated By</th>
        <th>Updated Date</th>
        </thead>
        <tbody></tbody>
    </table>
</div>

<div id="UpdateTank" class="modal fade" role="dialog" data-backdrop='static'>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form id="updateTankForm" class="form-inline" role="form" action="#" method="POST">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-tank" class="modal-title"></h4>
                </div>
                <div class="modal-body container-fluid">
                    <div class="row">
                        <input name="tankId" type="hidden" id="tankId"/>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Select Customer <span class="text-danger">*</span></label>
                            <select id="tankCustomerModal" class="form-control" onchange="getEquipments(this.value)" style="width: 100%">
                                <option value="">Select Customer *</option>
                            </select>
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Equipment Name</label>
                            <select id="tankEquipmentModal" name="tankEquipment" class="form-control" onchange="getTank(this.value)" style="width: 100%">
                                <option value="">Select Equipment *</option>
                            </select>
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Tank no</label>
                            <input id="tankTankNoModal" name="tankNoModal" placeholder="Tank no" class="form-control" type="text" onkeypress="javascript:return isNumber(event)">
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-12 floating-label-form-group floating-label-form-group-with-value">
                            <label>Tank Description</label>
                            <input name="tankDesc" id="tankTankDescModal" placeholder="Tank Description" style="width: 100%" class="form-control" type="text">
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                    </div>
                    <br/>
                    <br/>
                </div>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" id="tankSubmit" onclick="submitTank(this)" class="btn btn-default btn-primary"></button>
                </div>
            </form>
        </div>
    </div>
</div>