package com.cenfotec.ponto.data.model;

public class Contract {

    private String id;
    private String petitionerId;
    private String bidderId;
    private String petitionerSignatureUrl;
    private String bidderSignatureUrl;

    public Contract(){

    }

    public Contract(String id, String petitionerId, String bidderId, String petitionerSignatureUrl, String bidderSignatureUrl) {
        this.id = id;
        this.petitionerId = petitionerId;
        this.bidderId = bidderId;
        this.petitionerSignatureUrl = petitionerSignatureUrl;
        this.bidderSignatureUrl = bidderSignatureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetitionerId() {
        return petitionerId;
    }

    public void setPetitionerId(String petitionerId) {
        this.petitionerId = petitionerId;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getPetitionerSignatureUrl() {
        return petitionerSignatureUrl;
    }

    public void setPetitionerSignatureUrl(String petitionerSignatureUrl) {
        this.petitionerSignatureUrl = petitionerSignatureUrl;
    }

    public String getBidderSignatureUrl() {
        return bidderSignatureUrl;
    }

    public void setBidderSignatureUrl(String bidderSignatureUrl) {
        this.bidderSignatureUrl = bidderSignatureUrl;
    }
}
