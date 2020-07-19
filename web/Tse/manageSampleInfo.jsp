<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Sample Info"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body >
        <div class="container-fluid">
<!--            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#newSample">Add New Sample</a></li>
                <li><a data-toggle="tab" href="#manageCustomer">Manage Customer</a></li>
                <li><a data-toggle="tab" href="#manageDepartment">Manage Department</a></li>
                <li><a data-toggle="tab" href="#manageMake" onclick="getMake()">Manage Make</a></li>
                <li><a data-toggle="tab" href="#manageEquipment" onclick="getAllCustomers()">Manage Equipment</a></li>
                <li><a data-toggle="tab" href="#manageTank" onclick="getAllCustomers()">Manage Tank</a></li>
            </ul>-->
            <div class="tab-content">
                <div id="newSample" class="tab-pane fade in active">
                    <%@include file="manageSample.jsp" %>
                </div>
                <div id="manageCustomer" class="tab-pane fade">
                    <%@include file="manageCustomer.jsp" %>
                </div>
                <div id="manageDepartment" class="tab-pane fade">
                    <%@include file="manageDepartment.jsp" %>
                </div>
                <div id="manageMake" class="tab-pane fade">
                    <%@include file="manageMake.jsp" %>
                </div>
                <div id="manageEquipment" class="tab-pane fade">
                    <%@include file="manageEquipment.jsp" %>
                </div>
                <div id="manageTank" class="tab-pane fade">
                    <%@include file="manageTank.jsp" %>
                </div>
            </div>
        </div>
    </body>
    <%@include file="../UItemplate/footer.jsp" %>
</html>