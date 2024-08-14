package shared.record;

import shared.utils.Constants;

import java.io.Serializable;

/**
 * La classe {@code RecordCity} rappresenta una citt&agrave; e contiene
 * informazioni
 * come l'ID della citt&agrave;,
 * il nome, il nome ASCII, il codice del paese, il nome del paese, la latitudine
 * e la longitudine geografica.
 * <p>
 * Questa classe &egrave; definita come un record, il che significa che &egrave;
 * immutabile
 * una volta creata.
 * </p>
 * * <p>
 *  *     La classe implementa l'interfaccia {@link Serializable} per permettere la
 *  *     serializzazione e la deserializzazione degli oggetti di questa classe.
 *  *     Questo &egrave; necessario per inviare oggetti di questa classe tramite
 *  *     RMI.
 *  * </p>
 * 
 * @param ID          L'ID univoco della citt&agrave;.
 * @param name        Il nome della citt&agrave;.
 * @param ASCIIName   Il nome ASCII della citt&agrave;.
 * @param countryCode Il codice del paese a cui appartiene la citt&agrave;.
 * @param countryName Il nome del paese a cui appartiene la citt&agrave;.
 * @param latitude    La latitudine geografica della citt&agrave;.
 * @param longitude   La longitudine geografica della citt&agrave;.
 * 
 * @see Constants
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.1
 * @since 14/08/2024
 */
public record RecordCity(
        Integer ID,
        String name,
        String ASCIIName,
        String countryCode,
        String countryName,
        double latitude,
        double longitude) implements Serializable {

    /**
     * Restituisce una rappresentazione testuale formattata dell'oggetto
     * {@code RecordCity},
     * adatta per la memorizzazione o l'esportazione dei dati.
     * 
     * @return Una stringa formattata contenente tutte le informazioni della
     *         citt&agrave;.
     */
    @Override

    public String toString() {
        String[] dataStrings = new String[] {
                ID.toString(),
                name,
                ASCIIName,
                countryCode,
                countryName,
                String.valueOf(latitude),
                String.valueOf(longitude)
        };

        return String.join(Constants.CSV_SEPARATOR, dataStrings);
    }

    /**
     * Restituisce l'ID della citt&agrave;.
     *
     * @return L'ID della citt&agrave;.
     */
    @Override
    public Integer ID() {
        return ID;
    }

    /**
     * Restituisce il nome della citt&agrave;.
     * @return Il nome della citt&agrave;.
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * Restituisce il nome ASCII della citt&agrave;.
     *
     * @return Il nome ASCII della citt&agrave;.
     */
    @Override
    public String ASCIIName() {
        return ASCIIName;
    }

    /**
     * Restituisce il codice del paese a cui appartiene la citt&agrave;.
     *
     * @return Il codice del paese a cui appartiene la citt&agrave;.
     */
    @Override
    public String countryCode() {
        return countryCode;
    }

    /**
     * Restituisce il nome del paese a cui appartiene la citt&agrave;.
     *
     * @return Il nome del paese a cui appartiene la citt&agrave;.
     */
    @Override
    public String countryName() {
        return countryName;
    }

    /**
     * Restituisce la latitudine geografica della citt&agrave;.
     *
     * @return La latitudine geografica della citt&agrave;.
     */
    @Override
    public double latitude() {
        return latitude;
    }

    /**
     * Restituisce la longitudine geografica della citt&agrave;.
     *
     * @return La longitudine geografica della citt&agrave;.
     */
    @Override
    public double longitude() {
        return longitude;
    }

}
