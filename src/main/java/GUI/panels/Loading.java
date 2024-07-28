package GUI.panels;

import java.awt.*;

import javax.swing.*;

import GUI.GUI;
import GUI.Widget;
import GUI.layouts.TwoRows;
import models.MainModel;
import utils.Constants;
import utils.Interfaces;

/**
 * La classe {@code Loading} rappresenta un pannello di caricamento animato che
 * viene visualizzato all'avvio dell'applicazione.
 * <p>
 * Questo pannello mostra il nome dell'applicazione con una serie di punti che
 * si muovono per simulare un caricamento.
 * </p>
 * <p>
 * Una volta completata l'animazione, il pannello reindirizza all'homepage
 * dell'applicazione.
 * </p>
 * 
 * @see GUI.GUI
 * @see GUI.Widget
 * @see GUI.layouts.TwoRows
 * @see models.MainModel
 * @see utils.Constants
 * @see utils.Interfaces
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
    public static String ID = "Loading";

    /**
     * Etichetta per visualizzare il nome dell'applicazione durante l'animazione di
     * caricamento.
     */
    private JLabel labelAppName = new JLabel();

    /**
     * Timer utilizzato per gestire l'animazione di caricamento.
     */
    private Timer timer;

    /**
     * Crea una nuova istanza di {@code Loading}.
     *
     * @param mainModel Il modello principale dell'applicazione.
     */
    public Loading(MainModel mainModel) {
    }

    /**
     * Avvia l'animazione di caricamento.
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
        labelAppName.setFont(new Font("Ink Free", Font.CENTER_BASELINE, 35));

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

    @Override
    public void onOpen(Object[] args) {
        runAnimation();
    }

}
