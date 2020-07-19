package operations;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import viewModel.MessageDetails;
import viewModel.MstIndustry;
import viewModel.MstLab;
import viewModel.MstPriority;
import viewModel.MstResultDelayReason;
import viewModel.MstRole;
import viewModel.MstTank;
import viewModel.MstTest;
import viewModel.MstUser;
import viewModel.SampleDetails;
import viewModel.SamplePostponedHistory;

public class SharedOperations {

    public static HashMap<String, String> getSenderInfo(String empCode, String sCemDBPath) {

        String query = "select EMP_NAME, DESIGN_SHORT_DESC, CURR_COMP from COM_EMP_DB.EMP_DB where EMP_CODE=?";
        HashMap<String, String> hs = new HashMap<>();
        try (Connection con = DatabaseConnectionFactory.createCEMConnection(sCemDBPath);
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, empCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                System.err.println(res.getString(1) + "-" + res.getString(2) + "-" + res.getString(3));
                hs.put("fromName", res.getString(1));
                hs.put("fromDesign", res.getString(2));
                hs.put("fromComp", res.getString(3));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getCustSample() ");
        }
        return hs;
    }

    public static List getCustSample() {
        List<MstTank> custSampleInfo = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("select t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, t5.APPL_NAME, "
                        + "t6.EQUIP_NAME, TANK_NO, t7.PROD_NAME, t1.CAPACITY, t1.SAMPLE_FREQ, t1.PREV_SAMPLE_DATE, t1.NEXT_SAMPLE_DATE, "
                        + "t1.OLD_NEXT_SAMPLE_DATE from MST_TANK t1, MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION "
                        + "t5, MST_EQUIPMENT t6, MST_PRODUCT t7 where t1.IND_ID = t2.IND_ID and t1.CUST_ID = t3.CUST_ID and t1.DEPT_ID = t4.DEPT_ID "
                        + "and t1.APPL_ID = t5.APPL_ID and t1.EQUIP_ID = t6.EQUIP_ID and t1.PROD_ID = t7.PROD_ID")) {
            while (res.next()) {
                MstTank mstTank = new MstTank();
                mstTank.setTankId(res.getString(1));
                mstTank.setIndName(res.getString(2));
                mstTank.setCustName(res.getString(3));
                mstTank.setDeptName(res.getString(4));
                mstTank.setApplName(res.getString(5));
                mstTank.setEquipName(res.getString(6));
                mstTank.setTankNo(res.getString(7));
                mstTank.setProName(res.getString(8));
                mstTank.setCapacity(res.getString(9));
                mstTank.setSampleFreq(res.getString(10));
                mstTank.setNxtSampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(11)));
                mstTank.setPrevSampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(12)));
                mstTank.setStrNxtDate(ApplicationSQLDate.convertUtilDatetoString(mstTank.getNxtSampleDate()));
                mstTank.setStrPrevDate(ApplicationSQLDate.convertUtilDatetoString(mstTank.getPrevSampleDate()));
                custSampleInfo.add(mstTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getCustSample() ");
        }
        return custSampleInfo;
    }

    public static MessageDetails addSample(MstTank mstTank) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_TANK (TANK_ID, SAMPLING_NO, IND_ID, CUST_ID, "
                        + "DEPT_ID, APPL_ID, EQUIP_ID, TANK_NO, PROD_ID, CAPACITY, SAMPLE_FREQ, NEXT_SAMPLE_DATE, POSTPONE_COUNT, APPL_DESC) "
                        + "VALUES ((select MAX(TO_NUMBER(REPLACE(TANK_ID,'SYS',''))+1) from MST_TANK),?,?,?,?,?,?,?,?,?,?,?,?,?)");) {
            pst.setString(1, mstTank.getSamplingNo());
            pst.setString(2, mstTank.getIndId());
            pst.setString(3, mstTank.getCustId());
            pst.setString(4, mstTank.getDeptId());
            pst.setString(5, mstTank.getApplId());
            pst.setString(6, mstTank.getEquipId());
            pst.setString(7, mstTank.getTankNo());
            pst.setString(8, mstTank.getProId());
            pst.setString(9, mstTank.getCapacity());
            pst.setString(10, mstTank.getSampleFreq());
            pst.setDate(11, ApplicationSQLDate.convertUtiltoSqlDate(mstTank.getNxtSampleDate()));
            pst.setString(12, mstTank.getPostponeCount());
            pst.setString(13, mstTank.getApplDesc());
            if (pst.executeUpdate() > 0) {
                md.setStatus(true);
            }
        } catch (Exception ex) {
            md.setStatus(false);
            md.setMsgClass("text-danger");
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage("This Sample already Exist.");
            }
            MyLogger.logIt(ex, "SharedOperations.addSample() ");
        }
        return md;
    }

    public static void downloadUploadedTestDoc(String sampleId, String fileName, String Query, OutputStream out) {
        InputStream is = null;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(Query);) {
            if (res.next()) {
                is = res.getBinaryStream(1);
            }
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.downloadUploadedTestDoc() ");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                MyLogger.logIt(ex, "SharedOperations.downloadUploadedTestDoc()2 ");
            }
        }
    }

    public static List getcustomerDetails(String EMP_CODE, String strNotifyDaysLimit) {
        List cstdet = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, t5.APPL_NAME,"
                        + " t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.CAPACITY, t1.SAMPLE_FREQ, "
                        + " t1.PREV_SAMPLE_DATE, t1.NEXT_SAMPLE_DATE FROM MST_TANK t1, MST_INDUSTRY t2,"
                        + " MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6,"
                        + " MST_PRODUCT t7 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID"
                        + " AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.PROD_ID=t7.PROD_ID and"
                        + " t1.CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE = ?)"
                        + " AND (t1.NEXT_SAMPLE_DATE-SYSDATE)<= ?");) {
            pst.setString(1, EMP_CODE);
            pst.setString(2, strNotifyDaysLimit);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.getMstInd().setIndName(res.getString(2));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(3));
                sd.getMstDept().setDepartmentName(res.getString(4));
                sd.getMstApp().setAppName(res.getString(5));
                sd.setTankNo(res.getString(6));
                sd.getMstEquip().setEquipmentName(res.getString(7));
                sd.getMstProd().setProName(res.getString(8));
                sd.setSampleFreq(res.getString(10));
                sd.setStringpresampleDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(11)));
                sd.setStringnxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(12)));
                cstdet.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        return cstdet;
    }

    public static List getLabdetails() {
        List mstlabList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT T1.LAB_LOC_CODE, T1.LAB_NAME, T1.EMP_CODE, T2.EMP_NAME FROM MST_LAB T1 INNER JOIN MST_USER T2 ON T1.EMP_CODE=T2.EMP_CODE");) {
            while (res.next()) {
                MstLab mstLbt = new MstLab();
                mstLbt.setLabCode(res.getString(1));
                mstLbt.setLabName(res.getString(2));
                mstLbt.setLabEmpCode(res.getString(3));
                mstLbt.setLabAuthority(res.getString(4));
//                mstLbt.setLabTseCode(res.getString(5));
                mstlabList.add(mstLbt);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getLabdetails() ");
        }
        return mstlabList;
    }

    public static MstLab getLabdetails(String labLocCode) {
        MstLab mstLbt = null;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.LAB_LOC_CODE, T1.LAB_NAME, T1.EMP_CODE, T2.EMP_NAME, T4.EMP_CODE FROM MST_LAB T1 INNER JOIN MST_USER T2 ON T1.EMP_CODE=T2.EMP_CODE LEFT JOIN LAB_TSE_MAPPED T4 ON T1.LAB_LOC_CODE = T4.LAB_CODE WHERE T1.LAB_LOC_CODE= ?");) {
            pst.setString(1, labLocCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                mstLbt = new MstLab();
                mstLbt.setLabCode(res.getString(1));
                mstLbt.setLabName(res.getString(2));
                mstLbt.setLabEmpCode(res.getString(3));
                mstLbt.setLabAuthority(res.getString(4));
                mstLbt.setLabTseCode(res.getString(5));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getLabdetails(String labLocCode) ");
        }
        return mstLbt;
    }

    public static List getLabdetailsEmpCodeWise(String empCode) {
        List mstlabList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.LAB_LOC_CODE, T1.LAB_NAME, T1.EMP_CODE, T2.EMP_NAME FROM MST_LAB T1 INNER JOIN MST_USER T2 ON T1.EMP_CODE=T2.EMP_CODE AND T1.EMP_CODE = ?");) {
            pst.setString(1, empCode);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstLab mstLbt = new MstLab();
                mstLbt.setLabCode(res.getString(1));
                mstLbt.setLabName(res.getString(2));
                mstLbt.setLabEmpCode(res.getString(3));
                mstLbt.setLabAuthority(res.getString(4));
                mstlabList.add(mstLbt);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getLabdetails(String empCode) ");
        }
        return mstlabList;
    }

    public static List getLabdetailsTseEmpCodeWise(String empCode) {
        List mstlabList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.LAB_LOC_CODE, T1.LAB_NAME,T2.EMP_CODE, T2.EMP_NAME  FROM MST_LAB T1   \n" +
                        "INNER JOIN MST_USER T2 ON T1.EMP_CODE=T2.EMP_CODE " + 
                        "WHERE T1.LAB_LOC_CODE =( SELECT LAB_CODE FROM LAB_TSE_MAPPED WHERE EMP_CODE=?)");) {
            pst.setString(1, empCode);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstLab mstLbt = new MstLab();
                mstLbt.setLabCode(res.getString(1));
                mstLbt.setLabName(res.getString(2));
                mstLbt.setLabEmpCode(res.getString(3));
                mstLbt.setLabAuthority(res.getString(4));
                mstlabList.add(mstLbt);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getLabdetails() ");
        }
        return mstlabList;
    }

    public static List getPriorityMstdetails() {
        List mstpriorityList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT PRIORITY_ID, PRIORITY_NAME FROM MST_PRIORITY");) {
            while (res.next()) {
                MstPriority mstPriority = new MstPriority();
                mstPriority.setPriorityId(res.getString(1));
                mstPriority.setPriorityName(res.getString(2));
                mstpriorityList.add(mstPriority);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getPriorityMstdetails() ");
        }
        return mstpriorityList;
    }

    public static List getMstTestResultDelayReasons() {
        List mstResultDelayList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT DELAY_REASON_ID, DELAY_REASON_NAME FROM MST_RESULT_DELAY_REASON");) {
            while (res.next()) {
                MstResultDelayReason mstDelayReason = new MstResultDelayReason();
                mstDelayReason.setReasonId(res.getString(1));
                mstDelayReason.setReasonName(res.getString(2));
                mstResultDelayList.add(mstDelayReason);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.");
        }
        return mstResultDelayList;
    }

    public static List getIndustryMstdetails() {
        List mstIndList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT IND_ID, IND_NAME FROM MST_INDUSTRY");) {
            while (res.next()) {
                MstIndustry mstInd = new MstIndustry();
                mstInd.setIndId(res.getString(1));
                mstInd.setIndName(res.getString(2));
                mstIndList.add(mstInd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.");
        }
        return mstIndList;
    }

    public static List getRoleMstdetails() {
        List mstRoleList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT ROLE_ID, ROLE_NAME FROM MST_ROLE");) {
            while (res.next()) {
                MstRole mstRole = new MstRole();
                mstRole.setRoleId(res.getString(1));
                mstRole.setRoleName(res.getString(2));
                mstRoleList.add(mstRole);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.");
        }
        return mstRoleList;
    }

    public static List getTestParametersSampleIdwise(String smplid, String LabCode) {
        List mstTestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT TEST_ID FROM TEST_RESULT_DETAILS WHERE SAMPLE_ID = ? AND LAB_CODE = ?");) {
            pst.setString(1, smplid);
            pst.setString(2, LabCode);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTest mTest = new MstTest();
                mTest.setTestId(res.getString(1));
                mstTestList.add(mTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.");
        }
        return mstTestList;
    }

    public static List getOtherTestParametersSampleIdwise(String smplid, String LabCode) {
        List mstTestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT TEST_ID FROM TEST_RESULT_DETAILS WHERE SAMPLE_ID = ? AND LAB_CODE != ?");) {
            pst.setString(1, smplid);
            pst.setString(2, LabCode);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTest mTest = new MstTest();
                mTest.setTestId(res.getString(1));
                mstTestList.add(mTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.");
        }
        return mstTestList;
    }

    public static List getSampleWiseTestParamDetail(String smplId, String labCode) {
        List mstTestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pstmt = con.prepareStatement("SELECT t1.TEST_ID,t2.TEST_NAME from TEST_RESULT_DETAILS t1 inner join MST_TEST t2 on t1.TEST_ID=t2.TEST_ID WHERE SAMPLE_ID=? and LAB_CODE=?");) {
            pstmt.setString(1, smplId);
            pstmt.setString(2, labCode);
            ResultSet res = pstmt.executeQuery();
            while (res.next()) {
                MstTest mTest = new MstTest();
                mTest.setTestId(res.getString(1));
                mTest.setTestName(res.getString(2));
                mstTestList.add(mTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.");
        }
        return mstTestList;
    }

    public static List getPostponedSampleDetails(SamplePostponedHistory samplepostponed) {
        List samplePostponedList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select PREV_SAMPLE_DATE,OLD_NEXT_SAMPLE_DATE,NEW_NEXT_SAMPLE_DATE,REMARKS from SAMPLE_POSTPONED_HIST "
                        + " where TANK_ID = ? AND PREV_SAMPLE_DATE=to_date(?,'dd-mm-yyyy') order by new_next_sample_date");) {
            pst.setString(1, samplepostponed.getCustsampleinfoId());
            pst.setString(2, samplepostponed.getPrevsampleDate());
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SamplePostponedHistory samplePostponed = new SamplePostponedHistory();
                samplePostponed.setPrevsampleDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(1)));
                samplePostponed.setNxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(2)));
                samplePostponed.setPostponedDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(3)));
                samplePostponed.setRemarks(res.getString(4));
                samplePostponedList.add(samplePostponed);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getPostponedSampleDetails() ");
        }
        return samplePostponedList;
    }

    public static List getIomWiseSampleIds(String iomRefNO) {
        List sampleIds = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT SAMPLE_ID FROM SAMPLE_DETAILS WHERE IOM_REF_NO = ?")) {
            pst.setString(1, iomRefNO);

            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    String sampleId = res.getString(1);
                    sampleIds.add(sampleId);
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "SharedOperations.getIomWiseSampleIds() ");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getIomWiseSampleIds().Database() ");
        }
        return sampleIds;
    }

    public static List getAllTseOfficers() {
        List tseOfficer = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select emp_code, emp_name, EMP_EMAIL from MST_USER where role_id=1 and active = 1")) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstUser user = new MstUser();
                    user.setEmpCode(res.getString(1));
                    user.setEmpName(res.getString(2));
                    user.setEmpEmail(res.getString(3));
                    tseOfficer.add(user);
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "SharedOperations.getIomWiseSampleIds().Database() ");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getIomWiseSampleIds() While connecting");
        }
        return tseOfficer;
    }

    public static MstUser getTseOfficers(String empCode) {
        MstUser user = new MstUser();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select emp_code, emp_name, EMP_EMAIL from MST_USER where role_id=1 and emp_code = ? and active = 1")) {
            pst.setString(1, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    user.setEmpCode(res.getString(1));
                    user.setEmpName(res.getString(2));
                    user.setEmpEmail(res.getString(3));
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "SharedOperations.getIomWiseSampleIds().Database() ");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getIomWiseSampleIds() While connecting");
        }
        return user;
    }

    public static List getIndustryByTse(String empcode) {
        List indName = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select ind_id, ind_name from mst_industry where ind_id in (select distinct(ind_id) from mst_customer where emp_code = ?)")) {
            pst.setString(1, empcode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstIndustry ind = new MstIndustry();
                    ind.setIndId(res.getString(1));
                    ind.setIndName(res.getString(2));
                    indName.add(ind);
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "SharedOperations.getIomWiseSampleIds().Database() ");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getIomWiseSampleIds() While connecting");
        }
        return indName;
    }

    public static List getRNDLabdetailsEmpCodeWise(String prod_id) {
        List mstlabList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select l1.lab_loc_code, l1.lab_name, l1.emp_code, u1.emp_name from mst_lab l1 inner join mst_user u1 on l1.emp_code = u1.emp_code where type='RND' "
                        + "and lab_loc_code = (select DISTINCT(lab_code) from product_category where cat_ids = (select category from mst_product where prod_id = ?)) ");) {
            pst.setString(1, prod_id);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstLab mstLbt = new MstLab();
                mstLbt.setLabCode(res.getString(1));
                mstLbt.setLabName(res.getString(2));
                mstLbt.setLabEmpCode(res.getString(3));
                mstLbt.setLabAuthority(res.getString(4));
                mstlabList.add(mstLbt);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getLabdetails() ");
        }
        return mstlabList;
    }

    public static int getSampleCount(String SampleId) {
        int count = 0;
        String temp1 = "", temp2 = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select count(sample_id) from sample_details where sample_id = ?");) {
            pst.setString(1, SampleId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                count = res.getInt(1);
                if (count == 2) {
                    try (PreparedStatement pst1 = con.prepareStatement("Select STATUS_ID from sample_details where sample_id = ? ")) {
                        pst1.setString(1, SampleId);
                        ResultSet res1 = pst1.executeQuery();
                        int cnt = 1;
                        while (res1.next()) {
                            if (cnt == 1) {
                                temp1 = res1.getString(1);
                                cnt++;
                            } else if (cnt == 2) {
                                temp2 = res1.getString(1);
                                cnt++;
                            }
                        }
                        if (temp1.equals(temp2)) {
                            count = 1;
                        }
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "SharedOperations.getSampleCount() ");
                    }
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getSampleCount() ");
            count = -1;
        }
        return count;
    }

}
