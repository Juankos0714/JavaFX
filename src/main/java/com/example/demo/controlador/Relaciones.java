package com.example.demo.controlador;

import com.example.demo.Coordinador;
import com.example.demo.modelo.conexion.ConexionBD;
import com.example.demo.modelo.dao.LibroDAO; // Add this

public class Relaciones {

    public Relaciones(){

        ConexionBD miConexionBD = new ConexionBD();
        LibroDAO miLibroDAO = new LibroDAO(); // Instantiate LibroDAO
        Coordinador miCoordinador = new Coordinador();

        // Se establecen las relaciones entre clases
        miConexionBD.setCoordinador(miCoordinador);

        miLibroDAO.setCoordinador(miCoordinador); // If LibroDAO needs the coordinator

        // Se establecen relaciones con la clase coordinador
        miCoordinador.setMiConexionBD(miConexionBD);
        miCoordinador.setMiLibroDAO(miLibroDAO); // Link LibroDAO to Coordinator


        // Mostrar ventana principal
    }
}
