package proyecto.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import proyecto.modelo.Cliente;
import proyecto.servicio.ClienteServicio;


import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;

public class ClienteController implements Initializable {

    @FXML
    private TableColumn<Cliente, String> columnCedulaCliente;

    @FXML
    private TableColumn<Cliente, String> columnNombreCliente;

    @FXML
    private ComboBox<String> comboBoxGenero;

    @FXML
    private DatePicker dateCliente;

    @FXML
    private TableView<Cliente> tableCliente;

    @FXML
    private TextField txtCedulaCLiente;

    @FXML
    private TextField txtDireccionCliente;

    @FXML
    private TextField txtNombreCliente;

    @FXML
    private TextField txtTelefonoCLiente;

    private final ObservableList<Cliente> listaClientesData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar ComboBox
        comboBoxGenero.setItems(FXCollections.observableArrayList("Masculino", "Femenino", "Otro"));

        // Configurar columnas de la tabla
        columnNombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnCedulaCliente.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        tableCliente.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Cliente cliente = (Cliente) newSelection;
                txtNombreCliente.setText(cliente.getNombre());
                txtCedulaCLiente.setText(String.valueOf(cliente.getCedula()));
                txtDireccionCliente.setText(cliente.getDireccion());
                txtTelefonoCLiente.setText(cliente.getTelefono());
                comboBoxGenero.setValue(cliente.getGenero());
                // Si tienes un DatePicker para la fecha de nacimiento y guardas solo la edad,
                //dateCliente.setValue(cliente.getEdad()); // Aquí puedes establecer la fecha de nacimiento si la tienes.
                // puedes dejarlo vacío o calcular una fecha estimada si lo deseas.
                // dateCliente.setValue(...);
            }
        });

        // Enlazar la lista con la tabla
        tableCliente.setItems(listaClientesData);

        List<Cliente> clientes = ClienteServicio.obtenerCliente();
        listaClientesData.setAll(clientes);
    }

    @FXML
    void crearCliente(ActionEvent event) {
        try {
            crearCliente();
        } catch (Exception e) {
            mostrarMensajeError(e.getMessage());
        }
    }


        private void crearCliente () {
            String nombre = txtNombreCliente.getText();
            int cedula =Integer.parseInt(txtCedulaCLiente.getText());
            String direccion = txtDireccionCliente.getText();
            String telefono = txtTelefonoCLiente.getText();
            String genero = comboBoxGenero.getValue();
            int edad = 0;
            if (dateCliente.getValue() != null) {
                LocalDate nacimiento = dateCliente.getValue();
                LocalDate hoy = LocalDate.now();
                edad = Period.between(nacimiento, hoy).getYears();
            }
            List<Cliente> clientes = ClienteServicio.obtenerCliente();
            for (Cliente c : clientes) {
                if (c.getCedula() == cedula) {
                    mostrarMensajeError("El cliente con cédula: " + cedula + " ya se encuentra registrado");
                    return;
                }
            }
            Cliente cliente = new Cliente(cedula, nombre, edad, direccion, genero, telefono);
            ClienteServicio servicio = new ClienteServicio();
            Cliente clienteAUx = servicio.crearCliente(cliente);
            if(clienteAUx != null){

                actualizarNumerosDeFilas(tableCliente);
                listaClientesData.add(cliente);
                limpiarCampos();
                mostrarMensaje("Cliente Creado", "Cliente creado exitosamente", "El cliente " + clienteAUx.getNombre() + " ha sido creado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarMensajeError("Error al crear el cliente. Por favor, intente nuevamente.");
                return;
            }



        }



            @FXML
            void actualizarCliente(ActionEvent event){
                String nombre = txtNombreCliente.getText();
                int cedula = Integer.parseInt(txtCedulaCLiente.getText());
                String direccion = txtDireccionCliente.getText();
                String telefono = txtTelefonoCLiente.getText();
                String genero = comboBoxGenero.getValue();
                int edad = 0;
                if (dateCliente.getValue() != null) {
                    LocalDate nacimiento = dateCliente.getValue();
                    LocalDate hoy = LocalDate.now();
                    edad = Period.between(nacimiento, hoy).getYears();
                }

                ClienteServicio servicio = new ClienteServicio();
                Cliente clienteExistente = servicio.buscarCliente(cedula); // Debes tener este método

                if (clienteExistente == null) {
                    mostrarMensajeError("No se encontró un cliente con la cédula: " + cedula);
                    return;
                }

                // Actualizar los datos del cliente existente
                clienteExistente.setNombre(nombre);
                clienteExistente.setDireccion(direccion);
                clienteExistente.setGenero(genero);
                clienteExistente.setTelefono(telefono);
                clienteExistente.setEdad(edad);

                boolean actualizado = servicio.actualizarCliente(clienteExistente); // Método que actualiza en la base de datos
                if (actualizado) {
                    int index = tableCliente.getSelectionModel().getSelectedIndex();
                    listaClientesData.set(index, clienteExistente);
                    tableCliente.refresh(); // Si tienes una tabla visible

                    limpiarCampos();
                    mostrarMensaje("Cliente Actualizado", "Actualización exitosa", "El cliente ha sido actualizado correctamente.", Alert.AlertType.INFORMATION);
                } else {
                    mostrarMensajeError("Error al actualizar el cliente. Intenta nuevamente.");
                }
            }


    @FXML
    void eliminarCliente(ActionEvent event) {
        int cedula = Integer.parseInt(txtCedulaCLiente.getText());

        ClienteServicio servicio = new ClienteServicio();
        Cliente cliente = servicio.buscarCliente(cedula); // Método para buscar cliente

        if (cliente == null) {
            mostrarMensajeError("No se encontró un cliente con la cédula: " + cedula);
            return;
        }

        boolean eliminado = servicio.eliminarCliente(cedula); // Método que elimina en base de datos
        if (eliminado) {
            listaClientesData.remove(cliente); // Si usas ObservableList
            actualizarNumerosDeFilas(tableCliente);
            limpiarCampos();
            mostrarMensaje("Cliente Eliminado", "Eliminación exitosa", "El cliente con cédula " + cedula + " ha sido eliminado.", Alert.AlertType.INFORMATION);
        } else {
            mostrarMensajeError("No se pudo eliminar el cliente. Intenta nuevamente.");
        }
    }

    @FXML
            void nuevoCliente (ActionEvent event){
                limpiarCampos();
            }

    private void showAlert (String titulo, String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        txtNombreCliente.clear();
        txtCedulaCLiente.clear();
        txtDireccionCliente.clear();
        txtTelefonoCLiente.clear();
        comboBoxGenero.getSelectionModel().clearSelection();
        dateCliente.setValue(null);
    }

    private void mostrarMensajeError (String mensaje){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Confirmacion");
        alert.setContentText(mensaje);
        alert.showAndWait();

    }

    private void mostrarMensaje (String titulo, String header, String contenido, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void actualizarNumerosDeFilas(TableView<?> tableView) {
        if (tableView != null) {
            tableView.refresh();
        }
    }
}







