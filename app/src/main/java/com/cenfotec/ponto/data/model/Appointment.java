package com.cenfotec.ponto.data.model;

import java.time.LocalDateTime;

public class Appointment {
    private String id;
    private String title;
    private String location;
    private String startDateTime;
    private String description;
    private String petitionerId;
    private String bidderId;

    public Appointment() {
    }

    public Appointment(String id, String title, String location, String startDateTime,
                       String description, String petitionerId, String bidderId) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.startDateTime = startDateTime;
        this.description = description;
        this.petitionerId = petitionerId;
        this.bidderId = bidderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPetitionerId() {
        return petitionerId;
    }

    public void setPetitionerId(String petitionerId) {
        this.petitionerId = petitionerId;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }
}
