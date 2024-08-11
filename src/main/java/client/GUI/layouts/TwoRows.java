package client.GUI.layouts;

import javax.swing.*;
import java.awt.*;

/**
 * Questa &egrave; una classe astratta che rappresenta un layout a due righe per
 * un'interfaccia grafica Swing.
 * <p>
 * Le due righe contengono un pannello superiore e un pannello inferiore per
 * organizzare i componenti dell'interfaccia.
 * </p>
 * <p>
 * &#201; possibile aggiungere componenti ai pannelli superiore e inferiore
 * utilizzando i metodi {@code addTop} e {@code addBottom}.
 * </p>
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/2023
 */
public abstract class TwoRows extends JPanel {

    /**
     * Il pannello superiore in cui verranno aggiunti i componenti.
     */
    public JPanel topPanel;

    /**
     * Il pannello inferiore in cui verranno aggiunti i componenti.
     */
    public JPanel bottomPanel;

    /**
     * Le impostazioni del layout per il pannello principale che contiene le due righe.
     * <p>
     * Queste impostazioni definiscono il comportamento dei pannelli all'interno del layout
     * principale, assegnando loro un peso uniforme sia in larghezza che in altezza,
     * e specificando che occupino tutto lo spazio disponibile.
     * </p>
     */
    protected GridBagConstraints mainPanelConstraints = new GridBagConstraints() {
        {
            gridx = 0;
            gridy = GridBagConstraints.RELATIVE;
            weightx = 1;
            weighty = 1;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.BOTH;
        }
    };

    /**
     * Le impostazioni del layout per i pannelli superiore e inferiore.
     * <p>
     * Questi vincoli definiscono il comportamento dei componenti all'interno dei
     * pannelli superiore e inferiore, garantendo che siano disposti verticalmente
     * uno sotto l'altro e che occupino tutto lo spazio orizzontale disponibile.
     * </p>
     */
    protected GridBagConstraints subPanelConstraints = new GridBagConstraints() {
        {
            gridx = 0;
            gridy = GridBagConstraints.RELATIVE;
            weightx = 1;
            weighty = 1;
            anchor = GridBagConstraints.CENTER;
        }
    };

    /**
     * Costruttore che crea il layout a due righe e inizializza i pannelli superiore e inferiore.
     * <p>
     * Il costruttore imposta il layout del pannello principale come un {@link GridBagLayout},
     * e inizializza i pannelli {@code topPanel} e {@code bottomPanel} con lo stesso layout.
     * Entrambi i pannelli sono poi aggiunti al pannello principale utilizzando i vincoli
     * definiti in {@code mainPanelConstraints}.
     * </p>
     */
    public TwoRows() {
        setLayout(new GridBagLayout());
        topPanel = new JPanel(new GridBagLayout());
        bottomPanel = new JPanel(new GridBagLayout());

        add(topPanel, mainPanelConstraints);
        add(bottomPanel, mainPanelConstraints);
    }

    /**
     * Aggiunge un componente al pannello superiore.
     * <p>
     * Il componente viene aggiunto al {@code topPanel} utilizzando i vincoli definiti
     * in {@code subPanelConstraints}, che ne determinano la posizione e il comportamento
     * all'interno del pannello.
     * </p>
     *
     * @param component Il componente da aggiungere.
     */
    protected void addTop(Component component) {
        topPanel.add(component, subPanelConstraints);
    }

    /**
     * Aggiunge un componente al pannello inferiore.
     * <p>
     * Il componente viene aggiunto al {@code bottomPanel} utilizzando i vincoli definiti
     * in {@code subPanelConstraints}, che ne determinano la posizione e il comportamento
     * all'interno del pannello.
     * </p>
     *
     * @param component Il componente da aggiungere.
     */
    protected void addBottom(Component component) {
        bottomPanel.add(component, subPanelConstraints);
    }
}
