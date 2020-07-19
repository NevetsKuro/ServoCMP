package operations;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import viewModel.MessageDetails;
import viewModel.MstIndustry;
import viewModel.MstIom;
import viewModel.SampleDetails;
import viewModel.StevenModels.productCategory;

public class TSEoperations {

    public static String getSampleCount(String sampleId) {
        String query = "select s.sample_id,l.LAB_NAME , s.status_id from sample_details s "
                + "inner join mst_lab l on s.LAB_LOC_CODE = l.LAB_LOC_CODE "
                + "where s.sample_id =?";
        String prodId = "";
        String alert = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, sampleId);
            ResultSet res = pst.executeQuery();
            String temp = "";
            String tempLab = "";
            while (res.next()) {
                temp = res.getString(3);
                if (!temp.equals("3")) {
                    tempLab = res.getString(2);
                    alert = "Sample pending with " + tempLab;
                    break;
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, sampleId);
        }
        return alert;
    }

    public static String isSingleSampleType(String tankId) {
        String query = "select single_sampling from mst_tank where tank_id = ?";
        int flag = 0;
        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, tankId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                if (res.getString(1).equals("1")) {
                    flag = 1;
                } else {
                    flag = 2;
                }
            }

        } catch (Exception ex) {
            MyLogger.logIt(ex, tankId);
        }
        if (flag == 1) {
            return "1";
        } else {
            return "2";
        }
    }

    public static String getSimProductForOMC(String tankId) {
        String query = "Select SIMILAR_PRODUCT from MST_TANK where TANK_ID = ?";
        String prodId = "";
        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, tankId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                prodId = res.getString(1);
            }

        } catch (Exception ex) {
            MyLogger.logIt(ex, tankId);
        }
        return prodId;
    }

    public static List getAllCreatedSamplesLabWiseSummary(String EMP_CODE, String strStatus, String labLocCode, String rowLimit) {
        List listpndSmpls = new ArrayList();
        String res1 = "SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                + "t5.APPL_NAME, t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.STATUS_ID, t1.QTY_DRAWN, t1.SAMPLE_DRAWN_DATE, "
                + "t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE,t1.LAB_RECEIVED_DATE, t1.SAMPLE_PRIORITY_ID, t8.LAB_NAME FROM "
                + "SAMPLE_DETAILS t1, MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, "
                + "MST_PRODUCT t7, MST_LAB t8 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID "
                + "AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.LAB_LOC_CODE=t8.LAB_LOC_CODE AND t1.PROD_ID=t7.PROD_ID and "
                + "t1.CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE='" + EMP_CODE + "') AND t1.STATUS_ID IN ('"
                + strStatus + "') and t1.LAB_LOC_CODE='" + labLocCode + "' order by t1.SAMPLE_PRIORITY_ID";

        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                        + "t5.APPL_NAME, t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.STATUS_ID, t1.QTY_DRAWN, t1.SAMPLE_DRAWN_DATE, "
                        + "t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE,t1.LAB_RECEIVED_DATE, t1.SAMPLE_PRIORITY_ID, t8.LAB_NAME FROM "
                        + "SAMPLE_DETAILS t1, MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, "
                        + "MST_PRODUCT t7, MST_LAB t8 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID "
                        + "AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.LAB_LOC_CODE=t8.LAB_LOC_CODE AND t1.PROD_ID=t7.PROD_ID and "
                        + "t1.CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE=?) AND t1.STATUS_ID IN (?) "
                        + "and t1.LAB_LOC_CODE=? and rownum <= ? order by t1.SAMPLE_PRIORITY_ID");) {
            pst.setString(1, EMP_CODE);
            pst.setString(2, strStatus);
            pst.setString(3, labLocCode);
            pst.setString(4, rowLimit);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(4));
                sd.getMstDept().setDepartmentName(res.getString(5));
                sd.getMstApp().setAppName(res.getString(6));
                sd.setTankNo(res.getString(7));
                sd.getMstEquip().setEquipmentName(res.getString(8));
                sd.getMstProd().setProName(res.getString(9));
                sd.setStatusId(res.getString(10));
                sd.setQtyDrawn(res.getString(11));
                sd.setSampledrawnDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(12)));
                sd.setSamplecreatedBy(res.getString(13));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14)));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                if (res.getDate(15) != null) {
                    sd.setLabrecDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(15)));
                    sd.setStringLabrecDate(ApplicationSQLDate.convertUtilDatetoString(sd.getLabrecDate()));
                }
                sd.setSamplepriorityId(res.getString(16));
                sd.getMstLab().setLabName(res.getString(17));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                sd.setStringsampledrawnDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSampledrawnDate()));
                listpndSmpls.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        return listpndSmpls;
    }

    public static List getintransitsampleDetails(String EMP_CODE, String strStatus, String rowLimit) {
        List listpndSmpls = new ArrayList();
        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                        + "t5.APPL_NAME, t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.STATUS_ID, t1.QTY_DRAWN, t1.SAMPLE_DRAWN_DATE, "
                        + "t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE,t1.LAB_RECEIVED_DATE, t1.SAMPLE_PRIORITY_ID, t8.LAB_NAME FROM "
                        + "SAMPLE_DETAILS t1, MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, "
                        + "MST_PRODUCT t7, MST_LAB t8 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID "
                        + "AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.LAB_LOC_CODE=t8.LAB_LOC_CODE AND t1.PROD_ID=t7.PROD_ID and "
                        + "t1.CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE=?) AND t1.STATUS_ID IN (?) "
                        + "and rownum <= ? order by t1.SAMPLE_PRIORITY_ID");) {
            pst.setString(1, EMP_CODE);
            pst.setString(2, strStatus);
            pst.setString(3, rowLimit);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(4));
                sd.getMstDept().setDepartmentName(res.getString(5));
                sd.getMstApp().setAppName(res.getString(6));
                sd.setTankNo(res.getString(7));
                sd.getMstEquip().setEquipmentName(res.getString(8));
                sd.getMstProd().setProName(res.getString(9));
                sd.setStatusId(res.getString(10));
                sd.setQtyDrawn(res.getString(11));
                sd.setSampledrawnDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(12)));
                sd.setSamplecreatedBy(res.getString(13));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14)));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                if (res.getDate(15) != null) {
                    sd.setLabrecDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(15)));
                    sd.setStringLabrecDate(ApplicationSQLDate.convertUtilDatetoString(sd.getLabrecDate()));
                }
                sd.setSamplepriorityId(res.getString(16));
                sd.getMstLab().setLabName(res.getString(17));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                sd.setStringsampledrawnDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSampledrawnDate()));
                listpndSmpls.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        return listpndSmpls;
    }

    public static Boolean getSingleSampling(String tank_id) {
        Boolean isSinSample = false;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select single_sampling from MST_TANK where tank_id = ?");) {
            pst.setString(1, tank_id);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                isSinSample = res.getString(1).equals("1") ? true : false;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return isSinSample;
    }

    public static List<SampleDetails> getintransitsampleDetails(String EMP_CODE, String strStatus, String pid, String did, String cid, String aid, String sampleType, String rowLimit) {
        List<SampleDetails> listpndSmpls = new ArrayList<SampleDetails>();
        String query = "";

        query = "SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                + "t5.APPL_NAME, t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.STATUS_ID, t1.QTY_DRAWN, t1.SAMPLE_DRAWN_DATE, "
                + "t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE,t1.LAB_RECEIVED_DATE, t1.SAMPLE_PRIORITY_ID, t8.LAB_NAME, t8.type, t8.lab_loc_code FROM "
                + "SAMPLE_DETAILS t1, MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, "
                + "MST_PRODUCT t7, MST_LAB t8,mst_tank t9 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID "
                + "AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.LAB_LOC_CODE=t8.LAB_LOC_CODE AND t1.PROD_ID=t7.PROD_ID and "
                + "t1.tank_id = t9.tank_id and t1.CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE=? ) AND t1.STATUS_ID IN (?) and rownum <= ? AND t1.sample_type = ? ";
        query += (!pid.equals("") ? "AND t1.PROD_ID = " + pid + " " : "");
        query += (!cid.equals("") ? "AND t1.CUST_ID = " + cid + " " : "");
        query += (!did.equals("") ? "AND t1.DEPT_ID = " + did + " " : "");
        query += (!aid.equals("") ? "AND t1.APPL_ID = " + aid + " " : "");
        query += "order by t1.SAMPLE_PRIORITY_ID";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, EMP_CODE);
            pst.setString(2, strStatus);
            pst.setString(3, rowLimit);
            pst.setString(4, sampleType);
//            if(!pid.equals("")){ 
//                pst.setString(4, pid);
//            }
//            if(!cid.equals("")){ 
//                pst.setString(5, cid);
//            }
//            if(!did.equals("")){ 
//                pst.setString(6, did);
//            }
//            if(!aid.equals("")){ 
//                pst.setString(7, aid);
//            }
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(4));
                sd.getMstDept().setDepartmentName(res.getString(5));
                sd.getMstApp().setAppName(res.getString(6));
                sd.setTankNo(res.getString(7));
                sd.getMstEquip().setEquipmentName(res.getString(8));
                sd.getMstProd().setProName(res.getString(9));
                sd.setStatusId(res.getString(10));
                sd.setQtyDrawn(res.getString(11));
                sd.setSampledrawnDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(12)));
                sd.setSamplecreatedBy(res.getString(13));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14)));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                if (res.getDate(15) != null) {
                    sd.setLabrecDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(15)));
                    sd.setStringLabrecDate(ApplicationSQLDate.convertUtilDatetoString(sd.getLabrecDate()));
                }
                sd.setSamplepriorityId(res.getString(16));
                sd.getMstLab().setLabName(res.getString(17));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                sd.setStringsampledrawnDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSampledrawnDate()));
                sd.setIsSingleSampling(res.getString(18));
                sd.getMstLab().setLabCode(res.getString(19));
                listpndSmpls.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        return listpndSmpls;
    }

    public static List<SampleDetails> getintransitsampleLabDetails(String EMP_CODE, String strStatus, String pid, String did, String cid, String aid, String sampleType, String rowLimit) {
        List<SampleDetails> listpndSmpls = new ArrayList<SampleDetails>();
        String query = "";

        query = "SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                + "t5.APPL_NAME, t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.STATUS_ID, "
                + "LISTAGG(t1.QTY_DRAWN,',') WITHIN GROUP (ORDER BY t1.QTY_DRAWN) as qty_drawn, "
                + "t1.SAMPLE_DRAWN_DATE, t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE, "
                + "LISTAGG(t1.LAB_RECEIVED_DATE,',') WITHIN GROUP (ORDER BY t1.LAB_RECEIVED_DATE) as lab_rec, t1.SAMPLE_PRIORITY_ID, "
                + "LISTAGG(t8.lab_name,',')  WITHIN GROUP (ORDER BY t8.lab_name) as lab_name, "
                + "LISTAGG(t8.type,',')  WITHIN GROUP (ORDER BY t8.type) as lab_type, "
                + "LISTAGG(t8.lab_loc_code,',')  WITHIN GROUP (ORDER BY t8.lab_loc_code) as lab_Code "
                + "FROM SAMPLE_DETAILS t1, MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, MST_PRODUCT t7, MST_LAB t8,mst_tank t9 "
                + "WHERE t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID "
                + "AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.LAB_LOC_CODE=t8.LAB_LOC_CODE AND t1.PROD_ID=t7.PROD_ID AND "
                + "t1.tank_id = t9.tank_id AND t1.CUST_ID IN (SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE=? ) AND t1.STATUS_ID IN (?) and rownum <= ? AND t1.sample_type = ? ";
        query += (!pid.equals("") ? "AND t1.PROD_ID = " + pid + " " : "");
        query += (!cid.equals("") ? "AND t1.CUST_ID = " + cid + " " : "");
        query += (!did.equals("") ? "AND t1.DEPT_ID = " + did + " " : "");
        query += (!aid.equals("") ? "AND t1.APPL_ID = " + aid + " " : "");
        query += " GROUP BY t1.sample_id, t1.tank_id, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                + "t5.APPL_NAME, t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.STATUS_ID, t1.SAMPLE_DRAWN_DATE, "
                + "t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE, t1.SAMPLE_PRIORITY_ID order by t1.SAMPLE_PRIORITY_ID";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, EMP_CODE);
            pst.setString(2, strStatus);
            pst.setString(3, rowLimit);
            pst.setString(4, sampleType);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(4));
                sd.getMstDept().setDepartmentName(res.getString(5));
                sd.getMstApp().setAppName(res.getString(6));
                sd.setTankNo(res.getString(7));
                sd.getMstEquip().setEquipmentName(res.getString(8));
                sd.getMstProd().setProName(res.getString(9));
                sd.setStatusId(res.getString(10));
                sd.setQtyDrawn(res.getString(11));
                sd.setSampledrawnDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(12)));
                sd.setSamplecreatedBy(res.getString(13));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14)));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                if (res.getString(15) != null && res.getString(15).split(",").length > 0) {
//                    sd.setLabrecDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(15)));
                    sd.setStringLabrecDate(res.getString(15));
                }
                sd.setSamplepriorityId(res.getString(16));
                sd.getMstLab().setLabName(res.getString(17));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                sd.setStringsampledrawnDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSampledrawnDate()));
                sd.setIsSingleSampling(res.getString(18));
                sd.getMstLab().setLabCode(res.getString(19));
                listpndSmpls.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        return listpndSmpls;
    }

    public static List sendTseToLabSampleDetails(String EMP_CODE, String strStatus) {
        List listpndSmpls = new ArrayList();
        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                        + "t5.APPL_NAME, t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.STATUS_ID, t1.QTY_DRAWN, "
                        + "t1.SAMPLE_DRAWN_DATE, t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE,t1.LAB_RECEIVED_DATE, t1.SAMPLE_PRIORITY_ID,t1.LAB_LOC_CODE,t8.LAB_NAME FROM "
                        + "SAMPLE_DETAILS t1, MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, "
                        + "MST_EQUIPMENT t6, MST_PRODUCT t7, MST_LAB t8 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND "
                        + "t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND "
                        + "t1.PROD_ID=t7.PROD_ID and t1.LAB_LOC_CODE=T8.LAB_LOC_CODE and t1.CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE=?) "
                        + "AND t1.STATUS_ID IN (?) order by t1.SAMPLE_PRIORITY_ID");) {
            pst.setString(1, EMP_CODE);
            pst.setString(2, strStatus);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(4));
                sd.getMstDept().setDepartmentName(res.getString(5));
                sd.getMstApp().setAppName(res.getString(6));
                sd.setTankNo(res.getString(7));
                sd.getMstEquip().setEquipmentName(res.getString(8));
                sd.getMstProd().setProName(res.getString(9));
                sd.setStatusId(res.getString(10));
                sd.setQtyDrawn(res.getString(11));
                sd.setSampledrawnDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(12)));
                sd.setSamplecreatedBy(res.getString(13));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14)));
                if (res.getDate(15) != null) {
                    sd.setLabrecDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(15)));
                    sd.setStringLabrecDate(ApplicationSQLDate.convertUtilDatetoString(sd.getLabrecDate()));
                }
                sd.setSamplepriorityId(res.getString(16));
                sd.getMstLab().setLabCode(res.getString(17));
                sd.getMstLab().setLabName(res.getString(18));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                sd.setStringsampledrawnDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSampledrawnDate()));
                listpndSmpls.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        return listpndSmpls;
    }

    public static SampleDetails geteditsendsampletoLAB(String sample_id, String labCode) {
        SampleDetails sd = null;
        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, "
                        + "t5.APPL_NAME, t6.EQUIP_NAME, t1.TANK_NO, t1.PROD_ID, t7.PROD_NAME, t8.CAPACITY, t1.QTY_DRAWN, t1.SAMPLE_DRAWN_DATE, "
                        + "t1.SAMPLE_DRAWN_REMARKS, t1.SAMPLE_PRIORITY_ID, t1.SAMPLE_PRIORITY_REMARKS, t1.TOP_UP_QTY, t1.PREV_SAMPLE_DATE, "
                        + "t1.NEXT_SAMPLE_DATE, t1.SAMPLE_CREATED_DATE,t1.LAB_RECEIVED_DATE,t1.EXP_TEST_RESULT_DATE,t1.LAB_RECEIVED_REMARKS, "
                        + "t1.LAB_LOC_CODE,t1.RUNNING_HOURS_OIL,t8.APPL_DESC FROM SAMPLE_DETAILS t1, MST_INDUSTRY t2, MST_CUSTOMER t3, "
                        + "MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, MST_PRODUCT t7, MST_TANK T8 where t1.IND_ID=t2.IND_ID "
                        + "AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND "
                        + "t1.PROD_ID=t7.PROD_ID AND t1.TANK_ID=t8.TANK_ID AND t1.SAMPLE_ID=? AND t1.LAB_LOC_CODE = ?");) {
            pst.setString(1, sample_id);
            pst.setString(2, labCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(4));
                sd.getMstDept().setDepartmentName(res.getString(5));
                sd.getMstApp().setAppName(res.getString(6));
                sd.getMstEquip().setEquipmentName(res.getString(7));
                sd.setTankNo(res.getString(8));
                sd.getMstProd().setProId(res.getString(9));
                sd.getMstProd().setProName(res.getString(10));
                sd.getMstProd().setProCapacity(res.getString(11));
                sd.setQtyDrawn(res.getString(12));
                sd.setSampledrawnDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(13)));
                sd.setStringsampledrawnDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSampledrawnDate()));
                sd.setSampledrawnRemarks(res.getString(14));
                sd.setSamplepriorityId(res.getString(15));
                sd.setSamplepriorityRemarks(res.getString(16));
                sd.setTopupQty(res.getString(17));
                sd.setPresampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(18)));
                sd.setNxtsampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(19)));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(20)));
                if (res.getDate(21) != null) {
                    sd.setLabrecDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(21)));
                    sd.setStringLabrecDate(ApplicationSQLDate.convertUtilDatetoString(sd.getLabrecDate()));
                }
                if (res.getDate(22) != null) {
                    sd.setExptestresultDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(22)));
                    sd.setStringExptestresultDate(ApplicationSQLDate.convertUtilDatetoString(sd.getExptestresultDate()));
                }
                sd.setStringpresampleDate(ApplicationSQLDate.convertUtilDatetoString(sd.getPresampleDate()));
                sd.setStringnxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(sd.getNxtsampleDate()));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                if (res.getString(23) != null) {
                    sd.setLabrecRemarks(res.getString(23));
                }
                sd.getMstLab().setLabCode(res.getString(24));
                sd.setRunningHrs(res.getString(25));
                sd.setDescAppl(res.getString(26));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, sd.getSamplecreatedBy());
        }
        return sd;
    }

    public static MessageDetails postponeSample(SampleDetails sd) {
        MessageDetails md = new MessageDetails();

        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                CallableStatement cst = con.prepareCall("{call POSTPONED_SAMPLE_PROCEDURE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");) {
            if (con != null) {
                cst.setString(1, sd.getTankId());
                cst.setString(2, sd.getMstInd().getIndId());
                cst.setString(3, sd.getMstDept().getMstCustomer().getCustomerId());
                cst.setString(4, sd.getMstDept().getDepartmentId());
                cst.setString(5, sd.getMstApp().getAppId());
                cst.setString(6, sd.getMstEquip().getEquipmentId());
                cst.setString(7, sd.getTankNo());
                cst.setString(8, sd.getMstDept().getHodEmail());
                cst.setString(9, sd.getMstDept().getHodName());
                cst.setString(10, sd.getMstProd().getProId());
                cst.setString(11, sd.getMstProd().getProCapacity());
                cst.setString(12, sd.getSampleFreq());
                cst.setDate(13, ApplicationSQLDate.convertUtiltoSqlDate(sd.getPresampleDate()));
                cst.setDate(14, ApplicationSQLDate.convertUtiltoSqlDate(sd.getNxtsampleDate()));
                cst.setDate(15, ApplicationSQLDate.convertUtiltoSqlDate(ApplicationSQLDate.convertStringtoUtilDate(sd.getPostponetillDate())));
                cst.setString(16, sd.getPostponeReason());
                cst.setDate(17, ApplicationSQLDate.getcurrentSQLDate());
                cst.setString(18, sd.getSamplecreatedBy());
                cst.registerOutParameter(19, java.sql.Types.VARCHAR);
                cst.executeUpdate();
                md.setModalMessage(cst.getString(19));
                md.setMsgClass("text-success");
                if (md.getModalMessage().contains("Error")) {
                    md.setMsgClass("text-danger");
                }
                cst.close();
            }
        } catch (Exception ex) {
            md.setModalMessage(ex.getMessage());
            md.setMsgClass("text-danger");
            md = MyLogger.logIt(ex, sd.getSamplecreatedBy());
        }
        return md;
    }

    public static MessageDetails createSample(SampleDetails csl_sd, String labType, String sampleType) {
        MessageDetails md = new MessageDetails();

        if (!sampleType.equals("")) {
            try (
                    Connection con = DatabaseConnectionFactory.createConnection();
                    CallableStatement cst = con.prepareCall("{call CREATE_SAMPLE_PROCEDURE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"//24 inputs here
                            + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");) {//40 inputs
                ArrayDescriptor des = ArrayDescriptor.createDescriptor("ARRAY_TABLE", con);
                ARRAY csl_testIds = new ARRAY(des, con, csl_sd.getCsl_testIds());
                ARRAY rnd_testIds = new ARRAY(des, con, csl_sd.getRnd_testIds());
                String freq = (csl_sd.getSampleFreq() == null ? "0" : csl_sd.getSampleFreq());
                cst.setString(1, csl_sd.getTankId());
                cst.setString(2, csl_sd.getMstInd().getIndId());
                cst.setString(3, csl_sd.getMstDept().getMstCustomer().getCustomerId());
                cst.setString(4, csl_sd.getMstDept().getDepartmentId());
                cst.setString(5, csl_sd.getMstApp().getAppId());
                cst.setString(6, csl_sd.getMstEquip().getEquipmentId());
                cst.setString(7, csl_sd.getTankNo());
                cst.setString(8, csl_sd.getMstDept().getHodName());
                cst.setString(9, csl_sd.getMstDept().getHodEmail());
                cst.setString(10, csl_sd.getMstDept().getHodContact());
                cst.setString(11, csl_sd.getMstProd().getProId());
                cst.setString(12, "0");
                cst.setDouble(13, (csl_sd.getCsl_qtyDrawn() != null ? Double.parseDouble(csl_sd.getCsl_qtyDrawn()) : 0.0));
                cst.setDouble(14, (csl_sd.getRnd_qtyDrawn() != null ? Double.parseDouble(csl_sd.getRnd_qtyDrawn()) : 0.0));
                cst.setString(15, csl_sd.getSampledrawnRemarks());
                cst.setString(16, csl_sd.getSamplepriorityId());
                cst.setString(17, csl_sd.getSamplepriorityRemarks());
                cst.setDouble(18, Double.parseDouble(csl_sd.getTopupQty()));
                cst.setDouble(19, Double.parseDouble(csl_sd.getRunningHrs()));
                cst.setDate(20, ApplicationSQLDate.convertUtiltoSqlDate(csl_sd.getPresampleDate()));
                cst.setDate(21, ApplicationSQLDate.convertUtiltoSqlDate(csl_sd.getNxtsampleDate()));
                cst.setString(22, csl_sd.getCsl_labCode());
                cst.setString(23, csl_sd.getRnd_labCode());
                cst.setDate(24, null);
                cst.setDate(25, null);
                cst.setString(26, "");
                cst.setString(27, "");
                cst.setDate(28, ApplicationSQLDate.convertUtiltoSqlDate(csl_sd.getQtydrawnDate()));
                cst.setString(29, csl_sd.getSamplecreatedBy());
                cst.setDate(30, ApplicationSQLDate.getcurrentSQLDate());
                cst.setString(31, "");
                cst.setDate(32, null);
                cst.setDate(33, ApplicationSQLDate.convertUtiltoSqlDate(csl_sd.getNxtsampleDate()));
                cst.setDate(34, ApplicationSQLDate.getnextSampleSQLDate(csl_sd.getOldnxtsampleDate(), freq));
                cst.setString(35, (sampleType.equals("1") ? "OTS" : "CMP"));
                cst.setString(36, labType);
                cst.setArray(37, csl_testIds);
                cst.setArray(38, rnd_testIds);
                cst.registerOutParameter(39, java.sql.Types.VARCHAR);
                cst.registerOutParameter(40, java.sql.Types.VARCHAR);
                cst.executeUpdate();
                md.setModalMessage(cst.getString(39) + "'" + cst.getString(40) + "'");
                md.setMsgClass("text-success");
                if (md.getModalMessage().contains("Error")) {
                    md.setMsgClass("text-danger");
                }
            } catch (Exception ex) {
                md = MyLogger.logIt(ex, csl_sd.getSamplecreatedBy());
            }
        }
//        md.setModalMessage("Testing");
//        md.setMsgClass("text-success");
        return md;
    }

    public static MessageDetails updateSample(SampleDetails sd) {
        MessageDetails md = new MessageDetails();
        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                CallableStatement cst = con.prepareCall("{call UPDATE_SAMPLE_PROCEDURE(?,?,?,?,?,?,?,?,?,?,?,?,?)}");) {
            if (con != null) {
                ArrayDescriptor des = ArrayDescriptor.createDescriptor("ARRAY_TABLE", con);
                ARRAY testIds = new ARRAY(des, con, sd.getTestIds());
                cst.setString(1, sd.getSampleId());
                cst.setString(2, sd.getMstProd().getProId());
                cst.setString(3, "0");
                cst.setDouble(4, Double.parseDouble(sd.getQtyDrawn()));
                cst.setString(5, sd.getSamplepriorityId());
                cst.setDouble(6, Double.parseDouble(sd.getTopupQty()));
                cst.setString(7, sd.getMstLab().getLabCode());
                cst.setArray(8, testIds);
                cst.setString(9, sd.getSampledrawnRemarks());
                cst.setString(10, sd.getSamplepriorityRemarks());
                cst.setString(11, sd.getTankId());
                cst.registerOutParameter(12, java.sql.Types.VARCHAR);
                cst.registerOutParameter(13, java.sql.Types.VARCHAR);
                cst.executeUpdate();
                md.setModalTitle("Sample Update Status.");
                md.setModalMessage(cst.getString(12) + ": " + cst.getString(13));
                md.setMsgClass("text-success");
                cst.close();
            }
        } catch (Exception ex) {
            md.setModalTitle("Sample Update Status");
            md.setModalMessage(ex.getMessage());
            md.setMsgClass("text-danger");
            MyLogger.logIt(ex, sd.getSamplecreatedBy());
        }
        return md;
    }

    public static SampleDetails getcreateSmpl(String infoId) {
        SampleDetails createSampledetails = new SampleDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.TANK_ID, t2.IND_ID, t2.IND_NAME, t3.CUST_ID, t3.CUST_NAME, "
                        + "t1.DEPT_ID,t4.DEPT_NAME,t5.APPL_ID, t5.APPL_NAME,t1.EQUIP_ID, t6.EQUIP_NAME, t1.TANK_NO, "
                        + "t7.PROD_ID, t7.PROD_NAME, t1.CAPACITY, t1.SAMPLE_FREQ, t1.PREV_SAMPLE_DATE, "
                        + "t1.NEXT_SAMPLE_DATE ,t1.OLD_NEXT_SAMPLE_DATE,t1.CUST_ID,t4.HOD_NAME,t4.HOD_EMAIL,t4.HOD_CONTACT,t1.APPL_DESC FROM "
                        + "MST_TANK t1, MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, "
                        + "MST_APPLICATION t5, MST_EQUIPMENT t6, MST_PRODUCT t7 where t1.IND_ID=t2.IND_ID "
                        + "AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID AND "
                        + "t1.EQUIP_ID=t6.EQUIP_ID AND t1.PROD_ID=t7.PROD_ID AND "
                        + "t1.TANK_ID=?");) {
            pst.setString(1, infoId);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                createSampledetails.setTankId(infoId);
//                createSampledetails.setIndId(res.getString(2));
//                createSampledetails.setIndName(res.getString(3));
//                createSampledetails.setCustId(res.getString(4));
//                createSampledetails.setCustName(res.getString(5));
//                createSampledetails.setDeptId(res.getString(6));
//                createSampledetails.setDeptName(res.getString(7));
//                createSampledetails.setApplId(res.getString(8));
//                createSampledetails.setApplName(res.getString(9));
//                createSampledetails.setEquipId(res.getString(10));
//                createSampledetails.setEquipName(res.getString(11));
//                createSampledetails.setTankNo(res.getString(12));
//                createSampledetails.setProId(res.getString(13));
//                createSampledetails.setProName(res.getString(14));
//                createSampledetails.setCapacity(res.getString(15));
//                createSampledetails.setSampleFreq(res.getString(16));
//                createSampledetails.setPrevSampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(17)));
//                createSampledetails.setNxtSampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(18)));
//                createSampledetails.setOldSampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(19)));
//                createSampledetails.setCustId(res.getString(20));
//                createSampledetails.setHodName(res.getString(21));
//                createSampledetails.setHodEmail(res.getString(22));
//                createSampledetails.setHodContact(res.getString(23));
//                createSampledetails.setStrPrevDate(ApplicationSQLDate.convertUtilDatetoString(createSampledetails.getPrevSampleDate()));
//                createSampledetails.setStrNxtDate(ApplicationSQLDate.convertUtilDatetoString(createSampledetails.getNxtSampleDate()));
//                createSampledetails.setCreatedDate(ApplicationSQLDate.convertUtilDatetoString(ApplicationSQLDate.getcurrentSQLDate()));
//                createSampledetails.setStrOldNxtDate(ApplicationSQLDate.convertUtilDatetoString(createSampledetails.getOldSampleDate()));
//                createSampledetails.setApplDesc(res.getString(24));
                createSampledetails.setTankId(infoId);
                createSampledetails.getMstInd().setIndId(res.getString(2));
                createSampledetails.getMstInd().setIndName(res.getString(3));
                createSampledetails.getMstDept().getMstCustomer().setCustomerId(res.getString(4));
                createSampledetails.getMstDept().getMstCustomer().setCustomerName(res.getString(5));
                createSampledetails.getMstDept().setDepartmentId(res.getString(6));
                createSampledetails.getMstDept().setDepartmentName(res.getString(7));
                createSampledetails.getMstApp().setAppId(res.getString(8));
                createSampledetails.getMstApp().setAppName(res.getString(9));
                createSampledetails.getMstEquip().setEquipmentId(res.getString(10));
                createSampledetails.getMstEquip().setEquipmentName(res.getString(11));
                createSampledetails.setTankNo(res.getString(12));
                createSampledetails.getMstProd().setProId(res.getString(13));
                createSampledetails.getMstProd().setProName(res.getString(14));
                createSampledetails.getMstProd().setProCapacity(res.getString(15));
                createSampledetails.setSampleFreq(res.getString(16));
                createSampledetails.setPresampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(17)));
                createSampledetails.setNxtsampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(18)));
                createSampledetails.setOldnxtsampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(19)));
                createSampledetails.getMstDept().getMstCustomer().setCustomerId(res.getString(20));
                createSampledetails.getMstDept().setHodName(res.getString(21));
                createSampledetails.getMstDept().setHodEmail(res.getString(22));
                createSampledetails.getMstDept().setHodContact(res.getString(23));
                createSampledetails.setStringpresampleDate(ApplicationSQLDate.convertUtilDatetoString(createSampledetails.getPresampleDate()));
                createSampledetails.setStringnxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(createSampledetails.getNxtsampleDate()));
                createSampledetails.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(ApplicationSQLDate.getcurrentSQLDate()));
                createSampledetails.setStringoldnxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(createSampledetails.getOldnxtsampleDate()));
                createSampledetails.setDescAppl(res.getString(24));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, createSampledetails.getSamplecreatedBy());
        }
        return createSampledetails;
    }

    public static List getindustryTse(String EMP_CODE, String strNotifyDaysLimit, HttpServletRequest request) {
        List industrylst = new ArrayList();
        HttpSession session = request.getSession();
        int totalCount = 0;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t2.IND_ID,t2.IND_NAME, count(*) FROM MST_TANK t1, MST_INDUSTRY t2,"
                        + " MST_CUSTOMER t3 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND "
                        + " (t1.NEXT_SAMPLE_DATE-SYSDATE)<= ? "
                        + " and t1.CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE= ? ) "
                        + " group by t2.IND_ID,t2.IND_NAME order by t2.IND_NAME");) {
            pst.setString(1, strNotifyDaysLimit);
            pst.setString(2, EMP_CODE);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstIndustry mstInd = new MstIndustry();
                mstInd.setIndName(res.getString(2));
                mstInd.setIndCount(res.getString(3));
                totalCount += Integer.parseInt(res.getString(3));
                industrylst.add(mstInd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        session.setAttribute("notiCount", totalCount);
        return industrylst;
    }

    public static List getindustryTse2(String EMP_CODE) {
        List industrylst = new ArrayList();

        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT IND_ID,IND_NAME FROM MST_INDUSTRY where EMP_CODE=?")) {
            pst.setString(1, EMP_CODE);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    MstIndustry mstInd = new MstIndustry();
                    mstInd.setIndName(res.getString(1));
                    mstInd.setIndCount(res.getString(2));
                    industrylst.add(mstInd);
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "SharedOperations.getindustryTse2().Database() ");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SharedOperations.getindustryTse2() While connecting");
        }

        return industrylst;
    }

    public static List getcustomerDetailsTSE(String EMP_CODE, String strNotifyDaysLimit, String sampleType) {
        List cstdet = new ArrayList();
        if (sampleType.equals("OTS")) {
            sampleType = " ";
        } else if (sampleType.equals("CMP")) {
            sampleType = "AND (t1.NEXT_SAMPLE_DATE-SYSDATE)<=" + strNotifyDaysLimit + " AND t1.SAMPLE_FREQ >= 1 ";
        }
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, t5.APPL_NAME, "
                        + " t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.CAPACITY, t1.SAMPLE_FREQ, "
                        + " t1.PREV_SAMPLE_DATE, t1.NEXT_SAMPLE_DATE,t1.OLD_NEXT_SAMPLE_DATE,t1.POSTPONE_COUNT FROM MST_TANK t1, MST_INDUSTRY t2, "
                        + " MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, "
                        + " MST_PRODUCT t7 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID "
                        + " AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.PROD_ID=t7.PROD_ID and "
                        + " t1.CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE = ?) " + sampleType
                        + " ORDER BY t1.NEXT_SAMPLE_DATE DESC");) {
            pst.setString(1, EMP_CODE);
//            pst.setString(2, strNotifyDaysLimit);
            ResultSet res = pst.executeQuery();
//                ResultSet res = st.executeQuery("SELECT t1.TANK_ID, t2.IND_NAME, t3.CUST_NAME, t4.DEPT_NAME, t5.APPL_NAME,"
//                        + " t1.TANK_NO, t6.EQUIP_NAME, t7.PROD_NAME, t1.CAPACITY, t1.SAMPLE_FREQ, "
//                        + " t1.PREV_SAMPLE_DATE, t1.NEXT_SAMPLE_DATE,t1.OLD_NEXT_SAMPLE_DATE,t1.POSTPONE_COUNT FROM MST_TANK t1, MST_INDUSTRY t2,"
//                        + " MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6,"
//                        + " MST_PRODUCT t7 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID"
//                        + " AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.PROD_ID=t7.PROD_ID and"
//                        + " t1.CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE='" + EMP_CODE + "')"
//                        + " AND (t1.NEXT_SAMPLE_DATE-SYSDATE)<=" + strNotifyDaysLimit + " ORDER BY t1.NEXT_SAMPLE_DATE DESC");) {
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.getMstInd().setIndName(res.getString(2));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(3));
                sd.getMstDept().setDepartmentName(res.getString(4));
                sd.getMstApp().setAppName(res.getString(5));
                sd.setTankNo(res.getString(6));
                sd.getMstEquip().setEquipmentName(res.getString(7));
                sd.getMstProd().setProName(res.getString(8));
                sd.getMstProd().setProCapacity(res.getString(9));
                sd.setSampleFreq(res.getString(10));
                sd.setStringpresampleDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(11)));
                sd.setStringnxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(12)));
                sd.setStringoldnxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(13)));
                sd.setPostponedCount(res.getString(14));
//                MstTank mstTank = new MstTank();
//                mstTank.setTankId(res.getString(1));
//                mstTank.setIndName(res.getString(2));
//                mstTank.setCustName(res.getString(3));
//                mstTank.setDeptName(res.getString(4));
//                mstTank.setApplName(res.getString(5));
//                mstTank.setTankNo(res.getString(6));
//                mstTank.setEquipName(res.getString(7));
//                mstTank.setProName(res.getString(8));
//                mstTank.setCapacity(res.getString(9));
//                mstTank.setSampleFreq(res.getString(10));
//                mstTank.setStrPrevDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(11)));
//                mstTank.setStrNxtDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(12)));
//                mstTank.setStrOldNxtDate(ApplicationSQLDate.convertUtilDatetoString(res.getDate(13)));
//                mstTank.setPostponeCount(res.getString(14));
                cstdet.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        return cstdet;
    }

    public static SampleDetails getSampleDetails(String smplid) {
        SampleDetails sd = new SampleDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME,t1.CUST_ID, t3.CUST_NAME, t4.DEPT_NAME, "
                        + "t5.APPL_NAME, t6.EQUIP_NAME, t1.TANK_NO, t1.PROD_ID, t7.PROD_NAME, t8.CAPACITY, "
                        + "t1.QTY_DRAWN, t1.SAMPLE_DRAWN_DATE, t1.SAMPLE_DRAWN_REMARKS, t1.SAMPLE_PRIORITY_ID, "
                        + "t1.SAMPLE_PRIORITY_REMARKS, t1.TOP_UP_QTY, t1.PREV_SAMPLE_DATE, t1.NEXT_SAMPLE_DATE, "
                        + "t1.SAMPLE_CREATED_DATE, t1.LAB_LOC_CODE,t1.RUNNING_HOURS_OIL,t1.FILE_UPLOAD_STATUS,t1.SAMPLING_NO,"
                        + "t8.LAST_OIL_CHANGED,t1.TEST_RESULT_ENTERED_DATE,t1.TSE_FINAL_TEST_REMARKS,t1.TSE_FINALIZE_REPORT_DATE,T9.MAKE_NAME,t10.LAB_NAME, t1.sampling_no_cmp, t1.sampling_no_ots, "
                        + "t10.type FROM SAMPLE_DETAILS t1, MST_INDUSTRY t2, "
                        + "MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, MST_PRODUCT t7, "
                        + "MST_TANK T8, MST_MAKE t9, MST_LAB t10  where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND "
                        + "t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND "
                        + "t1.PROD_ID=t7.PROD_ID AND t1.TANK_ID=t8.TANK_ID AND  t6.MAKE_ID=t9.MAKE_ID AND t1.LAB_LOC_CODE=t10.LAB_LOC_CODE AND t1.SAMPLE_ID=? AND t1.status_id = '3'");) {
            pst.setString(1, smplid);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerId(res.getString(4));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(5));
                sd.getMstDept().setDepartmentName(res.getString(6));
                sd.getMstApp().setAppName(res.getString(7));
                sd.getMstEquip().setEquipmentName(res.getString(8));
                sd.setTankNo(res.getString(9));
                sd.getMstProd().setProId(res.getString(10));
                sd.getMstProd().setProName(res.getString(11));
                sd.getMstProd().setProCapacity(res.getString(12));
                sd.setQtyDrawn(res.getString(13));
                sd.setSampledrawnDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14)));
                sd.setSampledrawnRemarks(res.getString(15));
                sd.setSamplepriorityId(res.getString(16));
                sd.setSamplepriorityRemarks(res.getString(17));
                sd.setTopupQty(res.getString(18));
                sd.setPresampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(19)));
                sd.setNxtsampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(20)));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(21)));
                sd.setStringpresampleDate(ApplicationSQLDate.convertUtilDatetoString(sd.getPresampleDate()));
                sd.setStringnxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(sd.getNxtsampleDate()));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                sd.setStringsampledrawnDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSampledrawnDate()));
                sd.getMstLab().setLabCode(res.getString(22));
                sd.setRunningHrs(res.getString(23));
                sd.setFileuploadStatus(res.getString(24));
                sd.setSamplingNo(res.getString(25));//CMP + OTS sampling count
                sd.setLastoilChanged(res.getString(26));
                sd.setTestresultenteredDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(27)));
                sd.setStringtestresultenteredDate(ApplicationSQLDate.convertUtilDatetoString(sd.getTestresultenteredDate()));
                sd.setTseFinalTestRemarks(res.getString(28));
                if (res.getString(29) != null) {
                    sd.setTseFinalizeRptTstDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(29)));
                    sd.setStringTseFinalizeRptTstDate(ApplicationSQLDate.convertUtilDatetoString(sd.getTseFinalizeRptTstDate()));
                }
                sd.getMstEquip().getMstmake().setMakeName(res.getString(30));
                sd.getMstLab().setLabName(res.getString(31));
                sd.setSampling_no_cmp(res.getString(32));
                sd.setSampling_no_ots(res.getString(33));
                sd.getMstLab().setLabType(res.getString(34));

            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, sd.getSamplecreatedBy());
        }
        return sd;
    }

    public static List<SampleDetails> getSampleDetails2(String smplid, String labCode) {
        List<SampleDetails> sdList = new ArrayList<SampleDetails>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT t1.SAMPLE_ID, t1.TANK_ID, t2.IND_NAME,t1.CUST_ID, t3.CUST_NAME, t4.DEPT_NAME, "
                        + "t5.APPL_NAME, t6.EQUIP_NAME, t1.TANK_NO, t1.PROD_ID, t7.PROD_NAME, t8.CAPACITY, "
                        + "t1.QTY_DRAWN, t1.SAMPLE_DRAWN_DATE, t1.SAMPLE_DRAWN_REMARKS, t1.SAMPLE_PRIORITY_ID, "
                        + "t1.SAMPLE_PRIORITY_REMARKS, t1.TOP_UP_QTY, t1.PREV_SAMPLE_DATE, t1.NEXT_SAMPLE_DATE, "
                        + "t1.SAMPLE_CREATED_DATE, t1.LAB_LOC_CODE,t1.RUNNING_HOURS_OIL,t1.FILE_UPLOAD_STATUS,t1.SAMPLING_NO,"
                        + "t8.LAST_OIL_CHANGED,t1.TEST_RESULT_ENTERED_DATE,t1.TSE_FINAL_TEST_REMARKS,t1.TSE_FINALIZE_REPORT_DATE,T9.MAKE_NAME,t10.LAB_NAME, t1.sampling_no_cmp, t1.sampling_no_ots, "
                        + "t10.type FROM SAMPLE_DETAILS t1, MST_INDUSTRY t2, "
                        + "MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, MST_PRODUCT t7, "
                        + "MST_TANK T8, MST_MAKE t9, MST_LAB t10  where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND "
                        + "t1.DEPT_ID=t4.DEPT_ID AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND "
                        + "t1.PROD_ID=t7.PROD_ID AND t1.TANK_ID=t8.TANK_ID AND  t6.MAKE_ID=t9.MAKE_ID AND t1.LAB_LOC_CODE=t10.LAB_LOC_CODE AND t1.SAMPLE_ID=?");) {
            pst.setString(1, smplid);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                SampleDetails sd = new SampleDetails();
                sd.setSampleId(res.getString(1));
                sd.setTankId(res.getString(2));
                sd.getMstInd().setIndName(res.getString(3));
                sd.getMstDept().getMstCustomer().setCustomerId(res.getString(4));
                sd.getMstDept().getMstCustomer().setCustomerName(res.getString(5));
                sd.getMstDept().setDepartmentName(res.getString(6));
                sd.getMstApp().setAppName(res.getString(7));
                sd.getMstEquip().setEquipmentName(res.getString(8));
                sd.setTankNo(res.getString(9));
                sd.getMstProd().setProId(res.getString(10));
                sd.getMstProd().setProName(res.getString(11));
                sd.getMstProd().setProCapacity(res.getString(12));
                sd.setQtyDrawn(res.getString(13));
                sd.setSampledrawnDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14)));
                sd.setSampledrawnRemarks(res.getString(15));
                sd.setSamplepriorityId(res.getString(16));
                sd.setSamplepriorityRemarks(res.getString(17));
                sd.setTopupQty(res.getString(18));
                sd.setPresampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(19)));
                sd.setNxtsampleDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(20)));
                sd.setSamplecreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(21)));
                sd.setStringpresampleDate(ApplicationSQLDate.convertUtilDatetoString(sd.getPresampleDate()));
                sd.setStringnxtsampleDate(ApplicationSQLDate.convertUtilDatetoString(sd.getNxtsampleDate()));
                sd.setStringsamplecreatedDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSamplecreatedDate()));
                sd.setStringsampledrawnDate(ApplicationSQLDate.convertUtilDatetoString(sd.getSampledrawnDate()));
                sd.getMstLab().setLabCode(res.getString(22));
                sd.setRunningHrs(res.getString(23));
                sd.setFileuploadStatus(res.getString(24));
                sd.setSamplingNo(res.getString(25));//CMP + OTS sampling count
                sd.setLastoilChanged(res.getString(26));
                sd.setTestresultenteredDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(27)));
                sd.setStringtestresultenteredDate(ApplicationSQLDate.convertUtilDatetoString(sd.getTestresultenteredDate()));
                sd.setTseFinalTestRemarks(res.getString(28));
                if (res.getString(29) != null) {
                    sd.setTseFinalizeRptTstDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(29)));
                    sd.setStringTseFinalizeRptTstDate(ApplicationSQLDate.convertUtilDatetoString(sd.getTseFinalizeRptTstDate()));
                }
                sd.getMstEquip().getMstmake().setMakeName(res.getString(30));
                sd.getMstLab().setLabName(res.getString(31));
                sd.setSampling_no_cmp(res.getString(32));
                sd.setSampling_no_ots(res.getString(33));
                sd.getMstLab().setLabType(res.getString(34));
                sdList.add(sd);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getSampleDetails2()");
        }
        return sdList;
    }

    public static MessageDetails insertUploadedTestDoc(String smplId, Part file, String userId, String labCode) {
        MessageDetails md = new MessageDetails();
        md.setModalTitle("Send to Customer Status.");
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        if (file.getSize() > 0) {
            try (Connection con = DatabaseConnectionFactory.createConnection();
                    PreparedStatement pst = con.prepareStatement("DELETE FROM CMP_TEST_REPORT_INFO WHERE SAMPLE_ID = ? AND LAB_CODE = ?");
                    PreparedStatement pstmt = con.prepareStatement("INSERT INTO CMP_TEST_REPORT_INFO(SAMPLE_ID,LAB_CODE,TEST_REPORT,DOC_TYPE,UPLOADED_DATETIME,UPLOADED_BY) VALUES(?,?,?,?,?,?)");
                    InputStream is = file.getInputStream();) {
                pst.setString(1, smplId);
                pst.setString(2, labCode);
                ResultSet res = pst.executeQuery();
                pstmt.setString(1, smplId);
                pstmt.setString(2, labCode);
                pstmt.setBinaryStream(3, is, is.available());
                pstmt.setString(4, "pdf");
                pstmt.setString(5, format.format(new Date()));
                pstmt.setString(6, userId);
                pstmt.execute();
                md.setFilemsgClass("text-success");
                md.setFileMsg("File Uploaded Successfully");
                md.setStatus(true);
            } catch (Exception ex) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Failed to Upload File");
                md.setStatus(false);
                MyLogger.logIt(ex, userId);
            }
        } else {
            md.setFilemsgClass("text-warning");
            md.setFileMsg("No File was Uploaded");
        }
        return md;
    }

    public static MessageDetails sendtoCustomer(String sampleID, String remarks, MessageDetails md) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        int recordsUpdated = 0;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE SAMPLE_DETAILS SET TSE_FINAL_TEST_REMARKS=?, STATUS_ID='4',TSE_FINALIZE_REPORT_DATE= TO_DATE(?,'dd/mm/yyyy') WHERE "
                        + "SAMPLE_ID=?");) {
            pst.setString(1, remarks);
            pst.setString(2, format.format(new Date()));
            pst.setString(3, sampleID);
            recordsUpdated = pst.executeUpdate();
            switch (recordsUpdated) {
                case 0:
                    md.setModalMessage("No Sample were Sent to Customer");
                    md.setMsgClass("text-danger");
                    md.setStatus(false);
                    break;
                case 1:
                case 2:
                    md.setModalMessage("Sample sent to Customer Successfully");
                    md.setMsgClass("text-success");
                    md.setStatus(true);
                    break;
                default:
                    md.setModalMessage("Unknown Error");
                    md.setMsgClass("text-danger");
                    md.setStatus(false);
                    break;
            }
        } catch (Exception ex) {
            md.setModalMessage("Failed to Send Sample to Customer");
            md.setMsgClass("text-danger");
            md.setStatus(false);
            //MyLogger.logIt(ex);
        }
        return md;
    }

    public static MessageDetails createIOM(String iom_ref_no, String lab_loc_code, String[] sampleids, String userId) {
        MessageDetails md = new MessageDetails();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                CallableStatement cst = con.prepareCall("{call CREATE_IOM_PROCEDURE(?,?,?,?,?,?,?,?)}");) {
            if (con != null) {
                ArrayDescriptor des = ArrayDescriptor.createDescriptor("ARRAY_TABLE", con);
                ARRAY arr_sampleids = new ARRAY(des, con, sampleids);
                cst.setString(1, iom_ref_no);
                cst.setString(2, lab_loc_code);
                cst.setArray(3, arr_sampleids);
                cst.setString(4, "1");

                cst.setString(5, userId);
                cst.setDate(6, ApplicationSQLDate.convertUtiltoSqlDate(new Date()));

                cst.registerOutParameter(7, java.sql.Types.VARCHAR);
                cst.registerOutParameter(8, java.sql.Types.VARCHAR);

                cst.executeUpdate();
                md.setModalTitle("IOM Generate Status.");
                md.setModalMessage(cst.getString(7) + " " + cst.getString(8));
                md.setMsgClass("text-success");
                if (md.getModalMessage().contains("Error") || md.getModalMessage().contains("Duplicate")) {
                    md.setMsgClass("text-danger");
                }
            }
        } catch (Exception ex) {
            md.setModalTitle("Sample Generate Status");
            md.setModalMessage(ex.getMessage());
            md.setMsgClass("text-danger");
            md = MyLogger.logIt(ex, userId);
        }
        return md;
    }

    public static List getIOMSummaryData(String labLocCode, String EMP_CODE) {
        List listIoms = new ArrayList();
        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.IOM_REF_NO,T1.LAB_LOC_CODE,T2.LAB_NAME,T1.CREATED_BY,T1.CREATED_DATETIME from MST_IOM_REF T1 "
                        + "INNER JOIN MST_LAB T2 ON T1.LAB_LOC_CODE=T2.LAB_LOC_CODE and T1.LAB_LOC_CODE = ? AND t1.created_by = ?");) {
            pst.setString(1, labLocCode);
            pst.setString(2, EMP_CODE);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstIom miom = new MstIom();
                miom.setIomRefNo(res.getString(1));
                miom.getMstLab().setLabCode(res.getString(2));
                miom.getMstLab().setLabName(res.getString(3));
                miom.setCreatedBy(res.getString(4));

                if (res.getDate(5) != null) {
                    miom.setIomCreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(5)));
                    miom.setStrIomCreatedDate(ApplicationSQLDate.convertUtilDatetoString(miom.getIomCreatedDate()));
                }

                listIoms.add(miom);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, EMP_CODE);
        }
        return listIoms;
    }

    public static List getIOMSummaryData(String labLocCode) {
        List listIoms = new ArrayList();
        try (
                Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.IOM_REF_NO,T1.LAB_LOC_CODE,T2.LAB_NAME,T1.CREATED_BY,T1.CREATED_DATETIME from MST_IOM_REF T1 "
                        + "INNER JOIN MST_LAB T2 ON T1.LAB_LOC_CODE=T2.LAB_LOC_CODE and T1.LAB_LOC_CODE = ?");) {
            pst.setString(1, labLocCode);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstIom miom = new MstIom();
                miom.setIomRefNo(res.getString(1));
                miom.getMstLab().setLabCode(res.getString(2));
                miom.getMstLab().setLabName(res.getString(3));
                miom.setCreatedBy(res.getString(4));
                if (res.getDate(5) != null) {
                    miom.setIomCreatedDate(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(5)));
                    miom.setStrIomCreatedDate(ApplicationSQLDate.convertUtilDatetoString(miom.getIomCreatedDate()));
                }

                listIoms.add(miom);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getIOMSummaryData(String labLocCode)");
        }
        return listIoms;
    }
}
