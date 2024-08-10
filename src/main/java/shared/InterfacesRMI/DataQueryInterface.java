package shared.InterfacesRMI;

import shared.record.RecordCenter;
import shared.record.RecordCity;
import shared.record.RecordOperator;
import shared.record.RecordWeather;
import shared.record.QueryCondition;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DataQueryInterface extends Remote {
    RecordCity getCityBy(Integer ID) throws SQLException, RemoteException;

    RecordCity[] getCityBy(List<QueryCondition> conditions) throws SQLException, RemoteException;

    RecordOperator getOperatorBy(Integer ID) throws SQLException, RemoteException;

    RecordOperator[] getOperatorBy(QueryCondition condition) throws SQLException, RemoteException;

    RecordOperator[] getOperatorBy(List<QueryCondition> conditions) throws SQLException, RemoteException;

    RecordCenter getCenterBy(Integer ID) throws SQLException, RemoteException;

    RecordCenter[] getCenters() throws SQLException, RemoteException;

    RecordWeather[] getWeatherBy(QueryCondition condition) throws SQLException, RemoteException;

    RecordWeather[] getWeatherBy(List<QueryCondition> conditions) throws SQLException, RemoteException;

    Connection getConn() throws RemoteException;
}
