package Servlets.Tse;

import DAOs.MstEquipmentDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;
import viewModel.MstEquipment;

public class ManageEquipmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = null;
        MstEquipment mstEquip = new MstEquipment();
        mstEquip.setEquipmentNames(request.getParameterValues("equipName"));
        mstEquip.setEquipmentMakes(request.getParameterValues("equipMake"));
        mstEquip.setOtherRemarks(request.getParameterValues("otherRemarks"));
        mstEquip.setCustId(request.getParameter("equipCustomer"));
        mstEquip.setActive("1");
        mstEquip.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/Tse/AddEquipments":
                md = MstEquipmentDAO.AddEquipment(mstEquip);
                md.setModalTitle("Add Equipment(s) details");
                break;
        }
        if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(md));
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}