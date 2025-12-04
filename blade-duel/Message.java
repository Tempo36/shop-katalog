package com.simplemessenger.model;

import java.time.LocalDateTime;

public class Message {
    private String fromPhone;
    private String toPhone;
    private String text;
    private LocalDateTime timestamp;
    private boolean delivered;
    
    public Message() {
        this.timestamp = LocalDateTime.now();
        this.delivered = false;
    }
    
    // Getters and Setters
    public String getFromPhone() { return fromPhone; }
    public void setFromPhone(String fromPhone) { this.fromPhone = fromPhone; }
    
    public String getToPhone() { return toPhone; }
    public void setToPhone(String toPhone) { this.toPhone = toPhone; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }
}