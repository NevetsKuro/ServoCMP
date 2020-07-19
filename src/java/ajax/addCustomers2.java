package ajax;

import DAOs.MstCustomerDAO;
import Exceptions.MyLogger;
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
import viewModel.MstCustomer;
import viewModel.MstIndustry;
import viewModel.StevenModels.CustomerDetails;
import viewModel.StevenModels.Customers;


public class addCustomers2 extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/Tse/GetCustomers":
                listCustomer(request, response);
                break;
            case "/Tse/AllCustomer":
                listCustomer(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Gson gson = new Gson();
        List<Customers> custList = new ArrayList<Customers>();
        String json = request.getParameter("jsonObjs");
        String json2 = request.getParameter("commonDetails");
        Type listType =  new TypeToken<List<Customers>>(){}.getType();
        //Type listType2 =  new TypeToken<List<CustomerDetails>>(){}.getType();
        
        custList = gson.fromJson(json,listType);
        CustomerDetails custDetailList = gson.fromJson(json2,CustomerDetails.class);
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        RequestDispatcher rd = null;
        MstIndustry mstInd = new MstIndustry();
        mstInd.setIndId(request.getParameter("customerIndustry"));
        mstInd.setIndName(request.getParameter("customerIndustryName"));
        
        switch (request.getServletPath()) {
            case "/addCustomers2":
                md = AddCustomer(custList,custDetailList,mstInd, user);
                md.setModalTitle("Add Customer Details");
                break;
        }
        if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            response.getWriter().write(gson.toJson(md));
        } else {
            request.setAttribute("messageDetails", md);
            rd = request.getRequestDispatcher("Tse/ManageSample");
            rd.forward(request, response);
        }
        
    }
    
    private void listCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<MstCustomer> listCustomer;
        if (!"".equals(request.getParameter("Industry"))) {
            listCustomer = MstCustomerDAO.listAllCustomer(request.getParameter("Industry"));
        } else {
            listCustomer = MstCustomerDAO.listAllCustomer();
        }
        if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            Gson gson = new Gson();
            if (!listCustomer.isEmpty()) {
                response.getWriter().write(gson.toJson(listCustomer));
            } else {
                response.getWriter().write(gson.toJson("No Customer Found."));
            }
        }
    }
    
    public static MessageDetails AddCustomer(List<Customers> custList, CustomerDetails cust,MstIndustry mst,User user) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_CUSTOMER (CUST_ID, CUST_NAME, IND_ID, EMP_CODE, UPDATED_BY, "
                        + "UPDATED_DATE, ACTIVE) VALUES (?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'), ?)")) {
            md.setModalMessage("");
            try {
                for(Customers custL:custList){

                    pst.setString(1, custL.getCode());
                    pst.setString(2, custL.getName().toUpperCase());
                    pst.setString(3, mst.getIndId());
                    pst.setString(4, user.getsEMP_CODE());
                    pst.setString(5, user.getsEMP_CODE());
                    pst.setString(6, "1");
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Customer '" + custL.getName().toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Customer '" + custL.getName().toUpperCase() + "'. No changes were made.</span><br/><br/>");
                    }
                }
            } catch (Exception ex) {
                    MyLogger.logIt(ex, "AddCustomer()");
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Customer. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), user.getsEMP_CODE()) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique")) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Customer's Already exist in Master. </span><br/><br/>");
                    }
                }
//            for (String CustomerName : mstCust.getCustomerNames()) {
//                
//                    pst.setString(1, CustomerName.toUpperCase());
//                    pst.setString(2, mstCust.getMstIndustry().getIndId());
//                    pst.setString(3, mstCust.getUpdatedBy());
//                    pst.setString(4, mstCust.getUpdatedBy());
//                    pst.setString(5, mstCust.getActive());
//                    if (pst.executeUpdate() > 0) {
//                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Customer '" + CustomerName.toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
//                    } else {
//                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Customer '" + CustomerName.toUpperCase() + "'. No changes were made.</span><br/><br/>");
//                    }
//                
//            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, user.getsEMP_CODE());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Duplicate Customer or Customer Already Exist");
            }
        }
        return md;
    }
    
}
