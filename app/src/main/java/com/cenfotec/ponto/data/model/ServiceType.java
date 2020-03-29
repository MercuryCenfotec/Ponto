package com.cenfotec.ponto.data.model;

public class ServiceType {
    private String id;
    private String serviceType;
    private String imgUrl;
    private String color;

    public ServiceType() {
    }

    public ServiceType(String id, String serviceType, String imgUrl, String color) {
        this.id = id;
        this.serviceType = serviceType;
        this.imgUrl = imgUrl;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
