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

public class WlInVerifyResult {

    private String fhDw;
    private String shRq;
    private String inDh;
    private String fhR;
    private String shFzr;
    private String jhFzr;
    private String remark;
    private Integer fzrID;
    private Integer fzrStatus;
    private Integer zjyID;
    private Integer zjyStatus;
    private String zjyName;

    public String getFhDw() {
        return fhDw;
    }

    public void setFhDw(String fhDw) {
        this.fhDw = fhDw;
    }

    public String getShRq() {
        return shRq;
    }

    public void setShRq(String shRq) {
        this.shRq = shRq;
    }

    public String getInDh() {
        return inDh;
    }

    public void setInDh(String inDh) {
        this.inDh = inDh;
    }

    public String getFhR() {
        return fhR;
    }

    public void setFhR(String fhR) {
        this.fhR = fhR;
    }

    public String getShFzr() {
        return shFzr;
    }

    public void setShFzr(String shFzr) {
        this.shFzr = shFzr;
    }

    public String getJhFzr() {
        return jhFzr;
    }

    public void setJhFzr(String jhFzr) {
        this.jhFzr = jhFzr;
    }

    private List<WLINShowBean> beans;

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

    public Integer getZjyID() {
        return zjyID;
    }

    public void setZjyID(Integer zjyID) {
        this.zjyID = zjyID;
    }

    public Integer getZjyStatus() {
        return zjyStatus;
    }

    public void setZjyStatus(Integer zjyStatus) {
        this.zjyStatus = zjyStatus;
    }

    public String getZjyName() {
        return zjyName;
    }

    public void setZjyName(String zjyName) {
        this.zjyName = zjyName;
    }

    public List<WLINShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<WLINShowBean> beans) {
        this.beans = beans;
    }
}
