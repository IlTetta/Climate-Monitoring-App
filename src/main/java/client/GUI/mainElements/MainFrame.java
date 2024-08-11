package client.GUI.mainElements;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import client.GUI.Widget;
import shared.utils.Constants;

/**
 * Questa classe rappresenta il frame principale dell'applicazione.
 * <p>
 * Il frame contiene i componenti principali dell'interfaccia utente e funge da
 * contenitore principale per tutti i widget e pannelli dell'applicazione.
 * </p>
 *
 * @see Widget
 * @see Constants
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
     * Questo costruttore configura il frame principale dell'applicazione impostandone le
     * dimensioni, la modalità di chiusura, la posizione, la visibilità, il titolo e l'icona.
     * Viene inoltre impostato un layout di tipo {@link BorderLayout} per la gestione dei componenti.
     * </p>
     */
    public MainFrame() {
        setSize(Constants.GUI.FRAME_WIDTH, Constants.GUI.FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        setTitle(Constants.APP_TITLE);
        setIcon(Constants.Assets.LOGO);
        setLayout(new BorderLayout());
    }

    /**
     * Imposta l'icona del frame.
     * <p>
     * Questa icona viene visualizzata nell'angolo in alto a sinistra della finestra
     * del frame. L'icona viene caricata utilizzando il percorso specificato e impostata come
     * icona del frame. Se il caricamento dell'icona fallisce, viene lanciata un'eccezione di runtime.
     * </p>
     *
     * @param iconPath Il percorso dell'icona da caricare. Deve essere un percorso valido
     *                 all'interno delle risorse del progetto.
     * @throws RuntimeException se si verifica un errore durante il caricamento dell'icona.
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