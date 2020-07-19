package TseAdmin;

import DAOs.MstCustomerDAO;
import DAOs.MstLabDAO;
import DAOs.MstUserDAO;
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
import viewModel.MstCustomer;
import viewModel.MstLab;
import viewModel.MstUser;

public class TseAccessServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstCustomer> mCust = new ArrayList<>();
        List<MstUser> mUser = MstUserDAO.listAllUser("SELECT MST_USER.EMP_CODE, MST_USER.EMP_NAME, MST_USER.EMP_EMAIL, MST_USER.CNTRLG_EMP_CODE, MST_USER.CNTRLG_EMP_NAME, MST_USER.CNTRLG_EMP_EMAIL, MST_USER.ROLE_ID, MST_USER.UPDATED_BY, MST_USER.UPDATED_DATETIME, MST_ROLE.ROLE_NAME, MST_USER.ACTIVE FROM MST_USER INNER JOIN MST_ROLE ON MST_USER.ROLE_ID = MST_ROLE.ROLE_ID WHERE MST_USER.ACTIVE = 1");
        List<MstLab> mLab = MstLabDAO.listLabByLabType("CSL");
        List<MstLab> mLab2 = new ArrayList<>();

        MessageDetails md = null;
        String status = "Nada";

        switch (request.getServletPath()) {
            case "/TseAdmin/TseAccess":
                if (request.getParameter("empCode") == null || request.getParameter("empCode") == "") {
                    mCust = MstCustomerDAO.listTseCustomer();
                } else {
                    mCust = MstCustomerDAO.listTseCustomer(request.getParameter("empCode"));
                }
                break;
            case "/TseAdmin/TseAccessSync":
                status = MstLabDAO.syncCntrlOfficersMstUser(getServletContext().getInitParameter("cemDBPath"));
                break;
            case "/TseAdmin/TseLabAccess":
                if (request.getParameter("labCode") != null && request.getParameter("empCode") != null) {
                    md = MstLabDAO.updateLabTse(request.getParameter("empCode"), request.getParameter("labCode"));
                }
                break;
            case "/TseAdmin/TseLabAccessExist":
                if (request.getParameter("For").equals("All")) {
                    mLab2 = MstLabDAO.listTseLabMapped();
                } else {
                    if (request.getParameter("empCode") != null) {
                        status = MstLabDAO.checkLabTseAccess(request.getParameter("empCode"));
                    }
                }
                break;
            default:
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            switch (request.getServletPath()) {
                case "/TseAdmin/TseAccess":
                    if (!mCust.isEmpty()) {
                        response.getWriter().write(gson.toJson(mCust));
                    } else {
                        response.getWriter().write(gson.toJson("No Test Found."));
                    }
                    break;
                case "/TseAdmin/TseAccessSync":
                    gson.toJson(status, response.getWriter());
                    break;
                case "/TseAdmin/TseLabAccess":
                    gson.toJson(md, response.getWriter());
                    break;
                case "/TseAdmin/TseLabAccessExist":
                    if (request.getParameter("For").equals("All")) {
                        gson.toJson(mLab2, response.getWriter());
                    } else {
                        gson.toJson(status, response.getWriter());
                    }
                    break;
                case "/TseAdmin/TseLabAccessDel":
                    String empcode = request.getParameter("empCode");
                    String labcode = request.getParameter("labCode");
                    status = MstLabDAO.listTseLabDelete(empcode, labcode);
                    gson.toJson(status, response.getWriter());
                    break;
            }
        } else {
            request.setAttribute("messageDetails", request.getAttribute("messageDetails"));
            request.setAttribute("mCust", mCust);
            request.setAttribute("mLab", mLab);
            request.setAttribute("mUser", mUser);
            RequestDispatcher rd = request.getRequestDispatcher("TseAccess.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        if (request.getParameter("custNo") != null) {
            md = MstCustomerDAO.updateCustTse(request.getParameter("newEmpCode"), request.getParameter("custNo").split(","), user.getsEMP_CODE(), request.getParameter("custNames").split(","));
        } else {
            md.setModalMessage("No Customers are selected");
        }
        md.setModalTitle("Tse Access Details");
        request.setAttribute("messageDetails", md);
        doGet(request, response);
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
