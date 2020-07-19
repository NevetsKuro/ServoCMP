<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Category-to-Test Mapping"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container-fluid">
            <h4 class="centre-btn" style="text-decoration: underline">Manage Category-to-Test Mapping</h4>
        </div>
        <br/>
        <div class="container-fluid" style="width: 80%; margin: 0 auto">
            <div class="row">
                <div class="col-md-2 floating-label-form-group floating-label-form-group-with-value">
                    <label>Category:</label>
                    <select id="catIdList" class="form-control prodCat2 getProdCatList" style="width: 100%; box-shadow: none"  >

                    </select>
                </div>
                <div class="col-md-offset-8 col-md-2">
                    <button class="btn btn-success" data-toggle="modal" data-target="#prodCatModal" title="Add or Update Records">Add/Update</button>
                </div>
            </div>
            <br>
            <div class="row">
                <table id="prodCatTable" class="table-bordered table-condensed table table-hover table-responsive table-striped">
                    <thead>
                        <tr>
                            <th>Sr. No</th>
                            <th>Category Name</th>
                            <th>Lab Code</th>
                            <th>Lab Name</th>
                            <th>Test Name</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>

        <div class="modal fade" id="prodCatModal" role="dialog">
            <!-- Modal content-->
            <div class="modal-dialog modal-lg">
                <div class="modal-content modal-lg">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4> Add Product Category </h4>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="row">
                                <div class="col-md-offset-9 col-md-3">
                                    <input type="checkbox" id="crudProdCategory" data-width="100" checked/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Category ID: <span class="text-danger">*</span></label>
                                    <select id="prodCat_catId" name="prodCat_catId" style="width: 100%; box-shadow: none" class="form-control upperCase">

                                    </select>
                                </div>
                                <div class="form-group col-xs-6 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Lab Code: <span class="text-danger">*</span></label>
                                    <select id="prodCat_labCode" name="prodCat_labCode" style="width: 100%; box-shadow: none" class="form-control">

                                    </select>
                                </div>
                                <div class="form-group col-xs-12 floating-label-form-group floating-label-form-group-with-value">
                                    <label>Test Parameters: <span class="text-danger">*</span></label>
                                    <select id="prodCat_testId" name="prodCat_testId" style="width: 100%; box-shadow: none" class="form-control" multiple="">

                                    </select>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-danger" data-dismiss="modal">Close</button>
                        <button id="modal_addProductCat" class="btn btn-success" title="Add Mapping to Selected Ids" style="margin-left: 20px">Submit</button>
                    </div>
                </div>
            </div>
        </div>
    </div>         
</div>
</body>
<jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>