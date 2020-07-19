package TseAdmin;

import DAOs.MstApplicationDAO;
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
import viewModel.MstApplication;

public class manageApplicationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstApplication> listApplication = new ArrayList<>();
        switch (request.getServletPath()) {
            case "/TseAdmin/AllDeactiveApplication":
                listApplication = MstApplicationDAO.listAllApplication("SELECT APPL_ID, APPL_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_APPLICATION WHERE ACTIVE = 0");
                break;
            case "GetApplication":
                break;
            case "/TseAdmin/AllActiveApplication":
            default:
                listApplication = MstApplicationDAO.listAllApplication("SELECT APPL_ID, APPL_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_APPLICATION WHERE ACTIVE = 1");
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (!listApplication.isEmpty()) {
                response.getWriter().write(gson.toJson(listApplication));
            } else {
                response.getWriter().write(gson.toJson("No Application Found."));
            }
        } else {
            request.setAttribute("Applications", listApplication);
            RequestDispatcher rd = request.getRequestDispatcher("manageApplication.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        MstApplication mstApplication = new MstApplication();
        mstApplication.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/TseAdmin/AddApplication":
                mstApplication.setAppNames(request.getParameterValues("applicationNameInput"));
                mstApplication.setActive("1");
                md = MstApplicationDAO.AddApplication(mstApplication);
                md.setModalTitle("Add Application Details");
                break;
            case "/TseAdmin/RemoveApplication":
                mstApplication.setAppId(request.getParameter("appId"));
                mstApplication.setAppName(request.getParameter("appName"));
                mstApplication.setActive("0");
                md = MstApplicationDAO.markInactive(mstApplication);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Application '" + mstApplication.getAppName() + "' has been Removed Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Application '" + mstApplication.getAppName() + "' cannot be Removed");
                }
                md.setModalTitle("Application Status");
                break;
            case "/TseAdmin/ActivateApplication":
                mstApplication.setAppName(request.getParameter("applicationName"));
                mstApplication.setAppId(request.getParameter("applicationId"));
                mstApplication.setActive("1");
                md = MstApplicationDAO.markInactive(mstApplication);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Application '" + mstApplication.getAppName() + "' has been Activated Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Application '" + mstApplication.getAppName() + "' cannot be Activated");
                }
                md.setModalTitle("Application Status");
                break;
            case "/TseAdmin/UpdateApplication":
                mstApplication.setAppName(request.getParameter("applicationNameEdit"));
                mstApplication.setAppId(request.getParameter("appId"));
                md = MstApplicationDAO.UpdateApplication(mstApplication);
                md.setModalTitle("Application Update Status");
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Application '" + request.getParameter("oldAppName") + "' changed to '" + mstApplication.getAppName() + "' Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while changing Application '" + request.getParameter("oldAppName") + "' to '" + mstApplication.getAppName() + "'. Try agin Later");
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
