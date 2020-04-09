package com.hanzereversiai.projectp3.ui;

import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.GameBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GamePanelController {
    private AbstractGameInstance gameInstance;
    private GameBoard gameBoard;
    private String playerOneName;
    private String playerTwoName;
    private int playerOnePoints;
    private int playerTwoPoints;

    public BorderPane borderPane;
    public Button backButton;
    public Label playerOneLabel;
    public Label playerTwoLabel;
    public Label turnLabel;
    public StackPane centerStackPane;
    public Pane playerOneColorPane;
    public Pane playerTwoColorPane;

    @FXML
    public void initialize() {
        setPlayerOneInfo("Player 1", 0);
        setPlayerTwoInfo("Player 2", 0);
    }

    public void setGameInstance(AbstractGameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.gameBoard = gameInstance.getGameBoard();
        centerStackPane.getChildren().add(gameBoard);
    }

    public void updatePoints(int playerOnePoints, int playerTwoPoints) {
        setPlayerOnePoints(playerOnePoints);
        setPlayerTwoPoints(playerTwoPoints);
    }

    public void onBackButtonActivated(ActionEvent actionEvent) {
        UIHelper.switchScene(actionEvent, "lobby-panel");
    }

    // region Getters and setters
    public void setPlayerOnePoints(int points) {
        setPlayerOneInfo("", points);
    }

    public void setPlayerOneInfo(String name, int points) {
        if (!name.isEmpty())
            playerOneName = name;

        playerOnePoints = points;

        playerOneLabel.setText(playerOneName + ": " + playerOnePoints);
    }

    public void setPlayerTwoPoints(int points) {
        setPlayerTwoInfo("", points);
    }

    public void setPlayerTwoInfo(String name, int points) {
        if (!name.isEmpty())
            playerTwoName = name;

        playerTwoPoints = points;

        playerTwoLabel.setText( playerTwoPoints + " :" + playerTwoName);
    }
    // endregion
}
