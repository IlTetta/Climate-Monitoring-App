package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.mainElements.MainFrame;
import GUI.mainElements.MainWindows;
import GUI.mainElements.MenuBar;
import GUI.panels.*;
import models.MainModel;
import utils.Interfaces;

/**
 * La classe {@code GUI} gestisce l'interfaccia utente dell'applicazione e
 * la navigazione tra diversi pannelli. &#201; un componente chiave nell'architettura
 * dell'applicazione.
 *
 * @see GUI.mainElements.MainFrame
 * @see GUI.mainElements.MainWindows
 * @see GUI.mainElements.MenuBar
 * @see GUI.panels.Loading
 * @see GUI.panels.Home
 * @see GUI.panels.CityQuery
 * @see GUI.panels.CityVisualizer
 * @see GUI.panels.OperatorHome
 * @see GUI.panels.OperatorLogin
 * @see GUI.panels.OperatorRegister
 * @see GUI.panels.CenterCreateNew
 * @see GUI.panels.CityAddData
 * @see models.MainModel
 * @see utils.Interfaces
 * @see GUI.Theme
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

    private CardLayout cardLayout = new CardLayout();
    private JFrame mainFrame = new MainFrame();
    private Interfaces.UIWindows mainWindowsArea = new MainWindows(cardLayout);
    private Map<String, Interfaces.UIPanel> Panels = new HashMap<>();
    private String currentID;

    private Loading loadingPanel;
    private Home homePanel;
    private CityQuery cityQueryPanel;
    private CityVisualizer cityVisualizerPanel;
    private OperatorHome operatorHomePanel;
    private OperatorLogin operatorLoginPanel;
    private OperatorRegister operatorRegisterPanel;
    private CenterCreateNew centerCreateNewPanel;
    private CityAddData cityAddDataPanel;

    /**
     * Costruttore della classe {@code GUI}.
     * Inizializza il frame principale dell'applicazione e i pannelli dell'interfaccia utente.
     * 
     * @param mainModel Il modello dell'applicazione
     */
    public GUI(MainModel mainModel) {
        mainFrame.setJMenuBar(new MenuBar(this));
        mainFrame.add(mainWindowsArea.getMainPanel(), BorderLayout.CENTER);

        mainWindowsArea.getMainPanel().revalidate();
        mainWindowsArea.getMainPanel().repaint();

        loadingPanel = new Loading(mainModel);
        homePanel = new Home(mainModel);
        cityQueryPanel = new CityQuery(mainModel);
        cityVisualizerPanel = new CityVisualizer(mainModel);
        operatorHomePanel = new OperatorHome(mainModel);
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
     * Passa alla visualizzazione di un pannello specifico e esegue le operazioni
     * necessarie quando si passa ad un nuovo pannello.
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


    /*public void goToPanel(String ID, Object[] args) {
        try {
            if ("CityAddData".equals(currentID)) {
                clearCityAddData();
            }
            cardLayout.show(mainWindowsArea.getContentPanel(), ID);
            getUIPanel(ID).onOpen(args);
            currentID = ID;
        } catch (Exception e) {
            System.out.println("Errore: Panel non trovato.");
        }
    }*/
}
