package Servlets.Lab;

import globals.User;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.LABoperations;
import operations.FileOperations;
import viewModel.SampleDetails;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.Part;
import viewModel.FileDetails;
import viewModel.MessageDetails;
import viewModel.TestResultDetails;

@MultipartConfig
public class SendTestResultToTSE extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MessageDetails md = new MessageDetails();
        TestResultDetails trd = new TestResultDetails();
        FileDetails file = new FileDetails();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        SampleDetails sd = (SampleDetails) session.getAttribute("gtsm");
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Part filePart = request.getPart("testReport");
        if(FileOperations.isSafe(filePart)){
            file.setSaveStatus("0");
            file.setUploadedBy(user.getsEMP_CODE());
            trd.setTestIds(request.getParameterValues("evaluatetestIds"));
            trd.setValues(request.getParameterValues("evaluatetestValues"));
            trd.setRemarks(request.getParameterValues("evaluatetestRemarks"));
            trd.setConclusion(request.getParameter("finalTestRemarks"));
            if (request.getPart("testReport").getSize() > 0) {
                file.setFile(request.getPart("testReport"));
                file.setFileExt(".pdf");
                file.setFileName(sd.getSampleId());
                file.setUploadTime(format.format(new Date()));
                file.setFileType(file.getFile().getContentType());
                md = FileOperations.checkandUploadDoc(file,sd);
            }
            if (request.getPart("testReport").getSize() > 0) {
                file.setSaveStatus("YES");
                md = LABoperations.sendTestResult(sd, sd.getMstProd().getProId(), file.getSaveStatus(), trd, user.getsEMP_CODE(), md);
            }
            if (request.getPart("testReport").getSize() < 0) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Failed to Upload File. Test Result were NOT Sent. No Changes were made.");
            }
            if (request.getPart("testReport").getSize() <= 0) {
                file.setSaveStatus(sd.getFileuploadStatus());
                md = LABoperations.sendTestResult(sd, sd.getMstProd().getProId(), file.getSaveStatus(), trd, user.getsEMP_CODE(), md);
                md.setFilemsgClass("text-warning");
                md.setFileMsg("Test Result sent Successfully. But no file was Uploaded as no file was Selected.");
            }
            request.setAttribute("messageDetails", md);
            RequestDispatcher rd = null;
            switch (request.getParameter("Flag")) {
                case "AddResult":
                    rd = request.getRequestDispatcher("GetReceivedSample?status=2");
                    break;
                case "EditResult":
                    md.setFileMsg("Test Result sent Successfully. If you have not uploaded any file this time, your previously uploaded file will be considered.");
                    rd = request.getRequestDispatcher("GetReceivedSample?status=3");
                    break;
                default:
                    rd = request.getRequestDispatcher("/errorPages/error-404.html");
            }
            rd.forward(request, response);
        }else{
            response.setContentType("text/html");
                PrintWriter pw = response.getWriter();
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print("<h1>Reason: The File contains invalid entries.</h1>\n<pre>\n");
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
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
