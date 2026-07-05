module com.example.text_game {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.text_game to javafx.fxml;
    exports com.example.text_game;
}