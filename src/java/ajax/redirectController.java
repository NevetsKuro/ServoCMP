/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajax;

import DAOs.MstDepartmentDAO;
import DAOs.MstMakeDAO;
import DAOs.MstProdTest;
import DAOs.MstProductDAO;
import DAOs.MstTestDAO;
import DashBoards.LabAdminDashBoard;
import DashBoards.TseDashBoard;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.GetMaster;
import operations.SharedOperations;
import operations.TSEoperations;
import org.json.JSONArray;
import viewModel.MessageDetails;
import viewModel.MstCustomer;
import viewModel.MstDepartment;
import viewModel.MstProduct;
import viewModel.MstTest;
import viewModel.SampleDetails;

public class redirectController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getParameter("url");
        Gson gson = new Gson();
        HttpSession session = request.getSession();
        String rowLimit = (String) session.getAttribute("dataRowRetrieveLimit");
        User user = (User) session.getAttribute("sUser");
        String roleId = user.getRole_id();
        if ("OthValues".equalsIgnoreCase(url)) {
            String testId = request.getParameter("testId");
            request.setAttribute("testId", testId);
            request.getRequestDispatcher("tseOtherValues.jsp").forward(request, response);
        } else if ("fetchRejectReason".equalsIgnoreCase(url)) {
            Map<String,String> reasons = new HashMap<>();
            reasons = getRejectedReasonsList();
            gson.toJson(reasons, response.getWriter());
        } else if ("getSampleCount".equalsIgnoreCase(url)) {
            String sampleId = request.getParameter("sampleId");
            int count = SharedOperations.getSampleCount(sampleId);
            gson.toJson(count, response.getWriter());
        } else if ("fetchTestSpecList".equalsIgnoreCase(url)) {
            String prodId = request.getParameter("prodId");
            List<MstTest> mTest = null;
            if (null == roleId) {
                response.sendError(400);
            } else switch (roleId) {
                case "3":
                    mTest = MstProdTest.getAllMktTest(prodId);
                    break;
                case "4":
                    mTest = MstProdTest.getAllLabTest(prodId);
                    break;
                case "1":
                    mTest = MstProdTest.getAllMktTest(prodId);
                    break;
                case "2":
                    mTest = MstProdTest.getAllLabTest(prodId);
                    break;
                default:
                    response.sendError(400);
                    break;
            }
            gson.toJson(mTest, response.getWriter());
        } else if ("fetchProductList".equalsIgnoreCase(url)) {
            String prodName = request.getParameter("pName");
            List<MstProduct> listProduct = MstProductDAO.listAllProductsCat("SELECT p.PROD_ID, p.PROD_NAME, c.cat_name, p.ACTIVE, p.UPDATED_BY, p.UPDATED_DATETIME FROM MST_PRODUCT p inner join mst_category c on p.category = c.cat_id WHERE p.ACTIVE = 1 and p.PROD_NAME like UPPER('%" + prodName + "%')and rownum <=" + rowLimit);
            gson.toJson(listProduct, response.getWriter());
        } else if ("controlTable".equalsIgnoreCase(url)) {
            HashMap<String, String> globallist = getControlTable();
            gson.toJson(globallist, response.getWriter());
        } else if ("updateControlTable".equalsIgnoreCase(url)) {
            String json = request.getParameter("json");
            Type listType = new TypeToken<HashMap<String, String>>() {}.getType();
            HashMap<String, String> globallist =  gson.fromJson(json, listType);
            uploadData(globallist);
            gson.toJson("Successfully Updated", response.getWriter());
        } else if ("getEmployeeAvailable".equalsIgnoreCase(url)) {
            String empcode = request.getParameter("empcode");
            HashMap<String, String> emp = getEmployeeDetails(empcode, getServletContext().getInitParameter("cemDBPath"));
            int tempSize = emp.size();
            if (tempSize > 0) {
                gson.toJson(emp, response.getWriter());
            } else {
                gson.toJson("NF", response.getWriter());
            }
        } else if ("getCustomerDetails".equalsIgnoreCase(url)) {
            String CustID = request.getParameter("custid");
            HashMap<String, List> container = new HashMap<>();
            List<MstDepartment> departsArr = getCustomerDetails(CustID);
            container.put("departsArr", departsArr);
            List<String> ls = new ArrayList<>();

            String query1 = "Select COUNT(*) from sample_details where cust_id = " + CustID;
//            String query2 = "Select COUNT(*) from mst_map_cust_sapcode where cust_id = " + CustID;
            String query2 = "Select LISTAGG(SAP_CODE,',') within group (order by SAP_CODE) from mst_map_cust_sapcode where cust_id = " + CustID;
            ls.add(getByQuery(query1).get("value"));
            ls.add(getByQuery(query2).get("value"));
            container.put("total", ls);
            gson.toJson(container, response.getWriter());
        } else if ("updateDepartment".equalsIgnoreCase(url)) {
            String deptId = request.getParameter("deptid");
            String custId = request.getParameter("custid");
            String deptName = request.getParameter("deptname");
            String HODName = request.getParameter("HODname");
            String HODEmail = request.getParameter("HODemail");
            String HODContact = request.getParameter("HODcontact");
            MstDepartment mstDept = new MstDepartment();
            
            mstDept.setDepartmentId(deptId);
            MstCustomer c = new MstCustomer();
            c.setCustomerId(custId);
            mstDept.setDepartmentName(deptName);
            mstDept.setHodName(HODName);
            mstDept.setHodEmail(HODEmail);
            mstDept.setHodContact(HODContact);
            mstDept.setUpdatedBy(user.getsEMP_CODE());
            mstDept.setMstCustomer(c);
            MessageDetails md = MstDepartmentDAO.UpdateDepartment(mstDept);
            md.setModalTitle("Update Department");
            gson.toJson(md, response.getWriter());
        } else if ("openFileStorage".equalsIgnoreCase(url)) {
            request.setAttribute("Applications", GetMaster.getApplications());
            request.setAttribute("Industries", GetMaster.getIndustry());
            request.getRequestDispatcher("storageSite.jsp").forward(request, response);
        } else if ("industryDetails".equalsIgnoreCase(url)) {
            request.setAttribute("Industries", GetMaster.getIndustry());
            request.getRequestDispatcher("industryDetails.jsp").forward(request, response);
        } else if ("tseSampleHandle".equalsIgnoreCase(url)) {
            request.setAttribute("Industries", GetMaster.getIndustry());
            request.setAttribute("Customers", GetMaster.getCustomers());
            request.setAttribute("Applications", GetMaster.getApplications());
            request.setAttribute("Departments", GetMaster.getDepartments());
            request.setAttribute("Products", GetMaster.getProducts());
            request.setAttribute("custSampleInfo", SharedOperations.getCustSample());
            request.setAttribute("testMaster", MstTestDAO.gettestMaster());
            request.setAttribute("make", MstMakeDAO.listAllMake());
            request.setAttribute("tseOfficers", SharedOperations.getAllTseOfficers());
            session.setAttribute("notidetails", TSEoperations.getindustryTse(user.getsEMP_CODE(), session.getAttribute("NotifyDaysLimit").toString(), request));
            RequestDispatcher rd = request.getRequestDispatcher("tseSampleHandle.jsp");
            rd.forward(request, response);
        } else if ("tankDetails".equalsIgnoreCase(url)) {
            request.setAttribute("Industries", GetMaster.getIndustry());
            request.setAttribute("Customers", GetMaster.getCustomers());
            request.setAttribute("Applications", GetMaster.getApplications());
            request.setAttribute("Departments", GetMaster.getDepartments());
            request.setAttribute("Products", GetMaster.getProducts());
            request.setAttribute("custSampleInfo", SharedOperations.getCustSample());
            request.setAttribute("testMaster", MstTestDAO.gettestMaster());
            request.setAttribute("make", MstMakeDAO.listAllMake());
            request.setAttribute("tseOfficers", SharedOperations.getAllTseOfficers());
            session.setAttribute("notidetails", TSEoperations.getindustryTse(user.getsEMP_CODE(), session.getAttribute("NotifyDaysLimit").toString(), request));
            RequestDispatcher rd = request.getRequestDispatcher("alltankdetails.jsp");
            rd.forward(request, response);
        } else if ("getUpcomingData".equalsIgnoreCase(url)) {
            if("1".equals(roleId)){
                JSONArray jArr = TseDashBoard.getUpcomingDataAjax(user.getsEMP_CODE(),session.getAttribute("NotifyDaysLimit").toString());
                gson.toJson(jArr, response.getWriter());
            }else if("2".equals(roleId)){
                List<SampleDetails> sdArray1 = LabAdminDashBoard.getPendingSamplesinByIndustry(user.getsEMP_CODE());
                gson.toJson(sdArray1, response.getWriter());
            }
        } else if ("getLast10Data".equalsIgnoreCase(url)) {
            if(roleId.equals("1")){
                JSONArray jArr1 = TseDashBoard.getLast10DataAjax(user.getsEMP_CODE(),session.getAttribute("NotifyDaysLimit").toString());
                gson.toJson(jArr1, response.getWriter());
            }else if(roleId.equals("2")){
                //for getting Pending Sample for processing in Lab
                List<SampleDetails>sdArray = LabAdminDashBoard.getPendingSamplesinProcess(user.getsEMP_CODE());
                gson.toJson(sdArray, response.getWriter());
            }
        } else if ("getIndustryByTse".equalsIgnoreCase(url)) {
            //for getting Industry By Tse
            if(roleId.equals("3")){
                String empcode = request.getParameter("empCode");
                List indArray = SharedOperations.getIndustryByTse(empcode);
                gson.toJson(indArray, response.getWriter());
            }
        } else {
            response.getWriter().println("<b>No Page Found</b>");
        }
    }

    public HashMap<String, String> getByQuery(String query) {
        HashMap<String, String> hm = new HashMap<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    hm.put("value", res.getString(1));
                    System.out.println(res.getString(1));
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "getByQuery() inner");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getByQuery() outer");
        }
        return hm;
    }

    private List<MstDepartment> getCustomerDetails(String custid) {
        List<MstDepartment> departsArr = new ArrayList<>();
        String query = "select * from mst_department where cust_id = '" + custid +"'";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstDepartment departs = new MstDepartment();
                    departs.setDepartmentId(res.getString(1));
                    departs.setDepartmentName(res.getString(2));
                    departs.setHodName(res.getString(4));
                    departs.setHodEmail(res.getString(5));
                    departs.setHodContact(res.getString(6));
                    departs.setUpdatedBy(res.getString(7));
                    departsArr.add(departs);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getCustomerDetails()");
        }
        return departsArr;
    }

    public HashMap<String, String> getControlTable() {
        HashMap<String, String> gl = new HashMap<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select * from control_table");) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    gl.put(res.getString(1), res.getString(2));
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getControlTable()");
        }
        return gl;
    }

    private void uploadData(HashMap<String, String> globallist) {
        HashMap<String, String> gl = new HashMap<>();

        try (Connection con = DatabaseConnectionFactory.createConnection();) {
//            for (int i = 0; i < globallist.size(); i++) {
//                PreparedStatement pst = con.prepareStatement("update control_table set p_value = ? where param = ?");
//                pst.setString(1, globallist.get(i));
//                pst.setString(2, '');
//                pst.execute();
//            }
            for (Map.Entry entry : globallist.entrySet()) {
                PreparedStatement pst = con.prepareStatement("update control_table set p_value = ? where param = ?");
                pst.setString(1, (String) entry.getValue());
                pst.setString(2, (String) entry.getKey());
                System.out.println(entry.getKey() + " " + entry.getValue());
                pst.execute();
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "uploadData()");
        }
    }

    private HashMap<String, String> getEmployeeDetails(String empcode, String sCemDBPath) {
        String query = "SELECT EMP_CODE, EMP_NAME, EMAIL_ID, CNTRLG_EMP_CODE FROM COM_EMP_DB.EMP_DB WHERE EMP_STATUS_CODE = 3 AND EMP_CODE = ?";
        HashMap<String, String> emp = new HashMap<>();
        try (Connection con = DatabaseConnectionFactory.createCEMConnection(sCemDBPath);
                PreparedStatement pst = con.prepareStatement(query);
                ) {
            pst.setString(1, empcode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                emp.put("empcode", res.getString(1));
                emp.put("empname", res.getString(2));
                emp.put("empemail", res.getString(3));
                emp.put("empctrlemp", res.getString(4));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empcode);
        }
        return emp;
    }

    private Map<String, String> getRejectedReasonsList() {
        Map<String,String> reasons = new HashMap<>();
           try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT rea_id,reason_remark FROM reject_reason");) {
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                reasons.put(res.getString(1),res.getString(2));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return reasons;
    }
}