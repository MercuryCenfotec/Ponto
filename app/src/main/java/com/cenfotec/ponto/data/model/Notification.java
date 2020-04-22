package com.cenfotec.ponto.data.model;

public class Notification {
  private String id;
  private String userId;
  private String title;
  private String detail;
  private Integer iconId;
  private Integer number;
  private String actionValue;
  private String type;
  private boolean isRead;
  private boolean isShow;
  private boolean isDone;

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

  public Integer getIconId() {
    return iconId;
  }

  public void setIconId(Integer iconId) {
    this.iconId = iconId;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public String getActionValue() {
    return actionValue;
  }

  public void setActionValue(String actionValue) {
    this.actionValue = actionValue;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isRead() {
    return isRead;
  }

  public void setRead(boolean read) {
    isRead = read;
  }

  public boolean isShow() {
    return isShow;
  }

  public void setShow(boolean show) {
    isShow = show;
  }

  public boolean isDone() {
    return isDone;
  }

  public void setDone(boolean done) {
    isDone = done;
  }
}
