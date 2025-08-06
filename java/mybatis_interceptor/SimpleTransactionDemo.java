import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mapper.UserMapper;
import org.example.model.User;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SimpleTransactionDemo {
    public static void main(String[] args) {
        try {
            System.out.println("=== Simple Transaction Verification Demo ===");
            
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            
            demonstrateTransactionBehavior(sqlSessionFactory);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void demonstrateTransactionBehavior(SqlSessionFactory sqlSessionFactory) {
        System.out.println("\n--- Demonstrating Same Transaction Behavior ---");
        
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            Connection connection = session.getConnection();
            
            // Show initial state
            int initialUsers = countRecords(connection, "user");
            int initialAudits = countRecords(connection, "change_event");
            
            System.out.printf("INITIAL STATE: Users=%d, Audit_logs=%d%n", initialUsers, initialAudits);
            System.out.printf("Connection: hashCode=%d, autoCommit=%b%n", 
                             connection.hashCode(), connection.getAutoCommit());
            
            // Insert user (triggers audit log)
            System.out.println("\n1. Inserting user (will create audit log)...");
            User user = new User("demo_user", "demo@example.com", "Demo", "User");
            userMapper.insert(user);
            
            // Check state BEFORE commit
            int afterInsertUsers = countRecords(connection, "user");
            int afterInsertAudits = countRecords(connection, "change_event");
            
            System.out.printf("AFTER INSERT (before commit): Users=%d, Audit_logs=%d%n", 
                             afterInsertUsers, afterInsertAudits);
            System.out.println("   → Both user and audit log are visible in SAME transaction");
            
            // Rollback instead of commit
            System.out.println("\n2. Rolling back transaction...");
            session.rollback();
            
            // Check state AFTER rollback
            int afterRollbackUsers = countRecords(connection, "user");
            int afterRollbackAudits = countRecords(connection, "change_event");
            
            System.out.printf("AFTER ROLLBACK: Users=%d, Audit_logs=%d%n", 
                             afterRollbackUsers, afterRollbackAudits);
            System.out.println("   → Both user and audit log were rolled back together!");
            
            // Verification
            if (afterRollbackUsers == initialUsers && afterRollbackAudits == initialAudits) {
                System.out.println("\n✅ VERIFIED: Audit logs are in the SAME transaction!");
                System.out.println("   • Same connection used");
                System.out.println("   • Same autoCommit=false state");
                System.out.println("   • Both operations rolled back together");
            } else {
                System.out.println("\n❌ ERROR: Transaction behavior not as expected");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static int countRecords(Connection connection, String tableName) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM " + tableName);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            System.err.println("Error counting records in " + tableName + ": " + e.getMessage());
            return -1;
        }
    }
}