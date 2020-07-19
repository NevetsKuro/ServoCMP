<%-- 
    Document   : headTag
    Created on : 09 Oct, 2016, 6:39:11 PM
    Author     : Manish Jangir
--%>
<head>
    <title>${param.title}</title>
    <link rel="shortcut icon" type="image/ico" href="/favicon.ico" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="${sessionScope.csrfToken}">
    <meta name="helloician" content="${sessionScope.persLink}">
    <link rel="shortcut icon" href="/ServoCMP/resources/images/favicon_1.ico" type="image/x-icon">
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/bootstrap-datepicker/css/bootstrap-datepicker.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/formValidation.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/site.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/select2.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/floating-labels.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/formValidation.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/jquery-confirm.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/rowReorder.dataTables.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/customCss.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/buttons.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/Vendors/fontawesome/css/all.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap-checkbox.css" rel="stylesheet" type="text/css"/>
    <!--<link href="https://cdn.datatables.net/buttons/1.5.2/css/buttons.dataTables.min.css" rel="stylesheet" type="text/css"/>-->
    <!--<link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">-->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap-toggle.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/buttons.dataTables.min.css" rel="stylesheet">
    
    <script src="${pageContext.request.contextPath}/resources/js/jquery-2.2.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.unobtrusive-ajax.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-ui.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/siteScript.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.dataTable.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/dataTables.bootstrap.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap-filestyle.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/bootstrap-datepicker/locales/bootstrap-datepicker.en-GB.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/select2.full.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/floating-labels.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/validator.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/formValidation.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/framework/bootstrap.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/lubesFormValidation.js" type="text/javascript"></script> 
    <script src="${pageContext.request.contextPath}/resources/js/jquery-barcode-last.min.js" type="text/javascript"></script> 
    <script src="${pageContext.request.contextPath}/resources/js/dataTables.select.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-confirm.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap-waitingfor.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/dataTables.rowReorder.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/mappingCustSap.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/sweetalert.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/lodash.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap-toggle.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/dataTables.buttons.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/buttons.flash.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/buttons.colVis.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/buttons.html5.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/buttons.print.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jszip.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/pdfmake.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/vfs_fonts.js" type="text/javascript"></script>
<!--    <script src="https://cdn.datatables.net/buttons/1.5.2/js/dataTables.buttons.min.js" type="text/javascript"></script>
    <script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.flash.min.js" type="text/javascript"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js" type="text/javascript"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/pdfmake.min.js" type="text/javascript"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/vfs_fonts.js" type="text/javascript"></script>
    <script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.html5.min.js" type="text/javascript"></script>
    <script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.print.min.js" type="text/javascript"></script>
    <script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.colVis.min.js" type="text/javascript"></script>-->
    
    
    
<!--    <link href="${pageContext.request.contextPath}/resources/css/material-dashboard.min.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/resources/js/highchart.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/data.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/drilldown.js" type="text/javascript"></script>-->
        <!--<script src="https://code.highcharts.com/modules/drilldown.js" type="text/javascript"></script>-->
</head>
