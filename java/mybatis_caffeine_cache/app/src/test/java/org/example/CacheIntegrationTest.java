package org.example;

import org.example.entity.Role;
import org.example.entity.User;
import org.example.service.RoleService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CacheIntegrationTest {

    private RoleService roleService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        roleService = new RoleService();
        userService = new UserService();

        // Clear and reset test data
        clearAndResetTestData();
    }

    private void clearAndResetTestData() {
        // Create entities and use the service methods to insert data
        // Clear data by deleting all users (this will cascade to user_roles due to foreign key)
        List<User> existingUsers = userService.getAllUsers();
        for (org.example.entity.User user : existingUsers) {
            userService.deleteUser(user.getId());
        }

        List<Role> existingRoles = roleService.getAllRoles();
        for (Role role : existingRoles) {
            roleService.deleteRole(role.getId());
        }

        // Create fresh test data using entities (let database auto-generate IDs)
        // Create roles
        Role adminRole = new Role(null, "ADMIN", "Administrator with full system access");
        Role userRole = new Role(null, "USER", "Regular user with standard permissions");
        Role moderatorRole = new Role(null, "MODERATOR", "Moderator with content management permissions");
        Role viewerRole = new Role(null, "VIEWER", "Read-only access to the system");

        roleService.createRole(adminRole);
        roleService.createRole(userRole);
        roleService.createRole(moderatorRole);
        roleService.createRole(viewerRole);

        // Create users
        User user1 = new User(null, "John Doe", "john.doe@example.com");
        User user2 = new User(null, "Jane Smith", "jane.smith@example.com");
        User user3 = new User(null, "Mike Johnson", "mike.johnson@example.com");
        User user4 = new User(null, "Sarah Wilson", "sarah.wilson@example.com");
        User user5 = new User(null, "Tom Brown", "tom.brown@example.com");
        User user6 = new User(null, "Lisa Davis", "lisa.davis@example.com");

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);
        userService.createUser(user5);
        userService.createUser(user6);

        // Get the created users and roles to get their generated IDs
        List<User> allUsers = userService.getAllUsers();
        List<Role> allRoles = roleService.getAllRoles();

        // Find roles by name
        Role admin = allRoles.stream().filter(r -> "ADMIN".equals(r.getName())).findFirst().orElse(null);
        Role user = allRoles.stream().filter(r -> "USER".equals(r.getName())).findFirst().orElse(null);
        Role moderator = allRoles.stream().filter(r -> "MODERATOR".equals(r.getName())).findFirst().orElse(null);
        Role viewer = allRoles.stream().filter(r -> "VIEWER".equals(r.getName())).findFirst().orElse(null);

        // Find users by name
        User johnDoe = allUsers.stream().filter(u -> "John Doe".equals(u.getName())).findFirst().orElse(null);
        User janeSmith = allUsers.stream().filter(u -> "Jane Smith".equals(u.getName())).findFirst().orElse(null);
        User mikeJohnson = allUsers.stream().filter(u -> "Mike Johnson".equals(u.getName())).findFirst().orElse(null);
        User sarahWilson = allUsers.stream().filter(u -> "Sarah Wilson".equals(u.getName())).findFirst().orElse(null);
        User tomBrown = allUsers.stream().filter(u -> "Tom Brown".equals(u.getName())).findFirst().orElse(null);
        User lisaDavis = allUsers.stream().filter(u -> "Lisa Davis".equals(u.getName())).findFirst().orElse(null);

        // Create user-role relationships
        // Admin role has multiple users
        roleService.addUserToRole(johnDoe.getId(), admin.getId());
        roleService.addUserToRole(janeSmith.getId(), admin.getId());
        roleService.addUserToRole(mikeJohnson.getId(), admin.getId());

        // User role has multiple users
        roleService.addUserToRole(johnDoe.getId(), user.getId());
        roleService.addUserToRole(sarahWilson.getId(), user.getId());
        roleService.addUserToRole(tomBrown.getId(), user.getId());
        roleService.addUserToRole(lisaDavis.getId(), user.getId());

        // Moderator role has multiple users
        roleService.addUserToRole(janeSmith.getId(), moderator.getId());
        roleService.addUserToRole(mikeJohnson.getId(), moderator.getId());

        // Viewer role has multiple users
        roleService.addUserToRole(sarahWilson.getId(), viewer.getId());
        roleService.addUserToRole(tomBrown.getId(), viewer.getId());
        roleService.addUserToRole(lisaDavis.getId(), viewer.getId());
    }

    @Test
    public void testGetRoleByIdReturnsCorrectRole() {
        // Find the admin role
        Role adminRole = roleService.getAllRoles().stream()
                .filter(r -> "ADMIN".equals(r.getName()))
                .findFirst().orElse(null);
        assertNotNull(adminRole);

        Role fetchedRole = roleService.getRoleById(adminRole.getId());

        assertNotNull(fetchedRole);
        assertEquals("ADMIN", fetchedRole.getName());
        assertEquals("Administrator with full system access", fetchedRole.getDescription());
    }

    @Test
    public void testGetRoleByIdWithUsersReturnsMultipleUsers() {
        // Find the admin role
        Role adminRole = roleService.getAllRoles().stream()
                .filter(r -> "ADMIN".equals(r.getName()))
                .findFirst().orElse(null);
        assertNotNull(adminRole);

        Role adminRoleWithUsers = roleService.getRoleByIdWithUsers(adminRole.getId());

        assertNotNull(adminRoleWithUsers);
        assertEquals("ADMIN", adminRoleWithUsers.getName());
        assertNotNull(adminRoleWithUsers.getUsers());
        assertTrue(adminRoleWithUsers.getUsers().size() >= 3, "Admin role should have at least 3 users");

        boolean foundJohnDoe = adminRoleWithUsers.getUsers().stream()
                .anyMatch(user -> "John Doe".equals(user.getName()));
        assertTrue(foundJohnDoe, "Admin role should contain John Doe");
    }

    @Test
    public void testDeleteUserUpdatesRoleUsersCache() {
        // Find the admin role
        Role adminRole = roleService.getAllRoles().stream()
                .filter(r -> "ADMIN".equals(r.getName()))
                .findFirst().orElse(null);
        assertNotNull(adminRole);

        Role adminRoleBefore = roleService.getRoleByIdWithUsers(adminRole.getId());
        int initialUserCount = adminRoleBefore.getUsers().size();

        User userToDelete = adminRoleBefore.getUsers().get(0);
        Long userIdToDelete = userToDelete.getId();

        userService.deleteUser(userIdToDelete);

        Role adminRoleAfter = roleService.getRoleByIdWithUsers(adminRole.getId());

        assertNotNull(adminRoleAfter);
        assertEquals(initialUserCount - 1, adminRoleAfter.getUsers().size(),
                "Role should have one less user after deletion");

        boolean deletedUserStillExists = adminRoleAfter.getUsers().stream()
                .anyMatch(user -> user.getId().equals(userIdToDelete));
        assertFalse(deletedUserStillExists, "Deleted user should not appear in role's users list");
    }

    @Test
    public void testRemoveUserFromRoleUpdatesCache() {
        // Find the user role
        Role userRole = roleService.getAllRoles().stream()
                .filter(r -> "USER".equals(r.getName()))
                .findFirst().orElse(null);
        assertNotNull(userRole);

        Role userRoleWithUsers = roleService.getRoleByIdWithUsers(userRole.getId());
        assertNotNull(userRoleWithUsers);

        int initialUserCount = userRoleWithUsers.getUsers().size();
        assertTrue(initialUserCount > 0, "USER role should have users");

        User userToRemove = userRoleWithUsers.getUsers().get(0);
        Long userIdToRemove = userToRemove.getId();

        roleService.removeUserFromRole(userIdToRemove, userRole.getId());

        Role userRoleAfter = roleService.getRoleByIdWithUsers(userRole.getId());

        boolean removedUserStillExists = userRoleAfter.getUsers().stream()
                .anyMatch(user -> user.getId().equals(userIdToRemove));
        assertFalse(removedUserStillExists, "Removed user should not appear in role's users list");
        assertEquals(initialUserCount - 1, userRoleAfter.getUsers().size(),
                "Role should have one less user after removal");
    }

    @Test
    public void testCachePerformanceWithRepeatedRoleAccess() {
        // Find the admin role
        Role adminRole = roleService.getAllRoles().stream()
                .filter(r -> "ADMIN".equals(r.getName()))
                .findFirst().orElse(null);
        assertNotNull(adminRole);

        long startTime = System.nanoTime();

        for (int i = 0; i < 100; i++) {
            Role role = roleService.getRoleByIdWithUsers(adminRole.getId());
            assertNotNull(role);
        }

        long duration = System.nanoTime() - startTime;

        System.out.printf("duration: %d\n", duration);

        assertTrue(duration < 1_000_000_000L,
                "100 repeated access should complete in less than 1 second (indicating cache is working)");
    }

    @Test
    public void testMultipleUsersInRole() {
        List<Role> allRoles = roleService.getAllRoles();

        Role adminRole = allRoles.stream().filter(r -> "ADMIN".equals(r.getName())).findFirst().orElse(null);
        Role userRole = allRoles.stream().filter(r -> "USER".equals(r.getName())).findFirst().orElse(null);
        Role moderatorRole = allRoles.stream().filter(r -> "MODERATOR".equals(r.getName())).findFirst().orElse(null);
        Role viewerRole = allRoles.stream().filter(r -> "VIEWER".equals(r.getName())).findFirst().orElse(null);

        assertNotNull(adminRole);
        assertNotNull(userRole);
        assertNotNull(moderatorRole);
        assertNotNull(viewerRole);

        Role adminRoleWithUsers = roleService.getRoleByIdWithUsers(adminRole.getId());
        Role userRoleWithUsers = roleService.getRoleByIdWithUsers(userRole.getId());
        Role moderatorRoleWithUsers = roleService.getRoleByIdWithUsers(moderatorRole.getId());
        Role viewerRoleWithUsers = roleService.getRoleByIdWithUsers(viewerRole.getId());

        assertTrue(adminRoleWithUsers.getUsers().size() >= 3, "Admin role should have multiple users");
        assertTrue(userRoleWithUsers.getUsers().size() >= 4, "User role should have multiple users");
        assertTrue(moderatorRoleWithUsers.getUsers().size() >= 2, "Moderator role should have multiple users");
        assertTrue(viewerRoleWithUsers.getUsers().size() >= 3, "Viewer role should have multiple users");
    }
}
