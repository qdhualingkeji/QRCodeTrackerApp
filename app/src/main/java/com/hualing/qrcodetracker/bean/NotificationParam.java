package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class NotificationParam {

    public static final int FZR = 1;
    public static final int ZJY = 2;
    public static final int BZ = 3;
    public static final int ZJLD = 4;
    public static final int KG = 5;
    public static final int FLFZR = 6;
    public static final int LLFZR = 7;
    public static final int TLFZR = 8;
    public static final int SLFZR = 9;

    private String dh ;

    private int style;

    private int personFlag;

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getPersonFlag() {
        return personFlag;
    }

    public void setPersonFlag(int personFlag) {
        this.personFlag = personFlag;
    }
}
