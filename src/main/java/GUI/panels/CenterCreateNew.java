package GUI.panels;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import GUI.GUI;
import GUI.Widget;
import GUI.layouts.TwoColumns;
import models.CurrentOperator;
import models.MainModel;
import models.data.DataQuery.QueryCondition;
import models.record.RecordCity;
import utils.Constants;
import utils.Interfaces;

/**
 * La classe {@code CenterCreateNew} rappresenta un pannello per la creazione di
 * un nuovo Centro di Monitoraggio da parte dell'operatore.
 * <p>
 * Il pannello consente all'operatore di inserire informazioni sul centro, come
 * il nome, la via, il numero civico, il CAP, il comune,
 * la provincia, e le citt&agrave; associate al centro. Una volta inseriti i
 * dati,
 * l'operatore pu&ograve; salvare il centro nel sistema.
 * </p>
 * <p>
 * La classe gestisce la validazione dei dati inseriti e fornisce feedback
 * all'operatore in caso di errori.
 * 
 * @see GUI.GUI
 * @see GUI.Widget
 * @see GUI.layouts.TwoColumns
 * @see models.CurrentOperator
 * @see models.MainModel
 * @see models.data.DataQuery.QueryCondition
 * @see models.record.RecordCity
 * @see utils.Constants
 * @see utils.Interfaces
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/2023
 */
public class CenterCreateNew extends TwoColumns implements Interfaces.UIPanel {

    /**
     * L'identificatoe unico per questo pannello.
     */
    public static String ID = "CenterCreateNew";

    /**
     * Riferimento all'interfaccia utente grafica (GUI) associata alla barra del
     * menu.
     */
    private GUI gui;

    /**
     * Riferimento al modello principale associato a questo pannello.
     */
    private MainModel mainModel;

    /**
     * Campo di testo per il nome del centro.
     */
    private JTextField textfieldCenterName = new JTextField();

    /**
     * Campo di testo per il nome della via.
     */
    private JTextField textfieldStreetName = new JTextField();

    /**
     * Campo di testo per il numero civico.
     */
    private JTextField textfieldStreetNumber = new JTextField();

    /**
     * Campo di testo per il CAP.
     */
    private JTextField textfieldCAP = new JTextField();

    /**
     * Campo di testo per il nome del comune.
     */
    private JTextField textfieldTownName = new JTextField();

    /**
     * Campo di testo per la sigla della provincia.
     */
    private JTextField textfieldDistrictName = new JTextField();

    /**
     * Campo di testo per il nome dela citt&agrave; da associare al centro.
     */
    private JTextField textfieldCityName = new JTextField();

    /**
     * Bottone per la creazione del centro.
     */
    private JButton buttonPerformInit = new Widget.Button("Crea il centro");

    /**
     * Modello predefinito per la lista delle citt&agrave; asscoiate al centro.
     */
    private DefaultListModel<String> listmodelCityIDs = new DefaultListModel<>();

    /**
     * Lista delle citt&agrave; associate al centro.
     */
    private JList<String> listCityIDs = new JList<>(listmodelCityIDs);

    /**
     * ScrollPane per la lista delle citt&agrave;.
     */
    private JScrollPane scrollpaneCityInfo = new JScrollPane();

    /**
     * Crea una nuova istanza di {@code CenterCreateNew}.
     * 
     * @param mainModel Il modello principale dell'applicazione.
     */
    public CenterCreateNew(MainModel mainModel) {
        this.mainModel = mainModel;
    }

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
     * pressione
     * del pulsante "Crea il centro" o l'inserimento del nome delle citt&agrave;. In
     * caso di
     * errori o dati
     * non validi, il metodo fornisce feedback all'operatore attraverso messaggi di
     * errore o avvisi.
     * </p>
     */
    private void addActionEvent() {

        textfieldCityName.addActionListener(e -> {

            String cityName = textfieldCityName.getText().trim();

            if (!cityName.isEmpty() && !listmodelCityIDs.contains(cityName)) {

                RecordCity[] result = null;
                List<QueryCondition> conditions = new ArrayList<>();

                conditions.add(new QueryCondition("name", cityName));
                try {
                    result = mainModel.data.getCityBy(conditions);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
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
                    if (selectedCity != null) {
                        listmodelCityIDs.addElement(selectedCity.toString());
                        textfieldCityName.setText("");
                    }

                } else if (result.length == 1) {
                    listmodelCityIDs.addElement(result[0].toString());
                    textfieldCityName.setText("");
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "La città inserita non è presente nel database.",
                            "Città non trovata",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        listCityIDs.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = listCityIDs.getSelectedIndex();

                if (selectedIndex >= 0) {
                    Integer answer = JOptionPane.showConfirmDialog(
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

            try {
                mainModel.logicCenter.initNewCenter(centerName,
                        streetName,
                        streetNumber,
                        CAP,
                        townName,
                        districtName,
                        cityIDs);
                JOptionPane.showMessageDialog(
                        this,
                        "Nuovo centro inserito correttamente.",
                        "Nuovo centro inserito",
                        JOptionPane.INFORMATION_MESSAGE);
                gui.goToPanel(CityAddData.ID, null);

            } catch (Exception exception) {
                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Dato errato",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

    };

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
        addRight(new Widget.FormPanel(gui.appTheme, "Nomi delle città; associate al centro", textfieldCityName));
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
