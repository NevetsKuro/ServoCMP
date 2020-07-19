<br/><btn class ="btn btn-primary btn-group-sm" onclick="showUpdateMake('Add Make')">Add New Make</btn>
<form role="form" action="#" id="make" class="form-inline" method="GET">
    <div class="row centre-btn">
        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
            <label>Select Make *</label>
            <select id="makeName"  style="width: 100%; padding: 5px" class="form-control select2-select" required="required">
                <option value="">Select Make *</option>
            </select>
        </div>
    </div>
    <div class="row centre-btn">
        <div class="centre-btn" style="margin-top: 20px">
            <input type="button" value="Get Details" class="btn btn-primary" onclick="UpdateMake('Update Make Details')"/>
            <input style="margin-left: 20px" type="button" value="Get Summary" class="btn btn-primary" onclick="getMakeSummary()"/>
        </div>
    </div>
</form>
<div id="makeTableDiv" class="container-fluid">
    <table id="manageMakeTable" class="table table-bordered table-condensed table-responsive table-striped">
        <thead>
        <th>Make</th>
        <th>Updated By</th>
        <th>Updated Date</th>
        </thead>
        <tbody></tbody>
    </table>
</div>
<div id="UpdateMake" class="modal fade" role="dialog" data-backdrop='static'>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form id="updateMakeForm" class="form-inline" role="form" action="#" method="POST">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 id="modal-title-Make" class="modal-title"></h4>
                </div>
                <div class="modal-body container-fluid">
                    <input type="hidden" id="MakeId" name="makeId"/>
                    <div class="row centre-btn">
                        <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                            <label>Select Make *</label>
                            <input id="makeNameModal" name="makeName" placeholder="Enter Make Name" class="form-control" type="text" style="width: 100%"/>
                        </div>
                    </div>
                    <br/>
                    <br/>
                </div>
                <div class="modal-footer">
                    <span style="float: left" class="text-info">IndianOil.in</span>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" id="makeSubmit" onclick="submitMake(this)" class="btn btn-default btn-primary"></button>
                </div>
            </form>
        </div>
    </div>
</div>