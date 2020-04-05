package com.hanzereversiai.projectp3;

import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.ui.BoardGameOption;
import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import com.hanzereversiai.projectp3.ui.LobbyPanel;
import com.hanzereversiai.projectp3.ui.events.ConnectionRaisedEvent;
import com.hanzereversiai.projectp3.ui.events.GameStartedEvent;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
    private Network network;
    private String username;

    private StackPane rootStackPane;
    private ConnectionPanel connectionPanel;
    private LobbyPanel lobbyPanel;

    @Override
    public void start(Stage stage) {
        rootStackPane = new StackPane();

        connectionPanel = new ConnectionPanel();
        connectionPanel.addEventHandler(ConnectionRaisedEvent.CONNECTION_RAISED_EVENT_EVENT_TYPE,
                e -> onConnectionRaised(e.getNetwork(), e.getUsername()));
        switchRootStackPaneContent(connectionPanel);

        stage.setScene(new Scene(rootStackPane));
        stage.setTitle("Reversi AI Project");
        stage.show();
    }

    public void onConnectionRaised(Network network, String username) {
        this.network = network;
        this.username = username;

        lobbyPanel = new LobbyPanel(username);
        lobbyPanel.addEventHandler(GameStartedEvent.GAME_STARTED_EVENT_EVENT_TYPE,
                e -> onGameStarted(e.getBoardGameOption(),
                        e.getPlayerOneOption(), e.getPlayerTwoOption()));

        switchRootStackPaneContent(lobbyPanel);

        // Temporary fix to prevent nullpointer
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
        pauseTransition.setOnFinished(e -> {
            if (network == null)
                lobbyPanel.createOfflineContent();
            else
                lobbyPanel.createOnlineContent();
        });
        pauseTransition.play();
    }

    public void onGameStarted(BoardGameOption boardGameOption, String playerOneOption, String playerTwoOption) {
        AbstractGameInstance gameInstance = GameFactory.buildGameInstance(boardGameOption,
                playerOneOption, playerTwoOption);
        switchRootStackPaneContent(gameInstance.getGameBoard());

        // Temporary fix to prevent nullpointer
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
        pauseTransition.setOnFinished(e -> gameInstance.startGame());
        pauseTransition.play();
    }

    private void switchRootStackPaneContent(Control control) {
        if (rootStackPane.getChildren().size() != 0)
            rootStackPane.getChildren().remove(0);

        rootStackPane.getChildren().add(control);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
