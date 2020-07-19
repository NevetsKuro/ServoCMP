package Servlets.Tse;

import DAOs.MstMakeDAO;
import DAOs.MstTestDAO;
import globals.User;
import operations.GetMaster;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.SharedOperations;
import operations.TSEoperations;
//To System Details

public class ManageSampleInfoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("Industries", GetMaster.getIndustry());
        request.setAttribute("Customers", GetMaster.getCustomers());
        request.setAttribute("Applications", GetMaster.getApplications());
        request.setAttribute("Departments", GetMaster.getDepartments());
        request.setAttribute("Products", GetMaster.getProducts());
        request.setAttribute("custSampleInfo", SharedOperations.getCustSample());
        request.setAttribute("testMaster", MstTestDAO.gettestMaster());
        request.setAttribute("make", MstMakeDAO.listAllMake());
        RequestDispatcher rd = request.getRequestDispatcher("manageSampleInfo.jsp");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        session.setAttribute("notidetails", TSEoperations.getindustryTse(user.getsEMP_CODE(), session.getAttribute("NotifyDaysLimit").toString(), request));
        rd.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
