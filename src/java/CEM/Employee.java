package CEM;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import viewModel.MstUser;

public class Employee {

    public static MstUser getEmployeeDetails(String empCode, String sCemDBPath) {
        MstUser mstUser = new MstUser();
        try (Connection con = DatabaseConnectionFactory.createCEMConnection(sCemDBPath);
                PreparedStatement pst = con.prepareStatement("SELECT T1.EMP_CODE, T1.EMP_NAME, T1.EMAIL_ID, T1.CNTRLG_EMP_CODE, T2.EMP_NAME, T2.EMAIL_ID FROM "
                        + "COM_EMP_DB.EMP_DB T1 INNER JOIN COM_EMP_DB.EMP_DB T2 ON T1.CNTRLG_EMP_CODE = T2.EMP_CODE WHERE T1.EMP_CODE = ?");) {
            pst.setString(1, empCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                mstUser.setEmpCode(res.getString(1));
                mstUser.setEmpName(res.getString(2));
                mstUser.setEmpEmail(res.getString(3));
                mstUser.setCtrlEmpCode(res.getString(4));
                mstUser.setCtrlEmpName(res.getString(5));
                mstUser.setCtrlEmpEmail(res.getString(6));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return mstUser;
    }

    public static List<MstUser> getLocRoleEmp(String fCode, String locCode, String cemPath) {
        String Query = "";
        List<MstUser> cemUser = new ArrayList();
        if (!"".equals(locCode)) {
            Query = "SELECT EMP_CODE, EMP_NAME, EMAIL_ID, CNTRLG_EMP_CODE FROM COM_EMP_DB.EMP_DB WHERE EMP_STATUS_CODE = 3 AND FUNC_AREA_CODE = ? AND LOC_CODE = ?";
            try (Connection con = DatabaseConnectionFactory.createCEMConnection(cemPath);
                    PreparedStatement pst = con.prepareStatement(Query);) {
                pst.setString(1, fCode);
                pst.setInt(2, Integer.parseInt(locCode));
                ResultSet res = pst.executeQuery();
                while (res.next()) {
                    MstUser mstUser = new MstUser();
                    mstUser.setEmpCode(res.getString(1));
                    mstUser.setEmpName(res.getString(2));
                    mstUser.setEmpEmail(res.getString(3));
                    mstUser.setCtrlEmpCode(res.getString(4));
                    cemUser.add(mstUser);
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "Employee.getCemUser(fCode, locCode, cemPath)");
            }
        }else{
            Query = "SELECT EMP_CODE, EMP_NAME, EMAIL_ID, CNTRLG_EMP_CODE FROM COM_EMP_DB.EMP_DB WHERE EMP_STATUS_CODE = 3 AND FUNC_AREA_CODE = ?";
            try (Connection con = DatabaseConnectionFactory.createCEMConnection(cemPath);
                    PreparedStatement pst = con.prepareStatement(Query);) {
                pst.setString(1, fCode);
                ResultSet res = pst.executeQuery();
                while (res.next()) {
                    MstUser mstUser = new MstUser();
                    mstUser.setEmpCode(res.getString(1));
                    mstUser.setEmpName(res.getString(2));
                    mstUser.setEmpEmail(res.getString(3));
                    mstUser.setCtrlEmpCode(res.getString(4));
                    cemUser.add(mstUser);
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "Employee.getCemUser(fCode, locCode, cemPath)");
            }
        }
        return cemUser;
    }

}
