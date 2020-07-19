<%-- 
    Document   : receiveSample
    Created on : Jan 7, 2017, 8:50:04 PM
    Author     : Manish Jangir
--%>
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Acknowledge Sample"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="text-center text-info text-primary priorityLevel${rs.samplepriorityId}" style="border-bottom: 1px solid">Acknowledge Sample</h4>
            <div class="text-center">
                <label class="text-info"><u>PRIORITY</u>&nbsp; &rarr;&nbsp; </label>
                <span class="badge priorityLevel3">NORMAL</span>
                <span class="badge priorityLevel2">MEDIUM</span>
                <span class="badge priorityLevel1">HIGH</span>
            </div>
            <div>
                <form role="form" class="form-inline" action="${pageContext.request.contextPath}/Lab/AcknowledgeSample" method="POST" id="receivesampleForm">
                    
                    <br/>
                    <input name="smplid" type="hidden" value="${rs.sampleId}"/>
                    <input name="samplelabCode" type="hidden" value="${rs.mstLab.labCode}"/>
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    <div class="row">
                        <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                            <label>Industry</label>
                            <input value="${rs.mstInd.indName}" name="indName" placeholder="Industry" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>Customer</label>
                            <input value="${rs.mstDept.mstCustomer.customerName}" name="customerName" placeholder="Customer" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>Application</label>
                            <input value="${rs.mstApp.appName}" name="appName" placeholder="Application" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
                            <label>Equipment</label>
                            <input value="${rs.mstEquip.equipmentName}" name="equipmentName" placeholder="Equipment" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>Product</label>
                            <input value="${rs.mstProd.proName}" name="proName" placeholder="Product" disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-3 floating-label-form-group has-error floating-label-form-group-with-value">
                            <label>Sample Drawn in ml <span class="">*</span></label>
                            <input value="${rs.qtyDrawn}" name="qtyDrawn" placeholder="Qunatity Drawn in ml *" required="required" style="width: 100%" class="form-control col-xs-2" type="text" disabled>
                        </div>
                        <div id="dateContainer" class="input-group date col-xs-4 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer">
                            <label>Sample Created Date <span class="text-danger">*</span></label>
                            <input value="${rs.stringsamplecreatedDate}" name="qtydrawnDate" placeholder="Quantity Drawn date *" required="required" style="width: 100%" type="text" class="form-control col-xs-2 datepicker" disabled/>
                            <span class="input-group-addon" style="border:0; background: none">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                            <label>TOP UP Quantity between ${rs.stringpresampleDate} and ${rs.stringsamplecreatedDate} *</label>
                            <input value="${rs.topupQty}" name="topupQty" placeholder="TOP UP Quantity between last Sample date and today's date" style="width: 100%" class="form-control col-xs-2" type="text" disabled>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-12 floating-label-form-group floating-label-form-group-with-value">
                            <label>Parameters needed to be Tested for <span class="text-danger">*</span></label>
                            <select name="testIds" style="width: 100%; border: none" class="form-control exist-test-dropdown" id="exist-test-dropdown" multiple="multiple" disabled>
                                <c:forEach items="${existTestList}" var="data" varStatus="count">
                                    <option selected value="${data.testId}">${data.testName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Comments on Sample</label>
                            <textarea name="sampledrawnRemarks" style="width: 100%" placeholder="Comments on Sample" class="form-control" rows="2" maxlength="80" disabled>${rs.sampledrawnRemarks}</textarea>
                        </div>
                        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Reasons for Priority</label>
                            <textarea name="samplepriorityRemarks" style="width: 100%" placeholder="Reasons for Priority of Sample" class="form-control" rows="2" disabled>${rs.samplepriorityRemarks}</textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-8 floating-label-form-group">
                            <label>Comments on Sample Received <span class="text-danger">*</span></label>
                            <textarea name="samplereceivedRemarks" style="width: 100%" placeholder="Comments on Sample Received" class="form-control" rows="1" maxlength="95"></textarea>
                        </div>
                        <div style="border-left: 1px solid darkgrey; border-bottom: 1px solid darkgrey" id="recsampleDate" class="col-xs-4">
                            <div style="border: none" id="dateContainer" class="col-xs-12 input-group date floating-label-form-group floating-label-form-group-with-value" data-date-container="#dateContainer">
                                <label>Parameters' Result Expected Date <span class="text-danger">*</span></label>
                                <input value="${rs.defaultSelectedDate}" id="labreceiveDate" name="labreceiveDate" placeholder="Parameters' Result Expected Date *" required type="text" class="col-xs-12 form-control" onchange="eodDropdown(this.value, ${normalpriorityDays});"/>
                                <div class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                <span class="input-group-addon" style="border:0; background: none">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div id="delayReason" style="visibility: hidden; height: 50px" class="col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Reason when Parameters' Result Expected Date is beyond 5 Days <span class="text-danger">*</span></label>
                            <select onchange="equipmentDead(this)" id="selectdelayReasonid" name="delayReason" style="display: none; width: 100%" class="form-control">
                                <option value="">Select reason when Parameters' Result Expected Date is more than 5 Days </option>
                                <c:forEach items="${resultDelayReasonList}" var="rstDelayMaster">
                                    <option value="${rstDelayMaster.reasonId}">${rstDelayMaster.reasonName}</option>
                                </c:forEach>
                            </select>
                            <div id="delayreasonerrMsg" class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div id="Equipment" style="visibility: hidden; height: 50px" class="col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Which Equipment not Working? <span class="text-danger">*</span></label>
                            <select id="Equipmentid" name="Equipment" style="display: none; width: 100%" class="form-control">
                                <option value="">Select Equipment</option>
                            </select>
                            <div id="EquipmenterrMsg" class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                    </div>
                    <div class="row form-inline">
                        <br/>
                        <div class="centre-btn">
                            <a href="${pageContext.request.contextPath}/Lab/SampleSendByTSE?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" class="btn-primary form-control cursorOnHover" data-placement="top" title="Summary of samples sent to lab and yet to acknowledge upon received physically" style="margin-right: 20px; text-decoration: none">GO BACK</a>
                            <input id="Acknowledge" data-toggle="tooltip" data-placement="top" title="Acknowledge Sample for Testing" type="submit" class="form-control btn-success" value="Acknowledge">
                        </div>    
                    </div>
                </form>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>