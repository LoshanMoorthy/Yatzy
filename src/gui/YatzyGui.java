/** Yatzy Game - Gruppe 1  **/
/** Asger Hammond Raffnsøe **/
/** Loshan Sundaramoorthy  **/
/** Steffen Køhler Lassen  **/
/**       18-04-2023       **/

package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.YatzyDice;
import java.util.ArrayList;

public class YatzyGui extends Application {
    /**---------------Start Alert------------*/
    Dialog<ButtonType> welcomeDialog = new Dialog<>();
    ButtonType btnStartGame= new ButtonType("Start game!");
    ButtonType btnExitGame = new ButtonType("Exit game!");

    /**
     * ---------------Object------------------
     **/
    private final YatzyDice dice = new YatzyDice();

    /**
     * ---------------Textfields------------------
     **/
    // txfValues shows the face values of the 5 dice.
    private final TextField[] txfValues = new TextField[5];

    // Shows points in sums, bonus and total.
    private final TextField txfSumSame = new TextField();
    private final TextField txfBonus = new TextField();
    private final TextField txfSumOther = new TextField();
    private final TextField txfTotal = new TextField();

    // txfResults shows the obtained results.
    // For results not set yet, the possible result of
    // the actual face values of the 5 dice are shown.
    private final ArrayList<TextField> txfResults = new ArrayList<>(15);

    /**
     * ---------------Checkboxes------------------
     **/
    // cbxHolds shows the hold status of the 5 dice.
    private CheckBox[] cbxHolds = new CheckBox[5];
    private boolean[] holdStatus = new boolean[5];

    /**
     * ---------------Labels------------------
     **/

    private final Label lblThrowCount = new Label();
    private final Label[] lblResults = new Label[15];
    private final Label lblBonus = new Label("Bonus");
    private final Label lblSumSame = new Label("Sum");
    private final Label lblSumOther = new Label("Sum");
    private final Label lblTotal = new Label("Total");

    /**
     * ---------------Global variables------------------
     **/
    private int sumSame = 0;
    private int sumOther = 0;
    private int bonus = 0;
    private int total = sumSame + sumOther + bonus;
    private int roundCounter = 0;

    /** -----------Buttons--------------------*/
    private final Button btnThrow = new Button("Throw");
    private final Button btnRestart = new Button("Restart Game");

    /**
     * ---------------Launch------------------
     **/
    @Override
    public void start(Stage stage) {
        stage.setTitle("Yatzy");
        GridPane pane = new GridPane();

        welcomeDialog.setTitle("Gruppe 1 - Yatzy");
        welcomeDialog.setHeaderText("Velkommen til Yatzy");
        VBox dialogContent = new VBox();
        welcomeDialog.getDialogPane().setContent(dialogContent);
        welcomeDialog.getDialogPane().getButtonTypes().add(btnStartGame);
        welcomeDialog.getDialogPane().getButtonTypes().add(btnExitGame);

        welcomeDialog.showAndWait()
                .filter(response -> response.equals(btnStartGame))
                .filter(response -> response.equals(btnExitGame))
                .ifPresent(response -> {
                    if(response == btnStartGame){
                        this.initContent(pane);
                    } else if (response == btnExitGame) {
                        stage.close();
                    }

                    });

        this.initContent(pane);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * ---------------Initiate Content------------------
     **/
    private void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(0));
        pane.setHgap(15);
        pane.setVgap(15);

        /**---------------Dice Pane, top of screen------------------**/
        GridPane dicePane = new GridPane();
        pane.add(dicePane, 0, 0);
        dicePane.setGridLinesVisible(false);
        dicePane.setPadding(new Insets(10));
        dicePane.setHgap(10);
        dicePane.setVgap(10);

        int valueSquare = 60;
        int resultSquare= 30;

        for (int i = 0; i < cbxHolds.length; i++) {
            txfValues[i] = new TextField();
            cbxHolds[i] = new CheckBox("Hold");
        }
        for (int i = 0; i < txfValues.length; i++) {
            dicePane.add(txfValues[i], i, 0);
            txfValues[i].setMaxSize(valueSquare,valueSquare);;
            txfValues[i].setStyle("-fx-font-size: 20px; -fx-text-fill: black;");
            txfValues[i].setDisable(true);
            dicePane.add(cbxHolds[i], i, 1);
            cbxHolds[i].setMinSize(50,50);
        }
        /**-------------------------------------------------------------------------**/


        dicePane.add(btnThrow, 4, 2);

        /** ---------------Score Pane ------------------------------------------**/

        GridPane scorePane = new GridPane();
        pane.add(scorePane, 0, 4);
        scorePane.setGridLinesVisible(false);
        scorePane.setPadding(new Insets(10));
        scorePane.setVgap(5);
        scorePane.setHgap(10);
        scorePane.setStyle("-fx-border-color: red;-fx-font-size: 18px");
        scorePane.setStyle("-fx-font-size: 18px");
        int resultWidth = 35; // width of the text fields
        scorePane.add(btnRestart, 5, 17);
        btnRestart.setDisable(true);
        btnRestart.setOnAction(Event -> restartGame());



        /** Labels for results **/
        lblResults[0] = new Label("1-s");
        lblResults[1] = new Label("2-s");
        lblResults[2] = new Label("3-s");
        lblResults[3] = new Label("4-s");
        lblResults[4] = new Label("5-s");
        lblResults[5] = new Label("6-s");
        lblResults[6] = new Label("One Pair");
        lblResults[7] = new Label("Two Pair");
        lblResults[8] = new Label("Three-same");
        lblResults[9] = new Label("Four-same");
        lblResults[10] = new Label("Full House");
        lblResults[11] = new Label("Small str.");
        lblResults[12] = new Label("Large str.");
        lblResults[13] = new Label("Chance");
        lblResults[14] = new Label("Yatzy");

        int x = 0;
        int y = 1;

        for (int i = 0; i < lblResults.length; i++) {
            if(i > 5){
                scorePane.add(lblResults[i], x, i+2);
                lblResults[i].setStyle("-fx-font-size: 12px;");
                lblResults[i].setMaxWidth(60);
                lblResults[i].setAlignment(Pos.CENTER_RIGHT);

            } else{
                scorePane.add(lblResults[i], x, i);
                lblResults[i].setStyle("-fx-font-size: 12px;");
                lblResults[i].setMaxWidth(60);
                lblResults[i].setAlignment(Pos.CENTER_RIGHT);
            }

        }

        int size = 15;
        for (int i = 0; i < size; i++) {
            txfResults.add(new TextField());

        }

        for (int i = 0; i < txfResults.size(); i++) {
            if (i > 5) {
                TextField textField = txfResults.get(i);
                textField.setDisable(false);
                scorePane.add(textField, 1, i + 2);
                txfResults.get(i).setMaxWidth(resultWidth);
                txfResults.get(i).setStyle("-fx-font-size: 12px; ");

            } else {
                TextField textField = txfResults.get(i);
                textField.setDisable(false);
                scorePane.add(textField, 1, i * y);
                txfResults.get(i).setMaxWidth(resultWidth);
                txfResults.get(i).setStyle("-fx-font-size: 12px; ");
            }
        }

        /** ---------- Sum, bonus and total ---------**/

        scorePane.add(lblSumSame, 2, 5);
        lblSumSame.setStyle("-fx-font-size: 12px; ");
        scorePane.add(txfSumOther, 3, 5);
        txfSumOther.setMaxSize(resultSquare,resultSquare);
        txfSumOther.setStyle("-fx-font-size: 12px; ");

        scorePane.add(txfBonus, 3, 6);
        txfBonus.setMaxSize(resultSquare,resultSquare);
        txfBonus.setStyle("-fx-font-size: 12px; ");
        scorePane.add(lblBonus, 2, 6);
        lblBonus.setStyle("-fx-font-size: 12px; ");

        scorePane.add(lblSumOther, 2, 16);
        lblSumOther.setStyle("-fx-font-size: 12px; ");
        scorePane.add(txfSumSame, 3, 16);
        txfSumSame.setMaxSize(resultSquare,resultSquare);
        txfSumSame.setStyle("-fx-font-size: 12px; ");
        scorePane.add(lblTotal, 2, 17);
        lblTotal.setStyle("-fx-font-size: 12px; ");
        scorePane.add(txfTotal, 3, 17);
        txfTotal.setMaxSize(resultSquare,resultSquare);
        txfTotal.setStyle("-fx-font-size: 12px; ");



        /** ---------- Interactive elements ---------**/

        for (int i = 0; i < txfResults.size(); i++) {
            txfResults.get(i).setOnMouseClicked(Event -> this.mouseClicked(Event));
        }
        btnThrow.setOnAction(Event -> this.btnThrowAction());




    }



    /** ---------- Method that calls necessary method for btnThrowAction  ---------**/
    private void btnThrowAction() {
        setHoldStatus();
        dice.throwDice(holdStatus);
        displayDice();
        throwBtn();
        updateResultFields();


    }

    /** ---------- Sets holdstatus for chosen cbxHolds checkboxes ---------**/
    public void setHoldStatus() {
        for (int i = 0; i < cbxHolds.length; i++) {
            if (cbxHolds[i].isSelected()) {
                holdStatus[i] = true;
                cbxHolds[i].setDisable(true);
            }
        }
    }

    public boolean[] getHoldStatus() {
        return holdStatus;
    }

    private void resetHoldStatus() {
        for (int i = 0; i < cbxHolds.length; i++) {
            cbxHolds[i].setSelected(false);
            holdStatus[i] = false;

        }
    }



    /** ---------- Result fields ---------**/
    private void updateResultFields() {
        for (int i = 0; i < txfResults.size(); i++) {
            if (!txfResults.get(i).isDisable()) {
                txfResults.get(i).setText(Integer.toString(dice.getResults()[i]));
            }
        }

    }

    /** ---------- Mouseclick method ---------**/
    private void mouseClicked(MouseEvent event) {
        TextField txf = (TextField) event.getSource();
        int index = txfResults.indexOf(txf);
        txf.setDisable(true);
        if (index <= 5) {
            sumSame = sumSame + dice.getResults()[index];
        } else if (index > 5) {
            sumOther = sumOther + dice.getResults()[index];
        }
        resetRound();
        resetHoldStatus();
        endGame();
    }

    /** ---------- Sums, bonus og total fields ---------**/
    private void setTxfSumOther() {
        txfSumOther.setText(Integer.toString(sumSame));
        txfSumSame.setText((Integer.toString(sumOther)));
        if (sumSame >= 63) {
            bonus = 50;
            txfBonus.setText(Integer.toString(bonus));
        }
        txfTotal.setText(Integer.toString(sumSame + sumOther + bonus));
    }

    /** ---------- Sets throwcount to 0 and new round initiates ---------**/
    private void resetRound() {
        for (int i = 0; i < txfResults.size(); i++) {
            if (!txfResults.get(i).isDisable()) {
                txfResults.get(i).clear();
            }
        }
        for (int i = 0; i < txfValues.length; i++) {
            txfValues[i].clear();
        }
        roundCounter++;
        dice.resetThrowCount();
        btnThrow.setText("Throw " + dice.getThrowCount());
        btnThrow.setDisable(false);
        setTxfSumOther();
        endGame();
    }

    /** ---------- Displays values[] i txfValues [] ---------**/
    private void displayDice() {
        for (int i = 0; i < txfValues.length; i++) {
            txfValues[i].setText(Integer.toString(dice.getValues()[i]));
        }
    }

    /** ---------- Makes throwbutton unusable when equals 3 ---------**/
    private void throwBtn() {
        if (dice.getThrowCount() == 3) {
            btnThrow.setDisable(true);
            for (int i = 0; i < txfResults.size(); i++) {
                if (!txfResults.get(i).isDisable()) {

                }

            }
        }
    }


    /** ---------- Metoder allowing multiple games ---------**/
    private int getRoundCounter(){
        return roundCounter;
    }

    private void resetRoundCounter(){
        this.roundCounter = 0;
    }

    private void endGame() {
        if (getRoundCounter() == 15) {
            for (int i = 0; i < txfResults.size(); i++) {
                txfResults.get(i).setDisable(true);
                txfResults.get(i).clear();
            }
            for (int i = 0; i < txfValues.length; i++) {
                txfValues[i].setDisable(true);
                txfValues[i].clear();
                cbxHolds[i].setDisable(true);
            }
            btnRestart.setDisable(false);
            btnThrow.setDisable(true);
        }
    }

    private void restartGame(){
        resetRound();
        resetRoundCounter();
        sumSame = 0;
        sumOther = 0;
        bonus = 0;
        total = sumSame + sumOther + bonus;
        btnRestart.setDisable(true);
        for (int i = 0; i < txfResults.size(); i++) {
            txfResults.get(i).setDisable(false);
        }
    }
}
