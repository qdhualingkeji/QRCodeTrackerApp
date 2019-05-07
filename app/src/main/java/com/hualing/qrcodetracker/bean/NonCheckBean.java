package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class NonCheckBean {

    private String dh;
    private String name;
    private String time;
    private Integer fzrID;
    private Integer flfzrID;
    private Integer llfzrID;
    private Integer tlfzrID;//退料负责人ID
    private Integer slfzrID;//收料负责人ID

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getFzrID() {
        return fzrID;
    }

    public void setFzrID(Integer fzrID) {
        this.fzrID = fzrID;
    }

    public Integer getFlfzrID() {
        return flfzrID;
    }

    public void setFlfzrID(Integer flfzrID) {
        this.flfzrID = flfzrID;
    }

    public Integer getLlfzrID() {
        return llfzrID;
    }

    public void setLlfzrID(Integer llfzrID) {
        this.llfzrID = llfzrID;
    }

    public Integer getTlfzrID() {
        return tlfzrID;
    }

    public void setTlfzrID(Integer tlfzrID) {
        this.tlfzrID = tlfzrID;
    }

    public Integer getSlfzrID() {
        return slfzrID;
    }

    public void setSlfzrID(Integer slfzrID) {
        this.slfzrID = slfzrID;
    }
}
