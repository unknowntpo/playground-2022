package org.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class TestDatabaseSetup {
    
    private static SqlSessionFactory sqlSessionFactory;
    
    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            try {
                InputStream inputStream = Resources.getResourceAsStream("mybatis-config-test.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create SqlSessionFactory", e);
            }
        }
        return sqlSessionFactory;
    }
    
    public static void clearChangeEvents() {
        try (SqlSession session = getSqlSessionFactory().openSession()) {
            session.getConnection().createStatement().execute("DELETE FROM change_event");
            session.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear change_event table", e);
        }
    }
    
    public static void clearTestData() {
        try (SqlSession session = getSqlSessionFactory().openSession()) {
            var stmt = session.getConnection().createStatement();
            stmt.execute("DELETE FROM user WHERE username LIKE 'test_%' OR username LIKE '%_user' OR username LIKE 'integration_%'");
            stmt.execute("DELETE FROM product WHERE name LIKE 'Test %' OR name LIKE '%Product%'");
            session.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear test data", e);
        }
    }
    
    public static int getChangeEventCount() {
        try (SqlSession session = getSqlSessionFactory().openSession()) {
            var stmt = session.getConnection().createStatement();
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM change_event");
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get change_event count", e);
        }
    }
}