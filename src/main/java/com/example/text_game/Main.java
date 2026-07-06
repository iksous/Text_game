package com.example.text_game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Game game = null;
        File saveFile = new File("savegame.txt");

        // 1. KROK: Kontrola uložené hry
        if (saveFile.exists()) {
            ButtonType btnLoad = new ButtonType("Load Saved Game");
            ButtonType btnNew = new ButtonType("Start New Game");

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Save File Found");
            alert.setHeaderText("An existing save file was detected.");
            alert.setContentText("Would you like to load your previous adventure or start a new one?");
            alert.getButtonTypes().setAll(btnLoad, btnNew);

            Optional<ButtonType> choice = alert.showAndWait();

            if (choice.isPresent() && choice.get() == btnLoad) {
                game = new Game("Hero", "Knight"); // Dočasné hodnoty, load je přepíše
                game.loadGame();
            }
        }

        // 2. KROK: Nová hra (Jméno + Výběr Classy)
        if (game == null) {
            // A) Zadaní jména
            TextInputDialog nameDialog = new TextInputDialog("Martin");
            nameDialog.setTitle("Character Creation");
            nameDialog.setHeaderText("Welcome to the Medieval Adventure!");
            nameDialog.setContentText("Enter your hero's name:");

            Optional<String> nameResult = nameDialog.showAndWait();
            String playerName = nameResult.orElse("Hero").trim();
            if (playerName.isEmpty()) playerName = "Hero";

            // B) Výběr třídy (Class)
            List<String> classes = Arrays.asList("Knight", "Mage");
            ChoiceDialog<String> classDialog = new ChoiceDialog<>("Knight", classes);
            classDialog.setTitle("Class Selection");
            classDialog.setHeaderText("Choose your destiny, " + playerName + "!");
            classDialog.setContentText("Select your class:");

            Optional<String> classResult = classDialog.showAndWait();
            String playerClass = classResult.orElse("Knight");

            game = new Game(playerName, playerClass);
        }

        // 3. KROK: Spuštění okna
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