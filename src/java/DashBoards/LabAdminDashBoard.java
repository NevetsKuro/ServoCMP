package DashBoards;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import operations.ApplicationSQLDate;
import org.json.JSONArray;
import org.json.JSONObject;
import viewModel.RecentActivity;
import viewModel.SampleDetails;

public class LabAdminDashBoard {

    public static List<String> getStatuses(String empCode, String days) {
        List<String> statuses = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT (SELECT COUNT(*) FROM SAMPLE_DETAILS WHERE STATUS_ID=0 "
                        + "AND (SYSDATE - SAMPLE_CREATED_DATE) <=?) AS STAT0, (SELECT COUNT(*) FROM SAMPLE_DETAILS WHERE "
                        + "STATUS_ID=1 AND (SYSDATE - SAMPLE_CREATED_DATE) <=?) AS STAT1, (SELECT COUNT(*) FROM "
                        + "SAMPLE_DETAILS WHERE STATUS_ID=2 AND (SYSDATE - SAMPLE_CREATED_DATE) <=?) AS STAT2, (SELECT "
                        + "COUNT(*) FROM SAMPLE_DETAILS WHERE STATUS_ID=3 AND (SYSDATE - SAMPLE_CREATED_DATE) <=?) AS "
                        + "STAT3, (SELECT COUNT(*) FROM SAMPLE_DETAILS WHERE STATUS_ID=4 AND (SYSDATE - SAMPLE_CREATED_DATE) "
                        + "<=?) AS STAT4 FROM DUAL");) {
            pst.setString(1, days);
            pst.setString(2, days);
            pst.setString(3, days);
            pst.setString(4, days);
            pst.setString(5, days);
            try (ResultSet res = pst.executeQuery();) {
                if (res.next()) {
                    statuses.add(res.getString(1));
                    statuses.add(res.getString(2));
                    statuses.add(res.getString(3));
                    statuses.add(res.getString(4));
                    statuses.add(res.getString(5));
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return statuses;
    }

    public static List<String> getCustIndWise(String empCode) {
        ArrayList<ArrayList<String>> listOLists = new ArrayList<ArrayList<String>>();
        ArrayList<String> singleList = new ArrayList<String>();
        List<String> custInd = new ArrayList<>();
        String Customers = "'series': [";
        String IndData = "{'colorByPoint': true, 'data': [";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT  T3.IND_NAME, T2.CUST_NAME, COUNT(*) from MST_TANK T1 "
                        + "INNER JOIN MST_CUSTOMER T2 ON T1.CUST_ID=T2.CUST_ID INNER JOIN MST_INDUSTRY T3 ON T1.IND_ID = "
                        + "T3.IND_ID GROUP BY ROLLUP(T3.IND_NAME,T2.CUST_NAME) ORDER BY T3.IND_NAME");
                ResultSet res = pst.executeQuery();) {
            while (res.next()) {
                if (res.getString(2) == null) {
                    if (res.getString(1) != null) {
                        if (singleList.size() > 0) {
                            listOLists.add(singleList);
                            singleList = new ArrayList<String>();
                        }
                        IndData += "{'name':'" + res.getString(1) + "', 'y':" + res.getString(3) + ", 'drilldown':'" + res.getString(1) + "'},";
                    }
                }
                if (res.getString(2) != null) {
                    boolean isFound = singleList.contains(res.getString(1));
                    if (!isFound) {
                        if (singleList.size() > 0) {
                            listOLists.add(singleList);
                            singleList = new ArrayList<String>();
                        }
                        singleList.add(res.getString(1));
                        singleList.add(res.getString(2) + "::" + res.getString(3));
                    } else {
                        singleList.add(res.getString(2) + "::" + res.getString(3));
                    }
                }
            }
            IndData += "]}";
            if (singleList.size() > 0) {
                listOLists.add(singleList);
            }
            for (int i = 0; i < listOLists.size(); i++) {
                if (i != listOLists.size() - 1) {
                    singleList = listOLists.get(i);
                    Customers += "{'name':'" + singleList.get(0) + "','id':'" + singleList.get(0) + "', 'data':[";
                    for (int j = 1; j < singleList.size(); j++) {
                        String[] arrSplit = singleList.get(j).split("::");
                        if (j != singleList.size() - 1) {
                            Customers += "['" + arrSplit[0] + "'," + arrSplit[1] + "],";
                        } else {
                            Customers += "['" + arrSplit[0] + "'," + arrSplit[1] + "]";
                        }
                    }
                    Customers += "]},";
                } else {
                    singleList = listOLists.get(i);
                    Customers += "{'name':'" + singleList.get(0) + "','id':'" + singleList.get(0) + "', 'data':[";
                    for (int j = 1; j < singleList.size(); j++) {
                        String[] arrSplit = singleList.get(j).split("::");
                        if (j != singleList.size() - 1) {
                            Customers += "['" + arrSplit[0] + "'," + arrSplit[1] + "],";
                        } else {
                            Customers += "['" + arrSplit[0] + "'," + arrSplit[1] + "]";
                        }
                    }
                    Customers += "]}";
                }
            }
            Customers += "]";
            custInd.add(IndData);
            custInd.add(Customers);
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return custInd;
    }

    public static String getLast10Data(String empCode) {
       // empCode = "507469";
        String last10Data = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT * FROM (SELECT SAMPLE_CREATED_DATE,COUNT(*) FROM "
                        + "SAMPLE_DETAILS WHERE (SYSDATE-SAMPLE_CREATED_DATE)<= 1000  GROUP BY SAMPLE_CREATED_DATE ORDER "
                        + "BY SAMPLE_CREATED_DATE DESC) T WHERE ROWNUM <=10");
                ResultSet res = pst.executeQuery();) {
            if (res.isBeforeFirst()) {
                while (res.next()) {
                    String[] parts = ApplicationSQLDate.convertUtilDatetoString(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(1))).split("-");
                    last10Data = "[Date.UTC(" + parts[2] + ", " + parts[1] + "-1, " + parts[0] + "), " + res.getString(2) + "], " + last10Data;
                }
            } else {
                return "";
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return method(last10Data);
    }

    public static String getUpcomingData(String empCode) {
        String UpcomingData = "";
        try (Connection con = DatabaseConnectionFactory.createConnection();
               PreparedStatement pst = con.prepareStatement("SELECT * FROM (SELECT t1.NEXT_SAMPLE_DATE, count(*) FROM "
                        + "MST_TANK t1, MST_INDUSTRY t2, MST_CUSTOMER t3 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID = "
                        + "t3.CUST_ID AND (t1.NEXT_SAMPLE_DATE-SYSDATE) <= 10 group by t1.NEXT_SAMPLE_DATE order by "
                        + "t1.NEXT_SAMPLE_DATE ASC) T WHERE ROWNUM <= 10");) {
           // pst.setString(1, empCode);
            try (ResultSet res = pst.executeQuery();) {
                if (res.isBeforeFirst()) {
                    while (res.next()) {
                        String[] parts = ApplicationSQLDate.convertUtilDatetoString(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(1))).split("-");
                        UpcomingData = "[Date.UTC(" + parts[2] + ", " + parts[1] + "-1, " + parts[0] + "), " + res.getString(2) + "], " + UpcomingData;
                    }
                } else {
                    return "";
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return method(UpcomingData);
    }

    public static List<RecentActivity> getRecentActivity(String empCode, String days) {
        List<RecentActivity> rAct = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T1.SAMPLE_ID, T2.CUST_NAME, T3.STATUS_NAME FROM "
                        + "(select * from (SELECT SAMPLE_ID, CUST_ID, STATUS_ID FROM SAMPLE_DETAILS WHERE (SYSDATE - "
                        + "SAMPLE_CREATED_DATE) <=? order by SAMPLE_CREATED_DATE desc) where rownum <= 5 ) T1 INNER "
                        + "JOIN MST_CUSTOMER T2 ON T1.CUST_ID=T2.CUST_ID INNER JOIN MST_STATUS T3 ON T1.STATUS_ID = "
                        + "T3.STATUS_ID");) {
            pst.setString(1, days);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    RecentActivity recAct = new RecentActivity();
                    recAct.setSampleId(res.getString(1));
                    recAct.setCustName(res.getString(2));
                    recAct.setStatusName(res.getString(3));
                    rAct.add(recAct);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return rAct;
    }

    public static JSONArray getUpcomingDataAjax(String empCode,String notifyDays) {
        JSONArray jarray = new JSONArray();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(""+ 
                "select T1.CUST_ID,T1.NEXT_SAMPLE_DATE,T2.EMP_CODE ,T2.CUST_NAME ,T1.TOTAL_NOTIFY  from " +
                "( select CUST_ID,NEXT_SAMPLE_DATE, count(*)  TOTAL_NOTIFY from MST_TANK " +
                "where  (NEXT_SAMPLE_DATE-SYSDATE)>= ? and CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE=?) " +
                "group by CUST_ID,NEXT_SAMPLE_DATE)  T1, MST_CUSTOMER T2 " +
                "where T1.CUST_ID=T2.CUST_ID AND rownum<=10 order by T1.NEXT_SAMPLE_DATE");) {
            pst.setString(1, notifyDays);
            pst.setString(2, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    JSONObject jObj = new JSONObject();
                    jObj.put("name", res.getString(4));
                    jObj.put("custId", res.getString(1));
                    jObj.put("date", res.getString(2));
                    jObj.put("y", res.getString(5));
                    jarray.put(jObj);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return jarray;
    }
    
    public static JSONArray getLast10DataAjax(String empCode,String notifyDays) {
        JSONArray jarray = new JSONArray();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(""+ 
                "select T1.CUST_ID,T1.NEXT_SAMPLE_DATE,T2.EMP_CODE ,T2.CUST_NAME ,T1.TOTAL_NOTIFY  from " +
                "( select CUST_ID,NEXT_SAMPLE_DATE, count(*)  TOTAL_NOTIFY from MST_TANK " +
                "where  (NEXT_SAMPLE_DATE-SYSDATE)<= ? and CUST_ID IN(SELECT CUST_ID FROM MST_CUSTOMER WHERE EMP_CODE=?) " +
                "group by CUST_ID,NEXT_SAMPLE_DATE) T1, MST_CUSTOMER T2 " +
                "where T1.CUST_ID=T2.CUST_ID AND rownum<=10 order by T1.NEXT_SAMPLE_DATE");) {
            pst.setString(1, notifyDays);
            pst.setString(2, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    JSONObject jObj = new JSONObject();
                    jObj.put("name", res.getString(4));
                    jObj.put("custId", res.getString(1));
                    jObj.put("date", res.getString(2));
                    jObj.put("y", res.getString(5));
                    jarray.put(jObj);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return jarray;
    }
    
    public static List<SampleDetails> getPendingSamplesinByIndustry(String empCode) {
        List<SampleDetails> sdArray = new ArrayList<SampleDetails>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("" +
                "SELECT  t3.CUST_NAME, t2.ind_name, t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE,t1.exp_test_result_date ,t1.LAB_RECEIVED_DATE," +
                "t1.SAMPLE_PRIORITY_ID, t9.priority_name FROM SAMPLE_DETAILS t1, " +
                "MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, " +
                "MST_PRODUCT t7, MST_TANK t8, mst_priority t9 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID " +
                "AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.PROD_ID=t7.PROD_ID and t1.tank_id = t8.tank_id AND t1.SAMPLE_PRIORITY_ID = t9.priority_id AND " +
                "t1.LAB_LOC_CODE in(select LAB_LOC_CODE FROM MST_LAB where EMP_CODE=?) AND " +
                "t1.STATUS_ID IN ('2') order by t1.SAMPLE_PRIORITY_ID");) {
            pst.setString(1, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    SampleDetails sd = new SampleDetails();
                    sd.getMstDept().getMstCustomer().setCustomerName(res.getString(1));
                    sd.getMstInd().setIndName(res.getString(2));
                    sd.setSamplecreatedBy(res.getString(3));
                    sd.setSamplecreatedDate(res.getDate(4));
                    sd.setStringExptestresultDate(res.getString(5));
                    sd.setLabrecDate(res.getDate(6));
                    sd.setSamplepriorityId(res.getString(7));
                    sd.setSamplepriorityName(res.getString(8));
                    sdArray.add(sd);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return sdArray;
    }
    
    public static List<SampleDetails> getPendingSamplesinProcess(String empCode) {
        List<SampleDetails> sdArray = new ArrayList<SampleDetails>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(""+ 
                "SELECT  t3.CUST_NAME, t2.ind_name, t1.SAMPLE_CREATED_BY, t1.SAMPLE_CREATED_DATE,t1.exp_test_result_date ,t1.LAB_RECEIVED_DATE," +
                "t1.SAMPLE_PRIORITY_ID, t9.priority_name FROM SAMPLE_DETAILS t1, " +
                "MST_INDUSTRY t2, MST_CUSTOMER t3, MST_DEPARTMENT t4, MST_APPLICATION t5, MST_EQUIPMENT t6, " +
                "MST_PRODUCT t7, MST_TANK t8, mst_priority t9 where t1.IND_ID=t2.IND_ID AND t1.CUST_ID=t3.CUST_ID AND t1.DEPT_ID=t4.DEPT_ID " +
                "AND t1.APPL_ID=t5.APPL_ID AND t1.EQUIP_ID=t6.EQUIP_ID AND t1.PROD_ID=t7.PROD_ID and t1.tank_id = t8.tank_id AND t1.SAMPLE_PRIORITY_ID = t9.priority_id AND " +
                "t1.LAB_LOC_CODE in(select LAB_LOC_CODE FROM MST_LAB where EMP_CODE=?) AND " +
                "t1.STATUS_ID IN ('2') order by t1.SAMPLE_PRIORITY_ID");) {
            pst.setString(1, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    SampleDetails sd = new SampleDetails();
                    sd.getMstDept().getMstCustomer().setCustomerName(res.getString(1));
                    sd.getMstInd().setIndName(res.getString(2));
                    sd.setSamplecreatedBy(res.getString(3));
                    sd.setSamplecreatedDate(res.getDate(4));
                    sd.setStringExptestresultDate(res.getString(5));
                    sd.setLabrecDate(res.getDate(6));
                    sd.setSamplepriorityId(res.getString(7));
                    sd.setSamplepriorityName(res.getString(8));
                    sdArray.add(sd);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return sdArray;
    }
    
    
    private static String method(String last10Data) {
        return last10Data.substring(0, last10Data.length() - 2);
    }
}
