package com.cenfotec.ponto.data.model;

import java.util.List;

public class Membership {
    private String name;
    private float price;
    private String id;
    private List<String> benefits;

    public Membership() {
    }

    public Membership(String name, float price, String id, List<String> benefits) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.benefits = benefits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }
}
