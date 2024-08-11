package client.models;

import shared.record.RecordOperator;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code CurrentOperator} è un singleton che gestisce lo stato dell'operatore attualmente loggato.
 * Fornisce metodi per settare e recuperare l'operatore corrente, controllare lo stato di login, effettuare
 * il logout e gestire i listener che osservano i cambiamenti dell'operatore corrente.
 *
 * <p>
 *     Questa classe usa il pattern Singleton per assicurarsi che ci sia una sola istanza di {@code CurrentOperator}.
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

    /**
     * L'unica istanza della classe {@code CurrentOperator}.
     */
    private static CurrentOperator instance = null;

    /**
     * L'operatore attualmente loggato.
     */
    private RecordOperator currentOperator = null;

    /**
     * Lista dei listener che vengono notificati quando l'operatore corrente cambia.
     */
    private final List<CurrentUserChangeListener> listeners = new ArrayList<>();

    /**
     * Costruttore privato per evitare istanziazioni multiple della classe {@code CurrentOperator}.
     */
    private CurrentOperator() {
    }

    /**
     * Restituisce l'unica istanza della classe {@code CurrentOperator}.
     * Se l'istanza non esiste ancora, viene creata.
     *
     * @return l'istanza singleton di {@code CurrentOperator}
     */
    public static CurrentOperator getInstance() {
        if (instance == null) {
            instance = new CurrentOperator();
        }
        return instance;
    }

    /**
     * Imposta l'operatore corrente.
     * Se l'operatore è diverso dall'attuale, notifica i listener del cambiamento.
     *
     * @param operator il nuovo operatore corrente
     */
    public void setCurrentOperator(RecordOperator operator) {
        if (operator != currentOperator) {
            currentOperator = operator;
            notifyCurrentUserChange();
        }
    }

    /**
     * Restituisce l'operatore attualmente loggato.
     *
     * @return l'operatore corrente, o {@code null} se nessun operatore è loggato.
     */
    public RecordOperator getCurrentOperator() {
        return currentOperator;
    }

    /**
     * Verifica se un operatore è attualmente loggato.
     *
     * @return {@code true} se un operatore è loggato, {@code false} altrimenti
     */
    public boolean isUserLogged() {
        return currentOperator != null;
    }

    /**
     * Effettua il logout dell'operatore corrente, settando il valore a {@code null}.
     */
    public void performLogout() {
        setCurrentOperator(null);
    }

    /**
     * Interfaccia che deve essere implementata dai listener per reagire ai cambiamenti
     * dell'operatore corrente.
     */
    public interface CurrentUserChangeListener {

        /**
         * Metodo chiamato quando l'operatore corrente cambia.
         *
         * @param newOperator il nuovo operatore corrente
         */
        void onCurrentUserChange(RecordOperator newOperator);
    }

    /**
     * Aggiunge un listener alla lista dei listener che vengono notificati quando
     * l'operatore corrente cambia.
     *
     * @param listener il listener da aggiungere
     */
    public void addCurrentUserChangeListener(CurrentUserChangeListener listener) {
        listeners.add(listener);
    }


    /**
     * Rimuove un listener dalla lista dei listener che vengono notificati quando
     * l'operatore corrente cambia.
     *
     * @param listener il listener da rimuovere
     */
    public void removeCurrentUserChangeListener(CurrentUserChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifica tutti i listener che l'operatore corrente è cambiato.
     */
    private void notifyCurrentUserChange() {
        for (CurrentUserChangeListener listener : listeners) {
            listener.onCurrentUserChange(currentOperator);
        }
    }
}