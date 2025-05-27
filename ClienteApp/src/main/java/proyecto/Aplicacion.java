package proyecto;

import proyecto.controller.AdministradorController;
import proyecto.controller.GestionadorController;
import proyecto.controller.LoginController;
import proyecto.servicio.LoginServicio;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Aplicacion extends Application {
    private Stage primaryStage;
    LoginController logIn;

    // Variable para controlar si se debe registrar el cierre de sesión al cambiar de vista
    private boolean registrarLogout = true;
    @Override
    public void start(Stage primaryStage) {

        try {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Inventario");
            showLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * carga la vista del loggin
     */
    public void showLogin() {


        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/proyecto/view/LoginView.fxml"));

            BorderPane rootLayout = (BorderPane) loader.load();

            logIn = loader.getController();
            logIn.setAplicacion(this);
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Inicio de sesion");
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/inventario.png"))));

            // Center the window on the screen
            primaryStage.centerOnScreen();

            primaryStage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void showAdministrador() {
        try {
            // Configurar el manejador de cierre de ventana para la ventana principal
            primaryStage.setOnCloseRequest(event -> {
                // Cerrar sesión y registrar el logout
                LoginServicio loginServicio = new LoginServicio();
                if (LoginServicio.getUsuarioActual() != null) {
                    loginServicio.cerrarSesion();
                }
            });

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/proyecto/view/AdminView.fxml"));
            BorderPane rootLayout = (BorderPane) loader.load();
            AdministradorController controller = loader.getController();
            controller.setAplicacion(this);
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Portal administrativo");

            // Center the window on the screen
            primaryStage.centerOnScreen();

            primaryStage.show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showGestor() {
        try {
            // Configurar el manejador de cierre de ventana para la ventana principal
            primaryStage.setOnCloseRequest(event -> {
                // Cerrar sesión y registrar el logout
                LoginServicio loginServicio = new LoginServicio();
                if (LoginServicio.getUsuarioActual() != null) {
                    loginServicio.cerrarSesion();
                }
            });

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/proyecto/view/GestionadorView.fxml"));
            BorderPane rootLayout = (BorderPane) loader.load();

            // Get the controller and set the application reference
            GestionadorController controller = loader.getController();
            controller.setAplicacion(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Portal de Gestor");

            // Center the window on the screen
            primaryStage.centerOnScreen();

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTrabajador() {
        try {
            // Configurar el manejador de cierre de ventana para la ventana principal
            primaryStage.setOnCloseRequest(event -> {
                // Cerrar sesión y registrar el logout
                LoginServicio loginServicio = new LoginServicio();
                if (LoginServicio.getUsuarioActual() != null) {
                    loginServicio.cerrarSesion();
                }
            });

            // Aquí deberías cargar la vista del trabajador
            // Por ahora, solo configuramos el manejador de cierre
            primaryStage.setTitle("Portal de Trabajador");

            // Center the window on the screen
            primaryStage.centerOnScreen();

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la vista de gestión de cargos
     * @param adminController El controlador de la vista de administrador para actualizar el combobox de cargos
     */
    public void showCargoView(AdministradorController adminController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/proyecto/view/CargoView.fxml"));
            BorderPane rootLayout = (BorderPane) loader.load();
            AdministradorController controller = loader.getController();
            controller.setAplicacion(this);

            // Crear una nueva ventana para la vista de cargos
            Stage cargoStage = new Stage();
            Scene scene = new Scene(rootLayout);
            cargoStage.setScene(scene);
            cargoStage.setTitle("Gestión de Cargos");
            cargoStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/inventario.png"))));

            // Center the window on the screen
            cargoStage.centerOnScreen();

            // Agregar un listener para cuando se cierre la ventana
            if (adminController != null) {
                cargoStage.setOnHidden(e -> {
                    // Actualizar el combobox de cargos cuando se cierre la ventana
                    adminController.refreshCargos();
                });
            }

            cargoStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
