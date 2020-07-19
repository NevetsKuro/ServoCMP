package TseAdmin;

import CEM.Employee;
import DAOs.MstRoleDAO;
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
import viewModel.MstRole;
import viewModel.MstUser;

public class manageTseUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstUser> mstUser = null;
        List<MstRole> mstRole = MstRoleDAO.listAllTseRoles();
        switch (request.getServletPath()) {
            case "/TseAdmin/AllActiveUser":
                mstUser = MstUserDAO.listAllUser("SELECT MST_USER.EMP_CODE, MST_USER.EMP_NAME, MST_USER.EMP_EMAIL, MST_USER.CNTRLG_EMP_CODE, MST_USER.CNTRLG_EMP_NAME, MST_USER.CNTRLG_EMP_EMAIL, MST_USER.ROLE_ID, MST_USER.UPDATED_BY, MST_USER.UPDATED_DATETIME, MST_ROLE.ROLE_NAME, MST_USER.ACTIVE FROM MST_USER INNER JOIN MST_ROLE ON MST_USER.ROLE_ID = MST_ROLE.ROLE_ID WHERE MST_USER.ACTIVE = 1");
                break;
            case "/TseAdmin/AllDeactiveUser":
                mstUser = MstUserDAO.listAllUser("SELECT MST_USER.EMP_CODE, MST_USER.EMP_NAME, MST_USER.EMP_EMAIL, MST_USER.CNTRLG_EMP_CODE, MST_USER.CNTRLG_EMP_NAME, MST_USER.CNTRLG_EMP_EMAIL, MST_USER.ROLE_ID, MST_USER.UPDATED_BY, MST_USER.UPDATED_DATETIME, MST_ROLE.ROLE_NAME, MST_USER.ACTIVE FROM MST_USER INNER JOIN MST_ROLE ON MST_USER.ROLE_ID = MST_ROLE.ROLE_ID WHERE MST_USER.ACTIVE = 0 AND MST_USER.ROLE_ID IN (1, 3)");
                break;
            case "/GetRoleEmployee":
                mstUser = Employee.getLocRoleEmp(request.getParameter("fCode"), request.getParameter("locCode"), getServletContext().getInitParameter("cemDBPath"));
                break;
            case "/GetEmployee":
                mstUser = MstUserDAO.getCemEmployee(request.getParameter("empCode"), getServletContext().getInitParameter("cemDBPath"));
                break;
            default:
                mstUser = MstUserDAO.listAllUser("SELECT MST_USER.EMP_CODE, MST_USER.EMP_NAME, MST_USER.EMP_EMAIL, MST_USER.CNTRLG_EMP_CODE, MST_USER.CNTRLG_EMP_NAME, MST_USER.CNTRLG_EMP_EMAIL, MST_USER.ROLE_ID, MST_USER.UPDATED_BY, MST_USER.UPDATED_DATETIME, MST_ROLE.ROLE_NAME, MST_USER.ACTIVE FROM MST_USER INNER JOIN MST_ROLE ON MST_USER.ROLE_ID = MST_ROLE.ROLE_ID WHERE MST_USER.ACTIVE = 1");
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (!mstUser.isEmpty()) {
                response.getWriter().write(gson.toJson(mstUser));
            } else {
                response.getWriter().write(gson.toJson("No Employee found."));
            }
        } else {
            request.setAttribute("User", mstUser);
            request.setAttribute("Role", mstRole);
            RequestDispatcher rd = request.getRequestDispatcher("manageUser.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        MstRole mstRole = new MstRole();
        mstRole.setRoleId(request.getParameter("roleId"));
        mstRole.setRoleName(request.getParameter("roleName"));
        MstUser mstUser = new MstUser(mstRole);
        mstUser.setEmpCode(request.getParameter("empCode"));
        mstUser.setEmpName(request.getParameter("empName"));
        mstUser.setEmpEmail(request.getParameter("empEmail"));
        mstUser.setCtrlEmpCode(request.getParameter("ctrlEmpCode"));
        mstUser.setCtrlEmpName(request.getParameter("ctrlEmpName"));
        mstUser.setCtrlEmpEmail(request.getParameter("ctrlEmpEmail"));
        mstUser.setUpdatedBy(user.getsEMP_CODE());
        mstUser.setMstRole(mstRole);
        switch (request.getServletPath()) {
            case "/TseAdmin/AddUser":
                if (mstUser.getMstRole().getRoleId().equals("1") || mstUser.getMstRole().getRoleId().equals("3") || mstUser.getMstRole().getRoleId().equals("4")) {
                    mstUser.setActive("1");
                    md = MstUserDAO.addUser(mstUser);
                } else {
                    md.setModalMessage("You do not have sufficient rights to add employee code: " + mstUser.getEmpCode());
                    md.setMsgClass("text-danger");
                }
                md.setModalTitle("Add User Details !!!");
                break;
            case "/TseAdmin/ChangeRole":
                mstUser.setEmpCode(request.getParameter("changeEmpCode"));
                mstUser.getMstRole().setRoleId(request.getParameter("changeRole"));
                mstUser.getMstRole().setRoleName(request.getParameter("changeRoleName"));
                if (mstUser.getEmpCode().equals(user.getsEMP_CODE())) {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Your Role Cannot be Changed By You. No Changes were made to Your Role");
                    md.setModalTitle("Change User Role Details !!!");
                    break;
                }
                if (mstUser.getMstRole().getRoleId().equals("2") || mstUser.getMstRole().getRoleId().equals("4")) {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("You do not have sufficient rights to Change role of employee code: " + mstUser.getEmpCode());
                    md.setModalTitle("Change User Role Details !!!");
                    break;
                }
                md = MstUserDAO.changeRole(mstUser);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Employee Code: " + mstUser.getEmpCode() + " Role Changed to " + mstUser.getMstRole().getRoleName() + " Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while changing the role of Employee Code: " + mstUser.getEmpCode() + " to " + mstUser.getMstRole().getRoleName() + ". Try again Later");
                }
                md.setModalTitle("Change User Role Details !!!");
                break;
            case "/TseAdmin/RemoveUser":
                mstUser.setEmpCode(request.getParameter("empCode"));
                mstUser.setEmpName(request.getParameter("empName"));
                mstUser.getMstRole().setRoleId(request.getParameter("oldEmpRole"));
                mstUser.setActive("0");
                if (mstUser.getEmpCode().equals(user.getsEMP_CODE())) {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("You Cannot remove yourself. No Changes were made to Your Role");
                    md.setModalTitle("User Details !!!");
                    break;
                }
                if (mstUser.getMstRole().getRoleId().equals("2") || mstUser.getMstRole().getRoleId().equals("4")) {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("You do not have sufficient rights to remove employee code: " + mstUser.getEmpCode());
                    md.setModalTitle("User Details !!!");
                    break;
                }
                md = MstUserDAO.markInactive(mstUser);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Employee Code: " + mstUser.getEmpCode() + " Has been removed from ServoCMP Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while removing Employee Code: " + mstUser.getEmpCode() + " from ServoCMP. Try again Later");
                }
                md.setModalTitle("User Details !!!");
                break;
            case "/TseAdmin/ActivateUser":
                mstUser.setEmpCode(request.getParameter("inactiveEmpCode"));
                mstUser.getMstRole().setRoleId(request.getParameter("inActiveRoleId"));
                mstUser.setActive("1");
                if (mstUser.getEmpCode().equals(user.getsEMP_CODE())) {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("You Cannot Activate your own Employee Code. No Changes were made to Your Employee Code: " + mstUser.getEmpCode());
                    md.setModalTitle("Inactive User Details !!!");
                    break;
                }
                if (mstUser.getMstRole().getRoleId().equals("2") || mstUser.getMstRole().getRoleId().equals("4")) {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("You do not have sufficient rights to activate employee code: " + mstUser.getEmpCode());
                    md.setModalTitle("Inactive User Details !!!");
                    break;
                }
                md = MstUserDAO.markInactive(mstUser);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Employee Code: " + mstUser.getEmpCode() + " Has been Activated Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while activating Employee Code: " + mstUser.getEmpCode() + " from ServoCMP. Try again Later");
                }
                md.setModalTitle("Inactive User Details !!!");
        }
        request.setAttribute("messageDetails", md);
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }
}
