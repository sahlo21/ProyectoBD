module proyecto.clienteapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens proyecto to javafx.fxml;
    opens proyecto.controller to javafx.fxml;
    opens proyecto.modelo to javafx.base; // ← NECESARIO para reflejar propiedades en TableView

    exports proyecto;
    exports proyecto.controller;
}
