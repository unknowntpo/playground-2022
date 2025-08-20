package org.example;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DatabaseManager {
    public String DB_FILE_PATH;
    private Logger LOG = LoggerFactory.getLogger(DatabaseManager.class);
    private String DB_URL;
    private static final String DB_USER = "h2";
    private static final String DB_PASSWORD = "h2";

    private Connection connection;

    public DatabaseManager() throws SQLException, IOException {
        initializeDatabase();
    }

    public DatabaseManager(String dbFileName) throws SQLException, IOException {
        initializeDatabase(Optional.of(dbFileName));
    }

    private void initializeDatabase() throws SQLException, IOException {
        initializeDatabase(Optional.empty());
    }

    private void initializeDatabase(Optional<String> dbFileNameOpt) throws SQLException, IOException {
        setDbFilePathAndDbUrl(dbFileNameOpt);
        // Set max compact time to 10ms
        System.setProperty("h2.maxCompactTime", "10");
        this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        LOG.info("connection established, DB_FILE_PATH: {}, DB_URL: {}", DB_FILE_PATH, DB_URL);

        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS record (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    description TEXT
                )
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    private void setDbFilePathAndDbUrl() throws IOException {

    }

    private void setDbFilePathAndDbUrl(Optional<String> existedDbFilePath) throws IOException {
        String dbFilePath;
        File dir = new File("/tmp/h2_tmp");
        if (existedDbFilePath.isEmpty()) {
            dir.mkdir();
            dbFilePath = dir.getAbsolutePath() + "/" + UUID.randomUUID() + ".mv.db";
        } else {
            dbFilePath = existedDbFilePath.get();
        }

        this.DB_FILE_PATH = dbFilePath;
        LOG.info("Database will be created at: {}", DB_FILE_PATH);
        var pathNoSuffix = dbFilePath.substring(0, dbFilePath.lastIndexOf(".mv.db"));
        this.DB_URL = String.format("jdbc:h2:file:%s;DB_CLOSE_DELAY=-1", pathNoSuffix);
    }

    public Record save(Record record) throws SQLException {
        if (record.getId() == null) {
            return insert(record);
        } else {
            return update(record);
        }
    }

    private Record insert(Record record) throws SQLException {
        String sql = "INSERT INTO record (name, description) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, record.getName());
            pstmt.setString(2, record.getDescription());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating record failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating record failed, no ID obtained.");
                }
            }
        }

        return record;
    }

    private Record update(Record record) throws SQLException {
        String sql = "UPDATE record SET name = ?, description = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, record.getName());
            pstmt.setString(2, record.getDescription());
            pstmt.setLong(3, record.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating record failed, no rows affected.");
            }
        }

        return record;
    }

    public Record findById(Long id) throws SQLException {
        String sql = "SELECT id, name, description FROM record WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Record(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    );
                }
            }
        }

        return null;
    }

    public List<Record> findAll() throws SQLException {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT id, name, description FROM record";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(new Record(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        }

        return records;
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM record WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Long getDbFileSize() {
        var f = new File(DB_FILE_PATH);
        return f.length();
    }

    public void shutdown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SHUTDOWN");
        }
    }

    public void shutdownCompact() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SHUTDOWN COMPACT");
        }
    }
}
