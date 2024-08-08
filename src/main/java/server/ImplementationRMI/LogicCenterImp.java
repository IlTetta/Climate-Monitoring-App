package server.ImplementationRMI;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import client.models.CurrentOperator;
import shared.InterfacesRMI.LogicCenterInterface;
import shared.record.RecordCenter;
import shared.record.RecordOperator;
import shared.record.RecordWeather;
import shared.InterfacesRMI.DataHandlerInterface;
import shared.InterfacesRMI.DataQueryInterface;
import shared.utils.Functions;

/**
 * La classe {@code LogicCenterImp} gestisce la logica relativa ai Centri di
 * Monitoraggio.
 * <p>
 * Questa classe offre metodi per inizializzare un nuovo Centro di Monitoraggio
 * e per aggiungere dati meteorologici
 * associati a una citt&agrave; specifica.
 * </p>
 * 
 * @see DataHandlerImp
 * @see RecordCenter
 * @see RecordWeather
 * @see RecordOperator
 * @see CurrentOperator
 * @see Functions
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class LogicCenterImp extends UnicastRemoteObject implements LogicCenterInterface {

    @Serial
    private static final long serialVersionUID = 4L;
    /**
     * Gestore dei dati dell'applicazione.
     */
    private final DataHandlerInterface dataHandler;

    private final DataQueryInterface dataQuery;



    /**
     * Costruttore della classe {@code LogicCenterImp}.
     * 
     * @param dataHandler Il gestore dei dati utilizzato per l'accesso ai dati
     *                    dell'applicazione.
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
     * @param cityIDs      Un array di ID di citt&agrave; associate al centro di
     *                     monitoraggio.
     * 
     * @throws IllegalArgumentException Se uno dei parametri non &egrave; valido.
     * @throws RuntimeException         Se nessun utente &egrave; attualmente
     *                                  loggato o se
     *                                  l'utente &egrave; gi&agrave; associato a un
     *                                  centro di
     *                                  monitoraggio.
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


        if (centerName.isBlank())
            throw new IllegalArgumentException("Nome del Centro non valido!");
        if (streetName.isBlank())
            throw new IllegalArgumentException("Via/Piazza non valida!");
        if (streetNumber.isBlank())
            throw new IllegalArgumentException("Numero civico non valido!");
        if (CAP.isBlank())
            throw new IllegalArgumentException("CAP non valido!");
        if (townName.isBlank())
            throw new IllegalArgumentException("Comune non valido!");
        if (districtName.isBlank())
            throw new IllegalArgumentException("Provincia non valida!");

        for (Integer cityID : cityIDs) {
                if (cityID == null|| dataQuery.getCityBy(cityID) == null) {
                    throw new IllegalArgumentException("Nome della città non valido.");
                }
        }

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
     * Aggiunge dati meteorologici associati a un centro specifico.
     * 
     * @param cityID     L'ID della citt&agrave; a cui sono associati i dati
     *                   meteorologici.
     * @param date       La data relativa ai dati meteorologici.
     * @param tableDatas Una matrice di dati meteorologici da aggiungere.
     * 
     * @throws IllegalArgumentException Se uno dei parametri non &egrave; valido.
     * @throws RuntimeException         Se nessun utente &egrave; attualmente
     *                                  loggato o se
     *                                  l'utente non &egrave; associato a un centro
     *                                  di
     *                                  monitoraggio.
     */
    @Override
    public void addDataToCenter(
            Integer cityID,
            Integer operatorID,
            String date,
            Object[][] tableDatas) throws RemoteException, SQLException {

RecordOperator currentOperator = dataQuery.getOperatorBy(operatorID);

        if (currentOperator == null) {
            throw new RuntimeException("Nessun utente loggato");
        }

        if (currentOperator.centerID() == 0) {
            throw new RuntimeException("L'utente non è associtato a nessun centro");
        }

        if (date ==null || date.isBlank() || !Functions.isDateValid(date))
            throw new IllegalArgumentException("Data non valida");

        boolean allRowsNull = true;
        for (Object[] tableData : tableDatas) {
            if (tableData[0] != null) {
                allRowsNull = false;
                break;
            }
        }
        if (allRowsNull)
            throw new IllegalArgumentException("Dati non validi");

        List<RecordWeather.WeatherData> weatherDataList = new ArrayList<>();
        for (Object[] tableData : tableDatas) {
            Integer integerValue = (Integer) tableData[0];
            String stringValue = (String) tableData[1];
            weatherDataList.add(new RecordWeather.WeatherData(integerValue, stringValue));
        }

        dataHandler.addNewWeather(
                    cityID,
                    currentOperator.centerID(),
                    date,
                    weatherDataList.get(0),
                    weatherDataList.get(1),
                    weatherDataList.get(2),
                    weatherDataList.get(3),
                    weatherDataList.get(4),
                    weatherDataList.get(5),
                    weatherDataList.get(6));
    }
}