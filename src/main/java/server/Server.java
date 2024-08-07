package server;

import shared.InterfacesRMI.DataHandlerInterface;
import shared.InterfacesRMI.DataQueryInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            DataQueryInterface dataQuery = new DataQueryImp();
            DataHandlerInterface dataHandler = new DataHandlerImp();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("DataQuery", dataQuery);
            registry.rebind("DataHandler", dataHandler);
            System.out.println("Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
