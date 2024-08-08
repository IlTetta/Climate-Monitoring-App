package server;

import server.ImplementationRMI.DataHandlerImp;
import server.ImplementationRMI.DataQueryImp;
import server.ImplementationRMI.LogicOperatorImp;
import shared.InterfacesRMI.DataHandlerInterface;
import shared.InterfacesRMI.DataQueryInterface;
import shared.InterfacesRMI.LogicOperatorInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            DataQueryInterface dataQuery = new DataQueryImp();
            DataHandlerInterface dataHandler = new DataHandlerImp(dataQuery);
            LogicOperatorInterface logicOperator = new LogicOperatorImp(dataHandler, dataQuery);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("DataQuery", dataQuery);
            registry.rebind("DataHandler", dataHandler);
            registry.rebind("LogicOperator", logicOperator);
            System.out.println("Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
