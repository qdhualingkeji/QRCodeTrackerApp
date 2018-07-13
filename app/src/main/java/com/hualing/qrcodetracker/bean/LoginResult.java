package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class LoginResult {

    private String userId;

    private String userName ;

    private String pwd;

    private String trueName;

    private String checkQXGroup;//审核权限组，有19的话就是领导（负责人），反之是质检员

    public String getCheckQXGroup() {
        return checkQXGroup;
    }

    public void setCheckQXGroup(String checkQXGroup) {
        this.checkQXGroup = checkQXGroup;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
