package server.ImplementationRMI;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import shared.InterfacesRMI.LogicCenterInterface;
import shared.InterfacesRMI.DataHandlerInterface;
import shared.InterfacesRMI.DataQueryInterface;
import shared.record.RecordCenter;
import shared.record.RecordOperator;
import shared.record.RecordWeather;
import shared.utils.Functions;

/**
 * La classe {@code LogicCenterImp} implementa i servizi RMI per la gestione dei centri di monitoraggio
 * e dei dati associati, oltre alla loro logica.
 * <p>
 * Questa classe offre metodi per inizializzare un nuovo Centro di Monitoraggio
 * e per aggiungere dati meteorologici associati a una città specifica.
 * </p>
 *
 * @see LogicCenterInterface
 * @see DataHandlerInterface
 * @see DataQueryInterface
 * @see DataHandlerImp
 * @see RecordCenter
 * @see RecordWeather
 * @see RecordOperator
 * @see Functions
 * @serial exclude
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.1
 * @since 14/08/2024
 */
public class LogicCenterImp extends UnicastRemoteObject implements LogicCenterInterface {

    @Serial
    private static final long serialVersionUID = 4L;

    /**
     * Il gestore dei dati utilizzato per l'accesso ai dati dell'applicazione.
     */
    private final DataHandlerInterface dataHandler;

    /**
     * L'interfaccia per le query sui dati.
     */
    private final DataQueryInterface dataQuery;

    /**
     * Costruttore della classe {@code LogicCenterImp} che la inizializza cone le
     * interfacce necessarie.
     *
     * @param dataHandler Il gestore dei dati utilizzato per l'accesso ai dati
     *                    dell'applicazione.
     * @param dataQuery   L'interfaccia per le query sui dati.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    public LogicCenterImp(DataHandlerInterface dataHandler, DataQueryInterface dataQuery) throws RemoteException {
        this.dataHandler = dataHandler;
        this.dataQuery = dataQuery;
    }

    /**
     * Inizializza un nuovo Centro di Monitoraggio con i dati specificati e assegna
     * l'operatore a tale centro.
     *
     * @param centerName   Il nome del centro di monitoraggio.
     * @param streetName   Il nome della via o della piazza.
     * @param streetNumber Il numero civico.
     * @param CAP          Il CAP.
     * @param townName     Il nome del comune.
     * @param districtName Il nome della provincia.
     * @param cityIDs      Un array di ID di città associate al centro di monitoraggio.
     * @param operatorID   L'ID dell'operatore associato al centro.
     *
     * @throws IllegalArgumentException Se uno dei parametri non è valido.
     * @throws SQLException             Se si verifica un errore durante l'accesso al database.
     * @throws RemoteException          Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public void initNewCenter(
            String centerName,
            String streetName,
            String streetNumber,
            String CAP,
            String townName,
            String districtName,
            Integer[] cityIDs,
            Integer operatorID) throws RemoteException, SQLException {

        validateCenterParameters(centerName, streetName, streetNumber, CAP, townName, districtName, cityIDs);

        RecordCenter newCenter = dataHandler.addNewCenter(
                centerName,
                streetName,
                streetNumber,
                CAP,
                townName,
                districtName,
                cityIDs);

        RecordOperator currentOperator = dataQuery.getOperatorBy(operatorID);

        RecordOperator updatedOperator = new RecordOperator(
                currentOperator.ID(),
                currentOperator.nameSurname(),
                currentOperator.taxCode(),
                currentOperator.email(),
                currentOperator.username(),
                currentOperator.password(),
                newCenter.ID());

        dataHandler.updateOperator(updatedOperator);
    }

    /**
     * Aggiunge nuovi dati climatici per una città specifica e li associa al centro di
     * monitoraggio dell'operatore specificato.
     *
     * @param cityID     L'ID della città a cui sono associati i dati meteorologici.
     * @param operatorID L'ID dell'operatore che aggiunge i dati.
     * @param date       La data relativa ai dati meteorologici.
     * @param tableDatas Una matrice di dati meteorologici da aggiungere.
     *
     * @throws IllegalArgumentException Se uno dei parametri non è valido.
     * @throws SQLException             Se si verifica un errore durante l'accesso al database.
     * @throws RemoteException          Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public void addDataToCenter(
            Integer cityID,
            Integer operatorID,
            String date,
            Object[][] tableDatas) throws RemoteException, SQLException {

        validateOperator(operatorID);
        validateWeatherData(date, tableDatas);

        List<RecordWeather.WeatherData> weatherDataList = new ArrayList<>();
        for (Object[] tableData : tableDatas) {
            Integer integerValue = (Integer) tableData[0];
            String stringValue = (String) tableData[1];
            weatherDataList.add(new RecordWeather.WeatherData(integerValue, stringValue));
        }

        dataHandler.addNewWeather(
                cityID,
                dataQuery.getOperatorBy(operatorID).centerID(),
                date,
                weatherDataList.get(0),
                weatherDataList.get(1),
                weatherDataList.get(2),
                weatherDataList.get(3),
                weatherDataList.get(4),
                weatherDataList.get(5),
                weatherDataList.get(6));
    }

    /**
     * Valida i parametri per l'inizializzazione di un nuovo centro di monitoraggio.
     *
     * @param centerName   Il nome del centro.
     * @param streetName   Il nome della via.
     * @param streetNumber Il numero civico.
     * @param CAP          Il CAP.
     * @param townName     Il nome del comune.
     * @param districtName Il nome della provincia.
     * @param cityIDs      Gli ID delle città associate.
     * @throws RemoteException    Se si verifica un errore di comunicazione RMI.
     * @throws SQLException       Se si verifica un errore di accesso al database.
     * @throws IllegalArgumentException Se uno qualsiasi dei parametri non è valido.
     */
    private void validateCenterParameters(String centerName, String streetName, String streetNumber, String CAP,
                                          String townName, String districtName, Integer[] cityIDs) throws RemoteException, SQLException, IllegalArgumentException {
        if (centerName.isBlank())
            throw new IllegalArgumentException("Il nome del centro non può essere vuoto.");
        if (streetName.isBlank())
            throw new IllegalArgumentException("Il nome della via non può essere vuoto.");
        if (streetNumber.isBlank())
            throw new IllegalArgumentException("Il numero civico non può essere vuoto.");
        if (CAP.isBlank())
            throw new IllegalArgumentException("Il CAP non può essere vuoto.");
        if (townName.isBlank())
            throw new IllegalArgumentException("Il nome del comune non può essere vuoto.");
        if (districtName.isBlank())
            throw new IllegalArgumentException("Il nome della provincia non può essere vuoto.");

        for (Integer cityID : cityIDs) {
            if (cityID == null || dataQuery.getCityBy(cityID) == null) {
                throw new IllegalArgumentException("ID della città non valido: " + cityID);
            }
        }
    }

    /**
     * Valida l'operatore specificato assicurandosi che esista e sia associato a un centro di monitoraggio.
     *
     * @param operatorID L'ID dell'operatore da validare.
     * @throws RemoteException       Se si verifica un errore di comunicazione RMI.
     * @throws SQLException          Se si verifica un errore di accesso al database.
     * @throws NoSuchElementException Se l'operatore non esiste.
     * @throws IllegalStateException  Se l'operatore non è associato a nessun centro di monitoraggio.
     */
    private void validateOperator(Integer operatorID) throws RemoteException, SQLException {
        RecordOperator currentOperator = dataQuery.getOperatorBy(operatorID);

        if (currentOperator == null) {
            throw new NoSuchElementException("Nessun operatore trovato con ID specificato.");
        }

        if (currentOperator.centerID() == 0) {
            throw new IllegalStateException("L'operatore non è associato a nessun centro di monitoraggio.");
        }
    }

    /**
     * Valida i dati climatici specificati assicurandosi che la data sia valida e che almeno un dato sia non nullo.
     *
     * @param date       La data da validare.
     * @param tableDatas I dati climatici da validare.
     * @throws IllegalArgumentException Se i dati o la data non sono validi.
     */
    private void validateWeatherData(String date, Object[][] tableDatas) {
        if (date == null || date.isBlank() || !Functions.isDateValid(date)) {
            throw new IllegalArgumentException("Data non valida.");
        }

        boolean allRowsNull = true;
        for (Object[] tableData : tableDatas) {
            if (tableData[0] != null) {
                allRowsNull = false;
                break;
            }
        }

        if (allRowsNull) {
            throw new IllegalArgumentException("Dati meteorologici non validi.");
        }
    }
}
