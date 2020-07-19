/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TseAdmin;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import viewModel.ReportDetails;

/**
 *
 * @author NIT_steven
 */
public class GetTseReportsDetails extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rowName = request.getParameter("rowCode");
        String report = request.getParameter("report");
        String rowCode = converttoCode(rowName, report);
        String colNum = request.getParameter("colNum");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        List<ReportDetails> listRptDetails = new ArrayList<>();
        switch (report) {
            case "TsePerformanceReport":
                String query = "";

                switch (colNum) {
                    case "3":
                        query = "Select t1.tank_id, i1.ind_name, c1.cust_name, t1.tank_desc from mst_tank t1 inner join mst_industry i1 on t1.ind_id = i1.ind_id inner join mst_customer c1 on t1.cust_id = c1.cust_id inner join mst_product p1 on t1.prod_id = p1.prod_id where t1.cust_id in (select cust_id from mst_customer where emp_code = ? )";
                        break;
                    case "4":
                        query = "select cust_id, cust_name from mst_customer where emp_code = ?";
                        break;
                    case "5":
                        query = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where CUST_ID IN (SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE = ? ) and (SAMPLE_DRAWN_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE(? ,'dd/mm/yyyy'))";
                        break;
                    case "6":
                        query = "Nada";//create new query
                        break;
                    case "7":
                        query = "Select t1.tank_id,d1.ind_name, c1.cust_name, a1.appl_name, p1.prod_name, deferred from (select tank_id, ind_id, cust_id, appl_id, prod_id, count(*) deferred from sample_postponed_hist h1 "
                                + "where tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( OLD_NEXT_SAMPLE_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) group by tank_id,ind_id,cust_id, appl_id, prod_id, old_next_sample_date having count(*) = 1) t1 "
                                + "left join mst_industry d1 on t1.ind_id = d1.ind_id left join mst_customer c1 on t1.cust_id = c1.cust_id left join mst_application a1 on t1.appl_id = a1.appl_id left join mst_product p1 on t1.prod_id = p1.prod_id ";
                        break;
                    case "8":
                        query = "Select t1.tank_id,d1.ind_name, c1.cust_name, a1.appl_name, p1.prod_name, deferred from (select tank_id, ind_id, cust_id, appl_id, prod_id, count(*) deferred from sample_postponed_hist h1 "
                                + "where tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( OLD_NEXT_SAMPLE_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) group by tank_id,ind_id,cust_id, appl_id, prod_id, old_next_sample_date having count(*) > 1) t1 "
                                + "left join mst_industry d1 on t1.ind_id = d1.ind_id left join mst_customer c1 on t1.cust_id = c1.cust_id left join mst_application a1 on t1.appl_id = a1.appl_id left join mst_product p1 on t1.prod_id = p1.prod_id ";
                        break;
                    case "9":
                        query = "Select s.sample_id, s.sample_drawn_date, s.sample_drawn_remarks, s.sample_priority_id from sample_details s inner join mst_iom_ref i on s.IOM_REF_NO = i.IOM_REF_NO where s.tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) AND ( i.created_datetime BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ? ,'dd/mm/yyyy')) and s.status_id = 1 ";
                        break;
                    case "10":
                        query = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 2 and tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( LAB_RECEIVED_DATE BETWEEN TO_DATE( ?  ,'dd/mm/yyyy') and TO_DATE( ?  ,'dd/mm/yyyy'))";
                        break;
                    case "11":
                        query = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 3 and tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( TEST_RESULT_ENTERED_DATE BETWEEN TO_DATE( ? ,'dd/mm/yyyy') and TO_DATE( ?  ,'dd/mm/yyyy'))";
                        break;
                    case "12":
                        query = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 4 and tank_id in (select tank_id from mst_tank where cust_id in (select cust_id from mst_customer where emp_code = ? )) and ( TSE_FINALIZE_REPORT_DATE BETWEEN TO_DATE( ?  ,'dd/mm/yyyy') and TO_DATE( ?  ,'dd/mm/yyyy'))";
                        break;
                    default:
                        query = "Nada";
                        break;
                }
                if (!query.equals("Nada")) {
                    try (Connection con = DatabaseConnectionFactory.createConnection();
                            PreparedStatement pst = con.prepareStatement(query);) {
                        pst.setString(1, rowCode);
                        if (colNum.equals("5") || colNum.equals("7") || colNum.equals("8") || colNum.equals("9") || colNum.equals("10") || colNum.equals("11") || colNum.equals("12")) {
                            pst.setString(2, fromDate);
                            pst.setString(3, toDate);
                        }
                        ResultSet rs = pst.executeQuery();

                        while (rs.next()) {
                            ReportDetails rptDetails = new ReportDetails();
                            rptDetails.setCol1(rs.getString(1));
                            rptDetails.setCol2(rs.getString(2));
                            if (colNum.equals("3") || colNum.equals("5") || colNum.equals("7") || colNum.equals("8") || colNum.equals("9") || colNum.equals("10") || colNum.equals("11") || colNum.equals("12")) {
                                rptDetails.setCol3(rs.getString(3) != null ? rs.getString(3) : "N/A");
                                rptDetails.setCol4(rs.getString(4) != null ? rs.getString(4) : "N/A");
                                if (colNum.equals("7") || colNum.equals("8")) {
                                    rptDetails.setCol5(rs.getString(5) != null ? rs.getString(5) : "N/A");
                                    rptDetails.setCol6(rs.getString(6) != null ? rs.getString(6) : "N/A");
                                }
                            }
                            listRptDetails.add(rptDetails);
                        }
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "TsePerformanceReport ajax request");
                        new Gson().toJson("Error", response.getWriter());
                    }
                }
                new Gson().toJson(listRptDetails, response.getWriter());
                break;
            case "LabCSLEfficiencyReport":

                String query_2 = "";

                switch (colNum) {
                    case "2":
                        query_2 = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 1 and lab_loc_code = ?  and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy') ";
                        break;
                    case "3":
                        query_2 = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 2 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')";
                        break;
                    case "4":
                        query_2 = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 2 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')  and (SYSDATE - lab_received_date) > 10";//SHOULD NOT BE SYSDATE
                        break;
                    case "5":
                        query_2 = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 3 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')";
                        break;
                    case "6":
                        query_2 = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 3 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')  and (TEST_RESULT_ENTERED_DATE - lab_received_date) < 2";
                        break;
                    case "7":
                        query_2 = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 3 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')  and (TEST_RESULT_ENTERED_DATE - lab_received_date) < 3";
                        break;
                    case "8":
                        query_2 = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where status_id = 3 and lab_loc_code = ? and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')  and (TEST_RESULT_ENTERED_DATE - lab_received_date) < 5";
                        break;
                    default:
                        query = "Nada";
                        break;
                }
                if (!query_2.equals("Nada")) {
                    try (Connection con = DatabaseConnectionFactory.createConnection();
                            PreparedStatement pst = con.prepareStatement(query_2);) {
                        pst.setString(1, rowCode);
                        pst.setString(2, fromDate);
                        pst.setString(3, toDate);
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            ReportDetails rptDetails = new ReportDetails();
                            rptDetails.setCol1(rs.getString(1));
                            rptDetails.setCol2(rs.getString(2));
                            rptDetails.setCol3(rs.getString(3) != null ? rs.getString(3) : "N/A");
                            rptDetails.setCol4(rs.getString(4));
                            listRptDetails.add(rptDetails);
                        }
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "TsePerformanceReport ajax request");
                        new Gson().toJson("Error", response.getWriter());
                    }
                }
                new Gson().toJson(listRptDetails, response.getWriter());
                break;
            case "CustomerServicesReport":

                String query_3 = "";

                switch (colNum) {
                    case "4":
                        query_3 = "select t1.tank_id, d1.ind_name, c1.cust_name, t1.tank_desc from mst_tank t1 inner join mst_industry d1 on t1.ind_id = d1.ind_id inner join mst_customer c1 on t1.cust_id = c1.cust_id where t1.cust_id =  ? ";
                        break;
                    case "5":
                        query_3 = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where cust_id =  ?  and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy')";
                        break;
                    case "6":
                        query_3 = "select sample_id, sample_drawn_date, sample_drawn_remarks, sample_priority_id from sample_details where cust_id =  ?  and sample_drawn_date BETWEEN TO_DATE(  ?  ,'dd/mm/yyyy') and TO_DATE(  ?  ,'dd/mm/yyyy') and status_id = 4";
                        break;
                    default:
                        query = "Nada";
                        break;
                }
                if (!query_3.equals("Nada")) {
                    try (Connection con = DatabaseConnectionFactory.createConnection();
                            PreparedStatement pst = con.prepareStatement(query_3);) {
                        pst.setString(1, rowCode);
                        if (colNum.equals("5") || colNum.equals("6")) {
                            pst.setString(2, fromDate);
                            pst.setString(3, toDate);
                        }
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            ReportDetails rptDetails = new ReportDetails();
                            rptDetails.setCol1(rs.getString(1));
                            rptDetails.setCol2(rs.getString(2));
                            rptDetails.setCol3(rs.getString(3) != null ? rs.getString(3) : "N/A");
                            rptDetails.setCol4(rs.getString(4));
                            listRptDetails.add(rptDetails);
                        }
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "CustomerServicesReport ajax request");
                        new Gson().toJson("Error", response.getWriter());
                    }
                }
                new Gson().toJson(listRptDetails, response.getWriter());

                break;
            default:
                new Gson().toJson("Nada", response.getWriter());
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private String converttoCode(String rowName, String report) {
        String query = "";
        if (report.equalsIgnoreCase("TsePerformanceReport")) {
            query = "Select EMP_CODE from mst_user where EMP_NAME = ? ";
        } else if (report.equalsIgnoreCase("LabCSLEfficiencyReport")) {
            query = "Select LAB_LOC_CODE from MST_LAB where LAB_NAME = ? ";
        }else if (report.equalsIgnoreCase("CustomerServicesReport")) {
            query = "Select CUST_ID from MST_CUSTOMER where CUST_NAME = ? ";
        }
        String result = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, rowName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "Convert-name-to-code ajax request");
        }
        return result;
    }

}
