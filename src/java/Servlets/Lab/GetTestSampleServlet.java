package Servlets.Lab;

import DAOs.MstTestDAO;
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
import viewModel.SampleDetails;
import viewModel.MstTest;

public class GetTestSampleServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sampleID = request.getParameter("smplid");
        String labCode = request.getParameter("labCode");
        SampleDetails sd = LABoperations.gettestSample(request.getParameter("smplid"), request.getParameter("status").substring(0, 1), labCode);
        List<MstTest> testMaster = new ArrayList();
        HttpSession session = request.getSession();
        testMaster = (List<MstTest>) session.getAttribute("testMaster");
        if (testMaster == null) {
            session.setAttribute("testMaster", MstTestDAO.gettestMaster());
            testMaster = (List<MstTest>) session.getAttribute("testMaster");
        }
        List<MstTest> liExistTst = LABoperations.getsampleTestParameters(sd.getSampleId(),labCode);
        List<MstTest> limproTest = LABoperations.getmprodTest(sampleID,liExistTst, sd.getMstProd().getProId(), labCode);
        session.setAttribute("gtsm", sd);
        request.setAttribute("existTestList", liExistTst);
        request.setAttribute("mproTest", limproTest);
        RequestDispatcher rd = request.getRequestDispatcher("evaluateTest.jsp");
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
