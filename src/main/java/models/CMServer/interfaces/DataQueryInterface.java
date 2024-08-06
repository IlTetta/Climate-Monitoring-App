package models.CMServer.interfaces;

import models.record.RecordCenter;
import models.record.RecordCity;
import models.record.RecordOperator;
import models.record.RecordWeather;
import utils.QueryCondition;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface DataQueryInterface extends Remote {
    RecordCity getCityBy(Integer ID) throws SQLException, RemoteException;

    RecordCity[] getCityBy(List<QueryCondition> conditions) throws SQLException, RemoteException;

    RecordOperator[] getOperatorBy(QueryCondition condition) throws SQLException, RemoteException;

    RecordOperator[] getOperatorBy(List<QueryCondition> conditions) throws SQLException, RemoteException;

    RecordCenter getCenterBy(Integer ID) throws SQLException, RemoteException;

    RecordCenter[] getCenters() throws SQLException, RemoteException;

    RecordWeather[] getWeatherBy(QueryCondition condition) throws SQLException, RemoteException;

    RecordWeather[] getWeatherBy(List<QueryCondition> conditions) throws SQLException, RemoteException;
}
