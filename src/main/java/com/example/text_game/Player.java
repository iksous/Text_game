package com.example.text_game;

import java.util.LinkedHashMap;
import java.util.Map;

public class Player {
    private String name;
    private String playerClass;
    private int health;
    private int maxHealth;
    private int gold;
    private Map<String, Integer> inventory; // UPRAVENO: Seznam změněn na Mapu (Item -> Počet)
    private int attackPower;
    private int magicAttack;
    private int mana;
    private int maxMana;
    private int defense;
    private int level;
    private int currentXp;
    private int xpNeeded;

    public Player(String name, String playerClass) {
        this.name = name;
        this.playerClass = playerClass;
        this.inventory = new LinkedHashMap<>(); // LinkedHashMap drží pořadí přidání předmětů
        this.defense = 0;
        this.gold = 20;
        this.level = 1;
        this.currentXp = 0;
        this.xpNeeded = 100;

        if (playerClass.equals("Mage")) {
            this.maxHealth = 80;
            this.health = 80;
            this.maxMana = 50;
            this.mana = 50;
            this.attackPower = 4;
            this.magicAttack = 22;
            this.addItem("Apprentice Wand"); // Používá novou metodu
        } else {
            this.maxHealth = 100;
            this.health = 100;
            this.maxMana = 0;
            this.mana = 0;
            this.attackPower = 12;
            this.magicAttack = 0;
            this.addItem("Rusty Sword"); // Používá novou metodu
        }
    }

    // NOVÉ: Přidání předmětu (pokud existuje, zvýší se počet o 1)
    public void addItem(String item) {
        inventory.put(item, inventory.getOrDefault(item, 0) + 1);
    }

    // NOVÉ: Kontrola, zda hráč předmět vůbec má
    public boolean hasItem(String item) {
        return inventory.getOrDefault(item, 0) > 0;
    }

    // NOVÉ: Odebrání/Snížení počtu předmětu. Pokud klesne na 0, smaže ho úplně.
    public void removeItem(String item) {
        if (inventory.containsKey(item)) {
            int count = inventory.get(item);
            if (count > 1) {
                inventory.put(item, count - 1); // Sníží číslo za x
            } else {
                inventory.remove(item); // Úplně smaže z inventáře
            }
        }
    }

    // UPRAVENO: Použití lektvaru s novým systémem odebrání
    public boolean usePotion() {
        if (hasItem("Healing Potion")) {
            removeItem("Healing Potion");
            healing(40);
            return true;
        }
        return false;
    }

    // UPRAVENO: Použití elixíru s novým systémem odebrání
    public boolean useManaElixir() {
        if (hasItem("Mana Elixir")) {
            removeItem("Mana Elixir");
            this.mana = Math.min(maxMana, this.mana + 30);
            return true;
        }
        return false;
    }

    // Změna návratového typu getteru na Map
    public Map<String, Integer> getInventory() { return inventory; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPlayerClass() { return playerClass; }
    public void setPlayerClass(String playerClass) { this.playerClass = playerClass; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
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
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getCurrentXp() { return currentXp; }
    public void setCurrentXp(int currentXp) { this.currentXp = currentXp; }
    public int getXpNeeded() { return xpNeeded; }
    public void setXpNeeded(int xpNeeded) { this.xpNeeded = xpNeeded; }

    public String addXp(int amount) {
        this.currentXp += amount;
        StringBuilder sb = new StringBuilder();
        while (this.currentXp >= this.xpNeeded) {
            this.level++;
            this.currentXp -= this.xpNeeded;
            this.xpNeeded = (int) (this.xpNeeded * 1.5);

            if (playerClass.equals("Mage")) {
                this.maxHealth += 10; this.maxMana += 15; this.magicAttack += 5; this.attackPower += 1;
                this.health = this.maxHealth; this.mana = this.maxMana;
                sb.append("\n\n[LEVEL UP!] Reached Level ").append(level).append("!\n+10 Max HP, +15 Max MP, +5 Spell Power. Health and Mana restored!");
            } else {
                this.maxHealth += 18; this.attackPower += 4; this.defense += 1;
                this.health = this.maxHealth;
                sb.append("\n\n[LEVEL UP!] Reached Level ").append(level).append("!\n+18 Max HP, +4 Attack, +1 Defense. Health fully restored!");
            }
        }
        return sb.toString();
    }

    public void regenerateManaOnMove() {
        if (playerClass.equals("Mage") && maxMana > 0) {
            int amount = (int) (maxMana * 0.20);
            this.mana = Math.min(maxMana, this.mana + amount);
        }
    }

    public void addGold(int sum) { gold += sum; }
    public void removeGold(int sum) { gold = Math.max(0, gold - sum); }
    public void addAttackPower(int amount) { attackPower += amount; }
    public void addDefense(int amount) { defense += amount; }
    public void damage(int dmg) { health = Math.max(0, health - dmg); }
    public void healing(int hp) { health = Math.min(maxHealth, health + hp); }
}