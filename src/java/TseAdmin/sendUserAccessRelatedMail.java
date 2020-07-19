package TseAdmin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import communicate.SendMail;
import com.google.gson.Gson;
import communicate.MailTemplate;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;

public class sendUserAccessRelatedMail extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MessageDetails md = new MessageDetails();
        MailTemplate mt = new MailTemplate();
        mt.setTo(request.getParameter("toMailName"));
        mt.setCc(request.getParameter("ccMailName"));
        mt.setSubject(request.getParameter("subjectMailName"));
        mt.setMailContent(request.getParameter("bodyMailName"));
        mt.setTo("seng@indianoil.in");
        mt.setCc("seng@indianoil.in");
        boolean sendStatus = false;//sendMail.sendMailText("",toMailId,ccMailId,"",subjectMailText,bodyMailText);
        
        if (sendStatus == true) {
            String smplId = "";
            globals.User user = new globals.User();
            HttpSession session = request.getSession();
            user = (globals.User) session.getAttribute("sUser");
            SendMail.insertSentMailSummary(smplId, mt, user.getsEMP_CODE(), "ADMIN");
            md.setModalTitle("Mail Sent Status");
            md.setMsgClass("alert-success text-success");
            md.setModalMessage("Mail has sent Successfully.");
        } else {
            md.setModalTitle("Mail Sent Status");
            md.setMsgClass(" alert-success text-danger");
            md.setModalMessage("Error in sending mail.");
        }
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(md));
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
