package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class TLYLBean {

    private int ID;
    private String qrcodeID;
    private String ProductName;
    private Float syzl;
    private Float tlzl;
    private String dw;
    private boolean flag ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQrcodeID() {
        return qrcodeID;
    }

    public void setQrcodeID(String qrcodeID) {
        this.qrcodeID = qrcodeID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Float getSyzl() {
        return syzl;
    }

    public void setSyzl(Float syzl) {
        this.syzl = syzl;
    }

    public Float getTlzl() {
        return tlzl;
    }

    public void setTlzl(Float tlzl) {
        this.tlzl = tlzl;
    }


    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
