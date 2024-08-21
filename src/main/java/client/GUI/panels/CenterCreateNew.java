package client.GUI.panels;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import client.GUI.GUI;
import client.GUI.Widget;
import client.GUI.layouts.TwoColumns;
import client.models.CurrentOperator;
import client.models.MainModel;
import server.ImplementationRMI.DataQueryImp;
import shared.record.RecordCity;
import shared.utils.Constants;
import shared.utils.Interfaces;
import shared.record.QueryCondition;

/**
 * La classe {@code CenterCreateNew} rappresenta il pannello per la creazione di
 * un nuovo Centro di Monitoraggio da parte dell'operatore.
 * <p>
 * Il pannello consente all'operatore di inserire informazioni sul centro, come
 * il nome, la via, il numero civico, il CAP, il comune, la provincia e le
 * città associate al centro. Una volta inseriti i dati, l'operatore può salvare
 * il centro nel sistema utilizzando i servizi offerti dal modulo server RMI e
 * interagendo con il database.
 * </p>
 * <p>
 * La classe gestisce la validazione dei dati inseriti e fornisce feedback
 * all'operatore in caso di errori. La comunicazione con il server RMI è gestita
 * attraverso l'interfaccia {@link DataQueryImp} per le query sui dati e
 * {@link MainModel} per la logica di applicazione.
 * </p>
 *
 * @see GUI
 * @see Widget
 * @see TwoColumns
 * @see CurrentOperator
 * @see MainModel
 * @see QueryCondition
 * @see RecordCity
 * @see Constants
 * @see Interfaces
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.1
 * @since 18/08/2024
 */
public class CenterCreateNew extends TwoColumns implements Interfaces.UIPanel {

    /**
     * L'identificatore unico per questo pannello.
     */
    public static final String ID = "CenterCreateNew";

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
     * Campo di testo per il nome del centro.
     */
    private final JTextField textfieldCenterName = new JTextField();

    /**
     * Campo di testo per il nome della via.
     */
    private final JTextField textfieldStreetName = new JTextField();

    /**
     * Campo di testo per il numero civico.
     */
    private final JTextField textfieldStreetNumber = new JTextField();

    /**
     * Campo di testo per il CAP.
     */
    private final JTextField textfieldCAP = new JTextField();

    /**
     * Campo di testo per il nome del comune.
     */
    private final JTextField textfieldTownName = new JTextField();

    /**
     * Campo di testo per la sigla della provincia.
     */
    private final JTextField textfieldDistrictName = new JTextField();

    /**
     * Campo di testo per il nome della città da associare al centro.
     */
    private final JTextField textfieldCityName = new JTextField();

    /**
     * Bottone per la creazione del centro.
     */
    private final JButton buttonPerformInit = new Widget.Button("Crea il centro");

    /**
     * Modello predefinito per la lista delle città associate al centro.
     */
    private final DefaultListModel<String> listmodelCityIDs = new DefaultListModel<>();

    /**
     * Lista delle città associate al centro.
     */
    private final JList<String> listCityIDs = new JList<>(listmodelCityIDs);

    /**
     * ScrollPane per la lista delle città.
     */
    private final JScrollPane scrollpaneCityInfo = new JScrollPane();

    /**
     * Crea una nuova istanza di {@code CenterCreateNew}.
     *
     * @param mainModel Il modello principale dell'applicazione.
     */
    public CenterCreateNew(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * Pulisce i campi di testo dell'interfaccia.
     */
    private void clearFields() {
        textfieldCenterName.setText("");
        textfieldStreetName.setText("");
        textfieldStreetNumber.setText("");
        textfieldCAP.setText("");
        textfieldTownName.setText("");
        textfieldDistrictName.setText("");
        textfieldCityName.setText("");
        listmodelCityIDs.clear();
    }

    /**
     * Aggiunge azioni agli elementi dell'interfaccia.
     * <p>
     * Questo metodo gestisce le azioni degli elementi dell'interfaccia, come la
     * pressione del pulsante "Crea il centro" o l'inserimento del nome delle città.
     * In caso di errori o dati non validi, il metodo fornisce feedback all'operatore
     * attraverso messaggi di errore o avvisi. Le operazioni di query sui dati delle
     * città vengono eseguite tramite il modulo server RMI e le eccezioni relative
     * a tali operazioni sono gestite di conseguenza.
     * </p>
     */
    private void addActionEvent() {

        textfieldCityName.addActionListener(e -> {

            String cityName = textfieldCityName.getText().trim();

            if (!cityName.isEmpty() && !listmodelCityIDs.contains(cityName)) {

                RecordCity[] result;
                List<QueryCondition> conditions = new ArrayList<>();

                char firstChar = Character.toUpperCase(cityName.charAt(0));
                String restOfString = cityName.substring(1).toLowerCase();
                cityName = firstChar + restOfString;

                conditions.add(new QueryCondition("name", cityName));
                try {
                    result = mainModel.dataQuery.getCityBy(conditions);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Errore di connessione al server.",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (result.length > 1) {
                    RecordCity selectedCity = (RecordCity) JOptionPane.showInputDialog(
                            this,
                            "Sono state trovate più città con lo stesso nome. Seleziona quella desiderata.",
                            "Città trovate",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            result,
                            result[0]);
                    if (selectedCity != null && !listmodelCityIDs.contains(selectedCity.toString())) {
                        listmodelCityIDs.addElement(selectedCity.toString());
                        textfieldCityName.setText("");
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "La città inserita non è presente nel database o è già associata al centro.",
                                "Città non trovata",
                                JOptionPane.WARNING_MESSAGE);
                    }

                } else if (result.length == 1 && !listmodelCityIDs.contains(result[0].toString())) {
                    listmodelCityIDs.addElement(result[0].toString());
                    textfieldCityName.setText("");
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "La città inserita non è presente nel database o è già associata al centro.",
                            "Città non trovata",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        listCityIDs.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = listCityIDs.getSelectedIndex();

                if (selectedIndex >= 0) {
                    int answer = JOptionPane.showConfirmDialog(
                            this,
                            "Vuoi rimuovere la città selezionata?",
                            "Rimuovi città",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if (answer == JOptionPane.YES_OPTION) {
                        listmodelCityIDs.remove(selectedIndex);
                    }
                }
            }
        });

        buttonPerformInit.addActionListener(e -> {

            String centerName = textfieldCenterName.getText().trim();
            String streetName = textfieldStreetName.getText().trim();
            String streetNumber = textfieldStreetNumber.getText().trim();
            String CAP = textfieldCAP.getText().trim();
            String townName = textfieldTownName.getText().trim();
            String districtName = textfieldDistrictName.getText().trim();
            Integer[] cityIDs = new Integer[listmodelCityIDs.size()];

            for (int i = 0; i < listmodelCityIDs.size(); i++) {
                cityIDs[i] = Integer.parseInt(listmodelCityIDs.get(i).split(Constants.CSV_SEPARATOR)[0]);
            }

            CurrentOperator currentOperator = CurrentOperator.getInstance();
            try {
                mainModel.logicCenter.initNewCenter(centerName,
                        streetName,
                        streetNumber,
                        CAP,
                        townName,
                        districtName,
                        cityIDs,
                        currentOperator.getCurrentOperator().ID());

                currentOperator.setCurrentOperator(
                        mainModel.dataQuery.getOperatorBy(currentOperator.getCurrentOperator().ID()));

                JOptionPane.showMessageDialog(
                        this,
                        "Nuovo centro inserito correttamente.",
                        "Nuovo centro inserito",
                        JOptionPane.INFORMATION_MESSAGE);
                gui.goToPanel(CityAddData.ID, null);

            } catch (SQLException | NoSuchElementException | IllegalStateException | IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Errore di connessione al server.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public CenterCreateNew createPanel(GUI gui) {
        this.gui = gui;

        scrollpaneCityInfo.setViewportView(listCityIDs);
        scrollpaneCityInfo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpaneCityInfo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpaneCityInfo.setPreferredSize(Constants.GUI.WIDGET_DIMENSION);

        addLeft(new Widget.LogoLabel());
        addRight(new Widget.FormPanel(gui.appTheme, "Nome del centro", textfieldCenterName));
        addRight(new Widget.FormPanel(gui.appTheme, "Nome della via", textfieldStreetName));
        addRight(new Widget.FormPanel(gui.appTheme, "Numero civico", textfieldStreetNumber));
        addRight(new Widget.FormPanel(gui.appTheme, "CAP", textfieldCAP));
        addRight(new Widget.FormPanel(gui.appTheme, "Nome del comune", textfieldTownName));
        addRight(new Widget.FormPanel(gui.appTheme, "Nome della provincia", textfieldDistrictName));
        addRight(new Widget.FormPanel(gui.appTheme, "Nomi delle città associate al centro", textfieldCityName));
        addRight(scrollpaneCityInfo);
        addRight(buttonPerformInit);

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
        CurrentOperator currentOperator = CurrentOperator.getInstance();

        if (!currentOperator.isUserLogged()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Per creare un nuovo centro devi prima essere loggato.",
                    "Utente non loggato",
                    JOptionPane.ERROR_MESSAGE);

            gui.goToPanel(OperatorLogin.ID, null);

        } else if (currentOperator.getCurrentOperator().centerID() != 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Hai già creato il tuo centro.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);

            gui.goToPanel(CityAddData.ID, null);
        }
        clearFields();
    }
}
