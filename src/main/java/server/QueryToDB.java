package server;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

//Classe che si connette al database
public class QueryToDB {

    private final Connection conn;

    //Costruisce un oggetto QueryToDB con i parametri passati
    public QueryToDB(String url, String username, String password) throws SQLException {
    conn= connectionMaker(url,username,password);
        System.out.println("Connesso al database "+conn.getCatalog());
    }

    //ritorna un'istanza di QueryToDB da un file di properties
    public static QueryToDB createFromProperties(String filepath) throws SQLException, IOException {

        Properties props = new Properties();
        try (InputStream input = QueryToDB.class.getClassLoader().getResourceAsStream(filepath)) {
            if (input == null) {
                throw new RuntimeException("File " + filepath + " non trovato");
            }
            props.load(input);
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        return new QueryToDB(url, username, password);
    }
    private Connection connectionMaker(String url, String username, String password) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        return java.sql.DriverManager.getConnection(url, props);
    }

    public Connection getConnection() {
        return conn;
    }
}