package com.hanzereversiai.projectp3.ui;

import com.thowv.javafxgridgameboard.AbstractGameInstance;
import com.thowv.javafxgridgameboard.AbstractTurnEntity;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.GameBoardTileType;
import com.thowv.javafxgridgameboard.listeners.GameEndListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GamePanelController {
    private static final String TURN_LABEL_PREFIX = "Turn: ";
    private GameBoard gameBoard;

    public BorderPane borderPane;
    public Button backButton;
    public Label playerOneLabel;
    public Label playerTwoLabel;
    public Label turnLabel;
    public StackPane centerStackPane;
    public Pane playerOneColorPane;
    public Pane playerTwoColorPane;

    public void setGameInstance(AbstractGameInstance gameInstance) {
        this.gameBoard = gameInstance.getGameBoard();
        centerStackPane.getChildren().clear();
        centerStackPane.getChildren().add(gameBoard);

        // Subscribe to events
        gameInstance.onTurnSwitch(this::onTurnSwitch);
        gameInstance.onGameStart(this::onGameStart);
        gameInstance.onGameEnd(new GameEndListener() {
            @Override
            public void onGameEnd(AbstractTurnEntity winningTurnEntity, AbstractTurnEntity losingTurnEntity) {
                endGame(winningTurnEntity.getName() + " won!");
            }

            @Override
            public void onGameEnd(AbstractTurnEntity[] tieTurnEntities) {
                endGame("It was a tie");
            }
        });

        // Start the game instance
        Platform.runLater(gameInstance::start);
    }

    public void onBackButtonActivated(ActionEvent actionEvent) {
        UIHelper.switchScene(actionEvent, "lobby-panel");
    }

    private void onTurnSwitch(AbstractTurnEntity currentTurnEntity, AbstractTurnEntity lastTurnEntity) {
        Platform.runLater(() -> {
            setTurnLabelText(currentTurnEntity.getName());

            setPlayerInfo(currentTurnEntity);
            setPlayerInfo(lastTurnEntity);
        });
    }

    private void onGameStart(AbstractTurnEntity currentTurnEntity) {
        backButton.setDisable(true);
        setTurnLabelText(currentTurnEntity.getName());
    }

    private void endGame(String endMessage) {
        Platform.runLater(() -> {
            enableBackButton();
            centerStackPane.getChildren().clear();
            setTurnLabelText("");

            // Create an ending message
            Label endLabel = new Label(endMessage);
            endLabel.setId("end-message-label");
            centerStackPane.getChildren().add(endLabel);
        });
    }

    private void enableBackButton() {
        backButton.setDisable(false);
    }

    // region Getters and setters
    public void setTurnLabelText(String text) {
        turnLabel.setText(TURN_LABEL_PREFIX + text);
    }

    public void setPlayerInfo(AbstractTurnEntity turnEntity) {
        if (turnEntity.getGameBoardTileType() == GameBoardTileType.PLAYER_1) {
            playerOneLabel.setText(turnEntity.getName() + ": " + turnEntity.getPoints());
            playerOneColorPane.setStyle("-fx-background-color: " + turnEntity.getColor());
        }
        else {
            playerTwoLabel.setText(turnEntity.getPoints() + " :" + turnEntity.getName());
            playerTwoColorPane.setStyle("-fx-background-color: " + turnEntity.getColor());
        }
    }
    // endregion
}
