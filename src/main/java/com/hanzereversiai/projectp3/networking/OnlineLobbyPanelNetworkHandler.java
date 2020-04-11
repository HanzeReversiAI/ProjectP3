package com.hanzereversiai.projectp3.networking;

import com.hanzereversiai.projectp3.ui.OnlineLobbyPanelController;
import com.hanzereversiai.projectp3.ui.OnlineLobbyPlayerEntryController;
import com.hanzereversiai.projectp3.ui.OnlineLobbySubscriptionEntry;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles network commands for the OnlineLobbyPanel
 */
public class OnlineLobbyPanelNetworkHandler {
    private final static Pattern listPattern = Pattern.compile("\"(.*?)\"[,\\]]"); // REGEX Pattern

    private OnlineLobbyPanelController onlineLobbyPanelController;
    private ArrayList<String> lobbyPanelChallenges;

    /**
     * Constructor
     * @param network A Network instance
     */
    public OnlineLobbyPanelNetworkHandler(Network network) {
        lobbyPanelChallenges = new ArrayList<>();
        subscribeAll(network.getDelegateInputListener());
    }

    /**
     * Handle updating the playerList in the OnlineLobbyPanel UI
     * @param input SVR PLAYERLIST ["<player>", ...]
     */
    public void handlePlayerList(String input) {
        if (onlineLobbyPanelController != null) {
            // Avoiding a IllegalStateException by telling the program to update the UI in the JavaFX thread
            Platform.runLater(
                    // Remove the existing player list (To prevent duplicates)
                    () -> onlineLobbyPanelController.playerList.getChildren().clear()
            );

            Matcher matcher = listPattern.matcher(input);
            String username = NetworkSingleton.getNetworkInstance().getUsername();

            while(matcher.find()) {
                try {
                    // Add each player to the UI (Excluding us)
                    if (! matcher.group(1).equals(username)) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + "online-lobby-player-entry" + ".fxml"));
                        Parent player = loader.load();
                        OnlineLobbyPlayerEntryController controller = loader.getController();
                        BorderPane borderPane = (BorderPane) player;

                        String opponent = matcher.group(1);

                        for (String item : lobbyPanelChallenges) {
                            MenuItem menuItem = new MenuItem(item);
                            menuItem.setOnAction(e -> controller.onChallengeAcceptMenuButtonActivated(e, item, opponent));
                            controller.addChallengeOptions(menuItem);
                        }

                        Platform.runLater(
                                () -> onlineLobbyPanelController.playerList.getChildren().add(borderPane)
                        );

                        controller.setPlayerName(opponent);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else throw new IllegalStateException();

    }

    /**
     * Handle updating the possible games list in the OnlineLobbyPanel UI
     *
     * These games can then be subscribed to
     * @param input SVR GAMELIST ["<game type>", ...]
     */
    public void handleGameList(String input) {
        if (onlineLobbyPanelController != null) {

            lobbyPanelChallenges = new ArrayList<>();
            Matcher matcher = listPattern.matcher(input);

            while(matcher.find()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + "online-lobby-subscription-entry" + ".fxml"));
                    Parent subscription = loader.load();
                    OnlineLobbySubscriptionEntry controller = loader.getController();
                    BorderPane borderPane = (BorderPane) subscription;

                    Platform.runLater(
                            () -> onlineLobbyPanelController.subscriptionList.getChildren().add(borderPane)
                    );

                    controller.setGameName(matcher.group(1));
                    lobbyPanelChallenges.add(matcher.group(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else throw new IllegalStateException();
    }

    /**
     * Subscribe to inputs from the network
     * @param delegateInputListener DelegateInputListener instance to subscribe to.
     */
    private void subscribeAll(DelegateInputListener delegateInputListener) {
        delegateInputListener.SUBSCRIBE_PLAYERLIST(this::handlePlayerList, this.hashCode());
        delegateInputListener.SUBSCRIBE_GAMELIST(this::handleGameList, this.hashCode());
    }
    /**
     * Unsubscribe to inputs from the network
     * @param delegateInputListener DelegateInputListener instance to unsubscribe from.
     */
    private void unsubscribeAll(DelegateInputListener delegateInputListener) {
        delegateInputListener.UNSUBSCRIBE_PLAYERLIST(this.hashCode());
        delegateInputListener.UNSUBSCRIBE_GAMELIST(this.hashCode());
    }

    /**
     * The OnlineLobbyPanel that should be updated by the OnlineLobbyPanelNetworkHandler
     * @param onlineLobbyPanelController OnlineLobbyPanel instance
     */
    public void setOnlineLobbyPanelController(OnlineLobbyPanelController onlineLobbyPanelController) {
        this.onlineLobbyPanelController = onlineLobbyPanelController;
    }
}
