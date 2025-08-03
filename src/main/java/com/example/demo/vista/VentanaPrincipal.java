package com.example.demo.vista;

import com.example.demo.Coordinador;
import com.example.demo.modelo.Libro;
import javafx.beans.property.SimpleObjectProperty; // Añade esta importación
import javafx.beans.property.SimpleStringProperty; // Añade esta importación
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory; // Aunque la mantengas, no la usarás directamente para records
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class VentanaPrincipal {

    private Stage stage;
    private Coordinador miCoordinador;

    // Componentes de la interfaz
    private TextField txtTitulo;
    private TextField txtAutor;
    private TextField txtAño;
    private TextField txtBuscar;
    private TableView<Libro> tablaLibros;
    private ObservableList<Libro> listaLibros;
    private Label lblEstado;

    // Botones - referencias directas
    private Button btnRegistrar;
    private Button btnActualizar;
    private Button btnEliminar;
    private Button btnLimpiar;
    private Button btnBuscar;
    private Button btnMostrarTodos;

    public VentanaPrincipal() {
        stage = new Stage();
        configurarVentana();
        inicializarComponentes();
        configurarEventos();
    }

    public void setCoordinador(Coordinador coordinador) {
        this.miCoordinador = coordinador;
        cargarTodosLosLibros();
    }

    private void configurarVentana() {
        stage.setTitle("Gestión de Biblioteca");
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            if (miCoordinador != null) {
                miCoordinador.cerrarAplicacion();
            }
        });
    }

    private void inicializarComponentes() {
        // Panel principal
        BorderPane panelPrincipal = new BorderPane();
        panelPrincipal.setPadding(new Insets(10));

        // Panel superior - Formulario
        VBox panelFormulario = crearPanelFormulario();
        panelPrincipal.setTop(panelFormulario);

        // Panel central - Tabla
        VBox panelTabla = crearPanelTabla();
        panelPrincipal.setCenter(panelTabla);

        // Panel inferior - Estado
        lblEstado = new Label("Listo");
        lblEstado.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        panelPrincipal.setBottom(lblEstado);

        Scene scene = new Scene(panelPrincipal, 800, 600);
        stage.setScene(scene);
    }

    private VBox crearPanelFormulario() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;");

        Label lblTitulo = new Label("Gestión de Libros");
        lblTitulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Formulario de entrada
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);

        formulario.add(new Label("Título:"), 0, 0);
        txtTitulo = new TextField();
        txtTitulo.setPrefWidth(200);
        formulario.add(txtTitulo, 1, 0);

        formulario.add(new Label("Autor:"), 0, 1);
        txtAutor = new TextField();
        txtAutor.setPrefWidth(200);
        formulario.add(txtAutor, 1, 1);

        formulario.add(new Label("Año:"), 0, 2);
        txtAño = new TextField();
        txtAño.setPrefWidth(200);
        formulario.add(txtAño, 1, 2);

        // Crear botones con referencias
        btnRegistrar = new Button("Registrar");
        btnActualizar = new Button("Actualizar");
        btnEliminar = new Button("Eliminar");
        btnLimpiar = new Button("Limpiar");

        btnRegistrar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnActualizar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnEliminar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnLimpiar.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");

        // Panel de botones
        HBox panelBotones = new HBox(10);
        panelBotones.getChildren().addAll(btnRegistrar, btnActualizar, btnEliminar, btnLimpiar);

        panel.getChildren().addAll(lblTitulo, formulario, panelBotones);

        return panel;
    }

    private VBox crearPanelTabla() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        // Panel de búsqueda
        HBox panelBusqueda = new HBox(10);
        Label lblBuscar = new Label("Buscar por título:");
        txtBuscar = new TextField();
        txtBuscar.setPrefWidth(200);

        btnBuscar = new Button("Buscar");
        btnMostrarTodos = new Button("Mostrar Todos");

        panelBusqueda.getChildren().addAll(lblBuscar, txtBuscar, btnBuscar, btnMostrarTodos);

        // Tabla
        tablaLibros = new TableView<>();

        TableColumn<Libro, String> colTitulo = new TableColumn<>("Título");
        // CAMBIO AQUÍ: Usar un Callback para records
        colTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().titulo()));
        colTitulo.setPrefWidth(250);

        TableColumn<Libro, String> colAutor = new TableColumn<>("Autor");
        // CAMBIO AQUÍ: Usar un Callback para records
        colAutor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().autor()));
        colAutor.setPrefWidth(200);

        TableColumn<Libro, Integer> colAño = new TableColumn<>("Año");
        // CAMBIO AQUÍ: Usar un Callback para records
        colAño.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().añoPublicacion()));
        colAño.setPrefWidth(100);

        tablaLibros.getColumns().addAll(colTitulo, colAutor, colAño);

        listaLibros = FXCollections.observableArrayList();
        tablaLibros.setItems(listaLibros);

        panel.getChildren().addAll(panelBusqueda, tablaLibros);

        return panel;
    }

    private void configurarEventos() {
        // Configurar eventos de botones usando las referencias directas
        btnRegistrar.setOnAction(e -> registrarLibro());
        btnActualizar.setOnAction(e -> actualizarLibro());
        btnEliminar.setOnAction(e -> eliminarLibro());
        btnLimpiar.setOnAction(e -> limpiarCampos());
        btnBuscar.setOnAction(e -> buscarLibro());
        btnMostrarTodos.setOnAction(e -> cargarTodosLosLibros());

        // Selección en tabla
        tablaLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarLibroEnFormulario(newSelection);
            }
        });

        // Enter en campo de búsqueda
        txtBuscar.setOnAction(e -> buscarLibro());

        // Enter en campos del formulario
        txtTitulo.setOnAction(e -> txtAutor.requestFocus());
        txtAutor.setOnAction(e -> txtAño.requestFocus());
        txtAño.setOnAction(e -> registrarLibro());
    }

    private void registrarLibro() {
        if (validarCampos()) {
            try {
                int año = Integer.parseInt(txtAño.getText().trim());
                Libro libro = new Libro(
                        txtTitulo.getText().trim(),
                        txtAutor.getText().trim(),
                        año
                );

                String resultado = miCoordinador.registrarLibro(libro);
                mostrarMensaje(resultado);

                if (resultado.contains("exitosamente")) {
                    limpiarCampos();
                    cargarTodosLosLibros();
                }
            } catch (NumberFormatException e) {
                mostrarError("El año debe ser un número válido");
            }
        }
    }

    private void actualizarLibro() {
        if (validarCampos()) {
            try {
                int año = Integer.parseInt(txtAño.getText().trim());
                Libro libro = new Libro(
                        txtTitulo.getText().trim(),
                        txtAutor.getText().trim(),
                        año
                );

                String resultado = miCoordinador.actualizarLibro(libro);
                mostrarMensaje(resultado);

                if (resultado.contains("exitosamente")) {
                    cargarTodosLosLibros();
                }
            } catch (NumberFormatException e) {
                mostrarError("El año debe ser un número válido");
            }
        }
    }

    private void eliminarLibro() {
        if (txtTitulo.getText().trim().isEmpty()) {
            mostrarError("Debe ingresar el título del libro a eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar este libro?");
        confirmacion.setContentText("Título: " + txtTitulo.getText().trim() + "\nEsta acción no se puede deshacer");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean eliminado = miCoordinador.eliminarLibro(txtTitulo.getText().trim());

            if (eliminado) {
                mostrarMensaje("Libro eliminado exitosamente");
                limpiarCampos();
                cargarTodosLosLibros();
            } else {
                mostrarError("No se pudo eliminar el libro. Verifique que el título sea correcto.");
            }
        }
    }

    private void buscarLibro() {
        if (txtBuscar.getText().trim().isEmpty()) {
            mostrarError("Ingrese un título para buscar");
            return;
        }

        Libro libro = miCoordinador.consultarLibro(txtBuscar.getText().trim());

        if (libro != null) {
            listaLibros.clear();
            listaLibros.add(libro);
            cargarLibroEnFormulario(libro);
            mostrarMensaje("Libro encontrado");
        } else {
            listaLibros.clear();
            mostrarError("No se encontró ningún libro con ese título");
        }
    }

    private void cargarTodosLosLibros() {
        try {
            List<Libro> libros = miCoordinador.consultarTodosLosLibros();
            listaLibros.clear();
            listaLibros.addAll(libros);
            mostrarMensaje("Se cargaron " + libros.size() + " libros");
        } catch (Exception e) {
            mostrarError("Error al cargar los libros: " + e.getMessage());
        }
    }

    private void cargarLibroEnFormulario(Libro libro) {
        txtTitulo.setText(libro.titulo());
        txtAutor.setText(libro.autor());
        txtAño.setText(String.valueOf(libro.añoPublicacion()));
    }

    private void limpiarCampos() {
        txtTitulo.clear();
        txtAutor.clear();
        txtAño.clear();
        txtBuscar.clear();
        tablaLibros.getSelectionModel().clearSelection();
        txtTitulo.requestFocus();
    }

    private boolean validarCampos() {
        if (txtTitulo.getText().trim().isEmpty()) {
            mostrarError("El título es obligatorio");
            txtTitulo.requestFocus();
            return false;
        }

        if (txtAutor.getText().trim().isEmpty()) {
            mostrarError("El autor es obligatorio");
            txtAutor.requestFocus();
            return false;
        }

        if (txtAño.getText().trim().isEmpty()) {
            mostrarError("El año es obligatorio");
            txtAño.requestFocus();
            return false;
        }

        try {
            int año = Integer.parseInt(txtAño.getText().trim());
            if (año < 0 || año > java.time.Year.now().getValue() + 1) {
                mostrarError("El año debe estar entre 0 y " + (java.time.Year.now().getValue() + 1));
                txtAño.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El año debe ser un número válido");
            txtAño.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarMensaje(String mensaje) {
        lblEstado.setText(mensaje);
        lblEstado.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        System.out.println("INFO: " + mensaje);
    }

    private void mostrarError(String error) {
        lblEstado.setText(error);
        lblEstado.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();

        System.out.println("ERROR: " + error);
    }

    public void mostrarVentana() {
        stage.show();
        txtTitulo.requestFocus();
    }

    public Stage getStage() {
        return stage;
    }
}