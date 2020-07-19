/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ajax;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.SharedOperations;
import operations.TSEoperations;
import viewModel.SampleDetails;

public class getSampleCreatedAjax extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doPost(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String productId = request.getParameter("pid");
        String deptId = request.getParameter("did");
        String custId = request.getParameter("cid");
        String appId = request.getParameter("aid");
        String sampleType = request.getParameter("sampleType");
        if ("0".equals(request.getParameter("status"))
                || "1".equals(request.getParameter("status"))
                || "2".equals(request.getParameter("status"))
                || "3".equals(request.getParameter("status"))
                || "4".equals(request.getParameter("status"))) {
            HttpSession session = request.getSession();
            String rowLimit = (String) session.getAttribute("dataRowRetrieveLimit");
            String sLabLocCode = "";
            List<SampleDetails> listInProcessSmplDetails = null;
            globals.User user = new globals.User();
            user = (globals.User) session.getAttribute("sUser");
            Gson gson = new Gson();
//            System.out.println("Prod ID "+productId);
//            System.out.println("Customer ID "+custId);
//            System.out.println("Dept ID "+deptId);
//            System.out.println("application ID "+appId);
            
            switch (request.getParameter("status")) {
                case "0":
                    List ml = SharedOperations.getLabdetails();
                    request.setAttribute("labmaster", ml);
                    request.setAttribute("labLocCode", sLabLocCode);
                    //rd = request.getRequestDispatcher("getAllIntransitSample.jsp");
                    gson.toJson(ml,response.getWriter());
                    break;
                case "1":
                    listInProcessSmplDetails = TSEoperations.getintransitsampleDetails(user.getsEMP_CODE(), request.getParameter("status"), productId, deptId, custId, appId, sampleType, rowLimit);
                    gson.toJson(listInProcessSmplDetails,response.getWriter());
                    break;
                case "2":
                    listInProcessSmplDetails = TSEoperations.getintransitsampleDetails(user.getsEMP_CODE(), request.getParameter("status"), productId, deptId, custId, appId, sampleType, rowLimit);
                    gson.toJson(listInProcessSmplDetails,response.getWriter());
                    break;
                case "3":
                    listInProcessSmplDetails = TSEoperations.getintransitsampleLabDetails(user.getsEMP_CODE(), request.getParameter("status"), productId, deptId, custId, appId, sampleType, rowLimit);
                    gson.toJson(listInProcessSmplDetails,response.getWriter());
                    break;
                case "4":
                    listInProcessSmplDetails = TSEoperations.getintransitsampleLabDetails(user.getsEMP_CODE(), request.getParameter("status"), productId, deptId, custId, appId, sampleType, rowLimit);
                    gson.toJson(listInProcessSmplDetails,response.getWriter());
                    break;
                default:
                    gson.toJson("No Such status found.",response.getWriter());
            }
        }
    }
}
