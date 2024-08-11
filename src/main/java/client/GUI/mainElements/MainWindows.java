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
 * cui vengono visualizzate diverse schermate dell'applicazione. Inoltre, nella parte inferiore
 * della finestra, vengono visualizzate informazioni sull'operatore attualmente loggato e l'orario corrente.
 * </p>
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
     * Il pannello con barra di scorrimento per il contenuto principale.
     */
    private final JScrollPane scrollPane = new JScrollPane();

    /**
     * Il pannello di contenuto, gestito tramite un {@link CardLayout}.
     */
    private final JPanel contentPanel = new JPanel();

    /**
     * La label che indica che non c'&egrave; nessun operatore attualmente loggato.
     */
    private final JLabel labelAppInfo = new JLabel("Operatore: /");

    /**
     * La label che rappresenta la data e l'ora corrente.
     */
    private final JLabel labelCurrentData = new JLabel();

    /**
     * Il pannello inferiore della finestra che contiene le informazioni sull'operatore
     * e l'orario corrente.
     */
    private JPanel bottomPanel = new JPanel();

    /**
     * Costruttore per la classe {@code MainWindows}.
     * <p>
     * Inizializza la finestra principale impostando il layout del pannello di contenuto con
     * il {@code CardLayout} fornito, aggiungendo il pannello di scorrimento e configurando il pannello
     * inferiore con le informazioni sull'operatore e l'orario corrente.
     * Un timer viene avviato per aggiornare l'orario visualizzato ogni secondo.
     * </p>
     *
     * @param cardLayout Il layout delle schede da utilizzare per il pannello principale.
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
                if (newOperator == null) {
                    setAppInfo("Operatore: /");
                } else {
                    setAppInfo("Operatore: " + newOperator.username());
                }
            }
        });
    }

    /**
     * Restituisce il pannello principale della finestra.
     * <p>
     * Questo metodo implementa l'interfaccia {@link Interfaces.UIWindows}.
     * </p>
     *
     * @return Il pannello principale della finestra.
     */
    @Override
    public JPanel getMainPanel() {
        return this;
    }

    /**
     * Restituisce il pannello con barra di scorrimento della finestra.
     * <p>
     * Questo metodo implementa l'interfaccia {@link Interfaces.UIWindows}.
     * </p>
     *
     * @return Il pannello con barra di scorrimento.
     */
    @Override
    public JScrollPane getScrollPanel() {
        return scrollPane;
    }

    /**
     * Restituisce il pannello di contenuto della finestra, che utilizza un {@link CardLayout}.
     * <p>
     * Questo metodo implementa l'interfaccia {@link Interfaces.UIWindows}.
     * </p>
     *
     * @return Il pannello di contenuto.
     */
    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    /**
     * Imposta il testo informativo dell'applicazione relativo all'operatore corrente.
     * <p>
     * Questo testo viene visualizzato nella parte inferiore della finestra.
     * </p>
     *
     * @param text Il testo da visualizzare nell'etichetta informativa dell'applicazione.
     */
    @Override
    public void setAppInfo(String text) {
        labelAppInfo.setText(text);
    }
}
