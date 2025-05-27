package proyecto.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import proyecto.Aplicacion;
import proyecto.modelo.*;
import proyecto.servicio.*;

import java.net.URL;
import java.sql.Date;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class GestionadorController implements Initializable {
    private Aplicacion aplicacion;
    ObservableList<Producto> listaProductosData = FXCollections.observableArrayList();
    ObservableList<Proveedor> listaProveedoresData = FXCollections.observableArrayList();
    ObservableList<Evento> listaEventosData = FXCollections.observableArrayList();
    ObservableList<Trabajador> listaTrabajadorData = FXCollections.observableArrayList();
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCerrarSesion2;

    @FXML
    private TableColumn<Producto, String> columnAlquilerProducto;

    @FXML
    private TableColumn<Producto, String> columnCantidadProducto;

    @FXML
    private TableColumn<Proveedor, String> columnCedulaProveedor;

    @FXML
    private TableColumn<Producto, String> columnDescripcionProducto;

    @FXML
    private TableColumn<Producto, String> columnIdProducto;

    @FXML
    private TableColumn<Producto, String> columnNombreProducto;

    @FXML
    private TableColumn<Proveedor, String> columnNombreProveedor;

    @FXML
    private TableColumn<Producto, String> columnPrecioProducto;

    @FXML
    private TableColumn<Proveedor, String> columnTelefonoProveedor;

    @FXML
    private TableColumn<Producto, String> columnCantidadElemento;

    @FXML
    private TableColumn<Producto, String> columnNombreElemento;

    @FXML
    private TableColumn<Producto, String> columnPrecioElemento;
    @FXML
    private TableColumn<Trabajador, String> columnPrecioTrabajador;
    @FXML
    private TableColumn<Trabajador, String> columnCargoTrabajador;
    @FXML
    private TableColumn<Trabajador, String> columnNombreTrabajador;
    @FXML
    private ComboBox<Producto> comboBoxElementos;

    @FXML
    private DatePicker dpFechaEvento;

    @FXML
    private ComboBox<Trabajador> comboBoxTrabajadores;

    @FXML
    private Label lblFecha2;

    @FXML
    private Label lblHora2;

    @FXML
    private Label lblUserAdmin;

    @FXML
    private TableView<Producto> tableElementos;

    @FXML
    private TableView<Trabajador> tableTrabajadores;

    @FXML
    private TableView<Producto> tableProdcutos;

    @FXML
    private TableView<Proveedor> tableProveedor;

    @FXML
    private TextField txtCantidadProducto;

    @FXML
    private TextField txtCedulaProveedor;

    @FXML
    private TextField txtDescripProducto;

    @FXML
    private TextField txtIdProducto;

    @FXML
    private TextField txtNombreProducto;

    @FXML
    private TextField txtNombreProveedor;

    @FXML
    private TextField txtPrecioAlquilerProducto;

    @FXML
    private TextField txtPrecioProducto;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtCedulaCliente;

    @FXML
    private TextField txtIdEvento;

    @FXML
    private TextField txtLugar;

    @FXML
    private TextField txtNombreEvento;

    @FXML
    void actualizarPrducto(ActionEvent event) {
        actualizarProducto();
    }

    @FXML
    void actualizarProveedor(ActionEvent event) {
        actualizarProveedor();
    }

    @FXML
    void agregarProducto(ActionEvent event) {
        agregarProducto();
    }

    @FXML
    void agregarProveedor(ActionEvent event) {
        agregarProveedor();
    }

    @FXML
    void cerrarSesionAction2(ActionEvent event) {
        // Cerrar sesión y registrar el logout
        LoginServicio loginServicio = new LoginServicio();
        if (LoginServicio.getUsuarioActual() != null) {
            loginServicio.cerrarSesion();
        }
        aplicacion.showLogin();
    }

    @FXML
    void eliminarProducto(ActionEvent event) {
        eliminarProducto();
    }

    @FXML
    void eliminarTrabajador(ActionEvent event) {

    }

    @FXML
    void nuevoProducto(ActionEvent event) {
        limpiarCamposProducto();
    }

    @FXML
    void nuevoProveedor(ActionEvent event) {
        limpiarCamposProveedor();
    }

    @FXML
    void agregarElementos(ActionEvent event) {
        Producto seleccionado = comboBoxElementos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensajeError("Debe seleccionar un elemento.");
            return;
        }
        if (!listaProductosData.contains(seleccionado)) {
            listaProductosData.add(seleccionado);
            tableElementos.setItems(listaProductosData);
        } else {
            mostrarMensajeError("El elemento ya fue agregado.");
        }
    }
    @FXML
    void agregarTrabajadores(ActionEvent event) {
        Trabajador seleccionado = comboBoxTrabajadores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensajeError("Debe seleccionar un trabajador.");
            return;
        }
        if (!listaTrabajadorData.contains(seleccionado)) {
            listaTrabajadorData.add(seleccionado);
            tableTrabajadores.setItems(listaTrabajadorData);
        } else {
            mostrarMensajeError("El trabajador ya fue agregado.");
        }
    }

    public void actualizarPorducto(ActionEvent actionEvent) {
    }

    public void eliminarTrabajadorEvento(ActionEvent actionEvent) {
    }

    public void generarFactura(ActionEvent actionEvent) {
    }
    @FXML
    public void crearEvento(ActionEvent actionEvent) {
        String errores = validarCamposEvento();
        if (!errores.isEmpty()) {
            mostrarAlerta("Error en campos", errores);
            return;
        }

        int id = Integer.parseInt(txtIdEvento.getText().trim());
        String nombre = txtNombreEvento.getText().trim();
        LocalDate fecha = dpFechaEvento.getValue();
        String lugar = txtLugar.getText().trim();
        int cedulaCliente = Integer.parseInt(txtCedulaCliente.getText().trim());

        List<Evento> eventos = EventoServicio.obtenerEventos();
        for (Evento e : eventos) {
            if (e.getId() == id) {
                mostrarMensajeError("El evento con id: " + id + " ya se encuentra registrado");
                return;
            }
        }
        // Validar que haya al menos un elemento y un trabajador agregados
        if (listaProductosData.isEmpty()) {
            mostrarMensajeError("Debe agregar al menos un elemento para el evento.");
            return;
        }
        if (listaTrabajadorData.isEmpty()) {
            mostrarMensajeError("Debe agregar al menos un trabajador para el evento.");
            return;
        }
        Cliente cliente = ClienteServicio.buscarCliente(cedulaCliente);
        if (cliente == null) {
            mostrarMensajeError("No se encontró un cliente con la cédula ingresada.");
            return;
        }
        // Crear el evento con las listas actuales
        Evento evento = new Evento(
                id,
                nombre,
                Date.valueOf(fecha),
                lugar,
                0.0, // Puedes calcular el precio si lo necesitas
                cliente,
                listaProductosData,
                listaTrabajadorData
        );

        Evento event = EventoServicio.crearEvento(evento);

        if (event != null) {
            listaEventosData.add(evento);
            limpiarCamposEvento();
            listaProductosData.clear();
            listaTrabajadorData.clear();
            tableElementos.getItems().clear();
            tableTrabajadores.getItems().clear();
            mostrarMensaje("Notificación Evento", null, "El evento se ha creado con éxito", Alert.AlertType.INFORMATION);
        } else {
            mostrarMensajeError("El evento con ID: " + id + " ya se encuentra registrado.");
        }
    }

    @FXML
    void eliminarElemento(ActionEvent event) {

    }


    public void initialize(URL location, ResourceBundle resources) {
        columnCantidadElemento.setCellValueFactory(new PropertyValueFactory<Producto, String>("cantidad"));
        columnNombreElemento.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombre"));
        columnPrecioElemento.setCellValueFactory(new PropertyValueFactory<Producto, String>("precio"));
        columnNombreTrabajador.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("nombre"));
        columnCargoTrabajador.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("cargo"));
        columnPrecioTrabajador.setCellValueFactory(new PropertyValueFactory<Trabajador, String>("precio"));
        lblUserAdmin.setText(LoginServicio.getUsuarioActual().getNombre());
        columnCedulaProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("id"));
        columnNombreProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombre"));
        columnTelefonoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("numeroContacto"));

        lblFecha2.setText(lblFecha2.getText() + LocalDate.now(Clock.systemDefaultZone()));
        lblHora2.setText(lblHora2.getText() + LocalTime.now());

        columnNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombre"));
        columnIdProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("id"));
        columnPrecioProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("precio"));
        columnCantidadProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("cantidad"));
        columnAlquilerProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("precioDeAlquiler"));
        columnDescripcionProducto.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescripcion().isEmpty() ? "" : cellData.getValue().getDescripcion())
        );
        tableTrabajadores.setItems(listaTrabajadorData);
        tableElementos.setItems(listaProductosData);
        tableProdcutos.setItems(listaProductosData);
        tableProveedor.setItems(listaProveedoresData);
        /*tableElementos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtIdProducto.setText(String.valueOf(newSelection.getId()));
                txtNombreProducto.setText(newSelection.getNombre());
                txtDescripProducto.setText(newSelection.getDescripcion());
                txtPrecioProducto.setText(String.valueOf(newSelection.getPrecio()));
                txtCantidadProducto.setText(String.valueOf(newSelection.getCantidad()));
                txtPrecioAlquilerProducto.setText(String.valueOf(newSelection.getPrecioDeAlquiler()));
            }
        });*/
        tableProdcutos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtIdProducto.setText(String.valueOf(newSelection.getId()));
                txtNombreProducto.setText(newSelection.getNombre());
                txtDescripProducto.setText(newSelection.getDescripcion());
                txtPrecioProducto.setText(String.valueOf(newSelection.getPrecio()));
                txtCantidadProducto.setText(String.valueOf(newSelection.getCantidad()));
                txtPrecioAlquilerProducto.setText(String.valueOf(newSelection.getPrecioDeAlquiler()));
            }
        });
        tableProveedor.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCedulaProveedor.setText(newSelection.getId());
                txtNombreProveedor.setText(newSelection.getNombre());
                txtTelefono.setText(newSelection.getNumeroContacto());
            }
        });
        cargarProductosEnTabla();
        cargarProveedoresEnTabla();
        cargarElementosEnTabla();
        cargarTrabajadoresEnTabla();


    }
    private void cargarTrabajadoresEnTabla() {
        TrabajadorServicio servicio = new TrabajadorServicio();
        List<Trabajador> trabajadores = servicio.obtenerTrabajadores();

        ObservableList<Trabajador> listaObservable = FXCollections.observableArrayList(trabajadores);
        tableTrabajadores.setItems(listaObservable);

        // Configurar el ComboBox de trabajadores
        comboBoxTrabajadores.setItems(listaObservable);
        comboBoxTrabajadores.setConverter(new StringConverter<Trabajador>() {
            @Override
            public String toString(Trabajador trabajador) {
                return trabajador != null ? trabajador.getNombre() : "";
            }

            @Override
            public Trabajador fromString(String string) {
                return null; // No se usa en este caso
            }
        });
    }

    private void cargarElementosEnTabla() {
        ProductoServicio servicio = new ProductoServicio();
        List<Producto> productos = servicio.obtenerProductos();

        ObservableList<Producto> listaObservable = FXCollections.observableArrayList(productos);
        tableElementos.setItems(listaObservable);

        comboBoxElementos.setItems(listaObservable);
        comboBoxElementos.setConverter(new StringConverter<Producto>() {
            @Override
            public String toString(Producto producto) {
                return producto != null ? producto.getNombre() : "";
            }

            @Override
            public Producto fromString(String string) {
                return null; // No se usa en este caso
            }
        });
    }

    /*public void initializeTableProducto(URL location, ResourceBundle resources) {
        columnIdProducto.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnDescripcionProducto.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnPrecioProducto.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnCantidadProducto.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnAlquilerProducto.setCellValueFactory(new PropertyValueFactory<>("precioDeAlquiler"));

        cargarProductosEnTabla();
    }

    @FXML
    public void initializeTableProveedores(URL location, ResourceBundle resources) {
        columnCedulaProveedor.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNombreProveedor.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnTelefonoProveedor.setCellValueFactory(new PropertyValueFactory<>("numeroContacto"));

        cargarProveedoresEnTabla();
    }*/

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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }




    private void cargarProductosEnTabla() {
        ProductoServicio servicio = new ProductoServicio();
        List<Producto> productos = servicio.obtenerProductos();

        ObservableList<Producto> listaObservable = FXCollections.observableArrayList(productos);
        tableProdcutos.setItems(listaObservable);
    }



    private void agregarProducto() {
          // Validar los datos antes de crear el producto
          String error = validarCamposProducto();
          if (!error.isEmpty()) {
              mostrarAlerta("Error en campos", error);
              return;
          }

          int id = Integer.parseInt(txtIdProducto.getText().trim());
          String nombre = txtNombreProducto.getText().trim();
          String descripcion = txtDescripProducto.getText().trim();
          double precio = Double.parseDouble(txtPrecioProducto.getText().trim());
          int cantidad = Integer.parseInt(txtCantidadProducto.getText().trim());
          double precioDeAlquiler = Double.parseDouble(txtPrecioAlquilerProducto.getText().trim());

          Producto producto = new Producto(nombre, id, descripcion, precio, precioDeAlquiler, cantidad);

          ProductoServicio servicio = new ProductoServicio();
          Producto productoAux = servicio.crearProducto(producto);

          if (productoAux != null) {
              listaProductosData.add(productoAux);
              limpiarCamposProducto();
              mostrarMensaje("Notificación Producto", null, "El producto se ha creado con éxito",
                      Alert.AlertType.INFORMATION);
          } else {
              mostrarMensajeError("El producto con ID: " + id + " ya se encuentra registrado.");
          }
      }

      private String validarCamposProducto() {
          StringBuilder errores = new StringBuilder();

          if (txtIdProducto.getText().trim().isEmpty()) {
              errores.append("- El ID no puede estar vacío.\n");
          } else {
              try {
                  Integer.parseInt(txtIdProducto.getText().trim());
              } catch (NumberFormatException e) {
                  errores.append("- El ID debe ser numérico.\n");
              }
          }

          if (txtNombreProducto.getText().trim().isEmpty()) {
              errores.append("- El nombre no puede estar vacío.\n");
          }

          if (txtDescripProducto.getText().trim().isEmpty()) {
              errores.append("- La descripción no puede estar vacía.\n");
          }

          if (txtPrecioProducto.getText().trim().isEmpty()) {
              errores.append("- El precio no puede estar vacío.\n");
          } else {
              try {
                  Double.parseDouble(txtPrecioProducto.getText().trim());
              } catch (NumberFormatException e) {
                  errores.append("- El precio debe ser numérico.\n");
              }
          }

          if (txtCantidadProducto.getText().trim().isEmpty()) {
              errores.append("- La cantidad no puede estar vacía.\n");
          } else {
              try {
                  Integer.parseInt(txtCantidadProducto.getText().trim());
              } catch (NumberFormatException e) {
                  errores.append("- La cantidad debe ser numérica.\n");
              }
          }

          return errores.toString();
      }

     private void limpiarCamposProducto() {
          txtIdProducto.clear();
          txtNombreProducto.clear();
          txtDescripProducto.clear();
          txtPrecioProducto.clear();
          txtCantidadProducto.clear();
      }



    private void actualizarProducto() {
        String error = validarCamposProducto();
        if (!error.isEmpty()) {
            mostrarAlerta("Error en campos", error);
            return;
        }

        Producto productoSeleccionado = tableProdcutos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un producto de la tabla para editar.");
            return;
        }

        int id = productoSeleccionado.getId();  // Más seguro que confiar en el campo
        String nombre = txtNombreProducto.getText().trim();
        String descripcion = txtDescripProducto.getText().trim();
        double precio = Double.parseDouble(txtPrecioProducto.getText().trim());
        int cantidad = Integer.parseInt(txtCantidadProducto.getText().trim());
        double precioDeAlquiler = Double.parseDouble(txtPrecioAlquilerProducto.getText().trim());

        Producto producto = new Producto(nombre, id, descripcion, precio, precioDeAlquiler, cantidad);

        ProductoServicio servicio = new ProductoServicio();
        boolean exito = servicio.actualizarProducto(producto);

        if (exito) {
            mostrarMensaje("Notificación", null, "Producto actualizado con éxito", Alert.AlertType.INFORMATION);
            cargarProductosEnTabla();
            limpiarCamposProducto();
        } else {
            mostrarMensajeError("No se pudo actualizar el producto con ID: " + id);
        }
    }


    private void eliminarProducto() {
          Producto productoSeleccionado = tableProdcutos.getSelectionModel().getSelectedItem();

          if (productoSeleccionado == null) {
              mostrarMensajeError("Debe seleccionar un producto de la tabla para eliminar.");
              return;
          }

          int id = productoSeleccionado.getId();

          ProductoServicio servicio = new ProductoServicio();
          boolean exito = servicio.eliminarProducto(id);

          if (exito) {
              mostrarMensaje("Notificación", null, "Producto eliminado con éxito", Alert.AlertType.INFORMATION);
              cargarProductosEnTabla();
              limpiarCamposProducto();
          } else {
              mostrarMensajeError("No se pudo eliminar el producto con ID: " + id);
          }
      }





    private void agregarProveedor() {
              String id = txtCedulaProveedor.getText().trim();
              String nombre = txtNombreProveedor.getText().trim();
              String numeroContacto = txtTelefono.getText().trim();

              if (id.isEmpty() || nombre.isEmpty() || numeroContacto.isEmpty()) {
                  mostrarAlerta("Campos vacíos", "Debe completar todos los campos para agregar un proveedor.");
                  return;
              }

              Proveedor proveedor = new Proveedor(id, nombre, numeroContacto);

              boolean exito = ProveedorServicio.crearProveedor(proveedor);
              if (exito) {
                  listaProveedoresData.add(proveedor);
                  limpiarCamposProveedor();
                  mostrarMensaje("Proveedor creado", null, "El proveedor se ha registrado con éxito.",
                          Alert.AlertType.INFORMATION);
              } else {
                  mostrarMensajeError("No se pudo registrar el proveedor. Puede que ya exista.");
              }
          }

    private void actualizarProveedor() {
        Proveedor proveedorSeleccionado = tableProveedor.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un proveedor de la tabla para editar.");
            return;
        }

        String id = proveedorSeleccionado.getId();  // ID no debe cambiar, se toma del proveedor seleccionado
        String nombre = txtNombreProveedor.getText().trim();
        String numeroContacto = txtTelefono.getText().trim();

        if (nombre.isEmpty() || numeroContacto.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Debe completar todos los campos para actualizar un proveedor.");
            return;
        }

        Proveedor proveedor = new Proveedor(id, nombre, numeroContacto);

        boolean exito = ProveedorServicio.actualizarProveedor(proveedor);
        if (exito) {
            mostrarMensaje("Proveedor actualizado", null, "Proveedor actualizado correctamente.",
                    Alert.AlertType.INFORMATION);
            cargarProveedoresEnTabla();  // Refresca la tabla
            limpiarCamposProveedor();
        } else {
            mostrarMensajeError("No se pudo actualizar el proveedor.");
        }
    }


    private void buscarProveedor() {
              String id = txtCedulaProveedor.getText().trim();

              if (id.isEmpty()) {
                  mostrarAlerta("Campo ID vacío", "Debe ingresar el ID del proveedor que desea buscar.");
                  return;
              }

              Proveedor proveedor = ProveedorServicio.buscarProveedor(id);
              if (proveedor != null) {
                  txtNombreProveedor.setText(proveedor.getNombre());
                  txtTelefono.setText(proveedor.getNumeroContacto());
                  mostrarMensaje("Proveedor encontrado", null, "Proveedor cargado correctamente.",
                          Alert.AlertType.INFORMATION);
              } else {
                  mostrarMensajeError("No se encontró el proveedor con ID: " + id);
              }
          }

    private void eliminarProveedor() {
        Proveedor proveedorSeleccionado = tableProveedor.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Debe seleccionar un proveedor en la tabla para eliminar.");
            return;
        }

        String id = proveedorSeleccionado.getId();  // Asumiendo que getId() retorna la cédula o identificador del proveedor

        boolean exito = ProveedorServicio.eliminarProveedor(id);
        if (exito) {
            listaProveedoresData.remove(proveedorSeleccionado);  // Elimina de la tabla
            limpiarCamposProveedor();
            mostrarMensaje("Proveedor eliminado", null, "Proveedor eliminado exitosamente.",
                    Alert.AlertType.INFORMATION);
        } else {
            mostrarMensajeError("No se pudo eliminar el proveedor. Verifique si el ID es correcto.");
        }
    }


    private void cargarProveedoresEnTabla() {
            List<Proveedor> lista = ProveedorServicio.obtenerProveedores();
            tableProveedor.getItems().setAll(lista);

            }
    private void limpiarCamposProveedor() {
        txtCedulaProveedor.clear();
        txtNombreProveedor.clear();
        txtTelefono.clear();
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }





    private String validarCamposEvento() {
        StringBuilder errores = new StringBuilder();

        // Aquí deberías validar los campos del evento, por ejemplo:
        if (txtNombreEvento.getText().trim().isEmpty()) {
             errores.append("- El nombre del evento no puede estar vacío.\n");
        }
        if (dpFechaEvento.getValue() == null) {
            errores.append("- La fecha del evento no puede estar vacía.\n");
         }
        if (txtLugar.getText().trim().isEmpty()) {
            errores.append("- El lugar del evento no puede estar vacío.\n");
        }
        if (txtCedulaCliente.getText().trim().isEmpty()) {
            errores.append("- La cédula del cliente no puede estar vacía.\n");
        }
        if (txtIdEvento.getText().trim().isEmpty()) {
            errores.append("- El ID del evento no puede estar vacío.\n");
        } else {
            try {
                Integer.parseInt(txtIdEvento.getText().trim());
            } catch (NumberFormatException e) {
                errores.append("- El ID del evento debe ser numérico.\n");
            }
        }

        if (comboBoxTrabajadores.getSelectionModel().getSelectedItem() == null) {
            errores.append("- Debe seleccionar un trabajador.\n");
        }
        if (comboBoxElementos.getSelectionModel().getSelectedItem() == null) {
            errores.append("- Debe seleccionar al menos un elemento para el evento.\n");
        }
        if (dpFechaEvento.getValue() != null && dpFechaEvento.getValue().isBefore(LocalDate.now())) {
            errores.append("- La fecha del evento no puede ser anterior a la fecha actual.\n");
        }
        if (dpFechaEvento.getValue() != null && dpFechaEvento.getValue().isAfter(LocalDate.now().plusYears(1))) {
            errores.append("- La fecha del evento no puede ser más de un año en el futuro.\n");
        }
        if (txtLugar.getText().trim().length() > 100) {
            errores.append("- El lugar del evento no puede exceder los 100 caracteres.\n");
        }
        // ... y así sucesivamente para otros campos

        return errores.toString();
    }

    public void limpiarCamposEvento() {
        txtIdEvento.clear();
        txtNombreEvento.clear();
        dpFechaEvento.setValue(null);
        txtLugar.clear();
        txtCedulaCliente.clear();
    }

    public void limpiarCasillaEvento(ActionEvent actionEvent) {
    }
}
