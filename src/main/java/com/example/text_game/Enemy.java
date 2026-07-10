package com.example.text_game;

import java.util.Random;

public class Enemy {
    private String name;
    private int health;
    private int maxHealth;
    private int minDamage;
    private int maxDamage;
    private int goldReward;
    private int xpReward; // NOVÉ: Zkušenosti, které hráč získá

    public Enemy(String name, int health, int maxHealth, int minDamage, int maxDamage, int goldReward, int xpReward) {
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.goldReward = goldReward;
        this.xpReward = xpReward;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getGoldReward() { return goldReward; }
    public int getXpReward() { return xpReward; } // NOVÉ
    public boolean isDead() { return health <= 0; }

    public void takeDamage(int dmg) {
        this.health = Math.max(0, this.health - dmg);
    }

    public int getRandomDamage() {
        Random r = new Random();
        return r.nextInt((maxDamage - minDamage) + 1) + minDamage;
    }
}