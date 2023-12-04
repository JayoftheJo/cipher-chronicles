package views;

import AdventureModel.AdventureObject;
import AdventureModel.Player;
import AdventureModel.State.Token;
import BossFactory.concreteBossFactory;
import AdventureModel.AdventureGame;
import BossFactory.trollBoss;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import views.bars.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class BossView.
 *
 * This is the Class that will visualize the battle system
 * It follows the similar structure of AdventureView except
 * for a few GUI changes
 */
public class BossView extends AdventureGameView{

    Button bossHelp, healButton, attackButton, specAttackButton;
    trollBoss bossTroll;
    Random rand;
    Player finalPlayer;
    ImageView round_img_v, heal_img, attack_img, spec_img;
    Alert round, defeat_alert, victory_alert, intro_alert;
    PauseTransition ability_pause;
    private MediaPlayer mediaPlayer, abilityPlayer;
    Media ability_sound;
    boolean boss_helpToggle = false;
    int p_damage;
    double round_num = 1.0;


    boolean playerStatsToggle = false; //to know if health bar is on or off
    BarView healthBar; // to access the health bar
    BarView strengthBar; // to access the strength bar

    VBox playerStats; // holds the player stats


    BarView bossHealthBar;

    BarView bossStrengthBar;

    VBox bossStats;

    boolean invincible;    // for invincibility

    int invRoundNum; // num rounds invincible for

    boolean luckySpec = false;

    /**
     * BossView Constructor.
     * @param model the game we are playing
     * @param stage the window we are using
     * @throws IOException
     */
    public BossView(AdventureGame model, Stage stage) throws IOException {
        super(model, stage);
        rand = new Random();
        model.setHelpText(parseOtherFile("boss_help"));
        invRoundNum = 0;
        invincible = false;
    }

    @Override
    public void intiUI(){
        // setting up the stage
        this.stage.setTitle("mejiadal's Adventure Game");

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        this.gridPane.setPadding(new Insets(20));
        this.gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );


        this.gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        this.gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        bossHelp = new Button("Instructions");
        bossHelp.setId("Instructions");
        customizeButton(bossHelp);
        makeButtonAccessible(bossHelp, "Instruction Button", "Instructions for the player",
                "This button allows the player to view the instructions to defeat the troll");
        bossHelp.setOnAction(e -> {
            showInstructions();
        });

        healButton = new Button("Heal");
        healButton.setId("Heal");
        heal_img = new ImageView(new Image(this.model.getDirectoryName() + "/battleImages/" + "heal.png"));
        heal_img.setFitHeight(75);
        heal_img.setPreserveRatio(true);
        healButton.setGraphic(heal_img);
        customizeButton(healButton);
        makeButtonAccessible(healButton, "Heal Button", "Heals the player", "This button " +
                " the player if they have health tokens available");

        attackButton = new Button("Attack");
        attackButton.setId("Attack");
        attack_img = new ImageView(new Image(this.model.getDirectoryName() + "/battleImages/" + "attack.png"));
        attack_img.setFitHeight(75);
        attack_img.setPreserveRatio(true);
        attackButton.setGraphic(attack_img);
        customizeButton(attackButton);
        makeButtonAccessible(attackButton, "Attack Button", "Unleash a normal attack", "This " +
                "button allows the user to use a normal attack on the troll");

        specAttackButton = new Button("Special Attack");
        specAttackButton.setId("Special Attack");
        spec_img = new ImageView(new Image(this.model.getDirectoryName() + "/battleImages/" + "specattack.png"));
        spec_img.setFitHeight(75);
        spec_img.setPreserveRatio(true);
        specAttackButton.setGraphic(spec_img);
        customizeButton(specAttackButton);
        makeButtonAccessible(specAttackButton, "Special Attack Button", "Unleash a special attack",
                "This button allows the user to use a special attack on the troll if they have attack tokens " +
                        "available");

        HBox abilityButtons = new HBox();
        abilityButtons.getChildren().addAll(healButton, attackButton, specAttackButton);
        abilityButtons.setSpacing(10);
        abilityButtons.setAlignment(Pos.CENTER);

        specAttackButton.setDisable(true); // only activated with tokens

        healButton.setOnAction(event -> heal_handle());
        attackButton.setOnAction(event -> attack_handle());
        specAttackButton.setOnAction(event -> specAttack_handle());

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        concreteBossFactory factory = new concreteBossFactory();
        finalPlayer = this.model.getPlayer();
        bossTroll = (trollBoss) factory.createBossCharacter();

        updateObjs();

        String bossImg = this.model.getDirectoryName() + "/battleImages/" + "normalBoss.png";
        bossTroll.charImage = new Image(bossImg);
        bossTroll.charImageview = new ImageView(bossTroll.charImage);

        String round_file = this.model.getDirectoryName() + "/battleImages/" + "swords.png";
        Image round_img = new Image(round_file);
        round_img_v = new ImageView(round_img);
        round_img_v.setFitHeight(35);
        round_img_v.setPreserveRatio(true);


        playerStats = new VBox();
        playerStats.setSpacing(10);
        playerStats.setAlignment(Pos.CENTER_LEFT);
        // event for hiding or opening the health bar
        playerStatsEvent();
        showPlayerStats();

        bossStats = new VBox();
        bossStats.setSpacing(10);
        bossStats.setAlignment(Pos.CENTER_LEFT);

        bossHealthBar = new BossHealthBarView(bossTroll, this);// boss health and strength bar
        bossStrengthBar = new BossStrengthBarView(bossTroll, this);

        bossStats.getChildren().addAll(bossHealthBar.get(), bossStrengthBar.get());

        this.gridPane.add(bossStats, 2, 0);//top left

        bossTroll.setHealthBar(bossHealthBar);
        bossTroll.setStrengthBar(bossStrengthBar);

        this.gridPane.add(abilityButtons, 1, 2, 1, 2); //add ability buttons
        this.gridPane.add(bossHelp, 0, 0);
        this.gridPane.add(bossTroll.charImageview, 1, 1);
        GridPane.setHalignment(bossTroll.charImageview, HPos.CENTER);
        GridPane.setValignment(bossTroll.charImageview, VPos.CENTER);
        GridPane.setValignment(bossHelp, VPos.TOP);
        GridPane.setValignment(invLabel, VPos.BOTTOM);

        updateItems();

        // Render everything
        var scene = new Scene(this.gridPane,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

        // creating a looped background music for the entirety of the battle
        String bgrnd_music = this.model.getDirectoryName() + "/sounds/" + "bgrnd_music.mp3";
        Media sound = new Media(new File(bgrnd_music).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Set the music to loop continuously
        mediaPlayer.play();


        // Alerting the intro message to the boss room
        Platform.runLater(() -> {
            intro_alert = new Alert(Alert.AlertType.INFORMATION);
            ImageView intro_img = new ImageView(bossTroll.charImage);
            intro_img.setFitHeight(50);
            intro_img.setPreserveRatio(true);
            intro_alert.setGraphic(intro_img);
            intro_alert.setHeaderText("HAHAHAHAHAHA!");
            intro_alert.setContentText("YOU DARE TO CHALLENGE ME, PUNY ADVENTURER? YOUR JOURNEY ENDS HERE! PREPARE TO " +
                    "BE CRUSHED BENEATH MY MIGHT, FOR I AM THE GUARDIAN OF THESE LANDS, AND NONE SHALL PASS!");
            intro_alert.getButtonTypes().clear();
            intro_alert.getButtonTypes().addAll(ButtonType.OK);
            DialogPane dialogPane = intro_alert.getDialogPane();
            dialogPane.setPrefWidth(500);
            dialogPane.setPrefHeight(200);
            intro_alert.showAndWait();
            open_buttons();
        });
    }

    @Override
    public void showInstructions() {
        if (boss_helpToggle == Boolean.FALSE) {
            String help = model.getInstructions();
            Label helpLabel = new Label(help);
            helpLabel.setWrapText(true);
            helpLabel.setStyle("-fx-text-fill: white;");
            helpLabel.setFont(new Font("Arial", 16));
            helpLabel.setAlignment(Pos.CENTER);

            ScrollPane helpPane = new ScrollPane();
            helpPane.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            helpPane.setContent(helpLabel);
            helpPane.setFitToWidth(true);

            removeByCell(1, 1);
            this.gridPane.add(helpPane, 1, 1);

            boss_helpToggle = true;
        }
        else {
            removeByCell(1, 1);
            gridPane.add(bossTroll.charImageview, 1, 1);
            boss_helpToggle = false;
        }
    }

    /*
     * This method checks whether the battle has ended
     * based on the player and boss's health
     */
    private void check_status(){
        if (finalPlayer.getHealth() <= 0) {
            close_buttons();
            bossHelp.setDisable(true);
            defeat();
        }
        else if (bossTroll.bossHealth <= 0){
            close_buttons();
            bossHelp.setDisable(true);
            victory();
        }
        round_num += 0.5;
    }

    /*
     * This method handles when the heal
     * button has been clicked
     */
    private void heal_handle() {
        //creating sound for each ability effect
        String attack_music = this.model.getDirectoryName() + "/sounds/" + "heal.mp3";
        ability_sound = new Media(new File(attack_music).toURI().toString());
        abilityPlayer = new MediaPlayer(ability_sound);
        abilityPlayer.play();

        close_buttons();
        playerHeal();
        boss_move();
        check_status();
    }

    /*
     * This method handles when the attack
     * button has been clicked
     */
    private void attack_handle() {
        //creating sound for each ability effect
        String attack_music = this.model.getDirectoryName() + "/sounds/" + "attack.mp3";
        ability_sound = new Media(new File(attack_music).toURI().toString());
        abilityPlayer = new MediaPlayer(ability_sound);
        abilityPlayer.play();

        close_buttons();
        playerAttack();
        boss_move();
        check_status();
    }


    /*
     * Makes this BossView their view
     */
    private void updateObjs(){
        objectsInRoom.getChildren().clear();

        for (AdventureObject object: finalPlayer.inventory){
            object.getState().setView(this);
        }
    }

    /*
     * This method handles when the special attack
     * button has been clicked
     */
    private void specAttack_handle() {
        //creating sound for each ability effect
        String attack_music = this.model.getDirectoryName() + "/sounds/" + "specAttack.mp3";
        ability_sound = new Media(new File(attack_music).toURI().toString());
        abilityPlayer = new MediaPlayer(ability_sound);
        abilityPlayer.play();

        close_buttons();
        playerSpec();
        boss_move();
        check_status();
    }

    /*
     * This method lets the player attack
     * the enemy boss
     */
    private void playerAttack(){
        p_damage = rand.nextInt(finalPlayer.getStrength(), finalPlayer.getStrength() + 50);
        bossTroll.changeHealthBar(-p_damage);
    }

    /*
     * This method lets the player heal themselves
     */
    private void playerHeal(){
        finalPlayer.changeHealthBar(25);
//        if (finalPlayer.getHealth() > 100){
//            finalPlayer.changeHealthBar(-finalPlayer.getHealth() % 100);
//        }
    }

    /*
     * This method lets the player unleash
     * a special attack on the enemy boss
     */
    private void playerSpec(){
        p_damage = rand.nextInt(finalPlayer.getStrength(), finalPlayer.getStrength() * 15);
        bossTroll.changeHealthBar(-p_damage);

        // Special attack the noraml way
        if(!luckySpec) {
            p_damage = rand.nextInt(finalPlayer.getStrength(), (finalPlayer.getStrength()+1) * 15);
            bossTroll.changeHealthBar(-p_damage);
        }
        // or in the lucky way
        else{
            int output = rand.nextInt(0, 5);
            // luck wins out then user causes a potentially big damage
            if(output == 4){
                p_damage = rand.nextInt(finalPlayer.getStrength(), (finalPlayer.getStrength()+1) * 15);
                bossTroll.changeHealthBar(-p_damage);
                luckySpec = false;
            }
            // luck loses out, no attack from user, and user get the failure indicated with a red flicker
            else{
                String prev = specAttackButton.getStyle();
                specAttackButton.setStyle("-fx-background-colour: #8B0000;");
                PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
                pause.setOnFinished(actionEvent -> {
                    customizeButton(specAttackButton);
                    makeButtonAccessible(specAttackButton, "Special Attack Button", "Unleash a special " +
                            "attack", "This button allows the user to use a special attack on the troll if " +
                            "they have attack tokens available");

                });
                pause.play();
            }
        }
        this.strengthBar.initState();
        specAttackButton.setDisable(true);
    }

    /*
     * This method is used to enable the ability buttons
     */
    private void open_buttons(){
        attackButton.setDisable(false);
        healButton.setDisable(false);

        if (!specAttackButton.isDisabled()){
        specAttackButton.setDisable(false);
        }

        for (Node button: objectsInInventory.getChildren()){
            button.setDisable(false);
        }

    }

    /*
     * This method is used to disable the ability buttons
     */
    private void close_buttons(){
        attackButton.setDisable(true);
        healButton.setDisable(true);
        specAttackButton.setDisable(true);

        for (Node button: objectsInInventory.getChildren()){
            button.setDisable(true);
        }
    }

    /*
     * This method is used to initiate the boss's move
     * where they can either attack or heal based on
     * a random number between 0 and 50
     */
    private void boss_move(){
        int move = rand.nextInt(0,50);
        int boss_dmg;
        if (move > 10){

            // can only attack a non-invincible player
            if(!invincible){
                boss_dmg = bossTroll.attack(finalPlayer);
            } else {
                boss_dmg = 0;
            }
        }
        else{
            boss_dmg = 0;
            bossTroll.heal();
            bossTroll.changeStrengthBar(5);
        }

        // Keep track of how many rounds of invincibility
        if (invRoundNum >= 3){
            invincible = false; // after 3 the invincibility wears off
            invRoundNum = 0;
        }
        else{
            invRoundNum += 1;
        }
        check_status();
        Platform.runLater(() -> {
            round = new Alert(Alert.AlertType.INFORMATION);
            round.setHeaderText("ROUND " + round_num);
            round.setGraphic(round_img_v);
            round.setContentText(round_text(boss_dmg));
            round.getButtonTypes().clear();
            round.getButtonTypes().addAll(ButtonType.OK);
            round.showAndWait();
            open_buttons();
        });
    }

    /*
     * This method is for when the player loses the battle
     * Window closes
     */
    private void defeat() {
        // stop the background music
        mediaPlayer.stop();

        //cue the victory music
        String defeat_music = this.model.getDirectoryName() + "/sounds/" + "defeat_music.mp3";
        Media sound = new Media(new File(defeat_music).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        bossTroll.charImageview = new ImageView(new Image(this.model.getDirectoryName() + "/battleImages/" +
                    "defeatBoss.png"));
        bossTroll.charImageview.setFitHeight(500);
        bossTroll.charImageview.setPreserveRatio(true);
        removeByCell(1, 1);
        this.gridPane.add(bossTroll.charImageview, 1, 1);
        GridPane.setHalignment(bossTroll.charImageview, HPos.RIGHT);

        //Pause for alert
        PauseTransition def_pause = new PauseTransition(Duration.seconds(3));
        def_pause.setOnFinished(event -> {
        });
        def_pause.play();

        //Run the defeat message after the pause
        Platform.runLater(() -> {
                    defeat_alert = new Alert(Alert.AlertType.INFORMATION);
                    ImageView defeat_img = new ImageView(new Image(this.model.getDirectoryName() + "/battleImages/" +
                    "defeatBoss.png"));
                    defeat_img.setFitHeight(50);
                    defeat_img.setPreserveRatio(true);
                    defeat_alert.setGraphic(defeat_img);
                    defeat_alert.setHeaderText("I WILL GET YOU NEXT TIME!");
                    defeat_alert.setContentText("Pathetic adventurer, you were no match for my strength. Your feeble attempts to " +
                            "challenge me have come to an end. This land shall remain under my dominion, and your defeat serves as " +
                            "a warning to any who dare cross my path.");
                    defeat_alert.getButtonTypes().clear();
                    defeat_alert.getButtonTypes().addAll(ButtonType.OK);
                    DialogPane dialogPane = defeat_alert.getDialogPane();
                    dialogPane.setPrefWidth(600);
                    dialogPane.setPrefHeight(200);
                    defeat_alert.showAndWait();
                });

        //Exit the game after 10 seconds
        PauseTransition end_pause = new PauseTransition(Duration.seconds(10));
        end_pause.setOnFinished(event -> {
            Platform.exit();
        });
        end_pause.play();
    }

    /*
     * This method is for when the player wins the battle
     * Window closes
     */
    private void victory(){
        //stop background music
        mediaPlayer.stop();

        //cue the victory music
        String defeat_music = this.model.getDirectoryName() + "/sounds/" + "victory_music.mp3";
        Media sound = new Media(new File(defeat_music).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        bossTroll.charImageview = new ImageView(new Image(this.model.getDirectoryName() + "/battleImages/" +
                    "victoryBoss.png"));
        bossTroll.charImageview.setFitHeight(500);
        bossTroll.charImageview.setPreserveRatio(true);
        removeByCell(1, 1);
        this.gridPane.add(bossTroll.charImageview, 1, 1);

        //Pause for the alert
        PauseTransition victory_pause = new PauseTransition(Duration.seconds(3));
        victory_pause.setOnFinished(event -> {
        });
        victory_pause.play();

        // Display victory message after the pause
        Platform.runLater(() -> {
                    victory_alert = new Alert(Alert.AlertType.INFORMATION);
                    ImageView victory_img = new ImageView(new Image(this.model.getDirectoryName() + "/battleImages/" +
                            "victoryBoss.png"));
                    victory_img.setFitHeight(50);
                    victory_img.setPreserveRatio(true);
                    victory_alert.setGraphic(victory_img);
                    victory_alert.setHeaderText("I WILL GET YOU NEXT TIME!");
                    victory_alert.setContentText("You... you have proven your strength, adventurer. I underestimated " +
                            "you. The lands are now yours to protect. May your journey be filled with victories.");
                    victory_alert.getButtonTypes().clear();
                    victory_alert.getButtonTypes().addAll(ButtonType.OK);
                    DialogPane dialogPane = victory_alert.getDialogPane();
                    dialogPane.setPrefWidth(600);
                    dialogPane.setPrefHeight(200);
                    victory_alert.showAndWait();
                });

        //Exit the game after 10 seconds
        PauseTransition end_pause = new PauseTransition(Duration.seconds(10));
        end_pause.setOnFinished(event -> {
            Platform.exit();
        });
        end_pause.play();
    }


    /*
     * This method returns the text needed for move
     * confirmation message after each round
     *
     * @param boss_dmg
     */
    private String round_text(int boss_dmg){
        return "YOU DEALT " + p_damage + " DAMAGE!\nTHE TROLL DEALT " + boss_dmg + " DAMAGE!\n\n" +
                "CURRENT STATS:\nPLAYER: \nHealth = " + finalPlayer.getHealth() + "\nStrength = " +
                finalPlayer.getStrength() + "\nTROLL: \nHealth = " + bossTroll.bossHealth + "\nStrength = " +
                bossTroll.bossStrength;
    }

    private void customizeButton(Button inputButton) {
        inputButton.setPrefSize(200, 50);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    private void removeByCell(int row, int col) {
        this.gridPane.getChildren().removeIf
                (node -> GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col);
    }

    public String parseOtherFile(String fileName) throws IOException {
        String text = "";
        fileName = model.getDirectoryName() + "/" + fileName;
        BufferedReader buff = new BufferedReader(new FileReader(fileName));
        String line = buff.readLine();
        while (line != null) { // while not EOF
            text += line+"\n";
            line = buff.readLine();
        }
        return text;
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInInventory Vbox.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {

        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox
        //please use setAccessibleText to add "alt" descriptions to your images!
        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";

        ScrollPane scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI,2,1);

        // Poppulate the onjectsInInventory
        objectsInInventory.getChildren().clear();

        addImageButtons(this.model.getPlayer().inventory, objectsInInventory);

    }

    private void addImageButtons(ArrayList<AdventureObject> lst, VBox vbox){
        for (AdventureObject object: lst) {
            String objectName = object.getName();
            String objectDesc = object.getDescription();
            String objectHelp = object.getHelpTxt();
            Image objectImage = new Image(this.model.getDirectoryName() + "/objectImages/" + objectName + ".png");
            ImageView objectImageView = new ImageView(objectImage);
            objectImageView.setPreserveRatio(true);
            objectImageView.setFitWidth(100);
            objectImageView.setFitHeight(100);

            Button objectButton = new Button(objectName, objectImageView);
            objectButton.setContentDisplay(ContentDisplay.TOP);
            customizeButton(objectButton, 100, 100);
            int othernNum = 0;

            // Go through all the button nodes to find how many times this item is duplicated
            for(Node node: vbox.getChildren()){
                if(node instanceof Button){
                    if(((Button) node).getText().startsWith(objectName.split("x")[0])){
                        if(((Button) node).getText().split("x").length != 2){
                            othernNum = 1;
                        }
                        else {
                            othernNum = Integer.parseInt(((Button) node).getText().split("x")[1]);
                        }
                    }
                }
            }

            // Add 1 to that count to account for this item
            int count = 1 + othernNum;

            // if there were no duplicates, put this item on screen
            if (count == 1) {
                makeButtonAccessible(objectButton, objectName, objectName, objectDesc);
                objectButton.setTooltip(new Tooltip(objectHelp));
                objectButton.setFont(Font.font(14));
                vbox.getChildren().add(objectButton);

                EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {

                        // Treat the click of a object as using it and continue the game
                        if (objectsInInventory.getChildren().contains(objectButton)) {
                            object.getState().execute();
                            model.player.inventory.remove(object);
                            if (object.getState() instanceof Token){
                                finalPlayer.changeStrengthBar(2);
                            }
                            updateItems();
                            boss_move();
                            check_status();
                        }
                    }
                };

                objectButton.setOnMouseClicked(eventHandler);
            }
            // else update the button there to reflect how many duplicates of this item are there
            else {
                Button button = (Button) vbox.getChildren().stream().filter(node -> node instanceof Button && ((Button) node).getText().startsWith(objectName.split("x")[0])).findAny().get();
                button.setText(button.getText().split("x")[0] + "x" + count);
            }

        }
    }


    @Override
    public void addInstructionEvent() {
        bossHelp.setOnAction(e -> {
            showInstructions();
        });
    }

    /**
     * Responds to a 'H' click by showing the or closing the player's stats
     */
    public void playerStatsEvent(){
        // Initialize them
        healthBar = new HealthBarView(this.model.getPlayer(), this);
        strengthBar = new StrengthBarView(this.model.getPlayer(), this);

        finalPlayer.setHealthBar(healthBar);
        finalPlayer.setStrengthBar(strengthBar);

        EventHandler<KeyEvent> keyBindClick = new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent event){
                if (event.getCode().equals(KeyCode.H)){
                    showPlayerStats();
                }
            }
        };

        // Make the gridpane wait for it
        this.gridPane.setOnKeyPressed(keyBindClick);

    }

    public void showPlayerStats(){
        // if health bar is off
        if (!this.playerStatsToggle) {

            // turn it on, make and show it
            this.playerStatsToggle = true;
            removeByCell(2, 0);
            playerStats.getChildren().clear();
            playerStats.getChildren().add(healthBar.get());
            playerStats.getChildren().add(strengthBar.get());
            gridPane.add(playerStats, 0, 2, 1, 1);
        }
        // else
        else{
            //turn it off and close it
            this.playerStatsToggle = false;
            removeByCell(2, 0);
        }
    }

    /**
     * Allows user ability to use special attack
     */
    public void activateStrengthButton(){
        specAttackButton.setDisable(false);
    }

    /**
     * Makes the player invincible
     */
    public void invincible(){
        this.invincible = true;
    }


    /**
     * Halves the boss' health
     */
    public void halfDamage(){
        bossTroll.changeHealthBar(-(bossTroll.getHealth()/2));
    }

    /**
     * Lets user use the special attack button in the lucky way
     */
    public void specialAttackChance(){
        specAttackButton.setDisable(false);
        luckySpec = true;
    }
}
