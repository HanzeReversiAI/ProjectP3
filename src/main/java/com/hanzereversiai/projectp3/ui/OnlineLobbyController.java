package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.Command;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnlineLobbyController {

    private final static Pattern listPattern = Pattern.compile("\"(.*?)\"[,\\]]");
    private ArrayList<String> challenges;

    public VBox playerList;
    public VBox subscriptionList;
    public Button refreshPlayerListButton;

    @FXML
    public void initialize() {
        challenges = new ArrayList<>();
        playerList.getChildren().remove(0);
        subscriptionList.getChildren().remove(0);
        NetworkSingleton.getNetworkInstance().getDelegateInputListener().SUBSCRIBE_PLAYERLIST(this::handleInputPlayers);
        NetworkSingleton.getNetworkInstance().getDelegateInputListener().SUBSCRIBE_GAMELIST(this::handleInputSubscriptions);
        NetworkSingleton.getNetworkInstance().SendCommand(Command.GET_GAMELIST);
        NetworkSingleton.getNetworkInstance().SendCommand(Command.GET_PLAYERLIST);
    }

    public void handleInputPlayers(String input) {
        // Avoiding a IllegalStateException by updating the UI outside of the JavaFX thread.
        Platform.runLater(
                () -> playerList.getChildren().removeAll()
        );
        Matcher m = listPattern.matcher(input);
        String username = NetworkSingleton.getNetworkInstance().getUsername();
        while(m.find()) {
            try {
                if (! m.group(1).equals(username)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + "online-lobby-player-entry" + ".fxml"));
                    Parent player = loader.load();
                    OnlineLobbyPlayerEntryController controller = loader.getController();
                    BorderPane borderPane = (BorderPane) player;

                    String opponent = m.group(1);

                    for (String item : challenges) {
                        MenuItem menuItem = new MenuItem(item);
                        menuItem.setOnAction(a-> controller.onChallengeAcceptMenuButtonActivated(a, item, opponent));
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
        // Avoiding a IllegalStateException by updating the UI outside of the JavaFX thread.
        Platform.runLater(
                () -> subscriptionList.getChildren().retainAll()
        );
        challenges = new ArrayList<>();
        Matcher m = listPattern.matcher(input);
        while(m.find()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + "online-lobby-subscription-entry" + ".fxml"));
                Parent subscription = loader.load();
                OnlineLobbySubscriptionEntry controller = loader.getController();
                BorderPane borderPane = (BorderPane) subscription;

                Platform.runLater(
                        () -> subscriptionList.getChildren().add(borderPane)
                );

                controller.setGameName(m.group(1));
                challenges.add(m.group(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onRefreshPlayerListButton(ActionEvent actionEvent) {
        NetworkSingleton.getNetworkInstance().SendCommand(Command.GET_PLAYERLIST);
    }
}
