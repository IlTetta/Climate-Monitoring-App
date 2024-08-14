package server.ImplementationRMI;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shared.InterfacesRMI.LogicCityInterface;
import shared.record.RecordWeather;

/**
 * La classe {@code LogicCityImp} implementa i servizi RMI per la gestione dei dati meteorologici
 * delle città.
 * <p>
 * Fornisce metodi per l'elaborazione dei dati meteorologici delle città.
 * </p>
 *
 * @see RecordWeather
 * @see WeatherTableData
 * @see LogicCityInterface
 * @serial exclude
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.1
 * @since 14/08/2024
 */
public class LogicCityImp extends UnicastRemoteObject implements LogicCityInterface {

    @Serial
    private static final long serialVersionUID = 5L;
    /**
     * Costruttore della classe {@code LogicCityImp}.
     *
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    public LogicCityImp() throws RemoteException {
        super();
    }

    /**
     * La classe {@code WeatherTableData} elabora i dati meteorologici delle città.
     * <p>
     * Fornisce metodi per calcolare la media dei punteggi, il conteggio dei record
     * e i commenti per diverse categorie meteorologiche.
     * </p>
     */
    public static class WeatherTableData {

        /**
         * Array di chiavi per i dati meteorologici.
         */
        public static final String[] KEYS = {
                "wind",
                "humidity",
                "pressure",
                "temperature",
                "precipitation",
                "glacierElevation",
                "glacierMass"
        };

        /**
         * Mappa per archiviare il punteggio per ciascuna categoria di dati.
         */
        private final Map<String, Float> categoryScore = new HashMap<>();

        /**
         * Mappa per archiviare il conteggio dei record per ciascuna categoria di dati.
         */
        private final Map<String, Integer> categoryRecordCounts = new HashMap<>();

        /**
         * Mappa per archiviare i commenti relativi a ciascuna categoria di dati.
         */
        private final Map<String, List<String>> categoryComments = new HashMap<>();

        /**
         * Costruttore della classe {@code WeatherTableData}.
         * <p>
         * Elabora i dati meteorologici dei record specificati.
         * </p>
         *
         * @param weatherRecords Un array di record meteorologici.
            * @throws IllegalArgumentException Se non vengono forniti dati meteorologici.
         */
        public WeatherTableData(RecordWeather[] weatherRecords) {
            if (weatherRecords == null || weatherRecords.length == 0) {
                throw new IllegalArgumentException("Nessun dato meteorologico fornito.");
            }

            for (RecordWeather record : weatherRecords) {
                processCategory(record.wind(), KEYS[0]);
                processCategory(record.humidity(), KEYS[1]);
                processCategory(record.pressure(), KEYS[2]);
                processCategory(record.temperature(), KEYS[3]);
                processCategory(record.precipitation(), KEYS[4]);
                processCategory(record.glacierElevation(), KEYS[5]);
                processCategory(record.glacierMass(), KEYS[6]);
            }
        }

        /**
         * Processa i dati meteorologici per una categoria specifica.
         * <p>
         * Aggiorna i punteggi, i conteggi dei record e i commenti per la categoria
         * data.
         * </p>
         *
         * @param data     I dati meteorologici da processare.
         * @param category La categoria meteorologica a cui appartengono i dati.
         */
        private void processCategory(RecordWeather.WeatherData data, String category) {
            if (data == null) {
                return;
            }

            if (data.score() != null) {
                categoryScore.put(category, categoryScore.getOrDefault(category, 0f) + data.score());
                categoryRecordCounts.put(category, categoryRecordCounts.getOrDefault(category, 0) + 1);
            }

            if (data.comment() != null) {
                List<String> comments = categoryComments.getOrDefault(category, new ArrayList<>());
                comments.add(data.comment());
                categoryComments.put(category, comments);
            }
        }

        /**
         * Ottiene la media dei punteggi per una categoria meteorologica specifica.
         *
         * @param category La categoria meteorologica desiderata.
         * @return La media dei punteggi per la categoria o {@code null} se non ci sono
         *         record.
         */
        public Integer getCategoryAvgScore(String category) {
            if (category == null || getCategoryRecordCount(category) == 0) {
                return null;
            }
            return Math.round(categoryScore.getOrDefault(category, 0.0f) / getCategoryRecordCount(category));
        }

        /**
         * Ottiene il conteggio dei record per una categoria meteorologica specifica.
         *
         * @param category La categoria meteorologica desiderata.
         * @return Il conteggio dei record per la categoria.
         */
        public int getCategoryRecordCount(String category) {
            return categoryRecordCounts.getOrDefault(category, 0);
        }

        /**
         * Ottiene una lista di commenti per una categoria meteorologica specifica.
         *
         * @param category La categoria meteorologica desiderata.
         * @return Una lista di commenti per la categoria o una lista vuota se non ci
         *         sono commenti.
         */
        public List<String> getCategoryComments(String category) {
            return categoryComments.getOrDefault(category, new ArrayList<>());
        }
    }

    /**
     * Implementa il metodo per ottenere i dati meteorologici elaborati.
     *
     * @param weatherRecords Un array di record meteorologici.
     * @return Un oggetto {@code WeatherTableData} con i dati elaborati.
     * @throws RemoteException Se si verifica un errore durante la comunicazione RMI.
     * @throws IllegalArgumentException Se non vengono forniti dati meteorologici.
     */
    @Override
    public WeatherTableData getWeatherTableData(RecordWeather[] weatherRecords) throws RemoteException {
        if (weatherRecords == null) {
            throw new IllegalArgumentException("L'array di record meteorologici non può essere nullo.");
        }
        return new WeatherTableData(weatherRecords);
    }
}
