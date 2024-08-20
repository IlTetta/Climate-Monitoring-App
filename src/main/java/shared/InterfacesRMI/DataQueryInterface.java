package shared.interfacesRMI;

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

/**
 * L'interfaccia {@code DataQueryInterface} è un'interfaccia remota che permette di definire i metodi che possono
 * essere invocati da un client per interagire con il server per la gestione dei dati.
 * <p>
 *     Questa interfaccia estende l'interfaccia {@link Remote} e definisce i metodi per la ricerca di città, operatori
 *     e dati meteo.
 * </p>
 *
 * @see Remote
 * @see RecordCity
 * @see RecordOperator
 * @see RecordWeather
 * @see QueryCondition
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @since 14/08/2024
 */
public interface DataQueryInterface extends Remote {

    /**
     * Ottiene i dettagli di una città in base al suo ID.
     *
     * @param ID L'ID della città.
     * @return Il record della città corrispondente all'ID specificato.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordCity getCityBy(Integer ID) throws SQLException, RemoteException;

    /**
     * Ottiene un array di città che soddisfano le condizioni specificate.
     *
     * @param conditions Una lista di condizioni di query da soddisfare.
     * @return Un array di record delle città che soddisfano le condizioni specificate.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordCity[] getCityBy(List<QueryCondition> conditions) throws SQLException, RemoteException;

    /**
     * Ottiene i dettagli di un operatore in base al suo ID.
     *
     * @param ID L'ID dell'operatore.
     * @return Il record dell'operatore corrispondente all'ID specificato.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordOperator getOperatorBy(Integer ID) throws SQLException, RemoteException;

    /**
     * Ottiene un array di operatori che soddisfano la condizione specificata.
     *
     * @param condition Una condizione di query da soddisfare.
     * @return Un array di record degli operatori che soddisfano la condizione specificata.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordOperator[] getOperatorBy(QueryCondition condition) throws SQLException, RemoteException;

    /**
     * Ottiene un array di operatori che soddisfano le condizioni specificate.
     *
     * @param conditions Una lista di condizioni di query da soddisfare.
     * @return Un array di record degli operatori che soddisfano le condizioni specificate.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordOperator[] getOperatorBy(List<QueryCondition> conditions) throws SQLException, RemoteException;

    /**
     * Ottiene i dettagli di un centro di monitoraggio in base al suo ID.
     *
     * @param ID L'ID del centro di monitoraggio.
     * @return Il record del centro di monitoraggio corrispondente all'ID specificato.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordCenter getCenterBy(Integer ID) throws SQLException, RemoteException;

    /**
     * Ottiene un array di tutti i centri di monitoraggio disponibili.
     *
     * @return Un array di record dei centri di monitoraggio.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordCenter[] getCenters() throws SQLException, RemoteException;

    /**
     * Ottiene un array di dati meteorologici che soddisfano la condizione specificata.
     *
     * @param condition Una condizione di query da soddisfare.
     * @return Un array di record meteorologici che soddisfano la condizione specificata.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordWeather[] getWeatherBy(QueryCondition condition) throws SQLException, RemoteException;

    /**
     * Ottiene un array di dati meteorologici che soddisfano le condizioni specificate.
     *
     * @param conditions Una lista di condizioni di query da soddisfare.
     * @return Un array di record meteorologici che soddisfano le condizioni specificate.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    RecordWeather[] getWeatherBy(List<QueryCondition> conditions) throws SQLException, RemoteException;

    /**
     * Restituisce la connessione al database.
     *
     * @return La connessione al database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    Connection getConn() throws RemoteException;
}
