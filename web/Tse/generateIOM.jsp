<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="View IOM"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <script src="${pageContext.request.contextPath}/resources/js/jspdf.debug.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jspdf.plugin.autotable.js" type="text/javascript"></script>
<!--            <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/2.3.5/jspdf.plugin.autotable.js"></script>-->
        <div class="container-fluid" id="content" style="background-color: white;">
            <form id="idGenerateIOMForm" action="${pageContext.request.contextPath}/Tse/ManageIOM" class="form-inline generateIOM1" method="POST" role="form" >
                <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                <h4 class="text-center text-info" style="text-decoration: underline">View IOM</h4>
                <div id="IOMPrint">
                    <p style="text-align:center;"><img src="../resources/images/indianoil-logo.png" alt=""/></p>
                    <h4 class="text-center"><b>IOM - FOR USED OIL SAMPLES</b></h4>

                    <h5 class="text-center text-info">
                        <span style="float: left;"><b><u>TO</u></b></span>  <span style="float: right;"><b><u><label class="dateIOM" style="float: right;">Date:<b>${Date}</b></label></u></b></span>
                        <br/>
                        <span class="labAuthority" style="float: left;"><b>${mstLabDetail.labAuthority},</b></span>
                        <br/>
                        <span class="labName" style="float: left;"><b>${mstLabDetail.labName}</b></span>
                        <br/>
                        <br/>
                    </h5>
                    <span>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Please find below the list of oil samples being sent to CSL for testing. Request to do the needful and upload the results at the earliest.
                    </span>

                    <label>Ref No<strong>(Max:40)</strong> &nbsp;:&nbsp;&nbsp;</label><b><input name="nameIOMrefNO" id="idIOMrefNO" value="" Style="width:20%" placeholder="Enter your Ref No(Max: 45 chars)" class="form-control upperCase" maxlength="40" required></b>

                    <br/>
                    <br/>
                    <table id="iomDataTb1" class="table table-bordered table-striped" cellspacing="0" width="100%">
                        <thead style="font-size: x-small;" >
                            <tr>
                                <th>Sr. No</th>
                                <th>SAMPLE ID</th>
                                <th>CUSTOMER NAME</th>
                                <th>EQUIPMENT</th>
                                <th>PRODUCT</th>
                                <th>DATE OF SAMPLING</th>
                                <th style="display: none;" > SUMP CAPACITY</th>
                                <th style="display: none;"> RUNNING HRS</th>
                                <th>QTY. DRAWN(ml.)</th>
                                <th>TEST PARAMETERS</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${IOMSampleList}" var="data" varStatus="count">
                                <tr style="font-size:small" >
                                    <th>${count.count}</th>
                                    <td class="text-center">
                                        ${data.sampleId}
                                    </td>
                                    <td class="text-center">${data.mstDept.mstCustomer.customerName}</td>
                                    <td class="text-center">${data.mstEquip.equipmentName}</td>
                                    <td class="text-center">${data.mstProd.proName}</td>
                                    <td class="text-center">${data.stringsampledrawnDate}</td>
                                    <td class="text-center">${data.mstProd.proCapacity}</td>
                                    <td class="text-center" style="display: none;" >${data.runningHrs}</td>
                                    <td class="text-center" style="display: none;" >${data.qtyDrawn}</td>
                                    <td class="text-center">${data.testParamNames}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>


                    <br/>
                    <br/>
                    <div class="text-right">
                        <span class="empName" style="float: right;"><b>${sUser.sEMP_NAME}</b></span>
                        <br/>
                        <span class="designDesc" style="float: right;"><b>${sUser.sDESIGN_SHORT_DESC}</b></span>
                        <br/>
                        <span class="currComputer" style="float: right;"><b>${sUser.sCURR_COMP}</b></span>

                    </div>
                </div>
                <div class="text-center">
                    <input type="hidden" id="idIOMSampleIds" name="nameIOMSampleIds" value="">
                    <input type="hidden" id="idLabLocCode" name="nameLabLocCode" value="${labLocCode}">

                </div>
            </form>
        </div>
        <div class="text-center">
            <a class="btn-primary btn" href="${pageContext.request.contextPath}/Tse/GetCreatedSamples?status=0&labName=${labLocCode}" data-toggle="tooltip"  data-placement="top" title="View Samples Summary"  style="margin-right: 20px; text-decoration: none">GO BACK</a>
            <a href="#" id="generateIOMe" class="btn btn-primary" >Generate IOM</a>
        </div>
        <!--onclick="genIOM('iomDataTb1')"-->

        <script type="text/javascript">
            $(document).ready(function () {
                $(document).on('click', '#generateIOMe', function () {
                    var ref_no = $('#idIOMrefNO').val();
                    genIOM('iomDataTb1', ref_no);
                });
            });
        </script>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>