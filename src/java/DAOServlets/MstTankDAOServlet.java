package DAOServlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MstTankDAOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        Gson gson = new Gson();
//        List<MstTank> result;
//        switch (request.getServletPath()) {
//            case "/GetTank":
//                if (!"".equals(request.getParameter("industry"))
//                        && !"".equals(request.getParameter("customer"))
//                        && !"".equals(request.getParameter("department"))
//                        && !"".equals(request.getParameter("application"))
//                        && !"".equals(request.getParameter("equipment"))
//                        && !"".equals(request.getParameter("product"))) {
//                    result = MstTankDAO.listAllTank(request.getParameter("industry"),
//                            request.getParameter("customer"),
//                            request.getParameter("department"),
//                            request.getParameter("application"),
//                            request.getParameter("equipment"),
//                            request.getParameter("product"));
//                    if (!result.isEmpty()) {
//                        response.getWriter().write(gson.toJson(result));
//                    } else {
//                        response.getWriter().write(gson.toJson("No Tank Found"));
//                    }
//                }
//                break;
//        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
