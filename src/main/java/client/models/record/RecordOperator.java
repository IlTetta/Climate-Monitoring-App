package client.models.record;

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
 * @version 1.0
 * @since 16/09/2023
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

        // Unisci tutte le informazioni in una singola stringa separata da virgole
        return String.join(Constants.CSV_SEPARATOR, dataStrings);
    }

    @Override
    public Integer ID() {
        return ID;
    }

    @Override
    public String nameSurname() {
        return nameSurname;
    }

    @Override
    public String taxCode() {
        return taxCode;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public Integer centerID() {
        return centerID;
    }
}
