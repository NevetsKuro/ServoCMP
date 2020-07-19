/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TseAdmin;

import DAOs.MstLabDAO;
import DAOs.MstUserDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import viewModel.MstLab;
import viewModel.MstUser;

public class TseLabMappingServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TseLabMappingServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TseLabMappingServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstUser> mUser = MstUserDAO.listAllUser("SELECT MST_USER.EMP_CODE, MST_USER.EMP_NAME, MST_USER.EMP_EMAIL, MST_USER.CNTRLG_EMP_CODE, MST_USER.CNTRLG_EMP_NAME, MST_USER.CNTRLG_EMP_EMAIL, MST_USER.ROLE_ID, MST_USER.UPDATED_BY, MST_USER.UPDATED_DATETIME, MST_ROLE.ROLE_NAME, MST_USER.ACTIVE FROM MST_USER INNER JOIN MST_ROLE ON MST_USER.ROLE_ID = MST_ROLE.ROLE_ID WHERE MST_USER.ACTIVE = 1");
        List<MstLab> mLab = MstLabDAO.listLabByLabType("CSL");

        request.setAttribute("mLab", mLab);
        request.setAttribute("mUser", mUser);
        RequestDispatcher rd = request.getRequestDispatcher("TseLabMapping.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
