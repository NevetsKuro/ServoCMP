package DAOs;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import viewModel.MessageDetails;
import viewModel.MstCategory;
import viewModel.MstTest;

public class MstTestDAO {

    public static List<MstTest> listAllTest(String query) {
        List<MstTest> mstTest = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mTest = new MstTest();
                    mTest.setTestId(res.getString(1));
                    mTest.setTestName(res.getString(2));
                    mTest.setUnit(res.getString(3));
                    mTest.setTestMethod(res.getString(4));
                    mTest.setSampleqty(res.getString(5));
                    mTest.setDispSeqNo(Integer.parseInt(res.getString(6)));
                    mTest.setUpdatedBy(res.getString(7));
                    mTest.setUpdatedDate(res.getString(8));
                    mstTest.add(mTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.listAllTest()");
        }
        return mstTest;
    }
    
        public static List<MstCategory> listAllCategory() {
        List<MstCategory> mstCat = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT cat_id, cat_name, updated_by, updated_datetime FROM MST_CATEGORY");) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstCategory cat = new MstCategory();
                    cat.setCat_id(res.getString(1));
                    cat.setCat_name(res.getString(2));
                    cat.setUpdated_by(res.getString(3));
                    cat.setUpdated_date(res.getString(4));
                    mstCat.add(cat);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.listAllTest()");
        }
        return mstCat;
    }

    public static MessageDetails addTest(MstTest mTest) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_TEST (TEST_ID, TEST_NAME, UNIT, TEST_METHOD, SAMPLE_QTY, DISP_SEQ_NO, ACTIVE, "
                        + "UPDATED_BY, UPDATED_DATETIME) VALUES((SELECT COALESCE(MAX(TO_NUMBER(REPLACE(TEST_ID, 'SYS', ''))),0) + 1 FROM MST_TEST), ?, ?, ?, ?, ?, ?, ?, "
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))")) {
            pst.setString(1, mTest.getTestName().toUpperCase());
            pst.setString(2, mTest.getUnit());
            pst.setString(3, mTest.getTestMethod());
            pst.setString(4, mTest.getSampleqty());
            pst.setInt(5, mTest.getDispSeqNo());
            pst.setString(6, mTest.getActive());
            pst.setString(7, mTest.getUpdatedBy());
            if (pst.executeUpdate() > 0) {
                md.setModalMessage("Test '" + mTest.getTestName() + "' added Successfully.");
                md.setMsgClass("text-success");
                md.setStatus(true);
            } else {
                md.setModalMessage("Failed to add Test.");
                md.setMsgClass("text-danger");
                md.setStatus(false);
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mTest.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Test '" + mTest.getTestName() + "' Already exist in Master");
            }
        }
        return md;
    }

    public static MessageDetails markInactive(MstTest mTest) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_TEST SET ACTIVE = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE TEST_ID = ?");) {
            pst.setString(1, mTest.getActive());
            pst.setString(2, mTest.getUpdatedBy());
            pst.setString(3, mTest.getTestId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mTest.getUpdatedBy());
        }
        return md;
    }

    public static MessageDetails UpdateTest(MstTest mTest) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_TEST SET TEST_NAME = ?, UNIT = ?, TEST_METHOD = ?, SAMPLE_QTY = ?, DISP_SEQ_NO = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE TEST_ID = ?")) {
            pst.setString(1, mTest.getTestName().toUpperCase());
            pst.setString(2, mTest.getUnit());
            pst.setString(3, mTest.getTestMethod());
            pst.setString(4, mTest.getSampleqty());
            pst.setInt(5, mTest.getDispSeqNo());
            pst.setString(6, mTest.getUpdatedBy());
            pst.setString(7, mTest.getTestId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mTest.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Test " + mTest.getTestName() + " Already Exist");
                md.setFilemsgClass("text-danger");
            }
        }
        return md;
    }

    public static MessageDetails saveSequence(Map<String, String> testPos) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_TEST SET DISP_SEQ_NO = ? WHERE TEST_NAME = ?");) {
            for (String testName : testPos.keySet()) {
                pst.setString(1, testPos.get(testName));
                pst.setString(2, testName);
                pst.executeUpdate();
            }
            md.setModalMessage("Test Position saved Successfully");
            md.setMsgClass("text-success");
        } catch (Exception ex) {
            md.setMsgClass("text-danger");
            md = MyLogger.logIt(ex, "MstTestDAO.saveSequence()");
        }
        return md;
    }

    public static List getexistingtestofSample(String prodId) {
        List mstTestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT TEST_IDS FROM PRODUCT_CATEGORY WHERE CAT_IDS=(SELECT CATEGORY FROM MST_PRODUCT WHERE PROD_ID=?)");) {
            pst.setString(1, prodId);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mTest = new MstTest();
                    mTest.setTestId(res.getString(1));
                    mstTestList.add(mTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.getexistingtestofSample()");
        }
        return mstTestList;
    }

    public static List getMissingTestParameters(String prodId) {
        List mstTestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT TEST_ID,TEST_NAME FROM MST_TEST WHERE TEST_ID IN ( SELECT TEST_ID FROM MST_TEST MINUS SELECT TEST_ID from MST_MKT_TEST_VAL_SPEC where PROD_ID=? )");) {
            pst.setString(1, prodId);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mTest = new MstTest();
                    mTest.setTestId(res.getString(1));
                    mTest.setTestName(res.getString(2));
                    mstTestList.add(mTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.getMissingTestParameters()");
        }
        return mstTestList;
    }

    public static List gettestMaster() {
        List msttestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT TEST_ID, TEST_NAME, UNIT, SAMPLE_QTY FROM MST_TEST order by TEST_ID");) {
            while (res.next()) {
                MstTest mstTest = new MstTest();
                mstTest.setTestId(res.getString(1));
                mstTest.setTestName(res.getString(2));
                mstTest.setUnit(res.getString(3));
                mstTest.setSampleqty(res.getString(4));
                switch (res.getString(2)) {
                    case "WEAR METAL":
                        break;
                    case "APPEARANCE":
                        break;
                    default:
                        mstTest.setSelected("selected");
                }
                msttestList.add(mstTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.gettestMaster() ");
        }
        return msttestList;
    }

    
    public static List getProdSpecWiseTestMasterWithStatus(String prodId,String emp_code) {
        List msttestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.TEST_ID,T1.TEST_NAME, T1.UNIT, T1.SAMPLE_QTY, T2.VALUE_CHECK_ID, " +
                " T1.active FROM ( SELECT TEST_ID,TEST_NAME, UNIT, SAMPLE_QTY, ACTIVE FROM MST_TEST ) T1 LEFT OUTER JOIN " +
                "( SELECT TEST_ID,VALUE_CHECK_ID FROM MST_MKT_TEST_VAL_SPEC WHERE PROD_ID= ? ) T2 ON T1.TEST_ID=T2.TEST_ID" +
                " ORDER BY T1.TEST_ID");) {
            pst.setString(1, prodId);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mstTest = new MstTest();
                    mstTest.setTestId(res.getString(1));
                    mstTest.setTestName(res.getString(2));
                    mstTest.setUnit(res.getString(3));
                    mstTest.setSampleqty(res.getString(4));
                    mstTest.getMstTestParam().setCheckId(res.getString(5));
                    mstTest.setActive(res.getString(6));
                    switch (res.getString(2)) {
                        case "WEAR METAL":
                            break;
                        case "APPEARANCE":
                            break;
                        default:
                            mstTest.setSelected("selected");
                    }
                    msttestList.add(mstTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.gettestMaster() ");
        }
        return msttestList;
    }
    
    public static List getProdSpecWiseTestMaster(String prodId) {
        List msttestList = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.TEST_ID,T1.TEST_NAME, T1.UNIT, T1.SAMPLE_QTY,T2.VALUE_CHECK_ID FROM ( SELECT TEST_ID,TEST_NAME, UNIT, SAMPLE_QTY FROM MST_TEST ) T1 LEFT OUTER JOIN "
                        + "( SELECT TEST_ID,VALUE_CHECK_ID FROM MST_MKT_TEST_VAL_SPEC WHERE PROD_ID= ? ) T2 ON T1.TEST_ID=T2.TEST_ID ORDER BY T1.TEST_ID");) {
            pst.setString(1, prodId);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mstTest = new MstTest();
                    mstTest.setTestId(res.getString(1));
                    mstTest.setTestName(res.getString(2));
                    mstTest.setUnit(res.getString(3));
                    mstTest.setSampleqty(res.getString(4));
                    mstTest.getMstTestParam().setCheckId(res.getString(5));
                    switch (res.getString(2)) {
                        case "WEAR METAL":
                            break;
                        case "APPEARANCE":
                            break;
                        default:
                            mstTest.setSelected("selected");
                    }
                    msttestList.add(mstTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.gettestMaster() ");
        }
        return msttestList;
    }

    public static List<MstTest> listOfOthTestsWithVals(String query) {
        List<MstTest> mstTest = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mTest = new MstTest();
                    mTest.setTestName(res.getString(1));
                    mTest.setUnit(res.getString(2));
                    mTest.setTestId(res.getString(3));
                    mstTest.add(mTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.listAllTest()");
        }
        return mstTest;
    }
    
     public static List<MstTest> listOfAllLabTestsByLab(String labCode) {
        List<MstTest> mstTest = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(" Select test_id, test_name, updated_by, active from mst_lab_test_status where lab_code = ? ");) {
            pst.setString(1, labCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mTest = new MstTest();
                    mTest.setTestId(res.getString(1));
                    mTest.setTestName(res.getString(2));
                    mTest.setUpdatedBy(res.getString(3));
                    mTest.setActive(res.getString(4));
                    mstTest.add(mTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.listAllTest()");
        }
        return mstTest;
    }

    public static String updateOVtest(String testId, String[] Ovalues) {
        List<MstTest> mstTest = new ArrayList<>();
//        String query2 = "update mst_mkt_test_val_spec set value_check_id='5', min_val=null ,max_val=null ,typical_val=null,deviation=null,other_val='YES' where test_id =" + testId;
        String query3 = "delete from MST_OTHER_TEST_VALUE where test_id=" + testId;
        String query4 = "";

        String info = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();) {
            PreparedStatement pst1, pst2, pst3, pst4;
            if (Integer.parseInt(testId) > 0) {
                pst1 = con.prepareStatement(query3);
                int i = pst1.executeUpdate();
                info += i + " rows deleted/n";
            }
            for (String Ovalue : Ovalues) {
                query4 = "Insert into MST_OTHER_TEST_VALUE values('" + testId + "','" + Ovalue + "')";
                pst2 = con.prepareStatement(query4);
                int u = 0;
                u = pst2.executeUpdate();
                info += u + " rows inserted/n";
            }
            info += Ovalues.length + " rows inserted";
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.listAllTest()");
        }
        return info;
    }

    public static String addOVtest(String testId, String[] Ovalues) {
        List<MstTest> mstTest = new ArrayList<>();

        String query4 = "";
        PreparedStatement pst;
        int i = 0;
        try (Connection con = DatabaseConnectionFactory.createConnection();) {
            for (String Ovalue : Ovalues) {
                query4 = "Insert into MST_OTHER_TEST_VALUE values('" + testId + "','" + Ovalue + "')";
                System.out.println(query4);
                pst = con.prepareStatement(query4);
                i += pst.executeUpdate();
            }

        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.listAllTest()");
        }
        return String.valueOf(i);
    }

    public static List<MstTest> getExistingOValues() {
        List<MstTest> mstTest = new ArrayList<>();
        String query = "Select other_val from mst_other_test_value group by other_val";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mTest = new MstTest();
                    mTest.setTestName(res.getString(1));
                    mstTest.add(mTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.listAllTest()");
        }
        return mstTest;
    }

    public static List<MstTest> getTestIDByLab(String LabCode) {
        List<MstTest> mstTest = new ArrayList<>();
        String query = "SELECT t1.TEST_ID, t1.TEST_NAME, t2.active FROM MST_TEST t1 inner join mst_lab_test_status t2 on t1.test_id = t2.test_id where t2.lab_code = ?";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, LabCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mTest = new MstTest();
                    mTest.setTestId(res.getString(1));
                    mTest.setTestName(res.getString(2));
                    mTest.setActive(res.getString(3));
                    mstTest.add(mTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTestDAO.listAllTest()");
        }
        return mstTest;
    }
}
