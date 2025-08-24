package org.example.integration;

import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.example.dto.PolicyFilter;
import org.example.filter.JCasbinFilter;
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
    
    @Test
    void testFilteredPolicyLoading() {
        // Add multiple policies for different subjects
        enforcer.addPolicy("alice", "data1", "read");
        enforcer.addPolicy("alice", "data1", "write");
        enforcer.addPolicy("bob", "data2", "read");
        enforcer.addPolicy("carol", "data3", "write");
        enforcer.savePolicy();
        
        // Test loading filtered policies for specific subject
        PolicyFilter filter = new PolicyFilter("alice", null, null);
        JCasbinFilter jcasbinFilter = new JCasbinFilter(filter);
        
        // Create new enforcer and load filtered policies
        Enforcer filteredEnforcer = new Enforcer(
            getClass().getClassLoader().getResource("model.conf").getPath(),
            adapter
        );
        
        try {
            filteredEnforcer.loadFilteredPolicy(jcasbinFilter.getFilterValues());
            
            // Should be marked as filtered
            assertTrue(filteredEnforcer.isFiltered(), "Enforcer should be marked as filtered");
            
            // Should only have Alice's policies
            var policies = filteredEnforcer.getPolicy();
            assertEquals(2, policies.size(), "Should have 2 policies for Alice");
            
            // Verify policies contain only Alice's entries
            boolean hasAliceRead = policies.stream()
                .anyMatch(p -> p.get(0).equals("alice") && p.get(1).equals("data1") && p.get(2).equals("read"));
            boolean hasAliceWrite = policies.stream()
                .anyMatch(p -> p.get(0).equals("alice") && p.get(1).equals("data1") && p.get(2).equals("write"));
                
            assertTrue(hasAliceRead, "Should have Alice's read policy");
            assertTrue(hasAliceWrite, "Should have Alice's write policy");
            
            // Should not have other users' policies
            boolean hasOtherPolicies = policies.stream()
                .anyMatch(p -> !p.get(0).equals("alice"));
            assertFalse(hasOtherPolicies, "Should not have policies for other users");
            
        } catch (Exception e) {
            fail("Failed to load filtered policy: " + e.getMessage());
        }
    }
    
    @Test
    void testFilteredPolicyByAction() {
        // Add multiple policies with different actions
        enforcer.addPolicy("alice", "data1", "read");
        enforcer.addPolicy("alice", "data2", "write");
        enforcer.addPolicy("bob", "data1", "read");
        enforcer.addPolicy("bob", "data2", "write");
        enforcer.savePolicy();
        
        // Test loading filtered policies for specific action
        PolicyFilter filter = new PolicyFilter(null, null, "read");
        JCasbinFilter jcasbinFilter = new JCasbinFilter(filter);
        
        Enforcer filteredEnforcer = new Enforcer(
            getClass().getClassLoader().getResource("model.conf").getPath(),
            adapter
        );
        
        try {
            filteredEnforcer.loadFilteredPolicy(jcasbinFilter.getFilterValues());
            
            var policies = filteredEnforcer.getPolicy();
            
            // Should only have read policies
            boolean allReadPolicies = policies.stream()
                .allMatch(p -> p.get(2).equals("read"));
            assertTrue(allReadPolicies, "Should only have read policies");
            
            // Should have policies for both users with read action
            assertEquals(2, policies.size(), "Should have 2 read policies");
            
        } catch (Exception e) {
            fail("Failed to load filtered policy by action: " + e.getMessage());
        }
    }
}
