package com.cenfotec.ponto.data.model;

import java.time.LocalDateTime;

public class Appointment {
    private Integer id;
    private String title;
    private LocalDateTime dateTime;
    private String description;

    public Appointment() {
    }

    public Appointment(Integer id, String title, LocalDateTime dateTime, String description) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
