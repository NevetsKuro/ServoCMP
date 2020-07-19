package Servlets.Lab;

import DAOs.MstApplicationDAO;
import DAOs.MstCustomerDAO;
import DAOs.MstDepartmentDAO;
import DAOs.MstProductDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.LABoperations;
import viewModel.MessageDetails;
import viewModel.MstApplication;
import viewModel.MstCustomer;
import viewModel.MstDepartment;
import viewModel.MstProduct;

public class GetReceivedSampleServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        MessageDetails md = (MessageDetails) request.getAttribute("messsge");
        globals.User user = new globals.User();
        user = (globals.User) session.getAttribute("sUser");
        
        List listpndSmpls = new ArrayList();
        listpndSmpls = LABoperations.getLabSampleDetails(user.getsEMP_CODE(), request.getParameter("status"));
        session.setAttribute("receivedSampleDetails", listpndSmpls);
        session.setAttribute("notidetails", LABoperations.getIndustryWiseLABSummary(user.getsEMP_CODE(), request));
        
        List<MstProduct> mPro = MstProductDAO.listAllProducts("SELECT PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_PRODUCT WHERE ACTIVE = 1");
        List<MstCustomer> mCus = MstCustomerDAO.listAllCustomer();
        List<MstDepartment> mDept = MstDepartmentDAO.listAllDepartment();
        String query1 = "SELECT APPL_ID, APPL_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_APPLICATION WHERE ACTIVE = 1";
        List<MstApplication> mApp = MstApplicationDAO.listAllApplication(query1);

        request.setAttribute("mPro", mPro);
        request.setAttribute("mCus", mCus);
        request.setAttribute("mDept", mDept);
        request.setAttribute("mApp", mApp);
        
        RequestDispatcher rd = null;   
        if (request.getParameter("status").equals("2")) {
            rd = request.getRequestDispatcher("allReceivedSample.jsp");
        } else if (request.getParameter("status").equals("3")) {
            rd = request.getRequestDispatcher("allSampleSentToTSE.jsp");
        } else if(request.getParameter("status").equals("4")){
            rd = request.getRequestDispatcher("allSampleSentToCustomer.jsp");
        }
        rd.forward(request, response);
    }
// url for searching this page : " /Tse/GetReceivedSample?status=2 "
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
