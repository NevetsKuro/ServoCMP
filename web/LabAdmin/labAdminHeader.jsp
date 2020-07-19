<%-- 
    Document   : labAdminHeader
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
            <a data-toggle="dropdown" class="dropdown-toggle nav_a" href="/ServoCMP">
                LAB ACCESS
            </a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/LabAdmin/LabAccess?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of LAB access like addition and removal of LAB and its access are controlled here">LAB Allocation / Creation</a></li>
                <li><a href="${pageContext.request.contextPath}/LabAdmin/AllActiveUser?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Summary of User Access">Lab User(s)</a></li>
            </ul> 
        </li>
        <li class="dropdown">
            <a data-toggle="dropdown" class="dropdown-toggle nav_a" href="/ServoCMP"> 
                LAB MASTERS 
            </a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/LabAdmin/TestSpecification?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Test Specification">Test Specs</a></li>
                <li><a href="${pageContext.request.contextPath}/LabAdmin/AllActiveTest?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Test">Test</a></li>
                <li><a href="${pageContext.request.contextPath}/LabAdmin/tseOtherValues.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Master Records of Test Specification">Other Value Specification</a></li>
                <li><a href="${pageContext.request.contextPath}/LabAdmin/newsController.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="New GUI Page">News Controller</a></li>
            </ul> 

        </li>
        <li class="dropdown">
            <a data-toggle="dropdown" class="dropdown-toggle nav_a" href="/ServoCMP"> 
                LAB REPORTS 
            </a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/LabAdmin/reportLabEquipmentStatus?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Equipment Status by Lab">Lab Equipment Status</a></li>
                <li><a href="${pageContext.request.contextPath}/LabAdmin/reportLabRespnseTime.jsp?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="LAB Response Time report">CSL efficiency</a></li>
            </ul>
        </li>
    </ul>
    <ul class="dropDownMenu nav navbar-nav navbar-right navbar-header" >
        <li class="dropdown text-left" >
            <a data-toggle="dropdown" class="dropdown-toggle" style="background-color: transparent; border: none; cursor: pointer">
                <span class="glyphicon glyphicon-user"> </span> Hi ${sUser.sEMP_NAME} 
            </a>
            <ul style="float:right; width: 100%" >
                <li><a href="#" style="white-space: nowrap"><span class="glyphicon glyphicon-user"></span> Lab Admin Rights</a> </li>
                <li><hr style="margin: 0px"></li>
                <li><a href="${pageContext.request.contextPath}/resources/Files/ServoCMP-Release-Notes-Ver-LabAdmin.pdf" target="_blank"><span class="glyphicon glyphicon-file"></span> User Manual</a></li>
                <li><hr style="margin: 0px"></li>
                <li><a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-off"></span> Signout</a></li>
            </ul>
        </li>
    </ul>
</div>