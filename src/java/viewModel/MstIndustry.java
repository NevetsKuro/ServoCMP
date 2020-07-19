package viewModel;

public class MstIndustry {

    private String indId, indName, indCount, active, updatedBy, updatedDate;
    private String[] indNames;

    public String getIndId() {
        return indId;
    }

    public String getIndCount() {
        return indCount;
    }

    public void setIndCount(String indCount) {
        this.indCount = indCount;
    }

    public void setIndId(String indId) {
        this.indId = indId;
    }

    public String getIndName() {
        return indName;
    }

    public void setIndName(String indName) {
        this.indName = indName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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

    public String[] getIndNames() {
        return indNames;
    }

    public void setIndNames(String[] indNames) {
        this.indNames = indNames;
    }

}
