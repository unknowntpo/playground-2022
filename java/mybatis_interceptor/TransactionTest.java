import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mapper.UserMapper;
import org.example.model.User;

import java.io.InputStream;

public class TransactionTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== Transaction Behavior Test ===");
            
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            
            testTransactionRollback(sqlSessionFactory);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void testTransactionRollback(SqlSessionFactory sqlSessionFactory) {
        System.out.println("\n--- Testing Transaction Rollback Behavior ---");
        
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            System.out.println("1. Inserting user (NOT committed yet)...");
            User user = new User("test_rollback_user", "rollback@example.com", "Test", "Rollback");
            userMapper.insert(user);
            System.out.println("   User inserted with ID: " + user.getId());
            
            System.out.println("2. Rolling back transaction...");
            session.rollback();
            System.out.println("   Transaction rolled back");
            
            System.out.println("3. Testing if rollback affected both user and audit log...");
            
        } catch (Exception e) {
            System.err.println("Error during transaction test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}