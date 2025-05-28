package proyecto.modelo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class Calculadora extends Application {

    private TextField display = new TextField();

    @Override
    public void start(Stage stage) {
        display.setEditable(false);
        display.setStyle("-fx-font-size: 24; -fx-background-color: white; -fx-text-fill: black; -fx-border-radius: 5;");
        display.setPrefHeight(50);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(5);

        String[] buttons = {
                "7", "8", "9", "/", "C",
                "4", "5", "6", "*", "(",
                "1", "2", "3", "-", ")",
                "0", ".", "=", "+", "←"
        };

        int row = 0;
        int col = 0;
        for (String txt : buttons) {
            Button btn = new Button(txt);
            btn.setPrefSize(60, 60);
            btn.setStyle(
                    "-fx-background-color: #34495e; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 18; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 10;"
            );
            grid.add(btn, col, row);

            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: #1abc9c; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 18; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 10;"
            ));

            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: #34495e; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 18; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 10;"
            ));

            btn.setOnAction(e -> handleButton(txt));
            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }

        VBox root = new VBox(10, display, grid);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #2c3e50;");

        stage.setScene(new Scene(root));
        stage.setTitle("Calculadora");
        stage.setResizable(false);
        stage.show();
    }

    private void handleButton(String text) {
        switch (text) {
            case "=":
                try {
                    Expression expression = new ExpressionBuilder(display.getText()).build();
                    double result = expression.evaluate();
                    display.setText(String.valueOf(result));
                } catch (Exception e) {
                    display.setText("Error");
                }
                break;
            case "C":
                display.clear();
                break;
            case "←":
                String current = display.getText();
                if (!current.isEmpty()) {
                    display.setText(current.substring(0, current.length() - 1));
                }
                break;
            default:
                display.appendText(text);
                break;
        }
    }
}
