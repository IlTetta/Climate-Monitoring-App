package shared.record;

import shared.utils.Constants;

import java.io.Serializable;

/**
 * La classe {@code RecordOperator} rappresenta un operatore e contiene
 * informazioni come l'ID, il nome e cognome,
 * il codice fiscale, l'email, il nome utente, la password e l'ID del centro di
 * competenza (se assegnato).
 * <p>
 * Questa classe &egrave; definita come un record, il che significa che &egrave; immutabile
 * una volta creata.
 * </p>
 * * <p>
 *  *     La classe implementa l'interfaccia {@link Serializable} per permettere la
 *  *     serializzazione e la deserializzazione degli oggetti di questa classe.
 *  *     Questo &egrave; necessario per inviare oggetti di questa classe tramite
 *  *     RMI.
 *  * </p>
 * 
 * @param ID          L'ID univoco dell'operatore.
 * @param nameSurname Il nome e cognome dell'operatore.
 * @param taxCode     Il codice fiscale dell'operatore.
 * @param email       L'indirizzo email dell'operatore.
 * @param username    Il nome utente dell'operatore.
 * @param password    La password dell'operatore.
 * @param centerID      L'ID del centro di competenza dell'operatore (pu&ograve; essere
 *                    nullo).
 * 
 * @see Constants
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.1
 * @since 14/08/2024
 */
public record RecordOperator(
        Integer ID,
        String nameSurname,
        String taxCode,
        String email,
        String username,
        String password,
        Integer centerID) implements Serializable {

    /**
     * Restituisce una rappresentazione testuale formattata dell'oggetto
     * {@code RecordOperator},
     * adatta per la memorizzazione o l'esportazione dei dati.
     * 
     * @return Una stringa formattata contenente tutte le informazioni
     *         dell'operatore.
     */
    @Override
    public String toString() {
        String[] dataStrings = new String[] {
                ID.toString(),
                nameSurname,
                taxCode,
                email,
                username,
                password,
                centerID == null ? Constants.EMPTY_STRING : centerID.toString()
        };

        return String.join(Constants.CSV_SEPARATOR, dataStrings);
    }

    /**
     * Restituisce l'ID dell'operatore.
     * @return L'ID dell'operatore.
     */
    @Override
    public Integer ID() {
        return ID;
    }

    /**
     * Restituisce il nome e cognome dell'operatore.
     * @return Il nome e cognome dell'operatore.
     */
    @Override
    public String nameSurname() {
        return nameSurname;
    }

    /**
     * Restituisce il codice fiscale dell'operatore.
     * @return Il codice fiscale dell'operatore.
     */
    @Override
    public String taxCode() {
        return taxCode;
    }

    /**
     * Restituisce l'indirizzo email dell'operatore.
     * @return L'indirizzo email dell'operatore.
     */
    @Override
    public String email() {
        return email;
    }

    /**
     * Restituisce il nome utente dell'operatore.
     * @return Il nome utente dell'operatore.
     */
    @Override
    public String username() {
        return username;
    }

    /**
     * Restituisce la password dell'operatore.
     * @return La password dell'operatore.
     */
    @Override
    public String password() {
        return password;
    }

    /**
     * Restituisce l'ID del centro di competenza dell'operatore.
     * @return L'ID del centro di competenza dell'operatore.
     */
    @Override
    public Integer centerID() {
        return centerID;
    }
}
