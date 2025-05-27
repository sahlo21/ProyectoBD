package proyecto.controller;


import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import proyecto.Aplicacion;
import proyecto.modelo.Administrador;
import proyecto.modelo.GestorEvento;
import proyecto.modelo.Trabajador;
import proyecto.modelo.Usuario;
import proyecto.servicio.LoginServicio;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginController implements Initializable{

	Aplicacion aplicacion;


	@FXML
	private TextField txtUsuarioIngreso;

	@FXML
	private Button buttonLogin;
	@FXML
	private Button buttonCargar;



	@FXML
	private TextField txtContrasenaIngreso;

	@FXML
	private CheckBox cbCondiciones;

	boolean flagLogin=false;
	char tipoLogin;

	Socket miSocket;


	ObjectOutputStream flujoSalidaObject;
	ObjectInputStream flujoEntradaObject;

	DataOutputStream flujoSalidaData;
	DataInputStream flujoEntradaData;
	int contador = 0;
	boolean estadoCargareServer=false;
	private int flagCargar;





	public LoginController() {

	}





	@FXML
	void cbCondiciones(ActionEvent event) {

	}

	@FXML
	private Label wrongLogIn;





	public void iniciarSesionAction(ActionEvent event) throws IOException{


			inicioSesion();


	}


	@FXML
	private void inicioSesion() {
		String usuario = txtUsuarioIngreso.getText();
		String contrasena = txtContrasenaIngreso.getText();

		LoginServicio loginServicio = new LoginServicio();
		Usuario user = loginServicio.iniciarSesion(usuario, contrasena);

		if (user != null) {
			if (user instanceof Administrador) {
				mostrarMensajeInformacion("Bienvenido administrador");
				aplicacion.showAdministrador();
			} else if (user instanceof GestorEvento) {
				mostrarMensajeInformacion("Bienvenido gestor");
				aplicacion.showGestor();
			} else if (user instanceof Trabajador) {
				mostrarMensajeInformacion("Bienvenido trabajador");
				aplicacion.showTrabajador();
			}
		} else {
			mostrarMensajeError("Credenciales incorrectas");
		}
	}



	private void mostrarMensajeInformacion(String mensaje) {

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Informacion");
		alert.setContentText(mensaje);
		alert.showAndWait();
	}
	private void mostrarMensajeError(String mensaje) {

		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setTitle("Confirmacion");
		alert.setContentText(mensaje);
		alert.showAndWait();

	}
	private void mostrarMensaje(String titulo, String header, String contenido, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(titulo);
		alert.setHeaderText(header);
		alert.setContentText(contenido);
		alert.showAndWait();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// La configuración del manejador de cierre se hará en el método setAplicacion
	}

	/**
	 * Maneja el evento de cierre de ventana para registrar el logout
	 */
	private void handleWindowClose(WindowEvent event) {
		// Cerrar sesión y registrar el logout
		LoginServicio loginServicio = new LoginServicio();
		if (LoginServicio.getUsuarioActual() != null) {
			loginServicio.cerrarSesion();
		}
	}

	public void setAplicacion(Aplicacion mainAux) {
		aplicacion = mainAux;

		// Configurar el manejador de cierre de ventana para registrar el logout
		// Esto se hace aquí porque necesitamos que la escena esté completamente cargada
		Platform.runLater(() -> {
			try {
				Stage stage = (Stage) txtUsuarioIngreso.getScene().getWindow();
				stage.setOnCloseRequest(this::handleWindowClose);
			} catch (Exception e) {
				System.err.println("Error al configurar el manejador de cierre: " + e.getMessage());
			}
		});
	}





}
