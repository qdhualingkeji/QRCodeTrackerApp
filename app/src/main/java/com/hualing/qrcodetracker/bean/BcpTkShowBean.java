package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class BcpTkShowBean {

    //主键
    private Integer id;
    //二维码编号
    private String qRCodeID;
    //物料编码
    private String bcpCode;
    //货物名称
    private String productName;
    //类别编号
    private int sortID;
    //类别名称
    private String sortName;
    //原料批次
    private String yLPC;
    private String sCPC;
    private String scTime;
    private String ksTime;
    private String wcTime;
    //数量
    private float shl ;
    //车间
    private String cheJian;
    //工序
    private String gx;
    //操作员
    private String czy	;
    //单位重量
    private float dWZL;
    //剩余重量
    private float sYZL;
    //退库重量
    private float tKZL;
    //原来的退库重量
    private float tKZL1;
    //批次总量
    private float pCZL;
    //出库单号
    private String backDh;
    private String remark;
    //单位
    private String dW;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getsCPC() {
        return sCPC;
    }

    public void setsCPC(String sCPC) {
        this.sCPC = sCPC;
    }

    public String getScTime() {
        return scTime;
    }

    public void setScTime(String scTime) {
        this.scTime = scTime;
    }

    public String getKsTime() {
        return ksTime;
    }

    public void setKsTime(String ksTime) {
        this.ksTime = ksTime;
    }

    public String getWcTime() {
        return wcTime;
    }

    public void setWcTime(String wcTime) {
        this.wcTime = wcTime;
    }

    public float getShl() {
        return shl;
    }

    public void setShl(float shl) {
        this.shl = shl;
    }

    public String getCheJian() {
        return cheJian;
    }

    public void setCheJian(String cheJian) {
        this.cheJian = cheJian;
    }

    public String getGx() {
        return gx;
    }

    public void setGx(String gx) {
        this.gx = gx;
    }

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public float getdWZL() {
        return dWZL;
    }

    public void setdWZL(float dWZL) {
        this.dWZL = dWZL;
    }

    public float getsYZL() {
        return sYZL;
    }

    public void setsYZL(float sYZL) {
        this.sYZL = sYZL;
    }

    public float gettKZL() {
        return tKZL;
    }

    public void settKZL(float tKZL) {
        this.tKZL = tKZL;
    }

    public float gettKZL1() {
        return tKZL1;
    }

    public void settKZL1(float tKZL1) {
        this.tKZL1 = tKZL1;
    }

    public float getpCZL() {
        return pCZL;
    }

    public void setpCZL(float pCZL) {
        this.pCZL = pCZL;
    }

    public String getBackDh() {
        return backDh;
    }

    public void setBackDh(String backDh) {
        this.backDh = backDh;
    }

    public String getdW() {
        return dW;
    }

    public void setdW(String dW) {
        this.dW = dW;
    }
}
