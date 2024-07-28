package GUI.panels;

import javax.swing.JButton;

import GUI.GUI;
import GUI.Widget;
import GUI.layouts.TwoRows;
import models.MainModel;
import utils.Interfaces;

/**
 * La classe {@code OperatorHome} rappresenta il pannello principale per gli
 * operatori dell'applicazione.
 * <p>
 * Da questo pannello, gli operatori possono scegliere di registrarsi o accedere
 * all'applicazione.
 * </p>
 * 
 * @see GUI.GUI
 * @see GUI.Widget
 * @see GUI.layouts.TwoRows
 * @see models.MainModel
 * @see utils.Interfaces
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 17/09/2023
 */
public class OperatorHome extends TwoRows implements Interfaces.UIPanel {

    /**
     * L'ID univoco di questo pannello. Viene utilizzato per identificare e navigare
     * tra i diversi pannelli dell'applicazione.
     */
    public static String ID = "OperatorHome";

    /**
     * Riferimento all'interfaccia utente grafica (GUI) associata alla barra del
     * menu.
     */
    private GUI gui;

    /**
     * Pulsante per navigare alla schermata di registrazione degli operatori.
     */
    private JButton buttonToRegistration = new Widget.Button("Registrati");

    /**
     * Pulsante per navigare alla schermata di login degli operatori.
     */
    private JButton buttonToLogin = new Widget.Button("Accedi");

    /**
     * Crea una nuova istanza di OperatorHome.
     *
     * @param mainModel Il modello principale dell'applicazione.
     */
    public OperatorHome(MainModel mainModel) {
    }

    /**
     * Aggiunge un {@code ActionListener} ai pulsanti "Registrati" e "Accedi" per
     * gestire la navigazione tra i pannelli.
     * <p>
     * Quando uno dei pulsanti viene premuto, il pannello corrispondente viene
     * visualizzato.
     * </p>
     */
    public void addActionEvent() {
        buttonToRegistration.addActionListener(e -> {
            gui.goToPanel(OperatorRegister.ID, null);
        });

        buttonToLogin.addActionListener(e -> {
            gui.goToPanel(OperatorLogin.ID, null);
        });
    }

    
    
    @Override
    public OperatorHome createPanel(GUI gui) {
        this.gui = gui;

        addTop(new Widget.LogoLabel());
        addBottom(buttonToRegistration);
        addBottom(buttonToLogin);

        gui.appTheme.registerPanel(topPanel);
        gui.appTheme.registerPanel(bottomPanel);

        addActionEvent();

        return this;
    }

    
    
    @Override
    public String getID() {
        return ID;
    }

    /**
     * Invocato quando il pannello viene aperto. Non esegue azioni specifiche quando
     * il pannello viene aperto.
     *
     * @param args Argomenti aggiuntivi (non utilizzati in questo caso).
     */
    @Override
    public void onOpen(Object[] args) {
    }

}
