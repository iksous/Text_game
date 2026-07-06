package com.example.text_game;

public class Game {
    private Player player;
    private Location actualLocation;
    private Enemy currentEnemy;
    private String questState; // Stavy: "AVAILABLE" (Dostupný), "ACTIVE" (Přijatý), "DONE" (Splněný), "REWARDED" (Odevzdaný)
    private String combatLog = "";

    public Game(String name) {
        player = new Player(name);
        questState = "AVAILABLE";
        goIntoVillage(); // Začínáme na návsi
    }

    public Player getPlayer() { return player; }
    public Location getActualLocation() { return actualLocation; }
    public Enemy getCurrentEnemy() { return currentEnemy; }
    public String getQuestState() { return questState; }
    public void setQuestState(String state) { this.questState = state; }
    public String getCombatLog() { return combatLog; }
    public void setCombatLog(String log) { this.combatLog = log; }

    public void goIntoVillage() {
        currentEnemy = null;
        combatLog = "";
        actualLocation = new Location(
                "Village Square",
                "You enetered small village where you can choose to go to king, forest or buy new equipment.",
                "images/Village.png");
    }

    public void goIntoBlacksmith() {
        combatLog = "";
        actualLocation = new Location(
                "Blacksmith",
                "Blacksmith is hard working. But he offeres you his hand made weapons and armor.",
                "images/Village.png");
    }

    public void goIntoAlchemist() {
        combatLog = "";
        actualLocation = new Location(
                "Alchemist",
                "The air contains traces or herbs and sulfur. The alchemist offeres you her potions",
                "images/Village.png");
    }

    public void goIntoForest() {
        combatLog = "";
        actualLocation = new Location(
                "Forest",
                "You enetered dark forest",
                "images/Forest.png");
        spawnEnemy();
    }

    public void spawnEnemy() {
        currentEnemy = new Enemy("Goblin", 30, 50, 5, 12, 15, 30);
        combatLog = currentEnemy.getName() + "found you!";
    }

    public void goIntoCastle() {
        combatLog = "";
        actualLocation = new Location(
                "Castle",
                "You stand in throne room. Maybe king wants to give you some quests",
                "images/Castle.png");
    }
}