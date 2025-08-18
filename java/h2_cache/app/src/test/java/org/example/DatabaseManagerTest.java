package org.example;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {
    private DatabaseManager dbManager;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        dbManager = new DatabaseManager();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (dbManager != null) {
            dbManager.close();
        }
    }

    @Test
    void testSaveAndFindRecord() throws SQLException {
        Record record = new Record("Test Record", "Test Description");

        Record savedRecord = dbManager.save(record);

        assertNotNull(savedRecord.getId());
        assertEquals("Test Record", savedRecord.getName());
        assertEquals("Test Description", savedRecord.getDescription());

        Record foundRecord = dbManager.findById(savedRecord.getId());
        assertNotNull(foundRecord);
        assertEquals(savedRecord.getId(), foundRecord.getId());
        assertEquals("Test Record", foundRecord.getName());
        assertEquals("Test Description", foundRecord.getDescription());
    }

    @Test
    void testFindAll() throws SQLException {
        Record record1 = new Record("Record 1", "Description 1");
        Record record2 = new Record("Record 2", "Description 2");

        dbManager.save(record1);
        dbManager.save(record2);

        List<Record> allRecords = dbManager.findAll();

        assertEquals(2, allRecords.size());
        assertTrue(allRecords.stream().anyMatch(r -> "Record 1".equals(r.getName())));
        assertTrue(allRecords.stream().anyMatch(r -> "Record 2".equals(r.getName())));
    }

    @Test
    void testUpdateRecord() throws SQLException {
        Record record = new Record("Original Name", "Original Description");
        Record savedRecord = dbManager.save(record);

        savedRecord.setName("Updated Name");
        savedRecord.setDescription("Updated Description");

        Record updatedRecord = dbManager.save(savedRecord);

        assertEquals(savedRecord.getId(), updatedRecord.getId());
        assertEquals("Updated Name", updatedRecord.getName());
        assertEquals("Updated Description", updatedRecord.getDescription());

        Record foundRecord = dbManager.findById(savedRecord.getId());
        assertEquals("Updated Name", foundRecord.getName());
        assertEquals("Updated Description", foundRecord.getDescription());
    }

    @Test
    void testDeleteRecord() throws SQLException {
        Record record = new Record("To Delete", "Will be deleted");
        Record savedRecord = dbManager.save(record);

        assertNotNull(dbManager.findById(savedRecord.getId()));

        dbManager.deleteById(savedRecord.getId());

        assertNull(dbManager.findById(savedRecord.getId()));
    }

    @Test
    void testFindByIdNotFound() throws SQLException {
        Record notFound = dbManager.findById(999L);
        assertNull(notFound);
    }

    @Test
    void testRecordToString() {
        Record record = new Record(1L, "Test", "Description");
        String expected = "Record{id=1, name='Test', description='Description'}";
        assertEquals(expected, record.toString());
    }

    @Test
    void testGetDbFileSize() throws SQLException, InterruptedException {
        for (int i = 0; i < 10_000; i++) {
            Record record = new Record("Original Name", "Original Description" + UUID.randomUUID());
            dbManager.save(record);
        }

        Thread.sleep(5_000);
        Assertions.assertEquals(100, dbManager.getDbFileSize());
    }
}
