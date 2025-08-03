package com.example.demo.modelo.conexion;

import com.example.demo.Coordinador;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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

            this.url = "jdbc:mysql://localhost:3306/" + nombreBd +
                    "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true" +
                    "&serverTimezone=UTC";

            // Cargar el driver - MySQL 9.x usa el mismo driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL cargado correctamente");

        } catch (IOException ex) {
            System.out.println("Error loading properties: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver: " + e.getMessage());
            System.out.println("Asegúrate de tener mysql-connector-j en las dependencias");
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
            System.out.println("Detalles del error:");
            System.out.println("- URL: " + url);
            System.out.println("- Usuario: " + usuario);
            System.out.println("- Verifique que MySQL esté encendido y las credenciales sean correctas");
            e.printStackTrace();
        }
        return conn;
    }

    public void desconectar() {
    }

    public void setCoordinador(Coordinador miCoordinador) {
    }
}

// SCRIPT SQL PARA CREAR LA BASE DE DATOS:
/*
CREATE DATABASE biblioteca;
USE biblioteca;

CREATE TABLE libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    año_publicacion INT NOT NULL
);

-- Datos de prueba opcionales
INSERT INTO libros (titulo, autor, año_publicacion) VALUES
('El Quijote', 'Miguel de Cervantes', 1605),
('Cien años de soledad', 'Gabriel García Márquez', 1967),
('1984', 'George Orwell', 1949);
*/