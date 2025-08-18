package org.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        try {
            DatabaseManager dbManager = new DatabaseManager();

            Record record1 = new Record("Sample Record", "This is a sample record");
            Record record2 = new Record("Another Record", "This is another record");

            System.out.println("Saving records...");
            dbManager.save(record1);
            dbManager.save(record2);

            System.out.println("Retrieving all records:");
            List<Record> allRecords = dbManager.findAll();
            for (Record record : allRecords) {
                System.out.println(record);
            }

            System.out.println("Finding record by ID 1:");
            Record foundRecord = dbManager.findById(1L);
            if (foundRecord != null) {
                System.out.println(foundRecord);
            }

            dbManager.close();

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
