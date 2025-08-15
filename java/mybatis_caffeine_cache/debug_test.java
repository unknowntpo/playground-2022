import org.example.service.RoleService;
import org.example.entity.Role;

public class DebugTest {
    public static void main(String[] args) {
        try {
            RoleService roleService = new RoleService();
            
            System.out.println("Testing getRoleById(1L):");
            Role role = roleService.getRoleById(1L);
            System.out.println("Role: " + role);
            
            System.out.println("\nTesting getRoleByIdWithUsers(1L):");
            Role roleWithUsers = roleService.getRoleByIdWithUsers(1L);
            System.out.println("Role with users: " + roleWithUsers);
            if (roleWithUsers != null && roleWithUsers.getUsers() != null) {
                System.out.println("User count: " + roleWithUsers.getUsers().size());
                roleWithUsers.getUsers().forEach(user -> System.out.println("  User: " + user.getName()));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}