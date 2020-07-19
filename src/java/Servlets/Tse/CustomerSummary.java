/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servlets.Tse;

import DAOs.MstCustomerDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MstCustomer;

public class CustomerSummary extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doPost(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        List<MstCustomer> cust = new ArrayList<MstCustomer>();
        Gson gson = new Gson();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        String empcode = user.getsEMP_CODE();
        cust = MstCustomerDAO.listTseCustomer(empcode);
        request.setAttribute("cust", cust);
        request.getRequestDispatcher("customerSummary.jsp").forward(request, response);
    }
}
