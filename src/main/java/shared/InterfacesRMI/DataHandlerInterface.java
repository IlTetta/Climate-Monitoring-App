package shared.InterfacesRMI;

import shared.record.RecordCenter;
import shared.record.RecordOperator;
import shared.record.RecordWeather;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * L'interfaccia {@code DataHandlerInterface} è un'interfaccia remota che permette di definire i metodi che possono
 * essere invocati da un client per interagire con il server per la gestione dei dati.
 * <p>
 *     Questa interfaccia estende l'interfaccia {@link Remote} e definisce i metodi per l'aggiunta di un nuovo operatore,
 *     di un nuovo centro e di un nuovo dato meteo.
 * </p>
 *
 * @see Remote
 * @see RecordCenter
 * @see RecordOperator
 * @see RecordWeather
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @since 14/08/2024
 */
public interface DataHandlerInterface extends Remote {

    /**
     * Aggiunge un nuovo operatore al sistema.
     *
     * @param nameSurname Il nome e cognome dell'operatore.
     * @param taxCode Il codice fiscale dell'operatore.
     * @param email L'email dell'operatore.
     * @param username Il nome utente per l'accesso dell'operatore.
     * @param password La password per l'accesso dell'operatore.
     * @param centerID L'ID del centro di monitoraggio a cui l'operatore è associato.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    void addNewOperator(String nameSurname,
                        String taxCode,
                        String email,
                        String username,
                        String password,
                        Integer centerID) throws SQLException, RemoteException;

    /**
     * Aggiunge un nuovo centro di monitoraggio al sistema.
     *
     * @param centerName Il nome del centro di monitoraggio.
     * @param streetName Il nome della via in cui si trova il centro.
     * @param streetNumber Il numero civico del centro.
     * @param CAP Il codice di avviamento postale (CAP) del centro.
     * @param townName Il nome del comune in cui si trova il centro.
     * @param districtName Il nome della provincia in cui si trova il centro.
     * @param cityIDs Gli ID delle città monitorate dal centro.
     * @return Il record del centro di monitoraggio appena creato.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordCenter addNewCenter(String centerName,
                              String streetName,
                              String streetNumber,
                              String CAP,
                              String townName,
                              String districtName,
                              Integer[] cityIDs) throws SQLException, RemoteException;

    /**
     * Aggiunge nuovi dati meteorologici al sistema.
     *
     * @param cityID L'ID della città a cui si riferiscono i dati meteorologici.
     * @param centerID L'ID del centro di monitoraggio che ha raccolto i dati.
     * @param date La data di raccolta dei dati nel formato "yyyy-MM-dd".
     * @param wind I dati relativi al vento.
     * @param humidity I dati relativi all'umidità.
     * @param pressure I dati relativi alla pressione atmosferica.
     * @param temperature I dati relativi alla temperatura.
     * @param precipitation I dati relativi alle precipitazioni.
     * @param glacierElevation I dati relativi all'elevazione dei ghiacciai.
     * @param glacierMass I dati relativi alla massa dei ghiacciai.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
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


    /**
     * Aggiorna le informazioni di un operatore esistente nel sistema.
     *
     * @param operator Il record dell'operatore contenente le informazioni aggiornate.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    void updateOperator(RecordOperator operator) throws SQLException, RemoteException;
}
