module myjfx {
    requires javafx.fxml;
    requires java.desktop;
    requires animatefx;
    opens client to javafx.graphics, javafx.fxml;

    exports server;
    exports client;
    opens server to javafx.fxml, javafx.graphics;
    exports shared;
    opens shared to javafx.fxml, javafx.graphics;
}