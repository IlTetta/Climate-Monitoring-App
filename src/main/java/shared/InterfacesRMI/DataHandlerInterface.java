package shared.InterfacesRMI;

import shared.record.RecordCenter;
import shared.record.RecordOperator;
import shared.record.RecordWeather;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface DataHandlerInterface extends Remote {
    //aggiunge un nuovo operatore al database
    RecordOperator addNewOperator(String nameSurname,
                                  String taxCode,
                                  String email,
                                  String username,
                                  String password,
                                  Integer centerID) throws SQLException, RemoteException;

    //aggiunge un nuovo centro al database
    RecordCenter addNewCenter(String centerName,
                              String streetName,
                              String streetNumber,
                              String CAP,
                              String townName,
                              String districtName,
                              Integer[] cityIDs) throws SQLException, RemoteException;

    //aggiunge nuovi parametri climatici al database
    void addNewWeather(Integer cityID,
                       Integer centerID,
                       String date,
                       RecordWeather.WeatherData wind,
                       RecordWeather.WeatherData humidity,
                       RecordWeather.WeatherData pressure,
                       RecordWeather.WeatherData temperature,
                       RecordWeather.WeatherData precipitation,
                       RecordWeather.WeatherData glacierElevation,
                       RecordWeather.WeatherData glacierMass) throws SQLException, RemoteException;

    //aggiorna un RecordOperator nel sistema
    void updateOperator(RecordOperator operator) throws SQLException, RemoteException;
}
