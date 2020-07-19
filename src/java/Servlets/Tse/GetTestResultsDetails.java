/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets.Tse;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import com.google.gson.Gson;
import communicate.MailTemplate;
import communicate.SendMail;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.ApplicationSQLDate;
import operations.DispSeqNoComparator;
import operations.LABoperations;
import operations.TSEoperations;
import viewModel.MstTest;
import viewModel.MstTestParameter;
import viewModel.SampleDetails;
import viewModel.TestResultDetails;
import viewModel.graphData;
import viewModel.uGraphData;
import viewModel.StevenModels.TestResultIds;

public class GetTestResultsDetails extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        graphData gd = new graphData();
        HttpSession session = request.getSession();
        String labCode = request.getParameter("labCode");
        String sampleType = request.getParameter("sampleType");
        String lcode = "";
        SampleDetails sd = new SampleDetails();
        List<SampleDetails> sdlist = new ArrayList<SampleDetails>();
        //get count of samples
        String alert = TSEoperations.getSampleCount(request.getParameter("smplid"));
        //get samples thru lab codes
        if(labCode.indexOf(",")>-1){
            sdlist = TSEoperations.getSampleDetails2(request.getParameter("smplid"),labCode);
        }else{
            sd = TSEoperations.getSampleDetails(request.getParameter("smplid"));
            sdlist.add(sd);
        }
        //if OMC product, then fetch the (similar product values) selected
        if(sdlist.get(0).getMstProd().getProId().equals("OMC")){
            String proId = TSEoperations.getSimProductForOMC(sdlist.get(0).getTankId());
            sdlist.get(0).getMstProd().setProId(proId);
        }
        String strType = request.getParameter("type");
        String prevToPrevSampleNo = "", prevSampleNo = "", curSampleNO = "";
        
        curSampleNO = (sampleType.equals("CMP")?sdlist.get(0).getSampling_no_cmp():sdlist.get(0).getSampling_no_ots()); //sd.getSamplingNo();
        prevSampleNo = Integer.toString(Integer.parseInt(curSampleNO) - 1);
        prevToPrevSampleNo = Integer.toString(Integer.parseInt(curSampleNO) - 2);
        List listprevcurRst = LABoperations.getCustSampleIdNoWiseTestResult(sdlist.get(0).getTankId(), prevToPrevSampleNo, prevSampleNo, curSampleNO, sdlist.get(0).getMstProd().getProId(), sampleType);
        List lstSmplRstDate = LABoperations.getCustSampleIdNoWiseTestResultDate(sdlist.get(0).getTankId(), prevToPrevSampleNo, prevSampleNo, curSampleNO, sampleType);
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
        List<TestResultIds> mstTestIdsList = getTestIdsfromSampleId(sdlist.get(0),sampleType); // Test ids for each graph 

        String[] array = new String[mstTestIdsList.size()];
        int index = 0;
        for (TestResultIds value : mstTestIdsList) { //filter out test params for which graph cannot be generated
            if (!value.getValueCheck().equals("5") & !value.getValueCheck().equals("4")) {
                array[index] = value.getTestId();
                index++;
            }
        }
        List<String> list = new ArrayList<String>();
        for (String s : array) { // filter out null values
            if (s != null && s.length() > 0) {
                list.add(s);
            }
        }
        array = list.toArray(new String[list.size()]);
        
        List<MstTest> mstProdTestList = LABoperations.getmstProdTestMinMax(sdlist.get(0).getMstProd().getProId(),"1,3,9",mstTestIdsList); // String.join(",", array),mstTestIdsList); //test params with values 
//        List<MstTest> mstProdTestList = LABoperations.getmstProdTestMinMax(sd.getMstProd().getProId(), "1,3,9");
//
        List<uGraphData> ugraphData = createGraphDataObjects(sdlist.get(0), mstProdTestList, minSmplNo, maxSmplNo); // creating the graph object for individual test

        session.setAttribute("gpsd", sdlist); // sd = get processed sample details
        session.setAttribute("alerts", alert); // sd = get processed sample details
        request.setAttribute("arrRsltDts", arrRsltDates);
        session.setAttribute("finalTestReportResult", finalTestReport);
        request.setAttribute("graphData", new Gson().toJson(ugraphData));
        RequestDispatcher rd = null;
        if (sampleType.equals("OTS")) {
//            System.out.println("Cust ID is" + sd.getMstDept().getMstCustomer().getCustomerId() + " " + request.getParameter("smplid"));
            MailTemplate mt = SendMail.getMailPeople(sdlist.get(0).getMstDept().getMstCustomer().getCustomerId(), sdlist.get(0).getTankId());
            request.setAttribute("SendTo", mt.getHodEmail());
            request.setAttribute("SendCC", mt.getTseEmail() + ", " + mt.getEmpAuthorityEmail());
            request.setAttribute("SendSubject", "Status of Sample Id: " + sdlist.get(0).getSampleId());
            request.setAttribute("SendBody", "");
            request.setAttribute("hodName", mt.getHodName());
            switch (strType) {
                case "READ":
                    rd = request.getRequestDispatcher("singleTestedSamplingResults.jsp");
                    rd.forward(request, response);
                    break;
                case "WRITE":
                    rd = request.getRequestDispatcher("viewSingleTseTestResult.jsp");
                    rd.forward(request, response);
                    break;
                default:
                    System.out.println("error");
                    response.sendError(response.SC_NOT_FOUND);
            }
        } else {
//            System.out.println("Cust ID is" + sd.getMstDept().getMstCustomer().getCustomerId() + " " + request.getParameter("smplid"));
            MailTemplate mt = SendMail.getMailPeople(sdlist.get(0).getMstDept().getMstCustomer().getCustomerId(), sdlist.get(0).getTankId());
            request.setAttribute("SendTo", mt.getHodEmail());
            request.setAttribute("SendCC", mt.getTseEmail() + ", " + mt.getEmpAuthorityEmail());
            request.setAttribute("SendSubject", "Status of Sample Id: " + sdlist.get(0).getSampleId());
            request.setAttribute("SendBody", "");
            request.setAttribute("hodName", mt.getHodName());
            switch (strType) {
                case "READ":
                    rd = request.getRequestDispatcher("getTestedSample.jsp");
                    rd.forward(request, response);
                    break;
                case "WRITE":
                    rd = request.getRequestDispatcher("viewTseTestResult.jsp");
                    rd.forward(request, response);
                    break;
                default:
                    System.out.println("error");
                    response.sendError(response.SC_NOT_FOUND);
            }
        }
    }

    public ArrayList<Map<String,String>> getTestData(List listObject) {
        MstTest mstTest = null;
        ArrayList<Map<String,String>> arrMap = new ArrayList<Map<String,String>>();
        for (int m = 0; m < listObject.size(); m++) {
            Map<String,String> data = new HashMap<String,String>();
            mstTest = (MstTest) listObject.get(m);
            String[] parts = mstTest.getMstTestParam().getStrResultDate().split("-");
            data.put("date",mstTest.getMstTestParam().getStrResultDate());
//            data.put("date","Date.UTC(" + parts[2] + ", " + parts[1] + "-1, " + parts[0] + ")");
            data.put("val",mstTest.getTestVal());
            arrMap.add(data);
        }
        return arrMap;
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

    private List getTestIdsfromSampleId(SampleDetails sd,String sampleType) {
        List mstTestIdsList = new ArrayList();
        String samplingNo = (sampleType.equals("CMP")?sd.getSampling_no_cmp():sd.getSampling_no_ots());
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.sample_id, t1.tank_id, t1.test_id, m1.value_check_id FROM TEST_RESULT_DETAILS t1 " +
                    "inner join mst_mkt_test_val_spec m1 on t1.prod_id = m1.prod_id and t1.test_id = m1.test_id where sample_id = ? and sampling_no = ? ");) {
            pst.setString(1, sd.getSampleId());
            pst.setString(2, samplingNo);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                TestResultIds trd = new TestResultIds();
                trd.setSampleDetails(res.getString(1));
                trd.setTestId(res.getString(3));
                trd.setTankNo(res.getString(2));
                trd.setValueCheck(res.getString(4));
                mstTestIdsList.add(trd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getTestIdsfromSampleId() ");
        }
        return mstTestIdsList;
    }

    private List<uGraphData> createGraphDataObjects(SampleDetails sd, List<MstTest> mstProdTestList, int minSmplNo, int maxSmplNo) {
        List commontestData;
        List<uGraphData> graphDataList = new ArrayList<uGraphData>();
        for (int j = 0; j < mstProdTestList.size(); j++) {
            commontestData = LABoperations.getTestParameterWiseResultHistory(sd.getTankId(), mstProdTestList.get(j).getTestId(), minSmplNo, maxSmplNo);

            try {
                uGraphData graphData = new uGraphData();
                if (mstProdTestList.get(j).getMstTestParam() != null) {
                    String minValue = mstProdTestList.get(j).getMstTestParam().getMinValue();
                    String maxValue = mstProdTestList.get(j).getMstTestParam().getMaxValue();
                    if (minValue!=null) {
                        graphData.setMinRangeVal(getMinOfTestValuePropertyAmongListOfObject(commontestData, mstProdTestList.get(j).getMstTestParam().getMinValue()));
                        graphData.setMinVal(mstProdTestList.get(j).getMstTestParam().getMinValue());
                    }else{
                        graphData.setMinRangeVal(getMinOfTestValuePropertyAmongListOfObject(commontestData, mstProdTestList.get(j).getMstTestParam().getMaxValue()));
                        graphData.setMinVal(mstProdTestList.get(j).getMstTestParam().getMaxValue());
                    }
                    if (maxValue!=null) {
                        graphData.setMaxRangeVal(getMaxOfTestValuePropertyAmongListOfObject(commontestData, mstProdTestList.get(j).getMstTestParam().getMaxValue()));
                        graphData.setMaxVal(mstProdTestList.get(j).getMstTestParam().getMaxValue());
                    }else{
                        graphData.setMaxRangeVal(getMaxOfTestValuePropertyAmongListOfObject(commontestData, mstProdTestList.get(j).getMstTestParam().getMinValue()));
                        graphData.setMaxVal(mstProdTestList.get(j).getMstTestParam().getMinValue());
                    }
                }
                graphData.setTestData(getTestData(commontestData));
                graphData.setMinRangeVal(setLowerXAxisForMinRangeValue(graphData.getMaxRangeVal(), graphData.getMinRangeVal(), graphData.getMinVal()));
                graphData.setMaxRangeVal(setUpperXAxisForMinRangeValue(graphData.getMaxRangeVal(), graphData.getMinRangeVal(), graphData.getMaxVal()));
                graphData.setTestName(mstProdTestList.get(j).getTestName());
                graphDataList.add(graphData);
            } catch (Exception ex) {
                MyLogger.logIt(ex, "LABOperations.createGraphDataObjects() ");
            }
        }

        return graphDataList;
    }

}
