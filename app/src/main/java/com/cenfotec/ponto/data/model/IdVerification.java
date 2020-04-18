package com.cenfotec.ponto.data.model;

public class IdVerification {
    private String id;
    private String userName;
    private String userIdNumber;
    private String userId;
    private String idFrontImage;
    private String idBackImage;
    private String userRecentImage;

    public IdVerification() {
    }

    public IdVerification(String id, String userName, String userIdNumber, String userId, String idFrontImage, String idBackImage, String userRecentImage) {
        this.id = id;
        this.userName = userName;
        this.userIdNumber = userIdNumber;
        this.userId = userId;
        this.idFrontImage = idFrontImage;
        this.idBackImage = idBackImage;
        this.userRecentImage = userRecentImage;
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

    public String getIdFrontImage() {
        return idFrontImage;
    }

    public void setIdFrontImage(String idFrontImage) {
        this.idFrontImage = idFrontImage;
    }

    public String getIdBackImage() {
        return idBackImage;
    }

    public void setIdBackImage(String idBackImage) {
        this.idBackImage = idBackImage;
    }

    public String getUserRecentImage() {
        return userRecentImage;
    }

    public void setUserRecentImage(String userRecentImage) {
        this.userRecentImage = userRecentImage;
    }
}
