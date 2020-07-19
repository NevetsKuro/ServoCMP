package TseAdmin;

import DAOs.MstCustomerDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import viewModel.MstCustomer;

public class TseCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstCustomer> mCust =  new ArrayList<>();
        RequestDispatcher rd = null;
        String empcode = request.getParameter("empCode");
        switch (request.getServletPath()) {
            case "/TseAdmin/TseCustomer":
                if (empcode == null || empcode == "") {
                    mCust = MstCustomerDAO.listTseCustomer();
                } else {
                    mCust = MstCustomerDAO.listTseCustomer(request.getParameter("empCode"));
                }
                break;
                default:
                    break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            if (!mCust.isEmpty()) {
                response.getWriter().write(gson.toJson(mCust));
            } else {
                response.getWriter().write(gson.toJson("No Test Found."));
            }
        } else {
            request.setAttribute("mCust", mCust);
            rd = request.getRequestDispatcher("TseAccess.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        throw new UnsupportedOperationException();
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
