package com.hualing.qrcodetracker.bean;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

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
    private String bcpInShowJAStr;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBcpInShowJAStr() {
        return bcpInShowJAStr;
    }

    public void setBcpInShowJAStr(String bcpInShowJAStr) {
        this.bcpInShowJAStr = bcpInShowJAStr;
    }

    public static final Integer ZJY=0;//质检员常量
    public static final Integer FZR=1;//负责人常量
    public static final Integer BZ=2;//班长常量
    public static final Integer ZJLD=3;//质检领导常量
    public static final Integer FLFZR=4;//发料负责人常量
    public static final Integer LLFZR=5;//领料负责人常量
    public static final Integer KG=6;//仓库管理员常量
    public static final Integer TLFZR=7;//退料负责人常量
    public static final Integer SLFZR=8;//收料负责人常量
    public static final Integer BCPBZ=9;//半成品审核的班长常量
    public static final Integer CPBZ=10;//成品审核的班长常量

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
