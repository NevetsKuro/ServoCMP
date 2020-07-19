package viewModel;

public class MstDepartment {

    private String departmentId, departmentName, hodName, hodEmail, hodContact, updatedBy, updatedDatetime;
    private MstCustomer mstCustomer;
    private String[] departmentNames, hodNames, hodEmails, hodContacts;

    public MstDepartment() {
        mstCustomer = new MstCustomer();
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getHodName() {
        return hodName;
    }

    public void setHodName(String hodName) {
        this.hodName = hodName;
    }

    public String getHodEmail() {
        return hodEmail;
    }

    public void setHodEmail(String hodEmail) {
        this.hodEmail = hodEmail;
    }

    public String getHodContact() {
        return hodContact;
    }

    public void setHodContact(String hodContact) {
        this.hodContact = hodContact;
    }

    public MstCustomer getMstCustomer() {
        return mstCustomer;
    }

    public void setMstCustomer(MstCustomer mstCustomer) {
        this.mstCustomer = mstCustomer;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDatetime() {
        return updatedDatetime;
    }

    public void setUpdatedDatetime(String updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }

    public String[] getDepartmentNames() {
        return departmentNames;
    }

    public void setDepartmentNames(String[] departmentNames) {
        this.departmentNames = departmentNames;
    }

    public String[] getHodNames() {
        return hodNames;
    }

    public void setHodNames(String[] hodNames) {
        this.hodNames = hodNames;
    }

    public String[] getHodEmails() {
        return hodEmails;
    }

    public void setHodEmails(String[] hodEmails) {
        this.hodEmails = hodEmails;
    }

    public String[] getHodContacts() {
        return hodContacts;
    }

    public void setHodContacts(String[] hodContacts) {
        this.hodContacts = hodContacts;
    }

}
