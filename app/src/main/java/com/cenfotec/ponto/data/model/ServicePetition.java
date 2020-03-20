package com.cenfotec.ponto.data.model;

import java.time.LocalDate;
import java.util.List;

public class ServicePetition {
    private Long id;
    private Petitioner petitioner;
    private String Name;
    private String description;
    private List<Ability> abilities;
    private List<String> files;
    private ServiceType serviceType;
    private Boolean isFinished;
    private List<Offer> offers;
    private Appointment appointments;
    private Offer selectedOffer;
    private LocalDate closingDate;

    public ServicePetition() {
    }

    public ServicePetition(Long id, Petitioner petitioner, String name, String description, List<Ability> abilities, List<String> files, ServiceType serviceType, Boolean isFinished, List<Offer> offers, Appointment appointments, Offer selectedOffer, LocalDate closingDate) {
        this.id = id;
        this.petitioner = petitioner;
        Name = name;
        this.description = description;
        this.abilities = abilities;
        this.files = files;
        this.serviceType = serviceType;
        this.isFinished = isFinished;
        this.offers = offers;
        this.appointments = appointments;
        this.selectedOffer = selectedOffer;
        this.closingDate = closingDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Petitioner getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(Petitioner petitioner) {
        this.petitioner = petitioner;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public Appointment getAppointments() {
        return appointments;
    }

    public void setAppointments(Appointment appointments) {
        this.appointments = appointments;
    }

    public Offer getSelectedOffer() {
        return selectedOffer;
    }

    public void setSelectedOffer(Offer selectedOffer) {
        this.selectedOffer = selectedOffer;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }
}
