package server.ImplementationRMI;

import server.ConnectionMaker;
import shared.InterfacesRMI.DataQueryInterface;
import shared.record.*;

import java.io.IOException;
import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static shared.utils.Functions.zeroToNull;

/**
 * La classe {@code DataQueryImp} implementa l'interfaccia {@code DataQueryInterface} e fornisce i metodi
 * per interrogare il database e ottenere i dati richiesti.
 * <p>
 *     La classe si occupa di creare una connessione al database e di eseguire le query richieste.
 *     I metodi implementati permettono di ottenere informazioni riguardanti città, operatori, centri di monitoraggio
 *     e dati meteo.
 * </p>
 *
 * @see DataQueryInterface
 * @see RecordCity
 * @see RecordOperator
 * @see RecordCenter
 * @see RecordWeather
 * @see QueryCondition
 * @see ConnectionMaker
 *
 * @serial exclude
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @author Manuel Morlin
 * @version 1.0
 * @since 14/08/2024
 *
 */
public class DataQueryImp extends UnicastRemoteObject implements DataQueryInterface {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * La connessione al database utilizzata per eseguire le operazioni.
     */
    private final Connection conn;

    /**
     * Costruttore che inizializza la connessione al database utilizzando un file
     * di configurazione.
     *
     * @throws RemoteException Se la connessione al database fallisce
     */
    public DataQueryImp() throws RemoteException {
        super();
        try {
            ConnectionMaker connection = ConnectionMaker.createFromProperties("database.properties");
            this.conn = connection.getConnection();
        } catch (SQLException | IOException e) {
            throw new RemoteException("Inizializzazione della connessione al database fallita", e);
        }
    }

    /**
     * Ottiene le informazioni di una città dal database in base all'ID specificato.
     *
     * @param ID L'ID della città da cercare.
     * @return Un oggetto RecordCity contenente le informazioni della città.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query.
     * @throws RemoteException  Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public RecordCity getCityBy(Integer ID) throws SQLException, RemoteException {
        String sql = "SELECT * FROM coordinatemonitoraggio WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecordCity(rs);
                } else {
                    throw new SQLException("Nessuna città trovata con l'ID specificato");
                }
            }
        }
    }

    /**
     * Ottiene le informazioni delle città dal database in base a una lista di condizioni di ricerca.
     *
     * @param conditions Le condizioni di ricerca.
     * @return Un array di RecordCity contenente le informazioni delle città che soddisfano le condizioni.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query.
     * @throws RemoteException  Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public RecordCity[] getCityBy(List<QueryCondition> conditions) throws SQLException, RemoteException {
        String sql = "SELECT * FROM coordinatemonitoraggio WHERE " + createSQLCondition(conditions);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setPreparedStatementValues(stmt, conditions);
            try (ResultSet rs = stmt.executeQuery()) {
                List<RecordCity> cities = new ArrayList<>();
                while (rs.next()) {
                    cities.add(mapResultSetToRecordCity(rs));
                }
                return cities.toArray(new RecordCity[0]);
            }
        }
    }

    /**
     * Ottiene le informazioni di un operatore dal database in base all'ID specificato.
     *
     * @param ID L'ID dell'operatore da cercare.
     * @return Un oggetto RecordOperator contenente le informazioni dell'operatore.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query.
     * @throws RemoteException  Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public RecordOperator getOperatorBy(Integer ID) throws SQLException, RemoteException {
        String sql = "SELECT * FROM operatoriregistrati WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecordOperator(rs);
                } else {
                    throw new SQLException("Nessun operatore trovato con l'ID specificato");
                }
            }
        }
    }

    /**
     * Ottiene le informazioni di uno o più operatori dal database in base a una condizione di ricerca.
     *
     * @param condition La condizione di ricerca.
     * @return Un array di RecordOperator contenente le informazioni degli operatori che soddisfano la condizione.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query.
     * @throws RemoteException  Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public RecordOperator[] getOperatorBy(QueryCondition condition) throws SQLException, RemoteException {
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(condition);
        return getOperatorBy(conditions);
    }

    /**
     * Ottiene le informazioni di uno o più operatori dal database in base a una lista di condizioni di ricerca.
     *
     * @param conditions Le condizioni di ricerca.
     * @return Un array di RecordOperator contenente le informazioni degli operatori che soddisfano le condizioni.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query.
     * @throws RemoteException  Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public RecordOperator[] getOperatorBy(List<QueryCondition> conditions) throws SQLException, RemoteException {
        String sql = "SELECT * FROM operatoriregistrati WHERE " + createSQLCondition(conditions);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setPreparedStatementValues(stmt, conditions);
            try (ResultSet rs = stmt.executeQuery()) {
                List<RecordOperator> operators = new ArrayList<>();
                while (rs.next()) {
                    operators.add(mapResultSetToRecordOperator(rs));
                }
                return operators.toArray(new RecordOperator[0]);
            }
        }
    }

    /**
     * Ottiene le informazioni di un centro di monitoraggio dal database in base all'ID specificato.
     *
     * @param ID L'ID del centro di monitoraggio da cercare.
     * @return Un oggetto RecordCenter contenente le informazioni del centro, o null se non trovato.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query.
     * @throws RemoteException  Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public RecordCenter getCenterBy(Integer ID) throws SQLException, RemoteException {
        String sql = "SELECT * FROM centrimonitoraggio WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecordCenter(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Ottiene le informazioni di tutti i centri di monitoraggio dal database.
     *
     * @return Un array di RecordCenter contenente le informazioni di tutti i centri di monitoraggio.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query.
     * @throws RemoteException  Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public RecordCenter[] getCenters() throws SQLException, RemoteException {
        String sql = "SELECT * FROM centrimonitoraggio";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<RecordCenter> centers = new ArrayList<>();
                while (rs.next()) {
                    centers.add(mapResultSetToRecordCenter(rs));
                }
                return centers.toArray(new RecordCenter[0]);
            }
        }
    }

    /**
     * Ottiene i parametri climatici dal database in base a una condizione di ricerca.
     *
     * @param condition La condizione di ricerca.
     * @return Un array di RecordWeather contenente i parametri climatici che soddisfano la condizione.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query.
     * @throws RemoteException  Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public RecordWeather[] getWeatherBy(QueryCondition condition) throws SQLException, RemoteException {
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(condition);
        return getWeatherBy(conditions);
    }

    /**
     * Ottiene i parametri climatici dal database in base a una lista di condizioni di ricerca.
     *
     * @param conditions Le condizioni di ricerca.
     * @return Un array di RecordWeather contenente i parametri climatici che soddisfano le condizioni.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query.
     * @throws RemoteException  Se si verifica un errore di comunicazione RMI.
     */
    @Override
    public RecordWeather[] getWeatherBy(List<QueryCondition> conditions) throws SQLException, RemoteException {
        String sql = "SELECT * FROM parametriclimatici WHERE " + createSQLCondition(conditions);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setPreparedStatementValues(stmt, conditions);
            try (ResultSet rs = stmt.executeQuery()) {
                List<RecordWeather> weathers = new ArrayList<>();
                while (rs.next()) {
                    weathers.add(mapResultSetToRecordWeather(rs));
                }
                return weathers.toArray(new RecordWeather[0]);
            }
        }
    }

    /**
     * Crea una stringa di condizione SQL basata su una lista di {@code QueryCondition}.
     *
     * @param conditions Le condizioni di ricerca.
     * @return Una stringa rappresentante la condizione SQL.
     */
    private String createSQLCondition(List<QueryCondition> conditions) {
        StringBuilder conditionString = new StringBuilder();
        for (int i = 0; i < conditions.size(); i++) {
            QueryCondition condition = conditions.get(i);
            if (i > 0) {
                conditionString.append(" AND ");
            }
            if (condition.value() instanceof java.util.Date) {
                conditionString.append("CAST(").append(condition.key()).append(" AS DATE) = ?");
            } else {
                conditionString.append(condition.key()).append(" = ?");
            }
        }
        return conditionString.toString();
    }

    /**
     * Imposta i valori dei parametri nel PreparedStatement basato sulle condizioni di ricerca.
     *
     * @param stmt       Il PreparedStatement da popolare.
     * @param conditions Le condizioni di ricerca contenenti i valori dei parametri.
     * @throws SQLException Se si verifica un errore nell'impostazione dei valori dei parametri.
     */
    private void setPreparedStatementValues(PreparedStatement stmt, List<QueryCondition> conditions) throws SQLException {
        for (int i = 0; i < conditions.size(); i++) {
            Object value = conditions.get(i).value();
            if (value instanceof java.util.Date) {
                stmt.setDate(i + 1, new java.sql.Date(((java.util.Date) value).getTime()));
            } else {
                stmt.setObject(i + 1, value);
            }
        }
    }

    /**
     * Mappa un ResultSet a un oggetto RecordCity.
     *
     * @param rs Il ResultSet contenente i dati della città.
     * @return Un oggetto RecordCity mappato dal ResultSet.
     * @throws SQLException Se si verifica un errore durante la mappatura.
     */
    private RecordCity mapResultSetToRecordCity(ResultSet rs) throws SQLException {
        return new RecordCity(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("asciiname"),
                rs.getString("countrycode"),
                rs.getString("countryname"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude")
        );
    }

    /**
     * Mappa un ResultSet a un oggetto RecordOperator.
     *
     * @param rs Il ResultSet contenente i dati dell'operatore.
     * @return Un oggetto RecordOperator mappato dal ResultSet.
     * @throws SQLException Se si verifica un errore durante la mappatura.
     */
    private RecordOperator mapResultSetToRecordOperator(ResultSet rs) throws SQLException {
        return new RecordOperator(
                rs.getInt("id"),
                rs.getString("namesurname"),
                rs.getString("taxcode"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("centerid")
        );
    }

    /**
     * Mappa un ResultSet a un oggetto RecordCenter.
     *
     * @param rs Il ResultSet contenente i dati del centro di monitoraggio.
     * @return Un oggetto RecordCenter mappato dal ResultSet.
     * @throws SQLException Se si verifica un errore durante la mappatura.
     */
    private RecordCenter mapResultSetToRecordCenter(ResultSet rs) throws SQLException {
        Integer[] cityIds = (Integer[]) rs.getArray("cityids").getArray();
        return new RecordCenter(
                rs.getInt("id"),
                rs.getString("centername"),
                rs.getString("streetname"),
                rs.getString("streetnumber"),
                rs.getString("cap"),
                rs.getString("townname"),
                rs.getString("districtname"),
                cityIds
        );
    }

    /**
     * Mappa un ResultSet a un oggetto RecordWeather.
     *
     * @param rs Il ResultSet contenente i dati dei parametri climatici.
     * @return Un oggetto RecordWeather mappato dal ResultSet.
     * @throws SQLException Se si verifica un errore durante la mappatura.
     */
    private RecordWeather mapResultSetToRecordWeather(ResultSet rs) throws SQLException {
        return new RecordWeather(
                rs.getInt("id"),
                rs.getInt("cityid"),
                rs.getInt("centerid"),
                rs.getString("date"),
                new RecordWeather.WeatherData(zeroToNull(rs.getInt("windscore")), rs.getString("windcomment")),
                new RecordWeather.WeatherData(zeroToNull(rs.getInt("humidityscore")), rs.getString("humiditycomment")),
                new RecordWeather.WeatherData(zeroToNull(rs.getInt("pressurescore")), rs.getString("pressurecomment")),
                new RecordWeather.WeatherData(zeroToNull(rs.getInt("temperaturescore")), rs.getString("temperaturecomment")),
                new RecordWeather.WeatherData(zeroToNull(rs.getInt("precipitationscore")), rs.getString("precipitationcomment")),
                new RecordWeather.WeatherData(zeroToNull(rs.getInt("glacierelevationscore")), rs.getString("glacierelevationcomment")),
                new RecordWeather.WeatherData(zeroToNull(rs.getInt("glaciermassscore")), rs.getString("glaciermasscomment"))
        );
    }

    /**
     * Ritorna l'oggetto {@code Connection} al database, utilizzato anche per scopi di testing.
     *
     * @return L'oggetto Connection al database.
     */
    @Override
    public Connection getConn() {
        return conn;
    }
}
