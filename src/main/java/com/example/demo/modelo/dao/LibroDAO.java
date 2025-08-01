package com.example.demo.modelo.dao; // Or wherever you standardize your DAOs

import com.example.demo.modelo.conexion.ConexionBD;
import com.example.demo.modelo.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    private ConexionBD conexion = new ConexionBD();

    public String registrarLibro(Libro libro) {
        String resultado = "";
        Connection connection = null;
        PreparedStatement preStatement = null;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "INSERT INTO libros (titulo, autor, año_publicacion) VALUES (?, ?, ?)";
                preStatement = connection.prepareStatement(consulta);
                preStatement.setString(1, libro.titulo());
                preStatement.setString(2, libro.autor());
                preStatement.setInt(3, libro.añoPublicacion());

                int filasAfectadas = preStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado = "Libro registrado exitosamente";
                } else {
                    resultado = "No se pudo registrar el libro";
                }
            } else {
                resultado = "No se pudo conectar a la base de datos";
            }
        } catch (SQLException e) {
            System.out.println("Error SQL al registrar libro: " + e.getMessage());
            resultado = "Error al registrar el libro: " + e.getMessage();
        } finally {
            cerrarRecursos(null, preStatement, connection);
        }
        return resultado;
    }

    public Libro consultarLibroPorTitulo(String titulo) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        Libro libro = null;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "SELECT * FROM libros WHERE titulo = ?";
                statement = connection.prepareStatement(consulta);
                statement.setString(1, titulo);
                result = statement.executeQuery();

                if (result.next()) {
                    libro = new Libro(
                            result.getString("titulo"),
                            result.getString("autor"),
                            result.getInt("año_publicacion")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta del libro: " + e.getMessage());
        } finally {
            cerrarRecursos(result, statement, connection);
        }
        return libro;
    }

    public List<Libro> consultarTodosLosLibros() {
        List<Libro> listaLibros = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "SELECT * FROM libros ORDER BY titulo";
                statement = connection.prepareStatement(consulta);
                result = statement.executeQuery();

                while (result.next()) {
                    Libro libro = new Libro(
                            result.getString("titulo"),
                            result.getString("autor"),
                            result.getInt("año_publicacion")
                    );
                    listaLibros.add(libro);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta de libros: " + e.getMessage());
        } finally {
            cerrarRecursos(result, statement, connection);
        }
        return listaLibros;
    }

    public String actualizarLibro(Libro libro) {
        String resultado = "";
        Connection connection = null;
        PreparedStatement preStatement = null;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "UPDATE libros SET autor = ?, año_publicacion = ? WHERE titulo = ?";
                preStatement = connection.prepareStatement(consulta);
                preStatement.setString(1, libro.autor());
                preStatement.setInt(2, libro.añoPublicacion());
                preStatement.setString(3, libro.titulo());

                int filasAfectadas = preStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado = "Libro actualizado exitosamente";
                } else {
                    resultado = "No se encontró el libro con ese título";
                }
            } else {
                resultado = "No se pudo conectar a la base de datos";
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar libro: " + e.getMessage());
            resultado = "Error al actualizar el libro: " + e.getMessage();
        } finally {
            cerrarRecursos(null, preStatement, connection);
        }
        return resultado;
    }

    public boolean eliminarLibro(String titulo) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean eliminacion = false;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "DELETE FROM libros WHERE titulo = ?";
                statement = connection.prepareStatement(consulta);
                statement.setString(1, titulo);
                int filasAfectadas = statement.executeUpdate();
                eliminacion = (filasAfectadas > 0);
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar libro: " + e.getMessage());
        } finally {
            cerrarRecursos(null, statement, connection);
        }
        return eliminacion;
    }

    private void cerrarRecursos(ResultSet result, PreparedStatement statement, Connection connection) {
        try {
            if (result != null) {
                result.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
