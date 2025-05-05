package com.yonas.chatfx.Entities;

public class User {
    private int idUser = -1;
    private final String userName;
    private final String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(int idUser, String userName, String password) {
        this.idUser = idUser;
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getIdUser() {
        return this.idUser;
    }

    public void setIdUser(int IdUsuario) {
        this.idUser = IdUsuario;
    }
   
}
