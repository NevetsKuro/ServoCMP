package Servlets.Lab;

import DAOs.MstLabEquipmentDAO;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.MessageDetails;
import viewModel.MstLab;
import viewModel.MstLabEquipment;

public class MstLabEquipmentServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstLabEquipment> mLab = null;
        RequestDispatcher rd = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MstLab mstLab = MstLabEquipmentDAO.getUserLab(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/Lab/AllActiveLabEquipments":
                mLab = MstLabEquipmentDAO.getAllLabEquipments("SELECT MST_LAB_EQUIPMENT.LAB_EQUIP_ID, MST_LAB_EQUIPMENT.LAB_LOC_CODE, MST_LAB.LAB_NAME, LAB_EQUIP_NAME, MAKE_NAME, OPERATIONAL_STATUS, REMARKS, METHOD_NAME, MST_LAB_EQUIPMENT.UPDATED_BY, MST_LAB_EQUIPMENT.UPDATED_DATETIME FROM MST_LAB_EQUIPMENT INNER JOIN MST_LAB ON MST_LAB.LAB_LOC_CODE = MST_LAB_EQUIPMENT.LAB_LOC_CODE WHERE MST_LAB_EQUIPMENT.ACTIVE = 1");
                break;
            case "/Lab/AllActiveLabEquipmentsByEmpcode":
                mLab = MstLabEquipmentDAO.getEquipmentsForEmp(user.getsEMP_CODE());
                break;
            case "/Lab/AllDeactiveLabEquipments":
                mLab = MstLabEquipmentDAO.getAllLabEquipments("SELECT MST_LAB_EQUIPMENT.LAB_EQUIP_ID, MST_LAB_EQUIPMENT.LAB_LOC_CODE, MST_LAB.LAB_NAME, LAB_EQUIP_NAME, MAKE_NAME, OPERATIONAL_STATUS, REMARKS, METHOD_NAME, MST_LAB_EQUIPMENT.UPDATED_BY, MST_LAB_EQUIPMENT.UPDATED_DATETIME FROM MST_LAB_EQUIPMENT INNER JOIN MST_LAB ON MST_LAB.LAB_LOC_CODE = MST_LAB_EQUIPMENT.LAB_LOC_CODE WHERE MST_LAB_EQUIPMENT.ACTIVE = 0");
                break;
            default:
                mLab = MstLabEquipmentDAO.getEquipmentsForEmp(user.getsEMP_CODE());
//                mLab = MstLabEquipmentDAO.getAllLabEquipments("SELECT MST_LAB_EQUIPMENT.LAB_EQUIP_ID, MST_LAB_EQUIPMENT.LAB_LOC_CODE, MST_LAB.LAB_NAME, LAB_EQUIP_NAME, MAKE_NAME, OPERATIONAL_STATUS, REMARKS, METHOD_NAME, MST_LAB_EQUIPMENT.UPDATED_BY, MST_LAB_EQUIPMENT.UPDATED_DATETIME FROM MST_LAB_EQUIPMENT INNER JOIN MST_LAB ON MST_LAB.LAB_LOC_CODE = MST_LAB_EQUIPMENT.LAB_LOC_CODE WHERE MST_LAB_EQUIPMENT.ACTIVE = 1");
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(mLab));
        } else {
            request.setAttribute("labEquip", mLab);
            request.setAttribute("labDetails", mstLab);
            rd = request.getRequestDispatcher("labEquipment.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MessageDetails md = new MessageDetails();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MstLabEquipment mLab = new MstLabEquipment();
        mLab.setLabCode(request.getParameter("labLocCode"));
        mLab.setLabEquipName(request.getParameter("labEquipName"));
        mLab.setMakeName(request.getParameter("labMakeName"));
        mLab.setMethodName(request.getParameter("labMethodName"));
        mLab.setOperationalStatus(request.getParameter("operationalStatus"));
        mLab.setRemarks(request.getParameter("remarks"));
        mLab.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/Lab/AddLabEquipment":
                mLab.setActive("1");
                md = MstLabEquipmentDAO.addLabEquipment(mLab);
                md.setModalTitle("Add Lab Equipment Details !!!");
                break;
            case "/Lab/RemoveLabEquipment":
                mLab.setLabEquipId(request.getParameter("labEquipId"));
                mLab.setLabEquipName(request.getParameter("labEquipName"));
                mLab.setActive("0");
                md = MstLabEquipmentDAO.markInactive(mLab);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Equipment '" + mLab.getLabEquipName() + "' has been Removed Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Equipment '" + mLab.getLabEquipName() + "' cannot be Removed");
                }
                md.setModalTitle("Lab Equipment Status");
                break;
            case "/Lab/ActivateLabEquipment":
                mLab.setLabEquipName(request.getParameter("labEquipNameInput"));
                mLab.setLabEquipId(request.getParameter("labEquipNameModal"));
                mLab.setActive("1");
                md = MstLabEquipmentDAO.markInactive(mLab);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Equipment '" + mLab.getLabEquipName() + "' has been Activated Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Equipment '" + mLab.getLabEquipName() + "' cannot be Activated");
                }
                md.setModalTitle("Equipment Status");
                break;
            case "/Lab/UpdateLabEquipment":
                mLab.setLabEquipName(request.getParameter("labEquipName"));
                mLab.setLabEquipId(request.getParameter("labEquipId"));
                md = MstLabEquipmentDAO.UpdateLabEquipment(mLab);
                md.setModalTitle("Equipment Update Status");
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Equipment '" + request.getParameter("oldEquipName") + "' changed to '" + mLab.getLabEquipName() + "' Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while changing Equipment '" + request.getParameter("oldEquipName") + "' to '" + mLab.getLabEquipName() + "'. Try again Later");
                }
                break;
        }
        request.setAttribute("messageDetails", md);
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }

}
