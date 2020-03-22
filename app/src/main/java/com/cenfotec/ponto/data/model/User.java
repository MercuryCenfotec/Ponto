package com.cenfotec.ponto.data.model;

public class User {

    private String id;
    private String fullName;
    private String birthDate;
    private String email;
    private String identificationNumber;
    private String password;
    private int status;
    private float rating;
    private boolean allowsPushNotifications;
    private int userType;
    private String profileImageUrl;

    public User() {
    }

    public User(String id, String fullName, String birthDate, String email,
                String identificationNumber, String password, int status, float rating,
                boolean allowsPushNotifications, int userType, String profileImageUrl) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.identificationNumber = identificationNumber;
        this.password = password;
        this.status = status;
        this.rating = rating;
        this.allowsPushNotifications = allowsPushNotifications;
        this.userType = userType;
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isAllowsPushNotifications() {
        return allowsPushNotifications;
    }

    public void setAllowsPushNotifications(boolean allowsPushNotifications) {
        this.allowsPushNotifications = allowsPushNotifications;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}

