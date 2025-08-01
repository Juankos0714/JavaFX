package com.example.demo;


import com.example.demo.modelo.Libro;
import com.example.demo.modelo.conexion.ConexionBD;
import com.example.demo.modelo.dao.LibroDAO;

public class Coordinador {

    private ConexionBD miConexionBD;
    private LibroDAO miLibroDAO;



    public void setMiLibroDAO(LibroDAO miLibroDAO) {
        this.miLibroDAO = miLibroDAO;
    }
    public String registrarLibro(Libro libro) {
        return miLibroDAO.registrarLibro(libro);
    }

    public Libro consultarLibro(String titulo) {
        return miLibroDAO.consultarLibroPorTitulo(titulo);
    }

    public java.util.List<Libro> consultarTodosLosLibros() {
        return miLibroDAO.consultarTodosLosLibros();
    }

    public String actualizarLibro(Libro libro) {
        return miLibroDAO.actualizarLibro(libro);
    }

    public boolean eliminarLibro(String titulo) {
        return miLibroDAO.eliminarLibro(titulo);
    }
    public void setMiConexionBD(ConexionBD miConexionBD) {
        this.miConexionBD = miConexionBD;
    }

    public void cerrarAplicacion() {
        System.exit(0);
    }
}