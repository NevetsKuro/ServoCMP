<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Other Specification Values"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container">
            <h4 class="centre-btn" style="text-decoration: underline">Other Specification Values</h4>
            <div class="row" style="margin-bottom: 20px;">
                <section>
                    <input id="fetchTestId" type="hidden" value="${testId}"/>
                    <div class="form-group col-xs-4 floating-label-form-group floating-label-form-group-with-value">
                        <label>Select Test(with Other Values):<span class="text-danger">*</span></label>
                        <select id="TestsWithOV" name="TestsWithOV" style="width: 100%; box-shadow: none" class="form-control" required>

                        </select>
                    </div>
                    <div class="form-group col-xs-offset-1 col-xs-4 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                        <label>Other Values <span class="text-danger">*</span></label>
                        <select id="OVforTest" name="OVforTest" style="width: 100%; box-shadow: none; display: none;" class="form-control">
                            
                        </select>
                    </div>
                    <div class="col-xs-offset-1 col-xs-2" style="margin-top: 10px">
                        <button id="OthValUpdate" type="button" class="btn btn-success">Add</button>
                    </div>
                </section>
            </div>

            <div class="row">
                <section>
                    <div style="border-top: 1px solid lightgrey;">
                        <h3><strong>Summary</strong></h3>
                        <span id="getOVSummary" class="btn btn-primary btn-toolbar" style="float: right;margin-top: 10px" data-emp-code="${sUser.sEMP_CODE}"><i class="glyphicon glyphicon-refresh"></i> Get Data</span>
                    </div>
                    <table id="OVTable" class="table table-bordered table-striped table-hover tbCenter-head">
                        <thead>
                        <th>Test Name</th>
                        <th>Other Values</th>
                        </thead>
                        <tbody class="OVsummary">

                        </tbody>
                    </table>
                </section>
            </div>
        </div>
    </body>
</html>