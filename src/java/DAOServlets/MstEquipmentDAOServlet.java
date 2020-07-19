package DAOServlets;

import DAOs.MstEquipmentDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;
import viewModel.MstEquipment;

public class MstEquipmentDAOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        List<MstEquipment> result = new ArrayList<>();
        switch (request.getServletPath()) {
            case "/CustomerAllEquipments":
                listAllEquipments(request.getParameter("customer"), request, response);
                break;
            case "/GetEquipments":
                if (!"".equals(request.getParameter("customer"))) {
                    if (!"".equals(request.getParameter("make"))) {
                        if (!"".equals(request.getParameter("equipment"))) {
                            result = MstEquipmentDAO.listAllEquipments(request.getParameter("customer"), request.getParameter("make"), request.getParameter("equipment"));
                            if (!result.isEmpty()) {
                                response.getWriter().write(gson.toJson(result));
                            } else {
                                response.getWriter().write(gson.toJson("No Data Found"));
                            }
                        } else {
                            result = MstEquipmentDAO.listAllEquipments(request.getParameter("customer"), request.getParameter("make"));
                            if (!result.isEmpty()) {
                                response.getWriter().write(gson.toJson(result));
                            } else {
                                response.getWriter().write(gson.toJson("No Data Found"));
                            }
                        }

                    } else {
                        result = MstEquipmentDAO.listAllEquipments(request.getParameter("customer"));
                        if (!result.isEmpty()) {
                            response.getWriter().write(gson.toJson(result));
                        } else {
                            response.getWriter().write(gson.toJson("No Data Found"));
                        }
                    }
                }
                break;
            case "/Tse/GetCustomerEquipment":
            case "/TseAdmin/GetCustomerEquipment":
                if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
                    result = MstEquipmentDAO.listAllEquipments(request.getParameter("Customer"));
                    if (!result.isEmpty()) {
                        response.getWriter().write(gson.toJson(result));
                    } else {
                        response.getWriter().write(gson.toJson("No Equipment with selected Customer. Kindly Add New Equipment for "));
                    }
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = null;
        RequestDispatcher rd;
        MstEquipment mstEquipment = new MstEquipment();
        mstEquipment.setEquipmentId(request.getParameter("equipmentId"));
        mstEquipment.setEquipmentName(request.getParameter("equipmentName"));
        mstEquipment.getMstmake().setMakeId(request.getParameter("equipmentMake"));
        mstEquipment.setUpdatedBy(user.getsEMP_CODE());
        mstEquipment.setCustId(request.getParameter("equipmentCustomer"));
        switch (request.getServletPath()) {
            case "/AddEquipment":
                md = MstEquipmentDAO.insertEquipment(mstEquipment);
                md.setModalTitle("Add Equipment Details");
                break;
            case "/UpdateEquipment":
                md = MstEquipmentDAO.updateEquipment(mstEquipment);
                md.setModalTitle("Equipment Update Details");
                break;
        }
        request.setAttribute("messageDetails", md);
        rd = request.getRequestDispatcher("Tse/ManageSample");
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void listAllEquipments(String customer, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<MstEquipment> mstEquipment = MstEquipmentDAO.listAllEquipments(customer);
        if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            Gson gson = new Gson();
            if (!mstEquipment.isEmpty()) {
                response.getWriter().write(gson.toJson(mstEquipment));
            } else {
                response.getWriter().write(gson.toJson("No Equipment(s) Found for "));
            }
        }
    }
}
