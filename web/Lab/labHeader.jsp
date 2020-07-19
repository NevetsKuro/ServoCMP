    <%-- 
        Document   : labHeader
        Created on : 2 Dec, 2016, 12:25:59 PM
        Author     : Manish Jangir
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
                    Servo CMP
                </a>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/Lab/GetIOMSummary?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="To take print out of the IOM">Print IOM</a></li>
                    <li><a href="${pageContext.request.contextPath}/Lab/SampleSendByTSE?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip"  data-placement="right" title="Summary of samples sent to lab and yet to acknowledge upon received physically">Acknowledge</a></li>
                    <li><a href="${pageContext.request.contextPath}/Lab/GetReceivedSample?status=2&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Summary of samples which are already received and yet to enter the test results">Process</a></li>
                    <li><a href="${pageContext.request.contextPath}/Lab/GetReceivedSample?status=3&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Summary of samples for which test results have been entered">Forwarded to TSE</a></li>
                    <li><a href="${pageContext.request.contextPath}/Lab/GetReceivedSample?status=4&csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="Summary of samples for which test results have been forwared to Customer">Forwarded to Customer</a></li>
                </ul> 
            </li>
            <li class="dropdown">
                <a data-toggle="dropdown" class="dropdown-toggle nav_a" href="#">
                    Lab Details
                </a>
                <ul>
                    <li><a class="nav_a active" href="${pageContext.request.contextPath}/Lab/AllActiveLabEquipmentsByEmpcode?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="All Active Equipment under your Lab">Lab Equipment</a></li>
                    <li><a class="nav_a active" href="${pageContext.request.contextPath}/Lab/LabTestSpecification?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="View of Lab Test Specification">Lab Test Specification</a></li>
                    <li><a class="nav_a active" href="${pageContext.request.contextPath}/Lab/LabTestStatusMaster?csrftoken=${sessionScope.csrfToken}" data-toggle="tooltip" data-placement="right" title="View of Lab's Test Status">Lab Test Status Master</a></li>
                </ul>
            </li>
        </ul>
        <ul class="dropDownMenu nav navbar-nav navbar-right navbar-header">
            <li class="message">
                <a href="#" class="badge-message" data-badge="${notiCount}">&nbsp;
                    <span class="glyphicon size glyphicon-envelope"></span>
                </a>
                <div class="all-message" style="overflow: auto">
                    <span style="font-weight: bolder">Pending Sample(s) for Testing</span>
                    <c:choose>
                        <c:when test="${notiCount eq 0}">
                            <span>No Sample(s) pending for Testing.</span>
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
                <a data-toggle="dropdown" class="dropdown-toggle" style="background-color: transparent; border: none">
                    <span class="glyphicon glyphicon-user"> </span> Hi ${sUser.sEMP_NAME} 
                </a>
                <ul>
                    <li><a href="#" style="white-space: nowrap"><span class="glyphicon glyphicon-user"></span> Lab Rights</a></li>
                    <li><a href="${pageContext.request.contextPath}/TestDetails"><span class="glyphicon glyphicon-tint"></span> Qty of tests</a></li>
                    <li><hr style="margin: 0px"></li>
                    <li><a href="${pageContext.request.contextPath}/resources/Files/ServoCMP-Release-Notes-Ver-Lab.pdf" target="_blank"><span class="glyphicon glyphicon-file"></span> User Manual</a></li>
                    <li><hr style="margin: 0px"></li>
                    <li><a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-off"></span> Signout</a></li>
                </ul>
            </li>
        </ul>
    </div>