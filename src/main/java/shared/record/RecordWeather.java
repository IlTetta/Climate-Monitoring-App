package shared.record;

import shared.utils.Constants;

import java.io.Serializable;

/**
 * La classe {@code RecordWeather} rappresenta dati meteorologici registrati in
 * una determinata data per una citt&agrave; e un centro specifici.
 * Questa classe contiene informazioni come l'ID, l'ID della citt&agrave;, l'ID
 * del centro, la data e vari dati meteorologici come il vento,
 * l'umidit&agrave;, la
 * pressione, la temperatura, la precipitazione, l'elevazione del ghiacciaio e
 * la massa del ghiacciaio.
 * <p>
 * Questa classe &egrave; definita come un record, il che significa che &egrave;
 * immutabile
 * una volta creata.
 * </p>
 * <p>
 *     La classe implementa l'interfaccia {@link Serializable} per permettere la
 *     serializzazione e la deserializzazione degli oggetti di questa classe.
 *     Questo &egrave; necessario per inviare oggetti di questa classe tramite
 *     RMI.
 * </p>
 * 
 * @param ID               L'ID univoco dei dati meteorologici.
 * @param cityID           L'ID della citt&agrave; a cui appartengono i dati
 *                         meteorologici.
 * @param centerID         L'ID del centro a cui appartengono i dati
 *                         meteorologici.
 * @param date             La data di rilevazione dei dati meteorologici.
 * @param wind             I dati relativi al vento.
 * @param humidity         I dati relativi all'umidit&agrave;.
 * @param pressure         I dati relativi alla pressione atmosferica.
 * @param temperature      I dati relativi alla temperatura.
 * @param precipitation    I dati relativi alla precipitazione.
 * @param glacierElevation I dati relativi all'elevazione del ghiacciaio.
 * @param glacierMass      I dati relativi alla massa del ghiaccio.
 * 
 * @see WeatherData
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.1
 * @since 14/08/2024
 */
public record RecordWeather(
        Integer ID,
        Integer cityID,
        Integer centerID,
        String date,
        WeatherData wind,
        WeatherData humidity,
        WeatherData pressure,
        WeatherData temperature,
        WeatherData precipitation,
        WeatherData glacierElevation,
        WeatherData glacierMass) implements Serializable {

    /**
     * Restituisce una rappresentazione testuale formattata dell'oggetto
     * {@code RecordWeather},
     * adatta per la memorizzazione o l'esportazione dei dati.
     * 
     * @return Una stringa formattata contenente tutte le informazioni dei dati
     *         meteorologici.
     */
    @Override
    public String toString() {
        String[] dataStrings = new String[] {

                ID.toString(),
                cityID.toString(),
                centerID.toString(),
                date,
                wind.toString(),
                humidity.toString(),
                pressure.toString(),
                temperature.toString(),
                precipitation.toString(),
                glacierElevation.toString(),
                glacierMass.toString()
        };

        return String.join(Constants.CSV_SEPARATOR, dataStrings);
    }

    /**
     * Restituisce l'ID dei dati meteorologici.
     * @return L'ID dei dati meteorologici.
     */
    @Override
    public Integer ID() {
        return ID;
    }

    /**
     * Restituisce l'ID della citt&agrave; a cui appartengono i dati meteorologici.
     * @return L'ID della citt&agrave; a cui appartengono i dati meteorologici.
     */
    @Override
    public Integer cityID() {
        return cityID;
    }

    /**
     * Restituisce l'ID del centro a cui appartengono i dati meteorologici.
     * @return L'ID del centro a cui appartengono i dati meteorologici.
     */
    @Override
    public Integer centerID() {
        return centerID;
    }

    /**
     * Restituisce la data di rilevazione dei dati meteorologici.
     * @return La data di rilevazione dei dati meteorologici.
     */
    @Override
    public String date() {
        return date;
    }

    /**
     * Restituisce i dati relativi al vento.
     * @return I dati relativi al vento.
     */
    @Override
    public WeatherData wind() {
        return wind;
    }

    /**
     * Restituisce i dati relativi all'umidit&agrave;.
     * @return I dati relativi all'umidit&agrave;.
     */
    @Override
    public WeatherData humidity() {
        return humidity;
    }

    /**
     * Restituisce i dati relativi alla pressione atmosferica.
     * @return I dati relativi alla pressione atmosferica.
     */
    @Override
    public WeatherData pressure() {
        return pressure;
    }

    /**
     * Restituisce i dati relativi alla temperatura.
     * @return I dati relativi alla temperatura.
     */
    @Override
    public WeatherData temperature() {
        return temperature;
    }

    /**
     * Restituisce i dati relativi alla precipitazione.
     * @return I dati relativi alla precipitazione.
     */
    @Override
    public WeatherData precipitation() {
        return precipitation;
    }

    /**
     * Restituisce i dati relativi all'elevazione del ghiacciaio.
     * @return I dati relativi all'elevazione del ghiacciaio.
     */
    @Override
    public WeatherData glacierElevation() {
        return glacierElevation;
    }

    /**
     * Restituisce i dati relativi alla massa del ghiacciaio.
     * @return I dati relativi alla massa del ghiacciaio.
     */
    @Override
    public WeatherData glacierMass() {
        return glacierMass;
    }

    /**
     * La classe {@code WeatherData} rappresenta i dati meteorologici come un
     * punteggio e un commento.
     * <p>
     * &#201; utilizzata internamente per rappresentare i vari dati meteorologici
     * all'interno di {@code RecordWeather}.
     * </p>
     * 
     * @param score   Il punteggio relativo al dato meteorologico.
     * @param comment Un commento descrittivo sul dato meteorologico.
     * 
     * @see RecordWeather
     */
    public record WeatherData(
            Integer score,
            String comment) implements Serializable {

        /**
         * Restituisce una rappresentazione testuale formattata dell'oggetto
         * {@code WeatherData},
         * adatta per la memorizzazione o l'esportazione dei dati.
         * 
         * @return Una stringa formattata contenente il punteggio e il commento del dato
         *         meteorologico.
         */
        @Override
        public String toString() {
            return (score != null ? Integer.toString(score)
                    : Constants.EMPTY_STRING) +
                    Constants.CSV_SUB_SEPARATOR +
                    comment;
        }

        /**
         * Restituisce il punteggio relativo al dato meteorologico.
         * @return Il punteggio relativo al dato meteorologico.
         */
        @Override
        public Integer score() {
            return score;
        }

        /**
         * Restituisce un commento descrittivo sul dato meteorologico.
         * @return Un commento descrittivo sul dato meteorologico.
         */
        @Override
        public String comment() {
            return comment;
        }
    }
}
