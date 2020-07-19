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
import viewModel.MstRole;
import viewModel.MstUser;

public class MstUserDAO {

    public static List<MstUser> listAllUser(String query) {
        List<MstUser> mstUser = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);) {
            while (res.next()) {
                MstRole mr = new MstRole();
                mr.setRoleName(res.getString(10));
                mr.setRoleId(res.getString(7));
                MstUser mUser = new MstUser(mr);
                mUser.setEmpCode(res.getString(1));
                mUser.setEmpName(res.getString(2));
                mUser.setEmpEmail(res.getString(3));
                mUser.setCtrlEmpCode(res.getString(4));
                mUser.setCtrlEmpName(res.getString(5));
                mUser.setCtrlEmpEmail(res.getString(6));
                mUser.setUpdatedBy(res.getString(8));
                mUser.setUpdatedDate(res.getString(9));
                mUser.setActive(res.getString(11));
                mUser.setMstRole(mr);
                mstUser.add(mUser);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstUserDAO.listAllUser(query)");
        }
        return mstUser;
    }

    public static MessageDetails addUser(MstUser mstUser) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_USER (EMP_CODE, EMP_NAME, EMP_EMAIL, CNTRLG_EMP_CODE,"
                        + "CNTRLG_EMP_NAME, CNTRLG_EMP_EMAIL, ROLE_ID, UPDATED_BY, UPDATED_DATETIME, ACTIVE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'), ?)");) {
            pst.setString(1, mstUser.getEmpCode().trim());
            pst.setString(2, mstUser.getEmpName());
            pst.setString(3, mstUser.getEmpEmail());
            pst.setString(4, mstUser.getCtrlEmpCode());
            pst.setString(5, mstUser.getCtrlEmpName());
            pst.setString(6, mstUser.getCtrlEmpEmail());
            pst.setString(7, mstUser.getMstRole().getRoleId());
            pst.setString(8, mstUser.getUpdatedBy());
            pst.setString(9, mstUser.getActive());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("Employee " + mstUser.getEmpName() + " (" + mstUser.getEmpCode() + ") has been granted permission to use ServoCMP as a " + mstUser.getMstRole().getRoleName() + " User");
            } else {
                md.setMsgClass("text-danger");
                md.setModalMessage("Error occure while adding Employee. Try again Later.");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstUser.getUpdatedBy());
            md.setMsgClass("text-danger");
            if (ex.getMessage().contains("unique")) {
                md.setModalMessage("Employee " + mstUser.getEmpName() + " (" + mstUser.getEmpCode() + "), " + "already has access to ServoCMP portal");
            }
        }
        return md;
    }

    public static MessageDetails markInactive(MstUser mstUser) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_USER SET ACTIVE = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE EMP_CODE = ?");
                PreparedStatement pstTseCheck = con.prepareStatement("SELECT CUST_NAME FROM MST_CUSTOMER WHERE EMP_CODE = ?");
                PreparedStatement pstLabCheck = con.prepareStatement("SELECT LAB_NAME FROM MST_LAB WHERE EMP_CODE = ?");) {
            pstTseCheck.setString(1, mstUser.getEmpCode());
            pstLabCheck.setString(1, mstUser.getEmpCode());
            try (ResultSet res = pstTseCheck.executeQuery();) {
                if (res.isBeforeFirst()) {
                    md.setFileMsg("Employee cannot be removed as this employee manages Customer: ");
                    while (res.next()) {
                        md.setFileMsg(md.getFileMsg() + res.getString(1) + ", ");
                    }
                    md.setFileMsg(md.getFileMsg().substring(0, md.getFileMsg().length() - 2));
                    md.setStatus(false);
                } else {
                    try (ResultSet resLab = pstLabCheck.executeQuery();) {
                        if (resLab.isBeforeFirst()) {
                            md.setFileMsg("Employee cannot be removed as this employee manages Lab: ");
                            while (resLab.next()) {
                                md.setFileMsg(md.getFileMsg() + res.getString(1) + ", ");
                            }
                            md.setFileMsg(md.getFileMsg().substring(0, md.getFileMsg().length() - 2));
                            md.setStatus(false);
                        } else {
                            pst.setString(1, mstUser.getActive());
                            pst.setString(2, mstUser.getUpdatedBy());
                            pst.setString(3, mstUser.getEmpCode());
                            md.setStatus(pst.executeUpdate() > 0);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstUser.getUpdatedBy());
        }
        return md;
    }

    public static MessageDetails changeRole(MstUser mstUser) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_USER SET ROLE_ID = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE EMP_CODE = ?");
                PreparedStatement pstTseCheck = con.prepareStatement("SELECT CUST_NAME FROM MST_CUSTOMER WHERE EMP_CODE = ?");
                PreparedStatement pstLabCheck = con.prepareStatement("SELECT LAB_NAME FROM MST_LAB WHERE EMP_CODE = ?");) {
            pstTseCheck.setString(1, mstUser.getEmpCode());
            pstLabCheck.setString(1, mstUser.getEmpCode());
            try (ResultSet res = pstTseCheck.executeQuery();) {
                if (res.isBeforeFirst()) {
                    md.setFileMsg("Employee role cannot be changed as this employee manages Customer: ");
                    while (res.next()) {
                        md.setFileMsg(md.getFileMsg() + res.getString(1) + ", ");
                    }
                    md.setFileMsg(md.getFileMsg().substring(0, md.getFileMsg().length() - 2));
                    md.setStatus(false);
                } else {
                    try (ResultSet resLab = pstLabCheck.executeQuery();) {
                        if (resLab.isBeforeFirst()) {
                            md.setFileMsg("Employee role cannot be changed as this employee manages Lab: ");
                            while (resLab.next()) {
                                md.setFileMsg(md.getFileMsg() + resLab.getString(1) + ", ");
                            }
                            md.setFileMsg(md.getFileMsg().substring(0, md.getFileMsg().length() - 2));
                            md.setStatus(false);
                        } else {
                            pst.setString(1, mstUser.getMstRole().getRoleId());
                            pst.setString(2, mstUser.getUpdatedBy());
                            pst.setString(3, mstUser.getEmpCode());
                            md.setStatus(pst.executeUpdate() > 0);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstUser.getUpdatedBy());
        }
        return md;
    }

    public static List<MstUser> getCemEmployee(String empCode, String sCemDBPath) {
        List<MstUser> cemEmployee = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createCEMConnection(sCemDBPath);
                PreparedStatement pst = con.prepareStatement("SELECT EMP_CODE, EMP_NAME, EMAIL_ID FROM COM_EMP_DB.EMP_DB WHERE EMP_CODE = ?");) {
            pst.setString(1, empCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                MstUser mstUser = new MstUser();
                mstUser.setCtrlEmpCode(res.getString(1));
                mstUser.setCtrlEmpName(res.getString(2));
                mstUser.setCtrlEmpEmail(res.getString(3));
                cemEmployee.add(mstUser);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MyLogger.getCemEmployee(empCode, sCemDBPath)");
        }
        return cemEmployee;

    }
}
