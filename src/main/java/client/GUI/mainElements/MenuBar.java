package client.GUI.mainElements;

import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import client.GUI.GUI;
import client.GUI.panels.*;
import client.GUI.panels.CitySearch;
import client.models.CurrentOperator;

/**
 * La classe {@code MenuBar} rappresenta la barra del menu dell'interfaccia grafica dell'applicazione.
 * <p>
 * Questa barra del menu consente la navigazione tra diverse sezioni dell'applicazione e
 * fornisce opzioni per cambiare il tema dell'interfaccia utente e gestire la sessione dell'operatore.
 * </p>
 * <p>
 * Gli elementi principali del menu includono Home, Ricerca città, e un sotto-menu per l'Area Operatore
 * con le opzioni di Login, Registrazione, Gestione città e Logout.
 * </p>
 *
 * @see GUI
 * @see CityAddData
 * @see CitySearch
 * @see Home
 * @see OperatorHome
 * @see OperatorLogin
 * @see OperatorRegister
 * @see CurrentOperator
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/2023
 */
public class MenuBar extends JMenuBar {

    /**
     * Crea una nuova istanza di {@code MenuBar}.
     * <p>
     * Imposta il layout della barra del menu, aggiunge gli elementi del menu e
     * configura le azioni associate a ciascun elemento.
     * </p>
     *
     * @param gui L'istanza dell'interfaccia grafica principale a cui è associata
     *            la barra del menu.
     */
    public MenuBar(GUI gui) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JMenuItem itemHome = new JMenuItem("Home");
        JMenuItem itemCityQuery = new JMenuItem("Ricerca città");

        JMenu submenuOperator = new JMenu("Area Operatore");
        JMenuItem itemOperatorLogin = new JMenuItem("Login");
        JMenuItem itemOperatorRegistration = new JMenuItem("Registrazione");
        JMenuItem itemCityAddData = new JMenuItem("Gestisci città");
        JMenuItem itemAreaLogout = new JMenuItem("Logout");

        JCheckBoxMenuItem itemToggleTheme = new JCheckBoxMenuItem("Tema scuro");
        itemToggleTheme.setSelected(gui.appTheme.isDarkTheme());

        itemHome.addActionListener(e -> {
            gui.goToPanel(Home.ID, null);
        });

        itemCityQuery.addActionListener(e -> {
            gui.goToPanel(CitySearch.ID, null);
        });

        submenuOperator.addActionListener(e -> {
            gui.goToPanel(OperatorHome.ID, null);
        });

        itemOperatorLogin.addActionListener(e -> {
            gui.goToPanel(OperatorLogin.ID, null);
        });

        itemOperatorRegistration.addActionListener(e -> {
            gui.goToPanel(OperatorRegister.ID, null);
        });

        itemCityAddData.addActionListener(e -> {
            gui.clearCityAddData();
            gui.goToPanel(CityAddData.ID, null);
        });

        itemAreaLogout.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Sei sicuro di voler uscire dall'area operatore?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                CurrentOperator.getInstance().performLogout();
                JOptionPane.showMessageDialog(this, "Verrai reindirizzato alla Home");
                gui.goToPanel(Home.ID, null);
            }
        });

        itemToggleTheme.addActionListener(e -> {
            gui.appTheme.toggleTheme();
        });

        add(itemHome);
        add(itemCityQuery);

        add(submenuOperator);
        submenuOperator.add(itemOperatorLogin);
        submenuOperator.add(itemOperatorRegistration);
        submenuOperator.add(itemCityAddData);
        submenuOperator.add(itemAreaLogout);
        add(itemToggleTheme);

        for (JMenuItem jMenuItem : new JMenuItem[] {itemHome, itemCityQuery, itemOperatorLogin,
                itemOperatorRegistration, itemCityAddData, itemAreaLogout, itemToggleTheme}) {
            jMenuItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
}
