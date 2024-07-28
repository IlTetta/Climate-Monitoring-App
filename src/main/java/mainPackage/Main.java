package mainPackage;
import GUI.GUI;
import GUI.panels.Loading;
import models.MainModel;

/**
 * La classe {@code Main} &egrave; il punto di ingresso principale dell'applicazione.
 * <p>
 * Contiene un'istanza dell'interfaccia utente {@code GUI} e del modello
 * principale {@code MainModel}.
 * </p>
 * <p>
 * Questa classe &egrave; responsabile per l'inizializzazione dell'applicazione e il
 * lancio dell'interfaccia utente.
 * </p>
 * 
 * @see GUI
 * @see GUI.panels.Loading
 * @see models.MainModel
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/2023
 */

public class Main {

    private GUI gui;
    private MainModel mainModel;

    /**
     * Costruttore della classe {@code Main}.
     * <p>
     * Crea un nuovo oggetto {@code MainModel} per gestire la logica
     * dell'applicazione.
     * </p>
     */
    public Main() {
        mainModel = new MainModel();
    }

    /**
     * Metodo per avviare l'interfaccia utente.
     * <p>
     * Crea un'istanza di {@code GUI}, aggiunge i pannelli necessari e passa al
     * pannello di caricamento (Loading).
     * </p>
     */
    public void launchGUI() {
        gui = new GUI(mainModel);
        gui.addPanels();
        gui.goToPanel(Loading.ID, null);
    }

    /**
     * Il metodo principale dell'applicazione.
     * <p>
     * Crea un'istanza di {@code Main} e avvia l'interfaccia utente chiamando il
     * metodo {@code launchGUI}.
     * </p>
     * 
     * @param args Gli argomenti della riga di comando (non utilizzati in questo
     *             caso).
     * 
     */
    public static void main(String[] args) {
        Main mainInstance = new Main();
        mainInstance.launchGUI();
    }
}
