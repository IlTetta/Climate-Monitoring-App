package GUI.layouts;

import javax.swing.*;
import java.awt.*;

/**
 * Questa &egrave; una classe astratta che rappresenta un layout a due colonne per
 * un'interfaccia grafica swing.
 * <p>
 * Le due colonne contengono un pannello sinistro e un pannello destro per
 * organizzare i componenti dell'interfaccia.
 * </p>
 * <p>
 * E' possibile aggiungere componenti ai pannelli sinistro e destro utilizzando
 * i metodi {@code addLeft} e {@code addRight}.
 * </p>
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/2023
 */
public abstract class TwoColumns extends JPanel {

    /**
     * Il pannello sinistro in cui verranno aggiunti i componenti.
     */
    public JPanel leftPanel;

    /**
     * Il pannello destro in cui verranno aggiunti i componenti.
     */
    public JPanel rightPanel;

    /**
     * Le impostazioni del layout per il pannello principale che contiene le due
     * colonne.
     */
    protected GridBagConstraints mainPanelConstraints = new GridBagConstraints() {
        {
            gridx = GridBagConstraints.RELATIVE;
            gridy = 0;
            weightx = 1;
            weighty = 1;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.BOTH;
        }
    };

    /**
     * Le impostazioni del layout per i pannelli sinistro e destro.
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
     * Costruttore che crea il layout a due colonne e inizializza i pannelli
     * sinistro e destro.
     */
    public TwoColumns() {
        setLayout(new GridBagLayout());
        leftPanel = new JPanel(new GridBagLayout());
        rightPanel = new JPanel(new GridBagLayout());

        add(leftPanel, mainPanelConstraints);
        add(rightPanel, mainPanelConstraints);
    }

    /**
     * Aggiunge un componente al pannello sinistro.
     * 
     * @param component Il componente da aggiungere al pannello.
     */
    protected void addLeft(Component component) {
        leftPanel.add(component, subPanelConstraints);
    }

    /**
     * Aggiunge un componente al pannello destro.
     * 
     * @param component Il componente da aggiungere al pannello.
     */
    protected void addRight(Component component) {
        rightPanel.add(component, subPanelConstraints);
    }
}
