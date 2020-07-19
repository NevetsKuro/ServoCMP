<br/><btn class ="btn btn-primary btn-group-sm" onclick="showUpdateDepartment('Add Department')">Add New Department</btn>
<form class="form-inline" method="GET" action="#" role="form" id="Department">
    <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
    <div class="row">
        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
            <label>Customer Name <span class="text-danger">*</span></label>
            <select id="deptCustomer" name="custId" style="width: 100%; box-shadow: none" class="form-control" onchange="loadDepartment(this.value, 'MD')">
                <option value="">Select Customer</option>
                <c:forEach items="${Customers}" var="customer">
                    <option  value="${customer.customerId}">${customer.customerName}</option>
                </c:forEach>
            </select>
            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
        </div>
        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
            <label>Department Name</label>
            <select style="width: 100%" class="form-control" name="departmentName" id="departmentDepartment">
            </select>
            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
        </div>
    </div>
    <div class="centre-btn" style="margin-top: 50px">
        <input type="button" value="Get Details" class="btn btn-primary" onclick="UpdateDepartment('Update Department Details')"/>
        <input style="margin-left: 20px" type="button" value="Get Summary" class="btn btn-primary" onclick="getDepartmentSummary()"/>
    </div>
</form>
<div id="departmentTableDiv" class="container-fluid">
    <table id="manageDepartmentTable" class="table table-bordered table-condensed table-responsive table-striped">
        <thead>
        <th>Department</th>
        <th>HOD Name</th>
        <th>HOD Email</th>
        <th>HOD Contact</th>
        <th>Updated By</th>
        <th>Updated Date</th>
        </thead>
        <tbody></tbody>
    </table>
</div>
<div id="UpdateDept" class="modal fade" role="dialog" data-backdrop='static'>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form id="updateDeptForm" class="form-inline" role="form" action="#" method="POST">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-dept" class="modal-title"></h4>
                </div>
                <div class="modal-body container-fluid">
                    <div class="row">
                        <input name="deptId" type="hidden" id="deptId"/>
                        <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value" style="border-left: none; border-right: 1px solid darkgray">
                            <label>Select Customer <span class="text-danger">*</span></label>
                            <select id="deptCustomerModal" name="deptCustomer" class="form-control" onchange="" style="width: 100%">
                                <option value="">Select Customer</option>
                                <c:forEach items="${Customers}" var="customer">
                                    <option  value="${customer.customerId}">${customer.customerName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                            <label>Department Name</label>
                            <input name="deptName" id="deptNameModal" type="text" placeholder="Enter Department Name" class="form-control" style="width: 100%"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>HOD Name</label>
                            <input name="deptHOD" id="deptHODModal" type="text" placeholder="Enter HOD Name" class="form-control" style="width: 100%"/>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>HOD Email</label>
                            <input name="deptHODEmail" id="deptHODEmailModal" type="email" placeholder="Enter HOD E-mail ID" class="form-control" style="width: 100%"/>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>HOD Contact</label>
                            <input name="deptHODContact" id="deptHODContactModal" type="text" placeholder="Enter HOD Contact Number" class="form-control" style="width: 100%" onkeypress="javascript:return isNumber(event)"/>
                        </div>
                    </div>
                    <br/>
                    <br/>
                </div>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" id="deptSubmit" onclick="submitDept(this)" class="btn btn-default btn-primary"></button>
                </div>
            </form>
        </div>
    </div>
</div>