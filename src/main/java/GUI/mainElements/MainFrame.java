package GUI.mainElements;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import GUI.Widget;
import utils.Constants;

/**
 * Questa classe rappresenta il frame principale dell'applicazione.
 * 
 * <p>
 * Il frame contiene i componenti principali dell'interfaccia utente.
 * </p>
 * 
 * @see GUI.Widget
 * @see utils.Constants
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/2023
 */
public class MainFrame extends JFrame {

    /**
     * Crea una nuova istanza del frame principale.
     * <p>
     * Imposta la dimensione, la chiusura, la posizione e la visibilit&agrave; del frame.
     * Imposta il titolo del frame e l'icona.
     * Imposta il layout del frame.
     * </p>
     */
    public MainFrame() {
        setSize(Constants.GUI.FRAME_WIDTH, Constants.GUI.FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        setTitle(Constants.APP_TITLE);
        setIcon(Constants.Path.Assets.LOGO);
        setLayout(new BorderLayout());
    }

    /**
     * Imposta l'icona del frame.
     * <p>
     * Questa icona viene visualizzata nell'angolo in alto a sinistra della finestra
     * del frame.
     * </p>
     * 
     * @param iconPath Il percorso dell'icona da caricare.
     */
    private void setIcon(String iconPath) {
        ImageIcon iconImage = new ImageIcon();

        try {
            Image originalImage = ImageIO.read(Widget.class.getResource(iconPath));
            iconImage = new ImageIcon(originalImage);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante la lettura del file dell'icona", e);

        }

        setIconImage(iconImage.getImage());
    }
}
