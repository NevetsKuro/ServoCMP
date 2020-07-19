package DAOs;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import viewModel.MstRole;

public class MstRoleDAO {

    public static List<MstRole> listAllRoles() {
        List<MstRole> mstRole = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM MST_ROLE");) {
            while (res.next()) {
                MstRole mRole = new MstRole();
                mRole.setRoleId(res.getString(1));
                mRole.setRoleName(res.getString(2));
                mRole.setFuncAreaCode(res.getString(3));
                mRole.setLocCode(res.getString(4));
                mstRole.add(mRole);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstRoleDAO.listAllRoles()");
        }
        return mstRole;
    }

    public static List<MstRole> listAllTseRoles() {
        List<MstRole> mstRole = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
//                ResultSet res = st.executeQuery("SELECT * FROM MST_ROLE WHERE UPPER(ROLE_NAME) LIKE '%TSE%'");) {
                ResultSet res = st.executeQuery("SELECT * FROM MST_ROLE")) {
            while (res.next()) {
                MstRole mRole = new MstRole();
                mRole.setRoleId(res.getString(1));
                mRole.setRoleName(res.getString(2));
                mRole.setFuncAreaCode(res.getString(3));
                mRole.setLocCode(res.getString(4));
                mstRole.add(mRole);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstRoleDAO.listAllRoles()");
        }
        return mstRole;
    }
    
    public static List<MstRole> listAllLabRoles() {
        List<MstRole> mstRole = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM MST_ROLE WHERE UPPER(ROLE_NAME) LIKE '%LAB%'");) {
            while (res.next()) {
                MstRole mRole = new MstRole();
                mRole.setRoleId(res.getString(1));
                mRole.setRoleName(res.getString(2));
                mRole.setFuncAreaCode(res.getString(3));
                mRole.setLocCode(res.getString(4));
                mstRole.add(mRole);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstRoleDAO.listAllRoles()");
        }
        return mstRole;
    }
}
