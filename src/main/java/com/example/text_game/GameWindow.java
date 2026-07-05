package com.example.text_game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class GameWindow {

    private Parent root;

    public GameWindow(Game game) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("game.fxml"));

            root = loader.load();

            GameController controller = loader.getController();
            controller.setGame(game);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Parent getRoot() {
        return root;
    }
}