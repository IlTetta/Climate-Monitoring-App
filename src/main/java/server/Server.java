package server;

import server.ImplementationRMI.*;
import shared.interfacesRMI.*;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    private static Connection conn;
    private static final String csvFilePath = "/geonames-and-coordinates.CSV";
    private static final int TIMEOUT_MINUTES = 5;
    private static ScheduledExecutorService executor;

    public static void main(String[] args) {

        try {
            if (args.length == 2) {
                conn = new DataBaseManager(args[0], args[1]).getConnection();

                if (!DataBaseManager.checkTableExistence(conn, "coordinatemonitoraggio")) {
                    DataBaseManager.createTables(conn);
                    DataBaseManager.populateCoordinateMonitoraggio(conn, csvFilePath);
                }

            } else {
                System.out.println("Utilizzo: java -jar Server.jar [host password]");
                System.exit(1);
            }

            DataQueryInterface dataQuery = new DataQueryImp(conn);
            DataHandlerInterface dataHandler = new DataHandlerImp(dataQuery);
            LogicOperatorInterface logicOperator = new LogicOperatorImp(dataHandler, dataQuery);
            LogicCenterInterface logicCenter = new LogicCenterImp(dataHandler, dataQuery);
            LogicCityInterface logicCity = new LogicCityImp();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("DataQuery", dataQuery);
            registry.rebind("DataHandler", dataHandler);
            registry.rebind("LogicOperator", logicOperator);
            registry.rebind("LogicCenter", logicCenter);
            registry.rebind("LogicCity", logicCity);

            System.out.println("Server avviato con successo");

            startInactivityTimer();

        } catch (RemoteException e) {
            System.err.println("Si è verificata una RemoteException: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Si è verificato un SQLException: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Si è verificata una IOException: " + e.getMessage());
        }
    }

    /**
     * Avvia un timer per la chiusura del server in caso di inattività.
     */
    private static void startInactivityTimer() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(Server::shutdown, TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Resetta il timer di inattività del server.
     */
    public static void resetInactivityTimer() {
        if (executor != null) {
            executor.shutdownNow();
        }
        startInactivityTimer();
    }

    /**
     * Chiude la connessione al database e termina il server.
     */
    private static void shutdown() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connessione al database chiusa.");
            }
            System.out.println("Server inattivo da troppo tempo, terminazione in corso...");
            System.exit(0);
        } catch (SQLException e) {
            System.err.println("Errore durante la chiusura della connessione al database: " + e.getMessage());
        }
    }
}
