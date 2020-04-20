package com.cenfotec.ponto.data.model;

public class Notification {
    private String id;
    private String userId;
    private String title;
    private String detail;
    private boolean isRead;

    public Notification() {

    }

    public Notification(String id, String userId, String title, String detail, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.detail = detail;
        this.isRead = isRead;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
