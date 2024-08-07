package shared.utils;

import java.io.Serial;
import java.io.Serializable;

//classe interna per rappresentare una condizione di query
public class QueryCondition implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;
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