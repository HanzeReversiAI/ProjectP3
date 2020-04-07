package com.hanzereversiai.projectp3;

import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.reversiai.ReversiAi;
import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import com.hanzereversiai.projectp3.ui.events.ConnectionRaisedEvent;
import com.thowv.javafxgridgameboard.GameBoard;
import com.thowv.javafxgridgameboard.events.GameBoardTilePressedEvent;
import com.thowv.javafxgridgameboard.events.GameEndedEvent;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiGameInstance;
import com.thowv.javafxgridgameboard.premades.reversi.ReversiTurnEntityPlayer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;

public class App extends Application {
    Network network;
    StackPane rootStackPane;

    @Override
    public void start(Stage stage) {
        rootStackPane = new StackPane();

        ConnectionPanel connectionPanel = new ConnectionPanel();
        connectionPanel.addEventHandler(ConnectionRaisedEvent.CONNECTION_RAISED_EVENT_TYPE,
                e -> onConnectionRaised(e.getNetwork()));
        rootStackPane.getChildren().add(connectionPanel);

        stage.setScene(new Scene(rootStackPane));
        stage.setTitle("Reversi AI Project");
        stage.show();
    }

    public void onConnectionRaised(Network network) {
        this.network = network;
        rootStackPane.getChildren().remove(0);

        // Create a game board with a given size
        GameBoard gameBoard = new GameBoard(8);

        gameBoard.addEventHandler(GameBoardTilePressedEvent.TILE_PRESSED_EVENT_EVENT_TYPE,
                e -> System.out.println("Tile x: " + e.getXCord() + "\tTile y: " + e.getYCord()));

        gameBoard.addEventHandler(GameEndedEvent.GAME_ENDED_EVENT_TYPE,
                e -> System.out.println("Game ended: " + Arrays.toString(e.getWinningTileType())));
        rootStackPane.getChildren().add(gameBoard);

        // Create an instance with the required parameters
        ReversiGameInstance reversiGameInstance = new ReversiGameInstance(gameBoard,
                new ReversiTurnEntityPlayer(), new ReversiAi());

        // Start the game!
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
        pauseTransition.setOnFinished(e -> {
            reversiGameInstance.startGame();
        });
        pauseTransition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
