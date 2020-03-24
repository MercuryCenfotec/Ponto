package com.cenfotec.ponto.data.model;

import java.time.LocalDate;
import java.util.List;

public class ServicePetition {
    private String id;
    private String petitionerId;
    private String name;
    private String description;
//    private List<Ability> abilities;
//    private List<String> files;
    private String serviceTypeId;
    private Boolean isFinished;
//    private List<Offer> offers;
    private Appointment appointments;
    private String selectedOfferId;
    private LocalDate closingDate;

    public ServicePetition() {
    }

    public ServicePetition(String id, String petitionerId, String name, String description, String serviceTypeId, Boolean isFinished) {
        this.id = id;
        this.petitionerId = petitionerId;
        this.name = name;
        this.description = description;
        this.serviceTypeId = serviceTypeId;
        this.isFinished = isFinished;
    }

    public ServicePetition(String id, String petitionerId, String name, String description, String serviceTypeId, Boolean isFinished, Appointment appointments, String selectedOfferId, LocalDate closingDate) {
        this.id = id;
        this.petitionerId = petitionerId;
        this.name = name;
        this.description = description;
        this.serviceTypeId = serviceTypeId;
        this.isFinished = isFinished;
        this.appointments = appointments;
        this.selectedOfferId = selectedOfferId;
        this.closingDate = closingDate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceType) {
        this.serviceTypeId = serviceType;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public Appointment getAppointments() {
        return appointments;
    }

    public void setAppointments(Appointment appointments) {
        this.appointments = appointments;
    }

    public String getSelectedOfferId() {
        return selectedOfferId;
    }

    public void setSelectedOfferId(String selectedOfferId) {
        this.selectedOfferId = selectedOfferId;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }
}
