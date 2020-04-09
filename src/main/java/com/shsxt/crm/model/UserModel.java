package com.shsxt.crm.model;

public class UserModel {
    private String userIdStr;
    private String userName;
    private String trueName;
    private String userPwd;

    public UserModel(String userIdStr, String userName, String trueName, String userPwd) {
        this.userIdStr = userIdStr;
        this.userName = userName;
        this.trueName = trueName;
        this.userPwd = userPwd;
    }

    public UserModel() {
    }

    public String getuserIdStr() {
        return userIdStr;
    }

    public void setuserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
