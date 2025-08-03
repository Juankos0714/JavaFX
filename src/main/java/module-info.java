module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.demo to javafx.fxml;
    opens com.example.demo.modelo to javafx.base;
    opens com.example.demo.vista to javafx.fxml;

    exports com.example.demo;
    exports com.example.demo.modelo;
    exports com.example.demo.vista;
}