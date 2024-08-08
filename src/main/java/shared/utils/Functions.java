package shared.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * La classe {@code Functions} fornisce una serie di funzioni di utilit&agrave;
 * per la
 * gestione delle date e degli orari.
 * <p>
 * Queste funzioni includono la generazione della data corrente, la validazione
 * delle date e la generazione della data e dell'orario correnti.
 * </p>
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class Functions {

    /**
     * Il formato di data predefinito utilizzato per la rappresentazione delle date.
     */
    public static final String datePattern = "dd/MM/yyyy";

    /**
     * Restituisce una stringa rappresentante la data corrente nel formato
     * predefinito.
     * 
     * @return Una stringa contenente la data corrente nel formato "dd/MM/yyyy".
     */
    public static String getCurrentDateString() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

        return currentDate.format(formatter);
    }

    /**
     * Verifica se una data rappresentata come stringa &egrave; valida e non
     * &egrave; successiva
     * alla data corrente.
     * 
     * @param dateString La stringa che rappresenta la data da validare.
     * @return true se la data &egrave; valida e non &egrave; successiva alla data
     *         corrente,
     *         altrimenti false.
     */
    public static boolean isDateValid(String dateString) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
            LocalDate date = LocalDate.parse(dateString, dateFormatter);

            return !date.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Restituisce una stringa che rappresenta l'orario e la data correnti nel
     * formato "HH:mm:ss dd/MM/yyyy".
     * 
     * @return Una stringa contenente l'orario e la data correnti nel formato
     *         "HH:mm:ss dd/MM/yyyy".
     */
    public static String getCurrentTimeDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Converte una stringa in un'altra stringa utilizzando l'encoding UTF-8.
     * 
     * @param string La stringa da convertire.
     * @return Una nuova stringa convertita.
     */
    public static String charsetString(String string) {

        Charset charset = StandardCharsets.UTF_8;
        byte[] byteString = string.getBytes(charset);
        return new String(byteString, charset);
    }

    public static Integer zeroToNull(Integer value) {
        return value == 0 ? null : value;
    }

}
