package client.models;

import shared.InterfacesRMI.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * La classe {@code MainModel} è responsabile della connessione ai servizi RMI (Remote Method Invocation)
 * e del recupero delle interfacce remote. Queste interfacce permettono al client di comunicare con
 * il server ed eseguire operazioni remote come la gestione dei dati, le query sui dati e la logica operativa.
 * <p>
 *     Il costruttore della classe si occupa di localizzare il registro RMI e di effettuare il lookup delle
 *     interfacce remote. In caso di errore durante il processo di lookup, viene lanciata una RuntimeException.
 * </p>
 *
 * @see DataHandlerInterface
 * @see DataQueryInterface
 * @see LogicOperatorInterface
 * @see LogicCenterInterface
 * @see LogicCityInterface
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.1
 * @since 11/08/2024
 */
public class MainModel {

    /**
     * Interfaccia remota per la gestione dei dati.
     */
    public DataHandlerInterface dataHandler;

    /**
     * Interfaccia remota per le query sui dati.
     */
    public DataQueryInterface dataQuery;

    /**
     * Interfaccia remota per la logica degli operatori.
     */
    public LogicOperatorInterface logicOperator;

    /**
     * Interfaccia remota per la logica dei centri di monitoraggio.
     */
    public LogicCenterInterface logicCenter;

    /**
     * Interfaccia remota per la logica delle città.
     */
    public LogicCityInterface logicCity;

    /**
     * Costruttore della classe {@code MainModel}.
     * <p>
     *     Questo costruttore localizza il registro RMI sulla macchina locale e la porta 1099, ed
     *     effettua il lookup delle interfacce remote necessarie per il funzionamento del client.
     *     Se il processo di lookup fallisce, viene lanciata una {@link RuntimeException}.
     * </p>
     *
     * @throws RuntimeException se il processo di lookup delle interfacce remote fallisce.
     */
    public MainModel() {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            dataHandler = (DataHandlerInterface) registry.lookup("DataHandler");
            dataQuery = (DataQueryInterface) registry.lookup("DataQuery");
            logicOperator = (LogicOperatorInterface) registry.lookup("LogicOperator");
            logicCenter = (LogicCenterInterface) registry.lookup("LogicCenter");
            logicCity = (LogicCityInterface) registry.lookup("LogicCity");
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
