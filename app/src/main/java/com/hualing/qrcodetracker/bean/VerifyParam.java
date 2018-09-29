package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class VerifyParam {

    private String dh ;
    private Integer checkQXFlag;//审核人身份，1是领导（负责人），0是质检员
    public static final Integer ZJY=0;//质检员常量
    public static final Integer FZR=1;//负责人常量
    public static final Integer BZ=2;//班长常量
    public static final Integer ZJLD=3;//质检领导常量

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public Integer getCheckQXFlag() {
        return checkQXFlag;
    }

    public void setCheckQXFlag(Integer checkQXFlag) {
        this.checkQXFlag = checkQXFlag;
    }
}
