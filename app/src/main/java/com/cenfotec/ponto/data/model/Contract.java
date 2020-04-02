package com.cenfotec.ponto.data.model;

public class Contract {

    private String id;
    private String name;
    private String dateCreated;
    private String petitionerId;
    private String bidderId;
    private String servicePetitionId;
    private String offerId;
    private String petitionerSignatureUrl;
    private String bidderSignatureUrl;
    private Float finalCost;

    public Contract() {

    }

    public Contract(String id, String petitionerId, String bidderId, String petitionerSignatureUrl,
                    String bidderSignatureUrl) {
        this.id = id;
        this.petitionerId = petitionerId;
        this.bidderId = bidderId;
        this.petitionerSignatureUrl = petitionerSignatureUrl;
        this.bidderSignatureUrl = bidderSignatureUrl;
    }

    public Contract(String id, String name, String dateCreated, String petitionerId, String bidderId,
                    String servicePetitionId, String offerId, String petitionerSignatureUrl,
                    String bidderSignatureUrl) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.petitionerId = petitionerId;
        this.bidderId = bidderId;
        this.servicePetitionId = servicePetitionId;
        this.offerId = offerId;
        this.petitionerSignatureUrl = petitionerSignatureUrl;
        this.bidderSignatureUrl = bidderSignatureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getServicePetitionId() {
        return servicePetitionId;
    }

    public void setServicePetitionId(String servicePetitionId) {
        this.servicePetitionId = servicePetitionId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
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

    public Float getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(Float finalCost) {
        this.finalCost = finalCost;
    }
}
