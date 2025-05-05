package cliente.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdministradorController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void registrarUsuario(ActionEvent actionEvent) {
    }

    public void actualizarUsuario(ActionEvent actionEvent) {
    }

    public void eliminarUsuario(ActionEvent actionEvent) {
    }

    public void limpiarCampos(ActionEvent actionEvent) {

    }


    public void cerrarSesionAction(ActionEvent actionEvent) {
    }

    public void agregarVendedorAction(ActionEvent actionEvent) {
    }
}