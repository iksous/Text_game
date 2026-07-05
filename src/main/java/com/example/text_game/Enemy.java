package com.example.text_game;

import java.util.Random;

public class Enemy {
    private String name;
    private int health;
    private int maxHealth;
    private int minDmg;
    private int maxDmg;
    private int goldReward;

    public Enemy(String name, int minHp, int maxHp, int minDmg, int maxDmg, int minGold, int maxGold) {
        Random r = new Random();
        this.name = name;
        this.maxHealth = r.nextInt(maxHp - minHp + 1) + minHp;
        this.health = this.maxHealth;
        this.minDmg = minDmg;
        this.maxDmg = maxDmg;
        this.goldReward = r.nextInt(maxGold - minGold + 1) + minGold;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getGoldReward() { return goldReward; }

    public void takeDamage(int dmg) {
        this.health -= dmg;
        if (this.health < 0) this.health = 0;
    }

    public int getRandomDamage() {
        return new Random().nextInt(maxDmg - minDmg + 1) + minDmg;
    }

    public boolean isDead() {
        return health <= 0;
    }
}