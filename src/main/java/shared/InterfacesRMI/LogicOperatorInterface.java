package shared.InterfacesRMI;

import shared.record.RecordOperator;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * L'interfaccia {@code LogicOperatorInterface} è un'interfaccia remota che permette di definire i metodi che possono
 * essere invocati da un client per interagire con il server per la gestione della logica di business degli operatori.
 * <p>
 *     Questa interfaccia estende l'interfaccia {@link Remote} e definisce i metodi per il login, la registrazione e
 *     l'associazione di un operatore ad un centro.
 * </p>
 *
 * @see Remote
 * @see RecordOperator
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @since 14/08/2024
 */
public interface LogicOperatorInterface extends Remote {
    /**
     * Esegue il login di un operatore verificando le credenziali fornite.
     *
     * @param username Il nome utente dell'operatore.
     * @param password La password dell'operatore.
     * @return Un oggetto {@link RecordOperator} contenente i dettagli dell'operatore se il login ha successo.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     * @throws IllegalArgumentException Se il nome utente o la password sono nulli o vuoti.
     */
    RecordOperator performLogin(String username, String password) throws SQLException, RemoteException;

    /**
     * Registra un nuovo operatore con le informazioni fornite.
     *
     * @param nameSurname Il nome e il cognome dell'operatore.
     * @param taxCode Il codice fiscale dell'operatore.
     * @param email L'indirizzo email dell'operatore.
     * @param username Il nome utente scelto dall'operatore.
     * @param password La password scelta dall'operatore.
     * @param centerID L'ID del centro a cui associare l'operatore (può essere null per la registrazione senza centro).
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     * @throws IllegalArgumentException Se uno dei parametri è nullo o vuoto, o se il formato dei dati non è valido.
     */
    void performRegistration(String nameSurname,
                             String taxCode,
                             String email,
                             String username,
                             String password,
                             Integer centerID) throws SQLException, RemoteException;

    /**
     * Associa un operatore a un centro di monitoraggio specifico.
     *
     * @param operatorID L'ID dell'operatore da associare.
     * @param centerID L'ID del centro di monitoraggio al quale associare l'operatore.
     * @return Un oggetto {@link RecordOperator} aggiornato con l'ID del centro associato.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     * @throws IllegalArgumentException Se uno degli ID forniti è nullo o non valido.
     */
    RecordOperator associateCenter(Integer operatorID, Integer centerID) throws SQLException, RemoteException;
}
