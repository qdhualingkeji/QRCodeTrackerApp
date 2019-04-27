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

public class WlOutVerifyResult {

    private String outDh;
    private String lhDw;
    private String lhRq;
    private String lhR;
    private String remark;
    private Integer kgID;
    private String kg;
    private Integer kgStatus;
    private Integer flfzrID;
    private String fhFzr;
    private Integer flfzrStatus;
    private Integer bzID;
    private String bz;
    private Integer bzStatus;
    private Integer llfzrID;
    private String lhFzr;
    private Integer llfzrStatus;

    private List<WLOutShowBean> beans;

    public String getOutDh() {
        return outDh;
    }

    public void setOutDh(String outDh) {
        this.outDh = outDh;
    }

    public String getLhDw() {
        return lhDw;
    }

    public void setLhDw(String lhDw) {
        this.lhDw = lhDw;
    }

    public String getLhRq() {
        return lhRq;
    }

    public void setLhRq(String lhRq) {
        this.lhRq = lhRq;
    }

    public String getLhR() {
        return lhR;
    }

    public void setLhR(String lhR) {
        this.lhR = lhR;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getFlfzrID() {
        return flfzrID;
    }

    public void setFlfzrID(Integer flfzrID) {
        this.flfzrID = flfzrID;
    }

    public String getFhFzr() {
        return fhFzr;
    }

    public void setFhFzr(String fhFzr) {
        this.fhFzr = fhFzr;
    }

    public Integer getFlfzrStatus() {
        return flfzrStatus;
    }

    public void setFlfzrStatus(Integer flfzrStatus) {
        this.flfzrStatus = flfzrStatus;
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

    public List<WLOutShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<WLOutShowBean> beans) {
        this.beans = beans;
    }
}
