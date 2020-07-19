/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Lab;

import DAOs.MstLabDAO;
import DAOs.MstTestDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MstTest;

/**
 *
 * @author NIT_steven
 */
public class LabTestStatusMasterServlet extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        String labCode = MstLabDAO.listLabByEmp(user.getsEMP_CODE());
        List<MstTest> testList = MstTestDAO.getTestIDByLab(labCode);
        request.setAttribute("testList", testList);
        request.getRequestDispatcher("/Lab/LabTestStatusMaster.jsp").forward(request, response);;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        String testId = request.getParameter("testId");
        String labCode = MstLabDAO.listLabByEmp(user.getsEMP_CODE());
        String Status = MstLabDAO.toggleActiveForTest(testId, labCode);
        new Gson().toJson(Status,response.getWriter());
        
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
