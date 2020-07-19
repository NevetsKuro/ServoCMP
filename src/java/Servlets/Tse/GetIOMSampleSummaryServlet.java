package Servlets.Tse;

import Exceptions.MyLogger;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.SharedOperations;
import operations.TSEoperations;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import viewModel.MstLab;
import viewModel.MstTest;
import viewModel.SampleDetails;

public class GetIOMSampleSummaryServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("errorPages/error-404.html");
        switch (request.getServletPath()) {
            case "/Tse/GetSampleSummaryData":
                String arrSampleIDs[] = null;
                List smplList = new ArrayList();
                String sLabLocCode = request.getParameter("nameLabLocCode");
                try {
                    JSONParser parser = new JSONParser();
                    JSONArray array = (JSONArray) parser.parse(request.getParameter("sampleIds"));
                    arrSampleIDs = new String[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        arrSampleIDs[i] = (String) array.get(i);
                        SampleDetails smplDetail = TSEoperations.geteditsendsampletoLAB(arrSampleIDs[i],sLabLocCode);
                        List<MstTest> liExistTst = SharedOperations.getSampleWiseTestParamDetail(arrSampleIDs[i],sLabLocCode);
                        String allTestParamNames = "";
                        for (int j = 0; j < liExistTst.size(); j++) {
                            if (j == 0) {
                                allTestParamNames = liExistTst.get(j).getTestName();
                            } else {
                                allTestParamNames += " | " + liExistTst.get(j).getTestName();
                            }
                        }
                        smplDetail.setTestParamNames(allTestParamNames);
                        smplList.add(smplDetail);
                    }
                } catch (Exception e) {
                    smplList = null;
                    MyLogger.logIt(e, "GetSampleSummaryData Servlet");
                }
                MstLab mstLbt = SharedOperations.getLabdetails(sLabLocCode);
                request.setAttribute("mstLabDetail", mstLbt);
                request.setAttribute("IOMSampleList", smplList);
                request.setAttribute("labLocCode", sLabLocCode);
                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                request.setAttribute("Date", ft.format(date));
                rd = request.getRequestDispatcher("generateIOM.jsp");
                break;
            case "/Lab/GetIOMWiseSummaryData":
            case "/Tse/GetIOMWiseSummaryData":
                List smplList1 = new ArrayList();
                String sIomRefNo = request.getParameter("iomRefNo");
                List sampleIds = SharedOperations.getIomWiseSampleIds(sIomRefNo);
                String sLabLocCode2 = request.getParameter("nameLabLocCode");
                for (int i = 0; i < sampleIds.size(); i++) {
                    String sampleId = (String) sampleIds.get(i);
                    SampleDetails smplDetail1 = TSEoperations.geteditsendsampletoLAB(sampleId,sLabLocCode2);
                    List<MstTest> liExistTst1 = SharedOperations.getSampleWiseTestParamDetail(sampleId,sLabLocCode2);
                    String allTestParamNames = "";
                    for (int j = 0; j < liExistTst1.size(); j++) {
                        if (j == 0) {
                            allTestParamNames = liExistTst1.get(j).getTestName();
                        } else {
                            allTestParamNames += " | " + liExistTst1.get(j).getTestName();
                        }
                    }
                    smplDetail1.setTestParamNames(allTestParamNames);
                    smplList1.add(smplDetail1);
                }
                String sLabLocCode1 = request.getParameter("nameLabLocCode");
                String date1 = request.getParameter("date");
                MstLab mstLbt1 = SharedOperations.getLabdetails(sLabLocCode1);
                
                HashMap<String,String> hs = new HashMap<String,String>();
                String SenderEmpCode = request.getParameter("sEmpCod");
                hs = SharedOperations.getSenderInfo(SenderEmpCode, request.getServletContext().getInitParameter("cemDBPath"));
                String sfromName =  hs.get("fromName");
                String sDesign = hs.get("fromDesign");
                String sComp = hs.get("fromComp");
                
                request.setAttribute("mstLabDetail", mstLbt1);
                request.setAttribute("IOMSampleList", smplList1);
                request.setAttribute("labLocCode", sLabLocCode1);
                request.setAttribute("fromName", sfromName);
                request.setAttribute("fromDesign", sDesign);
                request.setAttribute("fromComp", sComp);
                request.setAttribute("iomRefNo", sIomRefNo);
                request.setAttribute("Date", date1);
                rd = request.getRequestDispatcher("printIOM.jsp");
                break;
        }
        rd.forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
