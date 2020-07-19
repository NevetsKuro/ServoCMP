<%-- 
    Document   : viewIOMSummary
    Created on : 4 May, 2018, 12:41:38 PM
    Author     : 00507469
--%>

<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Sample Sent to Lab for Testing. or View IOM."/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>

        <script src="../resources/js/jspdf.debug.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jspdf.plugin.autotable.js" type="text/javascript"></script>
<!--            <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/2.3.5/jspdf.plugin.autotable.js"></script>-->
        <h4 class="text-center text-info" style="text-decoration: underline">IOM Summary</h4>

        <form role="form" action="${pageContext.request.contextPath}/Tse/GetIOMSummaryDetails" id="iom" class="form-inline" method="post">
            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
            <div class="row centre-btn">
                <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                    <label>Select LAB *</label>
                    <select id="idlabName"  name="labName" style="width: 100%; padding: 5px" class="form-control select2-select" onchange="document.getElementById('iom').submit()" required="required">
                        <option value="">Select LAB *</option>
                        <c:forEach items="${labmaster}" var="labMaster">
                            <c:choose>
                                <c:when test="${labMaster.labCode eq labLocCode}">
                                    <option selected="" id="${labMaster.labAuthority}" value="${labMaster.labCode}">${labMaster.labName}</option>
                                    <c:set var="selectedLabAuthority" value="${labMaster.labAuthority}"/>
                                </c:when>
                                <c:otherwise>
                                    <option id="${labMaster.labAuthority}" value="${labMaster.labCode}">${labMaster.labName}</option>
                                </c:otherwise>
                            </c:choose>

                        </c:forEach>
                    </select>
                </div>
            </div>
<!--            <div class="row centre-btn">
                <div class="centre-btn" style="margin-top: 20px">

                    <input style="margin-left: 20px" type="submit" value="Get IOM Summary" class="btn btn-primary" />
                </div>
            </div>-->
        </form>




        <div class="container-fluid">
            <div style="width: 80%; margin: 0 auto">      

                <table id="iomSumTbl" class="table table-bordered"  cellspacing="0" >
                    <thead style="font-size: x-small">

                    <th>IOM REF NO</th>
                    <th>LAB NAME</th>
                    <th>CREATED BY</th>
                    <th>CREATED DATE</th>

                    </thead>
                    <tbody>
                        <c:forEach items="${lstIOMs}" var="data" varStatus="count">
                            <tr style="font-size:small" class="">
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/Tse/GetIOMWiseSummaryData?iomRefNo=${data.iomRefNo}&date=${data.strIomCreatedDate}&nameLabLocCode=${labLocCode}&csrftoken=${sessionScope.csrfToken}" title="Click to view the IOM wise summary data"> ${data.iomRefNo}</a>
                                </td>

                                <td class="text-center">${data.mstLab.labName}</td>
                                <td class="text-center">${data.createdBy}</td>
                                <td class="text-center">${data.strIomCreatedDate}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
