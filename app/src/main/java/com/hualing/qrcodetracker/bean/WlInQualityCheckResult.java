package com.hualing.qrcodetracker.bean;

import java.util.List;

public class WlInQualityCheckResult {

    private String fhDw;
    private String shRq;
    private String inDh;
    private String fhR;
    private String zjy;
    private String jhFzr;
    private String remark;

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

    public String getZjy() {
        return zjy;
    }

    public void setZjy(String zjy) {
        this.zjy = zjy;
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

    public List<WLINShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<WLINShowBean> beans) {
        this.beans = beans;
    }
}
