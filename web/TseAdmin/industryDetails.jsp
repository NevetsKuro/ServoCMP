<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Industry Details"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">INDUSTRY DETAILS</h4>
            <div class="row">
                <div class="col-lg-offset-1 col-lg-2">
                    <div class="form-group floating-label-form-group floating-label-form-group-with-value">
                        <label>Industry</label>
                        <select id="getIndustryDetails" name="getIndustryDetails" style="width: 100%; box-shadow: none" class="form-control" required>
                            <option selected value="">Select Industry</option>
                            <option  value="999999">All</option>
                            <option value="">Select Industry</option>
                            <c:forEach items="${Industries}" var="industry">
                                <option  value="${industry.indId}">${industry.indName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <br>
            <br>
            <br>
            <div class="row">
                <div class="col-lg-offset-1 col-lg-10" >
                    <table id="TankTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                        <thead>
                            <tr>
                                <th>Sr. No</th>
                                <th style="width: 50%">Customer Name</th>
                                <th style="width: 20%">Total Tanks Created</th>
                            </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#getIndustryDetails').select2({
                    placeholder: 'Select industry..'
                })
                var tanktable = $('#TankTable').DataTable({
                    pagelength: '10'
                })
                $(document).on('change', '#getIndustryDetails', function () {
                    var indCode = $(this).val();
                    $.ajax({
                        url: '/ServoCMP/TseAdmin/IndustryDetails?ind=' + indCode,
                        type: 'POST',
                        dataType: 'JSON',
                        success: function (data) {
                            console.log(data);
                            tanktable.clear();
                            if (data.length > 0) {
                                $.each(data, function (i, v) {
                                    tanktable.row.add([
                                        i + 1,
                                        v.CustName,
                                        v.Tanks
                                    ]).draw(false);
                                });
                            }else{
                                tanktable.row.add([
                                        "",
                                        "No Customers in the list as on date.",
                                        ""
                                    ]).draw(false);
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
