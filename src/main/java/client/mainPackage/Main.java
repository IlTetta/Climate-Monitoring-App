package client.mainPackage;
import client.GUI.GUI;
import client.GUI.panels.Loading;
import client.models.MainModel;

/**
 * La classe {@code Main} è il punto di ingresso dell'applicazione client.
 * Essa si occupa di inizializzare il modello principale e di lanciare l'interfaccia grafica utente (GUI).
 * <p>
 *     Il metodo {@code main} avvia l'applicazione creando un'istanza di {@code Main} e chiamando il metodo {@code launchGUI}.
 * </p>
 *
 * @see MainModel
 * @see GUI
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/20023
 */

public class Main {

    /**
     * Istanza di {@code MainModel} utilizzata per accedere ai servizi che gestiscono i dati e la logica dell'applicazione
     */
    private final MainModel mainModel;

    /**
     * Costruttore della classe {@code Main}.
     * <p>
     *     Questo costruttore inizializza un'istanza di {@code MainModel} per accedere ai servizi RMI.
     */
    public Main() {
        mainModel = new MainModel();
    }

    /**
     * Lancia l'interfaccia grafica utente (GUI) dell'applicazione.
     * <p>
     *     Questo metodo crea un'istanza di {@code GUI}, aggiunge i pannelli necessari e
     *     visualizza il pannello di caricamento iniziale.
     * </p>
     *
     * @see GUI#addPanels()
     * @see GUI#goToPanel(String, Object[])
     */
    public void launchGUI() {
        GUI gui = new GUI(mainModel);
        gui.addPanels();
        gui.goToPanel(Loading.ID, null);
    }

    /**
     * Punto di ingresso dell'applicazione client.
     * <p>
     *     Questo metodo avvia l'applicazione creando un'istanza di {@code Main} e chiamando il {@code launchGUI}.
     * </p>
     *
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        Main mainInstance = new Main();
        mainInstance.launchGUI();
    }
}
