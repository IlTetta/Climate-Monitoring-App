package models.data;

import models.record.RecordCenter;
import models.record.RecordCity;
import models.record.RecordOperator;
import models.record.RecordWeather;
import utils.ZeroToNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//classe che implementa i metodi per interrogare il database
public class DataQuery{


    private final Connection conn;

    public DataQuery(Connection conn){
        this.conn = conn;
    }

    public RecordCity getCityBy(Integer ID) throws SQLException {
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

    //non viene mai usato, lo si toglie? //TODO
    public RecordCity[] getCityBy(QueryCondition condition) throws SQLException{
        List<QueryCondition> conditions=new ArrayList<>();
        conditions.add(condition);
        return getCityBy(conditions);
    }

    public RecordCity[] getCityBy(List<QueryCondition> conditions) throws SQLException{
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

    //non viene mai usato, lo si toglie? //TODO
    public RecordOperator getOperatorBy(Integer ID) throws SQLException{
        String sql="SELECT * FROM operatoriregistrati WHERE id = ?";
        try(PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1, ID);
            try(ResultSet rs=stmt.executeQuery()){
                if(rs.next()){
                    return new RecordOperator(
                            rs.getInt("id"),
                            rs.getString("namesurname"),
                            rs.getString("taxcode"),
                            rs.getString("email"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.wasNull() ? null : rs.getInt("centerid")
                    );
                }else{
                    throw new SQLException("Nessun operatore trovato con l'ID specificato");
                }
            }
        }
    }

    public RecordOperator[] getOperatorBy(QueryCondition condition) throws SQLException{
        List<QueryCondition> conditions=new ArrayList<>();
        conditions.add(condition);
        return getOperatorBy(conditions);
    }

    public RecordOperator[] getOperatorBy(List<QueryCondition> conditions) throws SQLException{
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

    public RecordCenter getCenterBy(Integer ID) throws SQLException{
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

//non viene mai usato, lo si toglie? //TODO
    public RecordCenter[] getCenterBy(QueryCondition condition) throws SQLException{
        List<QueryCondition> conditions=new ArrayList<>();
        conditions.add(condition);
        return getCenterBy(conditions);
    }

    public RecordCenter[] getCenterBy(List<QueryCondition> conditions) throws SQLException{
        String sql="SELECT * FROM centrimonitoraggio WHERE "+createSQLCondition(conditions);
        try(PreparedStatement stmt=conn.prepareStatement(sql)){
            setPreparedStatementValues(stmt, conditions);
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

    public RecordCenter[] getCenters() throws SQLException{
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

    //non viene mai usato, lo si toglie? //TODO
    public RecordWeather getWeatherBy(Integer ID) throws SQLException{
        String sql="SELECT * FROM parametriclimatici WHERE id = ?";
        try(PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1, ID);
            try(ResultSet rs=stmt.executeQuery()){
                if(rs.next()){
                    return new RecordWeather(
                            rs.getInt("id"),
                            rs.getInt("cityid"),
                            rs.getInt("centerid"),
                            rs.getString("date"),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("windscore")), rs.getString("windcomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("humidityscore")), rs.getString("humiditycomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("pressurescore")), rs.getString("pressurecomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("temperaturescore")), rs.getString("temperaturecomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("precipitationscore")), rs.getString("precipitationcomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("glacierelevationscore")), rs.getString("glacierelevationcomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("glaciermassscore")), rs.getString("glaciermasscomment"))

                    );
                }else{
                    throw new SQLException("Nessuna registrazione meteo trovata con l'ID specificato");
                }
            }
        }
    }

    public RecordWeather[] getWeatherBy(QueryCondition condition) throws SQLException{
        List<QueryCondition> conditions=new ArrayList<>();
        conditions.add(condition);
        return getWeatherBy(conditions);
    }

    public RecordWeather[] getWeatherBy(List<QueryCondition> conditions) throws SQLException{
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
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("windscore")), rs.getString("windcomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("humidityscore")), rs.getString("humiditycomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("pressurescore")), rs.getString("pressurecomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("temperaturescore")), rs.getString("temperaturecomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("precipitationscore")), rs.getString("precipitationcomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("glacierelevationscore")), rs.getString("glacierelevationcomment")),
                            new RecordWeather.WeatherData(ZeroToNull.zeroToNull(rs.getInt("glaciermassscore")), rs.getString("glaciermasscomment"))
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



    //classe interna per rappresentare una condizione di query
    public static class QueryCondition{
        private final String key;
        private final Object value;

        public QueryCondition(String key, Object value){
            this.key=key;
            this.value=value;
        }

        public String getKey(){
            return key;
        }

        public Object getValue(){
            return value;
        }
    }

    //ritorna la connessione al database per i test
    public Connection getConn() {
        return conn;
    }

}

