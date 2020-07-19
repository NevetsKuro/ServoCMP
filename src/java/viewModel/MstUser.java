package viewModel;

public class MstUser {

        private String empCode, empName, empEmail, ctrlEmpCode, ctrlEmpName, ctrlEmpEmail, updatedBy, updatedDate, active;
    private MstRole mstRole;

    public MstUser() {
    }

    public MstUser(MstRole mstRole) {
        mstRole = new MstRole();
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getCtrlEmpCode() {
        return ctrlEmpCode;
    }

    public void setCtrlEmpCode(String ctrlEmpCode) {
        this.ctrlEmpCode = ctrlEmpCode;
    }

    public String getCtrlEmpName() {
        return ctrlEmpName;
    }

    public void setCtrlEmpName(String ctrlEmpName) {
        this.ctrlEmpName = ctrlEmpName;
    }

    public String getCtrlEmpEmail() {
        return ctrlEmpEmail;
    }

    public void setCtrlEmpEmail(String ctrlEmpEmail) {
        this.ctrlEmpEmail = ctrlEmpEmail;
    }

    public MstRole getMstRole() {
        return mstRole;
    }

    public void setMstRole(MstRole mstRole) {
        this.mstRole = mstRole;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
