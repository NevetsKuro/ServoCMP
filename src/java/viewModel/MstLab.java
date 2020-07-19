package viewModel;

public class MstLab {

    private String labId, labCode, labName, labAdd1, labAdd2, labAdd3, labAuthority, active, labEmpCode, labType,labTseCode;

    private MstUser mstUser,mstTseUser;

    public MstLab() {
    }

    public String getLabTseCode() {
        return labTseCode;
    }

    public void setLabTseCode(String labTseCode) {
        this.labTseCode = labTseCode;
    }

    public String getLabType() {
        return labType;
    }

    public void setLabType(String labType) {
        this.labType = labType;
    }
    
    public MstLab(MstUser mstUser) {
        mstUser = new MstUser();
    }

    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
    }

    public String getLabCode() {
        return labCode;
    }

    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getLabAdd1() {
        return labAdd1;
    }

    public void setLabAdd1(String labAdd1) {
        this.labAdd1 = labAdd1;
    }

    public String getLabAdd2() {
        return labAdd2;
    }

    public void setLabAdd2(String labAdd2) {
        this.labAdd2 = labAdd2;
    }

    public String getLabAdd3() {
        return labAdd3;
    }

    public void setLabAdd3(String labAdd3) {
        this.labAdd3 = labAdd3;
    }

    public String getLabAuthority() {
        return labAuthority;
    }

    public void setLabAuthority(String labAuthority) {
        this.labAuthority = labAuthority;
    }

    public MstUser getMstUser() {
        return mstUser;
    }

    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLabEmpCode() {
        return labEmpCode;
    }

    public void setLabEmpCode(String labEmpCode) {
        this.labEmpCode = labEmpCode;
    }
    
    public MstUser getMstTseUser() {
        return mstTseUser;
    }

    public void setMstTseUser(MstUser mstTseUser) {
        this.mstTseUser = mstTseUser;
    }
}
