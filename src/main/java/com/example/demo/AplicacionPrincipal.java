package com.example.demo;

import com.example.demo.controlador.Relaciones;
import javafx.application.Application;
import javafx.stage.Stage;

public class AplicacionPrincipal extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Inicializar las relaciones y la aplicación
        new Relaciones();
    }

    public static void main(String[] args) {
        launch(args);
    }
}