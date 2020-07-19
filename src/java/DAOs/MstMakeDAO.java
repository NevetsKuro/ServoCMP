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
import viewModel.MstMake;

public class MstMakeDAO {

    public static MessageDetails InsertMake(MstMake mstMake) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_MAKE (MAKE_ID, MAKE_NAME, UPDATED_BY, UPDATED_DATETIME) VALUES ((SELECT MAX(TO_NUMBER(REPLACE(MAKE_ID, 'SYS', ''))) + 1 FROM MST_MAKE),?, ?, TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))")) {
            pst.setString(1, mstMake.getMakeName().toUpperCase());
            pst.setString(2, mstMake.getUpdatedBy());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("Make Added Successfully");
            } else {
                md.setMsgClass("text-danger");
                md.setModalMessage("Error While adding Make. No changes were made");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstMake.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Duplicated Make or Make Already Exist");
                md.setFilemsgClass("text-danger");
            }
            md.setMsgClass("text-danger");
        }
        return md;
    }

    public static List<MstMake> listAllMake() {
        List<MstMake> mstMake = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM MST_MAKE")) {
            while (res.next()) {
                MstMake mMake = new MstMake();
                mMake.setMakeId(res.getString(1));
                mMake.setMakeName(res.getString(2));
                mMake.setUpdatedBy(res.getString(3));
                mMake.setUpdateddateTime(res.getString(4));
                mstMake.add(mMake);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstMakeDAO.listAllMake()");
        }
        return mstMake;
    }

    public static MessageDetails UpdateMake(MstMake mstMake) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_MAKE SET MAKE_NAME = ?, UPDATED_BY = ?, UPDATED_DATETIME = "
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE MAKE_ID = ?")) {
            pst.setString(1, mstMake.getMakeName());
            pst.setString(2, mstMake.getUpdatedBy());
            pst.setString(3, mstMake.getMakeId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstMake.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Duplicated Make or Make Already Exist");
                md.setFilemsgClass("text-danger");
            }
            md.setMsgClass("text-danger");
        }
        return md;
    }

    public static List<MstMake> listAllMake(String query) {
        List<MstMake> mstMake = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);) {
            while (res.next()) {
                MstMake mMake = new MstMake();
                mMake.setMakeId(res.getString(1));
                mMake.setMakeName(res.getString(2));
                mMake.setActive(res.getString(3));
                mMake.setUpdatedBy(res.getString(4));
                mMake.setUpdateddateTime(res.getString(5));
                mstMake.add(mMake);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstIndustryDAO.listAllIndustry()");
        }
        return mstMake;
    }

    public static MessageDetails AddMake(MstMake mstMake) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_MAKE (MAKE_ID, MAKE_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME) "
                        + "VALUES ((SELECT COALESCE(MAX(TO_NUMBER(REPLACE(MAKE_ID, 'SYS', ''))),0) + 1 FROM MST_MAKE), ?, ?, ?, "
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))");) {
            md.setModalMessage("");
            for (String makeName : mstMake.getMakeNames()) {
                try {
                    pst.setString(1, makeName.toUpperCase());
                    pst.setString(2, mstMake.getActive());
                    pst.setString(3, mstMake.getUpdatedBy());
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Make '" + makeName.toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Make '" + makeName.toUpperCase() + "'. No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Make '" + makeName.toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), mstMake.getUpdatedBy()) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique")) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Make '" + makeName.toUpperCase() + "' Already exist in Master. </span><br/><br/>");
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstMake.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Make '" + mstMake.getMakeName() + "' Already exist in Master");
            }
        }
        return md;
    }

    public static MessageDetails markInactive(MstMake mstMake) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_MAKE SET ACTIVE = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE MAKE_ID = ?");) {
            pst.setString(1, mstMake.getActive());
            pst.setString(2, mstMake.getUpdatedBy());
            pst.setString(3, mstMake.getMakeId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstMake.getUpdatedBy());
        }
        return md;
    }
}
