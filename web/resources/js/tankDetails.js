/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$.ajaxSetup({
    beforeSend: function (xhr, settings) {
        if (!/^(GET|HEAD|OPTIONS|TRACE)$/i.test(settings.type)) {
            xhr.setRequestHeader("X-CSRFToken", csrftoken);
        }
    }
});

$(document).ready(function () {

    $(document).on('click', '#excelDownload', function (e) {
        e.preventDefault();
        if ($('#tseOfficer option:selected').val() == "101") {
            $('#addTank').submit();
        } else {
            $.alert({
                title: 'Excel Download is only permitted for downloading MIS System Report',
                content: 'i.e. Selecting "All" Option in Tse Officer',
                type: 'red',
                typeAnimated: true
            });
        }
    });
});
function getCustomer1(indId) {
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
        url: '/ServoCMP/TseAdmin/GetCustomers',
        dataType: 'json',
        data: {
            Industry: indId
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

function getIndustryByTse(empCode) {
    $('#sampleIndustry option').remove();
    $('#sampleCustomer option').remove();
    if ($('#tseOfficer').val() == '' || $('#tseOfficer').val() === null) {
        $.alert({
            title: 'Select Fields',
            content: 'Select TSE Officer.',
            type: 'red',
            typeAnimated: true
        });
    } else {
        $.ajax({
            url: 'redirectController?url=getIndustryByTse&empCode=' + empCode,
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
                if (data.length > 0) {
                    jQuery.each(data, function (i, val) {
                        $('#sampleIndustry').append($('<option>', {value: val.indId}).text(val.indName));
                    });
                    $('#sampleIndustry').val("");
                }
            },
            error: function (error) {
                console.log(error)
            }
        });
    }
}

function getCustomerDept1() {
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
function getCustomerEquipment1() {
    $('#sampleEquipment option').remove();
    $.ajax({
        type: 'GET',
        url: '/ServoCMP/TseAdmin/GetCustomerEquipment',
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
function getProduct1()
{
    if ($('#prodId').val() !== '' && $('#prodId').val() !== null && $('#prodId').val().length > 2) {
        $('#sampleProduct option').remove();
        $.ajax({
            type: 'post',
            url: '/ServoCMP/TseAdmin/getProduct',
            dataType: 'JSON',
            data: {
                prodId: $('#prodId').val()
            },
            success: function (data) {
                //$('#sampleProduct').append($('<option>', {value: ''}).text("Enter New Tank No *"));
                if (data.constructor === Array) {
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
function getTank1(operation) {

    if ($('#sampleIndustry').val() !== '' && $('#sampleIndustry').val() !== null &&
            $('#sampleCustomer').val() !== '' && $('#sampleCustomer').val() !== null &&
            $('#sampleDepartment').val() !== '' && $('#sampleDepartment').val() !== null &&
            $('#sampleApplication').val() !== '' && $('#sampleApplication').val() !== null &&
            $('#sampleEquipment').val() !== '' && $('#sampleEquipment').val() !== null &&
            $('#sampleProduct').val() !== '' && $('#sampleProduct').val() !== null) {
        $('#sampleTankNo option').remove();
        $.ajax({
            type: 'GET',
            url: '/ServoCMP/TseAdmin/GetTank',
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
}
var tankTableDT = $('#tankTable').DataTable({
    "order": [],
    "columnDefs": [
        {"className": "dt-head-center", "targets": "_all"}
    ],
    "dom": 'Bfrtip',
    "buttons": [
        {
            extend: 'excel',
            filename: 'All Equipment master Tse-Wise'
        },
        'colvis'
    ]
});
function getTankSummary1() {
    console.log($('#sampleCustomer option:selected').val());
    if ($('#tseOfficer option:selected').val() != "101") {
        $.ajax({
            type: "GET",
            url: "/ServoCMP/TseAdmin/GetTank",
            data: {
                tseCode: $('#tseOfficer option:selected').val(),
                industry: $('#sampleIndustry option:selected').val(),
                customer: $('#sampleCustomer option:selected').val()
            },
            dataType: "json",
            success: function (data) {
                tankTableDT.clear().draw();
                if (data.constructor === Array) {
                    jQuery.each(data, function (i, val) {
                        var newRow = "";
//                        var newRow = $('<tr><td class="text-center"></td><td></td><td></td><td></td><td></td><td class="text-center"></td><td></td><td class="text-center"></td><td class="text-center"></td><td></td></tr>');
//                        newRow.children().eq(0).html(val.sampleFreq == '1' ? "<span style='color:mediumvioletred' class='defFont'><i class='glyphicon glyphicon-file'></i></span>" : "<span style='color:royalblue;' class='defFont'><i class='glyphicon glyphicon-book'></i></span>");
//                        newRow.children().eq(1).text(val.deptName);
//                        newRow.children().eq(2).text(val.applName);
//                        newRow.children().eq(3).text(val.equipName);
//                        newRow.children().eq(4).text(val.proName);
//                        newRow.children().eq(5).text(val.tankNo);
//                        newRow.children().eq(6).text(val.tankDesc);
//                        newRow.children().eq(7).text(val.strPrevDate);
//                        newRow.children().eq(8).text(val.strNxtDate);
//                        newRow.children().eq(9).text(val.applDesc);
                        newRow = tankTableDT.row.add([
                            val.OneTimeCheckbox == '1' ? "<span style='color:mediumvioletred' class='defFont'><i class='glyphicon glyphicon-file'></i></span><span style='display:none;'>1</span>" : "<span style='color:royalblue;' class='defFont'><i class='glyphicon glyphicon-book'></i></span><span style='display:none;'>2</span>",
                            val.indName,
                            val.applName,
                            val.custName,
                            val.deptName, //location & sap code
                            val.equipName,
                            val.mstEquipment.mstmake.makeName ? val.mstEquipment.mstmake.makeName : "",
                            val.proName,
                            val.tankNo,
                            val.capacity
                        ]).draw(true).node();
                        $(newRow).data('strNxtDate', val.strNxtDate);
                        $(newRow).data('strPrevDate', val.strPrevDate);
                        $(newRow).data('capacity', val.capacity);
                        $(newRow).data('tankNo', val.tankNo);
                        $(newRow).data('tankId', val.tankId);
                        $(newRow).data('updatedBy', val.updatedBy);
                        $(newRow).data('updatedDateTime', val.updatedDateTime);
                        $(newRow).data('tankDesc', val.tankDesc);
                        $(newRow).data('applDesc', val.applDesc);
                        $(newRow).data('lastOilChange', val.lastOilChange);
                        $(newRow).data('sampleFreq', val.sampleFreq);
//                        newRow.appendTo($('#tankTable tbody'));
                        tankTableDT.draw(true);
                    });
//                    tankTableDT = $('#tankTable').DataTable({"destroy": true,"order": []});
                } else {
//                    $('<tr><td colspan="10" class="text-center">' + data + '</td></tr>').appendTo($('#tankTable tbody'));
                }
            },
            error: function (data) {
                $('<tr><td colspan="10" class="text-center">' + data + '</td></tr>').appendTo($('#tankTable tbody'));
            }
        });
        $('#tankTableDiv').show();
    } else {
        $.alert({
            title: 'Not Allowed',
            content: 'When "All" Option is selected only "Get All System Details" is permitted.',
            type: 'red',
            typeAnimated: true
        });
    }
}
$(document).on('click', '#tankTable > tbody > tr', function () {
    var tr = $(this);
    $('input[name="TankNo"]').val(tr.data('tankNo'));
    $('input[name="TankDesc"]').val(tr.data('tankDesc'));
    $('input[name="CapaTank"]').val(tr.data('capacity'));
    $('input[name="AppDesc"]').val(tr.data('applDesc'));
    $('input[name="PrevDate"]').val(tr.data('strPrevDate'));
    $('input[name="NextDate"]').val(tr.data('strNxtDate'));
    $('input[name="Freq"]').val(tr.data('sampleFreq'));
    $('input[name="OilChan"]').val(tr.data('lastOilChange'));
    $('input[name="tankId"]').val(tr.data('tankId'));
    $('#IndsAtr').text(tr.find('td:nth-child(2)').text());
    $('#CustAtr').text(tr.find('td:nth-child(4)').text());
    $('#DeptAtr').text(tr.find('td:nth-child(5)').text());
    $('#ApplAtr').text(tr.find('td:nth-child(3)').text());
    $('#EquipAtr').text(tr.find('td:nth-child(6)').text());
//    $('#ProdAtr').text(tr.find('td:nth-child(7)').text());
    $('#ProdAtr').text(tr.find('td:nth-child(8)').text());
    var params = $('#exist-test-dropdown').parent().html();
    $('#paramss').html(params);
    $('#paramss > #exist-test-dropdown').attr('disabled');
    (tr.data('sampleFreq') == '0' || tr.data('sampleFreq') == null ? $('#titleSample').html('One-Time Sample') : $('#titleSample').html('Servo Monitoring Sample'))
    $('#showTankDetails').modal('show');
});

$(document).on('click', '#changeTankSubmit', function () {
    var freq = $('#NewFreq').val();
    var nexDate = $('input[name="NewNextSamDate"]').val()
    if (freq && nexDate) {
        $.ajax({
            url: '/ServoCMP/TseAdmin/UpdateTank2',
            type: 'GET',
            dataType: 'JSON',
            data: {
                tankid: $('#tankId').val(),
                freq: $('#NewFreq').val(),
                nexSampleDate: $('input[name="NewNextSamDate"]').val()
            },
            success: function (data) {
                if (data.indexOf('fully')) {
                    $.alert({
                        title: 'Update Status',
                        content: data,
                        type: 'green',
                        typeAnimated: true
                    });
                } else {
                    $.alert({
                        title: 'Update Status',
                        content: data,
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
