package client.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.GUI.mainElements.MainFrame;
import client.GUI.mainElements.MainWindows;
import client.GUI.mainElements.MenuBar;
import client.GUI.panels.*;
import client.models.MainModel;
import shared.utils.Interfaces;

/**
 * La classe {@code GUI} gestisce l'interfaccia utente dell'applicazione e
 * la navigazione tra diversi pannelli. &#201; un componente chiave nell'architettura
 * dell'applicazione.
 *
 * @see MainFrame
 * @see MainWindows
 * @see MenuBar
 * @see Loading
 * @see Home
 * @see CitySerch
 * @see CityVisualizer
 * @see OperatorHome
 * @see OperatorLogin
 * @see OperatorRegister
 * @see CenterCreateNew
 * @see CityAddData
 * @see MainModel
 * @see Interfaces
 * @see Theme
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
  * @version 1
 */

public class GUI {

    /**
     * Il tema grafico dell'applicazione
     */
    public Theme appTheme = new Theme();

    private final CardLayout cardLayout = new CardLayout();
    private final Interfaces.UIWindows mainWindowsArea = new MainWindows(cardLayout);
    private final Map<String, Interfaces.UIPanel> Panels = new HashMap<>();
    private String currentID;

    private final Loading loadingPanel;
    private final Home homePanel;
    private final CitySerch cityQueryPanel;
    private final CityVisualizer cityVisualizerPanel;
    private final OperatorHome operatorHomePanel;
    private final OperatorLogin operatorLoginPanel;
    private final OperatorRegister operatorRegisterPanel;
    private final CenterCreateNew centerCreateNewPanel;
    private final CityAddData cityAddDataPanel;

    /**
     * Costruttore della classe {@code GUI}.
     * Inizializza il frame principale dell'applicazione e i pannelli dell'interfaccia utente.
     * 
     * @param mainModel Il modello dell'applicazione
     */
    public GUI(MainModel mainModel) {
        JFrame mainFrame = new MainFrame();
        mainFrame.setJMenuBar(new MenuBar(this));
        mainFrame.add(mainWindowsArea.getMainPanel(), BorderLayout.CENTER);

        mainWindowsArea.getMainPanel().revalidate();
        mainWindowsArea.getMainPanel().repaint();

        loadingPanel = new Loading();
        homePanel = new Home();
        cityQueryPanel = new CitySerch(mainModel);
        cityVisualizerPanel = new CityVisualizer(mainModel);
        operatorHomePanel = new OperatorHome();
        operatorLoginPanel = new OperatorLogin(mainModel);
        operatorRegisterPanel = new OperatorRegister(mainModel);
        centerCreateNewPanel = new CenterCreateNew(mainModel);
        cityAddDataPanel = new CityAddData(mainModel);
    }

    /**
     * Aggiunge tutti i pannelli utilizzati nell'applicazione alla mappa dei pannelli.
     * Inoltre, applica il tema grafico a ciascun pannello.
     */
    public void addPanels() {
        addPanel(loadingPanel.createPanel(this));
        addPanel(homePanel.createPanel(this));
        addPanel(cityQueryPanel.createPanel(this));
        addPanel(cityVisualizerPanel.createPanel(this));
        addPanel(operatorHomePanel.createPanel(this));
        addPanel(operatorLoginPanel.createPanel(this));
        addPanel(operatorRegisterPanel.createPanel(this));
        addPanel(centerCreateNewPanel.createPanel(this));
        addPanel(cityAddDataPanel.createPanel(this));
    }

    /**
     * Aggiunge un pannello alla mappa dei pannelli e applica il tema grafico ad esso.
     * 
     * @param Panel Il pannello da aggiungere.
     */
    public void addPanel(Interfaces.UIPanel Panel) {
        Panels.put(Panel.getID(), Panel);
        mainWindowsArea.getContentPanel().add((Component) Panel, Panel.getID());

        appTheme.registerPanel((JPanel) Panel);
        appTheme.applyTheme();
    }
     /**
      * Cancella i dati nella citt&agrave; di aggiunta dati, se il pannello &egrave; attualmente visualizzato
      */
    public void clearCityAddData() {
        if (cityAddDataPanel != null) {
            cityAddDataPanel.clearTableData();
        }
    }

    /**
     * Ottiene un pannello dell'interfaccia utente in base all'ID specificato
     * 
     * @param ID L'ID del pannello da ottenere.
     * @return Il pannello dell'interfaccia utente corrispondente all'ID.
     */
    public Interfaces.UIPanel getUIPanel(String ID) {
        return Panels.get(ID);
    }

    /**
     * Ottiene l'area principale della finestra dell'interfaccia utente.
     * 
     * @return L'area principale della finestra dell'interfaccia utente.
     */
    public Interfaces.UIWindows getMainWindowArea() {
        return mainWindowsArea;
    }

    /**
     * Ottiene il layout utilizzato per la navigazione tra i pannelli.
     * 
     * @return Il layout.
     */
    public CardLayout getCardLayout() {
        return cardLayout;
    }

    /**
     * Ottiene l'ID del pannello attualmente visualizzato.
     * 
     * @return L'ID del pannello corrente.
     */
    public String getCurrentID() {
        return currentID;
    }

    /**
     * Passa alla visualizzazione di un pannello specifico ed esegue le operazioni
     * necessarie quando si passa a un nuovo pannello.
     * 
     * @param ID L'ID del pannello da visualizzare.
     * @param args Gli argomenti da passare al pannello.
     */

    public void goToPanel(String ID, Object[] args) {
        try {
            if ("CityAddData".equals(currentID)) {
                clearCityAddData();
            }
            cardLayout.show(mainWindowsArea.getContentPanel(), ID);
            getUIPanel(ID).onOpen(args);
            currentID = ID;
        } catch (NullPointerException e) {
            System.out.println("Errore: Un componente è nullo. Verifica che 'mainWindowsArea', 'cardLayout' e altri componenti siano inizializzati correttamente.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: L'ID del pannello '" + ID + "' non è valido o il pannello non esiste.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Errore: Si è verificato un errore inaspettato durante il cambio del pannello.");
            e.printStackTrace();
        }
    }
}
