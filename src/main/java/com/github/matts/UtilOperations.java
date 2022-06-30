package com.github.matts;

import com.github.matts.constant.Constant;
import com.google.common.io.Files;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for file operations.
 */
public final class UtilOperations {
    private static final Logger LOG = LoggerFactory.getLogger(UtilOperations.class);

    /**
     * Show a FileChooser.
     * @return File object
     */
    public static File getSelectedFile(){
        FileChooser fc = new FileChooser();
        fc.setTitle(Constant.WINDOW_TITLE_SELECT_FILE);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(Constant.CSV_FILTER_EXT_DESC,
                Constant.CSV_FILTER_EXT_VALUE));
        return fc.showOpenDialog(new Stage());
    }

    /**
     * Change CSV output delimiter.
     * @param inputFile Input File object
     * @param inputDelimiter Input delimiter
     * @param outputPath Output folder to store new output CSV
     * @param outputDelimiter Output delimiter
     * @param quoteAll Flag true of quote all, otherwise false
     */
    private static void changeDelimiter(final File inputFile,
                                       final char inputDelimiter,
                                       final String outputPath,
                                       final char outputDelimiter,
                                       final boolean quoteAll) throws IOException {
        String contentType = java.nio.file.Files.probeContentType(inputFile.toPath());
        if(Constant.CSV_MIME_TYPE.equals(contentType)) {
            CSVParser parser = new CSVParserBuilder().withSeparator(inputDelimiter).build();
            try (
                    CSVReader reader = new CSVReaderBuilder
                            (Files.newReader(inputFile, StandardCharsets.UTF_8))
                            .withCSVParser(parser).build();
                    CSVWriter writer = new CSVWriter(new FileWriter(
                            String.format("%s%s", outputPath, inputFile.getName())),
                            outputDelimiter,
                            CSVWriter.DEFAULT_QUOTE_CHARACTER,
                            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                            CSVWriter.DEFAULT_LINE_END)
            ) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    List<String> lineAsString = new ArrayList<>(Arrays.asList(nextLine));
                    writer.writeNext(lineAsString.toArray(new String[Constant.INDEX_ZERO]), quoteAll);
                }
            } catch (IOException | CsvValidationException e) {
                LOG.error("Message", e);
            }
        }
    }

    /**
     * Change CSV output delimiter (Wrap method).
     * @param inputFile Input File object
     * @param inputDelimiter Input delimiter
     * @param outputPath Output folder to store new output CSV
     * @param outputDelimiter Output delimiter
     * @param quoteAll Flag true of quote all, otherwise false
     */
    public static void applyChangeDelimiter(final File inputFile,
                                            final char inputDelimiter,
                                            final String outputPath,
                                            final char outputDelimiter,
                                            final boolean quoteAll) throws IOException {
        if(inputFile.isDirectory()){
            File[] listFiles = inputFile.listFiles();
            if (Objects.nonNull(listFiles) && listFiles.length > Constant.INDEX_ZERO) {
                for (File currentFile : listFiles) {
                    changeDelimiter(currentFile,
                            inputDelimiter,
                            outputPath,
                            outputDelimiter,
                            quoteAll);
                }
            }
        }else{
            changeDelimiter(inputFile,
                    inputDelimiter,
                    outputPath,
                    outputDelimiter,
                    quoteAll);
        }
    }

    /**
     * Create a basic dialog box.
     * @param title Window title
     * @param headerContent Header content
     * @param content Body content
     * @param alertType Alert type
     */
    public static void buildBasicAlert(final String title,
                           final String headerContent,
                           final String content,
                           final Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerContent);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Create a dialog box with full printed stacktrace exception.
     * @param ex Input exception
     */
    public static void buildExceptionAlert(final Exception ex){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An occurred exception!");
        alert.setHeaderText(null);
        alert.setContentText(ex.getMessage());

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, Constant.INDEX_ZERO, Constant.INDEX_ZERO);
        expContent.add(textArea, Constant.INDEX_ZERO, Constant.INDEX_ONCE);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    /**
     * Validate a string value.
     * @param content String content
     * @return true if content length is greater than 0
     */
    public static boolean isValidValue(String content){
        return Objects.nonNull(content) && !Constant.EMPTY.equals(content);
    }

    /**
     * Close file path with end slash.
     * @param input String path
     * @return String
     */
    public static String closePath(String input){
        if(!input.endsWith(Constant.SLASH)){
            input = input + Constant.SLASH;
        }else if(!input.endsWith(Constant.SLASH_WIN)){
            input = input + Constant.SLASH_WIN;
        }
        return input;
    }
}
