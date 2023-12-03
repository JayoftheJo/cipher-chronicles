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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import views.bars.*;

import java.io.BufferedReader;
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
    ImageView round_img_v;
    boolean boss_helpToggle = false;
    int damage, round_num = 1;


    boolean playerStatsToggle = false; //to know if health bar is on or off
    BarView healthBar; // to access the health bar
    BarView strengthBar; // to access the strength bar

    VBox playerStats; // holds the player stats


    BarView bossHealthBar;

    BarView bossStrengthBar;

    VBox bossStats;

    boolean invincible;    // for invincibility

    int invRoundNum; // num rounds invincible for

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
        makeButtonAccessible(bossHelp, "Instruction Button", "Instructions for the player", "This button allows the player to view the instructions to defeat the troll");
        bossHelp.setOnAction(e -> {
            showInstructions();
        });

        healButton = new Button("Heal");
        healButton.setId("Heal");
        customizeButton(healButton);
        makeButtonAccessible(healButton, "Heal Button", "Heals the player", "This button heals the player if they have health tokens available");

        attackButton = new Button("Attack");
        attackButton.setId("Attack");
        customizeButton(attackButton);
        makeButtonAccessible(attackButton, "Attack Button", "Unleash a normal attack", "This button allows the user to use a normal attack on the troll");

        specAttackButton = new Button("Special Attack");
        specAttackButton.setId("Special Attack");
        customizeButton(specAttackButton);
        makeButtonAccessible(specAttackButton, "Special Attack Button", "Unleash a special attack", "This button allows the user to use a special attack on the troll if they have attack tokens available");

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

        // Alerting the intro message to the boss room
        Platform.runLater(() -> {
            Alert round = new Alert(Alert.AlertType.INFORMATION);
            ImageView intro_img = new ImageView(bossTroll.charImage);
            intro_img.setFitHeight(35);
            intro_img.setPreserveRatio(true);
            round.setGraphic(intro_img);
            round.setHeaderText("HAHAHAHAHAHA!");
            round.setContentText("YOU DARE TO CHALLENGE ME, PUNY ADVENTURER? YOUR JOURNEY ENDS HERE! PREPARE TO BE CRUSHED " +
                    "BENEATH MY MIGHT, FOR I AM THE GUARDIAN OF THESE LANDS, AND NONE SHALL PASS!");
            round.getButtonTypes().clear();
            round.getButtonTypes().addAll(ButtonType.OK);
            DialogPane dialogPane = round.getDialogPane();
            dialogPane.setPrefWidth(600);
            dialogPane.setPrefHeight(200);
            round.showAndWait();
            open_buttons();
        });
    }

    //Displays boss instructions
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
        round_num += 1;
        if (finalPlayer.getHealth() <= 0 || bossTroll.bossHealth <= 0) {
            Platform.exit();
        }
    }

    /*
     * This method handles when the heal
     * button has been clicked
     */
    private void heal_handle() {
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
        close_buttons();
        playerAttack();
        boss_move();
        check_status();
    }


    /*
     * Makes this BossView thier view
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
        damage = rand.nextInt(finalPlayer.getStrength(), finalPlayer.getStrength() + 50);
        bossTroll.changeHealthBar(-damage);
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


        damage = rand.nextInt(finalPlayer.getStrength(), finalPlayer.getStrength() * 15);
        bossTroll.changeHealthBar(-damage);

        this.strengthBar.initState();

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
        int boss_dmg = 0;
        if (move > 10){

            // can only attack a non-invincible player
            if(!invincible){
                boss_dmg = bossTroll.attack(finalPlayer);
            }
        }
        else{
            boss_dmg = 0;
            bossTroll.heal();
        }

        // Keep track of how many rounds of invincibility
        if (invRoundNum >= 3){
            invincible = false; // after 3 the invincibility wears off
            invRoundNum = 0;
        }
        else{
            invRoundNum += 1;
        }

        Alert round = new Alert(Alert.AlertType.INFORMATION);
        round.setHeaderText("ROUND " + round_num);
        round.setGraphic(round_img_v);
        round.setContentText(round_text(boss_dmg));
        round.getButtonTypes().clear();
        round.getButtonTypes().addAll(ButtonType.OK);
        round.showAndWait();
        open_buttons();
    }

    /*
     * This method returns the text needed for move
     * confirmation message after each round
     *
     * @param boss_dmg
     */
    private String round_text(int boss_dmg){
        return "\nYou dealt " + damage + " damage\nThe troll dealt " + boss_dmg + "\n\n" +
                "CURRENT STATS:\nPLAYER: \nHEALTH = " + finalPlayer.getHealth() + "\nSTRENGTH = " +
                finalPlayer.getStrength() + "\nTROLL: \nHEALTH = " + bossTroll.bossHealth + "\nSTRENGTH = " +
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

            // if there was no duplicates, put this item on screen
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
     * GameOver.
     * Window closes
     */
    public void gameOver() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(actionEvent -> Platform.exit());
        pause.play();
    }

    /**
     * Halves the boss' health
     */
    public void halfDamage(){
        bossTroll.changeHealthBar(-(bossTroll.getHealth()/2));
    }

}
