package com.cenfotec.ponto.data.model;

public class SpinnerItem {
    private String key,value;

    public SpinnerItem() {
    }

    public SpinnerItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
