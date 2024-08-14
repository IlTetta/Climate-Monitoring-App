package server;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * La classe {@code ConnectionMaker} gestisce la connessione a un database utilizzando
 * le credenziali fornite o lette da un file di configurazione.
 * <p>
 *     Questa classe fornisce metodi per creare una connessione al database tramite
 *     parametri espliciti o utilizzando un file di properties.
 * </p>
 *
 * <p>
 *     La connessione viene gestita tramite l'oggetto {@link Connection}, che può
 *     essere recuperato per eseguire query e altre operazioni sul database.
 * </p>
 *
 * @see Connection
 * @see Properties
 * @see java.sql.DriverManager
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @since 14/08/2024
 */
public class ConnectionMaker {

    /**
     * La connessione al database gestita da questa classe
     */
    private final Connection conn;

    /**
     * Costruisce un oggetto {@code ConnectionMaker} utilizzando i parametri forniti.
     * <p>
     *     La connessione al database viene creata utilizzando lURL, il nome utente e
     *     la password specificati.
     * </p>
     *
     * @param url l'URL del database a cui connettersi
     * @param username il nome utente per l'autenticazione al database
     * @param password la password per l'autenticazione al database
     * @throws SQLException Se si verifica un errore durante la connessione al
     *                      database
     */
    public ConnectionMaker(String url, String username, String password) throws SQLException {
    conn= connectionMaker(url,username,password);
        System.out.println("Connesso al database "+conn.getCatalog());
    }

    /**
     * Crea e restituisce un'istanza di {@code ConnectionMaker} utilizzando un file di
     * properties.
     * <p>
     *     Il file di properties deve contenere le chiavi {@code db.url}, {@code db.username},
     *     e {@code db.password} per configurare la connessione al database.
     * </p>
     *
     * @param filepath Il percorso del file di properties
     * @return Un'istanza di {@code ConnectionMaker} configurata con i parametri del file
     * @throws SQLException Se si verifica un errore durante la connessione al
     *                     database
     * @throws IOException Se si verifica un errore durante la lettura del file di
     *                    properties
     */
    public static ConnectionMaker createFromProperties(String filepath) throws SQLException, IOException {

        Properties props = new Properties();
        try (InputStream input = ConnectionMaker.class.getClassLoader().getResourceAsStream(filepath)) {
            if (input == null) {
                throw new RuntimeException("File " + filepath + " non trovato");
            }
            props.load(input);
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        return new ConnectionMaker(url, username, password);
    }

    /**
     * Crea una connessione al database utilizzando i parametri forniti.
     * <p>
     *     Questo metodo è utilizzato internamente per creare l'oggetto {@link Connection}
     *     con le proprietà fornite.
     * </p>
     *
     * @param url l'URL del database a cui connettersi
     * @param username il nome utente per l'autenticazione al database
     * @param password  la password per l'autenticazione al database
     * @return Un oggetto {@link Connection} che rappresenta la connessione al
     *        database
     * @throws SQLException Se si verifica un errore durante la connessione al
     *                    database
     */
    private Connection connectionMaker(String url, String username, String password) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        return java.sql.DriverManager.getConnection(url, props);
    }

    /**
     * Restituisce la connessione attualmente gestita da questa istanza di {@code ConnectionMaker}.
     *
     * @return La connessione al database
     */
    public Connection getConnection() {
        return conn;
    }
}