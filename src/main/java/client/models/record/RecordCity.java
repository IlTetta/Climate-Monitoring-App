package client.models.record;

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
 * @version 1.0
 * @since 16/09/2023
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

    // Creazione di un array di stringhe contenente tutti i dati della citt&agrave;
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

        // Unisci tutte le informazioni in una singola stringa separata da virgole
        return String.join(Constants.CSV_SEPARATOR, dataStrings);
    }

    @Override
    public Integer ID() {
        return ID;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String ASCIIName() {
        return ASCIIName;
    }

    @Override
    public String countryCode() {
        return countryCode;
    }

    @Override
    public String countryName() {
        return countryName;
    }

    @Override
    public double latitude() {
        return latitude;
    }

    @Override
    public double longitude() {
        return longitude;
    }

}
