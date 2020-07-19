/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package viewModel;

import java.util.Date;

/**
 *
 * @author 00507469
 */
public class MstIom {
    
    private String iomRefNo,createdBy,strIomCreatedDate;
    private MstLab mstLab;
    private Date iomCreatedDate;

     public MstIom() {
        mstLab = new MstLab();
    }
     
    public MstLab getMstLab() {
        return mstLab;
    }

    public void setMstLab(MstLab mstLab) {
        this.mstLab = mstLab;
    }
    
    public String getIomRefNo() {
        return iomRefNo;
    }

    public void setIomRefNo(String iomRefNo) {
        this.iomRefNo = iomRefNo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

   public String getStrIomCreatedDate() {
        return strIomCreatedDate;
    }

    public void setStrIomCreatedDate(String strIomCreatedDate) {
        this.strIomCreatedDate = strIomCreatedDate;
    }

    public Date getIomCreatedDate() {
        return iomCreatedDate;
    }

    public void setIomCreatedDate(Date iomCreatedDate) {
        this.iomCreatedDate = iomCreatedDate;
    }

}
