package com.cenfotec.ponto.data.model;

import java.util.List;

public class IdVerification {
    private String id;
    private String userName;
    private String userIdNumber;
    private String userId;
    private List<String> files;

    public IdVerification() {
    }

    public IdVerification(String id, String userName, String userIdNumber, String userId, List<String> files) {
        this.id = id;
        this.userName = userName;
        this.userIdNumber = userIdNumber;
        this.userId = userId;
        this.files = files;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdNumber() {
        return userIdNumber;
    }

    public void setUserIdNumber(String userIdNumber) {
        this.userIdNumber = userIdNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
