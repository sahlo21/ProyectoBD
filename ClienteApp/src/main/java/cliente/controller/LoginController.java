package cliente.controller;


import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import cliente.Aplicacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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



	void inicioSesion() throws IOException {

		String usuario = txtUsuarioIngreso.getText().toString();
		String clave = txtContrasenaIngreso.getText().toString();

		String host = "localhost"; // Cambia si el servidor está en otra IP
		int puerto = 5000;

		try (Socket socket = new Socket(host, puerto);
			 PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
			 BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

			// Enviar datos al servidor
			salida.println(usuario + "|" + clave);

			// Leer respuesta del servidor
			String respuesta = entrada.readLine();
			if ("OK".equals(respuesta)) {
				System.out.println("Inicio de sesión exitoso.");
				mostrarMensajeInformacion("Bienvenido administrador");
				aplicacion.showAdministrador();
			} else {
				System.out.println("Usuario o contraseña incorrectos.");
			}

		} catch (Exception e) {
			e.printStackTrace();
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

	}
	public void setAplicacion(Aplicacion mainAux) {
		aplicacion= mainAux;

	}





}


