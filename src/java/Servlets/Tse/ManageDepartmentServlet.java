package Servlets.Tse;

import DAOs.MstDepartmentDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;
import viewModel.MstDepartment;

public class ManageDepartmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = null;
        MstDepartment mstDept = new MstDepartment();
        mstDept.setDepartmentNames(request.getParameterValues("deptName"));
        mstDept.setHodNames(request.getParameterValues("hodName"));
        mstDept.setHodEmails(request.getParameterValues("hodEmail"));
        mstDept.setHodContacts(request.getParameterValues("hodContact"));
        mstDept.setUpdatedBy(user.getsEMP_CODE());
        mstDept.getMstCustomer().setCustomerId(request.getParameter("deptCustomer"));
        mstDept.getMstCustomer().setCustomerName(request.getParameter("deptCustomerName"));
        switch (request.getServletPath()) {
            case "/Tse/AddDepartments":
                md = MstDepartmentDAO.AddDepartment(mstDept);
                md.setModalTitle("Add Department(s) details");
                break;
        }
        if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(md));
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
