package operations;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.CallableStatement;
import java.sql.Connection;

public class ErrorLogger {

    public static String getErrorCode(String errMsg, String updatedBy) {
        try (Connection con = DatabaseConnectionFactory.createConnection();
                CallableStatement cst = con.prepareCall("{call INSERT_ERR_MSG_DETAILS_PROC(?,?,?,?,?)}");) {
            cst.setString(1, errMsg);
            cst.setString(2, updatedBy);
            cst.setDate(3, ApplicationSQLDate.getcurrentSQLDate());
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.executeUpdate();
            return cst.getString(4) + cst.getString(5);
        } catch (Exception ex) {
            MyLogger.logIt(ex, updatedBy);
            MyLogger.logIt(ex, "sendProcessingError()");
            return null;
        }

    }
}
