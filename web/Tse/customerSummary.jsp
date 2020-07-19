<%-- 
    Document   : viewCreatedSampleSummary
    Created on : 15 Nov, 2017, 11:55:39 AM
    Author     : 00507469
--%>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Customer Summary"/>
    </jsp:include>
    <body>
        <div class="container">
            <h4 class="text-center text-info tbCenter" style="text-decoration: underline">Customer Summary</h4>
            <div class="row">
                <div class="col-md-4" style="padding-top: 5px;padding-right: 10px;">
                    <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">Filter:</div>
                    <div class="form-group col-lg-12 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                        <label style="padding-left: 7px;">Select Customer<span class="text-danger">*</span></label>
                        <select id="custId" name="custId" style="width: 100%; box-shadow: none" class="form-control selectFilter">
                            <option value="">Select Customer</option>
                            <c:forEach items="${cust}" var="cus">
                                <option value="${cus.customerId}">( ${cus.customerId} ) ${cus.customerName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="col-1" style="margin-top: 30px">
                    <div class="col-xs-1">
                        <button id="getCustSummary" class="btn btn-default btn-success" data-status="1" data-toggle="tooltip" data-placement="bottom" title="Fetch Departments under selected Customer">Search</button>
                    </div>
                </div>
                <div class="col-1" style="margin-top: 30px">
                    <div class="col-xs-1">
                        <button id="openCustModal" class="btn btn-default btn-success" data-status="1" data-toggle="tooltip" data-placement="bottom" title="Edit Name of Selected Customer">Change Name</button>
                    </div>
                </div>
            </div>
            <br>
            <br>
            <div class="row">
                <table id="custTable" class="table table-bordered tbCenter" style="border-bottom: 1px solid darkgray;">
                    <thead>
                        <tr style="font-size:small">
                            <th style="width: 30%">Department Name</th>
                            <th style="width: 20%">HOD Name</th>
                            <th style="width: 20%">HOD Email</th>
                            <th style="width: 15%">HOD Contact</th>
                            <th style="width: 15%">Updated By</th>
                            <th style="width: 10%">Action</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
            <br>
            <br>
            <br>
            <div class="row" style="border: 1px solid darkgrey;padding: 12px;border-radius: 6px;margin-bottom: 15px;">
                <h4 style="font-family: monospace;">Other Details: </h4>
                <div class="col-md-4"><label for="TotsSamples">Total No. Of Samples Send to Customer: </label><input id="TotsSamples" type="text" class="form-control" style="width:100%" value="" disabled></div>
                <div class="col-md-4"><label for="TotalSaps">Sap-Codes Assigned to this Customer: </label><input id="TotsSapLink" type="text" class="form-control" style="width:100%" value="" disabled></div>
            </div>
        </div>
        
        <div id="UpdateHODInfo" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST" id="formm">
                        <input type="hidden" id="departmentId" name="departmentId" value=""/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <!--<input type="hidden" id="departmentName" name="departmentName" value=""/>-->
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-LabEquip" class="modal-title">Update Department Details</h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="row" style="border: 1px solid darkgrey;padding: 12px;border-radius: 6px;margin-bottom: 15px;">
                                <h4 style="font-family: monospace;">HOD Details</h4>
                                <div class="col-md-3"><label for="Departmentname">Department Name:<span class="text-danger">*</span><strong>(Max:40)</strong></label><input id="Departmentname" name="Departmentname" type="text" class="form-control upperCase" style="width:100%" maxlength="45" value=""></div>
                                <div class="col-md-3"><label for="HODname">HOD Name:<span class="text-danger">*</span><strong>(Max:40)</strong></label><input id="HODname" name="HODname" type="text" class="form-control upperCase" style="width:100%" maxlength="40" value=""></div>
                                <div class="col-md-3"><label for="HODemail">HOD Email:<span class="text-danger">*</span><strong>(Max:40)</strong></label><input id="HODemail" name="HODemail" type="text" class="form-control" style="width:100%" maxlength="40" value=""></div>
                                <div class="col-md-3"><label for="HODcontact">HOD Contact:<span class="text-danger">*</span><strong>(Max:12)</strong></label><input id="HODcontact" name="HODcontact" type="text" class="form-control" style="width:100%" maxlength="12" value=""></div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                            <button type="button" id="editHOD" class="btn btn-default btn-success">Submit</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <div id="UpdateCustomerTable" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST" id="formm">
                        <input type="hidden" id="newCustId" name="custId" value=""/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title-LabEquip" class="modal-title">Update Customer</h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="row" style="border: 1px solid darkgrey;padding: 12px;border-radius: 6px;margin-bottom: 15px;">
                                <div class="col-md-3"><label for="customerId">Customer Id:</label><input id="customerId" name="customerId" type="text" class="form-control" style="width:100%" value="" disabled></div>
                                <div class="col-md-9"><label for="customerName">Customer Name:<span class="text-danger">*</span><strong>(Max:35)</strong></label><input id="custName" name="customerName" type="text" class="form-control" style="width:100%" maxlength="35" value=""></div>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                            <button type="button" id="editCustomerName" class="btn btn-default btn-success">Submit</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>


