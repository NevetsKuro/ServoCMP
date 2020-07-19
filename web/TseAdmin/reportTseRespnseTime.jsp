<%-- 
    Document   : reportTseRespnseTime
    Created on : 2 Jun, 2018, 2:07:24 PM
    Author     : 00507469
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../UItemplate/header.jsp">
    <jsp:param name="title" value="Tse Response Time Report"/>
</jsp:include>
<body>
    <div class="container">
        <form id="reportSample" name="reportSample"  class="form-inline" action="${pageContext.request.contextPath}/TseAdmin/TseResponseTimeReport" method="POST" role="form">
            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
            <div class="row">
                <div class="col-xs-3"></div>
                <div style="border-bottom: 1px solid darkgrey" id="fromDateDiv" class="col-xs-2">
                    <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer" data-date-format="dd-mm-yyyy">
                        <label>Enter From Date <span class="text-danger">*</span></label>
                        <input name="fromDate" id="fromDate" placeholder="Enter From Date *"  style="width: 100%" type="text" class="form-control col-xs-2 datepicker" value="${fromDate}" required/>
                        <span class="input-group-addon" style="border:0; background: none">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                    </div>
                </div>
                <div class="col-xs-1"></div>
                <div style="border-bottom: 1px solid darkgrey" id="toDateDiv" class="col-xs-2">
                    <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer" data-date-format="dd-mm-yyyy">
                        <label>Enter To Date <span class="text-danger">*</span></label>
                        <input name="toDate" id="tosDate" placeholder="Enter To Date *"  style="width: 100%" type="text" class="form-control col-xs-2 datepicker" value="${toDate}" required/>
                        <span class="input-group-addon" style="border:0; background: none">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                    </div>
                </div>
                <div class="form-group form-inline centre-btn col-xs-1">
                    <div class="centre-btn">
                        <input name="btnGenerate" id="btnGenerate" data-toggle="tooltip" data-placement="top" title="Click to Search" type="button" class="btn btn-primary input-group" style="margin-top: 20px" value="Generate">
                    </div>
                </div>
            </div>
        </form>                    
        <h4 class="text-center text-info">
            <div style="padding: 3px;font-weight: bold;font-family: monospace;text-decoration: underline">TSE wise Response Time</div>
            <c:if test="${listRptDetails.size() > -1}"> <div style="font-family: monospace;"> ( ${fromDate} to ${toDate} ) </div> </c:if>
        </h4>
            
            <br/>


            <table id="createdSample1" class="table table-bordered tbCenter tbCenter-head" cellspacing="0" width="100%">
                <thead style="font-size: 15px;">
                <th style="width: 30%;">Initiator name</th>
                <th style="width: 20%;">Report time in days</th>
                <th style="width: 20%;">Sample delivery time  in days </th>
                <th style="width: 30%;">Average number of days between sample receipt date and data upload date </th>

                </thead>

                <tbody>
                <c:forEach items="${listRptDetails}" var="eachRow">
                    <tr>
                        <td class="NevsLeft">${eachRow.col1}</td>
                        <td class="text-center">${eachRow.col2}</td>
                        <td class="text-center">${eachRow.col3}</td>
                        <td class="text-center">${eachRow.col4}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </div>

</body>
<%@include file="../UItemplate/footer.jsp" %>