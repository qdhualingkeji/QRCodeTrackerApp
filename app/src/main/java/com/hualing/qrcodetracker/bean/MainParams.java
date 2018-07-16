package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class MainParams {

    private String userId ;
    private String realName ;
    private Integer checkQXFlag;//审核人身份，1是领导（负责人），0是质检员

    public static final Integer FZR=1;//负责人常量
    public static final Integer ZJY=0;//质检员常量

    public Integer getCheckQXFlag() {
        return checkQXFlag;
    }

    public void setCheckQXFlag(Integer checkQXFlag) {
        this.checkQXFlag = checkQXFlag;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

