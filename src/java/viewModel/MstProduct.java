package viewModel;

public class MstProduct {

    private String proId, proName, updatedBy, updatedDate, active, proCapacity, prodCat;

    public String getProdCat() {
        return prodCat;
    }

    public void setProdCat(String prodCat) {
        this.prodCat = prodCat;
    }
    
    private String[] proNames;

    public MstProduct() {
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
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

    public String[] getProNames() {
        return proNames;
    }

    public void setProNames(String[] proNames) {
        this.proNames = proNames;
    }

    public String getProCapacity() {
        return proCapacity;
    }

    public void setProCapacity(String proCapacity) {
        this.proCapacity = proCapacity;
    }

}