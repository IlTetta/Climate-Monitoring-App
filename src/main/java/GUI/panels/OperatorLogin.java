package GUI.panels;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.*;

import GUI.GUI;
import GUI.Widget;
import GUI.layouts.TwoColumns;
import models.CurrentOperator;
import models.MainModel;
import models.record.RecordCenter;
import utils.Interfaces;

/**
 * La classe {@code OperatorLogin} rappresenta un pannello di login per gli
 * operatori dell'applicazione.
 * <p>
 * Gli operatori possono inserire il loro Username e la password per accedere
 * all'applicazione.
 * </p>
 * 
 * @see GUI.GUI
 * @see GUI.Widget
 * @see GUI.layouts.TwoColumns
 * @see models.CurrentOperator
 * @see models.MainModel
 * @see utils.Interfaces
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 17/09/2023
 */
public class OperatorLogin extends TwoColumns implements Interfaces.UIPanel {

    /**
     * L'ID univoco di questo pannello. Viene utilizzato per identificare e navigare
     * tra i diversi pannelli dell'applicazione.
     */
    public static String ID = "OperatorLogin";

    /**
     * Riferimento all'interfaccia utente grafica (GUI) associata alla barra del
     * menu.
     */
    private GUI gui;

    /**
     * Riferimento al modello principale associato a questo pannello.
     */
    private MainModel mainModel;

    /**
     * Campo di testo per l'inserimento dell'Username dell'operatore.
     */
    private JTextField textfieldUsedID = new JTextField();

    /**
     * Campo di testo per l'inserimento della password dell'operatore.
     */
    private JPasswordField textfieldPassword = new JPasswordField();

    /**
     * Pulsante per effettuare l'accesso come operatore.
     */
    private JButton buttonPerformLogin = new Widget.Button("Accedi");

    /**
     * Crea una nuova istanza di {@code OperatorLogin}.
     *
     * @param mainModel Il modello principale dell'applicazione.
     */
    public OperatorLogin(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * Aggiunge un {@code ActionListener} al pulsante di login e un
     * {@code KeyListener} per la pressione del tasto "Enter".
     * <p>
     * Quando il pulsante viene premuto o viene premuto "Enter", i dati inseriti
     * vengono validati e utilizzati per effettuare il login.
     * 
     * <p>
     * In caso di errore, viene visualizzato un messaggio di errore.
     * </p>
     */
    public void addActionEvent() {

        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    String userID = textfieldUsedID.getText();
                    String userPassword = new String(textfieldPassword.getPassword());

                    if (!userID.isEmpty() && !userPassword.isEmpty()) {
                        buttonPerformLogin.doClick();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Completa prima tutti i campi.",
                                "Dato mancante",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        };

        buttonPerformLogin.addActionListener(e -> {
            String userID = textfieldUsedID.getText();
            String userPassword = new String(textfieldPassword.getPassword());

            try {
                mainModel.logicOperator.performLogin(userID, userPassword);

                CurrentOperator currentOperator = CurrentOperator.getInstance();

                proceedToCenterCreation(currentOperator);

            } catch (Exception exception) {

                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Errore di login",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                textfieldPassword.setText("");
            }
        });

        textfieldUsedID.addKeyListener(enterKeyListener);
        textfieldPassword.addKeyListener(enterKeyListener);

    }

    private void proceedToCenterCreation(CurrentOperator currentOperator) {
        if (currentOperator.getCurrentOperator().centerID() == null) {

            String[] options = { "Crea nuovo centro", "Associa centro esistente", "Annulla" };
            var selection = JOptionPane.showOptionDialog(null,
                    "Ancora non hai associato nessun centro. Cosa vuoi fare?",
                    "Centro non definito",
                    0,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (selection == 0) {
                gui.goToPanel(CenterCreateNew.ID, null);
            }
            if (selection == 1) {
                // for (RecordCenter recordCenter : mainModel.data.getCenters()) {
                // comboboxExistingCenter.addItem(recordCenter.ID());
                // }

                RecordCenter[] result = new RecordCenter[0];
                try {
                    result = mainModel.data.getCenters();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (result.length == 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Non ci sono centri disponibili.",
                            "Centro non disponibile",
                            JOptionPane.WARNING_MESSAGE);
                    selection = 2;
                } else {
                    RecordCenter selectedCenter = (RecordCenter) JOptionPane.showInputDialog(
                            this,
                            "Seleziona il centro a cui associarti.",
                            "Seleziona centro",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            result,
                            result[0]);

                    if (selectedCenter != null) {

                        mainModel.logicOperator.associateCenter(selectedCenter.ID());
                        JOptionPane.showMessageDialog(
                                this,
                                "Centro associato con successo.",
                                "Centro associato",
                                JOptionPane.INFORMATION_MESSAGE);
                        gui.goToPanel(CityAddData.ID, null);

                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Centro non selezionato.",
                                "Centro non selezionato",
                                JOptionPane.WARNING_MESSAGE);
                        selection = 2;
                    }
                }
            }

            if (selection == 2) {
                JOptionPane.showMessageDialog(
                        this,
                        "Stai per essere reindirizzato a Home.",
                        "Centro mancante",
                        JOptionPane.INFORMATION_MESSAGE);
                gui.goToPanel(Home.ID, null);
            }

        } else {
            gui.goToPanel(CityAddData.ID, null);
        }
    }

    @Override
    public OperatorLogin createPanel(GUI gui) {
        this.gui = gui;

        addLeft(new Widget.LogoLabel());
        addRight(new Widget.FormPanel(gui.appTheme, "Username", textfieldUsedID));
        addRight(new Widget.FormPanel(gui.appTheme, "Password", textfieldPassword));
        addRight(buttonPerformLogin);

        gui.appTheme.registerPanel(leftPanel);
        gui.appTheme.registerPanel(rightPanel);

        addActionEvent();

        return this;
    }

    @Override
    public String getID() {
        return ID;
    }

    /**
     * Invocato quando il pannello viene aperto.
     * <p>
     * Esegue l'operazione di logout dell'operatore attuale se presente.
     * </p>
     *
     * @param args Argomenti aggiuntivi (non utilizzati in questo caso).
     */
    @Override
    public void onOpen(Object[] args) {
        textfieldPassword.setText("");
        textfieldUsedID.setText("");
        CurrentOperator currentOperator = CurrentOperator.getInstance();

        if (currentOperator.isUserLogged()) {
            Integer response = JOptionPane.showConfirmDialog(
                    null,
                    "Risulti già loggato con UserName: " + currentOperator.getCurrentOperator().username() + "\n"
                            + "Proseguire?",
                    "Utente già loggato",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                proceedToCenterCreation(currentOperator);
            } else {
                currentOperator.performLogout();
                gui.goToPanel(Home.ID, null);
            }
        }
    }

}
