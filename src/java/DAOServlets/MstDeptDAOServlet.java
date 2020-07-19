package DAOServlets;

import DAOs.MstDepartmentDAO;
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
import viewModel.MstDepartment;

public class MstDeptDAOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        switch (request.getServletPath()) {
            case "/GetDepartmentDetails":
                getDepartment(request.getParameter("customer"), request.getParameter("department"), request, response);
                break;
            case "/GetDepartment":
                getAllDepartment(request.getParameter("customer"), request.getParameter("department"), request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = null;
        RequestDispatcher rd = null;
        MstDepartment mstDept = new MstDepartment();
        mstDept.setDepartmentId(request.getParameter("deptId"));
        mstDept.setDepartmentName(request.getParameter("deptName"));
        mstDept.setHodName(request.getParameter("deptHOD"));
        mstDept.setHodEmail(request.getParameter("deptHODEmail"));
        mstDept.setHodContact(request.getParameter("deptHODContact"));
        mstDept.setUpdatedBy(user.getsEMP_CODE());
        mstDept.getMstCustomer().setCustomerId(request.getParameter("deptCustomer"));
        switch (request.getServletPath()) {
            case "/AddDepartment":
                md = MstDepartmentDAO.AddDepartment(mstDept);
                md.setModalTitle("Add Department Details.");
                break;
            case "/UpdateDepartment":
                md = MstDepartmentDAO.UpdateDepartment(mstDept);
                md.setModalTitle("Update Department Details.");
                break;
        }
        request.setAttribute("messageDetails", md);
        rd = request.getRequestDispatcher("Tse/ManageSample");
        rd.forward(request, response);
    }

    private void getDepartment(String CustId, String DeptId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        MstDepartment mstDept = MstDepartmentDAO.getDepartment(CustId, DeptId);
        if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(mstDept));
        }
    }

    private void getAllDepartment(String custId, String deptId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        List<MstDepartment> result = new ArrayList<>();
        if (!"".equals(custId)) {
            if (!"".equals(deptId)) {
                result = MstDepartmentDAO.listAllDepartment(custId, deptId);
                if (!result.isEmpty()) {
                    response.getWriter().write(gson.toJson(result));
                } else {
                    response.getWriter().write(gson.toJson("No Data Found"));
                }
            } else {
                result = MstDepartmentDAO.listAllDepartment(custId);
                if (!result.isEmpty()) {
                    response.getWriter().write(gson.toJson(result));
                } else {
                    response.getWriter().write(gson.toJson(result));
                }
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
