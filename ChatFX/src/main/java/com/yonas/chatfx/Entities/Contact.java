package com.yonas.chatfx.Entities;

public class Contact {
    private int idContact;

    private String userName;
    private boolean isBlocked;

    public Contact() {

    }

    public int getIdContact() {
        return idContact;
    }

    public String getUserName() {return userName;}

    public boolean getBlocked() {
        return isBlocked;
    }
    public void setIdContact(int idContact) {
        this.idContact = idContact;
    }

    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }
    public void setUserName(String userName) { this.userName = userName;}

    @Override
    public String toString() {
        return getUserName();
    }
}
