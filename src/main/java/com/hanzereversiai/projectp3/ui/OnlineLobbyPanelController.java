package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.GameFactory;
import com.hanzereversiai.projectp3.networking.Command;
import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import com.thowv.javafxgridgameboard.AbstractGameInstance;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnlineLobbyPanelController {

    private final static Pattern listPattern = Pattern.compile("\"(.*?)\"[,\\]]");
    private final static Pattern gameTypePattern = Pattern.compile("GAMETYPE: \"(.*?)\"");
    private final static Pattern opponentPattern = Pattern.compile("OPPONENT: \"(.*?)\"");
    private final static Pattern startingPlayerPattern = Pattern.compile("PLAYERTOMOVE: \"(.*?)\"");

    private ArrayList<String> challenges;

    public SplitPane lobbyPanelRoot;
    public VBox playerList;
    public VBox subscriptionList;
    public Button refreshPlayerListButton;

    @FXML
    public void initialize() {
        challenges = new ArrayList<>();
        playerList.getChildren().clear();
        subscriptionList.getChildren().clear();

        Network network = NetworkSingleton.getNetworkInstance();
        network.getDelegateInputListener().SUBSCRIBE_PLAYERLIST(this::handleInputPlayers);
        network.getDelegateInputListener().SUBSCRIBE_GAMELIST(this::handleInputSubscriptions);
        network.getDelegateInputListener().SUBSCRIBE_MATCH(this::handleMatch);

        network.SendCommand(Command.GET_GAMELIST);
        network.SendCommand(Command.GET_PLAYERLIST);
    }

    public void handleInputPlayers(String input) {
        // Avoiding a IllegalStateException by telling the program to update the UI in the JavaFX thread
        Platform.runLater(
                () -> playerList.getChildren().clear()
        );

        Matcher matcher = listPattern.matcher(input);
        String username = NetworkSingleton.getNetworkInstance().getUsername();

        while(matcher.find()) {
            try {
                if (! matcher.group(1).equals(username)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + "online-lobby-player-entry" + ".fxml"));
                    Parent player = loader.load();
                    OnlineLobbyPlayerEntryController controller = loader.getController();
                    BorderPane borderPane = (BorderPane) player;

                    String opponent = matcher.group(1);

                    for (String item : challenges) {
                        MenuItem menuItem = new MenuItem(item);
                        menuItem.setOnAction(e -> controller.onChallengeAcceptMenuButtonActivated(e, item, opponent));
                        controller.addChallengeOptions(menuItem);
                    }

                    Platform.runLater(
                            () -> playerList.getChildren().add(borderPane)
                    );

                    controller.setPlayerName(opponent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleInputSubscriptions(String input) {
        /// Avoiding a IllegalStateException by telling the program to update the UI in the JavaFX thread
        Platform.runLater(
                () -> subscriptionList.getChildren().clear()
        );

        challenges = new ArrayList<>();
        Matcher matcher = listPattern.matcher(input);

        while(matcher.find()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + "online-lobby-subscription-entry" + ".fxml"));
                Parent subscription = loader.load();
                OnlineLobbySubscriptionEntry controller = loader.getController();
                BorderPane borderPane = (BorderPane) subscription;

                Platform.runLater(
                        () -> subscriptionList.getChildren().add(borderPane)
                );

                controller.setGameName(matcher.group(1));
                challenges.add(matcher.group(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleMatch(String input) {
        BoardGameOption boardGameOption;
        String opponentName;

        // Game type matching
        Matcher matcher = gameTypePattern.matcher(input);

        if (!matcher.find())
            return;

        if (matcher.group(1).equals("Tic-tac-toe"))
            boardGameOption = new BoardGameOption("Tic-tac-toe", "tttoe");
        else if (matcher.group(1).equals("Reversi"))
            boardGameOption = new BoardGameOption("Reversi", "reversi");
        else
            throw new IllegalArgumentException("Unsupported Game: " + matcher.group(1));

        // Opponent matching
        matcher = opponentPattern.matcher(input);

        if (!matcher.find())
            return;

        opponentName = matcher.group(1);

        // Stating player matching
        matcher = startingPlayerPattern.matcher(input);

        if (!matcher.find())
            return;

        String playerOne, playerTwo;

        if (matcher.group(1).equals(NetworkSingleton.getNetworkInstance().getUsername())) {
            playerOne = "Player";
            playerTwo = "Network-" + opponentName;
        }
        else {
            playerOne = "Network-" + opponentName;
            playerTwo = "Player";
        }

        Platform.runLater(() -> {
            FXMLLoader gamePanelLoader = UIHelper.switchScene(lobbyPanelRoot.getScene(), "game-panel");

            AbstractGameInstance gameInstance = GameFactory.buildNetworkedGameInstance(
                    boardGameOption, playerOne, playerTwo);

            // Set the game board in the panel
            GamePanelController gamePanelController = gamePanelLoader != null ? gamePanelLoader.getController() : null;
            if (gamePanelController != null)
                ((GamePanelController)gamePanelLoader.getController()).setGameInstance(gameInstance);
        });
    }

    public void onRefreshPlayerListButtonActivated(ActionEvent actionEvent) {
        NetworkSingleton.getNetworkInstance().SendCommand(Command.GET_PLAYERLIST);
    }
}
