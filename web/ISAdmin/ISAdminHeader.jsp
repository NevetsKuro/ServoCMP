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
    </ul>
    <ul class="dropDownMenu nav navbar-nav navbar-right navbar-header">
        <li class="dropdown text-left">
            <a data-toggle="dropdown" class="dropdown-toggle" style="background-color: transparent; border: none">
                <span class="glyphicon glyphicon-user"> </span> Hi ${sUser.sEMP_NAME} 
            </a>
            <ul>
                <li><a href="#" style="white-space: nowrap"><span class="glyphicon glyphicon-user"></span> Lab Rights</a></li>
                <li><a href="${pageContext.request.contextPath}/TestDetails"><span class="glyphicon glyphicon-tint"></span> Qty of Test</a></li>
                <li><hr style="margin: 0px"></li>
                <li><a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-off"></span> Signout</a></li>
            </ul>
        </li>
    </ul>
</div>