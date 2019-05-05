package com.hualing.qrcodetracker.bean;

import java.util.List;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class WlTkVerifyResult {

    private String backDh;
    private String thDw;
    private String thRq;
    private String shR;
    private String thR;
    private Integer bzID;
    private String bz;
    private Integer bzStatus;
    private Integer tlfzrID;
    private String thFzr ;
    private Integer tlfzrStatus;
    private Integer kgID;
    private String kg ;
    private Integer kgStatus;
    private Integer slfzrID;
    private String shFzr ;
    private Integer slfzrStatus;
    private Integer fzrID;
    private Integer fzrStatus;
    private String remark;

    public Integer getBzID() {
        return bzID;
    }

    public void setBzID(Integer bzID) {
        this.bzID = bzID;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public Integer getBzStatus() {
        return bzStatus;
    }

    public void setBzStatus(Integer bzStatus) {
        this.bzStatus = bzStatus;
    }

    public Integer getTlfzrID() {
        return tlfzrID;
    }

    public void setTlfzrID(Integer tlfzrID) {
        this.tlfzrID = tlfzrID;
    }

    public String getThFzr() {
        return thFzr;
    }

    public void setThFzr(String thFzr) {
        this.thFzr = thFzr;
    }

    public Integer getTlfzrStatus() {
        return tlfzrStatus;
    }

    public void setTlfzrStatus(Integer tlfzrStatus) {
        this.tlfzrStatus = tlfzrStatus;
    }

    public Integer getKgID() {
        return kgID;
    }

    public void setKgID(Integer kgID) {
        this.kgID = kgID;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public Integer getKgStatus() {
        return kgStatus;
    }

    public void setKgStatus(Integer kgStatus) {
        this.kgStatus = kgStatus;
    }

    public Integer getSlfzrID() {
        return slfzrID;
    }

    public void setSlfzrID(Integer slfzrID) {
        this.slfzrID = slfzrID;
    }

    public Integer getSlfzrStatus() {
        return slfzrStatus;
    }

    public void setSlfzrStatus(Integer slfzrStatus) {
        this.slfzrStatus = slfzrStatus;
    }

    private List<WLTkShowBean> beans;

    public String getBackDh() {
        return backDh;
    }

    public void setBackDh(String backDh) {
        this.backDh = backDh;
    }

    public String getThDw() {
        return thDw;
    }

    public void setThDw(String thDw) {
        this.thDw = thDw;
    }

    public String getThRq() {
        return thRq;
    }

    public void setThRq(String thRq) {
        this.thRq = thRq;
    }

    public String getShR() {
        return shR;
    }

    public void setShR(String shR) {
        this.shR = shR;
    }

    public String getThR() {
        return thR;
    }

    public void setThR(String thR) {
        this.thR = thR;
    }

    public String getShFzr() {
        return shFzr;
    }

    public void setShFzr(String shFzr) {
        this.shFzr = shFzr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getFzrID() {
        return fzrID;
    }

    public void setFzrID(Integer fzrID) {
        this.fzrID = fzrID;
    }

    public Integer getFzrStatus() {
        return fzrStatus;
    }

    public void setFzrStatus(Integer fzrStatus) {
        this.fzrStatus = fzrStatus;
    }

    public List<WLTkShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<WLTkShowBean> beans) {
        this.beans = beans;
    }
}
