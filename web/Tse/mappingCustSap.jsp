<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Mapping Customer And SAP code"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
    <div class=" container">
        <div class="row">
            <section>
                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                    <label>Select Customer:</label>
                    <select id="CustCode" name="custCode" style="width: 100%; box-shadow: none" class="form-control" required>
                        <option></option>
                        <c:forEach var="cust" items="${custList}">
                            <option value="${cust.code}">${cust.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-xs-offset-1 col-xs-4" style="margin-top: 16px">
                    <button id="openSapModal" type="button" class="btn btn-success"  data-toggle="tooltip"  data-placement="right" title="Add New SAP code to selected customer">Add</button>
                </div>
            </section>
        </div>
        <div class="row">
            <section>
                <div id="existSAP" style="border: 1px solid black;padding: 2px;margin: 8px;border-radius: 6px;display: flow-root;">
                    <!--existing SAP codes-->
<!--                    <div class="sapcodes" style="background-color: bisque;width: fit-content;padding: 11px;line-height: 12px;border-radius: 12px;box-shadow: 3px 2px 10px black;font-family: monospace;color: black;margin: 7px;float:left">
                        <p>SAP code #1</p><p style="font-size: 20px;text-align: center">01515</p>
                    </div>-->
                        <div class="noData" style="
                            border: 4px dashed darkgrey;
                            border-radius: 12px;
                            margin: 7px;
                            text-align: center;
                            font-family: monospace;
                            font-size: x-large;
                            font-weight: bolder;
                            color: darkgray;
                        ">Select Customer...</div>
                    
                </div>
            </section>
        </div>
        <div class="row">
            <section>
                <div>
                <h3><strong>Summary</strong></h3>
                <span id="getSummary" data-toggle="tooltip"  data-placement="right" title="Get List of all customer with their mapped SAP codes" class="btn btn-success btn-toolbar" style="float: right;margin-top: 10px" data-emp-code="${sUser.sEMP_CODE}">Get Data</span>
                </div>
                <table id="SAPTable" class="table table-bordered table-striped table-hover tbCenter-head">
                    <thead>
                        <th>Customer Name</th>
                        <th>Sap code</th>
                        <th>Update By</th>
                        <th>Update On</th>
                    </thead>
                    <tbody class="summary">
                        
                    </tbody>
                </table>
            </section>
        </div>
    </div>
    
    
<div id="AddSAPcode" class="modal fade" role="dialog" data-backdrop='static'>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form role="form" class="form-inline" action="" id="updateCustomerForm">
                <input type="hidden" id="customerCode" name="customerCode"/>
                <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}"/>
                <div class="modal-header">
                    <button type="button" class="close removeBackdrop" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-Customer">Add Customer</h4>
                    <button id="addExSAP" type="button" class="btn btn-success">Add more SAP Codes</button>
                </div>
                <div class="modal-body container-fluid">
                    <table style="width: 100%;margin-bottom: 12px;margin: 10px">
                        <thead>
                        <th width="49%" style="padding-left: 10px;">Cust No. <span class="text-danger">*</span></th>
                            
                        <th width="49%" style="padding-left: 10px;">SAP No.<span class="text-danger">*</span></th>
                        
                        <th>Remove</th>
                        </thead>
                        <tbody id="SAPS">
                            <tr>
                                <td class="floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <input id="defCustCode" type="text" minlength="8" maxlength="10" placeholder="Enter Customer Code" class="custNoVal custSapNoVal form-control" value="" disabled>
                                </td>
                                <td class="floating-label-form-group floating-label-form-group-with-value">
                                    <input data-field="SAP Number" type="text" placeholder="Enter SAP code" class="SAPcode form-control isNumber">
                                </td>
                                <td class="text-danger">
                                    
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    
                </div>
                <br/>
                <br/>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-primary removeBackdrop" data-dismiss="modal">Cancel</button>
                    <button type="button" id="addSapDetails" class="btn btn-success">Add SAP</button>
                </div>
            </form>
        </div>
    </div>
</div>
        
        
<div id="EditSAPcode" class="modal fade" role="dialog" data-backdrop='static'>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form role="form" class="form-inline" action="" id="updateCustomerForm">
                <input type="hidden" id="customerCode2" name="customerCode"/>
                <input type="hidden" id="PrevsapCode" name="sapCode"/>
                <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}"/>
                <div class="modal-header">
                    <button type="button" class="close removeBackdrop" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-Customer">Update Customer</h4>
                </div>
                <div class="modal-body container-fluid">
                    <table style="width: 100%;margin-bottom: 12px;margin: 10px">
                        <thead>
                        <th width="49%" style="padding-left: 10px;">Cust No. <span class="text-danger">*</span></th>
                            
                        <th width="49%" style="padding-left: 10px;">SAP No.<span class="text-danger">*</span></th>
                        
                        </thead>
                        <tbody id="SAPS2">
                            <tr>
                                <td class="floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                    <input id="defCustCode2" type="text" minlength="8" maxlength="10" placeholder="Enter Customer Code" class="custNoVal custSapNoVal form-control" value="" disabled>
                                </td>
                                <td class="floating-label-form-group floating-label-form-group-with-value">
                                    <input id="defSAPCode2" type="text" placeholder="Enter SAP code" class="form-control">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    
                </div>
                <br/>
                <br/>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default btn-primary removeBackdrop" data-dismiss="modal">Cancel</button>
                    <button type="button" id="editSapDetails" class="btn btn-success">Update SAP</button>
                </div>
            </form>
        </div>
    </div>
</div>
        
    </body>
</html>