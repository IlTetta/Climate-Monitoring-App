package client.models;

import shared.record.RecordOperator;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code CurrentOperator} gestisce l'utente attualmente loggato
 * nell'applicazione
 * e fornisce un meccanismo per notificare i cambiamenti di utente tramite il
 * pattern Observer.
 * <p>
 * &#201; implementata come un Singleton per garantire una sola istanza attiva
 * nell'applicazione.
 * </p>
 * 
 * @see RecordOperator
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class CurrentOperator {
    private static CurrentOperator instance = null;
    private RecordOperator currentOperator = null;
    private List<CurrentUserChangeListener> listeners = new ArrayList<>();

    // Costruttore privato per implementare il pattern Singleton
    private CurrentOperator() {
    }

    /**
     * Restituisce l'istanza unica di {@code CurrentOperator} secondo il pattern
     * Singleton.
     * 
     * @return L'istanza di {@code CurrentOperator}
     */
    public static CurrentOperator getInstance() {
        if (instance == null) {
            instance = new CurrentOperator();
        }
        return instance;
    }

    /**
     * Imposta l'operatore corrente e notifica tutti i listener registrati in caso
     * di cambiamento.
     * 
     * @param operator L'operatore da impostare come corrente
     */
    public void setCurrentOperator(RecordOperator operator) {
        if (operator != currentOperator) {
            currentOperator = operator;
            notifyCurrentUserChange();
        }
    }

    /**
     * Restituisce l'operatore correntemente loggato.
     * 
     * @return L'operatore corrente o {@code null} se nessun utente &egrave; loggato
     */
    public RecordOperator getCurrentOperator() {
        return currentOperator;
    }

    /**
     * Verifica se un utente &egrave; attualmente loggato nell'applicazione.
     * 
     * @return {@code true} se un utente &egrave; loggato, {@code false} altrimenti
     */
    public boolean isUserLogged() {
        return currentOperator != null;
    }

    /**
     * Esegue il logout dell'utente corrente impostando l'operatore corrente a
     * {@code null}.
     */
    public void performLogout() {
        setCurrentOperator(null);
    }

    // Metodi del pattern Observer

    /**
     * L'interfaccia {@code CurrentUserChangeListener} definisce il metodo
     * {@code onCurrentUserChange} che deve essere implementato dalle classi
     * interessate ai cambiamenti dell'utente corrente.
     */
    public interface CurrentUserChangeListener {

        /**
         * Questo metodo viene chiamato quando l'utente corrente cambia.
         * 
         * @param newOperator Il nuovo operatore corrente
         */
        void onCurrentUserChange(RecordOperator newOperator);
    }

    /**
     * Registra un listener interessato ai cambiamenti dell'utente corrente.
     * 
     * @param listener Il listener da registrare
     */
    public void addCurrentUserChangeListener(CurrentUserChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Rimuove un listener precedentemente registrato per i cambiamenti dell'utente
     * corrente.
     * 
     * @param listener Il listener da rimuovere
     */
    public void removeCurrentUserChangeListener(CurrentUserChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifica tutti i listener registrati quando l'utente corrente cambia.
     */
    private void notifyCurrentUserChange() {
        for (CurrentUserChangeListener listener : listeners) {
            listener.onCurrentUserChange(currentOperator);
        }
    }
}
