package TseAdmin;

import DAOs.MstProdTest;
import DAOs.MstProductDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;
import viewModel.MstProduct;
import viewModel.MstTest;

public class TestSpecServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        String testId = "";
        testId = request.getParameter("testId");
        if (isAjax(request)) {
            if (testId != "" && testId != null) {
                Gson gson = new Gson();
                String othValues = MstProdTest.getOtherVal(testId);
                if (othValues == "") {
                    othValues = "F";
                }
//                System.out.println(othValues);
                response.getWriter().write(gson.toJson(othValues));
            } else {
                List<MstTest> mstTest = null;
                Gson gson = new Gson();
                if (user.getRole_id().equals("3")) {
                    mstTest = MstProdTest.getMktProdTest(request.getParameter("proName"));
                } else if (user.getRole_id().equals("4")) {
                    mstTest = MstProdTest.getLabProdTest(request.getParameter("proName"));
                } else {
                    response.sendError(400);
                }
                response.getWriter().write(gson.toJson(mstTest));
            }
        } else {
            List<MstProduct> mPro = MstProductDAO.listAllProducts("SELECT PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_PRODUCT WHERE ACTIVE = 1");
            List<MstTest> valChk = MstProdTest.getvalChk();
            List<MstTest> mTest = null;
            if (user.getRole_id().equals("3")) {
                mTest = MstProdTest.getAllMktTest();
            } else if (user.getRole_id().equals("4")) {
                mTest = MstProdTest.getAllLabTest();
            } else {
                response.sendError(400);
            }

            if (!isAjax(request)) {
                request.setAttribute("valChk", valChk);
    //            request.setAttribute("otherVal", MstProdTest.getOtherVal());
                request.setAttribute("testSpecs", mTest);
                request.setAttribute("mPro", mPro);
                RequestDispatcher rd = request.getRequestDispatcher("manageTestSpec.jsp");
                rd.forward(request, response);
            } else {
//                new Gson().toJson(mPro, response.getWriter());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        MstTest mTest = new MstTest();
        mTest.setUpdatedBy(user.getsEMP_CODE());
        mTest.getMstTestParam().setCheckId(request.getParameter("valChk"));
        switch (mTest.getMstTestParam().getCheckId()) {
            case "0":
                break;
            case "1":
                mTest.getMstTestParam().setMinValue(request.getParameter("minVal"));
                break;
            case "2":
                mTest.getMstTestParam().setMaxValue(request.getParameter("maxVal"));
                break;
            case "3":
                mTest.getMstTestParam().setMinValue(request.getParameter("minMinVal"));
                mTest.getMstTestParam().setMaxValue(request.getParameter("minMaxVal"));
                break;
            case "4":
                mTest.getMstTestParam().setTypValue(request.getParameter("eqVal"));
                mTest.getMstTestParam().setDevValue(request.getParameter("devVal"));
                break;
            case "5":
                mTest.getMstTestParam().setOtherVal("YES");
                break;
            case "6":
                mTest.getMstTestParam().setMaxValue(request.getParameter("maxDelVal"));
                break;
        }
        switch (request.getServletPath()) {
            case "/TseAdmin/AddTestSpec":
                mTest.setProId(request.getParameter("proName"));
                mTest.setTestId(request.getParameter("testName"));
                md = MstProdTest.addMktTestSpec(mTest);
                md.setModalTitle("Add Test Specification Details");
                break;
            case "/TseAdmin/UpdateTestSpec":
                mTest.setProId(request.getParameter("proId"));
                mTest.setTestId(request.getParameter("testId"));
                md = MstProdTest.UpdateMktTestSpec(mTest);
                md.setModalTitle("Update Test Specification Details");
                break;
            case "/LabAdmin/AddTestSpec":
                mTest.setProId(request.getParameter("proName"));
                mTest.setTestId(request.getParameter("testName"));
                md = MstProdTest.addLabTestSpec(mTest);
                md.setModalTitle("Add Test Specification Details");
                break;
            case "/LabAdmin/UpdateTestSpec":
                mTest.setProId(request.getParameter("proId"));
                mTest.setTestId(request.getParameter("testId"));
                md = MstProdTest.UpdateLabTestSpec(mTest);
                md.setModalTitle("Update Test Specification Details");
                break;
            default:
                response.sendError(404);
        }
        if (!isAjax(request)) {
            request.setAttribute("messageDetails", md);
            doGet(request, response);
        } else {
            new Gson().toJson(md, response.getWriter());
        }
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
