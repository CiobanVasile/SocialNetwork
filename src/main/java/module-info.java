module com.example.laborator7.Gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;


    exports com.example.laborator7;
    opens com.example.laborator7 to javafx.fxml;
    exports com.example.laborator7.Gui;
    opens com.example.laborator7.Gui to javafx.fxml;
    exports com.example.laborator7.Domain;
    opens com.example.laborator7.Domain;
}