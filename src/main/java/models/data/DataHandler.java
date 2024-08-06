package models.data;

import models.record.RecordCenter;
import models.record.RecordCity;
import models.record.RecordOperator;
import models.record.RecordWeather;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//Classe che gestisce i dati: aggiunge e aggiorna i record nel database
public class DataHandler  {

    private Connection conn;

    public DataQueryImp dataQuery;

    public DataHandler(Connection conn) {
        this.conn = conn;
        try {
            dataQuery=new DataQueryImp(conn);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    //aggiunge un nuovo operatore al database
    public RecordOperator addNewOperator(String nameSurname,
                                       String taxCode,
                                       String email,
                                       String username,
                                       String password,
                                       Integer centerID) throws SQLException {
        String checkSql = "SELECT * FROM operatoriregistrati WHERE username = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, username);
            if (checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("L'utente esiste già");
            }
        }

        String insertSql="INSERT INTO operatoriregistrati (namesurname, taxcode, email, username, password, centerid) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, nameSurname);
            insertStmt.setString(2, taxCode);
            insertStmt.setString(3, email);
            insertStmt.setString(4, username);
            insertStmt.setString(5, password);

            if(centerID != null) {
                insertStmt.setInt(6, centerID);
            } else {
                insertStmt.setNull(6, java.sql.Types.INTEGER);
            }

            insertStmt.executeUpdate();

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()){
                int newID = generatedKeys.getInt(1);
                return new RecordOperator(newID, nameSurname, taxCode, email, username, password, centerID);
            }else {
                throw new SQLException("Inserimento fallito, nessun ID generato.");
            }
        }


    }

    //aggiunge un nuovo centro al database
    public RecordCenter addNewCenter(String centerName,
                                     String streetName,
                                     String streetNumber,
                                     String CAP,
                                     String townName,
                                     String districtName,
                                     Integer[] cityIDs) throws SQLException {

        String checkSql = "SELECT * FROM centrimonitoraggio WHERE centername = ? AND streetname = ? AND streetnumber = ? AND CAP = ? AND townname = ? AND districtname = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, centerName);
            checkStmt.setString(2, streetName);
            checkStmt.setString(3, streetNumber);
            checkStmt.setString(4, CAP);
            checkStmt.setString(5, townName);
            checkStmt.setString(6, districtName);
            if (checkStmt.executeQuery().next()) {
                throw new IllegalArgumentException("Il centro esiste già");
            }
        }

        String insertSql="INSERT INTO centrimonitoraggio (centername, streetname, streetnumber, cap, townname, districtname, cityids) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, centerName);
            insertStmt.setString(2, streetName);
            insertStmt.setString(3, streetNumber);
            insertStmt.setString(4, CAP);
            insertStmt.setString(5, townName);
            insertStmt.setString(6, districtName);
            insertStmt.setArray(7, conn.createArrayOf("INTEGER", cityIDs));
            insertStmt.executeUpdate();

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()){
                int newID = generatedKeys.getInt(1);
                return new RecordCenter(newID, centerName, streetName, streetNumber, CAP, townName, districtName, cityIDs);
            }else {
                throw new SQLException("Inserimento fallito, nessun ID generato.");
            }
        }
    }

    //aggiunge nuovi parametri climatici al database
    public void addNewWeather(Integer cityID,
                              Integer centerID,
                              String date,
                              RecordWeather.WeatherData wind,
                              RecordWeather.WeatherData humidity,
                              RecordWeather.WeatherData pressure,
                              RecordWeather.WeatherData temperature,
                              RecordWeather.WeatherData precipitation,
                              RecordWeather.WeatherData glacierElevation,
                              RecordWeather.WeatherData glacierMass) throws SQLException {

        String insertSql = "INSERT INTO parametriclimatici (cityid, centerid, date, windscore, windcomment, humidityscore, humiditycomment, pressurescore, pressurecomment, temperaturescore, temperaturecomment, precipitationscore, precipitationcomment, glacierelevationscore, glacierelevationcomment, glaciermassscore, glaciermasscomment) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try(PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStmt.setInt(1, cityID);
            insertStmt.setInt(2, centerID);
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date parseDate = dateFormat.parse(date);
                java.sql.Date sqlDate = new java.sql.Date(parseDate.getTime());
                insertStmt.setDate(3, sqlDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if(wind.score()!=null)
                insertStmt.setInt(4, wind.score());
            else
                insertStmt.setNull(4, java.sql.Types.INTEGER);
            insertStmt.setString(5, wind.comment());
            if (humidity.score()!=null)
                insertStmt.setInt(6, humidity.score());
            else
                insertStmt.setNull(6, java.sql.Types.INTEGER);
            insertStmt.setString(7, humidity.comment());
            if (pressure.score()!=null)
                insertStmt.setInt(8, pressure.score());
            else
                insertStmt.setNull(8, java.sql.Types.INTEGER);
            insertStmt.setString(9, pressure.comment());
            if (temperature.score()!=null)
                insertStmt.setInt(10, temperature.score());
            else
                insertStmt.setNull(10, java.sql.Types.INTEGER);
            insertStmt.setString(11, temperature.comment());
            if (precipitation.score()!=null)
                insertStmt.setInt(12, precipitation.score());
            else
                insertStmt.setNull(12, java.sql.Types.INTEGER);
            insertStmt.setString(13, precipitation.comment());
            if (glacierElevation.score()!=null)
                insertStmt.setInt(14, glacierElevation.score());
            else
                insertStmt.setNull(14, java.sql.Types.INTEGER);
            insertStmt.setString(15, glacierElevation.comment());
            if (glacierMass.score()!=null)
                insertStmt.setInt(16, glacierMass.score());
            else
                insertStmt.setNull(16, java.sql.Types.INTEGER);
            insertStmt.setString(17, glacierMass.comment());
            insertStmt.executeUpdate();

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()){
                int newID = generatedKeys.getInt(1);
                new RecordWeather(newID, cityID, centerID, date, wind, humidity, pressure, temperature, precipitation, glacierElevation, glacierMass);
            }else {
                throw new SQLException("Inserimento fallito, nessun ID generato.");
            }
        }
    }

    //aggiorna un RecordCity nel sistema
    public void updateCity(RecordCity city) throws SQLException{
        updateRecord("coordinatemonitoraggio", city.ID(), city);
    }

    //aggiorna un RecordOperator nel sistema
    public void updateOperator(RecordOperator operator) throws SQLException{
        updateRecord("operatoriregistrati", operator.ID(), operator);
    }

    //aggiorna un RecordCenter nel sistema
    public void updateCenter(RecordCenter center) throws SQLException{
        updateRecord("centrimonitoraggio", center.ID(), center);
    }

    //aggiorna un RecordWeather nel sistema
    public void updateWeather(RecordWeather weather) throws SQLException{
        updateRecord("parametriclimatici", weather.ID(), weather);
    }

    //aggiorna un record nel sistema
    private void updateRecord(String tableName, int ID, Object record) throws SQLException{
        String updateSql = "UPDATE "+tableName+" SET "+getUpdateQueryPart(record)+" WHERE ID = ?";
        try(PreparedStatement updateStmt = conn.prepareStatement(updateSql)){
            setUpdateParameters(updateStmt, record);
            updateStmt.setInt(getParameterCount(record)+1, ID);
            updateStmt.executeUpdate();
        }
    }

    //metodi che generano la parte della query di aggiornamento che rappresenta la parte SET
    private String getUpdateQueryPart(Object object) {
        if (object instanceof RecordCity) {
            return getUpdateQueryPart((RecordCity) object);
        } else if (object instanceof RecordOperator) {
            return getUpdateQueryPart((RecordOperator) object);
        } else if (object instanceof RecordCenter) {
            return getUpdateQueryPart((RecordCenter) object);
        } else if (object instanceof RecordWeather) {
            return getUpdateQueryPart((RecordWeather) object);
        }
        return "";
    }

    private String getUpdateQueryPart(RecordCity city) {
        return "name = ?, street = ?, streetNumber = ?, CAP = ?, townName = ?, districtName = ?";
    }

    private String getUpdateQueryPart(RecordOperator operator) {
        return "nameSurname = ?, taxCode = ?, email = ?, username = ?, password = ?, centerID = ?";
    }

    private String getUpdateQueryPart(RecordCenter center) {
        return "centerName = ?, streetName = ?, streetNumber = ?, CAP = ?, townName = ?, districtName = ?, cityIDs = ?";
    }

    private String getUpdateQueryPart(RecordWeather weather) {
        return "cityID = ?, centerID = ?, date = ?, wind_score = ?, wind_comment = ?, humidity_score = ?, humidity_comment = ?, pressure_score = ?, pressure_comment = ?, temperature_score = ?, temperature_comment = ?, precipitation_score = ?, precipitation_comment = ?, glacierElevation_score = ?, glacierElevation_comment = ?, glacierMass_score = ?, glacierMass_comment = ?";
    }

    //metodi che impostano i parametri della query di aggiornamento
    private void setUpdateParameters(PreparedStatement stmt, Object object) throws SQLException {
        int index = 1;
        if (object instanceof RecordCity) {
            setUpdateParameters(stmt, (RecordCity) object, index);
        } else if (object instanceof RecordOperator) {
            setUpdateParameters(stmt, (RecordOperator) object, index);
        } else if (object instanceof RecordCenter) {
            setUpdateParameters(stmt, (RecordCenter) object, index);
        } else if (object instanceof RecordWeather) {
            setUpdateParameters(stmt, (RecordWeather) object, index);
        }
    }

    private void setUpdateParameters(PreparedStatement stmt, RecordCity city, int startIndex) throws SQLException {
        stmt.setString(startIndex++, city.name());
        stmt.setString(startIndex++, city.ASCIIName());
        stmt.setString(startIndex++, city.countryCode());
        stmt.setString(startIndex++, city.countryName());
        stmt.setDouble(startIndex++, city.latitude());
        stmt.setDouble(startIndex, city.longitude());
    }

    private void setUpdateParameters(PreparedStatement stmt, RecordOperator operator, int startIndex) throws SQLException {
        stmt.setString(startIndex++, operator.nameSurname());
        stmt.setString(startIndex++, operator.taxCode());
        stmt.setString(startIndex++, operator.email());
        stmt.setString(startIndex++, operator.username());
        stmt.setString(startIndex++, operator.password());
        stmt.setInt(startIndex, operator.centerID());
    }

    private void setUpdateParameters(PreparedStatement stmt, RecordCenter center, int startIndex) throws SQLException {
        stmt.setString(startIndex++, center.centerName());
        stmt.setString(startIndex++, center.streetName());
        stmt.setString(startIndex++, center.streetNumber());
        stmt.setString(startIndex++, center.CAP());
        stmt.setString(startIndex++, center.townName());
        stmt.setString(startIndex++, center.districtName());
        stmt.setArray(startIndex, conn.createArrayOf("INTEGER", center.cityIDs()));
    }

    private void setUpdateParameters(PreparedStatement stmt, RecordWeather weather, int startIndex) throws SQLException {
        stmt.setInt(startIndex++, weather.cityID());
        stmt.setInt(startIndex++, weather.centerID());
        stmt.setString(startIndex++, weather.date());
        stmt.setInt(startIndex++, weather.wind().score());
        stmt.setString(startIndex++, weather.wind().comment());
        stmt.setInt(startIndex++, weather.humidity().score());
        stmt.setString(startIndex++, weather.humidity().comment());
        stmt.setInt(startIndex++, weather.pressure().score());
        stmt.setString(startIndex++, weather.precipitation().comment());
        stmt.setInt(startIndex++, weather.temperature().score());
        stmt.setString(startIndex++, weather.temperature().comment());
        stmt.setInt(startIndex++, weather.precipitation().score());
        stmt.setString(startIndex++, weather.precipitation().comment());
        stmt.setInt(startIndex++, weather.glacierElevation().score());
        stmt.setString(startIndex++, weather.glacierElevation().comment());
        stmt.setInt(startIndex++, weather.glacierMass().score());
        stmt.setString(startIndex, weather.glacierMass().comment());
    }

    //metodi che restituiscono il numero di parametri della query di aggiornamento
    private int getParameterCount(Object object) {
        if (object instanceof RecordCity) {
            return getParameterCount((RecordCity) object);
        } else if (object instanceof RecordOperator) {
            return getParameterCount((RecordOperator) object);
        } else if (object instanceof RecordCenter) {
            return getParameterCount((RecordCenter) object);
        } else if (object instanceof RecordWeather) {
            return getParameterCount((RecordWeather) object);
        }
        return 0;
    }

    private int getParameterCount(RecordCity city) {
        return 6; // Numero di parametri per RecordCity
    }

    private int getParameterCount(RecordOperator operator) {
        return 6; // Numero di parametri per RecordOperator
    }

    private int getParameterCount(RecordCenter center) {
        return 7; // Numero di parametri per RecordCenter (incluso cityIDs)
    }

    private int getParameterCount(RecordWeather weather) {
        return 17; // Numero di parametri per RecordWeather (inclusi i punteggi e commenti)
    }
}