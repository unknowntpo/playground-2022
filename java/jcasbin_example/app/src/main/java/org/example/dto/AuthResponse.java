package org.example.dto;

public class AuthResponse {
    private boolean allowed;
    private String message;
    
    public AuthResponse() {}
    
    public AuthResponse(boolean allowed, String message) {
        this.allowed = allowed;
        this.message = message;
    }
    
    public boolean isAllowed() {
        return allowed;
    }
    
    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}