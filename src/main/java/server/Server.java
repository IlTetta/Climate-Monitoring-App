package server;

import server.ImplementationRMI.*;
import shared.interfacesRMI.*;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * La classe {@code Server} rappresenta il server principale che avvia e registra
 * le implementazioni RMI per i vari servizi offerti dall'applicazione.
 * <p>
 * Questo server avvia il registro RMI e pubblica le implementazioni delle
 * interfacce remote in modo che possano essere invocate dai client remoti.
 * </p>
 *
 * <p>
 * Le interfacce implementate e registrate includono:
 *     <ul>
 *         <li>{@link DataQueryInterface} per l'interrogazione dei dati.</li>
 *         <li>{@link DataHandlerInterface} per la gestione dei dati.</li>
 *         <li>{@link LogicOperatorInterface} per la logica di business degli operatori.</li>
 *         <li>{@link LogicCenterInterface} per la logica di business dei centri.</li>
 *         <li>{@link LogicCityInterface} per la logica di business delle città.</li>
 *     </ul>
 * </p>
 *
 * <p>
 *     Una volta avviato con successo, il server stampa un messaggio di conferma
 *     sulla console.
 * </p>
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @see DataQueryInterface
 * @see DataHandlerInterface
 * @see LogicOperatorInterface
 * @see LogicCenterInterface
 * @see LogicCityInterface
 * @see DataQueryImp
 * @see DataHandlerImp
 * @see LogicOperatorImp
 * @see LogicCenterImp
 * @see LogicCityImp
 * @since 14/08/2024
 */
public class Server {

    /**
     * La connessione al database.
     */
    private static Connection conn;

    /**
     * Il percorso del file CSV contenente i dati da popolare nel database.
     */
    private static final String csvFilePath = "/geonames-and-coordinates.CSV";

    /**
     * Il metodo principale per avviare il server.
     * <p>
     * Questo metodo prende in input gli argomenti della riga di comando e, se
     * presenti, crea una connessione al database e verifica la presenza delle
     * tabelle necessarie. In caso di assenza, le crea e popola con i dati
     * presenti nel file CSV. Inoltre crea le istanze delle implementazioni RMI e le registra nel
     * registro RMI. In caso di successo, il server sarà in ascolto sulle
     * richieste dei client remoti. In caso di errore, vengono gestite le
     * eccezioni e viene stampato un messaggio di errore sulla console.
     * </p>
     *
     * @param args Argomenti della riga di comando (non utilizzati).
     */
    public static void main(String[] args) {

        args = new String[]{"host", "password"};
        try {
            if (args.length == 2) {

                conn = new DataBaseManager(args[0], args[1]).getConnection();

                if(!DataBaseManager.checkTableExistence(conn, "coordinatemonitoraggio")) {
                    DataBaseManager.createTables(conn);
                    DataBaseManager.populateCoordinateMonitoraggio(conn, csvFilePath);
                }

            } else {
                System.err.println("Utilizzo: java -jar Server.jar [host password]");
                System.exit(1);
            }


            DataQueryInterface dataQuery = new DataQueryImp(conn);
            DataHandlerInterface dataHandler = new DataHandlerImp(dataQuery);
            LogicOperatorInterface logicOperator = new LogicOperatorImp(dataHandler, dataQuery);
            LogicCenterInterface logicCenter = new LogicCenterImp(dataHandler, dataQuery);
            LogicCityInterface logicCity = new LogicCityImp();

            Registry registry = LocateRegistry.createRegistry(1099);

            // Registrazione delle implementazioni nel registro RMI
            registry.rebind("DataQuery", dataQuery);
            registry.rebind("DataHandler", dataHandler);
            registry.rebind("LogicOperator", logicOperator);
            registry.rebind("LogicCenter", logicCenter);
            registry.rebind("LogicCity", logicCity);

            System.out.println("Server avviato con successo");

        } catch (RemoteException e) {
            System.err.println("Si è verificata una RemoteException: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Si è verificato un SQLException: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Si è verificata una IOException: " + e.getMessage());
        }
    }
}
