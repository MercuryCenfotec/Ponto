package model;

import com.cenfotec.ponto.data.model.Petitioner;

/**
 * Created by wolfsoft4 on 20/9/18.
 */

public class ProfileModel {
    Integer inbox,arrow;
    String txttrades, txthistory, userId, userType;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public ProfileModel(Integer inbox, Integer arrow, String txttrades, String txthistory) {
        this.inbox = inbox;
        this.arrow = arrow;
        this.txttrades = txttrades;
        this.txthistory = txthistory;
    }

    public ProfileModel(Integer inbox, Integer arrow, String txttrades, String txthistory, String userId, String userType) {
        this.inbox = inbox;
        this.arrow = arrow;
        this.txttrades = txttrades;
        this.txthistory = txthistory;
        this.userId = userId;
        this.userType = userType;
    }
}
