<%-- 
    Document   : reportCustomerServices
    Created on : 2 Jun, 2018, 3:47:24 PM
    Author     : 00507469
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../UItemplate/header.jsp">
    <jsp:param name="title" value="Customer Services Report"/>
</jsp:include>

<body>
    <div class="container-fluid">
        <!--action="${pageContext.request.contextPath}/TseAdmin/CustomerServicesReport" method="POST" role="form">-->
        <form id="reportSample" name="reportSample"  class="form-inline"> 
            <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
            <div class="row">
                <div class="col-xs-3"></div>
                <div style="border-bottom: 1px solid darkgrey" id="fromDateDiv" class="col-xs-2">
                    <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer" data-date-format="dd-mm-yyyy">
                        <label>Enter From Date <span class="text-danger">*</span></label>
                        <input name="fromDate" id="fromDate" placeholder="Enter From Date *"  style="width: 100%" type="text" class="form-control col-xs-2 datepicker" value="${fromDate}" required/>
                        <span class="input-group-addon" style="border:0; background: none">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                    </div>
                </div>
                <div class="col-xs-1"></div>
                <div style="border-bottom: 1px solid darkgrey" id="toDate" class="col-xs-2">
                    <div style="border: none;" id="dateContainer" class="input-group date col-xs-12 floating-label-form-group floating-label-form-group-with-value" data-provide="datepicker" data-date-container="#dateContainer" data-date-format="dd-mm-yyyy">
                        <label>Enter To Date <span class="text-danger">*</span></label>
                        <input name="toDate" id="tosDate" placeholder="Enter To Date *"  style="width: 100%" type="text" class="form-control col-xs-2 datepicker" value="${toDate}" required/>
                        <span class="input-group-addon" style="border:0; background: none">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                    </div>

                </div>
                <div class="form-group form-inline centre-btn col-xs-1">
                    <div class="centre-btn">
                        <input name="btnGenerate" id="btnGenerate" data-toggle="tooltip" data-placement="top" title="Click to Search" type="button" class="btn btn-primary input-group" style="margin-top: 20px" value="Generate">
                    </div>
                </div>
            </div>
        </form>                    
        <h4 class="text-center text-info">
            <div style="padding: 3px;font-weight: bold;font-family: monospace;text-decoration: underline">Customer Services</div>
            <div  id="reportTitle" style="font-family: monospace;"> </div>
        </h4>
            <br/>


            <table id="createdSample1" class="table table-bordered tbCenter tbCenter-head custServTable" cellspacing="0" width="100%">
                <thead style="font-size: 16px">
                <th style="text-align: center;">Customer name</th>
                <th>Sold to party code</th>
                <th>SO name</th>
                <th>Initiator name</th>
                <th>Total equipments</th>
                <th>Total samples</th>
                <th>Total reports submitted</th>
                <th>Report time in days</th>
                </thead>
                <tbody>
                <c:forEach items="${listRptDetails}" var="eachRow">
                    <tr>
                        <td class="NevsLeft">${eachRow.col1}</td>
                        <td>${eachRow.col2}</td>
                        <td>${eachRow.col3}</td>
                        <td>${eachRow.col4}</td>
                        <td>${eachRow.col5}</td>
                        <td>${eachRow.col6}</td>
                        <td>${eachRow.col7}</td>
                        <td>${eachRow.col8}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </div>
    <div>
        <div id="modal-details-table" class="modal fade" style="top: 10%;" tabindex="-1" role="dialog">

            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                        <h4 class="modal-title">Records</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered table-condensed" border="1px" style="">
                            <thead id="modalTableHead">

                            </thead>
                            <tbody id="modalTableBody">
                            </tbody> 
                        </table>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>
    </div>
    <script>
        $(document).ready(function () {
            
            var header1 = "<tr class='success'><th>Sample ID</th><th>Sample Drawn Date</th><th>Sample Remark</th><th>Sample Priority</th></tr>";
            var header2 = "<tr class='success'><th>Tank ID</th><th>Industry Name</th><th>Customer Name</th><th>Tank Desc</th></tr>";
            
            $(document).on('click', '#createdSample1 > tbody > tr > td:not(:first-child):not(:nth-child(2)):not(:nth-child(3)):not(:nth-child(4)):not(:nth-child(8))', function () {
                var td = $(this);
                var tr = td.parent();
                var th = $('#tankTable > thead > th');
                var th1 = $(th).eq(td.index());
                var rowCode = tr.find('td:nth-child(1)').text();
                var colNum = td.index();
                $.ajax({
                    url: '/ServoCMP/TseAdmin/GetTseReportsDetails?rowCode=' + rowCode + '&colNum=' + colNum + '&fromDate=' + $("#fromDate").val() + '&toDate=' + $("#tosDate").val() + '&report=CustomerServicesReport',
                    type: 'GET',
                    dataType: 'JSON',
                    success: function (data) {
                        $('#modalTableHead').empty();
                        if(colNum=='4'){
                            $('#modalTableHead').append(header2);
                        }else{
                            $('#modalTableHead').append(header1);
                        }
                        var trow = "";
                        $.each(data, function (row, val) {
                            Object.keys(val).forEach(key => val[key] === "" ? delete val[key] : '');//for filtering out blank values
                            trow += "<tr>";
                            $.each(val, function (cellno, val) {
                                trow += "<td>" + val + "</td>";
                            });
                            trow += "</tr>";
                        });
                        console.log(trow);
                        $('#modalTableBody').empty();
                        (trow!=""?$('#modalTableBody').append(trow):$('#modalTableBody').append("<tr><td colspan='2'><center>There are no records found against this entry</center></td></tr>"));
                        $('#modal-details-table').modal();
                    },
                    error: function (error) {
                        console.log(error.responseText);
                    }
                });
            });
            
            var createdSample = $('#createdSample1').DataTable({
                "order": [],
                "columnDefs": [
                    {"className": "dt-head-center", "targets": "_all"}
                ],
                "dom": 'Bfrtip',
                "buttons": [
                    {
                        extend: 'excel',
                        messageTop: 'Excel Data from ' + $("#fromDate").val() + ' TO ' + $("#tosDate").val() + ' ',
//                exportOptions: {
//                    columns: [ 0, 1, 2, 5 ]
//                },
//                customize: function ( xlsx ) {
//                    
//                }
                    }
                ]
            });
            function convertToDate(d) { //format from mm-dd-yyyy to dd-mm-yyyy
                if (d) {
                    if (d.toString().indexOf('-') >= 0) {
                        var date = d.split('-')[1] + "-" + d.split('-')[0] + "-" + d.split('-')[2];
                    } else {
                        var date = d;
                    }
                } else {
                    return false;
                }
                return date;
            }
            $(document).on('click', '#btnGenerate', function (e) {
                e.preventDefault();
                var fromdate = new Date(convertToDate($("#fromDate").val()));
                var todate = new Date(convertToDate($("#tosDate").val()));
                console.log(todate);
                console.log(fromdate);

                if (todate > fromdate) {
                    $.ajax({
                        url: '${pageContext.request.contextPath}/TseAdmin/CustomerServicesReport?fromDate=' + $("#fromDate").val() + '&toDate=' + $("#tosDate").val() + '&logIt=no',
                        type: 'POST',
                        dataType: 'JSON',
                        success: function (data) {
                            console.log(data);
                            createdSample.clear().draw(false);
                            if (data.length > 0) {
                                $.each(data, function (i, val) {
                                    var row = createdSample.row.add([
                                        val.col1,
                                        val.col2?val.col2:"Not Assigned",
                                        val.col3,
                                        val.col4,
                                        val.col5,
                                        val.col6,
                                        val.col7,
                                        val.col8
                                    ]).draw(false).node();
                                    $(row).data('custName',val.col1);
                                });
                                $('#reportTitle').text('( ' + convertToDate($("#fromDate").val()) + ' to ' + convertToDate($("#tosDate").val()) + ' )');
                            }
                        },
                        error: function (error) {
                            console.log(error.responseText);
                        }
                    });
                } else {
                    $.alert({
                        title: 'Date Invalidate !!!',
                        content: 'From Date is greather than To Date.',
                        type: 'red',
                        typeAnimated: true
                    });
                }

            });
        });
    </script>
</body>
<%@include file="../UItemplate/footer.jsp" %>
