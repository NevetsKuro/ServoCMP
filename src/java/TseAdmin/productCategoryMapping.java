/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TseAdmin;

import DAOs.MstLabDAO;
import DAOs.MstProductDAO;
import DAOs.MstTestDAO;
import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.TSEoperations;
import viewModel.MessageDetails;
import viewModel.MstCategory;
import viewModel.MstLab;
import viewModel.MstProduct;
import viewModel.MstTest;
import viewModel.StevenModels.productCategory;

/**
 *
 * @author NIT_steven
 */
public class productCategoryMapping extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet productCategoryMapping</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet productCategoryMapping at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstLab> listLab = new ArrayList<>();
        List<MstTest> listTest = new ArrayList<>();
        List<MstCategory> listCat = new ArrayList<>();
        
        switch (request.getServletPath()) {
            case "/TseAdmin/productCategoryMapping":
                List<productCategory> prodCat = new ArrayList<>();
                String cat = request.getParameter("cat");
                prodCat = getProdCatList(cat);
                new Gson().toJson(prodCat, response.getWriter());
                break;
            case "/TseAdmin/getLabList":
//                listLab = MstLabDAO.listLab();
                listLab = MstLabDAO.listLabByLabType("RND");
                new Gson().toJson(listLab, response.getWriter());
                break;
            case "/TseAdmin/getTestParams":
                listTest = MstTestDAO.gettestMaster();
                new Gson().toJson(listTest, response.getWriter());
                break;
            case "/TseAdmin/getCategory":
                listCat = MstTestDAO.listAllCategory();
                new Gson().toJson(listCat, response.getWriter());
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/TseAdmin/AddProductCategory":
                String testIds = request.getParameter("testIds");
                String labCode = request.getParameter("labCode");
                String cat_id = request.getParameter("cat_id");
                String type = request.getParameter("type");
                MessageDetails md = new MessageDetails();
                if (type.equalsIgnoreCase("Add")) {
                    md = addProductCategory(cat_id, labCode, testIds);
                } else if (type.equalsIgnoreCase("Update")) {
                    md = updateProductCategory(cat_id, labCode, testIds);
                }
                new Gson().toJson(md, response.getWriter());
                break;
            case "":
                
                break;
            default:
                break;
            
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
    public static List<productCategory> getProdCatList(String cat) {
        List<productCategory> prodCat = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(""
                    + "select pc1.cat_ids, c1.cat_name, pc1.test_ids,t1.test_name, pc1.lab_code, l1.lab_name from product_category pc1 inner join "
                    + "mst_test t1 on pc1.test_ids = t1.test_id inner join mst_lab l1 on pc1.lab_code = l1.lab_loc_code inner join mst_category c1 on pc1.cat_ids = c1.cat_id "
                    + "where pc1.cat_ids = ?");) {
            pst.setString(1, cat);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                productCategory pc = new productCategory();
                pc.setCat_id(res.getString(1));
                pc.setCat_name(res.getString(2));
                pc.setLabCode(res.getString(5));
                pc.setLabName(res.getString(6));
                pc.setTestId(res.getString(3));
                pc.setTestName(res.getString(4));
                prodCat.add(pc);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getProdCatList()");
        }
        return prodCat;
    }
    
    public static MessageDetails addProductCategory(String cat_id, String labCode, String testIds) {
        List<productCategory> prodCat = new ArrayList<>();
        MessageDetails md = new MessageDetails();
        md.setModalTitle("Product Category Status");
        md.setMsgClass("text-danger");
        int count = 0;
        String cat_id2 = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst1 = con.prepareStatement("Select MAX(cat_id)+1 from MST_CATEGORY");) {
            ResultSet rs = pst1.executeQuery();
            while (rs.next()) {
                cat_id2 = String.valueOf(rs.getInt(1));
            }
            try (PreparedStatement pst3 = con.prepareStatement("insert into MST_CATEGORY VALUES (?,?,?,TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))");) {
                pst3.setString(1, cat_id2);
                pst3.setString(2, cat_id);
                pst3.setString(3, "SYSTEM");
                int i = pst3.executeUpdate();
                if (i > 0) {
                    for (String testId : testIds.split(",")) {
                        try (PreparedStatement pst2 = con.prepareStatement("Insert into PRODUCT_CATEGORY (CAT_IDS,LAB_CODE,TEST_IDS) values ( ?, ?, ?)");) {
                            pst2.setString(1, cat_id2);
                            pst2.setString(2, labCode);
                            pst2.setString(3, testId);
                            int j = pst2.executeUpdate();
                            if (j > 0) {
                                count++;
                            }
                        } catch (Exception e) {
                            md = MyLogger.logIt(e, "getProdCatList()");
                        }
                    }
                    if (count == testIds.split(",").length) {
                        md.setMsgClass("text-success");
                        md.setModalMessage("Successfully inserted all records.");
                    } else if (count < testIds.split(",").length) {
                        md.setMsgClass("text-danger");
                        md.setModalMessage("Error. Rows inserted: " + count + " out of " + testIds.split(",").length + ".");
                    } else {
                        md.setMsgClass("text-danger");
                        md.setModalMessage("Failure to update");
                    }
                }
            } catch (Exception e) {
                md = MyLogger.logIt(e, "getProdCatList()");
            }
            
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, "getProdCatList()");
        }
        return md;
    }
    
    public static MessageDetails updateProductCategory(String cat_id, String labCode, String testIds) {
        MessageDetails md = new MessageDetails();
        md.setModalTitle("Product Category Status");
        int count = 0;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("delete from product_category where cat_ids = ?");) {
            pst.setString(1, cat_id);
            int i = pst.executeUpdate();
            if (i > 0) {
                for (String testId : testIds.split(",")) {
                    try (PreparedStatement pst2 = con.prepareStatement("Insert into PRODUCT_CATEGORY (CAT_IDS,LAB_CODE,TEST_IDS) values ( ?, ?, ?)");) {
                        pst2.setString(1, cat_id);
                        pst2.setString(2, labCode);
                        pst2.setString(3, testId);
                        int j = pst2.executeUpdate();
                        if (j > 0) {
                            count++;
                        }
                    } catch (Exception e) {
                        md = MyLogger.logIt(e, "getProdCatList()");
                    }
                }
                if (count == testIds.split(",").length) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Successfully updated all records.");
                } else if (count < testIds.split(",").length) {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error. Rows updated: " + count + " out of " + testIds.split(",").length + ".");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Failure to update");
                }
            }else{
                md.setMsgClass("text-danger");
                md.setModalMessage("Failure to update");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getProdCatList()");
        }
        return md;
    }
    
}
