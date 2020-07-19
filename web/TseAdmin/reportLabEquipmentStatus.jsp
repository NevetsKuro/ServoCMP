<%-- 
    Document   : reportCustomerServices
    Created on : 2 Jun, 2018, 3:47:24 PM
    Author     : 00507469
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../UItemplate/header.jsp">
    <jsp:param name="title" value="Lab Equipment Health Report"/>
</jsp:include>

<body>
    <div class="container-fluid">

        <div class="container">
            <div class="row" style="margin-bottom: 25px;">
                <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                    <label>Select Lab:</label>
                    <select id="LabCode" name="custCode" style="width: 100%; box-shadow: none" class="form-control getEquipment" required>
                        <option selected value="">Select..</option>
                        <c:forEach var="lab" items="${labList}">
                            <option value="${lab.equipmentId}">${lab.equipmentName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="row">
                <table id="equipmentList" class="table table-bordered tbCenter" cellspacing="0" width="100%">
                    <thead>
                    <th style="width: 32%">Equipment Name</th>
                    <th style="width: 15%">Working status
                        <button class="button button-primary button-circle" style="transform:scale(0.6) " data-toggle="popover" data-trigger="focus" title="Status Signs" data-content="The Green Circle represents Working Condition whereas the Red Circle represents Not Working Condition"><i class="fa fa-question-circle"></i></button>
                    </th>
                    <th>Remark</th>

                    </thead>
                    <tbody id="equipBody">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <script>
        $(document).ready(function () {

            $('[data-toggle="popover"]').popover();
            var equipListDatatable = $('#equipmentList').DataTable({
                "ordering": false,
                "pageLength": 10
            });

            $(document).on('change', '.getEquipment', function () {
                var labId = $(this).val();
                if (labId) {
                    $.ajax({
                        url: 'GetEquipmentByLabServlet?lab-id=' + labId,
                        type: 'POST',
                        dataType: 'JSON',
                        success: function (data) {
                            console.log('success');
                            console.log(data);
                            equipListDatatable.clear();
                            $.each(data, function (i, val) {
                                console.log('fetched data');
                                equipListDatatable.row.add([
                                    val.labEquipName,
                                    val.operationalStatus == 'YES' ? '<span class="dot-green"></span>' : '<span class="dot-red"></span>',
                                    val.remarks ? val.remarks : '-'
                                ])
                            });
                            equipListDatatable.draw(false);
                        },
                        error: function (error) {
                            console.log(error);
                        }
                    });
                }
            });
        });
    </script>
</body>
<%@include file="../UItemplate/footer.jsp" %>
