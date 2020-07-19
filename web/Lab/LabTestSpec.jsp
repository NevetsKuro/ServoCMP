<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Test Specification"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">TEST SPECIFICATION VIEW</h4>
            
            <div class="row" style="margin-top: 5px;">
                <div class="col-xs-12" style="font-family: monospace;font-size: medium;margin-left: 10px">Filter:</div>
                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                    <label style="padding-left: 7px;">Select Product <span class="text-danger">*</span></label>
                    <select id="prodFilter" name="productId" style="width: 100%; box-shadow: none" class="form-control">
                        <option value="">Select Product</option>
                        <c:forEach items="${mPro}" var="pro">
                            <option value="${pro.proId}">(${pro.proId}) ${pro.proName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-xs-1" style="padding-top: 10px">
                    <button id="filterSearch" class="btn btn-default btn-success" data-toggle="tooltip" data-placement="bottom" title="Search Records by Product Name" data-noedit="1">Search By</button>
                </div>
            </div>
            <br/><br/>
            <table id="TestSpecTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                <thead>
                    <tr>
                        <th>Product Name</th>
                        <th>Test Name</th>
                        <th>Value Check</th>
                        <th>MIN</th>
                        <th>MAX</th>
                        <th>EQUAL</th>
                        <th>DEVIATION</th>
                        <th>MAX (DELIMITER)</th>
                        <th>OTHER</th>
                        <th style="width: 10%">Updated By</th>
                        <th style="width: 15%">Updated Date</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
<%--
Body of table
<c:forEach items="${testSpecs}" var="testSpec">
                        <tr>
                            <td class="NevsLeft">${testSpec.proName}</td>
                            <td class="NevsLeft">
                                ${testSpec.testName}
                                <a href="#" onclick="AddTestSpec('Update Test', '${testSpec.proName}', '${testSpec.testName}', ${testSpec.testId}, ${testSpec.proId}, this, ${testSpec.mstTestParam.checkId})" data-toggle="tooltip" data-placement="right" title="Edit ${testSpec.testName} Test" style=" text-decoration: none; float: right">
                                    <span class="glyphicon glyphicon-pencil"></span>
                                </a>
                            </td>
                            <td>${testSpec.spec}</td>
                            <td>${testSpec.mstTestParam.minValue}</td>
                            <td>
                                <c:if test="${not fn:contains(testSpec.mstTestParam.maxValue, '/')}">
                                    ${testSpec.mstTestParam.maxValue}
                                </c:if>
                            </td>
                            <td>${testSpec.mstTestParam.typValue}</td>
                            <td>${testSpec.mstTestParam.devValue}</td>
                            <td>
                                <c:if test="${fn:contains(testSpec.mstTestParam.maxValue, '/')}">
                                    ${testSpec.mstTestParam.maxValue}
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${testSpec.mstTestParam.otherVal eq 'Yes'}">
                                    <span data-toggle="tooltip" data-placement="bottom" title="${otherVal}">${testSpec.mstTestParam.otherVal}</span>
                                </c:if>
                                <c:if test="${testSpec.mstTestParam.otherVal ne 'Yes'}">
                                    ${testSpec.mstTestParam.otherVal}
                                </c:if>
                            <td><a href="#" onclick="openHelloIOCian(${testSpec.updatedBy})">${testSpec.updatedBy}</a></td>
                            <td>${testSpec.updatedDate}</td>
                        </tr>
                    </c:forEach>
--%>