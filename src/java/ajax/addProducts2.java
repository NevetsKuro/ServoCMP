/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ajax;

import DAOs.MstProductDAO;
import Exceptions.MyLogger;
import static TseAdmin.manageIndustryServlet.isAjax;
import com.DatabaseConnectionFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import globals.User;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.ErrorLogger;
import viewModel.MessageDetails;
import viewModel.MstProduct;
import viewModel.StevenModels.Products;

public class addProducts2 extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        List<MstProduct> listProduct = new ArrayList<>();
        HttpSession session = request.getSession();
        String rowLimit = (String) session.getAttribute("dataRowRetrieveLimit");
        switch (request.getServletPath()) {
            case "/TseAdmin/AllActiveProduct":
                listProduct = MstProductDAO.listAllProducts("SELECT PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_PRODUCT WHERE ACTIVE = 1 and rownum <="+rowLimit);
                break;
            case "/TseAdmin/AllDeactiveProduct":
                listProduct = MstProductDAO.listAllProducts("SELECT PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_PRODUCT WHERE ACTIVE = 0");
                break;
            case "GetProduct":
                break;
            default:
                listProduct = MstProductDAO.listAllProducts("SELECT PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_PRODUCT WHERE ACTIVE = 1");
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
            request.setAttribute("Product", listProduct);
            RequestDispatcher rd = request.getRequestDispatcher("manageProduct.jsp");
            rd.forward(request, response);
        }
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        Gson gson = new Gson();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        String products = (String)request.getParameter("json");
        
        Type listType =  new TypeToken<List<Products>>(){}.getType();
        
        List<Products> prodList = gson.fromJson(products,listType);
        
        switch (request.getServletPath()) {
            case "/TseAdmin/addProducts2":
                md = AddProduct(prodList,request,user);
//                System.err.println(md.getModalMessage());
                md.setModalTitle("Add Product Details");
                break;
        }
        
        if (!isAjax(request))
        {
            request.setAttribute("messageDetails", md);
            doGet(request, response);
        }else{
            response.getWriter().write(gson.toJson(md));
        }
    }

    private MessageDetails AddProduct(List<Products> prodList,HttpServletRequest req, User user) {
        
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_PRODUCT (PROD_ID, PROD_NAME, CATEGORY, ACTIVE, UPDATED_BY, UPDATED_DATETIME) "
                        + "VALUES (?, ?, ?, ?, ?, "
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))");) {
            md.setModalMessage("");
            for (Products p : prodList) {
                try {
                    pst.setString(1, p.getCode());
                    pst.setString(2, p.getName().toUpperCase());
                    pst.setInt(3, Integer.parseInt(p.getProdcat()));
                    pst.setString(5, user.getsEMP_CODE());
                    pst.setString(4, "1");
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Product '" +  p.getName().toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Product '" +  p.getName().toUpperCase() + "'. No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md = MyLogger.logIt(ex, user.getsEMP_CODE());
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Product '" +  p.getName().toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), user.getsEMP_CODE()) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique")) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Product '" +  p.getName().toUpperCase() + "' Already exist in Master. </span><br/><br/>");
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, user.getsEMP_CODE());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Product '" + user.getsEMP_CODE() + "' Already exist in Master");
            }
        }
        return md;
        
        
    }
}
