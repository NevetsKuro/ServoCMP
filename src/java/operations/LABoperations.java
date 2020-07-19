package operations;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import viewModel.MessageDetails;
import viewModel.MstEquipment;
import viewModel.MstIndustry;
import viewModel.MstLabEquipment;
import viewModel.MstTest;
import viewModel.MstTestParameter;
import viewModel.SampleDetails;
import viewModel.StevenModels.TestResultIds;
import viewModel.TestResultDetails;

public class LABoperations {

    public static SampleDetails gettestSample(String smplid, String status, String labCode) {
        SampleDetails sd = new SampleDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.SAMPLE_ID,t1.TANK_ID,t2.IND_NAME,t1.CUST_ID,t3.CUST_NAME,t1.PROD_ID, "
                        + "t4.PROD_NAME,t1.QTY_DRAWN, t1.SAMPLE_CREATED_BY,t1.SAMPLE_CREATED_DATE, "
                        + "t1.TOP_UP_QTY,t1.LAB_LOC_CODE,t6.LAB_NAME,t6.LAB_ADDRESS1,t6.LAB_ADDRESS2, t6.LAB_ADDRESS3, t7.PRIORITY_ID,"
                        + "t7.PRIORITY_NAME, t1.FILE_UPLOAD_STATUS,t1.LAB_FINAL_TEST_REMARKS from SAMPLE_DETAILS t1,MST_INDUSTRY t2,MST_CUSTOMER t3,MST_PRODUCT "
                        + "t4, MST_LAB t6,MST_PRIORITY t7 WHERE t1.IND_ID=t2.IND_ID and t1.CUST_ID=t3.CUST_ID "
                        + "and t1.PROD_ID=t4.PROD_ID AND t1.LAB_LOC_CODE=t6.LAB_LOC_CODE and "
                        + "t1.SAMPLE_PRIORITY_ID=t7.PRIORITY_ID and t1.SAMPLE_ID=? and t1.STATUS_ID=? AND t1.LAB_LOC_CODE = ? ");) {
            pst.setString(1, smplid);
            pst.setString(2, status);
            pst.setString(3, labCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerId(res.getString(4));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(5));
                sd.getMstProd().setProId(res.getString(6));
                sd.getMstProd().setProName(res.getString(7));
                sd.setQtyDrawn(res.getString(8));
                sd.setSamplecreatedBy(res.getString(9));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(10)));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                sd.setTopupQty(res.getString(11));
                sd.getMstLab().setLabCode(res.getString(12));
                sd.getMstLab().setLabName(res.getString(13));
                sd.getMstLab().setLabAdd1(res.getString(14));
                sd.getMstLab().setLabAdd2(res.getString(15));
                sd.getMstLab().setLabAdd3(res.getString(16));
                sd.setSamplepriorityId(res.getString(17));
                sd.setSamplepriorityName(res.getString(18));
                sd.setFileuploadStatus(res.getString(19));
                sd.setLabFinalTestRemarks(res.getString(20));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.gettestSample() ");
        }
        return sd;
    }

    public static MessageDetails sendTestResult(SampleDetails sd, String prodId, String saveStatus, TestResultDetails trd, String createdBy, MessageDetails md) {
        String status = null;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                CallableStatement cst = con.prepareCall("{call INSERT_TEST_RESULT_PROCEDURE(?,?,?,?,?,?,?,?,?,?,?,?,?)}");) {
            ArrayDescriptor des = ArrayDescriptor.createDescriptor("ARRAY_TABLE", con);
            cst.setString(1, sd.getSampleId());
            cst.setString(2, sd.getMstLab().getLabCode());
            cst.setString(3, prodId);
            cst.setString(4, "3");
            cst.setString(5, createdBy);
            cst.setDate(6, ApplicationSQLDate.getcurrentSQLDate());
            cst.setArray(7, new ARRAY(des, con, trd.getTestIds()));
            cst.setArray(8, new ARRAY(des, con, trd.getValues()));
            cst.setArray(9, new ARRAY(des, con, trd.getRemarks()));
            cst.setString(10, trd.getConclusion());
            cst.setString(11, saveStatus);
            cst.registerOutParameter(12, java.sql.Types.VARCHAR);
            cst.registerOutParameter(13, java.sql.Types.VARCHAR);
            cst.executeUpdate();
            status = cst.getString(12);
            md.setModalTitle("Sample Sent Status.");
            md.setModalMessage(cst.getString(12) + " " + cst.getString(13));
            md.setMsgClass("text-success");
        } catch (Exception ex) {
            md.setModalTitle("Sample Sent Status.");
            md.setModalMessage(status + " " + sd.getSampleId());
            md.setMsgClass("text-danger");
            MyLogger.logIt(ex, createdBy);
        }
        return md;
    }

    public static MessageDetails updateTestDocDeleteFlag(String sampleId, String labCode, MessageDetails md) {
        int updateStatus = 0;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE SAMPLE_DETAILS SET FILE_UPLOAD_STATUS='NO' where SAMPLE_ID=? AND lab_loc_code = ?");) {
            pst.setString(1, sampleId);
            pst.setString(2, labCode);
            updateStatus = pst.executeUpdate();
            switch (updateStatus) {
                case 1:
                case 2:
                    md.setModalMessage("Sample Updated Successfully.");
                    md.setMsgClass("text-success");
                    break;
                default:
                    md.setModalMessage("No Sample were Updated.");
                    md.setMsgClass("text-danger");
            }
        } catch (Exception ex) {
            md.setModalMessage("Error While Updating Sample.");
            md.setMsgClass("text-danger");
            MyLogger.logIt(ex, "LABOperations.updateTestDocDeleteFlag() ");
        }
        return md;
    }

    public static List getLabSampleDetails(String sEmp_Code, String sStatus) {
        List listpndSmpls = new ArrayList();
        String query = "SELECT t1.SAMPLE_ID,t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                + "t5.APPL_NAME, t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.STATUS_ID, t1.QTY_DRAWN, "
                + "t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE,t1.LAB_RECEIVED_DATE, t1.SAMPLE_PRIORITY_ID, t1.sample_type, t1.lab_loc_code FROM SAMPLE_DETAILS t1, "
                + "MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, "
                + "MST_PRODUCT t7, MST_TANK t8, MST_LAB t9 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID "
                + "AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.PROD_ID=t7.PROD_ID and t1.tank_id = t8.tank_id AND t1.lab_loc_code = t9.lab_loc_code AND "
                + "t1.LAB_LOC_CODE in(select LAB_LOC_CODE FROM MST_LAB where EMP_CODE=?) AND "
                + "t1.STATUS_ID IN (?) order by t1.SAMPLE_PRIORITY_ID";
        System.out.println(query);
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, sEmp_Code);
            pst.setString(2, sStatus);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(4));
                sd.getMstDept().setDepartmentName(res.getString(5));
                sd.getMstApp().setAppName(res.getString(6));
                sd.setTankNo(res.getString(7));
                sd.getMstEquip().setEquipmentName(res.getString(8));
                sd.getMstProd().setProName(res.getString(9));
                sd.setStatusId(res.getString(10));
                sd.setQtyDrawn(res.getString(11));
                sd.setSamplecreatedBy(res.getString(12));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(13)));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                if (res.getDate(14) != null) {
                    sd.setLabrecDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14)));
                    sd.setStringLabrecDate(ApplicationSQLDate.convertUtilDatetoString(sd.getLabrecDate()));
                }
                sd.setSamplepriorityId(res.getString(15));
                sd.setIsSingleSampling(res.getString(16));
                sd.getMstLab().setLabCode(res.getString(17));
                listpndSmpls.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, sEmp_Code);
        }
        return listpndSmpls;
    }

    public static List<SampleDetails> getLabSampleDetails(String sEmp_Code, String sStatus, String pid, String cid, String did, String aid) {
        List<SampleDetails> listpndSmpls = new ArrayList();
        String query = "SELECT t1.SAMPLE_ID,t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                + "t5.APPL_NAME, t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.STATUS_ID, t1.QTY_DRAWN, "
                + "t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE,t1.LAB_RECEIVED_DATE, t1.SAMPLE_PRIORITY_ID,t1.sample_type, t1.LAB_LOC_CODE FROM SAMPLE_DETAILS t1, "
                + "MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, "
                + "MST_PRODUCT t7, mst_tank t8 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID "
                + "AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.PROD_ID=t7.PROD_ID and "
                + "t1.tank_id = t8.tank_id AND t1.LAB_LOC_CODE in(select LAB_LOC_CODE FROM MST_LAB where EMP_CODE=?) AND "
                + "t1.STATUS_ID IN (?) ";
        query += (!pid.equals("") ? "AND t1.PROD_ID = " + pid + " " : "");
        query += (!cid.equals("") ? "AND t1.CUST_ID = " + cid + " " : "");
        query += (!did.equals("") ? "AND t1.DEPT_ID = " + did + " " : "");
        query += (!aid.equals("") ? "AND t1.APPL_ID = " + aid + " " : "");
        query += "order by t1.SAMPLE_PRIORITY_ID";
        System.out.println(query);
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, sEmp_Code);
            pst.setString(2, sStatus);
//            pst.setString(3, pid);
//            pst.setString(4, cid);
//            pst.setString(5, did);
//            pst.setString(6, aid);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(4));
                sd.getMstDept().setDepartmentName(res.getString(5));
                sd.getMstApp().setAppName(res.getString(6));
                sd.setTankNo(res.getString(7));
                sd.getMstEquip().setEquipmentName(res.getString(8));
                sd.getMstProd().setProName(res.getString(9));
                sd.setStatusId(res.getString(10));
                sd.setQtyDrawn(res.getString(11));
                sd.setSamplecreatedBy(res.getString(12));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(13)));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                if (res.getDate(14) != null) {
                    sd.setLabrecDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14)));
                    sd.setStringLabrecDate(ApplicationSQLDate.convertUtilDatetoString(sd.getLabrecDate()));
                }
                sd.setSamplepriorityId(res.getString(15));
                sd.setIsSingleSampling(res.getString(16));
                sd.getMstLab().setLabCode(res.getString(17));
                listpndSmpls.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, sEmp_Code);
        }
        return listpndSmpls;
    }

    public static List getsampleTestParameters(String sampleId, String labCode) {
        List mstTestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.TEST_ID,t2.TEST_NAME,t2.UNIT,t2.TEST_METHOD,t2.SAMPLE_QTY FROM TEST_RESULT_DETAILS t1,"
                        + "MST_TEST t2 WHERE t1.TEST_ID=t2.TEST_ID and t1.SAMPLE_ID = ? and t1.lab_code = ?");) {
            pst.setString(1, sampleId);
            pst.setString(2, labCode);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTest mTest = new MstTest();
                mTest.setTestId(res.getString(1));
                mTest.setTestName(res.getString(2));
                mTest.setUnit(res.getString(3));
                mTest.setTestMethod(res.getString(4));
                mTest.setSampleqty(res.getString(5));
                mstTestList.add(mTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABoperations.getsampleTestParameters() ");
        }
        return mstTestList;
    }

    public static List getsampleTestResults(String sampleId, String labCode) {
        List mstTestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.TEST_ID,t2.TEST_NAME,t2.UNIT,t2.TEST_METHOD,t1.TEST_VALUE,t1.TEST_REMARKS FROM "
                        + "TEST_RESULT_DETAILS t1,MST_TEST t2 WHERE t1.TEST_ID=t2.TEST_ID and SAMPLE_ID = ? AND LAB_CODE = ?");) {
            pst.setString(1, sampleId);
            pst.setString(2, labCode);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTest mTest = new MstTest();
                mTest.setTestId(res.getString(1));
                mTest.setTestName(res.getString(2));
                mTest.setUnit(res.getString(3));
                mTest.setTestMethod(res.getString(4));
                mTest.setTestVal(res.getString(5));
                mTest.setTestRemarks(res.getString(6));
                mstTestList.add(mTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABoperations.getsampleTestResults() ");
        }
        return mstTestList;
    }

    public static List getIndustryWiseLABSummary(String EMP_CODE, HttpServletRequest request) {
        List industrylst = new ArrayList();
        HttpSession session = request.getSession();
        int totalCount = 0;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.IND_ID,t2.IND_NAME,count(*) from SAMPLE_DETAILS t1,MST_INDUSTRY t2 WHERE t1.IND_ID=t2.IND_ID and "
                        + "t1.STATUS_ID=1 and t1.LAB_LOC_CODE in(select LAB_LOC_CODE FROM MST_LAB where EMP_CODE=?) group by "
                        + "t1.IND_ID,t2.IND_NAME ");) {
            pst.setString(1, EMP_CODE);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstIndustry mstInd = new MstIndustry();
                mstInd.setIndName(res.getString(2));
                mstInd.setIndCount(res.getString(3));
                totalCount += Integer.parseInt(res.getString(3));
                industrylst.add(mstInd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        session.setAttribute("notiCount", totalCount);
        return industrylst;
    }

    public static MessageDetails receiveSample(SampleDetails sd) {
        MessageDetails md = new MessageDetails();
        int updateStatus = 0;
        String query = "UPDATE SAMPLE_DETAILS SET STATUS_ID='2', LAB_RECEIVED_DATE=to_date(?,'dd-mm-rr'), LAB_EQUIP_ID=?, LAB_RECEIVED_REMARKS = ?,"
                + " EXP_TEST_RESULT_DATE=to_date(?,'dd-mm-rr'), RESULT_DATE_EXTEND_REASON = ? WHERE SAMPLE_ID=? AND LAB_LOC_CODE = ?";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, sd.getStringLabrecDate());
            pst.setString(2, sd.getMstEquip().getEquipmentId());
            pst.setString(3, sd.getLabrecRemarks());
            pst.setString(4, sd.getStringExptestresultDate());
            pst.setString(5, sd.getResultdateextendReason());
            pst.setString(6, sd.getSampleId());
            pst.setString(7, sd.getMstLab().getLabCode());
            updateStatus = pst.executeUpdate();
            if (updateStatus > 0) {
                md.setModalTitle("Sample Receive Status");
                md.setModalMessage("Sample ID: " + sd.getSampleId() + " Received For Testing Successfully.");
                md.setMsgClass("text-success");
            } else {
                md.setModalMessage("No Sample were received for Testing OR Sample Already Received.");
                md.setMsgClass("text-danger");
            }
        } catch (Exception ex) {
            md.setModalMessage("Error Code: " + ex.getMessage());
            md.setMsgClass("text-danger");
            MyLogger.logIt(ex, sd.getSamplecreatedBy());
        }
        return md;
    }

    public static SampleDetails getreceiveSample(String sampleId,String labCode) {
        SampleDetails sd = new SampleDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t8.APPL_NAME, "
                        + "t9.EQUIP_NAME, t1.PROD_ID, t4.PROD_NAME, t1.QTY_DRAWN, t1.SAMPLE_CREATED_BY, "
                        + "t1.SAMPLE_CREATED_DATE, t1.PREV_SAMPLE_DATE, t1.NEXT_SAMPLE_DATE, t1.TOP_UP_QTY, "
                        + "t1.LAB_LOC_CODE, t7.PRIORITY_ID, t7.PRIORITY_NAME, t1.SAMPLE_DRAWN_REMARKS, t1.SAMPLE_PRIORITY_REMARKS "
                        + "from SAMPLE_DETAILS t1,MST_INDUSTRY t2, MST_CUSTOMER t3, MST_PRODUCT t4, MST_PRIORITY t7, "
                        + "MST_APPLICATION t8, MST_EQUIPMENT t9 WHERE t1.IND_ID=t2.IND_ID and t1.CUST_ID=t3.CUST_ID and "
                        + "t1.PROD_ID=t4.PROD_ID and t1.SAMPLE_PRIORITY_ID=t7.PRIORITY_ID AND t1.APPL_ID=T8.APPL_ID AND "
                        + "T1.EQUIP_ID=T9.EQUIP_ID and t1.SAMPLE_ID=? AND t1.lab_loc_code = ?");) {
            pst.setString(1, sampleId);
            pst.setString(2, labCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(4));
                sd.getMstApp().setAppName(res.getString(5));
                sd.getMstEquip().setEquipmentName(res.getString(6));
                sd.getMstProd().setProId(res.getString(7));
                sd.getMstProd().setProName(res.getString(8));
                sd.setQtyDrawn(res.getString(9));
                sd.setSamplecreatedBy(res.getString(10));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(11)));
                sd.setPresampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(12)));
                sd.setNxtsampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(13)));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                sd.setStringnxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(sd.getNxtsampleDate()));
                sd.setStringpresampleDate(ApplicationSQLDate.convertUtilDatetoString(sd.getPresampleDate()));
                sd.setTopupQty(res.getString(14));
                sd.getMstLab().setLabCode(res.getString(15));
                sd.setSamplepriorityId(res.getString(16));
                sd.setSamplepriorityName(res.getString(17));
                sd.setSampledrawnRemarks(res.getString(18));
                sd.setSamplepriorityRemarks(res.getString(19));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getreceiveSample() ");
        }
        return sd;
    }

    //Update:: Added tank ID as a parameter
    public static List getCustSampleIdNoWiseTestResult(String CustSampleInfoId, String prevToPrevSampleNo, String prevSamplingNo, String curSamplingNo, String prodId,String sampleType) {
        List mstTestList = new ArrayList();
        //                ResultSet res = st.executeQuery("SELECT t.SAMPLING_NO, t.TEST_ID, t.TEST_NAME, t.UNIT, t.TEST_METHOD, t.TEST_VALUE, "
        //                        + "t.TEST_REMARKS, t3.VALUE_CHECK_ID, t3.MIN_VAL, t3.MAX_VAL,t3.TYPICAL_VAL,t3.DEVIATION, t3.oTHER_VAL,t.DISP_SEQ_NO FROM (select "
        //                        + "t1.SAMPLING_NO, t1.TEST_ID, t2.TEST_NAME,t2.DISP_SEQ_NO, t2.UNIT, t2.TEST_METHOD, t1.TEST_VALUE, "
        //                        + "t1.TEST_REMARKS from TEST_RESULT_DETAILS t1,MST_TEST t2 where T1.TEST_ID=T2.TEST_ID AND "
        //                        + " t1.TANK_ID='" + CustSampleInfoId + "' and t1.sampling_no in ('" + prevSamplingNo
        //                        + "','" + prevToPrevSampleNo + "','" + curSamplingNo + "') order by t1.test_id asc) T, "
        //                        + "MST_MKT_TEST_VAL_SPEC t3 WHERE T.TEST_ID=T3.TEST_ID AND T3.PROD_ID='" + prodId + "'");
        String andQuery = "";
        if(sampleType.equals("CMP")){
            andQuery = "AND SAMPLING_NO_CMP IN (?,?,?)";
        }else{
            andQuery = "AND SAMPLING_NO_OTS IN (?,?,?)";
        }
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.SAMPLING_NO, T1.TEST_ID,T3.TEST_NAME,T3.UNIT,T3.TEST_METHOD,T1.TEST_VALUE, "
                        + "T1.TEST_REMARKS,T2.VALUE_CHECK_ID,T2.MIN_VAL,T2.MAX_VAL,T2.TYPICAL_VAL,T2.DEVIATION,T2.OTHER_VAL,T3.DISP_SEQ_NO "
                        + "FROM ( SELECT * FROM TEST_RESULT_DETAILS WHERE TANK_ID=? "+andQuery+" ) T1 LEFT OUTER JOIN "
                        + "( SELECT * FROM mst_mkt_test_val_spec WHERE PROD_ID = ? ) T2 ON T1.TEST_ID=T2.TEST_ID "
                        + "INNER JOIN MST_TEST T3 ON T1.TEST_ID=T3.TEST_ID "
                        + "order by SAMPLING_NO asc");) {
            pst.setString(1, CustSampleInfoId);
            pst.setString(2, prevToPrevSampleNo);
            pst.setString(3, prevSamplingNo);
            pst.setString(4, curSamplingNo);
            pst.setString(5, prodId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTest mTest = new MstTest();
                mTest.getMstTestParam().setSamplingNo(res.getString(1));
                mTest.setTestId(res.getString(2));
                mTest.setTestName(res.getString(3));
                mTest.setUnit(res.getString(4));
                mTest.setTestMethod(res.getString(5));
                mTest.setTestVal(res.getString(6));
                mTest.setTestRemarks(res.getString(7));
                mTest.getMstTestParam().setCheckId(res.getString(8));
                mTest.getMstTestParam().setMinValue(res.getString(9));
                mTest.getMstTestParam().setMaxValue(res.getString(10));
                mTest.getMstTestParam().setTypValue(res.getString(11));
                mTest.getMstTestParam().setDevValue(res.getString(12));
                mTest.getMstTestParam().setOtherVal(res.getString(13));
                mTest.setDispSeqNo(Integer.parseInt(res.getString(14)));
                if (res.getString(8) != null) {
                    switch (res.getString(8)) {
                        case "0":
                            mTest.setSpec("No Validation.");
                            break;
                        case "1":
                            mTest.setSpec(" >=  " + res.getString(9));
                            break;
                        case "2":
                            mTest.setSpec(" <= " + res.getString(10));
                            break;
                        case "3":
                            mTest.setSpec(res.getString(9) + " <= x  <= " + res.getString(10));
                            break;
                        case "4":
                            mTest.setSpec("= " + res.getString(11) + " (" + res.getString(12) + "%)");
                            break;
                        case "5":
                            mTest.setSpec("");
                            break;
                        case "6":
                            mTest.setSpec(" <= " + res.getString(10));
                            break;
                        default:
                            mTest.setSpec("Not Defined.");
                            break;
                    }
                } else {
                    mTest.setSpec("<span class='text-danger' style='font-size:24px;margin-left: 10%;' title='No Specification found'>*</span>");
                }
                mstTestList.add(mTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getCustSampleIdNoWiseTestResult() ");
        }
        return mstTestList;
    }

    public static List getCustSampleIdNoWiseTestResultDate(String CustSampleInfoId, String prevToPrevSampleNo, String prevSamplingNo, String curSamplingNo, String sampleType) {
        List mstTestParamList = new ArrayList();
        String andQuery = "";
        if(sampleType.equals("CMP")){
            andQuery = "AND SAMPLING_NO_CMP IN (?,?,?)";
        }else{
            andQuery = "AND SAMPLING_NO_OTS IN (?,?,?)";
        }
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT UNIQUE(SAMPLING_NO),TEST_RESULT_ENTERED_DATE FROM SAMPLE_DETAILS WHERE TANK_ID = ? "
                        + " "+andQuery+" order by SAMPLING_NO ASC");) {
            pst.setString(1, CustSampleInfoId);
            pst.setString(2, prevSamplingNo);
            pst.setString(3, prevToPrevSampleNo);
            pst.setString(4, curSamplingNo);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTestParameter mTestParam = new MstTestParameter();
                mTestParam.setSamplingNo(res.getString(1));
                mTestParam.setTestresultenteredDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(2)));
                mTestParam.setStrResultDate(ApplicationSQLDate.convertUtilDatetoString(mTestParam.getTestresultenteredDate()));
                mstTestParamList.add(mTestParam);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getCustSampleIdNoWiseTestResultDate() ");
        }
        return mstTestParamList;
    }

    public static List<MstTest> getTestParameterWiseResultHistory(String CustSampleInfoId, String testId, int minSmplNo, int maxSmplNo) {
        List<MstTest> mstTestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT SAMPLING_NO,TEST_VALUE,ACTUAL_TEST_RESULT_DATE FROM test_result_details where SAMPLING_NO >= ? "
                        + "and SAMPLING_NO <= ? and TANK_ID = ? and TEST_ID = ? order by SAMPLING_NO ASC");) {
            pst.setInt(1, minSmplNo);
            pst.setInt(2, maxSmplNo);
            pst.setString(3, CustSampleInfoId);
            pst.setString(4, testId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTest mstTest = new MstTest();
                mstTest.getMstTestParam().setSamplingNo(res.getString(1));
                if(res.getString(2).indexOf("/")==-1){
                    String temp = (String)res.getString(2).split("/")[0];
                    mstTest.setTestVal(temp);
                }else{
                    mstTest.setTestVal(res.getString(2));
                }
                mstTest.getMstTestParam().setTestresultenteredDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(3)));
                mstTest.getMstTestParam().setStrResultDate(ApplicationSQLDate.convertUtilDatetoString(mstTest.getMstTestParam().getTestresultenteredDate()));
                mstTestList.add(mstTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getTestParameterWiseResultHistory() ");
        }
        return mstTestList;
    }

    public static List getotherTestVal(String testId) {
        List othertstVal = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select * from MST_OTHER_TEST_VALUE where TEST_ID = ?");) {
            pst.setString(1, testId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                othertstVal.add(res.getString(2));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getotherTestVal() ");
        }
        return othertstVal;
    }

    public static List<MstTest> getmprodTest(String SampleID, List<MstTest> liExistTst, String proId, String labCode) {
        List<MstTest> mstTestList = new ArrayList();
//        String str="select t1.PROD_ID,t1.TEST_ID,t1.VALUE_CHECK_ID,t1.MIN_VAL,t1.MAX_VAL,t1.TYPICAL_VAL,t1.DEVIATION,t1.OTHER_VAL,t2.TEST_METHOD, t2.TEST_NAME, t2.UNIT from MST_LAB_TEST_VAL_SPEC t1  "
//                        + "right outer join MST_TEST t2 on t1.TEST_ID=t2.TEST_ID AND t1.PROD_ID =" + proId + " and t1.TEST_ID in(" + CollectionUtils.collect(
//                                liExistTst, new BeanToPropertyValueTransformer("testId")).toString().replace("[", "").replace("]", "") + ") order"
//                        + " by t2.DISP_SEQ_NO ASC";
        //                ResultSet res = st.executeQuery("select t1.PROD_ID,t1.TEST_ID,t1.VALUE_CHECK_ID,t1.MIN_VAL,t1.MAX_VAL,t1.TYPICAL_VAL,t1.DEVIATION,t1.OTHER_VAL,t2.TEST_METHOD, t2.TEST_NAME, t2.UNIT from MST_LAB_TEST_VAL_SPEC t1  "
        //                        + "inner join MST_TEST t2 on t1.TEST_ID=t2.TEST_ID AND t1.PROD_ID =" + proId + " and t1.TEST_ID in(" + CollectionUtils.collect(
        //                                liExistTst, new BeanToPropertyValueTransformer("testId")).toString().replace("[", "").replace("]", "") + ") order"
        //                        + " by t2.DISP_SEQ_NO ASC");
        String str2 = "SELECT T1.prod_ID,T1.TEST_ID,T2.VALUE_CHECK_ID,T2.MIN_VAL,T2.MAX_VAL,T2.TYPICAL_VAL,T2.DEVIATION,T2.OTHER_VAL,T3.TEST_METHOD,T3.TEST_NAME,T3.UNIT "
                + "FROM ( SELECT * FROM TEST_RESULT_DETAILS WHERE SAMPLE_ID=? AND LAB_CODE = ?) T1 LEFT OUTER JOIN "
                + "( SELECT * FROM MST_LAB_TEST_VAL_SPEC WHERE PROD_ID=? ) T2 ON T1.TEST_ID=T2.TEST_ID  "
                + "INNER JOIN MST_TEST T3 ON T1.TEST_ID=T3.TEST_ID "
                + "ORDER by T3.DISP_SEQ_NO ASC";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(str2);) {
            pst.setString(1, SampleID);
            pst.setString(2, labCode);
            pst.setString(3, proId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTest mstTest = new MstTest();
                mstTest.setTestId(res.getString(2));
                mstTest.getMstTestParam().setCheckId(res.getString(3));
                mstTest.getMstTestParam().setMinValue(res.getString(4));
                mstTest.getMstTestParam().setMaxValue(res.getString(5));
                mstTest.getMstTestParam().setTypValue(res.getString(6));
                mstTest.getMstTestParam().setDevValue(res.getString(7));
                mstTest.getMstTestParam().setOtherVal(res.getString(8));
                mstTest.setTestMethod(res.getString(9));
                mstTest.setTestName(res.getString(10));
                mstTest.setUnit(res.getString(11));
                if (res.getString(3) != null) {
                    switch (res.getString(3)) {
                        case "0":
                            mstTest.setSpec("No Validation.");
                            break;
                        case "1":
                            mstTest.setSpec(" >=  " + res.getString(4));
                            break;
                        case "2":
                            mstTest.setSpec(" <= " + res.getString(5));
                            break;
                        case "3":
                            mstTest.setSpec(res.getString(4) + " <= x  <= " + res.getString(5));
                            break;
                        case "4":
                            mstTest.setSpec("= " + res.getString(6) + " (" + res.getString(7) + "%)");
                            break;
                        case "5":
                            mstTest.setSpec("");
                            break;
                        case "6":
                            mstTest.setSpec(" <= " + res.getString(5));
                            break;
                        default:
                            mstTest.setSpec("Not Defined.");
                            break;
                    }
                } else {
                    mstTest.setSpec("<span class='text-danger' style='font-size:24px;margin-left: 10%;' title='No Specification found'>*</span>");
                }
                mstTestList.add(mstTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getmprodTest() ");
        }
        return mstTestList;
    }

    public static List<MstTest> getmstProdTestMinMax(String proId, String testIds, List<TestResultIds> mstTestIdsList) {
        List<MstTest> mstTestList = new ArrayList();
        String sql = "select t1.TEST_ID,t1.VALUE_CHECK_ID,t1.MIN_VAL,t1.MAX_VAL,t2.test_name from MST_MKT_TEST_VAL_SPEC t1 inner join mst_test t2 on t1.test_id = t2.test_id "
                + " WHERE t1.PROD_ID =? and t1.TEST_ID in(" + testIds + ") ORDER BY t1.TEST_ID";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(sql);) {
            pst.setString(1, proId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTest mstTest = new MstTest();
                mstTest.setTestId(res.getString(1));
                mstTest.getMstTestParam().setCheckId(res.getString(2));
                mstTest.getMstTestParam().setMinValue(res.getString(3));
                if (res.getString(4) != null) {
                    if (((String) res.getString(4)).indexOf("/") == -1) {
                        mstTest.getMstTestParam().setMaxValue(res.getString(4));
                    } else {
                        mstTest.getMstTestParam().setMaxValue("1");
                    }
                }
                mstTest.setTestName(res.getString(5));
                mstTestList.add(mstTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getmstProdTestMinMax() ");
        }
        return mstTestList;
    }

    public static List getEquipments() {
        List equipList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("select LAB_EQUIP_ID, LAB_EQUIP_NAME from MST_LAB_EQUIPMENT");) {
            while (res.next()) {
                MstEquipment e = new MstEquipment();
                e.setEquipmentId(res.getString(1));
                e.setEquipmentName(res.getString(2));
                equipList.add(e);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getEquipments() ");
        }
        return equipList;
    }

    public static List getLabs() {
        List equipList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("select LAB_LOC_CODE, LAB_NAME from MST_LAB");) {
            while (res.next()) {
                MstEquipment e = new MstEquipment();
                e.setEquipmentId(res.getString(1));
                e.setEquipmentName(res.getString(2));
                equipList.add(e);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getEquipments() ");
        }
        return equipList;
    }

    public static List getEquipmentsByLabCode(int labCode) {
        List equipList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select LAB_EQUIP_NAME, OPERATIONAL_STATUS,REMARKS, updated_by, updated_datetime  from MST_LAB_EQUIPMENT where LAB_LOC_CODE = ?");) {
            pst.setInt(1, labCode);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstLabEquipment e = new MstLabEquipment();
                e.setLabEquipName(res.getString(1));
                e.setOperationalStatus(res.getString(2));
                e.setRemarks(res.getString(3));
                e.setUpdatedBy(res.getString(4));
                e.setUpdatedDatetime(res.getString(5));
                equipList.add(e);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getEquipments() ");
        }
        return equipList;
    }

    public static List getNWEquipments(String empCode) {//list of all Non working Equipments
        List equipList = new ArrayList();
        
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select LAB_EQUIP_ID, LAB_EQUIP_NAME from MST_LAB_EQUIPMENT where operational_status = 'NO' and lab_loc_code = (select mst_lab.lab_loc_code from mst_lab where mst_lab.emp_code = ?) and active = 1");) {
                pst.setString(1, empCode);
                ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstEquipment e = new MstEquipment();
                e.setEquipmentId(res.getString(1));
                e.setEquipmentName(res.getString(2));
                equipList.add(e);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getEquipments() ");
        }
        return equipList;
    }
}
