package DAOs;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import operations.ErrorLogger;
import viewModel.MessageDetails;
import viewModel.MstLab;
import viewModel.MstTest;
import viewModel.MstUser;

public class MstLabDAO {

    public static MessageDetails updateLab(MstLab mstLab) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_LAB SET LAB_NAME = ?, LAB_ADDRESS1 = ?, LAB_ADDRESS2 = ?, LAB_ADDRESS3 = ?, "
                        + " EMP_CODE = ?, ACTIVE = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE LAB_LOC_CODE = ?");) {
            pst.setString(1, mstLab.getLabName());
            pst.setString(2, mstLab.getLabAdd1());
            pst.setString(3, mstLab.getLabAdd2());
            pst.setString(4, mstLab.getLabAdd3());
            pst.setString(5, mstLab.getLabAuthority());
            pst.setString(6, mstLab.getActive());
            pst.setString(7, mstLab.getLabEmpCode());
            pst.setString(8, mstLab.getLabCode());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("'" + mstLab.getLabName() + "' Lab Updated Successfully");
            } else {
                md.setMsgClass("text-success");
                md.setModalMessage("Unable to Update '" + mstLab.getLabName() + "' Lab");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstLab.getMstUser().getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage("'" + mstLab.getLabName() + "' already Exist in Master");
            }
        }
        return md;
    }

    public static MessageDetails updateLabTse(String empCode, String labCode) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pstdel = con.prepareStatement("DELETE from LAB_TSE_MAPPED WHERE EMP_CODE = ? and LAB_CODE = ?");
                PreparedStatement pst = con.prepareStatement("INSERT INTO LAB_TSE_MAPPED (LAB_CODE, EMP_CODE) VALUES (?,?)");) {
            pstdel.setString(1, empCode);
            pstdel.setString(2, labCode);
            pst.setString(1, labCode);
            pst.setString(2, empCode);
            pstdel.executeUpdate();
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("'Emp No. " + empCode + "' Successfully Updated in Lab ");
            } else {
                md.setMsgClass("text-success");
                md.setModalMessage("Unable to Update Emp No.'" + empCode + "' into Lab");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, empCode);
        }
        return md;
    }

    public static MessageDetails addLab(MstLab mLab) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_LAB (LAB_LOC_CODE, LAB_NAME, LAB_ADDRESS1, LAB_ADDRESS2, LAB_ADDRESS3, "
                        + " EMP_CODE, ACTIVE, UPDATED_BY, UPDATED_DATETIME, TYPE) VALUES (?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'),?)");) {
            pst.setString(1, mLab.getLabCode().trim());
            pst.setString(2, mLab.getLabName().toUpperCase());
            pst.setString(3, mLab.getLabAdd1());
            pst.setString(4, mLab.getLabAdd2());
            pst.setString(5, mLab.getLabAdd3());
            pst.setString(6, mLab.getLabAuthority());
            pst.setString(7, mLab.getActive());
            pst.setString(8, mLab.getLabEmpCode());
            pst.setString(9, mLab.getLabType());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("'" + mLab.getLabName() + "' Lab Added Successfully to the Master.");
                md.setStatus(true);
            } else {
                md.setMsgClass("text-success");
                md.setModalMessage("Unable to add '" + mLab.getLabName() + "' Lab to the Master.");
                md.setStatus(false);
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, "MstLabDAO.addLab()");
            if (ex.getMessage().contains("unique") && ex.getMessage().contains("UK1")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Lab '" + mLab.getLabName() + "' Already exist in Master");
            } else if (ex.getMessage().contains("unique") && ex.getMessage().contains("UK3")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Emp Code '" + mLab.getLabEmpCode() + "' Already assigned in Master");
            } else if (ex.getMessage().contains("unique") && ex.getMessage().contains("UK2")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Emp Code '" + mLab.getLabEmpCode() + "' Already assigned with a lab in Master");
            }
        }
        return md;
    }

    public static List<MstLab> listLab() {
        List<MstLab> mstLab = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.LAB_LOC_CODE, T1.LAB_NAME, T1.LAB_ADDRESS1, T1.LAB_ADDRESS2,"
                        + "T1.LAB_ADDRESS3, T1.EMP_CODE, T2.EMP_NAME, T1.UPDATED_BY, T1.UPDATED_DATETIME "
                        + "FROM MST_LAB T1 INNER JOIN MST_USER T2 ON T2.EMP_CODE = T1.EMP_CODE ");
                ResultSet res = pst.executeQuery();) {
            while (res.next()) {
                MstUser mUser = new MstUser();
                mUser.setEmpCode(res.getString(6));
                mUser.setEmpName(res.getString(7));
                mUser.setUpdatedBy(res.getString(8));
                mUser.setUpdatedDate(res.getString(9));
//                MstUser mTseUser = new MstUser();
//                mTseUser.setEmpCode(res.getString(10));
//                mTseUser.setEmpName(res.getString(11));
                MstLab mLab = new MstLab();
                mLab.setMstUser(mUser);
//                mLab.setMstTseUser(mTseUser);
                mLab.setLabCode(res.getString(1));
                mLab.setLabName(res.getString(2));
                mLab.setLabAdd1(res.getString(3));
                mLab.setLabAdd2(res.getString(4));
                mLab.setLabAdd3(res.getString(5));
                mstLab.add(mLab);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.listLab()");
        }
        return mstLab;
    }

    public static List<MstLab> listLabByLabType(String labType) {
        List<MstLab> mstLab = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.LAB_LOC_CODE, T1.LAB_NAME, T1.LAB_ADDRESS1, T1.LAB_ADDRESS2, "
                        + "T1.LAB_ADDRESS3, T1.EMP_CODE, T2.EMP_NAME, T1.UPDATED_BY, T1.UPDATED_DATETIME FROM MST_LAB T1 INNER JOIN MST_USER T2 "
                        + "ON T2.EMP_CODE = T1.EMP_CODE WHERE T1.TYPE = ?");) {
            pst.setString(1, labType);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstUser mUser = new MstUser();
                mUser.setEmpCode(res.getString(6));
                mUser.setEmpName(res.getString(7));
                mUser.setUpdatedBy(res.getString(8));
                mUser.setUpdatedDate(res.getString(9));
                MstLab mLab = new MstLab();
                mLab.setMstUser(mUser);
                mLab.setLabCode(res.getString(1));
                mLab.setLabName(res.getString(2));
                mLab.setLabAdd1(res.getString(3));
                mLab.setLabAdd2(res.getString(4));
                mLab.setLabAdd3(res.getString(5));
                mstLab.add(mLab);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.listLabByLabType()");
        }
        return mstLab;
    }

    public static List<MstLab> listLab(String empCode) {
        List<MstLab> mstLab = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.LAB_LOC_CODE, T1.LAB_NAME, T1.LAB_ADDRESS1, T1.LAB_ADDRESS2, "
                        + "T1.LAB_ADDRESS3, T1.EMP_CODE, T2.EMP_NAME, T1.UPDATED_BY, T1.UPDATED_DATETIME FROM MST_LAB T1 INNER JOIN MST_USER T2 "
                        + "ON T2.EMP_CODE = T1.EMP_CODE WHERE T1.EMP_CODE = ?");) {
            pst.setString(1, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstUser mUser = new MstUser();
                    mUser.setEmpCode(res.getString(6));
                    mUser.setEmpName(res.getString(7));
                    mUser.setUpdatedBy(res.getString(8));
                    mUser.setUpdatedDate(res.getString(9));
                    MstLab mLab = new MstLab();
                    mLab.setMstUser(mUser);
                    mLab.setLabCode(res.getString(1));
                    mLab.setLabName(res.getString(2));
                    mLab.setLabAdd1(res.getString(3));
                    mLab.setLabAdd2(res.getString(4));
                    mLab.setLabAdd3(res.getString(5));
                    mstLab.add(mLab);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.listLab(empCode)");
        }
        return mstLab;
    }

    public static MessageDetails updateLabEmp(String newEmpCode, String[] labCodes, String updatedBy) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_LAB SET EMP_CODE = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'), "
                        + "UPDATED_BY = ? WHERE LAB_LOC_CODE = ?");) {
            md.setModalMessage("");
            for (String labCode : labCodes) {
                try {
                    pst.setString(1, newEmpCode);
                    pst.setString(2, updatedBy);
                    pst.setString(3, labCode.toUpperCase());
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Customer '" + labCode.toUpperCase() + "' is now managed by " + newEmpCode + ".</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while changing Tse of Customer '" + labCode.toUpperCase() + "' to " + newEmpCode + ". No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While changing '" + labCode.toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), updatedBy) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique") && ex.getMessage().contains("UK1")) {
                        md.setFilemsgClass("text-danger");
                        md.setFileMsg("Lab Name Already exist in Master");
                    } else if (ex.getMessage().contains("unique") && ex.getMessage().contains("UK3")) {
                        md.setFilemsgClass("text-danger");
                        md.setFileMsg("Emp Code " + newEmpCode + " Already assigned in Master");
                    } else if (ex.getMessage().contains("unique") && ex.getMessage().contains("UK2")) {
                        md.setFilemsgClass("text-danger");
                        md.setFileMsg("Emp Code " + newEmpCode + " Already assigned with a lab in Master");
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, updatedBy);
        }
        return md;
    }

    public static String listLabByEmp(String empCode) {
        String LabCode = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select lab_loc_code from mst_lab where emp_code = ?");) {
            pst.setString(1, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    LabCode = res.getString(1);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.listLabByEmp(empCode)");
        }
        return LabCode;
    }

    public static String toggleActiveForTest(String test_id, String Lab_Code) {
        String status = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select ACTIVE from mst_lab_test_status where test_id = ? and lab_code = ?");) {
            pst.setString(1, test_id);
            pst.setString(2, Lab_Code);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    String active = "";
                    if (res.getString(1).equalsIgnoreCase("1")) {
                        active = "0";
                    } else if (res.getString(1).equalsIgnoreCase("0")) {
                        active = "1";
                    }
                    try (PreparedStatement pst1 = con.prepareStatement("update mst_lab_test_status set active = ? where test_id = ? and lab_code = ?");) {
                        pst1.setString(1, active);
                        pst1.setString(2, test_id);
                        pst1.setString(3, Lab_Code);
                        int i = pst1.executeUpdate();
                        if (i == 1) {
                            status = "Successful";
                        }
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "MstLabDAO.toggleActiveForTest(empCode)");
                    }
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.toggleActiveForTest(empCode)");
        }
        return status;
    }

    public static String checkLabTseAccess(String empCode) {
        String LabName = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select LAB_NAME from LAB_TSE_MAPPED m1 INNER JOIN mst_lab l1 on m1.LAB_CODE = l1.LAB_LOC_CODE where m1.EMP_CODE = ?");) {
            pst.setString(1, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    LabName = res.getString(1);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.checkLabTseAccess(empCode)");
        }
        if (LabName.length() > 0) {
            return "Mapped with lab " + LabName;
        } else {
            return "No Mapped with any Lab";
        }
    }

    public static String syncCntrlOfficersMstUser(String sCemDBPath) {
        String status = "", empList = "";

        try (Connection con1 = DatabaseConnectionFactory.createConnection();
                Connection con2 = DatabaseConnectionFactory.createCEMConnection(sCemDBPath);
                PreparedStatement pst = con1.prepareStatement("select listagg(emp_code,',') within group(order by emp_code) from mst_user");) {
            try (ResultSet res = pst.executeQuery();) {
                if (res.next()) {
                    empList = res.getString(1);
                    for (String empCode : empList.split(",")) {
                        try (PreparedStatement pst1 = con2.prepareStatement("SELECT T1.EMP_CODE, T1.EMP_NAME, T1.EMAIL_ID, T1.CNTRLG_EMP_CODE, T2.EMP_NAME, T2.EMAIL_ID FROM "
                                + "COM_EMP_DB.EMP_DB T1 INNER JOIN COM_EMP_DB.EMP_DB T2 ON T1.CNTRLG_EMP_CODE = T2.EMP_CODE WHERE T1.EMP_CODE = ?");) {
                            pst1.setString(1, empCode);
                            try (ResultSet res1 = pst1.executeQuery();) {
                                while (res1.next()) {
                                    try (PreparedStatement pst2 = con1.prepareStatement("update mst_user set cntrlg_emp_code = ?, cntrlg_emp_name = ?, cntrlg_emp_email = ? where emp_code = ?");) {
                                        pst2.setString(1, res1.getString(4));
                                        pst2.setString(2, res1.getString(5));
                                        pst2.setString(3, res1.getString(6));
                                        pst2.setString(4, empCode);
                                        int i = pst2.executeUpdate();
                                        if (i > 0) {
                                            status = "Table Updated Succesfully";
                                        } else {
                                            status = "Failed to ";
                                        }
                                    } catch (Exception e) {
                                        MyLogger.logIt(e, "MstLabDAO.syncCntrlOfficersMstUser(empCode)");
                                        status += e.getMessage();
                                    }
                                }
                            } catch (Exception ex) {
                                MyLogger.logIt(ex, "MstLabDAO.syncCntrlOfficersMstUser(empCode)");
                                status += ex.getMessage();
                            }
                        } catch (Exception ex) {
                            MyLogger.logIt(ex, "MstLabDAO.syncCntrlOfficersMstUser(empCode)");
                            status += ex.getMessage();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.syncCntrlOfficersMstUser(empCode)");
            status += ex.getMessage();
        }
        return status;
    }

    public static List<MstLab> listTseLabMapped() {
        List<MstLab> mstLab = new ArrayList<MstLab>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select l1.lab_loc_code,l1.lab_name, m1.emp_code, u1.emp_name from LAB_TSE_MAPPED m1 INNER JOIN mst_lab l1 on m1.LAB_CODE = l1.LAB_LOC_CODE INNER JOIN mst_user u1 on m1.EMP_CODE = u1.EMP_CODE");) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstLab mLab = new MstLab();
                    mLab.setLabCode(res.getString(1));
                    mLab.setLabName(res.getString(2));
                    MstUser user = new MstUser();
                    user.setEmpCode(res.getString(3));
                    user.setEmpName(res.getString(4));
                    mLab.setMstTseUser(user);
                    mstLab.add(mLab);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.listTseLabMapped(empCode)");
        }
        return mstLab;
    }

    public static String listTseLabDelete(String empcode, String labcode) {
        String status = "in progress..";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("DELETE from LAB_TSE_MAPPED WHERE EMP_CODE = ? and LAB_CODE = ?");) {
            pst.setString(1, empcode);
            pst.setString(2, labcode);
            int i = pst.executeUpdate();
            if (i > 0) {
                status = "Successfully Deleted";
            } else {
                status = "Failed to Deleted";
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.listTseLabDelete(empCode,labcode)");
        }
        return status;
    }

    public static MessageDetails addLabTestMapping(MstLab mLab) {
        MessageDetails md = new MessageDetails();
        List<MstTest> testList = new ArrayList<>();
        int count = 0;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select TEST_ID, TEST_NAME from mst_test");) {
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    MstTest mstTest = new MstTest();
                    mstTest.setTestId(res.getString(1));
                    mstTest.setTestName(res.getString(2));
                    testList.add(mstTest);
                }
                for (MstTest mstTest : testList) {
                    try (
                            PreparedStatement pst2 = con.prepareStatement("INSERT INTO MST_LAB_TEST_STATUS VALUES (?,?,?,?,?)");) {
                        pst2.setString(1, mLab.getLabCode());
                        pst2.setString(2, mstTest.getTestId());
                        pst2.setString(3, mstTest.getTestName());
                        pst2.setString(4, "SYSTEM");
                        pst2.setString(5, "1");
                        int i = pst2.executeUpdate();
                        if (i > 0) {
                            count++;
                        }
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "MstLabDAO.addLabTestMapping(MstLab)");
                    }
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.addLabTestMapping(MstLab)");
        }
        return md;
    }

    public static MessageDetails addNewTestLabMapping(MstTest mTest) {
        MessageDetails md = new MessageDetails();
        List<MstLab> labList = new ArrayList<>();
        int count = 0;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select LAB_LOC_CODE, LAB_NAME from mst_lab");
                PreparedStatement pst1 = con.prepareStatement("SELECT MAX(TO_NUMBER(REPLACE(TEST_ID, 'SYS', ''))) from mst_test ");) {
            try (ResultSet res = pst.executeQuery();
                    ResultSet res1 = pst1.executeQuery();) {
                while (res.next()) {
                    MstLab mstLab = new MstLab();
                    mstLab.setLabCode(res.getString(1));
                    mstLab.setLabName(res.getString(2));
                    labList.add(mstLab);
                }
                if (res1.next()) {
                    mTest.setTestId(res1.getString(1));
                }
                for (MstLab mstLab : labList) {
                    try (
                            PreparedStatement pst2 = con.prepareStatement("INSERT INTO MST_LAB_TEST_STATUS VALUES (?,?,?,?,?)");) {
                        pst2.setString(1, mstLab.getLabCode());
                        pst2.setString(2, mTest.getTestId());
                        pst2.setString(3, mTest.getTestName());
                        pst2.setString(4, "SYSTEM");
                        pst2.setString(5, "1");
                        int i = pst2.executeUpdate();
                        if (i > 0) {
                            count++;
                        }
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "MstLabDAO.addLabTestMapping(MstLab)");
                    }
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstLabDAO.addLabTestMapping(MstLab)");
        }
        return md;
    }
}
