<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="View IOM. 1"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <script src="../resources/js/jspdf.debug.js" type="text/javascript"></script>
            <script src="${pageContext.request.contextPath}/resources/js/jspdf.plugin.autotable.js" type="text/javascript"></script>
<!--            <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/2.3.5/jspdf.plugin.autotable.js"></script>-->
            <h4 class="text-center text-info" style="text-decoration: underline">View IOM</h4>
            <div id="IOMPrint">
                <p style="text-align:center;"><img src="../resources/images/indianoil-logo.png" alt=""/></p>
                <h4 class="text-center"><b>IOM - FOR USED OIL SAMPLES</b></h4>
                <h5 class="text-center text-info">
                    <span style="float: left;">Ref No:</span>
                    <span style="float: right;">IOM Date:<b>${Date}</b></span>
                </h5>
                <table class="table table-bordered table-striped" cellspacing="0" width="100%">
                    <thead style="font-size: x-small;" >
                        <tr>
                            <th>Sl No</th>
                            <th>SAMPLE ID</th>
                            <th>CUSTOMER NAME</th>
                            <th>EQUIPMENT</th>
                            <th>PRODUCT</th>
                            <th>DATE OF SAMPLING</th>
                            <th>SUMP CAPACITY</th>
                            <th>RUNNING HRS</th>
                            <th>QTY DRAWN(ml.)</th>
                            <th>TEST PARAMETER NAME's</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${IOMSampleList}" var="data" varStatus="count">
                            <tr style="font-size:small" >
                                <th>${count.count}</th>
                                <td class="text-center">
                                    ${data.sampleId}
                                </td>
                                <td class="text-center">${data.mstDept.mstCustomer.customerName}</td>
                                <td class="text-center">${data.mstEquip.equipmentName}</td>
                                <td class="text-center">${data.mstProd.proName}</td>
                                <td class="text-center">${data.stringsampledrawnDate}</td>
                                <td class="text-center">${data.mstProd.proCapacity}</td>
                                <td class="text-center">${data.runningHrs}</td>
                                <td class="text-center">${data.qtyDrawn}</td>
                                <td class="text-center">${data.testParamNames}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="text-center">
                <input type="button" class="btn-sm btn-primary" value="Print IOM" onclick="printIOM('IOMPrint')">
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>