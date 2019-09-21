package com.hualing.qrcodetracker.bean;

import java.io.Serializable;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class BcpInShowBean implements Serializable {

    //二维码编号
    private String qRCodeID;
    //物料编码
    private String wLCode;
    //货物名称
    private String productName;
    //类别编号
    private int sortID;
    //类别名称
    private String sortName;
    //原料批次
    private String yLPC;
    //生产批次
    private String sCPC;
    //车间
    private String cheJian;
    //工序
    private String gx;
    //操作员
    private String czy	;
    //质检员
    private String zjy	;
    //数量
    private float shl ;
    //单位重量
    private float dWZL;
    //入库重量
    private float rKZL;
    //剩余重量
    private float sYZL;
    //单位
    private String dW ;
    //规格
    private String gG;
    //出库单号
    private String inDh;
    //来料时间（入库时间）
    private String time;
    //检验状态
    private String jyzt;
    //质检状态
    private Integer zjzt;

    public Integer getZjzt() {
        return zjzt;
    }

    public void setZjzt(Integer zjzt) {
        this.zjzt = zjzt;
    }

    public String getJyzt() {
        return jyzt;
    }

    public void setJyzt(String jyzt) {
        this.jyzt = jyzt;
    }

    public String getqRCodeID() {
        return qRCodeID;
    }

    public void setqRCodeID(String qRCodeID) {
        this.qRCodeID = qRCodeID;
    }

    public String getwLCode() {
        return wLCode;
    }

    public void setwLCode(String wLCode) {
        this.wLCode = wLCode;
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

    public String getCheJian() {
        return cheJian;
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

    public String getZjy() {
        return zjy;
    }

    public void setZjy(String zjy) {
        this.zjy = zjy;
    }

    public void setCheJian(String cheJian) {
        this.cheJian = cheJian;
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

    public String getdW() {
        return dW;
    }

    public void setdW(String dW) {
        this.dW = dW;
    }

    public String getgG() {
        return gG;
    }

    public void setgG(String gG) {
        this.gG = gG;
    }

    public String getInDh() {
        return inDh;
    }

    public void setInDh(String inDh) {
        this.inDh = inDh;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
