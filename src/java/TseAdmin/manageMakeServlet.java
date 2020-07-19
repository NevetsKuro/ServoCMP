package TseAdmin;

import DAOs.MstMakeDAO;
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
import viewModel.MstMake;

public class manageMakeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstMake> listMake = new ArrayList<>();
        switch (request.getServletPath()) {
            case "/TseAdmin/AllDeactiveMake":
                listMake = MstMakeDAO.listAllMake("SELECT MAKE_ID, MAKE_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_MAKE WHERE ACTIVE = 0");
                break;
            case "GetMake":
                break;
            case "/TseAdmin/AllActiveMake":
            default:
                listMake = MstMakeDAO.listAllMake("SELECT MAKE_ID, MAKE_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_MAKE WHERE ACTIVE = 1");
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (!listMake.isEmpty()) {
                response.getWriter().write(gson.toJson(listMake));
            } else {
                response.getWriter().write(gson.toJson("No Make Found."));
            }
        } else {
            request.setAttribute("Makes", listMake);
            RequestDispatcher rd = request.getRequestDispatcher("manageMake.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        MstMake mstMake = new MstMake();
        mstMake.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/TseAdmin/AddMake":
                mstMake.setMakeNames(request.getParameterValues("makeNameInput"));
                mstMake.setActive("1");
                md = MstMakeDAO.AddMake(mstMake);
                md.setModalTitle("Add Make Details");
                break;
            case "/TseAdmin/RemoveMake":
                mstMake.setMakeId(request.getParameter("makeId"));
                mstMake.setMakeName(request.getParameter("makeName"));
                mstMake.setActive("0");
                md = MstMakeDAO.markInactive(mstMake);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Make '" + mstMake.getMakeName() + "' has been Removed Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Make '" + mstMake.getMakeName() + "' cannot be Removed");
                }
                md.setModalTitle("Make Status");
                break;
            case "/TseAdmin/ActivateMake":
                mstMake.setMakeName(request.getParameter("makeName"));
                mstMake.setMakeId(request.getParameter("ActMakeId"));
                mstMake.setActive("1");
                md = MstMakeDAO.markInactive(mstMake);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Make '" + mstMake.getMakeName() + "' has been Activated Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Make '" + mstMake.getMakeName() + "' cannot be Activated");
                }
                md.setModalTitle("Make Status");
                break;
            case "/TseAdmin/UpdateMake":
                mstMake.setMakeName(request.getParameter("makeNameEdit"));
                mstMake.setMakeId(request.getParameter("makeId"));
                md = MstMakeDAO.UpdateMake(mstMake);
                md.setModalTitle("Make Update Status");
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Make '" + request.getParameter("oldMakeName") + "' changed to '" + mstMake.getMakeName() + "' Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while changing Make '" + request.getParameter("oldMakeName") + "' to '" + mstMake.getMakeName() + "'. Try again Later");
                }
                break;
            default:
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
