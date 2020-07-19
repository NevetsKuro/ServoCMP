package com;

import globals.CemDB_Connection;
import java.sql.Connection;

public class DatabaseConnectionFactory {

    public static Connection createConnection() {
        globals.User user = new globals.User();
        globals.DBService dbservice = new globals.DBService();
        user.setIsloggedin(true);
        user.setDbCon(dbservice.getConnection());
        return user.getDbCon();
    }
    
    public static Connection createCEMConnection(String sCemDBPath){
        CemDB_Connection cemConnection = new CemDB_Connection(sCemDBPath);
        return cemConnection.getCemConnection();
    }
}
