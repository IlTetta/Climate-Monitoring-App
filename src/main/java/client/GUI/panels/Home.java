package client.GUI.panels;

import javax.swing.*;

import client.GUI.GUI;
import client.GUI.Widget;
import client.GUI.layouts.TwoRows;
import client.models.MainModel;
import shared.utils.Interfaces;

/**
 * La classe {@code Home} rappresenta il pannello principale dell'applicazione
 * visualizzato dopo il caricamento iniziale.
 * <p>
 * Questo pannello fornisce all'utente due opzioni principali:
 * <ul>
 *     <li>"Cerca e visualizza dati" per accedere alla funzionalità di ricerca e visualizzazione dei dati.</li>
 *     <li>"Gestisci area operatore" per accedere alla gestione dell'area riservata agli operatori.</li>
 * </ul>
 * </p>
 * <p>
 * L'utente può selezionare una delle opzioni per avviare le funzionalità specifiche dell'applicazione.
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
public class Home extends TwoRows implements Interfaces.UIPanel {

    /**
     * L'ID univoco di questo pannello. Viene utilizzato per identificare e navigare
     * tra i diversi pannelli dell'applicazione.
     */
    public static final String ID = "Home";

    /**
     * Riferimento all'interfaccia utente grafica (GUI) associata alla barra del
     * menu.
     */
    private GUI gui;

    /**
     * Pulsante per navigare alla funzionalità di ricerca e visualizzazione
     * dei dati.
     */
    private final JButton buttonToFind = new Widget.Button("Cerca e visualizza dati");

    /**
     * Pulsante per navigare alla funzionalità di gestione dell'area
     * riservata agli operatori.
     */
    private final JButton buttonToOperator = new Widget.Button("Gestisci area operatore");

    /**
     * Crea una nuova istanza di {@code Home}.
     * <p>
     * Questo costruttore inizializza il pannello principale dell'applicazione. Non
     * esegue operazioni aggiuntive al momento della creazione.
     * </p>
     */
    public Home() {
    }

    /**
     * Aggiunge gli eventi di azione ai pulsanti dell'interfaccia utente.
     * <p>
     * Questo metodo assegna i listener ai pulsanti per gestire la navigazione tra
     * i pannelli dell'applicazione quando vengono premuti.
     * </p>
     */
    private void addActionEvent() {
        buttonToFind.addActionListener(e -> {
            gui.goToPanel(CitySerch.ID, null);
        });

        buttonToOperator.addActionListener(e -> {
            gui.goToPanel(OperatorHome.ID, null);
        });
    }

    @Override
    public Home createPanel(GUI gui) {
        this.gui = gui;

        addTop(new Widget.LogoLabel());
        addBottom(buttonToFind);
        addBottom(buttonToOperator);

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
     * Invocato quando il pannello viene aperto.
     * <p>
     * Questo metodo non esegue azioni specifiche al momento dell'apertura del
     * pannello.
     * </p>
     *
     * @param args Argomenti aggiuntivi (non utilizzati in questo caso).
     */
    @Override
    public void onOpen(Object[] args) {
        // Nessuna azione specifica al momento dell'apertura
    }
}
