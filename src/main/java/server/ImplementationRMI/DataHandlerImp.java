package server.ImplementationRMI;

import shared.InterfacesRMI.DataHandlerInterface;
import shared.record.RecordCenter;
import shared.record.RecordCity;
import shared.record.RecordOperator;
import shared.record.RecordWeather;
import shared.InterfacesRMI.DataQueryInterface;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// Classe che gestisce i dati: aggiunge e aggiorna i record nel database
public class DataHandlerImp extends UnicastRemoteObject implements DataHandlerInterface {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Connection conn;

    public DataHandlerImp(DataQueryInterface dataQuery) throws RemoteException {
        super();
        try {
            this.conn = dataQuery.getConn();
        } catch (RemoteException e) {
            throw new RemoteException("Inizializzazione fallita", e);
        }
    }

    @Override
    public RecordOperator addNewOperator(String nameSurname,
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
                    return new RecordOperator(newID, nameSurname, taxCode, email, username, password, centerID);
                } else {
                    throw new SQLException("Inserimento fallito, nessun ID generato.");
                }
            }
        }
    }

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

    private void setWeatherData(PreparedStatement stmt, int index, RecordWeather.WeatherData data) throws SQLException {
        if (data.score() != null) {
            stmt.setInt(index, data.score());
        } else {
            stmt.setNull(index, java.sql.Types.INTEGER);
        }
        stmt.setString(index + 1, data.comment());
    }

    @Override
    public void updateOperator(RecordOperator operator) throws SQLException, RemoteException {
        updateRecord("operatoriregistrati", operator.ID(), operator);
    }

    private void updateRecord(String tableName, int ID, Object record) throws SQLException {
        String updateSql = "UPDATE " + tableName + " SET " + getUpdateQueryPart(record) + " WHERE ID = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            setUpdateParameters(updateStmt, record);
            updateStmt.setInt(getParameterCount(record) + 1, ID);
            updateStmt.executeUpdate();
        }
    }

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

    private void setUpdateParameters(PreparedStatement stmt, RecordCity city, int index) throws SQLException {
        stmt.setString(index++, city.name());
        stmt.setString(index++, city.ASCIIName());
        stmt.setString(index++, city.countryCode());
        stmt.setString(index++, city.countryName());
        stmt.setDouble(index++, city.latitude());
        stmt.setDouble(index, city.longitude());
    }

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

    private void setUpdateParameters(PreparedStatement stmt, RecordCenter center, int index) throws SQLException {
        stmt.setString(index++, center.centerName());
        stmt.setString(index++, center.streetName());
        stmt.setString(index++, center.streetNumber());
        stmt.setString(index++, center.CAP());
        stmt.setString(index++, center.townName());
        stmt.setString(index++, center.districtName());
        stmt.setArray(index, conn.createArrayOf("INTEGER", center.cityIDs()));
    }

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