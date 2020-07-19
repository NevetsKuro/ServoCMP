$(document).ready(function () {
//VALIDATION FOR TSE FORMS
    $('#sendsampletoLAB').bootstrapValidator({
        framework: 'bootstrap',
        err: {
            container: function ($field, validator) {
                return $field.next('.errmsgDropdown');
                //return $field.closest('.row').next('.row').find('.' + fclassName);
            }
        },
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            qtyDrawn: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Sample Drawn (in ml).'
                    }
                }
            },
            qtyDrawnDate: {
                excluded: false,
                validators: {
                    notEmpty: {
                        message: 'Please Enter Sample Drawn Date.'
                    }, date: {
                        format: 'DD-MM-YYYY',
                        message: 'Enter Date in format DD-MM-YYYY'
                    }
                }

            }, topupQty: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Top up Quantity.'
                    }
                }

            }, runningHrs: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Oil Running Hours'
                    }
                }
            }, labCode: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Lab to Send Sample.'
                    }
                }
            }, priorityId: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Priority of Sample.'
                    }
                }
            }, testIds: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Priority of Sample.'
                    }
                }
            }
        }
    }).on('err.validator.fv', function (e, data) {
        console.log("Error");
        data.element
                .data('fv.messages')
                .find('.help-block[data-fv-for="' + data.field + '"]').hide()
                .filter('[data-fv-validator="' + data.validator + '"]').show();
    }).on('success.form.fv', function (e) {
        e.preventDefault();
    });

    $('#editsendsampletoLAB').bootstrapValidator({
        framework: 'bootstrap',
        err: {
            container: function ($field, validator) {
                return $field.next('.errmsgDropdown');
            }
        },
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            qtyDrawn: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Sample Drawn (in ml).'
                    }
                }
            },
            qtydrawnDate: {
                excluded: false,
                validators: {
                    notEmpty: {
                        message: 'Please Enter Sample Drawn Date.'
                    }, date: {
                        format: 'DD-MM-YYYY',
                        message: 'Enter Date in format DD-MM-YYYY'
                    }
                }

            }, topupQty: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Top up Quantity.'
                    }
                }

            }, labCode: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Lab to Send Sample.'
                    }
                }
            }, samplepriorityId: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Priority of Sample.'
                    }
                }
            }, testIds: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Priority of Sample.'
                    }
                }
            }
        }
    }).on('err.validator.fv', function (e, data) {
        data.element
                .data('fv.messages')
                .find('.help-block[data-fv-for="' + data.field + '"]').hide()
                .filter('[data-fv-validator="' + data.validator + '"]').show();
    }).on('success.form.fv', function (e) {
        e.preventDefault();
    });

    $('#postponedSample').bootstrapValidator({
        framework: 'bootstrap',
        err: {
            container: function ($field, validator) {
                return $field.next('.errmsgDropdown');
            }
        },
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            postponetillDate: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Postponed Date.'
                    }
                }
            }, postponeReason: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Reason.'
                    }
                }
            }
        }
    }).on('err.validator.fv', function (e, data) {
        data.element
                .data('fv.messages')
                .find('.help-block[data-fv-for="' + data.field + '"]').hide()
                .filter('[data-fv-validator="' + data.validator + '"]').show();
    }).on('success.form.fv', function (e) {
        e.preventDefault();
    });


    //VALIDATION FOR LAB FORMS
    $('#receivesampleForm').bootstrapValidator({
        excluded: [':disabled', ':hidden', ':not(:visible)'],
        framework: 'bootstrap',
        err: {
            container: function ($field, validator) {
                return $field.next('.errmsgDropdown');
            }
        },
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            labreceiveDate: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Sample Received Date.'
                    }
                }
            }, delayReason: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Reason, when Parameters Result Expected Date is beyond 5 Days'
                    }
                }
            }, Equipment: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Malfunctioning Equipment'
                    }
                }
            }
        }
    }).on('click', 'button[data-toggle]', function () {
        var $target = $($(this).attr('data-toggle'));
        $target.toggle();
        if (!$target.is(':visible')) {
            // Enable the submit buttons in case additional fields are not valid
            $('#receivesampleForm').data('formValidation').disableSubmitButtons(false);
        }
    });

    $('#sendtestResulttoTSE').bootstrapValidator({
        framework: 'bootstrap',
        err: {
            container: function ($field, validator) {
                return $field.next('.errmsgDropdown');
                //return $field.closest('.row').next('.row').find('.' + fclassName);
            }
        },
        fields: {
            evaluatetestValues: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Sample Result Value.'
                    }
                }
            }, selectevaluatetestValues: {
                validators: {
                    notEmpty: {
                        message: 'Please Select value of Test.'
                    }
                }
            }, finalTestRemarks: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Final Remarks.'
                    }
                }
            }
        }
    }).on('err.validator.fv', function (e, data) {
        data.element
                .data('fv.messages')
                .find('.help-block[data-fv-for="' + data.field + '"]').hide()
                .filter('[data-fv-validator="' + data.validator + '"]').show();
    }).on('success.form.fv', function (e) {
        e.preventDefault();
    });



    $('#addCustomer').bootstrapValidator({
        excluded: [':hidden'],
        framework: 'bootstrap',
        err: {
            container: function ($field, validator) {
                return $field.next('.errmsgDropdown');
            }
        },
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            indId: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Industry Name.'
                    }
                }

            },
            customerName: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter Customer Name.'
                    }
                }
            },

            tseEmpCode: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter New TSE employee Code.'
                    }
                }

            },
            ctrlEmpCode: {
                validators: {
                    notEmpty: {
                        message: 'Please Enter controlling employee Code.'
                    }
                }
            }
        }
    }).on('err.validator.fv', function (e, data) {
        data.element
                .data('fv.messages')
                .find('.help-block[data-fv-for="' + data.field + '"]').hide()
                .filter('[data-fv-validator="' + data.validator + '"]').show();
    }).on('success.form.fv', function (e) {
        e.preventDefault();
    });

    $('#addSample').bootstrapValidator({
        framework: 'bootstrap',
        err: {
            container: function ($field, validator) {
                return $field.nextAll('.errmsgDropdown').first();
            }
        },
        fields: {
            mstIndustry: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Industry.'
                    }
                }
            }, mstCustomer: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Customer.'
                    }
                }
            }, mstDepartment: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Department.'
                    }
                }
            }, mstApplication: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Application.'
                    }
                }
            }, applicationDesc: {
                validators: {
                    notEmpty: {
                        message: 'Please enter Application Description.'
                    }
                }
            }, mstEquipment: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Equipment.'
                    }
                }
            }, mstTank: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Tank No.'
                    }
                }
            }, sampleFreq: {
                validators: {
                    notEmpty: {
                        message: 'Enter Sample Frequency.'
                    }
                }
            }, mstProduct: {
                validators: {
                    notEmpty: {
                        message: 'Please Select Product.'
                    }
                }
            }, sumpCapacity: {
                validators: {
                    notEmpty: {
                        message: 'Enter Sump Capacity.'
                    }
                }
            }
        }
    }).on('err.validator.fv', function (e, data) {
        data.element
                .data('fv.messages')
                .find('.help-block[data-fv-for="' + data.field + '"]').hide()
                .filter('[data-fv-validator="' + data.validator + '"]').show();
    }).on('success.form.fv', function (e) {
        e.preventDefault();
    });
});