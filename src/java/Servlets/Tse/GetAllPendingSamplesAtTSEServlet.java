package Servlets.Tse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.TSEoperations;
import viewModel.MessageDetails;

public class GetAllPendingSamplesAtTSEServlet extends HttpServlet {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MessageDetails md = (MessageDetails) request.getAttribute("messageDetails");
        HttpSession session = request.getSession();
        globals.User user = new globals.User();
        user = (globals.User) session.getAttribute("sUser");
        String sampleType = "";
        sampleType = (request.getParameter("sampleType")!=null?request.getParameter("sampleType"):(String)request.getAttribute("sampleType"));
        List listCustDetails = TSEoperations.getcustomerDetailsTSE(user.getsEMP_CODE(), session.getAttribute("NotifyDaysLimit").toString(),sampleType);
        session.setAttribute("customerDetails", listCustDetails);
        session.setAttribute("sampleType", sampleType);
        session.setAttribute("sampleTypeNo", (sampleType.equals("OTS")?"1":"0"));
        session.setAttribute("notidetails", TSEoperations.getindustryTse(user.getsEMP_CODE(), session.getAttribute("NotifyDaysLimit").toString(), request));
        RequestDispatcher rd = request.getRequestDispatcher("getAllPendingSamples.jsp");
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
