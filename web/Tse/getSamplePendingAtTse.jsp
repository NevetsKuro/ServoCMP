<!--Previous Page:GetSamplePendingAtTseServlet-->
<!--Create Sample Page-->
<!--Next Page:Page:GetSamplePendingAtTseServlet-POST-->
<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Create Sample for Testing"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <div id="takeSample1" class="test">
                <h4 class="text-center text-info text-primary" style="border-bottom: 1px solid">
                    <c:if test="${sampleType=='0'}">
                        <span style="float: left; font-size: 12px">Last Sample Date: ${cs.stringpresampleDate} &nbsp;&nbsp;&nbsp;</span>
                        <span style="float: left; font-size: 12px">
                            <a title="Click here to get details of postponed history." onclick="callGetSamplePostponedDetail('${cs.tankId}', '${cs.stringpresampleDate}')" style="cursor: pointer">
                                Postponed History
                            </a>
                        </span>
                    </c:if>
                    <span class="text-center">Create ${sampleType == '0'?'CMP':'OTS'} Sample</span>
                    <c:if test="${sampleType=='0'}">
                        <span style="float: right; font-size: 12px">Sample Due Date: ${cs.stringnxtsampleDate}</span>
                    </c:if>
                </h4>
                <div id="Sample">
                    <form class="form-inline" action="${pageContext.request.contextPath}/Tse/CreateSample" method="POST" role="form" id="sendsampletoLAB">
                        <br/>
                        <input type="hidden" value="${cs}" name="cs"/>
                        <input type="hidden" value="1" name="labType" id="labType"/>
                        <input type="hidden" id="csrftoken" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <input type="hidden" id="formValid" name="formValid" value="1" />
                        <input type="hidden" name="sampleProductId" id="sampleProductId" value="${listproduct[0].proId}"/>
                        <input type="hidden" name="sampleProductName" id="sampleProductName" value="${listproduct[0].proName}"/>
                        <input type="hidden" name="sampleTestQty" id="sampleTestQty" value=""/>
                        <input type="hidden" name="checkOtsSampling" id="checkOtsSampling" value="${sampleType}"/>
                        <input id="qtyDrawn" value="${cs.qtyDrawn}" name="qtyDrawn" type="hidden">
                        <div class="row">
                            <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                                <label>Industry</label>
                                <input value="${cs.mstInd.indName}" name="indName" placeholder="Industry" disabled="disabled" style="width: 100%" class="form-control" type="text">
                            </div>
                            <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                                <label>Customer</label>
                                <input value="${cs.mstDept.mstCustomer.customerName}" name="customerName" placeholder="Customer" disabled="disabled" style="width: 100%" class="form-control" type="text">
                            </div>
                            <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                                <label>Department</label>
                                <input value="${cs.mstDept.departmentName}" name="departmentName" placeholder="Department" disabled="disabled" style="width: 100%" class="form-control" type="text">
                            </div>
                            <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                                <label>Application</label>
                                <input value="${cs.mstApp.appName}" name="appName" placeholder="Application" disabled="disabled" style="width: 100%" class="form-control" type="text">
                            </div>
                            <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                                <label>Describe Application</label>
                                <input value="${cs.descAppl}" name="descAppName" placeholder="Describe Application" disabled="disabled" style="width: 100%" class="form-control" type="text">
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                <label>Equipment</label>
                                <input value="${cs.mstEquip.equipmentName}" name="equipmentName" placeholder="Equipment" disabled="disabled" style="width: 100%" class="form-control" type="text">
                            </div>
                            <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                                <label>Tank no</label>
                                <input value="${cs.tankNo}" name="tankNo" placeholder="Tank no" disabled="disabled" style="width: 100%" class="form-control" type="text">
                            </div>
                            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                <label>Product</label>
                                <input value="${cs.mstProd.proName}" name="proName" placeholder="Product" disabled="disabled" style="width: 100%" class="form-control" type="text">
                            </div>
                            <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                                <label>Capacity in Ltrs</label>
                                <input value="${cs.mstProd.proCapacity}" name="proCapacity" placeholder="Capacity in ltrs." disabled="disabled" style="width: 100%" class="form-control" type="text">
                            </div>
                        </div>
                        <div class="row">
                            <!--<div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Sample Drawn(in ml)<span class="has-error">*</span></label>
                                    <input id="qtyDrawn" value="${cs.qtyDrawn}" name="qtyDrawn" placeholder="Sample Drawn(in ml)*" required="required" style="width: 100%" class="form-control col-xs-2" type="text" onchange="" onkeypress="javascript:return isNumber(event)">
                                    <div style="border: none" class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                </div>-->
                            <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                                <label>Sample Priority *</label>
                                <select name="csl_priorityId" style="width: 100%; box-shadow: none" class="form-control" >
                                    <c:forEach items="${prioritymaster}" var="prioMaster">
                                        <option value="${prioMaster.priorityId}">${prioMaster.priorityName}</option>
                                    </c:forEach>
                                </select>
                                <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                            </div>
                            <div style="border-left: 1px solid darkgrey; border-bottom: 1px solid darkgrey" id="createSampleDate" class="col-xs-3">
                                <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-date-format="dd-mm-yyyy" data-date-start-date="${cs.sdStartDate}" data-date-end-date="${cs.sdEndDate}" data-date-container="#dateContainer">
                                    <label>Sample Drawn Date *</label>
                                    <input value="${cs.qtydrawnDate}" name="qtyDrawnDate" placeholder="Sample Drawn date *" required="required" style="width: 100%" type="text" class="form-control col-xs-2 datepicker"/>
                                    <div class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                    <span class="input-group-addon" style="border:0; background: none">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                <label>TOP UP Quantity between ${cs.stringpresampleDate} and ${cs.stringsamplecreatedDate} (in Ltrs) *</label>
                                <input value="${cs.topupQty}" name="topupQty" placeholder="TOP UP Quantity between ${cs.stringpresampleDate} and ${cs.stringsamplecreatedDate} (in Ltrs)" style="width: 100%" class="form-control col-xs-2" type="text" onkeypress="javascript:return isNumber(event)" maxlength="10">
                                <div class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                            </div>
                            <div class="form-group col-xs-3 floating-label-form-group  floating-label-form-group-with-value">
                                <label>Running Hrs(Current Oil) <span class="has-error">*</span></label>
                                <input value="${cs.runningHrs}" name="runningHrs" placeholder="Running Hrs(Current Oil) *" required="required" style="width: 100%" class="form-control col-xs-2" type="text" onkeypress="javascript:return isNumber(event)" maxlength="10">
                                <div style="border: none" class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                            </div>
                        </div>
                        <div id="labTypeTab">

                            <!-- Nav tabs -->
                            <ul class="nav nav-pills nav-justified" role="tablist">
                                <li role="presentation" class="active bgcolor-grey color-txt-aliceblue"><a href="#CSL" aria-controls="CSL" role="tab" data-toggle="tab" data-labType="1">CSL</a></li>
                                <li role="presentation" class="bgcolor-grey color-txt-aliceblue"><a href="#RND" aria-controls="RND" role="tab" data-toggle="tab" data-labType="2">R&D</a></li>
                                <li role="presentation" class="bgcolor-grey color-txt-aliceblue"><a href="#CSLnRND" aria-controls="CSLnRND" role="tab" data-toggle="tab" data-labType="3">BOTH( CSL+R&D )</a></li>
                            </ul>

                            <!-- Tab panes -->
                            <div class="tab-content borderStyleAccords" >
                                <div role="tabpanel" class="tab-pane fade in active" id="CSL">
                                    <div class="row">
                                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Lab Name</label>
                                            <select id="labCodeId" name="csl_labCode" style="width: 100%; box-shadow: none" class="form-control labCodeId">
                                                <c:forEach items="${labmaster}" var="labMaster">
                                                    <option id="${labMaster.labAuthority}" value="${labMaster.labCode}" selected>${labMaster.labName}</option>
                                                </c:forEach>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Lab In Charge *</label>
                                            <input id="labAuthorityId" name="csl_labAuthority" placeholder="Head of Laboratory" style="width: 100%" class="form-control labAuthorityId" type="text" readonly="readonly" value="${labmaster[0].labAuthority}">
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Sample Drawn(in ml)<span class="has-error">*</span></label>
                                            <input id="csl_qtyDrawn" value="${cs.qtyDrawn}" name="csl_qtyDrawn" placeholder="Sample Drawn(in ml)*" required="required" style="width: 100%" class="qtyDrawnByUser csl_qtyDrawn form-control col-xs-2" type="text" onchange="" onkeypress="javascript:return isNumber(event)" maxlength="10">
                                            <div style="border: none" class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Parameters needed to be Tested for *</label>
                                            <select name="csl_testIds" style="width: 100%; border: none" class="form-control exist-test-dropdown exist-test-dropdown_tab1" id="exist-test-dropdown" title="Choose Parameters to be Tested for" multiple="multiple" onchange="checkQtySamplePage();">
                                                <c:forEach items="${preTestList}" var="data" varStatus="count">
                                                    <option class="color-txt-palevioletred" data-qty="${data.sampleqty}" selected value="${data.testId}" data-spec="${data.mstTestParam.checkId}" data-mandatory="1" data-active="${data.active}" >${data.testName}</option>
                                                </c:forEach>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-5 floating-label-form-group">
                                            <label>Specify Additional Parameters needed to be Tested for (if any) *</label>
                                            <select name="csl_addtestIds" style="width: 100%; border: none" class="form-control add-test-dropdown exist-add-dropdown_tab1" id="add-test-dropdown" multiple="multiple" onchange="checkQtySamplePage()">
                                                <c:forEach items="${addTestList}" var="data" varStatus="count">
                                                    <option data-qty="${data.sampleqty}" value="${data.testId}" data-spec="${data.mstTestParam.checkId}" data-active="${data.active}">${data.testName} <span class="hidden">${data.sampleqty}</span></option>
                                                </c:forEach>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane fade " id="RND">
                                    <div class="row">
                                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Lab Name</label>
                                            <select id="labCodeId" name="rnd_labCode" style="width: 100%; box-shadow: none" class="form-control labCodeId">
                                                <option value="${labmaster2[0].labCode}">${labmaster2[0].labName}</option>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Lab In Charge *</label>
                                            <input id="labAuthorityId" name="rnd_labAuthority" placeholder="Head of Laboratory" style="width: 100%" class="form-control labAuthorityId" type="text" readonly="readonly"  value="${labmaster2[0].labAuthority}">
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Sample Drawn(in ml)<span class="has-error">*</span></label>
                                            <input id="rnd_qtyDrawn" value="${cs.qtyDrawn}" name="rnd_qtyDrawn" placeholder="Sample Drawn(in ml)*" required="required" style="width: 100%" class="qtyDrawnByUser rnd_qtyDrawn form-control col-xs-2" type="text" onchange="" onkeypress="javascript:return isNumber(event)" maxlength="10">
                                            <div style="border: none" class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Parameters needed to be Tested for <span class="text-danger">*</span></label>
                                            <select name="rnd_testIds" style="width: 100%; border: none" class="form-control exist-test-dropdown exist-test-dropdown_tab2" id="exist-test-dropdown" title="Choose Parameters to be Tested for" multiple="multiple" onchange="checkQtySamplePage();">
                                                <c:forEach items="${preTestList}" var="data" varStatus="count">
                                                    <option data-qty="${data.sampleqty}" selected value="${data.testId}" data-spec="${data.mstTestParam.checkId}" data-mandatory="1" data-active="${data.active}">${data.testName}</option>
                                                </c:forEach>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-5 floating-label-form-group">
                                            <label>Specify Additional Parameters needed to be Tested for (if any) <span class="text-danger">*</span></label>
                                            <select name="rnd_addtestIds" style="width: 100%; border: none" class="form-control add-test-dropdown exist-add-dropdown_tab2" id="add-test-dropdown" multiple="multiple" onchange="checkQtySamplePage();" >
                                                <c:forEach items="${addTestList}" var="data" varStatus="count">
                                                    <option data-qty="${data.sampleqty}" value="${data.testId}" data-spec="${data.mstTestParam.checkId}" data-active="${data.active}">${data.testName} <span class="hidden">${data.sampleqty}</span></option>
                                                </c:forEach>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane fade " id="CSLnRND">
                                    <div class="row">
                                        <h4>CSL Details:</h4>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                                            <label>CSL Lab Name</label>
                                            <select id="labCodeId" name="csl_bth_labCode" style="width: 100%; box-shadow: none" class="form-control labCodeId">
                                                <c:forEach items="${labmaster}" var="labMaster">
                                                    <option id="${labMaster.labAuthority}" value="${labMaster.labCode}" selected>${labMaster.labName}</option>
                                                </c:forEach>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Lab In Charge *</label>
                                            <input id="labAuthorityId" name="csl_bth_labAuthority" placeholder="Head of Laboratory" style="width: 100%" class="form-control labAuthorityId" type="text" readonly="readonly"  value="${labmaster[0].labAuthority}">
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Sample Drawn(in ml)<span class="has-error">*</span></label>
                                            <input id="csl_qtyDrawn1" value="${cs.qtyDrawn}" name="csl_qtyDrawn1" placeholder="Sample Drawn(in ml)*" required="required" style="width: 100%" class="qtyDrawnByUser csl_qtyDrawn1 form-control col-xs-2" type="text" onchange="" onkeypress="javascript:return isNumber(event)" maxlength="10">
                                            <div style="border: none" class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Parameters needed to be Tested for <span class="text-danger">*</span></label>
                                            <select name="csl_bth_testIds" style="width: 100%; border: none" class="form-control exist-test-dropdown2 sample-csl-test-dropdown" id="exist-test-dropdown" title="Choose Parameters to be Tested for" multiple="multiple" onchange="checkQtySamplePage()"/><!-- onchange="checksampleQty(this);">-->
                                            <c:forEach items="${preTestList}" var="data" varStatus="count">
                                                <option data-qty="${data.sampleqty}" selected value="${data.testId}" data-spec="${data.mstTestParam.checkId}" data-mandatory="1" data-active="${data.active}">${data.testName}</option>
                                            </c:forEach>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Specify Additional Parameters needed to be Tested for (if any) <span class="text-danger">*</span></label>
                                            <select name="csl_bth_addtestIds" style="width: 100%; border: none" class="form-control add-test-dropdown sample-csl-add-dropdown" id="add-test-dropdown" multiple="multiple" onchange="checkQtySamplePage()">
                                                <c:forEach items="${addTestList}" var="data" varStatus="count">
                                                    <option data-qty="${data.sampleqty}" value="${data.testId}" data-spec="${data.mstTestParam.checkId}" data-active="${data.active}">${data.testName} <span class="hidden">${data.sampleqty}</span></option>
                                                </c:forEach>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <h4>RND Details:</h4>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                                            <label>RND Lab Name</label>
                                            <select id="labCodeId" name="rnd_bth_labCode" style="width: 100%; box-shadow: none" class="form-control labCodeId">
                                                <option value="${labmaster2[0].labCode}">${labmaster2[0].labName}</option>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Lab In Charge <span class="text-danger">*</span></label>
                                            <input id="labAuthorityId" name="rnd_bth_labAuthority" placeholder="Head of Laboratory" style="width: 100%" class="form-control " type="text" readonly="readonly" value="${labmaster2[0].labAuthority}" >
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Sample Drawn(in ml)<span class="has-error">*</span></label>
                                            <input id="rnd_qtyDrawn1" value="${cs.qtyDrawn}" name="rnd_qtyDrawn1" placeholder="Sample Drawn(in ml)*" required="required" style="width: 100%" class="qtyDrawnByUser rnd_qtyDrawn1 form-control col-xs-2" type="text" onchange="" onkeypress="javascript:return isNumber(event)" maxlength="10">
                                            <div style="border: none" class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Parameters needed to be Tested for <span class="text-danger">*</span></label>
                                            <select name="rnd_bth_testIds" style="width: 100%; border: none" class="form-control exist-test-dropdown2 sample-rnd-test-dropdown" id="exist-test-dropdown" title="Choose Parameters to be Tested for" multiple="multiple" onchange="checkQtySamplePage()"/><!-- onchange="checksampleQty(this);">-->

                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                        <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                                            <label>Specify Additional Parameters needed to be Tested for (if any) *</label>
                                            <select name="rnd_bth_addtestIds" style="width: 100%; border: none" class="form-control add-test-dropdown sample-rnd-add-dropdown" id="add-test-dropdown" multiple="multiple" onchange="checkQtySamplePage()" >
                                                <c:forEach items="${addTestList}" var="data" varStatus="count">
                                                    <option data-qty="${data.sampleqty}" value="${data.testId}" data-spec="${data.mstTestParam.checkId}"  data-active="${data.active}">${data.testName} <span class="hidden">${data.sampleqty}</span></option>
                                                </c:forEach>
                                            </select>
                                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-xs-6 floating-label-form-group">
                                        <label>Comments on Sample <strong>(Max:95)</strong></label>
                                        <textarea name="drawnRemarks" style="width: 100%" placeholder="Comments on Sample" class="form-control" rows="2" maxlength="95"></textarea>
                                        <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                    </div>
                                    <div class="form-group col-xs-6 floating-label-form-group">
                                        <label>Reasons for Priority<strong>(Max:45)</strong></label>
                                        <textarea name="priorityRemarks" style="width: 100%" placeholder="Reasons for Priority of Sample" class="form-control" rows="2" maxlength="45"></textarea>
                                        <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>
                <div class="row form-inline">
                    <br/>
                    <div class="centre-btn">
                        <!--<a href="${pageContext.request.contextPath}/Tse/GetAllPendingSamplesAtTSE?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" class="btn-primary form-control" data-placement="top" title="Summary details for which samples need to be created or postponed" style="margin-right: 20px; text-decoration: none">GO BACK</a>-->
                        <a onclick="window.close()" data-toggle="tooltip" class="btn-primary form-control cursorOnHover" data-placement="top" title="Click here to close the window" style="margin-right: 20px; text-decoration: none"> CLOSE </a>
                        <button id="submitSample" type="submit" class="btn btn-success" data-toggle="tooltip" data-placement="top" title="Summary of Current Sample and Option to Submit or not. ">REVIEW | SUBMIT</button>
                    </div>    
                </div>
            </div>
        </div>
        <script type="text/javascript">

            function getSum(accumulator, a) {
                return accumulator + a;
            }

            function checkQtySamplePage() {

                var testParamNames = "";
                var testSpecMsg = "";
                var sampleQty = [];
                var testNonActive = "";
                switch ($('#labType').val()) {
                    case '1':
                        $.each($(".exist-test-dropdown_tab1 option:selected"), function () {
                            sampleQty.push($('.exist-test-dropdown_tab1 option[value="' + $(this).val() + '"]').data('qty'));
                            if ($('.exist-test-dropdown_tab1 option[value="' + $(this).val() + '"]').data('active') == '0') {
                                testNonActive += $('.exist-test-dropdown_tab1 option[value="' + $(this).val() + '"]').text() + " ";
                                $(".exist-test-dropdown_tab1").select2();
                            }
                        });
                        $.each($(".exist-add-dropdown_tab1 option:selected"), function () {
                            sampleQty.push($('.exist-add-dropdown_tab1 option[value="' + $(this).val() + '"]').data('qty'));
                            if ($('.exist-add-dropdown_tab1 option[value="' + $(this).val() + '"]').data('active') == '0') {
                                testNonActive += $('.exist-add-dropdown_tab1 option[value="' + $(this).val() + '"]').text() + " ";
                                $(".exist-add-dropdown_tab1").select2();
                            }

                        });
                        break;
                    case '2':
                        $.each($(".exist-test-dropdown_tab2 option:selected"), function () {
                            sampleQty.push($('.exist-test-dropdown_tab2 option[value="' + $(this).val() + '"]').data('qty'));
                            if ($('.exist-test-dropdown_tab2 option[value="' + $(this).val() + '"]').data('active') == '0') {
                                testNonActive += $('.exist-test-dropdown_tab2 option[value="' + $(this).val() + '"]').text() + " ";
                                $(".exist-test-dropdown_tab2").select2();
                            }
                        });
                        $.each($(".exist-add-dropdown_tab2 option:selected"), function () {
                            sampleQty.push($('.exist-add-dropdown_tab2 option[value="' + $(this).val() + '"]').data('qty'));
                            if ($('.exist-add-dropdown_tab2 option[value="' + $(this).val() + '"]').data('active') == '0') {
                                testNonActive += $('.exist-add-dropdown_tab2 option[value="' + $(this).val() + '"]').text() + " ";
                                $(".exist-add-dropdown_tab2").select2();
                            }

                        });
                        break;
                    case '3':
                        $.each($(".sample-csl-test-dropdown option:selected"), function () {
                            sampleQty.push($('.sample-csl-test-dropdown option[value="' + $(this).val() + '"]').data('qty'));
                            if ($('.sample-csl-test-dropdown option[value="' + $(this).val() + '"]').data('active') == '0') {
                                testNonActive += $('.sample-csl-test-dropdown option[value="' + $(this).val() + '"]').text() + " ";
                                $(".sample-csl-test-dropdown").select2();
                            }
                        });
                        $.each($(".sample-csl-add-dropdown option:selected"), function () {
                            sampleQty.push($('.sample-csl-add-dropdown option[value="' + $(this).val() + '"]').data('qty'));
                            if ($('.sample-csl-add-dropdown option[value="' + $(this).val() + '"]').data('active') == '0') {
                                testNonActive += $('.sample-csl-add-dropdown option[value="' + $(this).val() + '"]').text() + " ";
                                $(".sample-csl-add-dropdown").select2();
                            }
                        });
                        $.each($(".sample-rnd-test-dropdown option:selected"), function () {
                            sampleQty.push($('.sample-rnd-test-dropdown option[value="' + $(this).val() + '"]').data('qty'));
                            if ($('.sample-rnd-test-dropdown option[value="' + $(this).val() + '"]').data('active') == '0') {
                                testNonActive += $('.sample-rnd-test-dropdown option[value="' + $(this).val() + '"]').text() + " ";
                                $(".sample-rnd-test-dropdown").select2();
                            }
                        });
                        $.each($(".sample-rnd-add-dropdown option:selected"), function () {
                            sampleQty.push($('.sample-rnd-add-dropdown option[value="' + $(this).val() + '"]').data('qty'));
                            if ($('.sample-rnd-add-dropdown option[value="' + $(this).val() + '"]').data('active') == '0') {
                                testNonActive += $('.sample-rnd-add-dropdown option[value="' + $(this).val() + '"]').text() + " ";
                                $(".sample-rnd-add-dropdown").select2();
                            }
                        });
                        break;
                    default:

                        break;
                }
                $('#sampleTestQty').val(sampleQty.reduce(getSum, 0));
                if (testParamNames !== '')
                {
                    testSpecMsg = "No specification mapping wrt product is exist against the test parameter: " + testParamNames;
                }
                if (testNonActive !== '')
                {
                    testNonActive = "These test parameter are not active at the given lab: " + testNonActive;
                    $.alert({
                        title: 'Alert!!!',
                        content: '<span class="text-success">' + testNonActive + '</b></span>',
                        type: 'red',
                        typeAnimated: true
                    });
                    $('#formValid').val('0');
                } else {
                    $('#formValid').val('1');
                }
                if ($('#qtyDrawn').val() < sampleQty.reduce(getSum, 0)) {
                    $.alert({
                        title: 'Alert!!!',
                        content: '<span class="text-success">Minimum Recommended Quantity (in ml) for selected Test(s) is <b>' + sampleQty.reduce(getSum, 0) + ' ml</b></span>',
                        type: 'red',
                        typeAnimated: true
                    });
                }

                if (testParamNames !== '') {
                    $.alert({
                        title: 'Alert!!!',
                        content: '<span class="text-danger">' + testSpecMsg + '</b></span>',
                        type: 'red',
                        typeAnimated: true
                    });
                }
                $('#qtyDrawn').focus();

            }

            $(document).ready(function () {

//                $(document).on('click', '#csl_bth_priorityId', function () {
//                    var prio = $(this).val();
//                    $('#rnd_bth_priorityId').removeAttr('disabled').val(prio).attr('disabled', true);
//                });


                $(document).on('change', '.qtyDrawnByUser', function () {
                    switch ($('#labType').val()) {
                        case '1':
                            var a = $('.csl_qtyDrawn').val();
                            $('#qtyDrawn').val(a);
                            break;
                        case '2':
                            var b = $('.rnd_qtyDrawn').val();
                            $('#qtyDrawn').val(b);
                            break;
                        case '3':
                            var a = $('.csl_qtyDrawn1').val();
                            var b = $('.rnd_qtyDrawn1').val();
                            $('#qtyDrawn').val(parseInt(a) + parseInt(b));
                            break;
                    }
                });


                $(document).on('click', '#labTypeTab > ul > li > a', function () {
                    console.log($(this).data('labtype'));
                    $('#labType').val($(this).data('labtype'));

                });
//                $(document).on('change', '.labCodeId', function (event) {
//                    var x = $(this).find('option:selected').attr('id');
//                    $(".labAuthorityId").val(x);
//                });

                $('#exist-test-dropdown').select2();

                $('.sample-csl-test-dropdown').select2();
                $('.sample-csl-add-dropdown').select2();
                $('.sample-rnd-test-dropdown').select2();
                $('.sample-rnd-add-dropdown').select2();

                $('.sample-csl-test-dropdown').on("select2:unselecting", function (e) {
                    var selectedOption = $(e.params.args.data.element).data('mandatory');
                    var selectedOptionText = $(e.params.args.data.element).text();
                    var ele = $(e.params.args.data.element);
                    var value = ele.attr('value');
                    var qty = ele.data('qty');
                    var mand = ele.data('mandatory');
                    var active = ele.data('active');
                    if (selectedOption === 1) {
                        $('.sample-rnd-test-dropdown').append("<option value='" + value + "' data-qty='" + qty + "' data-mandatory='1' data-active='" + active + "' selected>" + selectedOptionText + "</option>").trigger('change');
                        $('.sample-csl-test-dropdown').find('option[value="' + value + '"]').remove();
                    } else
                        return true;
                });
                $('.sample-rnd-test-dropdown').on("select2:unselecting", function (e) {
                    var selectedOption = $(e.params.args.data.element).data('mandatory');
                    var selectedOptionText = $(e.params.args.data.element).text();
                    var ele = $(e.params.args.data.element);
                    var value = ele.attr('value');
                    var qty = ele.data('qty');
                    var mand = ele.data('mandatory');
                    var active = ele.data('active');
                    if (selectedOption === 1) {
                        $('.sample-csl-test-dropdown').append("<option value=" + value + " data-qty=" + qty + " data-mandatory='1' data-active='" + active + "' selected>" + selectedOptionText + "</option>").trigger('change');
                        $('.sample-rnd-test-dropdown').find('option[value="' + value + '"]').remove();
                    } else
                        return true;
                });

                $('.sample-csl-add-dropdown').on("select2:selecting", function (e) {
                    var selectedOption = $(e.params.args.data.element).data('mandatory');
                    var selectedOptionText = $(e.params.args.data.element).text();
                    var ele = $(e.params.args.data.element);
                    var value = ele.attr('value');
                    var qty = ele.data('qty');
                    var mand = ele.data('mandatory')
                    var item1 = $(this).find("option[value='" + value + "']");
                    var item2 = $('.sample-rnd-add-dropdown').find("option[value='" + value + "']");
                    if (item2.length) {
                        item2.attr('disabled', 'disabled');
                        $('.sample-csl-add-dropdown').select2();
                        $('.sample-rnd-add-dropdown').select2();
                    }
                });

                $('.sample-rnd-add-dropdown').on("select2:selecting", function (e) {
                    var selectedOptionText = $(e.params.args.data.element).text();
                    var ele = $(e.params.args.data.element);
                    var value = ele.attr('value');
                    var qty = ele.data('qty');
                    var item1 = $(this).find("option[value='" + value + "']");
                    var item2 = $('.sample-csl-add-dropdown').find("option[value='" + value + "']");
                    if (item2.length) {
                        item2.attr('disabled', 'disabled');
                        $('.sample-csl-add-dropdown').select2();
                        $('.sample-rnd-add-dropdown').select2();
                    }
                });

                $('.sample-csl-add-dropdown').on("select2:unselecting", function (e) {
                    var selectedOptionText = $(e.params.args.data.element).text();
                    var ele = $(e.params.args.data.element);
                    var value = ele.attr('value');
                    var qty = ele.data('qty');
                    var item1 = $(this).find("option[value='" + value + "']");
                    var item2 = $('.sample-rnd-add-dropdown').find("option[value='" + value + "']");
                    if (item2.length) {
                        item2.removeAttr('disabled').trigger('change');
                        $('.sample-csl-add-dropdown').select2();
                        $('.sample-rnd-add-dropdown').select2();
                    }
                });

                $('.sample-rnd-add-dropdown').on("select2:unselecting", function (e) {
                    var selectedOptionText = $(e.params.args.data.element).text();
                    var ele = $(e.params.args.data.element);
                    var value = ele.attr('value');
                    var qty = ele.data('qty');
                    var item1 = $(this).find("option[value='" + value + "']");
                    var item2 = $('.sample-csl-add-dropdown').find("option[value='" + value + "']");
                    if (item2.length) {
                        item2.removeAttr('disabled');
                        $('.sample-csl-add-dropdown').select2();
                        $('.sample-rnd-add-dropdown').select2();
                    }
                });

            });

//                $.ajax({
//                    url: '/ServoCMP/Tse/getProductCategory?prodId=' + $('#sampleProductId').val(),
//                    type: 'POST',
//                    dataType: 'JSON',
//                    success: function (data) {
//                        if (typeof data == "object") {
//                            console.log(data);
//                            $('#exist-test-dropdown').empty();
//                            $.each(data, function (key, val) {
//                                $('#exist-test-dropdown').append("<option value=" + val.col2 + " data-qty=" + val.col4 + " data-mandatory='1' selected>" + val.col3 + "</option>").trigger('change');
//                            });
//                        } else {
//                            $.alert({
//                                title: 'No Records Found',
//                                content: 'No Category mapping is there against this product.',
//                                type: 'red',
//                                typeAnimated: true
//                            });
//                        }
//                    },
//                    error: function (error) {
//                        console.log(error.responseText);
//                    }
//                });
//
//            });
        </script>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
    </html>

    <!--
        
        <div class="row">
            <div class="form-group col-xs-5 floating-label-form-group">
                <label>Lab Name</label>
                <select id="labCodeId" name="labCode" style="width: 100%; box-shadow: none" class="form-control">
                    <option value="">Select Lab to send Sample *</option>
<c:forEach items="${labmaster}" var="labMaster">
    <option id="${labMaster.labAuthority}" value="${labMaster.labCode}">${labMaster.labName}</option>
</c:forEach>
</select>
<div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
</div>
<div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
<label>Lab In Charge *</label>
<input id="labAuthorityId" name="labAuthority" placeholder="Head of Laboratory" style="width: 100%" class="form-control" type="text" readonly="readonly">
<div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
</div>
<div class="form-group col-xs-3 floating-label-form-group">
<label>Sample Priority *</label>
<select style="width: 100%; box-shadow: none" class="form-control" name="priorityId">
<option value="">Select Priority of Sample</option>
<c:forEach items="${prioritymaster}" var="prioMaster">
    <option value="${prioMaster.priorityId}">${prioMaster.priorityName}</option>
</c:forEach>
</select>
<div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
</div>
</div>
<div class="row">
<div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
<label>Parameters needed to be Tested for *</label>
<select name="testIds" style="width: 100%; border: none" class="form-control" id="exist-test-dropdown" title="Choose Parameters to be Tested for" multiple="multiple" onchange="checkQty();">
<c:forEach items="${preTestList}" var="data" varStatus="count">
    <option data-qty="${data.sampleqty}" selected value="${data.testId}" data-spec="${data.mstTestParam.checkId}" >${data.testName}</option>
</c:forEach>
</select>
<div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
</div>
<div class="form-group col-xs-5 floating-label-form-group">
<label>Specify Additional Parameters needed to be Tested for (if any) *</label>
<select name="addtestIds" style="width: 100%; border: none" class="form-control" id="add-test-dropdown" multiple="multiple" >
<c:forEach items="${addTestList}" var="data" varStatus="count">
    <option data-qty="${data.sampleqty}" value="${data.testId}" data-spec="${data.mstTestParam.checkId}">${data.testName} <span class="hidden">${data.sampleqty}</span></option>
</c:forEach>
</select>
<div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
</div>
</div>
<div class="row">
<div class="form-group col-xs-6 floating-label-form-group">
<label>Comments on Sample</label>
<textarea name="drawnRemarks" style="width: 100%" placeholder="Comments on Sample" class="form-control" rows="2"></textarea>
<div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
</div>
<div class="form-group col-xs-6 floating-label-form-group">
<label>Reasons for Priority</label>
<textarea name="priorityRemarks" style="width: 100%" placeholder="Reasons for Priority of Sample" class="form-control" rows="2"></textarea>
<div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
</div>
</div>
-->