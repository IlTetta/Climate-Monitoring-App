package shared.InterfacesRMI;

import shared.record.RecordOperator;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface LogicOperatorInterface extends Remote {
    RecordOperator performLogin(String username, String password) throws SQLException, RemoteException;

    void performRegistration(String nameSurname,
                             String taxCode,
                             String email,
                             String username,
                             String password,
                             Integer centerID) throws SQLException, RemoteException;

    RecordOperator associateCenter(Integer operatorID, Integer centerID) throws SQLException, RemoteException;
}
