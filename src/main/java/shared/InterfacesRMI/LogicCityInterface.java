package shared.InterfacesRMI;

import server.ImplementationRMI.LogicCityImp;
import shared.record.RecordWeather;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LogicCityInterface extends Remote {
    LogicCityImp.WeatherTableData getWeatherTableData(RecordWeather[] weatherRecords) throws RemoteException;
}
