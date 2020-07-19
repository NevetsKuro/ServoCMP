package viewModel;

import java.util.Date;

public class MstTank {

    private String tankId, indId, indName, custId, custName, sapcodes, deptId, deptName, applId, applName, proId, proName, tankNo, applDesc,
            capacity, samplingNo, sampleFreq, strPrevDate, strNxtDate, strOldNxtDate, postponeFlag, postponeCount, lastOilChange, updatedBy,
            updatedDateTime, active, equipId, equipName, tankDesc, createdDate, hodName, hodEmail, hodContact,OneTimeCheckbox, productGrade,productName;

    private Date prevSampleDate, nxtSampleDate, oldSampleDate;
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    private MstEquipment mstEquipment;
    
    public String getProductGrade() {
        return productGrade;
    }

    public void setProductGrade(String productGrade) {
        this.productGrade = productGrade;
    }
    
    public String getSapcodes() {
        return sapcodes;
    }

    public void setSapcodes(String sapcodes) {
        this.sapcodes = sapcodes;
    }
    
    public String getOneTimeCheckbox() {
        return OneTimeCheckbox;
    }

    public void setOneTimeCheckbox(String OneTimeCheckbox) {
        this.OneTimeCheckbox = OneTimeCheckbox;
    }

    public MstTank() {
        mstEquipment = new MstEquipment();
    }

    public String getTankId() {
        return tankId;
    }

    public void setTankId(String tankId) {
        this.tankId = tankId;
    }

    public String getIndId() {
        return indId;
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

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getApplId() {
        return applId;
    }

    public void setApplId(String applId) {
        this.applId = applId;
    }

    public String getApplName() {
        return applName;
    }

    public void setApplName(String applName) {
        this.applName = applName;
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

    public String getTankNo() {
        return tankNo;
    }

    public void setTankNo(String tankNo) {
        this.tankNo = tankNo;
    }

    public String getApplDesc() {
        return applDesc;
    }

    public void setApplDesc(String applDesc) {
        this.applDesc = applDesc;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getSamplingNo() {
        return samplingNo;
    }

    public void setSamplingNo(String samplingNo) {
        this.samplingNo = samplingNo;
    }

    public String getSampleFreq() {
        return sampleFreq;
    }

    public void setSampleFreq(String sampleFreq) {
        this.sampleFreq = sampleFreq;
    }

    public String getStrPrevDate() {
        return strPrevDate;
    }

    public void setStrPrevDate(String strPrevDate) {
        this.strPrevDate = strPrevDate;
    }

    public String getStrNxtDate() {
        return strNxtDate;
    }

    public void setStrNxtDate(String strNxtDate) {
        this.strNxtDate = strNxtDate;
    }

    public String getStrOldNxtDate() {
        return strOldNxtDate;
    }

    public void setStrOldNxtDate(String strOldNxtDate) {
        this.strOldNxtDate = strOldNxtDate;
    }

    public String getPostponeFlag() {
        return postponeFlag;
    }

    public void setPostponeFlag(String postponeFlag) {
        this.postponeFlag = postponeFlag;
    }

    public String getPostponeCount() {
        return postponeCount;
    }

    public void setPostponeCount(String postponeCount) {
        this.postponeCount = postponeCount;
    }

    public String getLastOilChange() {
        return lastOilChange;
    }

    public void setLastOilChange(String lastOilChange) {
        this.lastOilChange = lastOilChange;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(String updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Date getPrevSampleDate() {
        return prevSampleDate;
    }

    public void setPrevSampleDate(Date prevSampleDate) {
        this.prevSampleDate = prevSampleDate;
    }

    public Date getNxtSampleDate() {
        return nxtSampleDate;
    }

    public void setNxtSampleDate(Date nxtSampleDate) {
        this.nxtSampleDate = nxtSampleDate;
    }

    public MstEquipment getMstEquipment() {
        return mstEquipment;
    }

    public void setMstEquipment(MstEquipment mstEquipment) {
        this.mstEquipment = mstEquipment;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public String getTankDesc() {
        return tankDesc;
    }

    public void setTankDesc(String tankDesc) {
        this.tankDesc = tankDesc;
    }

    public Date getOldSampleDate() {
        return oldSampleDate;
    }

    public void setOldSampleDate(Date oldSampleDate) {
        this.oldSampleDate = oldSampleDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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
}
