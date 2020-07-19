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
import viewModel.MstTest;
import viewModel.SampleDetails;

public class getSampleTestResultServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String labCode = request.getParameter("labCode");
        SampleDetails sd = LABoperations.gettestSample(request.getParameter("smplid"), request.getParameter("status").substring(0, 1),labCode);
        List<MstTest> testMaster = new ArrayList();
        testMaster = (List<MstTest>) session.getAttribute("testMaster");
        List<MstTest> liExistTst = LABoperations.getsampleTestResults(sd.getSampleId(),labCode);
        List<MstTest> limproTest = LABoperations.getmprodTest(request.getParameter("smplid"),liExistTst, sd.getMstProd().getProId(),labCode);
        MstTest temp;
        MstTest mtest;
        String testId = "";
        if (testMaster == null) {
            session.setAttribute("testMaster", MstTestDAO.gettestMaster());
            testMaster = (List<MstTest>) session.getAttribute("testMaster");
        }
        for (int q = 0; q < limproTest.size(); q++) {
            temp = (MstTest) limproTest.get(q);
            testId = temp.getTestId();
            for (int r = 0; r < liExistTst.size(); r++) {
                mtest = (MstTest) liExistTst.get(r);
                if (testId.equals(mtest.getTestId())) {
                    temp.setTestVal(mtest.getTestVal());
                    temp.setTestRemarks(mtest.getTestRemarks());
                    break;
                }
            }
            limproTest.set(q, temp);
        }
        session.setAttribute("gtsm", sd);
        request.setAttribute("existTestList", liExistTst);
        request.setAttribute("mproTest", limproTest);
        RequestDispatcher rd = request.getRequestDispatcher("editSampleTestResult.jsp");
        if(request.getParameter("status").equals("4")){
            rd = request.getRequestDispatcher("sampleSentToCustomer.jsp");
        }
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
