package TseAdmin;

import static TseAdmin.manageIndustryServlet.isAjax;
import DAOs.MstProductDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;
import viewModel.MstProduct;
import viewModel.ReportDetails;

public class manageProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstProduct> listProduct = new ArrayList<>();
        
        switch (request.getServletPath()) {
            case "/TseAdmin/AllActiveProduct":
//                listProduct = MstProductDAO.listAllProducts("SELECT PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_PRODUCT WHERE ACTIVE = 1 and rownum <="+rowLimit);
                break;
            case "/TseAdmin/AllDeactiveProduct":
                listProduct = MstProductDAO.listAllProducts("SELECT PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_PRODUCT WHERE ACTIVE = 0");
                break;
            case "GetProduct":
                break;
            default:
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (!listProduct.isEmpty()) {
                response.getWriter().write(gson.toJson(listProduct));
            } else {
                response.getWriter().write(gson.toJson("No Product Found."));
            }
        } else {
            if (listProduct != null) {
                request.setAttribute("Product", listProduct);
            }
            RequestDispatcher rd = request.getRequestDispatcher("manageProduct.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        MstProduct mstProduct = new MstProduct();
        mstProduct.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/TseAdmin/AddProduct":
                mstProduct.setProNames(request.getParameterValues("productNameInput"));
                mstProduct.setActive("1");
                md = MstProductDAO.AddProduct(mstProduct);
                md.setModalTitle("Add Product Details");
                break;
            case "/TseAdmin/RemoveProduct":
                mstProduct.setProId(request.getParameter("proId"));
                mstProduct.setProName(request.getParameter("proName"));
                mstProduct.setActive("0");
                md = MstProductDAO.markInactive(mstProduct);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Product '" + mstProduct.getProName() + "' has been Removed Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Product '" + mstProduct.getProName() + "' cannot be Removed");
                }
                md.setModalTitle("Product Status");
                break;
            case "/TseAdmin/ActivateProduct":
                mstProduct.setProName(request.getParameter("productName"));
                mstProduct.setProId(request.getParameter("productId"));
                mstProduct.setActive("1");
                md = MstProductDAO.markInactive(mstProduct);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Product '" + mstProduct.getProName() + "' has been Activated Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Product '" + mstProduct.getProName() + "' cannot be Activated");
                }
                md.setModalTitle("Product Status");
                break;
            case "/TseAdmin/UpdateProduct":
                mstProduct.setProName(request.getParameter("productNameEdit"));
                mstProduct.setProId(request.getParameter("proId"));
                mstProduct.setProdCat(request.getParameter("productCat"));
                md = MstProductDAO.UpdateProduct(mstProduct);
                md.setModalTitle("Product Update Status");
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Product '" + request.getParameter("oldProName") + "' changed to '" + mstProduct.getProName() + "' Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while changing Product '" + request.getParameter("oldProName") + "' to '" + mstProduct.getProName() + "'. Try again Later");
                }
                break;
            case "/Tse/getProduct":
            case "/TseAdmin/getProduct":
                Gson gson = new Gson();
                List<MstProduct> result;
                String prodId = request.getParameter("prodId");
                result = MstProductDAO.listAllProducts("SELECT PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_PRODUCT WHERE ACTIVE = 1 and PROD_ID like '%" + prodId + "%' ");
                if (!result.isEmpty()) {
                    response.getWriter().write(gson.toJson(result));
                } else {
                    response.getWriter().write(gson.toJson("No Product Found"));
                }
                break;
            case "/Tse/getProductCategory":
            case "/TseAdmin/getProductCategory":
                Gson gson1 = new Gson();
                List<ReportDetails> listRptDetails;
                String productId = request.getParameter("prodId");
                listRptDetails = MstProductDAO.getCategoryByProduct(productId);
                if(listRptDetails.size()>0){
                    gson1.toJson(listRptDetails, response.getWriter());
                }else{
                    gson1.toJson("No category mapping", response.getWriter());
                }
                break;
            default:
                break;
        }

        if (!isAjax(request)) {
            request.setAttribute("messageDetails", md);
            doGet(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
