package client.GUI.panels;

import javax.swing.JButton;

import client.GUI.GUI;
import client.GUI.Widget;
import client.GUI.layouts.TwoRows;
import client.models.MainModel;
import shared.utils.Interfaces;

/**
 * La classe {@code OperatorHome} rappresenta il pannello principale per gli
 * operatori dell'applicazione.
 * <p>
 * Da questo pannello, gli operatori possono scegliere di registrarsi o accedere
 * all'applicazione. Questa classe gestisce la navigazione tra il pannello di
 * registrazione e quello di login tramite i pulsanti corrispondenti.
 * </p>
 *
 * @see GUI
 * @see Widget
 * @see TwoRows
 * @see MainModel
 * @see Interfaces
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
    public static final String ID = "OperatorHome";

    /**
     * Riferimento all'interfaccia utente grafica (GUI) associata alla barra del
     * menu.
     */
    private GUI gui;

    /**
     * Pulsante per navigare alla schermata di registrazione degli operatori.
     */
    private final JButton buttonToRegistration = new Widget.Button("Registrati");

    /**
     * Pulsante per navigare alla schermata di login degli operatori.
     */
    private final JButton buttonToLogin = new Widget.Button("Accedi");

    /**
     * Crea una nuova istanza di {@code OperatorHome}.
     * <p>
     * Questo costruttore inizializza un nuovo pannello per la home dell'operatore.
     * </p>
     */
    public OperatorHome() {
    }

    /**
     * Aggiunge un {@code ActionListener} ai pulsanti "Registrati" e "Accedi" per
     * gestire la navigazione tra i pannelli.
     * <p>
     * Quando uno dei pulsanti viene premuto, il pannello corrispondente viene
     * visualizzato. Il pulsante "Registrati" naviga al pannello di registrazione
     * per gli operatori, mentre il pulsante "Accedi" naviga al pannello di login.
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
     * <p>
     * Questo metodo può essere utilizzato per eseguire operazioni di inizializzazione
     * aggiuntive in futuro, se necessario.
     * </p>
     *
     * @param args Argomenti aggiuntivi (non utilizzati in questo caso).
     */
    @Override
    public void onOpen(Object[] args) {
    }
}