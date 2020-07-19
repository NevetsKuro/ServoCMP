package viewModel;

public class TestResultDetails {

    private String[] testIds, values, remarks;
    private String conclusion;

    public String[] getTestIds() {
        return testIds;
    }

    public void setTestIds(String[] testIds) {
        this.testIds = testIds;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getRemarks() {
        return remarks;
    }

    public void setRemarks(String[] remarks) {
        this.remarks = remarks;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

}
