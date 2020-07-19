<%-- 
    Document   : tseAdminHeader
    Created on : 31 Jul, 2017, 2:54:14 PM
    Author     : 00507469
--%>
<style>
.dropdown-toggle:after {
    display: inline-block;
    width: 0;
    height: 0;
    margin-left: .255em;
    vertical-align: .255em;
    content: "";
    border-top: .3em solid;
    border-right: .3em solid transparent;
    border-bottom: 0;
    border-left: .3em solid transparent;
}
</style>
<div class="navbar-collapse collapse navbar-default">
    <ul class="dropDownMenu nav navbar-nav">
        <li><a class="nav_a active" href="/ServoCMP">HOME</a></li>
        <li class="dropdown">
            <a data-toggle="dropdown" class="dropdown-toggle nav_a" href="/ServoCMP"> ACCESS
                <!--<span style="color: white" class="caret"></span>-->
            </a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/TseAdmin/TseAccess?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of TSE access like add on and removal of TSE access are controlled from this">TSE</a></li>
                <li><a href="${pageContext.request.contextPath}/TseAdmin/AllActiveUser?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Summary of User Access">User</a></li>
                <li><a href="${pageContext.request.contextPath}/TseAdmin/TseLabMappingServlet?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Mapping for Tse's to Lab">Tse-Lab Mapping</a></li>
            </ul>
        </li>
        <li class="dropdown">
            <a data-toggle="dropdown" class="dropdown-toggle nav_a" href="/ServoCMP"> MASTERS 
                <!--<span style="color: white" class="caret"></span>-->
            </a>
            <ul>
                <li><a href="#" style="background-color: transparent; border: none; width:120%;"> Master Tab <span style="-webkit-transform: rotate(-90deg);position: absolute;right: 16px;top: 16px;" class="caret"></span></a>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/AllActiveIndustry?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Industry ">Industry</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/AllActiveApplication?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Application">Application</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/AllActiveProduct?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Products">Product</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/AllActiveEquipment?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Equipment">Equipment</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/AllActiveMake?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Makes">Make</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/AllActiveTest?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Test">Test</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/TestSpecification?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Test Specification">Test Specs</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/tseOtherValues.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Other Test Specification">Other Value Specification</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/productCategoryList.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Mapping of Product Category-to-Test Parameter ">Category-to-Test Mapping</a></li>
                    </ul>
                </li>
                <li><a href="#" style="background-color: transparent; border: none; width:120%; "> Control Tab <span style="-webkit-transform: rotate(-90deg);position: absolute;right: 16px;top: 16px;" class="caret"></span></a>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/controlTable.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Controller master">Control Master</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/redirectController?url=tseSampleHandle&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="update Systems for due samples">Update System</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/newsController.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="News master">News Master</a></li>
                        <li><a href="${pageContext.request.contextPath}/TseAdmin/redirectController?url=industryDetails&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Check total customers and tanks industry-wise">Industry Details</a></li>
                    </ul>
                </li>
            </ul> 
        </li>
        <li class="dropdown">
            <a data-toggle="dropdown" class="dropdown-toggle nav_a" href="/ServoCMP">Reports 
            <!--<span style="color: white" class="caret"></span>-->
            </a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/TseAdmin/reportTsePerformance.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="TSE Performance report">TSE Efficiency</a></li>
                <!--<li><a href="${pageContext.request.contextPath}/TseAdmin/reportTseRespnseTime.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="TSE Response Time report">TSE Response Time</a></li>-->
                <li><a href="${pageContext.request.contextPath}/TseAdmin/reportLabRespnseTime.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="LAB Response Time report">CSL efficiency</a></li>
                <li><a href="${pageContext.request.contextPath}/TseAdmin/reportCustomerServices.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Customer Services report">Customer Service</a></li>
                <li><a href="${pageContext.request.contextPath}/TseAdmin/reportLabEquipmentStatus?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Customer Services report">CSL Equipment Status</a></li>
                <li><a href="${pageContext.request.contextPath}/TseAdmin/redirectController?url=tankDetails&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Check all tank Details">Equipment Master</a></li>
            </ul> 
        </li>
    </ul>
    <ul class="dropDownMenu nav navbar-nav navbar-right navbar-header" >
        <li class="dropdown text-left" >
            <a data-toggle="dropdown" class="dropdown-toggle" style="background-color: transparent; border: none; cursor: pointer">
                <span class="glyphicon glyphicon-user"> </span> Hi ${sUser.sEMP_NAME} 
                <!--<span style="color: white" class="caret"></span>-->
            </a>
            <ul style="float:right; width: 100%" >
                <li><a href="#" style="white-space: nowrap"><span class="glyphicon glyphicon-user"></span> Tse Admin Rights </a></li>
                <li><a href="${pageContext.request.contextPath}/TseAdmin/TestSequence?csrftoken=${sessionScope.csrfToken}"><span class="glyphicon glyphicon-sort-by-order"></span> Test Sequence</a></li>
                <li><hr style="margin: 0px"></li>
                <li><a href="${pageContext.request.contextPath}/resources/Files/ServoCMP-Release-Notes-TSE-Admin.pdf" target="_blank"><span class="glyphicon glyphicon-file"></span> User Manual</a></li>
                <li><hr style="margin: 0px"></li>
                <li><a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-off"></span> Signout</a></li>
            </ul>
        </li>
    </ul>
</div>