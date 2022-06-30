module com.github.matts {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.opencsv;
    requires com.google.common;
    requires org.slf4j;

    opens com.github.matts to javafx.fxml;
    exports com.github.matts;
}