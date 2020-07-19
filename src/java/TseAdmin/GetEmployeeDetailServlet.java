package TseAdmin;

import CEM.Employee;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import viewModel.MstUser;

public class GetEmployeeDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        MstUser mUser = Employee.getEmployeeDetails(request.getParameter("empCode"), getServletContext().getInitParameter("cemDBPath"));
        response.getWriter().write(gson.toJson(mUser));
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