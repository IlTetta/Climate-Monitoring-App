package client.GUI.panels;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.*;

import client.GUI.GUI;
import client.GUI.Widget;
import client.GUI.layouts.TwoColumns;
import client.models.CurrentOperator;
import client.models.MainModel;
import shared.record.RecordCenter;
import shared.record.RecordOperator;
import shared.utils.Interfaces;

/**
 * La classe {@code OperatorLogin} rappresenta un pannello di login per gli
 * operatori dell'applicazione.
 * <p>
 * Gli operatori possono inserire il loro Username e la password per accedere
 * all'applicazione.
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
    private final MainModel mainModel;

    /**
     * Campo di testo per l'inserimento dell'Username dell'operatore.
     */
    private final JTextField textfieldUsedID = new JTextField();

    /**
     * Campo di testo per l'inserimento della password dell'operatore.
     */
    private final JPasswordField textfieldPassword = new JPasswordField();

    /**
     * Pulsante per effettuare l'accesso come operatore.
     */
    private final JButton buttonPerformLogin = new Widget.Button("Accedi");

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

            CurrentOperator currentOperator = CurrentOperator.getInstance();

            try {
                RecordOperator loggedOperator = mainModel.logicOperator.performLogin(userID, userPassword);
                if (loggedOperator == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Utente non trovato",
                            "Errore di login",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    currentOperator.setCurrentOperator(loggedOperator);
                    proceedToCenterCreation(currentOperator);
                }
            } catch (IllegalArgumentException exception){
                JOptionPane.showMessageDialog(
                        this,
                        "Username o password non validi.",
                        "Errore di login",
                        JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Errore durante l'accesso al database.",
                        "Errore di connessione",
                        JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Errore di connessione al server.",
                        "Errore di connessione",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                textfieldPassword.setText("");
            }
        });

        textfieldUsedID.addKeyListener(enterKeyListener);
        textfieldPassword.addKeyListener(enterKeyListener);

    }

    private void proceedToCenterCreation(CurrentOperator currentOperator) {
        if (currentOperator.getCurrentOperator().centerID() == 0) {

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
                RecordCenter[] result;
                try {
                    result = mainModel.dataQuery.getCenters();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Errore durante l'accesso al database.",
                            "Errore di connessione",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (RemoteException e) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Errore di connessione al server.",
                            "Errore di connessione",
                            JOptionPane.ERROR_MESSAGE);
                    return;
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
                        RecordOperator updatedOperator;

                        try {
                            updatedOperator=mainModel.logicOperator.associateCenter(currentOperator.getCurrentOperator().ID(),selectedCenter.ID());
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Errore durante l'accesso al database.",
                                    "Errore di connessione",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } catch (RemoteException e) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Errore di connessione al server.",
                                    "Errore di connessione",
                                    JOptionPane.ERROR_MESSAGE);
                            return;

                        }
                        currentOperator.setCurrentOperator(updatedOperator);

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
            int response = JOptionPane.showConfirmDialog(
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