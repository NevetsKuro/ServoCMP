<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Update or Send Sample to Lab for Testing."/>
    </jsp:include>
    <body>
        <div class="container-fluid">
            <div id="Sample">
                <h4 class="text-center text-info text-primary" style="border-bottom: 1px solid">
                    <span style="float: left; font-size: 12px">Last Sample Date: ${cs.stringpresampleDate}</span>
                    <c:if test="${cs.statusId eq '1'}">
                        <span class="text-center">Sample Sent to Lab</span>
                    </c:if>
                    <c:if test="${cs.statusId eq '0'}">
                        <span class="text-center">Update or Send Sample to LAB</span>
                    </c:if>
                    <span style="float: right; font-size: 12px">Sample Due Date: ${cs.stringnxtsampleDate}</span>
                </h4>
                <form class="form-inline" action="<c:if test="${cs.statusId eq '1'}">#</c:if><c:if test="${cs.statusId eq '0'}">${pageContext.request.contextPath}/Tse/UpdateSample</c:if>" method="POST" role="form" id="editsendsampletoLAB">
                    <input type="hidden" value="${cs}" name="cs"/>
                    <input type="hidden" value="${cs.mstProd.proId}" name="productId" id="productId"/>
                    <input type="hidden" value="${sampleType}" name="sampleType" id="sampleType"/>
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
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
                            <label>Capacity in Liters</label>
                            <input value="${cs.mstProd.proCapacity}" name="proCapacity" placeholder="Capacity in lt." disabled="disabled" style="width: 100%" class="form-control" type="text">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-2 floating-label-form-group has-error floating-label-form-group-with-value">
                            <label>Sample Drawn in ml <span class="">*</span></label>
                            <input id="qtyDrawn" value="${cs.qtyDrawn}" name="qtyDrawn" placeholder="Qunatity Drawn in ml *" required style="width: 100%" class="form-control col-xs-2" type="text" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if> onchange="checkQty();">
                                <div class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                            </div>
                            <div style="border-left: 1px solid darkgrey; border-bottom: 1px solid darkgrey" id="editsampleDate" class="col-xs-3">
                                <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer">
                                    <label>Sample Drawn Date *</label>
                                    <input value="${cs.stringsampledrawnDate}" name="qtyDrawnDate" placeholder="Sample Drawn date *" required style="width: 100%" type="text" class="form-control col-xs-2 datepicker" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if>/>
                                    <div class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                                    <span class="input-group-addon" style="border:0; background: none">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                                <label>TOP UP Quantity between ${cs.stringpresampleDate} and ${cs.stringsamplecreatedDate} *</label>
                            <input value="${cs.topupQty}" name="topupQty" placeholder="TOP UP Quantity between ${cs.stringpresampleDate} and ${cs.stringsamplecreatedDate}" style="width: 100%" class="form-control col-xs-2" type="text" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if>>
                                <div class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                            </div>
                            <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                                <label>Running Hrs(Current Oil) <span class="has-error">*</span></label>
                                <input value="${cs.runningHrs}" name="runningHrs" placeholder="Running Hrs(Current Oil) *" required="required" style="width: 100%" class="form-control col-xs-2" type="text" onkeypress="javascript:return isNumber(event)" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if>>
                                <div class="errmsg-content errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                            </div>
                        </div>


                        <div class="row">
                            <div class="form-group col-xs-5 floating-label-form-group floating-label-form-group-with-value">
                                <label>Lab Name</label>
                                <select id="labCodeId" name="labCode" style="width: 100%" class="form-control" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if>>
                                <option selected value="${cs.mstLab.labCode}">${cs.mstLab.labName}</option>
                            </select>
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                            <label>Lab In Charge *</label>
                            <input id="labAuthorityId" name="labAuthority" placeholder="Head of Laboratory" style="width: 100%" class="form-control" type="text" value="${cs.mstLab.labAuthority}" readonly="readonly">
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-3 floating-label-form-group floating-label-form-group-with-value">
                            <label>Sample Priority *</label>
                            <select style="width: 100%" class="form-control" name="samplepriorityId" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if>>
                                    <option value="">Select Priority of Sample</option>
                                <c:forEach items="${prioritymaster}" var="prioMaster">
                                    <c:choose>
                                        <c:when test="${prioMaster.priorityId eq cs.samplepriorityId}">
                                            <option selected="" name="prioritymaster" value="${prioMaster.priorityId}">${prioMaster.priorityName}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option name="prioritymaster" value="${prioMaster.priorityId}">${prioMaster.priorityName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-7 floating-label-form-group floating-label-form-group-with-value">
                            <label>Parameters needed to be Tested for *</label>
                            <select style="width: 100%; border: none" class="form-control exist-test-dropdown" id="exist-test-dropdown" name="testIds" multiple="multiple" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if> onchange="checkQty();">
                                <c:forEach items="${preTestList}" var="data" varStatus="count">
                                    <c:set var="nonMand" value="true" />
                                    <c:forEach items="${mandTestList}" var="mand">
                                        <c:choose>
                                            <c:when test="${mand.col2 eq data.testId}">
                                                <option data-qty="${data.sampleqty}" selected value="${data.testId}" data-spec="${data.mstTestParam.checkId}" data-mandatory="1" selected>${data.testName}</option>
                                                <c:set var="nonMand" value="false" />
                                            </c:when>
                                        </c:choose>
                                    </c:forEach>
                                    <c:if test="${nonMand}">
                                        <option data-qty="${data.sampleqty}" selected value="${data.testId}" data-spec="${data.mstTestParam.checkId}" selected>${data.testName}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                            <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                        </div>
                        <div class="form-group col-xs-5 floating-label-form-group">
                            <label>Specify Additional Parameters needed to be Tested for (if any) *</label>
                            <select style="width: 100%; border: none" class="form-control add-test-dropdown" id="add-test-dropdown" name="addtestIds" multiple="multiple" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if> onchange="checkQty();">
                                <c:forEach items="${addTestList}" var="data" varStatus="count">
                                    <c:set var="disab" value="true" />
                                    <c:forEach items="${ignoreTestList}" var="iTest" varStatus="count1">
                                        <c:choose>
                                            <c:when test="${iTest.testId eq data.testId}">
                                                <option data-qty="${data.sampleqty}" value="${data.testId}" data-spec="${data.mstTestParam.checkId}" disabled>${data.testName}</option>
                                                <c:set var="disab" value="false" />
                                            </c:when>
                                        </c:choose>
                                    </c:forEach>
                                    <c:if test="${disab}">
                                        <option data-qty="${data.sampleqty}" value="${data.testId}" data-spec="${data.mstTestParam.checkId}">${data.testName}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Comments on Sample</label>
                            <textarea name="sampledrawnRemarks" style="width: 100%" placeholder="Comments on Sample" class="form-control" rows="2" maxlength="80" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if>>${cs.sampledrawnRemarks}</textarea>
                            </div>
                            <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                                <label>Comments on Priority</label>
                                <textarea name="samplepriorityRemarks" style="width: 100%" placeholder="Comments on Priority of Sample" class="form-control" rows="2" maxlength="45" <c:if test="${cs.statusId eq '1'}">disabled="disabled"</c:if>>${cs.samplepriorityRemarks}</textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="row form-inline">
                    <br/>
                    <div class="centre-btn">
                    <c:if test="${cs.statusId eq '1'}">
                        <!--<a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?status=1&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" class="btn-primary form-control" data-placement="top" title="Summary details for which samples need to be created or postponed" style="margin-right: 20px; text-decoration: none">GO BACK</a>-->
                        <a onclick="window.close()" data-toggle="tooltip" class="btn-primary form-control cursorOnHover" data-placement="top" title="Summary details for which samples need to be created or postponed" style="margin-right: 20px; text-decoration: none">CLOSE</a>
                    </c:if>
                    <c:if test="${cs.statusId eq '0'}">
                        <a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?status=0&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" class="btn-primary form-control" data-placement="top" title="Summary of samples which have been created and need to send to LAB" style="margin-right: 20px; text-decoration: none">GO BACK</a>
                        <!--<a data-toggle="tooltip" class="btn-primary form-control" data-placement="top" title="Summary details for which samples need to be created or postponed" style="margin-right: 20px; text-decoration: none" onclick="return window.close()">CLOSE</a>-->
                        <btn onclick="review('editsendsampletoLAB')" type="submit" class="btn btn-danger" data-toggle="tooltip" data-placement="top" title="Summary of Current Sample and Option to Submit or not. ">REVIEW | SUBMIT</btn>
                        </c:if>
                </div>    
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>