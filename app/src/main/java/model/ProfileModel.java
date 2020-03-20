package model;

import com.cenfotec.ponto.data.model.Petitioner;

/**
 * Created by wolfsoft4 on 20/9/18.
 */

public class ProfileModel {
    Integer inbox,arrow;
    String txttrades, txthistory, petitionerId;

    public Integer getInbox() {
        return inbox;
    }

    public void setInbox(Integer inbox) {
        this.inbox = inbox;
    }

    public Integer getArrow() {
        return arrow;
    }

    public void setArrow(Integer arrow) {
        this.arrow = arrow;
    }

    public String getTxttrades() {
        return txttrades;
    }

    public void setTxttrades(String txttrades) {
        this.txttrades = txttrades;
    }

    public String getTxthistory() {
        return txthistory;
    }

    public void setTxthistory(String txthistory) {
        this.txthistory = txthistory;
    }

    public String getPetitionerId() {
        return petitionerId;
    }

    public void setPetitionerId(String petitionerId) {
        this.petitionerId = petitionerId;
    }

    public ProfileModel(Integer inbox, Integer arrow, String txttrades, String txthistory) {
        this.inbox = inbox;
        this.arrow = arrow;
        this.txttrades = txttrades;
        this.txthistory = txthistory;
    }

    public ProfileModel(Integer inbox, Integer arrow, String txttrades, String txthistory, String petitionerId) {
        this.inbox = inbox;
        this.arrow = arrow;
        this.txttrades = txttrades;
        this.txthistory = txthistory;
        this.petitionerId = petitionerId;
    }
}
