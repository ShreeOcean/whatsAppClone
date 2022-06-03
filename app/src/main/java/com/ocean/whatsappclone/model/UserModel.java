package com.ocean.whatsappclone.model;

public class UserModel {

    private String profilePic;
    private String userName;
    private String email;
    private String password;
    private String userID;
    private String lastMsg;

    public UserModel(String profilePic, String userName, String email, String password, String userID, String lastMsg) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userID = userID;
        this.lastMsg = lastMsg;
    }

    //TODO: empty constructor for firebase
    public UserModel(){}

    //TODO: constructor for signup
    public UserModel(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getUserID(String key) {
        return userID;
    }
}
