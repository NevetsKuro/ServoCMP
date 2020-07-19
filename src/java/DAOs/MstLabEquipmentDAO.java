package DAOs;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import viewModel.MessageDetails;
import viewModel.MstLab;
import viewModel.MstLabEquipment;

public class MstLabEquipmentDAO {

    public static MstLab getUserLab(String empCode) {
        MstLab mLab = new MstLab();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT LAB_LOC_CODE, LAB_NAME FROM MST_LAB WHERE EMP_CODE = ?");) {
            pst.setString(1, empCode);
            try (ResultSet res = pst.executeQuery();) {
                if (res.next()) {
                    mLab.setLabCode(res.getString(1));
                    mLab.setLabName(res.getString(2));
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return mLab;
    }

    public static List<MstLabEquipment> getAllLabEquipments(String Query) {
        List<MstLabEquipment> mstLab = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(Query);) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstLabEquipment mLab = new MstLabEquipment();
                    mLab.setLabEquipId(res.getString(1));
                    mLab.setLabCode(res.getString(2));
                    mLab.setLabName(res.getString(3));
                    mLab.setLabEquipName(res.getString(4));
                    mLab.setMakeName(res.getString(5));
                    mLab.setOperationalStatus(res.getString(6));
                    mLab.setRemarks(res.getString(7));
                    mLab.setMethodName(res.getString(8));
                    mLab.setUpdatedBy(res.getString(9));
                    mLab.setUpdatedDatetime(res.getString(10));
                    mstLab.add(mLab);
                }
            }

        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstIndustryDAO.listAllIndustry()");
        }
        return mstLab;
    }

    public static List<MstLabEquipment> getEquipmentsForEmp(String EMP_CODE) {
        List<MstLabEquipment> equipList = new ArrayList<MstLabEquipment>();
        String Query = "SELECT MST_LAB_EQUIPMENT.LAB_EQUIP_ID, MST_LAB_EQUIPMENT.LAB_LOC_CODE, "
                + "MST_LAB.LAB_NAME, LAB_EQUIP_NAME, MAKE_NAME, OPERATIONAL_STATUS, REMARKS, "
                + "METHOD_NAME, MST_LAB_EQUIPMENT.UPDATED_BY, MST_LAB_EQUIPMENT.UPDATED_DATETIME "
                + "FROM MST_LAB_EQUIPMENT INNER JOIN MST_LAB ON MST_LAB.LAB_LOC_CODE = MST_LAB_EQUIPMENT.LAB_LOC_CODE "
                + "where MST_LAB_EQUIPMENT.active=1 AND MST_LAB.EMP_CODE =" + EMP_CODE;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(Query);) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstLabEquipment mLab = new MstLabEquipment();
                    mLab.setLabEquipId(res.getString(1));
                    mLab.setLabCode(res.getString(2));
                    mLab.setLabName(res.getString(3));
                    mLab.setLabEquipName(res.getString(4));
                    mLab.setMakeName(res.getString(5));
                    mLab.setOperationalStatus(res.getString(6));
                    mLab.setRemarks(res.getString(7));
                    mLab.setMethodName(res.getString(8));
                    mLab.setUpdatedBy(res.getString(9));
                    mLab.setUpdatedDatetime(res.getString(10));
                    equipList.add(mLab);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "LABOperations.getEquipments() ");
        }
        return equipList;
    }

    public static List<MstLabEquipment> getLabEquipments() {
        List<MstLabEquipment> mstLab = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT MST_LAB_EQUIPMENT.LAB_EQUIP_ID, MST_LAB_EQUIPMENT.LAB_LOC_CODE, "
                        + "MST_LAB.LAB_NAME, LAB_EQUIP_NAME, MAKE_NAME, OPERATIONAL_STATUS, REMARKS, METHOD_NAME, MST_LAB_EQUIPMENT.UPDATED_BY, MST_LAB_EQUIPMENT.UPDATED_DATETIME FROM MST_LAB_EQUIPMENT INNER "
                        + "JOIN MST_LAB ON MST_LAB.LAB_LOC_CODE = MST_LAB_EQUIPMENT.LAB_LOC_CODE");) {
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstLabEquipment mLab = new MstLabEquipment();
                    mLab.setLabEquipId(res.getString(1));
                    mLab.setLabCode(res.getString(2));
                    mLab.setLabName(res.getString(3));
                    mLab.setLabEquipName(res.getString(4));
                    mLab.setMakeName(res.getString(5));
                    mLab.setOperationalStatus(res.getString(6));
                    mLab.setRemarks(res.getString(7));
                    mLab.setMethodName(res.getString(8));
                    mLab.setUpdatedBy(res.getString(9));
                    mLab.setUpdatedDatetime(res.getString(10));
                    mstLab.add(mLab);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstIndustryDAO.listAllIndustry()");
        }
        return mstLab;
    }

    public static MessageDetails addLabEquipment(MstLabEquipment mLab) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_LAB_EQUIPMENT (LAB_EQUIP_ID, LAB_LOC_CODE, "
                        + "LAB_EQUIP_NAME, MAKE_NAME, OPERATIONAL_STATUS, METHOD_NAME, REMARKS, UPDATED_BY,UPDATED_DATETIME, ACTIVE) VALUES "
                        + "((SELECT COALESCE(MAX(TO_NUMBER(REPLACE(LAB_EQUIP_ID, 'SYS', ''))),0) + 1 FROM MST_LAB_EQUIPMENT), ?, ?, ?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'), ?)");) {
            pst.setString(1, mLab.getLabCode());
            pst.setString(2, mLab.getLabEquipName().toUpperCase());
            pst.setString(3, mLab.getMakeName());
            pst.setString(4, mLab.getOperationalStatus());
            pst.setString(5, mLab.getMethodName());
            pst.setString(6, mLab.getRemarks());
            pst.setString(7, mLab.getUpdatedBy());
            pst.setString(8, mLab.getActive());
            if (pst.executeUpdate() > 0) {
                md.setModalMessage("Lab Equipment added Successfully.");
                md.setMsgClass("text-success");
            } else {
                md.setModalMessage("Failed to add Lab Equipment.");
                md.setMsgClass("text-danger");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mLab.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
//                md.setFileMsg("Equipment " + mLab.getLabEquipName() + " Already Exist");
//                md.setFilemsgClass("text-danger");
                md.setModalMessage("Equipment " + mLab.getLabEquipName() + " Already Exist");
                md.setMsgClass("text-danger");
            }
        }
        return md;
    }

    public static MessageDetails markInactive(MstLabEquipment mLab) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_LAB_EQUIPMENT SET ACTIVE = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE LAB_EQUIP_ID = ?");) {
            pst.setString(1, mLab.getActive());
            pst.setString(2, mLab.getUpdatedBy());
            pst.setString(3, mLab.getLabEquipId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mLab.getUpdatedBy());
        }
        return md;
    }

    public static MessageDetails UpdateLabEquipment(MstLabEquipment mLab) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_LAB_EQUIPMENT SET LAB_EQUIP_NAME = ?, MAKE_NAME = ? , METHOD_NAME = ? ,  UPDATED_BY = ?, OPERATIONAL_STATUS = ? , REMARKS = ? , UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE LAB_EQUIP_ID = ?");) {
            pst.setString(1, mLab.getLabEquipName());
            pst.setString(2, mLab.getMakeName());
            pst.setString(3, mLab.getMethodName());
            pst.setString(4, mLab.getUpdatedBy());
            pst.setString(5, mLab.getOperationalStatus());
            pst.setString(6, mLab.getRemarks());
            pst.setString(7, mLab.getLabEquipId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mLab.getUpdatedBy());
        }
        return md;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
