package views;

import AdventureModel.AdventureObject;
import AdventureModel.Player;
import BossFactory.Boss;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    boolean boss_helpToggle = false;


    boolean playerStatsToggle = false; //to know if health bar is on or off
    BarView healthBar; // to access the health bar
    BarView strengthBar; // to access the strength bar

    VBox playerStats; // holds the player stats

    boolean invincible;    // for invincibility

    int roundNum; // num rounds invincible for

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
        roundNum = 0;
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

        String bossImg = this.model.getDirectoryName() + "/battleImages/" + "normalBoss.png";
        bossTroll.charImage = new Image(bossImg);
        bossTroll.charImageview = new ImageView(bossTroll.charImage);

        //add all the widgets to the GridPane
        this.gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

        playerStats = new VBox();
        playerStats.setSpacing(10);
        playerStats.setAlignment(Pos.CENTER_LEFT);
        // event for hiding or opening the health bar
        playerStatsEvent();

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

//        PauseTransition pause = new PauseTransition(Duration.seconds(10));
//        pause.setOnFinished(event -> {
//            Alert boss_msg = new Alert(Alert.AlertType.INFORMATION);
//            boss_msg.setGraphic(bossTroll.charImageview);
//        });
//        pause.play();
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
        System.out.println(finalPlayer.getHealth());
        System.out.println(bossTroll.bossHealth);
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
        strengthBar.change(1);
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
        strengthBar.initState();
    }

    /*
     * This method lets the player attack
     * the enemy boss
     */
    private void playerAttack(){
        int damage = rand.nextInt(finalPlayer.getStrength(), finalPlayer.getStrength() + 50);
        bossTroll.bossHealth -= damage;
    }

    /*
     * This method lets the player heal themselves
     */
    private void playerHeal(){
        finalPlayer.changeHealth(25);
        if (finalPlayer.getHealth() > 100){
            finalPlayer.changeHealth(-finalPlayer.getHealth() % 100);
        }
    }

    /*
     * This method lets the player unleash
     * a special attack on the enemy boss
     */
    private void playerSpec(){


        int damage = rand.nextInt(finalPlayer.getStrength(), finalPlayer.getStrength() * 15);
        bossTroll.bossHealth -= damage;

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
    }

    /*
     * This method is used to disable the ability buttons
     */
    private void close_buttons(){
        attackButton.setDisable(true);
        healButton.setDisable(true);
        specAttackButton.setDisable(true);
    }

    /*
     * This method is used to initiate the boss's move
     * where they can either attack or heal based on
     * a random number between 0 and 50
     */
    private void boss_move(){
        int move = rand.nextInt(0,50);
        if (move > 10){

            // can only attack a non-invincible player
            if(!invincible){
            bossTroll.attack(finalPlayer);
            }
        }
        else{
            bossTroll.heal();
        }

        // Keep track of how many rounds of invincibility
        roundNum += 1;
        if (roundNum == 3){
            invincible = false; // after 3 the invincibility wears off
        }

        open_buttons();
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

    public void updateScene(String textToDisplay){

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
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(actionEvent -> Platform.exit());
        pause.play();
    }

    public void halfDamage(){

    }

    /**
     * Set player stats toggle
     * @param playerStatsToggle what to set playerStatsToggle to
     */
    public void setPlayerStatsToggle(boolean playerStatsToggle){
        this.playerStatsToggle = playerStatsToggle;
        if(this.playerStatsToggle){
            this.playerStatsToggle = !this.playerStatsToggle;
            showPlayerStats();
        }
    }

}
