package com.example.text_game;

import java.io.*;
import java.util.Arrays;

public class Game {
    private Player player;
    private Location actualLocation;
    private Enemy currentEnemy;
    private String questState;
    private String combatLog = "";

    public Game(String name, String playerClass) {
        player = new Player(name, playerClass);
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

    public void saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("savegame.txt"))) {
            writer.println(player.getName());
            writer.println(player.getHealth());
            writer.println(player.getGold());
            writer.println(player.getAttackPower());
            writer.println(player.getDefense());
            writer.println(String.join(",", player.getInventory()));
            writer.println(questState);
            writer.println(actualLocation.getName());
            // NOVÉ: Ukládání magických dat
            writer.println(player.getPlayerClass());
            writer.println(player.getMana());
            writer.println(player.getMaxMana());
            writer.println(player.getMagicAttack());

            combatLog = "Game successfully saved to 'savegame.txt'!";
        } catch (IOException e) {
            combatLog = "Error while saving: " + e.getMessage();
        }
    }

    public boolean loadGame() {
        File file = new File("savegame.txt");
        if (!file.exists()) {
            combatLog = "No save file found!";
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            player.setName(reader.readLine());
            player.setHealth(Integer.parseInt(reader.readLine()));
            player.setGold(Integer.parseInt(reader.readLine()));
            player.setAttackPower(Integer.parseInt(reader.readLine()));
            player.setDefense(Integer.parseInt(reader.readLine()));

            String inventoryLine = reader.readLine();
            this.questState = reader.readLine();
            String savedLocationName = reader.readLine();

            // NOVÉ: Načítání magických dat
            player.setPlayerClass(reader.readLine());
            player.setMana(Integer.parseInt(reader.readLine()));
            player.setMaxMana(Integer.parseInt(reader.readLine()));
            player.setMagicAttack(Integer.parseInt(reader.readLine()));

            player.getInventory().clear();
            if (inventoryLine != null && !inventoryLine.trim().isEmpty()) {
                player.getInventory().addAll(Arrays.asList(inventoryLine.split(",")));
            }

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
        if (player != null) player.regenerateManaOnMove();
        currentEnemy = null;
        combatLog = "";
        actualLocation = new Location("Village Square", "Safe village square.", "images/Village.png");
    }

    public void goIntoBlacksmith() {
        player.regenerateManaOnMove();
        combatLog = "";
        actualLocation = new Location("Blacksmith", "The blacksmith forge.", "images/Village.png");
    }

    public void goIntoAlchemist() {
        player.regenerateManaOnMove();
        combatLog = "";
        actualLocation = new Location("Alchemist", "The alchemist lab.", "images/Village.png");
    }

    public void goIntoForest() {
        player.regenerateManaOnMove();
        combatLog = "";
        actualLocation = new Location("Forest", "Dark forest.", "images/Forest.png");
        spawnEnemy();
    }

    public void spawnEnemy() {
        currentEnemy = new Enemy("Forest Goblin", 30, 50, 5, 12, 15, 30);
        combatLog = "A wild " + currentEnemy.getName() + " engaged you!";
    }

    public void goIntoCastle() {
        player.regenerateManaOnMove();
        combatLog = "";
        actualLocation = new Location("Castle", "The majestic throne room.", "images/Castle.png");
    }
}