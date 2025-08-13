package org.example.dto;

public class PolicyRequest {
    private String subject;
    private String object;
    private String action;
    
    public PolicyRequest() {}
    
    public PolicyRequest(String subject, String object, String action) {
        this.subject = subject;
        this.object = object;
        this.action = action;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getObject() {
        return object;
    }
    
    public void setObject(String object) {
        this.object = object;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
}