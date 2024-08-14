package shared.record;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

import shared.utils.Constants;

/**
 * La classe {@code RecordCenter} rappresenta un centro di monitoraggio
 * e contiene informazioni come il nome del centro, il nome della via, il
 * numero civico,
 * il CAP, il nome del comune, la sigla della provincia e una lista di ID delle
 * citt&agrave; associate.
 * <p>
 * Questa classe &egrave; definita come un record, il che significa che &egrave;
 * immutabile
 * una volta creata.
 * </p>
 * * <p>
 *  *     La classe implementa l'interfaccia {@link Serializable} per permettere la
 *  *     serializzazione e la deserializzazione degli oggetti di questa classe.
 *  *     Questo &egrave; necessario per inviare oggetti di questa classe tramite
 *  *     RMI.
 *  * </p>
 * 
 * @param ID           L'ID univoco del centro.
 * @param centerName   Il nome del centro.
 * @param streetName   Il nome della via.
 * @param streetNumber Il numero civico.
 * @param CAP          Il CAP.
 * @param townName     Il nome del comune.
 * @param districtName La sigla della provincia.
 * @param cityIDs      Un array di ID delle citt&agrave; associate a questo centro.
 * 
 * @see Constants
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.1
 * @since 14/08/2024
 */
public record RecordCenter(
                Integer ID,
                String centerName,
                String streetName,
                String streetNumber,
                String CAP,
                String townName,
                String districtName,
                Integer[] cityIDs) implements Serializable {

        /**
         * Restituisce una rappresentazione testuale formattata dell'oggetto
         * {@code RecordCenter},
         * adatta per la memorizzazione o l'esportazione dei dati.
         * 
         * @return Una stringa formattata contenente tutte le informazioni della
         *         citt&agrave;.
         */
        @Override
        public String toString() {

                String cityIDsString = Arrays.stream(cityIDs)
                                .map(Object::toString)
                                .collect(Collectors.joining(Constants.CSV_SUB_SEPARATOR));

                String[] dataStrings = new String[] {
                                Integer.toString(ID),
                                centerName,
                                streetName,
                                streetNumber,
                                CAP,
                                townName,
                                districtName,
                                cityIDsString };

                return String.join(Constants.CSV_SEPARATOR, dataStrings);
        }

        /**
         * Restituisce l'ID del centro.
         * @return L'ID del centro.
         */
        @Override
        public Integer ID() {
                return ID;
        }

        /**
         * Restituisce il nome del centro.
         * @return Il nome del centro.
         */
        @Override
        public String centerName() {
                return centerName;
        }

        /**
         * Restituisce il nome della via.
         * @return Il nome della via.
         */
        @Override
        public String streetName() {
                return streetName;
        }

        /**
         * Restituisce il numero civico.
         * @return Il numero civico.
         */
        @Override
        public String streetNumber() {
                return streetNumber;
        }

        /**
         * Restituisce il CAP.
         * @return Il CAP.
         */
        @Override
        public String CAP() {
                return CAP;
        }

        /**
         * Restituisce il nome del comune.
         * @return Il nome del comune.
         */
        @Override
        public String townName() {
                return townName;
        }

        /**
         * Restituisce il nome della provincia.
         * @return Il nome della provincia.
         */
        @Override
        public String districtName() {
                return districtName;
        }

        /**
         * Restituisce un array di ID delle citt&agrave; associate a questo centro.
         * @return Un array di ID delle citt&agrave; associate a questo centro.
         */
        @Override
        public Integer[] cityIDs() {
                return cityIDs;
        }
}
