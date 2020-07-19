
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container" id="content" style="background-color: white;">
    <p style="text-align:center;"><img src="${pageContext.request.contextPath}/resources/images/indianoil-logo.png" alt=""/></p>
    <h4 class="text-center"><b>Universal logins</b></h4>

    <table id="iomDataTb32" class="table table-striped table-bordered" style="width:100%">
        <thead style="font-size: small;" >
            <tr>
                <th class="text-center">Sr No</th>
                <th class="text-center">User Code</th>
                <th class="text-center">User Name</th>
                <th class="text-center">Current Role</th>
                <th class="text-center">Log in As User?</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${userList}" var="data" varStatus="count">
                <c:if test="${data.mstRole.roleId != 5}">
                    <tr style="font-size:small" >
                        <td>${count.count}</td>
                        <td class="text-center">${data.empCode}</td>
                        <td class="text-left">${data.empName}</td>
                        <td class="text-center">${data.mstRole.roleName}</td>
                        <td class="text-center"><button data-id="${data.empCode}" class="button button-small button-pill button-primary button-raised loggedInAs">Log In</button></td>
                    </tr>
                </c:if>
            </c:forEach>
        </tbody>
    </table>
    <br/>
    <br/>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $(document).on('click', '.loggedInAs', function () {
            var empCode = $(this).data('id');
            //                    alert('logged as ' + $(this).parent('tr').find('td:nth-child(3)').html());
            window.open('/ServoCMP/initailizeByAdmin?userCode=' + empCode);
        });

        $('#iomDataTb32').DataTable({
            "pagelength": "8"
        });
    });
</script>