package proyecto.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.util.StringConverter;
import proyecto.Aplicacion;
import proyecto.modelo.Cargo;
import proyecto.modelo.GestorEvento;
import proyecto.modelo.Producto;
import proyecto.modelo.Trabajador;
import proyecto.servicio.CargoServicio;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministradorController implements Initializable {

    ObservableList<Trabajador> listaTrabajadoresData = FXCollections.observableArrayList();
    ObservableList<Producto> listaProductosData = FXCollections.observableArrayList();
    Trabajador trabajadorSeleccionado;
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
    private TableColumn<Trabajador, Cargo> columnCargo;

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
    void actualizarGestor(ActionEvent event) {

    }


    @FXML
    void agregarGestorAction(ActionEvent event) {

    }

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

    }

    @FXML
    void eliminarGestor(ActionEvent event) {

    }



    @FXML
    void registrarGestor(ActionEvent event) {

    }

    @FXML
    void registrarTrabajador(ActionEvent event) {

    }
    @FXML
    private TableView<?> tableCargo;

    @FXML
    private TextField txtNombreCargo;

    @FXML
    private TextField txtPrecioCargo;

    @FXML
    void actualizarCargo(ActionEvent event) {

    }

    @FXML
    void agregarCargoAction(ActionEvent event) {

    }

    @FXML
    void eliminarCargoAction(ActionEvent event) {

    }

    @FXML
    void nuevoCargo(ActionEvent event) {

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
        Cargo cargo = new Cargo(1, 1, 123123);
        int precioEvento = Integer.parseInt(txtPrecioEvento.getText().trim());  // Si tienes un campo para eso

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
        Cargo cargo = new Cargo(1, 1, 123123); // Puedes ajustar esto si usas comboBoxCargo

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

    /**
     * Métodos de Producto listos para implementar (a la espera de interfaz)
     *
     *  private void agregarProducto() {
     *     // Validar los datos antes de crear el producto
     *     String error = validarCamposProducto();
     *     if (!error.isEmpty()) {
     *         mostrarAlerta("Error en campos", error);
     *         return;
     *     }
     *
     *     int id = Integer.parseInt(txtIdProducto.getText().trim());
     *     String nombre = txtNombreProducto.getText().trim();
     *     String descripcion = txtDescripcionProducto.getText().trim();
     *     double precio = Double.parseDouble(txtPrecioProducto.getText().trim());
     *     int cantidad = Integer.parseInt(txtCantidadProducto.getText().trim());
     *
     *     Producto producto = new Producto(id, nombre, descripcion, precio, cantidad);
     *
     *     ProductoServicio servicio = new ProductoServicio();
     *     Producto productoAux = servicio.crearProducto(producto);
     *
     *     if (productoAux != null) {
     *         listaProductosData.add(productoAux);
     *         limpiarCamposProducto();
     *         mostrarMensaje("Notificación Producto", null, "El producto se ha creado con éxito",
     *                 Alert.AlertType.INFORMATION);
     *     } else {
     *         mostrarMensajeError("El producto con ID: " + id + " ya se encuentra registrado.");
     *     }
     * }
     *
     * private String validarCamposProducto() {
     *     StringBuilder errores = new StringBuilder();
     *
     *     if (txtIdProducto.getText().trim().isEmpty()) {
     *         errores.append("- El ID no puede estar vacío.\n");
     *     } else {
     *         try {
     *             Integer.parseInt(txtIdProducto.getText().trim());
     *         } catch (NumberFormatException e) {
     *             errores.append("- El ID debe ser numérico.\n");
     *         }
     *     }
     *
     *     if (txtNombreProducto.getText().trim().isEmpty()) {
     *         errores.append("- El nombre no puede estar vacío.\n");
     *     }
     *
     *     if (txtDescripcionProducto.getText().trim().isEmpty()) {
     *         errores.append("- La descripción no puede estar vacía.\n");
     *     }
     *
     *     if (txtPrecioProducto.getText().trim().isEmpty()) {
     *         errores.append("- El precio no puede estar vacío.\n");
     *     } else {
     *         try {
     *             Double.parseDouble(txtPrecioProducto.getText().trim());
     *         } catch (NumberFormatException e) {
     *             errores.append("- El precio debe ser numérico.\n");
     *         }
     *     }
     *
     *     if (txtCantidadProducto.getText().trim().isEmpty()) {
     *         errores.append("- La cantidad no puede estar vacía.\n");
     *     } else {
     *         try {
     *             Integer.parseInt(txtCantidadProducto.getText().trim());
     *         } catch (NumberFormatException e) {
     *             errores.append("- La cantidad debe ser numérica.\n");
     *         }
     *     }
     *
     *     return errores.toString();
     * }
     *
     *private void limpiarCamposProducto() {
     *     txtIdProducto.clear();
     *     txtNombreProducto.clear();
     *     txtDescripcionProducto.clear();
     *     txtPrecioProducto.clear();
     *     txtCantidadProducto.clear();
     * }
     *
     *private void obtenerProductos() {
     *     ProductoServicio servicio = new ProductoServicio();
     *     List<Producto> productos = servicio.obtenerProductos();
     *
     *     if (productos != null) {
     *         listaProductosData.clear();
     *         listaProductosData.addAll(productos);
     *     } else {
     *         mostrarMensajeError("Error al obtener la lista de productos");
     *     }
     * }
     *
     * private void actualizarProducto() {
     *     String error = validarCamposProducto();
     *     if (!error.isEmpty()) {
     *         mostrarAlerta("Error en campos", error);
     *         return;
     *     }
     *
     *     int id = Integer.parseInt(txtIdProducto.getText().trim());
     *     String nombre = txtNombreProducto.getText().trim();
     *     String descripcion = txtDescripcionProducto.getText().trim();
     *     double precio = Double.parseDouble(txtPrecioProducto.getText().trim());
     *     int cantidad = Integer.parseInt(txtCantidadProducto.getText().trim());
     *
     *     Producto producto = new Producto(id, nombre, descripcion, precio, cantidad);
     *
     *     ProductoServicio servicio = new ProductoServicio();
     *     boolean exito = servicio.actualizarProducto(producto);
     *
     *     if (exito) {
     *         mostrarMensaje("Notificación", null, "Producto actualizado con éxito", Alert.AlertType.INFORMATION);
     *         obtenerProductos();
     *         limpiarCamposProducto();
     *     } else {
     *         mostrarMensajeError("No se pudo actualizar el producto con ID: " + id);
     *     }
     * }
     *
     * private void eliminarProducto() {
     *     Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
     *
     *     if (productoSeleccionado == null) {
     *         mostrarMensajeError("Debe seleccionar un producto de la tabla para eliminar.");
     *         return;
     *     }
     *
     *     int id = productoSeleccionado.getId();
     *
     *     ProductoServicio servicio = new ProductoServicio();
     *     boolean exito = servicio.eliminarProducto(id);
     *
     *     if (exito) {
     *         mostrarMensaje("Notificación", null, "Producto eliminado con éxito", Alert.AlertType.INFORMATION);
     *         obtenerProductos();
     *         limpiarCamposProducto();
     *     } else {
     *         mostrarMensajeError("No se pudo eliminar el producto con ID: " + id);
     *     }
     * }
     *
     */


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

        if (txtPrecioEvento.getText().trim().isEmpty()) {
            errores.append("- El precio del evento no puede estar vacío.\n");
        } else {
            try {
                Integer.parseInt(txtPrecioEvento.getText().trim());
            } catch (NumberFormatException e) {
                errores.append("- El precio del evento debe ser numérico.\n");
            }
        }

        return errores.toString();
    }
    private void limpiarCamposTrabajador() {
        txtNombreTrabajador.clear();
        txtUsuarioTrabajador.clear();
        txtContrasenaTrabajador.clear();
        txtCedulaGestor.clear();
        txtTlf1Trabajador.clear();
        txtTlf2Trabajador.clear();
        txtPrecioEvento.clear();
        comboBoxCargo.getSelectionModel().clearSelection();
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
        comboBoxCargo.setItems(FXCollections.observableArrayList(cargos));

        comboBoxCargo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Cargo cargo) {
                return cargo != null ? "Cargo " + cargo.getName() + " - $" + cargo.getPrecio_evento() : "";
            }

            @Override
            public Cargo fromString(String s) {
                return comboBoxCargo.getItems().stream()
                        .filter(c -> s.contains(String.valueOf(c.getName())))
                        .findFirst()
                        .orElse(null);
            }
        });
        lblFecha.setText(lblFecha.getText() + LocalDate.now(Clock.systemDefaultZone()));
        lblHora.setText(lblHora.getText() + LocalTime.now());

        columnNombreTrabajador.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("nombre"));
        columnCedulaTrabajador.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("cedula"));
        columnTelefonoPrincipal.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTelefono().isEmpty() ? "" : cellData.getValue().getTelefono().get(0))
        );
        columnUsuarioTrabajador.setCellValueFactory(new PropertyValueFactory<>("usuario")); // si quieres el campo 'usuario'

        tableTrabajador.setItems(listaTrabajadoresData); // AÑADE ESTO en initialize()

        tableTrabajador.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            trabajadorSeleccionado = newSelection;

            mostrarInformacionVendedor(trabajadorSeleccionado);

        });
    }

    public ObservableList<Trabajador> getListaVendedorData() {
        listaTrabajadoresData.clear();
        listaTrabajadoresData.addAll(TrabajadorServicio.obtenerTrabajadores());

        return listaTrabajadoresData;
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
        tableTrabajador.getItems().clear();
        tableTrabajador.setItems(getListaVendedorData());
    }

    private void mostrarInformacionVendedor(Trabajador vendedorSeleccionado) {

        if (vendedorSeleccionado != null) {
            txtNombreTrabajador.setText(vendedorSeleccionado.getNombre());
            comboBoxCargo.setValue(vendedorSeleccionado.getCargo());
            txtUsuarioTrabajador.setText(vendedorSeleccionado.getUsuario());
            txtContrasenaTrabajador.setText(vendedorSeleccionado.getContrasena());
            txtCedulaTrabajador.setText(String.valueOf(vendedorSeleccionado.getCedula()));
            txtTlf1Trabajador.setText(vendedorSeleccionado.getTelefono().get(0));
            txtTlf2Trabajador.setText(vendedorSeleccionado.getTelefono().get(1));


        }

    }
}