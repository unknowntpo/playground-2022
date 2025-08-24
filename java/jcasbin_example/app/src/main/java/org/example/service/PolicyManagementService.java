package org.example.service;

import org.example.dto.PolicyFilter;
import java.util.List;
import java.util.ArrayList;

public class PolicyManagementService {
    private final AuthorizationService authService;
    
    public PolicyManagementService() {
        this.authService = AuthorizationService.getInstance();
    }
    
    public boolean addPolicy(String subject, String object, String action) {
        return authService.addPolicy(subject, object, action);
    }
    
    public boolean removePolicy(String subject, String object, String action) {
        return authService.removePolicy(subject, object, action);
    }
    
    public boolean addRole(String user, String role) {
        return authService.addRoleForUser(user, role);
    }
    
    public boolean removeRole(String user, String role) {
        return authService.deleteRoleForUser(user, role);
    }
    
    public List<List<String>> getAllPolicies() {
        return authService.getAllPolicies();
    }
    
    public List<List<String>> getAllRoles() {
        return authService.getAllRoles();
    }
    
    public void reloadPolicies() {
        authService.reloadPolicy();
    }
    
    public void loadFilteredPolicies(PolicyFilter filter) {
        authService.loadFilteredPolicy(filter);
    }
    
    public List<List<String>> getFilteredPolicies(PolicyFilter filter) {
        return authService.getFilteredPolicies(filter);
    }
    
    public boolean isFiltered() {
        return authService.isFiltered();
    }
}