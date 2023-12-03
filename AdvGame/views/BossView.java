package views;

import AdventureModel.Player;
import BossFactory.Boss;
import BossFactory.concreteBossFactory;
import AdventureModel.AdventureGame;
import BossFactory.trollBoss;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private String[] announcerText = new String[] {"Heya there Challenger... You are new here aren't ya? Well my name is Laudius and WELCOME TO SUMMONER'S FIGHT!",
            "Warriors here battle each other with their own Akuma, which are tamed demons...WAIT...YOU DON'T HAVE ONE???",
            "Hehehe...That's completely fine because we offer a variety of choices for your choosing! You get to pick an Akuma based on what YOU like!",
            "But be careful, as you win each battle, the next one gets even harder but the last one standing recieves... THE TROPHY OF ATROPHiIUS",
            "So get on with your registration and your choice of monster. Battle your way through VICTORY!"};
    boolean boss_helpToggle = false;
    public BossView(AdventureGame model, Stage stage) throws IOException {
        super(model, stage);
        rand = new Random();
        model.setHelpText(parseOtherFile("boss_help"));
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

        healButton.setOnAction(event -> heal_handle(healButton, attackButton, specAttackButton));
        attackButton.setOnAction(event -> attack_handle(healButton, attackButton, specAttackButton));
        specAttackButton.setOnAction(event -> specAttack_handle(healButton, attackButton, specAttackButton));

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));

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
        this.gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        this.gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label
        this.gridPane.add(abilityButtons, 1, 2, 1, 2); //add ability buttons
        this.gridPane.add(bossHelp, 0, 0);
        this.gridPane.add(bossTroll.charImageview, 1, 1);
        GridPane.setHalignment(bossTroll.charImageview, HPos.CENTER);
        GridPane.setValignment(bossTroll.charImageview, VPos.CENTER);
        GridPane.setValignment(bossHelp, VPos.TOP);
        GridPane.setValignment(objLabel, VPos.BOTTOM);
        GridPane.setValignment(invLabel, VPos.BOTTOM);
        GridPane.setHalignment(objLabel, HPos.RIGHT);

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

    private void check_status(){
        System.out.println(finalPlayer.getHealth());
        System.out.println(bossTroll.bossHealth);
        if (finalPlayer.getHealth() <= 0 || bossTroll.bossHealth <= 0) {
            Platform.exit();
        }
    }

    private void heal_handle(Button button1, Button button2, Button button3) {
        // Disable all buttons
        button1.setDisable(true);
        button2.setDisable(true);
        button3.setDisable(true);

        playerHeal();
        boss_move();
        check_status();
    }

    private void attack_handle(Button button1, Button button2, Button button3) {
        // Disable all buttons
        button1.setDisable(true);
        button2.setDisable(true);
        button3.setDisable(true);

        playerAttack();
        boss_move();
        check_status();
    }

    private void specAttack_handle(Button button1, Button button2, Button button3) {
        // Disable all buttons
        button1.setDisable(true);
        button2.setDisable(true);
        button3.setDisable(true);

        playerSpec();
        boss_move();
        check_status();
    }

    private void playerAttack(){
        int damage = rand.nextInt(finalPlayer.getStrength(), finalPlayer.getStrength() * 5);
        bossTroll.bossHealth -= damage;
    }

    private void playerHeal(){
        finalPlayer.changeHealth(25);
        if (finalPlayer.getHealth() > 100){
            finalPlayer.changeHealth(-finalPlayer.getHealth() % 100);
        }
    }

    private void playerSpec(){
        int damage = rand.nextInt(finalPlayer.getStrength(), finalPlayer.getStrength() * 15);
        bossTroll.bossHealth -= damage;
    }

    private void open_buttons(){
        attackButton.setDisable(false);
        healButton.setDisable(false);
        specAttackButton.setDisable(false);
    }

    private void boss_move(){
        int move = rand.nextInt(0,50);
        if (move > 10){
            bossTroll.attack(finalPlayer);
        }
        else{
            bossTroll.heal();
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

    @Override
    public void addInstructionEvent() {
        bossHelp.setOnAction(e -> {
            showInstructions();
        });
    }
}
