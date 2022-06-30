package com.github.matts;

import com.github.matts.constant.Constant;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppChangeCsvDelimiter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppChangeCsvDelimiter.class.getResource(Constant.WINDOW_FXML_RESOURCE));
        Scene scene = new Scene(fxmlLoader.load(), Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
        stage.setTitle(Constant.WINDOW_TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}