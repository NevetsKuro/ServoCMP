$(function(){
    $.ajaxSetup({
        headers:{
            'CSRFToken': $('#csrftoken').val()
        }
    })
})
$(document).ready(function () {

    function addSapDetails() {
        var sapObj, sapArr = [];
        $('#SAPS > tr').each(function () {
            sapObj = new Object();
            sapObj.code = $(this).find('.SAPcode').val();
            sapObj.name = $('#customerCode').val();
            sapArr.push(sapObj);
        });
        return sapArr;
    }

    var SAPDataTable = $('#SAPTable').DataTable({
        "lengthChange": false,
        "ordering": false,
        "searching": false
    });

    $('#CustCode').select2({
        placeholder: 'Select Customer..',
        allowClear: true,
        multiple: false
    });

    $(document).on('click', '#addExSAP', function () {
        var custCodeText = $('#CustCode option:selected').text();
        $('#SAPS').append(`
            <tr>
                <td class="floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                    <input id="defCustCode" type="text" minlength="8" maxlength="10" placeholder="Enter Customer Code" class="custNoVal custSapNoVal form-control" disabled>
                </td>
                <td class="floating-label-form-group floating-label-form-group-with-value">
                    <input data-field="SAP Number" type="text" placeholder="Enter SAP code" class="SAPcode form-control isNumber">
                </td>
                <td class="text-danger">
                    <span class="glyphicon glyphicon-remove removeRow1"></span>
                </td>
            </tr>
        `);
        $('.custNoVal').val(custCodeText);
    });

    $(document).on('click', '#openSapModal', function () {
        var custCodeText = $('#CustCode option:selected').text();
        var custCode = $('#CustCode').val();
        $('#SAPS').not(':first').remove();
        if (custCode) {
            $('#defCustCode').val(custCodeText);
            $('#customerCode').val(custCode);
        } else {
            $('#defCustCode').val("");
            $('#customerCode').val("");
        }
        if (custCode) {
            $('#AddSAPcode').modal('show');
        } else {
            swal('Select a Customer');
        }
    });

    $(document).on('click', '.editSapCode', function () {
        var custCodeText = $('#CustCode option:selected').text();
        var custCode = $('#CustCode').val();
        var sapcode = $(this).find('p:nth-child(2)').html();
        $('#defCustCode2').val(custCodeText);
        $('#customerCode2').val(custCode);
        $('#defSAPCode2').val(sapcode);
        $('#PrevsapCode').val(sapcode);
        $('#EditSAPcode').modal('show');
    });


    $(document).on('change', '#CustCode', function () {
        var custCode = $('#CustCode').val();
        if (custCode) {
            $.ajax({
                url: '/ServoCMP/Tse/MappingCustSAP?action=getSAP&custID=' + custCode,
                type: 'POST',
                dataType: 'JSON',
                success: function (data) {
                    $('#existSAP').empty();
                    if (data.length) {
                        $.each(data, function (index, value) {
                            console.log(index + ": " + value.code);
                            $('#existSAP').append(`
                                <div class="editSapCode" style="background-color: bisque;width: fit-content;padding: 11px;line-height: 12px;border-radius: 12px;box-shadow: 3px 2px 10px black;font-family: monospace;color: black;float:left;margin:7px" title="Click to edit">
                                    <p>SAP code #${index + 1}</p><p style="font-size: 20px;text-align: center">${value.code}</p>
                                </div>
                            `);
                        });
                    } else {
                        $('#existSAP').append(`
                            <div class="ress" style="
                                border: 4px dashed darkgrey;
                                border-radius: 12px;
                                margin: 7px;
                                text-align: center;
                                font-family: monospace;
                                font-size: x-large;
                                font-weight: bolder;
                                color: darkgray;
                            ">No SAP codes found...</div>
                        `);
                    }
                },
                error: function (error) {
                    console.log(error);
                }
            });
        }
    });

    $(document).on('click', '#getSAPDetails', function () {
        var custCode = $('#CustCode').val();
        $.ajax({
            url: '/ServoCMP/Tse/MappingCustSAP?action=getSAPDetails&custID=' + custCode,
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
                data ? showRawResponseDialog(data) : console.log(data);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $(document).on('click', '#addSapDetails', function () {
        var sapDetails = addSapDetails();
        console.log(sapDetails);
        var flag = true;
        $('.SAPcode').each(function () {
            if ($(this).val() == "") {
                flag = false;
                return false;
            }
        });
        if (flag) {
            $.ajax({
                url: '/ServoCMP/Tse/MappingCustSAP?action=addSAP',
                type: 'POST',
                dataType: 'JSON',
                data: {json: JSON.stringify(sapDetails)},
                success: function (data) {
                    $('#AddSAPcode').modal('hide');
                    data ? showRawResponseDialog(data) : console.log(data);
                    $('#CustCode').trigger('change');
                },
                error: function (error) {
                    console.log(error);
                }
            });
        }else{
            $.alert({
                title: 'Empty Field Value',
                content: 'Please fill all available fields',
                type: 'red',
                typeAnimated: true
            });
        }
    });

    $(document).on('click', '#editSapDetails', function () {

        var custCode = $('#customerCode2').val();
        var SAPCode = $('#defSAPCode2').val();
        var PrevsapCode = $('#PrevsapCode').val();
        $.ajax({
            url: '/ServoCMP/Tse/MappingCustSAP?action=updateSAP&custCode=' + custCode + '&sapCode=' + SAPCode + '&PrevsapCode=' + PrevsapCode,
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
                $('#EditSAPcode').modal('hide');
                console.log(data);
                data ? showRawResponseDialog(data) : console.log(data);
                $('#CustCode').trigger('change');
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $(document).on('click', '#getSummary', function () {
        console.log('fetching Summary');
        var empCode = $(this).attr('data-emp-code');
        $.ajax({
            url: '/ServoCMP/Tse/MappingCustSAP?action=getSummary&empCode=' + empCode,
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {

                SAPDataTable.clear();
                $.each(data, function (index, value) {
                    SAPDataTable.row.add([
                        value.name,
                        value.code,
                        value.updated_by,
                        value.update_date
                    ]).draw(false);
                });
                console.log('... fetched.');
            },
            error: function (error) {
                console.log(error);
                console.log('error');
            }
        });
    });
});