package com.example.text_game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.io.File;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Game game = null;
        File saveFile = new File("savegame.txt");

        // 1. KROK: Zkontrolujeme, zda existuje uložená hra
        if (saveFile.exists()) {
            ButtonType btnLoad = new ButtonType("Load Saved Game");
            ButtonType btnNew = new ButtonType("Start New Game");

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Save File Found");
            alert.setHeaderText("An existing save file was detected.");
            alert.setContentText("Would you like to load your previous adventure or start a new one?");
            alert.getButtonTypes().setAll(btnLoad, btnNew);

            Optional<ButtonType> choice = alert.showAndWait();

            // Pokud hráč kliknul na "Load Saved Game"
            if (choice.isPresent() && choice.get() == btnLoad) {
                game = new Game("Hero"); // Dočasné jméno, load ho hned přepíše
                game.loadGame();
            }
        }

        // 2. KROK: Pokud save neexistuje, nebo hráč zvolil "Start New Game" (nebo okno zavřel)
        if (game == null) {
            TextInputDialog dialog = new TextInputDialog("Martin");
            dialog.setTitle("Character Creation");
            dialog.setHeaderText("Welcome to the Medieval Adventure!");
            dialog.setContentText("Enter your hero's name:");

            Optional<String> result = dialog.showAndWait();
            String playerName = result.orElse("Hero").trim();

            if (playerName.isEmpty()) {
                playerName = "Hero";
            }

            game = new Game(playerName);
        }

        // 3. KROK: Spuštění samotného herního okna
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