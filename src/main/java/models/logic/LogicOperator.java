package models.logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.CurrentOperator;
import models.data.DataHandler;
import models.data.DataQuery.QueryCondition;
import models.record.RecordOperator;

/**
 * La classe {@code LogicOperator} gestisce la logica relativa agli operatori
 * dell'applicazione.
 * <p>
 * Fornisce metodi per effettuare il login, la registrazione e le verifiche di
 * validit&agrave; dei dati degli operatori.
 * </p>
 * 
 * @see models.CurrentOperator
 * @see models.data.DataHandler
 * @see models.data.DataQuery.QueryCondition
 * @see models.record.RecordOperator
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class LogicOperator {

    /**
     * Gestore dei dati dell'applicazione.
     */
    private DataHandler dataHandler;

    /**
     * Costruttore della classe {@code LogicOperator}.
     * 
     * @param dataHandler Il gestore dei dati utilizzato per l'accesso ai dati degli
     *                    operatori.
     */
    public LogicOperator(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
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
    public void performLogin(String username, String password) {

        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username e password non possono essere vuoti.");
        }

        CurrentOperator currentOperator = CurrentOperator.getInstance();

        if (currentOperator.isUserLogged()) {
            currentOperator.performLogout();
        }

        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("username", username));
        conditions.add(new QueryCondition("password", hashPassword(username, password)));

        RecordOperator[] result;
        try {
            result = dataHandler.getOperatorBy(conditions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (result.length == 1) {
            currentOperator.setCurrentOperator(result[0]);
        } else {
            currentOperator.performLogout();
            throw new IllegalArgumentException("Username o password non sono corretti.");
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
    public void performRegistration(String nameSurname,
            String taxCode,
            String email,
            String username,
            String password,
            Integer centerID) {

        CurrentOperator currentOperator = CurrentOperator.getInstance();

        if (currentOperator.isUserLogged()) {
            currentOperator.performLogout();
        }

        if (!isValidNameSurname(nameSurname))
            throw new IllegalArgumentException(
                    "Nome e Cognome non validi! \nNon devono contenere simboli numeri o altri caratteri speciali.");
        if (!isValidTaxCode(taxCode))
            throw new IllegalArgumentException("Codice fiscale non valido!\nEsempio atteso: RSSMRA80A01H501T");
        if (!isValidEmail(email))
            throw new IllegalArgumentException("Indirizzo E-mail non valido!");
        if (!isValidUsername(username))
            throw new IllegalArgumentException(
                    "Username non valido/già esistente!\nMinimo 3 caratteri tra cui lettere, numeri e i seguenti simboli: . - _");
        if (!isValidPassword(password))
            throw new IllegalArgumentException(
                    "Password non valida!\nDeve essere lunga almeno 8 caratteri e deve contenere una maiscula e un carattere speciale.");

        try {
            dataHandler.addNewOperator(nameSurname,
                    taxCode,
                    email,
                    username,
                    hashPassword(username, password),
                    centerID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public void associateCenter(Integer centerID) {

        CurrentOperator currentOperator = CurrentOperator.getInstance();

        if (currentOperator.isUserLogged() == false) {
            throw new RuntimeException("Nessun utente loggato");
        }

        if (currentOperator.getCurrentOperator().centerID() != 0) {
            throw new RuntimeException("Utente fa già parte di un Centro");
        }

        RecordOperator updatedOperator = new RecordOperator(
                currentOperator.getCurrentOperator().ID(),
                currentOperator.getCurrentOperator().nameSurname(),
                currentOperator.getCurrentOperator().taxCode(),
                currentOperator.getCurrentOperator().email(),
                currentOperator.getCurrentOperator().username(),
                currentOperator.getCurrentOperator().password(),
                centerID);

        try {
            dataHandler.updateOperator(updatedOperator);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        currentOperator.setCurrentOperator(updatedOperator);
    }

    /**
     * Verifica se il formato del nome e cognome &egrave; valido.
     * 
     * @param nameSurname Il nome e cognome da verificare.
     * @return {@code true} se il formato &egrave; valido, {@code false} altrimenti.
     */
    public boolean isValidNameSurname(String nameSurname) {
        return nameSurname.matches("^[a-zA-Z\\s]+$");
    }

    /**
     * Verifica se il formato del codice fiscale &egrave; valido.
     * 
     * @param taxCode Il codice fiscale da verificare.
     * @return {@code true} se il formato &egrave; valido, {@code false} altrimenti.
     */
    public boolean isValidTaxCode(String taxCode) {
        return taxCode.matches("^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$");
    }

    /**
     * Verifica se il formato dell'indirizzo email &egrave; valido.
     * 
     * @param email L'indirizzo email da verificare.
     * @return {@code true} se il formato &egrave; valido, {@code false} altrimenti.
     */
    public boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * Verifica se il formato del nome utente &egrave; valido e che non ci sia un
     * duplicato nel sistema.
     * 
     * @param username Il nome utente da verificare.
     * @return {@code true} se il formato &egrave; valido, {@code false} altrimenti.
     */
    public boolean isValidUsername(String username) {
        QueryCondition condition = new QueryCondition("username", username);
        RecordOperator[] result = new RecordOperator[0];
        try {
            result = dataHandler.getOperatorBy(condition);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        boolean correct = true;
        for (RecordOperator recordOperator : result) {
            if (recordOperator.username().equals(username))
                correct = false;

        }
        return (username.matches("^[a-zA-Z0-9._-]{3,}$") && correct);
    }

    /**
     * Verifica se il formato della password &egrave; valido.
     * 
     * @param password La password da verificare.
     * @return {@code true} se il formato &egrave; valido, {@code false} altrimenti.
     */
    public boolean isValidPassword(String password) {

        return password.matches("^(?=.*[A-Z])(?=.*[@#$%^&+=!.])(.{8,})$");
    }

    /**
     * Cifra la password inserita dall'utente usando l'algoritmo {@code SHA-256} e
     * un approccio di concatenazione tra username e password.
     * 
     * @param username L'username dell'utente.
     * @param password La password dell'utente.
     * @return La password cifrata.
     */
    public String hashPassword(String username, String password) {
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
