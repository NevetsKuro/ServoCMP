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
            <h4 class="centre-btn" style="text-decoration: underline">UPDATE TANKS DETAILS</h4>
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
                    <input style="margin-right: 20px;margin-top: 13px;" type="button" value="Get Tanks" class="btn btn-primary" onclick="getTankSummary1()" title="Fetch tank details"/>
                </div>
        </div>
        <div class="row">



        </div>
        <br/>
        <br/>

    </form>
</div>


<div id="showTankDetails" class="modal fade" role="dialog" data-backdrop='static' style="top: 5%;">
    <div class="modal-dialog modal-xl-lg">
        <div class="modal-content border-radius">
            <!--<form role="form" class="form-inline" action="#" method="POST" id="">-->
            <div class="modal-header">
                <button type="button" class="close removeBackdrop" data-dismiss="modal">&times;</button>
                <h2><center>Update Sample</center></h2>
            </div>
            <div class="modal-body container">
                <input id="tankId" name="tankId" type="hidden" value="">
                <div class="row" style="font-size: x-large;text-align: center;font-family: monospace;"><h3 id="titleSample"></h3></div>
                <div class="row">
                    <div class="col-xs-3">
                        <label for="TankNo">
                            <span>Tank No:</span>
                            <input name="TankNo" type="text" class="form-control" value="" disabled="">
                        </label>
                    </div>
                    <div class="col-xs-3">
                        <label for="TankDesc">
                            <span>Tank Description:</span>
                            <input name="TankDesc" type="text" class="form-control" value="" disabled="">
                        </label>
                    </div>
                    <div class="col-xs-3">
                        <label for="AppDesc">
                            <span>Application Description:</span>
                            <input name="AppDesc" type="text" class="form-control" value="" disabled="">
                        </label>
                    </div>
                    <div class="col-xs-3">
                        <label for="NextDate">
                            <span>Next Sampling Date:</span>
                            <input name="NextDate" type="text" class="form-control" value="" disabled="">
                        </label>
                    </div>
                </div>
                <div class="row">
                    <hr/>
                    <div id="paramss" class="col-xs-10"></div>
                </div><br>
                <div class="row">
                    <div class="col-xs-3">
                        <label for="Freq">
                            <span>Frequency of Sample(Months):</span>
                            <input id="NewFreq" name="Freq" type="text" class="form-control" placeholder="How Frequent is this Sample Drawn?"  onkeypress="javascript: return isNumber(event)"  maxlength="2" value="">
                        </label>
                    </div>
                    <!--                    <div id="NewNextSamDate" class="col-xs-4 form-group">
                                            <div id="dateContainer" class="input-group date col-xs-12" data-date-container="#dateContainer">
                                                <label>Postponed till Date *</label>
                                                <input name="NewNextSamDate" placeholder="Postpone till date *" required style="width: 100%" type="text" class="form-control col-xs-2 datepicker"/>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-calendar"></span>
                                                </span>
                                            </div>
                                        </div>-->
                    <div style="border-bottom: 1px solid darkgrey" id="NewNextSamDate" class="col-xs-2">
                        <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer" data-date->
                            <label>Enter From Date <span class="text-danger">*</span></label>
                            <input name="NewNextSamDate" placeholder="Enter From Date *"  style="width: 100%" type="text" class="form-control col-xs-2 datepicker" value="" required/>
                            <span class="input-group-addon" style="border:0; background: none">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">

                </div>
            </div>
            <div class="modal-footer">
                <span style="float: left" class="text-info">IndianOil.in</span>
                <button type="button" class="btn btn-default btn-primary removeBackdrop" data-dismiss="modal">Cancel</button>
                <button type="submit" id="changeTankSubmit" class="btn btn-default btn-success">Update Sample</button>
            </div>
            <!--</form>-->
        </div>
    </div>
</div>
<div id="tankTableDiv" class="row">
    <table class="table table-bordered table-striped table-hover tbCenter-head col-md-12 cursorOnHoverTable" id="tankTable">
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
