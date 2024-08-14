package shared.utils;

import java.awt.Dimension;

/**
 * La classe {@code Constants} fornisce una serie di costanti e definizioni
 * utilizzate
 * nell'applicazione.
 * <p>
 * Queste costanti includono separatori CSV, titolo dell'applicazione, indici di
 * dati, percorsi dei file, dimensioni GUI predefinite e dati predefiniti.
 * </p>
 * 
 * <p>
 * La classe &egrave; progettata per memorizzare costanti utilizzate in tutto il
 * codice
 * e semplificare eventuali modifiche future.
 * </p>
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.1
 * @since 14/08/2024
 */
public class Constants {

    /**
     * Stringa vuota predefinita.
     */
    public static final String EMPTY_STRING = "NULL";

    /**
     * Separatore CSV principale utilizzato per separare i campi nei file CSV.
     */
    public static final String CSV_SEPARATOR = ";";

    /**
     * Separatore CSV secondario utilizzato per separare i dati all'interno di un
     * campo CSV.
     */
    public static final String CSV_SUB_SEPARATOR = "|";

    /**
     * Titolo predefinito dell'applicazione.
     */
    public static final String APP_TITLE = "Monitoraggio Climatico";

    /**
     * Questa classe &egrave; progettata per memorizzare costanti utilizzate in
     * tutto il
     * codice dell'applicazione.
     * <p>
     * Non &egrave; possibile creare istanze di questa classe in quanto tutti i
     * membri sono
     * statici e accessibili attraverso la notazione di classe (es.
     * Constants.EMPTY_STRING).
     * </p>
     */
    private Constants() {
    }

    /**
     * La classe interna {@code Legend} fornisce costanti per le legende delle
     * categorie climatiche.
     * <p>
     * Queste costanti sono utilizzate per mostrare all'utente il signinficato dei
     * punteggi nella tabella della citt&agrave;.
     * </p>
     */
    public static final class Legend {

        /**
         * Legenda per la categoria Vento.
         */
        public static final String WIND_LEGEND = "Velocità del vento (km/h):\n" +
                "Fascia 1: 0-10 km/h (Calmi)\n" +
                "Fascia 2: 11-20 km/h (Brezza leggera)\n" +
                "Fascia 3: 21-30 km/h (Brezza moderata)\n" +
                "Fascia 4: 31-40 km/h (Brezza fresca)\n" +
                "Fascia 5: >40 km/h (Vento forte)";

        /**
         * Legenda per la categoria Umidit&agrave;.
         */
        public static final String HUMIDITY_LEGEND = "Umidità (% di umidità):\n" +
                "Fascia 1: 0-20% (Molto secco)\n" +
                "Fascia 2: 21-40% (Secco)\n" +
                "Fascia 3: 41-60% (Moderato)\n" +
                "Fascia 4: 61-80% (Umidità elevata)\n" +
                "Fascia 5: 81-100% (Molto umido)";

        /**
         * Legenda per la categoria Pressione.
         */
        public static final String PRESSURE_LEGEND = "Pressione (in hPa):\n" +
                "Fascia 1: <980 hPa (Bassa pressione)\n" +
                "Fascia 2: 980-1000 hPa (Pressione normale)\n" +
                "Fascia 3: 1001-1020 hPa (Pressione media)\n" +
                "Fascia 4: 1021-1040 hPa (Pressione alta)\n" +
                "Fascia 5: >1040 hPa (Pressione elevata)";

        /**
         * Legenda per la categoria Temperatura.
         */
        public static final String TEMPERATURE_LEGEND = "Temperatura (in °C):\n" +
                "Fascia 1: <-10°C (Molto freddo)\n" +
                "Fascia 2: -10°C a 0°C (Freddo)\n" +
                "Fascia 3: 1°C a 10°C (Mite)\n" +
                "Fascia 4: 11°C a 20°C (Piacevole)\n" +
                "Fascia 5: >20°C (Caldo)";

        /**
         * Legenda per la categoria Precipitazioni.
         */
        public static final String PRECIPITATION_LEGEND = "Precipitazioni (in mm di pioggia):\n" +
                "Fascia 1: 0 mm (Nessuna precipitazione)\n" +
                "Fascia 2: 1-5 mm (Pioviggine)\n" +
                "Fascia 3: 6-20 mm (Pioggia leggera)\n" +
                "Fascia 4: 21-50 mm (Pioggia moderata)\n" +
                "Fascia 5: >50 mm (Pioggia intensa)";

        /**
         * Legenda per la categoria Altitudine dei ghiacciai.
         */
        public static final String GLACIER_ALTITUDE_LEGEND = "Altitudine dei ghiacciai (in metri):\n" +
                "Fascia 1: 0-1000 m (Bassa quota)\n" +
                "Fascia 2: 1001-3000 m (Quota media)\n" +
                "Fascia 3: 3001-5000 m (Alta quota)\n" +
                "Fascia 4: 5001-7000 m (Quota elevata)\n" +
                "Fascia 5: >7000 m (Quota estrema)";

        /**
         * Legenda per la categoria Massa dei ghiacciai.
         */
        public static final String GLACIER_MASS_LEGEND = "Massa dei ghiacciai (in kg):\n" +
                "Fascia 1: <10^6 kg (Massa piccola)\n" +
                "Fascia 2: 10^6-10^8 kg (Massa media)\n" +
                "Fascia 3: 10^8-10^10 kg (Massa grande)\n" +
                "Fascia 4: 10^10-10^12 kg (Massa elevata)\n" +
                "Fascia 5: >10^12 kg (Massa estrema)";

        /**
         * Array contenente tutte le legende delle categorie climatiche.
         */
        public static final String[] LEGENDS = { WIND_LEGEND, HUMIDITY_LEGEND, PRESSURE_LEGEND, TEMPERATURE_LEGEND,
                PRECIPITATION_LEGEND, GLACIER_ALTITUDE_LEGEND, GLACIER_MASS_LEGEND };

        /**
         * Questa classe interna &egrave; progettata per memorizzare costanti per le
         * legende
         * delle categorie climatiche.
         * <p>
         * Non &egrave; possibile creare istanze di questa classe in quanto tutti i
         * membri sono
         * statici.
         * </p>
         */
        private Legend() {

        }
    }

    /**
     * La classe interna {@code Assets} fornisce costanti per i percorsi dei file e
     * delle risorse dell'applicazione.
     */
    public static final class Assets {

        /**
         * Separatore di percorsi.
         */
        public static final String SEPARATOR = "/";

            /**
             * Percorso del logo dell'applicazione.
             */
            public static final String LOGO = getPath("logo.png");

            /**
             * Percorso dell'icona home.
             */
            public static final String HOME = getPath("iconHome.png");

            /**
             * Percorso dell'icona del tema.
             */
            public static final String THEME = getPath("iconTheme.png");

            private static String getPath(String fileName) {
                return SEPARATOR + String.join(SEPARATOR, new String[] { "assets", fileName });
            }


        private Assets() {
        }
    }

    /**
     * La classe {@code GUI} fornisce costanti predefinite per le dimensioni della
     * finestra GUI principale.
     * <p>
     * Queste costanti includono larghezza, altezza e dimensione predefinita per i
     * widget GUI.
     * </p>
     * 
     * <p>
     * La classe &egrave; progettata per semplificare la gestione delle dimensioni e
     * dei
     * layout GUI e fornire valori di dimensioni predefiniti.
     * </p>
     */
    public static final class GUI {
        /**
         * Larghezza predefinita della finestra GUI principale.
         */
        public static final Integer FRAME_WIDTH = 1400;
        /**
         * Altezza predefinita della finestra GUI principale.
         */
        public static final Integer FRAME_HEIGHT = 900;

        /**
         * Dimensione predefinita per i widget GUI.
         */
        public static final Dimension WIDGET_DIMENSION = new Dimension(300, 40);

        /**
         * Questa classe &egrave; progettata per fornire costanti predefinite per le
         * dimensioni
         * della finestra GUI principale e le dimensioni dei widget GUI.
         * <p>
         * Non &egrave; possibile istanziare questa classe in quanto tutti i membri sono
         * statici e accessibili attraverso la notazione di classe.
         * </p>
         */
        private GUI() {
        }
    }

}
