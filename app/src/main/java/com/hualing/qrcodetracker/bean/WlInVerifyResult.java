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
    private Integer bzID;
    private Integer bzStatus;
    private String bzName;
    private Integer fzrID;
    private Integer fzrStatus;
    private Integer zjyID;
    private Integer zjyStatus;
    private String zjyName;
    private Integer zjldID;
    private Integer zjldStatus;
    private String zjldName;
    public String getZjldName() {
        return zjldName;
    }

    public void setZjldName(String zjldName) {
        this.zjldName = zjldName;
    }

    public Integer getBzID() {
        return bzID;
    }

    public void setBzID(Integer bzID) {
        this.bzID = bzID;
    }

    public Integer getBzStatus() {
        return bzStatus;
    }

    public void setBzStatus(Integer bzStatus) {
        this.bzStatus = bzStatus;
    }

    public String getBzName() {
        return bzName;
    }

    public void setBzName(String bzName) {
        this.bzName = bzName;
    }

    public Integer getZjldID() {
        return zjldID;
    }

    public void setZjldID(Integer zjldID) {
        this.zjldID = zjldID;
    }

    public Integer getZjldStatus() {
        return zjldStatus;
    }

    public void setZjldStatus(Integer zjldStatus) {
        this.zjldStatus = zjldStatus;
    }

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
