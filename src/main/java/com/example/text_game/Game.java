package com.example.text_game;

import java.io.*;
import java.util.Arrays;

public class Game {
    private Player player;
    private Location actualLocation;
    private Enemy currentEnemy;
    private String questState;
    private String combatLog = "";

    public Game(String name) {
        player = new Player(name);
        questState = "AVAILABLE";
        goIntoVillage();
    }

    public Player getPlayer() { return player; }
    public Location getActualLocation() { return actualLocation; }
    public Enemy getCurrentEnemy() { return currentEnemy; }
    public String getQuestState() { return questState; }
    public void setQuestState(String state) { this.questState = state; }
    public String getCombatLog() { return combatLog; }
    public void setCombatLog(String log) { this.combatLog = log; }

    // NOVÉ: Metoda pro uložení hry do souboru
    public void saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("savegame.txt"))) {
            writer.println(player.getName());
            writer.println(player.getHealth());
            writer.println(player.getGold());
            writer.println(player.getAttackPower());
            writer.println(player.getDefense());
            // Uloží inventář oddělený čárkami (např. "Rusty Sword,Healing Potion")
            writer.println(String.join(",", player.getInventory()));
            writer.println(questState);
            writer.println(actualLocation.getName());

            combatLog = "Game successfully saved to 'savegame.txt'!";
        } catch (IOException e) {
            combatLog = "Error while saving: " + e.getMessage();
        }
    }

    // NOVÉ: Metoda pro načtení hry ze souboru
    public boolean loadGame() {
        File file = new File("savegame.txt");
        if (!file.exists()) {
            combatLog = "No save file found! Save the game first.";
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String name = reader.readLine();
            int health = Integer.parseInt(reader.readLine());
            int gold = Integer.parseInt(reader.readLine());
            int attack = Integer.parseInt(reader.readLine());
            int defense = Integer.parseInt(reader.readLine());
            String inventoryLine = reader.readLine();
            String savedQuestState = reader.readLine();
            String savedLocationName = reader.readLine();

            // Použití setterů k obnově hrdiny
            player.setName(name);
            player.setHealth(health);
            player.setGold(gold);
            player.setAttackPower(attack);
            player.setDefense(defense);

            player.getInventory().clear();
            if (inventoryLine != null && !inventoryLine.trim().isEmpty()) {
                String[] items = inventoryLine.split(",");
                player.getInventory().addAll(Arrays.asList(items));
            }

            this.questState = savedQuestState;

            // Obnova správné lokace
            switch (savedLocationName) {
                case "Village Square": goIntoVillage(); break;
                case "Blacksmith": goIntoBlacksmith(); break;
                case "Alchemist": goIntoAlchemist(); break;
                case "Castle": goIntoCastle(); break;
                case "Forest": goIntoForest(); break;
                default: goIntoVillage(); break;
            }

            combatLog = "Game successfully loaded!";
            return true;
        } catch (Exception e) {
            combatLog = "Error while loading: " + e.getMessage();
            return false;
        }
    }

    public void goIntoVillage() {
        currentEnemy = null;
        combatLog = "";
        actualLocation = new Location(
                "Village Square",
                "You are in the safe square of a medieval village. From here, you can visit local shops.",
                "images/Village.png");
    }

    public void goIntoBlacksmith() {
        combatLog = "";
        actualLocation = new Location(
                "Blacksmith",
                "The blacksmith's forge is glowing and ringing with hammers. He offers sharp weapons and sturdy armor.",
                "images/Village.png");
    }

    public void goIntoAlchemist() {
        combatLog = "";
        actualLocation = new Location(
                "Alchemist",
                "The air here smells of herbs and sulfur. The alchemist sells magical potions.",
                "images/Village.png");
    }

    public void goIntoForest() {
        combatLog = "";
        actualLocation = new Location(
                "Forest",
                "You have entered the dark forest. Something rustled between the old trees!",
                "images/Forest.png");
        spawnEnemy();
    }

    public void spawnEnemy() {
        currentEnemy = new Enemy("Forest Goblin", 30, 50, 5, 12, 15, 30);
        combatLog = "A wild " + currentEnemy.getName() + " engaged you!";
    }

    public void goIntoCastle() {
        combatLog = "";
        actualLocation = new Location(
                "Castle",
                "You stand in the throne room of a majestic castle. An anxious King sits on the throne.",
                "images/Castle.png");
    }
}