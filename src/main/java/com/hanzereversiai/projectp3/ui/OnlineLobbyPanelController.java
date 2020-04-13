package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.Command;
import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * UI Controller for the lobby panel
 *
 * @author Thomas, Mike
 */
public class OnlineLobbyPanelController {

    public SplitPane lobbyPanelRoot;
    public VBox playerList;
    public VBox subscriptionList;
    public Button refreshPlayerListButton;
    public TextField aiDepthTextField;
    public Label aiDepthErrorLabel;

    /**
     * Clean ui and populate it with network data.
     */
    @FXML
    public void initialize() {
        aiDepthErrorLabel.setText("");
        aiDepthTextField.setText(String.valueOf(NetworkSingleton.getNetworkInstance().getAiDepthAmount()));
        playerList.getChildren().clear();
        subscriptionList.getChildren().clear();

        Network network = NetworkSingleton.getNetworkInstance();

        network.getNetworkHandler().setRootUIObject(lobbyPanelRoot);
        network.getOnlineLobbyPanelNetworkHandler().setOnlineLobbyPanelController(this);

        network.sendCommand(Command.GET_GAMELIST);
        network.sendCommand(Command.GET_PLAYERLIST);
    }

    /**
     * Refresh the player list.
     * @param actionEvent The event that fired this method
     */
    public void onRefreshPlayerListButtonActivated(ActionEvent actionEvent) {
        NetworkSingleton.getNetworkInstance().sendCommand(Command.GET_PLAYERLIST);
    }

    /**
     * Set the ai depth value after validating the given input.
     * @param keyEvent The event that fired this method
     */
    public void onAIDepthTextFieldKeyReleased(KeyEvent keyEvent) {
        aiDepthErrorLabel.setText("");

        if (aiDepthTextField.getText().isEmpty())
            return;

        try {
            int amount = Integer.parseInt(aiDepthTextField.getText());

            if (amount < 0) {
                aiDepthErrorLabel.setText("Value must be positive!");
                return;
            }

            NetworkSingleton.getNetworkInstance().setAiDepthAmount(amount);
        }
        catch (NumberFormatException e) {
            aiDepthErrorLabel.setText("Value must be a number!");
        }
    }
}
