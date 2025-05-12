package proyecto;

import proyecto.controller.LoginController;
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

            primaryStage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void showAdministrador() {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/proyecto/view/AdminView.fxml"));
            BorderPane rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Portal administrativo");

            primaryStage.show();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void showGestor() {
    }

    public void showTrabajador() {

    }
}