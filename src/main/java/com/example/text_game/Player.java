package com.example.text_game;

public class Player {
    private String name;
    private int health;
    private int gold;

    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.gold = 20;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int sum) {
        gold += sum;
    }

    public void damage(int dmg) {
        health -= dmg;
    }

    public void healing(int hp) {
        health += hp;
        if (health > 100)
            health = 100;
    }
}
