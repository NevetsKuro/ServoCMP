<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Control Manager"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container">
            <h4 class="centre-btn" style="text-decoration: underline">Control Manager</h4>
            <div>
                <br/>
                <table id="UserTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <td>Test ID</td>
                            <td>Test Name</td>
                            <td>Active Status</td>
                            <td>Change</td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${testList}" var="data" varStatus="items">
                            <tr>
                                <td>${data.testId}</td>
                                <td>${data.testName}</td>
                                <td>${(data.active=="1"?"<span class='dot-green'></span>":"<span class='dot-red'></span>")}</td>
                                <td><button class="button-action button-primary-flat button-rounded toggle-active-status">Active/Inactive</button></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <script>
            $(document).ready(function () {
                $(document).on('click', '.toggle-active-status', function () {
                    var thisEle = $(this);
                    var test_id = thisEle.parents('tr').find('td:nth-child(1)').text();
                    var test_name = thisEle.parents('tr').find('td:nth-child(2)').text();
                    $.ajax({
                        url: '/ServoCMP/Lab/LabTestStatusMaster?testId='+test_id,
                        type: 'POST',
                        dataType: 'JSON',
                        success: function (data) {
                            console.log(data);
                            if(data=="Successful"){
                                var ele = thisEle.parents("tr").find('td:nth-child(3)');
                                (ele.find('span').hasClass("dot-green")?ele.html("<span class='dot-red'></span>"):ele.html("<span class='dot-green'></span>"));
                                $.alert({
                                    title: 'Successfully Updated',
                                    content: 'Updated Test Status of '+ test_name,
                                    type: 'green',
                                    typeAnimated: true
                                });
                            }else{
                                $.alert({
                                    title: 'Unsuccessful',
                                    content: 'Failure to update Test '+test_name,
                                    type: 'red',
                                    typeAnimated: true
                                });
                            }
                        },
                        error: function (error) {
                            console.log(error.responseText);
                        }
                    });

                });
            });
        </script>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>