package DAOs;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import viewModel.MessageDetails;
import viewModel.MstTest;

public class MstProdTest {

    public static MessageDetails UpdateMktTestSpec(MstTest mTest) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_MKT_TEST_VAL_SPEC SET VALUE_CHECK_ID = ?, MIN_VAL = ?, MAX_VAL = ?, TYPICAL_VAL = ?, "
                        + "DEVIATION = ?, OTHER_VAL = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') "
                        + "WHERE TEST_ID = ? AND PROD_ID = ?")) {
            pst.setString(1, mTest.getMstTestParam().getCheckId());
            pst.setString(2, mTest.getMstTestParam().getMinValue());
            pst.setString(3, mTest.getMstTestParam().getMaxValue());
            pst.setString(4, mTest.getMstTestParam().getTypValue());
            pst.setString(5, mTest.getMstTestParam().getDevValue());
            pst.setString(6, mTest.getMstTestParam().getOtherVal());
            pst.setString(7, mTest.getUpdatedBy());
            pst.setString(8, mTest.getTestId());
            pst.setString(9, mTest.getProId());
            if (pst.executeUpdate() > 0) {
                md.setModalMessage("Test Specification Updated Successfully");
                md.setMsgClass("text-success");
            } else {
                md.setModalMessage("Failed to Update Test Specification. Try Again Later");
                md.setMsgClass("text-danger");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mTest.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage("Specification already Added. Kindly Edit Existing");
                md.setMsgClass("text-danger");
            }
        }
        return md;
    }

    public static MessageDetails addMktTestSpec(MstTest mTest) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_MKT_TEST_VAL_SPEC (PROD_ID, TEST_ID, VALUE_CHECK_ID, MIN_VAL, "
                        + "MAX_VAL, TYPICAL_VAL, DEVIATION, OTHER_VAL, UPDATED_BY, UPDATED_DATETIME) VALUES (?,?,?,?,?,?,?,?,?, TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))");) {
            pst.setString(1, mTest.getProId());
            pst.setString(2, mTest.getTestId());
            pst.setString(3, mTest.getMstTestParam().getCheckId());
            pst.setString(4, mTest.getMstTestParam().getMinValue());
            pst.setString(5, mTest.getMstTestParam().getMaxValue());
            pst.setString(6, mTest.getMstTestParam().getTypValue());
            pst.setString(7, mTest.getMstTestParam().getDevValue());
            pst.setString(8, mTest.getMstTestParam().getOtherVal());
            pst.setString(9, mTest.getUpdatedBy());
            if (pst.executeUpdate() > 0) {
                md.setModalMessage("Test Specification Added Successfully");
                md.setMsgClass("text-success");
            } else {
                md.setModalMessage("Failed to Add Test Specification. Try Again Later");
                md.setMsgClass("text-danger");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mTest.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage("Specification already Added");
                md.setMsgClass("text-danger");
            }
        }
        return md;
    }

    public static List<MstTest> getAllMktTest() {
        List<MstTest> mTestList = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT MST_MKT_TEST_VAL_SPEC.PROD_ID, MST_PRODUCT.PROD_NAME, MST_MKT_TEST_VAL_SPEC.TEST_ID, "
                        + "MST_TEST.TEST_NAME, MST_MKT_TEST_VAL_SPEC.VALUE_CHECK_ID, MST_MKT_TEST_VAL_SPEC.MIN_VAL, MST_MKT_TEST_VAL_SPEC.MAX_VAL, "
                        + "MST_MKT_TEST_VAL_SPEC.TYPICAL_VAL, MST_MKT_TEST_VAL_SPEC.DEVIATION, OTHER_VAL, MST_MKT_TEST_VAL_SPEC.UPDATED_BY, MST_MKT_TEST_VAL_SPEC.UPDATED_DATETIME FROM MST_MKT_TEST_VAL_SPEC INNER JOIN MST_PRODUCT "
                        + "ON MST_PRODUCT.PROD_ID = MST_MKT_TEST_VAL_SPEC.PROD_ID INNER JOIN MST_TEST ON MST_TEST.TEST_ID = MST_MKT_TEST_VAL_SPEC.TEST_ID");) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mstTest = new MstTest();
                    mstTest.setProId(res.getString(1));
                    mstTest.setProName(res.getString(2));
                    mstTest.setTestId(res.getString(3));
                    mstTest.setTestName(res.getString(4));
                    mstTest.getMstTestParam().setCheckId(res.getString(5));
                    mstTest.getMstTestParam().setMinValue(res.getString(6));
                    mstTest.getMstTestParam().setMaxValue(res.getString(7));
                    mstTest.getMstTestParam().setTypValue(res.getString(8));
                    mstTest.getMstTestParam().setDevValue(res.getString(9));
                    mstTest.getMstTestParam().setOtherVal(res.getString(10));
                    switch (res.getString(5)) {
                        case "0":
                            mstTest.setSpec("No Validation.");
                            break;
                        case "1":
                            mstTest.setSpec(" >=  " + res.getString(6));
                            break;
                        case "2":
                            mstTest.setSpec(" <= " + res.getString(7));
                            break;
                        case "3":
                            mstTest.setSpec(res.getString(6) + " <= x  <= " + res.getString(7));
                            break;
                        case "4":
                            mstTest.setSpec("= " + res.getString(8) + " (" + res.getString(9) + "%)");
                            break;
                        case "5":
                            mstTest.setSpec("");
                            break;
                        case "6":
                            mstTest.setSpec(" <= " + res.getString(7));
                            break;
                        default:
                            mstTest.setSpec("Not Defined.");
                            break;
                    }
                    mstTest.setUpdatedBy(res.getString(11));
                    mstTest.setUpdatedDate(res.getString(12));
                    mTestList.add(mstTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getMktProdTest()");
        }
        return mTestList;
    }
    
    public static List<MstTest> getAllMktTest(String prodId) {
        List<MstTest> mTestList = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT MST_MKT_TEST_VAL_SPEC.PROD_ID, MST_PRODUCT.PROD_NAME, MST_MKT_TEST_VAL_SPEC.TEST_ID, "
                        + "MST_TEST.TEST_NAME, MST_MKT_TEST_VAL_SPEC.VALUE_CHECK_ID, MST_MKT_TEST_VAL_SPEC.MIN_VAL, MST_MKT_TEST_VAL_SPEC.MAX_VAL, "
                        + "MST_MKT_TEST_VAL_SPEC.TYPICAL_VAL, MST_MKT_TEST_VAL_SPEC.DEVIATION, OTHER_VAL, MST_MKT_TEST_VAL_SPEC.UPDATED_BY, MST_MKT_TEST_VAL_SPEC.UPDATED_DATETIME FROM MST_MKT_TEST_VAL_SPEC INNER JOIN MST_PRODUCT "
                        + "ON MST_PRODUCT.PROD_ID = MST_MKT_TEST_VAL_SPEC.PROD_ID INNER JOIN MST_TEST ON MST_TEST.TEST_ID = MST_MKT_TEST_VAL_SPEC.TEST_ID where MST_PRODUCT.PROD_ID = ?");) {
            pst.setString(1, prodId);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mstTest = new MstTest();
                    mstTest.setProId(res.getString(1));
                    mstTest.setProName(res.getString(2));
                    mstTest.setTestId(res.getString(3));
                    mstTest.setTestName(res.getString(4));
                    mstTest.getMstTestParam().setCheckId(res.getString(5));
                    mstTest.getMstTestParam().setMinValue(res.getString(6));
                    mstTest.getMstTestParam().setMaxValue(res.getString(7));
                    mstTest.getMstTestParam().setTypValue(res.getString(8));
                    mstTest.getMstTestParam().setDevValue(res.getString(9));
                    mstTest.getMstTestParam().setOtherVal(res.getString(10));
                    switch (res.getString(5)) {
                        case "0":
                            mstTest.setSpec("No Validation.");
                            break;
                        case "1":
                            mstTest.setSpec(" >=  " + res.getString(6));
                            break;
                        case "2":
                            mstTest.setSpec(" <= " + res.getString(7));
                            break;
                        case "3":
                            mstTest.setSpec(res.getString(6) + " <= x  <= " + res.getString(7));
                            break;
                        case "4":
                            mstTest.setSpec("= " + res.getString(8) + " (" + res.getString(9) + "%)");
                            break;
                        case "5":
                            mstTest.setSpec("");
                            break;
                        case "6":
                            mstTest.setSpec(" <= " + res.getString(7));
                            break;
                        default:
                            mstTest.setSpec("Not Defined.");
                            break;
                    }
                    mstTest.setUpdatedBy(res.getString(11));
                    mstTest.setUpdatedDate(res.getString(12));
                    mTestList.add(mstTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getMktProdTest()");
        }
        return mTestList;
    }

    public static String getOtherVal(String testId) {
        String otherVal = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT OTHER_VAL FROM MST_OTHER_TEST_VALUE where TEST_ID = ?");) {
            pst.setString(1, testId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                otherVal += res.getString(1) + ", ";
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        if (otherVal.length() > 2) {
            return otherVal.substring(0, otherVal.length() - 2);
        } else {
            return otherVal;
        }
    }

    public static List<MstTest> getMktProdTest(String proId) {
        List<MstTest> mstTestList = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.TEST_ID, MST_TEST.TEST_NAME FROM (SELECT TEST_ID FROM MST_TEST MINUS "
                        + "SELECT TEST_ID FROM MST_MKT_TEST_VAL_SPEC WHERE PROD_ID = ?) T1 INNER JOIN MST_TEST ON MST_TEST.TEST_ID = T1.TEST_ID");) {
            pst.setString(1, proId);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mTest = new MstTest();
                    mTest.setTestId(res.getString(1));
                    mTest.setTestName(res.getString(2));
                    mstTestList.add(mTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getMktProdTest()");
        }
        return mstTestList;
    }

    public static List<MstTest> getvalChk() {
        List<MstTest> valChk = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM MST_VALUE_CHECK");) {
            while (res.next()) {
                MstTest mTest = new MstTest();
                mTest.setTestId(res.getString(1));
                mTest.setSpec(res.getString(2));
                valChk.add(mTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getvalChk()");
        }
        return valChk;
    }

    public static List<MstTest> getLabProdTest(String proId) {
        List<MstTest> mstTestList = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.TEST_ID, MST_TEST.TEST_NAME FROM (SELECT TEST_ID FROM MST_TEST MINUS SELECT TEST_ID "
                        + "FROM MST_LAB_TEST_VAL_SPEC WHERE PROD_ID = ?) T1 INNER JOIN MST_TEST ON MST_TEST.TEST_ID = T1.TEST_ID");) {
            pst.setString(1, proId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTest mTest = new MstTest();
                mTest.setTestId(res.getString(1));
                mTest.setTestName(res.getString(2));
                mstTestList.add(mTest);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getLabProdTest()");
        }
        return mstTestList;
    }

    public static MessageDetails UpdateLabTestSpec(MstTest mTest) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_LAB_TEST_VAL_SPEC SET VALUE_CHECK_ID = ?, MIN_VAL = ?, MAX_VAL = ?, TYPICAL_VAL = ?, "
                        + "DEVIATION = ?, OTHER_VAL = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') "
                        + "WHERE TEST_ID = ? AND PROD_ID = ?")) {
            pst.setString(1, mTest.getMstTestParam().getCheckId());
            pst.setString(2, mTest.getMstTestParam().getMinValue());
            pst.setString(3, mTest.getMstTestParam().getMaxValue());
            pst.setString(4, mTest.getMstTestParam().getTypValue());
            pst.setString(5, mTest.getMstTestParam().getDevValue());
            pst.setString(6, mTest.getMstTestParam().getOtherVal());
            pst.setString(7, mTest.getUpdatedBy());
            pst.setString(8, mTest.getTestId());
            pst.setString(9, mTest.getProId());
            if (pst.executeUpdate() > 0) {
                md.setModalMessage("Test Specification Updated Successfully");
                md.setMsgClass("text-success");
            } else {
                md.setModalMessage("Failed to Update Test Specification. Try Again Later");
                md.setMsgClass("text-danger");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mTest.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage("Specification already Added. Kindly Edit Existing");
                md.setMsgClass("text-danger");
            }
        }
        return md;
    }

    public static MessageDetails addLabTestSpec(MstTest mTest) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_LAB_TEST_VAL_SPEC (PROD_ID, TEST_ID, VALUE_CHECK_ID, MIN_VAL, "
                        + "MAX_VAL, TYPICAL_VAL, DEVIATION, OTHER_VAL, UPDATED_BY, UPDATED_DATETIME) VALUES (?,?,?,?,?,?,?,?,?, TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))");) {
            pst.setString(1, mTest.getProId());
            pst.setString(2, mTest.getTestId());
            pst.setString(3, mTest.getMstTestParam().getCheckId());
            pst.setString(4, mTest.getMstTestParam().getMinValue());
            pst.setString(5, mTest.getMstTestParam().getMaxValue());
            pst.setString(6, mTest.getMstTestParam().getTypValue());
            pst.setString(7, mTest.getMstTestParam().getDevValue());
            pst.setString(8, mTest.getMstTestParam().getOtherVal());
            pst.setString(9, mTest.getUpdatedBy());
            if (pst.executeUpdate() > 0) {
                md.setModalMessage("Test Specification Added Successfully");
                md.setMsgClass("text-success");
            } else {
                md.setModalMessage("Failed to Add Test Specification. Try Again Later");
                md.setMsgClass("text-danger");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mTest.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage("Specification already Added");
                md.setMsgClass("text-danger");
            }
        }
        return md;
    }

    public static List<MstTest> getAllLabTest() {
        List<MstTest> mTestList = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT MST_LAB_TEST_VAL_SPEC.PROD_ID, MST_PRODUCT.PROD_NAME, MST_LAB_TEST_VAL_SPEC.TEST_ID, "
                        + "MST_TEST.TEST_NAME, MST_LAB_TEST_VAL_SPEC.VALUE_CHECK_ID, MST_LAB_TEST_VAL_SPEC.MIN_VAL, MST_LAB_TEST_VAL_SPEC.MAX_VAL, "
                        + "MST_LAB_TEST_VAL_SPEC.TYPICAL_VAL, MST_LAB_TEST_VAL_SPEC.DEVIATION, OTHER_VAL, MST_LAB_TEST_VAL_SPEC.UPDATED_BY, MST_LAB_TEST_VAL_SPEC.UPDATED_DATETIME FROM MST_LAB_TEST_VAL_SPEC INNER JOIN MST_PRODUCT "
                        + "ON MST_PRODUCT.PROD_ID = MST_LAB_TEST_VAL_SPEC.PROD_ID INNER JOIN MST_TEST ON MST_TEST.TEST_ID = MST_LAB_TEST_VAL_SPEC.TEST_ID");) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mstTest = new MstTest();
                    mstTest.setProId(res.getString(1));
                    mstTest.setProName(res.getString(2));
                    mstTest.setTestId(res.getString(3));
                    mstTest.setTestName(res.getString(4));
                    mstTest.getMstTestParam().setCheckId(res.getString(5));
                    mstTest.getMstTestParam().setMinValue(res.getString(6));
                    mstTest.getMstTestParam().setMaxValue(res.getString(7));
                    mstTest.getMstTestParam().setTypValue(res.getString(8));
                    mstTest.getMstTestParam().setDevValue(res.getString(9));
                    mstTest.getMstTestParam().setOtherVal(res.getString(10));
                    switch (res.getString(5)) {
                        case "0":
                            mstTest.setSpec("No Validation.");
                            break;
                        case "1":
                            mstTest.setSpec(" >=  " + res.getString(6));
                            break;
                        case "2":
                            mstTest.setSpec(" <= " + res.getString(7));
                            break;
                        case "3":
                            mstTest.setSpec(res.getString(6) + " <= x  <= " + res.getString(7));
                            break;
                        case "4":
                            mstTest.setSpec("= " + res.getString(8) + " (" + res.getString(9) + "%)");
                            break;
                        case "5":
                            mstTest.setSpec("");
                            break;
                        case "6":
                            mstTest.setSpec(" <= " + res.getString(7));
                            break;
                        default:
                            mstTest.setSpec("Not Defined.");
                            break;
                    }
                    mstTest.setUpdatedBy(res.getString(11));
                    mstTest.setUpdatedDate(res.getString(12));
                    mTestList.add(mstTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstProdTest.getAllLabTest()");
        }
        return mTestList;
    }
    
    public static List<MstTest> getAllLabTest(String prodId) {
        List<MstTest> mTestList = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT MST_LAB_TEST_VAL_SPEC.PROD_ID, MST_PRODUCT.PROD_NAME, MST_LAB_TEST_VAL_SPEC.TEST_ID, "
                        + "MST_TEST.TEST_NAME, MST_LAB_TEST_VAL_SPEC.VALUE_CHECK_ID, MST_LAB_TEST_VAL_SPEC.MIN_VAL, MST_LAB_TEST_VAL_SPEC.MAX_VAL, "
                        + "MST_LAB_TEST_VAL_SPEC.TYPICAL_VAL, MST_LAB_TEST_VAL_SPEC.DEVIATION, OTHER_VAL, MST_LAB_TEST_VAL_SPEC.UPDATED_BY, MST_LAB_TEST_VAL_SPEC.UPDATED_DATETIME FROM MST_LAB_TEST_VAL_SPEC INNER JOIN MST_PRODUCT "
                        + "ON MST_PRODUCT.PROD_ID = MST_LAB_TEST_VAL_SPEC.PROD_ID INNER JOIN MST_TEST ON MST_TEST.TEST_ID = MST_LAB_TEST_VAL_SPEC.TEST_ID WHERE MST_PRODUCT.PROD_ID = ?");) {
            pst.setString(1, prodId);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTest mstTest = new MstTest();
                    mstTest.setProId(res.getString(1));
                    mstTest.setProName(res.getString(2));
                    mstTest.setTestId(res.getString(3));
                    mstTest.setTestName(res.getString(4));
                    mstTest.getMstTestParam().setCheckId(res.getString(5));
                    mstTest.getMstTestParam().setMinValue(res.getString(6));
                    mstTest.getMstTestParam().setMaxValue(res.getString(7));
                    mstTest.getMstTestParam().setTypValue(res.getString(8));
                    mstTest.getMstTestParam().setDevValue(res.getString(9));
                    mstTest.getMstTestParam().setOtherVal(res.getString(10));
                    switch (res.getString(5)) {
                        case "0":
                            mstTest.setSpec("No Validation.");
                            break;
                        case "1":
                            mstTest.setSpec(" >=  " + res.getString(6));
                            break;
                        case "2":
                            mstTest.setSpec(" <= " + res.getString(7));
                            break;
                        case "3":
                            mstTest.setSpec(res.getString(6) + " <= x  <= " + res.getString(7));
                            break;
                        case "4":
                            mstTest.setSpec("= " + res.getString(8) + " (" + res.getString(9) + "%)");
                            break;
                        case "5":
                            mstTest.setSpec("");
                            break;
                        case "6":
                            mstTest.setSpec(" <= " + res.getString(7));
                            break;
                        default:
                            mstTest.setSpec("Not Defined.");
                            break;
                    }
                    mstTest.setUpdatedBy(res.getString(11));
                    mstTest.setUpdatedDate(res.getString(12));
                    mTestList.add(mstTest);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstProdTest.getAllLabTest()");
        }
        return mTestList;
    }
}
