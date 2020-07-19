<%-- 
    Document   : alltankdetails
    Created on : 25 Jan, 2019, 12:44:16 PM
    Author     : wrtrg2
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value=" Tank Details View"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">SYSTEM DETAILS REPORT</h4>
            <div class="text-center">
                <label class="text-info"><u>One Time Sampling Type</u>&nbsp; &rarr;&nbsp; </label>
                <span class="defFont" style='color:mediumvioletred'><i class='glyphicon glyphicon-file'></i></span>
                <span class="spacespace"></span>
                <label class="text-info"><u>CMP Sampling Type</u>&nbsp; &rarr;&nbsp; </label>
                <span class="defFont" style='color:royalblue'><i class='glyphicon glyphicon-book'></i></span>
            </div>
            <br>
            <br>
            <form class="form-inline" method="GET" id="addTank" action="${pageContext.request.contextPath}/TseAdmin/GetTank">
                <input id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" type="hidden"/>
                <div class="row">
                    
                    <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                        <label>Tse Officer <span class="text-danger">*</span></label>
                        <select id="tseOfficer" name="tseOfficer" style="width: 100%; box-shadow: none" class="form-control" onchange="getIndustryByTse(this.value)" required>
                            <option selected value="">Select TSE Officer: </option>
                            <option  value="101">All</option>
                            <c:forEach items="${tseOfficers}" var="tse">
                                <option  value="${tse.empCode}">${tse.empName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                        <label>Industry</label>
                        <select id="sampleIndustry" name="tankIndustry" style="width: 100%; box-shadow: none" class="form-control" onchange="getCustomer1(this.value)">
                            <option value="">Select Industry</option>
                        </select>
                    </div>
                    
                    <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                        <label>Customer Name <span class="text-danger">*</span></label>
                        <select id="sampleCustomer" name="tankCustomer" style="width: 100%; box-shadow: none" class="form-control" onchange="getCustomerDept1(this.value)">
                        </select>
                    </div>
                    
                    <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value hide">
                        <label>Department Name <span class="text-danger">*</span></label>
                        <select id="sampleDepartment" name="tankDepartment" style="width: 100%; box-shadow: none" class="form-control" onchange="getTank1();getCustomerEquipment1();">
                        </select>
                    </div>
                    <!--                    <div class="col-xs-4" style="margin-bottom: auto; margin-top: 25px;">
                                            <a href="#" onclick="populateSampleDepartmentData()"><span class="glyphicon glyphicon-plus"></span></a>
                                        </div>-->
                    <!--                </div>
                                    <div class="row">-->
<!--                    <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value hide">
                        <label>Application Name <span class="text-danger">*</span></label>
                        <select id="sampleApplication" name="tankApplication" style="width: 100%; box-shadow: none" class="form-control" onchange="getTank1();" required>
                            <option value="">Select Application</option>
                            <c:forEach items="${Applications}" var="app">
                                <option value="${app.appId}">${app.appName}</option>
                            </c:forEach>
                        </select>
                    </div>-->
                    <!--                    <div class="col-xs-1" style="margin-bottom: auto; margin-top: 25px;">
                                            <a href="#" onclick="populateSampleApplicationData('Add Application')"><span class="glyphicon glyphicon-plus"></span></a>
                                        </div>
                                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="padding-bottom: 2px">
                                            <label>Application Description <span class="text-danger">*</span></label>
                                            <textarea name="tankAppDesc" id="sampleAppDesc" style="width: 100%" placeholder="Description of Application" class="form-control" rows="1" maxlength="95" required></textarea>
                                        </div>-->
                    <div class="col-md-12 col-lg-3">
                        <input style="margin-right: 20px;margin-top: 13px;" type="button" value="Get Summary" class="btn btn-primary" onclick="getTankSummary1()" title="Fetch Summary of Tanks by Customer"/>
                        <!--    <button type="button" class="btn btn-danger" style="margin-right: 20px" onclick="return resetFields();">Reset Fields</button>
                                <button type="button" onclick="submitTank();" class="btn btn-primary btn-success">Submit</button>   -->
                        <button id="excelDownload" style="margin-right: 20px;margin-top: 13px;" class="form-control btn-primary border-radius" title="Fetch all MIS System Report"><span class='glyphicon glyphicon-download-alt'></span> All System Details</button>
                    </div>
                </div>
                <!--                
                            <div class="row">
                                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Equipment Name <span class="text-danger">*</span></label>
                                    <select id="sampleEquipment" name="tankEquipment" style="width: 100%; box-shadow: none" class="form-control" onchange="getTank1();" required>
                                    </select>
                                </div>
                                <div class="col-xs-4" style="margin-bottom: auto; margin-top: 25px;">
                                    <a href="#" onclick="populateSampleEquipmentData()"><span class="glyphicon glyphicon-plus"></span></a>
                                </div>
                            </div>-->
                <!--                <div class="row">
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Product Name <span class="text-danger">*</span></label>
                                        <select id="sampleProduct" name="tankProduct" style="width: 100%; box-shadow: none" class="form-control" onchange="getTank1();" required>
                                            <option value="">Select Product</option>
                                            
                                            
                <%--<c:forEach items="${Products}" var="pro">--%>
                    <option  value="${pro.proId}">${pro.proName}</option>
                <%--</c:forEach>--%>
               
                
            </select>
        </div>
        <div class="col-xs-1" style="margin-bottom: auto; margin-top: 25px;">
            <a href="#" onclick="populateSampleApplicationData('Add Product')"><span class="glyphicon glyphicon-plus"></span></a>
        </div>
        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
            <label>Search Product</label>
            <input name="prodId" id="prodId" style="width: 100%"  placeholder="Enter product code" class="form-control" data-toggle="tooltip"  data-placement="left" title="Enter product code(at least 3 digit) to populate Product Name" onchange="getProduct1();" />
        </div>
        <div class="form-check col-xs-offset-1 col-xs-4" style="height: 50x;border: 1px solid lightgrey;height: 50px;border-top: 0px;border-right: 0px;">
            <label>For One-Time Sampling:</label><br>
            <label>
                <input type="checkbox" id="oneTimeCheckbox" name="oneTimeCheckbox" value="1"> <span id="chcktxt" style="margin-left: 2px;color:graytext;">Make it One Time Sampling?</span>
            </label>
        </div>
    </div>-->
                <!--                <div class="row">
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="padding-bottom: 5px">
                                        <label>Tank No <span class="text-danger">*</span>
                                            <span style="font-size: 11px;font-family: monospace;">(max 3 digits)</span>
                                        </label>
                                        <select id="sampleTankNo" name="tankTankNo" style="width: 100%; box-shadow: none" class="form-control" required>
                                        </select>
                                    </div>
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Tank Description <span class="text-danger">*</span></label>
                                        <input type="text" name="tankTankDesc" id="sampleTankDesc" style="width: 100%" placeholder="Description of Tank No" class="form-control" maxlength="45" minlength="5" required>
                                    </div>
                                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Capacity of Tank (in Liters) *</label>
                                        <input type="text" name="tankCapacity" id="sampleCapactiy" style="width: 100%" placeholder="Enter Capacity of Tank" class="form-control" onkeypress="javascript: return isNumber(event)" required/>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-xs-12 floating-label-form-group floating-label-form-group-with-value">
                                        <label>Parameters needed to be Tested for *</label>
                                        <select style="width: 100%; border: none" class="form-control" id="exist-test-dropdown" name="testIds" multiple="multiple" placeholder="Select Test Parameters" onchange="checkQty1();" required>
                <c:forEach items="${testMaster}" var="data" varStatus="count">
                    <option value="${data.testId}" ${data.selected}>${data.testName}</option>
                </c:forEach>
            </select>
            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
        </div>
    </div>
    <div class="row">
        <div style="border-bottom: 1px solid darkgrey" id="samplePreDate" class="col-xs-3">
            <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer">
                <label>Previously Sample Drawn Date *</label>
                <input name="tankPreDate" id="tankPreviousDate" placeholder="Previous Sample Drawn Date *" required style="width: 100%" type="text" class="form-control col-xs-2 datepicker" required/>
                <span class="input-group-addon" style="border:0; background: none">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value" style="border-right: 1px solid darkgray">
            <label>Frequency of Sample (in Months)*</label>
            <input name="tankFreq" id="sampleFreq" style="width: 100%" placeholder="How Frequent is this Sample Drawn?" class="form-control" onkeypress="javascript: return isNumber(event)" maxlength="2" onchange="addMonth();" required/>
        </div>
        <div style="border-bottom: 1px solid darkgrey;" id="manageNxtDate" class="col-xs-3">
            <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer">
                <label>Next Sample Date *</label>
                <input name="tankNextDate" id="sampleNextDate" placeholder="Next Sample Due Date *" required style="width: 100%" type="text" class="form-control col-xs-2 datepicker" required/>
                <span class="input-group-addon" style="border:0; background: none">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
            <label>Last Oil Changed *</label>
            <input type="text" name="tankLastOilChange" id="sampleLastOilChange" style="width: 100%" placeholder="Last Oil Changed" class="form-control" required/>
        </div>
    </div>-->

                <div class="row">



                </div>
                <br/>
                <br/>

            </form>
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
            <table class="table table-bordered table-striped table-hover tbCenter-head" id="tankTable">
                <thead>
                    <tr>
                        <th style="width:90px">Single Sampling Type</th>
                        <th style="width: 100px">Industry</th>
                        <th style="width: 120px">Oil Application Type </th>
                        <th style="width: 120px">Customer</th>
                        <th>Department</th>
                        <th>Equipment</th>
                        <th style="width: 188px">Make</th>
                        <th style="width: 188px">Product</th>
                        <th style="width: 25px">Tank No</th>
                        <th style="width: 25px">Capacity of Tank / Sump (ltrs)</th>
                    </tr>
                </thead>
                <tbody class="tankTableCss">

                </tbody>
            </table>
        </div>

        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/tankDetails.js"></script>
    </body>
</html>
