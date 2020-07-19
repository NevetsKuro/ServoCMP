/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Tse;

import DAOs.MstLabDAO;
import DAOs.MstTestDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import viewModel.MstLab;
import viewModel.MstTest;

/**
 *
 * @author NIT_steven
 */
public class LabTestSpecsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstLab> mstlab = MstLabDAO.listLab();
        request.setAttribute("mLab", mstlab);
        RequestDispatcher rd = request.getRequestDispatcher("labTestSpecs.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/Tse/LabTestSpecsServlet":
                String labCode = request.getParameter("labCode");
                List<MstTest> testList = MstTestDAO.listOfAllLabTestsByLab(labCode);
                new Gson().toJson(testList, response.getWriter());
                break;
            case "/Tse/LabTestSpecsServlet2":
                String labCode1 = request.getParameter("csl_labCode");
                String labCode2 = request.getParameter("rnd_labCode");
                switch (request.getParameter("labType")) {
                    case "1":
                        List<MstTest> testList1 = MstTestDAO.listOfAllLabTestsByLab(labCode1);
                        new Gson().toJson(testList1, response.getWriter());
                        break;
                    case "2":
                        List<MstTest> testList2 = MstTestDAO.listOfAllLabTestsByLab(labCode2);
                        new Gson().toJson(testList2, response.getWriter());
                        break;
                    case "3":
                        List<MstTest> testList3 = MstTestDAO.listOfAllLabTestsByLab(labCode1);
                        List<MstTest> testList4 = MstTestDAO.listOfAllLabTestsByLab(labCode2);
                        List<MstTest>[] arrayOfList = new List[2];
                        arrayOfList[0] = testList3;
                        arrayOfList[1] = testList4;
                        new Gson().toJson(arrayOfList, response.getWriter());
                        break;
                }
            default:
                break;
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
