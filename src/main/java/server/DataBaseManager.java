package server;

import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 * La classe {@code DataBaseManager} gestisce la connessione a un database utilizzando
 * le credenziali fornite o lette da un file di configurazione.
 * <p>
 * Questa classe fornisce metodi per creare una connessione al database tramite
 * parametri espliciti o utilizzando un file di properties.
 * </p>
 *
 * <p>
 * La connessione viene gestita tramite l'oggetto {@link Connection}, che può
 * essere recuperato per eseguire query e altre operazioni sul database.
 * </p>
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @see Connection
 * @see Properties
 * @see java.sql.DriverManager
 * @since 14/08/2024
 */
public class DataBaseManager {

    /**
     * La connessione al database gestita da questa classe
     */
    private Connection conn;


    /**
     * Costruisce un oggetto {@code DataBaseManager} utilizzando i parametri forniti.
     *
     * @param host     l'host del database a cui connettersi
     * @param password la password per l'autenticazione al database
     * @throws SQLException Se si verifica un errore durante la connessione al
     *                      database
     */
    public DataBaseManager(String host, String password) throws SQLException {
        conn = connectionMaker("jdbc:postgresql://" + host + ":5432/postgres", password);

        if(conn == null) {
            System.err.println("Errore durante la connessione al database. Ricontrollare le credenziali.");
            System.exit(1);
        }

        if (checkDatabaseExistence(conn)) {
            createDatabase(conn);
        }

        conn.close();

        conn = connectionMaker("jdbc:postgresql://" + host + ":5432/climatemonitoring", password);
    }

    /**
     * Crea una connessione al database utilizzando i parametri forniti.
     * <p>
     * Questo metodo è utilizzato internamente per creare l'oggetto {@link Connection}
     * con le proprietà fornite.
     * </p>
     *
     * @param url      l'URL del database a cui connettersi
     * @param password la password per l'autenticazione al database
     * @return Un oggetto {@link Connection} che rappresenta la connessione al
     * database
     * @throws SQLException Se si verifica un errore durante la connessione al
     *                      database
     */
    private Connection connectionMaker(String url, String password) throws SQLException {
        Properties props = new Properties();
        String username = "postgres";
        props.setProperty("user", username);
        props.setProperty("password", password);

        return java.sql.DriverManager.getConnection(url, props);
    }

    public boolean checkDatabaseExistence(Connection conn) throws SQLException {
        String query = "SELECT 1 FROM pg_database WHERE datname = 'climatemonitoring'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return !rs.next();
        }
    }

    public static boolean checkTableExistence(Connection conn, String tableName) throws SQLException {
        String query = "SELECT to_regclass('" + tableName + "')";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getObject(1) != null;
            }
        }
        return false;
    }

    public void createDatabase(Connection connection) throws SQLException {
        String query = "CREATE DATABASE climatemonitoring";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        }
    }

    public static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {

            // Creazione della tabella CoordinateMonitoraggio
            String sqlCoordinateMonitoraggio = "CREATE TABLE IF NOT EXISTS coordinatemonitoraggio (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "asciiname VARCHAR(100) NOT NULL, " +
                    "countrycode CHAR(2) NOT NULL, " +
                    "countryname VARCHAR(100) NOT NULL, " +
                    "latitude DECIMAL(9, 6) NOT NULL, " +
                    "longitude DECIMAL(9, 6) NOT NULL);";
            stmt.executeUpdate(sqlCoordinateMonitoraggio);

            // Assegnazione dei permessi alla tabella CoordinateMonitoraggio
            String grantCoordinateMonitoraggio = "GRANT SELECT ON coordinatemonitoraggio TO PUBLIC;";
            stmt.executeUpdate(grantCoordinateMonitoraggio);

            // Creazione della tabella CentriMonitoraggio
            String sqlCentriMonitoraggio = "CREATE TABLE IF NOT EXISTS centrimonitoraggio (" +
                    "id SERIAL PRIMARY KEY, " +
                    "centername VARCHAR(100) NOT NULL, " +
                    "streetname VARCHAR(100) NOT NULL, " +
                    "streetnumber VARCHAR(10) NOT NULL, " +
                    "cap VARCHAR(10) NOT NULL, " +
                    "townname VARCHAR(100) NOT NULL, " +
                    "districtname VARCHAR(100) NOT NULL, " +
                    "cityids INTEGER[]);";
            stmt.executeUpdate(sqlCentriMonitoraggio);

            // Creazione della tabella OperatoriRegistrati
            String sqlOperatoriRegistrati = "CREATE TABLE IF NOT EXISTS operatoriregistrati (" +
                    "id SERIAL PRIMARY KEY, " +
                    "namesurname VARCHAR(200) NOT NULL, " +
                    "taxcode VARCHAR(16) NOT NULL UNIQUE, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "centerid INTEGER, " +
                    "FOREIGN KEY (centerid) REFERENCES centrimonitoraggio(id));";
            stmt.executeUpdate(sqlOperatoriRegistrati);

            // Creazione della tabella ParametriClimatici
            String sqlParametriClimatici = "CREATE TABLE IF NOT EXISTS parametriclimatici (" +
                    "id SERIAL PRIMARY KEY, " +
                    "cityid INTEGER NOT NULL, " +
                    "centerid INTEGER NOT NULL, " +
                    "date DATE NOT NULL, " +
                    "windscore INTEGER, " +
                    "windcomment TEXT, " +
                    "humidityscore INTEGER, " +
                    "humiditycomment TEXT, " +
                    "pressurescore INTEGER, " +
                    "pressurecomment TEXT, " +
                    "temperaturescore INTEGER, " +
                    "temperaturecomment TEXT, " +
                    "precipitationscore INTEGER, " +
                    "precipitationcomment TEXT, " +
                    "glacierelevationscore INTEGER, " +
                    "glacierelevationcomment TEXT, " +
                    "glaciermassscore INTEGER, " +
                    "glaciermasscomment TEXT, " +
                    "FOREIGN KEY (cityid) REFERENCES coordinatemonitoraggio(id), " +
                    "FOREIGN KEY (centerid) REFERENCES centrimonitoraggio(id));";
            stmt.executeUpdate(sqlParametriClimatici);

            // Assegnazione dei permessi alla tabella ParametriClimatici
            String grantParametriclimatici = "GRANT SELECT ON parametriclimatici TO PUBLIC;";
            stmt.executeUpdate(grantParametriclimatici);

            System.out.println("Tabelle create con successo.");

        } catch (SQLException e) {
            System.err.println("Errore durante la creazione delle tabelle: " + e.getMessage());
        }
    }


    public static void populateCoordinateMonitoraggio(Connection conn, String csvFilePath) throws SQLException, IOException {
        String insertSquery = "INSERT INTO coordinatemonitoraggio (id, name, asciiname, countrycode, countryname, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (InputStream inputStream = DataBaseManager.class.getResourceAsStream(csvFilePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
             PreparedStatement pstmt = conn.prepareStatement(insertSquery)) {

            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] values = line.split(";");

                if (values.length < 6) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(values[0].trim());
                    String name = values[1].trim();
                    String asciiname = values[2].trim();
                    String countrycode = values[3].trim();
                    String countryname = values[4].trim();
                    String coordinates = values[5].trim();

                    String[] coords = coordinates.split(", ");
                    double latitude = Double.parseDouble(coords[0].trim());
                    double longitude = Double.parseDouble(coords[1].trim());

                    pstmt.setInt(1, id);
                    pstmt.setString(2, name);
                    pstmt.setString(3, asciiname);
                    pstmt.setString(4, countrycode);
                    pstmt.setString(5, countryname);
                    pstmt.setDouble(6, latitude);
                    pstmt.setDouble(7, longitude);

                    pstmt.addBatch();
                } catch (NumberFormatException e) {
                    System.err.println("Errore nel formato del numero: " + e.getMessage());
                } catch (SQLException e) {
                    System.err.println("Errore durante l'inserimento dei dati: " + e.getMessage());
                }
            }
            pstmt.executeBatch();
            System.out.println("Dati inseriti con successo.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Restituisce la connessione attualmente gestita da questa istanza di {@code DataBaseManager}.
     *
     * @return La connessione al database
     */
    public Connection getConnection() {
        return conn;
    }
}