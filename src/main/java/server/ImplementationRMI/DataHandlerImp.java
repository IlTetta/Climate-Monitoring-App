package server.ImplementationRMI;

import shared.interfacesRMI.DataHandlerInterface;
import shared.record.RecordCenter;
import shared.record.RecordCity;
import shared.record.RecordOperator;
import shared.record.RecordWeather;
import shared.interfacesRMI.DataQueryInterface;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * La classe {@code DataHandlerImp} implementa l'interfaccia {@code DataHandlerInterface}
 * e gestisce le operazioni di aggiunta e aggiornamento dei record nel database.
 * <p>
 *     Questa classe utilizza JDBC per interagire con il database e fornisce metodi per
 *     aggiungere nuovi operatori, centri di monitoraggio e parametri climatici, nonché
 *     per aggiornare i record esistenti.
 * </p>
 *
 * @see DataHandlerInterface
 * @see DataQueryInterface
 * @see RecordCenter
 * @see RecordCity
 * @see RecordOperator
 * @see RecordWeather
 * @serial exclude
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @since 14/08/2024
 *
 */
public class DataHandlerImp extends UnicastRemoteObject implements DataHandlerInterface {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * La connessione al database utilizzata per eseguire le operazioni.
     */
    private final Connection conn;

    /**
     * Costruisce un'istanza di {@code DataHandlerImp} utilizzando un'interfaccia
     * {@code DataQueryInterface} per ottenere la connessione al database.
     *
     * @param dataQuery L'interfaccia {@code DataQueryInterface} utilizzata per
     *                  ottenere la connessione al database.
     * @throws RemoteException Se si verifica un errore durante l'inizializzazione.
     */
    public DataHandlerImp(DataQueryInterface dataQuery) throws RemoteException {
        super();
        try {
            this.conn = dataQuery.getConn();
        } catch (RemoteException e) {
            throw new RemoteException("Inizializzazione fallita", e);
        }
    }

    /**
     * Aggiunge un nuovo operatore al database.
     *
     * @param nameSurname Il nome e cognome dell'operatore.
     * @param taxCode     Il codice fiscale dell'operatore.
     * @param email       L'email dell'operatore.
     * @param username    Il nome utente dell'operatore.
     * @param password    La password dell'operatore.
     * @param centerID    L'ID del centro a cui l'operatore è associato (può essere null).
     * @throws SQLException             Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException          Se si verifica un errore durante la comunicazione remota.
     * @throws IllegalArgumentException Se l'utente esiste già.
     */
    @Override
    public void addNewOperator(String nameSurname,
                               String taxCode,
                               String email,
                               String username,
                               String password,
                               Integer centerID) throws SQLException, RemoteException {
        String checkSql = "SELECT * FROM operatoriregistrati WHERE username = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    throw new IllegalArgumentException("L'utente esiste già");
                }
            }
        }

        String insertSql = "INSERT INTO operatoriregistrati (namesurname, taxcode, email, username, password, centerid) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, nameSurname);
            insertStmt.setString(2, taxCode);
            insertStmt.setString(3, email);
            insertStmt.setString(4, username);
            insertStmt.setString(5, password);

            if (centerID != null) {
                insertStmt.setInt(6, centerID);
            } else {
                insertStmt.setNull(6, java.sql.Types.INTEGER);
            }

            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newID = generatedKeys.getInt(1);
                    new RecordOperator(newID, nameSurname, taxCode, email, username, password, centerID);
                } else {
                    throw new SQLException("Inserimento fallito, nessun ID generato.");
                }
            }
        }
    }

    /**
     * Aggiunge un nuovo centro di monitoraggio al database.
     *
     * @param centerName   Il nome del centro di monitoraggio.
     * @param streetName   Il nome della strada del centro di monitoraggio.
     * @param streetNumber Il numero civico del centro di monitoraggio.
     * @param CAP          Il codice postale del centro di monitoraggio.
     * @param townName     Il nome della città del centro di monitoraggio.
     * @param districtName Il nome del distretto del centro di monitoraggio.
     * @param cityIDs      Gli ID delle città associate al centro di monitoraggio.
     * @return Un'istanza di {@code RecordCenter} che rappresenta il centro aggiunto.
     * @throws SQLException       Se si verifica un errore durante l'interazione con il database.
     * @throws RemoteException    Se si verifica un errore durante la comunicazione remota.
     * @throws IllegalArgumentException Se il centro esiste già.
     */
    @Override
    public RecordCenter addNewCenter(String centerName,
                                     String streetName,
                                     String streetNumber,
                                     String CAP,
                                     String townName,
                                     String districtName,
                                     Integer[] cityIDs) throws SQLException, RemoteException {

        String checkSql = "SELECT * FROM centrimonitoraggio WHERE centername = ? AND streetname = ? AND streetnumber = ? AND CAP = ? AND townname = ? AND districtname = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, centerName);
            checkStmt.setString(2, streetName);
            checkStmt.setString(3, streetNumber);
            checkStmt.setString(4, CAP);
            checkStmt.setString(5, townName);
            checkStmt.setString(6, districtName);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    throw new IllegalArgumentException("Il centro esiste già");
                }
            }
        }

        String insertSql = "INSERT INTO centrimonitoraggio (centername, streetname, streetnumber, cap, townname, districtname, cityids) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, centerName);
            insertStmt.setString(2, streetName);
            insertStmt.setString(3, streetNumber);
            insertStmt.setString(4, CAP);
            insertStmt.setString(5, townName);
            insertStmt.setString(6, districtName);
            insertStmt.setArray(7, conn.createArrayOf("INTEGER", cityIDs));
            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newID = generatedKeys.getInt(1);
                    return new RecordCenter(newID, centerName, streetName, streetNumber, CAP, townName, districtName, cityIDs);
                } else {
                    throw new SQLException("Inserimento fallito, nessun ID generato.");
                }
            }
        }
    }

    /**
     * Aggiunge nuovi dati climatici al database.
     *
     * @param cityID            L'ID della città associata ai dati climatici.
     * @param centerID          L'ID del centro di monitoraggio associato ai dati climatici.
     * @param date              La data dei dati climatici nel formato "dd/MM/yyyy".
     * @param wind              I dati relativi al vento.
     * @param humidity          I dati relativi all'umidità.
     * @param pressure          I dati relativi alla pressione.
     * @param temperature       I dati relativi alla temperatura.
     * @param precipitation     I dati relativi alle precipitazioni.
     * @param glacierElevation  I dati relativi all'elevazione del ghiacciaio.
     * @param glacierMass       I dati relativi alla massa del ghiacciaio.
     * @throws SQLException    Se si verifica un errore durante l'inserimento dei dati climatici.
     * @throws RemoteException Se si verifica un errore durante la comunicazione remota.
     */
    @Override
    public void addNewWeather(Integer cityID,
                              Integer centerID,
                              String date,
                              RecordWeather.WeatherData wind,
                              RecordWeather.WeatherData humidity,
                              RecordWeather.WeatherData pressure,
                              RecordWeather.WeatherData temperature,
                              RecordWeather.WeatherData precipitation,
                              RecordWeather.WeatherData glacierElevation,
                              RecordWeather.WeatherData glacierMass) throws SQLException, RemoteException {

        String insertSql = "INSERT INTO parametriclimatici (cityid, centerid, date, windscore, windcomment, humidityscore, humiditycomment, pressurescore, pressurecomment, temperaturescore, temperaturecomment, precipitationscore, precipitationcomment, glacierelevationscore, glacierelevationcomment, glaciermassscore, glaciermasscomment) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStmt.setInt(1, cityID);
            insertStmt.setInt(2, centerID);
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date parseDate = dateFormat.parse(date);
                java.sql.Date sqlDate = new java.sql.Date(parseDate.getTime());
                insertStmt.setDate(3, sqlDate);
            } catch (ParseException e) {
                throw new SQLException("Errore nel parsing della data", e);
            }

            setWeatherData(insertStmt, 4, wind);
            setWeatherData(insertStmt, 6, humidity);
            setWeatherData(insertStmt, 8, pressure);
            setWeatherData(insertStmt, 10, temperature);
            setWeatherData(insertStmt, 12, precipitation);
            setWeatherData(insertStmt, 14, glacierElevation);
            setWeatherData(insertStmt, 16, glacierMass);

            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newID = generatedKeys.getInt(1);
                    new RecordWeather(newID, cityID, centerID, date, wind, humidity, pressure, temperature, precipitation, glacierElevation, glacierMass);
                } else {
                    throw new SQLException("Inserimento fallito, nessun ID generato.");
                }
            }
        }
    }

    /**
     * Imposta i dati climatici per l'istruzione SQL preparata.
     *
     * @param stmt L'oggetto {@code PreparedStatement} su cui impostare i dati.
     * @param index L'indice del parametro da impostare.
     * @param data I dati climatici da impostare.
     * @throws SQLException Se si verifica un errore durante l'impostazione dei dati.
     */
    private void setWeatherData(PreparedStatement stmt, int index, RecordWeather.WeatherData data) throws SQLException {
        if (data.score() != null) {
            stmt.setInt(index, data.score());
        } else {
            stmt.setNull(index, java.sql.Types.INTEGER);
        }
        stmt.setString(index + 1, data.comment());
    }

    /**
     * Aggiorna le informazioni di un operatore nel database.
     *
     * @param operator L'operatore da aggiornare.
     * @throws SQLException    Se si verifica un errore durante l'aggiornamento del database.
     * @throws RemoteException Se si verifica un errore durante la comunicazione remota.
     */
    @Override
    public void updateOperator(RecordOperator operator) throws SQLException, RemoteException {
        updateRecord("operatoriregistrati", operator.ID(), operator);
    }

    /**
     * Aggiorna un record nel database.
     *
     * @param tableName Il nome della tabella in cui aggiornare il record.
     * @param ID        L'ID del record da aggiornare.
     * @param record    L'oggetto che rappresenta il record da aggiornare.
     * @throws SQLException Se si verifica un errore durante l'aggiornamento del database.
     */
    private void updateRecord(String tableName, int ID, Object record) throws SQLException {
        String updateSql = "UPDATE " + tableName + " SET " + getUpdateQueryPart(record) + " WHERE ID = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            setUpdateParameters(updateStmt, record);
            updateStmt.setInt(getParameterCount(record) + 1, ID);
            updateStmt.executeUpdate();
        }
    }

    /**
     * Genera la parte della query SQL per l'aggiornamento dei dati in base al tipo di record.
     *
     * @param object L'oggetto da aggiornare.
     * @return La stringa che rappresenta la parte della query per l'aggiornamento.
     */
    private String getUpdateQueryPart(Object object) {
        if (object instanceof RecordCity) {
            return "name = ?, street = ?, streetNumber = ?, CAP = ?, townName = ?, districtName = ?";
        } else if (object instanceof RecordOperator) {
            return "nameSurname = ?, taxCode = ?, email = ?, username = ?, password = ?, centerID = ?";
        } else if (object instanceof RecordCenter) {
            return "centerName = ?, streetName = ?, streetNumber = ?, CAP = ?, townName = ?, districtName = ?, cityIDs = ?";
        } else if (object instanceof RecordWeather) {
            return "cityID = ?, centerID = ?, date = ?, wind_score = ?, wind_comment = ?, humidity_score = ?, humidity_comment = ?, pressure_score = ?, pressure_comment = ?, temperature_score = ?, temperature_comment = ?, precipitation_score = ?, precipitation_comment = ?, glacierElevation_score = ?, glacierElevation_comment = ?, glacierMass_score = ?, glacierMass_comment = ?";
        }
        return "";
    }

    /**
     * Imposta i parametri per l'aggiornamento di un record nel {@code PreparedStatement}.
     *
     * @param stmt   L'oggetto {@code PreparedStatement} su cui impostare i parametri.
     * @param object L'oggetto da aggiornare.
     * @throws SQLException Se si verifica un errore durante l'impostazione dei parametri.
     */
    private void setUpdateParameters(PreparedStatement stmt, Object object) throws SQLException {
        if (object instanceof RecordCity) {
            setUpdateParameters(stmt, (RecordCity) object, 1);
        } else if (object instanceof RecordOperator) {
            setUpdateParameters(stmt, (RecordOperator) object, 1);
        } else if (object instanceof RecordCenter) {
            setUpdateParameters(stmt, (RecordCenter) object, 1);
        } else if (object instanceof RecordWeather) {
            setUpdateParameters(stmt, (RecordWeather) object, 1);
        }
    }

    /**
     * Imposta i parametri di aggiornamento per un record di tipo {@code RecordCity}.
     *
     * @param stmt  L'oggetto {@code PreparedStatement} su cui impostare i parametri.
     * @param city  Il record {@code RecordCity} da aggiornare.
     * @param index L'indice del parametro da impostare.
     * @throws SQLException Se si verifica un errore durante l'impostazione dei parametri.
     */
    private void setUpdateParameters(PreparedStatement stmt, RecordCity city, int index) throws SQLException {
        stmt.setString(index++, city.name());
        stmt.setString(index++, city.ASCIIName());
        stmt.setString(index++, city.countryCode());
        stmt.setString(index++, city.countryName());
        stmt.setDouble(index++, city.latitude());
        stmt.setDouble(index, city.longitude());
    }

    /**
     * Imposta i parametri di aggiornamento per un record di tipo {@code RecordOperator}.
     *
     * @param stmt     L'oggetto {@code PreparedStatement} su cui impostare i parametri.
     * @param operator Il record {@code RecordOperator} da aggiornare.
     * @param index    L'indice del parametro da impostare.
     * @throws SQLException Se si verifica un errore durante l'impostazione dei parametri.
     */
    private void setUpdateParameters(PreparedStatement stmt, RecordOperator operator, int index) throws SQLException {
        stmt.setString(index++, operator.nameSurname());
        stmt.setString(index++, operator.taxCode());
        stmt.setString(index++, operator.email());
        stmt.setString(index++, operator.username());
        stmt.setString(index++, operator.password());
        if (operator.centerID() != null) {
            stmt.setInt(index, operator.centerID());
        } else {
            stmt.setNull(index, java.sql.Types.INTEGER);
        }
    }

    /**
     * Imposta i parametri di aggiornamento per un record di tipo {@code RecordCenter}.
     *
     * @param stmt   L'oggetto {@code PreparedStatement} su cui impostare i parametri.
     * @param center Il record {@code RecordCenter} da aggiornare.
     * @param index  L'indice del parametro da impostare.
     * @throws SQLException Se si verifica un errore durante l'impostazione dei parametri.
     */
    private void setUpdateParameters(PreparedStatement stmt, RecordCenter center, int index) throws SQLException {
        stmt.setString(index++, center.centerName());
        stmt.setString(index++, center.streetName());
        stmt.setString(index++, center.streetNumber());
        stmt.setString(index++, center.CAP());
        stmt.setString(index++, center.townName());
        stmt.setString(index++, center.districtName());
        stmt.setArray(index, conn.createArrayOf("INTEGER", center.cityIDs()));
    }

    /**
     * Imposta i parametri di aggiornamento per un record di tipo {@code RecordWeather}.
     *
     * @param stmt    L'oggetto {@code PreparedStatement} su cui impostare i parametri.
     * @param weather Il record {@code RecordWeather} da aggiornare.
     * @param index   L'indice del parametro da impostare.
     * @throws SQLException Se si verifica un errore durante l'impostazione dei parametri.
     */
    private void setUpdateParameters(PreparedStatement stmt, RecordWeather weather, int index) throws SQLException {
        stmt.setInt(index++, weather.cityID());
        stmt.setInt(index++, weather.centerID());
        stmt.setDate(index++, java.sql.Date.valueOf(weather.date()));
        setWeatherData(stmt, index++, weather.wind());
        setWeatherData(stmt, index++, weather.humidity());
        setWeatherData(stmt, index++, weather.pressure());
        setWeatherData(stmt, index++, weather.temperature());
        setWeatherData(stmt, index++, weather.precipitation());
        setWeatherData(stmt, index++, weather.glacierElevation());
        setWeatherData(stmt, index, weather.glacierMass());
    }

    /**
     * Restituisce il numero di parametri per un determinato tipo di record.
     *
     * @param record L'oggetto di cui ottenere il conteggio dei parametri.
     * @return Il numero di parametri.
     */
    private int getParameterCount(Object record) {
        if (record instanceof RecordCity) {
            return 6;
        } else if (record instanceof RecordOperator) {
            return 6;
        } else if (record instanceof RecordCenter) {
            return 7;
        } else if (record instanceof RecordWeather) {
            return 16;
        }
        return 0;
    }
}