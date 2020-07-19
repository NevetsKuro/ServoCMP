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
import viewModel.MstDepartment;

public class MstDepartmentDAO {

    public static List<MstDepartment> listAllDepartment() {
        List<MstDepartment> mstDepartment = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT * FROM MST_DEPARTMENT");) {
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstDepartment mDept = new MstDepartment();
                mDept.setDepartmentId(res.getString(1));
                mDept.setDepartmentName(res.getString(2));
                mDept.getMstCustomer().setCustomerId(res.getString(3));
                mDept.setHodName(res.getString(4));
                mDept.setHodEmail(res.getString(5));
                mDept.setHodContact(res.getString(6));
                mDept.setUpdatedBy(res.getString(7));
                mDept.setUpdatedDatetime(res.getString(8));
                mstDepartment.add(mDept);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstDepartmentDAO.listAllDepartment()");
        }
        return mstDepartment;
    }

    public static MstDepartment getDepartment(String CustId, String DeptId) {
        MstDepartment mstDept = new MstDepartment();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT * FROM MST_DEPARTMENT WHERE CUST_ID = '?' AND DEPT_ID = '?'");) {
            pst.setString(1, CustId);
            pst.setString(2, DeptId);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                mstDept.setDepartmentId(res.getString(1));
                mstDept.setDepartmentName(res.getString(2));
                mstDept.getMstCustomer().setCustomerId(res.getString(3));
                mstDept.setHodName(res.getString(4));
                mstDept.setHodEmail(res.getString(5));
                mstDept.setHodContact(res.getString(6));
                mstDept.setUpdatedBy(res.getString(7));
                mstDept.setUpdatedDatetime(res.getString(8));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstDepartmentDAO.getDepartment(1,2)");
        }
        return mstDept;

    }

    public static MessageDetails UpdateDepartment(MstDepartment mstDept) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_DEPARTMENT SET DEPT_NAME = ?, CUST_ID = ?, HOD_NAME = ?, HOD_EMAIL = ?, "
                        + "HOD_CONTACT = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE DEPT_ID = ?")) {
            pst.setString(1, mstDept.getDepartmentName());
            pst.setString(2, mstDept.getMstCustomer().getCustomerId());
            pst.setString(3, mstDept.getHodName());
            pst.setString(4, mstDept.getHodEmail());
            pst.setString(5, mstDept.getHodContact());
            pst.setString(6, mstDept.getUpdatedBy());
            pst.setString(7, mstDept.getDepartmentId());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("Department Updated Successfully");
            } else {
                md.setMsgClass("text-danger");
                md.setModalMessage("Error While Updating Department. No Changes were made.");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstDept.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Duplicate Department or Department Already Exist");
            }
            md.setMsgClass("text-danger");
        }
        return md;
    }

    public static MessageDetails AddDepartment(MstDepartment mstDept) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_DEPARTMENT (DEPT_ID, DEPT_NAME, CUST_ID, HOD_NAME, HOD_EMAIL, HOD_CONTACT, UPDATED_BY, UPDATED_DATETIME) VALUES ((SELECT COALESCE(MAX(TO_NUMBER(REPLACE(DEPT_ID, 'SYS', ''))),0) + 1 FROM MST_DEPARTMENT), ?, ?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))")) {
            md.setModalMessage("");
            for (int i = 0; i < mstDept.getDepartmentNames().length; i++) {
                try {
                    pst.setString(1, mstDept.getDepartmentNames()[i].toUpperCase());
                    pst.setString(2, mstDept.getMstCustomer().getCustomerId());
                    pst.setString(3, mstDept.getHodNames()[i]);
                    pst.setString(4, mstDept.getHodEmails()[i]);
                    pst.setString(5, mstDept.getHodContacts()[i]);
                    pst.setString(6, mstDept.getUpdatedBy());
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Department '" + mstDept.getDepartmentNames()[i].toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Department '" + mstDept.getDepartmentNames()[i].toUpperCase() + "'. No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Department '" + mstDept.getDepartmentNames()[i].toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), mstDept.getUpdatedBy()) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique")) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Department '" + mstDept.getDepartmentNames()[i].toUpperCase() + "' Already exist in Master. </span><br/><br/>");
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstDept.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Duplicate Department or Department Already Exist");
            }
            md.setMsgClass("text-danger");
        }
        return md;
    }

    public static List<MstDepartment> listAllDepartment(String custId, String deptId) {
        List<MstDepartment> mstDept = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT * FROM MST_DEPARTMENT WHERE CUST_ID = '?' AND DEPT_ID = '?'");) {
            pst.setString(1, custId);
            pst.setString(2, deptId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstDepartment mDept = new MstDepartment();
                mDept.setDepartmentId(res.getString(1));
                mDept.setDepartmentName(res.getString(2));
                mDept.setHodName(res.getString(4));
                mDept.setHodEmail(res.getString(5));
                mDept.setHodContact(res.getString(6));
                mDept.setUpdatedBy(res.getString(7));
                mDept.setUpdatedDatetime(res.getString(8));
                mstDept.add(mDept);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstDepartmentDAO.listAllDepartment(custId, DeptId)");
        }
        return mstDept;
    }

    public static List<MstDepartment> listAllDepartment(String custId) {
        List<MstDepartment> mstDept = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT * FROM MST_DEPARTMENT WHERE CUST_ID = ?");) {
            pst.setString(1, custId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstDepartment mDept = new MstDepartment();
                mDept.setDepartmentId(res.getString(1));
                mDept.setDepartmentName(res.getString(2));
                mDept.setHodName(res.getString(4));
                mDept.setHodEmail(res.getString(5));
                mDept.setHodContact(res.getString(6));
                mDept.setUpdatedBy(res.getString(7));
                mDept.setUpdatedDatetime(res.getString(8));
                mstDept.add(mDept);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstDepartmentDAO.listAllDepartment(custId, DeptId)");
        }
        return mstDept;
    }
}