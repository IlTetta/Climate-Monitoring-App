package client.GUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * La classe {@code Theme} gestisce il tema grafico dell'applicazione,
 * inclusa la modalit&agrave; chiaro/scuro, e applica il tema a etichette (labels) e
 * pannelli (panels).
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 */
public class Theme {

    private boolean darkMode = true;
    private final static Color WHITE = new Color(255, 250, 250);
    private final static Color DARK_GRAY = new Color(49, 51, 56);

    private List<JLabel> labels = new ArrayList<>();
    private List<JPanel> panels = new ArrayList<>();

    /**
     * Alterna tra la modalit&agrave; chiara e scura del tema.
     * <p>
     * Dopo aver cambiato la modalit&agrave;, applica immediatamente il nuovo tema.
     * </p>
     */
    public void toggleTheme() {
        darkMode = !darkMode;
        applyTheme();
    }

    /**
     * Verifica se l'applicazione ; attualmente in modalit&agrave; scura.
     * 
     * @return {@code True} se l'applicazione &egrave; in modalit&agrave; scura, altrimenti
     *         {@code False}.
     */
    public boolean isDarkTheme() {
        return darkMode;
    }

    /**
     * Aggiunge alla lista la {@code JLabel} a cui applicare il tema grafico.
     *
     * @param label La {@code JLabel} da aggiungere.
     */
    public void registerLabel(JLabel label) {
        labels.add(label);
    }

    /**
     * Aggiunge alla lista il pannello a cui applicare il tema grafico.
     * 
     * @param panel Il {@code JPanel} da aggiungere.
     */
    public void registerPanel(JPanel panel) {
        panels.add(panel);
    }

    /**
     * Applica il tema grafico corrente alle liste di {@code JLabel} e {@code JPanel}.
     */
    public void applyTheme() {
        for (JLabel label : labels) {
            applyThemeToLabel(label);
        }

        for (JPanel panel : panels) {
            applyThemeToPanel(panel);
        }
    }

    /**
     * Applica il tema grafico corrente a un {@code JPanel} specifico.
     * 
     * @param panel Il {@code JPanel} a cui applicare il tema grafico.
     */
    public void applyThemeToPanel(JPanel panel) {
        if (isDarkTheme()) {
            panel.setBackground(DARK_GRAY);
        } else {
            panel.setBackground(WHITE);
        }
    }

    /**
     * Applica il tema grafico corrente a una {@code JLabel} Specifica.
     * 
     * @param label La {@code JLabel} a cui applicare il tema specifico.
     */
    public void applyThemeToLabel(JLabel label) {
        if (isDarkTheme()) {
            label.setForeground(Color.WHITE);
        } else {
            label.setForeground(Color.BLACK);
        }
    }
}
