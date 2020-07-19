package Master;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.GetMaster;

public class GetDepartmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        List departments = GetMaster.getDepartment(request.getParameter("customer"));
        if (!departments.isEmpty()) {
            List equipments = GetMaster.getEquipment(request.getParameter("customer"));
            List deptEquip = new ArrayList();
            deptEquip.add(departments);
            deptEquip.add(equipments);
            response.getWriter().write(gson.toJson(deptEquip));
        } else if (departments.isEmpty()) {
            response.getWriter().write(gson.toJson("There are no Departments and Equipments. Kindly Add them."));
        }
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
