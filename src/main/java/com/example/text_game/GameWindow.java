package com.example.text_game;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameWindow {

    private BorderPane root;

    private Label locationName;
    private Label locationDescription;
    private ImageView imageView;

    public GameWindow(Game game) {

        root = new BorderPane();

        locationName = new Label(game.getActualLocation().getName());
        locationName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        locationDescription = new Label(game.getActualLocation().getDescription());
        locationDescription.setWrapText(true);

        VBox center = new VBox(15);
        center.setPadding(new Insets(20));
        center.getChildren().addAll(locationName, locationDescription);

        Button forest = new Button("Forest");
        Button castle = new Button("Castle");

        forest.setOnAction(e -> {
            game.goIntoForest();
            refresh(game);
        });

        castle.setOnAction(e -> {
            game.goIntoFCastle();
            refresh(game);
        });

        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(15));
        buttons.getChildren().addAll(forest, castle);

        root.setCenter(center);
        root.setBottom(buttons);
    }

    private void refresh(Game game) {

        locationName.setText(game.getActualLocation().getName());
        locationDescription.setText(game.getActualLocation().getDescription());

    }

    public Parent getRoot() {
        return root;
    }
}