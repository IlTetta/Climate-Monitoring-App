package client.GUI.panels;

import javax.swing.*;

import client.GUI.GUI;
import client.GUI.Widget;
import client.GUI.layouts.TwoColumns;
import client.models.MainModel;

import shared.record.RecordCity;
import shared.utils.Interfaces;
import shared.record.QueryCondition;

import java.awt.event.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code CitySerch} rappresenta un pannello Swing per effettuare
 * query sulla base di dati delle citt&agrave;.
 * <p>
 * Gli utenti possono cercare una citt&agrave; per nome o per coordinate
 * geografiche.
 * </p>
 * 
 * @see GUI
 * @see Widget
 * @see TwoColumns
 * @see MainModel
 * @see RecordCity
 * @see Interfaces
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 17/09/2023
 */
public class CitySerch extends TwoColumns implements Interfaces.UIPanel {

    /**
     * L'ID univoco per identificare questo pannello.
     */
    public static String ID = "CitySerch";

    /**
     * Riferimento all'interfaccia utente grafica (GUI) associata alla barra del
     * menu.
     */
    private GUI gui;

    /**
     * Riferimento al modello principale associato a questo pannello.
     */
    private final MainModel mainModel;

    /**
     * Il campo di testo per il nome della citt&agrave;.
     */
    private final JTextField textfieldCityName = new JTextField();

    /**
     * Il campo per la latitudine della citt&agrave;.
     */
    private final JTextField textfieldLatitude = new JTextField();

    /**
     * Il campo per la longitudine della citt&agrave;.
     */
    private final JTextField textfieldLongitude = new JTextField();

    /**
     * Pulsante per passare alla finestra di visualizzazione dati.
     */
    private final JButton buttonPerfomQuery = new Widget.Button("Cerca dati città");

    /**
     * La combobox per il tipo di ricerca.
     */
    private final JComboBox<String> comboboxQueryType = new JComboBox<String>();

    /**
     * Costruttore della classe {@code CitySerch}.
     *
     * @param mainModel Il modello principale dell'applicazione.
     */
    public CitySerch(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * Aggiunge gestori degli eventi ai componenti del pannello.
     */
    public void addActionEvent() {
        KeyListener enterKeyListenerCityName = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buttonPerfomQuery.doClick();
                }
            }
        };

        KeyListener enterKeyListenerCoordinates = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String latitude = textfieldLatitude.getText();
                    String longitude = textfieldLongitude.getText();

                    if (!latitude.isEmpty() && !longitude.isEmpty()) {
                        buttonPerfomQuery.doClick();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Completa prima tutti i campi relativi alle coordinate",
                                "Dato mancante",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        };

        buttonPerfomQuery.addActionListener(e -> {

            RecordCity[] result = null;
            List<QueryCondition> conditions = new ArrayList<>();

            switch (comboboxQueryType.getSelectedIndex()) {
                case 0:
                    String cityName = textfieldCityName.getText();

                    char firstChar = Character.toUpperCase(cityName.charAt(0));
                    String restOfString = cityName.substring(1).toLowerCase();
                    cityName = firstChar + restOfString;

                    conditions.add(new QueryCondition("name", cityName));
                    try {
                        result = mainModel.dataQuery.getCityBy(conditions);
                    } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Errore di connessione al database",
                                    "Errore",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Errore di connessione al server",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;

                case 1:
                    try {
                        Double latitude = Double.parseDouble(textfieldLatitude.getText().replace(',', '.'));
                        Double longitude = Double.parseDouble(textfieldLongitude.getText().replace(',', '.'));

                        conditions.add(new QueryCondition("latitude", latitude));
                        conditions.add(new QueryCondition("longitude", longitude));
                        result = mainModel.dataQuery.getCityBy(conditions);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Errore di connessione al database",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Errore di connessione al server",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Inserire valori numerici validi per le coordinate",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
            }


            if (result!=null && result.length > 1) {
                RecordCity selectedCity = (RecordCity) JOptionPane.showInputDialog(
                        this,
                        "Sono state trovate più città con lo stesso nome. Seleziona quella desiderata.",
                        "Città trovate",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        result,
                        result[0]);
                if (selectedCity != null)
                    gui.goToPanel(CityVisualizer.ID, new Object[] { selectedCity.ID() });

            } else if (result!=null &&result.length == 1) {
                gui.goToPanel(CityVisualizer.ID, new Object[] { result[0].ID() });

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "La città inserita non è presente nel sistema.",
                        "Città non trovata",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        comboboxQueryType.addActionListener(e -> {
            if (comboboxQueryType.getSelectedIndex() == 0) {
                textfieldCityName.setEnabled(true);
                textfieldLatitude.setEnabled(false);
                textfieldLongitude.setEnabled(false);
            } else {
                textfieldCityName.setEnabled(false);
                textfieldLatitude.setEnabled(true);
                textfieldLongitude.setEnabled(true);
            }
        });

        textfieldCityName.addKeyListener(enterKeyListenerCityName);
        textfieldLatitude.addKeyListener(enterKeyListenerCoordinates);
        textfieldLongitude.addKeyListener(enterKeyListenerCoordinates);

    }

    @Override
    public CitySerch createPanel(GUI gui) {
        this.gui = gui;

        String[] comboboxValues = new String[] { "Cerca per nome", "Cerca per coordinate" };
        comboboxQueryType.setModel(new DefaultComboBoxModel<>(comboboxValues));

        addLeft(new Widget.LogoLabel());
        addRight(new Widget.FormPanel(gui.appTheme, "Tipo di ricerca", comboboxQueryType));
        addRight(new Widget.FormPanel(gui.appTheme, "Città", textfieldCityName));
        addRight(new Widget.FormPanel(gui.appTheme, "Latitudine", textfieldLatitude));
        addRight(new Widget.FormPanel(gui.appTheme, "Longitudine", textfieldLongitude));
        addRight(buttonPerfomQuery);

        gui.appTheme.registerPanel(leftPanel);
        gui.appTheme.registerPanel(rightPanel);

        addActionEvent();

        return this;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void onOpen(Object[] args) {
        comboboxQueryType.setSelectedIndex(0);
    }
}