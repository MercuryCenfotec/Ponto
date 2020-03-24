package com.cenfotec.ponto.data.model;

import java.util.List;

public class Offer {
    private String id;
    private String bidderId;
    private Float cost;
    private Float duration;
    private String durationType;
    private String description;
    private Boolean isCounterOffer;
    private Float counterOfferCost;
    private List<Task> taskList;
    private PaymentMethod paymentMethod;
    private boolean accepted;
    private String servicePetitionId;

    public Offer() {
    }

    public Offer(String id, String bidder, Float cost, Float duration, String durationType, String description, Boolean isCounterOffer, Float counterOfferCost, List<Task> taskList, PaymentMethod paymentMethod, boolean accepted, String servicePetitionId) {
        this.id = id;
        this.bidderId = bidder;
        this.cost = cost;
        this.duration = duration;
        this.durationType = durationType;
        this.description = description;
        this.isCounterOffer = isCounterOffer;
        this.counterOfferCost = counterOfferCost;
        this.taskList = taskList;
        this.paymentMethod = paymentMethod;
        this.accepted = accepted;
        this.servicePetitionId = servicePetitionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getDuration() {
        return duration;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCounterOffer() {
        return isCounterOffer;
    }

    public void setCounterOffer(Boolean counterOffer) {
        isCounterOffer = counterOffer;
    }

    public Float getCounterOfferCost() {
        return counterOfferCost;
    }

    public void setCounterOfferCost(Float counterOfferCost) {
        this.counterOfferCost = counterOfferCost;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getServicePetitionId() {
        return servicePetitionId;
    }

    public void setServicePetitionId(String servicePetitionId) {
        this.servicePetitionId = servicePetitionId;
    }
}
