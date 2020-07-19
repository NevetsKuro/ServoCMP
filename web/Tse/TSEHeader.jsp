<%-- 
    Document   : Header Page for TSE
    Created on : 2 Dec, 2016, 12:25:59 PM
    Author     : Manish Jangir, Gourab Sen
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <li><a class="nav_a active" href="/ServoCMP">Home</a></li>
        <li class="dropdown">
            <a data-toggle="dropdown" class="dropdown-toggle nav_a" href="#" style="background-color: transparent; border: none">Servo CMP 
                <!--<span style="color: white" class="caret"></span>-->
            </a>
            <ul>
                <li><a href="#" style="background-color: transparent; border: none; width:120%; ">Process Sample <span style="-webkit-transform: rotate(-90deg);" class="caret"></span></a>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetAllPendingSamplesAtTSE?sampleType=CMP&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary details for which samples need to be created or postponed">Create/Postpone Sample</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=CMP&status=0&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples which have been created and need to send to LAB">Generate IOM</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetIOMSummary?sampleType=CMP&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="To take print out of the IOM">Print IOM</a></li>
                    </ul>
                </li>
                <li><a href="#" style="background-color: transparent; border: none">Reports  <span style="-webkit-transform: rotate(-90deg);" class="caret"></span></a>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=CMP&status=1&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples which have been sent to lab but yet to receive by the lab physically">Sent to Lab</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=CMP&status=2&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples which are already received by lab but yet to enter the test result">Received by Lab</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=CMP&status=3&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples for which test results have already been entered and Sent back to Tse">Sent by Lab</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=CMP&status=4&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples for which test results have been sent to Customer">Send to Customer</a></li>
                    </ul>
                </li>
            </ul>
        </li>
        <li class="dropdown">
            <a data-toggle="dropdown" class="dropdown-toggle nav_a" href="#" style="background-color: transparent; border: none">Servo OTS 
                <!--<span style="color: white" class="caret"></span>-->
            </a>
            <ul>
                <li><a href="#" style="background-color: transparent; border: none; width:120%; ">Process Sample <span style="-webkit-transform: rotate(-90deg);" class="caret"></span></a>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetAllPendingSamplesAtTSE?sampleType=OTS&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary details for which samples need to be created or postponed">Create Sample</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=OTS&status=0&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples which have been created and need to send to LAB">Generate IOM</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetIOMSummary?sampleType=OTS&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="To take print out of the IOM">Print IOM</a></li>
                    </ul>
                </li>
                <li><a href="#" style="background-color: transparent; border: none">Reports  <span style="-webkit-transform: rotate(-90deg);" class="caret"></span></a>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=OTS&status=1&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples which have been sent to lab but yet to receive by the lab physically">Sent to Lab</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=OTS&status=2&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples which are already received by lab but yet to enter the test result">Received by Lab</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=OTS&status=3&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples for which test results have already been entered and Sent back to Tse">Sent by Lab</a></li>
                        <li><a href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?sampleType=OTS&status=4&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples for which test results have been sent to Customer">Send to Customer</a></li>
                    </ul>
                </li>
            </ul>
        </li>
        <li><a href="#" style="background-color: transparent; border: none">Masters<span class="caret"></span></a>
            <ul>
                <li><a class="nav_a active" href="${pageContext.request.contextPath}/Tse/ManageSample?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Add system details,master data for CMP">System Details</a></li>
                <li><a href="${pageContext.request.contextPath}/Tse/MappingCustSAP?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Mapping for customer-SAP code">Manage SAP Codes</a></li>
                <li><a href="${pageContext.request.contextPath}/Tse/CustomerSummary?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary on Individual Customers">Manage Customer</a></li>
                <li><a class="nav_a active" href="${pageContext.request.contextPath}/Tse/TseTestSpecification?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="View of Tse Test Specification">Test Specification</a></li>
                <li><a class="nav_a active" href="${pageContext.request.contextPath}/Tse/LabTestSpecsServlet?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="View of Lab wise Test Specification">Individual Lab-Tests Specs Availability</a></li>
            </ul>
        </li>
    </ul>
    <ul class="dropDownMenu nav navbar-nav navbar-right navbar-header">
        <li class="message">
            <a href="#" class="badge-message" data-badge="${notiCount}">&nbsp;
                <span class="glyphicon size glyphicon-envelope"></span>
            </a>
            <div class="all-message" style="overflow: auto">
                <span style="font-weight: bolder">Pending Sample(s)</span>
                <c:choose>
                    <c:when test="${notiCount eq 0}">
                        <span>No Sample(s) pending.</span>
                    </c:when>
                    <c:otherwise>
                        <hr style="border: 1px solid darkgray; margin-bottom: 10px; margin-top: 0"/>
                        <c:forEach items="${notidetails}" var="data" varStatus="count">
                            <ul><li>${data.indName} (${data.indCount})</li></ul>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </li>
        <li class="dropdown text-left">
            <a data-toggle="dropdown" class="dropdown-toggle" style="background-color: transparent; border: none; cursor: pointer">
                <span class="glyphicon glyphicon-user"></span> HI, ${sUser.sEMP_NAME}
                <!--<span style="color: white" class="caret"></span>-->
            </a>
            <ul style="width: 100%">
                <li><a href="#" style="white-space: nowrap"><span class="glyphicon glyphicon-user"></span> TSE Rights</a></li>
                <li><a href="${pageContext.request.contextPath}/TestDetails?csrftoken=${sessionScope.csrfToken}"><span class="glyphicon glyphicon-tint"></span> Qty. of tests</a></li>
                <li><hr style="margin: 0px"></li>
                <li><a href="${pageContext.request.contextPath}/resources/Files/ServoCMP-Release-Notes-Ver-TSE.pdf" target="_blank"><span class="glyphicon glyphicon-file"></span> User Manual</a></li>
                <li><hr style="margin: 0px"></li>
                <li><a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-off"></span> Sign out</a></li>
            </ul>
        </li>
    </ul>
</div>