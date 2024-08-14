package shared.record;

import java.io.Serializable;

/**
 * Il record {@link QueryCondition} rappresenta una condizione di query.
 *
 * @param key il parametro su cui fare la query
 * @param value il valore da confrontare
 */
public record QueryCondition(String key, Object value) implements Serializable {

}