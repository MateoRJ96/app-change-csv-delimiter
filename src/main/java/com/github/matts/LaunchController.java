package com.github.matts;

import com.github.matts.constant.Constant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LaunchController {
    private static final Logger LOG = LoggerFactory.getLogger(LaunchController.class);
    public TextField txtPathFilename;
    public TextField txtOutputDelimiter;
    public Button btnSearch;
    public Button btnChange;
    public CheckBox chkQuoteAll;
    public CheckBox chkEnableCustomInputDelimiter;
    public TextField txtCustomInputDelimiter;
    public CheckBox chkScanCurrentDir;
    public TextField txtDefaultOutputDir;

    private File selectedFile;

    @FXML
    public void openFileChooser(ActionEvent actionEvent) {
        selectedFile = UtilOperations.getSelectedFile();
        txtPathFilename.setText(selectedFile.getAbsolutePath());
    }

    @FXML
    public void enableCustomInputDelimiter(ActionEvent actionEvent) {
        boolean isSelected = chkEnableCustomInputDelimiter.isSelected();
        txtCustomInputDelimiter.setDisable(!isSelected);
    }

    @FXML
    public void changeDelimiter(ActionEvent actionEvent) {
        char inputDelimiter = Constant.DEFAULT_DELIMITER;
        char outputDelimiter = Constant.DEFAULT_DELIMITER;
        String defaultOutputDirectory = Constant.DEFAULT_OUTPUT_DIR;
        boolean quoteAll = chkQuoteAll.isSelected();
        boolean scanCurrentDir = chkScanCurrentDir.isSelected();
        if(UtilOperations.isValidValue(txtOutputDelimiter.getText())){
            outputDelimiter = txtOutputDelimiter.getText().charAt(Constant.INDEX_ZERO);
        }
        if(UtilOperations.isValidValue(txtCustomInputDelimiter.getText())){
            inputDelimiter = txtCustomInputDelimiter.getText().charAt(Constant.INDEX_ZERO);
        }
        if(UtilOperations.isValidValue(txtDefaultOutputDir.getText())){
            defaultOutputDirectory = txtDefaultOutputDir.getText();
        }
        if(Objects.nonNull(selectedFile)) {
            String parent = String.format("%s%s/", UtilOperations.closePath(selectedFile.getParent()) , defaultOutputDirectory);
            if(selectedFile.isDirectory()){
                parent = String.format("%s%s/",UtilOperations.closePath(selectedFile.getAbsolutePath()) , defaultOutputDirectory);
            }
            if(new File(parent).mkdirs()){
                LOG.info("Created {}!", parent);
            }
            if(scanCurrentDir){
                selectedFile = selectedFile.getParentFile();
            }
            try {
                UtilOperations.applyChangeDelimiter(
                        selectedFile,
                        inputDelimiter,
                        parent,
                        outputDelimiter,
                        quoteAll
                );
                UtilOperations.buildBasicAlert("Application message!",
                        null,
                        "Done!",
                        Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                UtilOperations.buildExceptionAlert(e);
            }
        }
    }
}