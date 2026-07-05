package com.example.text_game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int health;
    private int gold;
    private List<String> inventory;
    private int attackPower;
    private int defense;

    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.gold = 20;
        this.inventory = new ArrayList<>();
        this.inventory.add("Rusty sword");
        this.attackPower = 12; // Základní útok hráče
        this.defense = 0;     // Základní obrana hráče
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getGold() { return gold; }
    public List<String> getInventory() { return inventory; }
    public int getAttackPower() { return attackPower; }
    public int getDefense() { return defense; }

    public void addGold(int sum) { gold += sum; }
    public void removeGold(int sum) {
        gold -= sum;
        if (gold < 0) gold = 0;
    }

    public void addAttackPower(int amount) { attackPower += amount; }
    public void addDefense(int amount) { defense += amount; }

    public void damage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
    }

    public void healing(int hp) {
        health += hp;
        if (health > 100) health = 100;
    }

    public void addItem(String item) { inventory.add(item); }

    public boolean usePotion() {
        if (inventory.contains("Health potion")) {
            inventory.remove("Health potion");
            healing(40); // Lektvar vyléčí 40 HP
            return true;
        }
        return false;
    }
}