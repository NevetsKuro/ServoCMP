package initializeSesion;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import viewModel.MstTest;

public class initializeValues {

    private static String ServerLogsHost;

    public static String getServerLogsHost() {
        return ServerLogsHost;
    }

    public static void setServerLogsHost(String ServerLogsHost) {
        initializeValues.ServerLogsHost = ServerLogsHost;
    }

    public static void setSessionControlTable(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT PARAM,P_VALUE FROM CONTROL_TABLE");) {
            while (res.next()) {
                switch (res.getString(1)) {
                    case "NotifyDaysLimit":
                        session.setAttribute("NotifyDaysLimit", res.getString(2));
                        break;
                    case "SystemMaintenanceFlag":
                        session.setAttribute("SystemMaintenanceFlag", res.getString(2));
                        break;
                    case "LabSampleFilterDaysLimit":
                        session.setAttribute("LabSampleFilterDaysLimit", res.getString(2));
                        break;
                    case "TseSampleFilterDaysLimit":
                        session.setAttribute("TseSampleFilterDaysLimit", res.getString(2));
                        break;
                    case "postponerestrictDays":
                        session.setAttribute("postponerestrictDays", res.getString(2));
                        break;
                    case "sendtoLABdateLimit":
                        session.setAttribute("sendtoLABdateLimit", res.getString(2));
                        break;
                    case "highpriorityDays":
                        session.setAttribute("highpriorityDays", res.getString(2));
                        break;
                    case "mediumpriorityDays":
                        session.setAttribute("mediumpriorityDays", res.getString(2));
                        break;
                    case "normalpriorityDays":
                        session.setAttribute("normalpriorityDays", res.getString(2));
                        break;
                    case "createrestrictDays":
                        session.setAttribute("createrestrictDays", res.getString(2));
                        break;
                    case "ServerLogsHost":
                        setServerLogsHost(res.getString(2));
                        session.setAttribute("ServerLogsHost", res.getString(2));
                        break;
                    case "TseDashDaysLimit":
                        session.setAttribute("TseDashDaysLimit", res.getString(2));
                        break;
                    case "LabDashDaysLimit":
                        session.setAttribute("LabDashDaysLimit", res.getString(2));
                        break;
                    case "dataRowRetrieveLimit":
                        session.setAttribute("dataRowRetrieveLimit", res.getString(2));
                        break;
                    case "EmpPersonalFormLink":
                        session.setAttribute("persLink", res.getString(2));
                        break;
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "initializeValues.setSessionControlTable()");
        }
    }

    public static void setSessionTestMaster(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<MstTest> liTst = new ArrayList();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT TEST_ID,TEST_NAME,UNIT FROM MST_TEST order by TEST_ID");) {
            while (res.next()) {
                MstTest mt = new MstTest();
                mt.setTestId(res.getString(1));
                mt.setTestName(res.getString(2));
                mt.setUnit(res.getString(3));
                liTst.add(mt);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "initializeValues.setSessionTestMaster()");
        }
        session.setAttribute("TblTestMaster", liTst);
    }
}
