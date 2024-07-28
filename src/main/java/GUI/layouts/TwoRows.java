package GUI.layouts;

import javax.swing.*;
import java.awt.*;

/**
 * Questa &egrave; una classe astratta che rappresenta un layout a due righe per
 * un'interfaccia grafica swing.
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
     * Le impostazioni del layout per il pannello principale che contiene le due
     * righe.
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
     * Le impostazione del layout per i pannelli superiore e inferiore.
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
     * Costruttore che crea il layout a due righe e inizializza i pannelli superiore
     * e inferiore.
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
     * 
     * @param component Il componente da aggiungere.
     */
    protected void addTop(Component component) {
        topPanel.add(component, subPanelConstraints);
    }

    /**
     * Aggiunge un componente al pannello inferiore.
     * 
     * @param component Il componente da aggiungere.
     */
    protected void addBottom(Component component) {
        bottomPanel.add(component, subPanelConstraints);
    }
}
