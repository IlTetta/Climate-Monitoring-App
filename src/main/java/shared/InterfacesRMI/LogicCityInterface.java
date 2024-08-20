package shared.interfacesRMI;

import server.ImplementationRMI.LogicCityImp;
import shared.record.RecordWeather;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * L'interfaccia remota {@code LogicCityInterface} è utilizzata per esporre i metodi
 * che permettono di ottenere i dati relativi alle città, in particolare i dati
 * metereologici.
 * <p>
 *     Questa interfaccia estende l'interfaccia {@link Remote} e dichiara un solo metodo
 *     {@link #getWeatherTableData(RecordWeather[])} che permette di ottenere i dati
 *     metereologici di una città.
 * </p>
 *
 * @see Remote
 * @see LogicCityImp
 * @see RecordWeather
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @since 14/08/2024
 *
 */
public interface LogicCityInterface extends Remote {

    /**
     * Calcola e restituisce i dati tabellari basati sui record meteorologici forniti.
     *
     * @param weatherRecords Un array di record meteorologici da cui calcolare i dati tabellari.
     *                       Ogni record contiene informazioni su vari parametri meteorologici.
     * @return Un oggetto {@link LogicCityImp.WeatherTableData} che rappresenta i dati aggregati sui parametri meteorologici.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     * @throws IllegalArgumentException Se l'array di record meteorologici è nullo o vuoto.
     */
    LogicCityImp.WeatherTableData getWeatherTableData(RecordWeather[] weatherRecords) throws RemoteException;
}
