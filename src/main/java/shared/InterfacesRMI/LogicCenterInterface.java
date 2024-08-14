package shared.InterfacesRMI;

import shared.record.RecordCenter;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * L'interfaccia {@code LogicCenterInterface} è un'interfaccia remota che permette di definire i metodi che possono
 * essere invocati da un client per interagire con il server per la gestione dei centri di monitoraggio.
 * <p>
 *     Questa interfaccia estende l'interfaccia {@link Remote} e definisce i metodi per l'inizializzazione di un nuovo
 *     centro di monitoraggio e per l'aggiunta di dati a un centro di monitoraggio esistente.
 * </p>
 *
 * @see Remote
 * @see RecordCenter
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @since 14/08/2024
 */
public interface LogicCenterInterface extends Remote {

    /**
     * Inizializza un nuovo centro di monitoraggio con i dettagli forniti.
     *
     * @param centerName   Il nome del centro di monitoraggio.
     * @param streetName   Il nome della via dove si trova il centro.
     * @param streetNumber Il numero civico del centro.
     * @param CAP          Il codice di avviamento postale (CAP) del centro.
     * @param townName     Il nome della città in cui si trova il centro.
     * @param districtName Il nome della provincia in cui si trova il centro.
     * @param cityIDs      Un array di ID delle città associate al centro.
     * @param operatorID   L'ID dell'operatore associato al centro.
     * @throws SQLException    Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    void initNewCenter(
            String centerName,
            String streetName,
            String streetNumber,
            String CAP,
            String townName,
            String districtName,
            Integer[] cityIDs,
            Integer operatorID) throws SQLException, RemoteException;

    /**
     * Aggiunge dati meteorologici a un centro di monitoraggio specifico.
     *
     * @param cityID     L'ID della città a cui i dati meteorologici si riferiscono.
     * @param operatorID L'ID dell'operatore che aggiunge i dati.
     * @param date       La data in cui i dati sono stati rilevati, in formato "yyyy-MM-dd".
     * @param tableData  I dati meteorologici organizzati in una tabella.
     *                   Ogni riga rappresenta un parametro meteorologico e contiene due valori:
     *                   un intero per il punteggio e una stringa per eventuali commenti.
     * @throws SQLException    Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    void addDataToCenter(
            Integer cityID,
            Integer operatorID,
            String date,
            Object[][] tableData) throws SQLException, RemoteException;
}
