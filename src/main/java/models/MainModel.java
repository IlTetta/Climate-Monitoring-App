package models;

import models.CMServer.DBConnection.QueryToDB;
import models.data.DataHandler;

import models.logic.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * La classe {@code MainModel} rappresenta il modello principale
 * dell'applicazione.
 * <p>
 * Questa classe gestisce l'accesso ai dati, la logica pertinente agli
 * operatori, ai centri di monitoraggio
 * e alle citt&agrave; e la gestione dei file.
 * </p>
 * <p>
 * E' un componente centrale nell'architettura MVC dell'applicazione.
 * </p>
 * 
 * @see models.data.DataHandler
 * @see models.logic.LogicOperator
 * @see models.logic.LogicCenter
 * @see models.logic.LogicCity
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class MainModel {

    /**
     * Gestisce l'accesso e la manipolazione dei dati nell'applicazione.
     */
    public DataHandler data;

    /**
     * Gestisce la logica specifica dell'operatore.
     */
    public LogicOperator logicOperator;

    /**
     * Gestisce la logica specifica del centro di monitoraggio.
     */
    public LogicCenter logicCenter;

    /**
     * Gestisce la logica specifica della citt&agrave;.
     */
    public LogicCity logicCity;

    private Connection conn;

    /**
     * Costruttore della classe {@code MainModel}.
     * <p>
     * Inizializza le varie componenti del modello, inclusi i gestori dei file, dei
     * dati
     * e le istanze delle classi di logica.
     * </p>
     */
    public MainModel() {
        //bisogna collegarsi al registry che contiene il riferimento al database
        //bisogna creare un oggetto QueryToDB
        //conn = QueryToDB.createFromProperties("db.properties").getConnection();
        //data = new DataHandler(conn);
        try {
            data = new DataHandler(QueryToDB.createFromProperties("database.properties").getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logicOperator = new LogicOperator(data);
        logicCenter = new LogicCenter(data);
        logicCity = new LogicCity(data);
    }
}
