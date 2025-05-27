package proyecto.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
import javafx.util.StringConverter;
import proyecto.Aplicacion;
import proyecto.modelo.*;
import proyecto.servicio.CargoServicio;
import proyecto.servicio.GestorServicio;
import proyecto.servicio.LoginServicio;
import proyecto.servicio.ProveedorServicio;
import proyecto.servicio.TrabajadorServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministradorController implements Initializable {

    ObservableList<Trabajador> listaTrabajadoresData = FXCollections.observableArrayList();
    ObservableList<Producto> listaProductosData = FXCollections.observableArrayList();
    ObservableList<Proveedor> listaProveedoresData = FXCollections.observableArrayList();
    ObservableList<GestorEvento> listaGestoresData = FXCollections.observableArrayList();
    ObservableList<Cargo> listaCargosData = FXCollections.observableArrayList();
    Trabajador trabajadorSeleccionado;
    GestorEvento gestorSeleccionado;
    Cargo cargoSeleccionado;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private ComboBox<Cargo> comboBoxCargo;

    @FXML
    private TableColumn<GestorEvento, String> columnCedulaGestor;

    @FXML
    private TableColumn<Trabajador, String> columnCedulaTrabajador;

    @FXML
    private TableColumn<GestorEvento, String> columnTelefonoPrincipalGestor;

    @FXML
    private TableColumn<Trabajador, String> columnCargo;

    @FXML
    private TableColumn<Trabajador, String> columnTelefonoPrincipal;

    @FXML
    private TableColumn<GestorEvento, String> columnNombreGestor;

    @FXML
    private TableColumn<Trabajador, String> columnNombreTrabajador;

    @FXML
    private TableColumn<GestorEvento, String> columnUsuarioGestor;

    @FXML
    private TableColumn<Trabajador, String> columnUsuarioTrabajador;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblHora;

    @FXML
    private Label lblUserAdmin;

    @FXML
    private TableView<GestorEvento> tableGestores;

    @FXML
    private TableView<Trabajador> tableTrabajador;

    @FXML
    private TextField txtContrasenaGestor;

    @FXML
    private TextField txtContrasenaTrabajador;

    @FXML
    private TextField txtPrecioEvento;

    @FXML
    private TextField txtCedulaGestor;

    @FXML
    private TextField txtNombreGestor;

    @FXML
    private TextField txtNombreTrabajador;

    @FXML
    private TextField txtTlf1Gestor;

    @FXML
    private TextField txtTlf1Trabajador;

    @FXML
    private TextField txtTlf2Gestor;

    @FXML
    private TextField txtTlf2Trabajador;

    @FXML
    private TextField txtCedulaTrabajador;

    @FXML
    private TextField txtUsuarioGestor;

    @FXML
    private TextField txtUsuarioTrabajador;
    private Aplicacion aplicacion;



    @FXML
    void agregarTrabajadorAction(ActionEvent event) {
        try {
            agregarTrabajador();
        } catch (Exception e) {
            mostrarMensajeError(e.getMessage());
        }
    }


    @FXML
    void cerrarSesionAction(ActionEvent event) {
        // Cerrar sesión y registrar el logout
        LoginServicio loginServicio = new LoginServicio();
        if (LoginServicio.getUsuarioActual() != null) {
            loginServicio.cerrarSesion();
        }
        aplicacion.showLogin();
    }

    @FXML
    void eliminarGestor(ActionEvent event) {
        gestorSeleccionado = tableGestores.getSelectionModel().getSelectedItem();

        if (gestorSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un gestor de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de eliminar al gestor con cédula " + gestorSeleccionado.getCedula() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            GestorServicio servicio = new GestorServicio();
            boolean eliminado = servicio.eliminarGestor(gestorSeleccionado.getCedula());

            if (eliminado) {
                listaGestoresData.remove(gestorSeleccionado);
                tableGestores.refresh();
                limpiarCamposGestor();
                mostrarMensaje("Eliminación", null, "Gestor eliminado con éxito.", Alert.AlertType.INFORMATION);
            } else {
                mostrarMensajeError("No se pudo eliminar el gestor.");
            }
        }
    }

    @FXML
    void actualizarGestor(ActionEvent event) {
        if (gestorSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un gestor de la tabla para actualizar.");
            return;
        }

        String error = validarCamposGestor();
        if (!error.isEmpty()) {
            mostrarAlerta("Error en campos", error);
            return;
        }

        int cedula = Integer.parseInt(txtCedulaGestor.getText().trim());
        String nombre = txtNombreGestor.getText().trim();
        String usuario = txtUsuarioGestor.getText().trim();
        String contrasena = txtContrasenaGestor.getText().trim();

        ArrayList<String> telefonos = new ArrayList<>();
        telefonos.add(txtTlf1Gestor.getText().trim());
        telefonos.add(txtTlf2Gestor.getText().trim());

        GestorEvento gestorActualizado = new GestorEvento(cedula, nombre, usuario, contrasena, telefonos);

        GestorServicio servicio = new GestorServicio();
        boolean actualizado = servicio.actualizarGestor(gestorActualizado);

        if (actualizado) {
            int index = listaGestoresData.indexOf(gestorSeleccionado);
            listaGestoresData.set(index, gestorActualizado);
            tableGestores.refresh();
            limpiarCamposGestor();
            mostrarMensaje("Actualización", null, "Gestor actualizado con éxito.", Alert.AlertType.INFORMATION);
            gestorSeleccionado = null;
        } else {
            mostrarMensajeError("No se pudo actualizar el gestor.");
        }
    }


    @FXML
    void agregarGestorAction(ActionEvent event) {
        agregarGestor();
    }

    private void agregarGestor() {
        // Validar los datos antes de crear el gestor
        String error = validarCamposGestor();
        if (!error.isEmpty()) {
            mostrarAlerta("Error en campos", error);
            return;
        }

        String nombre = txtNombreGestor.getText().trim();
        String usuario = txtUsuarioGestor.getText().trim();
        String contrasena = txtContrasenaGestor.getText().trim();
        int cedula = Integer.parseInt(txtCedulaGestor.getText().trim());

        // Validar que la cédula no exista ya en la lista de gestores
        List<GestorEvento> gestores = GestorServicio.obtenerGestores();
        for (GestorEvento g : gestores) {
            if (g.getCedula() == cedula) {
                mostrarMensajeError("El gestor con cédula: " + cedula + " ya se encuentra registrado");
                return;
            }
        }

        ArrayList<String> telefonos = new ArrayList<>();
        telefonos.add(txtTlf1Gestor.getText().trim());
        telefonos.add(txtTlf2Gestor.getText().trim());

        GestorEvento gestor = new GestorEvento(cedula, nombre, usuario, contrasena, telefonos);

        GestorServicio servicio = new GestorServicio();
        GestorEvento gestorAux = servicio.crearGestor(gestor);
        if (gestorAux != null) {
            listaGestoresData.add(gestorAux);
            limpiarCamposGestor();
            mostrarMensaje("Notificación Gestor", null, "El gestor se ha creado con éxito",
                    Alert.AlertType.INFORMATION);
        } else {
            mostrarMensajeError("El gestor con cédula: " + cedula + " ya se encuentra registrado");
        }
    }

    private String validarCamposGestor() {
        StringBuilder errores = new StringBuilder();

        if (txtNombreGestor.getText().trim().isEmpty()) {
            errores.append("El nombre no puede estar vacío\n");
        }

        if (txtUsuarioGestor.getText().trim().isEmpty()) {
            errores.append("El usuario no puede estar vacío\n");
        }

        if (txtContrasenaGestor.getText().trim().isEmpty()) {
            errores.append("La contraseña no puede estar vacía\n");
        }

        if (txtCedulaGestor.getText().trim().isEmpty()) {
            errores.append("La cédula no puede estar vacía\n");
        } else if (!isNumericInt(txtCedulaGestor.getText().trim())) {
            errores.append("La cédula debe ser un número entero\n");
        }

        if (txtTlf1Gestor.getText().trim().isEmpty()) {
            errores.append("El teléfono 1 no puede estar vacío\n");
        }

        return errores.toString();
    }

    private void limpiarCamposGestor() {
        txtNombreGestor.clear();
        txtUsuarioGestor.clear();
        txtContrasenaGestor.clear();
        txtCedulaGestor.clear();
        txtTlf1Gestor.clear();
        txtTlf2Gestor.clear();
    }

    @FXML
    void nuevoGestor(ActionEvent event) {
        limpiarCamposGestor();
        gestorSeleccionado = null;
    }

    @FXML
    void registrarTrabajador(ActionEvent event) {

    }
    @FXML
    private TableView<Cargo> tableCargo;

    @FXML
    private TableColumn<Cargo, String> columnNombreCargo;

    @FXML
    private TableColumn<Cargo, Float> columnPrecioCargo;

    @FXML
    private TextField txtNombreCargo;

    @FXML
    private TextField txtPrecioCargo;

    @FXML
    void actualizarCargo(ActionEvent event) {
        if (cargoSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un cargo de la tabla para actualizar.");
            return;
        }

        // Validar que los campos no estén vacíos
        if (txtNombreCargo.getText().isEmpty() || txtPrecioCargo.getText().isEmpty()) {
            mostrarMensajeError("Debe completar todos los campos");
            return;
        }

        // Validar que el precio sea un número válido
        if (!isNumericFloat(txtPrecioCargo.getText())) {
            mostrarMensajeError("El precio debe ser un número válido");
            return;
        }

        // Obtener los valores de los campos
        String name = txtNombreCargo.getText().trim();
        float precio = Float.parseFloat(txtPrecioCargo.getText().trim());

        // Crear el cargo actualizado
        Cargo cargoActualizado = new Cargo(cargoSeleccionado.getIdCargo(), name, precio);

        // Actualizar el cargo
        boolean actualizado = CargoServicio.actualizarCargo(cargoActualizado);

        if (actualizado) {
            mostrarMensaje("Cargo actualizado", null, "El cargo se ha actualizado correctamente", Alert.AlertType.INFORMATION);

            // Limpiar los campos
            txtNombreCargo.clear();
            txtPrecioCargo.clear();

            // Actualizar la tabla de cargos
            if (tableCargo != null) {
                tableCargo.getItems().clear();
                tableCargo.setItems(getListaCargosData());
            }

            // Actualizar el combobox de cargos
            refreshCargos();
        } else {
            mostrarMensajeError("No se pudo actualizar el cargo");
        }
    }

    @FXML
    void agregarCargoAction(ActionEvent event) {
        // Validar que los campos no estén vacíos
        if (txtNombreCargo.getText().isEmpty() || txtPrecioCargo.getText().isEmpty()) {
            mostrarMensajeError("Debe completar todos los campos");
            return;
        }

        // Validar que el precio sea un número válido
        if (!isNumericFloat(txtPrecioCargo.getText())) {
            mostrarMensajeError("El precio debe ser un número válido");
            return;
        }

        // Obtener los valores de los campos
        String name = txtNombreCargo.getText().trim();
        float precio = Float.parseFloat(txtPrecioCargo.getText().trim());

        // Agregar el cargo
        Cargo nuevoCargo = CargoServicio.agregarCargo(name, precio);

        if (nuevoCargo != null) {
            mostrarMensaje("Cargo agregado", null, "El cargo se ha agregado correctamente", Alert.AlertType.INFORMATION);

            // Limpiar los campos
            txtNombreCargo.clear();
            txtPrecioCargo.clear();

            // Actualizar la tabla de cargos (si existe)
            if (tableCargo != null) {
                tableCargo.getItems().clear();
                tableCargo.setItems(getListaCargosData());
            }

            // Actualizar el combobox de cargos
            refreshCargos();
        } else {
            mostrarMensajeError("No se pudo agregar el cargo");
        }
    }

    /**
     * Verifica si una cadena es un número de punto flotante válido
     */
    private boolean isNumericFloat(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    void eliminarCargoAction(ActionEvent event) {
        if (cargoSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un cargo de la tabla para eliminar.");
            return;
        }

        boolean confirmacion = mostrarMensajeConfirmacion("¿Está seguro que desea eliminar el cargo seleccionado?");
        if (!confirmacion) {
            return;
        }

        boolean eliminado = CargoServicio.eliminarCargo(cargoSeleccionado.getIdCargo());
        if (eliminado) {
            mostrarMensaje("Cargo eliminado", null, "El cargo se ha eliminado correctamente", Alert.AlertType.INFORMATION);

            // Limpiar los campos
            txtNombreCargo.clear();
            txtPrecioCargo.clear();

            // Actualizar la tabla de cargos
            if (tableCargo != null) {
                tableCargo.getItems().clear();
                tableCargo.setItems(getListaCargosData());
            }

            // Actualizar el combobox de cargos
            refreshCargos();
        } else {
            mostrarMensajeError("No se pudo eliminar el cargo. Puede que esté siendo utilizado por algún trabajador.");
        }
    }

    @FXML
    void nuevoCargo(ActionEvent event) {
        // Abrir la vista de gestión de cargos
        if (aplicacion != null) {
            // Pasar este controlador para que se pueda actualizar el combobox cuando se cierre la ventana
            aplicacion.showCargoView(this);
        }
    }

    /**
     * Actualiza la lista de cargos en el combobox
     */
    public void refreshCargos() {
        // Check if comboBoxCargo is not null before using it
        if (comboBoxCargo != null) {
            List<Cargo> cargos = CargoServicio.obtenerCargos();
            comboBoxCargo.setItems(FXCollections.observableArrayList(cargos));
        }
    }


    //Implementacion de metodos auxiliares
    private void agregarTrabajador() {
        // Validar los datos antes de crear el trabajador
        String error = validarCamposTrabajador();
        if (!error.isEmpty()) {
            mostrarAlerta("Error en campos", error);  // Puedes personalizar esto con tu sistema de alertas
            return;
        }

        String nombre = txtNombreTrabajador.getText().trim();
        String usuario = txtUsuarioTrabajador.getText().trim();
        String contrasena = txtContrasenaTrabajador.getText().trim();
        int cedula = Integer.parseInt(txtCedulaTrabajador.getText().trim());

        // Validar que la cédula no exista ya en la lista de trabajadores
        List<Trabajador> trabajadores = TrabajadorServicio.obtenerTrabajadores();
        for (Trabajador t : trabajadores) {
            if (t.getCedula() == cedula) {
                mostrarMensajeError("El vendedor con cédula: " + cedula + " ya se encuentra registrado");
                return;
            }
        }

        // Use the selected cargo from comboBoxCargo
        Cargo cargo = null;
        if (comboBoxCargo != null) {
            cargo = comboBoxCargo.getValue();
        }
        if (cargo == null) {
            // If no cargo is selected, show an error message
            mostrarMensajeError("Debe seleccionar un cargo para el trabajador");
            return;
        }

        // Verificar si el cargo es el cargo por defecto (ID 1 y nombre "Default")
        if ("Default".equals(cargo.getName()) && cargo.getIdCargo() == 1) {
            mostrarMensajeError("No se puede crear un trabajador con el cargo por defecto. Por favor, seleccione un cargo válido.");
            return;
        }

        // Update precio_evento if txtPrecioEvento is not null
        if (txtPrecioEvento != null && !txtPrecioEvento.getText().trim().isEmpty()) {
            try {
                int precioEvento = Integer.parseInt(txtPrecioEvento.getText().trim());
                cargo = new Cargo(cargo.getIdCargo(), cargo.getName(), precioEvento);
            } catch (NumberFormatException e) {
                // Already validated in validarCamposTrabajador
            }
        }

        ArrayList<String> telefonos = new ArrayList<>();
        telefonos.add(txtTlf1Trabajador.getText().trim());
        telefonos.add(txtTlf2Trabajador.getText().trim());

        Trabajador trabajador = new Trabajador(cedula, nombre, usuario, contrasena, telefonos, cargo);

        TrabajadorServicio servicio = new TrabajadorServicio();
        Trabajador trabajadorAUx = servicio.crearTrabajador(trabajador);
        if (trabajadorAUx != null) {
            System.out.println("taxu"+trabajadorAUx);
            listaTrabajadoresData.add(trabajadorAUx);
            limpiarCamposTrabajador();
            mostrarMensaje("Notificacion Vendedor", null, "El trabajador se ha creado con exito",
                    Alert.AlertType.INFORMATION);

        } else {
            mostrarMensajeError("El vendedor: " + cedula + " ya se encuentra registrado");

        }
    }
    @FXML
    void actualizarTrabajador(ActionEvent event) {
        if (trabajadorSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un trabajador de la tabla para actualizar.");
            return;
        }

        String error = validarCamposTrabajador();
        if (!error.isEmpty()) {
            mostrarAlerta("Error en campos", error);
            return;
        }

        int cedula = Integer.parseInt(txtCedulaTrabajador.getText().trim());
        String nombre = txtNombreTrabajador.getText().trim();
        String usuario = txtUsuarioTrabajador.getText().trim();
        String contrasena = txtContrasenaTrabajador.getText().trim();

        // Use the selected cargo from comboBoxCargo
        Cargo cargo = null;
        if (comboBoxCargo != null) {
            cargo = comboBoxCargo.getValue();
        }
        if (cargo == null) {
            // If no cargo is selected, use the one from the selected worker
            cargo = trabajadorSeleccionado.getCargo();
            if (cargo == null) {
                // If still null, show an error message
                mostrarMensajeError("Debe seleccionar un cargo para el trabajador");
                return;
            }
        }

        // Verificar si el cargo es el cargo por defecto (ID 1 y nombre "Default")
        if ("Default".equals(cargo.getName()) && cargo.getIdCargo() == 1) {
            mostrarMensajeError("No se puede actualizar un trabajador con el cargo por defecto. Por favor, seleccione un cargo válido.");
            return;
        }

        // Update precio_evento if txtPrecioEvento is not null
        if (txtPrecioEvento != null && !txtPrecioEvento.getText().trim().isEmpty()) {
            try {
                int precioEvento = Integer.parseInt(txtPrecioEvento.getText().trim());
                cargo = new Cargo(cargo.getIdCargo(), cargo.getName(), precioEvento);
            } catch (NumberFormatException e) {
                // Already validated in validarCamposTrabajador
            }
        }

        ArrayList<String> telefonos = new ArrayList<>();
        telefonos.add(txtTlf1Trabajador.getText().trim());
        telefonos.add(txtTlf2Trabajador.getText().trim());

        Trabajador trabajadorActualizado = new Trabajador(cedula, nombre, usuario, contrasena, telefonos, cargo);

        TrabajadorServicio servicio = new TrabajadorServicio();
        boolean actualizado = servicio.actualizarTrabajador(trabajadorActualizado);

        if (actualizado) {
            int index = listaTrabajadoresData.indexOf(trabajadorSeleccionado);
            listaTrabajadoresData.set(index, trabajadorActualizado);
            tableTrabajador.refresh();
            limpiarCamposTrabajador();
            mostrarMensaje("Actualización", null, "Trabajador actualizado con éxito.", Alert.AlertType.INFORMATION);
            trabajadorSeleccionado = null;
        } else {
            mostrarMensajeError("No se pudo actualizar el trabajador.");
        }
    }
    @FXML
    void eliminarTrabajador(ActionEvent event) {
        trabajadorSeleccionado = tableTrabajador.getSelectionModel().getSelectedItem();

        if (trabajadorSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un trabajador de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de eliminar al trabajador con cédula " + trabajadorSeleccionado.getCedula() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            TrabajadorServicio servicio = new TrabajadorServicio();
            boolean eliminado = servicio.eliminarTrabajador(trabajadorSeleccionado.getCedula());

            if (eliminado) {
                listaTrabajadoresData.remove(trabajadorSeleccionado);
                tableTrabajador.refresh();
                limpiarCamposTrabajador();
                mostrarMensaje("Eliminación", null, "Trabajador eliminado con éxito.", Alert.AlertType.INFORMATION);
            } else {
                mostrarMensajeError("No se pudo eliminar el trabajador.");
            }
        }
    }



    private String validarCamposTrabajador() {
        StringBuilder errores = new StringBuilder();

        if (txtNombreTrabajador.getText().trim().isEmpty()) {
            errores.append("- El nombre no puede estar vacío.\n");
        }
        if (txtUsuarioTrabajador.getText().trim().isEmpty()) {
            errores.append("- El usuario no puede estar vacío.\n");
        }
        if (txtContrasenaTrabajador.getText().trim().isEmpty()) {
            errores.append("- La contraseña no puede estar vacía.\n");
        }
        if (txtCedulaTrabajador.getText().trim().isEmpty()) {
            errores.append("- La cédula no puede estar vacía.\n");
        } else {
            try {
                Integer.parseInt(txtCedulaTrabajador.getText().trim());
            } catch (NumberFormatException e) {
                errores.append("- La cédula debe ser numérica.\n");
            }
        }

        if (txtTlf1Trabajador.getText().trim().isEmpty() && txtTlf2Trabajador.getText().trim().isEmpty()) {
            errores.append("- Al menos un teléfono debe estar presente.\n");
        }

        // Check if txtPrecioEvento is not null before accessing it
        if (txtPrecioEvento != null) {
            if (txtPrecioEvento.getText().trim().isEmpty()) {
                errores.append("- El precio del evento no puede estar vacío.\n");
            } else {
                try {
                    Integer.parseInt(txtPrecioEvento.getText().trim());
                } catch (NumberFormatException e) {
                    errores.append("- El precio del evento debe ser numérico.\n");
                }
            }
        }

        return errores.toString();
    }
    private void limpiarCamposTrabajador() {
        txtNombreTrabajador.clear();
        txtUsuarioTrabajador.clear();
        txtContrasenaTrabajador.clear();
        txtCedulaTrabajador.clear(); // Fixed: was using txtCedulaGestor instead of txtCedulaTrabajador
        txtTlf1Trabajador.clear();
        txtTlf2Trabajador.clear();
        if (txtPrecioEvento != null) {
            txtPrecioEvento.clear();
        }
        if (comboBoxCargo != null) {
            comboBoxCargo.getSelectionModel().clearSelection();
        }
    }


    public static boolean isNumericDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNumericInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private boolean mostrarMensajeConfirmacion(String mensaje) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Informacion");
        alert.setContentText(mensaje);
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
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
    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //comboBoxCargo.setItems(FXCollections.observableArrayList(Cargo.values()));
        List<Cargo> cargos = CargoServicio.obtenerCargos();

        // Check if comboBoxCargo is not null before using it
        if (comboBoxCargo != null) {
            comboBoxCargo.setItems(FXCollections.observableArrayList(cargos));

            comboBoxCargo.setConverter(new StringConverter<>() {
                @Override
                public String toString(Cargo cargo) {
                    return cargo != null ? "Cargo " + cargo.getName() + " - $" + cargo.getPrecio_evento() : "";
                }

                @Override
                public Cargo fromString(String s) {
                    return comboBoxCargo.getItems().stream()
                            .filter(c -> s.contains(c.getName()))
                            .findFirst()
                            .orElse(null);
                }
            });
        }

        // Configure real-time clock for date and time labels
        if (lblFecha != null && lblHora != null) {
            // Create formatters for date and time
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            // Create a timeline that updates every second
            Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                LocalDate currentDate = LocalDate.now();
                LocalTime currentTime = LocalTime.now();

                lblFecha.setText("Fecha: " + currentDate.format(dateFormatter));
                lblHora.setText("Hora: " + currentTime.format(timeFormatter));
            }), new KeyFrame(Duration.seconds(1)));

            // Make the timeline run indefinitely
            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();
        }

        // Configuración de la tabla de trabajadores
        if (columnNombreTrabajador != null && columnCedulaTrabajador != null && 
            columnTelefonoPrincipal != null && columnUsuarioTrabajador != null) {

            columnNombreTrabajador.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("nombre"));
            columnCedulaTrabajador.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("cedula"));
            columnTelefonoPrincipal.setCellValueFactory(cellData -> {
                Trabajador trabajador = cellData.getValue();
                if (trabajador != null) {
                    List<String> telefonos = trabajador.getTelefono();
                    if (telefonos != null && !telefonos.isEmpty()) {
                        return new SimpleStringProperty(telefonos.get(0));
                    }
                }
                return new SimpleStringProperty("");
            });
            columnUsuarioTrabajador.setCellValueFactory(new PropertyValueFactory<>("usuario")); // si quieres el campo 'usuario'

            // Configuración de la columna de cargo
            if (columnCargo != null) {
                columnCargo.setCellValueFactory(cellData -> {
                    Trabajador trabajador = cellData.getValue();
                    if (trabajador != null && trabajador.getCargo() != null) {
                        // Si el cargo es "Default" y el ID es 1, podría ser un cargo por defecto no asignado explícitamente
                        if ("Default".equals(trabajador.getCargo().getName()) && trabajador.getCargo().getIdCargo() == 1) {
                            return new SimpleStringProperty("Sin cargo asignado");
                        }
                        return new SimpleStringProperty(trabajador.getCargo().getName());
                    }
                    return new SimpleStringProperty("");
                });
            }
        }

        // Cargar los datos de trabajadores si la tabla existe
        if (tableTrabajador != null) {
            getListaVendedorData();
            tableTrabajador.setItems(listaTrabajadoresData);

            tableTrabajador.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                trabajadorSeleccionado = newSelection;
                mostrarInformacionVendedor(trabajadorSeleccionado);
            });
        }

        // Configuración de la tabla de gestores
        if (columnNombreGestor != null && columnCedulaGestor != null && 
            columnTelefonoPrincipalGestor != null && columnUsuarioGestor != null) {

            columnNombreGestor.setCellValueFactory(new PropertyValueFactory<GestorEvento, String>("nombre"));
            columnCedulaGestor.setCellValueFactory(new PropertyValueFactory<GestorEvento, String>("cedula"));
            columnTelefonoPrincipalGestor.setCellValueFactory(cellData -> {
                GestorEvento gestor = cellData.getValue();
                if (gestor != null) {
                    List<String> telefonos = gestor.getTelefono();
                    if (telefonos != null && !telefonos.isEmpty()) {
                        return new SimpleStringProperty(telefonos.get(0));
                    }
                }
                return new SimpleStringProperty("");
            });
            columnUsuarioGestor.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        }

        // Cargar los datos de gestores si la tabla existe
        if (tableGestores != null) {
            getListaGestorData();
            tableGestores.setItems(listaGestoresData);

            tableGestores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                gestorSeleccionado = newSelection;
                mostrarInformacionGestor(gestorSeleccionado);
            });
        }

        // Configuración de la tabla de cargos
        if (columnNombreCargo != null && columnPrecioCargo != null) {
            columnNombreCargo.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnPrecioCargo.setCellValueFactory(new PropertyValueFactory<>("precio_evento"));
        }

        // Cargar los datos de cargos si la tabla existe
        if (tableCargo != null) {
            getListaCargosData();
            tableCargo.setItems(listaCargosData);

            tableCargo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                cargoSeleccionado = newSelection;
                mostrarInformacionCargo(cargoSeleccionado);
            });
        }
    }

    public ObservableList<Trabajador> getListaVendedorData() {
        listaTrabajadoresData.clear();
        listaTrabajadoresData.addAll(TrabajadorServicio.obtenerTrabajadores());

        return listaTrabajadoresData;
    }

    public ObservableList<GestorEvento> getListaGestorData() {
        listaGestoresData.clear();
        listaGestoresData.addAll(GestorServicio.obtenerGestores());

        return listaGestoresData;
    }

    /**
     * Obtiene la lista de cargos del servidor y la carga en la lista observable
     * @return La lista observable de cargos
     */
    public ObservableList<Cargo> getListaCargosData() {
        listaCargosData.clear();
        listaCargosData.addAll(CargoServicio.obtenerCargos());

        return listaCargosData;
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;

        // Check if tables are not null before using them
        if (tableTrabajador != null) {
            tableTrabajador.getItems().clear();
            tableTrabajador.setItems(getListaVendedorData());
        }

        if (tableGestores != null) {
            tableGestores.getItems().clear();
            tableGestores.setItems(getListaGestorData());
        }

        // Cargar los datos de cargos si la tabla existe
        if (tableCargo != null) {
            tableCargo.getItems().clear();
            tableCargo.setItems(getListaCargosData());
        }

        // Actualizar la lista de cargos en el combobox
        refreshCargos();
    }

    private void mostrarInformacionVendedor(Trabajador vendedorSeleccionado) {
        if (vendedorSeleccionado != null) {
            txtNombreTrabajador.setText(vendedorSeleccionado.getNombre());
            if (comboBoxCargo != null) {
                Cargo cargo = vendedorSeleccionado.getCargo();
                // Si el cargo es "Default" y el ID es 1, podría ser un cargo por defecto no asignado explícitamente
                if (cargo != null && !("Default".equals(cargo.getName()) && cargo.getIdCargo() == 1)) {
                    comboBoxCargo.setValue(cargo);
                } else {
                    comboBoxCargo.getSelectionModel().clearSelection();
                }
            }
            txtUsuarioTrabajador.setText(vendedorSeleccionado.getUsuario());
            txtContrasenaTrabajador.setText(vendedorSeleccionado.getContrasena());
            txtCedulaTrabajador.setText(String.valueOf(vendedorSeleccionado.getCedula()));

            // Set precio evento if the field exists and the cargo has a precio_evento
            if (txtPrecioEvento != null && vendedorSeleccionado.getCargo() != null) {
                Cargo cargo = vendedorSeleccionado.getCargo();
                // Si el cargo es "Default" y el ID es 1, no mostrar el precio
                if (!("Default".equals(cargo.getName()) && cargo.getIdCargo() == 1)) {
                    txtPrecioEvento.setText(String.valueOf(cargo.getPrecio_evento()));
                } else {
                    txtPrecioEvento.clear();
                }
            }

            List<String> telefonos = vendedorSeleccionado.getTelefono();
            if (telefonos != null && !telefonos.isEmpty()) {
                txtTlf1Trabajador.setText(telefonos.get(0));
                if (telefonos.size() > 1) {
                    txtTlf2Trabajador.setText(telefonos.get(1));
                } else {
                    txtTlf2Trabajador.setText("");
                }
            } else {
                txtTlf1Trabajador.setText("");
                txtTlf2Trabajador.setText("");
            }
        }
    }

    private void mostrarInformacionGestor(GestorEvento gestorSeleccionado) {
        if (gestorSeleccionado != null) {
            txtNombreGestor.setText(gestorSeleccionado.getNombre());
            txtUsuarioGestor.setText(gestorSeleccionado.getUsuario());
            txtContrasenaGestor.setText(gestorSeleccionado.getContrasena());
            txtCedulaGestor.setText(String.valueOf(gestorSeleccionado.getCedula()));

            List<String> telefonos = gestorSeleccionado.getTelefono();
            if (telefonos != null && !telefonos.isEmpty()) {
                txtTlf1Gestor.setText(telefonos.get(0));
                if (telefonos.size() > 1) {
                    txtTlf2Gestor.setText(telefonos.get(1));
                } else {
                    txtTlf2Gestor.setText("");
                }
            } else {
                txtTlf1Gestor.setText("");
                txtTlf2Gestor.setText("");
            }
        }
    }

    /**
     * Muestra la información del cargo seleccionado en los campos del formulario
     * @param cargoSeleccionado El cargo seleccionado en la tabla
     */
    private void mostrarInformacionCargo(Cargo cargoSeleccionado) {
        if (cargoSeleccionado != null) {
            txtNombreCargo.setText(cargoSeleccionado.getName());
            txtPrecioCargo.setText(String.valueOf(cargoSeleccionado.getPrecio_evento()));
        }
    }
}
