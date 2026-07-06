package com.example.text_game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;

public class GameWindow {

    private BorderPane root;
    private Label locationName;
    private Label locationDescription;
    private ImageView imageView;
    private HBox buttonLayout;

    // Hotbar components
    private Label characterNameLabel;
    private Label healthLabel;
    private Label manaLabel; // NOVÉ
    private Label goldLabel;
    private Label attackLabel;
    private Label defenseLabel;
    private Label itemsLabel;
    private Label questsLabel;
    private Button usePotionBtn;
    private Button useManaBtn; // NOVÉ

    public GameWindow(Game game) {
        root = new BorderPane();

        // --- CENTER PANEL ---
        locationName = new Label();
        locationName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        locationDescription = new Label();
        locationDescription.setWrapText(true);
        locationDescription.setStyle("-fx-font-size: 14px;");

        imageView = new ImageView();
        imageView.setFitWidth(450);
        imageView.setFitHeight(250);
        imageView.setPreserveRatio(true);

        VBox textContainer = new VBox(10, locationName, locationDescription);
        ScrollPane scrollPane = new ScrollPane(textContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        scrollPane.setPrefHeight(200);

        VBox center = new VBox(15, imageView, scrollPane);
        center.setPadding(new Insets(20));

        // --- RIGHT PANEL (Hotbar) ---
        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 1px;");

        Label sidebarTitle = new Label("Hero Status");
        sidebarTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        characterNameLabel = new Label();
        healthLabel = new Label();
        healthLabel.setStyle("-fx-text-fill: #cc0000; -fx-font-weight: bold;");

        // NOVÉ: Label pro zobrazení Many
        manaLabel = new Label();
        manaLabel.setStyle("-fx-text-fill: #0066cc; -fx-font-weight: bold;");

        goldLabel = new Label();
        goldLabel.setStyle("-fx-text-fill: #b38600; -fx-font-weight: bold;");

        attackLabel = new Label();
        defenseLabel = new Label();
        Label itemsTitle = new Label("Inventory:");
        itemsTitle.setStyle("-fx-font-weight: bold;");
        itemsLabel = new Label();
        itemsLabel.setWrapText(true);

        Label questsTitle = new Label("Quests:");
        questsTitle.setStyle("-fx-font-weight: bold;");
        questsLabel = new Label();
        questsLabel.setWrapText(true);

        usePotionBtn = new Button("Drink Potion (+40 HP)");
        usePotionBtn.setMaxWidth(Double.MAX_VALUE);
        usePotionBtn.setOnAction(e -> {
            if (game.getPlayer().usePotion()) {
                game.setCombatLog("You drank a Healing Potion and restored 40 HP.");
                refresh(game);
            }
        });

        // NOVÉ: Tlačítko pro pití mana elixíru
        useManaBtn = new Button("Drink Mana Elixir (+30 MP)");
        useManaBtn.setMaxWidth(Double.MAX_VALUE);
        useManaBtn.setOnAction(e -> {
            if (game.getPlayer().useManaElixir()) {
                game.setCombatLog("You drank a Mana Elixir and restored 30 MP.");
                refresh(game);
            }
        });

        Button saveBtn = new Button("Save Game");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setOnAction(e -> { game.saveGame(); refresh(game); });

        Button loadBtn = new Button("Load Game");
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setOnAction(e -> { game.loadGame(); refresh(game); });

        sidebar.getChildren().addAll(
                sidebarTitle, characterNameLabel, healthLabel, manaLabel, goldLabel, attackLabel, defenseLabel,
                itemsTitle, itemsLabel, questsTitle, questsLabel, usePotionBtn, useManaBtn, saveBtn, loadBtn
        );

        // --- BOTTOM PANEL ---
        buttonLayout = new HBox(10);
        buttonLayout.setPadding(new Insets(15));
        buttonLayout.setPrefHeight(60);
        buttonLayout.setAlignment(Pos.CENTER_LEFT);

        root.setCenter(center);
        root.setRight(sidebar);
        root.setBottom(buttonLayout);

        refresh(game);
    }

    private void refresh(Game game) {
        String fullDescription = game.getActualLocation().getDescription();
        String currentImagePath = game.getActualLocation().getImage();
        Player p = game.getPlayer();

        if (game.getActualLocation().getName().equals("Forest") && game.getCurrentEnemy() != null) {
            Enemy e = game.getCurrentEnemy();
            if (!e.isDead()) {
                fullDescription += "\n\nMonster: " + e.getName() + " (" + e.getHealth() + "/" + e.getMaxHealth() + " HP)";
                currentImagePath = "images/Goblin.png";
            }
        }

        if (!game.getCombatLog().isEmpty()) {
            fullDescription += "\n\n[Action Log]:\n" + game.getCombatLog();
        }

        locationName.setText(game.getActualLocation().getName());
        locationDescription.setText(fullDescription);
        updateImage(currentImagePath);

        // Hotbar updates
        characterNameLabel.setText("Name: " + p.getName() + " (" + p.getPlayerClass() + ")");
        healthLabel.setText("Health: " + p.getHealth() + " HP");

        // Zobrazování many pouze pro Mága
        if (p.getPlayerClass().equals("Mage")) {
            manaLabel.setVisible(true);
            manaLabel.setManaged(true);
            manaLabel.setText("Mana: " + p.getMana() + "/" + p.getMaxMana() + " MP");
            useManaBtn.setVisible(true);
            useManaBtn.setManaged(true);
            useManaBtn.setDisable(!p.getInventory().contains("Mana Elixir"));
        } else {
            manaLabel.setVisible(false);
            manaLabel.setManaged(false);
            useManaBtn.setVisible(false);
            useManaBtn.setManaged(false);
        }

        goldLabel.setText("Gold: " + p.getGold() + "g");

        if (p.getPlayerClass().equals("Mage")) {
            attackLabel.setText("Spell Power: " + p.getMagicAttack() + " (Melee: " + p.getAttackPower() + ")");
        } else {
            attackLabel.setText("Attack: " + p.getAttackPower());
        }

        defenseLabel.setText("Defense: " + p.getDefense());

        if (p.getInventory().isEmpty()) {
            itemsLabel.setText("(empty)");
        } else {
            itemsLabel.setText("• " + String.join("\n• ", p.getInventory()));
        }

        // Quest states
        switch (game.getQuestState()) {
            case "AVAILABLE": case "INSPECTING": questsLabel.setText("• No active quest"); questsLabel.setStyle("-fx-text-fill: #888888;"); break;
            case "ACTIVE": questsLabel.setText("• Slay the Forest Goblin"); questsLabel.setStyle("-fx-text-fill: #0066cc; -fx-font-weight: bold;"); break;
            case "DONE": questsLabel.setText("• Return to the King!"); questsLabel.setStyle("-fx-text-fill: #009933; -fx-font-weight: bold;"); break;
            case "REWARDED": questsLabel.setText("• Quest completed"); questsLabel.setStyle("-fx-text-fill: #888888;"); break;
        }

        usePotionBtn.setDisable(!p.getInventory().contains("Healing Potion"));
        updateButtons(game);
    }

    private void updateButtons(Game game) {
        buttonLayout.getChildren().clear();
        String currentLoc = game.getActualLocation().getName();
        Player p = game.getPlayer();

        if (currentLoc.equals("Village Square")) {
            Button forestBtn = new Button("Go to Forest");
            forestBtn.setOnAction(e -> { game.goIntoForest(); refresh(game); });
            Button castleBtn = new Button("Go to Castle");
            castleBtn.setOnAction(e -> { game.goIntoCastle(); refresh(game); });
            Button blacksmithBtn = new Button("Visit Blacksmith");
            blacksmithBtn.setOnAction(e -> { game.goIntoBlacksmith(); refresh(game); });
            Button alchemistBtn = new Button("Visit Alchemist");
            alchemistBtn.setOnAction(e -> { game.goIntoAlchemist(); refresh(game); });
            buttonLayout.getChildren().addAll(forestBtn, castleBtn, blacksmithBtn, alchemistBtn);

        } else if (currentLoc.equals("Blacksmith")) {
            Button buySword = new Button("Buy Steel Sword (+8 Atk) [15g]");
            buySword.setDisable(p.getGold() < 15 || p.getInventory().contains("Steel Sword") || p.getPlayerClass().equals("Mage"));
            buySword.setOnAction(e -> { p.removeGold(15); p.addItem("Steel Sword"); p.addAttackPower(8); refresh(game); });

            Button buyArmor = new Button("Buy Plate Armor (+4 Def) [20g]");
            buyArmor.setDisable(p.getGold() < 20 || p.getInventory().contains("Plate Armor"));
            buyArmor.setOnAction(e -> { p.removeGold(20); p.addItem("Plate Armor"); p.addDefense(4); refresh(game); });

            Button backBtn = new Button("Back to Village Square");
            backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });
            buttonLayout.getChildren().addAll(buySword, buyArmor, backBtn);

        } else if (currentLoc.equals("Alchemist")) {
            Button buyPotion = new Button("Buy Healing Potion [10g]");
            buyPotion.setDisable(p.getGold() < 10);
            buyPotion.setOnAction(e -> { p.removeGold(10); p.addItem("Healing Potion"); refresh(game); });

            // NOVÉ: Nákup Mana elixíru u alchymistky
            Button buyMana = new Button("Buy Mana Elixir [10g]");
            buyMana.setDisable(p.getGold() < 10 || p.getPlayerClass().equals("Knight"));
            buyMana.setOnAction(e -> { p.removeGold(10); p.addItem("Mana Elixir"); refresh(game); });

            Button backBtn = new Button("Back to Village Square");
            backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });
            buttonLayout.getChildren().addAll(buyPotion, buyMana, backBtn);

        } else if (currentLoc.equals("Forest")) {
            Enemy enemy = game.getCurrentEnemy();
            if (enemy != null && !enemy.isDead()) {
                Button attackBtn = new Button("Attack");
                attackBtn.setOnAction(e -> {
                    boolean canAttack = true;
                    String log = "";
                    int damageDealt = 0;

                    // Kontrola podmínek pro Mága (Vyžaduje Wand a minimálně 10 Many)
                    if (p.getPlayerClass().equals("Mage")) {
                        boolean hasWand = p.getInventory().stream().anyMatch(item -> item.toLowerCase().contains("wand"));
                        if (!hasWand) {
                            log = "You cannot attack! A Mage requires a Wand to cast spells.";
                            canAttack = false;
                        } else if (p.getMana() < 10) {
                            log = "Not enough Mana! Cast requires 10 MP. Use a Mana Elixir or move to regenerate.";
                            canAttack = false;
                        } else {
                            p.setMana(p.getMana() - 10); // Spotřeba many
                            damageDealt = p.getMagicAttack();
                        }
                    } else {
                        damageDealt = p.getAttackPower();
                    }

                    if (canAttack) {
                        enemy.takeDamage(damageDealt);
                        log = "You dealt " + damageDealt + " damage to the Goblin.";

                        if (enemy.isDead()) {
                            log += "\nYou defeated it! You found " + enemy.getGoldReward() + " gold.";
                            p.addGold(enemy.getGoldReward());
                            if (game.getQuestState().equals("ACTIVE")) game.setQuestState("DONE");
                        } else {
                            int enemyDmg = Math.max(1, enemy.getRandomDamage() - p.getDefense());
                            p.damage(enemyDmg);
                            log += "\nThe Goblin counter-attacked for " + enemyDmg + " damage.";

                            if (p.getHealth() <= 0) {
                                log = "You died! Villagers rescued you, but you lost half your gold.";
                                p.healing(40);
                                p.removeGold(p.getGold() / 2);
                                game.goIntoVillage();
                            }
                        }
                    }
                    game.setCombatLog(log);
                    refresh(game);
                });

                Button fleeBtn = new Button("Flee to Village");
                fleeBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });
                buttonLayout.getChildren().addAll(attackBtn, fleeBtn);
            } else {
                Button searchBtn = new Button("Search for another monster");
                searchBtn.setOnAction(e -> { game.spawnEnemy(); refresh(game); });
                Button backBtn = new Button("Back to Village");
                backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });
                buttonLayout.getChildren().addAll(searchBtn, backBtn);
            }

        } else if (currentLoc.equals("Castle")) {
            if (game.getQuestState().equals("AVAILABLE")) {
                Button inspectBtn = new Button("Inspect Quest");
                inspectBtn.setOnAction(e -> { game.setQuestState("INSPECTING"); game.setCombatLog("The King requests you to slay a Forest Goblin!"); refresh(game); });
                buttonLayout.getChildren().add(inspectBtn);
            } else if (game.getQuestState().equals("INSPECTING")) {
                Button acceptBtn = new Button("Accept Quest");
                acceptBtn.setOnAction(e -> { game.setQuestState("ACTIVE"); game.setCombatLog("Quest Accepted!"); refresh(game); });
                Button waitBtn = new Button("Wait");
                waitBtn.setOnAction(e -> { game.setQuestState("AVAILABLE"); refresh(game); });
                buttonLayout.getChildren().addAll(acceptBtn, waitBtn);
            } else if (game.getQuestState().equals("ACTIVE")) {
                Button waitingBtn = new Button("King is waiting..."); waitingBtn.setDisable(true); buttonLayout.getChildren().add(waitingBtn);
            } else if (game.getQuestState().equals("DONE")) {
                Button claimBtn = new Button("Claim Reward");
                claimBtn.setOnAction(e -> { p.addGold(40); p.addItem("Royal Sword"); p.addAttackPower(15); game.setQuestState("REWARDED"); refresh(game); });
                buttonLayout.getChildren().add(claimBtn);
            }

            Button backBtn = new Button("Back to Village");
            backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });
            buttonLayout.getChildren().add(backBtn);
        }
    }

    private void updateImage(String imagePath) {
        try {
            URL resourceUrl = getClass().getResource("/" + imagePath);
            if (resourceUrl != null) imageView.setImage(new Image(resourceUrl.toExternalForm()));
        } catch (Exception e) { System.out.println("Image error: " + e.getMessage()); }
    }

    public Parent getRoot() { return root; }
}