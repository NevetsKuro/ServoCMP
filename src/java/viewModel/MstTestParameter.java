package viewModel;

import java.util.Date;

public class MstTestParameter {

    private String minValue, maxValue, typValue, devValue, otherVal, checkId, strResultDate, samplingNo;
    private Date testresultenteredDate;

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getTypValue() {
        return typValue;
    }

    public void setTypValue(String typValue) {
        this.typValue = typValue;
    }

    public String getDevValue() {
        return devValue;
    }

    public void setDevValue(String devValue) {
        this.devValue = devValue;
    }

    public String getOtherVal() {
        return otherVal;
    }

    public void setOtherVal(String otherVal) {
        this.otherVal = otherVal;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getStrResultDate() {
        return strResultDate;
    }

    public void setStrResultDate(String strResultDate) {
        this.strResultDate = strResultDate;
    }

    public String getSamplingNo() {
        return samplingNo;
    }

    public void setSamplingNo(String samplingNo) {
        this.samplingNo = samplingNo;
    }

    public Date getTestresultenteredDate() {
        return testresultenteredDate;
    }

    public void setTestresultenteredDate(Date testresultenteredDate) {
        this.testresultenteredDate = testresultenteredDate;
    }

}
