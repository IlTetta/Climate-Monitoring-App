package server;

import server.ImplementationRMI.*;
import shared.InterfacesRMI.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;
import java.net.MalformedURLException;

/**
 * Classe principale per l'avvio del server RMI.
 */
public class Server {
    public static void main(String[] args) {
        try {
            // Inizializzazione delle interfacce e delle loro implementazioni
            DataQueryInterface dataQuery = new DataQueryImp();
            DataHandlerInterface dataHandler = new DataHandlerImp(dataQuery);
            LogicOperatorInterface logicOperator = new LogicOperatorImp(dataHandler, dataQuery);
            LogicCenterInterface logicCenter = new LogicCenterImp(dataHandler, dataQuery);
            LogicCityInterface logicCity = new LogicCityImp();

            // Creazione del registro RMI sulla porta 1099
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
        } catch (Exception e) {
            System.err.println("Si è verificato un errore imprevisto: " + e.getMessage());
        }
    }
}
