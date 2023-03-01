package com.example.shop_app.adapter;

public class User {
    public String id;
    public String email;
    public String username;
    public String uid;
    public String phone;
    public User() {
    }

    public User(String id, String email, String username, String uid, String phone) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.uid = uid;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
