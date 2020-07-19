package DAOServlets;

import DAOs.MstMakeDAO;
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
import viewModel.MstMake;

public class MstMakeDAOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/GetMake":
                List<MstMake> mstMake = MstMakeDAO.listAllMake();
                if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
                    Gson gson = new Gson();
                    if (!mstMake.isEmpty()) {
                        response.getWriter().write(gson.toJson(mstMake));
                    } else {
                        response.getWriter().write(gson.toJson("No Make Found for "));
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
        RequestDispatcher rd = null;
        MstMake mstMake = new MstMake();
        mstMake.setMakeId(request.getParameter("makeId"));
        mstMake.setMakeName(request.getParameter("makeName"));
        mstMake.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/AddMake":
                md = MstMakeDAO.InsertMake(mstMake);
                md.setModalTitle("Add Make Details");
                break;
            case "/UpdateMake":
                md = MstMakeDAO.UpdateMake(mstMake);
                md.setModalTitle("Update Make Details");
                break;
        }
        request.setAttribute("messageDetails", md);
        rd = request.getRequestDispatcher("Tse/ManageSample");
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
