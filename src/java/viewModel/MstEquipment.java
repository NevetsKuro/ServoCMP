package viewModel;

public class MstEquipment {

    private String equipmentId, equipmentName, updatedBy, updatedDatetime, custId, active;
    private MstMake mstmake;
    private String[] equipmentNames, equipmentMakes, otherRemarks;

    public String[] getOtherRemarks() {
        return otherRemarks;
    }

    public void setOtherRemarks(String[] otherRemarks) {
        this.otherRemarks = otherRemarks;
    }

    public MstEquipment() {
        mstmake = new MstMake();
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public MstMake getMstmake() {
        return mstmake;
    }

    public void setMstmake(MstMake mstmake) {
        this.mstmake = mstmake;
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

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String[] getEquipmentNames() {
        return equipmentNames;
    }

    public void setEquipmentNames(String[] equipmentNames) {
        this.equipmentNames = equipmentNames;
    }

    public String[] getEquipmentMakes() {
        return equipmentMakes;
    }

    public void setEquipmentMakes(String[] equipmentMakes) {
        this.equipmentMakes = equipmentMakes;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
