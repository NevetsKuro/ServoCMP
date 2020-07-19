<!-- Tank Creation Page  -->
<div class="container-fluid">
    <h4 class="centre-btn" style="text-decoration: underline">ADD SYSTEM DETAILS FOR CMP</h4>
    <form class="form-inline" method="post" id="addTank" action="${pageContext.request.contextPath}/Tse/AddNewTank">
        <div class="row">
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                <label>Industry</label>
                <select id="sampleIndustry" name="tankIndustry" style="width: 100%; box-shadow: none" class="form-control" onchange="getCustomer(this.value)" required>
                    <option value="">Select Industry</option>
                    <c:forEach items="${Industries}" var="industry">
                        <option  value="${industry.indId}">${industry.indName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-xs-2" style="margin-bottom: auto; margin-top: 25px;">
                <a href="#" onclick="populateSampleApplicationData('Add Industry');"><span class="glyphicon glyphicon-plus"></span></a>
            </div>
            <!--            <div class="form-check col-xs-offset-1 col-xs-4" style="height: 50x;border: 1px solid lightgrey;height: 50px;border-top: 0px;border-right: 0px;border-bottom: 0px; font-size: smaller">
                            <label>For Sampling Type:<i class="glyphicon glyphicon-question-sign text-primary"  data-toggle="tooltip"  data-placement="top" title="Choose between One Time Sampling (OTS) & Condition Monitoring Sampling (CMS)"></i> </label><br>
                            <label>
                                <input type="checkbox" id="toggle-two"/>
                            </label>
                        </div>-->
            <input class="hide" type="checkbox" id="oneTimeCheckbox" name="oneTimeCheckbox" value="1"> 
        </div>
        <div class="row">
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                <label>Customer Name <span class="text-danger">*</span></label>
                <select id="sampleCustomer" name="tankCustomer" style="width: 100%; box-shadow: none" class="form-control" onchange="getCustomerDept(this.value)" required>
                </select>
            </div>
            <div class="col-xs-4" style="margin-bottom: auto; margin-top: 25px;">
                <a href="#" onclick="populateSampleCustomerData(${sUser.sEMP_CODE});"><span class="glyphicon glyphicon-plus"></span></a>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                <label>Department Name <span class="text-danger">*</span></label>
                <select id="sampleDepartment" name="tankDepartment" style="width: 100%; box-shadow: none" class="form-control" onchange="getTank(); getCustomerEquipment();" required>
                </select>
            </div>
            <div class="col-xs-4" style="margin-bottom: auto; margin-top: 25px;">
                <a href="#" onclick="populateSampleDepartmentData()"><span class="glyphicon glyphicon-plus"></span></a>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                <label>Application Name <span class="text-danger">*</span></label>
                <select id="sampleApplication" name="tankApplication" style="width: 100%; box-shadow: none" class="form-control" onchange="getTank();" required>
                    <option value="">Select Application</option>
                    <c:forEach items="${Applications}" var="app">
                        <option  value="${app.appId}">${app.appName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-xs-1" style="margin-bottom: auto; margin-top: 25px;">
                <a href="#" onclick="populateSampleApplicationData('Add Application')"><span class="glyphicon glyphicon-plus"></span></a>
            </div>
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="padding-bottom: 2px">
                <label>Application Description <span class="text-danger">*</span></label>
                <textarea name="tankAppDesc" id="sampleAppDesc" style="width: 100%" placeholder="Description of Application" class="form-control" rows="1" maxlength="95" required>desc App</textarea>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                <label>Equipment Name <span class="text-danger">*</span></label>
                <select id="sampleEquipment" name="tankEquipment" style="width: 100%; box-shadow: none" class="form-control" onchange="getTank();" required>
                </select>
            </div>
            <div class="col-xs-4" style="margin-bottom: auto; margin-top: 25px;">
                <a href="#" onclick="populateSampleEquipmentData()"><span class="glyphicon glyphicon-plus"></span></a>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                <label>Product Name <span class="text-danger">*</span></label>
                <select id="sampleProduct" name="tankProduct" style="width: 100%; box-shadow: none" class="form-control" onchange="getTank();" required>
                    <option value="">Select Product</option>
                    <option value="OMC">OMC Product</option>
                    <!--
                    <c:forEach items="${Products}" var="pro">
                        <option  value="${pro.proId}">${pro.proName}</option>
                    </c:forEach>
                    -->
                </select>
            </div>
            <div class="col-xs-1" style="margin-bottom: auto; margin-top: 25px;">
                <a href="#" onclick="populateSampleApplicationData('Add Product')"><span class="glyphicon glyphicon-plus"></span></a>
            </div>
            <div  id="productNameField" class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value ">
                <label>Search Product</label>
                <input name="prodId" id="prodId" style="width: 100%"  placeholder="Enter product code" class="form-control" data-toggle="tooltip"  data-placement="left" title="Enter product code(at least 3 digit) to populate Product Name" onchange="getProduct();" maxlength="10"/>
            </div>
            <div id="productNameField1" class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value hide">
                <label>Grade <strong>(Max:40)</strong></label>
                <input name="prodGradeOMC" id="prodGradeOMC" class="omcProduct" style="width: 100%"  placeholder="Enter Grade" class="form-control" maxlength="40"/>
            </div>
            <div id="productNameField2" class="form-group col-xs-offset-1 col-xs-2 floating-label-form-group floating-label-form-group-with-value hide">
                <label>Similar product w.r.t Servo<span class="text-danger">*</span></label>
                <!--<input name="prodNameOMC" id="prodNameOMC" style="width: 100%"  placeholder="Enter product Name" class="form-control"/>-->
                <select id="prodNameOMC" name="prodNameOMC" class="omcProduct" style="width: 100%;" class="form-control select2" >
                    <option  value="" selected default>Select Product</option>
                    <c:forEach items="${Products}" var="product">
                        <option  value="${product.proId}">${product.proName}</option>
                    </c:forEach>
                </select>
            </div>
            <input class="hide" type="checkbox" id="oneTimeCheckbox" name="oneTimeCheckbox" value="1"> 

        </div>
        <div class="row">
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="padding-bottom: 5px">
                <label>Tank No <span class="text-danger">*</span>
                    <span style="font-size: 11px;font-family: monospace;">(max 3 digits)</span>
                </label>
                <select id="sampleTankNo" name="tankTankNo" style="width: 100%; box-shadow: none" class="form-control" required>
                </select>
            </div>
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                <label>Tank Description<strong>(Max:45)</strong><span class="text-danger">*</span></label>
                <input type="text" name="tankTankDesc" id="sampleTankDesc" style="width: 100%" placeholder="Description of Tank No" class="form-control" maxlength="45" minlength="5" required>
            </div>
            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                <label>Capacity of Tank (in Liters)<strong>(Max:6)</strong><span class="text-danger">*</span></label>
                <input type="text" name="tankCapacity" id="sampleCapactiy" style="width: 100%" placeholder="Enter Capacity of Tank" class="form-control" onkeypress="javascript: return isNumber(event)" maxlength="6" required/>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-xs-12 floating-label-form-group floating-label-form-group-with-value">
                <label>Parameters needed to be Tested for <span class="text-danger">*</span></label>
                <select style="width: 100%; border: none" class="form-control exist-test-dropdown" id="exist-test-dropdown" name="testIds" multiple="multiple" placeholder="Select Test Parameters" required><!--onchange="checkQty1();"-->
<!--                    <c:forEach items="${testMaster}" var="data" varStatus="count">
                        <option value="${data.testId}" ${data.selected}>${data.testName}</option>
                    </c:forEach>-->
                </select>
                <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
            </div>
        </div>
        <div class="row" id="lastRow">
            <div style="border-bottom: 1px solid darkgrey" id="samplePreDate" class="col-xs-3">
                <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer">
                    <label>Previously Sample Drawn Date *</label>
                    <input name="tankPreDate" id="tankPreviousDate" placeholder="Previous Sample Drawn Date *" style="width: 100%" type="text" class="form-control col-xs-2 datepicker" required/>
                    <span class="input-group-addon" style="border:0; background: none">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
            <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value" style="border-right: 1px solid darkgray">
                <label>Frequency of Sample (in Months)<span class="text-danger">*</span></label>
                <input name="tankFreq" id="sampleFreq" style="width: 100%" placeholder="How Frequent is this Sample Drawn?" class="form-control" onkeypress="javascript: return isNumber(event)" maxlength="2" onchange="addMonth();" required/>
            </div>
            <div style="border-bottom: 1px solid darkgrey;" id="manageNxtDate" class="col-xs-3">
                <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer">
                    <label>Next Sample Date *</label>
                    <input name="tankNextDate" id="sampleNextDate" placeholder="Next Sample Due Date *" style="width: 100%" type="text" class="form-control col-xs-2 datepicker" required/>
                    <span class="input-group-addon" style="border:0; background: none">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
            <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                <label>Last Oil Changed <span class="text-danger">*</span><strong>(Max:45)</strong></label>
                <input type="text" name="tankLastOilChange" id="sampleLastOilChange" style="width: 100%" placeholder="Last Oil Changed" class="form-control" maxlength="45" required/>
            </div>
        </div>

        <div class="row">



        </div>
        <br/>
        <br/>
        <div class="centre-btn">
            <input id="getSummaryTank" style="margin-right: 20px" type="button" value="Get Summary" class="btn btn-primary" title="Fetch Summary of Tanks by Customer"/>
            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
            <button type="button" class="btn btn-danger" style="margin-right: 20px" onclick="return resetFields();">Reset Fields</button>
            <button type="button" onclick="submitTank();" class="btn btn-primary btn-success">Submit</button>
        </div>
    </form>
</div>
<div id="UpdateCustomer" class="modal fade" role="dialog" data-backdrop='static'>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form role="form" class="form-inline" action="" id="updateCustomerForm">
                <input type="hidden" id="customerIndustry" name="customerIndustry"/>
                <input type="hidden" id="customerIndustryName" name="customerIndustryName"/>
                <div class="modal-header">
                    <button type="button" class="close removeBackdrop" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-Customer">Add Customer</h4>
                    <button id="addExCustomer" type="button" class="btn btn-success">Add more Customer</button>
                </div>
                <div class="modal-body container-fluid">
                    <table style="width: 100%;margin-bottom: 12px;margin: 10px">
                        <thead>
                        <th width="49%" style="padding-left: 10px;">Customer No. <span class="text-danger">*</span></th>

                        <th width="49%" style="padding-left: 10px;">Customer Name <span class="text-danger">*</span></th>

                        <th>Remove</th>
                        </thead>
                        <tbody id="custName">
                            <tr>
                                <td class="floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <input type="text" minlength="8" maxlength="10" placeholder="Enter Customer Code" class="custNoVal form-control isNumber" data-field="Customer Code" >
                                </td>
                                <td class="floating-label-form-group floating-label-form-group-with-value">
                                    <input type="text" placeholder="Enter Customer Name" class="custNameVal form-control">
                                </td>
                                <td class="text-danger">
                                    <span class="glyphicon glyphicon-remove removeRow1"></span>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="row">
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>TSE Emp Code</label>
                            <input value="${TseCustMapDetail.mstUsr.empCode}" name="EmpCodeLoaded" id="EmpCodeLoadedModal" placeholder="Enter New TSE Employee Code"  style="width: 100%" readonly="readonly" class="form-control" type="text">
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>TSE Emp Name</label>
                            <input value="${TseCustMapDetail.mstUsr.empName}" name="EmpName" id="EmpNameModal" placeholder="TSE Emp Name" style="width: 100%" readonly="readonly" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>TSE Emp Email</label>
                            <input value="${TseCustMapDetail.mstUsr.empName}" name="EmpEmail" id="EmpEmailModal" placeholder="TSE Emp Email" style="width: 100%" readonly="readonly" class="form-control" type="email">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>Ctrl Emp Code</label>
                            <input name="CtrlEmpCode" id="CtrlEmpCodeModal" placeholder="Ctrl Emp Code"  style="width: 100%" class="form-control" type="text" onchange="loadEmployeeDetailFromCEM(this.value, 2)" readonly="readonly">
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Ctrl Emp Name</label>
                            <input value="${TseCustMapDetail.mstUsr.ctrlEmpName}" name="CtrlEmpName" id="CtrlEmpNameModal" placeholder="Ctrl Emp Name"  style="width: 100%"  readonly="readonly" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>Ctrl Emp Email</label>
                            <input value="${TseCustMapDetail.mstUsr.ctrlEmpEmail}" name="CtrlEmpEmail" id="CtrlEmpEmailModal" placeholder="Ctrl Emp Email"  style="width: 100%" readonly="readonly" class="form-control" type="text">
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default btn-danger removeBackdrop" data-dismiss="modal">Cancel</button>
                    <!--<button type="button" id="customerSubmit" onclick="submitCustomer()" class="btn btn-default btn-success">Add Customer</button>-->
                    <button type="button" id="customerSubmit" class="btn btn-success">Add Customer</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="UpdateDepartment" class="modal fade" role="dialog" data-backdrop='static' style="top: 10%">
    <div class="modal-dialog modal-xl-lg">
        <div class="modal-content">
            <form role="form" class="form-inline" action="#" method="POST" id="updateDepartmentForm">
                <input type="hidden" id="deptCustomer" name="deptCustomer"/>
                <input type="hidden" id="deptCustomerName" name="deptCustomerName"/>
                <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
                <div class="modal-header">
                    <button type="button" class="close removeBackdrop" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-Department"></h4>
                </div>
                <div class="modal-body container-fluid">
                    <button type="button" class="btn btn-success btn-sm" onclick="return addRowDept();"><span class="glyphicon glyphicon-plus-sign"> </span> Add More Departments</button>
                    <table class="table" id="deptTable">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Department Name</th>
                                <th>Hod Name</th>
                                <th>Hod Email Id</th>
                                <th style="width: 15%">Hod Contact</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td style="padding-top: 20px"><span class="glyphicon glyphicon-trash" onclick="return delRowDept(this);"></span></td>
                                <td>
                                    <input name="deptName" placeholder="Enter Department Name *" type="text" style="width: 100%" class="form-control" required/>
                                </td>
                                <td>
                                    <input name="hodName" placeholder="Enter Hod Name *" type="text" style="width: 100%" class="form-control" required/>
                                </td>
                                <td>
                                    <input name="hodEmail" placeholder="Enter Hod Email Id *" type="email" style="width: 100%" class="form-control isEmail" required/>
                                </td>
                                <td>
                                    <input name="hodContact" placeholder="Enter Hod Contact *" type="text" style="width: 100%" class="form-control" required onkeypress="javascript: return isNumber(event);"minlength="10" maxlength="10"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default btn-primary removeBackdrop" data-dismiss="modal">Cancel</button>
                    <button type="submit" id="deptSubmit" class="btn btn-default btn-success">Add Department</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="UpdateEquipment" class="modal fade" role="dialog" data-backdrop='static' style="top: 10%">
    <div class="modal-dialog modal-xl-lg">
        <div class="modal-content">
            <form role="form" class="form-inline" action="#" method="POST" id="updateEquipmentForm">
                <input type="hidden" id="equipCustomer" name="equipCustomer"/>
                <input type="hidden" id="equipCustomerName" name="equipCustomerName"/>
                <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
                <div class="modal-header">
                    <button type="button" class="close removeBackdrop" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-Equipment"></h4>
                </div>
                <div class="modal-body container-fluid">
                    <div style="width: 70%; margin-left: auto; margin-right: auto">
                        <button type="button" id="addRowEquip" class="btn btn-success btn-sm" ><span class="glyphicon glyphicon-plus-sign"> </span> Add More Equipments</button>
                        <table class="table" id="equipTable">
                            <thead>
                                <tr>
                                    <th>Equipment Name</th>
                                    <th>Make Name</th>
                                    <th>Others Remarks</th>
                                    <th>Remove</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="tr_clone">
                                    <td>
                                        <input name="equipName" placeholder="Enter Equipment Name *" type="text" style="width: 100%" class="form-control" required/>
                                    </td>
                                    <td>
                                        <select id="equipMake" name="equipMake" style="width: 100%; box-shadow: none" class="form-control equipMakeClass" placeholder="">
                                            <option value="">Select Make *</option>
                                            <c:forEach items="${make}" var="data" varStatus="count">
                                                <option value="${data.makeId}">${data.makeName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input name="otherRemarks" placeholder="Enter Remarks" type="text" style="width: 100%" class="hide form-control remarksClass" title="If Others is selected in Make" value="" />
                                    </td>
                                    <td class="text-danger" style="padding-top: 20px"><span class="glyphicon glyphicon-trash" onclick="return delRowDept(this);"></span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default btn-primary removeBackdrop" data-dismiss="modal">Cancel</button>
                    <button type="submit" id="equipSubmit" class="btn btn-default btn-success">Add Equipment</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="UpdateMake" class="modal fade" role="dialog" data-backdrop='static' style="top: 15%">
    <div class="modal-dialog modal-xl-lg">
        <div class="modal-content">
            <form role="form" class="form-inline" action="#" method="POST" id="updateMakeForm">
                <input type="hidden" id="makeCustomer" name="makeCustomer"/>
                <input type="hidden" id="makeCustomerName" name="makeCustomerName"/>
                <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
                <div class="modal-header">
                    <button type="button" class="close removeBackdrop" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-Make"></h4>
                </div>
                <div class="modal-body container-fluid">
                    <div style="width: 50%; margin-left: auto; margin-right: auto">
                        <select multiple="multiple" id="makeName" name="makeName" style="width: 100%; box-shadow: none" class="form-control">
                            <option></option>
                            <c:forEach items="${make}" var="data">
                                <option value="${data.makeId}">${data.makeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default btn-primary removeBackdrop" data-dismiss="modal">Cancel</button>
                    <button type="submit" id="makeSubmit" class="btn btn-default btn-success">Add Make(s)</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="showTankDetails" class="modal fade" role="dialog" data-backdrop='static' style="top: 15%">
    <div class="modal-dialog modal-xl-lg">
        <div class="modal-content border-radius">
            <!--<form role="form" class="form-inline" action="#" method="POST" id="">-->
            <div class="modal-header">
                <button type="button" class="close removeBackdrop" data-dismiss="modal">&times;</button>
                <div class="col-xs-1 borderright" style="width: auto"><label><small>Industry Name:</small><div id="IndsAtr"></div></label></div>
                <div class="col-xs-2 borderright"><label><small>Customer Name:</small><div id="CustAtr"></div></label></div>
                <div class="col-xs-2 borderright"><label><small>Department Name:</small><div id="DeptAtr"></div></label></div>
                <div class="col-xs-2 borderright"><label><small>Application Name:</small><div id="ApplAtr"></div></label></div>
                <div class="col-xs-2 borderright "><label><small>Equipment Name:</small><div id="EquipAtr"></div></label></div>
                <div class="col-xs-2"><label><small>Product Name:</small><div id="ProdAtr"></div></label></div>
            </div>
            <div class="modal-body container">
                <div class="row" style="font-size: x-large;text-align: center;font-family: monospace;"><h3 id="titleSample"></h3></div>
                <div class="row">
                    <label for="TankNo">
                        <span>Tank No:</span>
                        <input name="TankNo" type="text" class="form-control" value="" disabled="">
                    </label>
                    <label for="TankDesc">
                        <span>Tank Description:</span>
                        <input name="TankDesc" type="text" class="form-control" value="" disabled="">
                    </label>
                    <label for="CapaTank">
                        <span>Tank Capacity:</span>
                        <input name="CapaTank" type="text" class="form-control" value="" disabled="">
                    </label>
                    <label for="AppDesc">
                        <span>Application Description:</span>
                        <input name="AppDesc" type="text" class="form-control" value="" disabled="">
                    </label>
                </div>
                <div class="row">
                    <div id="paramss" class="col-xs-10"></div>
                </div><br>
                <div class="row">
                    <label for="PrevDate">
                        <span>Previous Sampling Date:</span>
                        <input name="PrevDate" type="text" class="form-control" value="" disabled="">
                    </label>
                    <label for="NextDate">
                        <span>Next Sampling Date:</span>
                        <input name="NextDate" type="text" class="form-control" value="" disabled="">
                    </label>
                    <label for="Freq">
                        <span>Frequency of Sample(Months):</span>
                        <input name="Freq" type="text" class="form-control" value="" disabled="">
                    </label>
                    <label for="OilChan">
                        <span>Last Oil Changed:</span>
                        <input name="OilChan" type="text" class="form-control" value="" disabled="">
                    </label>
                </div>
            </div>
            <div class="modal-footer">
                <span style="float: left" class="text-info">IndianOil.in</span>
                <button type="button" class="btn btn-default btn-primary removeBackdrop" data-dismiss="modal">Cancel</button>
                <!--                    <button type="submit" id="makeSubmit" class="btn btn-default btn-success">Add Make(s)</button>-->
            </div>
            <!--</form>-->
        </div>
    </div>
</div>
<div id="tankTableDiv">
    <table class="table table-bordered table-striped table-hover tbCenter" id="tankTable">
        <thead>
            <tr>
                <th>Department</th>
                <th>Application</th>
                <th>Equipment</th>
                <th>Product</th>
                <th>Tank No</th>
                <th>Tank Description</th>
                <th>Prev Sample Date</th>
                <th>Next Sample Date</th>
                <th>App Desc</th>
                <th class="text-center">View/Remove Tank</th>
            </tr>
        </thead>
        <tbody>

        </tbody>
    </table>
</div>
<script type="text/javascript">
    $(document).ready(function () {

        var tankDT = $('#tankTable').DataTable({
            "order": [],
            "columnDefs": [
                {"className": "dt-head-center", "targets": "_all"}
            ],
            "dom": 'Bfrtip',
            "buttons": [
                {
                    extend: 'excel',
                    filename: 'Equipments Master'
                },
                'colvis'
            ]
        });
        $(document).on('click', '#getSummaryTank', function () {
            getTankSummary(tankDT);
        });
//        $('#toggle-two').bootstrapToggle({
//            on: 'OTS',
//            off: 'CMS'
//        });
        //$('#toggle-two').bootstrapToggle('on')
//        $('#toggle-two').on('change', function () {
//            if ($(this).prop('checked')) {//one-time sampling
//                $('#sampleFreq').val("0").removeAttr('required').attr('disabled');
//                $('#sampleFreq').on('change', function () {
//                    if ($(this).val() > 0 | $(this).val() == null) {
//                        $.alert({
//                            title: 'Change Not Allowed',
//                            content: 'Cannot change sample frequency for Single Sampling!',
//                            type: 'Red',
//                            typeAnimated: true
//                        });
//                        $(this).val(0);
//                    }
//                });
//                $('#oneTimeCheckbox').prop('checked', true);
////              $('#tankPreviousDate').prop('disabled','true');
//                $('#lastRow').addClass('hide');
//                $('#tankPreviousDate').attr('readonly',true);
//                $('#tankNextDate').attr('readonly',true);
//                $('#sampleLastOilChange').attr('readonly',true);
//
//            } else {
//                $('#sampleFreq').removeAttr('disabled').attr('required');
////              $('#tankPreviousDate').removeAttr('disabled');
//                $('#oneTimeCheckbox').prop('checked', true);
//                $('#sampleFreq').off();
//                $('#sampleFreq').on('change', function () {
//                    addMonth();
//                });
//                $('#lastRow').removeClass('hide');
//                $('#tankPreviousDate').attr('readonly',false);
//                $('#tankNextDate').attr('readonly',false);
//                $('#sampleLastOilChange').attr('readonly',false);
//            }
//        });
//        $('#oneTimeCheckbox').on('change', function () {
//            if ($(this).prop('checked')) {
//                $('#chcktxt').css('color', 'cornflowerblue');
//                $('#sampleFreq').val("0").removeAttr('required').attr('disabled');
//                $('#sampleFreq').on('change', function () {
//                    if ($(this).val() > 0 | $(this).val() == null) {
//                        $.alert({
//                            title: 'Change Not Allowed',
//                            content: 'Cannot change sample frequency for Single Sampling!',
//                            type: 'Red',
//                            typeAnimated: true
//                        });
//                        $(this).val(0);
//                    }
//                });
////              $('#tankPreviousDate').prop('disabled','true');
//            } else {
//                $('#chcktxt').css('color', 'lightgrey');
//                $('#sampleFreq').removeAttr('disabled').attr('required');
////              $('#tankPreviousDate').removeAttr('disabled');
//                $('#sampleFreq').off();
//                $('#sampleFreq').on('change', function () {
//                    addMonth();
//                });
//            }
//        });

        $(document).on('change', '#prodNameOMC', function () {
            $.ajax({
                url: '/ServoCMP/Tse/getProductCategory?prodId=' + $(this).val(),
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
                    console.log(data);
                    if (typeof data == "object") {
                        $('#exist-test-dropdown').empty();
                        $.each(data, function (key, val) {
                            $('#exist-test-dropdown').append("<option value=" + val.col2 + " data-qty=" + val.col4 + " data-mandatory='1' selected>" + val.col3 + "</option>").trigger('change');
                        });
                    } else {
                        $.alert({
                            title: 'No Records Found',
                            content: 'No Category mapping is there against this product.',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        });
//        $('body > span > span > span.select2-search.select2-search--dropdown > input').attr('maxlength',3)
        $('#sampleTankNo').on('change', function (evt) {
            var iKeyCode = (evt.which) ? evt.which : evt.keyCode;
            if ($(this).val().length > 3) {
                $('#sampleTankNo').val(null).trigger('change');
                $.alert({
                    title: 'Invalid',
                    content: 'The Tank no. can only have 3 or less digits.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                var valu = Array.from($(this).val());
                $.each(valu, function (i, v) {
                    if (isNaN(v)) {
                        $.alert({
                            title: '',
                            content: 'Only Numeric Values Are allowed',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                });
            }
        });
    });
</script>