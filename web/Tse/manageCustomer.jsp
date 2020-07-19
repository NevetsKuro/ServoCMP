<br/><btn class ="btn btn-primary btn-group-sm" onclick="showUpdateCustomer('Add Customer')">Add New Customer</btn>
<form class="form-inline" method="GET" action="#" role="form" id="Customer">
    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
    <div class="row">
        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
            <label>Industry</label>
            <select id="custIndustry" style="width: 100%; box-shadow: none" class="form-control" onchange="getCustomer()">
                <option value="">Select Industry</option>
                <c:forEach items="${Industries}" var="industry">
                    <option  value="${industry.indId}">${industry.indName}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
            <label>Customer Name <span class="text-danger">*</span></label>
            <select id="custCustomer" style="width: 100%; box-shadow: none" class="form-control">
            </select>
        </div>
    </div>
    <div class="centre-btn" style="margin-top: 50px">
        <input type="button" value="Get Summary" class="btn btn-primary" onclick="getAllCustomer()"/>
    </div>
</form>
<div id="UpdateCust" class="modal fade" role="dialog" data-backdrop='static'>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form role="form" class="form-inline" action="#" method="POST" id="updateCustForm">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-cust" class="modal-title"></h4>
                </div>
                <div class="modal-body container-fluid">
                    <div class="row vertical-center-row">
                        <input id="custId" name="custName" type="hidden"/>
                        <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Industry Name <span class="text-danger">*</span></label>
                            <select id="custIndustryModal" name="custIndustry" style="width: 100%; box-shadow: none" class="form-control">
                                <option value="">Select Industry</option>
                                <c:forEach items="${Industries}" var="industry">
                                    <option  value="${industry.indId}">${industry.indName}</option>
                                </c:forEach>
                            </select>
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Customer Name</label>
                            <input name="custCustomer" id="custCustomerModal" placeholder="Enter Customer Name"  style="width: 100%" class="form-control" type="text">
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>New TSE Emp Code</label>
                            <input  value="${TseCustMapDetail.mstUsr.empCode}" name="custEmpCode" id="custEmpCodeModal" placeholder="Enter New TSE Employee Code"   style="width: 100%" class="form-control" type="text" onchange="loadEmployeeDetailFromCEM(this.value, 1)" onkeypress="javascript:return isNumber(event)">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>TSE Emp Code</label>
                            <input value="${TseCustMapDetail.mstUsr.empCode}" name="custEmpCodeLoaded" id="custEmpCodeLoadedModal" placeholder="Enter New TSE Employee Code"  style="width: 100%" readonly="readonly" class="form-control" type="text">
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>TSE Emp Name</label>
                            <input value="${TseCustMapDetail.mstUsr.empName}" name="custEmpName" id="custEmpNameModal" placeholder="TSE Emp Name" style="width: 100%" readonly="readonly" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>TSE Emp Email</label>
                            <input value="${TseCustMapDetail.mstUsr.empName}" name="custEmpEmail" id="custEmpEmailModal" placeholder="TSE Emp Email" style="width: 100%" readonly="readonly" class="form-control" type="email">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>Ctrl Emp Code</label>
                            <input name="custCtrlEmpCode" id="custCtrlEmpCodeModal" placeholder="Ctrl Emp Code"  style="width: 100%"  class="form-control" type="text" onchange="loadEmployeeDetailFromCEM(this.value, 2)" onkeypress="javascript:return isNumber(event)">
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Ctrl Emp Name</label>
                            <input value="${TseCustMapDetail.mstUsr.ctrlEmpName}" name="custCtrlEmpName" id="custCtrlEmpNameModal" placeholder="Ctrl Emp Name"  style="width: 100%"  readonly="readonly" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>Ctrl Emp Email</label>
                            <input value="${TseCustMapDetail.mstUsr.ctrlEmpEmail}" name="custCtrlEmpEmail" id="custCtrlEmpEmailModal" placeholder="Ctrl Emp Email"  style="width: 100%" readonly="readonly" class="form-control" type="text">
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" id="custSubmit" onclick="submitCust(this)" class="btn btn-default btn-primary"></button>
                </div>
            </form>
        </div>
    </div>
</div>
<br/>
<br/>
<div id="custTableDiv" class="container-fluid">
    <table id="manageCustTable" class="table table-bordered table-condensed table-responsive table-striped" style="font-size: 15px">
        <thead style="font-size: 13px">
        <th>Customer</th>
        <th style="width: 7%;">TSE Emp Code</th>
        <th>TSE Emp Name</th>
        <th>TSE Emp Email</th>
        <th style="width: 7%;">Ctrl Emp Code</th>
        <th>CTRL Emp Name</th>
        <th>CTRL Emp Email</th>
        <th>Updated By</th>
        <th style="width: 15%">Updated Date</th>
        </thead>
        <tbody></tbody>
    </table>
</div>