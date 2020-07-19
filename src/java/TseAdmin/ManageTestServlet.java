package TseAdmin;

import DAOs.MstLabDAO;
import DAOs.MstTestDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;
import viewModel.MstTest;

public class ManageTestServlet extends HttpServlet {

   

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstTest> listTest = new ArrayList<MstTest>();
        switch (request.getServletPath()) {
            case "/TseAdmin/AllDeactiveTest":
            case "/LabAdmin/AllDeactiveTest":
                listTest = MstTestDAO.listAllTest("SELECT TEST_ID, TEST_NAME, UNIT, TEST_METHOD, SAMPLE_QTY, DISP_SEQ_NO, UPDATED_BY, UPDATED_DATETIME FROM MST_TEST WHERE ACTIVE = 0");
                break;
            case "GetTest":
                break;
            case "/TseAdmin/AllActiveTest":
            case "/LabAdmin/AllActiveTest":
            default:
                listTest = MstTestDAO.listAllTest("SELECT TEST_ID, TEST_NAME, UNIT, TEST_METHOD, SAMPLE_QTY, DISP_SEQ_NO, UPDATED_BY, UPDATED_DATETIME FROM MST_TEST WHERE ACTIVE = 1");
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (!listTest.isEmpty()) {
                response.getWriter().write(gson.toJson(listTest));
            } else {
                response.getWriter().write(gson.toJson("No Test Found."));
            }
        } else {
            request.setAttribute("Tests", listTest);
            RequestDispatcher rd = request.getRequestDispatcher("manageTest.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MessageDetails md = new MessageDetails();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        
        String testName = request.getParameter("testName");
        String testId = request.getParameter("testId");
        String dispSeq = request.getParameter("dispSeq");
        String sampleQty = request.getParameter("sampleQty");
        MstTest mTest = new MstTest();
        mTest.setTestName(testName);
        mTest.setUnit(request.getParameter("testUnit"));
        mTest.setTestMethod(request.getParameter("methodName"));
        mTest.setSampleqty(sampleQty);
        mTest.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/TseAdmin/AddTest":
            case "/LabAdmin/AddTest":
                mTest.setDispSeqNo(Integer.parseInt(dispSeq));
                if("".equals(sampleQty) || sampleQty == null){
                    mTest.setSampleqty("0");
                }
                mTest.setActive("1");
                md = MstTestDAO.addTest(mTest);
                if(md.isStatus()){
                    MstLabDAO.addNewTestLabMapping(mTest);
                }
                md.setModalTitle("Add Lab Test Details !!!");
                break;
            case "/TseAdmin/RemoveTest":
            case "/LabAdmin/RemoveTest":
                mTest.setTestId(testId);
                mTest.setTestName(testName);
                mTest.setActive("0");
                md = MstTestDAO.markInactive(mTest);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Test '" + mTest.getTestName() + "' has been Removed Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Test '" + mTest.getTestName() + "' cannot be Removed");
                }
                md.setModalTitle("Test Status");
                break;
            case "/TseAdmin/ActivateTest":
            case "/LabAdmin/ActivateTest":
                mTest.setTestName(request.getParameter("testNameInput"));
                mTest.setTestId(request.getParameter("testNameModal"));
                mTest.setActive("1");
                md = MstTestDAO.markInactive(mTest);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Test '" + mTest.getTestName() + "' has been Activated Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Test '" + mTest.getTestName() + "' cannot be Activated");
                }
                md.setModalTitle("Test Status");
                break;
            case "/TseAdmin/UpdateTest":
            case "/LabAdmin/UpdateTest":
                mTest.setDispSeqNo(Integer.parseInt(dispSeq));
                mTest.setTestName(testName);
                mTest.setTestId(testId);
                md = MstTestDAO.UpdateTest(mTest);
                md.setModalTitle("Test Update Status");
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Test '" + request.getParameter("oldTestName") + "' changed to '" + mTest.getTestName() + "' Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while changing Test '" + request.getParameter("oldTestName") + "' to '" + mTest.getTestName() + "'. Try again Later");
                }
                break;
        }
        request.setAttribute("messageDetails", md);
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }

}
