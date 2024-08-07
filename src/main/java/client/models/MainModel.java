package client.models;

import client.models.logic.LogicCenter;
import client.models.logic.LogicCity;
import client.models.logic.LogicOperator;
import server.DataHandlerImp;

import shared.InterfacesRMI.DataHandlerInterface;
import shared.InterfacesRMI.DataQueryInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;

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
 * @see DataHandlerImp
 * @see LogicOperator
 * @see LogicCenter
 * @see LogicCity
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
    public DataHandlerInterface dataHandler;

    public DataQueryInterface dataQuery;

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

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            dataHandler = (DataHandlerInterface) registry.lookup("DataHandler");
            dataQuery = (DataQueryInterface) registry.lookup("DataQuery");
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        logicOperator = new LogicOperator(dataHandler, dataQuery);
        logicCenter = new LogicCenter(dataHandler, dataQuery);
        logicCity = new LogicCity(dataHandler);
    }
}
