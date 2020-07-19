package LabAdmin;

import DAOs.MstLabDAO;
import DAOs.MstUserDAO;
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
import viewModel.MstLab;
import viewModel.MstUser;

public class LabAccessServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstLab> mLab = null;
        List<MstUser> mUser = MstUserDAO.listAllUser("SELECT MST_USER.EMP_CODE, MST_USER.EMP_NAME, MST_USER.EMP_EMAIL, MST_USER.CNTRLG_EMP_CODE, MST_USER.CNTRLG_EMP_NAME, MST_USER.CNTRLG_EMP_EMAIL, MST_USER.ROLE_ID, MST_USER.UPDATED_BY, MST_USER.UPDATED_DATETIME, MST_ROLE.ROLE_NAME, MST_USER.ACTIVE FROM MST_USER INNER JOIN MST_ROLE ON MST_USER.ROLE_ID = MST_ROLE.ROLE_ID WHERE MST_USER.ROLE_ID = 2 AND MST_USER.ACTIVE = 1");
        RequestDispatcher rd = null;
        switch (request.getServletPath()) {
            case "/LabAdmin/LabAccess":
                if (request.getParameter("empCode") == null || request.getParameter("empCode") == "") {
                    mLab = MstLabDAO.listLab();
                } else {
                    mLab = MstLabDAO.listLab(request.getParameter("empCode"));
                }
                break;
            default:
                mLab = MstLabDAO.listLab();
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (mLab.isEmpty()) {
                response.getWriter().write(gson.toJson(mLab));
            } else {
                response.getWriter().write(gson.toJson("No Test Found."));
            }
        } else {
            request.setAttribute("messageDetails", request.getAttribute("messageDetails"));
            request.setAttribute("mLab", mLab);
            request.setAttribute("mUser", mUser);
            rd = request.getRequestDispatcher("LabAccess.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = null;
        switch (request.getServletPath()) {
            case "/LabAdmin/LabAccess":
                md = MstLabDAO.updateLabEmp(request.getParameter("newEmpCode"), request.getParameter("labCodes").split(","), user.getsEMP_CODE());
                md.setModalTitle("Lab Access Details");
                request.setAttribute("messageDetails", md);
                doGet(request, response);
                break;
            case "/LabAdmin/AddLab":
                MstLab mLab = new MstLab();
                mLab.setLabCode(request.getParameter("labLocCode"));
                mLab.setLabName(request.getParameter("labName"));
                mLab.setLabAdd1(request.getParameter("labAdd"));
                mLab.setLabAdd2(request.getParameter("labAdd2"));
                mLab.setLabAdd3(request.getParameter("labAdd3"));
                mLab.setLabAuthority(request.getParameter("labEmpCode"));
                mLab.setLabType(request.getParameter("labType"));
                mLab.setActive("1");
                mLab.setLabEmpCode(user.getsEMP_CODE());
                md = MstLabDAO.addLab(mLab);
                if(md.isStatus()){
                    MstLabDAO.addLabTestMapping(mLab);
                }
                md.setModalTitle("Add Lab Details.");
                 break;
            case "/LabAdmin/UpdateLab":
                MstLab umLab = new MstLab();
                umLab.setLabCode(request.getParameter("labLocCode"));
                umLab.setLabName(request.getParameter("labName"));
                umLab.setLabAdd1(request.getParameter("labAdd"));
                umLab.setLabAdd2(request.getParameter("labAdd2"));
                umLab.setLabAdd3(request.getParameter("labAdd3"));
                umLab.setLabAuthority(request.getParameter("labEmpCode"));
                umLab.setActive("1");
                umLab.setLabEmpCode(user.getsEMP_CODE());
                md = MstLabDAO.updateLab(umLab);
                md.setModalTitle("Lab Update Details.");
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
