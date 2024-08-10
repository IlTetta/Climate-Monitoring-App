package server.ImplementationRMI;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import shared.InterfacesRMI.LogicCenterInterface;
import shared.InterfacesRMI.DataHandlerInterface;
import shared.InterfacesRMI.DataQueryInterface;
import shared.record.RecordCenter;
import shared.record.RecordOperator;
import shared.record.RecordWeather;
import shared.utils.Functions;

/**
 * La classe {@code LogicCenterImp} gestisce la logica relativa ai Centri di
 * Monitoraggio.
 * <p>
 * Questa classe offre metodi per inizializzare un nuovo Centro di Monitoraggio
 * e per aggiungere dati meteorologici associati a una città specifica.
 * </p>
 *
 * @see DataHandlerImp
 * @see RecordCenter
 * @see RecordWeather
 * @see RecordOperator
 * @see Functions
 *
 * @version 1.0
 * @since 16/09/2023
 */
public class LogicCenterImp extends UnicastRemoteObject implements LogicCenterInterface {

    @Serial
    private static final long serialVersionUID = 4L;

    private final DataHandlerInterface dataHandler;
    private final DataQueryInterface dataQuery;

    /**
     * Costruttore della classe {@code LogicCenterImp}.
     *
     * @param dataHandler Il gestore dei dati utilizzato per l'accesso ai dati
     *                    dell'applicazione.
     * @param dataQuery   L'interfaccia per le query sui dati.
     */
    public LogicCenterImp(DataHandlerInterface dataHandler, DataQueryInterface dataQuery) throws RemoteException {
        this.dataHandler = dataHandler;
        this.dataQuery = dataQuery;
    }

    /**
     * Inizializza un nuovo Centro di Monitoraggio con i dati specificati.
     *
     * @param centerName   Il nome del centro di monitoraggio.
     * @param streetName   Il nome della via o della piazza.
     * @param streetNumber Il numero civico.
     * @param CAP          Il CAP.
     * @param townName     Il nome del comune.
     * @param districtName Il nome della provincia.
     * @param cityIDs      Un array di ID di città associate al centro di monitoraggio.
     * @param operatorID   L'ID dell'operatore che gestirà il centro.
     *
     * @throws IllegalArgumentException Se uno dei parametri non è valido.
     * @throws RuntimeException         Se l'operatore non è valido o già associato a un centro di monitoraggio.
     * @throws SQLException             Se si verifica un errore durante l'accesso ai dati.
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
        if (currentOperator == null) {
            throw new RuntimeException("Operatore non trovato con ID specificato.");
        }

        if (currentOperator.centerID() != 0) {
            throw new RuntimeException("L'operatore è già associato a un centro di monitoraggio.");
        }

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
     * Aggiunge dati meteorologici associati a un centro specifico.
     *
     * @param cityID     L'ID della città a cui sono associati i dati meteorologici.
     * @param operatorID L'ID dell'operatore che aggiunge i dati.
     * @param date       La data relativa ai dati meteorologici.
     * @param tableDatas Una matrice di dati meteorologici da aggiungere.
     *
     * @throws IllegalArgumentException Se uno dei parametri non è valido.
     * @throws RuntimeException         Se l'operatore non è valido o non associato a un centro di monitoraggio.
     * @throws SQLException             Se si verifica un errore durante l'accesso ai dati.
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

    private void validateCenterParameters(String centerName, String streetName, String streetNumber, String CAP,
                                          String townName, String districtName, Integer[] cityIDs) throws RemoteException, SQLException, IllegalArgumentException {
        if (centerName.isBlank())
            throw new IllegalArgumentException();
        if (streetName.isBlank())
            throw new IllegalArgumentException();
        if (streetNumber.isBlank())
            throw new IllegalArgumentException();
        if (CAP.isBlank())
            throw new IllegalArgumentException();
        if (townName.isBlank())
            throw new IllegalArgumentException();
        if (districtName.isBlank())
            throw new IllegalArgumentException();

        for (Integer cityID : cityIDs) {
            if (cityID == null || dataQuery.getCityBy(cityID) == null) {
                throw new IllegalArgumentException("ID della città non valido: " + cityID);
            }
        }
    }

    private void validateOperator(Integer operatorID) throws RemoteException, SQLException {
        RecordOperator currentOperator = dataQuery.getOperatorBy(operatorID);

        if (currentOperator == null) {
            throw new RuntimeException("Nessun operatore trovato con ID specificato.");
        }

        if (currentOperator.centerID() == 0) {
            throw new RuntimeException("L'operatore non è associato a nessun centro di monitoraggio.");
        }
    }

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
