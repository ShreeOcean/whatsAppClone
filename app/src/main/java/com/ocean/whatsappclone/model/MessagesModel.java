package com.ocean.whatsappclone.model;

public class MessagesModel {

    private String userId;
    private String  message;
    private String userName;
    private Long timestamp;

    public MessagesModel(){}//empty constructor for firebase

    public MessagesModel(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public MessagesModel(String userId, String message, Long timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessagesModel(String senderId, String senderName, String msg) {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
