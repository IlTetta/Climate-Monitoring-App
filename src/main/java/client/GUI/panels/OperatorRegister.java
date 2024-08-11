package client.GUI.panels;

import javax.swing.*;

import client.GUI.GUI;
import client.GUI.Widget;
import client.GUI.layouts.TwoColumns;
import client.models.CurrentOperator;
import client.models.MainModel;
import shared.utils.Interfaces;

import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * La classe {@code OperatorRegister} &egrave; un pannello Swing utilizzato per
 * la
 * registrazione di un operatore all'interno dell'applicazione.
 * <p>
 * Questo pannello consente agli operatori di inserire i loro dati personali,
 * come nome, codice fiscale, email, username e password,
 * al fine di creare un nuovo account operatore.
 * </p>
 * 
 * @see GUI
 * @see Widget
 * @see TwoColumns
 * @see CurrentOperator
 * @see MainModel
 * @see Interfaces
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 17/09/2023
 */

public class OperatorRegister extends TwoColumns implements Interfaces.UIPanel {

    /**
     * L'ID univoco di questo pannello. Viene utilizzato per identificare e navigare
     * tra i diversi pannelli dell'applicazione.
     */
    public static String ID = "OperatorRegister";

    /**
     * Riferimento all'interfaccia utente grafica (GUI) associata alla barra del
     * menu.
     */
    private GUI gui;

    /**
     * Riferimento al modello principale associato a questo pannello.
     */
    private final MainModel mainModel;

    /**
     * Campo di testo per l'inserimento del nome dell'operatore.
     */
    private final JTextField textfieldName = new JTextField();

    /**
     * Campo di testo per l'inserimento del codice fiscale dell'operatore.
     */
    private final JTextField textfieldTaxCode = new JTextField();

    /**
     * Campo di testo per l'inserimento dell'email dell'operatore.
     */
    private final JTextField textfieldEmail = new JTextField();

    /**
     * Campo di testo per l'inserimento dell'username dell'operatore.
     */
    private final JTextField textfieldUsername = new JTextField();

    /**
     * Campo di testo per l'inserimento della password dell'operatore.
     */
    private final JPasswordField textfieldPassword = new JPasswordField();

    /**
     * Pulsante per effettuare la registrazione come operatore.
     */
    private final JButton buttonPerformRegistration = new Widget.Button("Registrati");

    /**
     * Crea una nuova istanza di OperatorRegister.
     *
     * @param mainModel Il modello principale dell'applicazione.
     */
    public OperatorRegister(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * Aggiunge un {@code ActionListener} al pulsante di registrazione.
     * <p>
     * Quando il pulsante viene premuto, i dati inseriti vengono validati e
     * utilizzati per registrare un nuovo operatore.
     * </p>
     * <p>
     * In caso di errore, viene visualizzato un messaggio di errore.
     * </p>
     */
    public void addActionEvent() {

        buttonPerformRegistration.addActionListener(e -> {

            String nameSurname = textfieldName.getText().trim();
            String taxCode = textfieldTaxCode.getText().trim();
            String email = textfieldEmail.getText().trim();
            String username = textfieldUsername.getText().trim();
            String password = new String(textfieldPassword.getPassword()).trim();

            CurrentOperator currentOperator = CurrentOperator.getInstance();

            if(currentOperator.isUserLogged()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Devi prima effettuare il logout.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }else {

                try {
                    mainModel.logicOperator.performRegistration(
                            nameSurname,
                            taxCode,
                            email,
                            username,
                            password,
                            null);
                    JOptionPane.showMessageDialog(
                            this,
                            "Profilo registrato con successo. Accedi.",
                            "Successo",
                            JOptionPane.INFORMATION_MESSAGE);
                    gui.goToPanel(OperatorLogin.ID, null);

                } catch (IllegalArgumentException e1) {
                    JOptionPane.showMessageDialog(
                            this,
                            e1.getMessage(),
                            "Errore di validazione",
                            JOptionPane.ERROR_MESSAGE);
                } catch (RemoteException e2) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Errore di connessione al server.",
                            "Errore di sistema",
                            JOptionPane.ERROR_MESSAGE);
                } catch (SQLException e3) {
                    JOptionPane.showMessageDialog(
                            this,
                            e3.getMessage(),
                            "Errore di sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Crea il pannello grafico e registra i componenti UI necessari.
     *
     * @param gui L'istanza dell'interfaccia grafica dell'applicazione.
     * @return Restituisce l'istanza corrente di OperatorRegister.
     */
    @Override
    public OperatorRegister createPanel(GUI gui) {
        this.gui = gui;

        addLeft(new Widget.LogoLabel());
        addRight(new Widget.FormPanel(gui.appTheme, "Nome e Cognome", textfieldName));
        addRight(new Widget.FormPanel(gui.appTheme, "Codice Fiscale", textfieldTaxCode));
        addRight(new Widget.FormPanel(gui.appTheme, "Email", textfieldEmail));
        addRight(new Widget.FormPanel(gui.appTheme, "Username", textfieldUsername));
        addRight(new Widget.FormPanel(gui.appTheme, "Password", textfieldPassword));
        addRight(buttonPerformRegistration);

        gui.appTheme.registerPanel(leftPanel);
        gui.appTheme.registerPanel(rightPanel);

        addActionEvent();

        return this;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void onOpen(Object[] args) {
        CurrentOperator currentOperator = CurrentOperator.getInstance();
        currentOperator.performLogout();
    }

}
