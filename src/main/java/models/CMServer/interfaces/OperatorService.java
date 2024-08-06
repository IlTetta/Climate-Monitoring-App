package models.CMServer.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OperatorService extends Remote {

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

    void associateCenter(Integer centerID) throws RemoteException;

    boolean isValidNameSurname(String nameSurname) throws RemoteException;

    boolean isValidTaxCode(String taxCode) throws RemoteException;

    boolean isValidEmail(String email) throws RemoteException;

    boolean isValidUsername(String username) throws RemoteException;

    boolean isValidPassword(String password) throws RemoteException;
}
