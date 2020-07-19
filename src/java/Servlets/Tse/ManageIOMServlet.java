/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Tse;

import Exceptions.MyLogger;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.SharedOperations;
import operations.TSEoperations;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import viewModel.MessageDetails;
import viewModel.MstLab;
import com.google.gson.Gson;

/**
 *
 * @author 00507469
 */
public class ManageIOMServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        globals.User user = new globals.User();

        HttpSession session = request.getSession();
        user = (globals.User) session.getAttribute("sUser");
        RequestDispatcher rd = request.getRequestDispatcher("errorPages/error-404.html");
        String empCode = "";

        switch (request.getServletPath()) {
            case "/Tse/ManageIOM":
                String arrSampleIDs[] = null,
                 strSampleIDs = "";
                MstLab mstLab = new MstLab();
                try {
                    Gson gson = new Gson();

                    JSONParser parser = new JSONParser();

                    String sIOMRefNo = request.getParameter("nameIOMrefNO").toUpperCase();
                    String sLabLocCode = request.getParameter("nameLabLocCode");
                    String arrSampleIDs1 = request.getParameter("nameIOMSampleIds");
                    JSONArray array = (JSONArray) parser.parse(request.getParameter("nameIOMSampleIds"));
                    arrSampleIDs = new String[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        arrSampleIDs[i] = (String) array.get(i);
                    }

                    MessageDetails md = TSEoperations.createIOM(sIOMRefNo, sLabLocCode, arrSampleIDs, user.getsEMP_CODE());

                    //  request.setAttribute("messageDetails", md);
                    //  rd = request.getRequestDispatcher("GetCreatedSamples?status=0&labName="+sLabLocCode);
                    //  rd.forward(request, response);
                    response.getWriter().write(gson.toJson(md));

                } catch (Exception ex) {
                    MyLogger.logIt(ex, "ManageIOM servlet");
                }
                break;
            case "/Tse/GetIOMSummary":
            case "/Lab/GetIOMSummary":
                List ml = null;
                if (request.getServletPath().equals("/Tse/GetIOMSummary")) {
                    ml = SharedOperations.getLabdetails();
                }
                if (request.getServletPath().equals("/Lab/GetIOMSummary")) {
                    ml = SharedOperations.getLabdetailsEmpCodeWise(user.getsEMP_CODE());
                }

                request.setAttribute("labmaster", ml);
                rd = request.getRequestDispatcher("viewIOMSummary.jsp");
                rd.forward(request, response);
                break;
            /*
            case "/Lab/GetIOMSummary":
                    List ml1 = SharedOperations.getLabdetails();
                    request.setAttribute("labmaster", ml1);
                    rd = request.getRequestDispatcher("viewIOMSummary.jsp");
                    rd.forward(request, response);
                    break;    
             */
            case "/Tse/GetIOMSummaryDetails":
            case "/Lab/GetIOMSummaryDetails":
                List listIOMs = null;
                List ml2 = null;
                String sLabLocCode = request.getParameter("labName");
                if (request.getServletPath().equals("/Tse/GetIOMSummaryDetails")) {
                    listIOMs = TSEoperations.getIOMSummaryData(sLabLocCode, user.getsEMP_CODE());
                    ml2 = SharedOperations.getLabdetails();
                }
                if (request.getServletPath().equals("/Lab/GetIOMSummaryDetails")) {
                    
                    listIOMs = TSEoperations.getIOMSummaryData(sLabLocCode);
                    ml2 = SharedOperations.getLabdetailsEmpCodeWise(user.getsEMP_CODE());
                }
                request.setAttribute("lstIOMs", listIOMs);
                request.setAttribute("labmaster", ml2);
                request.setAttribute("labLocCode", sLabLocCode);
                rd = request.getRequestDispatcher("viewIOMSummary.jsp");
                rd.forward(request, response);
                break;
            /*
        case "/Lab/GetIOMSummaryDetails":
                    List listIOMs1=null;
                    String sLabLocCode1=request.getParameter("labName");
                    listIOMs1 = TSEoperations.getIOMSummaryData(sLabLocCode1,user.getsEMP_CODE());
                    request.setAttribute("lstIOMs", listIOMs1);
                    List ml3 = SharedOperations.getLabdetails();
                    request.setAttribute("labmaster", ml3);
                    request.setAttribute("labLocCode", sLabLocCode1);
                    rd = request.getRequestDispatcher("viewIOMSummary.jsp");
                    rd.forward(request, response);
                    break;
             */
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
