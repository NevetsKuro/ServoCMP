package Servlets.Tse;

import Exceptions.MyLogger;
import communicate.SendMail;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;

@MultipartConfig
public class TseFileUploadServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MessageDetails md = new MessageDetails();
        HttpSession session = request.getSession();
        globals.User user = new globals.User();
        user = (globals.User) session.getAttribute("sUser");
        HashMap<String,String> hm = new HashMap<String,String>();
        hm.put("To", "");
        hm.put("CC", "");
        hm.put("subject", "");
        hm.put("body", "");
        try {
            md = SendMail.sentMailTSEToCustomer(
                    request.getParameter("sampleID"), 
                    request.getParameter("customerId"), 
                    request.getParameter("custsampleinfoId"), 
                    getServletContext().getRealPath("/TempFile"), 
                    request.getParameter("cbLabUploadedDoc") != null, 
                    user.getsEMP_CODE(),
                    md,hm);
        } catch (Exception ex) {
            MyLogger.logIt(ex, "TseFileUploadServlet.processRequest() ");
        }
        request.setAttribute("messageDetails", md);
        RequestDispatcher rd = request.getRequestDispatcher("/inprocessTseSamplesServlet?status=4");
        rd.forward(request, response);
    }

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
