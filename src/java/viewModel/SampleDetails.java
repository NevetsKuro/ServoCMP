package viewModel;

import java.util.Date;

public class SampleDetails {

    private String[] testIds,csl_testIds,rnd_testIds;
    private String sampleId, tankId, samplingNo, tankNo, statusId, statusName, qtyDrawn, sampledrawnRemarks, samplepriorityId,
            samplepriorityName, samplepriorityRemarks, topupQty, resultdateextendReason, labrecRemarks, labFinalTestRemarks, runningHrs,
            tseFinalTestRemarks, samplecreatedBy, testresultenteredBy, sampleFreq, stringpresampleDate, stringnxtsampleDate,
            stringsamplecreatedDate, stringsampledrawnDate, postponeReason, postponetillDate, stringtestresultenteredDate,
            postponeendDate, sdStartDate, sdEndDate, fileuploadStatus, postponedFlag, postponedCount, stringoldnxtsampleDate,
            lastoilChanged, defaultSelectedDate, stringLabrecDate, stringExptestresultDate, stringTseFinalizeRptTstDate, testParamNames, descAppl,isSingleSampling,
            csl_labCode, rnd_labCode,csl_labAuthority,rnd_labAuthority,sampling_no_cmp,sampling_no_ots,csl_qtyDrawn,rnd_qtyDrawn;


    private Date qtydrawnDate, presampleDate, nxtsampleDate, oldnxtsampleDate, samplecreatedDate, sampledrawnDate, exptestresultDate, labrecDate,
            testresultenteredDate, tseFinalizeRptTstDate;

    private MstIndustry mstInd;
    private MstDepartment mstDept;
    private MstApplication mstApp;
    private MstEquipment mstEquip;
    private MstProduct mstProd;

    private MstLab mstLab;

    public SampleDetails() {
        mstInd = new MstIndustry();
        mstDept = new MstDepartment();
        mstApp = new MstApplication();
        mstEquip = new MstEquipment();
        mstProd = new MstProduct();
        mstLab = new MstLab();
    }
    
    public String getCsl_qtyDrawn() {
        return csl_qtyDrawn;
    }

    public void setCsl_qtyDrawn(String csl_qtyDrawn) {
        this.csl_qtyDrawn = csl_qtyDrawn;
    }

    public String getRnd_qtyDrawn() {
        return rnd_qtyDrawn;
    }

    public void setRnd_qtyDrawn(String rnd_qtyDrawn) {
        this.rnd_qtyDrawn = rnd_qtyDrawn;
    }
    
    public String getSampling_no_cmp() {
        return sampling_no_cmp;
    }

    public void setSampling_no_cmp(String sampling_no_cmp) {
        this.sampling_no_cmp = sampling_no_cmp;
    }

    public String getSampling_no_ots() {
        return sampling_no_ots;
    }

    public void setSampling_no_ots(String sampling_no_ots) {
        this.sampling_no_ots = sampling_no_ots;
    }
    
    public String getCsl_labAuthority() {
        return csl_labAuthority;
    }

    public void setCsl_labAuthority(String csl_labAuthority) {
        this.csl_labAuthority = csl_labAuthority;
    }

    public String getRnd_labAuthority() {
        return rnd_labAuthority;
    }

    public void setRnd_labAuthority(String rnd_labAuthority) {
        this.rnd_labAuthority = rnd_labAuthority;
    }
    
    public String[] getCsl_testIds() {
        return csl_testIds;
    }

    public void setCsl_testIds(String[] csl_testIds) {
        this.csl_testIds = csl_testIds;
    }

    public String[] getRnd_testIds() {
        return rnd_testIds;
    }

    public void setRnd_testIds(String[] rnd_testIds) {
        this.rnd_testIds = rnd_testIds;
    }

    public String getCsl_labCode() {
        return csl_labCode;
    }

    public void setCsl_labCode(String csl_labCode) {
        this.csl_labCode = csl_labCode;
    }

    public String getRnd_labCode() {
        return rnd_labCode;
    }

    public void setRnd_labCode(String rnd_labCode) {
        this.rnd_labCode = rnd_labCode;
    }


    public String getIsSingleSampling() {
        return isSingleSampling;
    }

    public void setIsSingleSampling(String isSingleSampling) {
        this.isSingleSampling = isSingleSampling;
    }
    
    public String[] getTestIds() {
        return testIds;
    }

    public void setTestIds(String[] testIds) {
        this.testIds = testIds;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getTankId() {
        return tankId;
    }

    public void setTankId(String tankId) {
        this.tankId = tankId;
    }

    public String getSamplingNo() {
        return samplingNo;
    }

    public void setSamplingNo(String samplingNo) {
        this.samplingNo = samplingNo;
    }

    public String getTankNo() {
        return tankNo;
    }

    public void setTankNo(String tankNo) {
        this.tankNo = tankNo;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getQtyDrawn() {
        return qtyDrawn;
    }

    public void setQtyDrawn(String qtyDrawn) {
        this.qtyDrawn = qtyDrawn;
    }
    
    public String getSampledrawnRemarks() {
        return sampledrawnRemarks;
    }

    public void setSampledrawnRemarks(String sampledrawnRemarks) {
        this.sampledrawnRemarks = sampledrawnRemarks;
    }

    public String getSamplepriorityId() {
        return samplepriorityId;
    }

    public void setSamplepriorityId(String samplepriorityId) {
        this.samplepriorityId = samplepriorityId;
    }

    public String getSamplepriorityName() {
        return samplepriorityName;
    }

    public void setSamplepriorityName(String samplepriorityName) {
        this.samplepriorityName = samplepriorityName;
    }

    public String getSamplepriorityRemarks() {
        return samplepriorityRemarks;
    }

    public void setSamplepriorityRemarks(String samplepriorityRemarks) {
        this.samplepriorityRemarks = samplepriorityRemarks;
    }

    public String getTopupQty() {
        return topupQty;
    }

    public void setTopupQty(String topupQty) {
        this.topupQty = topupQty;
    }

    public String getResultdateextendReason() {
        return resultdateextendReason;
    }

    public void setResultdateextendReason(String resultdateextendReason) {
        this.resultdateextendReason = resultdateextendReason;
    }

    public String getLabrecRemarks() {
        return labrecRemarks;
    }

    public void setLabrecRemarks(String labrecRemarks) {
        this.labrecRemarks = labrecRemarks;
    }

    public String getLabFinalTestRemarks() {
        return labFinalTestRemarks;
    }

    public void setLabFinalTestRemarks(String labFinalTestRemarks) {
        this.labFinalTestRemarks = labFinalTestRemarks;
    }

    public String getRunningHrs() {
        return runningHrs;
    }

    public void setRunningHrs(String runningHrs) {
        this.runningHrs = runningHrs;
    }

    public String getTseFinalTestRemarks() {
        return tseFinalTestRemarks;
    }

    public void setTseFinalTestRemarks(String tseFinalTestRemarks) {
        this.tseFinalTestRemarks = tseFinalTestRemarks;
    }

    public String getSamplecreatedBy() {
        return samplecreatedBy;
    }

    public void setSamplecreatedBy(String samplecreatedBy) {
        this.samplecreatedBy = samplecreatedBy;
    }

    public String getTestresultenteredBy() {
        return testresultenteredBy;
    }

    public void setTestresultenteredBy(String testresultenteredBy) {
        this.testresultenteredBy = testresultenteredBy;
    }

    public String getSampleFreq() {
        return sampleFreq;
    }

    public void setSampleFreq(String sampleFreq) {
        this.sampleFreq = sampleFreq;
    }

    public String getStringpresampleDate() {
        return stringpresampleDate;
    }

    public void setStringpresampleDate(String stringpresampleDate) {
        this.stringpresampleDate = stringpresampleDate;
    }

    public String getStringnxtsampleDate() {
        return stringnxtsampleDate;
    }

    public void setStringnxtsampleDate(String stringnxtsampleDate) {
        this.stringnxtsampleDate = stringnxtsampleDate;
    }

    public String getStringsamplecreatedDate() {
        return stringsamplecreatedDate;
    }

    public void setStringsamplecreatedDate(String stringsamplecreatedDate) {
        this.stringsamplecreatedDate = stringsamplecreatedDate;
    }

    public String getStringsampledrawnDate() {
        return stringsampledrawnDate;
    }

    public void setStringsampledrawnDate(String stringsampledrawnDate) {
        this.stringsampledrawnDate = stringsampledrawnDate;
    }

    public String getPostponeReason() {
        return postponeReason;
    }

    public void setPostponeReason(String postponeReason) {
        this.postponeReason = postponeReason;
    }

    public String getPostponetillDate() {
        return postponetillDate;
    }

    public void setPostponetillDate(String postponetillDate) {
        this.postponetillDate = postponetillDate;
    }

    public String getStringtestresultenteredDate() {
        return stringtestresultenteredDate;
    }

    public void setStringtestresultenteredDate(String stringtestresultenteredDate) {
        this.stringtestresultenteredDate = stringtestresultenteredDate;
    }

    public String getPostponeendDate() {
        return postponeendDate;
    }

    public void setPostponeendDate(String postponeendDate) {
        this.postponeendDate = postponeendDate;
    }

    public String getSdStartDate() {
        return sdStartDate;
    }

    public void setSdStartDate(String sdStartDate) {
        this.sdStartDate = sdStartDate;
    }

    public String getSdEndDate() {
        return sdEndDate;
    }

    public void setSdEndDate(String sdEndDate) {
        this.sdEndDate = sdEndDate;
    }

    public String getFileuploadStatus() {
        return fileuploadStatus;
    }

    public void setFileuploadStatus(String fileuploadStatus) {
        this.fileuploadStatus = fileuploadStatus;
    }

    public String getPostponedFlag() {
        return postponedFlag;
    }

    public void setPostponedFlag(String postponedFlag) {
        this.postponedFlag = postponedFlag;
    }

    public String getPostponedCount() {
        return postponedCount;
    }

    public void setPostponedCount(String postponedCount) {
        this.postponedCount = postponedCount;
    }

    public String getStringoldnxtsampleDate() {
        return stringoldnxtsampleDate;
    }

    public void setStringoldnxtsampleDate(String stringoldnxtsampleDate) {
        this.stringoldnxtsampleDate = stringoldnxtsampleDate;
    }

    public String getLastoilChanged() {
        return lastoilChanged;
    }

    public void setLastoilChanged(String lastoilChanged) {
        this.lastoilChanged = lastoilChanged;
    }

    public String getDefaultSelectedDate() {
        return defaultSelectedDate;
    }

    public void setDefaultSelectedDate(String defaultSelectedDate) {
        this.defaultSelectedDate = defaultSelectedDate;
    }

    public String getStringLabrecDate() {
        return stringLabrecDate;
    }

    public void setStringLabrecDate(String stringLabrecDate) {
        this.stringLabrecDate = stringLabrecDate;
    }

    public String getStringExptestresultDate() {
        return stringExptestresultDate;
    }

    public void setStringExptestresultDate(String stringExptestresultDate) {
        this.stringExptestresultDate = stringExptestresultDate;
    }

    public String getStringTseFinalizeRptTstDate() {
        return stringTseFinalizeRptTstDate;
    }

    public void setStringTseFinalizeRptTstDate(String stringTseFinalizeRptTstDate) {
        this.stringTseFinalizeRptTstDate = stringTseFinalizeRptTstDate;
    }

    public String getTestParamNames() {
        return testParamNames;
    }

    public void setTestParamNames(String testParamNames) {
        this.testParamNames = testParamNames;
    }

    public String getDescAppl() {
        return descAppl;
    }

    public void setDescAppl(String descAppl) {
        this.descAppl = descAppl;
    }

    public Date getQtydrawnDate() {
        return qtydrawnDate;
    }

    public void setQtydrawnDate(Date qtydrawnDate) {
        this.qtydrawnDate = qtydrawnDate;
    }

    public Date getPresampleDate() {
        return presampleDate;
    }

    public void setPresampleDate(Date presampleDate) {
        this.presampleDate = presampleDate;
    }

    public Date getNxtsampleDate() {
        return nxtsampleDate;
    }

    public void setNxtsampleDate(Date nxtsampleDate) {
        this.nxtsampleDate = nxtsampleDate;
    }

    public Date getOldnxtsampleDate() {
        return oldnxtsampleDate;
    }

    public void setOldnxtsampleDate(Date oldnxtsampleDate) {
        this.oldnxtsampleDate = oldnxtsampleDate;
    }

    public Date getSamplecreatedDate() {
        return samplecreatedDate;
    }

    public void setSamplecreatedDate(Date samplecreatedDate) {
        this.samplecreatedDate = samplecreatedDate;
    }

    public Date getSampledrawnDate() {
        return sampledrawnDate;
    }

    public void setSampledrawnDate(Date sampledrawnDate) {
        this.sampledrawnDate = sampledrawnDate;
    }

    public Date getExptestresultDate() {
        return exptestresultDate;
    }

    public void setExptestresultDate(Date exptestresultDate) {
        this.exptestresultDate = exptestresultDate;
    }

    public Date getLabrecDate() {
        return labrecDate;
    }

    public void setLabrecDate(Date labrecDate) {
        this.labrecDate = labrecDate;
    }

    public Date getTestresultenteredDate() {
        return testresultenteredDate;
    }

    public void setTestresultenteredDate(Date testresultenteredDate) {
        this.testresultenteredDate = testresultenteredDate;
    }

    public Date getTseFinalizeRptTstDate() {
        return tseFinalizeRptTstDate;
    }

    public void setTseFinalizeRptTstDate(Date tseFinalizeRptTstDate) {
        this.tseFinalizeRptTstDate = tseFinalizeRptTstDate;
    }

    public MstIndustry getMstInd() {
        return mstInd;
    }

    public void setMstInd(MstIndustry mstInd) {
        this.mstInd = mstInd;
    }

    public MstDepartment getMstDept() {
        return mstDept;
    }

    public void setMstDept(MstDepartment mstDept) {
        this.mstDept = mstDept;
    }

    public MstApplication getMstApp() {
        return mstApp;
    }

    public void setMstApp(MstApplication mstApp) {
        this.mstApp = mstApp;
    }

    public MstEquipment getMstEquip() {
        return mstEquip;
    }

    public void setMstEquip(MstEquipment mstEquip) {
        this.mstEquip = mstEquip;
    }

    public MstProduct getMstProd() {
        return mstProd;
    }

    public void setMstProd(MstProduct mstProd) {
        this.mstProd = mstProd;
    }

    public MstLab getMstLab() {
        return mstLab;
    }

    public void setMstLab(MstLab mstLab) {
        this.mstLab = mstLab;
    }

}
