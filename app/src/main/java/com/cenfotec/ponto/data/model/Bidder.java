package com.cenfotec.ponto.data.model;

import java.time.LocalDate;

public class Bidder {

    private String id;
    private String biography;
    private String userId;

    public Bidder(){

    }

    public Bidder(String id, String biography, String userId) {
        this.id = id;
        this.biography = biography;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
