<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="headtag.jsp" %>
<%--<%@ page errorPage="/errorPages/error.jsp" %>--%>
<header>
    <div class="navbar navbar-default navbar-fixed-top" style="box-shadow: 1px -2px 13px 2px black">
        <div class="container-fluid">
            <div class="navbar-header navbar-default">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a href="/ServoCMP" style=""><img src="${pageContext.request.contextPath}/resources/images/indianoil-logo.png" title="IndianOil.co.in" alt="IndianOil logo"/></a>
            </div>
            <input id="globalMsg" name="appMsg" class="hidden" value="${messageDetails.modalMessage}"/>
            <div class="modal-review fade" id="reviewModal" style="overflow-y: auto" data-backdrop='static'>
                <div class="modal-dialog modal-lg-review" style="overflow-y: ">
                    <div class="modal-content" style="overflow-y: auto">
                        <div class="modal-header" style="overflow-y: auto">
                            <button id="closeBtn" onclick="closeReview()" type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="text-center modal-title">Sample Review</h4>
                        </div>
                        <div class="modal-body modal-body-review" style="overflow-y: auto;">
                            <div id="review-body" style="overflow-y: auto"></div>
                        </div>
                        <div class="modal-footer">
                            <button id="editBtn" class="btn btn-primary" onclick="closeReview()" data-dismiss="modal">EDIT</button>
                            <button class="btn btn-success" id="submitBtn" onclick="formSubmit()">SUBMIT</button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="responseDialog" class="modal fade" role="dialog" data-backdrop='static'>
                <div class="modal-dialog" style="overflow-y: initial !important">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close removeBackdrop" data-dismiss="modal">&times;</button>
                            <h4 id="modal-title" class="modal-title">${messageDetails.modalTitle}</h4>
                        </div>
                        <div class="modal-body" style="height:300px; overflow-y: auto">
                            <p id="modalMessage" class="${messageDetails.msgClass}">${messageDetails.modalMessage}</p>
                            <p id="fileMsg" class="${messageDetails.filemsgClass}">${messageDetails.fileMsg}</p>
                            <p id="mailMsg" class="${messageDetails.mailMsgClass}">${messageDetails.mailMsg}</p>
                            <p>${messageDetails.modalresourceMsg}</p>
                        </div>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-default removeBackdrop" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="PostponedDetailsModal" class="modal fade" role="dialog" data-backdrop='static'>
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">POSTPONED DETAILS OF THE SAMPLE</h4>
                        </div>
                        <div class="modal-body">
                            <table id="PostponedDetails" class="table table-bordered table-striped">
                                <thead style='font-size: x-small'> 
                                <th>Prev Sample Date</th>
                                <th>Next Sample Date</th> 
                                <th>Postponed Date</th> 
                                <th>Remarks</th> 
                                </thead>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil</span>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${sUser.role_id eq 1}">
                <%@include file="../Tse/TSEHeader.jsp" %>
            </c:if>
            <c:if test="${sUser.role_id eq 2}">
                <%@include file="../Lab/labHeader.jsp" %>
            </c:if>
            <c:if test="${sUser.role_id eq 3}">
                <%@include file="../TseAdmin/tseAdminHeader.jsp" %>
            </c:if>
            <c:if test="${sUser.role_id eq 4}">
                <%@include file="../LabAdmin/labAdminHeader.jsp" %>
            </c:if>
            <c:if test="${sUser.role_id eq 5}">
                <%@include file="../ISAdmin/ISAdminHeader.jsp" %>
            </c:if>
            <c:if test="${sUser.role_id eq 6}">

            </c:if>
            <c:if test="${sUser.role_id eq null || sUser.role_id eq ''}">
                <ul class="dropDownMenu nav navbar-nav navbar-right navbar-header">
                    <li class="dropdown text-left">
                        <a data-toggle="dropdown" class="dropdown-toggle" style="background-color: transparent; border: none; cursor: pointer">
                            <span class="glyphicon glyphicon-user"></span>HI, <c:if test="${sUser.sEMP_NAME eq null || sUser.sEMP_NAME eq ''}">Anonymous</c:if><c:if test="${not sUser.sEMP_NAME eq null || not sUser.sEMP_NAME eq ''}"> sUser.sEMP_NAME</c:if>
                                <span style="color: white" class="caret"></span>
                            </a>
                            <ul>
                                <li><a href="resources/Files/ServoCMP-Release-Notes.pdf" target="_blank"><span class="glyphicon glyphicon-file">  User Manual</span></a></li>
                                <li><a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-off"> Signout</span></a></li>
                                <li><a href=""><code>Version 1.0.0</code></a></li>
                        </ul>
                    </li>
                </ul>
            </c:if>

        </div>
    </div>
</header>