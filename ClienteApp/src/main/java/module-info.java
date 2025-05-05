module cliente.clienteapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens cliente to javafx.fxml;
    exports cliente;
    exports cliente.controller;
    opens cliente.controller to javafx.fxml;
}