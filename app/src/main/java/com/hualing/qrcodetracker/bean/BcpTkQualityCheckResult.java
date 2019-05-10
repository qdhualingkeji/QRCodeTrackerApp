package com.hualing.qrcodetracker.bean;

import java.util.List;

public class BcpTkQualityCheckResult {

    private String backDh;
    private String thDw;
    private String thRq;
    private String thR;
    private String zjy;
    private String remark;

    private List<BcpTkShowBean> beans;

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

    public String getThR() {
        return thR;
    }

    public void setThR(String thR) {
        this.thR = thR;
    }

    public String getZjy() {
        return zjy;
    }

    public void setZjy(String zjy) {
        this.zjy = zjy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<BcpTkShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<BcpTkShowBean> beans) {
        this.beans = beans;
    }
}
