package models.logic;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.CurrentOperator;
import models.data.DataHandlerImp;
import models.record.RecordCenter;
import models.record.RecordOperator;
import models.record.RecordWeather.WeatherData;
import utils.Functions;

/**
 * La classe {@code LogicCenter} gestisce la logica relativa ai Centri di
 * Monitoraggio.
 * <p>
 * Questa classe offre metodi per inizializzare un nuovo Centro di Monitoraggio
 * e per aggiungere dati meteorologici
 * associati a una citt&agrave; specifica.
 * </p>
 * 
 * @see DataHandlerImp
 * @see models.record.RecordCenter
 * @see models.record.RecordWeather
 * @see models.record.RecordOperator
 * @see models.CurrentOperator
 * @see utils.Functions
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class LogicCenter {

    /**
     * Gestore dei dati dell'applicazione.
     */
    private DataHandlerImp dataHandler;

    /**
     * Costruttore della classe {@code LogicCenter}.
     * 
     * @param dataHandler Il gestore dei dati utilizzato per l'accesso ai dati
     *                    dell'applicazione.
     */
    public LogicCenter(DataHandlerImp dataHandler) {
        this.dataHandler = dataHandler;
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
    public void initNewCenter(
            String centerName,
            String streetName,
            String streetNumber,
            String CAP,
            String townName,
            String districtName,
            Integer[] cityIDs) {

        CurrentOperator currentOperator = CurrentOperator.getInstance();

        if (currentOperator.isUserLogged() == false) {
            throw new RuntimeException("Nessun utente loggato");
        }

        if (currentOperator.getCurrentOperator().centerID() != 0) {
            throw new RuntimeException("Utente fa già parte di un Centro");
        }

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
            try {
                if (dataHandler.dataQuery.getCityBy(cityID) == null) {
                    throw new IllegalArgumentException("Nome della città non valido.");
                }
            } catch (SQLException | RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        RecordCenter newCenter = null;
        try {
            newCenter = dataHandler.addNewCenter(
                    centerName,
                    streetName,
                    streetNumber,
                    CAP,
                    townName,
                    districtName,
                    cityIDs);
        } catch (SQLException | RemoteException e) {
            throw new RuntimeException(e);
        }

        RecordOperator updatedOperator = new RecordOperator(
                currentOperator.getCurrentOperator().ID(),
                currentOperator.getCurrentOperator().nameSurname(),
                currentOperator.getCurrentOperator().taxCode(),
                currentOperator.getCurrentOperator().email(),
                currentOperator.getCurrentOperator().username(),
                currentOperator.getCurrentOperator().password(),
                newCenter.ID());

        try {
            dataHandler.updateOperator(updatedOperator);
        } catch (SQLException | RemoteException e) {
            throw new RuntimeException(e);
        }
        currentOperator.setCurrentOperator(updatedOperator);
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
    public void addDataToCenter(
            Integer cityID,
            String date,
            Object[][] tableDatas) {

        CurrentOperator currentOperator = CurrentOperator.getInstance();

        if (currentOperator.isUserLogged() == false) {
            throw new RuntimeException("Nessun utente loggato");
        }

        if (currentOperator.getCurrentOperator().centerID() == 0) {
            throw new RuntimeException("L'utente non è associtato a nessun centro");
        }

        if (date.isBlank() || !Functions.isDateValid(date))
            throw new IllegalArgumentException("Data non valida");

        boolean allRowsNull = true;
        for (int i = 0; i < tableDatas.length; i++) {
            if (tableDatas[i][0] != null) {
                allRowsNull = false;
                break;
            }
        }
        if (allRowsNull)
            throw new IllegalArgumentException("Dati non validi");

        List<WeatherData> weatherDataList = new ArrayList<>();
        for (int i = 0; i < tableDatas.length; i++) {
            Integer integerValue = (Integer) tableDatas[i][0];
            String stringValue = (String) tableDatas[i][1];
            weatherDataList.add(new WeatherData(integerValue, stringValue));
        }

        try {
            dataHandler.addNewWeather(
                    cityID,
                    currentOperator.getCurrentOperator().centerID(),
                    date,
                    weatherDataList.get(0),
                    weatherDataList.get(1),
                    weatherDataList.get(2),
                    weatherDataList.get(3),
                    weatherDataList.get(4),
                    weatherDataList.get(5),
                    weatherDataList.get(6));
        } catch (SQLException | RemoteException e) {
            throw new RuntimeException(e);
        }

    }

}
