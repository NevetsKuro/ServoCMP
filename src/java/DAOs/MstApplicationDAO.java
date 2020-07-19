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
import viewModel.MstApplication;

public class MstApplicationDAO {

    public static List<MstApplication> listAllApplication(String query) {
        List<MstApplication> mstApplication = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstApplication mApp = new MstApplication();
                mApp.setAppId(res.getString(1));
                mApp.setAppName(res.getString(2));
                mApp.setActive(res.getString(3));
                mApp.setUpdatedBy(res.getString(4));
                mApp.setUpdatedDate(res.getString(5));
                mstApplication.add(mApp);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstApplicationDAO.listAllApplication(query)");
        }
        return mstApplication;
    }

    public static MessageDetails AddApplication(MstApplication mstApplication) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_APPLICATION (APPL_ID, APPL_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME) "
                        + "VALUES ((SELECT MAX(TO_NUMBER(REPLACE(APPL_ID, 'SYS', ''))) + 1 FROM MST_APPLICATION), ?, ?, ?, "
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))");) {
            md.setModalMessage("");
            for (String appName : mstApplication.getAppNames()) {
                try {
                    pst.setString(1, appName.toUpperCase());
                    pst.setString(2, mstApplication.getActive());
                    pst.setString(3, mstApplication.getUpdatedBy());
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Application '" + appName.toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Application '" + appName.toUpperCase() + "'. No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Application '" + appName.toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), mstApplication.getUpdatedBy()) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique")) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Application '" + appName.toUpperCase() + "' Already exist in Master. </span><br/><br/>");
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstApplication.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Application '" + mstApplication.getAppName() + "' Already exist in Master");
            }
        }
        return md;
    }

    public static MessageDetails markInactive(MstApplication mstApplication) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_APPLICATION SET ACTIVE = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE APPL_ID = ?");) {
            pst.setString(1, mstApplication.getActive());
            pst.setString(2, mstApplication.getUpdatedBy());
            pst.setString(3, mstApplication.getAppId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstApplication.getUpdatedBy());
        }
        return md;
    }

    public static MessageDetails UpdateApplication(MstApplication mstApplication) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_APPLICATION SET APPL_NAME = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE APPL_ID = ?");) {
            pst.setString(1, mstApplication.getAppName().toUpperCase());
            pst.setString(2, mstApplication.getUpdatedBy());
            pst.setString(3, mstApplication.getAppId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstApplication.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Application '" + mstApplication.getAppName() + "' Already Exist");
                md.setFilemsgClass("text-danger");
            }
        }
        return md;
    }
}
