$(window).resize(function () {
    footerAlign();
});
var otherDropdownLoaded = true;
var parentDiv;
var currentForm;
var today = new Date();
$(document).ready(function () {
    $.ajaxSetup({
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-TOKEN', $('[name="csrf-token"]').attr('content'));
        }
    });
    function validateEmail1(email) {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }

    $(document).on('change', '.isEmail', function () {
        var a = $(this).val();
        if (!validateEmail1(a)) {
            $.alert({
                title: 'Invalid Input',
                content: 'Email should be in "example@email.com" format',
                type: 'red',
                typeAnimated: true
            });
            $(this).val("");
        }
    });
    $(document).on('change', '.isNumber', function () {
        var a = $(this).val();
        if (!isNaN(a)) {
            console.log("valid");
        } else {
            $.alert({
                title: 'Invalid Input',
                content: $(this).data('field') + ' accepts only numeric values',
                type: 'red',
                typeAnimated: true
            });
            $(this).val("");
        }
    });
    var CustDT = $('#custTable').DataTable({
        "order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Customer Info Table'
            },
            'colvis'
        ]
    });
    $('#getCustSummary').on('click', function () {
        var custID = $('#custId').val();
        if (custID) {
            var $btn = $(this).button('loading');
            $.ajax({
                url: 'redirectController?url=getCustomerDetails&custid=' + custID,
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
                    CustDT.clear().draw(false);
                    if (data.departsArr.length > 0) {
                        $.each(data.departsArr, function (i, cust) {
                            CustDT.row.add([
                                cust.departmentName,
                                cust.hodName,
                                cust.hodEmail,
                                cust.hodContact ? cust.hodContact : "N/A",
                                cust.updatedBy ? cust.updatedBy : "N/A",
                                '<span  data-toggle="tooltip"  data-placement="right" title="Click to edit ' + cust.departmentName + ' department details" class="text-success indDept" data-id="' + cust.departmentId + '"><i class="glyphicon glyphicon-pencil"></i></span>'
                            ]).draw(false);
                        });
                        $('#TotsSamples').val(data.total[0]);
                        if (data.total[1]) {
                            $('#TotsSapLink').val(data.total[1]);
                        } else {
                            $('#TotsSapLink').val("None Assigned");
                        }
                    } else {
                        $.alert({
                            title: 'No Data Found',
                            content: 'The selected customer does not belong to any department.',
                            type: 'Red',
                            typeAnimated: true
                        });
                    }
                    $btn.button('reset');
                },
                error: function (error) {
                    console.log(error.responseText);
                    $btn.button('reset')
                }
            });
        } else {
            $.alert({
                title: 'No option selected',
                content: 'Please select a Customer.',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $(document).on('click', '#openCustModal', function () {
        if ($('#custId').val()) {
            $('#newCustId').val($('#custId').val());
            $('#customerId').val($('#custId').val());
            $('#custName').attr('placeholder', 'Current Customer Name: ' + $('#custId option:selected').text());
            $('#UpdateCustomerTable').modal();
        } else {
            $.alert({
                title: 'No option selected',
                content: 'Please select a Customer.',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $(document).on('click', '#editCustomerName', function () {

        if ($('#newCustId').val()) {
            $.ajax({
                url: '/ServoCMP/Tse/UpdateCustomer?custId=' + $('#newCustId').val() + '&newCustName=' + $('#custName').val(),
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
                    $('#UpdateCustomerTable').modal('hide');
                    showRawResponseDialog(data);
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        } else {
            $.alert({
                title: 'No option selected',
                content: 'Please select a Customer.',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $(document).on('click', '.indDept', function () {
        $('#UpdateHODInfo').modal('show');
        $('#departmentId').val($(this).data('id'));
        $('#custId').val($('#custId').val());
        $('#Departmentname').val($(this).parents('tr').find('td:nth-child(1)').text());
        $('#HODname').val($(this).parents('tr').find('td:nth-child(2)').text());
        $('#HODemail').val($(this).parents('tr').find('td:nth-child(3)').text());
        $('#HODcontact').val($(this).parents('tr').find('td:nth-child(4)').text());
    });
    $(document).on('click', '#editHOD', function () {
        var department_id = $('#departmentId').val();
        var department_name = $('#Departmentname').val();
        var cust_id = $('#custId').val();
        var HODName = $('#HODname').val();
        var HODEmail = $('#HODemail').val();
        var HODContact = $('#HODcontact').val();
        $.ajax({
            url: 'redirectController?url=updateDepartment&deptid=' + department_id + '&deptname=' + department_name + '&HODname=' + HODName + '&HODemail=' + HODEmail + '&HODcontact=' + HODContact + '&custid=' + cust_id,
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
                showRawResponseDialog(data);
                $('#UpdateHODInfo').modal('hide');
                $('#getCustSummary').trigger('click');
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    });
    $(document).on('click', '#searchEmployee', function () {
        var empcode = $('#empCode').val();
        if (empcode) {

            if (!isNaN(empcode)) {
                $.ajax({
                    async: false,
                    url: 'redirectController?url=getEmployeeAvailable&empcode=' + empcode,
                    type: 'GET',
                    dataType: 'JSON',
                    success: function (data) {
//                        console.log(data);
                        if (data != 'NF') {
                            $('#empCode > option[value="' + empcode + '"]').remove();
                            $('#empCode').append(`<option data-empName="${data.empname}" data-empEmail="${data.empemail}" data-ctrlEmpCode="${data.empctrlemp}" value="${data.empcode}">${data.empcode} (${data.empname})</option>`);
                            $('#empCode').val(`${data.empcode}`).trigger('change')
                        } else {
                            $.alert({
                                title: 'Not Found',
                                content: 'No Such Employee with empcode ' + empcode,
                                type: 'red',
                                typeAnimated: true
                            });
                        }

                    },
                    error: function (error) {
                        console.log(error.responseText);
                    }
                });
            } else if (isNaN(empcode)) {
                $.alert({
                    title: 'Invalid Employee Code',
                    content: 'Only Numbers are allowed!',
                    type: 'red',
                    typeAnimated: true
                });
            }
        } else {
            $.alert({
                title: 'Invalid Employee Code',
                content: 'Blank Values not allowed!',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $("#addRowEquip").click('click', function () {
        var $tableBody = $('#equipTable').find("tbody"),
                $trLast = $tableBody.find("tr:last");
        $trLast.find('select').select2('destroy').end();
        var $trNew = $trLast.clone();
        $trNew.find('.remarksClass').val('N/A');
        $trLast.after($trNew);
        $('.equipMakeClass').select2();
    });
    $(document).on('change', '#equipTable .equipMakeClass', function () {
        if ($(this).val() == "22") {
            $(this).parents('tr').find('.remarksClass').removeClass('hide');
        } else if ($(this).val() != "22") {
            $(this).parents('tr').find('.remarksClass').addClass('hide');
            $(this).parents('tr').find('.remarksClass').val('');
        }
    });
    $('#tankTableDiv').hide();
    $('#equipmentTableDiv').hide();
    $('#makeTableDiv').hide();
    $('#departmentTableDiv').hide();
    $('#custTableDiv').hide();
    $('#sampleTableDiv').hide();
    footerAlign();
    $('.chkclass').click(function () {
        var sum = 0;
        $('.chkclass:checked').each(function () {
            sum += parseFloat($(this).closest('tr').find('.testQty').text());
        });
        $('#totalQuantity').html(sum + " (ml)");
    });
    $('#testReportFile').on('change', function (e) {
        var file = e.target.files[0];
        if (!file.type.match('application/pdf')) {
            document.getElementById("testReportFile").value = "";
            $.alert({
                title: '',
                content: 'Please Upload file in PDF format only.',
                type: 'red',
                typeAnimated: true
            });
            return;
        }
    });
    $('.exist-test-dropdown').select2();
    $('.exist-test-dropdown').on("select2:unselecting", function (e) {
        var selectedOption = $(e.params.args.data.element).data('mandatory');
        var selectedOptionText = $(e.params.args.data.element).text();
        if (selectedOption === 1) {
            e.preventDefault();
            $.alert({
                title: '',
                content: 'Test "' + selectedOptionText + '" is mandatory for all Sample(s)',
                type: 'red',
                typeAnimated: true
            });
        } else
            return true;
    });
    $('#Acknowledge').confirm({
        title: 'Confirm',
        content: 'Are you sure you want to Acknowledge this Sample?',
        type: 'red',
        typeAnimated: true,
        buttons: {
            Yes: {
                btnClass: 'btn-success',
                action: function () {
                    $('#receivesampleForm').submit();
                }
            },
            No: {
                btnClass: 'btn-red',
                action: function () {
                    $.alert({
                        title: '',
                        content: 'No changes have been made to this Sample.',
                        type: 'red',
                        typeAnimated: true
                    });
                }
            }
        }
    });


    $('#rejectSampleAtTest').confirm({
        title: 'Confirm',
//        content: '' +
//                '<form action="" class="formName">' +
//                '<div class="form-group">' +
//                '<label>Select Option:(If rejecting)</label>' +
//                '<select id="rejectReason2" name="rejectReason2" class="rejectReason2" required>' +
//                '<option value="select">Select an option</option>' +
//                '<option value="Non-standard container">Non-standard container</option>' +
//                '<option value="Damaged container">Damaged container</option>' +
//                '<option value="Insufficient quantity of product">Insufficient quantity of product</option>' +
//                '</select>' +
//                '</div>' +
//                '</form>',
        content: function () {
            var self = this;
            return $.ajax({
                url: 'redirectController?url=fetchRejectReason',
                dataType: 'JSON',
                method: 'POST'
            }).done(function (response) {
                var options = "";
                $.each(response, function (key, value) {
                    options += '<option value="' + key + '">' + value + '</option>';
                });
                self.setContentAppend(
                        '<form action="" class="formName">' +
                        '<div class="form-group">' +
                        '<label>Select Option:(If rejecting)</label>' +
                        '<select id="rejectReason2" name="rejectReason2" class="rejectReason2" required>'
                        + options +
                        '</select>' +
                        '</div>' +
                        '</form>');
                self.setTitle('Choose...');
            }).fail(function () {
                self.setContent('Something went wrong.');
            });
        },
        type: 'red',
        typeAnimated: true,
        buttons: {
            Reject: {
                btnClass: 'btn-red',
                action: function () {
                    var reason = this.$content.find('.rejectReason2').val();
                    var smpid = this.$target.data('smpid');
                    var labcode = this.$target.data('labcode');
                    if (reason == "select") {
                        $.alert("Kindly Give a reason!");
                    } else {
                        $.confirm({
                            title: 'Reject Sample',
                            content: 'Are you sure you want to reject the sample?',
                            icon: 'fa fa-warning',
                            animation: 'scale',
                            closeAnimation: 'zoom',
                            backgroundDismiss: true,
                            buttons: {
                                Yes: {
                                    text: 'Yes, sure!',
                                    btnClass: 'btn-orange',
                                    action: function () {
                                        if (reason != 'Select an option' || reason != '') {
                                            $.ajax({
                                                url: '/ServoCMP/Lab/RejectSampleServlet?sampleId=' + smpid + '&labCode=' + labcode + '&reason=' + reason,
                                                type: 'POST',
                                                dataType: 'JSON',
                                                success: function (data) {
                                                    $.alert({
                                                        title: 'Sample Status',
                                                        content: data,
                                                        type: 'red',
                                                        typeAnimated: true
                                                    });
                                                    if (data.indexOf("successfully") > -1) {
                                                        location.href = "/ServoCMP/Lab/GetReceivedSample?status=2&csrftoken=" + $('[name="csrf-token"]').attr('content');
                                                    }
                                                },
                                                error: function (error) {
                                                    console.log(error.responseText);
                                                }
                                            });
                                        } else {
                                            $.alert({
                                                title: 'Invalid Input',
                                                content: 'Please select a valid reason',
                                                type: 'red',
                                                typeAnimated: true
                                            });
                                        }
                                    }
                                },
                                No: function () {
                                    btnClass: 'btn-default'
                                }
                            }
                        });
                    }
                }
            },
            Cancel: {
                btnClass: 'btn-default'
            }
        }
    });
//    $('.getReceiveSample').confirm({



    $('.getReceiveSample').confirm({
        title: 'Confirm',
//        content: 'Do you want to receive the sample or reject it?',
        content: function () {
            var self = this;
            return $.ajax({
                url: 'redirectController?url=fetchRejectReason',
                dataType: 'JSON',
                method: 'POST'
            }).done(function (response) {
                var options = "";
                $.each(response, function (key, value) {
                    options += '<option value="' + key + '">' + value + '</option>';
                });
                self.setContentAppend(
                        '<form action="" class="formName">' +
                        '<div class="form-group">' +
                        '<label>Select Option:(If rejecting)</label>' +
                        '<select id="rejectReason" name="rejectReason" class="rejectReason" required>' +
                        '<option value="select">Select an option</option>'
                        + options +
                        '</select>' +
                        '</div>' +
                        '</form>');
                self.setTitle('Choose...');
            }).fail(function () {
                self.setContent('Something went wrong.');
            });
        },
        type: 'red',
        typeAnimated: true,
        buttons: {
            Confirm: {
                btnClass: 'btn-success',
                action: function () {
                    var href = this.$target.attr('href');
                    $.confirm({
                        title: 'Confirm Sample',
                        content: 'Have you received the sample physically?',
                        icon: 'fa fa-success',
                        animation: 'scale',
                        closeAnimation: 'zoom',
                        backgroundDismiss: true,
                        buttons: {
                            Yes: {
                                text: 'Yes, sure!',
                                btnClass: 'btn-success',
                                action: function () {
                                    location.href = href;
                                }
                            },
                            No: function () {
                                btnClass: 'btn-default'
                            }
                        }
                    });

                }
            },
            Reject: {
                btnClass: 'btn-red',
                action: function () {
                    var reason = this.$content.find('.rejectReason').val();
                    var smpid = this.$target.data('smpid');
                    var labcode = this.$target.data('labcode');
                    if (reason == "select") {
                        $.alert("Kindly Give a reason!");
                    } else {
                        $.confirm({
                            title: 'Reject Sample',
                            content: 'Are you sure you want to reject the sample?',
                            icon: 'fa fa-warning',
                            animation: 'scale',
                            closeAnimation: 'zoom',
                            backgroundDismiss: true,
                            buttons: {
                                Yes: {
                                    text: 'Yes, sure!',
                                    btnClass: 'btn-orange',
                                    action: function () {
                                        if (reason != 'Select an option' || reason != '') {
                                            $.ajax({
                                                url: '/ServoCMP/Lab/RejectSampleServlet?sampleId=' + smpid + '&labCode=' + labcode + '&reason=' + reason,
                                                type: 'POST',
                                                dataType: 'JSON',
                                                success: function (data) {
                                                    $.alert({
                                                        title: 'Sample Status',
                                                        content: data,
                                                        type: 'red',
                                                        typeAnimated: true
                                                    });
                                                    if (data.indexOf("successfully") > -1) {
                                                        location.reload();
                                                    }
                                                },
                                                error: function (error) {
                                                    console.log(error.responseText);
                                                }
                                            });
                                        } else {
                                            $.alert({
                                                title: 'Invalid Input',
                                                content: 'Please select a valid reason',
                                                type: 'red',
                                                typeAnimated: true
                                            });
                                        }
                                    }
                                },
                                No: function () {
                                    btnClass: 'btn-default'
                                }
                            }
                        });
                    }
                }
            },
            Cancel: {
                btnClass: 'btn-default'
            }
        }
    });

    $('#labDropdown').click();
    $('#ManageSamples').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Manage Sample List'
            },
            'colvis'
        ]});
    $('#testInfo').DataTable({
        "order": [],
        "paging": false,
        "searching": false,
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Test Information'
            },
            'colvis'
        ]
    });
    $('#PendingSamples').DataTable({"order": [],
        "columnDefs": [
            {"className": "dt-center", "targets": "_all"}
        ],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Pending Samples by TSE'
            },
            'colvis'
        ]
    });
    $('#receiveSample').DataTable({"order": [],
        "columnDefs": [
            {"className": "dt-center", "targets": "_all"}
        ],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Received Samples'
            },
            'colvis'
        ]
    });
    $('#testSample').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Samples Created by TSE'
            },
            'colvis'
        ]
    });
    $('#IndustryTable').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'List of Industries'
            },
            'colvis'
        ]});
    $('#ApplicationTable').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: ' List of Application'
            },
            'colvis'
        ]});
//    $('#ProductTable').DataTable({"order": []});
    $('#MakeTable').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'List of Make'
            },
            'colvis'
        ]});
    $('#TestTable').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Test Specification'
            },
            'colvis'
        ]});
    $('#labEquipTable').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Lab Equipment List'
            },
            'colvis'
        ]});
    $('#iomSumTbl').DataTable({"order": []});
    $('#LabUserTable').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Lab User List'
            },
            'colvis'
        ]});
//    $('#tseAccess').DataTable({
//        buttons: [
//            'selectAll',
//            'selectNone'
//        ],
//        language: {
//            buttons: {
//                selectAll: "Select all items",
//                selectNone: "Select none"
//            }
//        },
//        columnDefs: [{
//                orderable: false,
//                className: 'select-checkbox',
//                targets: 0
//            }],
//        select: {
//            style: 'os',
//            selector: 'td:first-child'
//        },
//        order: [[1, 'asc']],
//        "dom": 'Bfrtip',
//        "buttons": [
//            {
//                extend: 'excel',
//                filename: 'TSE Access List'
//            },
//            'colvis'
//        ]
//    });
    $('#tseAccess').DataTable({
        buttons: [
            'selectAll',
            'selectNone'
        ],
        language: {
            buttons: {
                selectAll: "Select all items",
                selectNone: "Select none"
            }
        },
        columnDefs: [{
                orderable: false,
                className: 'select-checkbox',
                targets: 0
            }],
        select: {
            style: 'os',
            selector: 'td:first-child'
        },
        order: [[1, 'asc']],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'TSE Access List'
            },
            'colvis'
        ]
    });
//    editor = new $.fn.dataTable.Editor({
//        ajax: '',
//        table: '#TestSequence',
//        fields: [{
//                label: 'Order:',
//                name: 'readingOrder',
//                fieldInfo: 'This field can only be edited via click and drag row reordering.'
//            }, {
//                label: 'Title:',
//                name: 'title'
//            }, {
//                label: 'Author:',
//                name: 'author'
//            }, {
//                label: 'Duration (seconds):',
//                name: 'duration'
//            }
//        ]
//    });
//    $('#TestSequence').DataTable({
//        "order": [],
//        "paging": false,
//        rowReorder: {
//            dataSrc: 'testOrder'
//        },
//        select: true
//    });
//    $('#TestSequence').on('row-reorder', function (e, diff, edit) {
//        var result = 'Reorder started on row: ' + edit.triggerRow.data()[1] + '<br>';
//        for (var i = 0, ien = diff.length; i < ien; i++) {
//            var rowData = $('#TestSequence').row(diff[i].node).data();
//            result += rowData[1] + ' updated to be in position ' +
//                    diff[i].newData + ' (was ' + diff[i].oldData + ')<br>';
//        }
//        console.log('Event result:<br>' + result);
//    });

    var table = $('#TestSequence').DataTable({
        'createdRow': function (row, data, dataIndex) {
            $(row).attr('id', 'row-' + dataIndex);
            $('#TestSequence').attr('width', '100%');
        },
        'paging': false
    });
    table.rowReordering();
    $(":file").filestyle({buttonBefore: true});
    $('.populatereceivemodal').click(populateData);
    $('[data-toggle="tooltip"]').tooltip({
        container: 'body'
    });
//    $('#ManageSamples').DataTable({
//        initComplete: function () {
//            this.api().columns().every(function () {
//                var column = this;
//                var select = $('<select><option value=""></option></select>')
//                        .appendTo($(column.footer()).empty())
//                        .on('change', function () {
//                            var val = $.fn.dataTable.util.escapeRegex(
//                                    $(this).val()
//                                    );
//
//                            column
//                                    .search(val ? '^' + val + '$' : '', true, false)
//                                    .draw();
//                        });
//
//                column.data().unique().sort().each(function (d, j) {
//                    select.append('<option value="' + d + '">' + d + '</option>');
//                });
//            });
//        },
//        columnDefs: [{
//                orderable: false,
//                className: '',
//                targets: 0
//            }],
//        select: {
//            style: 'os',
//            selector: 'td:first-child'
//        },
//        order: [[1, 'asc']]
//    });
    $('#IomTable').DataTable({
        initComplete: function () {
            this.api().columns().every(function () {
                var column = this;
                var select = $('<select><option value=""></option></select>')
                        .appendTo($(column.footer()).empty())
                        .on('change', function () {
                            var val = $.fn.dataTable.util.escapeRegex(
                                    $(this).val()
                                    );
                            column
                                    .search(val ? '^' + val + '$' : '', true, false)
                                    .draw();
                        });
                column.data().unique().sort().each(function (d, j) {
                    select.append('<option value="' + d + '">' + d + '</option>');
                });
            });
        },
        columnDefs: [{
                orderable: false,
                className: 'select-checkbox',
                targets: 0
            }],
        select: {
            style: 'os',
            selector: 'td:first-child'
        },
        order: [[1, 'asc']]
    });
    $('#viewIOM').click(function () {
        var data = $('#IomTable').DataTable().rows({selected: true}).data();
        var sampleIds = [];
        for (var i = 0; i < data.length; i++) {
            sampleIds.push(data[i][1]);
        }
        if (sampleIds.length > 0) {
            $("input[id=jsonSampleIds]").val(JSON.stringify(sampleIds));
            document.getElementById('iomForm').submit();
        } else {
            $('#modal-title').text('Record Not Selected!!!');
            $('#modalMessage').text('Please Select atleast one row from the Table.');
            $('#responseDialog').modal('show');
        }
    });
    $('#viewSample').click(function () {
        var data = $('#IomTable').DataTable().rows({selected: true}).data();
        if (data.length > 1) {
            $.alert({
                title: '',
                content: 'Please Select only one Sample to View',
                type: 'red',
                typeAnimated: true
            });
        }
        if (data.length === 0) {
            $.alert({
                title: '',
                content: 'Please Select a Sample to View',
                type: 'red',
                typeAnimated: true
            });
        }
        if (data.length === 1) {
            window.location.href = '/ServoCMP/Tse/GetUpdateSample?smplid=' + data[0][1] + '&sampleType=' + $('#sampleType').val() + '&labCode=' + $('#idlabName').val() + '&status=0&csrftoken=' + $('[name="csrf-token"]').attr('content');
        }
    });
    $('#selectionProcess').click(function () {
        $('#modal-title').text('Selection Process...');
        $('#modalMessage').text('To select multiple records press "Ctrl" key and click on the checkboxes. To select multiple Records in series first select one record and then press "Shift" and then select the last record, doing so will select all the records between the first and the last record clicked.');
        $('#responseDialog').modal('show');
    });
    $('#samplePreDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        endDate: new Date(),
        clearBtn: true,
        orientation: "top",
        autoclose: true,
        todayHighlight: true
    }).on('changeDate', function (e) {
        addMonth();
    });
    $('#manageNxtDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        startDate: new Date(),
        clearBtn: true,
        orientation: "top",
        autoclose: true,
        todayHighlight: true
    }).on('changeDate', function (e) {
        if ($('#sampleFreq').val() === '') {
            $.alert({
                title: '',
                content: 'Please Enter Sample Frequency in months first.',
                type: 'red',
                typeAnimated: true
            });
            return false;
        }
    });
    var todayDay = new Date();
    $('#samplePreDate .input-group.date').datepicker('setDate', new Date(todayDay.getFullYear(), todayDay.getMonth(), todayDay.getDate()));
    $('#NewNextSamDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        startDate: new Date(),
        clearBtn: true,
        autoclose: true,
        orientation: "top",
        todayHighlight: true,
        container: '#showTankDetails'
    });
    $('#createSampleDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        startDate: new Date(),
        clearBtn: true,
        orientation: "auto",
        autoclose: true,
        todayHighlight: true
    }).on('changeDate', function (e) {
        $('#sendsampletoLAB').formValidation('revalidateField', 'qtyDrawnDate');
    });
    $('#postponetillDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        startDate: new Date(),
        clearBtn: true,
        orientation: "auto",
        autoclose: true,
        todayHighlight: true
    }).on('changeDate', function (e) {
        $('#postponedSample').formValidation('revalidateField', 'postponetillDate');
    });
    $('#editsampleDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        startDate: new Date(),
        clearBtn: true,
        orientation: "auto",
        autoclose: true,
        todayHighlight: true
    }).on('changeDate', function (e) {
// Revalidate the date field
        $('#editsendsampletoLAB').formValidation('revalidateField', 'qtyDrawnDate');
    });
    $('#sampleLastDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        clearBtn: true,
        orientation: "top",
        autoclose: true,
        todayHighlight: true
    }).on('changeDate', function (e) {
// Revalidate the date field

    });
    $('#fromDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        clearBtn: true,
        orientation: "bottom",
        autoclose: true,
        todayHighlight: true
    }).on('changeDate', function (e) {
// Revalidate the date field

    });
    $('#toDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        clearBtn: true,
        orientation: "bottom",
        autoclose: true,
        todayHighlight: true
    }).on('changeDate', function (e) {
// Revalidate the date field
    });
    var today = new Date();
    var minusMonth = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
    var tmC = parseInt(today.getMonth()) + 1;
    var mmC = parseInt(minusMonth.getMonth()) + 1;
    var tm = (1 == tmC.toString().length ? "0" + tmC : tmC);
    var mm = (1 == mmC.toString().length ? "0" + mmC : mmC);
    $("#fromDate").val(minusMonth.getDate() + "-" + mm + "-" + minusMonth.getFullYear());
    $("#tosDate").val(today.getDate() + "-" + tm + "-" + today.getFullYear());
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

//    $("#btnGenerate").click(function ()
//    {
//    var fromdate = new Date($("#fromDate").val()); //Year, Month, Date
//    var todate = new Date($("#toDate").val()); //Year, Month, Date
//
//        var fromdate = new Date(convertToDate($("#fromDate").val()));
//        var todate = new Date(convertToDate($("#tosDate").val()));
//        console.log(todate);
//        console.log(fromdate);
//
//        if (todate > fromdate) {
//
//            $('#reportSample').submit();
//        } else {
//            $.alert({
//                title: 'Date Invalidate !!!',
//                content: 'From Date is greather than To Date.',
//                type: 'red',
//                typeAnimated: true
//            });
//        }
//    });

    $('#recsampleDate .input-group.date').datepicker({
        format: "dd-mm-yyyy",
        startDate: new Date(),
        clearBtn: true,
        orientation: "top auto",
        autoclose: true,
        todayHighlight: true
    }).on('changeDate', function (e) {
        $('#receivesampleForm').formValidation('revalidateField', 'labreceiveDate');
    });
    $('.add-test-dropdown').select2({
        placeholder: "Specify additional parameters to be tested for (if any)",
        allowClear: true
    });
    $('#mstApplication').select2({
        placeholder: "Select Application",
        allowClear: true
    });
    $('#tankCustomer').select2({
        placeholder: "Select Customer",
        allowClear: true
    });
    $('#fAreaCode').select2({
        placeholder: "Select Role",
        allowClear: true
    });
//    $('#empCode').select2({
//        placeholder: "Select Employee Code",
//        allowClear: true
//    });

    $('#empCode').select2({
        multiple: false,
        placeholder: "Enter Employee Number",
        tags: true,
        allowClear: true,
        createTag: function (tag) {
            found = false;
            $("#empCode option").each(function () {
                if ($.trim(tag.term).toUpperCase() === $.trim($(this).val())) {
                    found = false;
                    return found;
                } else {
                    found = true;
                    return found;
                }
            });
            if (found) {
                return {id: tag.term, text: tag.term, isNew: true};
            } else {
                return null;
            }
        }
    });
    $('#equipmentMake').select2({
        placeholder: "Select Make",
        allowClear: true
    });
    $('#equipmentEquipment').select2({
        placeholder: "Select Equipment",
        allowClear: true
    });
    $('#inactiveEmpCode').select2({
        placeholder: "Select Employee *",
        allowClear: true
    });
    $('#tankEquipment').select2({
        placeholder: "Select Application",
        allowClear: true
    });
    $('#tankTankNo').select2({
        placeholder: "Select Application",
        allowClear: true
    });
    $('#mstEquipment').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Equipment"
    });
    $('#equipmentEquipment').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Equipment"
    });
    $('#equipmentMake').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Make"
    });
    $('#makeName').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Make"
    });
    $("#departmentDepartment").select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Department"
    });
    $('#custIndustry').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Industry"
    });
    $('#custCustomer').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Customer"
    });
    $('#sampleIndustry').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Industry"
    });
    $('#tseOfficer').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Tse Officer"
    });
    $('#sampleCustomer').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Customer"
    });
    $('.equipMakeClass').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Make *"
    });
    $('#sampleDepartment').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Department"
    });
    $('#sampleApplication').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Application"
    });
    $('#sampleEquipment').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Equipment"
    });
    $('#prodNameOMC').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Equipment"
    });
    $('#changeRole').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Role"
    });
    $('#makeName').select2({
        multiple: true,
        placeholder: 'Enter Make Name *',
        tags: true,
        allowClear: true,
        createTag: function (tag) {
            found = false;
            $('#makeName option').each(function () {
                if ($.trim(tag.term).toUpperCase() === $.trim($(this).val())) {
                    found = false;
                    return found;
                } else {
                    found = true;
                    return found;
                }
            });
            if (found) {
                return {id: tag.term, text: tag.term, isNew: true};
            } else {
                return null;
            }
        }
    });
    $('#makeName').on("select2:selecting", function (e) {
        if (e.params.args.data.isNew) {
            return true;
        } else {
            $.alert({
                title: 'Duplicate Make',
                content: 'Make Already Exists.',
                type: 'red',
                typeAnimated: true
            });
            e.preventDefault();
        }
    });
    $('#sampleTankNo').select2({
        multiple: false,
        placeholder: "Enter Tank No",
        tags: true,
        allowClear: true,
        createTag: function (tag) {
            found = false;
            $("#sampleTankNo option").each(function () {
                if ($.trim(tag.term).toUpperCase() === $.trim($(this).val())) {
                    found = false;
                    return found;
                } else {
                    found = true;
                    return found;
                }
            });
            if (found) {
                return {id: tag.term, text: tag.term, isNew: true};
            } else {
                return null;
            }
        }
    });
    $('#sampleTankNo').on("select2:selecting", function (e) {
        if (e.params.args.data.isNew) {
            return true;
        } else {
            $.alert({
                title: 'Duplicate Tank Number',
                content: 'Tank Number Already Exists.',
                type: 'red',
                typeAnimated: true
            });
            e.preventDefault();
        }
    });
    $('#idlabName').select2({
        minimumResultsForSearch: -1
    });
    $('#sampleProduct').select2({
        allowClear: true,
        multiple: false,
        placeholder: "Select Product"
    });
    $('#industryNameInput').select2({
        multiple: true,
        placeholder: "Select Industry *",
        tags: true,
        allowClear: true
    });
    $('#productNameInput').select2({
        multiple: true,
        placeholder: "Select Product *",
        tags: true,
        allowClear: true
    });
    $('#makeNameInput').select2({
        multiple: true,
        placeholder: "Select Make *",
        tags: true,
        allowClear: true
    });
    $('#applicationNameInput').select2({
        multiple: true,
        placeholder: "Select Application *",
        tags: true,
        allowClear: true
    });
    $('#changeEmpCode').select2({
        placeholder: "Select Employee",
        allowClear: true
    });
    $('#OVforTest').select2({
        multiple: true,
        placeholder: "Add or Select other values",
        tags: true,
        allowClear: true
    });
    $('#CustomerModal').select2({
        multiple: true,
        placeholder: "Enter Customer(s) Name *",
        tags: true,
        allowClear: true,
        createTag: function (tag) {
            found = false;
            $("#CustomerModal option").each(function () {
                if ($.trim(tag.term).toUpperCase() === $.trim($(this).val())) {
                    found = false;
                    return found;
                } else {
                    found = true;
                    return found;
                }
            });
            if (found) {
                return {id: tag.term, text: tag.term, isNew: true};
            } else {
                return null;
            }
        }
    });
    $('#CustomerModal').on("select2:selecting", function (e) {
        if (e.params.args.data.isNew) {
            return true;
        } else {
            $.alert({
                title: 'Duplicate Customer',
                content: 'Customer Already Exists.',
                type: 'red',
                typeAnimated: true
            });
            e.preventDefault();
        }
    });
    $('#takeSample_click').on('click', function () {
        $('#sendtoMaintain').hide();
        $('#takeSample').show();
        $('#sendtoMaintain_click').removeClass('active');
        $('#takeSample_click').addClass('active');
    });
    $('#sendtoMaintain_click').on('click', function () {
        $('#takeSample').hide();
        $('#sendtoMaintain').show();
        $('#takeSample_click').removeClass('active');
        $('#sendtoMaintain_click').addClass('active');
    });
    $('#showaddtest').click(function (event) {
        $('.showaddtestbox').show();
    });
    if ($('#globalMsg').val() != "" || $('#globalMsg').val() != "") {
        $('#responseDialog').modal('show');
    }
    $(document).on('change', '#labCodeId', function (event) {
        var x = document.getElementById("labCodeId").selectedIndex;
        $("#labAuthorityId").val(document.getElementsByTagName("option")[x].id);
    });
    $('.removeBackdrop').click(function () {
        $('.modal-backdrop').hide();
    });
    $('#deptSubmit').click(function (e) {
        e.preventDefault();
        if ($('#deptTable > tbody > tr').length > 0) {
            if (document.getElementById('updateDepartmentForm').checkValidity()) {
                $.ajax({
                    type: 'POST',
                    url: 'AddDepartments',
                    dataType: 'JSON',
                    data: $('#updateDepartmentForm').serialize(),
                    success: function (data) {
                        $('#UpdateDepartment').modal('hide');
                        showRawResponseDialog(data);
                        getCustomerDept();
                    },
                    error: function (data) {
                        showRawResponseDialog(data);
                        $('#UpdateDepartment').modal('hide');
                    }
                });
            } else {
                $.alert({
                    title: 'Incomplete Details !!!',
                    content: 'All Fields are Required.',
                    type: 'red',
                    typeAnimated: true
                });
            }
        } else {
            $.alert({
                title: 'No Departments !!!',
                content: 'Add Department Details using "Add More Department" Button.',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $('#equipSubmit').click(function (e) {
        e.preventDefault();
        if ($('#equipTable > tbody > tr').length > 0) {
            if (document.getElementById('updateEquipmentForm').checkValidity()) {
                $.ajax({
                    type: 'POST',
                    url: 'AddEquipments',
                    dataType: 'JSON',
                    data: $('#updateEquipmentForm').serialize(),
                    success: function (data) {
                        showRawResponseDialog(data);
                        $('#UpdateEquipment').modal('hide');
                        getCustomerEquipment();
                    },
                    error: function (data) {
                        showRawResponseDialog(data);
                        $('#UpdateEquipment').modal('hide');
                    }
                });
            } else {
                $.alert({
                    title: 'Incomplete Details !!!',
                    content: 'All Fields are Required.',
                    type: 'red',
                    typeAnimated: true
                });
            }
        } else {
            $.alert({
                title: 'No Equipments !!!',
                content: 'Add Equipment Details using "Add More Equipment" Button.',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $('#addMake').click(function () {
        $('#UpdateMake').modal('show');
    });
    $(document).bind("ajaxSend", function () {
        waitingDialog.show('Loading Something...');
    }).bind("ajaxComplete", function () {
        waitingDialog.hide('Loading Something...');
        $('modal-backdrop').hide();
    });
    $('#inactiveEmpCode').change(function () {
        $('#inActiveRoleId').val($('#inactiveEmpCode option:selected').attr('data-roleId'));
    });
});
function populateSampleData(event, custInfoId) {
    $('#modal-title-sample').text('Edit Sample');
    $('#sampleDepartmentModal option').remove();
    $('#sampleEquipmentModal option').remove();
    $('#sampleTankNoModal option').remove();
    $('#sampleSubmit').text('EDIT');
    $('#custInfoId').val(custInfoId);
    document.getElementById("sampleIndustryModal").selectedIndex = [...document.getElementById("sampleIndustryModal").options].findIndex(option => option.text === $(event).closest("tr").find('td:eq(0)').text());
    document.getElementById("sampleApplicationModal").selectedIndex = [...document.getElementById("sampleApplicationModal").options].findIndex(option => option.text === $(event).closest("tr").find('td:eq(3)').text());
    document.getElementById("sampleProductModal").selectedIndex = [...document.getElementById("sampleProductModal").options].findIndex(option => option.text === $(event).closest("tr").find('td:eq(6)').text());
    if ($('#sampleCustomerModal option').length === 0) {
        getCustomer();
    }
    document.getElementById("sampleCustomerModal").selectedIndex = [...document.getElementById("sampleCustomerModal").options].findIndex(option => option.text === $(event).closest("tr").find('td:eq(1)').text());
    if ($('#sampleDepartmentModal option').length === 0) {
        getDeptEquip(event);
    }
    $('#sampleCapacityModal').val($(event).closest("tr").find('td:eq(7)').text());
    $('#sampleOilChangeModal').val($(event).closest("tr").find('td:eq(8)').text());
    $('#sampleDescModal').val($(event).closest("tr").find('td:eq(9)').text());
    $('#sampleFreqModal').val($(event).closest("tr").find('td:eq(10)').text());
    $('#samplePrevDateModal').val($(event).closest("tr").find('td:eq(11)').text());
    $('#sampleNxtDateModal').val($(event).closest("tr").find('td:eq(12)').text());
    nextSampleCheck();
    $('#UpdateSample').modal('show');
}

function nextSampleCheck() {
    var dateParts = $('#samplePrevDateModal').val().split('-');
    var fullDate = new Date(Number(dateParts[2]), Number(dateParts[1]) - 1, Number(dateParts[0]));
    fullDate.setMonth(fullDate.getMonth() + $('#sampleFreqModal').val());
    var currentDate = new Date();
    //    var age = currentDate - fullDate;
    var timediff = Math.abs(currentDate.getTime() - fullDate.getTime());
    //    var value = Math.ceil(timediff / (1000 * 3600 * 24));
    //if()
}

function isNumber(evt) {
    var iKeyCode = (evt.which) ? evt.which : evt.keyCode;
    if (iKeyCode != 47) {
        if (iKeyCode !== 46 && iKeyCode > 31 && (iKeyCode < 48 || iKeyCode > 57)) {
            $.alert({
                title: '',
                content: 'Only Numbers allowed.',
                type: 'red',
                typeAnimated: true
            });
            return false;
        }
    }
    return true;
}

function disabledMsg() {
    $.alert({
        title: '',
        content: "Changes Won't be Saved.",
        type: 'red',
        typeAnimated: true
    });
}

function eodDropdown(selectedDate, addDays) {
    var selectedDate = $('#recsampleDate .input-group.date').datepicker('getDate');
    var today = new Date();
    var targetDate = new Date();
    targetDate.setDate(today.getDate() + addDays);
    if (Date.parse(targetDate) > Date.parse(selectedDate)) {
        $("#delayReason").css('visibility', 'hidden');
        $("#selectdelayReasonid").css('display', 'none');
        $("#selectdelayReasonid option").prop("selected", false);
        $("#Equip").css('visibility', 'hidden');
        $("#Equipment").css('visibility', 'hidden');
        $("#Equipmentid").css('display', 'none');
        $("#Equipmentid option").prop("selected", false);
        $('#receivesampleForm').data('formValidation').disableSubmitButtons(false);
    } else {
        if (Date.parse(selectedDate) != null) {
            $("#delayReason").css('visibility', 'visible');
            $("#selectdelayReasonid").css('display', 'block');
        }
    }
}

function populateData(event) {
    document.getElementById("mstIndustry").selectedIndex = [...document.getElementById("mstIndustry").options].findIndex(option => option.text === $(event).closest("tr").find('td:eq(0)').text());
    loadCustomers($(event).closest("tr").find('td:eq(0)').text());
    document.getElementById("mstCustomer").selectedIndex = [...document.getElementById("mstCustomer").options].findIndex(option => option.text === $(event).closest("tr").find('td:eq(1)').text());
    $("#recsplind").val($(this).parent().parent().find('td:eq(1)').text());
    $("#recsplcust").val($(this).parent().parent().find('td:eq(2)').text());
    $("#recsplpro").val($(this).parent().parent().find('td:eq(3)').text());
    $("#recsplqty").val($(this).parent().parent().find('td:eq(5)').text());
    $("#recspldrawndate").val($(this).parent().parent().find('td:eq(6)').text())
}

function loadothData(testId, testVal) {

    if (otherDropdownLoaded) {
        $('#labDropdown').empty();
        $.ajax({
            type: "GET",
            url: "GetOtherDropdown",
            data: {testId: testId},
            dataType: "json",
            success: function (data) {
                $('#labDropdown').append($('<option>', {value: ''}).text("SELECT"));
                jQuery.each(data, function (i, val) {
                    if (testVal === val) {
                        $('#labDropdown').append($('<option selected>', {value: val}).text(val));
                    } else {
                        $('#labDropdown').append($('<option>', {value: val}).text(val));
                    }
                });
                otherDropdownLoaded = false;
            },
            error: function (data) {
                otherDropdownLoaded = true;
                $('#labDropdown').append($('<option>', {value: 0}).text("No values Has been Assigned."));
            }
        });
    }
}
function checkEmpty(val) {
    if (val === null || val === "") {
        return false;
    } else {
        return true;
    }
}

function validationMin(evnt, rangeVal) {
    if (checkEmpty(evnt.value)) {
        if (evnt.value < rangeVal) {
            $(evnt).removeClass("alert-success");
            $(evnt).addClass("outRange");
        } else if (evnt.value >= rangeVal) {
            $(evnt).addClass("alert-success");
            $(evnt).removeClass("outRange");
        }
    }
}

function validationMinMax(evnt, minVal, maxVal) {
    if (checkEmpty(evnt.value)) {
        if (evnt.value >= minVal && evnt.value <= maxVal) {
            $(evnt).addClass("alert-success");
            $(evnt).removeClass("outRange");
        } else {
            $(evnt).removeClass("alert-success");
            $(evnt).addClass("outRange");
        }
    }
}

function validationMax(evnt, rangeVal) {
    if (checkEmpty(evnt.value)) {
        if (evnt.value > rangeVal) {
            $(evnt).removeClass("alert-success");
            $(evnt).addClass("outRange");
        } else if (evnt.value <= rangeVal) {
            $(evnt).addClass("alert-success");
            $(evnt).removeClass("outRange");
        }
    }
}

function validationTypVal(evnt, typVal, devVal) {
    if (checkEmpty(evnt.value)) {
        var minRange = typVal - (typVal * devVal / 100);
        var maxRange = typVal + (typVal * devVal / 100);
        validationMinMax(evnt, minRange, maxRange);
    }
}

function validationSpclMax(evnt, rangeMax) {
    var txtVal = (evnt.value).split("/");
    var maxVal = rangeMax.split("/");
    if (txtVal[0] === '' || typeof txtVal[0] === 'undefined' || txtVal[1] === '' || typeof txtVal[1] === 'undefined') {
        $(evnt).removeClass("alert-success");
        $(evnt).addClass("alert-warning");
        $.alert({
            title: '',
            content: 'Please Enter value in correct Format.',
            type: 'red',
            typeAnimated: true
        });
        evnt.value = "";
    } else {
        $(evnt).removeClass("alert-warning");
        if (txtVal[0] <= maxVal[0] && txtVal[1] <= maxVal[1])
        {
            $(evnt).addClass("alert-success");
            $(evnt).removeClass("outRange");
        } else
        {
            $(evnt).removeClass("alert-success");
            $(evnt).addClass("outRange");
        }
    }

}
function callGetSamplePostponedDetail(ciId, psDate) {
    $('#PostponedDetails').DataTable().destroy();
    $('#PostponedDetails').DataTable({
        "ajax": 'GetPostponedDetails?custInfoId=' + ciId + '&prevsampledate=' + psDate,
        "columns": [
            {"data": "PREV_DATE"},
            {"data": "NEXT_DATE"},
            {"data": "POSTPONED_DATE"},
            {"data": "REMARKS"}
        ]
    });
    $('#PostponedDetailsModal').modal('show');
    $('#PostponedDetails').DataTable();
}

function footerAlign() {
    $('footer').css('display', 'block');
    $('footer').css('height', 'auto');
    var footerHeight = $('footer').outerHeight();
    $('body').css('padding-bottom', footerHeight);
    $('footer').css('height', footerHeight);
}

function validate() {
    document.querySelectorAll('.validate').forEach(function (button) {
        button.click();
    });
}

function getval(selectedEquipment) {
    if (selectedEquipment.options[selectedEquipment.selectedIndex].text === "Instrument Not Working") {
        $.ajax({
            type: "GET",
            url: "/GetEquipmentsForMalFunctioning",
            dataType: "json",
            success: function (data) {
                $('#labDropdown').append($('<option>', {value: ''}).text("SELECT"));
                jQuery.each(data, function (i, val) {
                    $('#labDropdown').append($('<option>', {value: val.equipmentId}).text(val.equipmentName));
                });
            },
            error: function (data) {
                $('#labDropdown').append($('<option>', {value: 0}).text("No values Has been Assigned."));
            }
        });
    }
}

function equipmentDead(selectedEquipment) {
    if (selectedEquipment.options[selectedEquipment.selectedIndex].text === "Instrument Not Working") {
        $.ajax({
            type: "GET",
            url: "GetEquipmentsForMalFunctioning?action=YES",
            dataType: "json",
            success: function (data) {
                jQuery.each(data, function (i, val) {
                    $('#Equipmentid').append($('<option>', {value: val.equipmentId}).text(val.equipmentName));
                });
            },
            error: function (data) {
                $('#Equipmentid').append($('<option>', {value: 0}).text("No values Has been Assigned."));
            }
        });
        $("#Equipment").css('visibility', 'visible');
        $("#Equipmentid").css('display', 'block');
    } else {
        $("#Equipment").css('visibility', 'hidden');
        $("#Equipmentid").css('display', 'none');
        $("#Equipmentid option").remove();
        $('#receivesampleForm').data('formValidation').disableSubmitButtons(false);
    }
}

function removeRow(e) {
    var click = $(e).closest('tr').index() + 1;
    $('#Market-Test tr:eq(' + click + ')').toggle();
    return false;
}

function loadEmployeeDetailFromCEM(empCode, flag) {
    $.ajax({
        url: "GetEmployeeDetails",
        data: {empCode: empCode},
        dataType: "json",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                if (flag === 1) {
                    $("#EmpCodeLoadedModal").val(data.empCode);
                    $("#EmpNameModal").val(data.empName);
                    $("#EmpEmailModal").val(data.empEmail);
                    $("#CtrlEmpCodeModal").val(data.ctrlEmpCode);
                    $("#CtrlEmpNameModal").val(data.ctrlEmpName);
                    $("#CtrlEmpEmailModal").val(data.ctrlEmpEmail);
                    $("#sampleEmpCodeLoadedModal").val(data.empCode);
                    $("#sampleEmpNameModal").val(data.empName);
                    $("#sampleEmpEmailModal").val(data.empEmail);
                    $("#sampleCtrlEmpCodeModal").val(data.ctrlEmpCode);
                    $("#sampleCtrlEmpNameModal").val(data.ctrlEmpName);
                    $("#sampleCtrlEmpEmailModal").val(data.ctrlEmpEmail);
                } else if (flag === 2) {
                    $("#custCtrlEmpNameModal").val(data.empName);
                    $("#custCtrlEmpEmailModal").val(data.empEmail);
                }
            } else {
                if (flag === 1)
                    $("#custEmpCodeModal").val("");
                else if (flag === 2) {
                    $("#custCtrlEmpCodeModal").val("");
                    $("#custCtrlEmpNameModal").val("");
                    $("#custCtrlEmpEmailModal").val("");
                }
                alert("Not a valid emp code");
                //$('#idAddEditTseCustMapForm').formValidation('revalidateField', 'ctrlEmpCode');
            }
            //$('#addCustomer').formValidation('revalidateField', 'tseEmpCode');
            //$('#addCustomer').formValidation('revalidateField', 'ctrlEmpCode');
        },
        error: function (data) {
            alert("Error in fetching data. Please try later.");
        }
    });
}

function loadCustomers(industry) {
    $("#mstCustomer option").remove();
    $('#mstCustomer').append($('<option>', {value: ''}).text("Select Customer"));
    $("#mstDepartment option").remove();
    $('#mstDepartment').append($('<option>', {value: ''}).text("Select Department"));
    $("#mstEquipment option").remove();
    $('#mstEquipment').append($('<option>', {value: ''}).text("Select Equipment"));
    if (industry === null || industry === "") {
        return false;
    } else {
        $.ajax({
            type: "GET",
            url: "/ServoCMP/GetCustomers",
            data: {industry: industry},
            dataType: "json",
            success: function (data) {
                jQuery.each(data, function (i, val) {
                    $('#mstCustomer').append($('<option>', {value: val.customerId}).text(val.customerName));
                });
            },
            error: function (data) {
                $('#mstCustomer').append($('<option>', {value: 0}).text("Try Again Later."));
            }
        });
    }
    getAvailableTank();
}

function loadDepartment(customer, identifier) {
    $("#mstDepartment option").remove();
    $('#mstDepartment').append($('<option>', {value: ''}).text("Select Department"));
    $("#departmentDepartment option").remove();
    $('#departmentDepartment').append($('<option>', {value: ''}).text("Select Department"));
    $("#mstEquipment option").remove();
    $('#mstEquipment').append($('<option>', {value: ''}).text("Select Equipment"));
    $.ajax({
        type: "GET",
        url: "/ServoCMP/GetDepartments",
        data: {customer: customer},
        dataType: "json",
        success: function (data) {
            switch (identifier) {
                case "NS":
                    if (data[0].constructor === Array) {
                        jQuery.each(data[0], function (i, val) {
                            $('#mstDepartment').append($('<option>', {value: val.departmentId}).text(val.departmentName));
                        });
                    } else {
                        $('#mstDepartment').append($('<option>', {value: null}).text("There are No Departments. Kindly add Departments."));
                    }
                    if (data[1].constructor === Array) {
                        jQuery.each(data[1], function (i, val) {
                            $('#mstEquipment').append($('<option>', {value: val.equipmentId}).text(val.equipmentName));
                        });
                    } else {
                        $('#mstEquipment').append($('<option>', {value: null}).text("There are No Equipments. Kindly Add Equipments."));
                    }
                    break;
                case "MD":
                    if (data[0].constructor === Array) {
                        jQuery.each(data[0], function (i, val) {
                            $('#departmentDepartment').append($('<option>', {value: val.departmentId}).text(val.departmentName));
                        });
                    } else {
                        $('#departmentDepartment').append($('<option>', {value: null}).text(data));
                    }
                    break;
            }
        },
        error: function (data) {
            $('#mstDepartment').append($('<option>', {value: 0}).text("There are no Department. Kindly Add Department."));
            $('#mstEquipment').append($('<option>', {value: 0}).text("There are no Equipments. Kindly Add Equipment."));
        }
    });
    getAvailableTank();
}

function getDepartment() {
    if ($('#deptCustomer').val() === '') {
        $.alert({
            title: 'Select Fields',
            content: 'Select Customer to get All Department',
            type: 'red',
            typeAnimated: true
        });
        return false;
    } else {
        $('#modalCustomer').text($('#customerId option:selected').text());
        $('#manageDepartmentModal').modal('show');
    }
}

function genIOM(divID, ref_no) {
//var status = confirm("Are you Sure?");
    if ($('#idIOMrefNO').val() !== "")
    {
        console.log($('#idIOMrefNO').val());
        $('#idIOMrefNO').removeClass("alert-danger");
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to generate IOM?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {

                        var temp = 0;
                        var data = $('#iomDataTb1').DataTable().rows().data();
                        var sampleIds = [];
                        for (var i = 0; i < data.length; i++) {
                            sampleIds.push(data[i][1]);
                        }
                        $('#iomDataTb1').DataTable().destroy();
                        if (sampleIds.length > 0) {
                            $("input[id=idIOMSampleIds]").val(JSON.stringify(sampleIds));
                        }
                        var nameIOMSampleIds = $('#idIOMSampleIds').val();
                        var nameLabLocCode = $('#idLabLocCode').val();
                        var nameIOMrefNO = $('#idIOMrefNO').val();
                        $.ajax({
                            type: "POST",
                            url: "/ServoCMP/Tse/ManageIOM",
                            data: {
                                nameIOMSampleIds: nameIOMSampleIds,
                                nameLabLocCode: nameLabLocCode,
                                nameIOMrefNO: nameIOMrefNO
                            },
                            dataType: "json",
                            success: function (data) {
                                if (data.msgClass === 'text-success') {
                                    temp = 1;
                                }
                                if (!(data.modalMessage).includes('Error')) {
                                    generatePDF2(divID, nameIOMrefNO);
                                }
                                showRawResponseDialog(data);
                                $('#responseDialog').modal('show');
                                $("#submitBut1").prop("disabled", true);
                            },
                            error: function (e) {
                                showResponseDialog();
                                $('#responseDialog').modal('show');
                            }
                        });
                        $('#responseDialog').on('hidden.bs.modal', function () {
                            if (temp === 1) {
                                window.location.href = "/ServoCMP/Tse/GetCreatedSamples?status=0&labName=" + nameLabLocCode + "&csrftoken=" + $('[name="csrf-token"]').attr('content');
                            }
                        });
                    }


                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No IOM is generated.',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    } else
    {
        $('#idIOMrefNO').addClass("alert-danger");
    }
}

//function generatePDF(divID, pdfFileName) {
//    var options = {};
//    var pdf = new jsPDF('p', 'pt', 'a4');
//    pdf.addHTML($("#" + divID), 15, 15, options, function () {
//        pdf.save(pdfFileName + '.pdf');
//    });
//}

function generatePDF(divID, pdfFileName) {
    var img = new Image;
    img.crossOrigin = "";
//        img.src = "http://10.147.69.98:8081/ServoCMP/resources/images/indianoil-logo.png";
    img.src = "../resources/images/indianoil-logo.png";
    img.onload = function () {
        var doc = new jsPDF('l', 'pt', 'a4');
        var res = doc.autoTableHtmlToJson(document.getElementById(divID));
        doc.setFontSize(20);
        doc.setFontStyle('helvetica');
        doc.addImage(img, 'PNG', 365, 15);
        doc.setFontSize(14);
        doc.text(340, 75, 'IOM- FOR USED OIL SAMPLES');
        doc.setFontSize(10);
        doc.setTextColor(0, 0, 230);
//        doc.text(35, 100, 'TO \nGOURAB SEN \nBRIAN HOUSE');
        doc.text(35, 100, 'TO \n' + document.getElementsByClassName('labAuthority').value + '\n' + document.getElementsByClassName('labName').value);
        doc.text(800, 100, 'Date: ' + $.datepicker.formatDate('dd/mm/yy', new Date()), 'right');
        doc.setTextColor(0, 0, 0);
        doc.setFontSize(10);
        doc.autoTable(res.columns, res.rows, {
            margin: {top: 150},
            styles: {
                fontSize: 7,
                font: "helvetica",
                valign: 'middle',
                overflow: 'linebreak',
                columnWidth: 'auto'
            },
            columnStyles: {
                7: {columnWidth: 250}
            }
        });
        doc.setFontSize(10);
//        doc.text(800, doc.autoTableEndPosY() + 50, 'GOURAB SEN\nIS OFFICER\nWESTERN REGION OFFICE', 'right');
        doc.text(800, doc.autoTableEndPosY() + 50, document.getElementsByClassName('empName') + '\n' + document.getElementsByClassName('designDesc') + '\n' + document.getElementsByClassName('currComputer'), 'right');
        doc.save(pdfFileName + '.pdf');
    };
}

function generatePDF2(divID, pdfFileName) {
    var img = new Image;
    img.crossOrigin = "";
//        img.src = "http://10.147.69.98:8081/ServoCMP/resources/images/indianoil-logo.png";
    img.src = "../resources/images/indianoil-logo.png";
    img.onload = function () {
        var doc = new jsPDF('l', 'pt', 'a4');
        var pageContent = function (data) {
            // HEADER
            doc.setFontSize(20);
            doc.setFontStyle('helvetica');
            doc.addImage(img, 'PNG', 365, 15);
        };
        var res = doc.autoTableHtmlToJson(document.getElementById(divID));
        doc.setFontSize(20);
        doc.setFontStyle('helvetica');
        doc.addImage(img, 'PNG', 365, 15);
        doc.setFontSize(14);
        doc.text(340, 75, 'IOM - FOR USED OIL SAMPLES');
        doc.setFontSize(10);
        doc.setTextColor(0, 0, 230);
//        doc.text(35, 100, 'TO \nGOURAB SEN \nBRIAN HOUSE');
        doc.text(35, 100, 'TO \n' + $('.labAuthority > b').html() + '\n' + $('.labName> b').html());
        doc.text(800, 100, 'Date:' + $('.dateIOM > b').html(), 'right');
        doc.setTextColor(0, 0, 0);
        doc.text(35, 160, 'Ref No : ' + pdfFileName);
        doc.setFontSize(10);
        doc.autoTable(res.columns, res.rows, {
            startY: 190,
            margin: {top: 100},
            styles: {
                fontSize: 7,
                font: "helvetica",
                valign: 'middle',
                overflow: 'linebreak',
                columnWidth: 'auto'
            },
            addPageContent: pageContent,
            columnStyles: {
                7: {columnWidth: 250}
            }
        });
        doc.setFontSize(10);
//        doc.text(800, doc.autoTableEndPosY() + 50, 'GOURAB SEN\nIS OFFICER\nWESTERN REGION OFFICE', 'right');
        doc.text(800, doc.autoTableEndPosY() + 50, $('.empName > b').html() + '\n' + $('.designDesc > b').html() + '\n' + $('.currComputer > b').html(), 'right');
        doc.save(pdfFileName + '.pdf');
    };
}


function callUpdate(sampleId, labCode) {
    $.ajax({
        type: "post",
        url: "DeleteFileUploadedByLAB?smplid=" + sampleId + '&labCode=' + labCode,
        dataType: 'json',
        success: function (data) {
            if (data.status) {
                $('#divUploadFile').html("");
                $('#divUploadFile').html("<label>No Files Attached</label>");
                $('#divUploadFile').css('padding', '15px');
            }
            $('#modal-title').text(data.modalTitle);
            $('#modalMessage').text(data.modalMessage);
            $('#fileMsg').text(data.fileMsg);
            $('#mailMsg').text(data.mailMsg);
            $('#modalMessage').addClass(data.msgClass);
            $('#fileMsg').addClass(data.filemsgClass);
            $('#mailMsg').addClass(data.mailMsgClass);
            $('#responseDialog').modal('show');
        },
        error: function (data) {
            $('#modal-title').text(data.modalTitle);
            $('#modalMessage').text(data.modalMessage);
            $('#fileMsg').text(data.fileMsg);
            $('#mailMsg').text(data.mailMsg);
            $('#modalMessage').addClass(data.msgClass);
            $('#fileMsg').addClass(data.filemsgClass);
            $('#mailMsg').addClass(data.mailMsgClass);
            $('#responseDialog').modal('show');
        }
    });
}

function showResponseDialog(data) {
    var jsonData = JSON.parse(data);
    $('#modal-title').text(jsonData.modalTitle);
    $('#modalMessage').text(jsonData.modalMessage);
    $('#fileMsg').text(jsonData.fileMsg);
    $('#mailMsg').text(jsonData.mailMsg);
    $('#modalMessage').addClass(jsonData.msgClass);
    $('#fileMsg').addClass(jsonData.filemsgClass);
    $('#mailMsg').addClass(jsonData.mailMsgClass);
    $('#responseDialog').modal('show');
}

function checkRemarks() {
    var remks = $('#remarks').val();
    if (remks === '' || remks === null) {
        $.alert({
            title: '',
            content: 'Please Enter Remarks to Review Sample.',
            type: 'red',
            typeAnimated: true
        });
        return false;
    } else
        return true;
}

function getLabTestData(labCode1, labCode2, labType) {
    var result = {
        "csl_labCode": labCode1,
        "rnd_labCode": labCode2
    };
    var result2 = [];
    var csl_result2 = {};
    var rnd_result2 = {};
    $.ajax({
        async: false,
        url: '/ServoCMP/Tse/LabTestSpecsServlet2?csl_labCode=' + result.csl_labCode + '&rnd_labCode=' + result.rnd_labCode + '&labType=' + labType,
        type: 'POST',
        dataType: 'JSON',
        success: function (data) {
            switch (labType) {
                case "1":
                    csl_result2 = data;
                    result2.push(csl_result2);
                    break;
                case "2":
                    rnd_result2 = data;
                    result2.push(rnd_result2);
                    break;
                case "3":
                    csl_result2 = data[0];
                    rnd_result2 = data[1];
                    result2.push(csl_result2);
                    result2.push(rnd_result2);
                    break;
            }
        },
        error: function (error) {
            console.log(error.responseText);
        }
    });
    return result2;
}

$(document).on('click', '#submitSample', function () {
    var labCode1 = $('select[name="csl_labCode"]').val();
    var labCode2 = $('select[name="rnd_labCode"]').val();
    var labCode3 = $('select[name="csl_bth_labCode"]').val();
    var labCode4 = $('select[name="rnd_bth_labCode"]').val();
    var labType = $('#labType').val();
    var availTest = false;
    var list;
    var testArr = [];
    var testArr1 = [];
    var testArr2 = [];
    var nonAvailTest1 = [];
    var nonAvailTest2 = [];
    var arr = [];

    switch (labType) {
        case "1":
            list = getLabTestData(labCode1, "", labType);
            nonAvailTest1 = list[0].filter(val => val.active == "0");

            if ($('.exist-add-dropdown_tab1').val() != null) {
                testArr = $('.exist-add-dropdown_tab1').val().concat($('.exist-test-dropdown_tab1').val());
            } else {
                testArr = $('.exist-test-dropdown_tab1').val();
            }

            $.each(nonAvailTest1, function (i, v) {
                if (testArr.indexOf(v.testId) > -1) {
                    arr.push(v.testName);
                }
            });

            break;
        case "2":
            list = getLabTestData("", labCode1, labType);
            nonAvailTest2 = list[0].filter(val => val.active == "0");

            if ($('.exist-add-dropdown_tab2').val() != null) {
                testArr = $('.exist-add-dropdown_tab2').val().concat($('.exist-test-dropdown_tab2').val());
            } else {
                testArr = $('.exist-test-dropdown_tab2').val();
            }
            $.each(nonAvailTest2, function (i, v) {
                if (testArr.indexOf(v.testId) > -1) {
                    arr.push(v.testName);
                }
            });
            break;
        case "3":
            list = getLabTestData(labCode3, labCode4, labType);
            nonAvailTest1 = list[0].filter(val => val.active == "0");
            nonAvailTest2 = list[1].filter(val => val.active == "0");

            if ($('.sample-csl-add-dropdown').val() != null) {
                testArr1 = $('.sample-csl-test-dropdown').val().concat($('.sample-csl-add-dropdown').val());
            } else {
                testArr1 = $('.sample-csl-test-dropdown').val();
            }
            if ($('.sample-rnd-add-dropdown').val() != null) {
                testArr2 = $('.sample-rnd-test-dropdown').val().concat($('.sample-rnd-add-dropdown').val());
            } else {
                testArr2 = $('.sample-rnd-test-dropdown').val();
            }

            $.each(nonAvailTest1, function (i, v) {
                if (testArr2 != null) {
                    if (testArr1.indexOf(v.testId) > -1) {
                        arr.push("CSL-" + v.testName);
                    }
                }
            });
            $.each(nonAvailTest2, function (i, v) {
                if (testArr2 != null) {
                    if (testArr2.indexOf(v.testId) > -1) {
                        arr.push("RND-" + v.testName);
                    }
                }
            });
            break;
        default:
            break;
    }


    var resultT = "";
    if (arr.length > 0) {
        resultT = arr.join(', ');
    } else {
        availTest = true;
    }

    if ($('#checkOtsSampling').val() == "0" && availTest) {
        if ($('#exist-test-dropdown option[value="1"]').data('spec') != "" &&
                $('#exist-test-dropdown option[value="3"]').data('spec') != "" &&
                $('#exist-test-dropdown option[value="9"]').data('spec') != ""
                ) {
            if ($('#formValid').val() == '1') {
                review('sendsampletoLAB');
            } else {
                $.alert({
                    title: 'Invalid Input',
                    content: 'Kindly check the test parameters again',
                    type: 'red',
                    typeAnimated: true
                });
            }
        } else {
            $.alert({
                title: '',
                content: 'Please Check If there is mapping against KV@40, VISCOSITY INDEX & TAN.',
                type: 'red',
                typeAnimated: true
            });
        }
    } else if ($('#checkOtsSampling').val() == "1" && availTest) {
        review('sendsampletoLAB');
    } else {

        var labName = "";
        switch (labType) {
            case '1':
                labName = $('select[name="csl_labCode"] option:selected').text()
                break;
            case '2':
                labName = $('select[name="rnd_labCode"] option:selected').text()
                break;
            case '3':
                labName = $('select[name="csl_bth_labCode"] option:selected').text() + "/" + $('select[name="rnd_bth_labCode"] option:selected').text();
                break;
        }

        var content = 'The following Test Params <b>(' + resultT + ')</b> cannot be tested at Lab:<b> ' + labName + '</b>';
        $.alert({
            title: 'Non Availlibity of Test Params',
            content: content,
            type: 'red',
            typeAnimated: true
        });
    }
});
function review(form) {
    var val = true;
    var sampleQty = [];
    // checkQty();


    if (form === 'sendsampletoLAB') {
        if (parseInt($('#qtyDrawn').val()) < parseInt($('#sampleTestQty').val())) {
            $.alert({
                title: 'Alert!!!',
                content: '<span class="text-success">Minimum Recommended Quantity (in ml) for selected Test(s) is <b>' + $('#sampleTestQty').val() + ' ml</b></span>',
                type: 'red',
                typeAnimated: true
            });
            val = false;
        }
    }
    if (val) {
        currentForm = form;
        parentDiv = $('#' + form).parent().prop('id');
        $("#" + form).data('formValidation').validate();
        if ($("#" + form).data('formValidation').isValid()) {
            $("#reviewModal").modal('show');
            $("#reviewModal .modal-lg-review .modal-content .modal-body-review").html("");
            $("#" + form).appendTo("#reviewModal .modal-lg-review .modal-content .modal-body-review");
            $("#" + form + " input, textarea, select").prop("disabled", true);
        } else {
            return false;
        }
    }
}

function closeReview() {
    $("#pdf").hide();
    $("#pdf").attr('src', '');
    $("#pdf").appendTo("#Sample");
    $("#" + currentForm).appendTo('#' + parentDiv);
    $("#" + currentForm + " input, textarea, select").prop("disabled", false);
    $("#reviewModal .modal-lg-review .modal-content .modal-body-review").html("");
}

function formSubmit() {
    $("#" + currentForm + " input, textarea, select").prop("disabled", false);
    document.getElementById(currentForm).submit();
}

function getAvailableTank() {
    $("#mstTank option").remove();
    $('#mstTank').append($('<option>', {value: ''}).text("Select Tank"));
    var industry = $('#mstIndustry').val();
    var customer = $('#mstCustomer').val();
    var department = $('#mstDepartment').val();
    var application = $('#mstApplication').val();
    var equipment = $('#mstEquipment').val();
    if (industry !== "" && customer !== "" && department !== "" && application !== "" && equipment !== "") {
        $.ajax({
            type: "GET",
            url: "/ServoCMP/GetAvailableTank",
            data: {
                industry: industry,
                customer: customer,
                department: department,
                application: application,
                equipment: equipment
            },
            dataType: "json",
            success: function (data) {
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#mstTank').append($('<option>', {value: val.tankId}).text(val.tankNo + " (" + val.tankDesc + ")"));
                    });
                } else {
                    $('#mstTank').append($('<option disabled>', {value: null}).text(data));
                }
            },
            error: function (data) {
                $('#mstTank').append($('<option>', {value: 0}).text("Error while fetching Tank Details."));
            }
        });
    }
}
function getPosition(arr) {
    var tickPositions;
    for (i = 0; i < arr.length; i++) {
        switch (i) {
            case 0:
                tickPositions = arr[i][0].toString() + ", ";
                break;
            case 1:
                tickPositions += arr[i][0].toString() + ", ";
                break;
            case 2:
                tickPositions += arr[i][0].toString();
        }
    }
    return tickPositions;
}

function comingSoon() {
    $.alert({
        title: '',
        content: 'This Feature is Under Construction.',
        type: 'red',
        typeAnimated: true
    });
}

function getSum(total, num) {
    return total + Math.round(num);
}

function checksampleQty(selec) {
    var sampleQty = [];
    $.each($(selec).find("option:selected"), function () {
        sampleQty.push($(selec).find('option[value="' + $(this).val() + '"]').data('qty'));
        if ($(selec).find('option[value="' + $(this).val() + '"]').data('spec') === '' || typeof ($('#exist-test-dropdown option[value="' + $(this).val() + '"]').data('spec')) === 'undefined') {
            testParamNames += $(this).text() + ' ';
//            $(this).prop('selected', false);
            $(selec).select2();
        }
    });
    if ($('#qtyDrawn').val() < sampleQty.reduce(getSum, 0)) {
        $.alert({
            title: 'Alert!!!',
            content: '<span class="text-success">Minimum Recommended Quantity (in ml) for selected Test(s) is <b>' + sampleQty.reduce(getSum, 0) + ' ml</b></span>',
            type: 'red',
            typeAnimated: true
        });
    }
}

function checkQty() {

    var testParamNames = "";
    var testSpecMsg = "";
    var sampleQty = [];
    $.each($("#exist-test-dropdown option:selected"), function () {
        sampleQty.push($('#exist-test-dropdown option[value="' + $(this).val() + '"]').data('qty'));
        if ($('#exist-test-dropdown option[value="' + $(this).val() + '"]').data('spec') === '' || typeof ($('#exist-test-dropdown option[value="' + $(this).val() + '"]').data('spec')) === 'undefined') {
            testParamNames += $(this).text() + ' ';
//            $(this).prop('selected', false);
            $("#exist-test-dropdown").select2();
        }
    });
    $.each($("#add-test-dropdown option:selected"), function () {

        if ($('#add-test-dropdown option[value="' + $(this).val() + '"]').data('spec') === '' || typeof ($('#add-test-dropdown option[value="' + $(this).val() + '"]').data('spec')) === 'undefined') {
            testParamNames += $(this).text() + ' ';
//            $(this).prop('selected', false);
            $("#add-test-dropdown").select2();
        } else
        {
            sampleQty.push($('#add-test-dropdown option[value="' + $(this).val() + '"]').data('qty'));
        }
    });
    if (testParamNames !== '')
    {
        testSpecMsg = "No specification mapping wrt product is exist against the test parameter: " + testParamNames;
    }
    if ($('#qtyDrawn').val() < sampleQty.reduce(getSum, 0)) {
        $.alert({
            title: 'Alert!!!',
            content: '<span class="text-success">Minimum Recommended Quantity (in ml) for selected Test(s) is <b>' + sampleQty.reduce(getSum, 0) + ' ml</b></span>',
            type: 'red',
            typeAnimated: true
        });
    }

    if (testParamNames !== '' && $('#sampleType').val() != 'OTS') {
        $.alert({
            title: 'Alert!!!',
            content: '<span class="text-danger">' + testSpecMsg + '</b></span>',
            type: 'red',
            typeAnimated: true
        });
    }
    $('#qtyDrawn').focus();
}

function openHelloIOCian(empId) {
    if (empId === "Unknown") {
        $.alert({
            title: 'Unknown Employee',
            content: 'The selected record is Updated by Unknown Employee',
            type: 'red',
            typeAnimated: true
        });
    } else {
        var linke = 'https://helloiocian.indianoil.in/HelloIOCian/Controlling_officer.aspx?emp_no=';
        if ($('[name="helloician"]').attr('content')) {
            linke = $('[name="helloician"]').attr('content');
        }
        window.open(linke + empId, '_blank');
    }
}

function AddIndustry(operation, indId, indName) {
    $('#modal-title-Industry').text(operation);
    $('#industrySubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#industrySubmit').val(operation.substr(0, operation.indexOf(' ')));
    if (operation.includes('Add')) {
        $('#industryId').val('');
        $('#industryNameInput').show();
        $('.hideActive').show();
        $('#industryNameModal').hide();
        $('.hideIndustryEdit').hide();
    } else {
        if (operation.includes('Update')) {
            $('#modal-title-Industry').text(operation.substr(0, operation.indexOf(' ')) + ' "' + indName + '" Industry');
            $('#indId').val(indId);
            $('#oldIndName').val(indName);
            $('#industryEdit').val(indName);
            $('#industryNameModal').hide();
            $('.hideActive').hide();
            $('#industryNameInput').hide();
            $('.hideIndustryEdit').show();
            $('#industryEdit').show();
        } else {
            $('#industryNameModal').show();
            $('.hideActive').hide();
            $('#industryNameInput').hide();
            $('.hideIndustryEdit').hide();
            $('#industryEdit').hide();
        }
    }
    $('#UpdateIndustry').modal('show');
}

function removeIndustry(indId, indName, event) {
    if (typeof indName === 'undefined') {
        $('#modal-title-Industry').text("Removed Industries");
        $('#industrySubmit').text("Activate");
        $('#industrySubmit').val("Activate");
        $('#industryNameModal option').remove();
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/TseAdmin/AllDeactiveIndustry',
            dataType: 'json',
            success: function (data) {
                $('#industryNameModal').append($('<option>', {value: ''}).text("Select Industry *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#industryNameModal').append($('<option>', {value: val.indId}).text(val.indName));
                    });
                } else {
                    $('#industryNameModal').append($('<option disabled>', {value: null}).text(data));
                }
            },
            error: function (data) {
                $('#industryNameModal').append($('<option disabled>', {value: null}).text("Error while fetching Industry"));
            }
        });
        $('#industryNameModal').show();
        $('#industryNameInput').hide();
        $('.hideActive').hide();
        $('.hideIndustryEdit').hide();
        $('#industryEdit').hide();
        $('#UpdateIndustry').modal('show');
    } else {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to Remove ' + indName + ' Industry?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $(event).parent('form').submit();
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made to ' + indName + ' Industry',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    }
}

function submitIndustry(btnObject) {
    switch (btnObject.value) {
        case "Add":
            if ($('#industryNameInput').val() === '' || $('#industryNameInput').val() === null) {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Industry Name',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateIndustryForm').attr('action', 'AddIndustry');
                $('#updateIndustryForm').submit();
            }
            break;
        case "Activate":
            if ($('#industryNameModal').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Select Industry.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateIndustryForm').attr('action', 'ActivateIndustry');
                $('#updateIndustryForm').append('<input type="hidden" name="industryName" value="' + $('#industryNameModal option:selected').text() + '"/>');
                $('#updateIndustryForm').submit();
            }
            break;
        case "Update":
            if ($('#industryEdit').val() === '' || $('#indId').val() === '' || $('#oldIndName').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Industry Name.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateIndustryForm').attr('action', 'UpdateIndustry');
                $('#updateIndustryForm').submit();
            }
            break;
    }
}

function AddApplication(operation, appId, appName) {
    $('#modal-title-Application').text(operation);
    $('#applicationSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#applicationSubmit').val(operation.substr(0, operation.indexOf(' ')));
    if (operation.includes('Add')) {
        $('#applicationId').val('');
        $('#applicationNameInput').show();
        $('.hideActive').show();
        $('#applicationNameModal').hide();
        $('.hideApplicationEdit').hide();
        $('#applicationEdit').hide();
    } else {
        if (operation.includes('Update')) {
            $('#modal-title-Application').text(operation.substr(0, operation.indexOf(' ')) + ' "' + appName + '" Application');
            $('#applicationEdit').val(appName);
            $('#appId').val(appId);
            $('#oldAppName').val(appName);
            $('#applicationNameInput').hide();
            $('.hideActive').hide();
            $('#applicationNameModal').hide();
            $('.hideApplicationEdit').show();
            $('#applicationEdit').show();
        } else {
            $('#applicationNameInput').hide();
            $('.hideActive').hide();
            $('.hideApplicationEdit').hide();
            $('#applicationEdit').hide();
            $('#applicationNameModal').show();
        }
    }
    $('#UpdateApplication').modal('show');
}

function removeApplication(appId, appName, event) {
    if (typeof appName === 'undefined') {
        $('#modal-title-Application').text("Inactive Applications");
        $('#applicationSubmit').text("Activate");
        $('#applicationSubmit').val("Activate");
        $('#applicationNameModal option').remove();
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/TseAdmin/AllDeactiveApplication',
            dataType: 'json',
            success: function (data) {
                $('#applicationNameModal').append($('<option>', {value: ''}).text("Select Application *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#applicationNameModal').append($('<option>', {value: val.appId}).text(val.appName));
                    });
                } else {
                    $('#applicationNameModal').append($('<option disabled>', {value: null}).text(data));
                }
                $('#UpdateApplication').modal('show');
            },
            error: function (data) {
                $('#applicationNameModal').append($('<option disabled>', {value: null}).text("Error while fetching Industry"));
            }
        });
        $('#applicationNameModal').show();
        $('#applicationNameInput').hide();
        $('.hideActive').hide();
        $('.hideApplicationEdit').hide();
        $('#applicationEdit').hide();
    } else {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to Remove ' + appName + ' Application?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $(event).parent('form').submit();
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made to ' + appName + ' Application',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    }
}

function submitApplication(btnObject) {
    switch (btnObject.value) {
        case "Add":
            if ($('#applicationNameInput').val() === '' || $('#applicationNameInput').val() === null) {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Application Name',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateApplicationForm').attr('action', 'AddApplication');
                $('#updateApplicationForm').submit();
            }
            break;
        case "Activate":
            if ($('#applicationNameModal').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Select Application.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateApplicationForm').attr('action', 'ActivateApplication');
                $('#updateApplicationForm').append('<input type="hidden" name="applicationName" value="' + $('#applicationNameModal option:selected').text() + '"/>');
                $('#updateApplicationForm').submit();
            }
            break;
        case "Update":
            if ($('#applicationEdit').val() === '' || $('#appId').val() === '' || $('#oldAppName').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Application Name.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateApplicationForm').attr('action', 'UpdateApplication');
                $('#updateApplicationForm').submit();
            }
            break;
    }
}

function populateSampleCustomerData(empCode) {
    if ($('#sampleIndustry').val() === '' || $('#sampleIndustry').val() === null) {
        $.alert({
            title: 'Select Fields',
            content: 'Select Industry.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $('#modal-title-Customer').text('Add Customer(s) in ' + $('#sampleIndustry option:selected').text() + ' Industry');
        getCustomer('');
        loadEmployeeDetailFromCEM(empCode, 1);
        $('#UpdateCustomer').modal('show');
    }
}

function getCustomerDept() {
    $('#sampleDepartment option').remove();
    $('#sampleEquipment option').remove();
    $('#sampleApplication').val('').change();
    $('#sampleProduct').val('').change();
    $('#sampleTankNo option').remove();
    if ($('#sampleIndustry').val() === '' || $('#sampleIndustry').val() === null) {
        $.alert({
            title: 'Select Fields',
            content: 'Select Industry.',
            type: 'red',
            typeAnimated: true
        });
    } else if ($('#sampleCustomer').val() === '' || $('#sampleCustomer').val() === null) {
        $.alert({
            title: 'Select Fields',
            content: 'Select Customer.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/GetCustomerDept',
            data: {
                Customer: $('#sampleCustomer option:selected').val()
            },
            dataType: 'JSON',
            success: function (data) {
                $('#sampleDepartment').append($('<option>', {value: ''}).text("Select Department *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#sampleDepartment').append($('<option>', {value: val.departmentId}).text(val.departmentName));
                    });
                } else {
                    $('#sampleDepartment').append($('<option disabled>', {value: null}).text(data + $('#sampleCustomer option:selected').text()));
                }
            },
            error: function (data) {
                $('#sampleDepartment').append($('<option disabled>', {value: null}).text("Error occured while fetching Departments. Try refreshing the window."));
            }
        });
    }
}

function getCustomerEquipment() {
    $('#sampleEquipment option').remove();
    $.ajax({
        type: 'GET',
        url: '/ServoCMP/Tse/GetCustomerEquipment',
        data: {
            Customer: $('#sampleCustomer option:selected').val()
        },
        dataType: 'JSON',
        success: function (data) {
            $('#sampleEquipment').append($('<option>', {value: ''}).text("Select Equipment *"));
            if (data.constructor === Array) {
                jQuery.each(data, function (i, val) {
                    $('#sampleEquipment').append($('<option>', {value: val.equipmentId}).text(val.equipmentName));
                });
            } else {
                $('#sampleEquipment').append($('<option disabled>', {value: null}).text(data + $('#sampleCustomer option:selected').text()));
            }
        },
        error: function (data) {
            $('#sampleEquipment').append($('<option disabled>', {value: null}).text("Error Occured While fetching Equipment. Try refreshing the window"));
        }
    });
}

function getCustomer(indId) {
    if (indId === '') {
        $('#CustomerModal option').remove();
    } else
        $('#sampleCustomer option').remove();
    $('#sampleDepartment option').remove();
    $('#sampleApplication').val('').change();
    $('#sampleEquipment option').remove();
    $('#sampleProduct').val('').change();
    $('#sampleTankNo option').remove();
    $('#customerIndustry').val($('#sampleIndustry').val());
    $('#customerIndustryName').val($('#sampleIndustry option:selected').text());
    $.ajax({
        type: 'GET',
        url: '/ServoCMP/Tse/GetCustomers',
        dataType: 'json',
        data: {
            Industry: indId,
            queryBy: 'Tse'
        },
        success: function (data) {
            $('#sampleCustomer').append($('<option>', {value: ''}).text("Select Customer *"));
            if (data.constructor === Array) {
                jQuery.each(data, function (i, val) {
                    if (indId === '') {
                        $('#CustomerModal').append($('<option>', {value: val.customerName}).text(val.customerName));
                    } else
                        $('#sampleCustomer').append($('<option>', {value: val.customerId}).text(val.customerName));
                });
            } else {
                if (indId === '') {
                    $('#CustomerModal').append($('<option disabled>', {value: null}).text(data));
                } else
                    $('#sampleCustomer').append($('<option disabled>', {value: null}).text(data));
            }
        },
        error: function (data) {
            if (indId === '') {
                $('#CustomerModal').append($('<option disabled>', {value: null}).text("Error while fetching Customer"));
            } else
                $('#sampleCustomer').append($('<option disabled>', {value: null}).text("Error while fetching Customer"));
        }
    });
}

function addCustomer() {
    if ($('#sampleIndustry').val() === '' ||
            $('#CustomerModal').val() === '' ||
            $('#EmpCodeLoadedModal').val() === '' ||
            $('#EmpNameModal').val() === '' ||
            $('#EmpEmailModal').val() === '' ||
            $('#CtrlEmpCodeModal').val() === '' ||
            $('#CtrlEmpNameModal').val() === '' ||
            $('#CtrlEmpEmailModal').val() === '') {
        $.alert({
            title: 'Select Fields',
            content: 'Select Industry and Enter Customer Name(s).',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $("#customerSubmit").prop("disabled", true);
        $.ajax({
            type: 'POST',
            url: '/ServoCMP/AddCustomer',
            dataType: 'json',
            data: {
                IndustryModal: $('#sampleIndustry').val(),
                Customers: $('#CustomerModal').val(),
                EmpCodeLoaded: $('#EmpCodeLoadedModal').val(),
                EmpNameModal: $('#EmpNameModal').val(),
                EmpEmailModal: $('#EmpEmailModal').val(),
                CtrlEmpCodeModal: $('#CtrlEmpCodeModal').val(),
                CtrlEmpNameModal: $('#CtrlEmpNameModal').val(),
                CtrlEmpEmailModal: $('#CtrlEmpEmailModal').val()
            },
            success: function (data) {
                showResponseDialog(data);
            },
            error: function (data) {
                showResponseDialog(data);
            }
        });
    }

}

function sampleSubmit(btnObject) {
    if ($('#sampleIndustry').val() === '' ||
            $('#sampleCustomerModal').val() === '' ||
            $('#sampleEmpCodeModal').val() === '' ||
            $('#sampleEmpCodeLoadedModal').val() === '' ||
            $('#sampleEmpNameModal').val() === '' ||
            $('#sampleEmpEmailModal').val() === '' ||
            $('#sampleCtrlEmpCodeModal').val() === '' ||
            $('#sampleCtrlEmpNameModal').val() === '' ||
            $('#sampleCtrlEmpEmailModal').val() === '') {
        $.alert({
            title: 'Select Fields',
            content: 'Select Industry and Enter Customer Name(s).',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $.ajax({
            type: 'POST',
            url: '/ServoCMP/AddCustomer'
        });
        $('#updateSampleForm').append('<input type="hidden" name="sampleIndustryModal" value="' + $('#sampleIndustry option:selected').val() + '"/>');
        $('#updateSampleForm').attr('action', '/ServoCMP/AddCustomer');
        $('#updateSampleForm').submit();
    }
}

function AddUser(operation) {
    $('#modal-title-User').text(operation);
    $('#userSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#userSubmit').val(operation.substr(0, operation.indexOf(' ')));
//    if (operation.includes('Add')) {
//
//    } else {
//
//    }
    $('#UpdateUser').modal('show');
}

function AddLabUser(operation) {
    $('#modal-title-User').text(operation);
    $('#userSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#userSubmit').val(operation.substr(0, operation.indexOf(' ')));
//    if (operation.includes('Add')) {
//
//    } else {
//
//    }
    $('#UpdateUser').modal('show');
}

function getEmployee(event) {
    $('#roleId').val($(event).find('option:selected').attr('data-roleId'));
    $('#roleName').val($(event).find('option:selected').text());
    $('#locCode').val($(event).find('option:selected').attr('data-locCode'));
    $('#empCode option').remove();
    if ($('#fAreaCode').val() === '') {
        $.alert({
            title: 'Select Role',
            content: 'Select Role to Get Employees.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/GetRoleEmployee',
            dataType: 'JSON',
            data: {
                fCode: $('#fAreaCode option:selected').val(),
                locCode: $(event).find('option:selected').attr('data-locCode')
            },
            success: function (data) {
                $('#empCode').append($('<option>', {value: ''}).text("Select Employee *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#empCode').append($('<option data-empName="' + val.empName + '" data-empEmail="' + val.empEmail + '" data-ctrlEmpCode="' + val.ctrlEmpCode + '" value="' + val.empCode + '">', {value: val.empCode}).text(val.empCode + ' (' + val.empName + ')'));
                    });
                } else {
                    $('#empCode').append($('<option disabled>', {value: null}).text(data));
                }
            },
            error: function () {
                $('#empCode').append($('<option disabled>', {value: null}).text("Error Ocurrend while fetching Employees"));
            }
        });
    }
}

function popEmployeeData(event) {
    var empName = $(event).find('option:selected').attr('data-empName');
    var empEmail = $(event).find('option:selected').attr('data-empEmail');
    var ctrlEmpCode = $(event).find('option:selected').attr('data-ctrlEmpCode');
    if (empName === '' || empName === null || empName === 'undefined') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Selected Employee Does not have a valid Employee Name.',
            type: 'red',
            typeAnimated: true
        });
        dropDetails();
    } else if (empEmail === '' || empEmail === null || empEmail === 'undefined') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Selected Employee Does not have a valid Employee Email or Selected Employee is a Staff Person.',
            type: 'red',
            typeAnimated: true
        });
        dropDetails();
    } else if (ctrlEmpCode === '' || ctrlEmpCode === null || ctrlEmpCode === 'undefined') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Selected Employee Does not have a Controlling Officer assigned. Kindly Choose Other Employee.',
            type: 'red',
            typeAnimated: true
        });
        dropDetails();
    } else {
        $('#empName').val(empName);
        $('#empEmail').val(empEmail);
        $('#ctrlEmpCode').val(ctrlEmpCode);
        $('#ctrlEmpName').val('');
        $('#ctrlEmpEmail').val('');
        $.ajax({
            async: false,
            type: 'GET',
            url: '/ServoCMP/GetEmployee',
            dataType: 'JSON',
            data: {
                empCode: ctrlEmpCode
            },
            success: function (data) {
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#ctrlEmpName').val(val.ctrlEmpName);
                        $('#ctrlEmpEmail').val(val.ctrlEmpEmail);
                    });
                } else {
                    console.log("Nada");
                }

            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    }
}

function dropDetails() {
    $('#empName').val('');
    $('#empEmail').val('');
    $('#ctrlEmpCode').val('');
    $('#ctrlEmpName').val('');
    $('#ctrlEmpEmail').val('');
}

////////////////////////////////////////////////////  Tse Admin User Master //////////////////////////////////////////////////

function submitUser(event, url) {
    var $btn = $(event).button('loading');
    if ($('#roleId').val() === '') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Select Role.',
            type: 'red',
            typeAnimated: true
        });
    } else if ($('#empCode').val() === '') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Select Employee.',
            type: 'red',
            typeAnimated: true
        });
    } else if ($('#empName').val() === '') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Selected Employee Does not have a valid Employee Name.',
            type: 'red',
            typeAnimated: true
        });
    } else if ($('#empEmail').val() === '') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Selected Employee Does not have a valid Employee Email or Selected Employee is a Staff Person.',
            type: 'red',
            typeAnimated: true
        });
    } else if ($('#ctrlEmpCode').val() === '') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Selected Employee Does not have a Controlling Officer assigned. Kindly Choose Other Employee.',
            type: 'red',
            typeAnimated: true
        });
    } else if ($('#ctrlEmpName').val() === '') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Selected Employee"s Controlling Officer does not have a valid Name. Kindly Choose Other Employee.',
            type: 'red',
            typeAnimated: true
        });
    } else if ($('#ctrlEmpEmail').val() === '') {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'Selected Employee"s controlling Officer does not have a valid EmailId. Kindly Choose Other Employee.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        if (url === 1) {
            $('#updateUserForm').attr('action', '/ServoCMP/LabAdmin/AddUser');
            $('#updateUserForm').submit();
        } else {
            $('#updateUserForm').attr('action', '/ServoCMP/TseAdmin/AddUser');
            $('#updateUserForm').submit();
        }

    }
    $btn.button('reset');
}

function changeRole(operation) {
    $('#modal-title-changeRole').text(operation);
    $('#changeRoleSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#changeRoleSubmit').val(operation.substr(0, operation.indexOf(' ')));
    $('#changeRoles').modal('show');
}

function getRole(event) {
    document.getElementById('changeRole').selectedIndex = [...document.getElementById("changeRole").options].findIndex(option => option.text === $(event).find('option:selected').attr('data-roleName'));
    if (document.getElementById('changeRole').selectedIndex === [...document.getElementById("changeRole").options].findIndex(option => option.text === $(event).find('option:selected').attr('data-roleName'))) {
        $('#changeRoleSubmit').prop('disabled', true);
    } else {
        $('#changeRoleSubmit').prop('disabled', false);
    }
}

function EDButton(event) {
    $('#changeRoleName').val($('#changeRole option:selected').text());
    if (document.getElementById('changeRole').selectedIndex === [...document.getElementById("changeRole").options].findIndex(option => option.text === $('#changeEmpCode').find('option:selected').attr('data-roleName'))) {
        $('#changeRoleSubmit').prop('disabled', true);
    } else {
        $('#changeRoleSubmit').prop('disabled', false);
    }
}

function removeUser(empCode, empName, event, roleId) {
    if (typeof empName === 'undefined') {
        $('#inactiveEmpCode option').remove();
        $('#modal-title-inactiveEmployee').text("Inactive Employees");
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/TseAdmin/AllDeactiveUser',
            dataType: 'JSON',
            success: function (data) {
                $('#inactiveEmpCode').append($('<option data-roleId="">', {value: ''}).text("Select Employee *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#inactiveEmpCode').append($('<option data-roleId = "' + val.mstRole.roleId + '" value="' + val.empCode + '">', {value: val.empCode}).text(val.empCode + ' (' + val.empName + ')'));
                    });
                } else {
                    $('#empCode').append($('<option disabled>', {value: null}).text(data));
                }
            },
            error: function (data) {
            }
        });
        $('#inactiveEmployee').modal('show');
    } else {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to Remove Employee "<b>' + empName + ' (' + empCode + ')</b>" from ServoCMP ?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $('#oldEmpRole').val(roleId);
                        $(event).parent('form').submit();
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made to Employee ' + empName + ' (' + empCode + ')',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    }
}

function removeLabUser(empCode, empName, event, roleId) {
    if (typeof empName === 'undefined') {
        $('#inactiveEmpCode option').remove();
        $('#modal-title-inactiveEmployee').text("Inactive Employees");
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/LabAdmin/AllDeactiveUser',
            dataType: 'JSON',
            success: function (data) {
                $('#inactiveEmpCode').append($('<option data-roleId="">', {value: ''}).text("Select Employee *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#inactiveEmpCode').append($('<option data-roleId = "' + val.mstRole.roleId + '" value="' + val.empCode + '">', {value: val.empCode}).text(val.empCode + ' (' + val.empName + ')'));
                    });
                } else {
                    $('#empCode').append($('<option disabled>', {value: null}).text(data));
                }
            },
            error: function (data) {
            }
        });
        $('#inactiveEmployee').modal('show');
    } else {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to Remove Employee "<b>' + empName + ' (' + empCode + ')</b>" from ServoCMP ?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $('#oldEmpRole').val(roleId);
                        $(event).parent('form').submit();
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made to Employee ' + empName + ' (' + empCode + ')',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    }
}

function populateSampleApplicationData(operation) {
    $.alert({
        title: 'Insufficient Rights',
        content: '"' + operation + '" rights is reserved with Ho Admin.',
        type: 'red',
        typeAnimated: true
    });
}


function getProduct()
{
    if ($('#prodId').val() !== '' && $('#prodId').val() !== null && $('#prodId').val().length > 2) {
        $('#sampleProduct option').remove();
        $.ajax({
            type: 'post',
            url: '/ServoCMP/Tse/getProduct',
            dataType: 'JSON',
            data: {
                prodId: $('#prodId').val()
            },
            success: function (data) {
                //$('#sampleProduct').append($('<option>', {value: ''}).text("Enter New Tank No *"));
                if (data.constructor === Array) {

                    $('#sampleProduct').append($('<option>', {value: "OMC"}).text(" ( OMC Product )"));
                    jQuery.each(data, function (i, val) {
                        $('#sampleProduct').append($('<option>', {value: val.proId}).text(val.proId + " (" + val.proName + ")"));
                    });
                    $("#sampleProduct").val('').change()
                } else {
                    $('#sampleProduct').append($('<option>', {value: null}).text(data));
                }
            },
            error: function (data) {
                $('#sampleProduct').append($('<option>', {value: null}).text("Error Ocurred while fetching Product"));
            }
        });
    } else
        return false;
}

function getTank(operation) {
    if ($('#sampleProduct').val() == "OMC") {
        $('#productNameField').addClass('hide');
        $('#productNameField1').removeClass('hide');
        $('#productNameField2').removeClass('hide');
        $('#sampleFreq').val("0").removeAttr('required').attr('readonly', true);
        $('#lastRow').addClass('hide');
        $('#tankPreviousDate').removeAttr('required').attr('readonly', true);
        $('#sampleNextDate').removeAttr('required').attr('readonly', true);
        $('#sampleLastOilChange').removeAttr('required').attr('readonly', true);
        $('#oneTimeCheckbox').prop('checked', true);
    } else {
        $('#productNameField').removeClass('hide');
        $('#productNameField1').addClass('hide');
        $('#productNameField2').addClass('hide');
        $('#sampleFreq').attr('required', true).attr('readonly', false);
        $('#lastRow').removeClass('hide');
        $('#tankPreviousDate').attr('required', true).attr('readonly', false);
        $('#tankNextDate').attr('required', true).attr('readonly', false);
        $('#sampleLastOilChange').attr('required', true).attr('readonly', false);
        $('#oneTimeCheckbox').prop('checked', false);
    }

    if ($('#sampleIndustry').val() !== '' && $('#sampleIndustry').val() !== null &&
            $('#sampleCustomer').val() !== '' && $('#sampleCustomer').val() !== null &&
            $('#sampleDepartment').val() !== '' && $('#sampleDepartment').val() !== null &&
            $('#sampleApplication').val() !== '' && $('#sampleApplication').val() !== null &&
            $('#sampleEquipment').val() !== '' && $('#sampleEquipment').val() !== null &&
            $('#sampleProduct').val() !== '' && $('#sampleProduct').val() !== null) {
        $('#sampleTankNo option').remove();
        $.ajax({
            async: false,
            type: 'GET',
            url: '/ServoCMP/Tse/GetTank',
            dataType: 'JSON',
            data: {
                industry: $('#sampleIndustry').val(),
                customer: $('#sampleCustomer').val(),
                department: $('#sampleDepartment').val(),
                application: $('#sampleApplication').val(),
                equipment: $('#sampleEquipment').val(),
                product: $('#sampleProduct').val()
            },
            success: function (data) {
                $('#sampleTankNo').append($('<option>', {value: ''}).text("Enter New Tank No *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#sampleTankNo').append($('<option>', {value: val.tankNo}).text(val.tankNo + " (" + val.tankDesc + ")"));
                    });
                } else {
                    $('#sampleTankNo').append($('<option>', {value: null}).text(data));
                }
            },
            error: function (data) {
                $('#sampleTankNo').append($('<option>', {value: null}).text("Error Ocurred while fetching Tank No"));
            }
        });
    }
    if ($('#sampleProduct').val() !== '' && $('#sampleProduct').val() !== null && $('#sampleProduct').val() != 'OMC') {
        $.ajax({
            async: false,
            url: '/ServoCMP/Tse/getProductCategory?prodId=' + $('#sampleProduct').val(),
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
                if (typeof data == "object") {
//                    console.log(data);
                    $('#exist-test-dropdown').empty();
                    $.each(data, function (key, val) {
                        $('#exist-test-dropdown').append("<option value=" + val.col2 + " data-qty=" + val.col4 + " data-mandatory='1' selected>" + val.col3 + "</option>").trigger('change');
                    });
                } else {
                    $.alert({
                        title: 'No Records Found',
                        content: 'No Category mapping is there against this product. Note: Kindly advise TSE admin to add category mapping for the respective product',
                        type: 'red',
                        typeAnimated: true
                    });
                }
//                showRawResponseDialog(data);
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    }
}

//function submitTank() {
//    if ($('#sampleIndustry option:selected').val() !== '' && $('#sampleIndustry option:selected').val() !== null &&
//            $('#sampleCustomer option:selected').val() !== '' && $('#sampleCustomer option:selected').val() !== null &&
//            $('#sampleDepartment option:selected').val() !== '' && $('#sampleDepartment option:selected').val() !== null &&
//            $('#sampleApplication option:selected').val() !== '' && $('#sampleApplication option:selected').val() !== null &&
//            $('#sampleEquipment option:selected').val() !== '' && $('#sampleEquipment option:selected').val() !== null &&
//            $('#sampleProduct option:selected').val() !== '' && $('#sampleProduct option:selected').val() !== null &&
//            $('#sampleTankNo option:selected').val() !== '' && $('#sampleTankNo option:selected').val() !== null &&
//            $('#sampleTankDesc').val() !== '' && $('#sampleTankDesc').val() !== null &&
//            $('#exist-test-dropdown').val() !== '' && $('#exist-test-dropdown').val() !== null &&
//            $('#tankPreviousDate').val() !== '' && $('#tankPreviousDate').val() !== null &&
//            $('#sampleFreq').val() !== '' && $('#sampleFreq').val() !== null &&
//            $('#sampleNextDate').val() !== '' && $('#sampleNextDate').val() !== null &&
//            $('#sampleAppDesc').val() !== '' && $('#sampleAppDesc').val() !== null &&
//            $('#sampleCapactiy').val() !== '' && $('#sampleCapactiy').val() !== null &&
//            $('#sampleLastOilChange').val() !== '' && $('#sampleLastOilChange').val() !== null) {
//        $('#customerIndustry').val($('#sampleIndustry option:selected').val());
//        $.ajax({
//            type: 'POST',
//            url: '/ServoCMP/AddNewTank',
//            data: $('#addTank').serialize(),
//            success: function (data) {
//            },
//            error: function (data) {
//
//            }
//        });
//    } else {
//        $.alert({
//            title: 'Select All Fields',
//            content: 'Select or Enter data in all fields.',
//            type: 'red',
//            typeAnimated: true
//        });
//    }
//}

function resetFields() {
//    $('#sampleIndustry').val('').change();
//    $('#sampleCustomer').val('').change();
//    $('#sampleDepartment').val('').change();
//    $('#sampleApplication').val('').change();
//    $('#sampleEquipment').val('').change();
//    $('#sampleProduct').val('').change();
//    $('#sampleTankNo').val('').change();
//    $('#sampleTankDesc').val('');
//    $('#sampleAppDesc').val('');
//    $('#sampleCapactiy').val('');
//    $('#sampleLastOilChange').val('');
//    $('#samplePreDate').val('');
//    $('#sampleFreq').val('');
//    $('#samplePreDate').val('');
    location.reload();
}

function addMonth() {
    var date = $('#tankPreviousDate').val().split("-");
    var nextDate = new Date(date[2], parseInt(date[1]), date[0]);
    nextDate.setMonth(nextDate.getMonth() + parseInt($('#sampleFreq').val()));
    if (!isNaN(nextDate.getDate())) {
        $('#sampleNextDate').val(nextDate.getDate() + "-" + nextDate.getMonth() + "-" + nextDate.getFullYear());
    }
}

function nextSampleDate() {
    if ($('#sampleNextDate').val() === '') {
        $.alert({
            title: 'Select Field',
            content: 'Enter Previous Date and Sample Frequency to get Next Sample Date.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $.alert({
            title: 'Select Field',
            content: 'Next Sample Date Cannot be changed as it has been set according to Previous Sample Date and Sample frequency in months',
            type: 'red',
            typeAnimated: true
        });
    }
}

function submitCustomer() {
    if ($('#sampleIndustry option:selected').val() === '' || $('#sampleIndustry option:selected').val() === null) {
        $.alert({
            title: 'Select Industry',
            content: 'Select Industry First.',
            type: 'red',
            typeAnimated: true
        });
    } else if ($('#CustomerModal option:selected').val() === '' || $('#CustomerModal option:selected').val() === null || typeof $('#CustomerModal option:selected').val() === 'undefined') {
        $.alert({
            title: 'Enter Details !!!',
            content: 'Enter Customer Name and Press Enter key to continue.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $('#customerIndustry').val($('#sampleIndustry option:selected').val());
        $('#customerIndustryName').val($('#sampleIndustry option:selected').text());
        $.ajax({
            type: 'POST',
            url: 'AddCustomers',
            data: $('#updateCustomerForm').serialize(),
            dataType: 'JSON',
            success: function (data) {
                showRawResponseDialog(data);
                $('#UpdateCustomer').modal('hide');
                getCustomer($('#sampleIndustry option:selected').val());
            },
            error: function (data) {
                showRawResponseDialog(data);
                $('#UpdateCustomer').modal('hide');
            }
        });
    }
}

//function populateSampleCustomerData(empCode) {
//    if ($('#sampleIndustry').val() === '' || $('#sampleIndustry').val() === null) {
//        $.alert({
//            title: 'Select Fields',
//            content: 'Select Industry.',
//            type: 'red',
//            typeAnimated: true
//        });
//    } else {
//        $('#modal-title-Customer').text('Add Customer(s) in ' + $('#sampleIndustry option:selected').text() + ' Industry');
//        getCustomer('');
//        loadEmployeeDetailFromCEM(empCode, 1);
//        $('#UpdateCustomer').modal('show');
//    }
//}

function populateSampleDepartmentData() {
    if ($('#sampleIndustry').val() === '' || $('#sampleIndustry option:selected').val() === null
            || $('#sampleCustomer option:selected').val() === '' || $('#sampleCustomer option:selected').val() === null) {
        $.alert({
            title: 'Select Fields',
            content: 'Select Industry and Customer to Add New Department.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $('#deptTable tbody tr').remove();
        addRowDept();
        $('#modal-title-Department').text('Add Department(s) for "' + $('#sampleCustomer option:selected').text() + '" Customer');
        $('#deptCustomer').val($('#sampleCustomer option:selected').val());
        $('#deptCustomerName').val($('#sampleCustomer option:selected').text());
        $('#UpdateDepartment').modal('show');
    }
}

function addRowDept() {
    $('#deptTable').append('<tr><td style="padding-top: 20px"><span class="glyphicon glyphicon-trash" onclick="return delRowDept(this);"></span></td><td><input name="deptName" placeholder="Enter Department Name *" type="text" style="width: 100%" class="form-control" required/></td><td><input name="hodName" placeholder="Enter Hod Name *" type="text" style="width: 100%" class="form-control" required/></td><td><input name="hodEmail" placeholder="Enter Hod Email Id *" type="text" style="width: 100%" class="form-control isEmail" required/></td><td><input name="hodContact" placeholder="Enter Hod Contact *" type="text" style="width: 100%" class="form-control" required/></td></tr>');
}

function delRowDept(event) {
    $(event).closest('tr').remove();
}

function showRawResponseDialog(data) {
    $('#modal-title').text(data.modalTitle);
    $('#modalMessage').html(data.modalMessage);
    $('#fileMsg').html(data.fileMsg);
    $('#mailMsg').html(data.mailMsg);
    $('#modalMessage').addClass(data.msgClass);
    $('#fileMsg').addClass(data.filemsgClass);
    $('#mailMsg').addClass(data.mailMsgClass);
    $('#responseDialog').modal('show');
}

function refreshDropdowns() {
    var industry = $('#sampleIndustry option:selected').val();
    var customer = $('#sampleCustomer option:selected').val();
    var department = $('#sampleDepartment option:selected').val();
    var app = $('#sampleApplication option:selected').val();
    var product = $('#sampleProduct option:selected').val();
    if (industry !== '' || industry !== null || typeof industry != 'undefined') {
        getCustomer(industry);
        if (customer !== '' || customer !== null || typeof customer != 'undefined') {
//            getCustomerDept(customer);
            getCustomerDept();
            if (department !== '' || department !== null || typeof department != 'undefined') {
                if (app !== '' || app !== null || typeof app != 'undefined') {
                    getCustomerEquipment();
                    //if()
                }
            }
        }
    }
}

function AddProduct(operation, proId, proName) {
    $('#modal-title-Product').text(operation);
    $('#productSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#productSubmit').val(operation.substr(0, operation.indexOf(' ')));
    if (operation.includes('Add')) {
        $('#productId').val('');
        $('#productNameInput').show();
        $('#productCatModal').hide();
        $('.hideActive').show();
        $('#productNameModal').hide();
        $('.hideProductEdit').hide();
    } else {
        if (operation.includes('Update')) {
            $('#modal-title-Product').text(operation.substr(0, operation.indexOf(' ')) + ' "' + proName + '" Product');
            $('#proId').val(proId);
            $('#oldProName').val(proName);
            $('#productEdit').val(proName);
            $('#productNameModal').hide();
            $('#productCatModal').show();
            $('.hideActive').hide();
            $('#productNameInput').hide();
            $('.hideProductEdit').show();
            $('#productEdit').show();
        } else {
            $('#productNameModal').show();
            $('.hideActive').hide();
            $('#productNameInput').hide();
            $('#productCatModal').hide();
            $('.hideProductEdit').hide();
            $('#productEdit').hide();
        }
    }
    $('#UpdateProduct').modal('show');
}

function removeProduct(proId, proName, event) {
    if (typeof proName === 'undefined') {
        $('#modal-title-Product').text("Removed Products");
        $('#productSubmit').text("Activate");
        $('#productSubmit').val("Activate");
        $('#productNameModal option').remove();
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/TseAdmin/AllDeactiveProduct',
            dataType: 'json',
            success: function (data) {
                $('#productNameModal').append($('<option>', {value: ''}).text("Select Product *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#productNameModal').append($('<option>', {value: val.proId}).text(val.proName));
                    });
                } else {
                    $('#productNameModal').append($('<option disabled>', {value: null}).text(data));
                }
            },
            error: function (data) {
                $('#productNameModal').append($('<option disabled>', {value: null}).text("Error while fetching Products"));
            }
        });
        $('#productNameModal').show();
        $('#productNameInput').hide();
        $('#productCatModal').hide();
        $('.hideActive').hide();
        $('.hideProductEdit').hide();
        $('#productEdit').hide();
        $('#UpdateProduct').modal('show');
    } else {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to Remove ' + proName + ' Product?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $(event).parent('form').submit();
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made to ' + proName + ' Product',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    }
}

function submitProduct(btnObject) {
    switch (btnObject.value) {
        case "Add":
            if ($('#productNameInput').val() === '' || $('#productNameInput').val() === null) {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Product Name',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateProductForm').attr('action', 'AddProduct');
                $('#updateProductForm').submit();
            }
            break;
        case "Activate":
            if ($('#productNameModal').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Select Product.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateProductForm').attr('action', 'ActivateProduct');
                $('#updateProductForm').append('<input type="hidden" name="productName" value="' + $('#productNameModal option:selected').text() + '"/>');
                $('#updateProductForm').submit();
            }
            break;
        case "Update":
            if ($('#productEdit').val() === '' || $('#proId').val() === '' || $('#oldproName').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Product Name.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateProductForm').attr('action', 'UpdateProduct');
                $('#updateProductForm').submit();
            }
            break;
    }
}

function populateSampleEquipmentData() {
    if ($('#sampleIndustry').val() === '' || $('#sampleIndustry option:selected').val() === null
            || $('#sampleCustomer option:selected').val() === '' || $('#sampleCustomer option:selected').val() === null) {
        $.alert({
            title: 'Select Fields',
            content: 'Select Industry and Customer to Add New Equipment.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $('#modal-title-Equipment').text('Add Equipment(s) for "' + $('#sampleCustomer option:selected').text() + '" Customer');
        $('#equipCustomer').val($('#sampleCustomer option:selected').val());
        $('#equipCustomerName').val($('#sampleCustomer option:selected').text());
        $('#UpdateEquipment').modal('show');
    }
}

function submitTank() {
    if ($('#sampleProduct').val() != "OMC") {
        if ($('#sampleFreq').val() == 0) {
            $.alert({
                title: 'Invalid Value',
                content: 'Sample Frequency cannot be 0 for CMP Sample',
                type: 'red',
                typeAnimated: true
            });
            return false;
        }
    }
    if (document.getElementById('addTank').checkValidity()) {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to Add Tank?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $.ajax({
                            url: 'AddNewTank',
                            type: 'POST',
                            data: $('#addTank').serialize(),
                            dataType: 'JSON',
                            success: function (data) {
                                showRawResponseDialog(data);
                                getTank();
                            },
                            error: function (data) {
                                showRawResponseDialog(data);
                            }
                        });
                        $('#sampleTankDesc').val('');
                        $('#tankPreviousDate').val('');
                        $('#sampleFreq').val('');
                        $('#sampldatepickereNextDate').val('');
                        $('#sampleAppDesc').val('');
                        $('#sampleCapactiy').val('');
                        $('#sampleLastOilChange').val('');
                        $('#sampleTankNo').val('').change();
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    } else {
        $.alert({
            title: 'Incomplete Details !!!',
            content: 'All Fields are Required.',
            type: 'red',
            typeAnimated: true
        });
    }
}


function getTankSummary(tankDT) {
    console.log($('#sampleCustomer option:selected').val());
    if ($('#sampleCustomer option:selected').val() === "" || typeof $('#sampleCustomer option:selected').val() === "undefined") {
        $.alert({
            title: 'Select Customer',
            content: 'Please Select either from Customer to Get Summary.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        tankDT.clear().draw(false);
        $.ajax({
            type: "GET",
            url: "/ServoCMP/Tse/GetTank",
            data: {
                customer: $('#sampleCustomer option:selected').val(),
                equipment: $('#sampleEquipment option:selected').val(),
                tank: $('#sampleTankNo option:selected').val()
            },
            dataType: "JSON",
            success: function (data) {
//                var a = '';
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
//                        console.log(val);
                        newRow = tankDT.row.add([
                            val.deptName,
                            val.applName,
                            val.equipName,
                            val.proName,
                            val.tankNo,
                            val.tankDesc,
                            val.strPrevDate,
                            val.strNxtDate,
                            val.applDesc,
                            (val.samplingNo > 0 ? ' <a class="viewTank" data-toggle="tooltip"  data-placement="top" title="View Tank"><i class="glyphicon glyphicon-eye-open">"</i></a> | <a href="#" onclick="cannotRemove();" data-toggle="tooltip"  data-placement="top" title="Delete tank"><span class="glyphicon glyphicon-ban-circle"></span><a/></td></tr>' : ' <a class="viewTank" data-toggle="tooltip"  data-placement="top" title="View Tank"><i class="glyphicon glyphicon-eye-open">"</i></a> | <a href="#" onclick="removeTank(' + val.tankId + ');"><span class="glyphicon glyphicon-trash"></span><a/>')
                        ]).draw(true).node();
                        //<a href="/ServoCMP/Tse/GetSamplePendingAtTSE?tank=' + val.tankId + '&do=create&csrftoken=' + $('[name="csrf-token"]').attr('content') + '" class="" data-toggle="tooltip"  data-placement="top" title="Create One-Time Sample"><i class="glyphicon glyphicon-pencil">"</i></a> |
                        $(newRow).data('strNxtDate', val.strNxtDate);
                        $(newRow).data('strPrevDate', val.strPrevDate);
                        $(newRow).data('capacity', val.capacity);
                        $(newRow).data('tankNo', val.tankNo);
                        $(newRow).data('updatedBy', val.updatedBy);
                        $(newRow).data('updatedDateTime', val.updatedDateTime);
                        $(newRow).data('tankDesc', val.tankDesc);
                        $(newRow).data('applDesc', val.applDesc);
                        $(newRow).data('lastOilChange', val.lastOilChange);
                        $(newRow).data('sampleFreq', val.sampleFreq ? val.sampleFreq : '0');
//                        var newRow = $('<tr data-strNxtDate=' + val.strNxtDate + '><td></td><td></td><td></td><td></td><td class="text-center"></td><td></td><td class="text-center"></td><td class="text-center"></td><td></td><td class="text-center">' + (val.samplingNo > 0 ? '<a href="#" onclick="cannotRemove();"><span class="glyphicon glyphicon-ban-circle"></span><a/></td></tr>' : '<a href="#" onclick="removeTank(' + val.tankId + ');"><span class="glyphicon glyphicon-trash"></span><a/></td></tr>'));
//                        newRow.children().eq(0).text(val.deptName);
//                        newRow.children().eq(1).text(val.applName);
//                        newRow.children().eq(2).text(val.equipName);
//                        newRow.children().eq(3).text(val.proName);
//                        newRow.children().eq(4).text(val.tankNo);
//                        newRow.children().eq(5).text(val.tankDesc);
//                        newRow.children().eq(6).text(val.strPrevDate);
//                        newRow.children().eq(7).text(val.strNxtDate);
//                        newRow.children().eq(8).text(val.applDesc);
//                        newRow.appendTo($('#tankTable tbody'));
                    });
//                    $('#tankTable').DataTable({"order": []});
                } else {
//                    $('<tr><td colspan="10" class="text-center">' + data + '</td></tr>').appendTo($('#tankTable tbody'));
                    console.log('No data');
                }
            },
            error: function (data) {
//                $('<tr><td colspan="10" class="text-center">' + data + '</td></tr>').appendTo($('#tankTable tbody'));
                $.alert({
                    title: 'Error',
                    content: 'Cannot fetch data.',
                    type: 'red',
                    typeAnimated: true
                });
            }
        });
        $('#tankTableDiv').show();
    }
}

$(document).on('click', '#tankTable > tbody > tr > td > a.createOTSample', function () {
    $.ajax({
        url: 'xxx',
        type: 'POST',
        dataType: 'JSON',
        success: function (data) {
            showRawResponseDialog(data);
        },
        error: function (error) {
            console.log(error.responseText);
        }
    });
});
$(document).on('click', '#tankTable > tbody > tr > td > a.viewTank', function () {
    var tr = $(this).parents('tr');
    $('input[name="TankNo"]').val(tr.find('td:nth-child(5)').text());
    $('input[name="TankDesc"]').val(tr.find('td:nth-child(6)').text());
    $('input[name="CapaTank"]').val(tr.data('capacity'));
    $('input[name="AppDesc"]').val(tr.find('td:nth-child(9)').text());
    $('input[name="PrevDate"]').val(tr.data('strNxtDate'));
    $('input[name="NextDate"]').val(tr.data('strNxtDate'));
    $('input[name="Freq"]').val(tr.data('sampleFreq'));
    $('input[name="OilChan"]').val(tr.data('lastOilChange'));
    $('#IndsAtr').text($('#sampleIndustry option:selected').text());
    $('#CustAtr').text($('#sampleCustomer option:selected').text());
    $('#DeptAtr').text(tr.find('td:nth-child(1)').text());
    $('#ApplAtr').text(tr.find('td:nth-child(2)').text());
    $('#EquipAtr').text(tr.find('td:nth-child(3)').text());
    $('#ProdAtr').text(tr.find('td:nth-child(4)').text());
    var params = $('#exist-test-dropdown').parent().html();
    $('#paramss').html(params);
    $('#paramss > #exist-test-dropdown').attr('disabled');
    (tr.data('sampleFreq') == '0' || tr.data('sampleFreq') == null ? $('#titleSample').html('Single Sampling') : $('#titleSample').html('Normal Sampling'))
    $('#showTankDetails').modal('show');
});
function removeTank(tank) {
    $.ajax({
        type: "POST",
        url: "/ServoCMP/Tse/RemoveTank",
        data: {
            tank: tank
        },
        dataType: "json",
        success: function (data) {
            showRawResponseDialog(data);
            if (data.modalMessage.includes("Successfully")) {
                $('#getSummaryTank').trigger('click');
            }
        },
        error: function (data) {
            showRawResponseDialog(data);
        }
    });
}

function AddMake(operation, makeId, makeName) {
    $('#modal-title-Make').text(operation);
    $('#makeSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#makeSubmit').val(operation.substr(0, operation.indexOf(' ')));
    if (operation.includes('Add')) {
        $('#makeId').val('');
        $('#makeNameInput').show();
        $('.hideActive').show();
        $('#makeNameModal').hide();
        $('.hideMakeEdit').hide();
    } else {
        if (operation.includes('Update')) {
            $('#modal-title-Make').text(operation.substr(0, operation.indexOf(' ')) + ' "' + makeName + '" Make');
            $('#makeId').val(makeId);
            $('#oldMakeName').val(makeName);
            $('#makeEdit').val(makeName);
            $('#makeNameModal').hide();
            $('.hideActive').hide();
            $('#makeNameInput').hide();
            $('.hideMakeEdit').show();
            $('#makeEdit').show();
        } else {
            $('#makeNameModal').show();
            $('.hideActive').hide();
            $('#makeNameInput').hide();
            $('.hideMakeEdit').hide();
            $('#makeEdit').hide();
        }
    }
    $('#UpdateMake').modal('show');
}

function removeMake(makeId, makeName, event) {
    if (typeof makeName === 'undefined') {
        $('#modal-title-Make').text("Removed Make(s)");
        $('#makeSubmit').text("Activate");
        $('#makeSubmit').val("Activate");
        $('#makeNameModal option').remove();
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/TseAdmin/AllDeactiveMake',
            dataType: 'json',
            success: function (data) {
                $('#makeNameModal').append($('<option>', {value: ''}).text("Select Make *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#makeNameModal').append($('<option>', {value: val.makeId}).text(val.makeName));
                    });
                } else {
                    $('#makeNameModal').append($('<option disabled>', {value: null}).text(data));
                }
            },
            error: function (data) {
                $('#makeNameModal').append($('<option disabled>', {value: null}).text("Error while fetching Make"));
            }
        });
        $('#makeNameModal').show();
        $('#makeNameInput').hide();
        $('.hideActive').hide();
        $('.hideMakeEdit').hide();
        $('#makeEdit').hide();
        $('#UpdateMake').modal('show');
    } else {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to Remove ' + makeName + ' Make?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $(event).parent('form').submit();
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made to ' + makeName + ' Make',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    }
}

function submitMake(btnObject) {
    switch (btnObject.value) {
        case "Add":
            if ($('#makeNameInput').val() === '' || $('#makeNameInput').val() === null) {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Make Name',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateMakeForm').attr('action', 'AddMake');
                $('#updateMakeForm').submit();
            }
            break;
        case "Activate":
            if ($('#makeNameModal').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Select Make.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateMakeForm').attr('action', 'ActivateMake');
                $('#updateMakeForm').append('<input type="hidden" name="makeName" value="' + $('#makeNameModal option:selected').text() + '"/>');
                $('#updateMakeForm').submit();
            }
            break;
        case "Update":
            if ($('#makeEdit').val() === '' || $('#makeId').val() === '' || $('#oldMakeName').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Make Name.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateMakeForm').attr('action', 'UpdateMake');
                $('#updateMakeForm').submit();
            }
            break;
    }
}

function AddTestSpec(operation, proName, testName, testId, proId, event, checkId) {
    $('#modal-title-Test-Spec').text(operation);
    $('#testSpecSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#testSpecSubmit').val(operation.substr(0, operation.indexOf(' ')));
    if (operation.includes('Add')) {
        $('#editTestName').hide();
        $('#testName').select2();
        $('#testName').show();
        $('#testId').val('');
        $('#proId').val('');
//        $('#proName').val('');
        $('#testName').val('');
        $('#valChk').val('');
        $('#minValId').val('');
        $('#maxValId').val('');
        $('#minMaxMinValId').val('');
        $('#minMaxMaxValId').val('');
        $('#eqValId').val('');
        $('#devValId').val('');
        $('#maxDevValId').val('');
//        $('#proName').select2();
        $('#proName').val(null).trigger('change');
        $('#updateTestSpecForm').attr('action', 'AddTestSpec');
    } else {
        if (operation.includes('Update')) {
            $('#editTestName').show();
            if ($('#testName').hasClass("select2-hidden-accessible")) {
// Select2 has been initialized
                $('#testName').select2('destroy');
            }
            $('#testName').hide();
            $('#modal-title-testSpecSubmit').text(operation.substr(0, operation.indexOf(' ')) + ' for "' + proName + '" Product of Test "' + testName + '"');
            $('#proId').val(proId);
            $('#proName').val(proId).select2();
            $('#testId').val(testId);
            $('#editTestName').data('testid', testId);
            $('#labEquipName').val($(event).closest("tr").find('td:eq(0)').text());
            $('#labMakeName').val($(event).closest("tr").find('td:eq(1)').text());
            $('#editTestName').val(testName);
            $('#valChk').val(checkId).select2();
            testId = "";
            showRelField(checkId.toString(), testId);
            $('#minValId').val($(event).closest("tr").find('td:eq(3)').text().trim(''));
            $('#maxValId').val($(event).closest("tr").find('td:eq(4)').text().trim(''));
            $('#minMaxMinValId').val($(event).closest("tr").find('td:eq(3)').text().trim(''));
            $('#minMaxMaxValId').val($(event).closest("tr").find('td:eq(4)').text().trim(''));
            $('#eqValId').val($(event).closest("tr").find('td:eq(5)').text().trim(''));
            $('#devValId').val($(event).closest("tr").find('td:eq(6)').text().trim(''));
            $('#maxDevValId').val($(event).closest("tr").find('td:eq(7)').text().trim(''));
            $('#updateTestSpecForm').attr('action', 'UpdateTestSpec');
        }
    }
    $('#UpdateTestSpec').modal('show');
}

function showRelField(val, testId) {
    switch (val) {
        case '1':
            $('#minVal').show();
            $('#maxVal').hide();
            $('#minMaxVal').hide();
            $('#eqVal').hide();
            $('#othVal').hide();
            $('#maxDelVal').hide();
            $('#minValId').prop('disabled', false);
            $('#maxValId').prop('disabled', true);
            $('#minMaxMinValId').prop('disabled', true);
            $('#minMaxMaxValId').prop('disabled', true);
            $('#eqValId').prop('disabled', true);
            $('#devValId').prop('disabled', true);
            $('#maxDevValId').prop('disabled', true);
            break;
        case '2':
            $('#maxVal').show();
            $('#minVal').hide();
            $('#minMaxVal').hide();
            $('#eqVal').hide();
            $('#othVal').hide();
            $('#maxDelVal').hide();
            $('#minValId').prop('disabled', true);
            $('#maxValId').prop('disabled', false);
            $('#minMaxMinValId').prop('disabled', true);
            $('#minMaxMaxValId').prop('disabled', true);
            $('#eqValId').prop('disabled', true);
            $('#devValId').prop('disabled', true);
            $('#maxDevValId').prop('disabled', true);
            break;
        case '3':
            $('#minMaxVal').show();
            $('#minVal').hide();
            $('#maxVal').hide();
            $('#eqVal').hide();
            $('#othVal').hide();
            $('#maxDelVal').hide();
            $('#minValId').prop('disabled', true);
            $('#maxValId').prop('disabled', true);
            $('#minMaxMinValId').prop('disabled', false);
            $('#minMaxMaxValId').prop('disabled', false);
            $('#eqValId').prop('disabled', true);
            $('#devValId').prop('disabled', true);
            $('#maxDevValId').prop('disabled', true);
            break;
        case '4':
            $('#eqVal').show();
            $('#minVal').hide();
            $('#maxVal').hide();
            $('#minMaxVal').hide();
            $('#othVal').hide();
            $('#maxDelVal').hide();
            $('#minValId').prop('disabled', true);
            $('#maxValId').prop('disabled', true);
            $('#minMaxMinValId').prop('disabled', true);
            $('#minMaxMaxValId').prop('disabled', true);
            $('#eqValId').prop('disabled', false);
            $('#devValId').prop('disabled', false);
            $('#maxDevValId').prop('disabled', true);
            break;
        case '5':
            $('#othVal').show();
            $('#minVal').hide();
            $('#maxVal').hide();
            $('#minMaxVal').hide();
            $('#eqVal').hide();
            $('#maxDelVal').hide();
            $('#minValId').prop('disabled', true);
            $('#maxValId').prop('disabled', true);
            $('#minMaxMinValId').prop('disabled', true);
            $('#minMaxMaxValId').prop('disabled', true);
            $('#eqValId').prop('disabled', true);
            $('#devValId').prop('disabled', true);
            $('#maxDevValId').prop('disabled', true);
            var testName = $('#testName').val() ? $('#testName').val() : $('#editTestName').data('testid');
            $.ajax({
                url: 'TestSpecification?testId=' + testName,
                type: 'GET',
                dataType: 'JSON',
                success: function (data) {
                    console.log("Does test hav other values? ")
                    if (data == "F") {
                        $.alert({
                            title: 'Not Found',
                            content: 'No other Values specs Found against ' + $('#testName option:selected').text() + ' test',
                            type: 'red',
                            typeAnimated: true
                        });
                        $('#othVal1').val("");
                    } else {
                        $('#othVal1').val(data);
                    }
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
            break;
        case '6':
            $('#maxDelVal').show();
            $('#minVal').hide();
            $('#maxVal').hide();
            $('#minMaxVal').hide();
            $('#eqVal').hide();
            $('#othVal').hide();
            $('#minValId').prop('disabled', true);
            $('#maxValId').prop('disabled', true);
            $('#minMaxMinValId').prop('disabled', true);
            $('#minMaxMaxValId').prop('disabled', true);
            $('#eqValId').prop('disabled', true);
            $('#devValId').prop('disabled', true);
            $('#maxDevValId').prop('disabled', false);
            break;
        default:
            $('#maxDelVal').hide();
            $('#minVal').hide();
            $('#maxVal').hide();
            $('#minMaxVal').hide();
            $('#eqVal').hide();
            $('#othVal').hide();
            $('#minValId').prop('disabled', true);
            $('#maxValId').prop('disabled', true);
            $('#minMaxMinValId').prop('disabled', true);
            $('#minMaxMaxValId').prop('disabled', true);
            $('#eqValId').prop('disabled', true);
            $('#devValId').prop('disabled', true);
            $('#maxDevValId').prop('disabled', true);
    }
}

function getTest(proName) {
    $('#testName option').remove();
    $.ajax({
        type: 'GET',
        url: 'TestSpecification',
        dataType: 'json',
        data: {
            proName: proName
        },
        success: function (data) {
            $('#testName').append($('<option>', {value: ''}).text("Select Test *"));
            if (data.constructor === Array) {
                jQuery.each(data, function (i, val) {
                    $('#testName').append($('<option>', {value: val.testId}).text(val.testName));
                });
            } else {
                $('#testName').append($('<option disabled>', {value: null}).text(data));
            }
            $('#testName').select2();
        },
        error: function (data) {
            $('#testName').append($('<option disabled>', {value: null}).text("Error while fetching Test"));
        }
    });
}

function submitTestSpec(btnObject) {
    switch (btnObject.value) {
        case "Add":
            if ($('#makeNameInput').val() === '' || $('#makeNameInput').val() === null) {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Make Name',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateMakeForm').attr('action', 'AddMake');
                $('#updateMakeForm').submit();
            }
            break;
        case "Activate":
            if ($('#makeNameModal').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Select Make.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateMakeForm').attr('action', 'ActivateMake');
                $('#updateMakeForm').append('<input type="hidden" name="makeName" value="' + $('#makeNameModal option:selected').text() + '"/>');
                $('#updateMakeForm').submit();
            }
            break;
        case "Update":
            if ($('#makeEdit').val() === '' || $('#makeId').val() === '' || $('#oldMakeName').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter Make Name.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateMakeForm').attr('action', 'UpdateMake');
                $('#updateMakeForm').submit();
            }
            break;
    }
}

function removeLabEquipments(labEquipId, labEquipName, status, event) {
    if (typeof labEquipName === 'undefined') {
        $('#modal-title-LabEquip').text("Removed Lab Equipment");
        $('#labEquipSubmit').text("Activate");
        $('#labEquipSubmit').val("Activate");
        $('#labEquipNameModal option').remove();
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/Lab/AllDeactiveLabEquipments',
            dataType: 'json',
            success: function (data) {
                $('#labEquipNameModal').append($('<option>', {value: ''}).text("Select Lab Equipment *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#labEquipNameModal').append($('<option>', {value: val.labEquipId}).text(val.labEquipName));
                    });
                } else {
                    $('#labEquipNameModal').append($('<option disabled>', {value: null}).text(data));
                }
            },
            error: function (data) {
                $('#labEquipNameModal').append($('<option disabled>', {value: null}).text("Error while fetching Lab Equipments"));
            }
        });
        $('.removedEquipment').show();
        $('.hideActive').hide();
        $('#UpdatelabEquipment').modal('show');
    } else {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to Remove ' + labEquipName + ' Lab Equipment?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $(event).parent('form').submit();
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made to ' + labEquipName + ' Lab Equipment',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    }
}

function submitLabEquipment(btnObject) {
    switch (btnObject.value) {
        case "Add":
            if ($('#labEquipName').val().trim() === '' || $('#labEquipName').val() === null
                    || $('#labMakeName').val().trim() === '' || $('#labMakeName').val() === null
                    || $('#labMethodName').val().trim() === '' || $('#labMethodName').val() === null
                    || $('#labEquipRemarks').val().trim() === '' || $('#labEquipRemarks').val() === null
                    || $('#operationalStatus').val().trim() === '' || $('#operationalStatus').val() === null) {
                $.alert({
                    title: 'Select All Fields',
                    content: 'Enter All Fields.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateLabEquipForm').attr('action', 'AddLabEquipment');
                $('#updateLabEquipForm').submit();
            }
            break;
        case "Activate":
            if ($('#labEquipNameModal').val() === '') {
                $.alert({
                    title: 'Select Fields',
                    content: 'Select Lab Equipment.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateLabEquipForm').attr('action', 'ActivateLabEquipment');
                $('#updateLabEquipForm').append('<input type="hidden" name="labEquipNameInput" value="' + $('#labEquipNameModal option:selected').text() + '"/>');
                $('#updateLabEquipForm').submit();
            }
            break;
        case "Update":
            if ($('#labEquipName').val().trim() === '' || $('#labEquipName').val() === null
                    || $('#labMakeName').val().trim() === '' || $('#labMakeName').val() === null
                    || $('#labMethodName').val().trim() === '' || $('#labMethodName').val() === null
                    || $('#labEquipRemarks').val().trim() === '' || $('#labEquipRemarks').val() === null
                    || $('#operationalStatus').val().trim() === '' || $('#operationalStatus').val() === null) {
                $.alert({
                    title: 'Select Fields',
                    content: 'Enter all Values.',
                    type: 'red',
                    typeAnimated: true
                });
            } else {
                $('#updateLabEquipForm').attr('action', 'UpdateLabEquipment');
                $('#updateLabEquipForm').submit();
            }
            break;
    }
}

function AddEquipments(operation, labEquipId, labEquipName, labEquipStatus, event) {
    $('#modal-title-LabEquip').text(operation);
    $('#labEquipSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#labEquipSubmit').val(operation.substr(0, operation.indexOf(' ')));
    $('.hideActive').show();
    $('.removedEquipment').hide();
    if (operation.includes('Add')) {
        $('#labEquipId').val('');
        $('#oldEquipName').val('');
        $('#labEquipName').val('');
        $('#labMakeName').val('');
        document.getElementById('operationalStatus').selectedIndex = 0;
        $('#labMethodName').val('');
        $('#labEquipRemarks').val('');
    } else {
        if (operation.includes('Update')) {
            $('#modal-title-LabEquip').text(operation.substr(0, operation.indexOf(' ')) + ' "' + labEquipName + '" Lab Equipment');
            $('#labEquipId').val(labEquipId);
            $('#oldEquipName').val(labEquipName);
            $('#labEquipName').val($(event).closest("tr").find('td:eq(0)').text());
            $('#labMakeName').val($(event).closest("tr").find('td:eq(1)').text());
//            document.getElementById('operationalStatus').selectedIndex = [...document.getElementById('operationalStatus').options].findIndex(option => option.text === $(event).closest('tr').find('td:eq(2)').text());
            $('#operationalStatus').val(labEquipStatus).select2();
            $('#labMethodName').val($(event).closest("tr").find('td:eq(3)').text());
            $('#labEquipRemarks').val($(event).closest("tr").find('td:eq(4)').text());
        }
    }
    $('#UpdatelabEquipment').modal('show');
}

function AddTest(operation, testId, testName, event) {
    $('#modal-title-Test').text(operation);
    $('#testSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#testSubmit').val(operation.substr(0, operation.indexOf(' ')));
    $('.hideActive').show();
    $('.removedTest').hide();
    if (operation.includes('Add')) {
        $('#testId').val('');
        $('#oldTestName').val('');
        $('#testName').val('');
        $('#testUnit').val('');
        $('#methodName').val('');
        $('#sampleQty').val('');
        $('#dispSeq').val('');
        $('#testSubmit').val('Add');
    } else {
        if (operation.includes('Update')) {
            $('#modal-title-Test').text(operation.substr(0, operation.indexOf(' ')) + ' "' + testName + '" Test');
            $('#testId').val(testId);
            $('#oldTestName').val(testName);
            $('#testUnit').val($(event).closest("tr").find('td:eq(1)').text());
            $('#testName').val($(event).closest("tr").find('td:eq(0)').text());
            $('#methodName').val($(event).closest("tr").find('td:eq(2)').text());
            $('#sampleQty').val($(event).closest("tr").find('td:eq(3)').text());
            $('#dispSeq').val($(event).closest("tr").find('td:eq(4)').text());
            $('#testSubmit').val('Update');
        }
    }
    $('#UpdateTest').modal('show');
}

function submitTest() {
    if ($('#testId').val() === '') {
        $.alert({
            title: 'Select Fields',
            content: 'Select Product.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $('#updateTestForm').attr('action', 'UpdateTest');
        $('#updateTestForm').submit();
        $('#UpdateTest').modal('hide');
    }
}

function removeTest(testId, testName, event) {
    if (typeof testName === 'undefined') {
        $('#modal-title-Test').text("Removed Test");
        $('#testSubmit').text("Activate");
        $('#testSubmit').val("Activate");
        $('#option').remove();
        var url1 = "";
        if ($('#role').val() == "labAdmin") {
            url1 = '/ServoCMP/LabAdmin/AllDeactiveTest';
        } else if ($('#role').val() == "tseAdmin") {
            url1 = '/ServoCMP/TseAdmin/AllDeactiveTest';
        }
        $.ajax({
            type: 'GET',
            url: url1,
            dataType: 'json',
            success: function (data) {
                $('#testNameModal').append($('<option>', {value: ''}).text("Select Test *"));
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        $('#testNameModal').append($('<option>', {value: val.testId}).text(val.testName));
                    });
                } else {
                    $('#testNameModal').append($('<option disabled>', {value: null}).text(data));
                }
            },
            error: function (data) {
                $('#testNameModal').append($('<option disabled>', {value: null}).text("Error while fetching Test"));
            }
        });
        $('.removedTest').show();
        $('.hideActive').hide();
        $('#UpdateTest').modal('show');
    } else {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to Remove ' + testName + ' Test?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $(event).parent('form').submit();
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made to ' + testName + ' Test',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    }
}

function cannotRemove() {
    $.alert({
        title: 'Cannot Remove !!!!',
        content: 'There are Sample(s) associated with this Tank, hence it cannot be removed.',
        type: 'red',
        typeAnimated: true
    });
}

function saveSequence() {
    var data = $('#TestSequence').DataTable().rows().data();
    var dispPos = [];
    for (i = 0; i < data.length; i++) {
        var testPos = [];
        testPos.push(data[i][1], data[i][0]);
        dispPos.push(testPos);
    }
    $.ajax({
        type: 'POST',
        url: '/ServoCMP/TseAdmin/TestSequence',
        data: {
            dispPos: JSON.stringify(dispPos)
        },
        dataType: 'json',
        success: function (data) {
            showRawResponseDialog(data);
        },
        error: function (data) {
            showRawResponseDialog(data);
        }
    });
}

function changeTse() {
    var data = $('#tseAccess').DataTable().rows({selected: true}).data();
    if (data.length === 0) {
        $.alert({
            title: 'No Selection !!!',
            content: 'Select Customer to Change Tse.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        var customer = [];
        var customerIds = [];
        $('#selectedCust li').remove();
        for (var i = 0; i < data.length; i++) {
            customer.push(data[i][3]);
            customerIds.push(data[i][1]);
            var node = document.createElement('LI');
            var nodeText = document.createTextNode(data[i][2]);
            node.appendChild(nodeText);
            document.getElementById('selectedCust').appendChild(node);
        }
//        $('#custNames').val(customer.toString());
        $('#custNames').val(customer.toString());
        $('#custNo').val(customerIds.toString());
        $('#UpdateTse').modal('show');
    }
}

function changeTseLab() {
    $('#lab_newEmpCode').val($('#empCode').val());
    $('#AssignTseLab').modal('show');
}

function changeLab() {
    var data = $('#labAccess').DataTable().rows({selected: true}).data();
    if (data.length === 0) {
        $.alert({
            title: 'No Selection !!!',
            content: 'Select Lab to Change Lab Person.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        var lab = [];
        var labCode = [];
        $('#selectedLab li').remove();
        for (var i = 0; i < data.length; i++) {
            lab.push(data[i][2]);
            labCode.push(data[i][1]);
            var node = document.createElement('LI');
            var nodeText = document.createTextNode(data[i][2]);
            node.appendChild(nodeText);
            document.getElementById('selectedLab').appendChild(node);
        }
        $('#labNames').val(lab.toString());
        $('#labCodes').val(labCode.toString());
        $('#modal-title-Lab').text('Allocate Below Lab(s) to: ');
        $('#UpdateLab').modal('show');
    }
}

function selectTse(event) {
    if ($('#empCode option:selected').val() === '' || $('#empCode option:selected').val() === null) {
        $.alert({
            title: 'Selection Employee !!!',
            content: 'You must select Tse Employee, to select all Customer.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        switch (event.value) {
            case '1':
                $('#tseAccess').DataTable().rows().select();
                event.value = 0;
                break;
            case '0':
                $('#tseAccess').DataTable().rows().deselect();
                event.value = 1;
                break;
        }
    }
}

function selectLab(event) {
    if ($('#empCode option:selected').val() === '' || $('#empCode option:selected').val() === null) {
        $.alert({
            title: 'Selection Employee !!!',
            content: 'You must select Lab Employee, to select all Lab(s).',
            type: 'red',
            typeAnimated: true
        });
    } else {
        switch (event.value) {
            case '1':
                $('#labAccess').DataTable().rows().select();
                $('#labSelect').html("Deselect All");
                event.value = 0;
                break;
            case '0':
                $('#labAccess').DataTable().rows().deselect();
                $('#labSelect').html("&nbsp;Select All&nbsp;");
                event.value = 1;
                break;
        }
    }
}

function AddLab(operation, labLocCode, labName, event, empCode) {
    $('#modal-title-LabDetails').text(operation);
    $('#LabDetailsSubmit').text(operation.substr(0, operation.indexOf(' ')));
    $('#LabDetailsSubmit').val(operation.substr(0, operation.indexOf(' ')));
    if (operation.includes('Add')) {
        $('#labLocCode').val('');
        $('#labName').val('');
        $('#labAdd').val('');
        $('#labAdd2').val('');
        $('#labAdd3').val('');
        $('#labEmpCode').val('');
        $('#updateLabForm').attr('action', 'AddLab');
    } else {
        if (operation.includes('Update')) {
            $('#modal-title-LabDetails').text(operation.substr(0, operation.indexOf(' ')) + ' "' + labName + '" Lab');
            $('#labLocCode').attr('readonly', true);
            $('#labLocCode').val(labLocCode);
            $('#labName').val(labName);
            $('#labAdd').val($(event).closest("tr").find('td:eq(4)').find('span:eq(0)').text().trim());
            $('#labAdd2').val($(event).closest("tr").find('td:eq(4)').find('span:eq(1)').text().trim());
            $('#labAdd3').val($(event).closest("tr").find('td:eq(4)').find('span:eq(2)').text().trim());
            $('#labEmpCode').val(empCode);
            $('#updateLabForm').attr('action', 'UpdateLab');
        }
    }
    $('#UpdateLabDetails').modal('show');
}

//////////////////////////////// Steven s code   /////////////////////////////////////

function getCustDetails() {
    var custObj, custArr = [];
    $('#custName > tr').each(function () {
        custObj = new Object();
        custObj.code = $(this).find('.custNoVal').val();
        custObj.name = $(this).find('.custNameVal').val();
        custArr.push(custObj);
    });
    return custArr;
}

function getProdDetails() {
    var prodObj, prodArr = [];
    $('#prodList > tr').each(function () {
        prodObj = new Object();
        prodObj.code = $(this).find('.prodNoVal').val();
        prodObj.name = $(this).find('.prodNameVal').val();
        prodObj.prodcat = $(this).find('.prodCatVal').val();
        prodArr.push(prodObj);
    });
    return prodArr;
}

$(document).ready(function () {



    $(document).on('click', '.idDropdown', function () {
        var testID = $(this).data('id');
        var sels = $(this);
        var loadd = sels.data('loadd');
        if (loadd == 0) {
            sels.empty();
            $.ajax({
                type: "GET",
                url: "GetOtherDropdown",
                data: {testId: testID},
                dataType: "json",
                success: function (data) {
//                    console.log(data);
                    $.each(data, function (i, val) {
                        sels.append(`<option value="${val}">${val}</option>`);
                    });
                    sels.data('loadd', '1');
                    sels.select2({
                        theme: "classic"
                    });
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        }
    });
    $(document).on('click', '#addExCustomer', function () {
        $('#custName').append(` 
            <tr>
                <td class="floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                    <input type="text" minlength="8" maxlength="10" placeholder="Enter Customer Code" class="custNoVal form-control isNumber">
                </td>
                <td class="floating-label-form-group floating-label-form-group-with-value">
                    <input type="text" placeholder="Enter Customer Name" class="custNameVal form-control">
                </td>
                <td class="text-danger">
                    <span class="glyphicon glyphicon-remove removeRow1"></span>
                </td>
            </tr>
        `);
    });
    if ($('.prodCat2')[0] || $('.prodCatVal')[0]) {
        $.ajax({
            async: false,
            url: '/ServoCMP/TseAdmin/getCategory',
            type: 'GET',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                if (typeof data == "object") {
//                    console.log(data);
                    if ($('.prodCatVal')[0]) {
                        $('.prodCatVal:last-child').empty();
                        $.each(data, function (key, val) {
                            $('.prodCatVal:last-child').append("<option value=" + val.cat_id + ">" + val.cat_name + "</option>").trigger('change');
                        });
                        $('.prodCatVal:last-child').select2();
                    }
                    if ($('.prodCat2')[0]) {
                        $('.prodCat2').empty();
                        $('.prodCat2').append("<option value=''>Select Category..</option>").trigger('change');
                        $.each(data, function (key, val) {
                            $('.prodCat2').append("<option value=" + val.cat_id + ">" + val.cat_name + "</option>").trigger('change');
                        });
                        $('.prodCat2').select2();
                    }
                }

            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    }
    $(document).on('click', '#addExProduct', function () {
        $('#prodList').append(` 
            <tr>
                <td class="floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                    <input type="text" minlength="4" maxlength="4" placeholder="Enter Customer Code" class="prodNoVal form-control isNumber" data-field="Product Code">
                </td>
                <td class="floating-label-form-group floating-label-form-group-with-value">
                    <input type="text" placeholder="Enter Customer Name" class="prodNameVal form-control">
                </td>
                <td class="floating-label-form-group floating-label-form-group-with-value">
                    <select class="prodCatVal">
                        <option>loading...</option>
                    </select>
                </td>
                <td class="text-danger">
                    <span class="glyphicon glyphicon-remove removeRow1"></span>
                </td>
            </tr>
        `);
        $.ajax({
            async: false,
            url: '/ServoCMP/TseAdmin/getCategory',
            type: 'GET',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                if (typeof data == "object") {
//                    console.log(data);
                    $('.prodCatVal:last-child').empty();
                    $.each(data, function (key, val) {
                        $('.prodCatVal:last-child').append("<option value=" + val.cat_id + ">" + val.cat_name + "</option>").trigger('change');
                    });
                    $('.prodCatVal:last-child').select2();
                }

            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    });
    $(document).on('click', '#customerSubmit', function () {
        var custArr = getCustDetails();
        var custCommonDetails = new Object();
        custCommonDetails.empcode = $('#EmpCodeLoadedModal').val();
        custCommonDetails.empname = $('#EmpNameModal').val();
        custCommonDetails.empemail = $('#EmpEmailModal').val();
        custCommonDetails.empCrcode = $('#CtrlEmpCodeModal').val();
        custCommonDetails.empCrname = $('#CtrlEmpNameModal').val();
        custCommonDetails.empCremail = $('#CtrlEmpEmailModal').val();
        $.ajax({
            url: '/ServoCMP/addCustomers2',
            type: 'POST',
            dataType: 'json',
            data: {jsonObjs: JSON.stringify(custArr), commonDetails: JSON.stringify(custCommonDetails), customerIndustry: $('#customerIndustry').val(), customerIndustryName: $('#customerIndustryName').val()},
            success: function (data) {
                showRawResponseDialog(data);
                $('#UpdateCustomer').modal('hide');
//                console.log(data);
                console.log("Success");
                getCustomer($('#customerIndustry').val());
                $.alert({
                    title: 'Customer added !!!',
                    content: 'Successfully',
                    type: 'green',
                    typeAnimated: true
                });
                // populate customer field again
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    });
    $(document).on('click', '#productSubmit', function () {
        var prodArr = getProdDetails();
//        console.log(prodArr);
        if ($(this).val() != "Update") {
            if (prodArr.length > 0) {
                if (prodArr.every((a) => a.code != "" && a.name != "")) {
                    $.ajax({
                        url: '/ServoCMP/TseAdmin/addProducts2',
                        type: 'POST',
                        dataType: 'JSON',
                        data: {json: JSON.stringify(prodArr)},
                        success: function (data) {
                            showRawResponseDialog(data);
//                            console.log(data);
                            $('#AddProduct').modal('hide');
                        },
                        error: function (error) {
                            console.log(error.responseText);
                        }
                    });
                } else {
                    $.alert({
                        title: 'Invalid Input',
                        content: 'Kindly fill all the required fields',
                        type: 'red',
                        typeAnimated: true
                    });
                }
            }
        }
    });
    $(document).on('click', '.removeRow1', function () {
        $(this).parents('tr').remove();
    });
    $('#proName').select2();
    $('#testNameModal').select2();
    $('#valChk').select2();
    $('#operationalStatus').select2({
        placeholder: "Select Operational Status.."
    });
    $('#LabCode').select2();
    $('#prodFilter').select2();
    $('.selectFilter').select2();
    $('#productId').select2();
//    $('#productNameModal').select2();
//    $('#applicationNameModal').select2();
//    $('#industryNameModal').select2();
//    $('#makeNameModal').select2();


    $('#testSubmit').on('click', function () {
        var url = "", url1 = "", url2 = "";
        if ($('#role').val() == "labAdmin") {
            url = '/ServoCMP/LabAdmin/AddTest';
        } else if ($('#role').val() == "tseAdmin") {
            url = '/ServoCMP/TseAdmin/AddTest';
        }
        if ($('#role').val() == "labAdmin") {
            url1 = '/ServoCMP/LabAdmin/UpdateTest';
        } else if ($('#role').val() == "tseAdmin") {
            url1 = '/ServoCMP/TseAdmin/UpdateTest';
        }
        if ($('#role').val() == "labAdmin") {
            url2 = '/ServoCMP/LabAdmin/ActivateTest';
        } else if ($('#role').val() == "tseAdmin") {
            url2 = '/ServoCMP/TseAdmin/ActivateTest';
        }
        if ($('#testName').val() != '' &&
                $('#testUnit').val() != '' &&
                $('#methodName').val() != '' &&
                $('#sampleQty').val() != '' &&
                $('#dispSeq').val() != '' &&
                $(this).val() == 'Add') {
            $.ajax({
                url: url,
                type: 'POST',
                dataType: 'JSON',
                data: {
                    testName: $('#testName').val(),
                    testUnit: $('#testUnit').val(),
                    methodName: $('#methodName').val(),
                    sampleQty: $('#sampleQty').val(),
                    dispSeq: $('#dispSeq').val()
                },
                success: function (data) {
//                    showRawResponseDialog(data);
                    $.alert({
                        title: 'Test',
                        content: 'Successfully Added ' + $('#testName').val(),
                        type: 'color',
                        typeAnimated: true
                    });
                    window.location.reload();
//                    console.log(data);
                    $('#UpdateTest').modal('hide');
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        } else if (
                $('#testId').val() != '' &&
                $(this).val() == 'Update') {
            $.ajax({
                url: url1,
                type: 'POST',
                dataType: 'JSON',
                data: {
                    testId: $('#testId').val(),
                    oldTestName: $('#oldTestName').val(),
                    testName: $('#testName').val(),
                    testUnit: $('#testUnit').val(),
                    methodName: $('#methodName').val(),
                    sampleQty: $('#sampleQty').val(),
                    dispSeq: $('#dispSeq').val()
                },
                success: function (data) {
                    showRawResponseDialog(data);
//                    console.log(data);
                    window.location.reload();
                    $('#UpdateTest').modal('hide');
//                            $('#AddProduct').modal('hide');
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        } else if ($(this).val() == 'Activate') {
            $.ajax({
                url: url2,
                type: 'POST',
                dataType: 'JSON',
                data: {
                    testNameInput: $('#testNameModal option:selected').text(),
                    testNameModal: $('#testNameModal').val()
                },
                success: function (data) {
                    showRawResponseDialog(data);
//                    console.log(data);
                    window.location.reload();
                    $('#UpdateTest').modal('hide');
                },
                error: function (error) {
                    showRawResponseDialog(data);
                }
            });
        } else {
            $.alert({
                title: 'Invalid input',
                content: 'All field are required',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $(document).on('change', '#testName', function () {
        $('#valChk').val(' ').trigger('change');
    });
//    $(document).ajaxComplete(function () {
//        console.log("all ajax excetued");
//        $('body > div.modal-backdrop.in').remove();
//    });
    ////////////////////////////////// Other Test Specs //////////////////////////////////////////////////////////////////
    $(document).on('click', '#OthValUpdate', function () {
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to ' + $('#OthValUpdate').val() + ' "' + $('#TestsWithOV option:selected').text() + '" ?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        var ovalues = null;
                        if ($('#TestsWithOV').val() != null && $("#OVforTest").val() != null) {
                            if ($('#OthValUpdate').val() == "Add") {
                                ovalues = $('#OVforTest').val();
                                $.ajax({
                                    url: 'OtherValuesServlet?action=addOValues&testId=' + $('#TestsWithOV').val(),
                                    type: 'POST',
                                    dataType: 'JSON',
                                    data: {json: JSON.stringify(ovalues)},
                                    success: function (data) {
//                                        console.log(data);
                                        $.alert({
                                            title: 'Success',
                                            content: 'New Values Added to "' + $('#TestsWithOV option:selected').text() + '"',
                                            type: 'green',
                                            typeAnimated: true
                                        });
                                        $('#TestsWithOV').trigger('change');
                                    },
                                    error: function (error) {
                                        console.log(error.responseText);
                                    },
                                    complete: function () {
                                        $('body > div.modal-backdrop.in').remove();
                                    }
                                });
                            } else if ($('#OthValUpdate').val() == "Update") {
                                ovalues = $('#OVforTest').val();
                                $.ajax({
                                    url: 'OtherValuesServlet?action=updateOVTests&testId=' + $('#TestsWithOV').val(),
                                    type: 'POST',
                                    dataType: 'JSON',
                                    data: {json: JSON.stringify(ovalues)},
                                    success: function (data) {
//                                        console.log(data);
                                        $.alert({
                                            title: 'Success',
                                            content: 'New Values Updated for "' + $('#TestsWithOV option:selected').text() + '"',
                                            type: 'green',
                                            typeAnimated: true
                                        });
                                        $('#TestsWithOV').trigger('change');
                                    },
                                    error: function (error) {
                                        console.log(error.responseText);
                                    },
                                    complete: function () {
                                        $('body > div.modal-backdrop.in').remove();
                                    }
                                });
                            }

                        } else {
                            if ($('#TestsWithOV').val() == null) {
                                $.alert({
                                    title: 'No Data',
                                    content: 'Please Select a Test parameter',
                                    type: 'red',
                                    typeAnimated: true
                                });
                            } else {
                                $.alert({
                                    title: 'No Data',
                                    content: 'Please Add Mapping Data against the respected Test Parameter',
                                    type: 'red',
                                    typeAnimated: true
                                });
                            }

                        }

                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: 'Denied',
                            content: 'No Changes Were Made',
                            type: 'green',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    });
    var OVdatable = $('#OVTable').DataTable({
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Other Specification Values'
            },
            'colvis'
        ]});
    if ($('#TestsWithOV')[0]) {
        $.ajax({
            url: 'OtherValuesServlet?action=getTests',
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                $('#TestsWithOV').append('<option disabled selected value>Select...</option>');
                $.each(data, function (i) {
                    $('#TestsWithOV').append('<option value="' + data[i].testId + '">' + data[i].testName + '</option>');
                });
                $('#TestsWithOV').select2();
                if ($('#fetchTestId').val() != "" && $('#fetchTestId').val() != null) {
                    $('#TestsWithOV').val($('#fetchTestId').val()).trigger('change');
                }
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
        $.ajax({
            url: 'OtherValuesServlet?action=getUniqueOValues',
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                var selectedValues = [];
                $.each(data, function (i, val) {
                    if ($('#OVforTest option[value=' + val.testName + ']').length > 0) {
                        selectedValues.push(val.testName);
                    } else {
                        $('#OVforTest').append(new Option(val.testName, val.testName, true, true));
                        selectedValues.push(val.testName);
                    }
                });
                $('#OVforTest').select2('val', ' ');
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
        $('body > div.modal-backdrop.in').remove();
    }

    $(document).on('click', '#getOVSummary', function () {
        var $btn = $(this).button('loading');
        $.ajax({
            url: 'OtherValuesServlet?action=getSummary',
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                OVdatable.clear().draw(false);
                $.each(data, function (i) {
                    OVdatable.row.add([
                        data[i].testName,
                        data[i].unit
                    ]).draw(true);
                });
                $btn.button('reset')
            },
            error: function (error) {
                console.log(error.responseText);
                $btn.button('reset')
            }
        });
    });
    $(document).on('click', '#UpdateOVTest', function () {
        $.ajax({
            url: 'OtherValuesServlet?action=getSummary',
            type: 'POST',
            data: JSON.stringify({'ovalues': $('#TestsWithOV').val(), 'testId': $('#OVforTest').val()}),
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                alert('No code written' + data);
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    });
    $(document).on('click', '#otherValuesPage', function () {
        var testName = $('#testName').val() ? $('#testName').val() : $('#editTestName').data('testid');
        $('#UpdateTestSpec').modal('hide');
        window.open('redirectController?url=OthValues&testId=' + testName, "_self");
    });
    $(document).on('change', '#TestsWithOV', function () {
        $.ajax({
            url: 'TestSpecification?testId=' + $('#TestsWithOV').val(),
            type: 'GET',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                if (data == "F") {
                    $.alert({
                        title: 'Not Found',
                        content: 'No other Values specs Found against ' + $('#TestsWithOV option:selected').text() + ' test',
                        type: 'red',
                        typeAnimated: true
                    });
                    console.log("Not Found");
                    $('#OVforTest').select2('val', ' ');
                    $('#OthValUpdate').val("Add").html("Add");
                } else {
                    $('#OVforTest').select2('val', ' ');
                    var values = data.split(',');
                    var cleanArry = new Array();
                    $.each(values, function (idx, val) {
                        cleanArry.push($.trim(this));
                    });
                    $('#OVforTest').val(cleanArry).trigger('change');
                    $('#OthValUpdate').val("Update").html("Update");
                }
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    });
    var testSpecDT = $('#TestSpecTable').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Test Specification List'
            },
            'colvis'
        ]});
    $(document).on('click', '#filterSearch', function () {
        var noedit = $(this).data("noedit");
        if ($('#prodFilter').val()) {
            $.ajax({
                url: 'redirectController?url=fetchTestSpecList&prodId=' + $('#prodFilter').val(),
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
                    if (data.length != 0) {
//                        console.log(data);
                        testSpecDT.clear().draw(false);
                        $.each(data, function (i, v) {
                            testSpecDT.row.add([
                                v.proName,
                                (noedit == 1 ? v.testName : v.testName + "<a href='#' class='editTestSpec' data-proName='" + v.proName + "' data-testName='" + v.testName + "' data-testId='" + v.testId + "' data-prodId='" + v.proId + "' data-checkId='" + v.mstTestParam.checkId + "' data-toggle='tooltip' data-placement='right' title='Edit " +
                                        v.testName + " Test' style='text-decoration: none; float: right'><span class='glyphicon glyphicon-pencil'></span></a>"),
                                v.spec,
                                v.mstTestParam.minValue ? v.mstTestParam.minValue : 'N/A',
                                v.mstTestParam.maxValue ? v.mstTestParam.maxValue : 'N/A',
                                v.mstTestParam.typValue ? v.mstTestParam.typValue : 'N/A',
                                v.mstTestParam.devValue ? v.mstTestParam.devValue : 'N/A',
                                v.mstTestParam.maxValue ? v.mstTestParam.maxValue : 'N/A',
                                v.mstTestParam.otherVal ? v.mstTestParam.otherVal : 'N/A',
                                `<a href="#" onclick="openHelloIOCian(${v.updatedBy})">${v.updatedBy}</a>`,
                                v.updatedDate
                            ]).draw(false);
                        });
                    } else {
                        testSpecDT.clear().draw();
                        $.alert({
                            title: 'No Data',
                            content: 'No Test mapped with this product',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        } else {
            $.alert({
                title: 'No Data',
                content: 'Please Select Product',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $(document).on('click', '.editTestSpec', function () {
        var ts = $(this);
        AddTestSpec('Update Test', ts.data('proname'), ts.data('testname'), ts.data('testid'), ts.data('prodid'), ts, ts.data('checkid'));
    });
    var ProdFilterDB = $('#ProductTable').DataTable({
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Samples Created by TSE'
            },
            'colvis'
        ]
    });
    $(document).on('click', '#btnProductFilter', function () {
        if ($('#prodName').val().length > 3) {
            $.ajax({
                url: 'redirectController?url=fetchProductList&pName=' + $('#prodName').val(),
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
                    console.log(data);
                    if (data.length > 0) {
                        ProdFilterDB.clear().draw(false);
                        $.each(data, function (i, v) {
                            ProdFilterDB.row.add([
                                v.proId,
                                v.proName,
                                v.prodCat,
                                v.updatedBy,
                                v.updatedDate,
                                `<form method="post" style="margin-bottom: 0px" action="RemoveProduct" id="inactiveProduct">
                                <input name="proId" type="hidden" value="${v.proId}"/>
                                <input name="proName" type="hidden" value="${v.proName}"/>
                                <a href="#" onclick="AddProduct('Update Product', ${v.proId}, '${v.proName}', this)" data-toggle="tooltip" data-placement="right" title="Edit ${v.proName} Industry" style=" text-decoration: none;">
                                    <span class="glyphicon glyphicon-pencil"></span>
                                </a>
                                |
                                <a href="#" onclick="removeProduct(${v.proId}, '${v.proName}', this)" data-toggle="tooltip" data-placement="right" title="Remove ${v.proName} Industry" style=" text-decoration: none;">
                                    <span class="glyphicon glyphicon-trash"></span>
                                </a>
                            </form>`
                            ]).draw(false);
                        });
                    } else {
                        ProdFilterDB.clear().draw();
                        $.alert({
                            title: 'No Results',
                            content: 'No Product was found with the text " ' + $('#prodName').val() + ' ".',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                },
                error: function (error) {
                    console.log(error);
                }
            });
        } else {
            $.alert({
                title: 'Invalid Search!',
                content: 'Kindly enter 4 or more letters for the Search.',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    //////////////////////////////////////////////// News GUI ///////////////////////////////////////////////////////////////

    function getNews() {
        $.ajax({
            url: 'newsServlet?url=getNewsList',
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                if (data.length > 0) {
                    NewsDT.clear().draw(false);
                    $.each(data, function (i, v) {
                        NewsDT.row.add([
                            v.msgId,
                            v.msgTitle,
                            v.msgBody,
                            v.updatedby,
                            v.updatedDate,
                            `<span class="editNews text-success"><i class="glyphicon glyphicon-pencil"></i></span> | <span class="removeNews text-danger"><i class="glyphicon glyphicon-trash"></i></span>`
                        ]).draw(false);
                    });
                }
            },
            error: function (error) {
                console.log(error);
            }
        });
        $('body > div.modal-backdrop.in').remove();
    }

    var NewsDT = "";
    if ($('#NewsTable')[0]) {

        NewsDT = $('#NewsTable').DataTable({
            pageLength: 8,
            ordering: false,
            info: false
        });
        getNews();
    }


    $(document).on('click', '.editNews', function () {
        $('#modal-title-News').html('Update News');
        $('#newsId').val($(this).parents('tr').find('td:nth-child(1)').text());
        $('#newsTitle').val($(this).parents('tr').find('td:nth-child(2)').text());
        $('#newsTxt').val($(this).parents('tr').find('td:nth-child(3)').text());
        $('#UpdateNews').modal('show');
    });
    $(document).on('click', '.removeNews', function () {
        var id = $(this);
        $.confirm({
            title: 'Confirm',
            content: 'Are you sure you want to remove this News Record?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $.ajax({
                            url: 'newsServlet?url=delNews&newsId=' + id.parents('tr').find('td:nth-child(1)').text(),
                            type: 'POST',
                            dataType: 'JSON',
                            success: function (data) {
//                                console.log(data);
                                if (data == "Success") {
                                    $.alert({
                                        title: 'Success',
                                        content: 'The Record was successfully deleted',
                                        type: 'green',
                                        typeAnimated: true
                                    });
                                    getNews();
                                } else if (data == "Failure") {
                                    $.alert({
                                        title: 'Failure',
                                        content: 'The Record wasnt deleted. Please try again later',
                                        type: 'red',
                                        typeAnimated: true
                                    });
                                }
                            },
                            error: function (error) {
                                console.log(error);
                            }
                        });
                    }
                },
                No: {
                    btnClass: 'btn-red',
                    action: function () {
                        $.alert({
                            title: '',
                            content: 'No changes were made to Master Record',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                }
            }
        });
    });
    $(document).on('click', '#addNews', function () {
        var title = $('#addNewsTitle').val();
        var body = $('#addNewsBody').val();
        if (title && body) {
            $.ajax({
                url: 'newsServlet?url=addNews&title=' + title + '&body=' + body,
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
//                    console.log(data);
                    $.alert({
                        title: 'News',
                        content: data + ' records Added to Master',
                        type: 'green',
                        typeAnimated: true
                    });
                    getNews();
                },
                error: function (error) {
                    console.log(error);
                }
            });
        } else {
            $.alert({
                title: 'Error',
                content: 'Please Enter title & body',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $(document).on('click', '#newsUpdate', function () {
        var newsId = $('#newsId').val();
        var title = $('#newsTitle').val();
        var body = $('#newsTxt').val();
        $.ajax({
            url: 'newsServlet?url=updateNews&newsId=' + newsId + '&title=' + title + '&body=' + body,
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                $('#UpdateNews').modal('hide');
                $('#newsId').val("");
                $('#newsTitle').val("");
                $('#newsTxt').val("");
                if (data == "Success") {
                    $.alert({
                        title: 'Success',
                        content: 'The Record was successfully updated',
                        type: 'green',
                        typeAnimated: true
                    });
                } else if (data == "Failure") {
                    $.alert({
                        title: 'Failure',
                        content: 'The Record wasnt updated. Please try again later',
                        type: 'red',
                        typeAnimated: true
                    });
                }
                getNews();
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    });
    ////////////////////////////////////////////////////////SampleCreated /////////////////////////////////////////////////////////
    var SCT = $('#createdSample');
    var RBL = $('#receivedByLab');
    var SBL = $('#sentByLab');
    var STC = $('#sentToCustomer');
    var SampleCreatedTable = $('#createdSample').DataTable({
        "order": [],
        "columnDefs": [
            {"className": "dt-head-center", "targets": "_all"}
        ],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Samples Created by TSE'
            },
            'colvis'
        ]
    });
    var receivedByLabDT = $('#receivedByLab').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Samples Received by Lab'
            },
            'colvis'
        ]});
    var sentByLabDT = $('#sentByLab').DataTable({
        "order": [],
        "columnDefs": [
            {"className": "dt-center", "targets": "_all"}
        ],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Samples Sent By Lab'
            },
            'colvis'
        ]
    });
    var sentToCustomerDT = $('#sentToCustomer').DataTable({"order": [],
        "columnDefs": [
            {"className": "dt-center", "targets": "_all"}
        ],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Samples Send to Customer'
            },
            'colvis'
        ]
    });
    $(document).on('click', '#filterSearchfrSC', function () {
        var status = $(this).data('status');
        var pid = $('#prodFilter').val();
        var cid = $('#custFilter').val();
        var did = $('#deptFilter').val();
        var aid = $('#appFilter').val();
        var SampleTable = null;
        var tableSelector = null;
        switch (status) {
            case 1:
                tableSelector = SCT;
                SampleTable = SampleCreatedTable;
                break;
            case 2:
                tableSelector = RBL;
                SampleTable = receivedByLabDT;
                break;
            case 3:
                tableSelector = SBL;
                SampleTable = sentByLabDT;
                break;
            case 4:
                tableSelector = STC;
                SampleTable = sentToCustomerDT;
                break;
            default:
                $.alert({
                    title: 'Error',
                    content: 'Please Reload the page',
                    type: 'blue',
                    typeAnimated: true
                });
        }
        var sampleType = $('#sampleType').val()
        if (SampleTable && sampleType && status) {
            $.ajax({
                url: 'getSampleCreatedAjax?sampleType=' + sampleType + '&status=' + status + '&pid=' + pid + '&cid=' + cid + '&did=' + did + '&aid=' + aid,
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
//                    console.log(data);
                    SampleTable.clear().draw(false);
                    if (data.length) {
                        $.each(data, function (i, data) {
                            var tmp = SampleTable.row.add([
                                (data.isSingleSampling == 'CSL' ? "<span style='color:mediumvioletred;border-color:mediumvioletred;' class='defFont'><i class='fas fa-flask'></i> CSL</span>" : (data.isSingleSampling == 'RND' ? "<span style='color:royalblue;border-color:royalblue;' class='defFont'><i class='fas fa-flask'></i> RND</span>" : "<span style='color:#3B1780;border-color:#3B1780;' class='defFont'><i class='fas fa-flask'></i> CSL/RND</span>")),
                                data.sampleId,
                                data.mstDept.mstCustomer.customerName,
                                data.mstDept.departmentName,
                                data.mstApp.appName,
                                data.tankNo,
                                data.mstEquip.equipmentName,
                                data.mstProd.proName,
                                (data.qtyDrawn.split(',').length > 1 ? parseInt(data.qtyDrawn.split(',')[0]) + parseInt(data.qtyDrawn.split(',')[1]) : data.qtyDrawn),
                                data.stringsamplecreatedDate,
                                data.stringsamplecreatedDate
                            ]).draw(false).node();
                            $(tmp).data('labCode', data.mstLab.labCode);
                            $(tmp).addClass('priorityLevel' + data.samplepriorityId);
                        });
                        /* $.each(data, function (i, v) {
                         i = i + 1;
                         tableSelector.find('tbody > tr:nth-child(' + i + ')').addClass('priorityLevel' + v.samplepriorityId);
                         });*/
                    } else {
                        $.alert({
                            title: 'No Data Found',
                            content: 'You don\'t have a sample related with this this filter',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        }
    });
    var sampleToTse = $('#sampleToTse').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Samples Created by TSE'
            },
            'colvis'
        ]});
    var sampleToCustomer = $('#sampleToCustomer').DataTable({"order": [],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Samples Send to Customer'
            },
            'colvis'
        ]});
    var STT2 = $('#sampleToTse');
    var STC2 = $('#sampleToCustomer');
    $(document).on('click', '#filterSearchfrLab', function () {
        var status = $(this).data('status');
        var pid = $('#prodFilter').val();
        var cid = $('#custFilter').val();
        var did = $('#deptFilter').val();
        var aid = $('#appFilter').val();
        var SampleTable = null;
        var tableSelector = null;
        switch (status) {
            case 3:
                tableSelector = STT2;
                SampleTable = sampleToTse;
                break;
            case 4:
                tableSelector = STC2;
                SampleTable = sampleToCustomer;
                break;
            default:
                $.alert({
                    title: 'Error',
                    content: 'Please Reload the page',
                    type: 'blue',
                    typeAnimated: true
                });
        }

        if (SampleTable && status) {
            $.ajax({
                url: 'getReceivedSampleAjax?status=' + status + '&pid=' + pid + '&cid=' + cid + '&did=' + did + '&aid=' + aid,
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
//                    console.log(data);
                    SampleTable.clear().draw(false);
                    if (data.length) {
                        $.each(data, function (i, data) {
                            var tmp = SampleTable.row.add([
                                (data.isSingleSampling == 'OTS' ? "<span style='color:mediumvioletred' class='defFont'><i class='glyphicon glyphicon-file'></i></span>" : "<span style='color:royalblue;' class='defFont'><i class='glyphicon glyphicon-book'></i></span>"),
//                              (data.isSingleSampling == '1' ? "<span class='defFont'>Single</span>" : "<span class='defFont'>Normal</span>"),
                                data.sampleId,
                                data.mstDept.mstCustomer.customerName,
                                data.mstDept.departmentName,
                                data.mstApp.appName,
                                data.tankNo,
                                data.mstEquip.equipmentName,
                                data.mstProd.proName,
                                data.qtyDrawn,
                                data.stringsamplecreatedDate,
                                data.stringsamplecreatedDate
                            ]).draw(false).node();
                            $(tmp).data('labCode', data.mstLab.labCode);
                            $(tmp).addClass('priorityLevel' + data.samplepriorityId);
                        });
                        /*
                        $.each(data, function (i, v) {
                            i = i + 1;
                            tableSelector.find('tbody > tr:nth-child(' + i + ')').addClass('priorityLevel' + v.samplepriorityId);
                        });*/
                    } else {
                        $.alert({
                            title: 'No Data Found',
                            content: 'You don\'t have a sample related with this selected filter',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        }
    });
///////////////////////////////////////CONTROL TABLE////////////////////////////////////////////////////////////////////////


    $('#UserTable').DataTable({"order": [], "pageLength": 15,
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'User List'
            },
            'colvis'
        ]});
    function executeCTRetrieve() {
        $.ajax({
            url: 'redirectController?url=controlTable',
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                $('#NotifyDaysLimit').find('td:nth-child(3)> .ins').val(data.NotifyDaysLimit);
                $('#NotifyDaysLimit').find('td:nth-child(3)> .ins').text(data.NotifyDaysLimit);
                $('#SystemMaintenanceFlag').find('td:nth-child(3)> .ins').val(data.SystemMaintenanceFlag);
                $('#SystemMaintenanceFlag').find('td:nth-child(3)> .ins').text(data.SystemMaintenanceFlag);
                $('#TseSampleFilterDaysLimit').find('td:nth-child(3)> .ins').val(data.TseSampleFilterDaysLimit);
                $('#TseSampleFilterDaysLimit').find('td:nth-child(3)> .ins').text(data.TseSampleFilterDaysLimit);
                $('#LabSampleFilterDaysLimit').find('td:nth-child(3)> .ins').val(data.LabSampleFilterDaysLimit);
                $('#LabSampleFilterDaysLimit').find('td:nth-child(3)> .ins').text(data.LabSampleFilterDaysLimit);
                $('#postponerestrictDays').find('td:nth-child(3)> .ins').val(data.postponerestrictDays);
                $('#postponerestrictDays').find('td:nth-child(3)> .ins').text(data.postponerestrictDays);
                $('#sendtoLABdateLimit').find('td:nth-child(3)> .ins').val(data.sendtoLABdateLimit);
                $('#sendtoLABdateLimit').find('td:nth-child(3)> .ins').text(data.sendtoLABdateLimit);
                $('#highpriorityDays').find('td:nth-child(3)> .ins').val(data.highpriorityDays);
                $('#highpriorityDays').find('td:nth-child(3)> .ins').text(data.highpriorityDays);
                $('#mediumpriorityDays').find('td:nth-child(3)> .ins').val(data.mediumpriorityDays);
                $('#mediumpriorityDays').find('td:nth-child(3)> .ins').text(data.mediumpriorityDays);
                $('#normalpriorityDays').find('td:nth-child(3)> .ins').val(data.normalpriorityDays);
                $('#normalpriorityDays').find('td:nth-child(3)> .ins').text(data.normalpriorityDays);
                $('#createrestrictDays').find('td:nth-child(3)> .ins').val(data.createrestrictDays);
                $('#createrestrictDays').find('td:nth-child(3)> .ins').text(data.createrestrictDays);
                $('#ServerLogsHost').find('td:nth-child(3)> .ins').val(data.ServerLogsHost);
                $('#ServerLogsHost').find('td:nth-child(3)> .ins').text(data.ServerLogsHost);
                $('#TseDashDaysLimit').find('td:nth-child(3)> .ins').val(data.TseDashDaysLimit);
                $('#TseDashDaysLimit').find('td:nth-child(3)> .ins').text(data.TseDashDaysLimit);
                $('#LabDashDaysLimit').find('td:nth-child(3)> .ins').val(data.LabDashDaysLimit);
                $('#LabDashDaysLimit').find('td:nth-child(3)> .ins').text(data.LabDashDaysLimit);
                $('#LabDashDaysLimit').find('td:nth-child(3)> .ins').text(data.LabDashDaysLimit);
                $('#dataRowRetrieveLimit').find('td:nth-child(3)> .ins').text(data.dataRowRetrieveLimit);
                $('#dataRowRetrieveLimit').find('td:nth-child(3)> .ins').val(data.dataRowRetrieveLimit);
            },
            error: function (error) {
                console.log(error);
            },
            complete: function () {
                $('body > div.modal-backdrop.in').remove();
            }
        });
    }
    if ($('#UserTable')[0]) {
        executeCTRetrieve();
    }
    $(document).on('click', '#evmode', function () {
        if ($(this).text().includes('Edit')) {
            $('span.ins').hide();
            $('input.ins').show();
            $(this).html('<i class="fa fa-exchange-alt"></i> View Mode');
        } else if ($(this).text().includes('View')) {
            $('input.ins').hide();
            $('span.ins').show();
            $(this).html('<i class="fa fa-exchange-alt"></i> Edit Mode');
        }
    });
    $('input.ins').change(function () {
        $('#updateCT').removeAttr("disabled");
        $('#updateCT').addClass('blinking');
    })

    $(document).on('click', '#updateCT', function () {
        var CTObj = {};
        $('#UserTable > tbody > tr').each(function (i, v) {
            CTObj[$(this).attr('id')] = $(this).find('input.ins').val();
        });
        var flag = true;
        Object.keys(CTObj).forEach(key => {
            let value = CTObj[key];
            if (value == "") {
                a = false
            }
        });
        if (flag) {
            $.ajax({
                url: 'redirectController?url=updateControlTable',
                type: 'POST',
                dataType: 'JSON',
                data: {json: JSON.stringify(CTObj)},
                success: function (data) {
//                    console.log(data);
                    executeCTRetrieve();
                    $('input.ins').show();
                    $('#updateCT').removeClass('blinking');
                    $('#updateCT').attr("disabled", true);
                },
                error: function (error) {
                    console.log(error);
                }
            });
        } else {
            $.alert({
                title: 'Invalid Input',
                content: 'Kindly fill all required fields',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $(document).on('click', '.closeIt', function () {
        $('.modal-backdrop.in').remove();
    });
    $(document).on('click', '#sendsampletoLAB > div:nth-child(10) > ul > li > a', function () {
        $('#labType').val($(this)[0].data('labType'));
//        console.log('changed!!');
    })

/////////////////////////////////////////////////// TseAdmin - Tse Access  ///////////////////////////////////////////////////////
    $(document).on('click', '#TseLabSubmit', function (e) {
        e.preventDefault();
        var empCode = $('#lab_newEmpCode').val()
        var labCode = $('#lab_labCode').val()
        if (empCode != null && labCode != null) {
            $.ajax({
                url: '/ServoCMP/TseAdmin/TseLabAccess?labCode=' + labCode + '&empCode=' + empCode,
                type: 'GET',
                dataType: 'JSON',
                success: function (data) {
//                    console.log(data);
                    $.alert({
                        title: 'Status',
                        content: data.modalMessage,
                        type: (data.msgClass == "text-success" ? "green" : "red"),
                        typeAnimated: true
                    });
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        } else {
            $.alert({
                title: 'Invalid Input',
                content: 'Kindly fill the required fields',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $(document).on('click', '.access_empCode', function (e) {
        e.preventDefault();
        $.ajax({
            url: '/ServoCMP/TseAdmin/TseLabAccessExist?For=All',
            type: 'GET',
            dataType: 'JSON',
            success: function (data) {
                var trow = "";
                var notAssign = "Not Assigned Yet";
                $('#modal_accessView').html("");
                $.each(data, function (i, v) {
                    trow += `<tr data-code='${v.mstTseUser.empCode}' data-lCode='${v.labCode}' data><td>${(v.mstTseUser.empName == null ? notAssign : v.mstTseUser.empName)}</td><td>${v.labCode}</td><td>${v.labName}</td><td class="text-center"><span class="text-danger glyphicon glyphicon-remove cursorOnHover removeTseLabMap" title="Remove the corresponding mapping"></span></td></tr>`;//
                    $('#modal_accessView').html(trow);
                });
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    });

    $(document).on('click', '.removeTseLabMap', function () {
        var tseEmpCode = $(this).parents('tr').data('code');
        var tseLabCode = $(this).parents('tr').data('lcode');
        if (tseEmpCode && tseLabCode) {
            $.ajax({
                url: '/ServoCMP/TseAdmin/TseLabAccessDel?empCode=' + tseEmpCode + '&labCode=' + tseLabCode,
                type: 'GET',
                dataType: 'JSON',
                success: function (data) {
                    $('#accessModal').modal('hide');
                    $.alert({
                        title: 'Status',
                        content: data,
                        type: 'green',
                        typeAnimated: true
                    });
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        }


    });

    $(document).on('click', '#sync_empCode', function () {
        $.confirm({
            title: 'Sync Details',
            content: 'Are you sure you want to Sync the Employee master table with Central Database?',
            type: 'red',
            typeAnimated: true,
            buttons: {
                Yes: {
                    btnClass: 'btn-success',
                    action: function () {
                        $.ajax({
                            url: '/ServoCMP/TseAdmin/TseAccessSync',
                            type: 'GET',
                            dataType: 'JSON',
                            success: function (data) {
                                if (data.indexOf("Succesfully") > -1) {
                                    $.alert({
                                        title: 'Successful',
                                        content: data,
                                        type: 'green',
                                        typeAnimated: true
                                    });
                                } else {
//                                    console.log(data)
                                    $.alert({
                                        title: 'Failure',
                                        content: 'Kindly contact IS Admin.',
                                        type: 'red',
                                        typeAnimated: true
                                    });

                                }
                            },
                            error: function (error) {
                                console.log(error.responseText);
                            }
                        });

                    }
                },
                No: {
                    btnClass: 'btn-red'
                }
            }
        });
    });

    /////////////////////////////////////////////////// TseAdmin - Product Category  //////////////////////////////////////////////
    var prodCatDT = $('#prodCatTable').DataTable({
        order: [[1, 'asc']],
        "dom": 'Bfrtip',
        "buttons": [
            {
                extend: 'excel',
                filename: 'Lab Access List'
            },
            'colvis'
        ]
    });
    $(document).on('change', '.getProdCatList', function () {
        if ($(this).val() != "") {
            $.ajax({
                async: false,
                url: '/ServoCMP/TseAdmin/productCategoryMapping?cat=' + $('#catIdList').val(),
                type: 'GET',
                dataType: 'JSON',
                success: function (data) {
                    prodCatDT.clear();
//              console.log(data);
//              $("#prodCatTable > tbody").append(`<tr><td>${v.cat_id}</td><td>${v.labCode}</td><td>${v.labName}</td><td>${v.testName}</td></tr>`);
                    $.each(data, function (i, v) {
                        prodCatDT.row.add([
                            i + 1,
                            v.cat_name,
                            v.labCode,
                            v.labName,
                            v.testName
                        ]).draw(false);
                    });
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        } else {
            $.alert({
                title: 'Invalid Input',
                content: 'Kindly Select an category',
                type: 'red',
                typeAnimated: true
            });
        }
    });
    $(document).on('click', '#modal_addProductCat', function () {
        if ($('#prodCat_catId').val() != null && $('#prodCat_labCode').val() != "" && $('#prodCat_testId').val() != null) {
            var type = "";
            if ($('#crudProdCategory').prop('checked')) {
                type = "Add";
            } else {
                type = "Update";
            }
            $.ajax({
                url: '/ServoCMP/TseAdmin/AddProductCategory?cat_id=' + $('#prodCat_catId').val() + '&labCode=' + $('#prodCat_labCode').val() + '&testIds=' + $('#prodCat_testId').val() + '&type=' + type,
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
                    $('#prodCatModal').modal('hide');
                    showRawResponseDialog(data);
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        }
    });
    $('#crudProdCategory').bootstrapToggle({
        on: 'Add',
        off: 'Update'
    });
    function initSelect2() {
        $('#prodCat_catId').select2({
            multiple: false,
            placeholder: "Enter Category Name",
            tags: true,
            allowClear: true,
            createTag: function (tag) {
                found = true;
                if ($("#prodCat_catId option").length != 0) {
                    $("#prodCat_catId option").each(function () {
                        if ($.trim(tag.term).toUpperCase() === $.trim($(this).val())) {
                            $.alert({
                                title: 'Invalid Input',
                                content: 'Value already Exist in the below list',
                                type: 'red',
                                typeAnimated: true
                            });
                            found = true;
                            return found;
                        } else {
                            found = false;
                            return found;
                        }
                    });
                } else {
                    found = false;
                }
                if (!found) {
                    return {id: tag.term, text: tag.term, isNew: true};
                } else {
                    return null;
                }
            }
        });
    }
    $('#crudProdCategory').on('change', function () {
        if ($(this).prop('checked')) {
            $('#prodCat_catId option').attr('disabled', true);
            initSelect2();
        } else {
            $('#prodCat_catId option').removeAttr('disabled');
            initSelect2();
        }
    });
    if ($('#prodCat_labCode')[0]) {
        $('#prodCat_labCode').select2();
        $.ajax({
            async: false,
            url: '/ServoCMP/TseAdmin/getLabList',
            type: 'GET',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                $.each(data, function (i, v) {
                    $('#prodCat_labCode').append('<option value=' + v.labCode + '>' + v.labName + '</option>')
                });
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    }

    if ($('#prodCat_testId')[0]) {
        $('#prodCat_testId').select2()
        $.ajax({
            async: false,
            url: '/ServoCMP/TseAdmin/getTestParams',
            type: 'GET',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                if (typeof data == "object") {
//                    console.log(data);
                    $('#prodCat_testId').empty();
                    $.each(data, function (key, val) {
                        $('#prodCat_testId').append("<option value=" + val.testId + " data-qty=" + val.sampleqty + " >" + val.testName + "</option>").trigger('change');
                    });
                }

            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    }

    if ($('#prodCat_catId')[0]) {
        $('#prodCat_catId').select2({
            multiple: false,
            placeholder: "Enter Category Name",
            tags: true,
            allowClear: true,
            createTag: function (tag) {
                found = false;
                $("#prodCat_catId option").each(function () {
                    if ($.trim(tag.term).toUpperCase() === $.trim($(this).val())) {
                        $.alert({
                            title: 'Invalid Input',
                            content: 'Value already Exist in the below list',
                            type: 'red',
                            typeAnimated: true
                        });
                        found = false;
                        return found;
                    } else {
                        found = true;
                        return found;
                    }
                });
                if (found) {
                    return {id: tag.term, text: tag.term, isNew: true};
                } else {
                    return null;
                }
            }
        });
        $.ajax({
            async: false,
            url: '/ServoCMP/TseAdmin/getCategory',
            type: 'GET',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                if (typeof data == "object") {
                    console.log(data);
                    $('#prodCat_catId').empty();
                    $.each(data, function (key, val) {
                        $('#prodCat_catId').append("<option value=" + val.cat_id + " disabled >" + val.cat_name + "</option>").trigger('change');
                    });
                }

            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    }

    /////////////////////////////////////////// Test Status Lab Wise /////////////////////////////////////////////////////
    $('#labAccess').DataTable({
        columnDefs: [{
                orderable: false,
                className: 'select-checkbox',
                targets: 0
            }],
        select: {
            style: 'os',
            selector: 'td:first-child'
        },
        order: [[1, 'asc']]
    })
    $('#testLabName').select2();
    $('.selectCustom').select2({
        placeholder: "Select...",
        allowClear: true
    });
    var testLabDT = $('#testLabTable').DataTable();
    $(document).on('change', '#testLabName', function () {
        var labCode = $('#testLabName').val();
        if (labCode != null || labCode != "") {
            $.ajax({
                url: '/ServoCMP/Tse/LabTestSpecsServlet?labCode=' + labCode,
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
                    if (data.length > 0) {
                        testLabDT.clear();
                        $.each(data, function (i, val) {
                            testLabDT.row.add([
                                val.testId,
                                val.testName,
                                val.updatedBy,
                                (val.active == '1' ? '<span class="dot-green"></span>' : '<span class="dot-red"></span>')
                            ]).draw(false);
                        });
                    } else {
                        $.alert({
                            title: 'No Mapping Found',
                            content: 'Kindly Contact IS Administrator',
                            type: 'red',
                            typeAnimated: true
                        });
                    }
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        }

    });
    $('#modal_makeName').select2();
    $(document).on('click', '#updateEquip', function () {
        $('#modal_makeName').val($(this).data('makeId'));
        $('#modal_equipId').val($(this).data('id'));
        $('#modal_equipName').val($(this).parents('tr').find('td:nth-child(1)').text());
        $('#UpdateEquipmentModal').modal('show');
    });

    $(document).on('click', '#modal_EquipmentSubmit', function () {
        var makeId = $('#modal_makeName').val();
        var equipmentId = $('#modal_equipId').val();
        var equipName = $('#modal_equipName').val();
        if (makeId && equipmentId) {
            $.ajax({
                url: '/ServoCMP/TseAdmin/UpdateEquipment',
                type: 'POST',
                data: {
                    makeId: makeId,
                    equipId: equipmentId,
                    equipName: equipName
                },
                dataType: 'JSON',
                success: function (data) {
                    $('#UpdateEquipmentModal').modal('hide');
                    showRawResponseDialog(data);
                    window.reload();
                },
                error: function (error) {
                    console.log(error.responseText);
                }
            });
        }
    });
});