package com.quwaysim.regapp.helpers;

import androidx.annotation.NonNull;

public class User {
    private String name;
    private String email;
    private String securityLevel;
    private String user_id;
    private String phone;

    public User(String name, String email, String securityLevel, String user_id, String phone) {
        this.name = name;
        this.email = email;
        this.securityLevel = securityLevel;
        this.user_id = user_id;
        this.phone = phone;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", securityLevel='" + securityLevel + '\'' +
                ", user_id='" + user_id + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
