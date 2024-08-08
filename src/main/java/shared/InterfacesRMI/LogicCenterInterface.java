package shared.InterfacesRMI;

import shared.record.RecordCenter;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface LogicCenterInterface extends Remote {
    void initNewCenter(
            String centerName,
            String streetName,
            String streetNumber,
            String CAP,
            String townName,
            String districtName,
            Integer[] cityIDs,
            Integer operatorID) throws SQLException, RemoteException;

    void addDataToCenter(
            Integer cityID,
            Integer operatorID,
            String date,
            Object[][] tableData) throws SQLException, RemoteException;
}
