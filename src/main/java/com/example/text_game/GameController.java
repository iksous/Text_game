package com.example.text_game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameController {

    @FXML
    private Label locationLabel;

    private Game game;

    public void setGame(Game game) {
        this.game = game;
        locationLabel.setText(game.getActualLocation().getName());
    }
}