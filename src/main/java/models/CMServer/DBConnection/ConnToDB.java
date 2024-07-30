package models.CMServer.DBConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

//Classe che si connette al database
public class ConnToDB {

    private Connection conn;

    //Costruisce un oggetto ConnToDB con i parametri passati
    public ConnToDB(String url, String username, String password) throws SQLException {
    conn=Conncetionmaker(url,username,password);
        System.out.println("Connesso al database "+conn.getCatalog());
    }

    //ritorna un'istanza di ConnToDB da un file di properties
    public static ConnToDB createFromProperties(String filepath) throws SQLException, IOException {

        Properties props = new Properties();
        try (InputStream input = ConnToDB.class.getClassLoader().getResourceAsStream(filepath)) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find " + filepath);
            }
            props.load(input);
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        return new ConnToDB(url, username, password);
    }
    private Connection Conncetionmaker(String url, String username, String password) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        return java.sql.DriverManager.getConnection(url, props);
    }

    public Connection getConnection() {
        return conn;
    }
}