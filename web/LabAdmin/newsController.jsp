<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage News Master"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
    <body>
        <div class="container">
            <h4 class="centre-btn" style="text-decoration: underline">News Table</h4>

            <br/><br/>

            <table id="NewsTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>News</th>
                        <th>Created By</th>
                        <th>Updated Date</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>

        <div class="container addBar">
            <div style="color: gray;font-size: medium;font-family: cursive;margin-left: 12px">Add News</div>
            <div class="col-xs-4">
                <label for="addNewsTitle" class="adjLabels">Title</label>
                <input id="addNewsTitle" name="addNewsTitle" class="form-control" type="text" placeholder="Enter News title">
            </div>
            <div class="col-xs-4">
                <label for="addNewsBody" class="adjLabels">Body</label>
                <input id="addNewsBody" name="addNewsBody" class="form-control" type="text" placeholder="Enter News Body ">
            </div>

            <button id="addNews" class="button button-circle button-raised button-action cirBut"><span class="glyphicon glyphicon-plus"></span></button>
        </div>


        <div id="UpdateNews" class="modal fade" role="dialog" data-backdrop='static'>
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form role="form" class="form-inline" action="#" method="POST">
                        <input type="hidden" id="newsId" name="newsId"/>
                        <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                            <h4 id="modal-title-News" class="modal-title">Update News</h4>
                        </div>
                        <div class="modal-body container-fluid">
                            <div class="floating-label-form-group floating-label-form-group-with-value col-xs-offset-1 col-xs-4">
                                <label>Title *</label>
                                <input class="form-control" placeholder="Enter Title *" type="text" id="newsTitle" name="newsTitle" required/>
                            </div>
                            <div class="floating-label-form-group floating-label-form-group-with-value col-xs-4">
                                <label>News *</label>
                                <input class="form-control" placeholder="Enter News *" type="text" id="newsTxt" name="newsTxt" required/>
                            </div>
                        </div>
                        <br/>
                        <div class="modal-footer">
                            <span style="float: left" class="text-info">IndianOil.in</span>
                            <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                            <button type="button" id="newsUpdate" class="btn btn-default btn-success">Update</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>