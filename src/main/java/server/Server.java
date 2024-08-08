package server;

import server.ImplementationRMI.*;
import shared.InterfacesRMI.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            DataQueryInterface dataQuery = new DataQueryImp();
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
            System.out.println("Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
