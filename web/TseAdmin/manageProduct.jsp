<html>
    <jsp:include page="../UItemplate/header.jsp">
        <jsp:param name="title" value="Manage Products"/>
    </jsp:include>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <body>
        <div class="container">
            <h4 class="centre-btn" style="text-decoration: underline">PRODUCT MASTERS</h4>
            <div style="margin-left: auto; margin-right: auto">
                <!--<button id="addProduct" class ="btn btn-primary btn-group-sm" onclick="AddProduct('Add Product')" data-toggle="tooltip" data-placement="bottom" title="Add Product to Master Records">Add Product</button>-->

                <div class="row">
                    <div class="col-xs-3" style="display: inline-flex;">
                        <div style="border: none;border-bottom: 1px solid grey;" class="input-group floating-label-form-group floating-label-form-group-with-value" >
                            <label>Enter Product Name:</label>
                            <input name="prodName" id="prodName" placeholder="Enter Product Name" type="text" class="form-control" value="" minlength="4" required/>
                        </div>
                    </div>
                    <div class="col-xs-3">
                        <input name="btnProductFilter" id="btnProductFilter" data-toggle="tooltip" data-placement="top" title="Click to Search" style="margin-top: 20px" type="button" class="btn btn-primary input-group"value="Search By">
                    </div>
                    <div class="col-xs-offset-2 col-xs-2">
                        <button  style="margin-top: 20px;float: right"  id="addProduct" class ="btn btn-primary btn-group-sm" onclick="$('#AddProduct').modal('show')" data-toggle="tooltip" data-placement="bottom" title="Add Product to Master Records">Add Product</button>
                    </div>
                    <div class="col-xs-2">
                        <button style="margin-top: 20px;float: right;" class ="btn btn-primary btn-group-sm right" onclick="removeProduct('Remove Product')" data-toggle="tooltip" data-placement="bottom" title="Inactive Products">Removed Products</button>
                    </div>
                </div>
                <br>
            </div>
            <table id="ProductTable" class="table table-bordered nowrap table-hover table-striped tbCenter tbCenter-head" >
                <thead>
                    <tr>
                        <th>Product Id</th>
                        <th>Product Name</th>
                        <th>Product Category</th>
                        <th style="width: 10%">Updated By</th>
                        <th style="width: 20%">Updated Date</th>
                        <th style="width: 10%">Update Product</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${Product}" var="pro">
                        <tr>
                            <td>${pro.proId}</td>
                            <td class="NevsLeft">${pro.proName}</td>
                            <td><a href="#" onclick="openHelloIOCian(${pro.updatedBy})">${pro.updatedBy}</a></td>
                            <td>${pro.prodCat}</td>
                            <td>${pro.updatedDate}</td>
                            <td class="text-center">
                                <form method="post" style="margin-bottom: 0px" action="RemoveProduct" id="inactiveProduct">
                                    <input name="proId" type="hidden" value="${pro.proId}"/>
                                    <input name="proName" type="hidden" value="${pro.proName}"/>
                                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                                    <a href="#" onclick="AddProduct('Update Product', ${pro.proId}, '${pro.proName}', this)" data-toggle="tooltip" data-placement="right" title="Edit ${pro.proName} Industry" style=" text-decoration: none;">
                                        <span class="glyphicon glyphicon-pencil"></span>
                                    </a>
                                    |
                                    <a href="#" onclick="removeProduct(${pro.proId}, '${pro.proName}', this)" data-toggle="tooltip" data-placement="right" title="Remove ${pro.proName} Industry" style=" text-decoration: none;">
                                        <span class="glyphicon glyphicon-trash"></span>
                                    </a>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div id="UpdateProduct" class="modal fade" role="dialog" data-backdrop='static'>
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form role="form" class="form-inline" action="#" method="POST" id="updateProductForm">
                    <input type="hidden" id="proId" name="proId"/>
                    <input type="hidden" id="oldProName" name="oldProName"/>
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 id="modal-title-Product" class="modal-title"></h4>
                    </div>
                    <div class="modal-body container-fluid">
                        <div class="row vertical-center-row">
                            <div class="form-group col-xs-offset-1 col-xs-3 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                <label>Product Name <span class="text-danger">*</span></label>
                                <select id="productNameModal" name="productId" style="width: 100%; box-shadow: none" class="form-control">
                                    <option value="">Select Product</option>
                                    <c:forEach items="${Product}" var="pro">
                                        <option value="${pro.proId}">${pro.proName}</option>
                                    </c:forEach>
                                </select>
                                <div class="hideActive">
                                    <select multiple="multiple" id="productNameInput" name="productNameInput" style="width: 100%; box-shadow: none" class="form-control">
                                        <option></option>
                                        <c:forEach items="${Product}" var="pro">
                                            <option  value="${pro.proName}">${pro.proName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="hideProductEdit">
                                    <input type="text" id="productEdit" name="productNameEdit" class="form-control" placeholder="Enter Product Name" style="width: 100%"/>
                                </div>
                                <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                            </div>
                            <div id="productCatModal" class="form-group col-xs-offset-1 col-xs-3 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                <label>Product Category <span class="text-danger">*</span></label>
                                <select id="productCatModal2" name="productCat" style="width: 100%; box-shadow: none" class="form-control prodCat2">
                                    
                                </select>
                            </div>
                        </div>
                    </div>
                    <br/>
                    <br/>
                    <div class="modal-footer">
                        <span style="float: left" class="text-info">IndianOil.in</span>
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                        <button type="button" id="productSubmit" onclick="submitProduct(this)" class="btn btn-default btn-success">Submit</button>
                    </div>
                </form>
            </div>
        </div>
    </div> 
    <div id="AddProduct" class="modal fade" role="dialog" data-backdrop='static'>
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form role="form" class="form-inline">
                    <input type="hidden" id="proId" name="proId"/>
                    <input type="hidden" id="oldProName" name="oldProName"/>
                    <input type="hidden" name="csrftoken" value="${sessionScope.csrfToken}" />
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 id="modal-title-Product" class="modal-title">Add Product <button id="addExProduct" type="button" class="btn btn-success btn-sm">Add more Products</button></h4>
                    </div>
                    <div class="modal-body container-fluid">
                        <div class="row vertical-center-row">
                            <div class="form-group col-xs-offset-1 col-xs-6 floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                <table>
                                    <thead style="width: 100%;margin-bottom: 12px;margin: 10px">
                                    <th width="49%" style="padding-left: 10px;">Product No. <span class="text-danger">*</span><strong>(Max:4)</strong></th>
                                    <th width="49%" style="padding-left: 10px;">Product Name <span class="text-danger">*</span><strong>(Max:40)</strong></th>
                                    <th width="49%" style="padding-left: 10px;">Product Category:<span class="text-danger">*</span></th>
                                    </thead>
                                    <tbody id="prodList">
                                        <tr>
                                            <td class="floating-label-form-group floating-label-form-group-with-value" style="border-left: none">
                                                <input type="text" minlength="4" maxlength="4" placeholder="Enter Product Code" class="prodNoVal form-control isNumber" data-field="Product Code">
                                            </td>
                                            <td class="floating-label-form-group floating-label-form-group-with-value">
                                                <input type="text" placeholder="Enter Product Name" class="prodNameVal form-control" maxlength="40">
                                            </td>
                                            <td class="floating-label-form-group floating-label-form-group-with-value">
                                                <select class="prodCatVal form-control">
                                                    <option>loading...</option>
                                                </select>
                                            </td>
                                            <td class="text-danger">
                                                <!--<span class="glyphicon glyphicon-remove removeRow1"></span>-->
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                <div class="errmsg-contentSelect errmsgDropdown has-error help-block col-xs-12 alert-danger"></div>
                            </div>
                        </div>
                    </div>
                    <br/>
                    <br/>
                    <div class="modal-footer">
                        <span style="float: left" class="text-info">IndianOil.in</span>
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                        <button type="button" id="productSubmit" class="btn btn-default btn-success">Submit</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
<jsp:include page="../UItemplate/footer.jsp"></jsp:include>
</html>