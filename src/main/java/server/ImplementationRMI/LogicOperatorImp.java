package server.ImplementationRMI;

import java.io.IOException;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import client.models.CurrentOperator;
import shared.InterfacesRMI.LogicOperatorInterface;
import shared.record.RecordOperator;
import shared.InterfacesRMI.DataHandlerInterface;
import shared.InterfacesRMI.DataQueryInterface;
import shared.utils.QueryCondition;
import shared.utils.Functions;

/**
 * La classe {@code LogicOperatorImp} gestisce la logica relativa agli operatori
 * dell'applicazione.
 * <p>
 * Fornisce metodi per effettuare il login, la registrazione e le verifiche di
 * validit&agrave; dei dati degli operatori.
 * </p>
 * 
 * @see CurrentOperator
 * @see DataHandlerImp
 * @see QueryCondition
 * @see RecordOperator
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class LogicOperatorImp extends UnicastRemoteObject implements LogicOperatorInterface {

    @Serial
    private static final long serialVersionUID = 3L;
    /**
     * Gestore dei dati dell'applicazione.
     */
    private final DataHandlerInterface dataHandler;

    private final DataQueryInterface dataQuery;

    /**
     * Costruttore della classe {@code LogicOperatorImp}.
     * 
     * @param dataHandler Il gestore dei dati utilizzato per l'accesso ai dati degli
     *                    operatori.
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
     * @throws IllegalArgumentException Se il nome utente o la password sono vuoti o
     *                                  se le credenziali sono errate.
     */
    @Override
    public RecordOperator performLogin(String username, String password) throws RemoteException {

        if (username==null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username e password non possono essere vuoti.");
        }

        List<QueryCondition> conditions = List.of(
                new QueryCondition("username", username),
                new QueryCondition("password", hashPassword(username, password))
        );

        try {
            RecordOperator[] result = dataQuery.getOperatorBy(conditions);
            if(result.length == 1){
                return result[0];
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il login", e);
        }
    }

    /**
     * Effettua la registrazione di un nuovo operatore con i dati forniti.
     * <p>
     * Se la registrazione &egrave; avvenuta con successo viene effettuato
     * automaticamente
     * il login.
     * </p>
     * 
     * @param nameSurname Il nome e cognome dell'operatore.
     * @param taxCode     Il codice fiscale dell'operatore.
     * @param email       L'indirizzo email dell'operatore.
     * @param username    Il nome utente desiderato per l'operatore.
     * @param password    La password per l'operatore.
     * @param centerID    L'ID del centro di competenza dell'operatore (pu&ograve;
     *                    essere
     *                    null).
     * @throws IllegalArgumentException Se uno dei dati inseriti non &egrave; valido
     *                                  o se
     *                                  si &egrave; gi&agrave; loggati come
     *                                  operatore.
     */
    @Override
    public void performRegistration(String nameSurname,
                                    String taxCode,
                                    String email,
                                    String username,
                                    String password,
                                    Integer centerID) {

        String nameSurnamePattern = "^[a-zA-Z\\s]+$";
        String taxCodePattern = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$";
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=!.])(.{8,})$";

        if (!nameSurname.matches(nameSurnamePattern))
            throw new IllegalArgumentException(
                    "Nome e Cognome non validi! \nNon devono contenere simboli numeri o altri caratteri speciali.");
        if (!taxCode.matches(taxCodePattern))
            throw new IllegalArgumentException("Codice fiscale non valido!\nEsempio atteso: RSSMRA80A01H501T");
        if (!email.matches(emailPattern))
            throw new IllegalArgumentException("Indirizzo E-mail non valido!");
        if (!isValidUsername(username))
            throw new IllegalArgumentException(
                    "Username non valido/già esistente!\nMinimo 3 caratteri tra cui lettere, numeri e i seguenti simboli: . - _");
        if (!password.matches(passwordPattern))
            throw new IllegalArgumentException(
                    "Password non valida!\nDeve essere lunga almeno 8 caratteri e deve contenere una maiscula e un carattere speciale.");

        try {
            dataHandler.addNewOperator(nameSurname,
                    taxCode,
                    email,
                    username,
                    hashPassword(username, password),
                    centerID);
        } catch (SQLException | RemoteException e) {
            throw new RuntimeException("Errore durante la registrazione", e);
        }

    }

    /**
     * Modifica i dati dell'operatore corrente riguardanti il centro ad esso
     * associato.
     * <p>
     * Questo metodo cerca un centro con il nome specificato e aggiorna l'operatore
     * corrente
     * con l'ID del centro trovato.
     * </p>
     *
     * @param centerID L'ID del centro di monitoraggio da associare all'operatore
     * @throws IOException Lanciata in caso di errori durante la lettura, la
     *                     modifica
     *                     dei file
     *                     o l'aggiornamento dei dati dell'operatore.
     */
    @Override
    public RecordOperator associateCenter(Integer operatorID, Integer centerID) throws SQLException, RemoteException {

        RecordOperator currentOperator = dataQuery.getOperatorBy(operatorID);

        if(currentOperator.centerID() != 0)
            throw new SQLException();

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
     * Verifica se il formato del nome utente &egrave; valido e che non ci sia un
     * duplicato nel sistema.
     * 
     * @param username Il nome utente da verificare.
     * @return {@code true} se il formato &egrave; valido, {@code false} altrimenti.
     */
    private boolean isValidUsername(String username) {
        QueryCondition condition = new QueryCondition("username", username);
        RecordOperator[] result;
        try {
            result = dataQuery.getOperatorBy(condition);
        } catch (SQLException | RemoteException e) {
            throw new RuntimeException(e);
        }
        boolean correct = true;
        for (RecordOperator recordOperator : result) {
            if (recordOperator.username().equals(username)) {
                correct = false;
                break;
            }
        }
        String usernamePattern = "^[a-zA-Z0-9._-]{3,}$";
        return (username.matches(usernamePattern) && correct);
    }

    /**
     * Cifra la password inserita dall'utente usando l'algoritmo {@code SHA-256} e
     * un approccio di concatenazione tra username e password.
     * 
     * @param username L'username dell'utente.
     * @param password La password dell'utente.
     * @return La password cifrata.
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
            e.printStackTrace();
            return null;
        }

    }

}
