package models.CMServer.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CMService extends Remote {

    void performLogin(
            String username,
            String password) throws RemoteException;

    void performRegistration(
            String nameSurname,
            String taxCode,
            String email,
            String username,
            String password,
            Integer centerID) throws RemoteException;

    void addData() throws RemoteException;
    void getData() throws RemoteException;
    void updateData() throws RemoteException;
    void crerateCenter() throws RemoteException;

}
