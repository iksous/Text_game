package com.example.text_game;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private HBox buttonLayout; // Dynamický kontejner na tlačítka

    // Hotbar komponenty
    private Label characterNameLabel;
    private Label healthLabel;
    private Label goldLabel;
    private Label attackLabel;
    private Label defenseLabel;
    private Label itemsLabel;
    private Button usePotionBtn;

    public GameWindow(Game game) {
        root = new BorderPane();

        // --- STŘEDOVÝ PANEL ---
        locationName = new Label();
        locationName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        locationDescription = new Label();
        locationDescription.setWrapText(true);
        locationDescription.setStyle("-fx-font-size: 14px;");

        imageView = new ImageView();
        imageView.setFitWidth(450);
        imageView.setPreserveRatio(true);

        VBox center = new VBox(15);
        center.setPadding(new Insets(20));
        center.getChildren().addAll(imageView, locationName, locationDescription);

        // --- PRAVÝ PANEL (Hotbar) ---
        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 1px;");

        Label sidebarTitle = new Label("Hero");
        sidebarTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        characterNameLabel = new Label("Name: " + game.getPlayer().getName());
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

        // Akční tlačítko pro rychlé použití lektvaru z inventáře
        usePotionBtn = new Button("Drink potion (+40 HP)");
        usePotionBtn.setMaxWidth(Double.MAX_VALUE);
        usePotionBtn.setOnAction(e -> {
            if (game.getPlayer().usePotion()) {
                if (game.getActualLocation().getName().equals("Forest") && game.getCurrentEnemy() != null && !game.getCurrentEnemy().isDead()) {
                    game.setCombatLog("You drank your health potion and regained some HP.");
                }
                refresh(game);
            }
        });

        sidebar.getChildren().addAll(sidebarTitle, characterNameLabel, healthLabel, goldLabel, attackLabel, defenseLabel, itemsTitle, itemsLabel, usePotionBtn);

        // --- SPODNÍ PANEL (Tlačítka) ---
        buttonLayout = new HBox(10);
        buttonLayout.setPadding(new Insets(15));

        root.setCenter(center);
        root.setRight(sidebar);
        root.setBottom(buttonLayout);

        // Prvotní načtení dat hry
        refresh(game);
    }

    private void refresh(Game game) {
        String fullDescription = game.getActualLocation().getDescription();

        // TADY: Zjistíme výchozí obrázek aktuální lokace
        String currentImagePath = game.getActualLocation().getImage();

        if (game.getActualLocation().getName().equals("Forest") && game.getCurrentEnemy() != null) {
            Enemy e = game.getCurrentEnemy();
            if (!e.isDead()) {
                fullDescription += "\n\nMonster: " + e.getName() + " (" + e.getHealth() + "/" + e.getMaxHealth() + " HP)";

                // TADY: Pokud nepřítel žije, změníme obrázek lesa na obrázek goblina
                currentImagePath = "images/Goblin.png";
            }
        }

        if (!game.getCombatLog().isEmpty()) {
            fullDescription += "\n\n[Action]:\n" + game.getCombatLog();
        }

        locationName.setText(game.getActualLocation().getName());
        locationDescription.setText(fullDescription);

        // TADY: Předáme metodě výsledný obrázek (buď lokaci, nebo goblina)
        updateImage(currentImagePath);

        // Obnova hotbaru
        healthLabel.setText("Health: " + game.getPlayer().getHealth() + " HP");
        goldLabel.setText("Gold: " + game.getPlayer().getGold() + "g");
        attackLabel.setText("Attack: " + game.getPlayer().getAttackPower());
        defenseLabel.setText("Defense: " + game.getPlayer().getDefense());

        if (game.getPlayer().getInventory().isEmpty()) {
            itemsLabel.setText("(blank)");
        } else {
            itemsLabel.setText("• " + String.join("\n• ", game.getPlayer().getInventory()));
        }

        usePotionBtn.setDisable(!game.getPlayer().getInventory().contains("Health potion"));

        updateButtons(game);
    }
    private void updateButtons(Game game) {
        buttonLayout.getChildren().clear();
        String currentLoc = game.getActualLocation().getName();

        if (currentLoc.equals("Village Square")) {
            Button forestBtn = new Button("Go into Forest");
            forestBtn.setOnAction(e -> { game.goIntoForest(); refresh(game); });

            Button castleBtn = new Button("Go into Castle");
            castleBtn.setOnAction(e -> { game.goIntoCastle(); refresh(game); });

            Button blacksmithBtn = new Button("Visit Blacksmith");
            blacksmithBtn.setOnAction(e -> { game.goIntoBlacksmith(); refresh(game); });

            Button alchemistBtn = new Button("Visit Alchemist");
            alchemistBtn.setOnAction(e -> { game.goIntoAlchemist(); refresh(game); });

            buttonLayout.getChildren().addAll(forestBtn, castleBtn, blacksmithBtn, alchemistBtn);

        } else if (currentLoc.equals("Blacksmith")) {
            Button buySword = new Button("Buy steel sword (+8 Attack) [15g]");
            buySword.setDisable(game.getPlayer().getGold() < 15 || game.getPlayer().getInventory().contains("Steel sword"));
            buySword.setOnAction(e -> {
                game.getPlayer().removeGold(15);
                game.getPlayer().addItem("Steel sword");
                game.getPlayer().addAttackPower(8);
                refresh(game);
            });

            Button buyArmor = new Button("Buy plate armor (+4 Defence) [20g]");
            buyArmor.setDisable(game.getPlayer().getGold() < 20 || game.getPlayer().getInventory().contains("Plate armor"));
            buyArmor.setOnAction(e -> {
                game.getPlayer().removeGold(20);
                game.getPlayer().addItem("Plate armor");
                game.getPlayer().addDefense(4);
                refresh(game);
            });

            Button backBtn = new Button("Back to town square");
            backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });

            buttonLayout.getChildren().addAll(buySword, buyArmor, backBtn);

        } else if (currentLoc.equals("Alchemist")) {
            Button buyPotion = new Button("Buy Health potion [10g]");
            buyPotion.setDisable(game.getPlayer().getGold() < 10);
            buyPotion.setOnAction(e -> {
                game.getPlayer().removeGold(10);
                game.getPlayer().addItem("Health potion");
                refresh(game);
            });

            Button backBtn = new Button("Back to town square");
            backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });

            buttonLayout.getChildren().addAll(buyPotion, backBtn);

        } else if (currentLoc.equals("Forest")) {
            Enemy enemy = game.getCurrentEnemy();
            if (enemy != null && !enemy.isDead()) {
                Button attackBtn = new Button("Attack");
                attackBtn.setOnAction(e -> {
                    // Tah hráče
                    int playerDmg = game.getPlayer().getAttackPower();
                    enemy.takeDamage(playerDmg);
                    String log = "You hurt your enemy for " + playerDmg + " dmg.";

                    if (enemy.isDead()) {
                        log += "\nYou defeated him! You earned " + enemy.getGoldReward() + " gold.";
                        game.getPlayer().addGold(enemy.getGoldReward());

                        // Kontrola Questu
                        if (game.getQuestState().equals("ACTIVE")) {
                            game.setQuestState("DONE");
                            log += "\n[Quest done. Come back to king for your reward.]";
                        }
                    } else {
                        // Tah nepřítele (bere v úvahu obranu hráče)
                        int enemyDmg = Math.max(1, enemy.getRandomDamage() - game.getPlayer().getDefense());
                        game.getPlayer().damage(enemyDmg);
                        log += "\nGoblin damaged you for " + enemyDmg + " dmg (after defence subtracted).";

                        // Kontrola smrti hráče
                        if (game.getPlayer().getHealth() <= 0) {
                            log = "You died. Villagers saved you but your gold has been halved";
                            game.getPlayer().healing(40); // Oživení s 40 HP
                            game.getPlayer().removeGold(game.getPlayer().getGold() / 2);
                            game.goIntoVillage();
                        }
                    }
                    game.setCombatLog(log);
                    refresh(game);
                });

                Button fleeBtn = new Button("Run back into the village");
                fleeBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });

                buttonLayout.getChildren().addAll(attackBtn, fleeBtn);
            } else {
                // Pokud je nepřítel mrtvý
                Button searchBtn = new Button("Travel deeper into the forest");
                searchBtn.setOnAction(e -> { game.spawnEnemy(); refresh(game); });

                Button backBtn = new Button("Back to the village");
                backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });

                buttonLayout.getChildren().addAll(searchBtn, backBtn);
            }

        } else if (currentLoc.equals("Castle")) {
            Button questActionBtn = new Button();

            switch (game.getQuestState()) {
                case "AVAILABLE":
                    questActionBtn.setText("Talk with the king (Accept quest)");
                    questActionBtn.setOnAction(e -> {
                        game.setQuestState("ACTIVE");
                        game.setCombatLog("The king says: 'Brave hero, dangerous goblins are wreaking havoc in the forest! Kill one of them, and I will reward you.'");
                        refresh(game);
                    });
                    break;
                case "ACTIVE":
                    questActionBtn.setText("King waits for the finished quest...");
                    questActionBtn.setDisable(true);
                    break;
                case "DONE":
                    questActionBtn.setText("Submit the quest");
                    questActionBtn.setOnAction(e -> {
                        game.setQuestState("REWARDED");
                        game.getPlayer().addGold(40);
                        game.getPlayer().addItem("Royal sword");
                        game.getPlayer().addAttackPower(15); // Výrazný bonus k útoku
                        game.setCombatLog("King says: 'Thank you! Here is your reward: 40 golds a my personal Royal sword!'");
                        refresh(game);
                    });
                    break;
                case "REWARDED":
                    questActionBtn.setText("Talk with the king");
                    questActionBtn.setOnAction(e -> {
                        game.setCombatLog("King says: 'My kingdom will be forever greatful hero.'");
                        refresh(game);
                    });
                    break;
            }

            Button backBtn = new Button("Back into village");
            backBtn.setOnAction(e -> { game.goIntoVillage(); refresh(game); });

            buttonLayout.getChildren().addAll(questActionBtn, backBtn);
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
            System.out.println("Picture error: " + e.getMessage());
        }
    }

    public Parent getRoot() { return root; }
}