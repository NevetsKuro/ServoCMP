package LabAdmin;

import DAOs.MstLabDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import viewModel.MstLab;

public class manageLabServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstLab> listLab = new ArrayList<MstLab>();
        switch (request.getServletPath()) {
            case "/TseAdmin/AllActiveLab":
                listLab = MstLabDAO.listLab();
                break;
            case "/TseAdmin/AllDeactiveLab":
                listLab = MstLabDAO.listLab();
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (!listLab.isEmpty()) {
                response.getWriter().write(gson.toJson(listLab));
            } else {
                response.getWriter().write(gson.toJson("No Lab Found."));
            }
        } else {
            request.setAttribute("Industries", listLab);
            RequestDispatcher rd = request.getRequestDispatcher("manageLab.jsp");
            rd.forward(request, response);
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

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }

}
