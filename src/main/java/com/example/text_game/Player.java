package com.example.text_game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private String playerClass;
    private int health;
    private int gold;
    private List<String> inventory;
    private int attackPower;
    private int magicAttack; // Nový stat
    private int mana;        // Nový stat
    private int maxMana;     // Nový stat
    private int defense;

    public Player(String name, String playerClass) {
        this.name = name;
        this.playerClass = playerClass;
        this.inventory = new ArrayList<>();
        this.defense = 0;
        this.gold = 20;

        if (playerClass.equals("Mage")) {
            this.health = 80;          // Mág má méně HP
            this.maxMana = 50;         // Má manu
            this.mana = 50;
            this.attackPower = 4;      // Nízký fyzický útok
            this.magicAttack = 22;     // Vysoký magický útok
            this.inventory.add("Apprentice Wand"); // Nutné pro útok mága
        } else {
            this.health = 100;
            this.maxMana = 0;          // Rytíř nemá manu
            this.mana = 0;
            this.attackPower = 12;
            this.magicAttack = 0;
            this.inventory.add("Rusty Sword");
        }
    }

    // Pasivní doplňování many při každém pohybu (20 % z Max Many)
    public void regenerateManaOnMove() {
        if (playerClass.equals("Mage") && maxMana > 0) {
            int amount = (int) (maxMana * 0.20);
            this.mana = Math.min(maxMana, this.mana + amount);
        }
    }

    // Použití Mana Elixíru
    public boolean useManaElixir() {
        if (inventory.contains("Mana Elixir")) {
            inventory.remove("Mana Elixir");
            this.mana = Math.min(maxMana, this.mana + 30); // Doplní 30 many
            return true;
        }
        return false;
    }

    public boolean usePotion() {
        if (inventory.contains("Healing Potion")) {
            inventory.remove("Healing Potion");
            healing(40);
            return true;
        }
        return false;
    }

    // Gettery a Settery (Nutné pro funkčnost a ukládání)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPlayerClass() { return playerClass; }
    public void setPlayerClass(String playerClass) { this.playerClass = playerClass; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public List<String> getInventory() { return inventory; }
    public int getAttackPower() { return attackPower; }
    public void setAttackPower(int attackPower) { this.attackPower = attackPower; }
    public int getMagicAttack() { return magicAttack; }
    public void setMagicAttack(int magicAttack) { this.magicAttack = magicAttack; }
    public int getMana() { return mana; }
    public void setMana(int mana) { this.mana = mana; }
    public int getMaxMana() { return maxMana; }
    public void setMaxMana(int maxMana) { this.maxMana = maxMana; }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }

    public void addGold(int sum) { gold += sum; }
    public void removeGold(int sum) { gold = Math.max(0, gold - sum); }
    public void addAttackPower(int amount) { attackPower += amount; }
    public void addMagicAttack(int amount) { magicAttack += amount; }
    public void addDefense(int amount) { defense += amount; }
    public void damage(int dmg) { health = Math.max(0, health - dmg); }
    public void healing(int hp) { health = Math.min(100, health + hp); }
    public void addItem(String item) { inventory.add(item); }
}