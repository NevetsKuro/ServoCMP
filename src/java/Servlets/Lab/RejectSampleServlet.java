/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Lab;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import com.google.gson.Gson;
import communicate.SendMail;
import globals.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.LABoperations;
import operations.SharedOperations;
import operations.TSEoperations;
import viewModel.MessageDetails;
import viewModel.MstLab;
import viewModel.MstUser;

/**
 *
 * @author NIT_steven
 */
public class RejectSampleServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String sampleId = request.getParameter("sampleId");
        String labCode = request.getParameter("labCode");
        String reason = request.getParameter("reason");
        String status = "";
        HttpSession session = request.getSession();
        globals.User user = new globals.User();
        user = (globals.User) session.getAttribute("sUser");
        status = rejectSample(sampleId, labCode);
        
        if (!status.equals("")) {
            User user2 = user;
            MstLab mstLab = SharedOperations.getLabdetails(labCode);
            MstUser user1 = SharedOperations.getTseOfficers(mstLab.getLabTseCode());
            MessageDetails md = SendMail.sendMailForRejectedSample(sampleId, user1, user2, reason);
            status += md.getModalMessage();
            status += addRejectionReason(sampleId, labCode, reason, user);
        }
        new Gson().toJson(status, response.getWriter());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private String rejectSample(String sampleId, String labCode) {
        int updateStatus = 0;
        String status = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst2 = con.prepareStatement(""
                        + "UPDATE SAMPLE_DETAILS SET STATUS_ID='0', IOM_REF_NO=null, LAB_RECEIVED_DATE=null, "
                        + "LAB_EQUIP_ID=null, LAB_RECEIVED_REMARKS = null, EXP_TEST_RESULT_DATE=null, RESULT_DATE_EXTEND_REASON = null "
                        + "where SAMPLE_ID = ? and lab_loc_code = ? ");) {
            pst2.setString(1, sampleId);
            pst2.setString(2, labCode);
            updateStatus = pst2.executeUpdate();
            switch (updateStatus) {
                case 1:
                    status = "Sample rejected successfully and send back to TSE \n";
                    break;
                default:
                    status = "Cannot be Rejected. Try Again Later \n";
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.rejectSample() ");
        }
        return status;
    }

    private String addRejectionReason(String sampleId, String labCode, String reason, User user) {
        int updateStatus = 0;
        String status = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO REJECTED_SAMPLES_HISTORY (sample_id,lab_code,reason,updated_by, updated_date ) VALUES (?,?,?,?,TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))");) {
            pst.setString(1, sampleId);
            pst.setString(2, labCode);
            pst.setString(3, reason);
            pst.setString(4, user.getsEMP_CODE());
            updateStatus = pst.executeUpdate();
            switch (updateStatus) {
                case 1:
                    status = "Done.";
                    break;
                default:
                    status = "Failure to add reason. Try Again Later";
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.addRejectionReason() ");
        }
        return status;
    }
}
