package globals;

import Exceptions.MyLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.sql.*;
import java.io.FileInputStream;

public final class CemDB_Connection {

    Connection conn = null, conSSMS = null;
    String line = "";
    boolean eof = false;
    public static PreparedStatement pSIns = null;
    private static String DBLogin = "";
    private static String DBPass = "";
    private static String JDBCurl = "";
    private static String Driver = "";

    public CemDB_Connection(String propFname) {
        conn = makeCemConnection(propFname);
    }

    public Connection makeCemConnection(String propFname) {
        Connection con = null;
        FileInputStream fIs = null;
        Properties prop = new Properties();
        try {
            fIs = new FileInputStream(propFname);
            prop.loadFromXML(fIs);
            DBLogin = prop.getProperty("login", DBLogin);
            DBPass = prop.getProperty("pass", DBPass);
            JDBCurl = prop.getProperty("JDBCurl", JDBCurl);
            Driver = prop.getProperty("Driver", Driver);
            Class.forName(Driver).newInstance();
            con = DriverManager.getConnection(JDBCurl, DBLogin, DBPass);

        } catch (Exception e) {
            MyLogger.logIt(e, "sendProcessingError()");
        }
        return con;
    }

    public Connection getCemConnection() {
        return this.conn;
    }

    public void closeCemConnection() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            MyLogger.logIt(e, "sendProcessingError()");
        }
    }

}
