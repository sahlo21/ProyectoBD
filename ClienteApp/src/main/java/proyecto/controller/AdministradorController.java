package proyecto.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import proyecto.Aplicacion;
import proyecto.modelo.*;
import proyecto.servicio.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
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

    // Columnas para numeración de filas
    private TableColumn<Trabajador, Number> columnNumeroTrabajador;
    private TableColumn<GestorEvento, Number> columnNumeroGestor;
    private TableColumn<Cargo, Number> columnNumeroCargo;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblHora;
    @FXML private ScrollPane scrollPdf;
    @FXML private VBox vboxPdf;

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
                actualizarNumerosDeFilas(tableGestores);
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
            actualizarNumerosDeFilas(tableGestores);
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
            actualizarNumerosDeFilas(tableGestores);
            limpiarCamposGestor();
            mostrarMensaje("Notificación Gestor", null, "El gestor se ha creado con éxito",
                    Alert.AlertType.INFORMATION);
        } else {
            // Check if the username already exists
            boolean usuarioDuplicado = false;

            // Check in gestores
            for (GestorEvento g : gestores) {
                if (usuario.equalsIgnoreCase(g.getUsuario())) {
                    usuarioDuplicado = true;
                    break;
                }
            }

            // If not found in gestores, check in trabajadores
            if (!usuarioDuplicado) {
                List<Trabajador> trabajadores = TrabajadorServicio.obtenerTrabajadores();
                for (Trabajador t : trabajadores) {
                    if (usuario.equalsIgnoreCase(t.getUsuario())) {
                        usuarioDuplicado = true;
                        break;
                    }
                }
            }

            if (usuarioDuplicado) {
                mostrarMensajeError("El nombre de usuario '" + usuario + "' ya está en uso. Por favor, elija otro nombre de usuario.");
            } else {
                mostrarMensajeError("El gestor con cédula: " + cedula + " ya se encuentra registrado");
            }
        }
    }

    private String validarCamposGestor() {
        StringBuilder errores = new StringBuilder();

        if (txtNombreGestor.getText().trim().isEmpty()) {
            errores.append("- El nombre no puede estar vacío\n");
        }

        // Validación de usuario
        String usuario = txtUsuarioGestor.getText().trim();
        if (usuario.isEmpty()) {
            errores.append("- El usuario no puede estar vacío\n");
        } else {
            boolean usuarioDuplicado = false;

            // Verificar si el usuario ya existe en otro gestor
            List<GestorEvento> gestores = GestorServicio.obtenerGestores();
            for (GestorEvento g : gestores) {
                // Si estamos actualizando, ignoramos el gestor seleccionado
                if (gestorSeleccionado != null && g.getCedula() == gestorSeleccionado.getCedula()) {
                    continue;
                }
                if (usuario.equalsIgnoreCase(g.getUsuario())) {
                    errores.append("- El nombre de usuario ya está en uso por otro gestor\n");
                    usuarioDuplicado = true;
                    break;
                }
            }

            // Si no se encontró duplicado en gestores, verificar en trabajadores
            if (!usuarioDuplicado) {
                List<Trabajador> trabajadores = TrabajadorServicio.obtenerTrabajadores();
                for (Trabajador t : trabajadores) {
                    if (usuario.equalsIgnoreCase(t.getUsuario())) {
                        errores.append("- El nombre de usuario ya está en uso por un trabajador\n");
                        break;
                    }
                }
            }
        }

        if (txtContrasenaGestor.getText().trim().isEmpty()) {
            errores.append("- La contraseña no puede estar vacía\n");
        }

        // Validación de cédula
        String cedulaStr = txtCedulaGestor.getText().trim();
        if (cedulaStr.isEmpty()) {
            errores.append("- La cédula no puede estar vacía\n");
        } else if (!isNumericInt(cedulaStr)) {
            errores.append("- La cédula debe ser un número entero\n");
        } else {
            // Verificar si la cédula ya existe en otro gestor (solo al crear nuevo)
            if (gestorSeleccionado == null) {
                int cedula = Integer.parseInt(cedulaStr);
                List<GestorEvento> gestores = GestorServicio.obtenerGestores();
                for (GestorEvento g : gestores) {
                    if (g.getCedula() == cedula) {
                        errores.append("- La cédula ya está registrada para otro gestor\n");
                        break;
                    }
                }
            }
        }

        if (txtTlf1Gestor.getText().trim().isEmpty()) {
            errores.append("- El teléfono 1 no puede estar vacío\n");
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

        // Validar los campos
        String errores = validarCamposCargo();
        if (!errores.isEmpty()) {
            mostrarMensajeError(errores);
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
                actualizarNumerosDeFilas(tableCargo);
            }

            // Actualizar el combobox de cargos
            refreshCargos();
        } else {
            mostrarMensajeError("No se pudo actualizar el cargo");
        }
    }

    /**
     * Valida los campos del formulario de cargo
     *
     * @return String con los errores encontrados, o cadena vacía si no hay errores
     */
    private String validarCamposCargo() {
        StringBuilder errores = new StringBuilder();

        // Validar que los campos no estén vacíos
        if (txtNombreCargo.getText().trim().isEmpty()) {
            errores.append("- El nombre del cargo no puede estar vacío\n");
        } else {
            // Verificar si el nombre ya existe en otro cargo
            String nombreCargo = txtNombreCargo.getText().trim();
            List<Cargo> cargos = CargoServicio.obtenerCargos();
            for (Cargo c : cargos) {
                // Si estamos actualizando, ignoramos el cargo seleccionado
                if (cargoSeleccionado != null && c.getIdCargo() == cargoSeleccionado.getIdCargo()) {
                    continue;
                }
                if (nombreCargo.equalsIgnoreCase(c.getName())) {
                    errores.append("- El nombre del cargo ya está en uso\n");
                    break;
                }
            }
        }

        if (txtPrecioCargo.getText().trim().isEmpty()) {
            errores.append("- El precio no puede estar vacío\n");
        } else if (!isNumericFloat(txtPrecioCargo.getText().trim())) {
            errores.append("- El precio debe ser un número válido\n");
        }

        return errores.toString();
    }

    @FXML
    void agregarCargoAction(ActionEvent event) {
        // Validar los campos
        String errores = validarCamposCargo();
        if (!errores.isEmpty()) {
            mostrarMensajeError(errores);
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
                actualizarNumerosDeFilas(tableCargo);
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
                actualizarNumerosDeFilas(tableCargo);
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
            System.out.println("taxu" + trabajadorAUx);
            listaTrabajadoresData.add(trabajadorAUx);
            actualizarNumerosDeFilas(tableTrabajador);
            limpiarCamposTrabajador();
            mostrarMensaje("Notificacion Vendedor", null, "El trabajador se ha creado con exito",
                    Alert.AlertType.INFORMATION);

        } else {
            // Check if the username already exists
            boolean usuarioDuplicado = false;
            // Reuse the trabajadores list that was already defined above
            for (Trabajador t : trabajadores) {
                if (usuario.equalsIgnoreCase(t.getUsuario())) {
                    usuarioDuplicado = true;
                    break;
                }
            }

            if (!usuarioDuplicado) {
                List<GestorEvento> gestores = GestorServicio.obtenerGestores();
                for (GestorEvento g : gestores) {
                    if (usuario.equalsIgnoreCase(g.getUsuario())) {
                        usuarioDuplicado = true;
                        break;
                    }
                }
            }

            if (usuarioDuplicado) {
                mostrarMensajeError("El nombre de usuario '" + usuario + "' ya está en uso. Por favor, elija otro nombre de usuario.");
            } else {
                mostrarMensajeError("El vendedor con cédula: " + cedula + " ya se encuentra registrado");
            }
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
            actualizarNumerosDeFilas(tableTrabajador);
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
                actualizarNumerosDeFilas(tableTrabajador);
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

        // Validación de usuario
        String usuario = txtUsuarioTrabajador.getText().trim();
        if (usuario.isEmpty()) {
            errores.append("- El usuario no puede estar vacío.\n");
        } else {
            boolean usuarioDuplicado = false;

            // Verificar si el usuario ya existe en otro trabajador
            List<Trabajador> trabajadores = TrabajadorServicio.obtenerTrabajadores();
            for (Trabajador t : trabajadores) {
                // Si estamos actualizando, ignoramos el trabajador seleccionado
                if (trabajadorSeleccionado != null && t.getCedula() == trabajadorSeleccionado.getCedula()) {
                    continue;
                }
                if (usuario.equalsIgnoreCase(t.getUsuario())) {
                    errores.append("- El nombre de usuario ya está en uso por otro trabajador.\n");
                    usuarioDuplicado = true;
                    break;
                }
            }

            // Si no se encontró duplicado en trabajadores, verificar en gestores
            if (!usuarioDuplicado) {
                List<GestorEvento> gestores = GestorServicio.obtenerGestores();
                for (GestorEvento g : gestores) {
                    if (usuario.equalsIgnoreCase(g.getUsuario())) {
                        errores.append("- El nombre de usuario ya está en uso por un gestor de eventos.\n");
                        break;
                    }
                }
            }
        }

        if (txtContrasenaTrabajador.getText().trim().isEmpty()) {
            errores.append("- La contraseña no puede estar vacía.\n");
        }

        // Validación de cédula
        String cedulaStr = txtCedulaTrabajador.getText().trim();
        if (cedulaStr.isEmpty()) {
            errores.append("- La cédula no puede estar vacía.\n");
        } else {
            try {
                int cedula = Integer.parseInt(cedulaStr);
                // Verificar si la cédula ya existe en otro trabajador (solo al crear nuevo)
                if (trabajadorSeleccionado == null) {
                    List<Trabajador> trabajadores = TrabajadorServicio.obtenerTrabajadores();
                    for (Trabajador t : trabajadores) {
                        if (t.getCedula() == cedula) {
                            errores.append("- La cédula ya está registrada para otro trabajador.\n");
                            break;
                        }
                    }
                }
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
                    return cargo != null ? "Cargo " + cargo.getName() + " - $" + cargo.getPrecio() : "";
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

            // Crear y configurar la columna de numeración
            columnNumeroTrabajador = new TableColumn<>("No.");
            columnNumeroTrabajador.setPrefWidth(50);
            columnNumeroTrabajador.setSortable(false);
            columnNumeroTrabajador.setCellValueFactory(column -> new javafx.beans.property.ReadOnlyObjectWrapper<Number>(
                    tableTrabajador.getItems().indexOf(column.getValue()) + 1));

            // Agregar la columna de numeración como primera columna si no existe ya
            if (tableTrabajador.getColumns().isEmpty() ||
                    !tableTrabajador.getColumns().get(0).getText().equals("No.")) {
                tableTrabajador.getColumns().add(0, columnNumeroTrabajador);
            }

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

            // Crear y configurar la columna de numeración
            columnNumeroGestor = new TableColumn<>("No.");
            columnNumeroGestor.setPrefWidth(50);
            columnNumeroGestor.setSortable(false);
            columnNumeroGestor.setCellValueFactory(column -> new javafx.beans.property.ReadOnlyObjectWrapper<Number>(
                    tableGestores.getItems().indexOf(column.getValue()) + 1));

            // Agregar la columna de numeración como primera columna si no existe ya
            if (tableGestores.getColumns().isEmpty() ||
                    !tableGestores.getColumns().get(0).getText().equals("No.")) {
                tableGestores.getColumns().add(0, columnNumeroGestor);
            }

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
            // Crear y configurar la columna de numeración
            columnNumeroCargo = new TableColumn<>("No.");
            columnNumeroCargo.setPrefWidth(50);
            columnNumeroCargo.setSortable(false);
            columnNumeroCargo.setCellValueFactory(column -> new javafx.beans.property.ReadOnlyObjectWrapper<Number>(
                    tableCargo.getItems().indexOf(column.getValue()) + 1));

            // Agregar la columna de numeración como primera columna si no existe ya
            if (tableCargo.getColumns().isEmpty() ||
                    !tableCargo.getColumns().get(0).getText().equals("No.")) {
                tableCargo.getColumns().add(0, columnNumeroCargo);
            }

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

    /**
     * Actualiza los números de fila en una tabla
     *
     * @param tableView La tabla a actualizar
     */
    private void actualizarNumerosDeFilas(TableView<?> tableView) {
        if (tableView != null) {
            tableView.refresh();
        }
    }

    public ObservableList<Trabajador> getListaVendedorData() {
        listaTrabajadoresData.clear();
        listaTrabajadoresData.addAll(TrabajadorServicio.obtenerTrabajadores());

        if (tableTrabajador != null) {
            actualizarNumerosDeFilas(tableTrabajador);
        }

        return listaTrabajadoresData;
    }

    public ObservableList<GestorEvento> getListaGestorData() {
        listaGestoresData.clear();
        listaGestoresData.addAll(GestorServicio.obtenerGestores());

        if (tableGestores != null) {
            actualizarNumerosDeFilas(tableGestores);
        }

        return listaGestoresData;
    }

    /**
     * Obtiene la lista de cargos del servidor y la carga en la lista observable
     *
     * @return La lista observable de cargos
     */
    public ObservableList<Cargo> getListaCargosData() {
        listaCargosData.clear();
        listaCargosData.addAll(CargoServicio.obtenerCargos());

        if (tableCargo != null) {
            actualizarNumerosDeFilas(tableCargo);
        }

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
                    txtPrecioEvento.setText(String.valueOf(cargo.getPrecio()));
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
     *
     * @param cargoSeleccionado El cargo seleccionado en la tabla
     */
    private void mostrarInformacionCargo(Cargo cargoSeleccionado) {
        if (cargoSeleccionado != null) {
            txtNombreCargo.setText(cargoSeleccionado.getName());
            txtPrecioCargo.setText(String.valueOf(cargoSeleccionado.getPrecio()));
        }
    }

    //Reportes

    @FXML
    void reporte1(ActionEvent event) {
        List<Map<String,Object>> datos = ReporteServicio.generarReporteAlquileresMensuales();
        if (datos.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No hay datos para el reporte.").showAndWait();
            return;
        }

        String ruta = "reporte_alquileres_mensuales.pdf";
        try {
            PDFUtil.generarReporteAlquileresMensualesPDF(datos, ruta);
            renderizarPDFaVBox(ruta);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Error generando el PDF:\n" + e.getMessage()).showAndWait();
        }
    }
    private void renderizarPDFaVBox(String rutaPdf) throws Exception {
        vboxPdf.getChildren().clear();
        try (PDDocument doc = PDDocument.load(new File(rutaPdf))) {
            PDFRenderer renderer = new PDFRenderer(doc);
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                // Aumenta el DPI para que tenga mejor resolución
                BufferedImage bim = renderer.renderImageWithDPI(i, 175); // Puedes probar 175 o 200

                Image fxImage = SwingFXUtils.toFXImage(bim, null);
                ImageView iv = new ImageView(fxImage);

                iv.setPreserveRatio(true);

                // Ajusta el ancho y alto (zoom visual)
                double zoom = 1.25; // 1.0 = 100%, 1.25 = 125%
                iv.setFitWidth(scrollPdf.getWidth() * zoom); // más ancho
                // opcionalmente puedes fijar un alto máximo:
                // iv.setFitHeight(scrollPdf.getHeight() * zoom);

                vboxPdf.getChildren().add(iv);
            }
        }
    }
    @FXML
    void reporte2(ActionEvent event) {
        List<Map<String,Object>> datos = ReporteServicio.generarReporteCostosEventos();
        if (datos.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No hay datos para el reporte.").showAndWait();
            return;
        }

        String ruta = "reporte_costos_eventos.pdf";
        try {
            PDFUtil.generarReporteEventosCostosPDF(datos, ruta);
            renderizarPDFaVBox(ruta);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Error generando el PDF:\n" + e.getMessage()).showAndWait();
        }
    }
    @FXML
    void reporte3(ActionEvent event) {
        List<Map<String,Object>> datos = ReporteServicio.generarReporteInventario();
        if (datos.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No hay datos para el reporte.").showAndWait();
            return;
        }

        String ruta = "reporte_inventario.pdf";
        try {
            PDFUtil.generarReporteInventarioPDF(datos, ruta);
            renderizarPDFaVBox(ruta);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Error generando el PDF:\n" + e.getMessage()).showAndWait();
        }
    }

    @FXML
    void reporte4(ActionEvent event) {
        List<Map<String,Object>> datos = ReporteServicio.generarReporteAuditoria();
        if (datos.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No hay datos para el reporte.").showAndWait();
            return;
        }

        String ruta = "reporte_auditoria.pdf";
        try {
            PDFUtil.generarReporteAuditoriaPDF(datos, ruta);
            renderizarPDFaVBox(ruta);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Error generando el PDF:\n" + e.getMessage()).showAndWait();
        }
    }



}