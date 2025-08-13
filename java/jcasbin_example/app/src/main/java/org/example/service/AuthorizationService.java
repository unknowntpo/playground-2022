package org.example.service;

import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.example.config.DatabaseConfig;
import java.util.List;

public class AuthorizationService {
    private static AuthorizationService instance;
    private final Enforcer enforcer;

    private AuthorizationService() {
        try {
            JDBCAdapter adapter = new JDBCAdapter(
                DatabaseConfig.getDriver(),
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword()
            );

            String modelPath = getClass().getClassLoader()
                .getResource("model.conf").getPath();

            this.enforcer = new Enforcer(modelPath, adapter);
            this.enforcer.loadPolicy();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize authorization service", e);
        }
    }

    public static synchronized AuthorizationService getInstance() {
        if (instance == null) {
            instance = new AuthorizationService();
        }
        return instance;
    }

    public boolean checkPermission(String subject, String object, String action) {
        return enforcer.enforce(subject, object, action);
    }

    public boolean addPolicy(String subject, String object, String action) {
        boolean added = enforcer.addPolicy(subject, object, action);
        if (added) {
            enforcer.savePolicy();
        }
        return added;
    }

    public boolean removePolicy(String subject, String object, String action) {
        boolean removed = enforcer.removePolicy(subject, object, action);
        if (removed) {
            enforcer.savePolicy();
        }
        return removed;
    }

    public boolean addRoleForUser(String user, String role) {
        boolean added = enforcer.addRoleForUser(user, role);
        if (added) {
            enforcer.savePolicy();
        }
        return added;
    }

    public boolean deleteRoleForUser(String user, String role) {
        boolean deleted = enforcer.deleteRoleForUser(user, role);
        if (deleted) {
            enforcer.savePolicy();
        }
        return deleted;
    }

    public void reloadPolicy() {
        enforcer.loadPolicy();
    }

    public List<List<String>> getAllPolicies() {
        return enforcer.getPolicy();
    }

    public List<List<String>> getAllRoles() {
        return enforcer.getGroupingPolicy();
    }
}
