/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reports;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import viewModel.ReportDetails;


/**
 *
 * @author 00507469
 */
public class TseReports {
    public static List getTseWisePerformance(String fromDate,String toDate,Boolean logIt) {
     
        List<ReportDetails> listRptDetails = new ArrayList<>();       
        String tseEmpCode="",tseEmpName="";
        String query1="SELECT EMP_CODE,EMP_NAME FROM MST_USER WHERE ACTIVE=1 AND ROLE_ID=1";
        
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst1 = con.prepareStatement(query1)) {
            try (ResultSet res1 = pst1.executeQuery();) {
                while (res1.next()) {
                    tseEmpCode = res1.getString(1);
                    tseEmpName = res1.getString(2);
                    /*String query2 = "Select ( select count(sample_id) from sample_details where ( sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) and sample_created_by= ? )  tot_samples_drawn,"
                            + " ( select count(tank_id)  from mst_tank where ( OLD_NEXT_SAMPLE_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) "
                            + " and cust_id in( select cust_id from mst_customer where emp_code= ?  ) and POSTPONE_FLAG='1' )  sample_postponed ,"
                            + " ( select count(tank_id)  from mst_tank where ( OLD_NEXT_SAMPLE_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) "
                            + " and cust_id in( select cust_id from mst_customer where emp_code= ?  ) and POSTPONE_FLAG=1 and POSTPONE_COUNT > 1 ) sample_postponed_morethanonce ,"
                            + " ( select count(tank_id)  from mst_tank where ( NEXT_SAMPLE_DATE  BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) "
                            + " and cust_id in( select cust_id from mst_customer where emp_code= ?  ) and  (SYSDATE - NEXT_SAMPLE_DATE) > 15 ) sample_pending_gt15days ,"
                            + " ( select count(tank_id)  from mst_tank where ( NEXT_SAMPLE_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) "
                            + " and cust_id in( select cust_id from mst_customer where emp_code= ?  ) and  (SYSDATE - NEXT_SAMPLE_DATE) > 0   ) sample_pending_noaction ,"
                            + " ( select count(distinct(EQUIP_ID)) from sample_details where ( sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) and sample_created_by= ? ) tot_equip_monitored,"
                            + " ( select count(distinct(CUST_ID)) from sample_details where ( sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) and sample_created_by= ? ) tot_cust_monitored "
                            + " from mst_priority where rownum=1";*/
                    String query2 = "SELECT (select emp_name from mst_user where emp_code = ?) TSE_name, (select STATE_OFFICE from mst_user where emp_code = ? ) State_Office, (select region from mst_user where emp_code = ? ) region, " +
                            "(Select count(*) from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) total_equipments, " +
                            "(select count(*) from mst_customer where emp_code = ?  ) total_customers, " +
                            "(select count(*) from sample_details where CUST_ID IN (SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE = ? ) and (SAMPLE_DRAWN_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE(? ,'dd/mm/yyyy'))) total_samples, " + 
                            "(select count(*) from sample_details where CUST_ID in (SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE = ? ) and (SAMPLE_DRAWN_DATE BETWEEN TO_DATE( ?  ,'dd/mm/yyyy') and TO_DATE(?  ,'dd/mm/yyyy')))-(select count(*) from (select count(*) from sample_postponed_hist where tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( OLD_NEXT_SAMPLE_DATE BETWEEN TO_DATE( ?  ,'dd/mm/yyyy') and TO_DATE(?  ,'dd/mm/yyyy')) group by tank_id, old_next_sample_date)) non_samples," + 
                            "(select count(*) from (select sample_postponed_hist.tank_id, count(*) from sample_postponed_hist where tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ?)) and ( OLD_NEXT_SAMPLE_DATE BETWEEN TO_DATE( ?  ,'dd/mm/yyyy') and TO_DATE( ?  ,'dd/mm/yyyy')) group by tank_id, old_next_sample_date having count(*) = 1)) single_samples, " + 
                            "(select count(*) from (select count(*) from sample_postponed_hist where tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( OLD_NEXT_SAMPLE_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ?  ,'dd/mm/yyyy')) group by tank_id, old_next_sample_date having count(*) > 1)) more_thn_twice, " +  
                            "(select count(*) from sample_details s inner join mst_iom_ref i on s.IOM_REF_NO = i.IOM_REF_NO where ( i.created_datetime BETWEEN TO_DATE( ?  ,'dd/mm/yyyy') and TO_DATE(?  ,'dd/mm/yyyy')) and s.status_id = 1 and s.tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? ))) transit, " + 
                            "(select count(*) from sample_details where status_id = 2 and tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( LAB_RECEIVED_DATE BETWEEN TO_DATE( ?  ,'dd/mm/yyyy') and TO_DATE( ?  ,'dd/mm/yyyy'))) under_tsting, " + 
                            "(select count(*) from sample_details where status_id = 3 and tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( TEST_RESULT_ENTERED_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ?  ,'dd/mm/yyyy'))) samples_under_final, " + 
                            "(select count(*) from sample_details where status_id = 4 and tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( TSE_FINALIZE_REPORT_DATE BETWEEN TO_DATE( ?  ,'dd/mm/yyyy') and TO_DATE( ?  ,'dd/mm/yyyy'))) samples_submitted " + 
                            "from dual";
                    try (
                            PreparedStatement pst2 = con.prepareStatement(query2);) {
//                        pst2.setString(1, fromDate);
//                        pst2.setString(2, toDate);
//                        pst2.setString(3, tseEmpCode);
                        pst2.setString(1, tseEmpCode);
                        pst2.setString(2, tseEmpCode);
                        pst2.setString(3, tseEmpCode);
                        pst2.setString(4, tseEmpCode);
                        pst2.setString(5, tseEmpCode);
                        pst2.setString(6, tseEmpCode);
                        pst2.setString(7, fromDate);
                        pst2.setString(8, toDate);
                        pst2.setString(9, tseEmpCode);
                        pst2.setString(10, fromDate);
                        pst2.setString(11, toDate);
                        pst2.setString(12, tseEmpCode);
                        pst2.setString(13, fromDate);
                        pst2.setString(14, toDate);
                        pst2.setString(15, tseEmpCode);
                        pst2.setString(16, fromDate);
                        pst2.setString(17, toDate);
                        pst2.setString(18, tseEmpCode);
                        pst2.setString(19, fromDate);
                        pst2.setString(20, toDate);
                        pst2.setString(21, fromDate);
                        pst2.setString(22, toDate);
                        pst2.setString(23, tseEmpCode); 
                        pst2.setString(24, tseEmpCode);
                        pst2.setString(25, fromDate);
                        pst2.setString(26, toDate);
                        pst2.setString(27, tseEmpCode);
                        pst2.setString(28, fromDate);
                        pst2.setString(29, toDate);
                        pst2.setString(30, tseEmpCode);
                        pst2.setString(31, fromDate);
                        pst2.setString(32, toDate);
                        
                        ResultSet res2 = pst2.executeQuery();
                        if (res2.next()) {
                            ReportDetails rptDetails = new ReportDetails();
                            rptDetails.setCol1(res2.getString(1));
                            rptDetails.setCol2(res2.getString(2));
                            rptDetails.setCol3(res2.getString(3));
                            rptDetails.setCol4(res2.getString(4));
                            rptDetails.setCol5(res2.getString(5));
                            rptDetails.setCol6(res2.getString(6));
                            rptDetails.setCol7(res2.getString(7));
                            rptDetails.setCol8(res2.getString(8));
                            rptDetails.setCol9(res2.getString(9));
                            rptDetails.setCol10(res2.getString(10));
                            rptDetails.setCol11(res2.getString(11));
                            rptDetails.setCol12(res2.getString(12));
                            rptDetails.setCol13(res2.getString(13));
                            listRptDetails.add(rptDetails);
                        }
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "resultSet()");
                    }
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "getTseWisePerformance");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getTseWisePerformance");
        }
        return listRptDetails;
    }
    
    public static List getTseWiseResponseTime(String fromDate,String toDate) {
     
        List<ReportDetails> listRptDetails = new ArrayList<>();       
        String tseEmpCode="",tseEmpName="";
        String query1="SELECT EMP_CODE,EMP_NAME FROM MST_USER WHERE ACTIVE=1 AND ROLE_ID=1";
        
        
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst1 = con.prepareStatement(query1)) {
            try (ResultSet res1 = pst1.executeQuery();) {
                while (res1.next()) {
                    tseEmpCode = res1.getString(1);
                    tseEmpName = res1.getString(2);
                    String query2= " select  ( select COALESCE(round(avg(tse_finalize_report_date-sample_drawn_date)),0)  from sample_details "
                            + " where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') and cust_id in( select cust_id from mst_customer where emp_code= ?   ) and status_id=4 ) avgcyclecomdays, "
                            + " ( select COALESCE(round(avg(LAB_RECEIVED_DATE-sample_drawn_date)),0)  from sample_details "
                            + " where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') and cust_id in( select cust_id from mst_customer where emp_code= ?  ) and status_id=4 ) as avglabreceivedays, "
                            + " ( select COALESCE(round(avg(TEST_RESULT_ENTERED_DATE-LAB_RECEIVED_DATE)),0) from sample_details "
                            + " where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') and cust_id in( select cust_id from mst_customer where emp_code= ?  ) and status_id=4 ) as avglabuploaddays "
                            + " from mst_priority where rownum=1";
                    try (
                            PreparedStatement pst2 = con.prepareStatement(query2);) {
                            pst2.setString(1, fromDate);
                            pst2.setString(2, toDate);
                            pst2.setString(3, tseEmpCode);
                            pst2.setString(4, fromDate);
                            pst2.setString(5, toDate);
                            pst2.setString(6, tseEmpCode);
                            pst2.setString(7, fromDate);
                            pst2.setString(8, toDate);
                            pst2.setString(9, tseEmpCode);
                        ResultSet res2 = pst2.executeQuery();
                        if (res2.next()) {
                            ReportDetails rptDetails = new ReportDetails();
                            rptDetails.setCol1(tseEmpName);
                            rptDetails.setCol2(res2.getString(1));
                            rptDetails.setCol3(res2.getString(2));
                            rptDetails.setCol4(res2.getString(3));                            
                            listRptDetails.add(rptDetails);
                        }
                    }
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "getTseWiseResponseTime()");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getTseWiseResponseTime()");
        }
        return listRptDetails;  
    }
    
    //CSL Efficiency
    public static List getLabCSLEfficiencyReport(String fromDate,String toDate) {
     
        List<ReportDetails> listRptDetails = new ArrayList<>();       
        String labCode="",labName="",empName="";
//        String query1="SELECT EMP_CODE,EMP_NAME FROM MST_USER WHERE ACTIVE=1 AND ROLE_ID=2";
        String query1="select LAB_LOC_CODE, LAB_NAME from mst_lab ";// link with mst user
        
        
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst1 = con.prepareStatement(query1)) {
            try (ResultSet res1 = pst1.executeQuery();) {
                while (res1.next()) {
                    labCode = res1.getString(1);
                    labName = res1.getString(2);
                    empName = "Nada";// res1.getString(3);
//                    String query2=" select (select count(*)  from sample_details where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') "
//                         + " and LAB_LOC_CODE IN (SELECT LAB_LOC_CODE FROM MST_LAB WHERE EMP_CODE= ? ) and status_id in (3,4) ) as tot_lab_sample_tested, "
//                         +" (select count(*)  from sample_details where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')  "
//                         + " and EXP_TEST_RESULT_DATE >= TEST_RESULT_ENTERED_DATE and LAB_LOC_CODE IN (SELECT LAB_LOC_CODE FROM MST_LAB WHERE EMP_CODE= ? ) ) as tot_sample_tested_withintime,"
//                         + " (select count(*)  from sample_details where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') "
//                         + " and (SYSDATE - LAB_RECEIVED_DATE) > 7 and LAB_LOC_CODE IN (SELECT LAB_LOC_CODE FROM MST_LAB WHERE EMP_CODE= ? ) and status_id=3) as tot_sample_pending_after7days,"
//                         + " (select round(avg(TEST_RESULT_ENTERED_DATE-LAB_RECEIVED_DATE),2)  from sample_details where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') "
//                         + " and (SYSDATE - LAB_RECEIVED_DATE) > 7 and LAB_LOC_CODE IN (SELECT LAB_LOC_CODE FROM MST_LAB WHERE EMP_CODE= ? ) and status_id in (3,4) ) as avg_test_upload_days, "
//                         + " (select count( distinct(LAB_EQUIP_ID)) FROM sample_details where TEST_RESULT_ENTERED_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') "
//                         + " and RESULT_DATE_EXTEND_REASON =1 ) as tot_error_equipments from mst_priority where rownum=1";
                    
                    String query2 = "Select " +
                            "(select count(*) from sample_details where status_id = 1 and lab_loc_code = ?  and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy') ) tots_samples_recs," +
                            "(select count(*) from sample_details where status_id = 2 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy') ) tots_samples_tests," +
                            "(select count(*) from sample_details where status_id = 2 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')  and (SYSDATE - lab_received_date) > 10) tots_samples_tests, " +
                            "(select count(*) from sample_details where status_id = 3 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy') ) tots_samples_tests, " +
                            "(select count(*) from sample_details where status_id = 3 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')  and (TEST_RESULT_ENTERED_DATE - lab_received_date) < 2) tots_samples_tests1, " +
                            "(select count(*) from sample_details where status_id = 3 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')  and (TEST_RESULT_ENTERED_DATE - lab_received_date) < 3) tots_samples_tests2, " +
                            "(select count(*) from sample_details where status_id = 3 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')  and (TEST_RESULT_ENTERED_DATE - lab_received_date) < 5) tots_samples_tests3, " +
                            "(select COALESCE(round(avg(t1.dateZ),2),0) average from (select TO_DATE(t1.TEST_RESULT_ENTERED_DATE,'DD-MM-YYYY')-TO_DATE(t1.lab_received_date,'DD-MM-YYYY') dateZ from sample_details t1 where t1.lab_loc_code = ? and t1.status_id = 4 and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')) t1)  " +
                            "from dual";
                    try (
                            PreparedStatement pst2 = con.prepareStatement(query2);) {
                            pst2.setString(1, labCode);
                            pst2.setString(2, fromDate);
                            pst2.setString(3, toDate);
                            pst2.setString(4, labCode);
                            pst2.setString(5, fromDate);
                            pst2.setString(6, toDate);
                            pst2.setString(7, labCode);
                            pst2.setString(8, fromDate);
                            pst2.setString(9, toDate);
                            pst2.setString(10, labCode);
                            pst2.setString(11, fromDate);
                            pst2.setString(12, toDate);
                            pst2.setString(13, labCode);
                            pst2.setString(14, fromDate);
                            pst2.setString(15, toDate);
                            pst2.setString(16, labCode);
                            pst2.setString(17, fromDate);
                            pst2.setString(18, toDate);
                            pst2.setString(19, labCode);
                            pst2.setString(20, fromDate);
                            pst2.setString(21, toDate);
                            pst2.setString(22, labCode);
                            pst2.setString(23, fromDate);
                            pst2.setString(24, toDate);
                            ResultSet res2 = pst2.executeQuery();
                        if (res2.next()) {
                            ReportDetails rptDetails = new ReportDetails();
                            rptDetails.setCol1(labName);
                            rptDetails.setCol2(empName);
                            rptDetails.setCol3(res2.getString(1));
                            rptDetails.setCol4(res2.getString(2));  
                            rptDetails.setCol5(res2.getString(3));
                            rptDetails.setCol6(res2.getString(4));
                            rptDetails.setCol7(res2.getString(5));
                            rptDetails.setCol8(res2.getString(6));
                            rptDetails.setCol9(res2.getString(7));
                            rptDetails.setCol10(res2.getString(8));
                            listRptDetails.add(rptDetails);
                        }
                    }
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "getLabWiseResponseTime()");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getLabWiseResponseTime()");
            System.out.println(ex.getMessage());
        }
        return listRptDetails;  
    }
    
    public static List getCustomerServices(String fromDate,String toDate) {
     
        List<ReportDetails> listRptDetails = new ArrayList<>();       
        String custId="",custName="";
        String query1="SELECT CUST_ID,CUST_NAME FROM MST_CUSTOMER WHERE ACTIVE=1";
        
        
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst1 = con.prepareStatement(query1)) {
            try (ResultSet res1 = pst1.executeQuery();) {
                while (res1.next()) {
                    custId = res1.getString(1);
                    custName = res1.getString(2);
//                    String query2=" select (select count(*) from sample_details where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') and cust_id= ? ) as tot_sample_drawn, "
//                            + " (select count(*) from sample_details where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') and cust_id= ?  and status_id=3 ) as tot_sample_test_report_sub, "
//                            + " (select count(distinct(equip_id)) from sample_details where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') and cust_id= ? ) as tot_equip_monitored,"
//                            + " (select COALESCE(round(avg(TSE_FINALIZE_REPORT_DATE-sample_drawn_date)),0) from sample_details where sample_drawn_date BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy') and status_id=4  and cust_id= ? ) as avg_compl_cycle_days "
//                            + " from mst_priority where rownum=1";
                    String query2 = "Select " +
                        "(select cust_name from mst_customer where cust_id = ? ) customer_name, " +
                        "(select LISTAGG(sap_code,', ') WITHIN GROUP(order by sap_code) as SapCodes from mst_map_cust_sapcode where cust_id =  ? ) sap_codes, " +
                        "(select state_office from mst_user where emp_code = (select emp_code from mst_customer where cust_id =  ? )) state_office, " +
                        "(select emp_name from mst_user where emp_code = (select emp_code from mst_customer where cust_id =  ? )) Emp_name, " +
                        "(select count(*) from mst_tank where cust_id =  ? ) Tanks, " +
                        "(select count(*) from sample_details where cust_id =  ?  and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')) Samples, " +
                        "(select count(*) from sample_details where cust_id =  ?  and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy') and status_id = 4) Samples_sent, " +
                        "(select COALESCE(round(avg(t1.dateZ),2),0) average from (select TO_DATE(t1.TSE_FINALIZE_REPORT_DATE,'DD-MM-YYYY')-TO_DATE(t1.SAMPLE_DRAWN_DATE,'DD-MM-YYYY') dateZ from sample_details t1 where t1.cust_id =  ?  and t1.status_id = 4 and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) t1) Average " +
                        "from dual";
                    try (
                            PreparedStatement pst2 = con.prepareStatement(query2);) {
                        pst2.setString(1, custId);
                        pst2.setString(2, custId);
                        pst2.setString(3, custId);
                        pst2.setString(4, custId);
                        pst2.setString(5, custId);
                        pst2.setString(6, custId);
                        pst2.setString(7, fromDate);
                        pst2.setString(8, toDate);
                        pst2.setString(9, custId);
                        pst2.setString(10, fromDate);
                        pst2.setString(11, toDate);
                        pst2.setString(12, custId);
                        pst2.setString(13, fromDate);
                        pst2.setString(14, toDate);
                        ResultSet res2 = pst2.executeQuery();
                        if (res2.next()) {
                            ReportDetails rptDetails = new ReportDetails();
                            rptDetails.setCol1(res2.getString(1));
                            rptDetails.setCol2(res2.getString(2));
                            rptDetails.setCol3(res2.getString(3));
                            rptDetails.setCol4(res2.getString(4));  
                            rptDetails.setCol5(res2.getString(5));
                            rptDetails.setCol6(res2.getString(6));
                            rptDetails.setCol7(res2.getString(7));
                            rptDetails.setCol8(res2.getString(8));
                            listRptDetails.add(rptDetails);
                        }
                    }
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "getCustomerServices()");
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "getCustomerServices()");
        }
        return listRptDetails;  
    }
}
