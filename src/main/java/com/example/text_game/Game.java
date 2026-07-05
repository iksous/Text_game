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
                "Nacházíš se na bezpečné návsi středověké vesnice. Odsud můžeš navštívit místní obchody.",
                "images/Village.png");
    }

    public void goIntoBlacksmith() {
        combatLog = "";
        actualLocation = new Location(
                "Blacksmith",
                "U kováře to žhne a zvoní kladivy. Nabízí ostré zbraně a pevná brnění.",
                "images/Village.png");
    }

    public void goIntoAlchemist() {
        combatLog = "";
        actualLocation = new Location(
                "Alchemist",
                "Vzduch zde voní po bylinkách a síře. Alchymistka prodává magické lektvary.",
                "images/Village.png");
    }

    public void goIntoForest() {
        combatLog = "";
        actualLocation = new Location(
                "Forest",
                "Vstoupil jsi do temného lesa. Mezi stromy se něco pohnulo!",
                "images/Forest.png");
        spawnEnemy();
    }

    public void spawnEnemy() {
        // Vytvoří Goblina: 30-50 HP, útok 5-12, odměna 15-30 goldů
        currentEnemy = new Enemy("Lesní Goblin", 30, 50, 5, 12, 15, 30);
        combatLog = "Vyskočil na tebe " + currentEnemy.getName() + "!";
    }

    public void goIntoCastle() {
        combatLog = "";
        actualLocation = new Location(
                "Castle",
                "Stojíš v trůním sále majestátního hradu. Na trůnu sedí ustaraný král.",
                "images/Castle.png");
    }
}