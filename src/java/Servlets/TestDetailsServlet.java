package Servlets;

import DAOs.MstTestDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MstTest;

public class TestDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd;
        List<MstTest> testMaster = new ArrayList();
        HttpSession session = request.getSession();
        testMaster = (List<MstTest>) session.getAttribute("testMaster");
        if (testMaster == null) {
            session.setAttribute("testMaster", MstTestDAO.gettestMaster());
            testMaster = (List<MstTest>) session.getAttribute("testMaster");
        }
        rd = request.getRequestDispatcher("testDetails.jsp");
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
