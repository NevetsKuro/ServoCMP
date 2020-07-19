package globals;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {

    private String user_id = "", user_name = "", emp_code = "", emp_email = "", emp_contact_no = "", role_id = "", sEMP_CODE = "", sEMP_NAME = "",
            sDESIGN_SHORT_DESC = "", sCURR_COMP_CODE = "", sCURR_COMP = "", sPSA_CODE = "", sPSA = "", sSECTION_CODE = "", sSECTION = "", sLOC_CODE = "", sFUNC_CODE = "",
            sFUNC = "", sFUNC_AREA_CODE = "", sFUNC_AREA = "", sEMAIL_ID = "", sEMP_STATUS_CODE = "", sCNTRLG_EMP_CODE = "", sLabLocCode = "";
    private boolean hasTseAccess, hasLabAccess, isloggedin, isvaliduser;
    private Connection dbCon;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_contact_no() {
        return emp_contact_no;
    }

    public void setEmp_contact_no(String emp_contact_no) {
        this.emp_contact_no = emp_contact_no;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getsEMP_CODE() {
        return sEMP_CODE;
    }

    public void setsEMP_CODE(String sEMP_CODE) {
        this.sEMP_CODE = sEMP_CODE;
    }

    public String getsEMP_NAME() {
        return sEMP_NAME;
    }

    public void setsEMP_NAME(String sEMP_NAME) {
        this.sEMP_NAME = sEMP_NAME;
    }

    public String getsDESIGN_SHORT_DESC() {
        return sDESIGN_SHORT_DESC;
    }

    public void setsDESIGN_SHORT_DESC(String sDESIGN_SHORT_DESC) {
        this.sDESIGN_SHORT_DESC = sDESIGN_SHORT_DESC;
    }

    public String getsCURR_COMP_CODE() {
        return sCURR_COMP_CODE;
    }

    public void setsCURR_COMP_CODE(String sCURR_COMP_CODE) {
        this.sCURR_COMP_CODE = sCURR_COMP_CODE;
    }

    public String getsCURR_COMP() {
        return sCURR_COMP;
    }

    public void setsCURR_COMP(String sCURR_COMP) {
        this.sCURR_COMP = sCURR_COMP;
    }

    public String getsPSA_CODE() {
        return sPSA_CODE;
    }

    public void setsPSA_CODE(String sPSA_CODE) {
        this.sPSA_CODE = sPSA_CODE;
    }

    public String getsPSA() {
        return sPSA;
    }

    public void setsPSA(String sPSA) {
        this.sPSA = sPSA;
    }

    public String getsSECTION_CODE() {
        return sSECTION_CODE;
    }

    public void setsSECTION_CODE(String sSECTION_CODE) {
        this.sSECTION_CODE = sSECTION_CODE;
    }

    public String getsSECTION() {
        return sSECTION;
    }

    public void setsSECTION(String sSECTION) {
        this.sSECTION = sSECTION;
    }

    public String getsLOC_CODE() {
        return sLOC_CODE;
    }

    public void setsLOC_CODE(String sLOC_CODE) {
        this.sLOC_CODE = sLOC_CODE;
    }

    public String getsFUNC_CODE() {
        return sFUNC_CODE;
    }

    public void setsFUNC_CODE(String sFUNC_CODE) {
        this.sFUNC_CODE = sFUNC_CODE;
    }

    public String getsFUNC() {
        return sFUNC;
    }

    public void setsFUNC(String sFUNC) {
        this.sFUNC = sFUNC;
    }

    public String getsFUNC_AREA_CODE() {
        return sFUNC_AREA_CODE;
    }

    public void setsFUNC_AREA_CODE(String sFUNC_AREA_CODE) {
        this.sFUNC_AREA_CODE = sFUNC_AREA_CODE;
    }

    public String getsFUNC_AREA() {
        return sFUNC_AREA;
    }

    public void setsFUNC_AREA(String sFUNC_AREA) {
        this.sFUNC_AREA = sFUNC_AREA;
    }

    public String getsEMAIL_ID() {
        return sEMAIL_ID;
    }

    public void setsEMAIL_ID(String sEMAIL_ID) {
        this.sEMAIL_ID = sEMAIL_ID;
    }

    public String getsEMP_STATUS_CODE() {
        return sEMP_STATUS_CODE;
    }

    public void setsEMP_STATUS_CODE(String sEMP_STATUS_CODE) {
        this.sEMP_STATUS_CODE = sEMP_STATUS_CODE;
    }

    public String getsCNTRLG_EMP_CODE() {
        return sCNTRLG_EMP_CODE;
    }

    public void setsCNTRLG_EMP_CODE(String sCNTRLG_EMP_CODE) {
        this.sCNTRLG_EMP_CODE = sCNTRLG_EMP_CODE;
    }

    public String getsLabLocCode() {
        return sLabLocCode;
    }

    public void setsLabLocCode(String sLabLocCode) {
        this.sLabLocCode = sLabLocCode;
    }

    public boolean isHasTseAccess() {
        return hasTseAccess;
    }

    public void setHasTseAccess(boolean hasTseAccess) {
        this.hasTseAccess = hasTseAccess;
    }

    public boolean isHasLabAccess() {
        return hasLabAccess;
    }

    public void setHasLabAccess(boolean hasLabAccess) {
        this.hasLabAccess = hasLabAccess;
    }

    public boolean isIsloggedin() {
        return isloggedin;
    }

    public void setIsloggedin(boolean isloggedin) {
        this.isloggedin = isloggedin;
    }

    public boolean isIsvaliduser() {
        return isvaliduser;
    }

    public void setIsvaliduser(boolean isvaliduser) {
        this.isvaliduser = isvaliduser;
    }

    public Connection getDbCon() {
        return dbCon;
    }

    public void setDbCon(Connection dbCon) {
        this.dbCon = dbCon;
    }

    public boolean initialiseCemUser(String empCode, String sCemDBPath) {
        boolean flag = false;
        try (Connection con = DatabaseConnectionFactory.createCEMConnection(sCemDBPath);
                PreparedStatement pst = con.prepareStatement("select EMP_CODE, EMP_NAME, DESIGN_SHORT_DESC, CURR_COMP_CODE, CURR_COMP, PSA_CODE, PSA, "
                        + "SECTION_CODE, SECTION, LOC_CODE, FUNC_CODE, FUNC, FUNC_AREA_CODE, FUNC_AREA, EMAIL_ID, EMP_STATUS_CODE, CNTRLG_EMP_CODE "
                        + "from COM_EMP_DB.EMP_DB where EMP_CODE = ?");) {
            pst.setString(1, empCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                if (res.getString("EMP_CODE") != null) {
                    this.sEMP_CODE = res.getString("EMP_CODE");
                } else {
                    this.sEMP_CODE = "";
                }
                if (res.getString("EMP_NAME") != null) {
                    this.sEMP_NAME = res.getString("EMP_NAME");
                } else {
                    this.sEMP_NAME = "";
                }
                if (res.getString("DESIGN_SHORT_DESC") != null) {
                    this.sDESIGN_SHORT_DESC = res.getString("DESIGN_SHORT_DESC");
                } else {
                    this.sDESIGN_SHORT_DESC = "";
                }
                if (res.getString("CURR_COMP_CODE") != null) {
                    this.sCURR_COMP_CODE = res.getString("CURR_COMP_CODE");
                } else {
                    this.sCURR_COMP_CODE = "";
                }
                if (res.getString("CURR_COMP") != null) {
                    this.sCURR_COMP = res.getString("CURR_COMP");
                } else {
                    this.sCURR_COMP = "";
                }
                if (res.getString("PSA_CODE") != null) {
                    this.sPSA_CODE = res.getString("PSA_CODE");
                } else {
                    this.sPSA_CODE = "";
                }
                if (res.getString("SECTION_CODE") != null) {
                    this.sSECTION_CODE = res.getString("SECTION_CODE");
                } else {
                    this.sSECTION_CODE = "";
                }
                if (res.getString("SECTION") != null) {
                    this.sSECTION = res.getString("SECTION");
                } else {
                    this.sSECTION = "";
                }
                if (res.getString("LOC_CODE") != null) {
                    this.sLOC_CODE = res.getString("LOC_CODE");
                } else {
                    this.sLOC_CODE = "";
                }
                if (res.getString("FUNC_CODE") != null) {
                    this.sFUNC_CODE = res.getString("FUNC_CODE");
                } else {
                    this.sFUNC_CODE = "";
                }
                if (res.getString("FUNC") != null) {
                    this.sFUNC = res.getString("FUNC");
                } else {
                    this.sFUNC = "";
                }
                if (res.getString("FUNC_AREA_CODE") != null) {
                    this.sFUNC_AREA_CODE = res.getString("FUNC_AREA_CODE");
                } else {
                    this.sFUNC_AREA_CODE = "";
                }
                if (res.getString("FUNC_AREA") != null) {
                    this.sFUNC_AREA = res.getString("FUNC_AREA");
                } else {
                    this.sFUNC_AREA = "";
                }
                if (res.getString("EMAIL_ID") != null) {
                    this.sEMAIL_ID = res.getString("EMAIL_ID");
                } else {
                    this.sEMAIL_ID = "";
                }
                if (res.getString("EMP_STATUS_CODE") != null) {
                    this.sEMP_STATUS_CODE = res.getString("EMP_STATUS_CODE");
                } else {
                    this.sEMP_STATUS_CODE = "";
                }
                if (res.getString("CNTRLG_EMP_CODE") != null) {
                    this.sCNTRLG_EMP_CODE = res.getString("CNTRLG_EMP_CODE");
                } else {
                    this.sCNTRLG_EMP_CODE = "";
                }
                if (!this.user_id.equalsIgnoreCase("")) {
                    this.isvaliduser = true;
                    flag = true;
                }
            } else {
                this.isvaliduser = false;
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "User.initialiseCemUser() ");
        }
        return flag;
    }

    public boolean getUserRoleId(String sFuncAreaCode, String sLocCode, String empCode) {
        boolean flag = false;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select ROLE_ID from MST_USER where EMP_CODE= ?");) {
            pst.setString(1, empCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                if (res.getString(1) != null) {
                    this.role_id = res.getString(1);
                } else {
                    this.role_id = "";
                }
                if (!this.role_id.equalsIgnoreCase("")) {
                    this.isvaliduser = true;
                    flag = true;
                }
            }
        } catch (Exception ex) {
            flag = false;
            MyLogger.logIt(ex, "User.getUserRoleId() ");
        }
        return flag;
    }

    public boolean initialiseTseUser(String empCode) {
        boolean flag = false;
        if (isloggedin) {
            try (Connection con = DatabaseConnectionFactory.createConnection();
                    PreparedStatement pst = con.prepareStatement("select count(*) from MST_CUSTOMER where EMP_CODE= ?");) {
                pst.setString(1, empCode);
                ResultSet res = pst.executeQuery();
                if (res.next()) {
                    if (!res.getString(1).equals("0")) {
                        this.hasTseAccess = true;
                        flag = true;
                    } else {
                        this.hasTseAccess = false;
                    }
                } else {
                    this.hasTseAccess = false;
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "User.initialiseTseUser() ");
            }
        }
        return flag;
    }

    public boolean initialiseLabUser(String empCode) {
        boolean flag = false;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select LAB_LOC_CODE from MST_LAB where EMP_CODE=?");) {
            pst.setString(1, empCode);
            ResultSet res = pst.executeQuery();
            if (isloggedin) {
                if (res.next()) {
                    if (res.getString(1) != null) {
                        this.hasLabAccess = true;
                        this.sLabLocCode = res.getString(1);
                        flag = true;
                    } else {
                        this.hasLabAccess = false;
                    }
                } else {
                    this.hasLabAccess = false;
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "User.initialiseLabUser() ");
        }
        return flag;
    }
}
