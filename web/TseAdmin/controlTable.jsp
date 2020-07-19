<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Control Manager"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container">
            <h4 class="centre-btn" style="text-decoration: underline">Control Managers</h4>
            <div>
                <button id="evmode" class="btn btn-primary" title="click here to change the mode"><i class="fa fa-exchange-alt"></i> Edit Mode</button>
                <button id="updateCT" class="btn btn-primary" title="click here to sync with server" type="button" disabled=""><i class="fa fa-sync"></i> Update and Sync</button>
                <br/><br/>
                <table id="UserTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                    <thead>
                        <tr>
                            <th>Index</th>
                            <th>Field Name</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr id="NotifyDaysLimit">
                            <td>1</td>
                            <td>Notify Days Limit <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Notify Days Limit" id="ndl" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="SystemMaintenanceFlag">
                            <td>2</td>
                            <td>System Maintenance Flag <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="System Maintenance Flag" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;"  maxlength="1" ></td>
                        </tr>
                        <tr id="TseSampleFilterDaysLimit">
                            <td>3</td>
                            <td>Tse Sample Filter Days Limit <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="LabSampleFilterDaysLimit">
                            <td>4</td>
                            <td>Lab Sample Filter Days Limit <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Lab Sample Filter Days Limit" type="text" class="form-control ins" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="postponerestrictDays">
                            <td>5</td>
                            <td>Postpone restrict Days <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Postpone restrict Days" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="sendtoLABdateLimit">
                            <td>6</td>
                            <td>Send to LAB date Limit <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Send to LAB date Limit" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="highpriorityDays">
                            <td>7</td>
                            <td>High Priority Days <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="High Priority Days" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="mediumpriorityDays">
                            <td>8</td>
                            <td>Medium Priority Days <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Medium Priority Days" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="normalpriorityDays">
                            <td>9</td>
                            <td>Normal Priority Days <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Normal Priority Days" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="createrestrictDays">
                            <td>10</td>
                            <td>Create Restrict Days <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Create Restrict Days" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="ServerLogsHost">
                            <td>11</td>
                            <td>Server Logs Host <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input type="text" class="form-control ins" placeholder="Enter value..." style="width: 100%;display: none;"></td>
                        </tr>
                        <tr id="TseDashDaysLimit">
                            <td>12</td>
                            <td>Tse Dash Days Limit <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Tse Dash Days Limit" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="LabDashDaysLimit">
                            <td>13</td>
                            <td>Lab Dash Days Limit <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Lab Dash Days Limit" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                        <tr id="dataRowRetrieveLimit">
                            <td>14</td>
                            <td>Row Limit(for every table) <span class="text-danger">*</span></td>
                            <td><span class="ins"></span><input data-field="Row Limit" type="text" class="form-control ins isNumber" placeholder="Enter value..." style="width: 100%;display: none;" maxlength="5"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>