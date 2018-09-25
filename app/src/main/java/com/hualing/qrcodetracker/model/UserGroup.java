package com.hualing.qrcodetracker.model;

public class UserGroup {
    private int GroupID;
    private int IsAdmin;
    private int GroupStatus;
    private int GroupOrder;
    private int GroupCode;
    private String GroupName;
    private String GroupDesc;
    private String GroupMenu;

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public int getIsAdmin() {
        return IsAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        IsAdmin = isAdmin;
    }

    public int getGroupStatus() {
        return GroupStatus;
    }

    public void setGroupStatus(int groupStatus) {
        GroupStatus = groupStatus;
    }

    public int getGroupOrder() {
        return GroupOrder;
    }

    public void setGroupOrder(int groupOrder) {
        GroupOrder = groupOrder;
    }

    public int getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(int groupCode) {
        GroupCode = groupCode;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupDesc() {
        return GroupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        GroupDesc = groupDesc;
    }

    public String getGroupMenu() {
        return GroupMenu;
    }

    public void setGroupMenu(String groupMenu) {
        GroupMenu = groupMenu;
    }

    public String getGroupSort() {
        return GroupSort;
    }

    public void setGroupSort(String groupSort) {
        GroupSort = groupSort;
    }

    public String getAuth_Mobile() {
        return Auth_Mobile;
    }

    public void setAuth_Mobile(String auth_Mobile) {
        Auth_Mobile = auth_Mobile;
    }

    public String getAuth_Computer() {
        return Auth_Computer;
    }

    public void setAuth_Computer(String auth_Computer) {
        Auth_Computer = auth_Computer;
    }

    private String GroupSort;
    private String Auth_Mobile;
    private String Auth_Computer;

}
