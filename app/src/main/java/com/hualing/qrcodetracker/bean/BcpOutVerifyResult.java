package com.hualing.qrcodetracker.bean;

import java.util.List;

public class BcpOutVerifyResult {

    private String outDh;
    private String lhRq;
    private Integer bzID;
    private String bz;
    private Integer bzStatus;
    private Integer llfzrID;
    private String lhFzr;
    private Integer llfzrStatus;
    private Integer kgID;
    private String kg;
    private Integer kgStatus;
    private Integer fzrID;
    private String fhFzr;
    private Integer fzrStatus;
    private String remark;
    private List<BcpOutShowBean> beans;

    public String getOutDh() {
        return outDh;
    }

    public void setOutDh(String outDh) {
        this.outDh = outDh;
    }

    public String getLhRq() {
        return lhRq;
    }

    public void setLhRq(String lhRq) {
        this.lhRq = lhRq;
    }

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

    public Integer getLlfzrID() {
        return llfzrID;
    }

    public void setLlfzrID(Integer llfzrID) {
        this.llfzrID = llfzrID;
    }

    public String getLhFzr() {
        return lhFzr;
    }

    public void setLhFzr(String lhFzr) {
        this.lhFzr = lhFzr;
    }

    public Integer getLlfzrStatus() {
        return llfzrStatus;
    }

    public void setLlfzrStatus(Integer llfzrStatus) {
        this.llfzrStatus = llfzrStatus;
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

    public Integer getFzrID() {
        return fzrID;
    }

    public void setFzrID(Integer fzrID) {
        this.fzrID = fzrID;
    }

    public String getFhFzr() {
        return fhFzr;
    }

    public void setFhFzr(String fhFzr) {
        this.fhFzr = fhFzr;
    }

    public Integer getFzrStatus() {
        return fzrStatus;
    }

    public void setFzrStatus(Integer fzrStatus) {
        this.fzrStatus = fzrStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<BcpOutShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<BcpOutShowBean> beans) {
        this.beans = beans;
    }
}
