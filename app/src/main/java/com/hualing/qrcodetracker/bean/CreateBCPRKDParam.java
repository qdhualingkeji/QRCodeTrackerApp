package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */
//这个类半成品、成品都用，只是访问的属性不同
public class CreateBCPRKDParam {

    private String InDh;
    private String JhDw;
    private String ShRq;
    private String Shr;
    private String ShFzr;
    private String Jhr;
    private String JhFzr;
    private Integer BzID;
    private Integer BzStatus;
    private Integer FzrID;//半成品录入时的负责人
    private Integer FzrStatus;
    private Integer ZjyID;
    private Integer ZjyStatus;
    private Integer ZjldID;
    private Integer ZjldStatus;
    private Integer FlfzrID;//成品入库时的发料负责人
    private Integer FlfzrStatus;
    private Integer KgID;
    private Integer KgStatus;
    private Integer LlfzrID;//成品入库时的仓库负责人
    private Integer LlfzrStatus;
    private String remark;

    public String getInDh() {
        return InDh;
    }

    public void setInDh(String inDh) {
        InDh = inDh;
    }

    public String getJhDw() {
        return JhDw;
    }

    public void setJhDw(String jhDw) {
        JhDw = jhDw;
    }

    public String getShRq() {
        return ShRq;
    }

    public void setShRq(String shRq) {
        ShRq = shRq;
    }

    public Integer getFzrID() {
        return FzrID;
    }

    public void setFzrID(Integer fzrID) {
        FzrID = fzrID;
    }

    public Integer getFzrStatus() {
        return FzrStatus;
    }

    public void setFzrStatus(Integer fzrStatus) {
        FzrStatus = fzrStatus;
    }

    public Integer getZjyID() {
        return ZjyID;
    }

    public void setZjyID(Integer zjyID) {
        ZjyID = zjyID;
    }

    public Integer getZjyStatus() {
        return ZjyStatus;
    }

    public void setZjyStatus(Integer zjyStatus) {
        ZjyStatus = zjyStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShr() {
        return Shr;
    }

    public void setShr(String shr) {
        Shr = shr;
    }

    public String getShFzr() {
        return ShFzr;
    }

    public void setShFzr(String shFzr) {
        ShFzr = shFzr;
    }

    public String getJhr() {
        return Jhr;
    }

    public void setJhr(String jhr) {
        Jhr = jhr;
    }

    public String getJhFzr() {
        return JhFzr;
    }

    public void setJhFzr(String jhFzr) {
        JhFzr = jhFzr;
    }

    public Integer getBzID() {
        return BzID;
    }

    public void setBzID(Integer bzID) {
        BzID = bzID;
    }

    public Integer getBzStatus() {
        return BzStatus;
    }

    public void setBzStatus(Integer bzStatus) {
        BzStatus = bzStatus;
    }

    public Integer getZjldID() {
        return ZjldID;
    }

    public void setZjldID(Integer zjldID) {
        ZjldID = zjldID;
    }

    public Integer getZjldStatus() {
        return ZjldStatus;
    }

    public void setZjldStatus(Integer zjldStatus) {
        ZjldStatus = zjldStatus;
    }

    public Integer getFlfzrID() {
        return FlfzrID;
    }

    public void setFlfzrID(Integer flfzrID) {
        FlfzrID = flfzrID;
    }

    public Integer getFlfzrStatus() {
        return FlfzrStatus;
    }

    public void setFlfzrStatus(Integer flfzrStatus) {
        FlfzrStatus = flfzrStatus;
    }

    public Integer getKgID() {
        return KgID;
    }

    public void setKgID(Integer kgID) {
        KgID = kgID;
    }

    public Integer getKgStatus() {
        return KgStatus;
    }

    public void setKgStatus(Integer kgStatus) {
        KgStatus = kgStatus;
    }

    public Integer getLlfzrID() {
        return LlfzrID;
    }

    public void setLlfzrID(Integer llfzrID) {
        LlfzrID = llfzrID;
    }

    public Integer getLlfzrStatus() {
        return LlfzrStatus;
    }

    public void setLlfzrStatus(Integer llfzrStatus) {
        LlfzrStatus = llfzrStatus;
    }

}
