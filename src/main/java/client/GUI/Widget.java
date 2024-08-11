package client.GUI;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import shared.utils.Constants;

/**
 * La classe {@code Widget} fornisce componenti grafici comuni utilizzati
 * nell'interfaccia utente dell'applicazione.
 * <p>
 * Include pannelli di formattazione, pulsanti con cursori personalizzati, etichette per immagini
 * e oggetti per elementi di una lista a discesa. Questi componenti sono progettati per facilitare
 * la creazione di interfacce utente coerenti e ben stilizzate.
 * </p>
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 */
public class Widget {

    /**
     * La classe {@code FormPanel} rappresenta un pannello di formattazione che
     * contiene una {@code JLabel} e un'area per i componenti.
     * <p>
     * Il pannello è organizzato usando un layout a griglia ({@code GridBagLayout})
     * e posiziona il testo della {@code JLabel} sopra l'area dei componenti.
     * </p>
     */
    public static class FormPanel extends JPanel {

        /**
         * Crea un nuovo pannello di formattazione con una {@code JLabel} e un'area per i
         * componenti.
         *
         * @param appTheme   Il tema grafico dell'applicazione, usato per applicare stili
         *                   ai componenti.
         * @param labelText  Il testo della {@code JLabel} che sarà visualizzato.
         * @param activeArea L'area ({@code JComponent}) da inserire nel pannello, ad esempio
         *                   un campo di testo o un pulsante.
         */
        public FormPanel(Theme appTheme, String labelText, JComponent activeArea) {
            super(new GridBagLayout());

            JLabel label = new JLabel(labelText);
            activeArea.setPreferredSize(Constants.GUI.WIDGET_DIMENSION);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.insets = new Insets(5, 5, 2, 5);
            add(label, gbc);

            gbc.gridy = 1;
            gbc.insets = new Insets(0, 5, 5, 5);
            add(activeArea, gbc);

            appTheme.registerPanel(this);
            appTheme.registerLabel(label);
        }
    }

    /**
     * La classe {@code Button} estende {@code JButton} e fornisce un pulsante con testo
     * personalizzato e uno stile di cursore a mano.
     * <p>
     * Questo pulsante è progettato per essere utilizzato in modo consistente in tutta
     * l'applicazione, con una dimensione predefinita e un cursore che indica che il pulsante
     * è cliccabile.
     * </p>
     */
    public static class Button extends JButton {

        /**
         * Crea un nuovo pulsante con il testo specificato.
         *
         * @param text Il testo del pulsante.
         */
        public Button(String text) {
            setText(text);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(Constants.GUI.WIDGET_DIMENSION);
        }
    }

    /**
     * La classe {@code LogoLabel} estende {@code JLabel} e fornisce una label per visualizzare
     * un'immagine con possibilità di ridimensionamento.
     * <p>
     * Questa label è utile per visualizzare loghi o altre immagini, con supporto per
     * la scala dell'immagine.
     * </p>
     */
    public static class LogoLabel extends JLabel {

        /**
         * Crea una {@code JLabel} con le dimensioni predefinite per il logo.
         */
        public LogoLabel() {
            this(2);
        }

        /**
         * Crea una {@code JLabel} con una scala specificata per il logo.
         *
         * @param scale La scala da applicare all'immagine del logo.
         */
        public LogoLabel(double scale) {
            this((int) (200 * scale), (int) (186 * scale));
        }

        /**
         * Crea una {@code JLabel} con dimensioni personalizzate.
         *
         * @param width  Larghezza della {@code JLabel}.
         * @param height Altezza della {@code JLabel}.
         */
        public LogoLabel(int width, int height) {
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);

            try {
                BufferedImage originalImage = ImageIO.read(getClass().getResource(Constants.Assets.LOGO));
                Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

                ImageIcon icon = new ImageIcon(scaledImage);
                setIcon(icon);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Errore durante il caricamento dell'immagine del logo.", "Errore",
                        JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    /**
     * La classe {@code ComboItem} rappresenta un elemento per una lista a discesa ({@code ComboBox}),
     * con una {@code String} come etichetta e un {@code int} come valore associato.
     * <p>
     * Utilizzato per popolare le liste a discesa con elementi che hanno un'etichetta visibile e
     * un valore interno associato.
     * </p>
     */
    public static class ComboItem {
        private final String label;
        private final int value;

        /**
         * Crea un nuovo elemento per la {@code ComboBox} con una {@code String} come etichetta e un {@code int}
         * come valore associato.
         *
         * @param label La label dell'elemento.
         * @param value Il valore associato all'elemento.
         */
        public ComboItem(String label, int value) {
            this.label = label;
            this.value = value;
        }

        /**
         * Ottiene il valore associato all'elemento.
         *
         * @return Il valore dell'elemento.
         */
        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
