package Master;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.GetMaster;

public class GetAvailableTankServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        List tank = GetMaster.getAvailableTank(request.getParameter("industry"),
                request.getParameter("customer"),
                request.getParameter("department"),
                request.getParameter("application"),
                request.getParameter("equipment"));
        if(!tank.isEmpty()){
            response.getWriter().write(gson.toJson(tank));
        } else{
            response.getWriter().write(gson.toJson("There no Tank(s) Available."));
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
