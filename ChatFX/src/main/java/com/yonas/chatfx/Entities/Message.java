package com.yonas.chatfx.Entities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    int idUser;
    String userName;
    String text;
    Timestamp date;
    Boolean readed;


    public int getIdUser() {
        return idUser;
    }
    public String getUserName() { return userName;}
    public String getText() {
        return text;
    }
    public Date getDate() {
        return date;
    }
    public Boolean getReaded() {
        return readed;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    public void setReaded(Boolean readed) {
        this.readed = readed;
    }
    public void setUserName(String userName) {
        this.userName = userName;}

    @Override
    public String toString() {
        String leido = (readed)?"✓✓":"✓";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        return sdf.format(getDate()) + " || " + getUserName() + ": " + getText() + "  " + leido + "  ";
    }
}
