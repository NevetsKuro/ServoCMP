package DAOs;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import operations.ErrorLogger;
import viewModel.MessageDetails;
import viewModel.MstCustomer;
import viewModel.MstIndustry;
import viewModel.MstUser;

public class MstCustomerDAO {

    public static MessageDetails updateCustTse(String newEmpCode, String[] custNo, String updatedBy,String[] custNames) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_CUSTOMER SET EMP_CODE = ?, UPDATED_DATE = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'), UPDATED_BY = ? "
                        + "WHERE CUST_ID = ?");) {
            md.setModalMessage("");
            for (int i = 0; i < custNo.length; i++) {
                try {
                    pst.setString(1, newEmpCode);
                    pst.setString(2, updatedBy);
                    pst.setString(3, custNo[i]);
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Customer '" + custNames[i].toUpperCase() + "' is now managed by Emp No. " + newEmpCode + ".</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while changing Tse of Customer '" + custNames[i].toUpperCase() + "' to " + newEmpCode + ". No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md = MyLogger.logIt(ex, updatedBy);
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While changing '" + custNames[i].toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), updatedBy) + ") </span> <br/>");
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, updatedBy);
        }
        return md;
    }

    public static List<MstCustomer> listTseCustomer() {
        List<MstCustomer> mstCust = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.IND_NAME, T2.CUST_ID, T2.CUST_NAME, T2.EMP_CODE, T3.EMP_NAME FROM "
                        + "MST_CUSTOMER T2 INNER JOIN MST_INDUSTRY T1 ON T1.IND_ID = T2.IND_ID INNER JOIN MST_USER T3 ON T3.EMP_CODE = T2.EMP_CODE WHERE T2.ACTIVE = 1 AND ROLE_ID = 1");) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstIndustry mInd = new MstIndustry();
                    mInd.setIndName(res.getString(1));
                    MstUser mUser = new MstUser();
                    mUser.setEmpCode(res.getString(4));
                    mUser.setEmpName(res.getString(5));
                    MstCustomer mCust = new MstCustomer();
                    mCust.setCustomerId(res.getString(2));
                    mCust.setCustomerName(res.getString(3));
                    mCust.setMstIndustry(mInd);
                    mCust.setMstUser(mUser);
                    mstCust.add(mCust);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstCustomerDAO.listAllCustomer()");
        }
        return mstCust;
    }

    public static List<MstCustomer> listTseCustomer(String empCode) {
        List<MstCustomer> mstCust = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.IND_NAME, T2.CUST_ID, T2.CUST_NAME, T2.EMP_CODE, T3.EMP_NAME FROM "
                        + "MST_CUSTOMER T2 INNER JOIN MST_INDUSTRY T1 ON T1.IND_ID = T2.IND_ID INNER JOIN MST_USER T3 ON T3.EMP_CODE = T2.EMP_CODE WHERE T2.ACTIVE = ? AND T2.EMP_CODE = ? AND T3.ROLE_ID = 1");) {
            pst.setString(1, "1");
            pst.setString(2, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstIndustry mInd = new MstIndustry();
                    mInd.setIndName(res.getString(1));
                    MstUser mUser = new MstUser();
                    mUser.setEmpCode(res.getString(4));
                    mUser.setEmpName(res.getString(5));
                    MstCustomer mCust = new MstCustomer();
                    mCust.setCustomerId(res.getString(2));
                    mCust.setCustomerName(res.getString(3));
                    mCust.setMstIndustry(mInd);
                    mCust.setMstUser(mUser);
                    mstCust.add(mCust);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstCustomerDAO.listAllCustomer()");
        }
        return mstCust;
    }

    public static List<MstCustomer> listAllCustomer() {
        List<MstCustomer> mstCustomer = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT CUST_ID,CUST_NAME,IND_ID,EMP_CODE,UPDATED_BY,UPDATED_DATE,ACTIVE FROM MST_CUSTOMER")) {
            while (res.next()) {
                MstCustomer mCustomer = new MstCustomer();
                mCustomer.setCustomerId(res.getString(1));
                mCustomer.setCustomerName(res.getString(2));
                mCustomer.setUpdatedBy(res.getString(5));
                mCustomer.setUpdatedDate(res.getString(6));
                mstCustomer.add(mCustomer);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstCustomerDAO.listAllCustomer()");
        }
        return mstCustomer;
    }

    public static List<MstCustomer> listAllCustomer(String IndustryId) {
        List<MstCustomer> mstCustomer = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT CUST_ID,CUST_NAME,IND_ID,EMP_CODE,UPDATED_BY,UPDATED_DATE,ACTIVE FROM MST_CUSTOMER WHERE IND_ID = ? AND ACTIVE = 1");) {
            pst.setString(1, IndustryId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstIndustry mstInd = new MstIndustry();
                MstUser mstUser = new MstUser();
                mstInd.setIndId(res.getString(3));
                mstUser.setEmpCode(res.getString(4));
                MstCustomer mCustomer = new MstCustomer(mstInd, mstUser);
                mCustomer.setCustomerId(res.getString(1));
                mCustomer.setCustomerName(res.getString(2));
                mCustomer.setMstIndustry(mstInd);
                mCustomer.setMstUser(mstUser);
                mCustomer.setUpdatedBy(res.getString(5));
                mCustomer.setUpdatedDate(res.getString(6));
                mstCustomer.add(mCustomer);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstCustomerDAO.listAllCustomer()");
        }
        return mstCustomer;
    }
    
    public static List<MstCustomer> listAllCustomerByTse(String IndustryId,String emp_code) {
        List<MstCustomer> mstCustomer = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT CUST_ID,CUST_NAME,IND_ID,EMP_CODE,UPDATED_BY,UPDATED_DATE,ACTIVE FROM MST_CUSTOMER WHERE IND_ID = ? AND EMP_CODE = ? AND ACTIVE = 1");) {
            pst.setString(1, IndustryId);
            pst.setString(2, emp_code);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstIndustry mstInd = new MstIndustry();
                MstUser mstUser = new MstUser();
                mstInd.setIndId(res.getString(3));
                mstUser.setEmpCode(res.getString(4));
                MstCustomer mCustomer = new MstCustomer(mstInd, mstUser);
                mCustomer.setCustomerId(res.getString(1));
                mCustomer.setCustomerName(res.getString(2));
                mCustomer.setMstIndustry(mstInd);
                mCustomer.setMstUser(mstUser);
                mCustomer.setUpdatedBy(res.getString(5));
                mCustomer.setUpdatedDate(res.getString(6));
                mstCustomer.add(mCustomer);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstCustomerDAO.listAllCustomer()");
        }
        return mstCustomer;
    }

    public static List<MstCustomer> listAllCustomer(String IndustryId, String CustId) {
        List<MstCustomer> mstCustomer = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT CUST_ID,CUST_NAME,IND_ID,EMP_CODE,UPDATED_BY,UPDATED_DATE,ACTIVE FROM MST_CUSTOMER WHERE IND_ID = ? AND CUST_ID = ?" );) {
            pst.setString(1, IndustryId);
            pst.setString(2, CustId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstIndustry mstInd = new MstIndustry();
                MstUser mstUser = new MstUser();
                mstInd.setIndId(res.getString(3));
                mstUser.setEmpCode(res.getString(4));
                MstCustomer mCustomer = new MstCustomer(mstInd, mstUser);
                mCustomer.setCustomerId(res.getString(1));
                mCustomer.setCustomerName(res.getString(2));
                mCustomer.setMstIndustry(mstInd);
                mCustomer.setMstUser(mstUser);
                mCustomer.setUpdatedBy(res.getString(5));
                mCustomer.setUpdatedDate(res.getString(6));
                mstCustomer.add(mCustomer);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstCustomerDAO.listAllCustomer()");
        }
        return mstCustomer;
    }

    public static MessageDetails AddCustomer(MstCustomer mstCust) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_CUSTOMER (CUST_ID, CUST_NAME, IND_ID, EMP_CODE, UPDATED_BY, "
                        + "UPDATED_DATE, ACTIVE) VALUES ((SELECT COALESCE(MAX(TO_NUMBER(REPLACE(CUST_ID, 'SYS', ''))),0) + 1 FROM MST_CUSTOMER), ?, ?, ?, ?, TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'), ?)")) {
            md.setModalMessage("");
            for (String CustomerName : mstCust.getCustomerNames()) {
                try {
                    pst.setString(1, CustomerName.toUpperCase());
                    pst.setString(2, mstCust.getMstIndustry().getIndId());
                    pst.setString(3, mstCust.getUpdatedBy());
                    pst.setString(4, mstCust.getUpdatedBy());
                    pst.setString(5, mstCust.getActive());
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Customer '" + CustomerName.toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Customer '" + CustomerName.toUpperCase() + "'. No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Customer '" + CustomerName.toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), mstCust.getUpdatedBy()) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique")) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Customer '" + CustomerName.toUpperCase() + "' Already exist in Master. </span><br/><br/>");
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstCust.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Duplicate Customer or Customer Already Exist");
            }
        }
        return md;
    }

    public static MessageDetails UpdateCustomer(MstCustomer mstCust) {
        MessageDetails md = new MessageDetails();
        String CustomerName = mstCust.getCustomerName().toUpperCase();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("update mst_customer set cust_name = ?  where cust_id = ?")) {
            md.setModalMessage("");
            try {
                pst.setString(1, CustomerName);
                pst.setString(2, mstCust.getCustomerId());
                if (pst.executeUpdate() > 0) {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Customer '" + CustomerName.toUpperCase() + "' Updated Successfully in Masters.</span><br/><br/>");
                } else {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while updating Customer '" + CustomerName.toUpperCase() + "'. No changes were made.</span><br/><br/>");
                }
            } catch (Exception ex) {
                md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Customer '" + CustomerName.toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), mstCust.getUpdatedBy()) + ") </span> <br/>");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstCust.getUpdatedBy());
        }
        return md;
    }

}
