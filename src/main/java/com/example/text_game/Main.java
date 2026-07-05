package com.example.text_game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Zobrazení dialogu pro zadání jména postavy
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Character creation");
        dialog.setHeaderText("Welcome in your medieaval adventure!");
        dialog.setContentText("Enter your heroes name:");

        // Počkej na potvrzení od hráče
        Optional<String> result = dialog.showAndWait();
        String playerName = result.orElse("Hero").trim();

        // Pokud hráč jméno smazal a nechal prázdné
        if (playerName.isEmpty()) {
            playerName = "Hero";
        }

        // 2. Spuštění hry s vybraným jménem
        Game game = new Game(playerName);
        GameWindow window = new GameWindow(game);

        Scene scene = new Scene(window.getRoot(), 1050, 650);

        stage.setTitle("Medieval Adventure");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}