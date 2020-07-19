package Servlets.Tse;

import DAOs.MstProductDAO;
import DAOs.MstTestDAO;
import Exceptions.MyLogger;
import Validators.ServoTSEValidator;
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
import operations.ApplicationSQLDate;
import operations.TSEoperations;
import operations.SharedOperations;
import viewModel.MessageDetails;
import viewModel.MstProduct;
import viewModel.MstTest;
import viewModel.SampleDetails;

public class GetSamplePendingAtTseServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd;
        try {
            if (request.getParameter("do").equals("create") || request.getParameter("do").equals("maintenance") && !request.getParameter("sampleType").equals("")) {
                List<MstTest> testMaster = new ArrayList();
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("sUser");
                testMaster = (List<MstTest>) session.getAttribute("testMaster");
                List<MstProduct> listproduct = new ArrayList<MstProduct>();
                SampleDetails sd = TSEoperations.getcreateSmpl(request.getParameter("tank"));
                if (sd.getMstProd().getProId().equalsIgnoreCase("OMC")) {
                    listproduct = MstProductDAO.getProductByOMCTag(sd.getTankId(), sd.getMstProd().getProId());
                } else {
                    MstProduct prod = new MstProduct();
                    prod.setProId(sd.getMstProd().getProId());
                    prod.setProName(sd.getMstProd().getProName());
                    listproduct.add(prod);
                }
                if (testMaster == null) {
                    session.setAttribute("testMaster", MstTestDAO.gettestMaster());
                    testMaster = (List<MstTest>) session.getAttribute("testMaster");//All Test Cases available
                }
                testMaster = MstTestDAO.getProdSpecWiseTestMasterWithStatus(listproduct.get(0).getProId(), user.getsEMP_CODE());//test case with details i.e. qty,active
                List ml = SharedOperations.getLabdetailsTseEmpCodeWise(user.getsEMP_CODE());//for CSL lab Details
                List ml2 = SharedOperations.getRNDLabdetailsEmpCodeWise(listproduct.get(0).getProId());//for RND lab Details
                List pl = SharedOperations.getPriorityMstdetails();
                List<MstTest> liExistTst = MstTestDAO.getexistingtestofSample(listproduct.get(0).getProId());
                List liOtherTst = new ArrayList();
                int flag = 0;
                for (int i = 0; i < testMaster.size(); i++) {
                    flag = 0;
                    for (int j = 0; j < liExistTst.size(); j++) {
                        if (liExistTst.get(j).getTestId().equals(testMaster.get(i).getTestId())) {
                            liExistTst.get(j).setTestName(testMaster.get(i).getTestName());
                            liExistTst.get(j).setSampleqty(testMaster.get(i).getSampleqty());
                            liExistTst.get(j).getMstTestParam().setCheckId(testMaster.get(i).getMstTestParam().getCheckId());
                            liExistTst.get(j).setActive(testMaster.get(i).getActive());
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        liOtherTst.add(testMaster.get(i));
                    }
                }
//                request.setAttribute("sampleType", request.getParameter("sampleType"));
                request.setAttribute("labmaster", ml);
                request.setAttribute("labmaster2", ml2);
                request.setAttribute("prioritymaster", pl);
                request.setAttribute("preTestList", liExistTst);
                request.setAttribute("addTestList", liOtherTst);
                if (request.getParameter("do").equals("maintenance")) {
                    request.setAttribute("sampleType", request.getParameter("sampleType").equals("0")?"CMP":"OTS");
                    sd.setPostponetillDate(ApplicationSQLDate.getfutureDate(session.getAttribute("postponerestrictDays").toString(), sd.getNxtsampleDate()));
                    session.setAttribute("ms", sd);
                    rd = request.getRequestDispatcher("toMaintenance.jsp");
                    rd.forward(request, response);
                } else if (request.getParameter("do").equals("create")) {
                    request.setAttribute("sampleType", request.getParameter("sampleType"));
                    request.setAttribute("missingTestList", MstTestDAO.getMissingTestParameters(sd.getMstProd().getProId()));
                    sd.setSdStartDate(ApplicationSQLDate.subtractDate(sd.getNxtsampleDate(), -Integer.parseInt(session.getAttribute("sendtoLABdateLimit").toString())));
                    sd.setSdEndDate(ApplicationSQLDate.addDate(sd.getNxtsampleDate(), Integer.parseInt(session.getAttribute("sendtoLABdateLimit").toString())));
                    request.setAttribute("listproduct", listproduct);
                    session.setAttribute("cs", sd);
                    rd = request.getRequestDispatcher("getSamplePendingAtTse.jsp");
                    rd.forward(request, response);
                }
            } else {
                rd = request.getRequestDispatcher("/errorPages/error-404.jsp");
                rd.forward(request, response);
            }
        } catch (Exception ex) {
            MessageDetails md = MyLogger.logIt(ex, "GetSamplePendingAtTseServlet.doGet() ");
            request.setAttribute("errorMsg", md.getModalMessage());
            rd = request.getRequestDispatcher("/errorPages/error.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd;
        HttpSession session = request.getSession();
        MessageDetails md = new MessageDetails();
        try {
            if (ServoTSEValidator.createSample(request, md)) {
                SampleDetails sd = (SampleDetails) session.getAttribute("cs");
                md = TSEoperations.createSample(sd,request.getParameter("labType"),request.getParameter("checkOtsSampling"));
            }
            md.setModalTitle("Sample Creation Status.");
            request.setAttribute("messageDetails", md);
            request.setAttribute("sampleType", (request.getParameter("checkOtsSampling").equals("1")?"OTS":"CMP"));
            rd = request.getRequestDispatcher("GetAllPendingSamplesAtTSE");
            rd.forward(request, response);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, "GetSamplePendingAtTseServlet.doPost() ");
            request.setAttribute("errorMsg", md.getModalMessage());
            rd = request.getRequestDispatcher("/errorPages/error.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
