package models.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.data.DataHandler;
import models.record.RecordWeather;
import models.record.RecordWeather.WeatherData;

/**
 * La classe {@code LogicCity} gestisce la logica relativa alle citt&agrave;.
 * <p>
 * Fornisce metodi per l'elaborazione dei dati meteorologici delle citt&agrave;.
 * </p>
 * 
 * @see models.data.DataHandler
 * @see models.record.RecordWeather
 * @see models.record.RecordWeather.WeatherData
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class LogicCity {

    /**
     * Costruttore della classe {@code LogicCity}.
     * 
     * @param dataHandler Il gestore dei dati utilizzato per l'accesso ai dati delle
     *                    citt&agrave;.
     */
    public LogicCity(DataHandler dataHandler) {
    }

    /**
     * La classe {@code WeatherTableData} elabora i dati meteorologici delle citt&agrave;.
     * <p>
     * Fornisce metodi per calcolare la media dei punteggi, il conteggio dei record
     * e i commenti per diverse categorie meteorologiche.
     * </p>
     */
    public static class WeatherTableData {

        /**
         * Array di chiavi per i dati meteorologici.
         */
        public static final String[] keys = {
                "wind",
                "humidity",
                "pressure",
                "temperature",
                "precipitation",
                "glacierElevation",
                "glacierMass", };

        /**
         * Mappa per archiviare il punteggio per ciascuna categoria di dati.
         */
        private Map<String, Float> categoryScore = new HashMap<>();

        /**
         * Mappa per archiviare il conteggio dei record per ciascuna categoria di dati.
         */
        private Map<String, Integer> categoryRecordCounts = new HashMap<>();

        /**
         * Mappa per archiviare i commenti relativi a ciascuna categoria di dati.
         */
        private Map<String, List<String>> categoryComments = new HashMap<>();

        /**
         * Costruttore della classe {@code WeatherTableData}.
         * <p>
         * Elabora i dati meteorologici dei record specificati.
         * </p>
         * 
         * @param weatherRecords Un array di record meteorologici.
         */
        public WeatherTableData(RecordWeather[] weatherRecords) {
            for (RecordWeather record : weatherRecords) {
                processCategory(record.wind(), keys[0]);
                processCategory(record.humidity(), keys[1]);
                processCategory(record.pressure(), keys[2]);
                processCategory(record.temperature(), keys[3]);
                processCategory(record.precipitation(), keys[4]);
                processCategory(record.glacierElevation(), keys[5]);
                processCategory(record.glacierMass(), keys[6]);
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
        private void processCategory(WeatherData data, String category) {
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
            if (getCategoryRecordCount(category) == 0) {
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

}
