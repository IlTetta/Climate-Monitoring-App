package models.file;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import javax.swing.JOptionPane;

import utils.Constants;

/**
 * La classe {@code FileHandler} gestisce le operazioni di lettura e scrittura
 * dei file nell'applicazione.
 * <p>
 * Questa classe &egrave; responsabile per la creazione dei file di dati
 * iniziali, la
 * lettura dei contenuti dei file
 * e la scrittura di nuovi dati nei file.
 * </p>
 * 
 * 
 * @see utils.Constants
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class FileHandler {

    /**
     * Costruttore della classe {@code FileHandler}.
     * <p>
     * Inizializza i file di dati iniziali se non esistono.
     * </p>
     * 
     * 
     */
    public FileHandler() {
        try {
            initFile(Constants.Path.Files.CENTER,
                    new String[] {
                            "Center ID",
                            "Center Name",
                            "Street Name",
                            "Street Number",
                            "CAP",
                            "Town Name",
                            "District Name",
                            "City IDs" });

            initFile(Constants.Path.Files.WEATHER,
                    new String[] {
                            "Record ID",
                            "City ID",
                            "Center ID",
                            "Date",
                            "Wind",
                            "Humidity",
                            "Pressure",
                            "Temperature",
                            "Precipitation",
                            "Glacier elevation",
                            "Mass of glaciers" });

            initFile(Constants.Path.Files.CITY,
                    new String[] {
                            "City ID",
                            "Name",
                            "ASCII Name",
                            "Country Code",
                            "Country Name",
                            "Latitude",
                            "Longitude" });

            initFile(Constants.Path.Files.OPERATOR,
                    new String[] {
                            "Operator ID",
                            "Name Surname",
                            "Tax code",
                            "Email",
                            "Username",
                            "Password",
                            "Center ID" });

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Errore nella creazione dei file.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);

        }
    }

    /**
     * Inizializza un file con gli header specificati se il file non esiste.
     * 
     * @param filePath    Il percorso del file da inizializzare.
     * @param fileHeaders Gli header da aggiungere al file.
     * 
     * @throws IOException Se si verifica un errore durante la creazione del file.
     */
    private static void initFile(String filePath, String[] fileHeaders) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(String.join(Constants.CSV_SEPARATOR, fileHeaders) + "\n");
            writer.close();
        }
    }

    /**
     * Legge il contenuto di un file e restituisce le linee lette come una lista di
     * stringhe.
     * 
     * @param filePath Il percorso del file da leggere.
     * @return Una lista di stringhe rappresentanti le linee del file.
     * 
     * @throws IOException Se si verifica un errore durante la lettura del file.
     */
    public static List<String> readFile(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    /**
     * Scrive le linee specificate nel file specificato.
     * 
     * @param filePath Il percorso del file in cui scrivere le linee.
     * @param lines    Una lista di stringhe da scrivere nel file.
     * 
     * @throws IOException Se si verifica un errore durante la scrittura nel file.
     */
    public static void writeFile(String filePath, List<String> lines) throws IOException {
        Files.write(Paths.get(filePath), lines);
    }

    /**
     * Aggiunge una nuova linea alla fine di un file esistente.
     * 
     * @param filePath Il percorso del file a cui aggiungere la nuova linea.
     * @param newLine  La nuova linea da aggiungere al file.
     * 
     * @throws IOException Se si verifica un errore durante l'aggiunta della nuova
     *                     linea.
     */
    public static void appendToFile(String filePath, String newLine) throws IOException {
        FileWriter fout = new FileWriter(filePath, true);
        BufferedWriter wfbuffer = new BufferedWriter(fout);

        wfbuffer.write(newLine);
        wfbuffer.newLine();
        wfbuffer.close();
    }

}
