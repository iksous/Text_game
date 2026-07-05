package com.example.text_game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Game game = new Game("Martin");

        GameWindow window = new GameWindow(game);

        Scene scene = new Scene(window.getRoot(), 900, 600);

        stage.setTitle("Medieval Adventure");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}