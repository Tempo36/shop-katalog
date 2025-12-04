package com.simplemessenger.model;

public class User {
    private String phone;
    private String name;
    private boolean online;
    
    public User() {}
    
    public User(String phone, String name) {
        this.phone = phone;
        this.name = name;
        this.online = false;
    }
    
    // Getters and Setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
}
