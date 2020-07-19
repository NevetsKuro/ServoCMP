<%-- 
    Document   : viewIOMSummary
    Created on : 4 May, 2018, 12:41:38 PM
    Author     : 00507469
--%>

<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Test Params Status in particular Lab(s)"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>

        <h4 class="text-center text-info" style="text-decoration: underline">Test Parameter's Status in particular Lab(s)</h4>

        <form role="form" action="${pageContext.request.contextPath}/Tse/GetIOMSummaryDetails" id="iom" class="form-inline" method="post">
            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
            <div class="row centre-btn">
                <div class="form-group col-xs-2 floating-label-form-group floating-label-form-group-with-value">
                    <label>Select LAB <span class="text-danger">*</span></label>
                    <select id="testLabName" name="testLabName" style="width: 100%; padding: 5px" class="form-control" required="required">
                        <option value="">Select LAB </option>
                        <c:forEach items="${mLab}" var="labMaster">
                            <option value="${labMaster.labCode}">${labMaster.labName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </form>

        <div class="container-fluid">
            <div style="width: 80%; margin: 0 auto">
                <table id="testLabTable" class="table table-bordered tbCenter-head"  cellspacing="0" >
                    <thead style="font-size: x-small">
                    <th>TEST ID</th>
                    <th>TEST NAME</th>
                    <th>CREATED BY</th>
                    <th>ACTIVE STATUS</th>
                    </thead>
                    <tbody>
                        
                    </tbody>
                </table>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>
