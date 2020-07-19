package Servlets.Tse;

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
import viewModel.MstProduct;
import viewModel.MstTest;

public class TseTestSpecServlet extends HttpServlet {

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
                if (user.getRole_id().equals("1")) {
                    mstTest = MstProdTest.getMktProdTest(request.getParameter("proName"));
                } else if (user.getRole_id().equals("2")) {
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
            if (user.getRole_id().equals("1")) {
                mTest = MstProdTest.getAllMktTest();
            } else if (user.getRole_id().equals("2")) {
                mTest = MstProdTest.getAllLabTest();
            } else {
                response.sendError(400);
            }

            if (!isAjax(request)) {
                request.setAttribute("valChk", valChk);
    //            request.setAttribute("otherVal", MstProdTest.getOtherVal());
                request.setAttribute("testSpecs", mTest);
                request.setAttribute("mPro", mPro);
                RequestDispatcher rd = request.getRequestDispatcher("TseTestSpec.jsp");
                rd.forward(request, response);
            } else {
//                new Gson().toJson(mPro, response.getWriter());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
