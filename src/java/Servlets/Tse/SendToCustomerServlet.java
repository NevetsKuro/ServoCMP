package Servlets.Tse;

import Exceptions.MyLogger;
import com.google.gson.Gson;
import communicate.SendMail;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.FileOperations;
import operations.TSEoperations;
import viewModel.MessageDetails;

@MultipartConfig
public class SendToCustomerServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        MessageDetails md = new MessageDetails();
        HttpSession session = request.getSession();
        globals.User user = new globals.User();
        user = (globals.User) session.getAttribute("sUser");
        String labCode1 = request.getParameter("labCode1");
        String labCode2 = request.getParameter("labCode2");
        Boolean test = false;
        if(labCode2!=null&&!labCode2.equals("")){
            test = (FileOperations.isSafe(request.getPart("doc1")) && FileOperations.isSafe(request.getPart("doc2"))?true:false);
        }else{
            test = FileOperations.isSafe(request.getPart("doc1"));
        }
        if(test){
            md = TSEoperations.insertUploadedTestDoc(request.getParameter("sampleID"), request.getPart("doc1"), user.getsEMP_CODE(), labCode1);
            if(labCode2!=null&&!labCode2.equals("")){
                md = TSEoperations.insertUploadedTestDoc(request.getParameter("sampleID"), request.getPart("doc2"), user.getsEMP_CODE(), labCode2);
            }
            HashMap<String,String> hm = new HashMap<String,String>();
            hm.put("To", request.getParameter("fm1Value"));
            hm.put("CC", request.getParameter("fm2Value"));
            hm.put("subject", request.getParameter("fm3Value"));
            hm.put("body", request.getParameter("fm4Value"));
            if (md.isStatus()) {
                md = TSEoperations.sendtoCustomer(request.getParameter("sampleID"), request.getParameter("finalRemarks"), md);
                if (md.isStatus()) {
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
                        MyLogger.logIt(ex, "SendToCustomerServlet");
                    }
                }
            }
            response.getWriter().write(gson.toJson(md));
        }else{
            response.getWriter().write(gson.toJson("The Uploaded File is not a valid File. Please Try again."));
        }
        
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
