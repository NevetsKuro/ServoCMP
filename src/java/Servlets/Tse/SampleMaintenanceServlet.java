package Servlets.Tse;

import globals.User;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.TSEoperations;
import viewModel.MessageDetails;
import viewModel.SampleDetails;

public class SampleMaintenanceServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("GetAllPendingSamplesAtTSE");
        HttpSession session = request.getSession();
        User user = (globals.User) session.getAttribute("sUser");
        SampleDetails sd = (SampleDetails) session.getAttribute("ms");
        sd.setPostponetillDate(request.getParameter("postponetillDate"));
        sd.setPostponeReason(request.getParameter("postponeReason"));
        sd.setSamplecreatedBy(user.getsEMP_CODE());
        MessageDetails md = TSEoperations.postponeSample(sd);
        md.setModalTitle("Sample Defered Details.");
        request.setAttribute("sampleType", request.getParameter("sampleType"));
        request.setAttribute("messageDetails", md);
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
