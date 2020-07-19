package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import operations.SharedOperations;

public class DownloadUploadedFileServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sampleId = request.getParameter("smplid");
        String labCode = request.getParameter("labCode");
        String typeofrole = request.getParameter("roleDoc");
        String fileName = sampleId + ".pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=" + fileName + ";");
        OutputStream out = response.getOutputStream();
        String query="";
        if(typeofrole.equals("TSE"))
        {
            query= "SELECT TEST_REPORT FROM CMP_TEST_REPORT_INFO where SAMPLE_ID='" + sampleId + "' AND LAB_CODE='"+labCode+"'";
        }
        if(typeofrole.equals("LAB"))
        {
            query= "SELECT LAB_RESULT_DOC FROM LAB_TEST_RESULT_DOC_INFO where SAMPLE_ID='" + sampleId + "' AND LAB_CODE='"+labCode+"'";
        }
        SharedOperations.downloadUploadedTestDoc(sampleId, fileName, query, out);
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
