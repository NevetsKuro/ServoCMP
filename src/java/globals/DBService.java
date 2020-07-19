package globals;

import Exceptions.MyLogger;
import java.sql.*;
import java.util.*;

public class DBService {

    private Connection con;
    public boolean isconnected;
    public boolean isconnectionvalid;
    private ReadSnConfig snconf;
    public String sErrorMsg;
    public String uploadfilepath = "";
    public String uploadinspectionpath = "";
    public String startmonth = "";

    public DBService() {
        isconnected = false;
        isconnectionvalid = false;
        con = null;
        snconf = new ReadSnConfig();
        snconf.readParameters();
        uploadfilepath = snconf.uploadfilepath;
        startmonth = snconf.startmonth;
        try {
            Class.forName(snconf.dbdriver);
            isconnectionvalid = true;
        } catch (Exception e) {
            MyLogger.logIt(e, "sendProcessingError()");
            sErrorMsg = e.getMessage();
        }
    }

    private boolean openConnection() {
        if (isconnectionvalid == true && isconnected == false) {
            try {
                Class.forName(snconf.dbdriver).newInstance();
                Properties props = new Properties();
                props.setProperty("user", snconf.loginuser);
                props.setProperty("password", snconf.loginpassword);
                props.setProperty("oracle.jdbc.convertNcharLiterals", "true");
                props.setProperty("oracle.jdbc.defaultNChar", "true");
                con = DriverManager.getConnection(snconf.dbpath, props);
                isconnected = true;
            } catch (Exception e) {
                sErrorMsg = e.getMessage();
                isconnected = false;
            }
        }
        return isconnected;
    }

    public void closeConnection() {
        try {
            con.close();
            isconnected = false;
        } catch (Exception e) {
            isconnected = false;
            sErrorMsg = e.getMessage();
        }
    }

    public Connection getConnection() {
        if (isconnected) {
            return con;
        } else {
            boolean flag = openConnection();
            if (flag == true) {
                return con;
            } else {
                return null;
            }
        }
    }
}