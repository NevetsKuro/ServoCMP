package DAOs;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import operations.ApplicationSQLDate;
import viewModel.MessageDetails;
import viewModel.MstTank;

public class MstCustSampleDAO {

    public static List<MstTank> listAllSamples() {
        List<MstTank> sampleInfo = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("");) {
            while (res.next()) {
                MstTank mstTank = new MstTank();
                mstTank.setTankId(res.getString(1));
                mstTank.setIndId(res.getString(2));
                mstTank.setCustId(res.getString(3));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstCustSampleDAO.listAllSamples()");
        }
        return sampleInfo;
    }

    public static List<MstTank> listAllSamples(String sqlQuery) {
        List<MstTank> mstTank = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sqlQuery);) {
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setTankId(res.getString(1));
                mTank.setIndId(res.getString(2));
                mTank.setIndName(res.getString(3));
                mTank.setCustId(res.getString(4));
                mTank.setCustName(res.getString(5));
                mTank.setDeptId(res.getString(6));
                mTank.setDeptName(res.getString(7));
                mTank.setApplId(res.getString(8));
                mTank.setApplName(res.getString(9));
                mTank.setEquipId(res.getString(10));
                mTank.setEquipName(res.getString(11));
                mTank.setProId(res.getString(12));
                mTank.setProName(res.getString(13));
                mTank.setTankNo(res.getString(14));
                mTank.setTankDesc(res.getString(15));
                mTank.setApplDesc(res.getString(16));
                mTank.setCapacity(res.getString(17));
                mTank.setSamplingNo(res.getString(18));
                mTank.setSampleFreq(res.getString(19));
                mTank.setPrevSampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(20)));
                mTank.setStrPrevDate(ApplicationSQLDate.convertUtilDatetoString(mTank.getPrevSampleDate()));
                mTank.setNxtSampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(21)));
                mTank.setStrNxtDate(ApplicationSQLDate.convertUtilDatetoString(mTank.getNxtSampleDate()));
                mTank.setPostponeFlag(res.getString(23));
                mTank.setPostponeCount(res.getString(24));
                mTank.setLastOilChange(res.getString(25));
                mTank.setUpdatedBy(res.getString(26));
                mTank.setUpdatedDateTime(res.getString(27));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstCustSampleInfoDAO.listAllSamples(custSampleInfo)");
        }
        return mstTank;
    }

    public static MessageDetails insertSample(MstTank mstTank) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_TANK (TANK_ID, SAMPLING_NO, IND_ID, CUST_ID, "
                        + "DEPT_ID, APPL_ID, EQUIP_ID, TANK_NO, PROD_ID, CAPACITY, SAMPLE_FREQ, PREV_SAMPLE_DATE, NEXT_SAMPLE_DATE, "
                        + "OLD_NEXT_SAMPLE_DATE, POSTPONED_FLAG, POSTPONE_COUNT, LAST_OIL_CHANGED, APPL_DESC, UPDATED_BY, UPDATED_DATETIME) VALUES "
                        + "((SELECT MAX(TO_NUMBER(REPLACE(TANK_ID, 'SYS', ''))) + 1 FROM MST_TANK), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM')))");) {
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
            pst.setDate(11, ApplicationSQLDate.convertUtiltoSqlDate(mstTank.getPrevSampleDate()));
            pst.setDate(12, ApplicationSQLDate.convertUtiltoSqlDate(mstTank.getNxtSampleDate()));
            pst.setString(13, null);
            pst.setString(14, mstTank.getPostponeFlag());
            pst.setString(15, mstTank.getPostponeCount());
            pst.setString(16, mstTank.getLastOilChange());
            pst.setString(17, mstTank.getApplDesc());
            pst.setString(18, mstTank.getUpdatedBy());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("Sample Created Successfully");
            } else {
                md.setMsgClass("text-danger");
                md.setModalMessage("Error Occured while creating Sample. No changes were Made");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstTank.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("text-danger");
                md.setModalMessage("Duplicate Sample or Sample Already Exist.");
            }
        }
        return md;
    }

    public static MessageDetails updateSample(MstTank mstTank) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_TANK SET IND_ID = ?,CUST_ID = ?, DEPT_ID = ?, "
                        + "APPL_ID = ?, EQUIP_ID = ?, TANK_NO = ?, PROD_ID = ?, CAPACITY = ?, SAMPLE_FREQ = ?, PREV_SAMPLE_DATE = ?, "
                        + "NEXT_SAMPLE_DATE = ?, OLD_NEXT_SAMPLE_DATE = ?, POSTPONED_FLAG = ?, LAST_OIL_CHANGED = ?, "
                        + "APPL_DESC = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM')) WHERE TANK_ID = ?")) {
            pst.setString(1, mstTank.getIndId());
            pst.setString(2, mstTank.getCustId());
            pst.setString(3, mstTank.getDeptId());
            pst.setString(4, mstTank.getApplId());
            pst.setString(5, mstTank.getEquipId());
            pst.setString(6, mstTank.getTankNo());
            pst.setString(7, mstTank.getProId());
            pst.setString(8, mstTank.getCapacity());
            pst.setString(9, mstTank.getSampleFreq());
            pst.setDate(10, ApplicationSQLDate.convertUtiltoSqlDate(mstTank.getPrevSampleDate()));
            pst.setDate(11, ApplicationSQLDate.convertUtiltoSqlDate(mstTank.getNxtSampleDate()));
            pst.setString(12, null);
            pst.setString(13, mstTank.getPostponeFlag());
            pst.setString(14, mstTank.getLastOilChange());
            pst.setString(15, mstTank.getApplDesc());
            pst.setString(16, mstTank.getUpdatedBy());
            pst.setString(17, mstTank.getTankId());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("Sample Updated Successfully");
            } else {
                md.setMsgClass("text-danger");
                md.setModalMessage("Error Occured while Updating Sample. No changes were Made");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstTank.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("text-danger");
                md.setModalMessage("Duplicate Sample or Sample Already Exist.");
            }
        }
        return md;
    }
}
