package com.cenfotec.ponto.data.model;

public class ServiceType {
    private Integer id;
    private String ServiceType;

    public ServiceType() {
    }

    public ServiceType(Integer id, String serviceType) {
        this.id = id;
        ServiceType = serviceType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }
}
