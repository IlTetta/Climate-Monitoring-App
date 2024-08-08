package client.GUI.mainElements;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import client.models.CurrentOperator;
import shared.record.RecordOperator;
import shared.utils.Functions;
import shared.utils.Interfaces;

/**
 * La classe {@code MainWindows} rappresenta la finestra principale
 * dell'applicazione.
 * <p>
 * Questa finestra contiene un pannello scorrevole con un layout a schede, in
 * cui vengono visualizzate diverse
 * schermate dell'applicazione. Inoltre, nella parte inferiore della finestra,
 * vengono visualizzate infromazioni
 * sull'operatore attualmente loggato e l'orario correnti.
 *
 * @see CurrentOperator
 * @see CurrentOperator.CurrentUserChangeListener
 * @see RecordOperator
 * @see Functions
 * @see Interfaces
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/2023
 */
public class MainWindows extends JPanel implements Interfaces.UIWindows {

    /**
     * Il Pannello con barra di scorrimento.
     */
    private JScrollPane scrollPane = new JScrollPane();
    
    /**
     * Il pannello di contenuto.
     */
    private JPanel contentPanel = new JPanel();

    /**
     * La label che indica che non c'&egrave; nesun operatore attualmente loggato.
     */
    private JLabel labelAppInfo = new JLabel("Operatore: /");

    /**
     * La label che rappresenta la data corrente.
     */
    private JLabel labelCurrentData = new JLabel();

    /**
     * Il pannello inferiore della finestra.
     */
    private JPanel bottomPanel = new JPanel();

    /**
     * Costruttore per la classe {@code MainWindows}.
     * 
     * @param cardLayout Layout delle schede da utilizzare per il pannello
     *                   principale.
     */
    public MainWindows(CardLayout cardLayout) {
        super(new BorderLayout());

        contentPanel.setLayout(cardLayout);

        scrollPane.setViewportView(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(labelAppInfo, BorderLayout.WEST);
        bottomPanel.add(labelCurrentData, BorderLayout.EAST);

        bottomPanel.setBorder(new EmptyBorder(3, 7, 3, 7));

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        new Timer(1000, e -> {
            String dateTime = Functions.getCurrentTimeDateString();
            labelCurrentData.setText(dateTime);
        }).start();

        CurrentOperator.getInstance().addCurrentUserChangeListener(new CurrentOperator.CurrentUserChangeListener() {
            @Override
            public void onCurrentUserChange(RecordOperator newOperator) {
                if (newOperator == null)
                    setAppInfo("Operatore: /");
                else
                    setAppInfo("Operatore: " + newOperator.username());
            }
        });
    }

    
    /** 
     * @return JPanel
     */
    @Override
    public JPanel getMainPanel() {
        return this;
    }

    
    /** 
     * @return JScrollPane
     */
    @Override
    public JScrollPane getScrollPanel() {
        return scrollPane;
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public void setAppInfo(String text) {
        labelAppInfo.setText(text);
    }

}
