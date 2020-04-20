package com.cenfotec.ponto.data.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rating {
    private String id;
    private String userId;
    private String reviewerId;
    private Float rating;
    private String  dateCreated;

    public Rating() {
    }

    public Rating(String id, String userId, String reviewerId, Float rating) {
        this.id = id;
        this.userId = userId;
        this.reviewerId = reviewerId;
        this.rating = rating;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();

        this.dateCreated = df.format(today);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
