package com.hualing.qrcodetracker.bean;

public class BcpOutShowBean {

    //主键
    private Integer id;
    //二维码编号
    private String qRCodeID;
    //半成品编码
    private String bcpCode;
    //货物名称
    private String productName;
    //类别编号
    private int sortID;
    //类别名称
    private String sortName;
    //原料批次
    private String yLPC;
    //数量
    private float shl;
    //单位重量
    private float dWZL;
    //入库重量
    private float rKZL;
    //剩余重量
    private float sYZL;
    //出库重量
    private float cKZL;
    //修改前的出库重量
    private float cKZL1;
    //出库单号
    private String outDh;
    //出库时间
    private String time;
    //单位
    private String dw;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getqRCodeID() {
        return qRCodeID;
    }

    public void setqRCodeID(String qRCodeID) {
        this.qRCodeID = qRCodeID;
    }

    public String getBcpCode() {
        return bcpCode;
    }

    public void setBcpCode(String bcpCode) {
        this.bcpCode = bcpCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSortID() {
        return sortID;
    }

    public void setSortID(int sortID) {
        this.sortID = sortID;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getyLPC() {
        return yLPC;
    }

    public void setyLPC(String yLPC) {
        this.yLPC = yLPC;
    }

    public float getShl() {
        return shl;
    }

    public void setShl(float shl) {
        this.shl = shl;
    }

    public float getdWZL() {
        return dWZL;
    }

    public void setdWZL(float dWZL) {
        this.dWZL = dWZL;
    }

    public float getrKZL() {
        return rKZL;
    }

    public void setrKZL(float rKZL) {
        this.rKZL = rKZL;
    }

    public float getsYZL() {
        return sYZL;
    }

    public void setsYZL(float sYZL) {
        this.sYZL = sYZL;
    }

    public float getcKZL() {
        return cKZL;
    }

    public void setcKZL(float cKZL) {
        this.cKZL = cKZL;
    }

    public float getcKZL1() {
        return cKZL1;
    }

    public void setcKZL1(float cKZL1) {
        this.cKZL1 = cKZL1;
    }

    public String getOutDh() {
        return outDh;
    }

    public void setOutDh(String outDh) {
        this.outDh = outDh;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }
}
