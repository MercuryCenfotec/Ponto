package com.cenfotec.ponto.data.model;

public class Message {
  private String id;
  private String message;
  private String ownerId;
  private String dateTime;

  public Message() {
  }

  public Message(String id, String message, String ownerId, String dateTime) {
    this.id = id;
    this.message = message;
    this.ownerId = ownerId;
    this.dateTime = dateTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }
}
