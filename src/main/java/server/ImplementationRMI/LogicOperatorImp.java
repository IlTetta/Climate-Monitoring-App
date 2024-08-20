package server.ImplementationRMI;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import shared.interfacesRMI.LogicOperatorInterface;
import shared.interfacesRMI.DataHandlerInterface;
import shared.interfacesRMI.DataQueryInterface;
import shared.record.RecordOperator;
import shared.record.QueryCondition;

/**
 * La classe {@code LogicOperatorImp} gestisce la logica relativa agli operatori
 * dell'applicazione.
 * <p>
 * Fornisce metodi per effettuare il login, la registrazione e le verifiche di
 * validità dei dati degli operatori.
 * </p>
 *
 * @see RecordOperator
 * @see QueryCondition
 * @see DataHandlerInterface
 * @see DataQueryInterface
 * @see LogicOperatorInterface
 *
 * @serial exclude
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.1
 * @since 14/08/2024
 */
public class LogicOperatorImp extends UnicastRemoteObject implements LogicOperatorInterface {

    @Serial
    private static final long serialVersionUID = 3L;

    /**
     * Gestore dei dati dell'applicazione.
     */
    private final DataHandlerInterface dataHandler;

    /**
     * Interfaccia per le query sui dati.
     */
    private final DataQueryInterface dataQuery;

    /**
     * Costruttore della classe {@code LogicOperatorImp}.
     *
     * @param dataHandler Il gestore dei dati utilizzato per l'accesso ai dati degli
     *                    operatori.
     * @param dataQuery   L'interfaccia per le query sui dati.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    public LogicOperatorImp(DataHandlerInterface dataHandler, DataQueryInterface dataQuery) throws RemoteException {
        super();
        this.dataHandler = dataHandler;
        this.dataQuery = dataQuery;
    }

    /**
     * Effettua il login di un operatore utilizzando il nome utente e la password
     * forniti.
     *
     * @param username Il nome utente dell'operatore.
     * @param password La password dell'operatore.
     * @return Il record dell'operatore se le credenziali sono corrette, {@code null} altrimenti.
     * @throws RemoteException Se si verifica un errore durante la comunicazione remota.
     * @throws IllegalArgumentException Se il nome utente o la password sono vuoti.ù
     * @throws SQLException Se si verifica un errore durante l'accesso ai dati.
     */
    @Override
    public RecordOperator performLogin(String username, String password) throws RemoteException, SQLException {
        validateLoginInputs(username, password);

        List<QueryCondition> conditions = List.of(
                new QueryCondition("username", username),
                new QueryCondition("password", hashPassword(username, password))
        );
            RecordOperator[] result = dataQuery.getOperatorBy(conditions);
            return result.length == 1 ? result[0] : null;
    }

    /**
     * Effettua la registrazione di un nuovo operatore con i dati forniti.
     *
     * @param nameSurname Il nome e cognome dell'operatore.
     * @param taxCode     Il codice fiscale dell'operatore.
     * @param email       L'indirizzo email dell'operatore.
     * @param username    Il nome utente desiderato per l'operatore.
     * @param password    La password per l'operatore.
     * @param centerID    L'ID del centro di competenza dell'operatore (può essere
     *                    {@code null}).
     * @throws IllegalArgumentException Se uno dei dati inseriti non è valido
     *                                  o se si è già loggati come operatore.
     * @throws RemoteException Se si verifica un errore durante la comunicazione remota.
     * @throws SQLException Se si verifica un errore durante l'accesso ai dati.
     *
     */
    @Override
    public void performRegistration(String nameSurname, String taxCode, String email, String username,
                                    String password, Integer centerID) throws RemoteException, SQLException, IllegalArgumentException {

        validateRegistrationInputs(nameSurname, taxCode, email, username, password);

        dataHandler.addNewOperator(nameSurname, taxCode, email, username,
                    hashPassword(username, password), centerID);
    }

    /**
     * Modifica i dati dell'operatore corrente riguardanti il centro a esso
     * associato.
     * <p>
     * Questo metodo cerca un centro con l'ID specificato e aggiorna l'operatore
     * corrente con l'ID del centro trovato.
     * </p>
     *
     * @param operatorID L'ID dell'operatore da aggiornare.
     * @param centerID   L'ID del centro di monitoraggio da associare all'operatore.
     * @return Il record dell'operatore aggiornato.
     * @throws SQLException Se si verifica un errore durante l'accesso ai dati o
     *                      durante l'aggiornamento.
     * @throws RemoteException Se si verifica un errore durante la comunicazione remota.
     * @throws IllegalStateException Se l'operatore è già associato a un centro.
     */
    @Override
    public RecordOperator associateCenter(Integer operatorID, Integer centerID) throws SQLException, RemoteException {
        RecordOperator currentOperator = dataQuery.getOperatorBy(operatorID);

        if (currentOperator.centerID() != 0) {
            throw new IllegalStateException("L'operatore è già associato a un centro.");
        }

        RecordOperator updatedOperator = new RecordOperator(
                currentOperator.ID(),
                currentOperator.nameSurname(),
                currentOperator.taxCode(),
                currentOperator.email(),
                currentOperator.username(),
                currentOperator.password(),
                centerID);

        dataHandler.updateOperator(updatedOperator);
        return updatedOperator;
    }

    /**
     * Verifica se il formato del nome utente è valido e che non ci sia un
     * duplicato nel sistema.
     *
     * @param username Il nome utente da verificare.
     * @return {@code true} se il formato è valido e non esiste già, {@code false} altrimenti.
     * @throws SQLException Se si verifica un errore durante l'accesso ai dati.
     * @throws RemoteException Se si verifica un errore durante la comunicazione remota.
     */
    private boolean isValidUsername(String username) throws SQLException, RemoteException {
        String usernamePattern = "^[a-zA-Z0-9._-]{3,}$";
        if (!username.matches(usernamePattern)) {
            return false;
        }

        QueryCondition condition = new QueryCondition("username", username);
            RecordOperator[] result = dataQuery.getOperatorBy(condition);
            return result.length == 0;
    }

    /**
     * Cifra la password inserita dall'utente usando l'algoritmo {@code SHA-256} e
     * un approccio di concatenazione tra username e password.
     *
     * @param username L'username dell'utente.
     * @param password La password dell'utente.
     * @return La password cifrata.
     * @throws RuntimeException Se l'algoritmo di cifratura non è disponibile.
     */
    private String hashPassword(String username, String password) {
        try {
            String combinedString = username + password;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(combinedString.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                sb.append(String.format("%02x", hashedByte));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo di cifratura non disponibile", e);
        }
    }

    /**
     * Convalida i dati per il login.
     *
     * @param username Il nome utente da validare.
     * @param password La password da validare.
     * @throws IllegalArgumentException Se il nome utente o la password sono nulli o vuoti.
     */
    private void validateLoginInputs(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Nome utente e/o password non validi. Assicurati di inserire entrambi i dati.");
        }
    }

    /**
     * Convalida i dati per la registrazione.
     *
     * @param nameSurname Il nome e cognome da validare.
     * @param taxCode     Il codice fiscale da validare.
     * @param email       L'email da validare.
     * @param username    Il nome utente da validare.
     * @param password    La password da validare.
     * @throws IllegalArgumentException Se uno dei dati inseriti non è valido.
     * @throws RemoteException Se si verifica un errore durante la comunicazione remota.
     * @throws SQLException Se si verifica un errore durante l'accesso ai dati.
     */
    private void validateRegistrationInputs(String nameSurname, String taxCode, String email,
                                            String username, String password) throws RemoteException, SQLException {
        String nameSurnamePattern = "^[a-zA-Z\\s]+$";
        String taxCodePattern = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$";
        String emailPattern = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=!.])(.{8,})$";

        if (!nameSurname.matches(nameSurnamePattern)) {
            throw new IllegalArgumentException("Nome e cognome non validi! Esempio atteso: Mario Rossi.");
        }
        if (!taxCode.matches(taxCodePattern)) {
            throw new IllegalArgumentException("Codice fiscale non valido! Esempio atteso: RSSMRA80A01H501T.");
        }
        if (!email.matches(emailPattern)) {
            throw new IllegalArgumentException("Indirizzo email non valido! Esempio atteso: esempio@mail.com.");
        }
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("Username non valido/già esistente! Esempio atteso: utente_01.");
        }
        if (!password.matches(passwordPattern)) {
            throw new IllegalArgumentException("Password non valida! Deve essere lunga almeno 8 caratteri, con una maiuscola e un carattere speciale. Esempio: Password@123.");
        }
    }
}