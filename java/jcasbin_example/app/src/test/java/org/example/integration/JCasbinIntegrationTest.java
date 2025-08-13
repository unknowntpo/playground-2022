package org.example.integration;

import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

public class JCasbinIntegrationTest {

    private Enforcer enforcer;
    private JDBCAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        // Use the same database configuration as the application
        String jdbcUrl = "jdbc:mysql://localhost:3308/jcasbin_demo";
        String username = "jcasbin";
        String password = "jcasbin123";

        adapter = new JDBCAdapter("com.mysql.cj.jdbc.Driver", jdbcUrl, username, password);

        String modelPath = getClass().getClassLoader()
            .getResource("model.conf").getPath();

        enforcer = new Enforcer(modelPath, adapter);

        // Ensure clean state for each test
        enforcer.clearPolicy();
        enforcer.savePolicy();
    }

    @Test
    void testPolicyPersistence() {
        // Add a policy
        boolean added = enforcer.addPolicy("alice", "data1", "read");
        assertTrue(added, "Policy should be added successfully");

        // Save to database
        enforcer.savePolicy();

        // Create new enforcer instance to simulate another application instance
        Enforcer newEnforcer = new Enforcer(
            getClass().getClassLoader().getResource("model.conf").getPath(),
            adapter
        );

        // Load policies from database
        newEnforcer.loadPolicy();

        // Verify policy exists in new instance
        boolean hasPolicy = newEnforcer.hasPolicy("alice", "data1", "read");
        assertTrue(hasPolicy, "Policy should be persisted and loaded");

        // Test authorization
        boolean allowed = newEnforcer.enforce("alice", "data1", "read");
        assertTrue(allowed, "Alice should be allowed to read data1");

        boolean denied = newEnforcer.enforce("alice", "data1", "write");
        assertFalse(denied, "Alice should not be allowed to write data1");
    }

    @Test
    void testRoleBasedAccessControl() {
        // Add role assignment
        enforcer.addRoleForUser("alice", "admin");
        enforcer.addPolicy("admin", "data1", "read");
        enforcer.addPolicy("admin", "data1", "write");

        // Save to database
        enforcer.savePolicy();

        // Test role-based access
        boolean canRead = enforcer.enforce("alice", "data1", "read");
        assertTrue(canRead, "Alice with admin role should be able to read");

        boolean canWrite = enforcer.enforce("alice", "data1", "write");
        assertTrue(canWrite, "Alice with admin role should be able to write");

        // Create new enforcer to test persistence
        Enforcer newEnforcer = new Enforcer(
            getClass().getClassLoader().getResource("model.conf").getPath(),
            adapter
        );
        newEnforcer.loadPolicy();

        // Verify role assignment persisted
        boolean stillCanRead = newEnforcer.enforce("alice", "data1", "read");
        assertTrue(stillCanRead, "Role assignment should persist across instances");
    }

    @Test
    void testMultiInstanceSynchronization() {
        // Instance 1 adds a policy
        enforcer.addPolicy("bob", "data2", "read");
        enforcer.savePolicy();

        // Instance 2 loads policies
        Enforcer instance2 = new Enforcer(
            getClass().getClassLoader().getResource("model.conf").getPath(),
            adapter
        );
        instance2.loadPolicy();

        // Instance 2 should see the policy
        boolean canRead = instance2.enforce("bob", "data2", "read");
        assertTrue(canRead, "Instance 2 should see policy added by instance 1");

        // Instance 2 adds another policy
        instance2.addPolicy("carol", "data3", "write");
        instance2.savePolicy();

        // Instance 1 reloads and should see new policy
        enforcer.loadPolicy();
        boolean carolCanWrite = enforcer.enforce("carol", "data3", "write");
        assertTrue(carolCanWrite, "Instance 1 should see policy added by instance 2 after reload");
    }
}
