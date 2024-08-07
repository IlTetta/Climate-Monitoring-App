package server;

import shared.InterfacesRMI.DataQueryInterface;
import client.models.record.RecordCenter;
import client.models.record.RecordCity;
import client.models.record.RecordOperator;
import client.models.record.RecordWeather;
import shared.utils.QueryCondition;

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

//classe che implementa i metodi per interrogare il database
public class DataQueryImp extends UnicastRemoteObject implements DataQueryInterface{

    @Serial
    private static final long serialVersionUID = 2L;

    private final Connection conn;

    public DataQueryImp() throws RemoteException {
        super();
        try {
            QueryToDB queryToDB = QueryToDB.createFromProperties("database.properties");
            this.conn= queryToDB.getConnection();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RemoteException("Inizializzazione della connessione al database fallita", e);
        }
    }

    @Override
    public RecordCity getCityBy(Integer ID) throws SQLException, RemoteException {
        String sql = "SELECT * FROM coordinatemonitoraggio WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new RecordCity(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("asciiname"),
                            rs.getString("countrycode"),
                            rs.getString("countryname"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude")
                            );
                } else {
                    throw new SQLException("Nessuna città trovata con l'ID specificato");

                }
            }
        }
    }

    @Override
    public RecordCity[] getCityBy(List<QueryCondition> conditions) throws SQLException, RemoteException{
        String sql="SELECT * FROM coordinatemonitoraggio WHERE "+createSQLCondition(conditions);
        try (PreparedStatement stmt=conn.prepareStatement(sql)){
            setPreparedStatementValues(stmt, conditions);
            try (ResultSet rs=stmt.executeQuery()){
                List<RecordCity> cities=new ArrayList<>();
                while (rs.next()){
                    cities.add(new RecordCity(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("asciiname"),
                            rs.getString("countrycode"),
                            rs.getString("countryname"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude")
                    ));
                }
                return cities.toArray(new RecordCity[0]);
            }
        }
    }


    @Override
    public RecordOperator[] getOperatorBy(QueryCondition condition) throws SQLException, RemoteException{
        List<QueryCondition> conditions=new ArrayList<>();
        conditions.add(condition);
        return getOperatorBy(conditions);
    }

    @Override
    public RecordOperator[] getOperatorBy(List<QueryCondition> conditions) throws SQLException, RemoteException{
        String sql="SELECT * FROM operatoriregistrati WHERE "+createSQLCondition(conditions);
        try(PreparedStatement stmt=conn.prepareStatement(sql)){
            setPreparedStatementValues(stmt, conditions);
            try(ResultSet rs=stmt.executeQuery()){
                List<RecordOperator> operators=new ArrayList<>();
                while(rs.next()){
                    operators.add(new RecordOperator(
                            rs.getInt("id"),
                            rs.getString("namesurname"),
                            rs.getString("taxcode"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("centerid")
                    ));
                }
                return operators.toArray(new RecordOperator[0]);
            }
        }
    }

    @Override
    public RecordCenter getCenterBy(Integer ID) throws SQLException, RemoteException{
        String sql="SELECT * FROM centrimonitoraggio WHERE id = ?";
        try(PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1, ID);
            try(ResultSet rs=stmt.executeQuery()){
                if(rs.next()){
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
                }else{
                    return null;
                }
            }
        }
    }

    @Override
    public RecordCenter[] getCenters() throws SQLException, RemoteException{
        String sql="SELECT * FROM centrimonitoraggio";

        try(PreparedStatement stmt=conn.prepareStatement(sql)){
            try(ResultSet rs=stmt.executeQuery()){
                List<RecordCenter> centers=new ArrayList<>();
                while(rs.next()){
                    Integer[] cityIds = (Integer[]) rs.getArray("cityids").getArray();
                    centers.add(new RecordCenter(
                            rs.getInt("id"),
                            rs.getString("centername"),
                            rs.getString("streetname"),
                            rs.getString("streetnumber"),
                            rs.getString("cap"),
                            rs.getString("townname"),
                            rs.getString("districtname"),
                            cityIds
                    ));
                }
                return centers.toArray(new RecordCenter[0]);
            }
        }
    }

    @Override
    public RecordWeather[] getWeatherBy(QueryCondition condition) throws SQLException, RemoteException{
        List<QueryCondition> conditions=new ArrayList<>();
        conditions.add(condition);
        return getWeatherBy(conditions);
    }

    @Override
    public RecordWeather[] getWeatherBy(List<QueryCondition> conditions) throws SQLException, RemoteException{
        String sql="SELECT * FROM parametriclimatici WHERE "+createSQLCondition(conditions);
        try(PreparedStatement stmt=conn.prepareStatement(sql)){
            setPreparedStatementValues(stmt, conditions);
            try(ResultSet rs=stmt.executeQuery()){
                List<RecordWeather> weathers=new ArrayList<>();
                while(rs.next()){
                    weathers.add(new RecordWeather(
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
                    ));
                }
                return weathers.toArray(new RecordWeather[0]);
            }
        }
    }



//crea una stringa di condizione sql basata su una lista di QueryCondition
private String createSQLCondition(List<QueryCondition> conditions) {
    StringBuilder conditionString = new StringBuilder();
    for (int i = 0; i < conditions.size(); i++) {
        QueryCondition condition = conditions.get(i);
        if (i > 0) {
            conditionString.append(" AND ");
        }
        if (condition.getValue() instanceof java.util.Date) {
            conditionString.append("CAST(").append(condition.getKey()).append(" AS DATE) = ?");
        } else {
            conditionString.append(condition.getKey()).append(" = ?");
        }
    }
    return conditionString.toString();
}

    //imposta i valori dei parametri nel PreparedStatement basato sulle condizioni, attenzione al tipo date che deve essere convertito in java.sql.Date
    private void setPreparedStatementValues(PreparedStatement stmt, List<QueryCondition> conditions) throws SQLException {
        for (int i = 0; i < conditions.size(); i++) {
            Object value = conditions.get(i).getValue();
            if (value instanceof java.sql.Date) {
                stmt.setDate(i + 1, (java.sql.Date) value);
            } else if (value instanceof java.util.Date) {
                stmt.setDate(i + 1, new java.sql.Date(((java.util.Date) value).getTime()));
            } else {
                stmt.setObject(i + 1, value);
            }
        }
    }

    //ritorna la connessione al database per i test
    @Override
    public Connection getConn() {
        return conn;
    }

}

