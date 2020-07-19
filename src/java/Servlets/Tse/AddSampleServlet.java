package Servlets.Tse;

import java.io.IOException;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.ApplicationSQLDate;
import operations.SharedOperations;
import viewModel.MessageDetails;
import viewModel.MstTank;

public class AddSampleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MstTank mstTank = new MstTank();
        mstTank.setSamplingNo("0");
        mstTank.setPostponeCount("0");
        mstTank.setApplDesc(request.getParameter("applicationDesc"));
        mstTank.setApplId(request.getParameter("mstApplication"));
        mstTank.setCustId(request.getParameter("mstCustomer"));
        mstTank.setDeptId(request.getParameter("mstDepartment"));
        mstTank.setEquipId(request.getParameter("mstEquipment"));
        mstTank.setIndId(request.getParameter("mstIndustry"));
        mstTank.setProId(request.getParameter("mstProduct"));
        mstTank.setSampleFreq(request.getParameter("sampleFreq"));
        mstTank.setCapacity(request.getParameter("productCapacity"));
        mstTank.setTankNo(request.getParameter("mstTank"));
        mstTank.setNxtSampleDate(ApplicationSQLDate.getnextSampleSQLDate(new Date(), mstTank.getSampleFreq()));
        MessageDetails md = SharedOperations.addSample(mstTank);
        md.setModalTitle("New Sample Creation Status.");
        if (md.isStatus()) {
            md.setModalMessage("New Sample Created Successfully. Due Date for this Sample is: " + ApplicationSQLDate.convertUtilDatetoString(mstTank.getNxtSampleDate()));
            md.setMsgClass("text-success");
        }
        request.setAttribute("messageDetails", md);
        RequestDispatcher rd = request.getRequestDispatcher("ManageSample");
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
