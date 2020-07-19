/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TseAdmin;

import DAOs.MstEquipmentDAO;
import DAOs.MstMakeDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;
import viewModel.MstApplication;
import viewModel.MstEquipment;
import viewModel.MstMake;

/**
 *
 * @author NIT_steven
 */
public class manageEquipmentServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet manageEquipment</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet manageEquipment at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstEquipment> listEquipment = new ArrayList<>();
        List<MstMake> listMake = new ArrayList<>();
        switch (request.getServletPath()) {
            case "/TseAdmin/AllDeactiveEquipment":
                listEquipment = MstEquipmentDAO.listAllDeactivatedEquipments();
                break;
            case "/TseAdmin/AllActiveEquipment":
                listEquipment = MstEquipmentDAO.listAllActiveEquipments();
                listMake = MstMakeDAO.listAllMake();
            default:
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (!listEquipment.isEmpty()) {
                response.getWriter().write(gson.toJson(listEquipment));
            } else {
                response.getWriter().write(gson.toJson("No Application Found."));
            }
        } else {
            request.setAttribute("Equipment", listEquipment);
            request.setAttribute("Make", listMake);
            RequestDispatcher rd = request.getRequestDispatcher("manageEquipment.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        MstEquipment mstEquipment = new MstEquipment();
        mstEquipment.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/TseAdmin/AddEquipment":
//                MstEquipmentDAO.insertEquipment(mstEquipment)
                new Gson().toJson(md, response.getWriter());
                break;
            case "/TseAdmin/UpdateEquipment":
                mstEquipment.setEquipmentId(request.getParameter("equipId"));
                mstEquipment.setEquipmentName(request.getParameter("equipName"));
                mstEquipment.setUpdatedBy(user.getsEMP_CODE());
                mstEquipment.getMstmake().setMakeId(request.getParameter("makeId"));
                md = MstEquipmentDAO.updateEquipment2(mstEquipment);
                new Gson().toJson(md, response.getWriter());
                break;
            case "/TseAdmin/DeleteEquipment":
                
                new Gson().toJson(md, response.getWriter());
                break;
            default:
                break;
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }
}
