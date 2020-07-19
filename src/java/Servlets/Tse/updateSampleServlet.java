package Servlets.Tse;

import DAOs.MstProductDAO;
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
import operations.SharedOperations;
import org.apache.commons.lang3.ArrayUtils;
import viewModel.SampleDetails;
import operations.TSEoperations;
import viewModel.MessageDetails;
import viewModel.MstLab;
import viewModel.MstTest;
import viewModel.ReportDetails;

public class updateSampleServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        List<MstTest> testMaster = new ArrayList();
        HttpSession session = request.getSession();
        testMaster = (List<MstTest>) session.getAttribute("testMaster");
        if (testMaster == null) {
            session.setAttribute("testMaster", MstTestDAO.gettestMaster());
            testMaster = (List<MstTest>) session.getAttribute("testMaster");
        }
        String labCode = request.getParameter("labCode");
        SampleDetails sampleDetails = TSEoperations.geteditsendsampletoLAB(request.getParameter("smplid"),labCode);
        testMaster=MstTestDAO.getProdSpecWiseTestMaster( sampleDetails.getMstProd().getProId() );
        sampleDetails.setStatusId(request.getParameter("status"));
        
        List<MstLab> ml = SharedOperations.getLabdetails();
        for (MstLab mlab: ml) {
            if(mlab.getLabCode().equals(labCode)){
                sampleDetails.getMstLab().setLabName(mlab.getLabName());
                sampleDetails.getMstLab().setLabAuthority(mlab.getLabAuthority());
            }
        }
        
        List pl = SharedOperations.getPriorityMstdetails();
        String temp = request.getParameter("smplid");
        List<MstTest> liExistTst = SharedOperations.getTestParametersSampleIdwise(temp,labCode);
        List<MstTest> ignoreliExistTst = SharedOperations.getOtherTestParametersSampleIdwise(temp,labCode);
        List<ReportDetails> listRptDetails;
        listRptDetails = MstProductDAO.getCategoryByProduct(sampleDetails.getMstProd().getProId());
        List liOtherTst = new ArrayList();
        String strTempId;
        int flag = 0;
        for (int i = 0; i < testMaster.size(); i++) {
            strTempId = testMaster.get(i).getTestId();
            flag = 0;
            for (int j = 0; j < liExistTst.size(); j++) {
                if (liExistTst.get(j).getTestId().equals(strTempId)) {
                    liExistTst.get(j).setTestName(testMaster.get(i).getTestName());
                    liExistTst.get(j).setSampleqty(testMaster.get(i).getSampleqty());
                    liExistTst.get(j).getMstTestParam().setCheckId(testMaster.get(i).getMstTestParam().getCheckId());
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                liOtherTst.add(testMaster.get(i));
            }
        }
        request.setAttribute("labmaster", ml);
        request.setAttribute("prioritymaster", pl);
        request.setAttribute("preTestList", liExistTst);
        request.setAttribute("ignoreTestList", ignoreliExistTst);
        request.setAttribute("addTestList", liOtherTst);
        request.setAttribute("mandTestList", listRptDetails);
        request.setAttribute("sampleType", request.getParameter("sampleType"));
        session.setAttribute("cs", sampleDetails);
        switch (request.getParameter("status")) {
            case "0":
                rd = request.getRequestDispatcher("getCreatedToSendLab.jsp");
                break;
            case "1":
                rd = request.getRequestDispatcher("getCreatedToSendLab.jsp");
                break;
            case "2":
                rd = request.getRequestDispatcher("sampleReceivedByLab.jsp");
                break;
            case "3":
                rd = request.getRequestDispatcher("getTestedSample.jsp");
                break;
            case "4":
                rd = request.getRequestDispatcher("getSentToCustomer.jsp");
                break;
            default:
                response.sendError(response.SC_NOT_FOUND);
        }
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MessageDetails md = new MessageDetails();
        SampleDetails sd = (SampleDetails) request.getSession().getAttribute("cs");
        if (sd == null) {
            sd = new SampleDetails();
        }
        sd.setQtyDrawn(request.getParameter("qtyDrawn"));
        sd.setTopupQty(request.getParameter("topupQty"));
        sd.getMstLab().setLabCode(request.getParameter("labCode"));
        sd.getMstLab().setLabAuthority(request.getParameter("labAuthority"));
        sd.setSamplepriorityId(request.getParameter("samplepriorityId"));
        sd.setSampledrawnRemarks(request.getParameter("sampledrawnRemarks"));
        sd.setSamplepriorityRemarks(request.getParameter("samplepriorityRemarks"));
        String[] test = request.getParameterValues("testIds");
        String[] addtest = request.getParameterValues("addtestIds");
        if (addtest != null) {
            if (addtest.length > 0) {
                sd.setTestIds(ArrayUtils.addAll(test, addtest));
            }
        } else {
            sd.setTestIds(test);
        }
        md = TSEoperations.updateSample(sd);
        request.setAttribute("messageDetails", md);
        RequestDispatcher rd = request.getRequestDispatcher("GetCreatedSamples?status=0");
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
