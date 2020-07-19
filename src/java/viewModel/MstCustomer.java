package viewModel;

public class MstCustomer {

    private String customerId, customerName, updatedBy, updatedDate, active;
    private MstIndustry mstIndustry;
    private MstUser mstUser;
    private String[] customerNames;

    public MstCustomer() {
    }

    public MstCustomer(MstIndustry mstIndustry, MstUser mstUser) {
        mstIndustry = new MstIndustry();
        mstUser = new MstUser();
    }
    
    public MstCustomer(MstIndustry mstIndustry) {
        mstIndustry = new MstIndustry();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public MstIndustry getMstIndustry() {
        return mstIndustry;
    }

    public void setMstIndustry(MstIndustry mstIndustry) {
        this.mstIndustry = mstIndustry;
    }

    public MstUser getMstUser() {
        return mstUser;
    }

    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }

    public String[] getCustomerNames() {
        return customerNames;
    }

    public void setCustomerNames(String[] customerNames) {
        this.customerNames = customerNames;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
