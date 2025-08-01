package com.example.demo.modelo.conexion;



import com.example.demo.Coordinador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream; // Add this import

public class ConexionBD {
    private String nombreBd;
    private String usuario;
    private String password;
    private String url;

    public ConexionBD() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            properties.load(input);

            this.nombreBd = properties.getProperty("db.nombre_bd");
            this.usuario = properties.getProperty("db.usuario");
            this.password = properties.getProperty("db.password");
            this.url = "jdbc:mysql://localhost:3306/" + nombreBd + "?useUnicode=true&use"
                    + "JDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&"
                    + "serverTimezone=UTC";

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL cargado correctamente");
        } catch (IOException ex) {
            System.out.println("Error loading properties: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, usuario, password);
            if (conn != null) {
                System.out.println("Conexión exitosa a la BD: " + nombreBd);
            } else {
                System.out.println("******************NO SE PUDO CONECTAR " + nombreBd);
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            System.out.println("Verifique que MySQL esté encendido y las credenciales sean correctas");
        }
        return conn;
    }

    public void desconectar() {

    }

    public void setCoordinador(Coordinador miCoordinador) {
    }
}

//CREATE DATABASE biblioteca;
//USE biblioteca;
//
//CREATE TABLE libros (
//        id INT AUTO_INCREMENT PRIMARY KEY,
//        titulo VARCHAR(255) NOT NULL,
//autor VARCHAR(255) NOT NULL,
//año_publicacion INT NOT NULL
//);