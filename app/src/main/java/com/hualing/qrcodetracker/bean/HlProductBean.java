package com.hualing.qrcodetracker.bean;

import java.sql.Timestamp;

public class HlProductBean {

    private int productID;
    private String productName;
    private int sortID;
    private String model;
    private float num;
    private float price;
    private String productCode;
    private Timestamp addTime;
    private String company;
    private float yuJing;
    private float beginNum;
    private Timestamp updateTime;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public float getYuJing() {
        return yuJing;
    }

    public void setYuJing(float yuJing) {
        this.yuJing = yuJing;
    }

    public float getBeginNum() {
        return beginNum;
    }

    public void setBeginNum(float beginNum) {
        this.beginNum = beginNum;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
