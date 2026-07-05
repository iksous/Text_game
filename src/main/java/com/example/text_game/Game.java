package com.example.text_game;

public class Game {
    private Player player;
    private Location actualLocation;

    public Game(String name) {

        player = new Player(name);

        actualLocation = new Location(
                "Village",
                "You are in medieval village.",
                "images/Village.png");
    }

    public Player getPlayer() {
        return player;
    }

    public Location getActualLocation() {
        return actualLocation;
    }

    public void goIntoForest() {

        actualLocation = new Location(
                "Forest",
                "Everywhere around you are old tall trees.",
                "images/les.png");
    }

    public void goIntoFCastle() {

        actualLocation = new Location(
                "Castle",
                "Majestic castle stands before you.",
                "images/hrad.png");
    }
}
