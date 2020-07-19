package Servlets.Tse;

import DAOs.MstApplicationDAO;
import DAOs.MstCustomerDAO;
import DAOs.MstDepartmentDAO;
import DAOs.MstProductDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.SharedOperations;
import operations.TSEoperations;
import viewModel.MstApplication;
import viewModel.MstCustomer;
import viewModel.MstDepartment;
import viewModel.MstProduct;

public class GetCreatedSampleServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("errorPages/error-404.html");
        if ("0".equals(request.getParameter("status"))
                || "1".equals(request.getParameter("status"))
                || "2".equals(request.getParameter("status"))
                || "3".equals(request.getParameter("status"))
                || "4".equals(request.getParameter("status"))) {
            HttpSession session = request.getSession();
            String sLabLocCode = "";
            List listInProcessSmplDetails = null;
            globals.User user = new globals.User();
            user = (globals.User) session.getAttribute("sUser");
            String rowLimit = (String) session.getAttribute("dataRowRetrieveLimit");
            String sampleType = (String) request.getParameter("sampleType");
            request.setAttribute("sampleType", sampleType);

            Gson gson = new Gson();
            if ("0".equals(request.getParameter("status"))) {
                sLabLocCode = request.getParameter("labName");
                listInProcessSmplDetails = TSEoperations.getAllCreatedSamplesLabWiseSummary(user.getsEMP_CODE(), request.getParameter("status"), sLabLocCode, rowLimit);
                session.setAttribute("InProcessTseSampleDetails", listInProcessSmplDetails);
            } else {
                
                List<MstProduct> mPro = MstProductDAO.listAllProducts("SELECT PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_PRODUCT WHERE ACTIVE = 1");
                listInProcessSmplDetails = TSEoperations.getintransitsampleDetails(user.getsEMP_CODE(), request.getParameter("status"), rowLimit);
                List<MstCustomer> mCus = MstCustomerDAO.listTseCustomer(user.getsEMP_CODE()) ;//MstCustomerDAO.listAllCustomer();
                List<MstDepartment> mDept = MstDepartmentDAO.listAllDepartment();
                String query1 = "SELECT APPL_ID, APPL_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME FROM MST_APPLICATION WHERE ACTIVE = 1";
                List<MstApplication> mApp = MstApplicationDAO.listAllApplication(query1);

                request.setAttribute("mPro", mPro);
                request.setAttribute("mCus", mCus);
                request.setAttribute("mDept", mDept);
                request.setAttribute("mApp", mApp);
                session.setAttribute("InProcessTseSampleDetails", listInProcessSmplDetails);
            }
            switch (request.getParameter("status")) {
                case "0":
                    List ml = SharedOperations.getLabdetails();
                    request.setAttribute("labmaster", ml);
                    request.setAttribute("labLocCode", sLabLocCode);
                    rd = request.getRequestDispatcher("getAllIntransitSample.jsp");
                    break;
                case "1":
                    rd = request.getRequestDispatcher("getAllCreatedSample.jsp");
                    break;
                case "2":
                    rd = request.getRequestDispatcher("getAllReceivedByLab.jsp");//Pending
                    break;
                case "3":
                    rd = request.getRequestDispatcher("getAllTestedSample.jsp");
                    break;
                case "4":
                    rd = request.getRequestDispatcher("getAllSentToCustomer.jsp");
                    break;
                default:
                    rd = request.getRequestDispatcher("getAllSentToCustomer.jsp");
            }
            rd.forward(request, response);
        } else {
            response.sendError(response.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
