package GUI.panels;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.EventObject;

import GUI.GUI;
import GUI.Widget;
import models.MainModel;
import models.logic.LogicCity.WeatherTableData;
import models.record.RecordCity;
import models.record.RecordWeather;
import utils.Interfaces;
import utils.Constants.Legend;
import utils.QueryCondition;

/**
 * La classe {@code CityVisualizer} rappresenta un pannello Swing per la
 * visualizzazione dei dati di una citt&agrave;,
 * inclusi i dati meteorologici relativi a diverse categorie.
 * <p>
 * &#201; utilizzato nell'applicazione per mostrare dettagli sulla citt&agrave;
 * selezionata e
 * i dati meteorologici associati.
 * </p>
 * 
 * @see GUI
 * @see Widget
 * @see models.MainModel
 * @see models.logic.LogicCity.WeatherTableData
 * @see models.record.RecordCity
 * @see models.record.RecordWeather
 * @see utils.Interfaces
 * @see utils.Constants.Legend
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 17/09/2023
 */
public class CityVisualizer extends JPanel implements Interfaces.UIPanel {

    /**
     * L'ID univoco per identificare questo pannello.
     */
    public static String ID = "CityVisualizer";

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
     * Campo di testo per il nome della citt&agrave;.
     */
    private JTextField textfieldCityName = new JTextField();

    /**
     * Campo di testo per il nome del paese.
     */
    private JTextField textfieldCountryName = new JTextField();

    /**
     * Campo di testo per la latitudine della citt&agrave;.
     */
    private JTextField textfieldLatitude = new JTextField();

    /**
     * Campo di testo per la longitudine della citt&agrave;.
     */
    private JTextField textfieldLongitude = new JTextField();

    /**
     * Tabella per visualizzare i dati meteorologici della citt&agrave;.
     */
    private JTable table = new JTable();

    /**
     * Modello di tabella predefinito per i dati meteorologici.
     */
    private DefaultTableModel defaulmodelTable = new DefaultTableModel();

    /**
     * Pulsante per tornare indietro.
     */
    private JButton buttonToBack = new Widget.Button("Indietro");

    /**
     * Categorie della tabella per i dati meteorologici.
     */
    private static String[] tableCategory = {
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
     */
    private void addActionEvent() {

        buttonToBack.addActionListener(e -> {
            gui.goToPanel(CityQuery.ID, null);
        });
    }

    /**
     * Carica i dati relativi a una citt&agrave; specifica e li visualizza nella
     * tabella.
     *
     * @param cityID L'ID della citt&agrave; di cui caricare i dati.
     */
    public void loadDatas(Integer cityID) {

        RecordCity RecordCity = null;
        try {
            RecordCity = mainModel.data.dataQuery.getCityBy(cityID);
        } catch (SQLException | RemoteException e) {
            throw new RuntimeException(e);
        }
        textfieldCityName.setText(RecordCity.name());
        textfieldCountryName.setText(RecordCity.countryName());
        textfieldLatitude.setText(String.valueOf(RecordCity.latitude()));
        textfieldLongitude.setText(String.valueOf(RecordCity.longitude()));

        QueryCondition condition = new QueryCondition("cityID", cityID);
        RecordWeather[] weatherRecords;
        try {
            weatherRecords = mainModel.data.dataQuery.getWeatherBy(condition);

        } catch (SQLException | RemoteException e) {
            throw new RuntimeException(e);
        }

        if (weatherRecords.length > 0) {

            Integer row = 0;
            WeatherTableData weatherTableData = new WeatherTableData(weatherRecords);

            for (String keyString : WeatherTableData.keys) {
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
            gui.goToPanel(CityQuery.ID, null);
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

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setHeaderValue(tableCategory[i]);
        }

        table.getTableHeader().repaint();

        table.setModel(defaulmodelTable);

        table.getColumnModel().getColumn(0).setCellEditor(new NonEditableCellEditor());
        table.getColumnModel().getColumn(1).setCellEditor(new NonEditableCellEditor());
        table.getColumnModel().getColumn(2).setCellEditor(new NonEditableCellEditor());
        table.getColumnModel().getColumn(3).setCellEditor(new NonEditableCellEditor());
        table.getColumnModel().getColumn(3).setCellRenderer(new TooltipCellRenderer());

        for (String columnName : tableCategory) {
            defaulmodelTable.addRow(new Object[] { columnName, "/", "0", "" });
        }

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(40);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();

                if (col == 3) { // Colonna "Commenti"
                    String comment = ((String) table.getValueAt(row, col)).trim();
                    if (comment != null && !comment.isEmpty()) {
                        // Sostituisci "|" con il carattere di nuova riga "\n" nei commenti
                        comment = comment.replace("| ", "\n");

                        // Apri un pannello con il testo del commento
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

        // Crea un nuovo pannello per contenere la tabella

        JPanel tablePanel = new JPanel(new BorderLayout());

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // add(table, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        add(buttonToBack, BorderLayout.SOUTH);

        gui.appTheme.registerPanel(topPanel);

        addActionEvent();

        return this;
    }

    /**
     * Classe interna per il rendering delle celle con tooltip.
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

    @Override
    public String getID() {
        return ID;
    }

    /**
     * Classe interna per l'editor di celle non modificabili.
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

    /**
     * Invocato quando il pannello viene aperto. Carica i dati della citt&agrave;
     * specificata.
     *
     * @param args Argomenti aggiuntivi (in questo caso, l'ID della citt&agrave; da
     *             visualizzare).
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
