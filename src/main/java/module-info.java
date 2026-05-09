/**
 * Java Platform Module System descriptor for the MiniProyecto2 Sudoku application.
 *
 * <p>Declares dependencies on JavaFX and Swing (desktop) APIs, exports application packages,
 * and opens packages required for FXML reflection-based controller injection.</p>
 */
module com.example.miniproyecto2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.miniproyecto2 to javafx.fxml;
    exports com.example.miniproyecto2;
    exports com.example.miniproyecto2.controller;
    opens com.example.miniproyecto2.controller to javafx.fxml;
}