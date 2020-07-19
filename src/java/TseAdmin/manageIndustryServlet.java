package TseAdmin;

import DAOs.MstIndustryDAO;
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
import viewModel.MstIndustry;
import viewModel.StevenModels.IndustryDetails;

public class manageIndustryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstIndustry> listIndustry = null;
        switch (request.getServletPath()) {
            case "/TseAdmin/AllActiveIndustry":
                listIndustry = MstIndustryDAO.listAllIndustry("SELECT IND_ID, IND_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_INDUSTRY WHERE ACTIVE = 1");
                break;
            case "/TseAdmin/AllDeactiveIndustry":
                listIndustry = MstIndustryDAO.listAllIndustry("SELECT IND_ID, IND_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_INDUSTRY WHERE ACTIVE = 0");
                break;
            case "GetIndustry":
                break;
            default:
                listIndustry = MstIndustryDAO.listAllIndustry("SELECT IND_ID, IND_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_INDUSTRY WHERE ACTIVE = 1");
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (!listIndustry.isEmpty()) {
                response.getWriter().write(gson.toJson(listIndustry));
            } else {
                response.getWriter().write(gson.toJson("No Industry Found."));
            }
        } else {
            request.setAttribute("Industries", listIndustry);
            RequestDispatcher rd = request.getRequestDispatcher("manageIndustry.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        MstIndustry mstIndustry = new MstIndustry();
        mstIndustry.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/TseAdmin/AddIndustry":
                mstIndustry.setIndNames(request.getParameterValues("industryNameInput"));
                mstIndustry.setActive("1");
                md = MstIndustryDAO.AddIndustry(mstIndustry);
                md.setModalTitle("Add Industry Details");
                break;
            case "/TseAdmin/RemoveIndustry":
                mstIndustry.setIndId(request.getParameter("indId"));
                mstIndustry.setIndName(request.getParameter("indName"));
                mstIndustry.setActive("0");
                md = MstIndustryDAO.markInactive(mstIndustry);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Industry '" + mstIndustry.getIndName() + "' has been Removed Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Industry '" + mstIndustry.getIndName() + "' cannot be Removed");
                }
                md.setModalTitle("Industry Status");
                break;
            case "/TseAdmin/ActivateIndustry":
                mstIndustry.setIndName(request.getParameter("industryName"));
                mstIndustry.setIndId(request.getParameter("industryId"));
                mstIndustry.setActive("1");
                md = MstIndustryDAO.markInactive(mstIndustry);
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Industry '" + mstIndustry.getIndName() + "' has been Activated Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Industry '" + mstIndustry.getIndName() + "' cannot be Activated");
                }
                md.setModalTitle("Industry Status");
                break;
            case "/TseAdmin/UpdateIndustry":
                mstIndustry.setIndName(request.getParameter("industryNameEdit"));
                mstIndustry.setIndId(request.getParameter("indId"));
                md = MstIndustryDAO.UpdateIndustry(mstIndustry);
                md.setModalTitle("Industry Update Status");
                if (md.isStatus()) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Industry '" + request.getParameter("oldIndName") + "' changed to '" + mstIndustry.getIndName() + "' Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while changing Industry '" + request.getParameter("oldIndName") + "' to '" + mstIndustry.getIndName() + "'. Try again Later");
                }
                break;
        }
        if(request.getServletPath().equals("/TseAdmin/IndustryDetails")){
            ArrayList<IndustryDetails> indDets = new ArrayList<IndustryDetails>();
            String indCode = request.getParameter("ind");
            if(!indCode.equals("999999")){
                if(indCode!=""){indDets = MstIndustryDAO.getIndustryDetails(indCode);
                    new Gson().toJson(indDets, response.getWriter());
                }else{
                    String status = "In valid input";
                    new Gson().toJson(status, response.getWriter());
                }
            }else{
                indDets = MstIndustryDAO.getIndustryDetails("999999");
                new Gson().toJson(indDets, response.getWriter());
            }
        }else{
            request.setAttribute("messageDetails", md);
            doGet(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }

    
}
