package DAOs;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import operations.ApplicationSQLDate;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import viewModel.MessageDetails;
import viewModel.MstTank;

public class MstTankDAO {

    public static List<MstTank> getAllTanks(String query, Map<String, Object> params) {
        int paramNumber = 1;
        List<MstTank> mstTank = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            for (String paramName : params.keySet()) {
                Object paramVal = params.get(paramName);
                if (paramVal != null) {
                    pst.setString(paramNumber, paramVal.toString());
                }
                paramNumber++;
            }
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstTank mTank = new MstTank();
                    mTank.setTankId(res.getString(1));
                    mTank.setIndName(res.getString(2));
                    mTank.setCustName(res.getString(3));
                    mTank.setDeptName(res.getString(4));
                    mTank.setApplName(res.getString(5));
                    mTank.setEquipName(res.getString(6));
                    mTank.setProName(res.getString(7));
                    mTank.setTankNo(res.getString(8));
                    mTank.setTankDesc(res.getString(9));
                    mTank.setApplDesc(res.getString(10));
                    mTank.setCapacity(res.getString(11));
                    mTank.setSamplingNo(res.getString(12));
                    mTank.setStrPrevDate(ApplicationSQLDate.convertUtilDatetoString(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(13))));
                    mTank.setStrNxtDate(ApplicationSQLDate.convertUtilDatetoString(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14))));
                    mTank.setUpdatedBy(res.getString(15));
                    mTank.setUpdatedDateTime(res.getString(16));
                    mTank.setSampleFreq(res.getString(17));
                    mTank.setLastOilChange(res.getString(18));
                    mTank.getMstEquipment().getMstmake().setMakeName(res.getString(19)); 
                    mstTank.add(mTank);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTankDAO.getAllTank()");
        }
        return mstTank;
    }

    public static MessageDetails InsertTank(MstTank mTank, String[] testid) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                CallableStatement cs = con.prepareCall("CALL ADD_TANK_PROCEDURE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            ArrayDescriptor des = ArrayDescriptor.createDescriptor("ARRAY_TABLE", con);
            ARRAY testIds = new ARRAY(des, con, testid);
            cs.setString(1, "");
            cs.setString(2, mTank.getIndId());
            cs.setString(3, mTank.getCustId());
            cs.setString(4, mTank.getDeptId());
//            cs.setString(4, null); // testing purpose
            cs.setString(5, mTank.getApplId());
            cs.setString(6, mTank.getEquipId());
            cs.setString(7, mTank.getProId());
            cs.setString(8, mTank.getTankNo());
            cs.setString(9, mTank.getTankDesc());
            cs.setString(10, mTank.getApplDesc());
            cs.setString(11, mTank.getCapacity());
            cs.setString(12, mTank.getSamplingNo());
            cs.setString(13, mTank.getSampleFreq());
            cs.setDate(14, ApplicationSQLDate.convertUtiltoSqlDate(mTank.getPrevSampleDate()));
            cs.setDate(15, ApplicationSQLDate.convertUtiltoSqlDate(mTank.getNxtSampleDate()));
            cs.setDate(16, ApplicationSQLDate.convertUtiltoSqlDate(mTank.getNxtSampleDate()));
            cs.setString(17, mTank.getPostponeFlag());
            cs.setString(18, mTank.getPostponeCount());
            cs.setString(19, mTank.getLastOilChange());
            cs.setString(20, mTank.getUpdatedBy());
            cs.setDate(21, ApplicationSQLDate.getcurrentSQLDate());
            cs.setString(22, mTank.getActive());
            cs.setString(23, mTank.getProductGrade());
            cs.setString(24, mTank.getProductName());
            cs.setArray(25, testIds);
            cs.registerOutParameter(26, java.sql.Types.VARCHAR);
            cs.registerOutParameter(27, java.sql.Types.VARCHAR);
            cs.executeUpdate();
            md.setModalMessage(cs.getString(26) + " " + cs.getString(27));
            md.setMsgClass("text-success");
            if (md.getModalMessage().contains("Error") || md.getModalMessage().contains("Duplicate")) {
                md.setMsgClass("text-danger");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mTank.getUpdatedBy());
        }
        return md;
    }

    public static List<MstTank> listAllTank() {
        List<MstTank> mstTank = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM MST_TANK")) {
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setTankId(res.getString(1));
                mTank.getMstEquipment().setEquipmentId(res.getString(2));
                mTank.setTankNo(res.getString(3));
                mTank.setTankDesc(res.getString(4));
                mTank.setUpdatedBy(res.getString(5));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTankDAO.listAllTank()");
        }
        return mstTank;
    }

    public static List<MstTank> listAllTank(String CustomerId) {
        List<MstTank> mstTank = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.TANK_ID, T1.EQUIP_ID, T1.TANK_NO, T1.TANK_DESC, T1.UPDATED_BY, T1.UPDATED_DATETIME, "
                        + "T2.EQUIP_NAME FROM MST_TANK T1, MST_EQUIPMENT T2 where T2.EQUIP_ID = T1.EQUIP_ID AND T2.CUST_ID = ?");) {
            pst.setString(1, CustomerId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setTankId(res.getString(1));
                mTank.getMstEquipment().setEquipmentId(res.getString(2));
                mTank.setTankNo(res.getString(3));
                mTank.setTankDesc(res.getString(4));
                mTank.setUpdatedBy(res.getString(5));
                mTank.setUpdatedDateTime(res.getString(6));
                mTank.getMstEquipment().setEquipmentName(res.getString(7));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTankDAO.listAllTank()");
        }
        return mstTank;
    }

    public static List<MstTank> listAllTank(String CustomerId, String EquipmentId) {
        List<MstTank> mstTank = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.TANK_ID, T1.EQUIP_ID, T1.TANK_NO, T1.TANK_DESC, T1.UPDATED_BY, T1.UPDATED_DATETIME, T2.EQUIP_NAME "
                        + "FROM MST_TANK T1, MST_EQUIPMENT T2 where T2.EQUIP_ID = T1.EQUIP_ID AND T2.CUST_ID = ? AND T1.EQUIP_ID = ?");) {
            pst.setString(1, CustomerId);
            pst.setString(2, EquipmentId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setTankId(res.getString(1));
                mTank.getMstEquipment().setEquipmentId(res.getString(2));
                mTank.setTankNo(res.getString(3));
                mTank.setTankDesc(res.getString(4));
                mTank.setUpdatedBy(res.getString(5));
                mTank.setUpdatedDateTime(res.getString(6));
                mTank.getMstEquipment().setEquipmentName(res.getString(7));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTankDAO.listAllTank()");
        }
        return mstTank;
    }

    public static List<MstTank> listAllTank(String CustomerId, String EquipmentId, String TankNo) {
        List<MstTank> mstTank = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.TANK_ID, T1.EQUIP_ID, T1.TANK_NO, T1.TANK_DESC, T1.UPDATED_BY, T1.UPDATED_DATETIME, T2.EQUIP_NAME "
                        + "FROM MST_TANK T1, MST_EQUIPMENT T2 where T2.EQUIP_ID = T1.EQUIP_ID AND T2.CUST_ID = ? AND T1.EQUIP_ID = ? AND T1.TANK_ID = ?");
                ) {
            pst.setString(1, CustomerId);
            pst.setString(2, EquipmentId);
            pst.setString(3, TankNo);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setTankId(res.getString(1));
                mTank.getMstEquipment().setEquipmentId(res.getString(2));
                mTank.setTankNo(res.getString(3));
                mTank.setTankDesc(res.getString(4));
                mTank.setUpdatedBy(res.getString(5));
                mTank.setUpdatedDateTime(res.getString(6));
                mTank.getMstEquipment().setEquipmentName(res.getString(7));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTankDAO.listAllTank()");
        }
        return mstTank;
    }

    public static MessageDetails deleteTank(MstTank mTank) {
        MessageDetails md = new MessageDetails();
        try ( Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("DELETE FROM SAMPLE_TEST_INFO WHERE TANK_ID = ?");
                ){
            pst.setString(1, mTank.getTankId());
            con.setAutoCommit(false);

            if (pst.executeUpdate() > 0) {
                System.out.println("deleted from SAMPLE_TEST_INFO where tank id is " + mTank.getTankId());
                PreparedStatement pst1 = con.prepareStatement("DELETE FROM MST_TANK WHERE TANK_ID = ? AND SAMPLING_NO = ?");
                pst1.setString(1, mTank.getTankId());
                pst1.setString(2, mTank.getSamplingNo());
                if (pst1.executeUpdate() > 0) {
                    System.out.println("deleted from MST_TANK where tank id is " + mTank.getTankId());
                    con.commit();
                    md.setMsgClass("text-success");
                    md.setModalMessage("Tank deleted Successfully.");
                } else {
                    System.out.println("Error : while deleting in second table");
                    con.rollback();
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while deleting Tank. No Changes were made.");
                }
            } else {
                System.out.println("Error : while deleting in first table");
                md.setMsgClass("text-danger");
                md.setModalMessage("Error occured while deleting Tank. No Changes were made.");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, "MstTankDAO.deleteTank()");
            md.setMsgClass("text-danger");
            md.setModalMessage("Error occured while deleting Tank. No Changes were made.");
            if (ex.getMessage().contains("child")) {
                md.setMsgClass("text-danger");
                md.setModalMessage("There are Sample(s) Associated with the Tank You are trying to delete, hence it cannot be deleted.");
            }
        }
        return md;
    }

    public static MessageDetails UpdateTank(MstTank mTank) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_TANK SET EQUIP_ID = ?, TANK_NO = ?, TANK_DESC = ?, UPDATED_BY = ?, "
                        + "UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE TANK_ID = ?")) {
            pst.setString(1, mTank.getMstEquipment().getEquipmentId());
            pst.setString(2, mTank.getTankNo());
            pst.setString(3, mTank.getTankDesc());
            pst.setString(4, mTank.getUpdatedBy());
            pst.setString(5, mTank.getTankId());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("Tank Updated Successfully.");
            } else {
                md.setMsgClass("text-danger");
                md.setModalMessage("Error occured while Updating Tank. No Changes were made.");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mTank.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Duplicate Tank or Tank already exist");
                md.setFilemsgClass("text-danger");
            }
            md.setMsgClass("text-danger");
        }
        return md;
    }

    public static MstTank getTank(MstTank mTank) {
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.TANK_ID, T1.EQUIP_ID, T1.TANK_NO, T1.TANK_DESC, T1.UPDATED_BY, "
                        + "T1.UPDATED_DATETIME, T2.EQUIP_NAME FROM MST_TANK T1, MST_EQUIPMENT T2 WHERE TANK_ID = ? and T2.EQUIP_ID = T1.EQUIP_ID")) {
            pst.setString(1, mTank.getTankId());
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                mTank.setTankId(res.getString(1));
                mTank.getMstEquipment().setEquipmentId(res.getString(2));
                mTank.setTankNo(res.getString(3));
                mTank.setTankDesc(res.getString(4));
                mTank.setUpdatedBy(res.getString(5));
                mTank.setUpdatedDateTime(res.getString(6));
                mTank.getMstEquipment().setEquipmentName(res.getString(7));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTankDAO.getTank()");
        }
        return mTank;
    }

    public static List<MstTank> listAllTank(String industry, String customer, String department, String application, String equipment, String product) {
        List<MstTank> mstTank = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT TANK_ID, TANK_NO, TANK_DESC FROM MST_TANK WHERE IND_ID = ? AND CUST_ID = ?" 
                        + " AND DEPT_ID = ? AND APPL_ID = ? AND EQUIP_ID = ? AND PROD_ID = ?");
                ) {
            pst.setString(1, industry);
            pst.setString(2, customer);
            pst.setString(3, department);
            pst.setString(4, application);
            pst.setString(5, equipment);
            pst.setString(6, product);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setTankId(res.getString(1));
                mTank.setTankNo(res.getString(2));
                mTank.setTankDesc(res.getString(3));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstTankDAO.listAllTank()");
        }
        return mstTank;
    }
}
