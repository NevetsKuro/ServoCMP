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
import viewModel.MstIndustry;
import viewModel.StevenModels.IndustryDetails;

public class MstIndustryDAO {

    public static List<MstIndustry> listAllIndustry(String query) {
        List<MstIndustry> mstIndustry = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);) {
            while (res.next()) {
                MstIndustry mInd = new MstIndustry();
                mInd.setIndId(res.getString(1));
                mInd.setIndName(res.getString(2));
                mInd.setActive(res.getString(3));
                mInd.setUpdatedBy(res.getString(4));
                mInd.setUpdatedDate(res.getString(5));
                mstIndustry.add(mInd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstIndustryDAO.listAllIndustry()");
        }
        return mstIndustry;
    }

    public static MstIndustry getIndustry(MstIndustry mstIndustry) {
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT IND_ID, IND_NAME, ACTIVE FROM MST_INDUSTRY WHERE IND_ID = ?");) {
            pst.setString(1, mstIndustry.getIndId());
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                mstIndustry.setIndId(res.getString(1));
                mstIndustry.setIndName(res.getString(2));
                mstIndustry.setActive(res.getString(3));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstIndustryDAO.getIndustry(MstIndustry mstIndustry)");
        }
        return mstIndustry;
    }

    public static MessageDetails AddIndustry(MstIndustry mstIndustry) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_INDUSTRY (IND_ID, IND_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME) "
                        + "VALUES ((SELECT MAX(TO_NUMBER(REPLACE(IND_ID, 'SYS', ''))) + 1 FROM MST_INDUSTRY), ?, ?, ?, "
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))");) {
            md.setModalMessage("");
            for (String indName : mstIndustry.getIndNames()) {
                try {
                    pst.setString(1, indName.toUpperCase());
                    pst.setString(2, mstIndustry.getActive());
                    pst.setString(3, mstIndustry.getUpdatedBy());
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Industry '" + indName.toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Industry '" + indName.toUpperCase() + "'. No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Industry '" + indName.toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), mstIndustry.getUpdatedBy()) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique")) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Industry '" + indName.toUpperCase() + "' Already exist in Master. </span><br/><br/>");
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstIndustry.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Industry '" + mstIndustry.getIndName() + "' Already exist in Master");
            }
        }
        return md;
    }

    public static MessageDetails UpdateIndustry(MstIndustry mstIndustry) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_INDUSTRY SET IND_NAME = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE IND_ID = ?");) {
            pst.setString(1, mstIndustry.getIndName().toUpperCase());
            pst.setString(2, mstIndustry.getUpdatedBy());
            pst.setString(3, mstIndustry.getIndId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstIndustry.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Industry " + mstIndustry.getIndName() + " Already Exist");
                md.setFilemsgClass("text-danger");
            }
        }
        return md;
    }

    public static MessageDetails markInactive(MstIndustry mstIndustry) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_INDUSTRY SET ACTIVE = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE IND_ID = ?");) {
            pst.setString(1, mstIndustry.getActive());
            pst.setString(2, mstIndustry.getUpdatedBy());
            pst.setString(3, mstIndustry.getIndId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, "markInactive()");
        }
        return md;
    }

    public static ArrayList<IndustryDetails> getIndustryDetails(String indCode) {
        ArrayList<IndustryDetails> indDets = new ArrayList<IndustryDetails>();
        String query = "";
        if ("999999".equals(indCode)) {
            query = "SELECT CUST_NAME,COUNT(*) from MST_CUSTOMER C LEFT JOIN MST_TANK T on C.CUST_ID = T.CUST_ID group by CUST_NAME";
        } else {
            query = "SELECT CUST_NAME,COUNT(*) from MST_CUSTOMER C LEFT JOIN MST_TANK T on C.CUST_ID = T.CUST_ID where T.IND_ID = ? group by CUST_NAME";
        }
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            if (!"999999".equals(indCode)) {
                pst.setString(1, indCode);
            }
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                IndustryDetails ind = new IndustryDetails();
                ind.setCustName(rs.getString(1));
                ind.setTanks(rs.getString(2));
                indDets.add(ind);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, indCode + " error");
        }
        return indDets;
    }
}
