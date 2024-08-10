package shared.record;

import java.io.Serializable;

//Implementa Serializable per poter essere inviata tramite RMI
public record QueryCondition(String key, Object value) implements Serializable {

}