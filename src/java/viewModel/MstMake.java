package viewModel;

public class MstMake {

    private String makeId, makeName, updatedBy, updateddateTime, Active;
    private String[] makeNames;

    public String getMakeId() {
        return makeId;
    }

    public void setMakeId(String makeId) {
        this.makeId = makeId;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdateddateTime() {
        return updateddateTime;
    }

    public void setUpdateddateTime(String updateddateTime) {
        this.updateddateTime = updateddateTime;
    }

    public String[] getMakeNames() {
        return makeNames;
    }

    public void setMakeNames(String[] makeNames) {
        this.makeNames = makeNames;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String Active) {
        this.Active = Active;
    }
}
