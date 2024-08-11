package client.GUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * La classe {@code Theme} gestisce il tema grafico dell'applicazione, inclusa la modalità
 * chiaro/scuro, e applica il tema alle etichette ({@code JLabel}) e ai pannelli ({@code JPanel}).
 * <p>
 * La classe consente di passare tra modalità chiara e scura e applica automaticamente il tema
 * corrente a tutti i componenti registrati.
 * </p>
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 */
public class Theme {

    private boolean darkMode = true; // Stato corrente della modalità del tema
    private final static Color WHITE = new Color(255, 250, 250); // Colore per la modalità chiara
    private final static Color DARK_GRAY = new Color(49, 51, 56); // Colore per la modalità scura

    private final List<JLabel> labels = new ArrayList<>(); // Lista di etichette a cui applicare il tema
    private final List<JPanel> panels = new ArrayList<>(); // Lista di pannelli a cui applicare il tema

    /**
     * Alterna tra la modalità chiara e scura del tema.
     * <p>
     * Dopo aver cambiato la modalità, applica immediatamente il nuovo tema a tutti i componenti
     * registrati.
     * </p>
     */
    public void toggleTheme() {
        darkMode = !darkMode;
        applyTheme();
    }

    /**
     * Verifica se l'applicazione è attualmente in modalità scura.
     *
     * @return {@code true} se l'applicazione è in modalità scura, altrimenti {@code false}.
     */
    public boolean isDarkTheme() {
        return darkMode;
    }

    /**
     * Aggiunge una {@code JLabel} alla lista dei componenti a cui applicare il tema grafico.
     *
     * @param label La {@code JLabel} da aggiungere.
     */
    public void registerLabel(JLabel label) {
        labels.add(label);
    }

    /**
     * Aggiunge un {@code JPanel} alla lista dei componenti a cui applicare il tema grafico.
     *
     * @param panel Il {@code JPanel} da aggiungere.
     */
    public void registerPanel(JPanel panel) {
        panels.add(panel);
    }

    /**
     * Applica il tema grafico corrente a tutte le {@code JLabel} e {@code JPanel} registrate.
     * <p>
     * Questo metodo aggiorna il colore di sfondo dei pannelli e il colore del testo delle etichette
     * in base alla modalità del tema.
     * </p>
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
     * Applica il tema grafico corrente a una {@code JLabel} specifica.
     *
     * @param label La {@code JLabel} a cui applicare il tema grafico.
     */
    public void applyThemeToLabel(JLabel label) {
        if (isDarkTheme()) {
            label.setForeground(Color.WHITE);
        } else {
            label.setForeground(Color.BLACK);
        }
    }
}