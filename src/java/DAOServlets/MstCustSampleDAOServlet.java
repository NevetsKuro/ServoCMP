package DAOServlets;

import DAOs.MstCustSampleDAO;
import DAOs.MstDepartmentDAO;
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
import operations.ApplicationSQLDate;
import viewModel.MessageDetails;
import viewModel.MstTank;

public class MstCustSampleDAOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/GetCustomerDept":
                getCustomerDept(request, response);
                break;
            case "/GetSample":
                getSample(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MessageDetails md = new MessageDetails();
        
        MstTank mstTank = new MstTank();
        mstTank.setTankId(request.getParameter("sampleInfoId"));
        mstTank.setIndId(request.getParameter("sampleIndustry"));
        mstTank.setCustId(request.getParameter("sampleCustomer"));
        mstTank.setDeptId(request.getParameter("sampleDepartment"));
        mstTank.setApplId(request.getParameter("sampleApplication"));
        mstTank.setEquipId(request.getParameter("sampleEquipment"));
        mstTank.setTankNo(request.getParameter("sampleTankNo"));
        mstTank.setProId(request.getParameter("sampleProduct"));
        mstTank.setCapacity(request.getParameter("sampleProCapacity"));
        mstTank.setSampleFreq(request.getParameter("sampleFreq"));
        mstTank.setStrNxtDate(request.getParameter("sampleNextDate"));
        mstTank.setNxtSampleDate(ApplicationSQLDate.convertStringtoUtilDate(mstTank.getStrNxtDate()));
        mstTank.setStrPrevDate(request.getParameter("samplePrevDate"));
        mstTank.setPrevSampleDate(ApplicationSQLDate.convertStringtoUtilDate(mstTank.getStrPrevDate()));
        mstTank.setLastOilChange(request.getParameter("sampleOilChanged"));
        mstTank.setApplDesc(request.getParameter("sampleAppDesc"));
        mstTank.setUpdatedBy(user.getsEMP_CODE());
        switch (request.getServletPath()) {
            case "/UpdateSample":
                md = MstCustSampleDAO.updateSample(mstTank);
                md.setModalTitle("Sample Update Details");
                break;
            case "/AddSample":
                mstTank.setSamplingNo("0");
                mstTank.setPostponeFlag("0");
                mstTank.setPostponeCount("0");
                md = MstCustSampleDAO.insertSample(mstTank);
                md.setModalTitle("Add Sample Details");
                break;
        }
        request.setAttribute("messageDetails", md);
        RequestDispatcher rd = request.getRequestDispatcher("Tse/ManageSample");
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void getCustomerDept(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            List dept = MstDepartmentDAO.listAllDepartment(request.getParameter("Customer"));
            Gson gson = new Gson();
            if (!dept.isEmpty()) {
                response.getWriter().write(gson.toJson(dept));
            } else {
                response.getWriter().write(gson.toJson("No Department with the selected Customer. Kindly Add New Department for "));
            }
        }
    }

    private void getSample(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            String sqlQuery = null;
            if (!"".equals(request.getParameter("Industry"))) {
                sqlQuery = "SELECT MST_TANK.TANK_ID, MST_INDUSTRY.IND_ID, MST_INDUSTRY.IND_NAME, MST_CUSTOMER.CUST_ID, MST_CUSTOMER.CUST_NAME, "
                        + "MST_DEPARTMENT.DEPT_ID, MST_DEPARTMENT.DEPT_NAME, MST_APPLICATION.APPL_ID, MST_APPLICATION.APPL_NAME, MST_EQUIPMENT.EQUIP_ID, "
                        + "MST_EQUIPMENT.EQUIP_NAME, MST_PRODUCT.PROD_ID, MST_PRODUCT.PROD_NAME, MST_TANK.TANK_NO, MST_TANK.TANK_DESC, MST_TANK.APPL_DESC, "
                        + "MST_TANK.CAPACITY, MST_TANK.SAMPLING_NO, MST_TANK.SAMPLE_FREQ, MST_TANK.PREV_SAMPLE_DATE, MST_TANK.NEXT_SAMPLE_DATE, "
                        + "MST_TANK.OLD_NEXT_SAMPLE_DATE, MST_TANK.POSTPONE_FLAG, MST_TANK.POSTPONE_COUNT, MST_TANK.LAST_OIL_CHANGED, MST_TANK.UPDATED_BY, "
                        + "MST_TANK.UPDATED_DATETIME FROM ((((((MST_TANK INNER JOIN MST_INDUSTRY ON MST_TANK.IND_ID = MST_INDUSTRY.IND_ID) INNER "
                        + "JOIN MST_CUSTOMER ON MST_TANK.IND_ID = MST_CUSTOMER.CUST_ID) INNER JOIN MST_PRODUCT ON MST_TANK.IND_ID = MST_PRODUCT.PROD_ID) "
                        + "INNER JOIN MST_EQUIPMENT ON MST_TANK.EQUIP_ID = MST_EQUIPMENT.EQUIP_ID) INNER JOIN MST_APPLICATION ON MST_TANK.APPL_ID = "
                        + "MST_APPLICATION.APPL_ID) INNER JOIN MST_DEPARTMENT ON MST_TANK.DEPT_ID = MST_DEPARTMENT.DEPT_ID) WHERE MST_TANK.IND_ID = " + request.getParameter("Industry");
            }
            if (!"".equals(request.getParameter("Customer"))) {
                sqlQuery += " AND MST_TANK.CUST_ID = " + request.getParameter("Customer");
            }
            if (!"".equals(request.getParameter("Department"))) {
                sqlQuery += " AND MST_TANK.DEPT_ID = " + request.getParameter("Department");
            }
            if (!"".equals(request.getParameter("Application"))) {
                sqlQuery += " AND MST_TANK.APPL_ID = " + request.getParameter("Application");
            }
            if (!"".equals(request.getParameter("Equipment"))) {
                sqlQuery += " AND MST_TANK.EQUIP_ID = " + request.getParameter("Equipment");
            }
            if (!"".equals(request.getParameter("TankNo"))) {
                sqlQuery += " AND MST_TANK.TANK_NO = " + request.getParameter("TankNo");
            }
            if (!"".equals(request.getParameter("Product"))) {
                sqlQuery += " AND MST_TANK.PROD_ID = " + request.getParameter("Product");
            }
            List result = MstCustSampleDAO.listAllSamples(sqlQuery);
            Gson gson = new Gson();
            if (!result.isEmpty()) {
                response.getWriter().write(gson.toJson(result));
            } else {
                response.getWriter().write(gson.toJson("No Data Found"));
            }
        }
    }
}
