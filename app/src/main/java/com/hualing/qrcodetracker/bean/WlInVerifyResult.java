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
    private String shR;
    private String inDh;
    private String fhR;
    private Integer zjyID;
    private String zjy;
    private Integer zjyStatus;
    private Integer zjldID;
    private String zjld;
    private Integer zjldStatus;
    private Integer fzrID;
    private String shFzr;
    private Integer fzrStatus;
    private String remark;
    private List<WLINShowBean> beans;

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

    public String getShR() {
        return shR;
    }

    public void setShR(String shR) {
        this.shR = shR;
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

    public Integer getZjyID() {
        return zjyID;
    }

    public void setZjyID(Integer zjyID) {
        this.zjyID = zjyID;
    }

    public String getZjy() {
        return zjy;
    }

    public void setZjy(String zjy) {
        this.zjy = zjy;
    }

    public Integer getZjyStatus() {
        return zjyStatus;
    }

    public void setZjyStatus(Integer zjyStatus) {
        this.zjyStatus = zjyStatus;
    }

    public Integer getZjldID() {
        return zjldID;
    }

    public void setZjldID(Integer zjldID) {
        this.zjldID = zjldID;
    }

    public String getZjld() {
        return zjld;
    }

    public void setZjld(String zjld) {
        this.zjld = zjld;
    }

    public Integer getZjldStatus() {
        return zjldStatus;
    }

    public void setZjldStatus(Integer zjldStatus) {
        this.zjldStatus = zjldStatus;
    }

    public Integer getFzrID() {
        return fzrID;
    }

    public void setFzrID(Integer fzrID) {
        this.fzrID = fzrID;
    }

    public String getShFzr() {
        return shFzr;
    }

    public void setShFzr(String shFzr) {
        this.shFzr = shFzr;
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

    public List<WLINShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<WLINShowBean> beans) {
        this.beans = beans;
    }
}
