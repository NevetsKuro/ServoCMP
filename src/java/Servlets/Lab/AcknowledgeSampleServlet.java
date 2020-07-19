package Servlets.Lab;

import DAOs.MstTestDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.ApplicationSQLDate;
import operations.LABoperations;
import operations.SharedOperations;
import viewModel.MessageDetails;
import viewModel.MstTest;
import viewModel.SampleDetails;

public class AcknowledgeSampleServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String labCode = request.getParameter("labCode");
        SampleDetails sd = LABoperations.getreceiveSample(request.getParameter("smplid"),labCode);
        switch (sd.getSamplepriorityId()){
            case "1":
                sd.setDefaultSelectedDate(ApplicationSQLDate.addDate(new Date(), Integer.parseInt(session.getAttribute("highpriorityDays").toString())));
                break;
            case "2":
                sd.setDefaultSelectedDate(ApplicationSQLDate.addDate(new Date(), Integer.parseInt(session.getAttribute("mediumpriorityDays").toString())));
                break;
            case "3":
                sd.setDefaultSelectedDate(ApplicationSQLDate.addDate(new Date(), Integer.parseInt(session.getAttribute("normalpriorityDays").toString())));
                break;
            default:
                sd.setDefaultSelectedDate(ApplicationSQLDate.getcurrentSQLDate().toString());
        }
        List<MstTest> testMaster = new ArrayList();
        testMaster = (List<MstTest>) session.getAttribute("testMaster");
        if (testMaster == null) {
            session.setAttribute("testMaster", MstTestDAO.gettestMaster());
            testMaster = (List<MstTest>) session.getAttribute("testMaster");
        }
        List<MstTest> liExistTst = LABoperations.getsampleTestParameters(sd.getSampleId(),sd.getMstLab().getLabCode());
        List resultDelayReason = SharedOperations.getMstTestResultDelayReasons();
        request.setAttribute("rs", sd);
        request.setAttribute("existTestList", liExistTst);
        request.setAttribute("resultDelayReasonList", resultDelayReason);
        RequestDispatcher rd = request.getRequestDispatcher("acknowledgeSample.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SampleDetails sd = new SampleDetails();
        sd.setSampleId(request.getParameter("smplid"));
        sd.setStringLabrecDate(ApplicationSQLDate.convertUtilDatetoString(ApplicationSQLDate.convertSqltoUtilDate(ApplicationSQLDate.getcurrentSQLDate())));
        sd.setLabrecRemarks(request.getParameter("samplereceivedRemarks"));
        sd.setResultdateextendReason("");
        sd.getMstLab().setLabCode(request.getParameter("samplelabCode"));
        sd.getMstEquip().setEquipmentId("");
        sd.setStringExptestresultDate(request.getParameter("labreceiveDate"));
        if (!"".equals(request.getParameter("delayReason"))) {
            sd.setResultdateextendReason(request.getParameter("delayReason"));
        }
        if (!"".equals(request.getParameter("Equipment"))) {
            sd.getMstEquip().setEquipmentId(request.getParameter("Equipment"));
        }
        MessageDetails md = LABoperations.receiveSample(sd);
        request.setAttribute("messageDetails", md);
        RequestDispatcher rd = request.getRequestDispatcher("/Lab/SampleSendByTSE");
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
