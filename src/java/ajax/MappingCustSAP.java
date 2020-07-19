/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ajax;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import globals.User;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.ErrorLogger;
import viewModel.MessageDetails;
import viewModel.StevenModels.Customers;

public class MappingCustSAP extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        Gson gson = new Gson();
        try {
            switch (request.getServletPath()) {
                case "/Tse/MappingCustSAP":
                    List<Customers> custList = new ArrayList<Customers>();
                    custList = getCustomers(user.getsEMP_CODE(), user);
    //                gson.toJson(custList, response.getWriter());
                    session.setAttribute("custList", custList);
                    request.getRequestDispatcher("mappingCustSap.jsp").forward(request, response);
                    break;
            }
            
        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
        }
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        Gson gson = new Gson();
        
        switch(action){
            case "getSAP":
                List<Customers> sapList = new ArrayList<Customers>();
                String custCode = request.getParameter("custID");
                sapList = getSAPDetails(custCode,user);
                System.out.println(sapList);
                gson.toJson(sapList, response.getWriter());
                break;
            case "addSAP":
                List<Customers> sapListAdd = new ArrayList<Customers>();
                MessageDetails md1 = new MessageDetails();
                String json = request.getParameter("json");
                Type listType =  new TypeToken<List<Customers>>(){}.getType();
                
                sapListAdd = gson.fromJson(json,listType);
                System.out.println(sapListAdd);
                md1 = addSAPCodes(request,response,sapListAdd,user);
                md1.setModalTitle("Add SAP Code");
                gson.toJson(md1, response.getWriter());
                break;
            case "getSummary":
                List<Customers> sapSummary = new ArrayList<Customers>();
                String empCode = request.getParameter("empCode");
                sapSummary = getSummary(empCode,user);
                System.out.println(sapSummary);
                gson.toJson(sapSummary, response.getWriter());
                break;
            case "updateSAP":
                String custCode2 = request.getParameter("custCode");
                String sapCode2 = request.getParameter("sapCode");
                String sapCode3 = request.getParameter("PrevsapCode");
                MessageDetails md = new MessageDetails();
                
                md = updateSAPCode(custCode2,sapCode2,sapCode3,user);
                md.setModalTitle("Update SAP Code");
                gson.toJson(md, response.getWriter());
                break;
        }
    }
    
    private List<Customers> getSAPDetails(String custCode,User user) {
        MessageDetails md = new MessageDetails();
        List<Customers> sapList = new ArrayList<>();
        try(Connection con = DatabaseConnectionFactory.createConnection();
            PreparedStatement pst = con.prepareStatement("Select * from MST_MAP_CUST_SAPCODE where cust_id = ?");){
            pst.setString(1, custCode);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                Customers c = new Customers();
                c.setCode(rs.getString(2));
                sapList.add(c);
            }
            
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getSAPDetails()");
            md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Customer. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), user.getsEMP_CODE()) + ") </span> <br/>");
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Customer's Already exist in Master. </span><br/><br/>");
            }
        }
        
        return sapList;
    }

    private List<Customers> getCustomers(String semp_code, User user) {
        MessageDetails md = new MessageDetails();
        List<Customers> custList = new ArrayList<Customers>();
        try(Connection con = DatabaseConnectionFactory.createConnection();
            PreparedStatement pst = con.prepareStatement("Select cust_id,cust_name from MST_CUSTOMER where emp_code = ?");){
            pst.setString(1, semp_code);
            ResultSet rs1 = pst.executeQuery();
            while(rs1.next()){
                Customers c = new Customers();
                c.setCode(rs1.getString(1));
                c.setName(rs1.getString(2));
                custList.add(c);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getCustomers()");
            md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Customer. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), user.getsEMP_CODE()) + ") </span> <br/>");
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Customer's Already exist in Master. </span><br/><br/>");
            }
        }
        return custList;
    }

    private MessageDetails addSAPCodes(HttpServletRequest request, HttpServletResponse response, List<Customers> sapListAdd, User user) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection()){
            PreparedStatement ps = null;
            for (Customers cust : sapListAdd) {
                String sql = "INSERT INTO MST_MAP_CUST_SAPCODE VALUES ('"+cust.getName()+"', '"+cust.getCode()+"','"+user.getsEMP_CODE()+"', TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))";
                ps = con.prepareStatement(sql);
                int i = ps.executeUpdate();
                String msg = (md.getModalMessage()!=null?md.getModalMessage():"");
                if ( i > 0) {
                    
                    md.setModalMessage(msg+"<span class='text-success'> SAP code "+ cust.getCode() +" of Customer '" + cust.getName().toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                } else {
                    
                    md.setModalMessage(msg+"<span class='text-danger'> Error occured while adding SAP code("+ cust.getCode() +") of Customer '" + cust.getName().toUpperCase() + "'. No changes were made.</span><br/><br/>");
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "addSAPCodes()");
            md.setModalMessage("<span class='text-danger'> Error While adding Customer's SAP Code. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), user.getsEMP_CODE()) + ") </span> <br/>");
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage("<span class='text-danger'> SAP Code Already exist in Master. </span><br/><br/>");
            }
        }
        return md;
    }

    private List<Customers> getSummary(String custCode, User user) {
        MessageDetails md = new MessageDetails();
        List<Customers> sapList2 = new ArrayList<Customers>();
        String sql = "Select m.cust_id, m.cust_name, t.sap_code, t.updated_by, t.update_date from mst_customer m inner Join mst_map_cust_sapcode t on m.cust_id=t.cust_id where emp_code = ?";
        try(Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(sql);){
            pst.setString(1, custCode);
            ResultSet rs2 = pst.executeQuery();
            while(rs2.next()){
                Customers c = new Customers();
                c.setCode(rs2.getString(3));
                c.setName(rs2.getString(2));
                c.setUpdated_by(rs2.getString(4));
                c.setUpdate_date(rs2.getString(5));
                sapList2.add(c);
            }
            md.setModalMessage("success");
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getSummary()");
            md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While fetching SAP code of "+ custCode +". (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), user.getsEMP_CODE()) + ") </span> <br/>");
        }
        return sapList2;
    }

    private MessageDetails updateSAPCode(String custCode2, String sapCode2,String sapCode3,User user) {
        MessageDetails md = new MessageDetails();
        String sql = "Update mst_map_cust_sapcode set sap_code=? where cust_id=? and sap_code=?";
        try (Connection con = DatabaseConnectionFactory.createConnection();
            PreparedStatement pst = con.prepareStatement(sql);){
            pst.setString(1, sapCode2);
            pst.setString(2, custCode2);
            pst.setString(3, sapCode3);
            int i = pst.executeUpdate();
            con.commit();
            if (i>0) {
                md.setModalMessage("SAP Code Updated of '"+ custCode2 + "'.</span><br/><br/>");
            } else {
                md.setModalMessage("<span class='text-danger'> Error occured while Updating SAP code of '" + custCode2+ "'. No changes were made.</span><br/><br/>");
            }
            
        } catch (Exception ex) {
            MyLogger.logIt(ex, "updateSAPCode()");
            md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While updating Customer SAP Code. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), user.getsEMP_CODE()) + ") </span> <br/>");
        }
        
        return md;
    }
}
