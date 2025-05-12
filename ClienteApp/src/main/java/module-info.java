module proyecto.clienteapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens proyecto to javafx.fxml;
    exports proyecto;
    exports proyecto.controller;
    opens proyecto.controller to javafx.fxml;
}