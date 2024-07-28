package org.example;

import models.data.DataQuery;
import models.record.RecordCenter;
import models.record.RecordCity;
import models.record.RecordOperator;
import models.record.RecordWeather;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class DataQueryTest {

    private static Connection connection;
    private static DataQuery dataQuery;

    @BeforeAll
    public static void setUp() throws SQLException, IOException {
        // Configurazione della connessione al database
        Properties props = new Properties();
        try (InputStream input = DataQueryTest.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find database.properties");
            }
            props.load(input);
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        connection = DriverManager.getConnection(url, username, password);
        dataQuery = new DataQuery(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private static void insertBaseData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Inserimento dati nella tabella coordinatemonitoraggio
            stmt.execute("INSERT INTO coordinatemonitoraggio (id, name, asciiname, countrycode, countryname, latitude, longitude) " +
                    "VALUES (1, 'Sample City', 'Sample AsciiName', 'SC', 'Sample Country', 10.0, 20.0)");

            // Inserimento dati nella tabella centrimonitoraggio
            Array array = connection.createArrayOf("INTEGER", new Integer[]{1});
            try (PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO centrimonitoraggio (id, centername, streetname, streetnumber, cap, townname, districtname, cityids) " +
                            "VALUES (1, 'Sample Center', 'Sample Street', '100', '10000', 'Sample Town', 'Sample District', ?)")) {
                pstmt.setArray(1, array);
                pstmt.executeUpdate();
            }

            // Inserimento dati nella tabella operatoriregistrati
            stmt.execute("INSERT INTO operatoriregistrati (id, namesurname, taxcode, email, username, password, centerid) " +
                    "VALUES (1, 'Sample Operator', 'S123456789', 'operator@example.com', 'operator', 'password', 1)");

            // Inserimento dati nella tabella parametriclimatici
            stmt.execute("INSERT INTO parametriclimatici (id, cityid, centerid, date, wind_score, wind_comment, humidity_score, humidity_comment, " +
                    "pressure_score, pressure_comment, temperature_score, temperature_comment, precipitation_score, precipitation_comment, " +
                    "glacierelevation_score, glacierelevation_comment, glaciermass_score, glaciermass_comment) " +
                    "VALUES (1, 1, 1, '2024-01-01', 5, 'Good', 6, 'Moderate', 7, 'High', 8, 'Very High', 9, 'Extreme', 10, 'Excellent', 11, 'Outstanding')");
        }
    }

    @BeforeEach
    public void setUpTest() throws SQLException {
        // Garantire che i dati di base siano presenti
        insertBaseData();
    }

    @AfterEach
    public void tearDownTest() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Pulizia dei dati dalle tabelle dopo ogni test
            stmt.execute("DELETE FROM parametriclimatici");
            stmt.execute("DELETE FROM operatoriregistrati");
            stmt.execute("DELETE FROM centrimonitoraggio");
            stmt.execute("DELETE FROM coordinatemonitoraggio");
        }
    }

    @Test
    public void testGetCityById() throws SQLException {
        int testId = 1;
        RecordCity city = dataQuery.getCityBy(testId);
        assertNotNull(city);
        assertEquals(testId, city.ID());
        assertEquals("Sample City", city.name());
    }

    @Test
    public void testGetCityByConditions() throws SQLException {
        DataQuery.QueryCondition condition = new DataQuery.QueryCondition("countrycode", "SC");
        RecordCity[] cities = dataQuery.getCityBy(condition);
        assertNotNull(cities);
        assertNotEquals(0, cities.length);
        assertEquals(1, cities.length);
        assertEquals("Sample City", cities[0].name());
    }

    @Test
    public void testGetOperatorById() throws SQLException {
        int testId = 1;
        RecordOperator operator = dataQuery.getOperatorBy(testId);
        assertNotNull(operator);
        assertEquals(testId, operator.ID());
        assertEquals("Sample Operator", operator.nameSurname());
    }

    @Test
    public void testGetOperatorByConditions() throws SQLException {
        DataQuery.QueryCondition condition = new DataQuery.QueryCondition("username", "operator");
        RecordOperator[] operators = dataQuery.getOperatorBy(condition);
        assertNotNull(operators);
        assertTrue(operators.length > 0);
        assertEquals("operator", operators[0].username());
    }

    @Test
    public void testGetCenterById() throws SQLException {
        int testId = 1;
        RecordCenter center = dataQuery.getCenterBy(testId);
        assertNotNull(center);
        assertEquals(testId, center.ID());
        assertEquals("Sample Center", center.centerName());
    }

    @Test
    public void testGetCenterByConditions() throws SQLException {
        DataQuery.QueryCondition condition = new DataQuery.QueryCondition("centername", "Sample Center");
        RecordCenter[] centers = dataQuery.getCenterBy(condition);
        assertNotNull(centers);
        assertTrue(centers.length > 0);
        assertEquals("Sample Center", centers[0].centerName());
    }

    @Test
    public void testGetWeatherById() throws SQLException {
        int testId = 1;
        RecordWeather weather = dataQuery.getWeatherBy(testId);
        assertNotNull(weather);
        assertEquals(testId, weather.ID());
        assertEquals("2024-01-01", weather.date());
    }

    @Test
    void testGetWeatherBy() throws SQLException {
        DataQuery.QueryCondition condition = new DataQuery.QueryCondition("date", java.sql.Date.valueOf("2024-01-01"));
        RecordWeather[] weathers = dataQuery.getWeatherBy(condition);
        assertNotNull(weathers);
        assertTrue(weathers.length > 0);
        assertEquals(1, weathers[0].ID());
    }

    // Test con condizioni multiple
    @Test
    public void testGetCityByMultipleConditions() throws SQLException {
        DataQuery.QueryCondition condition1 = new DataQuery.QueryCondition("countrycode", "SC");
        DataQuery.QueryCondition condition2 = new DataQuery.QueryCondition("name", "Sample City");
        List<DataQuery.QueryCondition> conditions = List.of(condition1, condition2);
        RecordCity[] cities = dataQuery.getCityBy(conditions);
        assertNotNull(cities);
        assertEquals(1, cities.length);
        assertEquals("Sample City", cities[0].name());
    }

    @Test
    public void testGetWeatherByMultipleConditions() throws SQLException {
        DataQuery.QueryCondition condition1 = new DataQuery.QueryCondition("date", java.sql.Date.valueOf("2024-01-01"));
        DataQuery.QueryCondition condition2 = new DataQuery.QueryCondition("wind_score", 5);
        List<DataQuery.QueryCondition> conditions = List.of(condition1, condition2);
        RecordWeather[] weathers = dataQuery.getWeatherBy(conditions);
        assertNotNull(weathers);
        assertEquals(1, weathers.length);
        assertEquals(1, weathers[0].ID());
    }

    // Test con dati non esistenti
    @Test
    public void testGetNonExistingCityById() throws SQLException {
        int testId = 999;
        assertThrows(SQLException.class, () -> dataQuery.getCityBy(testId));
    }

    @Test
    public void testGetNonExistingWeatherByConditions() throws SQLException {
        DataQuery.QueryCondition condition = new DataQuery.QueryCondition("date", java.sql.Date.valueOf("2099-01-01"));
        RecordWeather[] weathers = dataQuery.getWeatherBy(condition);
        assertNotNull(weathers);
        assertEquals(0, weathers.length);
    }

    // Test di inserimento di nuovi dati
    @Test
    public void testInsertNewCity() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO coordinatemonitoraggio (id, name, asciiname, countrycode, countryname, latitude, longitude) " +
                    "VALUES (2, 'New City', 'New AsciiName', 'NC', 'New Country', 30.0, 40.0)");
        }
        RecordCity city = dataQuery.getCityBy(2);
        assertNotNull(city);
        assertEquals(2, city.ID());
        assertEquals("New City", city.name());
    }

    // Test di aggiornamento di dati esistenti
    @Test
    public void testUpdateCityName() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("UPDATE coordinatemonitoraggio SET name = 'Updated City' WHERE id = 1");
        }
        RecordCity city = dataQuery.getCityBy(1);
        assertNotNull(city);
        assertEquals("Updated City", city.name());
    }
}
