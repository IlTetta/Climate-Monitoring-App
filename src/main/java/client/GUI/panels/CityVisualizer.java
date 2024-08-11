package client.GUI.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import client.GUI.GUI;
import client.GUI.Widget;
import client.models.MainModel;
import server.ImplementationRMI.LogicCityImp;
import server.ImplementationRMI.LogicCityImp.WeatherTableData;
import shared.record.RecordCity;
import shared.record.RecordWeather;
import shared.utils.Constants;
import shared.utils.Interfaces;
import shared.utils.Constants.Legend;
import shared.record.QueryCondition;

/**
 * La classe {@code CityVisualizer} rappresenta un pannello Swing per la
 * visualizzazione dei dati di una città, inclusi i dati meteorologici relativi
 * a diverse categorie.
 * <p>
 * È utilizzato nell'applicazione per mostrare dettagli sulla città selezionata e
 * i dati meteorologici associati.
 * </p>
 *
 * @see GUI
 * @see Widget
 * @see MainModel
 * @see WeatherTableData
 * @see RecordCity
 * @see RecordWeather
 * @see Interfaces
 * @see Legend
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.1
 * @since 17
 * 18/08/2024
 */
public class CityVisualizer extends JPanel implements Interfaces.UIPanel {

    /**
     * L'ID univoco per identificare questo pannello.
     */
    public static final String ID = "CityVisualizer";

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
     * Campo di testo per il nome della città.
     */
    private final JTextField textfieldCityName = new JTextField();

    /**
     * Campo di testo per il nome del paese.
     */
    private final JTextField textfieldCountryName = new JTextField();

    /**
     * Campo di testo per la latitudine della città.
     */
    private final JTextField textfieldLatitude = new JTextField();

    /**
     * Campo di testo per la longitudine della città.
     */
    private final JTextField textfieldLongitude = new JTextField();

    /**
     * Tabella per visualizzare i dati meteorologici della città.
     */
    private final JTable table = new JTable();

    /**
     * Modello di tabella predefinito per i dati meteorologici.
     */
    private final DefaultTableModel defaulmodelTable = new DefaultTableModel();

    /**
     * Pulsante per tornare indietro.
     */
    private final JButton buttonToBack = new Widget.Button("Indietro");

    /**
     * Categorie della tabella per i dati meteorologici.
     */
    private static final String[] tableCategory = {
            "Vento",
            "Umidità",
            "Pressione",
            "Temperatura",
            "Precipitazioni",
            "Altitudine dei ghiacciai",
            "Massa dei ghiacciai" };

    /**
     * Costruttore della classe {@code CityVisualizer}.
     *
     * @param mainModel Il modello principale dell'applicazione.
     */
    public CityVisualizer(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * Aggiunge gli eventi di azione ai componenti dell'interfaccia utente.
     * <p>
     * Questo metodo assegna i listener ai pulsanti per gestire la navigazione
     * tra i pannelli dell'applicazione quando vengono premuti.
     * </p>
     */
    private void addActionEvent() {
        buttonToBack.addActionListener(e -> {
            gui.goToPanel(CitySerch.ID, null);
        });
    }

    /**
     * Carica i dati relativi a una città specifica e li visualizza nella tabella.
     *
     * @param cityID L'ID della città di cui caricare i dati.
     */
    public void loadDatas(Integer cityID) {
        RecordCity recordCity;
        try {
            recordCity = mainModel.dataQuery.getCityBy(cityID);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "Errore nella connessione al server",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        textfieldCityName.setText(recordCity.name());
        textfieldCountryName.setText(recordCity.countryName());
        textfieldLatitude.setText(String.valueOf(recordCity.latitude()));
        textfieldLongitude.setText(String.valueOf(recordCity.longitude()));

        QueryCondition condition = new QueryCondition("cityID", cityID);
        RecordWeather[] weatherRecords;
        try {
            weatherRecords = mainModel.dataQuery.getWeatherBy(condition);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Errore di connessione al database",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "Errore nella connessione al server",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (weatherRecords.length > 0) {
            int row = 0;
            WeatherTableData weatherTableData = new WeatherTableData(weatherRecords);

            for (String keyString : WeatherTableData.KEYS) {
                Integer avgScore = weatherTableData.getCategoryAvgScore(keyString);
                if (avgScore == null) avgScore = 0;
                Integer recordCount = weatherTableData.getCategoryRecordCount(keyString);

                String comment = String.join(" / ", weatherTableData.getCategoryComments(keyString));

                if (avgScore != 0) {
                    defaulmodelTable.setValueAt(avgScore.toString(), row, 1);
                } else {
                    defaulmodelTable.setValueAt("N/A", row, 1);
                }
                comment = comment.replaceAll("NULL / ", "NULL");
                comment = comment.replaceAll("NULL / NULL", "NULL NULL");
                comment = comment.replaceAll("NULL", "");

                defaulmodelTable.setValueAt(recordCount, row, 2);
                defaulmodelTable.setValueAt(comment, row, 3);

                row++;
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "L'operatore non ha ancora inserito dati per la città selezionata.",
                    "Dati mancanti",
                    JOptionPane.WARNING_MESSAGE);
            gui.goToPanel(CitySerch.ID, null);
        }
    }

    @Override
    public CityVisualizer createPanel(GUI gui) {
        this.gui = gui;

        textfieldCityName.setEditable(false);
        textfieldCountryName.setEditable(false);
        textfieldLatitude.setEditable(false);
        textfieldLongitude.setEditable(false);

        defaulmodelTable.addColumn("Categoria");
        defaulmodelTable.addColumn("Punteggio");
        defaulmodelTable.addColumn("Numero campionamenti");
        defaulmodelTable.addColumn("Commenti");

        for (int i = 0; i < tableCategory.length; i++) {
            defaulmodelTable.addRow(new Object[] { tableCategory[i], "/", "0", "" });
        }

        table.setModel(defaulmodelTable);

        table.getColumnModel().getColumn(0).setCellEditor(new NonEditableCellEditor());
        table.getColumnModel().getColumn(1).setCellEditor(new NonEditableCellEditor());
        table.getColumnModel().getColumn(2).setCellEditor(new NonEditableCellEditor());
        table.getColumnModel().getColumn(3).setCellEditor(new NonEditableCellEditor());
        table.getColumnModel().getColumn(3).setCellRenderer(new TooltipCellRenderer());

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(40);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();

                if (col == 3) {
                    String comment = ((String) table.getValueAt(row, col)).trim();
                    if (!comment.isEmpty()) {
                        comment = comment.replace(" / ", "\n");
                        JOptionPane.showMessageDialog(null, comment, "Commenti", JOptionPane.PLAIN_MESSAGE);
                    }
                }

                if (col == 1 && row >= 0 && row < Legend.LEGENDS.length) {
                    String legendMessage = Legend.LEGENDS[row];
                    JOptionPane.showMessageDialog(null, legendMessage, "Legenda", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new Widget.FormPanel(gui.appTheme, "Nome città", textfieldCityName));
        topPanel.add(new Widget.FormPanel(gui.appTheme, "Nome nazione", textfieldCountryName));
        topPanel.add(new Widget.FormPanel(gui.appTheme, "Latitudine", textfieldLatitude));
        topPanel.add(new Widget.FormPanel(gui.appTheme, "Longitudine", textfieldLongitude));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonToBack, BorderLayout.SOUTH);

        gui.appTheme.registerPanel(topPanel);

        addActionEvent();

        return this;
    }

    /**
     * Classe interna per il rendering delle celle con tooltip.
     * <p>
     * Questa classe estende {@code JTextArea} e implementa {@code TableCellRenderer}
     * per mostrare un tooltip con il testo della cella.
     * </p>
     */
    static class TooltipCellRenderer extends JTextArea implements TableCellRenderer {

        public TooltipCellRenderer() {
            setLineWrap(false);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {

            setText(value != null ? value.toString() : "");
            setToolTipText(value != null ? value.toString() : "");

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    /**
     * Classe interna per l'editor di celle non modificabili.
     * <p>
     * Questa classe estende {@code DefaultCellEditor} e sovrascrive il metodo
     * {@code isCellEditable} per impedire la modifica delle celle.
     * </p>
     */
    class NonEditableCellEditor extends DefaultCellEditor {

        public NonEditableCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return false;
        }
    }

    @Override
    public String getID() {
        return ID;
    }

    /**
     * Invocato quando il pannello viene aperto. Carica i dati della città specificata.
     * <p>
     * Se l'ID della città non è fornito o non valido, viene mostrato un messaggio di errore.
     * </p>
     *
     * @param args Argomenti aggiuntivi (in questo caso, l'ID della città da visualizzare).
     */
    @Override
    public void onOpen(Object[] args) {
        if (args != null && args.length > 0) {
            loadDatas((Integer) args[0]);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Errore nell'apertura della pagina.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            gui.goToPanel(Home.ID, null);
        }
    }
}
