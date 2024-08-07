package client.models.record;

import shared.utils.Constants;

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
 * @version 1.0
 * @since 16/09/2023
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
        WeatherData glacierMass) {

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

                // Creazione di un array di stringhe contenente tutti i dati meteorologici
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

        // Unisci tutte le informazioni in una singola stringa separata da virgole
        return String.join(Constants.CSV_SEPARATOR, dataStrings);
    }

    @Override
    public Integer ID() {
        return ID;
    }

    @Override
    public Integer cityID() {
        return cityID;
    }

    @Override
    public Integer centerID() {
        return centerID;
    }

    @Override
    public String date() {
        return date;
    }

    @Override
    public WeatherData wind() {
        return wind;
    }

    @Override
    public WeatherData humidity() {
        return humidity;
    }

    @Override
    public WeatherData pressure() {
        return pressure;
    }

    @Override
    public WeatherData temperature() {
        return temperature;
    }

    @Override
    public WeatherData precipitation() {
        return precipitation;
    }

    @Override
    public WeatherData glacierElevation() {
        return glacierElevation;
    }

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
            String comment) {

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

        @Override
        public Integer score() {
            return score;
        }

        @Override
        public String comment() {
            return comment;
        }
    }
}
