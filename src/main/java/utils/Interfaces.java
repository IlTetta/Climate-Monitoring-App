
package utils;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import GUI.GUI;

/**
 * L'interfaccia {@code Interfaces} definisce una serie di interfacce e
 * contratti che rappresentano diversi aspetti
 * e entit&agrave; all'interno del sistema. Queste interfacce definiscono i
 * metodi e le
 * propriet&agrave; che devono essere implementati
 * da classi specifiche per fornire funzionalit&agrave; legate a citt&agrave;,
 * operatori,
 * centri, dati meteorologici, finestre UI e pannelli UI.
 * 
 * <p>
 * Le interfacce definite in questo package includono:
 * <ul>
 * <li>{@link RecordCity}: Rappresenta i dati relativi ad una citt&agrave;.</li>
 * <li>{@link RecordOperator}: Rappresenta i dati di un operatore registrato nel
 * sistema.</li>
 * <li>{@link RecordCenter}: Rappresenta i dati relativi ad un centro di
 * monitoraggio.</li>
 * <li>{@link RecordWeather}: Rappresenta i dati meteorologici per un centro e
 * una citt&agrave; specifiche.</li>
 * <li>{@link WeatherData}: Rappresenta i dati specifici delle condizioni
 * meteorologiche.</li>
 * <li>{@link UIWindows}: Definisce i metodi necessari per gestire le finestre
 * UI.</li>
 * <li>{@link UIPanel}: Definisce i metodi necessari per creare e gestire i
 * pannelli UI.</li>
 * </ul>
 * 
 * <p>
 * Queste interfacce forniscono un contratto standard che deve essere seguito
 * dalle classi che implementano queste funzionalit&agrave;
 * nel sistema.
 * </p>
 * 
 * @see GUI.GUI
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */

public class Interfaces {

    /**
     * L'interfaccia {@code RecordCity} definisce il contratto per i dati relativi
     * ad una citt&agrave;.
     * <p>
     * Questi dati includono informazioni come l'identificatore della citt&agrave;,
     * il nome,
     * il nome in formato ASCII, il codice del paese,
     * il nome del paese, la latitudine e la longitudine geografica della
     * citt&agrave;.
     * </p>
     * 
     * <p>
     * Ogni classe che implementa questa interfaccia dovrebbe fornire
     * implementazioni per i seguenti metodi:
     * <ul>
     * <li>{@link #getID()}: Questo metodo restituisce l'identificatore unico
     * della citt&agrave;.</li>
     * 
     * <li>{@link #getName()}: Questo metodo restituisce il nome della
     * citt&agrave;.</li>
     * 
     * <li>{@link #getASCIIName()}: Questo metodo restituisce il nome della
     * citt&agrave; in
     * formato ASCII.</li>
     * 
     * <li>{@link #getCountryCode()}: Questo metodo restituisce il codice del paese
     * a cui appartiene la citt&agrave;.</li>
     * 
     * <li>{@link #getCountryName()}: Questo metodo restituisce il nome del paese a
     * cui appartiene la citt&agrave;.</li>
     * 
     * <li>{@link #getLatitude()}: Questo metodo restituisce la latitudine
     * geografica della citt&agrave;.</li>
     * 
     * <li>{@link #getLongitude()}: Questo metodo restituisce la longitudine
     * geografica della citt&agrave;.</li>
     * 
     * <li>{@link #toString()}: Questo metodo restituisce una rappresentazione
     * testuale della citt&agrave;.</li>
     * </ul>
     */
    public interface RecordCity {

        /**
         * Restituisce l'identificatore unico della citt&agrave;.
         * 
         * @return L'identificatore unico della citt&agrave;.
         */
        Integer getID();

        /**
         * Restituisce il nome della citt&agrave;.
         * 
         * @return Il nome della citt&agrave;.
         */
        String getName();

        /**
         * Restituisce il nome della citt&agrave; in formato ASCII.
         * 
         * @return Il nome della citt&agrave; in formato ASCII.
         */
        String getASCIIName();

        /**
         * Restituisce il codice del paese a cui appartiene la citt&agrave;.
         * 
         * @return Il codice del paese a cui appartiene la citt&agrave;.
         */
        String getCountryCode();

        /**
         * Restituisce il nome del paese a cui appartiene la citt&agrave;.
         * 
         * @return Il nome del paese a cui appartiene la citt&agrave;.
         */
        String getCountryName();

        /**
         * Restituisce la latitudine geografica della citt&agrave;.
         * 
         * @return La latitudine geografica della citt&agrave;.
         */
        double getLatitude();

        /**
         * Restituisce la longitudine geografica della citt&agrave;.
         * 
         * @return La longitudine geografica della citt&agrave;.
         */
        double getLongitude();

        /**
         * Restituisce una rappresentazione testuale della citt&agrave;.
         * 
         * @return Una rappresentazione testuale della citt&agrave;.
         */
        String toString();
    }

    /**
     * L'interfaccia {@code RecordOperator} definisce il contratto per i dati
     * dell'operatore registrato nel sistema.
     * <p>
     * Questi dati includono informazioni come l'identificatore dell'operatore, il
     * nome e cognome, il codice fiscale, l'email,
     * il nome utente, la password e l'identificatore del centro associato
     * all'operatore (se presente).
     * </p>
     * 
     * <p>
     * Ogni classe che implementa questa interfaccia dovrebbe fornire
     * implementazioni per i seguenti metodi:
     * <ul>
     * <li>{@link #getID()}: Questo metodo restituisce l'identificatore unico
     * dell'operatore.</li>
     * 
     * <li>{@link #getNameSurname()}: Questo metodo restituisce il nome e cognome
     * dell'operatore.</li>
     * 
     * <li>{@link #getTaxCode()}: Questo metodo restituisce il codice fiscale
     * dell'operatore.</li>
     * 
     * <li>{@link #getEmail()}: Questo metodo restituisce l'indirizzo email
     * dell'operatore.</li>
     * 
     * <li>{@link #getUsername()}: Questo metodo restituisce il nome utente
     * dell'operatore.</li>
     * 
     * <li>{@link #getPassword()}: Questo metodo restituisce la password
     * dell'operatore.</li>
     * 
     * <li>{@link #getCenterID()}: Questo metodo restituisce l'identificatore unico
     * del centro associato all'operatore, se presente.</li>
     * </ul>
     * 
     */
    public interface RecordOperator {

        /**
         * Restituisce l'identificatore unico dell'operatore.
         * 
         * @return L'identificatore unico dell'operatore.
         */
        Integer getID();

        /**
         * Restituisce il nome e cognome dell'operatore.
         * 
         * @return Il nome e cognome dell'operatore.
         */
        String getNameSurname();

        /**
         * Restituisce il codice fiscale dell'operatore.
         * 
         * @return Il codice fiscale dell'operatore.
         */
        String getTaxCode();

        /**
         * Restituisce l'indirizzo email dell'operatore.
         * 
         * @return L'indirizzo email dell'operatore.
         */
        String getEmail();

        /**
         * Restituisce il nome utente dell'operatore.
         * 
         * @return Il nome utente dell'operatore.
         */
        String getUsername();

        /**
         * Restituisce la password dell'operatore.
         * 
         * @return La password dell'operatore.
         */
        String getPassword();

        /**
         * Restituisce l'identificatore unico del centro associato all'operatore, se
         * presente.
         * 
         * @return L'identificatore unico del centro associato all'operatore, o null se
         *         l'operatore non &egrave; associato a un centro.
         */
        Integer getCenterID();

        String toString();
    }

    /**
     * L'interfaccia {@code RecordCenter} definisce il contratto per i dati relativi
     * ad un Centro di Monitoraggio.
     * <p>
     * Questi dati includono informazioni come il nome del centro, il nome della
     * via, il numero civico, il CAP, il nome del comune,
     * la sigla della provincia e l'elenco delle citt&agrave; associate a questo centro.
     * </p>
     * 
     * <p>
     * Ogni classe che implementa questa interfaccia dovrebbe fornire
     * implementazioni per i seguenti metodi:
     * <ul>
     * <li>{@link #getID()}: Questo metodo restituisce l'identificatore unico del
     * centro di monitoraggio.</li>
     * 
     * <li>{@link #getCenterName()}: Questo metodo restituisce il nome del
     * centro.</li>
     * 
     * <li>{@link #getStreetName()}: Questo metodo restituisce il nome della
     * via.</li>
     * 
     * <li>{@link #getStreetNumber()}: Questo metodo restituisce il numero
     * civico.</li>
     * 
     * <li>{@link #getCAP()}: Questo metodo restituisce il codice di avviamento
     * postale (CAP).</li>
     * 
     * <li>{@link #getTownName()}: Questo metodo restituisce il nome del
     * comune.</li>
     * 
     * <li>{@link #getDistrictName()}: Questo metodo restituisce la sigla della
     * provincia.</li>
     * 
     * <li>{@link #getCityIDs()}: Questo metodo restituisce un elenco di
     * identificatori unici delle citt&agrave; associate a questo centro.</li>
     * </ul>
     * 
     */
    public interface RecordCenter {

        /**
         * Restituisce l'identificatore unico del centro.
         * 
         * @return L'identificatore unico del centro.
         */
        Integer getID();

        /**
         * Restituisce il nome del cnetro.
         * 
         * @return Il nome del cnetro.
         */
        String getCenterName();

        /**
         * Restituisce il nome della via.
         * 
         * @return Il nome della via.
         */
        String getStreetName();

        /**
         * Restituisce il numero civico.
         * 
         * @return Il numero civico.
         */
        String getStreetNumber();

        /**
         * Restituisce il codice di avviamento postale (CAP).
         * 
         * @return Il codice di avviamento postale (CAP).
         */
        String getCAP();

        /**
         * Restituisce il nome del comune.
         * 
         * @return Il nome del comune.
         */
        String getTownName();

        /**
         * Restituisce la sigla della provincia.
         * 
         * @return La sigla della provincia.
         */
        String getDistrictName();

        /**
         * Restituisce un elenco di identificatori unici delle citt&agrave; associate a questo
         * centro.
         * 
         * @return Un array di identificatori unici delle citt&agrave; associate a questo
         *         centro.
         */
        Integer[] getCityIDs();
    }

    /**
     * L'interfaccia {@code RecordWeather} definisce il contratto per i dati
     * meteorologici registrati associati a una specifica citt&agrave; e centro.
     * <p>
     * Questi dati includono informazioni come la data della registrazione, i dati
     * meteorologici effettivi (come velocit&agrave; del vento, temperatura, ecc.)
     * e l'ID della citt&agrave; e del centro associati a questa registrazione.
     * </p>
     * 
     * <p>
     * Ogni classe che implementa questa interfaccia dovrebbe fornire
     * implementazioni per i seguenti metodi:
     * <ul>
     * <li>{@link #getID()}: Questo metodo restituisce l'identificatore unico dei
     * dati meteorologici registrati.</li>
     * 
     * <li>{@link #getCenterID()}: Questo metodo restituisce l'identificatore unico
     * del centro associata a questi dati meteorologici registrati.</li>
     * 
     * <li>{@link #getCityID()}: Questo metodo restituisce l'identificatore unico
     * della citt&agrave; associata a questi dati meteorologici registrati.</li>
     * 
     * <li>{@link #getDate()}: Questo metodo restituisce la data della registrazione
     * dei dati meteorologici.</li>
     * 
     * <li>{@link #getWind()}: Questo metodo restituisce i dati meteorologici
     * relativi alla velocit&agrave; del vento.</li>
     * 
     * <li>{@link #getHumidity()}: Questo metodo restituisce i dati meteorologici
     * relativi all'umidit&agrave;.</li>
     * 
     * <li>{@link #getPressure()}: Questo metodo restituisce i dati meteorologici
     * relativi alla pressione atmosferica.</li>
     * 
     * <li>{@link #getTemperature()}: Questo metodo restituisce i dati meteorologici
     * relativi alla temperatura.</li>
     * 
     * <li>{@link #getPrecipitation()}: Questo metodo restituisce i dati
     * meteorologici relativi alle precipitazioni.</li>
     * 
     * <li>{@link #getGlacierElevation()}: Questo metodo restituisce i dati
     * meteorologici relativi all'altitudine dei ghiacciai.</li>
     * 
     * <li>{@link #getMassOfGlaciers()}: Questo metodo restituisce i dati
     * meteorologici relativi alla massa dei ghiacciai.</li>
     * 
     * <li>{@link #toString()}: Questo metodo restituisce una rappresentazione
     * testuale dei dati meteorologici registrati.</li>
     * </ul>
     * 
     */
    public interface RecordWeather {

        /**
         * Restituisce l'identificatore unico dei dati meteorologici registrati.
         * 
         * @return L'identificatore unico dei dati meteorologici registrati.
         */
        Integer getID();

        /**
         * Restituisce l'identificatore unico del centro associata a questi dati
         * meteorologici registrati.
         * 
         * @return L'identificatore unico del centro associato.
         */
        Integer getCenterID();

        /**
         * Restituisce l'identificatore unico della citt&agrave; associata a questi dati
         * meteorologici registrati.
         * 
         * @return L'identificatore unico della citt&agrave; associata.
         */
        Integer getCityID();

        /**
         * Restituisce la data della registrazione dei dati meteorologici.
         * 
         * @return La data della registrazione dei dati meteorologici.
         */
        String getDate();

        /**
         * Restituisce i dati meteorologici relativi alla velocit&agrave; del vento.
         * 
         * @return I dati meteorologici relativi alla velocit&agrave; del vento.
         */
        WeatherData getWind();

        /**
         * Restituisce i dati meteorologici relativi all'umidit&agrave;.
         * 
         * @return I dati meteorologici relativi all'umidit&agrave;.
         */
        WeatherData getHumidity();

        /**
         * Restituisce i dati meteorologici relativi alla pressione atmosferica.
         * 
         * @return I dati meteorologici relativi alla pressione atmosferica.
         */
        WeatherData getPressure();

        /**
         * Restituisce i dati meteorologici relativi alla temperatura.
         * 
         * @return I dati meteorologici relativi alla temperatura.
         */
        WeatherData getTemperature();

        /**
         * Restituisce i dati meteorologici relativi alle precipitazioni.
         * 
         * @return I dati meteorologici relativi alle precipitazioni.
         */
        WeatherData getPrecipitation();

        /**
         * Restituisce i dati meteorologici relativi all'altitudine dei ghiacciai.
         * 
         * @return I dati meteorologici relativi all'altitudine dei ghiacciai.
         */
        WeatherData getGlacierElevation();

        /**
         * Restituisce i dati meteorologici relativi alla massa dei ghiacciai.
         * 
         * @return I dati meteorologici relativi alla massa dei ghiacciai.
         */
        WeatherData getMassOfGlaciers();

        /**
         * Restituisce una rappresentazione testuale dei dati meteorologici registrati.
         * 
         * @return Una rappresentazione testuale dei dati meteorologici registrati.
         */
        String toString();
    }

    /**
     * L'interfaccia {@code WeatherData} definisce il contratto per i dati
     * meteorologici associati a una registrazione meteorologica.
     * <p>
     * I dati meteorologici includono un punteggio e un commento relativi a una
     * specifica categoria di dati meteorologici, come la temperatura,
     * la pressione, la velocit&agrave; del vento, ecc.
     * </p>
     * 
     * <p>
     * Ogni classe che implementa questa interfaccia dovrebbe fornire
     * implementazioni per i seguenti metodi:
     * <ul>
     * <li>{@link #getScore()}: Questo metodo restituisce il punteggio associato ai
     * dati meteorologici.</li>
     * 
     * <li>{@link #getComment()}: Questo metodo restituisce il commento associato ai
     * dati meteorologici, che fornisce una descrizione o ulteriori
     * informazioni sui dati.</li>
     * </ul>
     * 
     */
    public interface WeatherData {

        /**
         * Restituisce il punteggio associato ai dati meteorologici.
         * 
         * @return Il punteggio associato ai dati meteorologici.
         */
        Integer getScore();

        /**
         * Restituisce il commento associato ai dati meteorologici, che fornisce una
         * descrizione o ulteriori informazioni sui dati.
         * 
         * @return Il commento associato ai dati meteorologici.
         */
        String getComment();

        String toString();
    }

    /**
     * L'interfaccia {@code UIWindows} definisce il contratto per le finestre
     * dell'interfaccia utente dell'applicazione.
     * <p>
     * Una finestra pu&ograve; contenere uno o pi&ugrave; pannelli e fornisce metodi
     * per ottenere
     * il pannello principale, lo scrollpane
     * e il pannello del contenuto, nonché per impostare informazioni
     * sull'applicazione.
     * </p>
     * 
     * <p>
     * Ogni classe che implementa questa interfaccia dovrebbe fornire
     * implementazioni per i seguenti metodi:
     * <ul>
     * <li>{@link #getMainPanel()}: Questo metodo restituisce il pannello principale
     * della finestra, che pu&ograve; contenere altri pannelli
     * o componenti dell'interfaccia utente.</li>
     * 
     * <li>{@link #getScrollPanel()}: Questo metodo restituisce lo scrollpane
     * associato alla finestra. Lo scrollpane pu&ograve; essere utilizzato
     * per visualizzare contenuti pi&ugrave; grandi della finestra stessa.</li>
     * 
     * <li>{@link #getContentPanel()}: Questo metodo restituisce il pannello del
     * contenuto della finestra, che contiene il contenuto principale
     * visualizzato nella finestra stessa.</li>
     * 
     * <li>{@link #setAppInfo(String)}: Questo metodo consente di impostare
     * informazioni sull'applicazione, ad esempio un titolo o una descrizione,
     * che possono essere visualizzate nella finestra.</li>
     * </ul>
     * 
     */
    public interface UIWindows {

        /**
         * Restituisce il pannello principale della finestra, che pu&ograve; contenere
         * altri
         * pannelli o componenti dell'interfaccia utente.
         * 
         * @return Il pannello principale della finestra.
         */
        JPanel getMainPanel();

        /**
         * Restituisce lo scrollpane associato alla finestra.
         * <p>
         * Lo scrollpane pu&ograve; essere
         * utilizzato per visualizzare contenuti pi&ugrave; grandi della finestra
         * stessa.
         * </p>
         * 
         * @return Lo scrollpane della finestra.
         */
        JScrollPane getScrollPanel();

        /**
         * Restituisce il pannello di contenuto del pannello dell'interfaccia utente.
         * 
         * <p>
         * Il pannello di contenuto &egrave; responsabile per la visualizzazione del
         * contenuto
         * principale del pannello
         * dell'interfaccia utente.
         * </p>
         * <p>
         * &#201; il componente in cui vengono posizionati gli elementi UI, come
         * etichette,
         * campi di input,
         * bottoni, tabelle, ecc., per rappresentare il contenuto specifico del
         * pannello.
         * </p>
         * 
         * @return Il pannello di contenuto dell'interfaccia utente.
         */
        JPanel getContentPanel();

        /**
         * Imposta le informazioni sull'applicazione visualizzate nella finestra
         * principale.
         *
         * <p>
         * Questo metodo consente di aggiornare le informazioni sull'applicazione
         * mostrate agli utenti
         * nella finestra principale dell'interfaccia utente.
         * </p>
         * <p>
         * Le informazioni possono
         * essere utilizzate
         * per fornire dettagli sull'applicazione o sullo stato dell'operazione
         * corrente.
         * </p>
         *
         * @param text Il testo da visualizzare come informazioni sull'applicazione.
         */
        void setAppInfo(String text);
    }

    /**
     * L'interfaccia {@code UIPanel} definisce il contratto per i pannelli
     * dell'interfaccia utente dell'applicazione.
     * <p>
     * Un pannello rappresenta una vista o una schermata dell'applicazione ed
     * &egrave;
     * responsabile della sua creazione e gestione.
     * </p>
     * 
     * <p>
     * Ogni classe che implementa questa interfaccia dovrebbe fornire
     * implementazioni per i seguenti metodi:
     * <ul>
     * <li>{@link #createPanel(GUI)}: Questo metodo crea il pannello
     * dell'interfaccia
     * utente e lo restituisce. Deve essere implementato
     * da ciascuna classe che rappresenta un pannello specifico
     * dell'applicazione.</li>
     * 
     * <li>{@link #onOpen(Object[])}: Questo metodo viene chiamato quando il
     * pannello
     * viene aperto. Pu&ograve; essere utilizzato per inizializzare
     * il pannello con dati o eseguire azioni specifiche quando il pannello &egrave;
     * visualizzato.</li>
     * 
     * <li>{@link #getID()}: Questo metodo restituisce l'identificatore unico del
     * pannello. L'identificatore &egrave; utilizzato per la gestione
     * e la navigazione tra i diversi pannelli dell'applicazione.</li>
     * </ul>
     */
    public interface UIPanel {

        /**
         * Crea il pannello dell'interfaccia utente e lo restituisce.
         * 
         * @param gui L'istanza dell'oggetto GUI utilizzata per la creazione del
         *            pannello.
         * @return Il pannello dell'interfaccia utente creato.
         */
        JPanel createPanel(GUI gui);

        /**
         * Questo metodo viene chiamato quando il pannello viene aperto.
         * <p>
         * Pu&ograve; essere
         * utilizzato per inizializzare
         * il pannello con dati o eseguire azioni specifiche quando il pannello &egrave;
         * visualizzato.
         * </p>
         * 
         * @param args Un array di argomenti opzionali da passare al pannello quando
         *             viene aperto.
         */
        void onOpen(Object[] args);

        /**
         * Restituisce l'identificatore unico del pannello.
         * <p>
         * L'identificatore &egrave;
         * utilizzato per la gestione
         * e la navigazione tra i diversi pannelli dell'applicazione.
         * </p>
         * 
         * @return L'identificatore unico del pannello.
         */
        String getID();
    }
}
