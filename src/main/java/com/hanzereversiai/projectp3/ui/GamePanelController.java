package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.NetworkSingleton;
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

/**
 * @author Thomas
 */
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

    /**
     * Takes a game instance, renders the board included in the instance,
     * subscribes to the events and starts the game instance.
     * @param gameInstance The gameInstance used for operation
     */
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
                setPlayerInfo(winningTurnEntity);
                setPlayerInfo(losingTurnEntity);

                endGame(winningTurnEntity.getName() + " won!");
            }

            @Override
            public void onGameEnd(AbstractTurnEntity[] tieTurnEntities) {
                setPlayerInfo(tieTurnEntities[0]);
                setPlayerInfo(tieTurnEntities[1]);

                endGame("It was a tie");
            }
        });

        // Start the game instance
        Platform.runLater(gameInstance::start);
    }

    // region Events
    /**
     * Return to the lobby screen
     * @param actionEvent The event that fired this method
     */
    public void onBackButtonActivated(ActionEvent actionEvent) {
        UIHelper.switchScene(actionEvent, "lobby-panel");
    }

    /**
     * Set the player information and the turn label text.
     * @param currentTurnEntity The entity that has the current turn
     * @param lastTurnEntity The entity that had the turn last time
     */
    private void onTurnSwitch(AbstractTurnEntity currentTurnEntity, AbstractTurnEntity lastTurnEntity) {
        Platform.runLater(() -> {
            setTurnLabelText(currentTurnEntity.getName());

            setPlayerInfo(currentTurnEntity);
            setPlayerInfo(lastTurnEntity);
        });
    }

    /**
     * Disable the back button and set the turn label text.
     * @param turnEntity The entity that has to start first
     */
    private void onGameStart(AbstractTurnEntity turnEntity) {
        backButton.setDisable(true);
        setTurnLabelText(turnEntity.getName());
    }
    // endregion

    /**
     * Ends the current running game and displays a message with the results.
     * @param endMessage The message displayed after ending the game
     */
    private void endGame(String endMessage) {
        Platform.runLater(() -> {
            backButton.setDisable(false);

            centerStackPane.getChildren().clear();
            setTurnLabelText("");

            // Create an ending message
            Label endLabel = new Label(endMessage);
            endLabel.setId("end-message-label");
            centerStackPane.getChildren().add(endLabel);

            if (NetworkSingleton.getNetworkInstance().getNetworkHandler() != null)
                NetworkSingleton.getNetworkInstance().getNetworkHandler().setRootUIObject(centerStackPane);
        });
    }

    // region Getters and setters

    /**
     * Set the turn label text.
     * @param text Text that the turn label has to display
     */
    public void setTurnLabelText(String text) {
        turnLabel.setText(TURN_LABEL_PREFIX + text);
    }

    /**
     * Set the player information such as name and points.
     * @param entity The entity that the info has to be used from
     */
    public void setPlayerInfo(AbstractTurnEntity entity) {
        if (entity.getGameBoardTileType() == GameBoardTileType.PLAYER_1) {
            playerOneLabel.setText(entity.getName() + ": " + entity.getPoints());
            playerOneColorPane.setStyle("-fx-background-color: " + entity.getColor());
        }
        else {
            playerTwoLabel.setText(entity.getPoints() + " :" + entity.getName());
            playerTwoColorPane.setStyle("-fx-background-color: " + entity.getColor());
        }
    }
    // endregion
}
