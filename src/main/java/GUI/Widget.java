package GUI;

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

import utils.Constants;

/**
 * La classe {@code Widget} fornisce componenti grafici comuni utilizzati
 * nell'interfaccia utente dell'applicazione.
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
  * @version 1
 */
public class Widget {

    /**
     * Il pannello di formattazione che contiene una Label e un'area per i
     * componenti.
     */
    public static class FormPanel extends JPanel {

        /**
         * Crea un nuovo pannello di formattazione con una Label e un'area per i
         * componenti.
         * 
         * @param appTheme   Il tema grafico dell'applicazione.
         * @param labelText  Il testo della Label.
         * @param activeArea L'area da inserire nel pannello
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
     * Un pulsante (Button) con testo personalizzato e stile del cursore a mano.
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
     * una Label (Label) per visualizzare un'immagine con possibilit&agrave; di
     * ridimensionamento.
     */
    public static class LogoLabel extends JLabel {

        /**
         * Crea una Label con le dimensioni predefinite.
         */
        public LogoLabel() {
            this(2);
        }

        /**
         * Crea una Label con la scala specificata.
         * 
         * @param scale La scala da applicare al logo.
         */
        public LogoLabel(double scale) {
            this((int) (200 * scale), (int) (186 * scale));
        }

        /**
         * Crea una Label con dimensioni personalizzate.
         * 
         * @param width  Larghezza della Label.
         * @param height Altezza della Label.
         */
        public LogoLabel(int width, int height) {
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);

            try {
                BufferedImage originalImage = ImageIO.read(getClass().getResource(Constants.Path.Assets.LOGO));
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
     * Un oggetto per rappresentare un elemento di una lista a discesa (Combo Box).
     */
    public static class ComboItem {
        private String label;
        private int value;

        /**
         * Crea un nuovo elemtno ComboBox con una Label e un valore associato.
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
