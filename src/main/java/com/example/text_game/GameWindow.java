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
    private Label goldLabel;
    private Label attackLabel;
    private Label defenseLabel;
    private Label itemsLabel;
    private Label questsLabel;
    private Button usePotionBtn;

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

        VBox textContainer = new VBox(10);
        textContainer.getChildren().addAll(locationName, locationDescription);

        ScrollPane scrollPane = new ScrollPane(textContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        scrollPane.setPrefHeight(200);

        VBox center = new VBox(15);
        center.setPadding(new Insets(20));
        center.getChildren().addAll(imageView, scrollPane);

        // --- RIGHT PANEL (Hotbar) ---
        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 1px;");

        Label sidebarTitle = new Label("Hero Status");
        sidebarTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        characterNameLabel = new Label(); // Aktualizuje se dynamicky v refresh()
        healthLabel = new Label();
        healthLabel.setStyle("-fx-text-fill: #cc0000; -fx-font-weight: bold;");

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

        // NOVÉ: Tlačítka pro Save a Load systému
        Button saveBtn = new Button("Save Game");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setStyle("-fx-background-color: #e1f5fe; -fx-border-color: #b3e5fc;");
        saveBtn.setOnAction(e -> {
            game.saveGame();
            refresh(game);
        });

        Button loadBtn = new Button("Load Game");
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setStyle("-fx-background-color: #efebe9; -fx-border-color: #d7ccc8;");
        loadBtn.setOnAction(e -> {
            game.loadGame();
            refresh(game);
        });

        sidebar.getChildren().addAll(
                sidebarTitle, characterNameLabel, healthLabel, goldLabel, attackLabel, defenseLabel,
                itemsTitle, itemsLabel, questsTitle, questsLabel, usePotionBtn, saveBtn, loadBtn
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
        characterNameLabel.setText("Name: " + game.getPlayer().getName()); // Zde opraveno pro správný refresh jména
        healthLabel.setText("Health: " + game.getPlayer().getHealth() + " HP");
        goldLabel.setText("Gold: " + game.getPlayer().getGold() + "g");
        attackLabel.setText("Attack: " + game.getPlayer().getAttackPower());
        defenseLabel.setText("Defense: " + game.getPlayer().getDefense());

        if (game.getPlayer().getInventory().isEmpty()) {
            itemsLabel.setText("(empty)");
        } else {
            itemsLabel.setText("• " + String.join("\n• ", game.getPlayer().getInventory()));
        }

        // Quest updates
        switch (game.getQuestState()) {
            case "AVAILABLE":
            case "INSPECTING":
                questsLabel.setText("• No active quest");
                questsLabel.setStyle("-fx-text-fill: #888888;");
                break;
            case "ACTIVE":
                questsLabel.setText("• Slay the Forest Goblin");
                questsLabel.setStyle("-fx-text-fill: #0066cc; -fx-font-weight: bold;");
                break;
            case "DONE":
                questsLabel.setText("• Return to the King!");
                questsLabel.setStyle("-fx-text-fill: #009933; -fx-font-weight: bold;");
                break;
            case "REWARDED":
                questsLabel.setText("• No active quest (All done)");
                questsLabel.setStyle("-fx-text-fill: #888888;");
                break;
        }

        usePotionBtn.setDisable(!game.getPlayer().getInventory().contains("Healing Potion"));

        updateButtons(game);
    }

    private void updateButtons(Game game) {
        buttonLayout.getChildren().clear();
        String currentLoc = game.getActualLocation().getName();

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
            Button buySword = new Button("Buy Steel Sword (+8 Attack) [15g]");
            buySword.setDisable(game.getPlayer().getGold() < 15 || game.getPlayer().getInventory().contains("Steel Sword"));
            buySword.setOnAction(e -> {
                game.getPlayer().removeGold(15);
                game.getPlayer().addItem("Steel Sword");
                game.getPlayer().addAttackPower(8);
                refresh(game);
            });

            Button buyArmor = new Button("Buy Plate Armor (+4 Defense) [20g]");
            buyArmor.setDisable(game.getPlayer().getGold() < 20 || game.getPlayer().getInventory().contains("Plate Armor"));
            buyArmor.setOnAction(e -> {
                game.getPlayer().removeGold(20);
                game.getPlayer().addItem("Plate Armor");
                game.getPlayer().addDefense(4);
                refresh(game);
            });

            Button backBtn = new Button("Back to Village Square");
            backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });

            buttonLayout.getChildren().addAll(buySword, buyArmor, backBtn);

        } else if (currentLoc.equals("Alchemist")) {
            Button buyPotion = new Button("Buy Healing Potion [10g]");
            buyPotion.setDisable(game.getPlayer().getGold() < 10);
            buyPotion.setOnAction(e -> {
                game.getPlayer().removeGold(10);
                game.getPlayer().addItem("Healing Potion");
                refresh(game);
            });

            Button backBtn = new Button("Back to Village Square");
            backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });

            buttonLayout.getChildren().addAll(buyPotion, backBtn);

        } else if (currentLoc.equals("Forest")) {
            Enemy enemy = game.getCurrentEnemy();
            if (enemy != null && !enemy.isDead()) {
                Button attackBtn = new Button("Attack");
                attackBtn.setOnAction(e -> {
                    int playerDmg = game.getPlayer().getAttackPower();
                    enemy.takeDamage(playerDmg);
                    String log = "You dealt " + playerDmg + " damage to the Goblin.";

                    if (enemy.isDead()) {
                        log += "\nYou defeated it! You found " + enemy.getGoldReward() + " gold.";
                        game.getPlayer().addGold(enemy.getGoldReward());

                        if (game.getQuestState().equals("ACTIVE")) {
                            game.setQuestState("DONE");
                            log += "\n[Quest Updated: Return to the King for your reward!]";
                        }
                    } else {
                        int enemyDmg = Math.max(1, enemy.getRandomDamage() - game.getPlayer().getDefense());
                        game.getPlayer().damage(enemyDmg);
                        log += "\nThe Goblin counter-attacked for " + enemyDmg + " damage.";

                        if (game.getPlayer().getHealth() <= 0) {
                            log = "You died in battle! Villagers rescued you, but you lost half your gold.";
                            game.getPlayer().healing(40);
                            game.getPlayer().removeGold(game.getPlayer().getGold() / 2);
                            game.goIntoVillage();
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
                inspectBtn.setOnAction(e -> {
                    game.setQuestState("INSPECTING");
                    game.setCombatLog("The King says: 'Brave hero, dangerous goblins are terrorizing the forest! Slay one of them and I shall reward you handsomely.'");
                    refresh(game);
                });
                buttonLayout.getChildren().add(inspectBtn);

            } else if (game.getQuestState().equals("INSPECTING")) {
                Button acceptBtn = new Button("Accept Quest");
                acceptBtn.setOnAction(e -> {
                    game.setQuestState("ACTIVE");
                    game.setCombatLog("Quest Accepted! Go to the Forest and defeat the goblin.");
                    refresh(game);
                });

                Button waitBtn = new Button("Wait");
                waitBtn.setOnAction(e -> {
                    game.setQuestState("AVAILABLE");
                    game.setCombatLog("You decided to wait. The King looks disappointed.");
                    refresh(game);
                });

                buttonLayout.getChildren().addAll(acceptBtn, waitBtn);

            } else if (game.getQuestState().equals("ACTIVE")) {
                Button waitingBtn = new Button("King is waiting...");
                waitingBtn.setDisable(true);
                buttonLayout.getChildren().add(waitingBtn);

            } else if (game.getQuestState().equals("DONE")) {
                Button claimBtn = new Button("Claim Reward");
                claimBtn.setOnAction(e -> {
                    game.setQuestState("REWARDED");
                    game.getPlayer().addGold(40);
                    game.getPlayer().addItem("Royal Sword");
                    game.getPlayer().addAttackPower(15);
                    game.setCombatLog("The King cheers: 'Thank you, savior! Here is your reward: 40 gold and my personal Royal Sword!'");
                    refresh(game);
                });
                buttonLayout.getChildren().add(claimBtn);

            } else if (game.getQuestState().equals("REWARDED")) {
                Button talkBtn = new Button("Talk to King");
                talkBtn.setOnAction(e -> {
                    game.setCombatLog("The King says: 'My kingdom is forever in your debt, hero.'");
                    refresh(game);
                });
                buttonLayout.getChildren().add(talkBtn);
            }

            Button backBtn = new Button("Back to Village");
            backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });
            buttonLayout.getChildren().add(backBtn);
        }
    }

    private void updateImage(String imagePath) {
        try {
            URL resourceUrl = getClass().getResource("/" + imagePath);
            if (resourceUrl != null) {
                Image image = new Image(resourceUrl.toExternalForm());
                imageView.setImage(image);
            }
        } catch (Exception e) {
            System.out.println("Image error: " + e.getMessage());
        }
    }

    public Parent getRoot() { return root; }
}