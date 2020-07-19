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
import viewModel.MstEquipment;

public class MstEquipmentDAO {

    public static MessageDetails insertEquipment(MstEquipment mstEquipment) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Insert into MST_EQUIPMENT(EQUIP_ID, EQUIP_NAME, MAKE_ID, CUST_ID, UPDATED_BY, "
                        + "UPDATED_DATETIME) values ((SELECT MAX(TO_NUMBER(REPLACE(EQUIP_ID, 'SYS', ''))) + 1 FROM MST_EQUIPMENT),?,?,?,?,"
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))")) {
            pst.setString(1, mstEquipment.getEquipmentName().toUpperCase());
            pst.setString(2, mstEquipment.getMstmake().getMakeId());
            pst.setString(3, mstEquipment.getCustId());
            pst.setString(4, mstEquipment.getUpdatedBy());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("Equipment Added Successfully");
            } else {
                md.setMsgClass("text-danger");
                md.setModalMessage("Error occured while adding Equipment. No changes were made");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstEquipment.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Duplicate Equipment or Equipment Already exist.");
                md.setFilemsgClass("text-danger");
            }
            md.setMsgClass("text-danger");
        }
        return md;
    }

    public static List<MstEquipment> listAllEquipments() {
        List<MstEquipment> mstEquipments = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM MST_EQUIPMENT")) {
            while (res.next()) {
                MstEquipment mEquip = new MstEquipment();
                mEquip.setEquipmentId(res.getString(1));
                mEquip.setEquipmentName(res.getString(2));
                mstEquipments.add(mEquip);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstEquipmentDAO.listAllEquipments()");
        }
        return mstEquipments;
    }

    public static List<MstEquipment> listAllActiveEquipments() {
        List<MstEquipment> mstEquipments = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT e1.equip_id, e1.equip_name, m1.make_id, m1.make_name, e1.updated_by, e1.updated_datetime FROM MST_EQUIPMENT e1 inner join mst_make m1 on e1.make_id = m1.make_id where e1.active = 1")) {
            while (res.next()) {
                MstEquipment mEquip = new MstEquipment();
                mEquip.setEquipmentId(res.getString(1));
                mEquip.setEquipmentName(res.getString(2));
                mEquip.getMstmake().setMakeId(res.getString(3));
                mEquip.getMstmake().setMakeName(res.getString(4));
                mEquip.setUpdatedBy(res.getString(5));
                mEquip.setUpdatedDatetime(res.getString(6));
                mstEquipments.add(mEquip);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstEquipmentDAO.listAllActiveEquipments()");
        }
        return mstEquipments;
    }

    public static List<MstEquipment> listAllDeactivatedEquipments() {
        List<MstEquipment> mstEquipments = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT equip_id, equip_name FROM MST_EQUIPMENT where active = 0")) {
            while (res.next()) {
                MstEquipment mEquip = new MstEquipment();
                mEquip.setEquipmentId(res.getString(1));
                mEquip.setEquipmentName(res.getString(2));
                mstEquipments.add(mEquip);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstEquipmentDAO.listAllDeactivatedEquipments()");
        }
        return mstEquipments;
    }

    public static List<MstEquipment> listAllEquipments(String CustomerId) {
        List<MstEquipment> mstEquipments = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT mst_equipment.equip_id, mst_equipment.equip_name, mst_make.make_name, "
                        + "mst_equipment.updated_by, mst_equipment.updated_datetime FROM ((mst_equipment left join mst_customer ON "
                        + "mst_equipment.cust_id = mst_customer.cust_id) left join mst_make ON mst_equipment.make_id = mst_make.make_id) WHERE "
                        + "mst_equipment.cust_id = ?");) {
            pst.setString(1, CustomerId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstEquipment mEquip = new MstEquipment();
                mEquip.setEquipmentId(res.getString(1));
                mEquip.setEquipmentName(res.getString(2));
                mEquip.getMstmake().setMakeName(res.getString(3));
                mEquip.setUpdatedBy(res.getString(4));
                mEquip.setUpdatedDatetime(res.getString(5));
                mstEquipments.add(mEquip);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstEquipmentDAO.listAllEquipments(String CustomerId)");
        }
        return mstEquipments;
    }

    public static List<MstEquipment> listAllEquipments(String CustomerId, String MakeId) {
        List<MstEquipment> mstEquipments = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT mst_equipment.equip_id, mst_equipment.equip_name, mst_make.make_name, mst_customer.cust_name "
                        + "FROM ((mst_equipment left join mst_customer ON mst_equipment.cust_id = mst_customer.cust_id) left join mst_make ON "
                        + "mst_equipment.make_id = mst_make.make_id) WHERE  mst_equipment.cust_id = ? AND mst_equipment.make_id = ?");) {
            pst.setString(1, CustomerId);
            pst.setString(2, MakeId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstEquipment mEquip = new MstEquipment();
                mEquip.setEquipmentId(res.getString(1));
                mEquip.setEquipmentName(res.getString(2));
                mEquip.getMstmake().setMakeName(res.getString(3));
                mstEquipments.add(mEquip);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstEquipmentDAO.listAllEquipments(String CustomerId, String MakeId)");
        }
        return mstEquipments;
    }

    public static List<MstEquipment> listAllEquipments(String CustomerId, String MakeId, String EquipmentId) {
        List<MstEquipment> mstEquipments = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT mst_equipment.equip_id, mst_equipment.equip_name, mst_make.make_name, mst_customer.cust_name "
                        + "FROM ((mst_equipment left join mst_customer ON mst_equipment.cust_id = mst_customer.cust_id) left join mst_make ON "
                        + "mst_equipment.make_id = mst_make.make_id) WHERE  mst_equipment.cust_id = ? AND mst_equipment.make_id = ? and mst_equipment.equip_id = ?");) {
            pst.setString(1, CustomerId);
            pst.setString(2, MakeId);
            pst.setString(3, EquipmentId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstEquipment mEquip = new MstEquipment();
                mEquip.setEquipmentId(res.getString(1));
                mEquip.setEquipmentName(res.getString(2));
                mstEquipments.add(mEquip);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstEquipmentDAO.listAllEquipments(String CustomerId)");
        }
        return mstEquipments;
    }

    public static MessageDetails updateEquipment(MstEquipment mstEquipment) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_EQUIPMENT SET EQUIP_NAME = ?, MAKE_ID = ?, CUST_ID = ?, UPDATED_BY = ?, "
                        + "UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE EQUIP_ID = ?")) {
            md = checkIfFound(mstEquipment, md);
            if (md.isStatus()) {
                pst.setString(1, mstEquipment.getEquipmentName().toUpperCase());
                pst.setString(2, mstEquipment.getMstmake().getMakeId());
                pst.setString(3, mstEquipment.getCustId());
                pst.setString(4, mstEquipment.getUpdatedBy());
                pst.setString(5, mstEquipment.getEquipmentId());
                if (pst.executeUpdate() > 0) {
                    md.setMsgClass("text-success");
                    md.setModalMessage("Equipment Updated Successfully");
                } else {
                    md.setMsgClass("text-danger");
                    md.setModalMessage("Error occured while Updating Equipment. No changes were made");
                }
            } else {
                md.setMsgClass("text-danger");
                md.setModalMessage("Cannot Change Customer as " + mstEquipment.getEquipmentName() + " already is mapped");
            }

        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstEquipment.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Duplicate Equipment or Equipment already exist");
                md.setFilemsgClass("text-danger");
            }
            md.setMsgClass("text-danger");

        }
        return md;
    }

    public static MessageDetails updateEquipment2(MstEquipment mstEquipment) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_EQUIPMENT SET EQUIP_NAME = ?, MAKE_ID = ?, UPDATED_BY = ?, "
                        + "UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE EQUIP_ID = ?")) {
            pst.setString(1, mstEquipment.getEquipmentName().toUpperCase());
            pst.setString(2, mstEquipment.getMstmake().getMakeId());
            pst.setString(3, mstEquipment.getUpdatedBy());
            pst.setString(4, mstEquipment.getEquipmentId());
            if (pst.executeUpdate() > 0) {
                md.setMsgClass("text-success");
                md.setModalMessage("Equipment Updated Successfully");
            } else {
                md.setMsgClass("text-danger");
                md.setModalMessage("Error occured while Updating Equipment. No changes were made");
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstEquipment.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Duplicate Equipment or Equipment already exist");
                md.setFilemsgClass("text-danger");
            }
            md.setMsgClass("text-danger");

        }
        return md;
    }

    public static MessageDetails checkIfFound(MstEquipment mstEquipment, MessageDetails md) {
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT DISTINCT(CUST_ID) FROM CUST_SAMPLE_INFO WHERE EQUIP_ID = ?");) {
            pst.setString(1, mstEquipment.getEquipmentId());
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                md.setStatus(mstEquipment.getCustId().equals(res.getString(1)));
            } else {
                md.setStatus(true);
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstEquipment.getUpdatedBy());
        }
        return md;
    }

    public static MessageDetails AddEquipment(MstEquipment mstEquip) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Insert into MST_EQUIPMENT(EQUIP_ID, EQUIP_NAME, MAKE_ID, CUST_ID, UPDATED_BY, "
                        + "UPDATED_DATETIME, ACTIVE, OTHERS_REMARKS) values ((SELECT COALESCE(MAX(TO_NUMBER(REPLACE(EQUIP_ID, 'SYS', ''))),0) + 1 FROM MST_EQUIPMENT),?,?,?,?,"
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'),?,?)")) {
            md.setModalMessage("");
            for (int i = 0; i < mstEquip.getEquipmentNames().length; i++) {
                try {
                    pst.setString(1, mstEquip.getEquipmentNames()[i].toUpperCase());
                    pst.setString(2, mstEquip.getEquipmentMakes()[i]);
                    pst.setString(3, mstEquip.getCustId());
                    pst.setString(4, mstEquip.getUpdatedBy());
                    pst.setString(5, mstEquip.getActive());
                    pst.setString(6, mstEquip.getOtherRemarks()[i].toString());
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Equipment '" + mstEquip.getEquipmentNames()[i].toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Equipment '" + mstEquip.getEquipmentNames()[i].toUpperCase() + "'. No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Equipment '" + mstEquip.getEquipmentNames()[i].toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), mstEquip.getUpdatedBy()) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique")) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Equipment '" + mstEquip.getEquipmentNames()[i].toUpperCase() + "' Already exist in Master. </span><br/><br/>");
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstEquip.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Duplicate Equipment or Equipment Already exist.");
                md.setFilemsgClass("text-danger");
            }
            md.setMsgClass("text-danger");
        }
        return md;
    }
}
