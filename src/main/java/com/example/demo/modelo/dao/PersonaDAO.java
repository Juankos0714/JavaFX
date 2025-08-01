package MVC_IMC.modelo.dao;

import MVC_IMC.controlador.Coordinador;
import MVC_IMC.modelo.conexion.ConexionBD;
import MVC_IMC.modelo.dto.PersonaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {
    ConexionBD conexion = new ConexionBD();

    public String registrarPersona(PersonaDTO persona) {
        String resultado = "";
        Connection connection = null;
        PreparedStatement preStatement = null;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "INSERT INTO personas (documento, nombre, edad) VALUES (?, ?, ?)";
                System.out.println("Ejecutando consulta: " + consulta);

                preStatement = connection.prepareStatement(consulta);
                preStatement.setString(1, persona.getDocumento());
                preStatement.setString(2, persona.getNombre());
                preStatement.setInt(3, persona.getEdad());

                int filasAfectadas = preStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado = "Persona registrada exitosamente";
                    System.out.println("Registro exitoso: " + filasAfectadas + " fila(s) afectada(s)");
                } else {
                    resultado = "No se pudo registrar la persona";
                }
            } else {
                resultado = "No se pudo conectar a la base de datos";
            }
        } catch (SQLException e) {
            System.out.println("Error SQL al registrar: " + e.getMessage());
            resultado = "Error al registrar la persona: " + e.getMessage();
        } finally {
            cerrarRecursos(null, preStatement, connection);
        }
        return resultado;
    }

    public PersonaDTO consultarPersonaPorDocumento(String documento) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        PersonaDTO persona = null;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "SELECT * FROM personas WHERE documento = ?";
                statement = connection.prepareStatement(consulta);
                statement.setString(1, documento);
                result = statement.executeQuery();

                if (result.next()) {
                    persona = new PersonaDTO();
                    persona.setDocumento(result.getString("documento"));
                    persona.setNombre(result.getString("nombre"));
                    persona.setEdad(result.getInt("edad"));

                    try {
                        double peso = result.getDouble("peso");
                        double talla = result.getDouble("talla");
                        double imc = result.getDouble("imc");
                        String estado = result.getString("estado");
                        String mensaje = result.getString("mensaje");

                        if (!result.wasNull()) {
                            persona.setPeso(peso);
                            persona.setTalla(talla);
                            persona.setImc(imc);
                            persona.setEstado(estado);
                            persona.setMensaje(mensaje);
                        }

                    } catch (SQLException e) {
                        System.out.println("Algunas columnas de IMC no están disponibles: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta del usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(result, statement, connection);
        }
        return persona;
    }


    public ArrayList<PersonaDTO> consultarPersonas() {
        ArrayList<PersonaDTO> listaPersonas = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "SELECT * FROM personas ORDER BY nombre";
                statement = connection.prepareStatement(consulta);
                result = statement.executeQuery();

                while (result.next()) {
                    PersonaDTO persona = new PersonaDTO();
                    persona.setDocumento(result.getString("documento"));
                    persona.setNombre(result.getString("nombre"));
                    persona.setEdad(result.getInt("edad"));

                    try {
                        double peso = result.getDouble("peso");
                        double talla = result.getDouble("talla");
                        double imc = result.getDouble("imc");
                        String estado = result.getString("estado");
                        String mensaje = result.getString("mensaje");

                        if (!result.wasNull()) {
                            persona.setPeso(peso);
                            persona.setTalla(talla);
                            persona.setImc(imc);
                            persona.setEstado(estado);
                            persona.setMensaje(mensaje);
                        }
                    } catch (SQLException e) {
                        System.out.println("Columnas de IMC no disponibles para " + persona.getDocumento());
                    }

                    listaPersonas.add(persona);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta de personas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(result, statement, connection);
        }
        return listaPersonas;
    }

    public boolean eliminarPersona(String documento) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean eliminacion = false;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "DELETE FROM personas WHERE documento = ?";
                statement = connection.prepareStatement(consulta);
                statement.setString(1, documento);
                int filasAfectadas = statement.executeUpdate();
                eliminacion = (filasAfectadas > 0);
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        } finally {
            cerrarRecursos(null, statement, connection);
        }
        return eliminacion;
    }

    public boolean existePersona(String documento) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        boolean existe = false;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "SELECT COUNT(*) FROM personas WHERE documento = ?";
                statement = connection.prepareStatement(consulta);
                statement.setString(1, documento);
                result = statement.executeQuery();
                if (result.next()) {
                    int count = result.getInt(1);
                    existe = (count > 0);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta del usuario: " + e.getMessage());
        } finally {
            cerrarRecursos(result, statement, connection);
        }
        return existe;
    }

    public String actualizarPersona(PersonaDTO persona) {
        String resultado = "";
        Connection connection = null;
        PreparedStatement preStatement = null;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "UPDATE personas SET nombre = ?, edad = ? WHERE documento = ?";
                preStatement = connection.prepareStatement(consulta);
                preStatement.setString(1, persona.getNombre());
                preStatement.setInt(2, persona.getEdad());
                preStatement.setString(3, persona.getDocumento());

                int filasAfectadas = preStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado = "Persona actualizada exitosamente";
                } else {
                    resultado = "No se encontró la persona con ese documento";
                }
            } else {
                resultado = "No se pudo conectar a la base de datos";
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            resultado = "Error al actualizar la persona: " + e.getMessage();
        } finally {
            cerrarRecursos(null, preStatement, connection);
        }
        return resultado;
    }
    public String asignarIMC(PersonaDTO persona) {
        String resultado = "";
        Connection connection = null;
        PreparedStatement preStatement = null;

        try {
            connection = conexion.getConnection();
            if (connection != null) {
                String consulta = "UPDATE personas SET peso = ?, talla = ?, imc = ?, estado = ?, mensaje = ? WHERE documento = ?";
                preStatement = connection.prepareStatement(consulta);

                preStatement.setDouble(1, persona.getPeso());
                preStatement.setDouble(2, persona.getTalla());
                preStatement.setDouble(3, persona.getImc());
                preStatement.setString(4, persona.getEstado());
                preStatement.setString(5, persona.getMensaje());
                preStatement.setString(6, persona.getDocumento());

                int filasAfectadas = preStatement.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado = "Persona actualizada exitosamente";
                } else {
                    resultado = "No se encontró la persona con ese documento";
                }
            } else {
                resultado = "No se pudo conectar a la base de datos";
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            resultado = "Error al actualizar la persona: " + e.getMessage();
        } finally {
            cerrarRecursos(null, preStatement, connection);
        }
        return resultado;
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

    public void setCoordinador(Coordinador miCoordinador) {
    }
}


