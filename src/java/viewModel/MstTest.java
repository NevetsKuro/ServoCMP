package viewModel;

public class MstTest {

    private String testId, testName, unit, testMethod, sampleqty, testVal, testRemarks, prevToprevVal, prevVal, curVal, spec, valWithinRange, selected, proId, proName, updatedBy, updatedDate, active;
    private int dispSeqNo;
    private MstTestParameter mstTestParam;

    public MstTest() {
        this.dispSeqNo = 0;
        this.valWithinRange = "-1";
        mstTestParam = new MstTestParameter();
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public String getSampleqty() {
        return sampleqty;
    }

    public void setSampleqty(String sampleqty) {
        this.sampleqty = sampleqty;
    }

    public String getTestVal() {
        return testVal;
    }

    public void setTestVal(String testVal) {
        this.testVal = testVal;
    }

    public String getTestRemarks() {
        return testRemarks;
    }

    public void setTestRemarks(String testRemarks) {
        this.testRemarks = testRemarks;
    }

    public String getPrevToprevVal() {
        return prevToprevVal;
    }

    public void setPrevToprevVal(String prevToprevVal) {
        this.prevToprevVal = prevToprevVal;
    }

    public String getPrevVal() {
        return prevVal;
    }

    public void setPrevVal(String prevVal) {
        this.prevVal = prevVal;
    }

    public String getCurVal() {
        return curVal;
    }

    public void setCurVal(String curVal) {
        this.curVal = curVal;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getValWithinRange() {
        return valWithinRange;
    }

    public void setValWithinRange(String valWithinRange) {
        this.valWithinRange = valWithinRange;
    }

    public int getDispSeqNo() {
        return dispSeqNo;
    }

    public void setDispSeqNo(int dispSeqNo) {
        this.dispSeqNo = dispSeqNo;
    }

    public MstTestParameter getMstTestParam() {
        return mstTestParam;
    }

    public void setMstTestParam(MstTestParameter mstTestParam) {
        this.mstTestParam = mstTestParam;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
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
}
