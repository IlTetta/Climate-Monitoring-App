package client.GUI.panels;

import java.awt.*;
import javax.swing.*;

import client.GUI.GUI;
import client.GUI.Widget;
import client.GUI.layouts.TwoRows;
import client.models.MainModel;
import shared.utils.Constants;
import shared.utils.Interfaces;

/**
 * La classe {@code Loading} rappresenta un pannello di caricamento animato che
 * viene visualizzato all'avvio dell'applicazione.
 * <p>
 * Questo pannello mostra il nome dell'applicazione con una serie di punti che
 * si muovono per simulare un caricamento. L'animazione prosegue fino a quando
 * il pannello non reindirizza automaticamente all'homepage dell'applicazione.
 * </p>
 *
 * @see GUI
 * @see Widget
 * @see TwoRows
 * @see MainModel
 * @see Constants
 * @see Interfaces
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 17/09/2023
 */
public class Loading extends TwoRows implements Interfaces.UIPanel {

    /**
     * L'ID univoco di questo pannello. Viene utilizzato per identificare e navigare
     * tra i diversi pannelli dell'applicazione.
     */
    public static final String ID = "Loading";

    /**
     * Etichetta per visualizzare il nome dell'applicazione durante l'animazione di
     * caricamento.
     */
    private final JLabel labelAppName = new JLabel();

    /**
     * Timer utilizzato per gestire l'animazione di caricamento.
     * <p>
     * Il timer aggiorna il testo dell'etichetta {@code labelAppName} a intervalli
     * regolari per simulare il caricamento.
     * </p>
     */
    private Timer timer;

    /**
     * Crea una nuova istanza di {@code Loading}.
     * <p>
     * Questo costruttore inizializza il pannello di caricamento. Non esegue alcuna
     * operazione aggiuntiva al momento della creazione.
     * </p>
     */
    public Loading() {
    }

    /**
     * Avvia l'animazione di caricamento.
     * <p>
     * Questo metodo avvia il timer che gestisce l'animazione del caricamento e
     * la transizione automatica al pannello dell'homepage.
     * </p>
     */
    public void runAnimation() {
        timer.start();
    }

    @Override
    public Loading createPanel(GUI gui) {

        timer = new Timer(700, e -> {
            int animationSteps = 5;
            int currentStep = (int) (e.getWhen() / 700 % animationSteps);

            labelAppName.setText(Constants.APP_TITLE + ".".repeat((currentStep % 4)));

            if (currentStep == animationSteps - 1) {
                timer.stop();
                gui.goToPanel(Home.ID, null);
            }
        });

        labelAppName.setText(Constants.APP_TITLE);
        labelAppName.setFont(new Font("Ink Free", Font.BOLD, 35));

        addTop(new Widget.LogoLabel(2));
        addBottom(labelAppName);

        gui.appTheme.registerPanel(topPanel);
        gui.appTheme.registerPanel(bottomPanel);
        gui.appTheme.registerLabel(labelAppName);

        return this;
    }

    @Override
    public String getID() {
        return ID;
    }

    /**
     * Invocato quando il pannello viene aperto.
     * <p>
     * Questo metodo avvia l'animazione di caricamento al momento dell'apertura
     * del pannello.
     * </p>
     *
     * @param args Argomenti aggiuntivi (non utilizzati in questo caso).
     */
    @Override
    public void onOpen(Object[] args) {
        runAnimation();
    }
}
