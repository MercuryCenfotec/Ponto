package com.cenfotec.ponto.data.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
  private String id;
  private String servicePetitionId;
  private String servicePetitionName;
  private String petitionerId;
  private String petitionerImgUrl;
  private String petitionerName;
  private String bidderId;
  private String bidderImgUrl;
  private String bidderName;
  private Boolean isUnreadBidder;
  private Boolean isUnreadPetitioner;
  private List<Message> messages;
  private String state;

  public Chat() {
  }

  public Chat(String id, String servicePetitionId, String servicePetitionName, String petitionerId, String petitionerImgUrl, String petitionerName, String bidderId, String bidderImgUrl, String bidderName) {
    this.id = id;
    this.servicePetitionId = servicePetitionId;
    this.servicePetitionName = servicePetitionName;
    this.petitionerId = petitionerId;
    this.petitionerImgUrl = petitionerImgUrl;
    this.petitionerName = petitionerName;
    this.bidderId = bidderId;
    this.bidderImgUrl = bidderImgUrl;
    this.bidderName = bidderName;
    this.messages = new ArrayList<>();
    this.state = "active";
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getServicePetitionId() {
    return servicePetitionId;
  }

  public void setServicePetitionId(String servicePetitionId) {
    this.servicePetitionId = servicePetitionId;
  }

  public String getServicePetitionName() {
    return servicePetitionName;
  }

  public void setServicePetitionName(String servicePetitionName) {
    this.servicePetitionName = servicePetitionName;
  }

  public String getPetitionerId() {
    return petitionerId;
  }

  public void setPetitionerId(String petitionerId) {
    this.petitionerId = petitionerId;
  }

  public String getPetitionerImgUrl() {
    return petitionerImgUrl;
  }

  public void setPetitionerImgUrl(String petitionerImgUrl) {
    this.petitionerImgUrl = petitionerImgUrl;
  }

  public String getPetitionerName() {
    return petitionerName;
  }

  public void setPetitionerName(String petitionerName) {
    this.petitionerName = petitionerName;
  }

  public String getBidderId() {
    return bidderId;
  }

  public void setBidderId(String bidderId) {
    this.bidderId = bidderId;
  }

  public String getBidderImgUrl() {
    return bidderImgUrl;
  }

  public void setBidderImgUrl(String bidderImgUrl) {
    this.bidderImgUrl = bidderImgUrl;
  }

  public String getBidderName() {
    return bidderName;
  }

  public void setBidderName(String bidderName) {
    this.bidderName = bidderName;
  }

  public Boolean getUnreadBidder() {
    return isUnreadBidder;
  }

  public void setUnreadBidder(Boolean unreadBidder) {
    isUnreadBidder = unreadBidder;
  }

  public Boolean getUnreadPetitioner() {
    return isUnreadPetitioner;
  }

  public void setUnreadPetitioner(Boolean unreadPetitioner) {
    isUnreadPetitioner = unreadPetitioner;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
