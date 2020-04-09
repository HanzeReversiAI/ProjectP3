package com.hanzereversiai.projectp3.ui;

import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.GameBoardTileType;
import com.thowv.javafxgridgameboard.listeners.GameEndListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GamePanelController {
    private static final String TURN_LABEL_PREFIX = "Turn: ";

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

    public void setGameInstance(AbstractGameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.gameBoard = gameInstance.getGameBoard();
        centerStackPane.getChildren().add(gameBoard);

        // Subscribe to events
        gameInstance.onTurnSwitch(this::onTurnSwitch);
        gameInstance.onGameEnd(new GameEndListener() {
            @Override
            public void onGameEnd(AbstractTurnEntity winningTurnEntity, AbstractTurnEntity losingTurnEntity) {
                backButton.setDisable(false);
            }

            @Override
            public void onGameEnd(AbstractTurnEntity[] tieTurnEntities) {
                backButton.setDisable(false);
            }
        });
        gameInstance.onGameStart(this::onGameStart);
    }

    public void updatePoints(int playerOnePoints, int playerTwoPoints) {
        setPlayerOnePoints(playerOnePoints);
        setPlayerTwoPoints(playerTwoPoints);
    }

    public void onBackButtonActivated(ActionEvent actionEvent) {
        UIHelper.switchScene(actionEvent, "lobby-panel");
    }

    private void onTurnSwitch(AbstractTurnEntity currentTurnEntity, AbstractTurnEntity lastTurnEntity) {
        setTurnLabelText(currentTurnEntity.getName());
    }

    private void onGameStart() {
        backButton.setDisable(true);
        setTurnLabelText(gameInstance.getCurrentTurnEntity().getName());
    }

    // region Getters and setters
    public void setTurnLabelText(String text) {
        turnLabel.setText(TURN_LABEL_PREFIX + text);
    }

    public void setPlayerOnePoints(int points) {
        setPlayerOneInfo("", points);
    }

    public void setPlayerOneInfo(String name, int points) {
        if (!name.isEmpty())
            playerOneName = name;

        if (points != -1)
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
