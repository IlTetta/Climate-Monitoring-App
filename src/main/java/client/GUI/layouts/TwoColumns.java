package client.GUI.layouts;

import javax.swing.*;
import java.awt.*;

/**
 * La classe astratta {@code TwoColumns} rappresenta un layout a due colonne, con un pannello sinistro
 * e un pannello destro. È progettata per essere estesa da altre classi che necessitano di questo tipo di layout.
 * <p>
 *     Entrambi i pannelli utilizzano un {@link GridBagLayout} per permettere un layout flessibile dei componenti.
 *     La classe fornisce metodi protetti per aggiungere componenti ai pannelli sinistro e destro.
 * </p>
 *
 * @see JPanel
 * @see GridBagLayout
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/2024
 */
public abstract class TwoColumns extends JPanel {

    /**
     * Pannello a sinistra della disposizione a due colonne.
     */
    public JPanel leftPanel;

    /**
     * Pannello a destra della disposizione a due colonne.
     */
    public JPanel rightPanel;

    /**
     * Vincoli utilizzati per i pannelli principali (sinistro e destro) nel layout della classe {@code TwoColumns}.
     * <p>
     *     Questi vincoli stabiliscono che i pannelli occupino tutto lo spazio disponibile, con un peso
     *     uguale per la distribuzione orizzontale e verticale, e siano riempiti completamente.
     * </p>
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
     * Vincoli utilizzati per i componenti aggiunti ai pannelli sinistro e destro.
     * <p>
     *     Questi vincoli stabiliscono che i componenti occupino una posizione relativa nel layout
     *     e abbiano un peso uguale per la distribuzione orizzontale.
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
     * Costruttore della classe {@code TwoColumns}.
     * <p>
     *     Questo costruttore imposta il layout del pannello principale come un {@link GridBagLayout},
     *     e inizializza i pannelli sinistro e destro con lo stesso layout, aggiungendoli poi al pannello principale.
     * </p>
     */
    public TwoColumns() {
        setLayout(new GridBagLayout());
        leftPanel = new JPanel(new GridBagLayout());
        rightPanel = new JPanel(new GridBagLayout());

        add(leftPanel, mainPanelConstraints);
        add(rightPanel, mainPanelConstraints);
    }

    /**
     * Aggiunge un componente al pannello sinistro usando i vincoli {@code subPanelConstraints}.
     * @param component il componente da aggiungere al pannello sinistro.
     */
    protected void addLeft(Component component) {
        leftPanel.add(component, subPanelConstraints);
    }

    /**
     * Aggiunge un componente al pannello destro usando i vincoli {@code subPanelConstraints}.
     * @param component il componente da aggiungere al pannello destro.
     */
    protected void addRight(Component component) {
        rightPanel.add(component, subPanelConstraints);
    }
}
