package org.example;

import models.CMServer.DBConnection.ConnToDB;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;


public class QueryToDBTest {

    private static final String TEST_PROPERTIES_FILE = "database.properties";

    @BeforeAll
    public static void setup() {
        // Non è necessario creare il file di properties se è già nella directory resources
    }

    @AfterAll
    public static void teardown() {
        // Non è necessario eliminare il file di properties se è nella directory resources
    }

    @Test
    public void testConnectionCreation() {
        assertDoesNotThrow(() -> {
            ConnToDB db = new ConnToDB("jdbc:postgresql://localhost/climateMonitoring", "postgres", "3s7k2a1m6e");
            assertNotNull(db);
            System.out.println("Test di creazione connessione riuscito.");
        });
    }

    @Test
    public void testCreateFromProperties() {
        assertDoesNotThrow(() -> {
            ConnToDB db = ConnToDB.createFromProperties(TEST_PROPERTIES_FILE);
            assertNotNull(db);
            System.out.println("Test di creazione da file properties riuscito.");
        });
    }

    @Test
    public void testInvalidUrl() {
        assertThrows(SQLException.class, () -> {
            new ConnToDB("invalid-url", "postgres", "3s7k2a1m6e");
        });
    }

    @Test
    public void testInvalidCredentials() {
        assertThrows(SQLException.class, () -> {
            new ConnToDB("jdbc:mysql://localhost:3306/testdb", "wronguser", "wrongpassword");
        });
    }

    @Test
    public void testInvalidPropertiesFile() {
        assertThrows(RuntimeException.class, () -> {
            ConnToDB.createFromProperties("nonexistentfile.properties");
        });
    }

    @Test
    public void testGetCatalog() {
        assertDoesNotThrow(() -> {
            ConnToDB db = new ConnToDB("jdbc:postgresql://localhost/climateMonitoring", "postgres", "3s7k2a1m6e");
            Connection conn = db.getConnection();
            assertEquals("climateMonitoring", conn.getCatalog());
            System.out.println("Test di recupero catalogo riuscito.");
        });
    }

    // Helper method to get the file path from resources
    // Modifica del metodo per utilizzare il ClassLoader
    private String getResourceFilePath(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IOException("File not found: " + fileName);
        } else {
            return resource.getPath();
        }
    }

}