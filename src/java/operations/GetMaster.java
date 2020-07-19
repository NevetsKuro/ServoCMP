package operations;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import viewModel.MstApplication;
import viewModel.MstCustomer;
import viewModel.MstDepartment;
import viewModel.MstEquipment;
import viewModel.MstIndustry;
import viewModel.MstProduct;
import viewModel.MstTank;

public class GetMaster {

    public static List<MstCustomer> getCustomers() {
        List<MstCustomer> mstCust = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT CUST_ID,CUST_NAME,IND_ID,EMP_CODE,UPDATED_BY,UPDATED_DATE,ACTIVE FROM MST_CUSTOMER WHERE ACTIVE = 1");) {
            while (res.next()) {
                MstCustomer mCust = new MstCustomer();
                mCust.setCustomerId(res.getString(1));
                mCust.setCustomerName(res.getString(2));
                mstCust.add(mCust);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getCustomers() ");
        }
        return mstCust;
    }

    public static List getCustomers(String industry) {
        List<MstCustomer> mstCust = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT CUST_ID,CUST_NAME,IND_ID,EMP_CODE,UPDATED_BY,UPDATED_DATE,ACTIVE FROM MST_CUSTOMER WHERE IND_ID = ? AND ACTIVE = 1");) {
            pst.setString(1, industry);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstCustomer mCust = new MstCustomer();
                mCust.setCustomerId(res.getString(1));
                mCust.setCustomerName(res.getString(2));
                mstCust.add(mCust);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getCustomers(industry) ");
        }
        return mstCust;
    }

    public static List getDepartment(String custId) {
        List<MstDepartment> mstDept = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT CUST_ID,CUST_NAME,IND_ID,EMP_CODE,UPDATED_BY,UPDATED_DATE,ACTIVE FROM MST_DEPARTMENT WHERE CUST_ID = ? AND ACTIVE = 1");) {
            pst.setString(1, custId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstDepartment mDept = new MstDepartment();
                mDept.setDepartmentId(res.getString(1));
                mDept.setDepartmentName(res.getString(2));
                mstDept.add(mDept);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getDepartment(custId) ");
        }
        return mstDept;
    }

    public static List<MstDepartment> getDepartments() {
        List<MstDepartment> mstDept = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT DEPT_ID, DEPT_NAME FROM MST_DEPARTMENT WHERE ACTIVE = 1");) {
            while (res.next()) {
                MstDepartment mDept = new MstDepartment();
                mDept.setDepartmentId(res.getString(1));
                mDept.setDepartmentName(res.getString(2));
                mstDept.add(mDept);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getDepartments() ");
        }
        return mstDept;
    }

    public static List<MstEquipment> getEquipment(String customer) {
        List<MstEquipment> mstEquip = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT * FROM MST_EQUIPMENT WHERE CUST_ID = ? AND ACTIVE = 1");) {
            pst.setString(1, customer);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstEquipment mEquip = new MstEquipment();
                mEquip.setEquipmentId(res.getString(1));
                mEquip.setEquipmentName(res.getString(2));
                mstEquip.add(mEquip);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getEquipments(customer) ");
        }
        return mstEquip;
    }

    public static List<MstEquipment> getEquipments() {
        List<MstEquipment> mstEquip = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM MST_EQUIPMENT WHERE ACTIVE = 1");) {
            while (res.next()) {
                MstEquipment mEquip = new MstEquipment();
                mEquip.setEquipmentId(res.getString(1));
                mEquip.setEquipmentName(res.getString(2));
                mstEquip.add(mEquip);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getEquipment() ");
        }
        return mstEquip;
    }

    public static List<MstApplication> getApplications() {
        List<MstApplication> mstApp = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM MST_APPLICATION WHERE ACTIVE = 1");) {
            while (res.next()) {
                MstApplication mApp = new MstApplication();
                mApp.setAppId(res.getString(1));
                mApp.setAppName(res.getString(2));
                mstApp.add(mApp);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getApplications() ");
        }
        return mstApp;
    }

    public static List<MstIndustry> getIndustry() {
        List<MstIndustry> mstInd = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM MST_INDUSTRY WHERE ACTIVE = 1");) {
            while (res.next()) {
                MstIndustry mInd = new MstIndustry();
                mInd.setIndId(res.getString(1));
                mInd.setIndName(res.getString(2));
                mstInd.add(mInd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getIndustry() ");
        }
        return mstInd;
    }

    public static List<MstTank> getAvailableTank(String ind, String cust, String dept, String app, String equip) {
        List<MstTank> mstTank = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select TANK_ID, EQUIP_ID, TANK_NO, TANK_DESC from mst_tank where tank_no in(select tank_no from "
                        + "mst_tank where equip_id=? minus select tank_no  from MST_TANK where ind_id=? and "
                        + "cust_id=? and dept_id=? and appl_id=? and equip_id=?) and equip_id=?");) {
            pst.setString(1, equip);
            pst.setString(2, ind);
            pst.setString(3, cust);
            pst.setString(4, dept);
            pst.setString(5, app);
            pst.setString(6, equip);
            pst.setString(7, equip);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setTankId(res.getString(1));
                mTank.getMstEquipment().setEquipmentId(res.getString(2));
                mTank.setTankNo(res.getString(3));
                mTank.setTankDesc(res.getString(4));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getAvailableTank() ");
        }
        return mstTank;
    }

    public static Object getProducts() {
        List<MstProduct> mstProduct = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("Select * from MST_PRODUCT WHERE ACTIVE = 1")) {
            while (res.next()) {
                MstProduct mProduct = new MstProduct();
                mProduct.setProId(res.getString(1));
                mProduct.setProName(res.getString(2));
                mstProduct.add(mProduct);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getProducts()");
        }
        return mstProduct;
    }

    public static List getTanks(String Equipment) {
        List<MstTank> mstTank = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select TANK_ID, TANK_NO, TANK_DESC from MST_TANK WHERE EQUIP_ID=?");) {
            pst.setString(1, Equipment);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setTankId(res.getString(1));
                mTank.setTankNo(res.getString(2));
                mTank.setTankDesc(res.getString(3));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetMaster.getProducts()");
        }
        return mstTank;
    }

}
