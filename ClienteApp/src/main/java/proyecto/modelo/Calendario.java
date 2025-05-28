package proyecto.modelo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Calendario extends Application {

    private YearMonth currentYearMonth;
    private final Map<LocalDate, String> notas = new HashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ZoneId zonaColombia = ZoneId.of("America/Bogota");
    private final File notasFile = new File("notas.txt");
    private GridPane calendar;
    private Label monthYearLabel;

    @Override
    public void start(Stage stage) {
        currentYearMonth = YearMonth.now(zonaColombia);
        cargarNotas();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f5;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);

        Button prevMonth = new Button("←");
        Button nextMonth = new Button("→");

        monthYearLabel = new Label();
        monthYearLabel.setFont(Font.font("Arial", 20));
        monthYearLabel.setTextFill(Color.web("#2f3640"));
        actualizarEtiquetaMes();

        prevMonth.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            actualizarEtiquetaMes();
            construirCalendario();
        });

        nextMonth.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            actualizarEtiquetaMes();
            construirCalendario();
        });

        prevMonth.setStyle("-fx-background-color: transparent; -fx-font-size: 18;");
        nextMonth.setStyle("-fx-background-color: transparent; -fx-font-size: 18;");

        header.getChildren().addAll(prevMonth, monthYearLabel, nextMonth);
        root.setTop(header);

        calendar = new GridPane();
        calendar.setHgap(8);
        calendar.setVgap(8);
        calendar.setPadding(new Insets(20));
        construirCalendario();

        root.setCenter(calendar);

        stage.setScene(new Scene(root, 520, 500));
        stage.setTitle("Calendario");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void actualizarEtiquetaMes() {
        String mes = currentYearMonth.getMonth().getDisplayName(java.time.format.TextStyle.FULL, new Locale("es", "CO"));
        mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);
        monthYearLabel.setText(mes + " " + currentYearMonth.getYear());
    }

    private void construirCalendario() {
        calendar.getChildren().clear();

        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (int i = 0; i < dias.length; i++) {
            Label lbl = new Label(dias[i]);
            lbl.setFont(new Font("Arial", 13));
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #718093;");
            calendar.add(lbl, i, 0);
        }

        LocalDate primerDiaDelMes = currentYearMonth.atDay(1);
        int primerDiaSemana = primerDiaDelMes.getDayOfWeek().getValue();
        int offset = primerDiaSemana == 7 ? 0 : primerDiaSemana;
        int diasEnMes = currentYearMonth.lengthOfMonth();

        int fila = 1;
        int columna = offset - 1;
        if (columna < 0) columna = 6;

        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fecha = currentYearMonth.atDay(dia);
            VBox celda = new VBox();
            celda.setPrefSize(65, 65);
            celda.setAlignment(Pos.TOP_LEFT);
            celda.setPadding(new Insets(5));
            celda.setStyle("-fx-background-color: #dcdde1; -fx-border-color: #dcdde1; -fx-border-radius: 5; -fx-background-radius: 5;");

            Label lblDia = new Label(String.valueOf(dia));
            lblDia.setFont(new Font(13));
            lblDia.setTextFill(Color.web("#2f3640"));

            if (notas.containsKey(fecha)) {
                celda.setStyle("-fx-background-color: #e1bee7; -fx-border-color: #b388ff; -fx-border-radius: 5; -fx-background-radius: 5;");
                Label lblNota = new Label(notas.get(fecha).split("\\n")[0]);
                lblNota.setFont(new Font(10));
                lblNota.setTextFill(Color.web("#4b4b4b"));
                celda.getChildren().addAll(lblDia, lblNota);
            } else {
                celda.getChildren().add(lblDia);
            }

            celda.setOnMouseClicked(e -> mostrarDialogoNota(fecha));
            calendar.add(celda, columna, fila);

            columna++;
            if (columna > 6) {
                columna = 0;
                fila++;
            }
        }
    }

    private void mostrarDialogoNota(LocalDate fecha) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nota para " + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        TextArea area = new TextArea();
        area.setPromptText("Escribe tu nota aquí...");
        area.setWrapText(true);
        area.setPrefRowCount(5);

        if (notas.containsKey(fecha)) {
            area.setText(notas.get(fecha));
        }

        Button guardar = new Button("Guardar");
        guardar.setStyle("-fx-background-color: #44bd32; -fx-text-fill: white; -fx-font-weight: bold;");

        guardar.setOnAction(e -> {
            String contenido = area.getText().trim();
            if (!contenido.isEmpty()) {
                notas.put(fecha, contenido);
            } else {
                notas.remove(fecha);
            }
            guardarNotas();
            construirCalendario();
            dialog.close();
        });

        vbox.getChildren().addAll(area, guardar);

        dialog.setScene(new Scene(vbox, 300, 200));
        dialog.show();
    }

    private void cargarNotas() {
        if (!notasFile.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(notasFile))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.contains("=")) {
                    String[] partes = linea.split("=", 2);
                    LocalDate fecha = LocalDate.parse(partes[0], formatter);
                    notas.put(fecha, partes[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarNotas() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(notasFile))) {
            for (Map.Entry<LocalDate, String> entry : notas.entrySet()) {
                writer.write(entry.getKey().format(formatter) + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
