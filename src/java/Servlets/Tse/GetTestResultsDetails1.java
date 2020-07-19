package Servlets.Tse;

import communicate.MailTemplate;
import communicate.SendMail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.DispSeqNoComparator;
import operations.LABoperations;
import operations.TSEoperations;
import viewModel.graphData;
import viewModel.MstTest;
import viewModel.MstTestParameter;
import viewModel.SampleDetails;

public class GetTestResultsDetails1 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        graphData gd = new graphData();
        HttpSession session = request.getSession();
        SampleDetails sd = TSEoperations.getSampleDetails(request.getParameter("smplid"));
        String strType = request.getParameter("type");
        String prevToPrevSampleNo = "", prevSampleNo = "", curSampleNO = "";
        curSampleNO = sd.getSamplingNo();
        prevSampleNo = Integer.toString(Integer.parseInt(curSampleNO) - 1);
        prevToPrevSampleNo = Integer.toString(Integer.parseInt(curSampleNO) - 2);
        List listprevcurRst = null;//LABoperations.getCustSampleIdNoWiseTestResult(sd.getTankId(), prevToPrevSampleNo, prevSampleNo, curSampleNO, sd.getMstProd().getProId());
        List lstSmplRstDate = null;//LABoperations.getCustSampleIdNoWiseTestResultDate(sd.getTankId(), prevToPrevSampleNo, prevSampleNo, curSampleNO);
        List finalTestReport = new ArrayList();
        MstTest mTest;
        MstTest mstTestTemp = new MstTest();
        MstTest mstTestTemp1, mstTestTemp2;
        int i;
        int flag = 0;
        for (i = 0; i < listprevcurRst.size(); i++) {
            mstTestTemp1 = (MstTest) listprevcurRst.get(i);
            flag = 0;
            for (int k = 0; k < finalTestReport.size(); k++) {
                mstTestTemp2 = (MstTest) finalTestReport.get(k);
                if (mstTestTemp2.getTestId().equals(mstTestTemp1.getTestId())) {
                    if (mstTestTemp1.getMstTestParam().getSamplingNo().equals(prevToPrevSampleNo)) {
                        ((MstTest) finalTestReport.get(k)).setPrevToprevVal(mstTestTemp1.getTestVal());
                    } else if (mstTestTemp1.getMstTestParam().getSamplingNo().equals(prevSampleNo)) {
                        ((MstTest) finalTestReport.get(k)).setPrevVal(mstTestTemp1.getTestVal());
                    } else {
                        ((MstTest) finalTestReport.get(k)).setCurVal(mstTestTemp1.getTestVal());
                    }
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                mTest = new MstTest();
                mTest = mstTestTemp1;
                if (mstTestTemp1.getMstTestParam().getSamplingNo().equals(prevToPrevSampleNo)) {
                    mTest.setPrevToprevVal(mstTestTemp1.getTestVal());
                } else if (mstTestTemp1.getMstTestParam().getSamplingNo().equals(prevSampleNo)) {
                    mTest.setPrevVal(mstTestTemp1.getTestVal());
                } else {
                    mTest.setCurVal(mstTestTemp1.getTestVal());
                }
                finalTestReport.add(mTest);
            }
        }
        
        String[] arrRsltDates = {"No Data", "No Data", "No Data"};
         for (int q = 0; q < lstSmplRstDate.size(); q++) {
            mstTestTemp.setMstTestParam((MstTestParameter) lstSmplRstDate.get(q));
            if (mstTestTemp.getMstTestParam().getSamplingNo().equals(prevToPrevSampleNo)) {
                arrRsltDates[0] = mstTestTemp.getMstTestParam().getStrResultDate();
            } else if (mstTestTemp.getMstTestParam().getSamplingNo().equals(prevSampleNo)) {
                arrRsltDates[1] = mstTestTemp.getMstTestParam().getStrResultDate();
            } else if (mstTestTemp.getMstTestParam().getSamplingNo().equals(curSampleNO)) {
                arrRsltDates[2] = mstTestTemp.getMstTestParam().getStrResultDate();
            } else {
                arrRsltDates[q] = "No Data";
            }
        }
        Collections.sort(finalTestReport, new DispSeqNoComparator());
        int maxSmplNo = Integer.parseInt(curSampleNO);
        int minSmplNo = maxSmplNo - 6; //considering past 6 sampling results
        if (minSmplNo < 1) {
            minSmplNo = 1; //as sampling no starts from 1 for each sample
        }
        List<MstTest> mstProdTestList = new ArrayList<MstTest>(); //= LABoperations.getmstProdTestMinMax(sd.getMstProd().getProId(), "1,3,9");
        List periodVsKv40 = LABoperations.getTestParameterWiseResultHistory(sd.getTankId(), "1", minSmplNo, maxSmplNo);
        List periodVsVI = LABoperations.getTestParameterWiseResultHistory(sd.getTankId(), "3", minSmplNo, maxSmplNo);
        List periodVsTAN = LABoperations.getTestParameterWiseResultHistory(sd.getTankId(), "9", minSmplNo, maxSmplNo);

        gd.setKv40minRangeVal(getMinOfTestValuePropertyAmongListOfObject(periodVsKv40, mstProdTestList.get(0).getMstTestParam().getMinValue()));
        gd.setKv40maxRangeVal(getMaxOfTestValuePropertyAmongListOfObject(periodVsKv40, mstProdTestList.get(0).getMstTestParam().getMaxValue()));
        gd.setKv40minVal(mstProdTestList.get(0).getMstTestParam().getMinValue());
        gd.setKv40maxVal(mstProdTestList.get(0).getMstTestParam().getMaxValue());
        gd.setKv40Data(getTestData(periodVsKv40));
        gd.setKv40minRangeVal(setLowerXAxisForMinRangeValue(gd.getKv40maxRangeVal(), gd.getKv40minRangeVal(), gd.getKv40minVal()));
        gd.setKv40maxRangeVal(setUpperXAxisForMinRangeValue(gd.getKv40maxRangeVal(), gd.getKv40minRangeVal(), gd.getKv40maxVal()));

        gd.setViscocityIndexminRangeVal(getMinOfTestValuePropertyAmongListOfObject(periodVsVI, mstProdTestList.get(1).getMstTestParam().getMinValue()));
        gd.setViscocityIndexmaxRangeVal(getMaxOfTestValuePropertyAmongListOfObject(periodVsVI, mstProdTestList.get(1).getMstTestParam().getMinValue()));
        gd.setViscocityIndexminVal(mstProdTestList.get(1).getMstTestParam().getMinValue());
        gd.setViscocityIndexData(getTestData(periodVsVI));
        gd.setViscocityIndexminRangeVal(setLowerXAxisForMinRangeValue(gd.getViscocityIndexmaxRangeVal(), gd.getViscocityIndexminRangeVal(), gd.getViscocityIndexminVal()));
        gd.setViscocityIndexmaxRangeVal(setUpperXAxisForMinRangeValue(gd.getViscocityIndexmaxRangeVal(), gd.getViscocityIndexminRangeVal(), gd.getViscocityIndexminVal()));

        gd.setTanMinRangeVal(getMinOfTestValuePropertyAmongListOfObject(periodVsTAN, mstProdTestList.get(2).getMstTestParam().getMaxValue()));
        gd.setTanMaxRangeVal(getMaxOfTestValuePropertyAmongListOfObject(periodVsTAN, mstProdTestList.get(2).getMstTestParam().getMaxValue()));
        gd.setTanMaxVal(mstProdTestList.get(2).getMstTestParam().getMaxValue());
        gd.setTanData(getTestData(periodVsTAN));
        gd.setTanMinRangeVal(setLowerXAxisForMinRangeValue(gd.getTanMaxRangeVal(), gd.getTanMinRangeVal(), gd.getTanMaxVal()));
        gd.setTanMaxRangeVal(setUpperXAxisForMinRangeValue(gd.getTanMaxRangeVal(), gd.getTanMinRangeVal(), gd.getTanMaxVal()));

        session.setAttribute("gpsd", sd); //psd=get processed sample details
        request.setAttribute("arrRsltDts", arrRsltDates);
        session.setAttribute("finalTestReportResult", finalTestReport);
        request.setAttribute("graphData", gd);
        RequestDispatcher rd = null;
        if(TSEoperations.getSingleSampling(sd.getTankId())){
            switch (strType){
                case "READ":
                    rd = request.getRequestDispatcher("singleTestedSamplingResults.jsp");
                    rd.forward(request, response);
                    break;
                case "WRITE":
                    rd = request.getRequestDispatcher("viewSingleTseTestResult.jsp");
                    rd.forward(request, response);
                    break;
                default:
                    response.sendError(response.SC_NOT_FOUND);
            }
        }else{
            System.out.println("Cust ID is"+sd.getMstDept().getMstCustomer().getCustomerId()+" "+request.getParameter("smplid"));
            MailTemplate mt = SendMail.getMailPeople(sd.getMstDept().getMstCustomer().getCustomerId(),sd.getTankId());
            request.setAttribute("SendTo", mt.getHodEmail());
            request.setAttribute("SendCC", mt.getTseEmail() + ", " + mt.getEmpAuthorityEmail());
            request.setAttribute("SendSubject", "Status of Sample Id: " + sd.getSampleId());
            request.setAttribute("SendBody", "");
            request.setAttribute("hodName", mt.getHodName());
            switch (strType){
                case "READ":
                    rd = request.getRequestDispatcher("getTestedSample.jsp");
                    rd.forward(request, response);
                    break;
                case "WRITE":
                    rd = request.getRequestDispatcher("viewTseTestResult.jsp");
                    rd.forward(request, response);
                    break;
                default:
                    response.sendError(response.SC_NOT_FOUND);
            }
        }
    }
    
    public String getTestData(List listObject) {
        MstTest mstTest = null;
        String data = "";
        for (int m = 0; m < listObject.size(); m++) {
            mstTest = (MstTest) listObject.get(m);
            String[] parts = mstTest.getMstTestParam().getStrResultDate().split("-");
            data = "[Date.UTC(" + parts[2] + ", " + parts[1] + "-1, " + parts[0] + "), " + mstTest.getTestVal() + "], " + data;
        }
        return method(data);
    }

    public String method(String str) {
        return str.substring(0, str.length() - 2);
    }

    private String getMinOfTestValuePropertyAmongListOfObject(List<MstTest> list, String compare) {
        Double min = Double.parseDouble(list.get(0).getTestVal().trim()), comp = Double.parseDouble(compare);
        for (int i = 1; i < list.size(); i++) {
            if (Double.parseDouble(list.get(i).getTestVal().trim()) < min) {
                min = Double.parseDouble(list.get(i).getTestVal().trim());
            }
        }
        return (comp >= min ? Double.toString(min) : compare);
    }

    private String getMaxOfTestValuePropertyAmongListOfObject(List<MstTest> list, String compare) {
        Double max = Double.parseDouble(list.get(0).getTestVal().trim()), comp = Double.parseDouble(compare);
        for (int i = 1; i < list.size(); i++) {
            if (Double.parseDouble(list.get(i).getTestVal().trim()) > max) {
                max = Double.parseDouble(list.get(i).getTestVal().trim());
            }
        }
        return (max >= comp ? Double.toString(max) : compare);
    }

    private String setLowerXAxisForMinRangeValue(String maxRangeVal, String minRangeVal, String minVal) {
        Double maxRange = Double.parseDouble(maxRangeVal);
        Double minRange = Double.parseDouble(minRangeVal), min = Double.parseDouble(minVal);
        Double temp;
        int retval = Double.compare(minRange, min);
        if (retval == 0) {
            temp = (double) (maxRange - minRange) / 10;
            minRange = minRange - temp;
            if (minRange < 0) {
                minRange = 0.0;
            }
        }
        return Double.toString(minRange);
    }

    private String setUpperXAxisForMinRangeValue(String maxRangeVal, String minRangeVal, String maxVal) {
        Double maxRange = Double.parseDouble(maxRangeVal);
        Double minRange = Double.parseDouble(minRangeVal), max = Double.parseDouble(maxVal);
        Double temp;
        int retval = Double.compare(maxRange, max);
        if (retval < 0) {
            maxRange = max;
        }
        temp = (double) (maxRange - minRange) / 10;
        maxRange = maxRange + temp;
        if (maxRange < 0) {
            maxRange = 0.0;
        }
        return Double.toString(maxRange);
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
}
