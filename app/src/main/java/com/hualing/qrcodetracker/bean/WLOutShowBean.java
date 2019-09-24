package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class WLOutShowBean {

    //主键
    private Integer id;
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
    //批次总量
    private float pCZL;
    //数量
    private float shl ;
    //单位重量
    private float dWZL;
    //剩余重量
    private float sYZL;
    //出库重量
    private float cKZL;
    //修改前的出库重量
    private float cKZL1;
    //单位
    private String dW ;
    //规格
    private String gG;
    //出库单号
    private String outDh;
    //来料时间（入库时间）
    private String time;
    //标志位
    private int bz;
    //产地
    private String cHD;
    //操作员（入库人）
    private String cZY;

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

    public float getpCZL() {
        return pCZL;
    }

    public void setpCZL(float pCZL) {
        this.pCZL = pCZL;
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

    public int getBz() {
        return bz;
    }

    public void setBz(int bz) {
        this.bz = bz;
    }

    public String getcHD() {
        return cHD;
    }

    public void setcHD(String cHD) {
        this.cHD = cHD;
    }

    public String getcZY() {
        return cZY;
    }

    public void setcZY(String cZY) {
        this.cZY = cZY;
    }
}
