package Servlets.Tse;

import DAOs.MstCustomerDAO;
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
import viewModel.MstCustomer;
import viewModel.MstIndustry;

public class ManageCustomersServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/Tse/GetCustomers":
            case "/TseAdmin/GetCustomers":
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        RequestDispatcher rd = null;
        MstIndustry mstInd = new MstIndustry();
        MstCustomer mstCust = new MstCustomer(mstInd);
        if (request.getParameter("customerIndustry")!=null) {
            mstInd.setIndId(request.getParameter("customerIndustry"));
            mstInd.setIndName(request.getParameter("customerIndustryName"));
            mstCust.setMstIndustry(mstInd);
        }
        mstCust.setActive("1");
        mstCust.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/Tse/AddCustomers":
                mstCust.setCustomerNames(request.getParameterValues("Customers"));
                md = MstCustomerDAO.AddCustomer(mstCust);
                md.setModalTitle("Add Customer Details");
                break;
            case "/Tse/UpdateCustomer":
                mstCust.setCustomerId(request.getParameter("custId"));
                mstCust.setCustomerName(request.getParameter("newCustName").toUpperCase());
                md = MstCustomerDAO.UpdateCustomer(mstCust);
                md.setModalTitle("Update Customer Details");
                md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Note: Kindly refresh the page for changes to take effective.</span><br/><br/>");
                break;
        }
        if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(md));
        } else {
            request.setAttribute("messageDetails", md);
            rd = request.getRequestDispatcher("Tse/ManageSample");
            rd.forward(request, response);
        }
        
    }
    
    private void listCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<MstCustomer> listCustomer = new ArrayList<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        String emp_code = user.getsEMP_CODE();
        
        if("Tse".equals(request.getParameter("queryBy"))){
            if(!"".equals(request.getParameter("Industry"))){
                listCustomer = MstCustomerDAO.listAllCustomerByTse(request.getParameter("Industry"),emp_code);
            }
        }else{
            if (!"".equals(request.getParameter("Industry"))) {
                listCustomer = MstCustomerDAO.listAllCustomer(request.getParameter("Industry"));
            } else {
                listCustomer = MstCustomerDAO.listAllCustomer();
            }
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
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
